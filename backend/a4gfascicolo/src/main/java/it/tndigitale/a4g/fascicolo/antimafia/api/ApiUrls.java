package it.tndigitale.a4g.fascicolo.antimafia.api;

public final class ApiUrls {
    
	protected static final String API_V1 = "/api/v1";

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
