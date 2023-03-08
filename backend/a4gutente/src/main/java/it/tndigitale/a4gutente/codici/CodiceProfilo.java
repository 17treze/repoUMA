package it.tndigitale.a4gutente.codici;

public enum CodiceProfilo {
	AMMINISTRATORE(CodiceProfilo.CODICE_AMMINISTRATORE),
	AZIENDA(CodiceProfilo.CODICE_AZIENDA),
	OPERATORE_CAA(CodiceProfilo.CODICE_OPERATORE_CAA),
	APPAG(CodiceProfilo.CODICE_APPAG),
	ISTRUTTORE_UMA(CodiceProfilo.CODICE_ISTRUTTORE_UMA);
	
	private String codice;
	
	CodiceProfilo(String _codice) {
		codice = _codice;
	}
	
	public String getCodice() {
		return codice;
	}
	
	public static final String CODICE_AMMINISTRATORE = "amministratore";
	public static final String CODICE_AZIENDA = "azienda";
	public static final String CODICE_OPERATORE_CAA = "caa";
	public static final String CODICE_APPAG = "appag";
	public static final String CODICE_ISTRUTTORE_UMA = "istruttoreuma";
	
}
