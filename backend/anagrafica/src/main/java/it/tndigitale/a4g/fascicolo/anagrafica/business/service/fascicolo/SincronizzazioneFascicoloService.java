//package it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import it.tndigitale.a4g.fascicolo.anagrafica.business.event.EndValidazioneFascicoloEvent;
//import it.tndigitale.a4g.fascicolo.anagrafica.dto.SchedaValidazioneFascicoloDto;
//import it.tndigitale.a4g.framework.event.store.handler.EventBus;
//
//@Service
//public class SincronizzazioneFascicoloService {
//	@Autowired private EventBus eventBus;
//
//	@Transactional
//	public void invioEventoFineValidazione(final SchedaValidazioneFascicoloDto scheda) {
//		var event = new EndValidazioneFascicoloEvent(scheda);
//		eventBus.publishEvent(event);
//	}
//}
