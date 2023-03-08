package it.tndigitale.a4g.framework.component.dto;

import java.io.Serializable;
import java.util.List;

public class EsitoControlloDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer esito;
	private Long idControllo;
	private List<SegnalazioneDto> segnalazioni;

	public EsitoControlloDto() {
		super();
	}

	public EsitoControlloDto(Integer esito, Long idControllo, List<SegnalazioneDto> segnalazioni) {
		super();
		this.esito = esito;
		this.idControllo = idControllo;
		this.segnalazioni = segnalazioni;
	}

	public List<SegnalazioneDto> getSegnalazioni() {
		return segnalazioni;
	}

	public void setSegnalazioni(List<SegnalazioneDto> segnalazioni) {
		this.segnalazioni = segnalazioni;
	}

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

}
