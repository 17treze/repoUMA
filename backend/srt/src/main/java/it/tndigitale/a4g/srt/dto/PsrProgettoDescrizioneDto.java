package it.tndigitale.a4g.srt.dto;

import java.math.BigDecimal;

public class PsrProgettoDescrizioneDto {
	private Integer idProgetto;
	private String codificaDescrizione;
	private String dettaglioDescrizione;
	private BigDecimal costoRichiesto;
	private BigDecimal contributoRichiesto;
	private BigDecimal contributoAmmesso;
	
	public Integer getIdProgetto() {
		return idProgetto;
	}
	public void setIdProgetto(Integer idProgetto) {
		this.idProgetto = idProgetto;
	}
	public String getCodificaDescrizione() {
		return codificaDescrizione;
	}
	public void setCodificaDescrizione(String codificaDescrizione) {
		this.codificaDescrizione = codificaDescrizione;
	}
	public String getDettaglioDescrizione() {
		return dettaglioDescrizione;
	}
	public void setDettaglioDescrizione(String dettaglioDescrizione) {
		this.dettaglioDescrizione = dettaglioDescrizione;
	}
	public BigDecimal getCostoRichiesto() {
		return costoRichiesto;
	}
	public void setCostoRichiesto(BigDecimal costoRichiesto) {
		this.costoRichiesto = costoRichiesto;
	}
	public BigDecimal getContributoRichiesto() {
		return contributoRichiesto;
	}
	public void setContributoRichiesto(BigDecimal contributoRichiesto) {
		this.contributoRichiesto = contributoRichiesto;
	}
	public BigDecimal getContributoAmmesso() {
		return contributoAmmesso;
	}
	public void setContributoAmmesso(BigDecimal contributoAmmesso) {
		this.contributoAmmesso = contributoAmmesso;
	}	
}
