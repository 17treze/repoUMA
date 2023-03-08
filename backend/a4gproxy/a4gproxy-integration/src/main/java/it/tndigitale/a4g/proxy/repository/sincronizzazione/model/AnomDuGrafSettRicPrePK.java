package it.tndigitale.a4g.proxy.repository.sincronizzazione.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class AnomDuGrafSettRicPrePK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="ID_ATTO_AMMI")
	private long idAttoAmmi;
	
	@Column(name="ID_ANOM")
	private long idAnom;
	
	@Column(name="ID_PARC_AGRI")
	private long idParcAgri;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_INIZ_VALI")
	private java.util.Date dataInizVali;

	public AnomDuGrafSettRicPrePK() {
		// Default empty constructor
	}

	public long getIdAttoAmmi() {
		return idAttoAmmi;
	}

	public void setIdAttoAmmi(long idAttoAmmi) {
		this.idAttoAmmi = idAttoAmmi;
	}

	public long getIdAnom() {
		return idAnom;
	}

	public void setIdAnom(long idAnom) {
		this.idAnom = idAnom;
	}

	public long getIdParcAgri() {
		return idParcAgri;
	}

	public void setIdParcAgri(long idParcAgri) {
		this.idParcAgri = idParcAgri;
	}

	public java.util.Date getDataInizVali() {
		return dataInizVali;
	}

	public void setDataInizVali(java.util.Date dataInizVali) {
		this.dataInizVali = dataInizVali;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataInizVali, idAnom, idAttoAmmi, idParcAgri);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AnomDuGrafSettRicPrePK other = (AnomDuGrafSettRicPrePK) obj;
		return Objects.equals(dataInizVali, other.dataInizVali) && idAnom == other.idAnom
				&& idAttoAmmi == other.idAttoAmmi && idParcAgri == other.idParcAgri;
	}
}
