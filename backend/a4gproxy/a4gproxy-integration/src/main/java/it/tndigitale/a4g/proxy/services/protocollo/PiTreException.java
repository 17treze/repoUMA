package it.tndigitale.a4g.proxy.services.protocollo;

public class PiTreException extends Exception {

	private static final long serialVersionUID = -6911901138378087273L;
	
	public static final String ERROR_NO_TOKEN = "Token di autenticazione necessario per poter eseguire la chiamata al servizio.";
	public static final String ERROR_UNSPECIFIED = "Errore del servizio di protocollo.";

	public PiTreException(String errorMessage) {
		super(errorMessage);
	}

	public PiTreException(String errorMessage, Throwable cause) {
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
