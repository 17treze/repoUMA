//package it.tndigitale.a4g.fascicolo.anagrafica.business.event;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.event.TransactionalEventListener;
//
//import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloService;
//import it.tndigitale.a4g.fascicolo.anagrafica.dto.SchedaValidazioneFascicoloDto;
//import it.tndigitale.a4g.framework.event.store.EventStoreService;
//import it.tndigitale.a4g.framework.extension.DefaultNameExtension;
//
//@Component
//public class SincronizzazioneAgsSchedaValidazioneListener {
//	@Autowired private FascicoloService fascicoloService;
//	@Autowired private EventStoreService eventStoreService;
//
//	@Async(DefaultNameExtension.DEFAULT_SECURITY_THREAD_ASYNC)
//	@TransactionalEventListener
//	public void handleEvent(EndValidazioneFascicoloEvent event) {
//		SchedaValidazioneFascicoloDto data = event.getData();
//		String cuaa = data.getCodiceFiscale();
//		Integer idValidazione = data.getNextIdValidazione();
//		try {
//			fascicoloService.sincronizzaAgs(cuaa, idValidazione);
//		} catch (Exception ex) {
//			eventStoreService.triggerRetry(ex, event);
//		}
//	}
//}
