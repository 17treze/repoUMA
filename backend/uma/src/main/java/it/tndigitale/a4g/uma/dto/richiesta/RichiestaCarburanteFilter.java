package it.tndigitale.a4g.uma.dto.richiesta;

import java.util.List;
import java.util.Set;

import it.tndigitale.a4g.uma.business.persistence.entity.StatoRichiestaCarburante;

public class RichiestaCarburanteFilter {

	private Long id;
	private String cuaa;
	private List<Long> campagna;
	private Set<StatoRichiestaCarburante> stati;
	private String denominazione;

	public String getCuaa() {
		return cuaa;
	}
	public RichiestaCarburanteFilter setCuaa(String cuaa) {
		this.cuaa = cuaa;
		return this;
	}
	public Set<StatoRichiestaCarburante> getStati() {
		return stati;
	}
	public RichiestaCarburanteFilter setStati(Set<StatoRichiestaCarburante> stati) {
		this.stati = stati;
		return this;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public RichiestaCarburanteFilter setDenominazione(String denominazione) {
		this.denominazione = denominazione;
		return this;
	}
	public Long getId() {
		return id;
	}
	public RichiestaCarburanteFilter setId(Long id) {
		this.id = id;
		return this;
	}
	public List<Long> getCampagna() {
		return campagna;
	}
	public RichiestaCarburanteFilter setCampagna(List<Long> campagna) {
		this.campagna = campagna;
		return this;
	}
}
