package it.tndigitale.a4g.fascicolo.antimafia.repository.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * The persistent class for the A4GT_DICHIARAZIONE_ANTIMAFIA database table.
 * 
 */
@Entity
@Table(name = "A4GT_DICHIARAZIONE_ANTIMAFIA")
public class A4gtDichiarazioneAntimafia extends EntitaDominio implements Serializable {
	private static final long serialVersionUID = -8891611497857721253L;

	@Formula("JSON_VALUE(DATI_DICHIARAZIONE, '$.dettaglioImpresa.denominazione')")
    private String denominazioneImpresa;
	
	@Lob
	@Column(name = "DATI_DICHIARAZIONE")
	private String datiDichiarazione;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_GENERAZIONE_PDF")
	private Date dtGenerazionePdf;

	@Column(name = "DT_INIZIO_COMPILAZIONE")
	@CreationTimestamp
	private Date dtInizioCompilazione;

	@Column(name = "DT_ULTIMO_AGGIORNAMENTO")
	@UpdateTimestamp
	private Date dtUltimoAggiornamento;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_UPLOAD_PDF_FIRMATO")
	private Date dtUploadPdfFirmato;

	@Column(name = "ID_PROTOCOLLO")
	private String idProtocollo;

	@Column(name = "DT_PROTOCOLLAZIONE")
	private Date dtProtocollazione;

	@Lob
	@Column(name = "PDF_FIRMATO")
	private byte[] pdfFirmato;

	@Lob
	@Column(name = "PDF_GENERATO")
	private byte[] pdfGenerato;

	@Column(name = "ASSENZA_DT")
	private Boolean assenzaDt;

	@Column(name = "DT_FINE")
	private Date dtFine;

	@Column(name = "ESITO")
	private String esito;
	
	@Column(name = "ESITO_DATA_ELABORAZIONE")
	private LocalDateTime esitoDtElaborazione;

	@Column(name = "ESITO_DESCRIZIONE")
	private String esitoDescrizione;
	
	@Column(name = "ESITO_INVIO_AGEA")
	private String esitoInvioAgea;
	
	@Column(name = "ESITO_INVIO_BDNA")
	private String esitoInvioBdna;
	
	// bi-directional many-to-one association to A4gtAllegatoDicAntimafia
	@OneToMany(mappedBy = "a4gtDichiarazioneAntimafia")
	private Set<A4gtAllegatoDicAntimafia> a4gtAllegatoDicAntimafias;

	// bi-directional many-to-one association to A4gdStatoDicAntimafia
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_STATO", nullable = false)
	private A4gdStatoDicAntimafia a4gdStatoDicAntimafia;

	// bi-directional many-to-one association to A4gtAziendaAgricola
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_AZIENDA_AGRICOLA", nullable = false)
	private A4gtAziendaAgricola a4gtAziendaAgricola;

	// bi-directional one-to-many association to A4gtAllegatoDicFamConv
	@OneToMany(mappedBy = "a4gtDichiarazioneAntimafia", fetch = FetchType.LAZY)
	private Set<A4gtAllegatoDicFamConv> a4gtAllegatoDicFamConv;
	
	//bi-directional many-to-one association to A4gtProcedimentoAmf
	@OneToMany(mappedBy="a4gtDichiarazioneAntimafia")
	private Set<A4gtProcedimentoAmf> a4gtProcedimentoAmfs;
	
	public String getDatiDichiarazione() {
		return this.datiDichiarazione;
	}

	public void setDatiDichiarazione(String datiDichiarazione) {
		this.datiDichiarazione = datiDichiarazione;
	}

	public Date getDtGenerazionePdf() {
		return this.dtGenerazionePdf;
	}

	public void setDtGenerazionePdf(Date dtGenerazionePdf) {
		this.dtGenerazionePdf = dtGenerazionePdf;
	}

	public Date getDtInizioCompilazione() {
		return this.dtInizioCompilazione;
	}

	public void setDtInizioCompilazione(Date dtInizioCompilazione) {
		this.dtInizioCompilazione = dtInizioCompilazione;
	}

	public Date getDtUltimoAggiornamento() {
		return this.dtUltimoAggiornamento;
	}

	public void setDtUltimoAggiornamento(Date dtUltimoAggiornamento) {
		this.dtUltimoAggiornamento = dtUltimoAggiornamento;
	}

	public Date getDtUploadPdfFirmato() {
		return this.dtUploadPdfFirmato;
	}

	public void setDtUploadPdfFirmato(Date dtUploadPdfFirmato) {
		this.dtUploadPdfFirmato = dtUploadPdfFirmato;
	}

	public byte[] getPdfFirmato() {
		return this.pdfFirmato;
	}

	public void setPdfFirmato(byte[] pdfFirmato) {
		this.pdfFirmato = pdfFirmato;
	}

	public byte[] getPdfGenerato() {
		return this.pdfGenerato;
	}

	public void setPdfGenerato(byte[] pdfGenerato) {
		this.pdfGenerato = pdfGenerato;
	}

	public Set<A4gtAllegatoDicAntimafia> getA4gtAllegatoDicAntimafias() {
		return this.a4gtAllegatoDicAntimafias;
	}

	public void setA4gtAllegatoDicAntimafias(Set<A4gtAllegatoDicAntimafia> a4gtAllegatoDicAntimafias) {
		this.a4gtAllegatoDicAntimafias = a4gtAllegatoDicAntimafias;
	}

	public A4gtAllegatoDicAntimafia addA4gtAllegatoDicAntimafia(A4gtAllegatoDicAntimafia a4gtAllegatoDicAntimafia) {
		getA4gtAllegatoDicAntimafias().add(a4gtAllegatoDicAntimafia);
		a4gtAllegatoDicAntimafia.setA4gtDichiarazioneAntimafia(this);

		return a4gtAllegatoDicAntimafia;
	}

	public A4gtAllegatoDicAntimafia removeA4gtAllegatoDicAntimafia(A4gtAllegatoDicAntimafia a4gtAllegatoDicAntimafia) {
		getA4gtAllegatoDicAntimafias().remove(a4gtAllegatoDicAntimafia);
		a4gtAllegatoDicAntimafia.setA4gtDichiarazioneAntimafia(null);

		return a4gtAllegatoDicAntimafia;
	}

	public A4gdStatoDicAntimafia getA4gdStatoDicAntimafia() {
		return this.a4gdStatoDicAntimafia;
	}

	public void setA4gdStatoDicAntimafia(A4gdStatoDicAntimafia a4gdStatoDicAntimafia) {
		this.a4gdStatoDicAntimafia = a4gdStatoDicAntimafia;
	}

	public A4gtAziendaAgricola getA4gtAziendaAgricola() {
		return this.a4gtAziendaAgricola;
	}

	public void setA4gtAziendaAgricola(A4gtAziendaAgricola a4gtAziendaAgricola) {
		this.a4gtAziendaAgricola = a4gtAziendaAgricola;
	}

	public Boolean getAssenzaDt() {
		return assenzaDt;
	}

	public void setAssenzaDt(Boolean assenzaDt) {
		this.assenzaDt = assenzaDt;
	}

	public Set<A4gtAllegatoDicFamConv> getA4gtAllegatoDicFamConv() {
		return a4gtAllegatoDicFamConv;
	}

	public void setA4gtAllegatoDicFamConv(Set<A4gtAllegatoDicFamConv> a4gtAllegatoDicFamConv) {
		this.a4gtAllegatoDicFamConv = a4gtAllegatoDicFamConv;
	}

	public Date getDtProtocollazione() {
		return dtProtocollazione;
	}

	public void setDtProtocollazione(Date dtProtocollazione) {
		this.dtProtocollazione = dtProtocollazione;
	}

	public String getIdProtocollo() {
		return idProtocollo;
	}

	public void setIdProtocollo(String idProtocollo) {
		this.idProtocollo = idProtocollo;
	}

	public Date getDtFine() {
		return dtFine;
	}

	public void setDtFine(Date dtFine) {
		this.dtFine = dtFine;
	}
	
	public Set<A4gtProcedimentoAmf> getA4gtProcedimentoAmfs() {
		return this.a4gtProcedimentoAmfs;
	}

	public void setA4gtProcedimentoAmfs(Set<A4gtProcedimentoAmf> a4gtProcedimentoAmfs) {
		this.a4gtProcedimentoAmfs = a4gtProcedimentoAmfs;
	}

	public A4gtProcedimentoAmf addA4gtProcedimentoAmf(A4gtProcedimentoAmf a4gtProcedimentoAmf) {
		getA4gtProcedimentoAmfs().add(a4gtProcedimentoAmf);
		a4gtProcedimentoAmf.setA4gtDichiarazioneAntimafia(this);

		return a4gtProcedimentoAmf;
	}

	public A4gtProcedimentoAmf removeA4gtProcedimentoAmf(A4gtProcedimentoAmf a4gtProcedimentoAmf) {
		getA4gtProcedimentoAmfs().remove(a4gtProcedimentoAmf);
		a4gtProcedimentoAmf.setA4gtDichiarazioneAntimafia(null);

		return a4gtProcedimentoAmf;
	}
	
	public String getDenominazioneImpresa() {
		return denominazioneImpresa;
	}

	public void setDenominazioneImpresa(String denominazioneImpresa) {
		this.denominazioneImpresa = denominazioneImpresa;
	}

	public String getEsito() {
		return esito;
	}

	public void setEsito(String esito) {
		this.esito = esito;
	}

	public LocalDateTime getEsitoDtElaborazione() {
		return esitoDtElaborazione;
	}

	public void setEsitoDtElaborazione(LocalDateTime esitoDtElaborazione) {
		this.esitoDtElaborazione = esitoDtElaborazione;
	}

	public String getEsitoDescrizione() {
		return esitoDescrizione;
	}

	public void setEsitoDescrizione(String esitoDescrizione) {
		this.esitoDescrizione = esitoDescrizione;
	}

	public String getEsitoInvioAgea() {
		return esitoInvioAgea;
	}

	public void setEsitoInvioAgea(String esitoInvioAgea) {
		this.esitoInvioAgea = esitoInvioAgea;
	}

	public String getEsitoInvioBdna() {
		return esitoInvioBdna;
	}

	public void setEsitoInvioBdna(String esitoInvioBdna) {
		this.esitoInvioBdna = esitoInvioBdna;
	}
}