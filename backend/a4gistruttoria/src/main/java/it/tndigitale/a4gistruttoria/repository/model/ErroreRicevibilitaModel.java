package it.tndigitale.a4gistruttoria.repository.model;

import it.tndigitale.a4gistruttoria.dto.domandaunica.ErroreControlloRicevibilitaDomanda;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name="A4GT_ERRORE_RICEVIBILITA")
@NamedQuery(name="ErroreRicevibilitaModel.findAll", query="SELECT a FROM ErroreRicevibilitaModel a")
public class ErroreRicevibilitaModel extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {
	private static final long serialVersionUID = -1945947575883525703L;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_ESECUZIONE")
	private Date dtEsecuzione;

	@Column(name="TIPOLOGIA_CONTROLLO")
	@Enumerated(EnumType.STRING)
	private ErroreControlloRicevibilitaDomanda tipologiaControllo;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_DOMANDA")
	private DomandaUnicaModel domandaUnicaModel;

	public Date getDtEsecuzione() {
		return this.dtEsecuzione;
	}

	public void setDtEsecuzione(Date dtEsecuzione) {
		this.dtEsecuzione = dtEsecuzione;
	}

	public DomandaUnicaModel getDomandaUnicaModel() {
		return this.domandaUnicaModel;
	}

	public void setDomandaUnicaModel(DomandaUnicaModel domandaUnicaModel) {
		this.domandaUnicaModel = domandaUnicaModel;
	}

	public ErroreControlloRicevibilitaDomanda getTipologiaControllo() {
		return tipologiaControllo;
	}

	public void setTipologiaControllo(ErroreControlloRicevibilitaDomanda tipologiaControllo) {
		this.tipologiaControllo = tipologiaControllo;
	}


}