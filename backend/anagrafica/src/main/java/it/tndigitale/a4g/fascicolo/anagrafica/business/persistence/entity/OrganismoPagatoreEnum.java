package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

public enum OrganismoPagatoreEnum {
	APPAG("Agenzia Provinciale Per i Pagamenti"),
	ALTRO_OP("Altro organismo pagatore");

	private String denominazione;

	private OrganismoPagatoreEnum(String denominazione) {
		this.denominazione = denominazione;
	}

	public String getDenominazione() {
		return denominazione;
	}
}
