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
 * The persistent class for the A4GT_ANOM_DOMANDA_SOSTEGNO database table.
 * 
 */
@Entity
@Table(name = "A4GT_ANOM_DOMANDA_SOSTEGNO")
@NamedQuery(name = "A4gtAnomDomandaSostegno.findAll", query = "SELECT a FROM A4gtAnomDomandaSostegno a")
public class A4gtAnomDomandaSostegno extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "CODICE_ANOMALIA")
	private String codiceAnomalia;

	private String esito;

	@Column(name = "LIVELLO_ANOMALIA")
	private String livelloAnomalia;

	// bi-directional many-to-one association to DomandaUnicaModel
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PASSO_LAVORAZIONE")
	private PassoTransizioneModel passoLavorazione;
	
	public A4gtAnomDomandaSostegno() {
	}

	public String getCodiceAnomalia() {
		return this.codiceAnomalia;
	}

	public void setCodiceAnomalia(String codiceAnomalia) {
		this.codiceAnomalia = codiceAnomalia;
	}

	public String getEsito() {
		return this.esito;
	}

	public void setEsito(String esito) {
		this.esito = esito;
	}

	public String getLivelloAnomalia() {
		return this.livelloAnomalia;
	}

	public void setLivelloAnomalia(String livelloAnomalia) {
		this.livelloAnomalia = livelloAnomalia;
	}

	public PassoTransizioneModel getPassoLavorazione() {
		return passoLavorazione;
	}

	public void setPassoLavorazione(PassoTransizioneModel passoLavorazione) {
		this.passoLavorazione = passoLavorazione;
	}

}