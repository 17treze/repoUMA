package it.tndigitale.a4g.fascicolo.anagrafica.dto;

import java.io.Serializable;
import java.time.LocalDate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Descrizione di un fascicolo validato")
public class ValidazioneFascicoloDto implements Serializable {
	private static final long serialVersionUID = 8157889358051189749L;

	@ApiModelProperty(value = "Identificativo fascicolo")
	private Long id;
	
	@ApiModelProperty(value = "Identificativo validazione")
	private Integer idValidazione;
	
	@ApiModelProperty(value = "Codice fiscale azienda agricola")
	private String cuaa;
	
	@ApiModelProperty(value = "Data validazione del fascicolo")
	private LocalDate dataValidazione;
	
	@ApiModelProperty(value = "Data aggiornamento del fascicolo")
	private LocalDate dataModifica;
	
	@ApiModelProperty(value = "Denominazione Sportello")
	private String denominazioneSportello;

	@ApiModelProperty(value = "Identificativo utente che ha effettuato la validazione")
	private String utenteValidazione;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getIdValidazione() {
		return idValidazione;
	}

	public void setIdValidazione(Integer idValidazione) {
		this.idValidazione = idValidazione;
	}

	public String getCuaa() {
		return cuaa;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	public LocalDate getDataValidazione() {
		return dataValidazione;
	}

	public void setDataValidazione(LocalDate dataValidazione) {
		this.dataValidazione = dataValidazione;
	}

	public LocalDate getDataModifica() {
		return dataModifica;
	}

	public void setDataModifica(LocalDate dataModifica) {
		this.dataModifica = dataModifica;
	}

	public String getDenominazioneSportello() {
		return denominazioneSportello;
	}

	public void setDenominazioneSportello(String denominazioneSportello) {
		this.denominazioneSportello = denominazioneSportello;
	}
	
	public String getUtenteValidazione() {
		return utenteValidazione;
	}

	public void setUtenteValidazione(String utenteValidazione) {
		this.utenteValidazione = utenteValidazione;
	}
}
