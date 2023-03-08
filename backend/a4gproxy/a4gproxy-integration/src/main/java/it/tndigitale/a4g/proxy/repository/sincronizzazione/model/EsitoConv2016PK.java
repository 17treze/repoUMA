package it.tndigitale.a4g.proxy.repository.sincronizzazione.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The primary key class for the ESITO_CONV_2016 database table.
 * 
 */
@Embeddable
public class EsitoConv2016PK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="CODI_ATTO_OOPR")
	private String codiAttoOopr;

	@Column(name="ID_ATTO_AMMI")
	private long idAttoAmmi;

	@Column(name="CODI_FISC")
	private String codiFisc;

	@Column(name="NUME_CAMP")
	private long numeCamp;

	@Column(name="CODI_CAMP")
	private long codiCamp;

	@Column(name="FLAG_CONDIZ")
	private String flagCondiz;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_INIZ_VALI")
	private java.util.Date dataInizVali;

	public EsitoConv2016PK() {
	}
	public String getCodiAttoOopr() {
		return this.codiAttoOopr;
	}
	public void setCodiAttoOopr(String codiAttoOopr) {
		this.codiAttoOopr = codiAttoOopr;
	}
	public long getIdAttoAmmi() {
		return this.idAttoAmmi;
	}
	public void setIdAttoAmmi(long idAttoAmmi) {
		this.idAttoAmmi = idAttoAmmi;
	}
	public String getCodiFisc() {
		return this.codiFisc;
	}
	public void setCodiFisc(String codiFisc) {
		this.codiFisc = codiFisc;
	}
	public long getNumeCamp() {
		return this.numeCamp;
	}
	public void setNumeCamp(long numeCamp) {
		this.numeCamp = numeCamp;
	}
	public long getCodiCamp() {
		return this.codiCamp;
	}
	public void setCodiCamp(long codiCamp) {
		this.codiCamp = codiCamp;
	}
	public String getFlagCondiz() {
		return this.flagCondiz;
	}
	public void setFlagCondiz(String flagCondiz) {
		this.flagCondiz = flagCondiz;
	}
	public java.util.Date getDataInizVali() {
		return this.dataInizVali;
	}
	public void setDataInizVali(java.util.Date dataInizVali) {
		this.dataInizVali = dataInizVali;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof EsitoConv2016PK)) {
			return false;
		}
		EsitoConv2016PK castOther = (EsitoConv2016PK)other;
		return 
			this.codiAttoOopr.equals(castOther.codiAttoOopr)
			&& (this.idAttoAmmi == castOther.idAttoAmmi)
			&& this.codiFisc.equals(castOther.codiFisc)
			&& (this.numeCamp == castOther.numeCamp)
			&& (this.codiCamp == castOther.codiCamp)
			&& this.flagCondiz.equals(castOther.flagCondiz)
			&& this.dataInizVali.equals(castOther.dataInizVali);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.codiAttoOopr.hashCode();
		hash = hash * prime + ((int) (this.idAttoAmmi ^ (this.idAttoAmmi >>> 32)));
		hash = hash * prime + this.codiFisc.hashCode();
		hash = hash * prime + ((int) (this.numeCamp ^ (this.numeCamp >>> 32)));
		hash = hash * prime + ((int) (this.codiCamp ^ (this.codiCamp >>> 32)));
		hash = hash * prime + this.flagCondiz.hashCode();
		hash = hash * prime + this.dataInizVali.hashCode();
		
		return hash;
	}
}