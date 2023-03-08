package it.tndigitale.a4g.zootecnia.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class ReportValidazioneConsistenzaZootecnicaDto {

	private String classificazione;
	private Integer numeroCapi;
	private Float coefficienteUba;
	private Float numeroUba;

	public ReportValidazioneConsistenzaZootecnicaDto(String classificazione, Integer numeroCapi, Float coefficienteUba,
			Float numeroUba) {
		super();
		this.classificazione = classificazione;
		this.numeroCapi = numeroCapi;
		this.coefficienteUba = coefficienteUba;
		this.numeroUba = numeroUba;
	}
	public String getClassificazione() {
		return classificazione;
	}
	public void setClassificazione(String classificazione) {
		this.classificazione = classificazione;
	}
	public Integer getNumeroCapi() {
		return numeroCapi;
	}
	public void setNumeroCapi(Integer numeroCapi) {
		this.numeroCapi = numeroCapi;
	}
	public Float getCoefficienteUba() {
		return coefficienteUba;
	}
	public void setCoefficienteUba(Float coefficienteUba) {
		this.coefficienteUba = coefficienteUba;
	}
	public Float getNumeroUba() {
		return numeroUba;
	}
	public void setNumeroUba(Float numeroUba) {
		this.numeroUba = numeroUba;
	}

}
