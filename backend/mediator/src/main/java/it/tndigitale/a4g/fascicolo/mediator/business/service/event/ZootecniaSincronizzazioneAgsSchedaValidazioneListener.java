package it.tndigitale.a4g.fascicolo.mediator.business.service.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import it.tndigitale.a4g.fascicolo.mediator.business.service.client.ZootecniaPrivateClient;
import it.tndigitale.a4g.framework.event.store.EventStoreService;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.FascicoloNonValidabileException;
import it.tndigitale.a4g.framework.extension.DefaultNameExtension;

@Service
public class ZootecniaSincronizzazioneAgsSchedaValidazioneListener {
	@Autowired private ZootecniaPrivateClient zootecniaPrivateClient;
	@Autowired private EventStoreService eventStoreService;
	
	@Async(DefaultNameExtension.DEFAULT_SECURITY_THREAD_ASYNC)
	@TransactionalEventListener
	public void handleEvent(ZootecniaEndValidazioneFascicoloEvent event) {
		String cuaa = event.getData().getCuaa();
		Integer idValidazione = event.getData().getIdValidazione();
		try {
			ResponseEntity<Void> startValidazioneFascicolo = zootecniaPrivateClient.sincronizzazioneAgsWithHttpInfo(cuaa, idValidazione);
			if (!startValidazioneFascicolo.getStatusCode().equals(HttpStatus.OK)) {
				eventStoreService.triggerRetry(
	    			new FascicoloNonValidabileException("Errore in validazione zootecnia"), event);
			}
		} catch (Exception ex) {
			eventStoreService.triggerRetry(ex, event);
		}
	}
}
