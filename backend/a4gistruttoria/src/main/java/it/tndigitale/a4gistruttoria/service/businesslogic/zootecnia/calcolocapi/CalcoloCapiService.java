package it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia.calcolocapi;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4gistruttoria.dto.RichiestaAllevamDu;
import it.tndigitale.a4gistruttoria.repository.dao.EsitoCalcoloCapoDao;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.AllevamentoImpegnatoModel;
import it.tndigitale.a4gistruttoria.repository.model.DatiIstruttoreZootecniaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.CambioStatoIstruttoria;
import it.tndigitale.a4gistruttoria.service.businesslogic.ElaborazioneIstruttoria;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElaborazioneIstruttoriaException;
import it.tndigitale.a4gistruttoria.strategy.CalcoloZootecniaStrategy;

@Service
public class CalcoloCapiService extends CambioStatoIstruttoria implements ElaborazioneIstruttoria{

	private static Logger log = LoggerFactory.getLogger(CalcoloCapiService.class);
	
	@Autowired
	private IstruttoriaDao istruttoriaDao;
	@Autowired
	private CalcoloCapiHandler calcoloCapiHandler;
	@Autowired
	private ControlliLatteService controlliLatteService;
	@Autowired
	private CalcoloZootecniaStrategy calcoloZootecniaStrategy;
	@Autowired
	private CalcoloCapiRichiesti calcoloCapiRichiesti;
	
	@Autowired
	private EsitoCalcoloCapoDao esitoCalcoloCapoDao;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ElaborazioneIstruttoriaException.class, timeout = 600 )//600 secondi = 10 minuti
	public void elabora(Long idIstruttoria) throws ElaborazioneIstruttoriaException {
		try {
			log.debug("Inizio calcolo capi per istruttoria con id {} ",idIstruttoria);
			IstruttoriaModel istruttoria = 
					istruttoriaDao
						.findById(idIstruttoria)
						.orElseThrow(() ->
							new EntityNotFoundException("Istruttoria[" + idIstruttoria + "] non trovata"));
			//spostare qui la logica di AvviaControlloZootecniaConsumer
			//recupero allevamenti dalla domanda
			Set<AllevamentoImpegnatoModel> sostegniZootecnici = istruttoria.getDomandaUnicaModel().getAllevamentiImpegnati();
			//pulizia dei dati dei capi e domanda integrativa 
			log.debug("pulizia dei dati dei capi");
			sostegniZootecnici.forEach(calcoloCapiHandler.deleteElement());
			// creazione DTO degli allevamenti
			List<RichiestaAllevamDu> richiesteAllevamDu = 
					sostegniZootecnici.stream()
						.map(calcoloCapiHandler.creaDto(
								istruttoria.getDomandaUnicaModel().getCampagna(),
								Optional.ofNullable(istruttoria.getDatiIstruttoreZootecnia())
									.map(DatiIstruttoreZootecniaModel::getCuaaSubentrante)
									.orElse(null)
								))
						.collect(toList());
			//esegui controlli latte
			List<RichiestaAllevamDu> allevamentiDaControllare = controlliLatteService.eseguiControlliLatte(richiesteAllevamDu);
			// esecuzioni calcoli sostegno per gli allevamenti che hanno superato i controlli latte
			log.debug("esecuzione calcoli sostegno per gli allevamenti");
			allevamentiDaControllare
				.stream()
				.forEach(calcoloZootecniaStrategy::executeStrategy);
			double totaleUba = calcoloCapiHandler.calcoloUBA(sostegniZootecnici);
			log.info("Totale UBA per la domanda {} = {}",idIstruttoria, totaleUba);
			StatoIstruttoria statoIstruttoriaFinale = totaleUba >= 3 ? 
					StatoIstruttoria.INTEGRATO : StatoIstruttoria.NON_AMMISSIBILE;
			cambiaStato(idIstruttoria, statoIstruttoriaFinale);
			//prendo l'ultimo la transizione più recente
			TransizioneIstruttoriaModel transizioneIstruttoria = istruttoria.getTransizioni().iterator().next();
			//collego la transizione all'esito per poi essere salvata
			sostegniZootecnici
				.stream()
				.flatMap(a4gtRichiestaAllevamDu -> a4gtRichiestaAllevamDu.getEsitiCalcoloCapi().stream()) // Flattening la lista degli esiti
				.forEach(esito -> {
					esito.setTransizione(transizioneIstruttoria);
					esitoCalcoloCapoDao.save(esito);
				});
			if (StatoIstruttoria.INTEGRATO.equals(statoIstruttoriaFinale)) {
				//esegui IDU-EVO-27-05 Algoritmo Capi Richiesti Zootecnia
				log.debug("esecuzione Capi Richiesti Zootecnia");
				calcoloCapiRichiesti.eseguiCalcolo(
						sostegniZootecnici,
						istruttoria.getDomandaUnicaModel().getCampagna());
			}
			istruttoria.setDataUltimoCalcolo(LocalDateTime.now());
			istruttoriaDao.save(istruttoria);
			log.debug("Fine calcolo capi per istruttoria con id {} ",idIstruttoria);
		} catch (RuntimeException e) {
			throw new ElaborazioneIstruttoriaException(
					"Errore calcolo capi per istruttoria con id ".concat(idIstruttoria.toString()) ,e);
		}
	}
	

	
	

}
