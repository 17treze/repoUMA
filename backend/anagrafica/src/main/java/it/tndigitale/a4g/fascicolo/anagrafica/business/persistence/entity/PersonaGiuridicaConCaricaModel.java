package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo;


@Entity
@Table(name = "A4GT_PERSONA_GIURIDICA_CARICA", uniqueConstraints = {@UniqueConstraint(columnNames={"ID_VALIDAZIONE", "CODICE_FISCALE"})})
public class PersonaGiuridicaConCaricaModel extends EntitaDominioFascicolo {
	
	private static final long serialVersionUID = 780015391733297324L;
	
	@Column(name = "CODICE_FISCALE", length = 16)
	protected String codiceFiscale;

	@Column(name = "DENOMINAZIONE", length = 2048)
	private String denominazione;
	
	@OneToMany(mappedBy="personaGiuridicaConCaricaModel")
	private List<CaricaModel> cariche;

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public PersonaGiuridicaConCaricaModel setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
		return this;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public PersonaGiuridicaConCaricaModel setDenominazione(String denominazione) {
		this.denominazione = denominazione;
		return this;
	}

	public List<CaricaModel> getCariche() {
		return cariche;
	}

	public void setCariche(List<CaricaModel> cariche) {
		this.cariche = cariche;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cariche, codiceFiscale, denominazione);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonaGiuridicaConCaricaModel other = (PersonaGiuridicaConCaricaModel) obj;
		return Objects.equals(cariche, other.cariche) && Objects.equals(codiceFiscale, other.codiceFiscale)
				&& Objects.equals(denominazione, other.denominazione);
	}


}
