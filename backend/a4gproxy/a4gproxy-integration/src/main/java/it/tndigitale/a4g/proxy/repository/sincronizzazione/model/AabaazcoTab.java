package it.tndigitale.a4g.proxy.repository.sincronizzazione.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;


/**
 * The persistent class for the AABAAZCO_TAB database table.
 * 
 */
@Entity
@Table(name="AABAAZCO_TAB")
@NamedQuery(name="AabaazcoTab.findAll", query="SELECT a FROM AabaazcoTab a")
public class AabaazcoTab implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_AZCO")
	@SequenceGenerator(name = "AABAAZCO_GENERATOR", sequenceName = "AABAAZCO_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AABAAZCO_GENERATOR")		
	private long idAzco;

	@Column(name="CODI_CARI")
	private String codiCari;

	private String cuaa;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_AGGI")
	private Date dataAggi;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_FINE_VALI")
	private Date dataFineVali;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_INIZ_CARI")
	private Date dataInizCari;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_INIZ_VALI")
	private Date dataInizVali;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_INSE")
	private Date dataInse;

	@Column(name="FLAG_VISURA")
	private BigDecimal flagVisura;

	@Version
	@Column(name = "VERSIONE")
	private Integer versione;

	//bi-directional many-to-one association to Aabaall2Tab
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_ALL2")
	private Aabaall2Tab aabaall2Tab;

	public AabaazcoTab() {
	}

	public long getIdAzco() {
		return this.idAzco;
	}

	public void setIdAzco(long idAzco) {
		this.idAzco = idAzco;
	}

	public String getCodiCari() {
		return this.codiCari;
	}

	public void setCodiCari(String codiCari) {
		this.codiCari = codiCari;
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

	public Date getDataInizCari() {
		return this.dataInizCari;
	}

	public void setDataInizCari(Date dataInizCari) {
		this.dataInizCari = dataInizCari;
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

	public BigDecimal getFlagVisura() {
		return this.flagVisura;
	}

	public void setFlagVisura(BigDecimal flagVisura) {
		this.flagVisura = flagVisura;
	}

	public Integer getVersione() {
		return this.versione;
	}

	public void setVersione(Integer versione) {
		this.versione = versione;
	}

	public Aabaall2Tab getAabaall2Tab() {
		return this.aabaall2Tab;
	}

	public void setAabaall2Tab(Aabaall2Tab aabaall2Tab) {
		this.aabaall2Tab = aabaall2Tab;
	}

}