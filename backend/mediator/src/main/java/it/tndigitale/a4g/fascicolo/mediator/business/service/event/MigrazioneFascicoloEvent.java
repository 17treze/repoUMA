package it.tndigitale.a4g.fascicolo.mediator.business.service.event;

import it.tndigitale.a4g.fascicolo.mediator.dto.MigrazioneFascicoloDto;
import it.tndigitale.a4g.framework.event.AbstractWrapperEvent;

public class MigrazioneFascicoloEvent extends AbstractWrapperEvent<MigrazioneFascicoloDto> {
	
	public MigrazioneFascicoloEvent() {
		super();
	}

	public MigrazioneFascicoloEvent(final MigrazioneFascicoloDto migrazioneFascicoloDto) {
		super();
		setData(migrazioneFascicoloDto);
	}
}
