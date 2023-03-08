package it.tndigitale.a4g.fascicolo.antimafia;

import it.tndigitale.a4g.framework.security.model.Ruolo;

public enum Ruoli implements Ruolo {

	
	RICERCA_FASCICOLO_FILTRO_ENTE(Ruoli.RICERCA_FASCICOLO_FILTRO_ENTE_COD),
	RICERCA_FASCICOLO_NON_FILTRATA(Ruoli.RICERCA_FASCICOLO_NON_FILTRATA_COD),
	RICERCA_FASCICOLO_FILTRO_UTENTE(Ruoli.RICERCA_FASCICOLO_FILTRO_UTENTE_COD),
	RICERCA_ANTIMAFIA_TUTTI(Ruoli.RICERCA_ANTIMAFIA_TUTTI_COD),
	RICERCA_ANTIMAFIA_ENTE(Ruoli.RICERCA_ANTIMAFIA_ENTE_COD),
	// Possibilita di compilare i dati antimafia per le aziende dell'utente
	RICERCA_ANTIMAFIA_UTENTE(Ruoli.RICERCA_ANTIMAFIA_UTENTE_COD),
	// Possibilita di compilare i dati antimafia per tutte le aziende
	EDITA_ANTIMAFIA_TUTTI(Ruoli.EDITA_ANTIMAFIA_TUTTI_COD),
	// Possibilita di compilare i dati antimafia per le aziende collegate all'ente
	EDITA_ANTIMAFIA_ENTE(Ruoli.EDITA_ANTIMAFIA_ENTE_COD),
	EDITA_ANTIMAFIA_UTENTE(Ruoli.EDITA_ANTIMAFIA_UTENTE_COD),
	CANCELLA_ANTIMAFIA_TUTTI(Ruoli.CANCELLA_ANTIMAFIA_TUTTI_COD),
	CANCELLA_ANTIMAFIA_ENTE(Ruoli.CANCELLA_ANTIMAFIA_ENTE_COD),
	CANCELLA_ANTIMAFIA_UTENTE(Ruoli.CANCELLA_ANTIMAFIA_UTENTE_COD);
	

	private String codiceRuolo;
	

	private Ruoli(String codiceRuolo) {
		this.codiceRuolo = codiceRuolo;
	}
	
	public String getCodiceRuolo() {
		return codiceRuolo;
	}
	
	
	public static final String RICERCA_FASCICOLO_FILTRO_ENTE_COD = "a4gfascicolo.fascicolo.ricerca.ente";
	public static final String RICERCA_FASCICOLO_NON_FILTRATA_COD = "a4gfascicolo.fascicolo.ricerca.tutti";
	public static final String RICERCA_FASCICOLO_FILTRO_UTENTE_COD = "a4gfascicolo.fascicolo.ricerca.utente";
	public static final String RICERCA_ANTIMAFIA_TUTTI_COD = "a4gfascicolo.antimafia.ricerca.tutti";
	public static final String RICERCA_ANTIMAFIA_ENTE_COD = "a4gfascicolo.antimafia.ricerca.ente";
	public static final String RICERCA_ANTIMAFIA_UTENTE_COD = "a4gfascicolo.antimafia.ricerca.utente";
	public static final String EDITA_ANTIMAFIA_TUTTI_COD = "a4gfascicolo.antimafia.edita.tutti";
	public static final String EDITA_ANTIMAFIA_ENTE_COD = "a4gfascicolo.antimafia.edita.ente";
	public static final String EDITA_ANTIMAFIA_UTENTE_COD = "a4gfascicolo.antimafia.edita.utente";
	public static final String CANCELLA_ANTIMAFIA_TUTTI_COD = "a4gfascicolo.antimafia.cancella.tutti";
	public static final String CANCELLA_ANTIMAFIA_ENTE_COD = "a4gfascicolo.antimafia.cancella.ente";
	public static final String CANCELLA_ANTIMAFIA_UTENTE_COD = "a4gfascicolo.antimafia.cancella.utente";
}
