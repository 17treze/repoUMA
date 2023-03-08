package it.tndigitale.a4gutente.api;

public interface ApiUrls {

    static final String API_V1 = "/api/v1";

    static final String UTENTI_V1 = API_V1 + "/utenti";

    static final String DOMANDE_V1 = API_V1 + "/domande";

    static final String PROFILI_V1 = API_V1 + "/profili";

    static final String UTENTE_CORRENTE = "/utente";

    static final String UTENTE_DATI = UTENTE_CORRENTE + "/anagrafica";

    static final String UTENTE_RUOLI = UTENTE_CORRENTE + "/ruoli";

    static final String UTENTE_PROFILI = UTENTE_CORRENTE + "/profili";

    static final String UTENTE_PROFILI_BO_VITICOLO = "/boviticolo";

    static final String UTENTE_ENTI = UTENTE_CORRENTE + "/enti";

    static final String UTENTE_AZIENDE = UTENTE_CORRENTE + "/aziende";

	static final String UTENTE_DISTRIBUTORI = UTENTE_CORRENTE + "/distributori";

	static final String UTENTE_REGISTRABILE = UTENTE_CORRENTE + "/isRegistrabile";
	
	static final String UTENTE_AVVIO_PROTOCOLLAZIONE = UTENTE_CORRENTE + "/avvio-protocollazione";		

    static final String FIRMA = UTENTE_CORRENTE + "/firma";

    static final String ELENCO_CAA = "/EntiCAA";

	static final String ELENCO_DISTRIBUTORI = "/distributori";

	static final String ELENCO_DIPATIMENTI = "/dipartimenti";

    static final String PERSONE_V1 = API_V1 + "/persone";

    static final String ISTRUTTORIA = API_V1 + "/istruttoria";
    
    public static final String UTENTI_PRIVATE = UTENTI_V1 + "/private";
}
