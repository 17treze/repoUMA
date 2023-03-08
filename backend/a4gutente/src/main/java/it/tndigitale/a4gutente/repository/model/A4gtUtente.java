package it.tndigitale.a4gutente.repository.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;


/**
 * The persistent class for the A4GT_UTENTE database table.
 * 
 */
@Entity
@Table(name="A4GT_UTENTE")
@NamedQuery(name="A4gtUtente.findAll", query="SELECT a FROM A4gtUtente a")
public class A4gtUtente extends EntitaDominio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="CODICE_FISCALE")
	private String codiceFiscale;

	private String identificativo;
	
	private String email;
	
	private String telefono;

	@OneToMany(mappedBy = "a4gtUtente", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<A4grUtenteAzienda> a4grUtenteAziendas;

	@OneToMany(mappedBy = "utente", fetch = FetchType.LAZY)
	@OrderBy(value = "dataTermineIstruttoria DESC")
	private List<IstruttoriaEntita> istruttorie;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "A4GR_UTENTE_ENTE", joinColumns = @JoinColumn(name = "ID_UTENTE"), inverseJoinColumns = @JoinColumn(name = "ID_ENTE"))
	private Set<A4gtEnte> a4gtEntes;

	@ManyToMany(fetch=FetchType.EAGER)
    @JoinTable( name="A4GR_UTENTE_PROFILO", joinColumns=@JoinColumn(name="ID_UTENTE"),  inverseJoinColumns=@JoinColumn(name="ID_PROFILO") )
	private Set<A4gtProfilo> profili;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "A4GR_UTENTE_DISTRIBUTORE", joinColumns = @JoinColumn(name = "ID_UTENTE"), inverseJoinColumns = @JoinColumn(name = "ID_DISTRIBUTORE"))
	private Set<A4gtDistributore> a4gtDistributori;

	@Transient
	private PersonaEntita persona;

	public A4gtUtente() {
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public A4gtUtente setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
		return this;
	}

	public String getIdentificativo() {
		return identificativo;
	}

	public A4gtUtente setIdentificativo(String identificativo) {
		this.identificativo = identificativo;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public A4gtUtente setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getTelefono() {
		return telefono;
	}

	public A4gtUtente setTelefono(String telefono) {
		this.telefono = telefono;
		return this;
	}

	public Set<A4grUtenteAzienda> getA4grUtenteAziendas() {
		return a4grUtenteAziendas;
	}

	public A4gtUtente setA4grUtenteAziendas(Set<A4grUtenteAzienda> a4grUtenteAziendas) {
		this.a4grUtenteAziendas = a4grUtenteAziendas;
		return this;
	}

	public Set<A4gtEnte> getA4gtEntes() {
		return a4gtEntes;
	}

	public A4gtUtente setA4gtEntes(Set<A4gtEnte> a4gtEntes) {
		this.a4gtEntes = a4gtEntes;
		return this;
	}

	public Set<A4gtProfilo> getProfili() {
		return profili;
	}

	public A4gtUtente setProfili(Set<A4gtProfilo> profili) {
		this.profili = profili;
		return this;
	}

	public A4gtUtente setPersona(PersonaEntita persona) {
		this.persona = persona;
		return this;
	}

	public PersonaEntita getPersona() {
		return persona;
	}

	public List<IstruttoriaEntita> getIstruttorie() {
		return istruttorie;
	}

	public A4gtUtente setIstruttorie(List<IstruttoriaEntita> istruttorie) {
		this.istruttorie = istruttorie;
		return this;
	}

	public Set<A4gtDistributore> getA4gtDistributori() {
		return a4gtDistributori;
	}

	public A4gtUtente setA4gtDistributori(Set<A4gtDistributore> a4gtDistributori) {
		this.a4gtDistributori = a4gtDistributori;
		return this;
	}
}
