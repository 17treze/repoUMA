package it.tndigitale.a4g.proxy.services.pagopa;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"IBANisValid"
})
public class ValidateIbanResponse implements Serializable{
	@JsonProperty("IBANisValid")
	private Boolean iBANisValid;

	@JsonProperty("IBANisValid")
	public Boolean getIBANisValid() {
		return iBANisValid;
	}

	@JsonProperty("IBANisValid")
	public void setIBANisValid(Boolean iBANisValid) {
		this.iBANisValid = iBANisValid;
	}
	
}




