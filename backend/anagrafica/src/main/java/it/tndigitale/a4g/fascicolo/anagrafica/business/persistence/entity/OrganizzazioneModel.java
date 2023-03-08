package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;


@Entity
@Table(name = "A4GT_ORGANIZZAZIONE")
public class OrganizzazioneModel extends EntitaDominio {
	private static final long serialVersionUID = 5467395124981139407L;
	
	@Column(name = "DENOMINAZIONE", length = 100, nullable = false)
	private String denominazione;
	
	@OneToMany(mappedBy = "organizzazione", fetch = FetchType.LAZY)
    private Set<FascicoloOrganizzazioneModel> fascicoloOrganizzazioni;

	public String getDenominazione() {
		return denominazione;
	}

	public OrganizzazioneModel setDenominazione(String denominazione) {
		this.denominazione = denominazione;
		return this;
	}

	public Set<FascicoloOrganizzazioneModel> getOrganizzazioni() {
		return fascicoloOrganizzazioni;
	}

	public OrganizzazioneModel setOrganizzazioni(Set<FascicoloOrganizzazioneModel> fascicoloOrganizzazioni) {
		this.fascicoloOrganizzazioni = fascicoloOrganizzazioni;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(denominazione);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrganizzazioneModel other = (OrganizzazioneModel) obj;
		return Objects.equals(denominazione, other.denominazione);
	}
}