package it.tndigitale.a4g.proxy.bdn.repository.schedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import it.tndigitale.a4g.proxy.bdn.service.BDNSyncService;
import it.tndigitale.a4g.proxy.bdn.service.manager.ConsistenzaAlPascoloPAC2015Manager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.proxy.bdn.config.AbilitazioneUtenzaTecnica;
import it.tndigitale.a4g.proxy.bdn.repository.interfaces.StatoSincronizzazioneBdnDAO;

/**
 * Classe che gestisce la lettura schedulata dei dati da BDN
 *
 * @author A2AC0147
 *
 */
@Component
public class BDNSyncSchedule {

	private Logger logger = LoggerFactory.getLogger(BDNSyncSchedule.class);

	@Autowired
    private BDNSyncService syncService;
	@Autowired
	private StatoSincronizzazioneBdnDAO statoSincronizzazioneDao;
	@Autowired
    private ConsistenzaAlPascoloPAC2015Manager consistenzaManager;
	@Autowired
	private AbilitazioneUtenzaTecnica abilitazioneUtenzaTecnica;

	// Il metodo annotato come Scheduled deve obbligatoriamente essere void e non
	// può ricevere parametri.
	@Scheduled(cron = "${cron.expression}")
	public void scheduleImportDatiBDN() {
		List<Integer> anni = new ArrayList<>();
		anni.add(Calendar.getInstance().get(Calendar.YEAR));
		anni.add(Calendar.getInstance().get(Calendar.YEAR) - 1);
		abilitazioneUtenzaTecnica.configuraUtenzaTecnica();
	 	try {
			logger.debug("Start import dati BDN");
			for (Integer annoCampagna : anni) {
				logger.info("Richiamo elaborazione per l'anno {}", annoCampagna);
				syncService.sincronizzaCacheDatiDomandaUnicaPerAnno(annoCampagna);
				logger.info("Finita elaborazione per anno {}", annoCampagna);
			}
		} catch (Exception e) {
			logger.error("Error import dati BDN", e);
		}
		logger.debug("End import dati BDN");
	}

}
