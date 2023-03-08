package it.tndigitale.a4gistruttoria.repository.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the A4GT_RICEVUTA_DOM_INT_ZOOT database table.
 * 
 */
@Entity
@Table(name = "A4GT_RICEVUTA_DOM_INT_ZOOT")
@NamedQuery(name = "A4gtRicevutaDomIntZoot.findAll", query = "SELECT a FROM A4gtRicevutaDomIntZoot a")
public class A4gtRicevutaDomIntZoot extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Lob
	@Column(nullable=false)
	private byte[] ricevuta;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_ULTIMO_AGGIORNAMENTO")
	private Date dtUltimoAggiornamento;

	// bi-directional one-to-one association to DomandaUnicaModel
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_DOMANDA")
	private DomandaUnicaModel domandaUnicaModel;

	public A4gtRicevutaDomIntZoot() {
	}

	public byte[] getRicevuta() {
		return ricevuta;
	}

	public void setRicevuta(byte[] ricevuta) {
		this.ricevuta = ricevuta;
	}

	public Date getDtUltimoAggiornamento() {
		return dtUltimoAggiornamento;
	}

	public void setDtUltimoAggiornamento(Date dtUltimoAggiornamento) {
		this.dtUltimoAggiornamento = dtUltimoAggiornamento;
	}

	public DomandaUnicaModel getDomandaUnicaModel() {
		return domandaUnicaModel;
	}

	public void setDomandaUnicaModel(DomandaUnicaModel domandaUnicaModel) {
		this.domandaUnicaModel = domandaUnicaModel;
	}


}