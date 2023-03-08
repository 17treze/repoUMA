package it.tndigitale.a4gistruttoria.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "cuaa", "descrizioneImpresa", "tipoDomanda", "numeroDomanda", "annoCampagna", "ibanDomanda", "ibanFascicolo", "ibanValido" })
public class DatiDomandaIbanErrato {

	@JsonProperty("cuaa")
	private String cuaa;
	@JsonProperty("descrizioneImpresa")
	private String descrizioneImpresa;
	@JsonProperty("tipoDomanda")
	private String tipoDomanda;
	@JsonProperty("numeroDomanda")
	private Integer numeroDomanda;
	@JsonProperty("annoCampagna")
	private Integer annoCampagna;
	@JsonProperty("ibanDomanda")
	private String ibanDomanda;
	@JsonProperty("ibanFascicolo")
	private String ibanFascicolo;
	@JsonProperty("ibanValido")
	private Boolean ibanValido;

	@JsonProperty("cuaa")
	public String getCuaa() {
		return cuaa;
	}

	@JsonProperty("cuaa")
	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	@JsonProperty("descrizioneImpresa")
	public String getDescrizioneImpresa() {
		return descrizioneImpresa;
	}

	@JsonProperty("descrizioneImpresa")
	public void setDescrizioneImpresa(String descrizioneImpresa) {
		this.descrizioneImpresa = descrizioneImpresa;
	}

	@JsonProperty("tipoDomanda")
	public String getTipoDomanda() {
		return tipoDomanda;
	}

	@JsonProperty("tipoDomanda")
	public void setTipoDomanda(String tipoDomanda) {
		this.tipoDomanda = tipoDomanda;
	}

	@JsonProperty("numeroDomanda")
	public Integer getNumeroDomanda() {
		return numeroDomanda;
	}

	@JsonProperty("numeroDomanda")
	public void setNumeroDomanda(Integer numeroDomanda) {
		this.numeroDomanda = numeroDomanda;
	}

	@JsonProperty("annoCampagna")
	public Integer getAnnoCampagna() {
		return annoCampagna;
	}

	@JsonProperty("annoCampagna")
	public void setAnnoCampagna(Integer annoCampagna) {
		this.annoCampagna = annoCampagna;
	}

	@JsonProperty("ibanDomanda")
	public String getIbanDomanda() {
		return ibanDomanda;
	}

	@JsonProperty("ibanDomanda")
	public void setIbanDomanda(String ibanDomanda) {
		this.ibanDomanda = ibanDomanda;
	}

	@JsonProperty("ibanFascicolo")
	public String getIbanFascicolo() {
		return ibanFascicolo;
	}

	@JsonProperty("ibanFascicolo")
	public void setIbanFascicolo(String ibanFascicolo) {
		this.ibanFascicolo = ibanFascicolo;
	}
	
	@JsonProperty("ibanValido")
	public Boolean getIbanValido() {
		return ibanValido;
	}

	@JsonProperty("ibanValido")
	public void setIbanValido(Boolean ibanValido) {
		this.ibanValido = ibanValido;
	}
}