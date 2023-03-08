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
	@JsonPropertyOrder({
	"IBAN",
	"codFiscalePersonaGiuridica",
	"codIVAPersonaGiuridica"
	})
	public class CheckIbanPersonaGiuridicaRequestBody {

	@JsonProperty("IBAN")
	private String iban;
	@JsonProperty("codFiscalePersonaGiuridica")
	private String codFiscalePersonaGiuridica;
	@JsonProperty("codIVAPersonaGiuridica")
	private String codIVAPersonaGiuridica;
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

	@JsonProperty("codFiscalePersonaGiuridica")
	public String getCodFiscalePersonaGiuridica() {
	return codFiscalePersonaGiuridica;
	}

	@JsonProperty("codFiscalePersonaGiuridica")
	public void setCodFiscalePersonaGiuridica(String codFiscalePersonaGiuridica) {
	this.codFiscalePersonaGiuridica = codFiscalePersonaGiuridica;
	}

	@JsonProperty("codIVAPersonaGiuridica")
	public String getCodIVAPersonaGiuridica() {
	return codIVAPersonaGiuridica;
	}

	@JsonProperty("codIVAPersonaGiuridica")
	public void setCodIVAPersonaGiuridica(String codIVAPersonaGiuridica) {
	this.codIVAPersonaGiuridica = codIVAPersonaGiuridica;
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