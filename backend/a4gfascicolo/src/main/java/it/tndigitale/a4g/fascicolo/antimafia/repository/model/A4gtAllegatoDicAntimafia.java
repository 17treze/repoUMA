package it.tndigitale.a4g.fascicolo.antimafia.repository.model;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the A4GT_ALLEGATO_DIC_ANTIMAFIA database table.
 * 
 */
@Entity
@Table(name="A4GT_ALLEGATO_DIC_ANTIMAFIA")
public class A4gtAllegatoDicAntimafia extends EntitaDominio implements Serializable {
	private static final long serialVersionUID = -4368552830288286656L;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_UPLOAD_PDF_ALLEGATO")
	private Date dtUploadPdfAllegato;

	@Lob
	@Column(name="PDF_ALLEGATO")
	private byte[] pdfAllegato;

	//bi-directional many-to-one association to A4gdTipoAllegato
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_TIPO_ALLEGATO", nullable=false)
	private A4gdTipoAllegato a4gdTipoAllegato;

	//bi-directional many-to-one association to A4gtDichiarazioneAntimafia
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_DICHIARAZIONE_ANTIMAFIA", nullable=false)
	private A4gtDichiarazioneAntimafia a4gtDichiarazioneAntimafia;

	public A4gtAllegatoDicAntimafia() {
		//Default empty constructor
	}

	public Date getDtUploadPdfAllegato() {
		return this.dtUploadPdfAllegato;
	}

	public void setDtUploadPdfAllegato(Date dtUploadPdfAllegato) {
		this.dtUploadPdfAllegato = dtUploadPdfAllegato;
	}

	public byte[] getPdfAllegato() {
		return this.pdfAllegato;
	}

	public void setPdfAllegato(byte[] pdfAllegato) {
		this.pdfAllegato = pdfAllegato;
	}

	public A4gdTipoAllegato getA4gdTipoAllegato() {
		return this.a4gdTipoAllegato;
	}

	public void setA4gdTipoAllegato(A4gdTipoAllegato a4gdTipoAllegato) {
		this.a4gdTipoAllegato = a4gdTipoAllegato;
	}

	public A4gtDichiarazioneAntimafia getA4gtDichiarazioneAntimafia() {
		return this.a4gtDichiarazioneAntimafia;
	}

	public void setA4gtDichiarazioneAntimafia(A4gtDichiarazioneAntimafia a4gtDichiarazioneAntimafia) {
		this.a4gtDichiarazioneAntimafia = a4gtDichiarazioneAntimafia;
	}

}