package it.tndigitale.a4gistruttoria.dto.antimafia;

import java.time.LocalDateTime;
import java.util.Date;


/**
 * The dao class for the DICHIARAZIONE_ANTIMAFIA database table.
 * 
 */

public class DichiarazioneAntimafia {

	private Long id;
	private Integer version;
	private DatiDichiarazioneAntimafia datiDichiarazione;
	private Date dtGenerazionePdf;
	private Date dtInizioCompilazione;
	private Date dtUltimoAggiornamento;
	private Date dtUploadPdfFirmato;
	private Date dtFine;
	private String idProtocollo;
	private byte[] pdfFirmato;
	private byte[] pdfGenerato;
	private String pdfFirmatoName;
	private Date dtProtocollazione;
	private StatoDic stato;
	private Azienda azienda;
	private Boolean assenzaDt;
	private String esito;
	private LocalDateTime esitoDtElaborazione;
	private String esitoDescrizione;
	private String esitoInvioAgea;
	private String esitoInvioBdna;

	public DichiarazioneAntimafia() {
		this.azienda = new Azienda();
		this.stato = new StatoDic();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public DatiDichiarazioneAntimafia getDatiDichiarazione() {
		return datiDichiarazione;
	}

	public void setDatiDichiarazione(DatiDichiarazioneAntimafia datiDichiarazione) {
		this.datiDichiarazione = datiDichiarazione;
	}

	public Date getDtGenerazionePdf() {
		return dtGenerazionePdf;
	}

	public void setDtGenerazionePdf(Date dtGenerazionePdf) {
		this.dtGenerazionePdf = dtGenerazionePdf;
	}

	public Date getDtInizioCompilazione() {
		return dtInizioCompilazione;
	}

	public void setDtInizioCompilazione(Date dtInizioCompilazione) {
		this.dtInizioCompilazione = dtInizioCompilazione;
	}

	public Date getDtUltimoAggiornamento() {
		return dtUltimoAggiornamento;
	}

	public void setDtUltimoAggiornamento(Date dtUltimoAggiornamento) {
		this.dtUltimoAggiornamento = dtUltimoAggiornamento;
	}

	public Date getDtUploadPdfFirmato() {
		return dtUploadPdfFirmato;
	}

	public void setDtUploadPdfFirmato(Date dtUploadPdfFirmato) {
		this.dtUploadPdfFirmato = dtUploadPdfFirmato;
	}

	public byte[] getPdfFirmato() {
		return pdfFirmato;
	}

	public void setPdfFirmato(byte[] pdfFirmato) {
		this.pdfFirmato = pdfFirmato;
	}

	public byte[] getPdfGenerato() {
		return pdfGenerato;
	}

	public void setPdfGenerato(byte[] pdfGenerato) {
		this.pdfGenerato = pdfGenerato;
	}

	public StatoDic getStato() {
		return stato;
	}

	public void setStato(StatoDic stato) {
		this.stato = stato;
	}

	public Azienda getAzienda() {
		return azienda;
	}

	public void setAzienda(Azienda azienda) {
		this.azienda = azienda;
	}

	public Boolean getAssenzaDt() {
		return assenzaDt;
	}

	public void setAssenzaDt(Boolean assenzaDt) {
		this.assenzaDt = assenzaDt;
	}


	@Override
	public String toString() {
		return String.format("QuadroDichiarazione ToString [id=%s, datiDichiarazione = %s, aziendaAgricola=%s]", id, datiDichiarazione, (azienda == null) ? "aziendaAgricola null" : azienda.toString());

		// return "QuadroDichiarazione [id=" + id + ", aziendaAgricola=" + ((aziendaAgricola
		// != null) ? "aziendaAgricola null" : aziendaAgricola.toString()) + "]";
	}

	public String getPdfFirmatoName() {
		return pdfFirmatoName;
	}

	public void setPdfFirmatoName(String pdfFirmatoName) {
		this.pdfFirmatoName = pdfFirmatoName;
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