package it.tndigitale.a4g.fascicolo.mediator.dto;

import java.io.Serializable;
import java.time.LocalDate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Detenzione di un fascicolo ")
public class DetenzioneDto implements Serializable {

	private static final long serialVersionUID = -3671223164827549257L;

	@ApiModelProperty(value = "Id detenzione")
	private Long id;
	@ApiModelProperty(value = "Denominazione fascicolo")
	private String denominazione;
	@ApiModelProperty(value = "Data inizio detenzione")
	private LocalDate dataInizio;
	@ApiModelProperty(value = "Dati fine detenzione")
	private LocalDate dataFine;
	@ApiModelProperty(value = "Codice fiscale")
	private String codiceFiscale;
	
	@ApiModelProperty(value = "Tipo di detenzione del fascicolo (e.g. 'mandato' oppure 'detenzione in proprio')")
	private TipoDetenzioneEnum tipoDetenzione;

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
	public LocalDate getDataInizio() {
		return dataInizio;
	}
	public void setDataInizio(LocalDate dataInizio) {
		this.dataInizio = dataInizio;
	}
	public LocalDate getDataFine() {
		return dataFine;
	}
	public void setDataFine(LocalDate dataFine) {
		this.dataFine = dataFine;
	}
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public TipoDetenzioneEnum getTipoDetenzione() {
		return tipoDetenzione;
	}
	public void setTipoDetenzione(TipoDetenzioneEnum tipoDetenzione) {
		this.tipoDetenzione = tipoDetenzione;
	}
	
}
