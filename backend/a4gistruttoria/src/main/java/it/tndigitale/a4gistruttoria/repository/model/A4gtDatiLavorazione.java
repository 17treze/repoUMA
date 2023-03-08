package it.tndigitale.a4gistruttoria.repository.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the A4GT_DATI_LAVORAZIONE database table.
 * 
 */
@Entity
@Table(name="A4GT_DATI_LAVORAZIONE")
@NamedQuery(name="A4gtDatiLavorazione.findAll", query="SELECT a FROM A4gtDatiLavorazione a")
public class A4gtDatiLavorazione extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Lob
	@Column(name="JSON_DATI_RICEVIBILITA")
	private String jsonDatiRicevibilita;

	@Lob
	@Column(name="JSON_DATI_SINTESI_IMPEGNI")
	private String jsonDatiSintesiImpegni;

	//bi-directional many-to-one association to DomandaUnicaModel
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_DOMANDA")
	private DomandaUnicaModel domandaUnicaModel;

	public A4gtDatiLavorazione() {
	}

	public String getJsonDatiRicevibilita() {
		return this.jsonDatiRicevibilita;
	}

	public void setJsonDatiRicevibilita(String jsonDatiRicevibilita) {
		this.jsonDatiRicevibilita = jsonDatiRicevibilita;
	}

	public String getJsonDatiSintesiImpegni() {
		return this.jsonDatiSintesiImpegni;
	}

	public void setJsonDatiSintesiImpegni(String jsonDatiSintesiImpegni) {
		this.jsonDatiSintesiImpegni = jsonDatiSintesiImpegni;
	}
	
	public DomandaUnicaModel getDomandaUnicaModel() {
		return this.domandaUnicaModel;
	}

	public void setDomandaUnicaModel(DomandaUnicaModel domandaUnicaModel) {
		this.domandaUnicaModel = domandaUnicaModel;
	}

}