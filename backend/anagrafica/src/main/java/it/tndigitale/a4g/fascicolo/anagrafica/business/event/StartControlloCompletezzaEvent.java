package it.tndigitale.a4g.fascicolo.anagrafica.business.event;

import it.tndigitale.a4g.framework.event.AbstractWrapperEvent;

public class StartControlloCompletezzaEvent extends AbstractWrapperEvent<ValidazionePair> {

	public StartControlloCompletezzaEvent() {
		super();
	}

	public StartControlloCompletezzaEvent(final String cuaa, final Integer idValidazione) {
		super();
		setData(new ValidazionePair(cuaa, idValidazione));
	}
}
