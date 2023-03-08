package it.tndigitale.a4g.richiestamodificasuolo.exception;

public class SuoloException extends RuntimeException {
	
	private static final long serialVersionUID = 4539477468146106978L;

	private ExceptionType type;

	public SuoloException(ExceptionType type) {
		super();
		this.type = type;
	}

	public SuoloException(ExceptionType type, String message, Throwable cause) {
		super(message, cause);
		this.type = type;
	}

	public SuoloException(ExceptionType type, String message) {
		super(message);
		this.type = type;
	}

	public SuoloException(ExceptionType type, Throwable cause) {
		super(cause);
		this.type = type;
	}
	
	public ExceptionType getType() {
		return type;
	}

	public enum ExceptionType {
        SECURITY_EXCEPTION,
        NOT_FOUND_EXCEPTION,
        CONFLICT_EXCEPTION, // Optimistic Lock
        INVALID_ARGUMENT_EXCEPTION,
        GENERIC_EXCEPTION;
		
		public SuoloException newSuoloExceptionInstance(String message, Throwable cause) {
			return new SuoloException(this, message, cause);
		}
		public SuoloException newSuoloExceptionInstance(String message) {
			return new SuoloException(this, message);
		}
	}
}
