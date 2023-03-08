package it.tndigitale.a4gutente.repository.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;


/**
 * The persistent class for the A4GR_UTENTE_AZIENDA database table.
 * 
 */
@Entity
@Table(name="A4GR_UTENTE_AZIENDA")
@NamedQuery(name="A4grUtenteAzienda.findAll", query="SELECT a FROM A4grUtenteAzienda a")
public class A4grUtenteAzienda extends EntitaDominio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="DATA_AGGIORNAMENTO")
	private LocalDateTime dataAggiornamento;

	@Column(name="ID_CARICA")
	private Long idCarica;

	
	@Column(name="CUAA")
	private String cuaa;

	//bi-directional many-to-one association to A4gtUtente
	@ManyToOne
	@JoinColumn(name = "ID_UTENTE", nullable = false)
	private A4gtUtente a4gtUtente;

	public A4grUtenteAzienda() {
	}

	public LocalDateTime getDataAggiornamento() {
		return this.dataAggiornamento;
	}

	public void setDataAggiornamento(LocalDateTime dataAggiornamento) {
		this.dataAggiornamento = dataAggiornamento;
	}

	public Long getIdCarica() {
		return this.idCarica;
	}

	public void setIdCarica(Long idCarica) {
		this.idCarica = idCarica;
	}

	public A4gtUtente getA4gtUtente() {
		return this.a4gtUtente;
	}

	public void setA4gtUtente(A4gtUtente a4gtUtente) {
		this.a4gtUtente = a4gtUtente;
	}
	
	public String getCuaa() {
		return cuaa;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

}
