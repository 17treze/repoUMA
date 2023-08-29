package it.tndigitale.a4g.uma.dto.aual;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TipoFabbricatoAualDto {
	
	private String codiTipoFabb;
	private String descFabb;
	
	public String getCodiTipoFabb() {
		return codiTipoFabb;
	}
	public void setCodiTipoFabb(String codiTipoFabb) {
		this.codiTipoFabb = codiTipoFabb;
	}
	public String getDescFabb() {
		return descFabb;
	}
	public void setDescFabb(String descFabb) {
		this.descFabb = descFabb;
	}
	
}
