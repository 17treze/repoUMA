package it.tndigitale.a4g.proxy.config;

import javax.xml.namespace.QName;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonIgnoreProperties(value = { "globalScope", "typeSubstituted", "nil" })
public abstract class JAXBElementMixIn<T> {

	@JsonValue
	abstract Object getValue();

	@JsonCreator
    public JAXBElementMixIn(@JsonProperty("name") QName name,
            @JsonProperty("declaredType") Class<T> declaredType,
            @SuppressWarnings("rawtypes") @JsonProperty("scope") Class scope,
            @JsonProperty("value") T value) {
    }
}