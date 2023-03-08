package it.tndigitale.a4g.proxy.repository.sincronizzazione.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;


/**
 * The persistent class for the AABAANTI_TAB database table.
 * 
 */
@Entity
@Table(name="AABAANTI_TAB")
@NamedQuery(name="AabaantiTab.findAll", query="SELECT a FROM AabaantiTab a")
public class AabaantiTab implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_ANTI")
	private long idAnti;

	private String cuaa;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_AGGI")
	private Date dataAggi;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_FINE_VALI")
	private Date dataFineVali;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_INIZ_VALI")
	private Date dataInizVali;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_INSE")
	private Date dataInse;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_INVI_PREF")
	private Date dataInviPref;

	@Column(name="ID_ALL1")
	private BigDecimal idAll1;

	@Column(name="ID_ALL2")
	private BigDecimal idAll2;

	@Column(name="ID_ORPA")
	private BigDecimal idOrpa;

	@Column(name="TIPO_PERS")
	private BigDecimal tipoPers;

	@Version
	@Column(name = "VERSIONE")
	private Integer versione;

	//bi-directional many-to-one association to AabaesanTab
	@OneToMany(mappedBy="aabaantiTab")
	private Set<AabaesanTab> aabaesanTabs;

	public AabaantiTab() {
	}

	public long getIdAnti() {
		return this.idAnti;
	}

	public void setIdAnti(long idAnti) {
		this.idAnti = idAnti;
	}

	public String getCuaa() {
		return this.cuaa;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	public Date getDataAggi() {
		return this.dataAggi;
	}

	public void setDataAggi(Date dataAggi) {
		this.dataAggi = dataAggi;
	}

	public Date getDataFineVali() {
		return this.dataFineVali;
	}

	public void setDataFineVali(Date dataFineVali) {
		this.dataFineVali = dataFineVali;
	}

	public Date getDataInizVali() {
		return this.dataInizVali;
	}

	public void setDataInizVali(Date dataInizVali) {
		this.dataInizVali = dataInizVali;
	}

	public Date getDataInse() {
		return this.dataInse;
	}

	public void setDataInse(Date dataInse) {
		this.dataInse = dataInse;
	}

	public Date getDataInviPref() {
		return this.dataInviPref;
	}

	public void setDataInviPref(Date dataInviPref) {
		this.dataInviPref = dataInviPref;
	}

	public BigDecimal getIdAll1() {
		return this.idAll1;
	}

	public void setIdAll1(BigDecimal idAll1) {
		this.idAll1 = idAll1;
	}

	public BigDecimal getIdAll2() {
		return this.idAll2;
	}

	public void setIdAll2(BigDecimal idAll2) {
		this.idAll2 = idAll2;
	}

	public BigDecimal getIdOrpa() {
		return this.idOrpa;
	}

	public void setIdOrpa(BigDecimal idOrpa) {
		this.idOrpa = idOrpa;
	}

	public BigDecimal getTipoPers() {
		return this.tipoPers;
	}

	public void setTipoPers(BigDecimal tipoPers) {
		this.tipoPers = tipoPers;
	}

	public Integer getVersione() {
		return this.versione;
	}

	public void setVersione(Integer versione) {
		this.versione = versione;
	}

	public Set<AabaesanTab> getAabaesanTabs() {
		return this.aabaesanTabs;
	}

	public void setAabaesanTabs(Set<AabaesanTab> aabaesanTabs) {
		this.aabaesanTabs = aabaesanTabs;
	}

	public AabaesanTab addAabaesanTab(AabaesanTab aabaesanTab) {
		getAabaesanTabs().add(aabaesanTab);
		aabaesanTab.setAabaantiTab(this);

		return aabaesanTab;
	}

	public AabaesanTab removeAabaesanTab(AabaesanTab aabaesanTab) {
		getAabaesanTabs().remove(aabaesanTab);
		aabaesanTab.setAabaantiTab(null);

		return aabaesanTab;
	}

	@PrePersist
	void preInsert() {
	  if (this.dataFineVali == null)
	      this.dataFineVali = new GregorianCalendar(9999, Calendar.DECEMBER, 31).getTime();
	  if (this.idOrpa == null)
		  this.idOrpa=(new BigDecimal(160l));//Identificativo organismo pagatore	160
	}

}