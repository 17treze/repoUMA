package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "A4GT_PERSONA", uniqueConstraints = {@UniqueConstraint(columnNames={"ID_VALIDAZIONE", "CODICE_FISCALE"})})
public abstract class PersonaModel extends EntitaDominioFascicolo {

	private static final long serialVersionUID = 1208656086021401560L;
	
	@Column(name = "CODICE_FISCALE", length = 16)
	protected String codiceFiscale;
	
	@OneToMany(mappedBy = "persona", fetch = FetchType.LAZY)
	private List<IscrizioneSezioneModel> iscrizioniSezione;
	
	@OneToMany(mappedBy = "persona", fetch = FetchType.LAZY)
	private List<UnitaTecnicoEconomicheModel> unitaTecnicoEconomiche;

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public PersonaModel setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
		return this;
	}

	public List<IscrizioneSezioneModel> getIscrizioniSezione() {
		return iscrizioniSezione;
	}

	public void setIscrizioniSezione(List<IscrizioneSezioneModel> iscrizioniSezione) {
		this.iscrizioniSezione = iscrizioniSezione;
	}
	
	public List<UnitaTecnicoEconomicheModel> getUnitaTecnicoEconomiche() {
		return unitaTecnicoEconomiche;
	}

	public void setUnitaTecnicoEconomiche(List<UnitaTecnicoEconomicheModel> unitaTecnicoEconomiche) {
		this.unitaTecnicoEconomiche = unitaTecnicoEconomiche;
	}

	public abstract List<CaricaModel> getCariche();

	public abstract void setCariche(List<CaricaModel> cariche);
        
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		PersonaModel that = (PersonaModel) o;
		return Objects.equals(codiceFiscale, that.codiceFiscale);
	}

	@Override
	public int hashCode() {
		return Objects.hash(codiceFiscale);
	}
}
