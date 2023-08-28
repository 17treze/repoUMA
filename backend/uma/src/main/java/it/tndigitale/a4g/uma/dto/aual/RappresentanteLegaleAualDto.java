package it.tndigitale.a4g.uma.dto.aual;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RappresentanteLegaleAualDto {
	
    private Long codiRappLega;
    private String dataFineRapp;
    private String dataInizRapp;
    private String codiTipoCari;
    private String descTipoCari;
    private String codiFisc;
    
	public Long getCodiRappLega() {
		return codiRappLega;
	}
	public void setCodiRappLega(Long codiRappLega) {
		this.codiRappLega = codiRappLega;
	}
	public String getDataFineRapp() {
		return dataFineRapp;
	}
	public void setDataFineRapp(String dataFineRapp) {
		this.dataFineRapp = dataFineRapp;
	}
	public String getDataInizRapp() {
		return dataInizRapp;
	}
	public void setDataInizRapp(String dataInizRapp) {
		this.dataInizRapp = dataInizRapp;
	}
	public String getCodiTipoCari() {
		return codiTipoCari;
	}
	public void setCodiTipoCari(String codiTipoCari) {
		this.codiTipoCari = codiTipoCari;
	}
	public String getDescTipoCari() {
		return descTipoCari;
	}
	public void setDescTipoCari(String descTipoCari) {
		this.descTipoCari = descTipoCari;
	}
	public String getCodiFisc() {
		return codiFisc;
	}
	public void setCodiFisc(String codiFisc) {
		this.codiFisc = codiFisc;
	}
	
}
