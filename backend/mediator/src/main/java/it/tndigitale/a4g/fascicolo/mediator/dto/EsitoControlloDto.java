package it.tndigitale.a4g.fascicolo.mediator.dto;

import java.io.Serializable;
import java.util.List;

public class EsitoControlloDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer esito;
//	idControllo viene solitamente usato da AGS per i warning / informazioni da inserire in scheda di validazione.
//	Negli altri casi è nullo
	private Long idControllo;

	private List<SegnalazioneDto> segnalazioni;

	public Integer getEsito() {
		return esito;
	}

	public void setEsito(Integer esito) {
		this.esito = esito;
	}

	public Long getIdControllo() {
		return idControllo;
	}

	public void setIdControllo(Long idControllo) {
		this.idControllo = idControllo;
	}

	public List<SegnalazioneDto> getSegnalazioni() {
		return segnalazioni;
	}

	public void setSegnalazioni(List<SegnalazioneDto> segnalazioni) {
		this.segnalazioni = segnalazioni;
	}
}
