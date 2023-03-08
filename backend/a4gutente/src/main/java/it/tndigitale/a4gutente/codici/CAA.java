package it.tndigitale.a4gutente.codici;

public enum CAA {
	ATS("CAA ATS", 105),
	ACLI("CAA ACLI",181),
	CIA("CAA CIA", 107),
	COOP("CAA COOPTN",122),
	APPAG("APPAG", 999),
	COLDIRETTI("CAA COLDIRETTI DEL TRENTINO",103),
	AGRI("CAA CAF AGRI SRL",71);
	
	private CAA(String descrizione, int codice) {
		this.descrizione = descrizione;
		this.codice = codice;
	}
	
	private String descrizione;
	private int codice;
	
	
	public String getDescrizione() {
		return descrizione;
	}
	public int getCodice() {
		return codice;
	}
}
