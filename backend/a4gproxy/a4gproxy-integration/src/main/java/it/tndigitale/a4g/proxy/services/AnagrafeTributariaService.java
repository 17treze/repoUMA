package it.tndigitale.a4g.proxy.services;

import it.tndigitale.a4g.proxy.dto.persona.PersonaFisicaDto;
import it.tndigitale.a4g.proxy.dto.persona.PersonaGiuridicaDto;
import it.tndigitale.ws.isvalidazioneanagrafe.RispostaRichiestaRispostaSincronaRicercaAnagraficaAll;

/**
 * Service per la gestione dei WS esposti dall'anagrafe tributaria.
 * 
 * @author S.DeLuca
 *
 */
public interface AnagrafeTributariaService {
	
	/**
	 * Cerca l'anagrafica per codice fiscale.
	 * 
	 * @param codiceFiscale
	 * @return {@link RispostaRichiestaRispostaSincronaRicercaAnagraficaAll}
	 * @throws Exception 
	 */
	public RispostaRichiestaRispostaSincronaRicercaAnagraficaAll caricaAnagraficaPersonaFisica(String codiceFiscale) throws Exception;
	
	/**
	 * Carica dati Anagrafe tributaria per persone fisiche
	 *
	 * @param codiceFiscale
	 * @return {@link RispostaRichiestaRispostaSincronaRicercaAnagraficaAll}
	 */
	public PersonaFisicaDto getPersonaFisica(String codiceFiscale) throws Exception;
	
	/**
	 * Carica dati Anagrafe tributaria per persone giuridiche
	 *
	 * @param codiceFiscale
	 * @return {@link RispostaRichiestaRispostaSincronaRicercaAnagraficaAll}
	 */
	public PersonaGiuridicaDto getPersonaGiuridica(String codiceFiscale) throws Exception;
	
}
