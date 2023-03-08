package it.tndigitale.a4g.fascicolo.anagrafica.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class ReportValidazioneFascicoloPrimoFirmatarioDto {
	private String codiceFiscale;
	private String nome;
	private String cognome;

	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String cuaa) {
		this.codiceFiscale = cuaa;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String denominazioneAzienda) {
		this.nome = denominazioneAzienda;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String dataCostituzione) {
		this.cognome = dataCostituzione;
	}

}