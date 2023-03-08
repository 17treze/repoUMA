package it.tndigitale.a4g.ags.api;

public final class ApiUrls {

	protected static final String API_V1 = "/api/v1";

	public static final String FASCICOLI_V1 = API_V1 + "/fascicoli";

	public static final String UTENTI_V1 = API_V1 + "/utente";

	public static final String DOMANDE_PSR_V1 = API_V1 + "/domandePSR";

	public static final String DOMANDE_DU_V1 = API_V1 + "/domandeDU";
	
	public static final String DOMANDE_UNICHE_V1 = API_V1 + "/domandeUniche";

	public static final String PIANI_COLTURALI_V1 = API_V1 + "/pianicolture";

	public static final String FUNZIONE_FASCICOLO_VALIDO = "/controllaValidita";
	
	public static final String FUNZIONE_UTENZE = "/utenze";

	public static final String ESITI_ANTIMAFIA_V1 = API_V1 + "/esiti/antimafia";

	public static final String CUAA = API_V1 + "/cuaa";
	
	public static final String DATI_CATASTALI_V1 = API_V1 + "/daticatastali";
	
	public static final String DATI_DU = "/dati-du";
	
	public static final String CERTIFICAZIONI = API_V1 + "/certificazioni/antimafia";
	
	public static final String ISTRUTTORIA_GRAFICA_V1 = API_V1 + "/istruttoria-grafica";

	private ApiUrls() {
	}
}
