package it.tndigitale.a4g.srt.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PsrCostiContributiDto {
	private Integer idProgetto;
	private Integer idBando;
	private BigDecimal costoTotale;
	private BigDecimal contributoTotale;
	private BigDecimal contributoRimanente;
	private LocalDate data;
	
	public Integer getIdProgetto() {
		return idProgetto;
	}
	public void setIdProgetto(Integer idProgetto) {
		this.idProgetto = idProgetto;
	}
	public Integer getIdBando() {
		return idBando;
	}
	public void setIdBando(Integer idBando) {
		this.idBando = idBando;
	}
	public BigDecimal getCostoTotale() {
		return costoTotale;
	}
	public void setCostoTotale(BigDecimal costoTotale) {
		this.costoTotale = costoTotale;
	}
	public BigDecimal getContributoTotale() {
		return contributoTotale;
	}
	public void setContributoTotale(BigDecimal contributoTotale) {
		this.contributoTotale = contributoTotale;
	}
	public BigDecimal getContributoRimanente() {
		return contributoRimanente;
	}
	public void setContributoRimanente(BigDecimal contributoRimanente) {
		this.contributoRimanente = contributoRimanente;
	}
	public LocalDate getData() {
		return data;
	}
	public void setData(LocalDate data) {
		this.data = data;
	}
}
