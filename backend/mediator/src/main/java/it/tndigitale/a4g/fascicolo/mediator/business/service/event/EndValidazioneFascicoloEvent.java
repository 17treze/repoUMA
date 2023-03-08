package it.tndigitale.a4g.fascicolo.mediator.business.service.event;

import it.tndigitale.a4g.fascicolo.mediator.dto.SchedaValidazioneFascicoloDto;
import it.tndigitale.a4g.framework.event.AbstractWrapperEvent;

public class EndValidazioneFascicoloEvent extends AbstractWrapperEvent<SchedaValidazioneFascicoloDto> {
	
	public EndValidazioneFascicoloEvent() {
		super();
	}

	public EndValidazioneFascicoloEvent(final SchedaValidazioneFascicoloDto scheda) {
		super();
		setData(scheda);
	}
}
