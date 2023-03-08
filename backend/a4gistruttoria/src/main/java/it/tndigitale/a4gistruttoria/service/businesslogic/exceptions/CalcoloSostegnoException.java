package it.tndigitale.a4gistruttoria.service.businesslogic.exceptions;

public class CalcoloSostegnoException extends ElaborazioneIstruttoriaException {

	private static final long serialVersionUID = -3644421198117753568L;

	public CalcoloSostegnoException(String errorMessage) {
		super(errorMessage);
	}

	public CalcoloSostegnoException(String errorMessage, Throwable cause) {
		super(errorMessage, cause);
	}

	public String getExtendedMessage() {
		String message = this.getMessage();
		if (this.getCause() != null) {
			message += String.format(" (cause: %s)", this.getCause().getMessage());
		}
		return message;
	}

}