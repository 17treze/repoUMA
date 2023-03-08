package it.tndigitale.a4g.proxy.repository.sincronizzazione.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The primary key class for the INVIATE_CCOO_2016 database table.
 * 
 */
@Embeddable
public class InviateCcoo2016PK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ID_DOM_CCOO")
	private long idDomCcoo;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_INIZ_VALI")
	private java.util.Date dataInizVali;

	public InviateCcoo2016PK() {
	}
	public long getIdDomCcoo() {
		return this.idDomCcoo;
	}
	public void setIdDomCcoo(long idDomCcoo) {
		this.idDomCcoo = idDomCcoo;
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
		if (!(other instanceof InviateCcoo2016PK)) {
			return false;
		}
		InviateCcoo2016PK castOther = (InviateCcoo2016PK)other;
		return 
			(this.idDomCcoo == castOther.idDomCcoo)
			&& this.dataInizVali.equals(castOther.dataInizVali);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.idDomCcoo ^ (this.idDomCcoo >>> 32)));
		hash = hash * prime + this.dataInizVali.hashCode();
		
		return hash;
	}
}