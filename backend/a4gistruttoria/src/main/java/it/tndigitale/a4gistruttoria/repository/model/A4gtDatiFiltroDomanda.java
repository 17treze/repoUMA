package it.tndigitale.a4gistruttoria.repository.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the A4GT_DATI_FILTRO_DOMANDA database table.
 * 
 */
@Entity
@Table(name = "A4GT_DATI_FILTRO_DOMANDA")
@NamedQuery(name = "A4gtDatiFiltroDomanda.findAll", query = "SELECT a FROM A4gtDatiFiltroDomanda a")
public class A4gtDatiFiltroDomanda extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "TIPO_FILTRO")
	private String tipoFiltro;
	@Column(name = "VALORE")
	private String valore;

	// bi-directional many-to-one association to DomandaUnicaModel
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_DOMANDA")
	private DomandaUnicaModel domandaUnicaModel;

	public A4gtDatiFiltroDomanda() {
	}

	public String getTipoFiltro() {
		return this.tipoFiltro;
	}

	public void setTipoFiltro(String tipoFiltro) {
		this.tipoFiltro = tipoFiltro;
	}

	public String getValore() {
		return this.valore;
	}

	public void setValore(String valore) {
		this.valore = valore;
	}

	public DomandaUnicaModel getDomandaUnicaModel() {
		return this.domandaUnicaModel;
	}

	public void setDomandaUnicaModel(DomandaUnicaModel domandaUnicaModel) {
		this.domandaUnicaModel = domandaUnicaModel;
	}

}