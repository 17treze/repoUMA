package it.tndigitale.a4gistruttoria.service.businesslogic.avvio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.dao.StatoLavorazioneSostegnoDao;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElaborazioneIstruttoriaException;

/**
 * Avvia una nuova Istruttoria (dato il riferimento alla domanda unica, la tipologia
 * e il Sostegno). La inizializza in stato RICHIESTO.
 * 
 * @author IT417
 *
 */
@Service
public class AvvioIstruttoriaService {
	
	private static final Logger logger = LoggerFactory.getLogger(AvvioIstruttoriaService.class);
	
	@Autowired
	private DomandaUnicaDao domandaDao;
	
	@Autowired
	private StatoLavorazioneSostegnoDao statoDao;
	
	@Autowired
	private IstruttoriaDao istruttoriaDao;
	
	@Autowired
	private VerificatoreIstruttoriaFactory verificatoreIstruttoriaFactory;
	@Autowired
	private InizializzaDatiIstruttoreFactory inizializzaDatiIstruttoreFactory;
	
	public Long avvioIstruttoria(Long idDomanda, TipoIstruttoria tipologia, Sostegno sostegno) throws ElaborazioneIstruttoriaException {
		logger.debug("check condizioni per avvio istruttoria");
		if (!isIstruttoriaAvviabile(idDomanda, tipologia, sostegno)) {
			logger.error("Nuova istruttoria per domanda {}, tipologia {}, sostegno {} non avviabile", idDomanda, tipologia, sostegno);
			throw new ElaborazioneIstruttoriaException(
					"Nuova istruttoria per domanda ".concat(idDomanda.toString()).concat(" tipologia ").concat(tipologia.name())
							.concat(" sostegno ").concat(sostegno.name()).concat(" non avviabile"));
		}
		logger.info("Creazione nuova istruttoria per domanda {}, tipologia {}, sostegno {}", idDomanda, tipologia, sostegno);
		// inizializzo istruttoria
		DomandaUnicaModel domanda = domandaDao.findById(idDomanda).get();
		IstruttoriaModel istruttoria = creaIstruttoria(domanda, tipologia, sostegno);
		
		// aggancio dati istruttori
		inizializzaDatiIstruttore(istruttoria, sostegno);
		
		return istruttoria.getId();
	}
	
	protected IstruttoriaModel creaIstruttoria(DomandaUnicaModel domanda, TipoIstruttoria tipologia, Sostegno sostegno) {
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setSostegno(sostegno);
		istruttoria.setTipologia(tipologia);
		istruttoria.setA4gdStatoLavSostegno(statoDao.findByIdentificativo(StatoIstruttoria.RICHIESTO.name()).get());

		// salvo istruttoria
		istruttoria = istruttoriaDao.save(istruttoria);

		return istruttoria;
		
	}
	
	/**
	 * recupera, tramite FactoryBean, il verificatore in base al TipoIstruttoria
	 */
	public boolean isIstruttoriaAvviabile(Long idDomanda, TipoIstruttoria tipologia, Sostegno sostegno) {
		// AGGANCIO IL VERIFICATORE IN FUNZIONE DELLA TIPOLOGIA
		VerificatoreIstruttoria verificatoreLoader =
				verificatoreIstruttoriaFactory.getVerificatoreByTipoIstruttoria(VerificatoreIstruttoria.getNomeQualificatore(tipologia));
		return verificatoreLoader.isIstruttoriaAvviabile(idDomanda, tipologia, sostegno);
	}
	
	/**
	 * recupera, tramite FactoryBean, l'inizializzatore in base al Sostegno
	 */
	protected void inizializzaDatiIstruttore(IstruttoriaModel istruttoria, Sostegno sostegno) {
		InizializzaDatiIstruttore inizializzatoreLoader =
				inizializzaDatiIstruttoreFactory.getInizializzatoreBySostegno(InizializzaDatiIstruttore.getNomeQualificatore(sostegno));
		inizializzatoreLoader.inizializzaDati(istruttoria);
	}
}
