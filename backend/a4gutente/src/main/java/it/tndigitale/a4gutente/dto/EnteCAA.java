package it.tndigitale.a4gutente.dto;

import java.io.Serializable;
import java.util.List;

public class EnteCAA implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Long codice;
	private String descrizione;
	private List<EnteSede> sedi;

	public Long getId() {
		return id;
	}

	public EnteCAA setId(Long id) {
		this.id = id;
		return this;
	}

	public Long getCodice() {
		return codice;
	}

	public EnteCAA setCodice(Long codice) {
		this.codice = codice;
		return this;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public EnteCAA setDescrizione(String descrizione) {
		this.descrizione = descrizione;
		return this;
	}

	public List<EnteSede> getSedi() {
		return sedi;
	}

	public EnteCAA setSedi(List<EnteSede> sedi) {
		this.sedi = sedi;
		return this;
	}
}
