package it.tndigitale.a4g.fascicolo.mediator.dto;

import java.io.Serializable;

public class SegnalazioneDto implements Serializable {
	
	
	private static final long serialVersionUID = 3216785941959536554L;
	
	private String descrizione;
	private TipoSegnalazioneEnum tipo;

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public TipoSegnalazioneEnum getTipo() {
		return tipo;
	}

	public void setTipo(TipoSegnalazioneEnum tipo) {
		this.tipo = tipo;
	}
}
