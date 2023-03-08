package it.tndigitale.a4g.proxy.repository.sincronizzazione.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the AABARAIA_TAB database table.
 * 
 */
@Entity
@Table(name="AABARAIA_TAB")
@NamedQuery(name="AabaraiaTab.findAll", query="SELECT a FROM AabaraiaTab a")
public class AabaraiaTab implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_RAIA")
	private long idRaia;

	@Column(name="ANNO_CAMP")
	private BigDecimal annoCamp;

	@Column(name="COD_FISC")
	private String codFisc;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_FINE_ASSE")
	private Date dataFineAsse;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_FINE_POSS_REQU")
	private Date dataFinePossRequ;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_INIZ_ASSE")
	private Date dataInizAsse;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_INIZ_POSS_REQU")
	private Date dataInizPossRequ;

	@Column(name="DTL__CAPXACTION")
	private String dtlCapxaction;

	@Column(name="DTL__CAPXTIMESTAMP")
	private String dtlCapxtimestamp;

	@Column(name="FLAG_AGRI_ATTI")
	private BigDecimal flagAgriAtti;

	@Column(name="FLAG_GIOV_AGRI")
	private BigDecimal flagGiovAgri;

	@Column(name="ID_ORPA")
	private BigDecimal idOrpa;

	@Column(name="ID_SOGG")
	private BigDecimal idSogg;

	public AabaraiaTab() {
	}

	public long getIdRaia() {
		return this.idRaia;
	}

	public void setIdRaia(long idRaia) {
		this.idRaia = idRaia;
	}

	public BigDecimal getAnnoCamp() {
		return this.annoCamp;
	}

	public void setAnnoCamp(BigDecimal annoCamp) {
		this.annoCamp = annoCamp;
	}

	public String getCodFisc() {
		return this.codFisc;
	}

	public void setCodFisc(String codFisc) {
		this.codFisc = codFisc;
	}

	public Date getDataFineAsse() {
		return this.dataFineAsse;
	}

	public void setDataFineAsse(Date dataFineAsse) {
		this.dataFineAsse = dataFineAsse;
	}

	public Date getDataFinePossRequ() {
		return this.dataFinePossRequ;
	}

	public void setDataFinePossRequ(Date dataFinePossRequ) {
		this.dataFinePossRequ = dataFinePossRequ;
	}

	public Date getDataInizAsse() {
		return this.dataInizAsse;
	}

	public void setDataInizAsse(Date dataInizAsse) {
		this.dataInizAsse = dataInizAsse;
	}

	public Date getDataInizPossRequ() {
		return this.dataInizPossRequ;
	}

	public void setDataInizPossRequ(Date dataInizPossRequ) {
		this.dataInizPossRequ = dataInizPossRequ;
	}

	public String getDtlCapxaction() {
		return this.dtlCapxaction;
	}

	public void setDtlCapxaction(String dtlCapxaction) {
		this.dtlCapxaction = dtlCapxaction;
	}

	public String getDtlCapxtimestamp() {
		return this.dtlCapxtimestamp;
	}

	public void setDtlCapxtimestamp(String dtlCapxtimestamp) {
		this.dtlCapxtimestamp = dtlCapxtimestamp;
	}

	public BigDecimal getFlagAgriAtti() {
		return this.flagAgriAtti;
	}

	public void setFlagAgriAtti(BigDecimal flagAgriAtti) {
		this.flagAgriAtti = flagAgriAtti;
	}

	public BigDecimal getFlagGiovAgri() {
		return this.flagGiovAgri;
	}

	public void setFlagGiovAgri(BigDecimal flagGiovAgri) {
		this.flagGiovAgri = flagGiovAgri;
	}

	public BigDecimal getIdOrpa() {
		return this.idOrpa;
	}

	public void setIdOrpa(BigDecimal idOrpa) {
		this.idOrpa = idOrpa;
	}

	public BigDecimal getIdSogg() {
		return this.idSogg;
	}

	public void setIdSogg(BigDecimal idSogg) {
		this.idSogg = idSogg;
	}

}