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
 * The persistent class for the AABAFACO_TAB database table.
 * 
 */
@Entity
@Table(name="AABAFACO_TAB")
@NamedQuery(name="AabafacoTab.findAll", query="SELECT a FROM AabafacoTab a")
public class AabafacoTab implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_FACO")
	@SequenceGenerator(name = "AABAFACO_GENERATOR", sequenceName = "AABAFACO_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AABAFACO_GENERATOR")		
	private long idFaco;

	@Column(name="CODI_BELF_NASC")
	private String codiBelfNasc;

	@Column(name="CODI_BELF_RECA")
	private String codiBelfReca;

	@Column(name="CODI_CAPP_RECA")
	private String codiCappReca;

	@Column(name="CODI_GEOG_BELF")
	private String codiGeogBelf;

	@Column(name="CODI_GEOG_CAPP")
	private String codiGeogCapp;

	@Column(name="CODI_GEOG_SIGL_PROV")
	private String codiGeogSiglProv;

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
	@Column(name="DATA_NASC")
	private Date dataNasc;

	@Column(name="DECO_TIPO_PARE")
	private BigDecimal decoTipoPare;

	@Column(name="DESC_COGN")
	private String descCogn;

	@Column(name="DESC_COMU_NASC")
	private String descComuNasc;

	@Column(name="DESC_COMU_RECA")
	private String descComuReca;

	@Column(name="DESC_GEOG_CIVI")
	private String descGeogCivi;

	@Column(name="DESC_GEOG_COMU")
	private String descGeogComu;

	@Column(name="DESC_GEOG_FRAZ")
	private String descGeogFraz;

	@Column(name="DESC_INDI_RECA")
	private String descIndiReca;

	@Column(name="DESC_INDI_STRD")
	private String descIndiStrd;

	@Column(name="DESC_NOME")
	private String descNome;

	@Column(name="NUME_CIVI_RECA")
	private String numeCiviReca;

	@Version
	@Column(name = "VERSIONE")
	private Integer versione;

	//bi-directional many-to-one association to Aabaall3Tab
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_ALL3")
	private Aabaall3Tab aabaall3Tab;

	public AabafacoTab() {
	}

	public long getIdFaco() {
		return this.idFaco;
	}

	public void setIdFaco(long idFaco) {
		this.idFaco = idFaco;
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

	public String getCodiGeogBelf() {
		return this.codiGeogBelf;
	}

	public void setCodiGeogBelf(String codiGeogBelf) {
		this.codiGeogBelf = codiGeogBelf;
	}

	public String getCodiGeogCapp() {
		return this.codiGeogCapp;
	}

	public void setCodiGeogCapp(String codiGeogCapp) {
		this.codiGeogCapp = codiGeogCapp;
	}

	public String getCodiGeogSiglProv() {
		return this.codiGeogSiglProv;
	}

	public void setCodiGeogSiglProv(String codiGeogSiglProv) {
		this.codiGeogSiglProv = codiGeogSiglProv;
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

	public Date getDataNasc() {
		return this.dataNasc;
	}

	public void setDataNasc(Date dataNasc) {
		this.dataNasc = dataNasc;
	}

	public BigDecimal getDecoTipoPare() {
		return this.decoTipoPare;
	}

	public void setDecoTipoPare(BigDecimal decoTipoPare) {
		this.decoTipoPare = decoTipoPare;
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

	public String getDescGeogCivi() {
		return this.descGeogCivi;
	}

	public void setDescGeogCivi(String descGeogCivi) {
		this.descGeogCivi = descGeogCivi;
	}

	public String getDescGeogComu() {
		return this.descGeogComu;
	}

	public void setDescGeogComu(String descGeogComu) {
		this.descGeogComu = descGeogComu;
	}

	public String getDescGeogFraz() {
		return this.descGeogFraz;
	}

	public void setDescGeogFraz(String descGeogFraz) {
		this.descGeogFraz = descGeogFraz;
	}

	public String getDescIndiReca() {
		return this.descIndiReca;
	}

	public void setDescIndiReca(String descIndiReca) {
		this.descIndiReca = descIndiReca;
	}

	public String getDescIndiStrd() {
		return this.descIndiStrd;
	}

	public void setDescIndiStrd(String descIndiStrd) {
		this.descIndiStrd = descIndiStrd;
	}

	public String getDescNome() {
		return this.descNome;
	}

	public void setDescNome(String descNome) {
		this.descNome = descNome;
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

	public Aabaall3Tab getAabaall3Tab() {
		return this.aabaall3Tab;
	}

	public void setAabaall3Tab(Aabaall3Tab aabaall3Tab) {
		this.aabaall3Tab = aabaall3Tab;
	}

}