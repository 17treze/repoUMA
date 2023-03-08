package it.tndigitale.a4g.ags.repository.dao;

import java.util.List;

import it.tndigitale.a4g.ags.dto.EsitoAntimafia;

public interface AntimafiaDao {

	/**
	 * Inserisce o aggiorna l'esito per il pagamento della dichiarazione antimafia.
	 * 
	 * @param esitiAntimafia
	 * @throws Exception
	 */
	void salva(List<EsitoAntimafia> esitiAntimafia) throws Exception;
	/**
	 * Cancella gli esiti antimafia per CUAA
	 * 
	 * @param cuaa
	 * @throws Exception
	 * 
	 */
	void cancella(String cuaa) throws Exception;
	
	/**
	 * Recupera gli esiti antimafia in base ai parametri
	 * 
	 * @param params
	 * @throws Exception
	 * 
	 */
	List<EsitoAntimafia> recuperaEsiti(EsitoAntimafia esitoAntimafia) throws Exception;
	
	/**
	 * Aggiorna la tabella TPAGA_CERTIFICAZIONI_ANTIMAFIA cancellando i precedenti cuaa e inserendo i nuovi
	 * 
	 * @param cuaaList
	 * @throws Exception
	 * 
	 */
	void sincronizzaCert(List<String> cuaaList) throws Exception;

}
