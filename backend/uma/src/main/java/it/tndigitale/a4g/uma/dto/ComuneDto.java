package it.tndigitale.a4g.uma.dto;

import java.io.Serializable;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

public class ComuneDto implements Serializable {

	private String codiProv;
	private String codiComu;
	private String descComu;
	private String codiNcap;
	private String codiComuCapo;
	private String codiCata;

	public ComuneDto(String codiProv, String codiComu, String descComu, String codiNcap, String codiComuCapo,
			String codiCata) {
		super();
		this.codiProv = codiProv;
		this.codiComu = codiComu;
		this.descComu = descComu;
		this.codiNcap = codiNcap;
		this.codiComuCapo = codiComuCapo;
		this.codiCata = codiCata;
	}
		
	public String getCodiProv() {
		return codiProv;
	}
	public void setCodiProv(String codiProv) {
		this.codiProv = codiProv;
	}
	public String getCodiComu() {
		return codiComu;
	}
	public void setCodiComu(String codiComu) {
		this.codiComu = codiComu;
	}
	public String getDescComu() {
		return descComu;
	}
	public void setDescComu(String descComu) {
		this.descComu = descComu;
	}
	public String getCodiNcap() {
		return codiNcap;
	}
	public void setCodiNcap(String codiNcap) {
		this.codiNcap = codiNcap;
	}
	public String getCodiComuCapo() {
		return codiComuCapo;
	}
	public void setCodiComuCapo(String codiComuCapo) {
		this.codiComuCapo = codiComuCapo;
	}
	public String getCodiCata() {
		return codiCata;
	}
	public void setCodiCata(String codiCata) {
		this.codiCata = codiCata;
	}
	
	
}
