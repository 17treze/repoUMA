package it.tndigitale.a4gutente.repository.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;
import it.tndigitale.a4gutente.codici.CAA;


/**
 * The persistent class for the A4GT_ENTE database table.
 * 
 */
@Entity
@Table(name="A4GT_ENTE")
@NamedQuery(name="A4gtEnte.findAll", query="SELECT a FROM A4gtEnte a")
public class A4gtEnte extends EntitaDominio implements Serializable {
	private static final long serialVersionUID = 1L;

	private String descrizione;
	
	@Enumerated(EnumType.STRING)
	private CAA caa;

	private Long identificativo;

	public A4gtEnte() {
	}

	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Long getIdentificativo() {
		return this.identificativo;
	}

	public void setIdentificativo(Long identificativo) {
		this.identificativo = identificativo;
	}
	
	public CAA getCAA() {
		return  caa;
	}
}
	