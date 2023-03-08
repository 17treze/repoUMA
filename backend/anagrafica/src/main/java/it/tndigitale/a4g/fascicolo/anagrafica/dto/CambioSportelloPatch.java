package it.tndigitale.a4g.fascicolo.anagrafica.dto;

import java.time.LocalDate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Dati a supporto della modifica della sede operativa del CAA mandatario  ")
public class CambioSportelloPatch {
	
	@ApiModelProperty(value = "identificativo nuovo sportello")
	private Long idNuovoSportello;
	@ApiModelProperty(value = "Data inizio validità nuovo sportello")
	private LocalDate dataCambio;
	@ApiModelProperty(value = "Motivazione al cambiamento")
	private String motivazione;
	
	public Long getIdNuovoSportello() {
		return idNuovoSportello;
	}

	public void setIdNuovoSportello(Long idNuovoSportello) {
		this.idNuovoSportello = idNuovoSportello;
	}

	public LocalDate getDataCambio() {
		return dataCambio;
	}

	public void setDataCambio(LocalDate dataCambio) {
		this.dataCambio = dataCambio;
	}

	public String getMotivazione() {
		return motivazione;
	}

	public void setMotivazione(String motivazione) {
		this.motivazione = motivazione;
	}

	@Override
	public String toString() {
		return "CambioSportelloPatch [idNuovoSportello=" + idNuovoSportello + ", dataCambio=" + dataCambio + ", motivazione=" + motivazione + "]";
	}
	

}
