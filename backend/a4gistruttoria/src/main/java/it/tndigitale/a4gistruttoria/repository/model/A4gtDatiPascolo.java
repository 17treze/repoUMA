package it.tndigitale.a4gistruttoria.repository.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the A4GT_DATI_PASCOLO database table.
 * 
 */
@Entity
@Table(name="A4GT_DATI_PASCOLO")
@NamedQuery(name="A4gtDatiPascolo.findAll", query="SELECT a FROM A4gtDatiPascolo a")
public class A4gtDatiPascolo extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="CODICE_PASCOLO")
	private String codicePascolo;

	@Column(name="DESCRIZIONE_PASCOLO")
	private String descrizionePascolo;

	@Lob
	@Column(name="PARTICELLE_CATASTALI")
	private String particelleCatastali;

	@Column(name="UBA_DICHIARATE")
	private BigDecimal ubaDichiarate;

	//bi-directional many-to-one association to DomandaUnicaModel
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_DOMANDA")
	private DomandaUnicaModel domandaUnicaModel;

	public A4gtDatiPascolo() {
	}

	public String getCodicePascolo() {
		return this.codicePascolo;
	}

	public void setCodicePascolo(String codicePascolo) {
		this.codicePascolo = codicePascolo;
	}

	public String getDescrizionePascolo() {
		return this.descrizionePascolo;
	}

	public void setDescrizionePascolo(String descrizionePascolo) {
		this.descrizionePascolo = descrizionePascolo;
	}

	public String getParticelleCatastali() {
		return this.particelleCatastali;
	}

	public void setParticelleCatastali(String particelleCatastali) {
		this.particelleCatastali = particelleCatastali;
	}

	public BigDecimal getUbaDichiarate() {
		return this.ubaDichiarate;
	}

	public void setUbaDichiarate(BigDecimal ubaDichiarate) {
		this.ubaDichiarate = ubaDichiarate;
	}

	public DomandaUnicaModel getDomandaUnicaModel() {
		return this.domandaUnicaModel;
	}

	public void setDomandaUnicaModel(DomandaUnicaModel domandaUnicaModel) {
		this.domandaUnicaModel = domandaUnicaModel;
	}

}