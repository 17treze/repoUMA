
package it.tndigitale.a4gistruttoria.dto.antimafia;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "codice", "tipologia", "dataInizio", "dataFine", "href" })
public class Carica {

	@JsonProperty("codice")
	private String codice;
	
	@JsonProperty("tipologia")
	private String tipologia;

	@JsonProperty("dataInizio")
	private String dataInizio;

	@JsonProperty("dataFine")
	private String dataFine;
	
	@JsonProperty("selezionato")
	private boolean selezionato = false;
	
	@JsonProperty("href")
	private String href;
	
	
	@JsonProperty("href")
	public String getHref() {
		return href;
	}

	@JsonProperty("href")
	public void setHref(String href) {
		this.href = href;
	}

	@JsonProperty("dichiarazione")
	private boolean dichiarazione = false;

	public boolean isDichiarazione() {
		return dichiarazione;
	}

	public void setDichiarazione(boolean dichiarazione) {
		this.dichiarazione = dichiarazione;
	}

	public boolean isSelezionato() {
		return selezionato;
	}

	public void setSelezionato(boolean selezionato) {
		this.selezionato = selezionato;
	}

	@JsonProperty("codice")
	public String getCodice() {
		return codice;
	}

	@JsonProperty("codice")
	public void setCodice(String codice) {
		this.codice = codice;
	}

	@JsonProperty("tipologia")
	public String getTipologia() {
		return tipologia;
	}

	@JsonProperty("tipologia")
	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
	}

	@JsonProperty("dataInizio")
	public String getDataInizio() {
		return dataInizio;
	}

	@JsonProperty("dataInizio")
	public void setDataInizio(String dataInizio) {
		this.dataInizio = dataInizio;
	}

	@JsonProperty("dataFine")
	public String getDataFine() {
		return dataFine;
	}

	@JsonProperty("dataFine")
	public void setDataFine(String dataFine) {
		this.dataFine = dataFine;
	}
	

}
