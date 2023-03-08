package it.tndigitale.a4g.proxy.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.tndigitale.a4g.proxy.config.LocalDateTimeSerializerDefault;

@ApiModel(description = "Rappresenta gli attributi di un esito antimafia legato a un CUAA")
public class AntimafiaEsitoDto {
	@ApiModelProperty(value = "Valore dell'esito")
	private String esitoTrasmissione;

	@ApiModelProperty(value = "Codice Fiscale legato all'esito antimafia")
	private String cuaa;
	
	@JsonSerialize(using = LocalDateTimeSerializerDefault.class)
	@ApiModelProperty(value = "Data di inizio validità della dichiarazione antimafia")
	private LocalDateTime dtValidita;
	
	@JsonSerialize(using = LocalDateTimeSerializerDefault.class)
	@ApiModelProperty(value = "Data di elaborazione dell'esito")
	private LocalDateTime dtElaborazione;
	
	@ApiModelProperty(value = "Descrizione dell'esito antimafia")
	private String descrizioneEsito;
	
	@ApiModelProperty(value = "Esito invio ad AGEA")
	private String esitoInvioAgea;
	
	@ApiModelProperty(value = "Esito invio a BDNA")
	private String esitoInvioBdna;

	public String getEsitoTrasmissione() {
		return esitoTrasmissione;
	}

	public void setEsitoTrasmissione(String esitoTrasmissione) {
		this.esitoTrasmissione = esitoTrasmissione;
	}

	public String getDescrizioneEsito() {
		return descrizioneEsito;
	}

	public void setDescrizioneEsito(String descrizioneEsito) {
		this.descrizioneEsito = descrizioneEsito;
	}

	public String getEsitoInvioAgea() {
		return esitoInvioAgea;
	}

	public void setEsitoInvioAgea(String esitoInvioAgea) {
		this.esitoInvioAgea = esitoInvioAgea;
	}

	public String getEsitoInvioBdna() {
		return esitoInvioBdna;
	}

	public void setEsitoInvioBdna(String esitoInvioBdna) {
		this.esitoInvioBdna = esitoInvioBdna;
	}

	public LocalDateTime getDtElaborazione() {
		return dtElaborazione;
	}

	public void setDtElaborazione(LocalDateTime dtElaborazione) {
		this.dtElaborazione = dtElaborazione;
	}

	public String getCuaa() {
		return cuaa;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
}

	public LocalDateTime getDtValidita() {
		return dtValidita;
	}

	public void setDtValidita(LocalDateTime dtValidita) {
		this.dtValidita = dtValidita;
	}
}
