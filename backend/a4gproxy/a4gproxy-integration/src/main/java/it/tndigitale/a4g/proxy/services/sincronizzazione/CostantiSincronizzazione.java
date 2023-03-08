package it.tndigitale.a4g.proxy.services.sincronizzazione;

interface CostantiSincronizzazione {
	// Introdurre una gestione che salvaguardi in caso di rilancio completo del processo le modifiche inserite manualmente
	// e che dia evidenza dell'ultima data in cui è stato effettuato il calcolo
	String FONTE_SINCRONIZZAZIONE = "A4G";

	// Codice APPAG
	long CODICE_APPAG = 160;

	String CODICE_AGEA_PREMIO_BASE = "026";
}
