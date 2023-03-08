package it.tndigitale.a4g.proxy.dto.fascicolo.sian;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.tndigitale.a4g.proxy.config.LocalDateTimeSerializerDefault;

@ApiModel(description = "DTO del fascicolo recuperato dal SIAN")
public class FascicoloSian implements Serializable{

	private static final long serialVersionUID = 1685849890954852006L;
	
	@ApiModelProperty(value = "Codice fiscale azienda agricola", required = true)
	private String cuaa;
	@ApiModelProperty(value = "data Apertura Fascicolo")
	@JsonSerialize(using = LocalDateTimeSerializerDefault.class)
	private LocalDateTime dataAperturaFascicolo;
	@ApiModelProperty(value = "data Chiusura Fascicolo")
	@JsonSerialize(using = LocalDateTimeSerializerDefault.class)
	private LocalDateTime dataChiusuraFascicolo;
	@ApiModelProperty(value = "data Validazione Fascicolo")
	@JsonSerialize(using = LocalDateTimeSerializerDefault.class)
	private LocalDateTime dataValidazFascicolo;
	@ApiModelProperty(value = "Codice di riconoscimento della tipologia del detentore")
	private TipoDetentore tipoDetentore;
	@ApiModelProperty(value = "Codice di identificazione dell’ufficio presso cui è custodito il fascicolo cartaceo")
	private String detentore;
	@ApiModelProperty(value = "Codice di identificazione dell’OP competente (FEOGA)")
	private OrganismoPagatore organismoPagatore;
	
	public String getCuaa() {
		return cuaa;
	}
	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}
	public LocalDateTime getDataAperturaFascicolo() {
		return dataAperturaFascicolo;
	}
	public void setDataAperturaFascicolo(LocalDateTime dataAperturaFascicolo) {
		this.dataAperturaFascicolo = dataAperturaFascicolo;
	}
	public LocalDateTime getDataChiusuraFascicolo() {
		return dataChiusuraFascicolo;
	}
	public void setDataChiusuraFascicolo(LocalDateTime dataChiusuraFascicolo) {
		this.dataChiusuraFascicolo = dataChiusuraFascicolo;
	}
	public LocalDateTime getDataValidazFascicolo() {
		return dataValidazFascicolo;
	}
	public void setDataValidazFascicolo(LocalDateTime dataValidazFascicolo) {
		this.dataValidazFascicolo = dataValidazFascicolo;
	}
	public TipoDetentore getTipoDetentore() {
		return tipoDetentore;
	}
	public void setTipoDetentore(TipoDetentore tipoDetentore) {
		this.tipoDetentore = tipoDetentore;
	}
	public String getDetentore() {
		return detentore;
	}
	public void setDetentore(String detentore) {
		this.detentore = detentore;
	}
	public OrganismoPagatore getOrganismoPagatore() {
		return organismoPagatore;
	}
	public void setOrganismoPagatore(OrganismoPagatore organismoPagatore) {
		this.organismoPagatore = organismoPagatore;
	}
	
}
