package it.tndigitale.a4g.fascicolo.territorio.api;

import it.tndigitale.a4g.framework.security.model.Ruolo;

public enum Ruoli implements Ruolo {
	RICERCA_FASCICOLO_ENTE(Ruoli.RICERCA_FASCICOLO_ENTE_COD),
	RICERCA_FASCICOLO_TUTTI(Ruoli.RICERCA_FASCICOLO_TUTTI_COD),
	RICERCA_FASCICOLO_UTENTE(Ruoli.RICERCA_FASCICOLO_UTENTE_COD);

	private static final String RICERCA_FASCICOLO_ENTE_COD = "a4gfascicolo.fascicolo.ricerca.ente";
	private static final String RICERCA_FASCICOLO_TUTTI_COD = "a4gfascicolo.fascicolo.ricerca.tutti";
	private static final String RICERCA_FASCICOLO_UTENTE_COD = "a4gfascicolo.fascicolo.ricerca.utente";
	
	private String codiceRuolo;

	Ruoli(final String codiceRuolo) {
		this.codiceRuolo = codiceRuolo;
	}
	
	public String getCodiceRuolo() {
		return codiceRuolo;
	}
}
