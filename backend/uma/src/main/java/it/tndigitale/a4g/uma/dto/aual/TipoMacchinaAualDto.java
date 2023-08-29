package it.tndigitale.a4g.uma.dto.aual;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TipoMacchinaAualDto {
	
	private String codiTipoMacc;
	private String descMacc;
	private String codiTipoMaccSian;
	
	public String getCodiTipoMacc() {
		return codiTipoMacc;
	}
	public void setCodiTipoMacc(String codiTipoMacc) {
		this.codiTipoMacc = codiTipoMacc;
	}
	public String getDescMacc() {
		return descMacc;
	}
	public void setDescMacc(String descMacc) {
		this.descMacc = descMacc;
	}
	public String getCodiTipoMaccSian() {
		return codiTipoMaccSian;
	}
	public void setCodiTipoMaccSian(String codiTipoMaccSian) {
		this.codiTipoMaccSian = codiTipoMaccSian;
	}	
}
