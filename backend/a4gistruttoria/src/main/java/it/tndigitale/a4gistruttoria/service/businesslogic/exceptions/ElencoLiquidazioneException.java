package it.tndigitale.a4gistruttoria.service.businesslogic.exceptions;

public class ElencoLiquidazioneException extends Exception {

	public ElencoLiquidazioneException(String errorMessage) {
		super(errorMessage);
	}

	public ElencoLiquidazioneException(String errorMessage, Throwable cause) {
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
