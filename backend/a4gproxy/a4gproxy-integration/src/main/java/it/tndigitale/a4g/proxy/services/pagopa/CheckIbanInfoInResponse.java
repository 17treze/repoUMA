package it.tndigitale.a4g.proxy.services.pagopa;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "banca", "sede", "codPaese", "bic", "filiale" })
public class CheckIbanInfoInResponse {

	@JsonProperty("banca")
	private String banca;
	@JsonProperty("sede")
	private String sede;
	@JsonProperty("codPaese")
	private String codPaese;
	@JsonProperty("bic")
	private String bic;
	@JsonProperty("filiale")
	private String filiale;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("banca")
	public String getBanca() {
		return banca;
	}

	@JsonProperty("banca")
	public void setBanca(String banca) {
		this.banca = banca;
	}

	@JsonProperty("sede")
	public String getSede() {
		return sede;
	}

	@JsonProperty("sede")
	public void setSede(String sede) {
		this.sede = sede;
	}

	@JsonProperty("codPaese")
	public String getCodPaese() {
		return codPaese;
	}

	@JsonProperty("codPaese")
	public void setCodPaese(String codPaese) {
		this.codPaese = codPaese;
	}

	@JsonProperty("bic")
	public String getBic() {
		return bic;
	}

	@JsonProperty("bic")
	public void setBic(String bic) {
		this.bic = bic;
	}

	@JsonProperty("filiale")
	public String getFiliale() {
		return filiale;
	}

	@JsonProperty("filiale")
	public void setFiliale(String filiale) {
		this.filiale = filiale;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}