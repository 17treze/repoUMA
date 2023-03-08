package it.tndigitale.a4gistruttoria.processo.exceptions;

public class ProcessoInEsecuzioneException extends Exception {
	private static final long serialVersionUID = 1L;

	public ProcessoInEsecuzioneException() {
		super();
	}

	public ProcessoInEsecuzioneException(String errorMessage) {
		super(errorMessage);
	}
}
