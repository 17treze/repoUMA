package it.tndigitale.a4g.fascicolo.mediator.business.service.event;

import it.tndigitale.a4g.fascicolo.mediator.dto.SchedaValidazioneFascicoloDto;
import it.tndigitale.a4g.framework.event.AbstractWrapperEvent;

public class StartValidazioneFascicoloEvent extends AbstractWrapperEvent<SchedaValidazioneFascicoloDto> {
	
	public StartValidazioneFascicoloEvent() {
		super();
	}

	public StartValidazioneFascicoloEvent(final SchedaValidazioneFascicoloDto scheda) {
		super();
		setData(scheda);
	}
}
