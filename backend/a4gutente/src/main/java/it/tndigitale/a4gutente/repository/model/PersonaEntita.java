package it.tndigitale.a4gutente.repository.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name = "A4GT_PERSONA")
public class PersonaEntita extends EntitaDominio implements Serializable {

	private static final long serialVersionUID = -5981589950559948009L;

	@Column(name = "CODICE_FISCALE")
	private String codiceFiscale;

	private String cognome;

	private String nome;

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PersonaEntita that = (PersonaEntita) o;
		return Objects.equals(codiceFiscale, that.codiceFiscale) &&
				Objects.equals(cognome, that.cognome) &&
				Objects.equals(nome, that.nome);
	}

	@Override
	public int hashCode() {
		return Objects.hash(codiceFiscale, cognome, nome);
	}
}
