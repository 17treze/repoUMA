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
 * The persistent class for the AABASGCA_TAB database table.
 * 
 */
@Entity
@Table(name="AABASGCA_TAB")
@NamedQuery(name="AabasgcaTab.findAll", query="SELECT a FROM AabasgcaTab a")
public class AabasgcaTab implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_SGCA")
	@SequenceGenerator(name = "AABASGCA_GENERATOR", sequenceName = "AABASGCA_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AABASGCA_GENERATOR")		
	private long idSgca;

	@Column(name="CODI_BELF_NASC")
	private String codiBelfNasc;

	@Column(name="CODI_BELF_RECA")
	private String codiBelfReca;

	@Column(name="CODI_CAPP_RECA")
	private String codiCappReca;

	@Column(name="CODI_CARI")
	private String codiCari;

	@Column(name="CODI_NATU_GIUR")
	private String codiNatuGiur;

	@Column(name="CODI_SEX")
	private String codiSex;

	@Column(name="CODI_SIGL_PROV_NASC")
	private String codiSiglProvNasc;

	@Column(name="CODI_SIGL_PROV_RECA")
	private String codiSiglProvReca;

	private String cuaa;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_AGGI")
	private Date dataAggi;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_INIZ_VALI")
	private Date dataInizVali;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_INSE")
	private Date dataInse;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_NASC")
	private Date dataNasc;

	@Column(name="DESC_COGN")
	private String descCogn;

	@Column(name="DESC_COMU_NASC")
	private String descComuNasc;

	@Column(name="DESC_COMU_RECA")
	private String descComuReca;

	@Column(name="DESC_INDI_RECA")
	private String descIndiReca;

	@Column(name="DESC_NOME")
	private String descNome;

	@Column(name="FLAG_VISURA")
	private BigDecimal flagVisura;

	@Column(name="NUME_CIVI_RECA")
	private String numeCiviReca;

	@Version
	@Column(name = "VERSIONE")
	private Integer versione;

	//bi-directional many-to-one association to Aabaall1Tab
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_ALL1")
	private Aabaall1Tab aabaall1Tab;

	//bi-directional many-to-one association to Aabaall2Tab
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_ALL2")
	private Aabaall2Tab aabaall2Tab;

	public AabasgcaTab() {
	}

	public long getIdSgca() {
		return this.idSgca;
	}

	public void setIdSgca(long idSgca) {
		this.idSgca = idSgca;
	}

	public String getCodiBelfNasc() {
		return this.codiBelfNasc;
	}

	public void setCodiBelfNasc(String codiBelfNasc) {
		this.codiBelfNasc = codiBelfNasc;
	}

	public String getCodiBelfReca() {
		return this.codiBelfReca;
	}

	public void setCodiBelfReca(String codiBelfReca) {
		this.codiBelfReca = codiBelfReca;
	}

	public String getCodiCappReca() {
		return this.codiCappReca;
	}

	public void setCodiCappReca(String codiCappReca) {
		this.codiCappReca = codiCappReca;
	}

	public String getCodiCari() {
		return this.codiCari;
	}

	public void setCodiCari(String codiCari) {
		this.codiCari = codiCari;
	}

	public String getCodiNatuGiur() {
		return this.codiNatuGiur;
	}

	public void setCodiNatuGiur(String codiNatuGiur) {
		this.codiNatuGiur = codiNatuGiur;
	}

	public String getCodiSex() {
		return this.codiSex;
	}

	public void setCodiSex(String codiSex) {
		this.codiSex = codiSex;
	}

	public String getCodiSiglProvNasc() {
		return this.codiSiglProvNasc;
	}

	public void setCodiSiglProvNasc(String codiSiglProvNasc) {
		this.codiSiglProvNasc = codiSiglProvNasc;
	}

	public String getCodiSiglProvReca() {
		return this.codiSiglProvReca;
	}

	public void setCodiSiglProvReca(String codiSiglProvReca) {
		this.codiSiglProvReca = codiSiglProvReca;
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

	public Date getDataNasc() {
		return this.dataNasc;
	}

	public void setDataNasc(Date dataNasc) {
		this.dataNasc = dataNasc;
	}

	public String getDescCogn() {
		return this.descCogn;
	}

	public void setDescCogn(String descCogn) {
		this.descCogn = descCogn;
	}

	public String getDescComuNasc() {
		return this.descComuNasc;
	}

	public void setDescComuNasc(String descComuNasc) {
		this.descComuNasc = descComuNasc;
	}

	public String getDescComuReca() {
		return this.descComuReca;
	}

	public void setDescComuReca(String descComuReca) {
		this.descComuReca = descComuReca;
	}

	public String getDescIndiReca() {
		return this.descIndiReca;
	}

	public void setDescIndiReca(String descIndiReca) {
		this.descIndiReca = descIndiReca;
	}

	public String getDescNome() {
		return this.descNome;
	}

	public void setDescNome(String descNome) {
		this.descNome = descNome;
	}

	public BigDecimal getFlagVisura() {
		return this.flagVisura;
	}

	public void setFlagVisura(BigDecimal flagVisura) {
		this.flagVisura = flagVisura;
	}

	public String getNumeCiviReca() {
		return this.numeCiviReca;
	}

	public void setNumeCiviReca(String numeCiviReca) {
		this.numeCiviReca = numeCiviReca;
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

	public Aabaall2Tab getAabaall2Tab() {
		return this.aabaall2Tab;
	}

	public void setAabaall2Tab(Aabaall2Tab aabaall2Tab) {
		this.aabaall2Tab = aabaall2Tab;
	}

}