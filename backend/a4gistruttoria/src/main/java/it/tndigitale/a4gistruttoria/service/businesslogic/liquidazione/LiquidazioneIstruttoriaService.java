package it.tndigitale.a4gistruttoria.service.businesslogic.liquidazione;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4gistruttoria.dto.IstruttoriaDomandaUnicaFilter;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.ElencoLiquidazioneModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.specification.IstruttoriaSpecificationBuilder;
import it.tndigitale.a4gistruttoria.service.businesslogic.CambioStatoIstruttoria;
import it.tndigitale.a4gistruttoria.service.businesslogic.ElaborazioneIstruttoria;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElaborazioneIstruttoriaException;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElencoLiquidazioneException;

@Service
public class LiquidazioneIstruttoriaService extends CambioStatoIstruttoria implements ElaborazioneIstruttoria {

	private static final Logger logger = LoggerFactory.getLogger(LiquidazioneIstruttoriaService.class);

	@Autowired
	private CaricatoreDatiLiquidazioneIstruttoriaFactory caricatoreFactory;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ElaborazioneIstruttoriaException.class)
	public void elabora(Long idIstruttoria) throws ElaborazioneIstruttoriaException {
		cambiaStato(idIstruttoria, StatoIstruttoria.PAGAMENTO_AUTORIZZATO);
	}
	
	/**
	 * Metodo per la generazione della riga del tracciato per l'istruttoria
	 * 
	 * Senza la transazione va in LazyInitialization (no Session). Serve
	 * gestione timeout. Se faccio REQUIRES_NEW non vede elenco creato 
	 * al passo precedente
	 * 
	 * @param idIstruttoria
	 * @param annoCampagna
	 * @param sostegno
	 * @param elenco
	 * @param counter
	 * @return
	 * @throws ElencoLiquidazioneException
	 */
	public String generaRigaTracciato(Long idIstruttoria, Integer annoCampagna, Sostegno sostegno, ElencoLiquidazioneModel elenco, int counter) throws ElencoLiquidazioneException {
		IstruttoriaDao istruttoriaDao = getIstruttoriaDao();
		IstruttoriaModel istruttoria = 
				istruttoriaDao.findById(idIstruttoria)
				.orElseThrow(() -> new ElencoLiquidazioneException("Istruttoria " + idIstruttoria + " non trovata"));
		String rigaTracciato = null;
		if (isIstruttoriaLiquidabile(istruttoria, annoCampagna, sostegno)) {
			String nomeCaricatore = 
					CaricatoreDatiLiquidazioneIstruttoria.PREFISSO_CARICATORE_DATI_QUALIFIER + istruttoria.getSostegno().name();
			CaricatoreDatiLiquidazioneIstruttoria caricatoreDati = 
					caricatoreFactory.getCaricatore(nomeCaricatore);

			DatiLiquidazioneIstruttoria datiIstruttoria = 
					caricatoreDati.creaRecordElenco(istruttoria);
			datiIstruttoria.setProgressivo(counter);
			datiIstruttoria.setIdentificativoElenco(elenco.getId());
			rigaTracciato =
					SerializzatoreDatiLiquidazioneIstruttoria.trasforma(datiIstruttoria);
			if (rigaTracciato.length() != CampoElencoLiquidazione.FILLER.getEnd() + 1) {
				logger.error("Errore generando la riga del tracciato per l'istruttoria {}: riga lunghezza non compatibile {}", istruttoria.getId(), rigaTracciato);
				throw new ElencoLiquidazioneException("Errore generando la riga del tracciato per l'istruttoria " + istruttoria.getId());
			}

			istruttoria.setElencoLiquidazione(elenco);
			istruttoriaDao.save(istruttoria);
		}
		return rigaTracciato;
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
	protected TipoIstruttoria getTipoIstruttoriaPerElenco(Long idElenco) {
		IstruttoriaDao istruttoriaDao = getIstruttoriaDao();
		return istruttoriaDao.findByElencoLiquidazioneId(idElenco).stream().findAny().map(i -> i.getTipologia()).orElse(null);
	}
	
	protected boolean isIstruttoriaLiquidabile(IstruttoriaModel istruttoria, Integer annoCampagna, Sostegno sostegno) {
		return Optional.of(istruttoria)
				// controllo che sia una istruttoria che riferisce l'anno campagna dell'elenco
				.filter(i -> annoCampagna.equals(i.getDomandaUnicaModel().getCampagna()))
				// controllo che sia del sostegno dell'elenco
				.filter(i -> sostegno.equals(i.getSostegno()))
				// controllo che sia dello stato dell'elenco
				.filter(i -> StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK.equals(i.getStato()))
				.map(i -> Boolean.TRUE)
				.orElse(false);
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
	public Set<Long> caricaIdentificativiIstruttorieLiquidabili(Integer campagna, Sostegno sostegno) {
		IstruttoriaDomandaUnicaFilter filter = new IstruttoriaDomandaUnicaFilter();
		filter.setCampagna(campagna);
		filter.setSostegno(sostegno);
		filter.setStato(StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK);
		
		IstruttoriaDao istruttoriaDao = getIstruttoriaDao();
		// cerco tutte le istruttorie potenzialmente liquidabili, poi vanno filtrate con quelle in input
		List<IstruttoriaModel> istruttorieLiquidabili = istruttoriaDao.findAll(IstruttoriaSpecificationBuilder.getFilter(filter));
		return istruttorieLiquidabili.stream().filter(i -> !i.getBloccataBool()).map(IstruttoriaModel::getId).collect(Collectors.toSet());
		
	}

}
