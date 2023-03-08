package it.tndigitale.a4gistruttoria.repository.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import it.tndigitale.a4gistruttoria.util.TipoDomandaCollegata;


/**
 * The persistent class for the A4GT_TRASMISSIONE_BDNA database table.
 * 
 */
@Entity
@Table(name="A4GT_TRASMISSIONE_BDNA")
@NamedQuery(name="A4gtTrasmissioneBdna.findAll", query="SELECT a FROM A4gtTrasmissioneBdna a")
public class A4gtTrasmissioneBdna extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="CF_OPERATORE", nullable=false, length=50)
	private String cfOperatore;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DT_CONFERMA")
	private Date dtConferma;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DT_CREAZIONE", nullable=false)
	private Date dtCreazione;
	
	@Column(name="TIPO_DOMANDA", length=50)
	@Enumerated(EnumType.STRING)
	private TipoDomandaCollegata tipoDomanda;

	//bi-directional many-to-one association to A4gtDomandeCollegate
	@OneToMany(mappedBy="a4gtTrasmissioneBdna")
	private List<A4gtDomandeCollegate> a4gtDomandeCollegates;

	public A4gtTrasmissioneBdna() {
	}

	public TipoDomandaCollegata getTipoDomanda() {
		return tipoDomanda;
	}

	public void setTipoDomanda(TipoDomandaCollegata tipoDomanda) {
		this.tipoDomanda = tipoDomanda;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCfOperatore() {
		return this.cfOperatore;
	}

	public void setCfOperatore(String cfOperatore) {
		this.cfOperatore = cfOperatore;
	}

	public Date getDtConferma() {
		return this.dtConferma;
	}

	public void setDtConferma(Date dtConferma) {
		this.dtConferma = dtConferma;
	}

	public Date getDtCreazione() {
		return this.dtCreazione;
	}

	public void setDtCreazione(Date dtCreazione) {
		this.dtCreazione = dtCreazione;
	}
	
	public List<A4gtDomandeCollegate> getA4gtDomandeCollegates() {
		return this.a4gtDomandeCollegates;
	}

	public void setA4gtDomandeCollegates(List<A4gtDomandeCollegate> a4gtDomandeCollegates) {
		this.a4gtDomandeCollegates = a4gtDomandeCollegates;
	}

	public A4gtDomandeCollegate addA4gtDomandeCollegate(A4gtDomandeCollegate a4gtDomandeCollegate) {
		getA4gtDomandeCollegates().add(a4gtDomandeCollegate);
		a4gtDomandeCollegate.setA4gtTrasmissioneBdna(this);

		return a4gtDomandeCollegate;
	}

	public A4gtDomandeCollegate removeA4gtDomandeCollegate(A4gtDomandeCollegate a4gtDomandeCollegate) {
		getA4gtDomandeCollegates().remove(a4gtDomandeCollegate);
		a4gtDomandeCollegate.setA4gtTrasmissioneBdna(null);

		return a4gtDomandeCollegate;
	}
}