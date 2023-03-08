package it.tndigitale.a4g.proxy.bdn.repository.interfaces;

import java.util.List;

import it.tndigitale.a4g.proxy.bdn.dto.StatoSincronizzazioneDO;

public interface StatoSincronizzazioneBdnDAO {

	/**
	 * Carica dalla tabella TDOM_CUAA_SCARICO_BDN i cuaa per i quali va fatta la sincronizzazione in base allo stato e all'ultima esecuzione
	 * 
	 * @return
	 */
	public List<StatoSincronizzazioneDO> getListaCuaaDaSincronizzare(Integer annoCampagna);

	/**
	 * Aggiorna lo stato di un record della tabella TDOM_CUAA_SCARICO_BDN (SUCCESS, FAIL, RETRY)
	 * 
	 * @param annoCampagna
	 * @param cuaa
	 * @param statoEsecuzione
	 */
	public void aggiornaStatoSincronizzazione(Integer annoCampagna, String cuaa, String statoEsecuzione);

	/**
	 * Verifica i cuaa già presenti nella tabella di sincronizzazione per anno di campagna
	 * 
	 * @param annoCampagna
	 * @return
	 */
	public List<String> getListaCuaaTabella(Integer annoCampagna);

	/**
	 * inserisce un nuovo record da sincronizzare
	 * 
	 * @param annoCampagna
	 * @param cuaa
	 */
	public void insertCuaa(Integer annoCampagna, String cuaa);

	public List<StatoSincronizzazioneDO> findAllPerCampagna(Integer annoCampagna);

}
