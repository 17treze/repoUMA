package it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaFisicaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.FascicoloDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.ChiusuraFascicoloException;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.MovimentazioneFascicoloService;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.StatoFascicoloEnum;
import it.tndigitale.a4g.framework.time.Clock;

@Component
public class FascicoliInChiusuraScheduler extends AbilitazioneUtenzaTecnica {

	private static final Logger logger = LoggerFactory.getLogger(FascicoliInChiusuraScheduler.class);

	@Autowired
	private FascicoloDao fascicoloDao;
	@Autowired
	private Clock clock;
	@Autowired
	private MovimentazioneFascicoloService movimentazioneFascicoloService;

	@Scheduled(cron = "${it.tndigit.event.cron.FASCICOLI_IN_CHIUSURA}")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void schedulerProcess() {
		configuraUtenzaTecnica();
		logger.info("start job fascicoli in chiusura");
		fascicoloDao.findByStato(StatoFascicoloEnum.IN_CHIUSURA).stream()
		.filter(fascicolo -> fascicolo.getPersona() instanceof PersonaFisicaModel)
		.filter(fascicolo -> {
			var personaFisica = (PersonaFisicaModel) fascicolo.getPersona();
			return clock.today().isAfter(personaFisica.getDataMorte().plusYears(1));
		})
		.forEach(fascicolo -> {
			try {
				logger.info("Avvio procedura chiusura per il fascicolo {}" , fascicolo.getCuaa());
				movimentazioneFascicoloService.chiudi(fascicolo);
			} catch (ChiusuraFascicoloException e) {
				logger.error("Errore esecuzione job chiusura per il fascicolo {} , {}", fascicolo.getCuaa(), e.getMessage());
			}
		});
		logger.info("Fine job fascicoli in chiusura.");
	}

}
