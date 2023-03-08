package it.tndigitale.a4gistruttoria.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4gistruttoria.util.TipologiaSincronizzazioneAGEA;

@ApiModel(description = "Rappresenta le possibili sincronizzazioni verso AGEA gestite dal sistema")
public class SincronizzazioneFilter implements Serializable {
	
	private static final long serialVersionUID = -1248504037515238190L;

	@ApiParam(
		value = "Lista delle possibili sincronizzazioni con AGEA che e' possibile richiedere al sistema", 
		required = true)
	private TipologiaSincronizzazioneAGEA tipologiaSincronizzazione;
	
	@ApiParam(
		value = "Anno di campagna", 
		required = true)
	private Integer campagna;
	
	public TipologiaSincronizzazioneAGEA getTipologiaSincronizzazione() {
		return tipologiaSincronizzazione;
	}
	
	public void setTipologiaSincronizzazione(
		TipologiaSincronizzazioneAGEA tipologiaSincronizzazione) {
		this.tipologiaSincronizzazione = tipologiaSincronizzazione;
	}
	
	public Integer getCampagna() {
		return campagna;
	}
	
	public void setCampagna(Integer campagna) {
		this.campagna = campagna;
	}
}
