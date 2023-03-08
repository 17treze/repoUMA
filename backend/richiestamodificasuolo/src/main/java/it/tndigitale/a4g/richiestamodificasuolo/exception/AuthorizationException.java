package it.tndigitale.a4g.richiestamodificasuolo.exception;

public class AuthorizationException extends RuntimeException {
	
	private static final long serialVersionUID = 4539477468146106978L;

	private ExceptionType type;

	public AuthorizationException(ExceptionType type) {
		super();
		this.type = type;
	}

	public AuthorizationException(ExceptionType type, String message, Throwable cause) {
		super(message, cause);
		this.type = type;
	}

	public AuthorizationException(ExceptionType type, String message) {
		super(message);
		this.type = type;
	}

	public AuthorizationException(ExceptionType type, Throwable cause) {
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
		
		public AuthorizationException newAuthorizationExceptionInstance(String message, Throwable cause) {
			return new AuthorizationException(this, message, cause);
		}
		public AuthorizationException newAuthorizationExceptionInstance(String message) {
			return new AuthorizationException(this, message);
		}
	}
}
