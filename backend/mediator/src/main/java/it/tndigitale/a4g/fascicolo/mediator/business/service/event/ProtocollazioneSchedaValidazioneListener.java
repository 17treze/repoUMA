package it.tndigitale.a4g.fascicolo.mediator.business.service.event;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import it.tndigitale.a4g.fascicolo.mediator.business.service.client.AnagraficaPrivateClient;
import it.tndigitale.a4g.fascicolo.mediator.dto.SchedaValidazioneFascicoloDto;
import it.tndigitale.a4g.framework.event.store.EventStoreService;
import it.tndigitale.a4g.framework.extension.DefaultNameExtension;

@Component
class ProtocollazioneSchedaValidazioneListener {
	private static final Logger logger = LoggerFactory.getLogger(ProtocollazioneSchedaValidazioneListener.class);

	@Autowired private AnagraficaPrivateClient anagraficaPrivateClient;
	@Autowired private EventStoreService eventStoreService;

	@Async(DefaultNameExtension.DEFAULT_SECURITY_THREAD_ASYNC)
	@TransactionalEventListener
	public void handleEvent(EndValidazioneFascicoloEvent event) {
		SchedaValidazioneFascicoloDto scheda = event.getData();
		logger.debug("Mi accingo a protocollare la scheda per il cuaa {}", scheda.getCodiceFiscale());
		File schedaValidazioneFile = null;
        try {
        	schedaValidazioneFile = File.createTempFile("schedaValidazione", ".pdf");
            FileUtils.writeByteArrayToFile(schedaValidazioneFile, scheda.getReport().getByteArray());
            
        	anagraficaPrivateClient.protocollaSchedaValidazioneUsingPOST(scheda.getCodiceFiscale(), schedaValidazioneFile, new ArrayList<File>(), scheda.getNextIdValidazione(), scheda.getTipoDetenzione());
        } catch (Exception ex) {
        	eventStoreService.triggerRetry(ex, event);
        } finally {
        	if (schedaValidazioneFile != null) {
        		try {
					Files.delete(schedaValidazioneFile.toPath());
				} catch (IOException e) {
					eventStoreService.triggerRetry(e, event);
				}	
        	}
        }
	}
}
