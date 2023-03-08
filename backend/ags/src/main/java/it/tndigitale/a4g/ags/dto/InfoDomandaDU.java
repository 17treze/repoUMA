package it.tndigitale.a4g.ags.dto;

import java.util.Date;

public class InfoDomandaDU {

	private Integer annoRiferimento;
	private String pac;
	private Long numeroDomanda;
	private String descrizioneDomanda;
	private Date dataPresentazione;
	private String cuaa;
	private String enteCompilatore;
	private String ragioneSociale;

	public Integer getAnnoRiferimento() {
		return annoRiferimento;
	}

	public void setAnnoRiferimento(Integer annoRiferimento) {
		this.annoRiferimento = annoRiferimento;
	}

	public String getPac() {
		return pac;
	}

	public void setPac(String pac) {
		this.pac = pac;
	}
	
	public Long getNumeroDomanda() {
		return numeroDomanda;
	}
	
	public void setNumeroDomanda(Long numeroDomanda) {
		this.numeroDomanda = numeroDomanda;
	}

	public String getDescrizioneDomanda() {
		return descrizioneDomanda;
	}

	public void setDescrizioneDomanda(String descrizioneDomanda) {
		this.descrizioneDomanda = descrizioneDomanda;
	}

	public Date getDataPresentazione() {
		return dataPresentazione;
	}

	public void setDataPresentazione(Date dataPresentazione) {
		this.dataPresentazione = dataPresentazione;
	}

	public String getCuaa() {
		return cuaa;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	public String getEnteCompilatore() {
		return enteCompilatore;
	}

	public void setEnteCompilatore(String enteCompilatore) {
		this.enteCompilatore = enteCompilatore;
	}
	
	public String getRagioneSociale() {
		return ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}
}
