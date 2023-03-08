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

@Entity
@Table(name = "ANOM_DU_GRAF_SETT_RIC_PRE")
@NamedQuery(name = "AnomDuGrafSettRicPre.findAll", query = "SELECT i FROM AnomDuGrafSettRicPre i")
public class AnomDuGrafSettRicPre implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public AnomDuGrafSettRicPre() {
		// Default empty constructor
	}

	@EmbeddedId
	private AnomDuGrafSettRicPrePK id;
	
	@Column(name = "DESC_ANOM")
	private String descAnom;

	@Column(name = "ID_PARC_OP")
	private long idParcOp;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_FINE_VALI")
	private Date dataFineVali;
	
	@Column(name = "CODI_TIPO_ANOM")
	private String codiTipoAnom;
	
	@Column(name = "VALO_ANOM")
	private String valoAnom;
	
	@Column(name = "NUME_ANNO_CAMP")
	private long numeAnnoCamp;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DT_INS")
	private Date dtIns;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DT_UPD")
	private Date dtUpd;

	public AnomDuGrafSettRicPrePK getId() {
		return id;
	}

	public void setId(AnomDuGrafSettRicPrePK id) {
		this.id = id;
	}

	public String getDescAnom() {
		return descAnom;
	}

	public void setDescAnom(String descAnom) {
		this.descAnom = descAnom;
	}

	public long getIdParcOp() {
		return idParcOp;
	}

	public void setIdParcOp(long idParcOp) {
		this.idParcOp = idParcOp;
	}

	public Date getDataFineVali() {
		return dataFineVali;
	}

	public void setDataFineVali(Date dataFineVali) {
		this.dataFineVali = dataFineVali;
	}

	public String getCodiTipoAnom() {
		return codiTipoAnom;
	}

	public void setCodiTipoAnom(String codiTipoAnom) {
		this.codiTipoAnom = codiTipoAnom;
	}

	public String getValoAnom() {
		return valoAnom;
	}

	public void setValoAnom(String valoAnom) {
		this.valoAnom = valoAnom;
	}

	public long getNumeAnnoCamp() {
		return numeAnnoCamp;
	}

	public void setNumeAnnoCamp(long numeAnnoCamp) {
		this.numeAnnoCamp = numeAnnoCamp;
	}

	public Date getDtIns() {
		return dtIns;
	}

	public void setDtIns(Date dtIns) {
		this.dtIns = dtIns;
	}

	public Date getDtUpd() {
		return dtUpd;
	}

	public void setDtUpd(Date dtUpd) {
		this.dtUpd = dtUpd;
	}
}
