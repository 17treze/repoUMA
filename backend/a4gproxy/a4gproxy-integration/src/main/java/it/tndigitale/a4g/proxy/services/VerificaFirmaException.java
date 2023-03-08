package it.tndigitale.a4g.proxy.services;

public class VerificaFirmaException extends Exception {

	private static final long serialVersionUID = -690212493492387474L;
	
	private VerificaFirmaErrorCodeEnum error;

	public VerificaFirmaErrorCodeEnum getError() {
		return error;
	}

	public void setError(VerificaFirmaErrorCodeEnum error) {
		this.error = error;
	}

	public VerificaFirmaException(VerificaFirmaErrorCodeEnum verificaFirmaErrorCodeEnum) {
		super(verificaFirmaErrorCodeEnum.name());
		this.error = verificaFirmaErrorCodeEnum;
	}
}
