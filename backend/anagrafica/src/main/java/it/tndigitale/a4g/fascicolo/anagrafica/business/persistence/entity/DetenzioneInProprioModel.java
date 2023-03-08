package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "A4GT_DETENZIONE_IN_PROPRIO")
public class DetenzioneInProprioModel extends DetenzioneModel {
	private static final long serialVersionUID = -5536887496185128304L;

	@Override
	public int hashCode() {
		int result = super.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetenzioneInProprioModel other = (DetenzioneInProprioModel) obj;
		return super.equals(other);
	}

}
