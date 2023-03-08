package it.tndigitale.a4g.proxy.services.pagopa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "validation", "errors", "info" })
public class CheckIbanResponse {

	@JsonProperty("validation")
	private Boolean validation;
	@JsonProperty("errors")
	private List<CheckIbanErrorInResponse> errors = null;
	@JsonProperty("info")
	private CheckIbanInfoInResponse info;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("validation")
	public Boolean getValidation() {
		return validation;
	}

	@JsonProperty("validation")
	public void setValidation(Boolean validation) {
		this.validation = validation;
	}

	@JsonProperty("errors")
	public List<CheckIbanErrorInResponse> getErrors() {
		return errors;
	}

	@JsonProperty("errors")
	public void setErrors(List<CheckIbanErrorInResponse> errors) {
		this.errors = errors;
	}

	@JsonProperty("info")
	public CheckIbanInfoInResponse getInfo() {
		return info;
	}

	@JsonProperty("info")
	public void setInfo(CheckIbanInfoInResponse info) {
		this.info = info;
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
