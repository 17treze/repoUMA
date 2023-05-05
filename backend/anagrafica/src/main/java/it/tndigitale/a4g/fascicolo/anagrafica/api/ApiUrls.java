package it.tndigitale.a4g.fascicolo.anagrafica.api;

public class ApiUrls {
	
	private ApiUrls() {
	}
	
	public static final String API_V1 = "/api/v1";
	
	public static final String LEGACY = API_V1 + "/legacy";
	
	public static final String FASCICOLO = API_V1 + "/fascicolo";
	
	public static final String FASCICOLO_PRIVATE = FASCICOLO + "/private";
	
	public static final String FASCICOLO_DETENZIONE_AUTONOMA = API_V1 + "/fascicolo-detenzione-autonoma";
	
	public static final String DETENZIONE = API_V1 + "/detenzione";
	
	public static final String MANDATO = API_V1 + "/mandato";
	
	public static final String FASCICOLO_AGS = FASCICOLO + "/legacy";
	
	public static final String CAA = API_V1 + "/caa";
	
	public static final String CAA_AGS = LEGACY + "/caa";
	
	public static final String PERSONA = "/persona";
	
	public static final String PERSONA_FISICA = API_V1 + "/personafisica";
	
	public static final String PERSONA_GIURIDICA = API_V1 + "/personagiuridica";
	
	// Da fascicolo
	
	public static final String CONSULTAZIONE_V1 = API_V1 + "/consultazione";
	
	public static final String ANTIMAFIA_V1 = API_V1 + "/antimafia";
	
	public static final String CONSULTAZIONE_FASCICOLI = "/fascicoli";
	
	public static final String CONSULTAZIONE_FASCICOLO = "/fascicolo";
	
	public static final String CONSULTAZIONE_FASCICOLI_AZIENDA = "/mieifascicoli";
	
	public static final String CONSULTAZIONE_FASCICOLI_ENTE = "/fascicolicaa";
	
	public static final String AMBITO_PERSONE = "/persone";
	
	public static final String AMBITO_AZIENDE = "/aziende";
	
	public static final String FUNZIONE_FASCICOLO_VALIDO = "/controllaValidita";
	
	public static final String FUNZIONE_IS_RAPPRESENTANTE_LEGALE = "/isRappresentanteLegale";
	
	// url a chiamate esterne
	
	public static final String AGS_FASCICOLO_VALIDO = "/%s/controllaValidita";
	
	public static final String INTEGRAZIONI_ANAGRAFICA_IMPRESA_PERSONENONCESSATEPERCF = "/dettagliPersone/%s";
	
}
