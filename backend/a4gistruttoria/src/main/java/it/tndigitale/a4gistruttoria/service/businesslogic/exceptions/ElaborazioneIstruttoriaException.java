package it.tndigitale.a4gistruttoria.service.businesslogic.exceptions;

public class ElaborazioneIstruttoriaException extends Exception {

	private static final long serialVersionUID = 3159317716726528812L;

	public ElaborazioneIstruttoriaException(String errorMessage) {
		super(errorMessage);
	}

	public ElaborazioneIstruttoriaException(String errorMessage, Throwable cause) {
		super(errorMessage, cause);
	}
}
