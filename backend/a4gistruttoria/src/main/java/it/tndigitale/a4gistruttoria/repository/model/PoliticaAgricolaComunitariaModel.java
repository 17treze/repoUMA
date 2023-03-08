package it.tndigitale.a4gistruttoria.repository.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="A4GT_POLITICA_AGRICOLA")
@NamedQuery(name="PoliticaAgricolaComunitariaModel.findAll", query="SELECT a FROM PoliticaAgricolaComunitariaModel a")
public class PoliticaAgricolaComunitariaModel extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {

	@Column(name="ANNO_INIZIO")
	private Integer annoInizio;
	
	@Column(name="ANNO_FINE")
	private Integer annoFine;
	
	@Column(name="CODICE_PAC")
	private String codicePac;
	
	@Column(name="DESCRIZIONE_PAC")
	private String descrizionePac;

	public Integer getAnnoInizio() {
		return annoInizio;
	}

	public void setAnnoInizio(Integer annoInizio) {
		this.annoInizio = annoInizio;
	}

	public Integer getAnnoFine() {
		return annoFine;
	}

	public void setAnnoFine(Integer annoFine) {
		this.annoFine = annoFine;
	}

	public String getCodicePac() {
		return codicePac;
	}

	public void setCodicePac(String codicePac) {
		this.codicePac = codicePac;
	}

	public String getDescrizionePac() {
		return descrizionePac;
	}

	public void setDescrizionePac(String descrizionePac) {
		this.descrizionePac = descrizionePac;
	}
}
