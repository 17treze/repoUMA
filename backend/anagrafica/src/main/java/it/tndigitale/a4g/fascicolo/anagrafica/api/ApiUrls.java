package it.tndigitale.a4g.fascicolo.anagrafica.api;

public class ApiUrls {
	
	private ApiUrls() {}
	
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
	
}
