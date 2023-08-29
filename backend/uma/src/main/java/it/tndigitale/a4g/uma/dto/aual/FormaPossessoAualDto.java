package it.tndigitale.a4g.uma.dto.aual;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FormaPossessoAualDto {
	
	private String codiFormPoss;
	private String descFormPoss;
	private String codiFormPossSian;
	
	public String getCodiFormPoss() {
		return codiFormPoss;
	}
	public void setCodiFormPoss(String codiFormPoss) {
		this.codiFormPoss = codiFormPoss;
	}
	public String getDescFormPoss() {
		return descFormPoss;
	}
	public void setDescFormPoss(String descFormPoss) {
		this.descFormPoss = descFormPoss;
	}
	public String getCodiFormPossSian() {
		return codiFormPossSian;
	}
	public void setCodiFormPossSian(String codiFormPossSian) {
		this.codiFormPossSian = codiFormPossSian;
	}
}
