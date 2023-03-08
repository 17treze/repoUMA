package it.tndigitale.a4gutente.exception;

public class ValidationException extends RuntimeException {

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
