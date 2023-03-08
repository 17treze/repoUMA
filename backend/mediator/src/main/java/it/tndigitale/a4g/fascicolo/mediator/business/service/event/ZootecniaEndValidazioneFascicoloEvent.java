package it.tndigitale.a4g.fascicolo.mediator.business.service.event;

import it.tndigitale.a4g.framework.event.AbstractWrapperEvent;

public class ZootecniaEndValidazioneFascicoloEvent extends AbstractWrapperEvent<ValidazionePair> {
	
	public ZootecniaEndValidazioneFascicoloEvent() {
		super();
	}

	public ZootecniaEndValidazioneFascicoloEvent(final String cuaa, final Integer idValidazione) {
		super();
		setData(new ValidazionePair(cuaa, idValidazione));
	}
}
