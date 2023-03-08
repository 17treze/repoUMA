package it.tndigitale.a4g.proxy.services;

import java.util.concurrent.Future;

import it.tndigitale.a4g.proxy.dto.DatiPagamentiDto;
import it.tndigitale.a4g.proxy.dto.Dichiarazione;
import it.tndigitale.a4g.proxy.dto.SuperficiAccertateDto;

/**
 * Service per la gestione della sincronizzazione dei dati verso sistemi esterni.
 * 
 * @author S.DeLuca
 *
 */
public interface SincronizzazioneService {

	/**
	 * Sincronizza i dati della dichiarazione antimafia verso AGEA.
	 * 
	 */
	public Future<String> dichiarazioneAntimafia(String dichiarazioneAntimafia) throws Exception;

	/**
	 * Cerca la dichiarazione per id.
	 * 
	 * @param id della dichiarazione
	 * @return la dichiarazione se esiste nel DB
	 */
	public Dichiarazione getDichiarazione(Long id);

	/**
	 * Aggiorna la dichiarazione per id.
	 * 
	 * @param dichiarazione
	 * @return la dichiarazione aggiornata
	 * @throws Exception 
	 */
	public Dichiarazione aggiornaDichiarazione(Dichiarazione dichiarazione) throws Exception;

	/**
	 * Sincronizza i dati delle superfici accertate verso AGEA.
	 */
	public void creaSuperficiAccertate(SuperficiAccertateDto superficiAccertate) throws Exception;

	/**
	 * Cancella logicamente tutti i dati sulle superfici accertate già esistenti.
	 * @param Anno campagna per il quale effettuare la cancellazione dei dati
	 */
	public void pulisciSuperficiAccertate(Long annoCampagna);

	/**
	 * Sincronizza i dati dei pagamenti verso AGEA.
	 */
	public void creaDatiPagamenti(DatiPagamentiDto datiPagamenti);

	/**
	 * Cancella logicamente tutti i dati dei pagamenti già esistenti.
	 * @param Anno campagna per il quale effettuare la cancellazione dei dati
	 */
	public void pulisciDatiPagamenti(Long annoCampagna);
}
