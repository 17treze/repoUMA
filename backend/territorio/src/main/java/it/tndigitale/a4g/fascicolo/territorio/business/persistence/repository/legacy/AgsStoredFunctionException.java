package it.tndigitale.a4g.fascicolo.territorio.business.persistence.repository.legacy;

public class AgsStoredFunctionException extends Exception {

	private static final long serialVersionUID = 6242470159053410413L;

	public AgsStoredFunctionException(final String msg) {
		super(String.format("Errore nel salvataggio della conduzione terreni su AGS '%s'", msg));
	}

	public AgsStoredFunctionException(final Exception ex) {
		super("Errore salvataggio della conduzione terreni su AGS", ex);
	}
}
