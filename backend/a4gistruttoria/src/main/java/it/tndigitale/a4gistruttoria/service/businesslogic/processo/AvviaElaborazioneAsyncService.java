package it.tndigitale.a4gistruttoria.service.businesslogic.processo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import it.tndigitale.a4gistruttoria.dto.Processo;

@Service
public class AvviaElaborazioneAsyncService {

	private static final Logger logger = LoggerFactory.getLogger(AvviaElaborazioneAsyncService.class);

	@Autowired
	private ElaborazioneProcessoAsyncStrategyFactory elaborazioneIstruttoriaFactory;


	@Async("threadGestioneProcessi")
	public void avviaProcesso(Processo processo) {
		//check su tipoProcesso e tipoIstruttoria
		ElaboraProcesso<Processo, ?> processoStrategyInput = elaborazioneIstruttoriaFactory.getElaborazioneAsync(processo.getTipoProcesso().name());
		logger.debug("Avvio la class " + processoStrategyInput.getClass());
		processoStrategyInput.avvia(processo);
	}
}
