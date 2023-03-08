package it.tndigitale.a4g.proxy.repository.sincronizzazione.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the ESITO_CONV_2016 database table.
 * 
 */
@Entity
@Table(name="ESITO_CONV_2016")
@NamedQuery(name="EsitoConv2016.findAll", query="SELECT e FROM EsitoConv2016 e")
public class EsitoConv2016 implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private EsitoConv2016PK id;

	@Column(name="CODI_OOPR")
	private String codiOopr;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_FINE_VALI")
	private Date dataFineVali;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_INIZ_CONV")
	private Date dataInizConv;

	@Column(name="FLAG_AMMISS")
	private String flagAmmiss;

	@Column(name="FLAG_CONV")
	private String flagConv;

	@Column(name="SEDE_CONV")
	private String sedeConv;

	public EsitoConv2016() {
	}

	public EsitoConv2016PK getId() {
		return this.id;
	}

	public void setId(EsitoConv2016PK id) {
		this.id = id;
	}

	public String getCodiOopr() {
		return this.codiOopr;
	}

	public void setCodiOopr(String codiOopr) {
		this.codiOopr = codiOopr;
	}

	public Date getDataFineVali() {
		return this.dataFineVali;
	}

	public void setDataFineVali(Date dataFineVali) {
		this.dataFineVali = dataFineVali;
	}

	public Date getDataInizConv() {
		return this.dataInizConv;
	}

	public void setDataInizConv(Date dataInizConv) {
		this.dataInizConv = dataInizConv;
	}

	public String getFlagAmmiss() {
		return this.flagAmmiss;
	}

	public void setFlagAmmiss(String flagAmmiss) {
		this.flagAmmiss = flagAmmiss;
	}

	public String getFlagConv() {
		return this.flagConv;
	}

	public void setFlagConv(String flagConv) {
		this.flagConv = flagConv;
	}

	public String getSedeConv() {
		return this.sedeConv;
	}

	public void setSedeConv(String sedeConv) {
		this.sedeConv = sedeConv;
	}

}