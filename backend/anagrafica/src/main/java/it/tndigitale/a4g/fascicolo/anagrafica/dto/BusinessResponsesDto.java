package it.tndigitale.a4g.fascicolo.anagrafica.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Classe per memorizzare errori non bloccanti")
public class BusinessResponsesDto implements Serializable {
	
	public enum Esiti {
		OK,
		KO,
		NON_BLOCCANTE
	}
	
	@ApiModelProperty(value = "esito dell'operazione")
	private Esiti esito = Esiti.OK;
	
	@ApiModelProperty(value = "elenco esiti business")
	private List<String> responseList = new ArrayList<String>();

	public Esiti getEsito() {
		return esito;
	}

	public BusinessResponsesDto setEsito(Esiti esito) {
		this.esito = esito;
		return this;
	}

	public List<String> getResponseList() {
		return responseList;
	}

	public BusinessResponsesDto setResponseList(List<String> responseList) {
		this.responseList = responseList;
		return this;
	}
	
	
	
}
