package it.tndigitale.a4gistruttoria.util;

public enum TipoControllo {
	BRIDUSDC009_infoAgricoltoreAttivo("BRIDUSDC009", "VERIFICA AGRICOLTORE ATTIVO: INFO ASSENTE", GruppoControllo.AMMISSIBILITA, 10, LivelloControllo.ERROR, LivelloControllo.SUCCESS),
	BRIDUSDC010_agricoltoreAttivo("BRIDUSDC010", "VERIFICA AGRICOLTORE ATTIVO: NON ATTIVO", GruppoControllo.AMMISSIBILITA, 20, LivelloControllo.ERROR, LivelloControllo.SUCCESS),
	BRIDUSDC011_impegnoTitoli("BRIDUSDC011", "VERIFICA PRESENZA TITOLI", GruppoControllo.AMMISSIBILITA, 30, LivelloControllo.ERROR, LivelloControllo.SUCCESS),
	BRIDUSDC012_superficieMinima("BRIDUSDC012", "VERIFICA SUPERFICIE MINIMA", GruppoControllo.AMMISSIBILITA, 40, LivelloControllo.ERROR, LivelloControllo.SUCCESS),
	BRIDUSDC023_infoGreening("BRIDUSDC023", "Verifico che la domanda abbia richiesto l'intervento del greening", GruppoControllo.GREENING, 50, LivelloControllo.INFO, LivelloControllo.NULL),
	BRIDUSDC024_aziendaBiologica("BRIDUSDC024", "Verifico che l'azienda assolva il greening in quanto totalmente biologica", GruppoControllo.GREENING, 60, LivelloControllo.NULL,
			LivelloControllo.INFO),
	BRIDUSDC025_impegniGreening("BRIDUSDC025", "Verifico quali impegni per il greening l'azienda debba assolvere", GruppoControllo.GREENING, 70, LivelloControllo.NULL, LivelloControllo.INFO),
	BRIDUSDC026_riduzioniGreening("BRIDUSDC026", "Verifico che ci siano riduzioni da greening", GruppoControllo.GREENING, 80, LivelloControllo.NULL, LivelloControllo.WARNING),
	BRIDUSDC033_esenzioneDiversificazione("BRIDUSDC033", "Verifico che l'azienda sia esente dalla diversificazione", GruppoControllo.GREENING, 90, LivelloControllo.NULL, LivelloControllo.INFO),
	BRIDUSDC055_sanzioneGreening("BRIDUSDC055", "Verifico la presenza di sanzione greening", GruppoControllo.GREENING, 100, LivelloControllo.NULL, LivelloControllo.WARNING),
	BRIDUSDC117_diversificazioneEFA("BRIDUSDC117", "Verifico che l'azienda sia esente dalla diversificazione e dall'obbligo delle EFA", GruppoControllo.GREENING, 101, LivelloControllo.NULL,
			LivelloControllo.INFO),
	BRIDUSDC027_infoGiovane("BRIDUSDC027", "Verifico che la domanda abbia richiesto l'intervento del giovane", GruppoControllo.GIOVANE, 110, LivelloControllo.NULL, LivelloControllo.INFO),
	BRIDUSDC028_requisitiGiovane("BRIDUSDC028", "Verifico che l'agricoltore abbia i requisiti del giovane agricoltore", GruppoControllo.GIOVANE, 120, LivelloControllo.WARNING, LivelloControllo.NULL),
	BRIDUSDC020_idDomandaRiduzione("BRIDUSDC020", "Presenza riduzioni", GruppoControllo.RIDUZIONE, 130, LivelloControllo.NULL, LivelloControllo.WARNING),
	BRIDUSDC021_idDomandaSanzioni("BRIDUSDC021", "Presenza sanzioni", GruppoControllo.RIDUZIONE, 140, LivelloControllo.INFO, LivelloControllo.WARNING),
	BRIDUSDC022_idDomandaCampione("BRIDUSDC022", "Verifica il dettaglio delle particelle/colture impegnate", GruppoControllo.RIDUZIONE, 150, LivelloControllo.NULL, LivelloControllo.INFO),
	BRIDUSDC034_sigeco("BRIDUSDC034", "Verifico che il controllo in SIGECO sia chiuso", GruppoControllo.RIDUZIONE, 160, LivelloControllo.ERROR, LivelloControllo.NULL),
	// IDU-2021-17 - Modifica anomalie da WARNING a INFO
	BRIDUSDC035_supMinimaAmmessa("BRIDUSDC035", "Sup amm minima 5000 metri", GruppoControllo.RIDUZIONE, 170, LivelloControllo.INFO, LivelloControllo.NULL),
	BRIDUSDC031_sanzioniComminate("BRIDUSDC031", "Verifico se le sanzioni comminate sono recuperabili per interno nella domanda", GruppoControllo.RIDUZIONE, 180, LivelloControllo.ERROR,
			LivelloControllo.NULL),
	// IDU-2021-17 - Modifica anomalie da WARNING a INFO
	BRIDUSDC135_isAnomalieCoordinamento("BRIDUSDC135", "Presenza anomalie coordinamento", GruppoControllo.RIDUZIONE, 185, LivelloControllo.NULL, LivelloControllo.INFO),
	BRIDUSDC043_riduzioneCapping("BRIDUSDC043", "Verifico la presenza di riduzioni da capping", GruppoControllo.FINALE, 190, LivelloControllo.NULL, LivelloControllo.WARNING),
	BRIDUSDC036_verificaRitardo("BRIDUSDC036", "Verifico se la domanda è stata presentata in ritardo", GruppoControllo.FINALE, 200, LivelloControllo.NULL, LivelloControllo.WARNING),
	BRIDUSDC029_recidiva("BRIDUSDC029", "Verifico la presenza della recidiva per il premio di base", GruppoControllo.SANZIONI, 210, LivelloControllo.NULL, LivelloControllo.INFO),
	BRIDUSDC030_sanzioniAnnoPrec("BRIDUSDC030", "Verifico il recupero della sanzione scontata l'anno precedente per il premio di base", GruppoControllo.SANZIONI, 220, LivelloControllo.NULL,
			LivelloControllo.INFO),
	BRIDUSDC109_isDomandaLiqAnnoPrec("BRIDUSDC109", "Verifica Liquidazione domanda sostegno disaccoppiato domanda anno precedente", GruppoControllo.SANZIONI, 225, LivelloControllo.ERROR,
			LivelloControllo.NULL),
	BRIDUSDC032_yellowCard("BRIDUSDC032", "Verifico l'applicazione della yellow card per il premio di base", GruppoControllo.SANZIONI, 230, LivelloControllo.NULL, LivelloControllo.INFO),
	BRIDUSDC056_recidiva_giovane("BRIDUSDC056", "Verifico la presenza della recidiva per il giovane agricoltore", GruppoControllo.GIOVANE, 240, LivelloControllo.NULL, LivelloControllo.INFO),
	BRIDUSDC057_yellowCardGiovane("BRIDUSDC057", "Verifico l'applicazione della yellow card per il premio giovane", GruppoControllo.GIOVANE, 250, LivelloControllo.NULL, LivelloControllo.INFO),
	BRIDUSDC058_sanzioniAnnoPrecGiovane("BRIDUSDC058", "Verifico il recupero della sanzione scontata l'anno precedente per il premio giovane", GruppoControllo.GIOVANE, 260, LivelloControllo.NULL,
			LivelloControllo.INFO),
	BRIDUSDC059_riduzioniGiovane("BRIDUSDC059", "Verifico la presenza di riduzioni per il premio giovane", GruppoControllo.GIOVANE, 270, LivelloControllo.NULL, LivelloControllo.WARNING),
	BRIDUSDC060_sanzioniGiovane("BRIDUSDC060", "Verifico la presenza di sanzioni per il premio giovane", GruppoControllo.GIOVANE, 280, LivelloControllo.NULL, LivelloControllo.WARNING),
	BRIDUSDL037_iban("BRIDUSDL037", "Verifico se IBAN è valido", GruppoControllo.LIQUIDABILE, 290, LivelloControllo.ERROR, LivelloControllo.INFO),

	BRIDUSDL038_titolare("BRIDUSDL038", "Verifico se IL TITOLARE ditta individuale è deceduto", GruppoControllo.LIQUIDABILE, 300, LivelloControllo.INFO, LivelloControllo.ERROR),
	BRIDUSDL039_agea("BRIDUSDL039", "Verifico se la domanda è nella lista Nera Agea", GruppoControllo.LIQUIDABILE, 310, LivelloControllo.INFO, LivelloControllo.ERROR),
	BRIDUSDS040_importoMinimo("BRIDUSDS040", "Verifico il raggiungimento dell'importo minimo", GruppoControllo.INTERSOSTEGNO, 320, LivelloControllo.ERROR, LivelloControllo.INFO),
	BRIDUSDS041_noAltriSostegni("BRIDUSDS041", "Verifico la presenza di altri sostegni richiesti", GruppoControllo.INTERSOSTEGNO, 330, LivelloControllo.ERROR, LivelloControllo.NULL),
	BRIDUSDS049_importoMinimoAntimafia("BRIDUSDS049", "Verifico il raggiungimento della soglia minima per la verifica antimafia", GruppoControllo.INTERSOSTEGNO, 330, LivelloControllo.INFO,
			LivelloControllo.ERROR),
	BRIDUSDS050_esitoAntimafia("BRIDUSDS050", "Verifico la presenza di un esito negativo di antimafia", GruppoControllo.INTERSOSTEGNO, 330, LivelloControllo.INFO, LivelloControllo.NULL),
	BRIDUSDC013_tipoPascolo("BRIDUSDC013", "Verifico che si tratti di un pascolo condotto in conformità agli \"Usi e consuetudini locali\"", GruppoControllo.MAN, 331, LivelloControllo.NULL,
			LivelloControllo.NULL),
	BRIDUSDC015_mantenimentoSupMan1("BRIDUSDC015", "Controllo il mantenimento delle superfici MAN1", GruppoControllo.MAN, 332, LivelloControllo.WARNING, LivelloControllo.NULL),
	BRIDUSDC016_mantenimentoSupMan2("BRIDUSDC016", "Controllo il mantenimento delle superfici MAN2", GruppoControllo.MAN, 333, LivelloControllo.WARNING, LivelloControllo.NULL),
	BRIDUSDC068_presenzaAllevamentiFuoriComune("BRIDUSDC068", "Verifico la presenza di allevamenti in comuni diversi da quello del pascolo e non limitrofi", GruppoControllo.MAN, 334,
			LivelloControllo.NULL, LivelloControllo.NULL),
	BRIDUSDC069_presenzaDatoDocumentazioneTrasporto("BRIDUSDC069", "Verifica la presenza dell'informazione della presenta della documentazione di trasporto inserita dall'istruttore in applicativo",
			GruppoControllo.MAN, 335, LivelloControllo.ERROR, LivelloControllo.NULL),
	BRIDUSDC017_mantenimentoSupMan3("BRIDUSDC017", "Controllo il mantenimento delle superfici MAN3", GruppoControllo.MAN, 336, LivelloControllo.WARNING, LivelloControllo.NULL),
	BRIDUSDC014_mantenimentoSupMan4("BRIDUSDC014", "Controllo il mantenimento delle superfici MAN4", GruppoControllo.MAN, 337, LivelloControllo.WARNING, LivelloControllo.NULL),

	// Accoppiato Superficie
	BRIDUACS090_olivo75("BRIDUACS090", "Appezzamento olivicolo con pendenza superiore al 7,5%", GruppoControllo.ACC_SUPERFICIE, 400, LivelloControllo.ERROR, LivelloControllo.INFO),
	BRIDUACS091_olivoNazionale("BRIDUACS091", "Tenuta del registro telematico DM 23 dicembre 2013", GruppoControllo.ACC_SUPERFICIE, 410, LivelloControllo.ERROR, LivelloControllo.SUCCESS),
	BRIDUACS092_olivoQualita("BRIDUACS092", "Obblighi di qualità DOP o IGP", GruppoControllo.ACC_SUPERFICIE, 420, LivelloControllo.ERROR, LivelloControllo.SUCCESS),
	BRIDUACS083_supMinima("BRIDUACS083", "Superficie minima Accoppiato Superficie", GruppoControllo.ACC_SUPERFICIE, 430, LivelloControllo.ERROR, LivelloControllo.INFO),

	// Accoppiato Zootecnia
	BRIDUACZ107_UbaAmmessi("BRIDUACZ107", "Al fine di poter presentare la domanda integrativa zootecnia una azienda deve avere almeno 3 UBA (>=) ammissibili a premio", GruppoControllo.ACC_ZOOTECNIA,
			500, LivelloControllo.ERROR, LivelloControllo.INFO),
	BRIDUACZ118_DomandaIntegrativa("BRIDUACZ118", "Controllo se è stata presentata la domanda integrativa zootecnia per la campagna corrente", GruppoControllo.ACC_ZOOTECNIA, 510,
			LivelloControllo.ERROR, LivelloControllo.INFO),
	BRIDUACZ123_VerificaControlliInLoco("BRIDUACZ123", "Verifica controlli in loco", GruppoControllo.ACC_ZOOTECNIA, 560, LivelloControllo.ERROR, LivelloControllo.INFO),
	BRIDUACZ126_VerificaSanzioni("BRIDUACZ126", "Verifico sanzioni", GruppoControllo.ACC_ZOOTECNIA, 570, LivelloControllo.ERROR, LivelloControllo.INFO),
	BRIDUACZ127_Riduzioni("BRIDUACZ127", "Verifico se ci sono riduzioni ritardo per domanda ACZ o per Sanzioni ACZ", GruppoControllo.ACC_ZOOTECNIA, 580, LivelloControllo.WARNING,
			LivelloControllo.INFO),
	// IDU-2021-17 - Modifica anomalie da WARNING a INFO
	BRIDUSDC019_PresenzaMAN("BRIDUSDC019", "Verifico se ci sono anomalie di mantenimento", GruppoControllo.MAN, 590, LivelloControllo.NULL, LivelloControllo.INFO),
	BRIDUSDC062_PresenzaInfoIstruttoreMan5("BRIDUSDC062", "Verifica la presenza dell'informazione dell'esito della MAN5 inserita dall'istruttore in applicativo", GruppoControllo.MAN, 600,
			LivelloControllo.ERROR, LivelloControllo.NULL),
	BRIDUSDC061_VerificaPascoloProvinciaTN("BRIDUSDC061", "Verifica che il Pascolo di malga (Codice Pascolo) sia in provincia di Trento", GruppoControllo.MAN, 610, LivelloControllo.INFO,
			LivelloControllo.NULL),
	BRIDUSDC063_EsitoMAN5("BRIDUSDC063", "Controllo il mantenimento delle superfici per i pascoli fuori provincia MAN5", GruppoControllo.MAN, 620, LivelloControllo.WARNING, LivelloControllo.NULL),
	BRIDUSDC064_VerificaResponsabilePascolo("BRIDUSDC064", "Verifica la presenza del dato relativo al responsabile del pascolo", GruppoControllo.MAN, 630, LivelloControllo.INFO,
			LivelloControllo.NULL),
	BRIDUSDC065_VerificaMan4Titolare("BRIDUSDC065", "Controllo responsabile del pascolo nell'ambito della MAN4", GruppoControllo.MAN, 640, LivelloControllo.WARNING, LivelloControllo.NULL),
	BRIDUSDC066_PresenzaInfoIstruttoreMan4("BRIDUSDC066", "Verifica la presenza dell'informazione del responsabile del pascolo inserita dall'istruttore in applicativo", GruppoControllo.MAN, 650,
			LivelloControllo.ERROR, LivelloControllo.NULL),
	BRIDUSDC067_PresenzaInfoIstruttoreMAN("BRIDUSDC067", "Verifica se non servano informazioni da inserire da parte dell'istruttore per finalizzare il controllo delle anomalie di mantenimento",
			GruppoControllo.MAN, 660, LivelloControllo.ERROR, LivelloControllo.NULL),
	importoMinimoAntimafia("importoMinimoAntimafia", "", GruppoControllo.INTERSOSTEGNO, 700, LivelloControllo.ERROR, LivelloControllo.INFO),
	importoMinimoPagamento("importoMinimoPagamento", "", GruppoControllo.INTERSOSTEGNO, 710, LivelloControllo.ERROR, LivelloControllo.INFO),
	BRIDUSDI050("BRIDUSDI050", "", GruppoControllo.INTERSOSTEGNO, 720, LivelloControllo.ERROR, LivelloControllo.INFO),
	BRIDUNVL129_erede("BRIDUNVL129", "Verifico se l'erede della domanda ha i dati certificati", GruppoControllo.LIQUIDABILE, 730, LivelloControllo.ERROR, LivelloControllo.NULL),
	BRIDUSDL133_importoErogabilePositivo("BRIDUSDL133", "Importo da liquidare positivo", GruppoControllo.LIQUIDABILE, 750, LivelloControllo.NULL, LivelloControllo.NULL),//null e null per non essere visualizzato
	BRIDUSDL134_importoErogabileNegativo("BRIDUSDL134", "Importo da liquidare negativo", GruppoControllo.LIQUIDABILE, 751, LivelloControllo.NULL, LivelloControllo.WARNING),
	BRIDUSDL135_importoErogabileZero("BRIDUSDL135", "Importo da liquidare nullo", GruppoControllo.LIQUIDABILE, 752, LivelloControllo.NULL, LivelloControllo.WARNING),
	BRIDUSDL135_importoErogabileIrrilevante("BRIDUSDL135_IRR", "Importo da liquidare irrilevante", GruppoControllo.LIQUIDABILE, 753, LivelloControllo.NULL, LivelloControllo.WARNING),
	BRIDUSDC134_importoErogabilePositivo("BRIDUSDC134", "Importo da liquidare positivo", GruppoControllo.FINALE, 740, LivelloControllo.NULL, LivelloControllo.NULL);//non più visualizzabile
	
	public enum LivelloControllo {
		INFO("INFORMAZIONI", 1), WARNING("WARNING", 2), SUCCESS("SUCCESS", 3), ERROR("ANOMALIE BLOCCANTI", 4), NULL("NULL", 5);

		private String descrizione;
		private Integer ordine;

		LivelloControllo(String descrizione, Integer ordine) {
			this.descrizione = descrizione;
			this.ordine = ordine;
		}

		public String getDescrizione() {
			return descrizione;
		}

		public Integer getOrdine() {
			return ordine;
		}
	}

	private String codice;
	private String descrizione;
	private Boolean persist;
	private Integer ordine;
	private LivelloControllo livelloPositivo;
	private LivelloControllo livelloNegativo;
	private GruppoControllo gruppoControllo;

	private TipoControllo(String codice, String descrizione, GruppoControllo gruppoControllo, Integer ordine, LivelloControllo livelloNegativo, LivelloControllo livelloPositivo) {
		this.codice = codice;
		this.descrizione = descrizione;

		this.persist = true;
		this.gruppoControllo = gruppoControllo;
		this.ordine = ordine;
		this.livelloPositivo = livelloPositivo;
		this.livelloNegativo = livelloNegativo;
	}

	public String getCodice() {
		return codice;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public GruppoControllo getGruppoControllo() {
		return gruppoControllo;
	}

	public Boolean getPersist() {
		return persist;
	}

	public Integer getOrdine() {
		return ordine;
	}

	public LivelloControllo getLivelloPositivo() {
		return livelloPositivo;
	}

	public LivelloControllo getLivelloNegativo() {
		return livelloNegativo;
	}
}
