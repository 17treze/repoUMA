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
 * The persistent class for the AABAESAN_TAB database table.
 * 
 */
@Entity
@Table(name="AABAESAN_TAB")
@NamedQuery(name="AabaesanTab.findAll", query="SELECT a FROM AabaesanTab a")
public class AabaesanTab implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_ESAN")
	@SequenceGenerator(name = "AABAESAN_GENERATOR", sequenceName = "AABAESAN_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AABAESAN_GENERATOR")	
	private long idEsan;

	private String cuaa;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_FINE_VALI")
	private Date dataFineVali;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_INIZ_VALI")
	private Date dataInizVali;

	@Column(name="DECO_ESIT")
	private BigDecimal decoEsit;

	@Column(name="PROT_ESIT")
	private String protEsit;

	@Version
	@Column(name = "VERSIONE")
	private Integer versione;

	//bi-directional many-to-one association to AabaantiTab
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_ANTI")
	private AabaantiTab aabaantiTab;

	public AabaesanTab() {
	}

	public long getIdEsan() {
		return this.idEsan;
	}

	public void setIdEsan(long idEsan) {
		this.idEsan = idEsan;
	}

	public String getCuaa() {
		return this.cuaa;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
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

	public BigDecimal getDecoEsit() {
		return this.decoEsit;
	}

	public void setDecoEsit(BigDecimal decoEsit) {
		this.decoEsit = decoEsit;
	}

	public String getProtEsit() {
		return this.protEsit;
	}

	public void setProtEsit(String protEsit) {
		this.protEsit = protEsit;
	}

	public Integer getVersione() {
		return this.versione;
	}

	public void setVersione(Integer versione) {
		this.versione = versione;
	}

	public AabaantiTab getAabaantiTab() {
		return this.aabaantiTab;
	}

	public void setAabaantiTab(AabaantiTab aabaantiTab) {
		this.aabaantiTab = aabaantiTab;
	}

}