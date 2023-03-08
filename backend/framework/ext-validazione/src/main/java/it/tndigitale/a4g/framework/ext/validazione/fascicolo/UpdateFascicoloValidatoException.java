package it.tndigitale.a4g.framework.ext.validazione.fascicolo;

public class UpdateFascicoloValidatoException extends Exception {
	private static final long serialVersionUID = 5670587735347744463L;
	
	public UpdateFascicoloValidatoException() {
		super("Non è possibile cancellare/modificare un fascicolo validato");
	}
}
