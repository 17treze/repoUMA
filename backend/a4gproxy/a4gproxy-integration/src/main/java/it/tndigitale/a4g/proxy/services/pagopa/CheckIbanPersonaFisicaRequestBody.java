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
@JsonPropertyOrder({ "IBAN", "codFiscalePersonaFisica" })
public class CheckIbanPersonaFisicaRequestBody {
	@JsonProperty("IBAN")
	private String iban;
	@JsonProperty("codFiscalePersonaFisica")
	private String codFiscalePersonaFisica;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("IBAN")
	public String getIban() {
		return iban;
	}

	@JsonProperty("IBAN")
	public void setIban(String iban) {
		this.iban = iban;
	}

	@JsonProperty("codFiscalePersonaFisica")
	public String getCodFiscalePersonaFisica() {
		return codFiscalePersonaFisica;
	}

	@JsonProperty("codFiscalePersonaFisica")
	public void setCodFiscalePersonaFisica(String codFiscalePersonaFisica) {
		this.codFiscalePersonaFisica = codFiscalePersonaFisica;
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