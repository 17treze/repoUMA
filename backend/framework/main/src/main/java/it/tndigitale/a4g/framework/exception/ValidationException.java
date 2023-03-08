package it.tndigitale.a4g.framework.exception;

public class ValidationException extends RuntimeException {

		private static final long serialVersionUID = 214504642377638412L;

		public ValidationException(String message) {
        super(message);
    }

    public ValidationException() {
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }


    private Integer code;

    public Integer getCode() {
        return code;
    }

    public ValidationException setCode(Integer code) {
        this.code = code;
        return this;
    }
}
