package it.tndigitale.a4g.fascicolo.mediator.dto;

import java.io.Serializable;
import java.time.LocalDate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.StatoFascicoloEnum;

@ApiModel(description = "Dati anagrafici generali del fascicolo ")
public class FascicoloDto implements Serializable {
	
	private static final long serialVersionUID = -4652562617910293029L;
	
	@ApiModelProperty(value = "Identificativo fascicolo")
	private Long id;
	
	@ApiModelProperty(value = "Codice fiscale azienda agricola")
	private String cuaa;
	
	@ApiModelProperty(value = "Denominazione Impresa")
	private String denominazione;
	
	@ApiModelProperty(value = "Stato del fascicolo")
	private StatoFascicoloEnum stato;
	
	@ApiModelProperty(value = "Stato del fascicolo")
	private OrganismoPagatoreEnum organismoPagatore;

	@ApiModelProperty(value = "Dati del Mandato di un fascicolo ")
	private DetenzioneDto detenzione;
	
	@ApiModelProperty(value = "Data apertura del fascicolo")
	private LocalDate dataApertura;
	
	@ApiModelProperty(value = "Data validazione del fascicolo")
	private LocalDate dataValidazione;
	
	@ApiModelProperty(value = "Data ultima modifica del fascicolo")
	private LocalDate dataModifica;

	@ApiModelProperty(value = "Data ultima validazione del fascicolo")
	private LocalDate dataUltimaValidazione;
	
	private Boolean cameraCommercioCodiceFiscaleDifferente;
	
	public StatoFascicoloEnum getStato() {
		return stato;
	}
	
	public void setStato(StatoFascicoloEnum stato) {
		this.stato = stato;
	}
	
	public String getCuaa() {
		return cuaa;
	}
	
	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getDenominazione() {
		return denominazione;
	}
	
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}
	
	public OrganismoPagatoreEnum getOrganismoPagatore() {
		return organismoPagatore;
	}
	
	public void setOrganismoPagatore(OrganismoPagatoreEnum organismoPagatore) {
		this.organismoPagatore = organismoPagatore;
	}
	
	public DetenzioneDto getDetenzione() {
		return detenzione;
	}
	
	public void setDetenzione(DetenzioneDto detenzione) {
		this.detenzione = detenzione;
	}
	
	public LocalDate getDataApertura() {
		return dataApertura;
	}
	
	public void setDataApertura(LocalDate dataApertura) {
		this.dataApertura = dataApertura;
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

	public LocalDate getDataUltimaValidazione() {
		return dataUltimaValidazione;
	}

	public void setDataUltimaValidazione(LocalDate dataUltimaValidazione) {
		this.dataUltimaValidazione = dataUltimaValidazione;
	}

	public Boolean getCameraCommercioCodiceFiscaleDifferente() {
		return cameraCommercioCodiceFiscaleDifferente;
	}
	
	public void setCameraCommercioCodiceFiscaleDifferente(Boolean cameraCommercioCodiceFiscaleDifferente) {
		this.cameraCommercioCodiceFiscaleDifferente = cameraCommercioCodiceFiscaleDifferente;
	}
}
