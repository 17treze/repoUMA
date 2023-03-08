package it.tndigitale.a4g.territorio.dto;

import java.io.Serializable;
import java.util.List;

public class SottotipoConduzioneDto implements Serializable {
	private Long id;
	private String descrizione;
	private List<TipoDocumentoConduzioneDto> documenti;

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

	public List<TipoDocumentoConduzioneDto> getDocumenti() {
		return documenti;
	}

	public void setDocumenti(List<TipoDocumentoConduzioneDto> documenti) {
		this.documenti = documenti;
	}
}
