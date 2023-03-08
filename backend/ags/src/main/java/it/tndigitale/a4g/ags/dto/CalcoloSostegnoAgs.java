package it.tndigitale.a4g.ags.dto;

import java.util.List;

public class CalcoloSostegnoAgs {

	private Long idCalcolo;
	private Long idDefCalcolo;
	private String codCalcolo;
	private List<VariabileSostegnoAgs> variabiliCalcolo;

	public Long getIdCalcolo() {
		return idCalcolo;
	}

	public void setIdCalcolo(Long idCalcolo) {
		this.idCalcolo = idCalcolo;
	}

	public Long getIdDefCalcolo() {
		return idDefCalcolo;
	}

	public void setIdDefCalcolo(Long idDefCalcolo) {
		this.idDefCalcolo = idDefCalcolo;
	}

	public String getCodCalcolo() {
		return codCalcolo;
	}

	public void setCodCalcolo(String codCalcolo) {
		this.codCalcolo = codCalcolo;
	}

	public List<VariabileSostegnoAgs> getVariabiliCalcolo() {
		return variabiliCalcolo;
	}

	public void setVariabiliCalcolo(List<VariabileSostegnoAgs> variabiliCalcolo) {
		this.variabiliCalcolo = variabiliCalcolo;
	}

}
