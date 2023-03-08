package it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo;

public class FascicoloValidazioneException extends Exception {
	private static final long serialVersionUID = 2506717638096750130L;

	public FascicoloValidazioneException(final FascicoloValidazioneEnum aperturaNotification) {
		super(aperturaNotification.name());
	}
}
