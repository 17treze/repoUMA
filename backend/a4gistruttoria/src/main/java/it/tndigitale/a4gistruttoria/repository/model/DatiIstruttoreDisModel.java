package it.tndigitale.a4gistruttoria.repository.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;


/**
 * The persistent class for the A4GT_ISTRUTTORE_DISACCOPPIATO database table.
 * 
 */
@Entity
@Table(name="A4GT_ISTRUTTORE_DISACCOPPIATO")
@NamedQuery(name="DatiIstruttoreDisModel.findAll", query="SELECT a FROM DatiIstruttoreDisModel a")
public class DatiIstruttoreDisModel  extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {

	private static final long serialVersionUID = 3722484433767159656L;

	@Column(name="BPS_IMP_SAN")
	private BigDecimal bpsImpSan;

	@Column(name="BPS_SANZ_ANNO_PREC")
	private Boolean bpsSanzAnnoPrec;

	@Column(name="BPS_SUPERFICIE")
	private BigDecimal bpsSuperficie;

	@Column(name="CONTROLLO_ANTIMAFIA")
	private Boolean controlloAntimafia;

	@Column(name="DOM_ANNO_PREC_NON_LIQ")
	private Boolean domAnnoPrecNonLiq;

	@Column(name="GIO_IMP_SAN")
	private BigDecimal gioImpSan;

	@Column(name="GIO_SANZ_ANNO_PREC")
	private Boolean gioSanzAnnoPrec;

	@Column(name="GREENING_SUPERFICIE")
	private BigDecimal greeningSuperficie;

	@Column(name="IMPORTO_SALARI")
	private BigDecimal importoSalari;

	@Lob
	@Column(name="NOTE_ISTRUTTORE")
	private String noteIstruttore;

	@Column(name = "ANNULLO_RIDUZIONE")
	private Boolean annulloRiduzione;

	//bi-directional many-to-one association to A4gtLavorazioneSostegno
	@OneToOne()
	@JoinColumn(name="ID_ISTRUTTORIA")
	private IstruttoriaModel istruttoria;
	
	
	public DatiIstruttoreDisModel() {
	}


	public BigDecimal getBpsImpSan() {
		return this.bpsImpSan;
	}

	public void setBpsImpSan(BigDecimal bpsImpSan) {
		this.bpsImpSan = bpsImpSan;
	}

	public Boolean getBpsSanzAnnoPrec() {
		return this.bpsSanzAnnoPrec;
	}

	public void setBpsSanzAnnoPrec(Boolean bpsSanzAnnoPrec) {
		this.bpsSanzAnnoPrec = bpsSanzAnnoPrec;
	}

	public BigDecimal getBpsSuperficie() {
		return this.bpsSuperficie;
	}

	public void setBpsSuperficie(BigDecimal bpsSuperficie) {
		this.bpsSuperficie = bpsSuperficie;
	}

	public Boolean getControlloAntimafia() {
		return this.controlloAntimafia;
	}

	public void setControlloAntimafia(Boolean controlloAntimafia) {
		this.controlloAntimafia = controlloAntimafia;
	}

	public Boolean getDomAnnoPrecNonLiq() {
		return this.domAnnoPrecNonLiq;
	}

	public void setDomAnnoPrecNonLiq(Boolean domAnnoPrecNonLiq) {
		this.domAnnoPrecNonLiq = domAnnoPrecNonLiq;
	}

	public BigDecimal getGioImpSan() {
		return this.gioImpSan;
	}

	public void setGioImpSan(BigDecimal gioImpSan) {
		this.gioImpSan = gioImpSan;
	}

	public Boolean getGioSanzAnnoPrec() {
		return this.gioSanzAnnoPrec;
	}

	public void setGioSanzAnnoPrec(Boolean gioSanzAnnoPrec) {
		this.gioSanzAnnoPrec = gioSanzAnnoPrec;
	}

	public BigDecimal getGreeningSuperficie() {
		return this.greeningSuperficie;
	}

	public void setGreeningSuperficie(BigDecimal greeningSuperficie) {
		this.greeningSuperficie = greeningSuperficie;
	}

	public BigDecimal getImportoSalari() {
		return this.importoSalari;
	}

	public void setImportoSalari(BigDecimal importoSalari) {
		this.importoSalari = importoSalari;
	}

	public String getNoteIstruttore() {
		return this.noteIstruttore;
	}

	public void setNoteIstruttore(String noteIstruttore) {
		this.noteIstruttore = noteIstruttore;
	}

	public Boolean getAnnulloRiduzione() {
		return annulloRiduzione;
	}

	public void setAnnulloRiduzione(Boolean annulloRiduzione) {
		this.annulloRiduzione = annulloRiduzione;
	}

	public IstruttoriaModel getIstruttoria() {
		return istruttoria;
	}


	public void setIstruttoria(IstruttoriaModel istruttoria) {
		this.istruttoria = istruttoria;
	}

}
