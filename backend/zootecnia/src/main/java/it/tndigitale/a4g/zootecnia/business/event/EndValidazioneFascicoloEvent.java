package it.tndigitale.a4g.zootecnia.business.event;

import it.tndigitale.a4g.framework.event.AbstractWrapperEvent;

public class EndValidazioneFascicoloEvent extends AbstractWrapperEvent<ValidazionePair> {
	
	public EndValidazioneFascicoloEvent() {
		super();
	}

	public EndValidazioneFascicoloEvent(final String cuaa, final Integer idValidazione) {
		super();
		setData(new ValidazionePair(cuaa, idValidazione));
	}
}
