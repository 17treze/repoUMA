package it.tndigitale.a4g.uma.dto.aual;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CarburanteAualDto {
	
	private String codiCarb;
	private String descCarb;
	private String codiCarbSian;
	
	public String getCodiCarb() {
		return codiCarb;
	}
	public void setCodiCarb(String codiCarb) {
		this.codiCarb = codiCarb;
	}
	public String getDescCarb() {
		return descCarb;
	}
	public void setDescCarb(String descCarb) {
		this.descCarb = descCarb;
	}
	public String getCodiCarbSian() {
		return codiCarbSian;
	}
	public void setCodiCarbSian(String codiCarbSian) {
		this.codiCarbSian = codiCarbSian;
	}
}
