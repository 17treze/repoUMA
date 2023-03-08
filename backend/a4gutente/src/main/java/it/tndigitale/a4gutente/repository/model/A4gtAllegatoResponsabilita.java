package it.tndigitale.a4gutente.repository.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;
import it.tndigitale.a4gutente.codici.CodResponsabilita;

@Entity
@Table(name = "A4GT_ALLEGATO_RESPONSABILITA")
@NamedQuery(name = "A4gtAllegatoResponsabilita.findAll", query = "SELECT a FROM A4gtAllegatoResponsabilita a")
public class A4gtAllegatoResponsabilita extends EntitaDominio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.DATE)
	@Column(name = "DT_INSERIMENTO")
	private Date dtInserimento;

	@Lob
	@Column(name = "ALLEGATO")
	private byte[] allegato;

	@Column(name = "COD_RESPONSABILITA", length = 50)
	@Enumerated(EnumType.STRING)
	private CodResponsabilita codResponsabilita;

	@Column(name = "ID_RESPONSABILITA")
	private Long idResponsabilita;
	
	// non bi-directional, serve per poter inserire allegati al salvataggio della domanda
	//@Column(name = "ID_DOMANDA_REGISTRAZIONE", insertable = false, updatable = false)
	//private Long idDomandaRegistrazione;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_DOMANDA_REGISTRAZIONE", nullable = false)
	DomandaRegistrazioneUtente domandaRegistrazione;
/*
	/*public Long getIdDomandaRegistrazione() {
		return idDomandaRegistrazione;
	}

	public void setIdDomandaRegistrazione(Long idDomandaRegistrazione) {
		this.idDomandaRegistrazione = idDomandaRegistrazione;
	}*/

	public DomandaRegistrazioneUtente getDomandaRegistrazione() {
		return domandaRegistrazione;
	}

	public void setDomandaRegistrazione(DomandaRegistrazioneUtente domandaRegistrazione) {
		this.domandaRegistrazione = domandaRegistrazione;
	}

	public A4gtAllegatoResponsabilita() {
	}

	public Date getDtInserimento() {
		return dtInserimento;
	}

	public void setDtInserimento(Date dtInserimento) {
		this.dtInserimento = dtInserimento;
	}

	public byte[] getAllegato() {
		return allegato;
	}

	public void setAllegato(byte[] allegato) {
		this.allegato = allegato;
	}

	public CodResponsabilita getCodResponsabilita() {
		return codResponsabilita;
	}

	public void setCodResponsabilita(CodResponsabilita codResponsabilita) {
		this.codResponsabilita = codResponsabilita;
	}

	public Long getIdResponsabilita() {
		return idResponsabilita;
	}

	public void setIdResponsabilita(Long idResponsabilita) {
		this.idResponsabilita = idResponsabilita;
	}

	/*public DomandaRegistrazioneUtente getDomandaRegistrazioneUtente() {
		return domandaRegistrazioneUtente;
	}

	public void setDomandaRegistrazioneUtente(DomandaRegistrazioneUtente domandaRegistrazioneUtente) {
		this.domandaRegistrazioneUtente = domandaRegistrazioneUtente;
	}*/
}
