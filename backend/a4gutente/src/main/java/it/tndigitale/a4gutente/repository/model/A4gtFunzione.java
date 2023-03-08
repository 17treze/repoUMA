package it.tndigitale.a4gutente.repository.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;


/**
 * The persistent class for the A4GT_FUNZIONE database table.
 * 
 */
@Entity
@Table(name="A4GT_FUNZIONE")
@NamedQuery(name="A4gtFunzione.findAll", query="SELECT a FROM A4gtFunzione a")
public class A4gtFunzione extends EntitaDominio implements Serializable {
	private static final long serialVersionUID = 1L;

	private String descrizione;

	private String identificativo;

	public A4gtFunzione() {
	}

	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getIdentificativo() {
		return this.identificativo;
	}

	public void setIdentificativo(String identificativo) {
		this.identificativo = identificativo;
	}
}