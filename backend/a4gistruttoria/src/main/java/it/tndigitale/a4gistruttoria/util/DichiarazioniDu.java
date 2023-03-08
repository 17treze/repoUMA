package it.tndigitale.a4gistruttoria.util;

public enum DichiarazioniDu {

	DURN_1("A - GIOVANE AGRICOLTORE", "DURN_1"),
	DURN_3("B - NUOVO AGRICOLTORE", "DURN_3"),
	DURN_7("C - ABBANDONO DI TERRE", "DURN_7"),
	DURN17("Eta' inferiore o uguale a 40 anni compiuti nell'anno di presentazione domanda", "DURN_1"),
	DURN18("L'insediamento (apertura p. Iva) e' avvenuto nei 5 anni precedenti alla presentazione della prima DU", "DURN_1"),
	DURN19("La superficie dichiarata e' pari o superiore ad 1 ettaro", "DURN_1"),
	DURN20("Eta' compresa tra i 18 ed i 65 anni compiuti nell'anno di presentazione domanda", "DURN_3"),
	DURN21("Inizio dell'attivita' agricola nel 2013 o successivamente a tale anno", "DURN_3"),
	DURN22("Presentazione domanda di pagamento di base entro i due anni successivi all'inizio dell'attivita' agricola", "DURN_3"),
	DURN24("La superficie dichiarata e' pari o superiore ad 1 ettaro", "DURN_3"),
	DURN_9("D - COMPENSAZIONE DI SVANTAGGI SPECIFICI", "DURN_9"),
	DURN23("F – PROVVEDIMENTI AMMINISTRATIVI E DECISIONI GIUDIZIARIE", "DURN23");

	private String descrizione;
	private String gruppo;

	public String getGruppo() {
		return gruppo;
	}

	public void setGruppo(String gruppo) {
		this.gruppo = gruppo;
	}

	private DichiarazioniDu(String pdescrizione, String pgruppo) {
		this.descrizione = pdescrizione;
		this.gruppo = pgruppo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
}
