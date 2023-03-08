package it.tndigitale.a4gistruttoria.repository.model;

//import java.io.Serializable;
import java.math.BigDecimal;
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
 * The persistent class for the A4GT_TITOLO_DU database table.
 * 
 */
//@Entity
//@Table(name="A4GT_TITOLO_DU")
//@NamedQuery(name="A4gtTitoloDu.findAll", query="SELECT a FROM A4gtTitoloDu a")
//public class A4gtTitoloDu extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {
//	private static final long serialVersionUID = 1L;
//
//	@Column(name="ANNO_CAMPAGNA_FINE")
//	private BigDecimal annoCampagnaFine;
//
//	@Column(name="ANNO_CAMPAGNA_INIZIO")
//	private BigDecimal annoCampagnaInizio;
//
//	@Temporal(TemporalType.DATE)
//	@Column(name="DATA_FINE_POSSESSO")
//	private Date dataFinePossesso;
//
//	@Temporal(TemporalType.DATE)
//	@Column(name="DATA_INIZIO_POSSESSO")
//	private Date dataInizioPossesso;
//
//	@Column(name="ID_TITOLARE")
//	private BigDecimal idTitolare;
//
//	@Column(name="NUMERO_TITOLARE")
//	private String numeroTitolare;
//
//	private BigDecimal superficie;
//
//	private BigDecimal valore;
//
//	//bi-directional many-to-one association to DomandaUnicaModel
//	@ManyToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name="ID_DOMANDA")
//	private DomandaUnicaModel domandaUnicaModel;
//
//	public A4gtTitoloDu() {
//	}
//
//	public BigDecimal getAnnoCampagnaFine() {
//		return this.annoCampagnaFine;
//	}
//
//	public void setAnnoCampagnaFine(BigDecimal annoCampagnaFine) {
//		this.annoCampagnaFine = annoCampagnaFine;
//	}
//
//	public BigDecimal getAnnoCampagnaInizio() {
//		return this.annoCampagnaInizio;
//	}
//
//	public void setAnnoCampagnaInizio(BigDecimal annoCampagnaInizio) {
//		this.annoCampagnaInizio = annoCampagnaInizio;
//	}
//
//	public Date getDataFinePossesso() {
//		return this.dataFinePossesso;
//	}
//
//	public void setDataFinePossesso(Date dataFinePossesso) {
//		this.dataFinePossesso = dataFinePossesso;
//	}
//
//	public Date getDataInizioPossesso() {
//		return this.dataInizioPossesso;
//	}
//
//	public void setDataInizioPossesso(Date dataInizioPossesso) {
//		this.dataInizioPossesso = dataInizioPossesso;
//	}
//
//	public BigDecimal getIdTitolare() {
//		return this.idTitolare;
//	}
//
//	public void setIdTitolare(BigDecimal idTitolare) {
//		this.idTitolare = idTitolare;
//	}
//
//	public String getNumeroTitolare() {
//		return this.numeroTitolare;
//	}
//
//	public void setNumeroTitolare(String numeroTitolare) {
//		this.numeroTitolare = numeroTitolare;
//	}
//
//	public BigDecimal getSuperficie() {
//		return this.superficie;
//	}
//
//	public void setSuperficie(BigDecimal superficie) {
//		this.superficie = superficie;
//	}
//
//	public BigDecimal getValore() {
//		return this.valore;
//	}
//
//	public void setValore(BigDecimal valore) {
//		this.valore = valore;
//	}
//
//	public DomandaUnicaModel getDomandaUnica() {
//		return this.domandaUnicaModel;
//	}
//
//	public void setDomandaUnica(DomandaUnicaModel domandaUnicaModel) {
//		this.domandaUnicaModel = domandaUnicaModel;
//	}
//
//}