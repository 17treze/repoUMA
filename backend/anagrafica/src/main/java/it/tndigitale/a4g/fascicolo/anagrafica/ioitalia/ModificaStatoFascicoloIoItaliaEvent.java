package it.tndigitale.a4g.fascicolo.anagrafica.ioitalia;

import it.tndigitale.a4g.framework.event.AbstractWrapperEvent;

public class ModificaStatoFascicoloIoItaliaEvent extends AbstractWrapperEvent<IoItaliaMessage> {
	public ModificaStatoFascicoloIoItaliaEvent() {}
	
	public ModificaStatoFascicoloIoItaliaEvent(final IoItaliaMessage message) {
		this.setData(message);
	}
}
