package it.tndigitale.a4g.ags.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "idRegione", "denominazione", "codiceIstat" })
public class RegioneCatastale implements Serializable {

	@JsonProperty("idRegione")
	public Integer idRegione;
	@JsonProperty("denominazione")
	public String denominazione;
	@JsonProperty("codiceIstat")
	public String codiceIstat;
	private static final long serialVersionUID = -1202626955467944302L;

	public RegioneCatastale withIdRegione(Integer idRegione) {
		this.idRegione = idRegione;
		return this;
	}

	public RegioneCatastale withDenominazione(String denominazione) {
		this.denominazione = denominazione;
		return this;
	}

	public RegioneCatastale withCodiceIstat(String codiceIstat) {
		this.codiceIstat = codiceIstat;
		return this;
	}

}