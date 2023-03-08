package it.tndigitale.a4gistruttoria.api;

public interface ApiUrls {

	public static final String API_V1 = "/api/v1";

	public static final String ANTIMAFIA_V1 = API_V1 + "/antimafia";

	public static final String DOMANDE_V1 = API_V1 + "/domande";

	public static final String DOMANDA_UNICA_V1 = API_V1 + "/domandaunica";

	public static final String DISACCOPPIATO_DOMANDA_UNICA_V1 = DOMANDA_UNICA_V1 + "/disaccoppiato";
	
	public static final String ACC_SUP_DOMANDA_UNICA_V1 = DOMANDA_UNICA_V1 + "/acs";
	
	public static final String ACC_ZOO_DOMANDA_UNICA_V1 = DOMANDA_UNICA_V1 + "/acz";
	
	public static final String CUP_V1 = API_V1 + "/cup";

	public static final String DOMANDE_INTEGRATIVE_V1 = API_V1 + "/domandeintegrative";

	public static final String COUNT = "/count";

	public static final String ELENCO_STATI = "/elencoStati";

	public static final String INFO_SIAN = "/informazioniSian";

	public static final String ISTRUTTORIE_V1 = API_V1 + "/istruttorie";

	public static final String ISTRUTTORIE_DU_V1 = API_V1 + "/istruttorie/du";
	
	public static final String ISTRUTTORIE_DU_CONF_V1 = API_V1 + "/istruttorie/du/conf";
	
	public static final String ISTRUTTORIE_ACCOPPIATO_SUPERFICIE_V1 = ISTRUTTORIE_DU_V1 + "/superficie";
	
	public static final String ISTRUTTORIE_ACCOPPIATO_ZOOTECNIA_V1 = ISTRUTTORIE_DU_V1 + "/zootecnia";

	public static final String ISTRUTTORIE_DISACCOPPIATO_V1 = ISTRUTTORIE_DU_V1 + "/disaccoppiato";

	public static final String ELENCO_LIQUIDAZIONE_ISTRUTTORIA_DU_V1 = ISTRUTTORIE_DU_V1 + "/liquidazione";

	public static final String PROCESSI = API_V1 + "/processi";

	public static final String PROCESSI_ISTRUTTORIE_DU_V1 = PROCESSI + "/istruttorie/du";

	public static final String DATI_DU = "/dati-du";

	public static final String DATI_ISTRUTTORIA_DOMANDA = "/datiIstruttoria";

	public static final String DOMANDA_DETTAGLIO = "/dettaglio";

	public static final String DOMANDA_PARTICELLE = "/particelle";

	public static final String ELENCO_CUAA_CAMPAGNA = "/elencoCuaa";
	
	public static final String ELENCO_CUAA_FILTRATI = "/elenco-cuaa-filtrati";
	
	public static final String ELENCO_RAGIONESOCIALE_FILTRATI = "/elenco-ragionesociale-filtrati";

	public static final String STAMPA = "/stampa";

	public static final String ELENCO_PASCOLI = "/elencoPascoli";

	public static final String AGS_DOMANDE_DU = "domandeDU/";

	public static final String AGS_ESEGUI_MOVIMENTO = "/eseguiMovimento";

	public static final String STAMPA_ELENCO_LIQUIDAZIONE = "/stampaElencoLiquidazione";

	public static final String STAMPA_DOMANDE_LIQUIDABILI = "/stampaDomandeLiquidabili";
	
	public static final String STAMPA_DOMANDE_LIQUIDABILI_ACS = "/stampaDomandeLiquidabiliAcs";
	
	//public static final String STAMPA_DOMANDE_LIQUIDABILI_ACZ = "/stampaDomandeLiquidabiliAcz";

	public static final String AGS_CUAA = "cuaa";

	public static final String CAPI = "/capi";

	public static final String BLOCCO_DOMANDE = "/aggiornaStatoBloccoDomande";
	
	public static final String STATISTICHE_V1 = API_V1 + "/statistiche";

	public static final String STAMPA_RICEVUTA_DOMANDA_INTEGRATIVA = "/stampaRicevutaDomandaIntegrativa";
	
	public static final String AGS_FORZA_MOVIMENTO = "/forzaMovimento";
	
	public static final String FASCICOLO_CONSULTAZIONE_FASCICOLICAA = "consultazione/fascicolicaa";
	
	public static final String SINCRONIZZAZIONE_V1 = API_V1 + "/sincronizzazione";

	public static final String DETTAGLIO_PARTICELLA_V1 = API_V1 + "/dettaglioparticella";

	public static final String DETTAGLIO_PASCOLI_V1 = API_V1 + "/dettagliopascoli";

    public static final String RICERCA_DOMANDE_V1 = API_V1 + "/ricercaDomande";
}
