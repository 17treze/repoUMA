package it.tndigitale.a4gistruttoria.util;

public enum StatoDomandaCollegata {

	NON_CARICATO("NON CARICATO"), 
	IN_LAVORAZIONE("IN LAVORAZIONE"), 
	CARICATO("CARICATO"), 
	CHIUSA_CON_ESITO_NEGATIVO("CHIUSA CON ESITO NEGATIVO"), 
	ESITO_POSITIVO("ESITO POSITIVO"), 
	ANOMALIA("ANOMALIA"), 
	IN_ISTRUTTORIA("IN ISTRUTTORIA"), 
	IN_INSERIMENTO("IN INSERIMENTO"),
	IN_CODA("IN CODA"),
	ATTI("ATTI");
	
	private String statoDomandaCollegata;
	
	private StatoDomandaCollegata(String statoDomandaCollegata) {
		this.statoDomandaCollegata = statoDomandaCollegata;
	}

	public String getStatoDomandaCollegata() {
		return statoDomandaCollegata;
	}

	public void setStatoDomandaCollegata(String statoDomandaCollegata) {
		this.statoDomandaCollegata = statoDomandaCollegata;
	}
	
}
