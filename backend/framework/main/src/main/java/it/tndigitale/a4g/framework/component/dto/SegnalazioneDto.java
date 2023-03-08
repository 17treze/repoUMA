package it.tndigitale.a4g.framework.component.dto;

import java.io.Serializable;

public class SegnalazioneDto implements Serializable {

	private static final long serialVersionUID = 1L;

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

	public SegnalazioneDto(String descrizione, TipoSegnalazioneEnum tipo) {
		super();
		this.descrizione = descrizione;
		this.tipo = tipo;
	}

}
