package it.tndigitale.a4g.fascicolo.mediator.business.service.fascicolo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4g.fascicolo.mediator.business.service.event.ZootecniaEndValidazioneFascicoloEvent;
import it.tndigitale.a4g.framework.event.store.handler.EventBus;

@Service
public class ZootecniaService {
	@Autowired private EventBus eventBus;

	@Transactional
	public void invioEventoFineValidazione(final String cuaa, final Integer idValidazione){
		var event = new ZootecniaEndValidazioneFascicoloEvent(cuaa, idValidazione);
		eventBus.publishEvent(event);
	}
}
