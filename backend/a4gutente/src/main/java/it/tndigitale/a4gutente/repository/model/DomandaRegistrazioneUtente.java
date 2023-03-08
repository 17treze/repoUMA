package it.tndigitale.a4gutente.repository.model;

import static it.tndigitale.a4gutente.codici.StatoDomandaRegistrazioneUtente.APPROVATA;
import static it.tndigitale.a4gutente.codici.StatoDomandaRegistrazioneUtente.RIFIUTATA;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;
import it.tndigitale.a4gutente.codici.StatoDomandaRegistrazioneUtente;
import it.tndigitale.a4gutente.dto.TipoDomandaRegistrazione;

@Entity
@Table(name = "A4GT_DOMANDA_REGISTRAZIONE")
public class DomandaRegistrazioneUtente extends EntitaDominio implements Serializable {

	private static final long serialVersionUID = 3086812550655827127L;

	@Enumerated(EnumType.STRING)
	private StatoDomandaRegistrazioneUtente stato;

	@Column(name = "IDENTIFICATIVO_UTENTE")
	private String identificativoUtente;

	@Column(name = "CODICE_FISCALE")
	private String codiceFiscale;

	private String cognome;

	private String nome;

	private String email;

	private String telefono;

	private String responsabilita;

	private Boolean a4g;

	private Boolean srt;

	private Boolean ags;

	@Lob
	private byte[] documento;
	
	private String idProtocollo;
	
	private LocalDateTime dtProtocollazione;

	private Boolean configurato = Boolean.FALSE;
	
	@Column(name = "TIPO_DOMANDA_REGISTRAZIONE")
	@Enumerated(EnumType.STRING)
	private TipoDomandaRegistrazione tipoDomandaRegistrazione;

	@OneToOne(mappedBy = "domanda", fetch = FetchType.EAGER)
	private IstruttoriaEntita istruttoriaEntita;

	@OneToMany(mappedBy = "domandaRegistrazione", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<A4gtAllegatoResponsabilita> a4gtAllegatoResponsabilita;

	public StatoDomandaRegistrazioneUtente getStato() {
		return stato;
	}

	public DomandaRegistrazioneUtente setStato(StatoDomandaRegistrazioneUtente stato) {
		this.stato = stato;
		return this;
	}

	public String getIdentificativoUtente() {
		return identificativoUtente;
	}

	public DomandaRegistrazioneUtente setIdentificativoUtente(String identificativoUtente) {
		this.identificativoUtente = identificativoUtente;
		return this;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public DomandaRegistrazioneUtente setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
		return this;
	}

	public String getCognome() {
		return cognome;
	}

	public DomandaRegistrazioneUtente setCognome(String cognome) {
		this.cognome = cognome;
		return this;
	}

	public String getNome() {
		return nome;
	}

	public DomandaRegistrazioneUtente setNome(String nome) {
		this.nome = nome;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public DomandaRegistrazioneUtente setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getTelefono() {
		return telefono;
	}

	public DomandaRegistrazioneUtente setTelefono(String telefono) {
		this.telefono = telefono;
		return this;
	}

	public String getResponsabilita() {
		return responsabilita;
	}

	public DomandaRegistrazioneUtente setResponsabilita(String responsabilita) {
		this.responsabilita = responsabilita;
		return this;
	}

	public Boolean getA4g() {
		return a4g;
	}

	public DomandaRegistrazioneUtente setA4g(Boolean a4g) {
		this.a4g = a4g;
		return this;
	}

	public Boolean getSrt() {
		return srt;
	}

	public DomandaRegistrazioneUtente setSrt(Boolean srt) {
		this.srt = srt;
		return this;
	}

	public Boolean getAgs() {
		return ags;
	}

	public DomandaRegistrazioneUtente setAgs(Boolean ags) {
		this.ags = ags;
		return this;
	}

	public byte[] getDocumento() {
		return documento;
	}

	public DomandaRegistrazioneUtente setDocumento(byte[] documento) {
		this.documento = documento;
		return this;
	}

	public String getIdProtocollo() {
		return idProtocollo;
	}

	public DomandaRegistrazioneUtente setIdProtocollo(String idProtocollo) {
		this.idProtocollo = idProtocollo;
		return this;
	}

	public LocalDateTime getDtProtocollazione() {
		return dtProtocollazione;
	}

	public DomandaRegistrazioneUtente setDtProtocollazione(LocalDateTime dtProtocollazione) {
		this.dtProtocollazione = dtProtocollazione;
		return this;
	}

	public Boolean getConfigurato() {
		return configurato;
	}

	public DomandaRegistrazioneUtente setConfigurato(Boolean configurato) {
		this.configurato = configurato;
		return this;
	}

	public IstruttoriaEntita getIstruttoriaEntita() {
		return istruttoriaEntita;
	}

	public DomandaRegistrazioneUtente setIstruttoriaEntita(IstruttoriaEntita istruttoriaEntita) {
		this.istruttoriaEntita = istruttoriaEntita;
		return this;
	}

	public Set<A4gtAllegatoResponsabilita> getA4gtAllegatoResponsabilita() {
		return a4gtAllegatoResponsabilita;
	}

	public DomandaRegistrazioneUtente setA4gtAllegatoResponsabilita(Set<A4gtAllegatoResponsabilita> a4gtAllegatoResponsabilita) {
		this.a4gtAllegatoResponsabilita = a4gtAllegatoResponsabilita;
		return this;
	}

	public TipoDomandaRegistrazione getTipoDomandaRegistrazione() {
		return tipoDomandaRegistrazione;
	}

	public DomandaRegistrazioneUtente setTipoDomandaRegistrazione(TipoDomandaRegistrazione tipoDomandaRegistrazione) {
		this.tipoDomandaRegistrazione = tipoDomandaRegistrazione;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DomandaRegistrazioneUtente that = (DomandaRegistrazioneUtente) o;
		return stato == that.stato &&
				Objects.equals(identificativoUtente, that.identificativoUtente) &&
				Objects.equals(codiceFiscale, that.codiceFiscale) &&
				Objects.equals(cognome, that.cognome) &&
				Objects.equals(nome, that.nome) &&
				Objects.equals(email, that.email) &&
				Objects.equals(telefono, that.telefono) &&
				Objects.equals(responsabilita, that.responsabilita) &&
				Objects.equals(a4g, that.a4g) &&
				Objects.equals(srt, that.srt) &&
				Objects.equals(ags, that.ags) &&
				Arrays.equals(documento, that.documento) &&
				Objects.equals(idProtocollo, that.idProtocollo) &&
				Objects.equals(dtProtocollazione, that.dtProtocollazione) &&
				Objects.equals(configurato, that.configurato) &&
				Objects.equals(istruttoriaEntita, that.istruttoriaEntita) &&
				Objects.equals(a4gtAllegatoResponsabilita, that.a4gtAllegatoResponsabilita) &&
				Objects.equals(tipoDomandaRegistrazione, that.tipoDomandaRegistrazione);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(
				stato, identificativoUtente, codiceFiscale, cognome, nome,
				email, telefono, responsabilita, a4g, srt, ags, idProtocollo,
				dtProtocollazione, configurato, istruttoriaEntita,
				a4gtAllegatoResponsabilita, tipoDomandaRegistrazione);
		result = 31 * result + Arrays.hashCode(documento);
		return result;
	}

	public void preparaPerCambioStato(StatoDomandaRegistrazioneUtente newState) {
		this.stato = newState;
	}

	public LocalDateTime dataApprovazione() {
		return (existIstruttoria() && APPROVATA.equals(stato))? this.getIstruttoriaEntita().getDataTermineIstruttoria():null;
	}

	public LocalDateTime dataRifiuto() {
		return (existIstruttoria() && RIFIUTATA.equals(stato))? this.getIstruttoriaEntita().getDataTermineIstruttoria():null;
	}

	public Boolean existIstruttoria() {
		return this.getIstruttoriaEntita() != null;
	}
}
