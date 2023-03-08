package it.tndigitale.a4g.fascicolo.dotazionetecnica;

public class ApiUrls {
	private ApiUrls() {
	}

	public static final String API_V1 = "/api/v1";

	public static final String LEGACY = API_V1 + "/legacy";

	public static final String FASCICOLO = "/fascicolo";

	public static final String MACCHINE = "/macchine";

	public static final String FABBRICATI = "/fabbricati";

	public static final String ALLEGATI = "/allegati";

	public static final String TIPOLOGIE = "/tipologie";

	public static final String CLASSI_FUNZIONALI = "/classifunzionali";

	public static final String SOTTOTIPI = "/sottotipi";

	public static final String SOTTOTIPI_MACCHINARI = "/sottotipiMacchinari";

	public static final String SOTTOTIPI_FABBRICATI = "/sottotipiFabbricati";

	public static final String DOTAZIONE_TECNICA = API_V1 + "/dotazione-tecnica";

	public static final String DOTAZIONE_TECNICA_PRIVATE = DOTAZIONE_TECNICA + "/private";
}