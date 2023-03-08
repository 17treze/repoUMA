package it.tndigitale.a4g.zootecnia.business.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import it.tndigitale.a4g.framework.event.store.EventStoreService;
import it.tndigitale.a4g.framework.extension.DefaultNameExtension;
import it.tndigitale.a4g.zootecnia.business.service.ZootecniaService;

@Service
public class SincronizzazioneAgsSchedaValidazioneListener {
	@Autowired private ZootecniaService zootecniaService;
	@Autowired private EventStoreService eventStoreService;
	
	@Async(DefaultNameExtension.DEFAULT_SECURITY_THREAD_ASYNC)
	@TransactionalEventListener
	public void handleEvent(EndValidazioneFascicoloEvent event) {
		String cuaa = event.getData().getCuaa();
		Integer idValidazione = event.getData().getIdValidazione();
		try {
			zootecniaService.sincronizzaAgs(cuaa, idValidazione);		
		} catch (Exception ex) {
			eventStoreService.triggerRetry(ex, event);
		}
	}
}
