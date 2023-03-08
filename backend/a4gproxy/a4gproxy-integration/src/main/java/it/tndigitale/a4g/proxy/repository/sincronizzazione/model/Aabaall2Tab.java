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
 * The persistent class for the AABAALL2_TAB database table.
 * 
 */
@Entity
@Table(name="AABAALL2_TAB")
@NamedQuery(name="Aabaall2Tab.findAll", query="SELECT a FROM Aabaall2Tab a")
public class Aabaall2Tab implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_ALL2")
	@SequenceGenerator(name = "AABAALL2_GENERATOR", sequenceName = "AABAALL2_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AABAALL2_GENERATOR")		
	private long idAll2;

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

	@Column(name="DESC_ATTO_COST")
	private String descAttoCost;

	@Column(name="DESC_CAPI_SOCI")
	private String descCapiSoci;

	@Column(name="DESC_COMU_RECA")
	private String descComuReca;

	@Column(name="DESC_DURA_SOCI")
	private String descDuraSoci;

	@Column(name="DESC_FORM_GIUR")
	private String descFormGiur;

	@Column(name="DESC_IMPR")
	private String descImpr;

	@Column(name="DESC_INDI_RECA")
	private String descIndiReca;

	@Column(name="DESC_OGGE_SOCI")
	private String descOggeSoci;

	@Column(name="DESC_PEC")
	private String descPec;

	@Column(name="DESC_PROV_IMPR")
	private String descProvImpr;

	@Column(name="DESC_RAGI_SOCI")
	private String descRagiSoci;

	@Column(name="NUME_CIVI_RECA")
	private String numeCiviReca;

	@Version
	@Column(name = "VERSIONE")
	private Integer versione;

	//bi-directional many-to-one association to AabaazcoTab
	@OneToMany(mappedBy="aabaall2Tab")
	private Set<AabaazcoTab> aabaazcoTabs;

	//bi-directional many-to-one association to AabasgcaTab
	@OneToMany(mappedBy="aabaall2Tab")
	private Set<AabasgcaTab> aabasgcaTabs;

	public Aabaall2Tab() {
	}

	public long getIdAll2() {
		return this.idAll2;
	}

	public void setIdAll2(long idAll2) {
		this.idAll2 = idAll2;
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

	public String getDescAttoCost() {
		return this.descAttoCost;
	}

	public void setDescAttoCost(String descAttoCost) {
		this.descAttoCost = descAttoCost;
	}

	public String getDescCapiSoci() {
		return this.descCapiSoci;
	}

	public void setDescCapiSoci(String descCapiSoci) {
		this.descCapiSoci = descCapiSoci;
	}

	public String getDescComuReca() {
		return this.descComuReca;
	}

	public void setDescComuReca(String descComuReca) {
		this.descComuReca = descComuReca;
	}

	public String getDescDuraSoci() {
		return this.descDuraSoci;
	}

	public void setDescDuraSoci(String descDuraSoci) {
		this.descDuraSoci = descDuraSoci;
	}

	public String getDescFormGiur() {
		return this.descFormGiur;
	}

	public void setDescFormGiur(String descFormGiur) {
		this.descFormGiur = descFormGiur;
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

	public String getDescRagiSoci() {
		return this.descRagiSoci;
	}

	public void setDescRagiSoci(String descRagiSoci) {
		this.descRagiSoci = descRagiSoci;
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

	public Set<AabaazcoTab> getAabaazcoTabs() {
		return this.aabaazcoTabs;
	}

	public void setAabaazcoTabs(Set<AabaazcoTab> aabaazcoTabs) {
		this.aabaazcoTabs = aabaazcoTabs;
	}

	public AabaazcoTab addAabaazcoTab(AabaazcoTab aabaazcoTab) {
		getAabaazcoTabs().add(aabaazcoTab);
		aabaazcoTab.setAabaall2Tab(this);

		return aabaazcoTab;
	}

	public AabaazcoTab removeAabaazcoTab(AabaazcoTab aabaazcoTab) {
		getAabaazcoTabs().remove(aabaazcoTab);
		aabaazcoTab.setAabaall2Tab(null);

		return aabaazcoTab;
	}

	public Set<AabasgcaTab> getAabasgcaTabs() {
		return this.aabasgcaTabs;
	}

	public void setAabasgcaTabs(Set<AabasgcaTab> aabasgcaTabs) {
		this.aabasgcaTabs = aabasgcaTabs;
	}

	public AabasgcaTab addAabasgcaTab(AabasgcaTab aabasgcaTab) {
		getAabasgcaTabs().add(aabasgcaTab);
		aabasgcaTab.setAabaall2Tab(this);

		return aabasgcaTab;
	}

	public AabasgcaTab removeAabasgcaTab(AabasgcaTab aabasgcaTab) {
		getAabasgcaTabs().remove(aabasgcaTab);
		aabasgcaTab.setAabaall2Tab(null);

		return aabasgcaTab;
	}

}