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
 * The persistent class for the AABAGARE_TAB database table.
 * 
 */
@Entity
@Table(name = "AABAGARE_TAB", schema = "SUAGS04")
@NamedQuery(name = "AabagareTab.findAll", query = "SELECT a FROM AabagareTab a")
public class AabagareTab implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_GARE")
	private long idGare;

	@Column(name = "ANNO_CAMP")
	private BigDecimal annoCamp;

	@Column(name = "COD_FISC")
	private String codFisc;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_CARI_REGI")
	private Date dataCariRegi;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_FINE_ASSE")
	private Date dataFineAsse;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_FINE_POSS_REQU")
	private Date dataFinePossRequ;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_INIZ_ASSE")
	private Date dataInizAsse;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_INIZ_POSS_REQU")
	private Date dataInizPossRequ;

	@Column(name = "FLAG_GIOV_AGRI")
	private BigDecimal flagGiovAgri;

	@Column(name = "ID_ORPA")
	private BigDecimal idOrpa;

	@Column(name = "ID_SOGG")
	private BigDecimal idSogg;

	@Column(name = "TIPO_REGI_GA")
	private BigDecimal tipoRegiGa;

	@Column(name = "USER_NAME_AGGI")
	private String userNameAggi;

	@Column(name = "USER_NAME_INSE")
	private String userNameInse;

	public AabagareTab() {
	}

	public long getIdGare() {
		return this.idGare;
	}

	public void setIdGare(long idGare) {
		this.idGare = idGare;
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

	public Date getDataCariRegi() {
		return this.dataCariRegi;
	}

	public void setDataCariRegi(Date dataCariRegi) {
		this.dataCariRegi = dataCariRegi;
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

	public BigDecimal getTipoRegiGa() {
		return this.tipoRegiGa;
	}

	public void setTipoRegiGa(BigDecimal tipoRegiGa) {
		this.tipoRegiGa = tipoRegiGa;
	}

	public String getUserNameAggi() {
		return this.userNameAggi;
	}

	public void setUserNameAggi(String userNameAggi) {
		this.userNameAggi = userNameAggi;
	}

	public String getUserNameInse() {
		return this.userNameInse;
	}

	public void setUserNameInse(String userNameInse) {
		this.userNameInse = userNameInse;
	}

}