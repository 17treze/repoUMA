package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo;

@Entity
@Table(name = "A4GT_DESTINAZIONE_USO")
public class DestinazioneUsoModel extends EntitaDominioFascicolo {

	private static final long serialVersionUID = 2261567843022158179L;

	@Column(name = "DESCRIZIONE", length = 100)
	private String descrizione;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_UNITA_TECNICO_ECONOMICHE")
	@JoinColumn(name = "UNITA_TECNICO_ECONOMICHE_IDVAL")
	private UnitaTecnicoEconomicheModel unitaTecnicoEconomiche;

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public UnitaTecnicoEconomicheModel getUnitaTecnicoEconomiche() {
		return unitaTecnicoEconomiche;
	}

	public void setUnitaTecnicoEconomiche(UnitaTecnicoEconomicheModel unitaTecnicoEconomiche) {
		this.unitaTecnicoEconomiche = unitaTecnicoEconomiche;
	}

	@Override
	public int hashCode() {
		return Objects
				.hash(descrizione, unitaTecnicoEconomiche);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj
				.getClass())
			return false;
		DestinazioneUsoModel other = (DestinazioneUsoModel) obj;
		return Objects
				.equals(descrizione, other.descrizione)
				&& Objects
				.equals(unitaTecnicoEconomiche, other.unitaTecnicoEconomiche);
	}


}
