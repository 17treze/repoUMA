package it.tndigitale.a4g.uma.dto.aual;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecapitoAualDto {
	
	private String codiCap;
	private String codiStatEste;
	private String codiComu;
	private String descIndi;
	private String codiProv;
	private String tipoRecapito;
	private String dataIniz;
	private String dataFine;
	private String descTele;
	private String descFaxInte;
	private String descMailInte;
	private String codiFiscReca;
	private String codiSiglProvReca;
	private String descProvReca;
	private String descComuReca;
	
	public String getCodiCap() {
		return codiCap;
	}
	public void setCodiCap(String codiCap) {
		this.codiCap = codiCap;
	}
	public String getCodiStatEste() {
		return codiStatEste;
	}
	public void setCodiStatEste(String codiStatEste) {
		this.codiStatEste = codiStatEste;
	}
	public String getCodiComu() {
		return codiComu;
	}
	public void setCodiComu(String codiComu) {
		this.codiComu = codiComu;
	}
	public String getDescIndi() {
		return descIndi;
	}
	public void setDescIndi(String descIndi) {
		this.descIndi = descIndi;
	}
	public String getCodiProv() {
		return codiProv;
	}
	public void setCodiProv(String codiProv) {
		this.codiProv = codiProv;
	}
	public String getTipoRecapito() {
		return tipoRecapito;
	}
	public void setTipoRecapito(String tipoRecapito) {
		this.tipoRecapito = tipoRecapito;
	}
	public String getDataIniz() {
		return dataIniz;
	}
	public void setDataIniz(String dataIniz) {
		this.dataIniz = dataIniz;
	}
	public String getDataFine() {
		return dataFine;
	}
	public void setDataFine(String dataFine) {
		this.dataFine = dataFine;
	}
	public String getDescTele() {
		return descTele;
	}
	public void setDescTele(String descTele) {
		this.descTele = descTele;
	}
	public String getDescFaxInte() {
		return descFaxInte;
	}
	public void setDescFaxInte(String descFaxInte) {
		this.descFaxInte = descFaxInte;
	}
	public String getDescMailInte() {
		return descMailInte;
	}
	public void setDescMailInte(String descMailInte) {
		this.descMailInte = descMailInte;
	}
	public String getCodiFiscReca() {
		return codiFiscReca;
	}
	public void setCodiFiscReca(String codiFiscReca) {
		this.codiFiscReca = codiFiscReca;
	}
	public String getCodiSiglProvReca() {
		return codiSiglProvReca;
	}
	public void setCodiSiglProvReca(String codiSiglProvReca) {
		this.codiSiglProvReca = codiSiglProvReca;
	}
	public String getDescProvReca() {
		return descProvReca;
	}
	public void setDescProvReca(String descProvReca) {
		this.descProvReca = descProvReca;
	}
	public String getDescComuReca() {
		return descComuReca;
	}
	public void setDescComuReca(String descComuReca) {
		this.descComuReca = descComuReca;
	}

}
