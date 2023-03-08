package it.tndigitale.a4g.ags.service;

import java.util.List;

import it.tndigitale.a4g.ags.dto.EsitoAntimafia;

/**
 * 
 * @author S.DeLuca
 *
 */
public interface AntimafiaService {

	/**
	 * Inserisce o aggiorna l'esito per il pagamento della dichiarazione antimafia.
	 * 
	 * @param esitiAntimafia
	 * @throws Exception
	 */
	public void salva(List<EsitoAntimafia> esitiAntimafia) throws Exception;

	/**
	 * Cancella gli esiti antimafia per CUAA
	 * 
	 * @param cuaa
	 * @throws Exception
	 * 
	 */
	public void cancella(EsitoAntimafia esitoAntimafia) throws Exception;
	
	
	/**
	 * Recupera gli esiti antimafia in base ai parametri
	 * 
	 * @param params
	 * @throws Exception
	 * 
	 */
	public List<EsitoAntimafia> recuperaEsiti(EsitoAntimafia esitoAntimafia) throws Exception;
	
	
	/**
	 * Sincronizza la tabella TPAGA_CERTIFICAZIONI_ANTIMAFIA con i dati provenienti da istruttoria
	 * 
	 * @param cuaaList
	 * @throws Exception
	 * 
	 */
	public void sincronizzaCert(List<String> cuaaList) throws Exception;

}
