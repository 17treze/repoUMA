package it.tndigitale.a4g.fascicolo.dotazionetecnica.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class ReportValidazioneMacchinarioDto {

	private String classe;
	private String sottoClasse;
	private String targa;
	private String telaio;

	public String getClasse() {
		return classe;
	}
	public ReportValidazioneMacchinarioDto setClasse(String classe) {
		this.classe = classe;
		return this;
	}
	public String getSottoClasse() {
		return sottoClasse;
	}
	public ReportValidazioneMacchinarioDto setSottoClasse(String sottoClasse) {
		this.sottoClasse = sottoClasse;
		return this;
	}
	public String getTarga() {
		return targa;
	}
	public ReportValidazioneMacchinarioDto setTarga(String targa) {
		this.targa = targa;
		return this;
	}
	public String getTelaio() {
		return telaio;
	}
	public ReportValidazioneMacchinarioDto setTelaio(String telaio) {
		this.telaio = telaio;
		return this;
	}
}
