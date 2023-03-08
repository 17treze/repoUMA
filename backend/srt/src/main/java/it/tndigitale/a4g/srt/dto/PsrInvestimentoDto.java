package it.tndigitale.a4g.srt.dto;

import java.math.BigDecimal;
public class PsrInvestimentoDto {
	private Integer idInvestimento;
	private Integer idProgetto;
	private String descrizione;
	private BigDecimal costoInvestimento;
	private BigDecimal speseGenerali;
	private BigDecimal contributoRichiesto;
	private BigDecimal quotaContributoRichiesto;
	private Integer idVariante;
	private Integer idInvestimentoOriginale;
	private String descrizioneCodificaInvestimento;
	private String descrizioneDettaglioInvestimenti;
	private String descrizioneSettoriProduttivi;

	public Integer getIdInvestimento() {
		return idInvestimento;
	}

	public void setIdInvestimento(Integer idInvestimento) {
		this.idInvestimento = idInvestimento;
	}

	public Integer getIdProgetto() {
		return idProgetto;
	}

	public void setIdProgetto(Integer idProgetto) {
		this.idProgetto = idProgetto;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public BigDecimal getCostoInvestimento() {
		return costoInvestimento;
	}

	public void setCostoInvestimento(BigDecimal costoInvestimento) {
		this.costoInvestimento = costoInvestimento;
	}

	public BigDecimal getSpeseGenerali() {
		return speseGenerali;
	}

	public void setSpeseGenerali(BigDecimal speseGenerali) {
		this.speseGenerali = speseGenerali;
	}

	public BigDecimal getContributoRichiesto() {
		return contributoRichiesto;
	}

	public void setContributoRichiesto(BigDecimal contributoRichiesto) {
		this.contributoRichiesto = contributoRichiesto;
	}

	public BigDecimal getQuotaContributoRichiesto() {
		return quotaContributoRichiesto;
	}

	public void setQuotaContributoRichiesto(BigDecimal quotaContributoRichiesto) {
		this.quotaContributoRichiesto = quotaContributoRichiesto;
	}

	public Integer getIdVariante() {
		return idVariante;
	}

	public void setIdVariante(Integer idVariante) {
		this.idVariante = idVariante;
	}

	public Integer getIdInvestimentoOriginale() {
		return idInvestimentoOriginale;
	}

	public void setIdInvestimentoOriginale(Integer idInvestimentoOriginale) {
		this.idInvestimentoOriginale = idInvestimentoOriginale;
	}	public String getDescrizioneCodificaInvestimento() {
		return descrizioneCodificaInvestimento;
	}
	public void setDescrizioneCodificaInvestimento(String descrizioneCodificaInvestimento) {
		this.descrizioneCodificaInvestimento = descrizioneCodificaInvestimento;
	}
	public String getDescrizioneDettaglioInvestimenti() {
		return descrizioneDettaglioInvestimenti;
	}
	public void setDescrizioneDettaglioInvestimenti(String descrizioneDettaglioInvestimenti) {
		this.descrizioneDettaglioInvestimenti = descrizioneDettaglioInvestimenti;
	}
	public String getDescrizioneSettoriProduttivi() {
		return descrizioneSettoriProduttivi;
	}
	public void setDescrizioneSettoriProduttivi(String descrizioneSettoriProduttivi) {
		this.descrizioneSettoriProduttivi = descrizioneSettoriProduttivi;
	}
}
