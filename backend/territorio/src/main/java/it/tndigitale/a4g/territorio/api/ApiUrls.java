package it.tndigitale.a4g.territorio.api;

public class ApiUrls {
	private ApiUrls() {}
	
	public static final String API_V1 = "/api/v1";
	public static final String COLTURE = API_V1 + "/colture";
	public static final String SEZIONE_CATASTALE = API_V1 + "/sezione";
	public static final String FASCICOLI_V1 = API_V1 + "/fascicoli";
	public static final String FASCICOLI_PRIVATE_V1 = FASCICOLI_V1 + "/private";
	public static final String CONDUZIONE_TERRENO = API_V1 + "/conduzione-terreno";
}
