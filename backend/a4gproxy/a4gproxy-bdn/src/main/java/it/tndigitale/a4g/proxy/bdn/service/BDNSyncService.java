package it.tndigitale.a4g.proxy.bdn.service;

import it.tndigitale.a4g.proxy.bdn.dto.StatoSincronizzazioneDO;

public interface BDNSyncService {

	/**
	 * Metodo che lancia i servizi per la lettura dei dati da BDN per le domande uniche
	 * 
	 * @param cuaa
	 * @param annoCampagna
	 * @return true se l'elaborazione va tutta a buon fine, false altrimenti
	 */
	public boolean syncDatiDomandaUnica(String cuaa, Integer annoCampagna);

	public Long syncIngressoUscitaPascolo(String cuaa, Integer annoCampagna) throws Exception;

	public Long syncDatiDomandaUnicaPerAnno(Integer annoCampagna);
	
	public void sincronizzaCacheDatiDomandaUnicaPerAnno(Integer annoCampagna) throws Exception;

	public void sincronizzaAzienda(String codiceAzienda) throws Exception;
	
	public void sincronizzaStatoSincronizzazione(boolean elaborazioneDomandaOK, StatoSincronizzazioneDO dati);
}
