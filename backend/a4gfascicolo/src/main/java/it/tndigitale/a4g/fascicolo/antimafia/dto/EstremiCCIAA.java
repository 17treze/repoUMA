
package it.tndigitale.a4g.fascicolo.antimafia.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "sede", "numeroIscrizione", "dataIscrizione" })
public class EstremiCCIAA {

	@JsonProperty("sede")
	private String sede;
	@JsonProperty("numeroIscrizione")
	private String numeroIscrizione;
	@JsonProperty("dataIscrizione")
	private String dataIscrizione;

	@JsonProperty("sede")
	public String getSede() {
		return sede;
	}

	@JsonProperty("sede")
	public void setSede(String sede) {
		this.sede = sede;
	}

	@JsonProperty("numeroIscrizione")
	public String getNumeroIscrizione() {
		return numeroIscrizione;
	}

	@JsonProperty("numeroIscrizione")
	public void setNumeroIscrizione(String numeroIscrizione) {
		this.numeroIscrizione = numeroIscrizione;
	}

	@JsonProperty("dataIscrizione")
	public String getDataIscrizione() {
		return dataIscrizione;
	}

	@JsonProperty("dataIscrizione")
	public void setDataIscrizione(String dataIscrizione) {
		this.dataIscrizione = dataIscrizione;
	}
}
