package it.tndigitale.a4g.fascicolo.mediator.business.service.event;

import it.tndigitale.a4g.framework.event.AbstractWrapperEvent;

public class DotazioneTecnicaEndValidazioneFascicoloEvent extends AbstractWrapperEvent<ValidazionePair> {
	
	public DotazioneTecnicaEndValidazioneFascicoloEvent() {
		super();
	}

	public DotazioneTecnicaEndValidazioneFascicoloEvent(final String cuaa, final Integer idValidazione) {
		super();
		setData(new ValidazionePair(cuaa, idValidazione));
	}
}
