package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.legacy;

public class SincronizzazioneAgsException extends Exception {
	private static final long serialVersionUID = 314995271834527864L;
	
	public SincronizzazioneAgsException(final String msg) {
		super(String.format("Errore nella sincronizzazione su AGS '%s'", msg));
	}
	
	public SincronizzazioneAgsException(final Exception ex) {
		super("Errore nella sincronizzazione su AGS", ex);
	}
}
