/**
 * 
 */
package it.tndigitale.a4gistruttoria.service;

import it.tndigitale.a4gistruttoria.dto.RicevutaDomandaIntegrativaZootecnia;

/**
 * 
 * @author B.Conetta
 *
 */
public interface DomandaIntegrativaService {

	public RicevutaDomandaIntegrativaZootecnia salva(RicevutaDomandaIntegrativaZootecnia ricevutaDI) throws Exception;

	public RicevutaDomandaIntegrativaZootecnia cancella(RicevutaDomandaIntegrativaZootecnia dto);
	
	public byte[] getRicevutaDomandaIntegrativa(Long idDomanda);
}
