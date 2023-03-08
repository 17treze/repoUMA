package it.tndigitale.a4g.proxy.services;

import java.util.List;

import it.tndigitale.a4g.proxy.dto.DettaglioImpresa;
import it.tndigitale.a4g.proxy.dto.persona.PersonaFisicaDto;
import it.tndigitale.a4g.proxy.dto.persona.PersonaGiuridicaDto;
import it.tndigitale.a4g.proxy.dto.persona.UnitaLocaleDto;
import it.tndigitale.ws.wssanagraficaimprese.RISPOSTA;

/**
 * Service per la gestione dei WS esposti dall'anagrafica impresa.
 * 
 * @author S.DeLuca
 *
 */
public interface AnagraficaImpresaService {

	/**
	 * Cerca l'anagrafica dell'impresa per anziende non cessate.
	 * @param codiceFiscale
	 * @return {@link RISPOSTA}
	 * @throws Exception
	 */
    public RISPOSTA getAnagraficaImpresaNonCessata(String codiceFiscale) throws Exception;
    
    /**
     * Cerca il dettaglio completo dell'azienda passando in input i parametri del {@link DettaglioImpresa}
     * 
     * @param ricercaImpresa
     * @return {@link RISPOSTA}
     * @throws Exception
     */
    public RISPOSTA getDettaglioCompletoImpresa(DettaglioImpresa ricercaImpresa) throws Exception;
   
    
	/**
	 * Cerca i dettegli delle persone non cessate.
	 * @param codiceFiscale
	 * @return {@link RISPOSTA}
	 * @throws Exception
	 */
    public RISPOSTA getPersoneNonCessata(String codiceFiscale) throws Exception;
    
    public PersonaFisicaDto getPersonaFisica(String codiceFiscale, String provinciaIscrizione) throws Exception;

	public PersonaGiuridicaDto getPersonaGiuridica(String codiceFiscale, String provinciaIscrizione) throws Exception;

//	public List<UnitaLocaleDto> getUnitaLocaliPersonaGiuridica(String codiceFiscale, String provinciaIscrizione) throws Exception ;
//
//	public List<UnitaLocaleDto> getUnitaLocaliPersonaFisica(String codiceFiscale, String provinciaIscrizione) throws Exception ;
    
}
