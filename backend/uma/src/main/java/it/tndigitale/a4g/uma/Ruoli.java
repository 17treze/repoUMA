package it.tndigitale.a4g.uma;

import it.tndigitale.a4g.framework.security.model.Ruolo;

public enum Ruoli implements Ruolo {

	DOMANDE_UMA_EDITA_ENTE(Ruoli.DOMANDE_UMA_EDITA_ENTE_COD),
	DOMANDE_UMA_EDITA_UTENTE(Ruoli.DOMANDE_UMA_EDITA_UTENTE_COD),

	DOMANDE_UMA_RICERCA_ENTE(Ruoli.DOMANDE_UMA_RICERCA_ENTE_COD),
	DOMANDE_UMA_RICERCA_UTENTE(Ruoli.DOMANDE_UMA_RICERCA_UTENTE_COD),
	DOMANDE_UMA_RICERCA_TUTTI(Ruoli.DOMANDE_UMA_RICERCA_TUTTI_COD),

	DOMANDE_UMA_CANCELLA_ENTE(Ruoli.DOMANDE_UMA_CANCELLA_ENTE_COD),
	DOMANDE_UMA_CANCELLA_UTENTE(Ruoli.DOMANDE_UMA_CANCELLA_UTENTE_COD),

	// Istruttore uma include anche il caso Appag
	ISTRUTTORE_UMA(Ruoli.DICHIARAZIONE_CONSUMI_MODIFICA_COD),

	// profilo amministratore 
	DOMANDE_UMA_EDITA_TUTTI(Ruoli.DOMANDE_UMA_EDITA_TUTTI_COD);

	private String codiceRuolo;

	private Ruoli(String codiceRuolo) {
		this.codiceRuolo = codiceRuolo;
	}

	public String getCodiceRuolo() {
		return codiceRuolo;
	}

	public static final String DOMANDE_UMA_EDITA_ENTE_COD = "a4g.uma.domandarichiestacarburante.edita.ente";
	public static final String DOMANDE_UMA_EDITA_UTENTE_COD = "a4g.uma.domandarichiestacarburante.edita.utente";

	public static final String DOMANDE_UMA_RICERCA_ENTE_COD = "a4g.uma.domandarichiestacarburante.ricerca.ente";
	public static final String DOMANDE_UMA_RICERCA_UTENTE_COD = "a4g.uma.domandarichiestacarburante.ricerca.utente";
	public static final String DOMANDE_UMA_RICERCA_TUTTI_COD = "a4g.uma.domandarichiestacarburante.ricerca.tutti";

	public static final String DOMANDE_UMA_CANCELLA_ENTE_COD = "a4g.uma.domandarichiestacarburante.cancella.ente";
	public static final String DOMANDE_UMA_CANCELLA_UTENTE_COD = "a4g.uma.domandarichiestacarburante.cancella.utente";

	//22/06/2021: utilizzatO solo per modifica data conduzione istruttore UMA
	public static final String DICHIARAZIONE_CONSUMI_MODIFICA_COD = "a4g.uma.dichiarazioneconsumi.modifica"; 

	//Inutilizzato: può essere utilizzato esclusivamente dall'amministratore di sistema per import di dati e bonifiche
	public static final String DOMANDE_UMA_EDITA_TUTTI_COD = "a4g.uma.domandarichiestacarburante.edita.tutti";
}
