package it.tndigitale.a4g.proxy.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"numeroProgressivoLavorazione",
"annoCampagna",
"cuaa",
"numeroDomanda",
"codiceIntervento",
"importoDeterminato",
"importoLiquidato",
"importoRichiesto",
"pagamentoAutorizzato"
})
public class DatiPagamentiDto {

	@JsonProperty("numeroProgressivoLavorazione")
	private Long numeroProgressivoLavorazione;
	
	@JsonProperty("annoCampagna")
	private Long annoCampagna;
	
	@JsonProperty("cuaa")
	private String cuaa;
	
	@JsonProperty("numeroDomanda")
	private String numeroDomanda;
	
	@JsonProperty("codiceIntervento")
	private Long codiceIntervento;
	
	@JsonProperty("importoDeterminato")
	private Double importoDeterminato;
	
	@JsonProperty("importoLiquidato")
	private Double importoLiquidato;
	
	@JsonProperty("importoRichiesto")
	private Double importoRichiesto;
	
	@JsonProperty("pagamentoAutorizzato")
	private Boolean pagamentoAutorizzato;

	@JsonProperty("numeroProgressivoLavorazione")
	public Long getNumeroProgressivoLavorazione() {
		return numeroProgressivoLavorazione;
	}

	@JsonProperty("numeroProgressivoLavorazione")
	public void setNumeroProgressivoLavorazione(Long numeroProgressivoLavorazione) {
		this.numeroProgressivoLavorazione = numeroProgressivoLavorazione;
	}

	@JsonProperty("annoCampagna")
	public Long getAnnoCampagna() {
		return annoCampagna;
	}

	@JsonProperty("annoCampagna")
	public void setAnnoCampagna(Long annoCampagna) {
		this.annoCampagna = annoCampagna;
	}

	@JsonProperty("cuaa")
	public String getCuaa() {
		return cuaa;
	}

	@JsonProperty("cuaa")
	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	@JsonProperty("numeroDomanda")
	public String getNumeroDomanda() {
		return numeroDomanda;
	}

	@JsonProperty("numeroDomanda")
	public void setNumeroDomanda(String numeroDomanda) {
		this.numeroDomanda = numeroDomanda;
	}

	@JsonProperty("codiceIntervento")
	public Long getCodiceIntervento() {
		return codiceIntervento;
	}

	@JsonProperty("codiceIntervento")
	public void setCodiceIntervento(Long codiceIntervento) {
		this.codiceIntervento = codiceIntervento;
	}

	@JsonProperty("importoDeterminato")
	public Double getImportoDeterminato() {
		return importoDeterminato;
	}

	@JsonProperty("importoDeterminato")
	public void setImportoDeterminato(Double importoDeterminato) {
		this.importoDeterminato = importoDeterminato;
	}

	@JsonProperty("importoLiquidato")
	public Double getImportoLiquidato() {
		return importoLiquidato;
	}

	@JsonProperty("importoLiquidato")
	public void setImportoLiquidato(Double importoLiquidato) {
		this.importoLiquidato = importoLiquidato;
	}

	@JsonProperty("importoRichiesto")
	public Double getImportoRichiesto() {
		return importoRichiesto;
	}

	@JsonProperty("importoRichiesto")
	public void setImportoRichiesto(Double importoRichiesto) {
		this.importoRichiesto = importoRichiesto;
	}

	@JsonProperty("pagamentoAutorizzato")
	public Boolean getPagamentoAutorizzato() {
		return pagamentoAutorizzato;
	}

	@JsonProperty("pagamentoAutorizzato")
	public void setPagamentoAutorizzato(Boolean pagamentoAutorizzato) {
		this.pagamentoAutorizzato = pagamentoAutorizzato;
	}
}