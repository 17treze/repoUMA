package it.tndigitale.a4g.uma.dto.aual;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TipoRecapitoAualDto {
	
	private String codiTipoReca;
	private String descTipoReca;
	public String getCodiTipoReca() {
		return codiTipoReca;
	}
	public void setCodiTipoReca(String codiTipoReca) {
		this.codiTipoReca = codiTipoReca;
	}
	public String getDescTipoReca() {
		return descTipoReca;
	}
	public void setDescTipoReca(String descTipoReca) {
		this.descTipoReca = descTipoReca;
	}
	
}
