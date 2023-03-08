package it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.scheduler;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.FascicoloDao;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.StatoFascicoloEnum;

@Component
public class FascicoliDormientiScheduler extends AbilitazioneUtenzaTecnica {

	private static final Logger logger = LoggerFactory.getLogger(FascicoliDormientiScheduler.class);
	@Autowired
	private FascicoloDao fascicoloDao;

	@Scheduled(cron = "${it.tndigit.event.cron.FASCICOLI_DORMIENTI}")
	public void schedulerProcess() {
		configuraUtenzaTecnica();
		logger.debug("start job fascicoli dormienti");
		LocalDate minDataValidazione = LocalDate.now().minus(1, ChronoUnit.YEARS);
		List<FascicoloModel> fascicoliDormienti = fascicoloDao.findTheSleepers(minDataValidazione);
		if (CollectionUtils.isEmpty(fascicoliDormienti)) {
			logger.info("Nessun fascicolo da portare nello stato DORMIENTE");
			return;
		}

		fascicoliDormienti.forEach(fascicolo -> fascicoloDao.save(fascicolo.setStato(StatoFascicoloEnum.DORMIENTE)
				.setDataModifica(LocalDate.now())
				.setUtenteModifica("automazioneCambioStato")));
		clearAutenticazione();
		logger.info("Fine job fascicoli dormienti. Operazione eseguita su {} record", fascicoliDormienti.size());
	}
}
