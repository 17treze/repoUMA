package it.tndigitale.a4gistruttoria.repository.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the A4GT_DATI_EREDE database table.
 * 
 */
@Entity
@Table(name="A4GT_DATI_EREDE")
@NamedQuery(name="A4gtDatiErede.findAll", query="SELECT a FROM A4gtDatiErede a")
public class A4gtDatiErede extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable  {
	private static final long serialVersionUID = 1L;

	@Column(name="CAP")
	private String cap;
	
	@Column(name="CERTIFICATO")
	private Boolean certificato;

	@Column(name="COD_ISTAT_NASCITA")
	private String codIstatNascita;

	@Column(name="CODICE_FISCALE")
	private String codiceFiscale;

	@Column(name="CODICE_ISTAT")
	private String codiceIstat;

	@Column(name="COGNOME")
	private String cognome;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_NASCITA")
	private Date dtNascita;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_ULTIMO_AGGIORNAMENTO")
	private Date dtUltimoAggiornamento;

	@Column(name="IBAN")
	private String iban;

	@Column(name="INDIRIZZO")
	private String indirizzo;

	@Column(name="NOME")
	private String nome;

	@Column(name="PROVINCIA")
	private String provincia;

	@Column(name="PROVINCIA_NASCITA")
	private String provinciaNascita;

	@Column(name="SESSO")
	private String sesso;

	//bi-directional many-to-one association to DomandaUnicaModel
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_DOMANDA")
	private DomandaUnicaModel domandaUnicaModel;

	public A4gtDatiErede() {
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public Boolean getCertificato() {
		return certificato;
	}

	public void setCertificato(Boolean certificato) {
		this.certificato = certificato;
	}

	public String getCodIstatNascita() {
		return codIstatNascita;
	}

	public void setCodIstatNascita(String codIstatNascita) {
		this.codIstatNascita = codIstatNascita;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getCodiceIstat() {
		return codiceIstat;
	}

	public void setCodiceIstat(String codiceIstat) {
		this.codiceIstat = codiceIstat;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public Date getDtNascita() {
		return dtNascita;
	}

	public void setDtNascita(Date dtNascita) {
		this.dtNascita = dtNascita;
	}

	public Date getDtUltimoAggiornamento() {
		return dtUltimoAggiornamento;
	}

	public void setDtUltimoAggiornamento(Date dtUltimoAggiornamento) {
		this.dtUltimoAggiornamento = dtUltimoAggiornamento;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getProvinciaNascita() {
		return provinciaNascita;
	}

	public void setProvinciaNascita(String provinciaNascita) {
		this.provinciaNascita = provinciaNascita;
	}

	public String getSesso() {
		return sesso;
	}

	public void setSesso(String sesso) {
		this.sesso = sesso;
	}

	public DomandaUnicaModel getDomandaUnicaModel() {
		return domandaUnicaModel;
	}

	public void setDomandaUnicaModel(DomandaUnicaModel domandaUnicaModel) {
		this.domandaUnicaModel = domandaUnicaModel;
	}

}