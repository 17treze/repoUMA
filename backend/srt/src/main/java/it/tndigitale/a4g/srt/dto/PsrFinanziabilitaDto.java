package it.tndigitale.a4g.srt.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PsrFinanziabilitaDto {
	private BigDecimal costoRichiesto;
	private BigDecimal contributoRichiesto;
	private BigDecimal contributoAmmesso;
	private LocalDate data;
	
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
	public LocalDate getData() {
		return data;
	}
	public void setData(LocalDate data) {
		this.data = data;
	}
}
