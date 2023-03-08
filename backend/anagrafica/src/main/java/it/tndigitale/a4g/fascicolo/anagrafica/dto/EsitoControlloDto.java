package it.tndigitale.a4g.fascicolo.anagrafica.dto;

import java.io.Serializable;

public class EsitoControlloDto implements Serializable {
	private Integer esito;
	//	idControllo viene solitamente usato da AGS per i warning / informazioni da inserire in scheda di validazione.
	//	Negli altri casi è nullo
	private Long idControllo;

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
