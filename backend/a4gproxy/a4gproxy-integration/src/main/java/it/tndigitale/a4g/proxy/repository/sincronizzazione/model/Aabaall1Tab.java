package it.tndigitale.a4g.proxy.repository.sincronizzazione.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;


/**
 * The persistent class for the AABAALL1_TAB database table.
 * 
 */
@Entity
@Table(name="AABAALL1_TAB")
@NamedQuery(name="Aabaall1Tab.findAll", query="SELECT a FROM Aabaall1Tab a")
public class Aabaall1Tab implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_ALL1")
	@SequenceGenerator(name = "AABAALL1_GENERATOR", sequenceName = "AABAALL1_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AABAALL1_GENERATOR")		
	private long idAll1;

	@Column(name="CODI_BELF_NASC")
	private String codiBelfNasc;

	@Column(name="CODI_BELF_RECA")
	private String codiBelfReca;

	@Column(name="CODI_CAPP_RECA")
	private String codiCappReca;

	@Column(name="CODI_FISC")
	private String codiFisc;

	@Column(name="CODI_NATU_GIUR")
	private String codiNatuGiur;

	@Column(name="CODI_NUME_ISCR")
	private String codiNumeIscr;

	@Column(name="CODI_PART_IVAA")
	private String codiPartIvaa;

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
	@Column(name="DATA_INSE")
	private Date dataInse;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_ISCR")
	private Date dataIscr;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_NASC")
	private Date dataNasc;

	@Column(name="DESC_COGN")
	private String descCogn;

	@Column(name="DESC_COMU_NASC")
	private String descComuNasc;

	@Column(name="DESC_COMU_RECA")
	private String descComuReca;

	@Column(name="DESC_IMPR")
	private String descImpr;

	@Column(name="DESC_INDI_RECA")
	private String descIndiReca;

	@Column(name="DESC_NOME")
	private String descNome;

	@Column(name="DESC_OGGE_SOCI")
	private String descOggeSoci;

	@Column(name="DESC_PEC")
	private String descPec;

	@Column(name="DESC_PROV_IMPR")
	private String descProvImpr;

	@Column(name="NUME_CIVI_RECA")
	private String numeCiviReca;
	
	@Version
	@Column(name = "VERSIONE")
	private Integer versione;

	//bi-directional many-to-one association to Aabaall3Tab
	@OneToMany(mappedBy="aabaall1Tab")
	private Set<Aabaall3Tab> aabaall3Tabs;

	//bi-directional many-to-one association to AabasgcaTab
	@OneToMany(mappedBy="aabaall1Tab")
	private Set<AabasgcaTab> aabasgcaTabs;

	public Aabaall1Tab() {
	}

	public long getIdAll1() {
		return this.idAll1;
	}

	public void setIdAll1(long idAll1) {
		this.idAll1 = idAll1;
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

	public String getCodiFisc() {
		return this.codiFisc;
	}

	public void setCodiFisc(String codiFisc) {
		this.codiFisc = codiFisc;
	}

	public String getCodiNatuGiur() {
		return this.codiNatuGiur;
	}

	public void setCodiNatuGiur(String codiNatuGiur) {
		this.codiNatuGiur = codiNatuGiur;
	}

	public String getCodiNumeIscr() {
		return this.codiNumeIscr;
	}

	public void setCodiNumeIscr(String codiNumeIscr) {
		this.codiNumeIscr = codiNumeIscr;
	}

	public String getCodiPartIvaa() {
		return this.codiPartIvaa;
	}

	public void setCodiPartIvaa(String codiPartIvaa) {
		this.codiPartIvaa = codiPartIvaa;
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

	public Date getDataInse() {
		return this.dataInse;
	}

	public void setDataInse(Date dataInse) {
		this.dataInse = dataInse;
	}

	public Date getDataIscr() {
		return this.dataIscr;
	}

	public void setDataIscr(Date dataIscr) {
		this.dataIscr = dataIscr;
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

	public String getDescImpr() {
		return this.descImpr;
	}

	public void setDescImpr(String descImpr) {
		this.descImpr = descImpr;
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

	public String getDescOggeSoci() {
		return this.descOggeSoci;
	}

	public void setDescOggeSoci(String descOggeSoci) {
		this.descOggeSoci = descOggeSoci;
	}

	public String getDescPec() {
		return this.descPec;
	}

	public void setDescPec(String descPec) {
		this.descPec = descPec;
	}

	public String getDescProvImpr() {
		return this.descProvImpr;
	}

	public void setDescProvImpr(String descProvImpr) {
		this.descProvImpr = descProvImpr;
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

	public Set<Aabaall3Tab> getAabaall3Tabs() {
		return this.aabaall3Tabs;
	}

	public void setAabaall3Tabs(Set<Aabaall3Tab> aabaall3Tabs) {
		this.aabaall3Tabs = aabaall3Tabs;
	}

	public Aabaall3Tab addAabaall3Tab(Aabaall3Tab aabaall3Tab) {
		getAabaall3Tabs().add(aabaall3Tab);
		aabaall3Tab.setAabaall1Tab(this);

		return aabaall3Tab;
	}

	public Aabaall3Tab removeAabaall3Tab(Aabaall3Tab aabaall3Tab) {
		getAabaall3Tabs().remove(aabaall3Tab);
		aabaall3Tab.setAabaall1Tab(null);

		return aabaall3Tab;
	}

	public Set<AabasgcaTab> getAabasgcaTabs() {
		return this.aabasgcaTabs;
	}

	public void setAabasgcaTabs(Set<AabasgcaTab> aabasgcaTabs) {
		this.aabasgcaTabs = aabasgcaTabs;
	}

	public AabasgcaTab addAabasgcaTab(AabasgcaTab aabasgcaTab) {
		getAabasgcaTabs().add(aabasgcaTab);
		aabasgcaTab.setAabaall1Tab(this);

		return aabasgcaTab;
	}

	public AabasgcaTab removeAabasgcaTab(AabasgcaTab aabasgcaTab) {
		getAabasgcaTabs().remove(aabasgcaTab);
		aabasgcaTab.setAabaall1Tab(null);

		return aabasgcaTab;
	}

}