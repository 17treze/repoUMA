package it.tndigitale.a4gistruttoria;

public class A4gIstruttoriaConstants {
	
	private A4gIstruttoriaConstants() {
	}

	public static final String DEFAULT_ROLE_PREFIX = "ROLE_";
	// costanti utenti
	public static final String HEADER_CF = "codicefiscale";
	public static final String HEADER_UPN = "upn";
	
	// path per chiamate a A4gutente
	public static final String PREFISSO_UTENTE = "utenti";
	public static final String RUOLI_UTENTE = PREFISSO_UTENTE + "/utente/ruoli";
	public static final String ENTI_UTENTE = PREFISSO_UTENTE + "/utente/enti";
	public static final String AZIENDE_UTENTE = PREFISSO_UTENTE + "/utente/aziende";
	
}
