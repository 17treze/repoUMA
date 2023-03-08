package it.tndigitale.a4g.proxy.bdn.dto.istruttoria;

import java.math.BigDecimal;

public class ConsistenzaPascolo2015Dto {

	private String codiceFiscaleSoggetto;
	private BigDecimal annoCampagna;
	private String codicePascolo;
	private String fasciaEta;
	private BigDecimal numeroCapi;
	private BigDecimal numeroCapiMedi;
	private BigDecimal giorniAlPascolo;
	private String specie;
	private String provincia;
	private String codiceFiscaleResponsabile;

	public String getCodiceFiscaleSoggetto() {
		return codiceFiscaleSoggetto;
	}

	public void setCodiceFiscaleSoggetto(String codiceFiscaleSoggetto) {
		this.codiceFiscaleSoggetto = codiceFiscaleSoggetto;
	}

	public BigDecimal getAnnoCampagna() {
		return annoCampagna;
	}

	public void setAnnoCampagna(BigDecimal annoCampagna) {
		this.annoCampagna = annoCampagna;
	}

	public String getCodicePascolo() {
		return codicePascolo;
	}

	public void setCodicePascolo(String codicePascolo) {
		this.codicePascolo = codicePascolo;
	}

	public String getFasciaEta() {
		return fasciaEta;
	}

	public void setFasciaEta(String fasciaEta) {
		this.fasciaEta = fasciaEta;
	}

	public BigDecimal getNumeroCapi() {
		return numeroCapi;
	}

	public void setNumeroCapi(BigDecimal numeroCapi) {
		this.numeroCapi = numeroCapi;
	}

	public BigDecimal getNumeroCapiMedi() {
		return numeroCapiMedi;
	}

	public void setNumeroCapiMedi(BigDecimal numeroCapiMedi) {
		this.numeroCapiMedi = numeroCapiMedi;
	}

	public BigDecimal getGiorniAlPascolo() {
		return giorniAlPascolo;
	}

	public void setGiorniAlPascolo(BigDecimal giorniAlPascolo) {
		this.giorniAlPascolo = giorniAlPascolo;
	}

	public String getSpecie() {
		return specie;
	}

	public void setSpecie(String specie) {
		this.specie = specie;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getCodiceFiscaleResponsabile() {
		return codiceFiscaleResponsabile;
	}

	public void setCodiceFiscaleResponsabile(String codiceFiscaleResponsabile) {
		this.codiceFiscaleResponsabile = codiceFiscaleResponsabile;
	}

}
