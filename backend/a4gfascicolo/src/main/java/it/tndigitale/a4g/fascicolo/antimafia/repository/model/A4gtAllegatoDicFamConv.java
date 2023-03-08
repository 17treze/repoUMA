package it.tndigitale.a4g.fascicolo.antimafia.repository.model;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "A4GT_ALLEGATO_DIC_FAM_CONV")
public class A4gtAllegatoDicFamConv extends EntitaDominio implements Serializable {

	private static final long serialVersionUID = -6901945388237040575L;

	@Temporal(TemporalType.DATE)
	@Column(name = "DT_PDF_DIC_FAM_CONV")
	private Date dtPdfDicFamConv;

	@Lob
	@Column(name = "PDF_DIC_FAM_CONV")
	private byte[] pdfDicFamConv;

	@Column(name = "COD_CARICA", length = 50)
	private String codCarica;

	@Column(name = "CF_SOGGETTO_IMPRESA", length = 16)
	private String cfSoggettoImpresa;

	// bi-directional many-to-one association to A4gtDichiarazioneAntimafia
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_DICHIARAZIONE_ANTIMAFIA", nullable = false)
	private A4gtDichiarazioneAntimafia a4gtDichiarazioneAntimafia;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TIPO_ALLEGATO", nullable = false)
	private A4gdTipoAllegato a4gdTipoAllegato;

	public Date getDtPdfDicFamConv() {
		return dtPdfDicFamConv;
	}

	public byte[] getPdfDicFamConv() {
		return pdfDicFamConv;
	}

	public String getCodCarica() {
		return codCarica;
	}

	public String getCfSoggettoImpresa() {
		return cfSoggettoImpresa;
	}

	public A4gtDichiarazioneAntimafia getA4gtDichiarazioneAntimafia() {
		return a4gtDichiarazioneAntimafia;
	}

	public void setDtPdfDicFamConv(Date dtPdfDicFamConv) {
		this.dtPdfDicFamConv = dtPdfDicFamConv;
	}

	public void setPdfDicFamConv(byte[] pdfDicFamConv) {
		this.pdfDicFamConv = pdfDicFamConv;
	}

	public void setCodCarica(String codCarica) {
		this.codCarica = codCarica;
	}

	public void setCfSoggettoImpresa(String cfSoggettoImpresa) {
		this.cfSoggettoImpresa = cfSoggettoImpresa;
	}

	public void setA4gtDichiarazioneAntimafia(A4gtDichiarazioneAntimafia a4gtDichiarazioneAntimafia) {
		this.a4gtDichiarazioneAntimafia = a4gtDichiarazioneAntimafia;
	}

	public A4gdTipoAllegato getA4gdTipoAllegato() {
		return a4gdTipoAllegato;
	}

	public void setA4gdTipoAllegato(A4gdTipoAllegato a4gdTipoAllegato) {
		this.a4gdTipoAllegato = a4gdTipoAllegato;
	}

}
