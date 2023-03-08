package it.tndigitale.a4gistruttoria.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "aggiornamentoFascicolo", "visioneAnomalie", "firmaDomanda", "archiviazioneDomanda" })
public class ControlliPresentazione {

	@JsonProperty("aggiornamentoFascicolo")
	private String aggiornamentoFascicolo;
	@JsonProperty("visioneAnomalie")
	private String visioneAnomalie;
	@JsonProperty("firmaDomanda")
	private String firmaDomanda;
	@JsonProperty("archiviazioneDomanda")
	private String archiviazioneDomanda;

	@JsonProperty("aggiornamentoFascicolo")
	public String getAggiornamentoFascicolo() {
		return aggiornamentoFascicolo;
	}

	@JsonProperty("aggiornamentoFascicolo")
	public void setAggiornamentoFascicolo(String aggiornamentoFascicolo) {
		this.aggiornamentoFascicolo = aggiornamentoFascicolo;
	}

	@JsonProperty("visioneAnomalie")
	public String getVisioneAnomalie() {
		return visioneAnomalie;
	}

	@JsonProperty("visioneAnomalie")
	public void setVisioneAnomalie(String visioneAnomalie) {
		this.visioneAnomalie = visioneAnomalie;
	}

	@JsonProperty("firmaDomanda")
	public String getFirmaDomanda() {
		return firmaDomanda;
	}

	@JsonProperty("firmaDomanda")
	public void setFirmaDomanda(String firmaDomanda) {
		this.firmaDomanda = firmaDomanda;
	}

	@JsonProperty("archiviazioneDomanda")
	public String getArchiviazioneDomanda() {
		return archiviazioneDomanda;
	}

	@JsonProperty("archiviazioneDomanda")
	public void setArchiviazioneDomanda(String archiviazioneDomanda) {
		this.archiviazioneDomanda = archiviazioneDomanda;
	}

}
