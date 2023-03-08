package it.tndigitale.a4g.ags.dto;

import java.math.BigDecimal;

public class VariabileSostegnoAgs {

	private String variabile;
	private String descVariabile;
	private String valore;
	private String codGruppo;
	private BigDecimal ordine;

	public String getVariabile() {
		return variabile;
	}

	public void setVariabile(String variabile) {
		this.variabile = variabile;
	}

	public String getDescVariabile() {
		return descVariabile;
	}

	public void setDescVariabile(String descVariabile) {
		this.descVariabile = descVariabile;
	}

	public String getValore() {
		return valore;
	}

	public void setValore(String valore) {
		this.valore = valore;
	}

	public BigDecimal getOrdine() {
		return ordine;
	}

	public void setOrdine(BigDecimal ordine) {
		this.ordine = ordine;
	}

	public String getCodGruppo() {
		return codGruppo;
	}

	public void setCodGruppo(String codGruppo) {
		this.codGruppo = codGruppo;
	}
}
