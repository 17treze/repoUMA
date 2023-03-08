package it.tndigitale.a4g.proxy.ags.bdn.business.service;

import it.tndigitale.a4g.proxy.ags.bdn.business.persistence.repository.DomandaPsrDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SincronizzazioneAllevamentiService {
	private static final Logger logger = LoggerFactory.getLogger(SincronizzazioneAllevamentiService.class);

	@Autowired
	private DomandaPsrDao domandaPsrDao;

	@Autowired
	private SincronizzazioneAllevamentoComponent sincronizzazioneAllevamentoComponent;

	@Async
	public void sincronizzaAllevamenti(LocalDate dataRiferimento) {
		logger.info("attivo sincronizzazione per {}", dataRiferimento);

		List<String> listOfCuaa = domandaPsrDao.getDomandePresentatoPsrInCampagna(dataRiferimento.getYear());

		if (listOfCuaa == null || listOfCuaa.isEmpty()) {
			logger.info("Nessun cuaa da sincronizzare per la data di riferimento {}", dataRiferimento);
			return;
		}

		logger.info("Inizio la sincronizzazione per #{} cuaa ", listOfCuaa.size());
		for (String cuaa : listOfCuaa) {
			try {
				sincronizzazioneAllevamentoComponent.sincronizzaSingoloAllevamento(cuaa, dataRiferimento);
			} catch (Throwable e) {
				logger.warn("Non sono riuscito a sincronizzare il cuaa {}", cuaa);
			}
		}

		logger.info("terminata la sincronizzazione per {} e #{} cuaa", dataRiferimento, listOfCuaa.size());
	}

	public Boolean sincronizzaSingoloAllevamento(String cuaa, LocalDate dataRiferimento) {
		logger.debug("attivo sincronizzazione per {} {}", cuaa, dataRiferimento);
		return sincronizzazioneAllevamentoComponent.sincronizzaSingoloAllevamento(cuaa, dataRiferimento);
	}
}
