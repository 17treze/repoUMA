package it.tndigitale.a4g.zootecnia.business.persistence.repository.legacy;

public class SincronizzazioneAgsException extends Exception {
	private static final long serialVersionUID = 314995271834527864L;
	
	public SincronizzazioneAgsException(final String msg) {
		super(String.format("Errore nella sincronizzazione Zootecnia su AGS '%s'", msg));
	}
}
