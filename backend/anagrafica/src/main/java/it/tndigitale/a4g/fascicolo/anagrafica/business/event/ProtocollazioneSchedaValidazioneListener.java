//package it.tndigitale.a4g.fascicolo.anagrafica.business.event;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.event.TransactionalEventListener;
//
//import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.ProtocollazioneSchedaValidazioneService;
//import it.tndigitale.a4g.fascicolo.anagrafica.dto.SchedaValidazioneFascicoloDto;
//import it.tndigitale.a4g.framework.event.store.EventStoreService;
//import it.tndigitale.a4g.framework.ext.validazione.fascicolo.FascicoloNonValidabileException;
//import it.tndigitale.a4g.framework.extension.DefaultNameExtension;
//
//@Component
//class ProtocollazioneSchedaValidazioneListener {
//	private static final Logger logger = LoggerFactory.getLogger(ProtocollazioneSchedaValidazioneListener.class);
//
//	@Autowired private ProtocollazioneSchedaValidazioneService protocollazioneSchedaService;
//	@Autowired private EventStoreService eventStoreService;
//
//	@Async(DefaultNameExtension.DEFAULT_SECURITY_THREAD_ASYNC)
//	@TransactionalEventListener
//	public void handleEvent(EndValidazioneFascicoloEvent event) {
//		SchedaValidazioneFascicoloDto scheda = event.getData();
//		logger.debug("Mi accingo a protocollare la scheda per il cuaa {}", scheda.getCodiceFiscale());
//		try {
//			protocollazioneSchedaService.protocolla(scheda);
//		} catch (FascicoloNonValidabileException ex) {
//			eventStoreService.triggerRetry(ex, event);
//		}
//	}
//}
