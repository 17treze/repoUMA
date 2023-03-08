package it.tndigitale.a4gutente.codici;

import it.tndigitale.a4g.framework.security.model.Ruolo;

public enum Ruoli implements Ruolo {
	
	IMPORTAZIONE_MASSIVA_UTENTI(Ruoli.IMPORTAZIONE_MASSIVA_UTENTI_COD),
	CREA_UTENTE(Ruoli.CREA_UTENTE_COD),
	VISUALIZZA_DOMANDE(Ruoli.VISUALIZZA_DOMANDE_COD),
	EDITA_PROPRI_DATI_PERSONALI(Ruoli.EDITA_PROPRI_DATI_PERSONALI_COD),
	EDITA_TUTTI_I_DATI_PERSONALI(Ruoli.EDITA_TUTTI_I_DATI_PERSONALI_COD),
	EDITA_DOMANDE(Ruoli.EDITA_DOMANDE_COD),
	VISUALIZZA_ISTRUTTORIA_DOMANDA(Ruoli.VISUALIZZA_ISTRUTTORIA_DOMANDA_COD),
	EDITA_ISTRUTTORIA_DOMANDA(Ruoli.EDITA_ISTRUTTORIA_DOMANDA_COD),
	VISUALIZZA_PROFILI_UTENTE(Ruoli.VISUALIZZA_PROFILI_UTENTE_COD);

	private String codiceRuolo;

	private Ruoli(String codiceRuolo) {
		this.codiceRuolo = codiceRuolo;
	}
	
	public String getCodiceRuolo() {
		return codiceRuolo;
	}

	public static final String IMPORTAZIONE_MASSIVA_UTENTI_COD = "a4gutente.utenti.importazione";
	public static final String CREA_UTENTE_COD = "a4gutente.utenti.edita";
	public static final String VISUALIZZA_DOMANDE_COD = "a4gutente.domandaregistrazione.visualizza";
	public static final String EDITA_PROPRI_DATI_PERSONALI_COD = "a4gutente.persone.persona.edita";
	public static final String EDITA_TUTTI_I_DATI_PERSONALI_COD = "a4gutente.persone.tutti.edita";
	public static final String EDITA_DOMANDE_COD = "a4gutente.domandaregistrazione.edita";
	public static final String VISUALIZZA_ISTRUTTORIA_DOMANDA_COD = "a4gutente.domandaregistrazione.istruttoria.visualizza";
	public static final String EDITA_ISTRUTTORIA_DOMANDA_COD = "a4gutente.domandaregistrazione.edita";
	public static final String VISUALIZZA_PROFILI_UTENTE_COD = "a4gutente.utenti.profili.visualizza";

}








