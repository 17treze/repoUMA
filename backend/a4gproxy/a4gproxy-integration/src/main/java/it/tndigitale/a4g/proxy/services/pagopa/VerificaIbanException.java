package it.tndigitale.a4g.proxy.services.pagopa;

import org.springframework.http.HttpStatus;

public class VerificaIbanException extends Exception {
	
	private HttpStatus httpStatus;
	
	public VerificaIbanException() {
		super();
	}
	
	public VerificaIbanException(String msg) {
		super(msg);
	}
	
	public VerificaIbanException(HttpStatus httpStatus) {
		super();
		this.httpStatus = httpStatus;
	}
	
	public VerificaIbanException(HttpStatus httpStatus, String msg) {
		super(msg);
		this.httpStatus = httpStatus;
	}
	
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}


}
