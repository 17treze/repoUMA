package it.tndigitale.a4g.proxy.repository.sincronizzazione.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;


/**
 * The persistent class for the AABAALL3_TAB database table.
 * 
 */
@Entity
@Table(name="AABAALL3_TAB")
@NamedQuery(name="Aabaall3Tab.findAll", query="SELECT a FROM Aabaall3Tab a")
public class Aabaall3Tab implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_ALL3")
	@SequenceGenerator(name = "AABAALL3_GENERATOR", sequenceName = "AABAALL3_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AABAALL3_GENERATOR")		
	private long idAll3;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_AGGI")
	private Date dataAggi;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_INSE")
	private Date dataInse;

	@Column(name="DESC_PEC")
	private String descPec;

	@Column(name="ID_SGCA")
	private BigDecimal idSgca;

	@Version
	@Column(name = "VERSIONE")
	private Integer versione;

	//bi-directional many-to-one association to Aabaall1Tab
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_ALL1")
	private Aabaall1Tab aabaall1Tab;

	//bi-directional many-to-one association to AabafacoTab
	@OneToMany(mappedBy="aabaall3Tab")
	private Set<AabafacoTab> aabafacoTabs;

	public Aabaall3Tab() {
	}

	public long getIdAll3() {
		return this.idAll3;
	}

	public void setIdAll3(long idAll3) {
		this.idAll3 = idAll3;
	}

	public Date getDataAggi() {
		return this.dataAggi;
	}

	public void setDataAggi(Date dataAggi) {
		this.dataAggi = dataAggi;
	}

	public Date getDataInse() {
		return this.dataInse;
	}

	public void setDataInse(Date dataInse) {
		this.dataInse = dataInse;
	}

	public String getDescPec() {
		return this.descPec;
	}

	public void setDescPec(String descPec) {
		this.descPec = descPec;
	}

	public BigDecimal getIdSgca() {
		return this.idSgca;
	}

	public void setIdSgca(BigDecimal idSgca) {
		this.idSgca = idSgca;
	}

	public Integer getVersione() {
		return this.versione;
	}

	public void setVersione(Integer versione) {
		this.versione = versione;
	}

	public Aabaall1Tab getAabaall1Tab() {
		return this.aabaall1Tab;
	}

	public void setAabaall1Tab(Aabaall1Tab aabaall1Tab) {
		this.aabaall1Tab = aabaall1Tab;
	}

	public Set<AabafacoTab> getAabafacoTabs() {
		return this.aabafacoTabs;
	}

	public void setAabafacoTabs(Set<AabafacoTab> aabafacoTabs) {
		this.aabafacoTabs = aabafacoTabs;
	}

	public AabafacoTab addAabafacoTab(AabafacoTab aabafacoTab) {
		getAabafacoTabs().add(aabafacoTab);
		aabafacoTab.setAabaall3Tab(this);

		return aabafacoTab;
	}

	public AabafacoTab removeAabafacoTab(AabafacoTab aabafacoTab) {
		getAabafacoTabs().remove(aabafacoTab);
		aabafacoTab.setAabaall3Tab(null);

		return aabafacoTab;
	}

}