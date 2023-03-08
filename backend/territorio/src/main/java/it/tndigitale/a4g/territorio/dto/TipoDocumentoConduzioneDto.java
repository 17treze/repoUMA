package it.tndigitale.a4g.territorio.dto;

import java.io.Serializable;

public class TipoDocumentoConduzioneDto implements Serializable {

	private Long id; 
	private String descrizione;
 
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
}
