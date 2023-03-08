package it.tndigitale.a4g.framework.ext.validazione.fascicolo;

public class FascicoloNonValidabileException extends Exception {
	private static final long serialVersionUID = -8851288706447309277L;
	
	private static final String MESSAGE = "Fascicolo non validabile";

	public FascicoloNonValidabileException() {
		super(MESSAGE);
	}
	
	public FascicoloNonValidabileException(final String customMessage) {
		super(customMessage);
	}

	public FascicoloNonValidabileException(final StatoFascicoloEnum statoFascicolo) {
		super(String.format("%s, stato: %s", MESSAGE, statoFascicolo.name()));
	}
}
