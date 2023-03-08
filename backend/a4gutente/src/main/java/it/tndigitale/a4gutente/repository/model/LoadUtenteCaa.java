package it.tndigitale.a4gutente.repository.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Embeddable
@Table(name = "A4GT_LOAD_UTENTE_CAA")
public class LoadUtenteCaa implements Serializable {

	private static final long serialVersionUID = 3086812550655827127L;

	@EmbeddedId LoadUtenteCaaPK id;
	
	@Column(name = "COGNOME")
	private String cognome;
	
	@Column(name = "NOME")
	private String nome;
	
	/*
	@Column(name = "CODICE_FISCALE")
	private String codiceFiscale;*/
	
	@Column(name = "EMAIL")
	private String email;
	
	@Column(name = "TELEFONO")
	private String telefono;
	
	@Column(name = "CAA")
	private String caa;

	@Column(name = "RESPONSABILE")
	private String responsabile;
	
	@Column(name = "NR_FASCICOLO_PRIVACY")
	private String nrFascicoloPrivacy;
	
	@Column(name = "NR_PROTOCOLLO_PRIVACY")
	private String nrProtocolloPrivacy;
	
	@Column(name = "CARICATO")
	private Integer caricato;

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCodiceFiscale() {
		if(this.id == null)
			this.id = new LoadUtenteCaaPK();
		return id.getCodiceFiscale();
	}

	public void setCodiceFiscale(String codiceFiscale) {
		if(this.id == null)
			this.id = new LoadUtenteCaaPK();
		this.id.setCodiceFiscale(codiceFiscale);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCaa() {
		return caa;
	}

	public void setCaa(String caa) {
		this.caa = caa;
	}

	public String getUfficio() {
		if(this.id == null)
			this.id = new LoadUtenteCaaPK();
		return this.id.getUfficio();
	}

	public void setUfficio(String ufficio) {
		if(this.id == null)
			this.id = new LoadUtenteCaaPK();
		this.id.setUfficio(ufficio);
	}

	public String getResponsabile() {
		return responsabile;
	}

	public void setResponsabile(String responsabile) {
		this.responsabile = responsabile;
	}

	public String getNrFascicoloPrivacy() {
		return nrFascicoloPrivacy;
	}

	public void setNrFascicoloPrivacy(String nrFascicoloPrivacy) {
		this.nrFascicoloPrivacy = nrFascicoloPrivacy;
	}

	public String getNrProtocolloPrivacy() {
		return nrProtocolloPrivacy;
	}

	public void setNrProtocolloPrivacy(String nrProtocolloPrivacy) {
		this.nrProtocolloPrivacy = nrProtocolloPrivacy;
	}

	public Integer getCaricato() {
		return caricato;
	}

	public void setCaricato(Integer caricato) {
		this.caricato = caricato;
	}
}


			