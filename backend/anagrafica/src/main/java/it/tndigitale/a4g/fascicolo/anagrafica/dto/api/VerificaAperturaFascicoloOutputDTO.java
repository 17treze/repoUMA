package it.tndigitale.a4g.fascicolo.anagrafica.dto.api;

import java.time.LocalDate;

public class VerificaAperturaFascicoloOutputDTO {
	private boolean verificaApertura;
	private String errorCode;
	private LocalDate dataSottoscrizione;
	
	public VerificaAperturaFascicoloOutputDTO(boolean verificaApertura, String errorCode) {
		this.verificaApertura = verificaApertura;
		this.errorCode = errorCode;
	}
	public VerificaAperturaFascicoloOutputDTO(boolean verificaApertura, String errorCode,
			LocalDate dataSottoscrizione) {
		this.verificaApertura = verificaApertura;
		this.errorCode = errorCode;
		this.dataSottoscrizione = dataSottoscrizione;
	}
	
	public boolean isVerificaApertura() {
		return verificaApertura;
	}
	public void setVerificaApertura(boolean verificaApertura) {
		this.verificaApertura = verificaApertura;
	}
	public LocalDate getDataSottoscrizione() {
		return dataSottoscrizione;
	}
	public void setDataSottoscrizione(LocalDate dataSottoscrizione) {
		this.dataSottoscrizione = dataSottoscrizione;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
