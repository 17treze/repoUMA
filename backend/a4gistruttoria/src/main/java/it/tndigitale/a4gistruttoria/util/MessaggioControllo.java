package it.tndigitale.a4gistruttoria.util;

import it.tndigitale.a4gistruttoria.dto.lavorazione.OpzioniImportoMinimo;
import it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato.PassoCalcoloGreeningService;
import it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato.PassoCalcoloRiduzioniService;

public enum MessaggioControllo {
	BRIDUSDC35_NO("BRIDUSDC035", "NO", "La superficie ammissibile è inferiore a 0,5 ha"),
	BRIDUSDC35_SI("BRIDUSDC035", "SI", null),
	BRIDUSDC009_NO("BRIDUSDC009", "NO", "Informazione sull'agricoltore attivo non presente"),
	BRIDUSDC009_SI("BRIDUSDC009", "SI", null),
	BRIDUSDC010_NO("BRIDUSDC010", "NO", "L'agricoltore risulta non attivo"),
	BRIDUSDC010_SI("BRIDUSDC010", "SI", null),
	BRIDUSDC011_NO("BRIDUSDC011", "NO", "L'agricoltore non possiede titoli"),
	BRIDUSDC011_SI("BRIDUSDC011", "SI", null),
	BRIDUSDC012_NO("BRIDUSDC012", "NO", "La superficie impegnata non raggiunge la soglia minima di 0,5 ha"),
	BRIDUSDC012_SI("BRIDUSDC012", "SI", null),
	BRIDUSDC013_NO("BRIDUSDC013", "NO", "Pascolo Aziendale"),
	BRIDUSDC013_SI("BRIDUSDC013", "SI", "Pascolo condotto in conformità agli Usi e consuetudini locali"),
	BRIDUSDC014_NO("BRIDUSDC014", "NO", "Presenza anomalia di mantenimento MAN4 - carico di bestiame inferiore a 0,20"),
	BRIDUSDC014_SI("BRIDUSDC014", "SI", null),
	BRIDUSDC015_NO("BRIDUSDC015", "NO", "Presenza anomalia di mantenimento MAN1 - il titolare della domanda non risulta essere detentore di alcun allevamento"),
	BRIDUSDC015_SI("BRIDUSDC015", "SI", null),
	BRIDUSDC016_NO("BRIDUSDC016", "NO", "Presenza anomalia di mantenimento MAN2"),
	BRIDUSDC016_SI("BRIDUSDC016", "SI", null),
	BRIDUSDC017_NO("BRIDUSDC017", "NO", "Presenza anomalia di mantenimento MAN3"),
	BRIDUSDC017_SI("BRIDUSDC017", "SI", null),
	BRIDUSDC018_NO("BRIDUSDC018", "NO", "L'azienda non ha pascoli (No controllo MAN)"),
	BRIDUSDC018_SI("BRIDUSDC018", "SI", "L'azienda ha pascoli (Controllo MAN)"),
	BRIDUSDC019_NO("BRIDUSDC019", "NO", null),
	BRIDUSDC019_SI("BRIDUSDC019", "SI", "L'azienda ha anomalie di mantenimento"),
	BRIDUSDC020_NO("BRIDUSDC020", "NO", null),
	BRIDUSDC020_SI("BRIDUSDC020", "SI", "Presenza di riduzioni da sovradichiarazione"),
	BRIDUSDC021_NO("BRIDUSDC021", "NO", null),
	BRIDUSDC021_SANZION_FALSE("BRIDUSDC021", PassoCalcoloRiduzioniService.SANZIONIFALSE, null),
	BRIDUSDC021_SANZION_MIN_10("BRIDUSDC021", PassoCalcoloRiduzioniService.SANZIONIINF10, "Presenza di sanzioni da sovradichiarazione (Scostamento compreso fra il 3% e il 10%)"),
	BRIDUSDC021_SANZION_SUP_10("BRIDUSDC021", PassoCalcoloRiduzioniService.SANZIONISUP10, "Presenza di sanzioni da sovradichiarazione (Scostamento superiore al 10%)"),
	BRIDUSDC022_NO("BRIDUSDC022", "NO", null),
	BRIDUSDC022_SI("BRIDUSDC022", "SI", "Azienda estratta a campione superfici"),
	BRIDUSDC023_NO("BRIDUSDC023", "NO", "L'azienda ha rinunciato al greening"),
	BRIDUSDC023_SI("BRIDUSDC023", "SI", null),
	BRIDUSDC024_NO("BRIDUSDC024", "NO", null),
	BRIDUSDC024_SI("BRIDUSDC024", "SI", "L'azienda assolve agli impegni greening in quanto biologica"),
	BRIDUSDC025_NO("BRIDUSDC025", PassoCalcoloGreeningService.esitoNoImpegni, null),
	BRIDUSDC025_DIV("BRIDUSDC025", PassoCalcoloGreeningService.esitoDiversificazione, "Greening: l'azienda è soggetta alla diversificazione"),
	BRIDUSDC025_DIV_EFA("BRIDUSDC025", PassoCalcoloGreeningService.esitoDiversificazioneEfa, "Greening: l'azienda è soggetta alla diversificazione e alle EFA"),
	BRIDUSDC026_NO("BRIDUSDC026", "NO", null),
	BRIDUSDC026_SI("BRIDUSDC026", "SI", "Greening - Presenza di riduzioni"),
	BRIDUSDC027_NO("BRIDUSDC027", "NO", null),
	BRIDUSDC027_SI("BRIDUSDC027", "SI", "L'azienda ha richiesto il premio aggiuntivo in quanto giovane agricoltore"),
	BRIDUSDC028_NO("BRIDUSDC028", "NO", "L'azienda non possiede i requisiti del giovane agricoltore"),
	BRIDUSDC028_SI("BRIDUSDC028", "SI", null),
	BRIDUSDC029_NO("BRIDUSDC029", "NO", null),
	BRIDUSDC029_SI("BRIDUSDC029", "SI", "Presenza Recidiva"),
	BRIDUSDC030_NO("BRIDUSDC030", "NO", null),
	BRIDUSDC030_SI("BRIDUSDC030", "SI", "Recupero sanzione scontata anno precedente"),
	BRIDUSDC031_NO("BRIDUSDC031", "NO", "Nessun importo da erogare e Azienda da iscrivere al registro debitori"),
	BRIDUSDC031_SI("BRIDUSDC031", "SI", null),
	BRIDUSDC032_NO("BRIDUSDC032", "NO", null),
	BRIDUSDC032_SI("BRIDUSDC032", "SI", "Applicazione sanzione scontata (Yellow Card)"),
	BRIDUSDC033_NO("BRIDUSDC033", "NO", null),
	BRIDUSDC033_SI("BRIDUSDC033", "SI", "L'azienda è esente dall'obbligo della diversificazione"),
	BRIDUSDC034_NO("BRIDUSDC034", "NO", "Il controllo in loco è ancora in corso"),
	BRIDUSDC034_SI("BRIDUSDC034", "SI", null),
	BRIDUSDC036_NO("BRIDUSDC036", "NO", null),
	BRIDUSDC036_SI("BRIDUSDC036", "SI", "Riduzione per ritardata presentazione"),

	BRIDUSDC043_NO("BRIDUSDC043", "NO", null),
	BRIDUSDC043_SI("BRIDUSDC043", "SI", "Presenza Riduzioni da Capping"),
	BRIDUSDC055_NO("BRIDUSDC055", "NO", null),
	BRIDUSDC055_SI("BRIDUSDC055", "SI", "Greening - Presenza di sanzioni"),
	BRIDUSDC056_NO("BRIDUSDC056", "NO", null),
	BRIDUSDC056_SI("BRIDUSDC056", "SI", "Presenza Recidiva"),
	BRIDUSDC057_NO("BRIDUSDC057", "NO", null),
	BRIDUSDC057_SI("BRIDUSDC057", "SI", "Giovane - Applicazione sanzione scontata (Yellow Card)"),
	BRIDUSDC058_NO("BRIDUSDC058", "NO", null),
	BRIDUSDC058_SI("BRIDUSDC058", "SI", "Recupero sanzione scontata anno precedente"),
	BRIDUSDC059_NO("BRIDUSDC059", "NO", null),
	BRIDUSDC059_SI("BRIDUSDC059", "SI", "Giovane - Presenza di riduzioni da sovradichiarazione"),
	BRIDUSDC060_NO("BRIDUSDC060", "NO", null),
	BRIDUSDC060_SI("BRIDUSDC060", "SI", "Giovane - Presenza di sanzioni da sovradichiarazione"),
	BRIDUSDC061_NO("BRIDUSDC061", "NO", "Pascolo situato al di fuori della provincia di Trento"),
	BRIDUSDC061_SI("BRIDUSDC061", "SI", null),
	BRIDUSDC062_NO("BRIDUSDC062", "NO", "Inserire l'esito della verifica del mantenimento"),
	BRIDUSDC062_SI("BRIDUSDC062", "SI", null),
	BRIDUSDC063_NO("BRIDUSDC063", "NO", "Presenza anomalia di mantenimento MAN5"),
	BRIDUSDC063_SI("BRIDUSDC063", "SI", null),
	BRIDUSDC064_NO("BRIDUSDC064", "NO", "Dato del responsabile del pascolo non disponibile"),
	BRIDUSDC064_SI("BRIDUSDC064", "SI", null),
	BRIDUSDC065_NO("BRIDUSDC065", "NO", "Presenza anomalia di mantenimento MAN4 - intestatario della domanda diverso dal resposabile del pascolo"),
	BRIDUSDC065_SI("BRIDUSDC065", "SI", null),
	BRIDUSDC066_NO("BRIDUSDC066", "NO", "Inserire l'informazione del responsabile del pascolo"),
	BRIDUSDC066_SI("BRIDUSDC066", "SI", null),
	BRIDUSDC067_NO("BRIDUSDC067", "NO", "Controllo del mantenimento - Mancano informazioni da inserire da parte dell'istruttore"),
	BRIDUSDC067_SI("BRIDUSDC067", "SI", null),
	BRIDUSDC068_NO("BRIDUSDC068", "NO", null),
	BRIDUSDC068_SI("BRIDUSDC068", "SI", null),
	BRIDUSDC069_NO("BRIDUSDC069", "NO", "Inserire l'informazione della presenza della documentazione di trasporto"),
	BRIDUSDC069_SI("BRIDUSDC069", "SI", null),
	BRIDUACS090_NO("BRIDUACS090", "NO", "Superficie determinata da Istruttore per intervento Olivo con pendenza 7,5% assente"),
	BRIDUACS090_SI("BRIDUACS090", "SI", "Superficie determinata da Istruttore per intervento Olivo con pendenza 7,5%"),
	BRIDUACS091_NO("BRIDUACS091", "NO", "Azienda non risulta regolarmente iscritta al registro telematico nazionale"),
	BRIDUACS091_SI("BRIDUACS091", "SI", null),
	BRIDUACS092_NO("BRIDUACS092", "NO", "Azienda non certificata DOP o IGP"),
	BRIDUACS092_SI("BRIDUACS092", "SI", null),
	BRIDUACS083_NO("BRIDUACS083", "NO", "La superficie impegnata non raggiunge la soglia minima di 0,5 ha"),
	BRIDUACS083_SI("BRIDUACS083", "SI", null),
	BRIDUSDC109_SI("BRIDUSDC109", "SI", null),
	BRIDUSDC109_NO("BRIDUSDC109", "NO", "La domanda dell'anno precedente non risulta liquidata: impossibilità di verificare applicazione yellow card"),
	BRIDUSDS041_SI("BRIDUSDS041", "SI", null),
	BRIDUSDS041_NO("BRIDUSDS041", "NO", "Nessun altro sostegno richiesto in domanda"),
	BRIDUSDS040_SI("BRIDUSDS040", "SI", null),
	BRIDUSDS040_NO("BRIDUSDS040", "NO", "La domanda non ha superato la soglia minima pagamento per il sostegno"),
	BRIDUSDS042_SI("BRIDUSDS042", "SI", null),
	BRIDUSDS042_NO("BRIDUSDS042", "NO", "Controllo antimafia non superato"),
	BRIDUSDS044_SI("BRIDUSDS044", "SI", null),
	BRIDUSDS044_NO("BRIDUSDS044", "NO", "Per la domanda non è liquidabile il sostegno Aggiuntivo 1"),
	BRIDUSDS045_SI("BRIDUSDS045", "SI", null),
	BRIDUSDS045_NO("BRIDUSDS045", "NO", "Per la domanda non esiste o non è liquidabile il sostegno Aggiuntivo 2"),
	BRIDUSDS046_SI("BRIDUSDS046", "SI", null),
	BRIDUSDS046_NO("BRIDUSDS046", "NO", "Per la domanda non esistono sostegni ACZ e ACS"),
	BRIDUSDS049_SI("BRIDUSDS049", "SI", "L'importo calcolato della domanda supera i 25000 euro"),
	BRIDUSDS049_NO("BRIDUSDS049", "NO", "L'importo calcolato della domanda non supera i 25000 euro"),
	BRIDUSDS050_SI("BRIDUSDS050", "SI", null),
	BRIDUSDS050_NO("BRIDUSDS050", "NO", "L'azienda non ha un esito negativo della dichiarazione antimafia"),
	BRIDUSDS051_SI("BRIDUSDS051", "SI", null),
	BRIDUSDS051_NO("BRIDUSDS051", "NO", "Residuo disciplina finanziaria minore di 0"),
	BRIDUSDL037_SI("BRIDUSDL037", "SI", null),
	BRIDUSDL037_NO("BRIDUSDL037", "NO", "L'IBAN indicato in domanda non presente in fascicolo aziendale"),
	BRIDUSDL038_SI("BRIDUSDL038", "SI", "L'agricoltore intestatario della domanda è deceduto"),
	BRIDUSDL038_NO("BRIDUSDL038", "NO", null),
	BRIDUSDL039_SI("BRIDUSDL039", "SI", "L'agricoltore intestatario della domanda risulta essere sospeso dai pagamenti"),
	BRIDUSDL039_NO("BRIDUSDL039", "NO", null),
	BRIDUSDC117_NO("BRIDUSDC117", "NO", null),
	BRIDUSDC117_SI("BRIDUSDC117", "SI", "Greening: l'azienda è esente dall'obbigo della diversificazione e delle EFA"),
	BRIDUNVL129_NO("BRIDUNVL129", "NO", "Dati dell'erede mancanti o non certificati"),
	BRIDUNVL129_SI("BRIDUNVL129", "SI", null),
	BRIDUSDL133_NO("BRIDUSDL133", "NO", "Nessun importo da erogare e Azienda da iscrivere al registro debitori: importo calcolato inferiore ai pagamenti già effettuati"),
	BRIDUSDL133_SI("BRIDUSDL133", "SI", null),
	
	BRIDUSDL134_NO("BRIDUSDL134", "NO", null),
	BRIDUSDL134_SI("BRIDUSDL134", "SI", "Nessun importo da erogare e Azienda da iscrivere al registro debitori: importo calcolato inferiore ai pagamenti già effettuati"),
	BRIDUSDL135_NO("BRIDUSDL135", "NO", null),
	BRIDUSDL135_SI("BRIDUSDL135", "SI", "Nessun importo da erogare: importo calcolato uguale a zero"),
	BRIDUSDL135_IRR_NO("BRIDUSDL135_IRR", "NO", null),
	BRIDUSDL135_IRR_SI("BRIDUSDL135_IRR", "SI", "Nessun importo da erogare: importo calcolato irrilevante"),
	
	BRIDUSDC134_NO("BRIDUSDC134", "NO", null),
	BRIDUSDC134_SI("BRIDUSDC134", "SI", null),
	BRIDUSDC135_NO("BRIDUSDC135", "NO", null),
	BRIDUSDC135_SI("BRIDUSDC135", "SI", "L'azienda ha anomalie di coordinamento"),
	importoMinimoPagamento_NO("importoMinimoPagamento", "NO", "Importo minimo della domanda non raggiunto o non determinabile"),
	importoMinimoPagamento_SI("importoMinimoPagamento", "SI", null),
	importoMinimoAntimafia_SI("importoMinimoAntimafia", "SI", "Importo della domanda superiore a 25000 o non determinabile"),
	importoMinimoAntimafia_NO("importoMinimoAntimafia", "NO", null),
	BRIDUSDS050_esitoAntimafia_NO("BRIDUSDS050", "NO", "L'azienda non ha un esito negativo della dichiarazione antimafia "),
	BRIDUSDS050_esitoAntimafia_SI("BRIDUSDS050", "SI", null),
	BRIDUSDS040_importoMinimo_RAGGIUNTO("BRIDUSDS040", OpzioniImportoMinimo.RAGGIUNTO.name(), null),
	BRIDUSDS040_importoMinimo_NON_RAGGIUNTO("BRIDUSDS040", OpzioniImportoMinimo.NON_RAGGIUNTO.name(), null),
	BRIDUSDS040_importoMinimo_NON_DETERMINABILE("BRIDUSDS040",  OpzioniImportoMinimo.NON_DETERMINABILE.name(), null),
	importoMinimo_RAGGIUNTO("importoMinimoPagamento", OpzioniImportoMinimo.RAGGIUNTO.name(), null),
	importoMinimo_NON_RAGGIUNTO("importoMinimoPagamento", OpzioniImportoMinimo.NON_RAGGIUNTO.name(), "L'importo calcolato della domanda non raggiunge l'importo minimo liquidabile"),
	importoMinimo_NON_DETERMINABILE("importoMinimoPagamento",  OpzioniImportoMinimo.NON_DETERMINABILE.name(), "Importo della domanda non determinabile"),
	importoMinimoAntimafia_RAGGIUNTO("importoMinimoAntimafia", OpzioniImportoMinimo.RAGGIUNTO.name(), "L'importo calcolato della domanda supera i 25000 euro"),
	importoMinimoAntimafia_NON_RAGGIUNTO("importoMinimoAntimafia", OpzioniImportoMinimo.NON_RAGGIUNTO.name(), "L'importo calcolato della domanda non supera i 25000 euro"),
	importoMinimoAntimafia_NON_DETERMINABILE("importoMinimoAntimafia",  OpzioniImportoMinimo.NON_DETERMINABILE.name(), "Importo della domanda non determinabile per il controllo della soglia antimafia"),
	BRIDUSDS049_importoMinimoAntimafia_RAGGIUNTO("BRIDUSDS049", OpzioniImportoMinimo.RAGGIUNTO.name(), null),
	BRIDUSDS049_importoMinimoAntimafia_NON_RAGGIUNTO("BRIDUSDS049", OpzioniImportoMinimo.NON_RAGGIUNTO.name(), null),
	BRIDUSDS049_importoMinimoAntimafia_NON_DETERMINABILE("BRIDUSDS049",  OpzioniImportoMinimo.NON_DETERMINABILE.name(), null);
	

	private String codiceBridu;
	private String codiceResBridu;
	private String messaggio;

	private MessaggioControllo(String codiceBridu, String codiceResBridu, String messaggio) {
		this.codiceBridu = codiceBridu;
		this.codiceResBridu = codiceResBridu;
		this.messaggio = messaggio;
	}

	public String getCodiceBridu() {
		return codiceBridu;
	}

	public void setCodiceBridu(String codiceBridu) {
		this.codiceBridu = codiceBridu;
	}

	public String getCodiceResBridu() {
		return codiceResBridu;
	}

	public void setCodiceResBridu(String codiceResBridu) {
		this.codiceResBridu = codiceResBridu;
	}

	public String getMessaggio() {
		return messaggio;
	}

	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}
}
