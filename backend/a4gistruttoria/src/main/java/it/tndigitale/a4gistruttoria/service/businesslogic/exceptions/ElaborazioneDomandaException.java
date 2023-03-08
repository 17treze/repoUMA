package it.tndigitale.a4gistruttoria.service.businesslogic.exceptions;

public class ElaborazioneDomandaException extends Exception {

	private static final long serialVersionUID = 3159317716726528812L;

	public ElaborazioneDomandaException(String errorMessage) {
		super(errorMessage);
	}

	public ElaborazioneDomandaException(String errorMessage, Throwable cause) {
		super(errorMessage, cause);
	}
}
