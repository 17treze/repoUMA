package it.tndigitale.a4gistruttoria;

import it.tndigitale.a4g.framework.security.model.Ruolo;

public enum Ruoli implements Ruolo {

	VISUALIZZA_ISTRUTTORIA_ANTIMAFIA(Ruoli.VISUALIZZA_ISTRUTTORIA_ANTIMAFIA_COD),
	VISUALIZZA_ISTRUTTORIA_ANTIMAFIA_ENTE(Ruoli.VISUALIZZA_ISTRUTTORIA_ANTIMAFIA_ENTE_COD),
	VISUALIZZA_ISTRUTTORIA_ANTIMAFIA_UTENTE(Ruoli.VISUALIZZA_ISTRUTTORIA_ANTIMAFIA_UTENTE_COD),
	CANCELLA_ISTRUTTORIA_ANTIMAFIA(Ruoli.CANCELLA_ISTRUTTORIA_ANTIMAFIA_COD),
	EDITA_ISTRUTTORIA_ANTIMAFIA(Ruoli.EDITA_ISTRUTTORIA_ANTIMAFIA_COD),
	RIELABORA_DATI_ISTRUTTORIA(Ruoli.RIELABORA_DATI_ISTRUTTORIA_COD),
	EDITA_PAC_DU(Ruoli.EDITA_PAC_DU_COD),
	EDITA_PAC_STAT(Ruoli.EDITA_PAC_STAT_COD),
	EDITA_PAC_ISTRUTT(Ruoli.EDITA_PAC_ISTRUTT_COD),
	EDITA_PAC_DU_DI(Ruoli.EDITA_PAC_DU_DI_COD),
	RICERCA_DOMANDE_NON_FILTRATA(Ruoli.RICERCA_DOMANDE_NON_FILTRATA_COD),
	RICERCA_DOMANDE_FILTRO_ENTE(Ruoli.RICERCA_DOMANDE_FILTRO_ENTE_COD),
	VISUALIZZA_ISTRUTTORIA_DU_UTENTE(Ruoli.VISUALIZZA_ISTRUTTORIA_DU_UTENTE_COD),
	VISUALIZZA_ISTRUTTORIA_DU(Ruoli.VISUALIZZA_ISTRUTTORIA_DU_COD);


	private String codiceRuolo;

	private Ruoli(String codiceRuolo) {
		this.codiceRuolo = codiceRuolo;
	}

	public String getCodiceRuolo() {
		return codiceRuolo;
	}
	
	public static final String CANCELLA_ISTRUTTORIA_ANTIMAFIA_COD = "a4gistruttoria.antimafia.cancella";
	public static final String EDITA_ISTRUTTORIA_ANTIMAFIA_COD = "a4gistruttoria.antimafia.edita";
	public static final String VISUALIZZA_ISTRUTTORIA_ANTIMAFIA_COD = "a4gistruttoria.antimafia.visualizza";
	public static final String VISUALIZZA_ISTRUTTORIA_ANTIMAFIA_ENTE_COD = "a4gistruttoria.antimafia.visualizza.ente";
	public static final String VISUALIZZA_ISTRUTTORIA_ANTIMAFIA_UTENTE_COD = "a4gistruttoria.antimafia.visualizza.utente";
	public static final String RIELABORA_DATI_ISTRUTTORIA_COD = "a4gistruttoria.pac.domandaUnica.rielaboraDati";
	public static final String EDITA_PAC_DU_COD = "a4gistruttoria.pac.domandaUnica.edita";
	public static final String EDITA_PAC_STAT_COD = "a4gistruttoria.pac.statistica.edita";
	public static final String EDITA_PAC_ISTRUTT_COD = "a4gistruttoria.pac.istruttoria.edita";
	public static final String EDITA_PAC_DU_DI_COD = "a4gistruttoria.pac.domandaUnica.domandaIntegrativa.edita";
	public static final String RICERCA_DOMANDE_NON_FILTRATA_COD = "a4gistruttoria.pac.domandaUnica.visualizza.tutti";
	public static final String RICERCA_DOMANDE_FILTRO_ENTE_COD = "a4gistruttoria.pac.domandaUnica.visualizza.ente";
	public static final String VISUALIZZA_ISTRUTTORIA_DU_COD = "a4gistruttoria.pac.istruttoria.du.visualizza.tutti";
	public static final String VISUALIZZA_ISTRUTTORIA_DU_UTENTE_COD = "a4gistruttoria.pac.istruttoria.du.visualizza.utente";
	
}
