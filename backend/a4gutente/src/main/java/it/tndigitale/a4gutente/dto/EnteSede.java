package it.tndigitale.a4gutente.dto;

import it.tndigitale.a4gutente.codici.CAA;

import java.io.Serializable;
import java.util.Objects;

public class EnteSede implements Serializable {

	private static final long serialVersionUID = 5315994563375406475L;
	private Long id;
	private String descrizione;
	private Long identificativo;
	private CAA caa;

	public Long getId() {
		return id;
	}

	public EnteSede setId(Long id) {
		this.id = id;
		return this;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public EnteSede setDescrizione(String descrizione) {
		this.descrizione = descrizione;
		return this;
	}

	public Long getIdentificativo() {
		return identificativo;
	}

	public EnteSede setIdentificativo(Long identificativo) {
		this.identificativo = identificativo;
		return this;
	}

	public CAA getCaa() {
		return caa;
	}

	public EnteSede setCaa(CAA caa) {
		this.caa = caa;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		EnteSede enteSede = (EnteSede) o;
		return Objects.equals(id, enteSede.id) &&
				Objects.equals(descrizione, enteSede.descrizione) &&
				Objects.equals(identificativo, enteSede.identificativo) &&
				caa == enteSede.caa;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, descrizione, identificativo, caa);
	}
}
