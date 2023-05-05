package it.tndigitale.a4g.fascicolo.anagrafica;

import it.tndigitale.a4g.framework.security.model.Ruolo;

public enum Ruoli implements Ruolo {
	RICERCA_FASCICOLO_ENTE(Ruoli.RICERCA_FASCICOLO_ENTE_COD), RICERCA_FASCICOLO_TUTTI(
			Ruoli.RICERCA_FASCICOLO_TUTTI_COD), RICERCA_FASCICOLO_UTENTE(Ruoli.RICERCA_FASCICOLO_UTENTE_COD),
	
	RICERCA_FASCICOLO_FILTRO_ENTE(Ruoli.RICERCA_FASCICOLO_FILTRO_ENTE_COD), RICERCA_FASCICOLO_NON_FILTRATA(
			Ruoli.RICERCA_FASCICOLO_NON_FILTRATA_COD), RICERCA_FASCICOLO_FILTRO_UTENTE(
					Ruoli.RICERCA_FASCICOLO_FILTRO_UTENTE_COD),
	
	APERTURA_FASCICOLO_ENTE(Ruoli.APERTURA_FASCICOLO_ENTE_COD), REVOCA_ORDINARIA_MANDATO_ENTE(
			Ruoli.REVOCA_ORDINARIA_MANDATO_ENTE_COD), RICHIESTA_REVOCA_IMMEDIATA_AZIENDA_MANDATO(
					Ruoli.REVOCA_IMMEDIATA_MANDATO_AZIENDA_COD), ACCETTAZIONE_REVOCA_IMMEDIATA_MANDATO(
							Ruoli.ACCETTAZIONE_REVOCA_IMMEDIATA_MANDATO_COD), MODIFICA_SPORTELLO_MANDATO(
									Ruoli.MODIFICA_SPORTELLO_MANDATO_COD), AGGIORNAMENTO_MODALITA_PAGAMENTO_FASCICOLO_ENTE(
											Ruoli.AGGIORNAMENTO_MODALITA_PAGAMENTO_FASCICOLO_ENTE_COD), VISUALIZZAZIONE_MODALITA_PAGAMENTO_FASCICOLO_ENTE(
													Ruoli.VISUALIZZAZIONE_MODALITA_PAGAMENTO_FASCICOLO_ENTE_COD), CRUSCOTTO_REVOCA_IMMEDIATA_MANDATO_RICHIESTE_ENTE(
															Ruoli.CRUSCOTTO_REVOCA_IMMEDIATA_MANDATO_RICHIESTE_ENTE_COD), CRUSCOTTO_REVOCA_IMMEDIATA_MANDATO_RICHIESTE_VALUTATE_TUTTI(
																	Ruoli.CRUSCOTTO_REVOCA_IMMEDIATA_MANDATO_RICHIESTE_VALUTATE_TUTTI_COD), DICHIARAZIONI_ASSOCIATIVE_VISUALIZZA_UTENTE(
																			Ruoli.DICHIARAZIONI_ASSOCIATIVE_VISUALIZZA_UTENTE_COD), DICHIARAZIONI_ASSOCIATIVE_EDITA_UTENTE(
																					Ruoli.DICHIARAZIONI_ASSOCIATIVE_EDITA_UTENTE_COD), VISUALIZZAZIONE_FASCICOLO_SOLA_LETTURA_TUTTI(
																							Ruoli.VISUALIZZAZIONE_FASCICOLO_SOLA_LETTURA_TUTTI_COD);
	
	private static final String RICERCA_FASCICOLO_ENTE_COD = "a4gfascicolo.fascicolo.ricerca.ente";
	private static final String RICERCA_FASCICOLO_TUTTI_COD = "a4gfascicolo.fascicolo.ricerca.tutti";
	private static final String RICERCA_FASCICOLO_UTENTE_COD = "a4gfascicolo.fascicolo.ricerca.utente";
	
	public static final String RICERCA_FASCICOLO_FILTRO_ENTE_COD = "a4gfascicolo.fascicolo.ricerca.ente";
	public static final String RICERCA_FASCICOLO_NON_FILTRATA_COD = "a4gfascicolo.fascicolo.ricerca.tutti";
	public static final String RICERCA_FASCICOLO_FILTRO_UTENTE_COD = "a4gfascicolo.fascicolo.ricerca.utente";
	
	private static final String APERTURA_FASCICOLO_ENTE_COD = "a4ganagrafica.fascicolo.apertura.ente";
	private static final String REVOCA_ORDINARIA_MANDATO_ENTE_COD = "a4ganagrafica.mandato.revoca.ente";
	private static final String REVOCA_IMMEDIATA_MANDATO_AZIENDA_COD = "a4ganagrafica.mandato.revocaimmediata";
	private static final String CRUSCOTTO_REVOCA_IMMEDIATA_MANDATO_RICHIESTE_ENTE_COD = "a4ganagrafica.mandato.revocaimmediata.cruscotto.ente";
	private static final String ACCETTAZIONE_REVOCA_IMMEDIATA_MANDATO_COD = "a4ganagrafica.mandato.revocaimmediata.accettazione";
	private static final String MODIFICA_SPORTELLO_MANDATO_COD = "a4ganagrafica.mandato.modificasportello";
	private static final String AGGIORNAMENTO_MODALITA_PAGAMENTO_FASCICOLO_ENTE_COD = "a4ganagrafica.fascicolo.aggiornamento.modalitapagamento.ente";
	private static final String VISUALIZZAZIONE_MODALITA_PAGAMENTO_FASCICOLO_ENTE_COD = "a4ganagrafica.fascicolo.visualizzazione.modalitapagamento.ente";
	private static final String CRUSCOTTO_REVOCA_IMMEDIATA_MANDATO_RICHIESTE_VALUTATE_TUTTI_COD = "a4ganagrafica.mandato.revocaimmediata.cruscotto.valutate.tutti";
	private static final String DICHIARAZIONI_ASSOCIATIVE_VISUALIZZA_UTENTE_COD = "a4ganagrafica.dichiarazioniassociative.visualizza.utente";
	private static final String DICHIARAZIONI_ASSOCIATIVE_EDITA_UTENTE_COD = "a4ganagrafica.dichiarazioniassociative.edita.utente";
	private static final String VISUALIZZAZIONE_FASCICOLO_SOLA_LETTURA_TUTTI_COD = "a4ganagrafica.fascicolo.visualizzazione.tutti";
	
	private String codiceRuolo;
	
	Ruoli(String codiceRuolo) {
		this.codiceRuolo = codiceRuolo;
	}
	
	public String getCodiceRuolo() {
		return codiceRuolo;
	}
}
