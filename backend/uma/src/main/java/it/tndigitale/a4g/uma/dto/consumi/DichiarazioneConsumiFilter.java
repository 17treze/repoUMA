package it.tndigitale.a4g.uma.dto.consumi;

import java.util.List;
import java.util.Set;

import it.tndigitale.a4g.uma.business.persistence.entity.StatoDichiarazioneConsumi;

public class DichiarazioneConsumiFilter {

	private Long id;
	private String cuaa;
	private String denominazione;
	private List<Long> campagna; 
	private Set<StatoDichiarazioneConsumi> stati;

	public Long getId() {
		return id;
	}
	public DichiarazioneConsumiFilter setId(Long id) {
		this.id = id;
		return this;
	}
	public String getCuaa() {
		return cuaa;
	}
	public DichiarazioneConsumiFilter setCuaa(String cuaa) {
		this.cuaa = cuaa;
		return this;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public DichiarazioneConsumiFilter setDenominazione(String denominazione) {
		this.denominazione = denominazione;
		return this;
	}
	public List<Long> getCampagna() {
		return campagna;
	}
	public DichiarazioneConsumiFilter setCampagna(List<Long> campagna) {
		this.campagna = campagna;
		return this;
	}
	public Set<StatoDichiarazioneConsumi> getStati() {
		return stati;
	}
	public DichiarazioneConsumiFilter setStati(Set<StatoDichiarazioneConsumi> stati) {
		this.stati = stati;
		return this;
	}
}
