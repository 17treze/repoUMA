package it.tndigitale.a4g.fascicolo.antimafia.service;

import java.util.List;

import it.tndigitale.a4g.fascicolo.antimafia.dto.KeyValueStringString;
import it.tndigitale.a4g.fascicolo.antimafia.dto.consultazione.Fascicolo;
/**
 * 
 * @author B.Irler
 *
 */
public interface ConsultazioneService {

	/**
	 * Ricerca il fascicolo in base ai parametri di input.
	 * 
	 * @param fascicolo
	 * @return
	 */
	public List<Fascicolo> getFascicoli(String params)  throws Exception ;

	public List<Fascicolo> getFascicoliAziendeUtente() throws Exception;

	/**
	 * Ricerca il fascicolo per id.
	 * @param idFascicolo
	 * @return
	 */
	public Fascicolo getFascicolo(Long idFascicolo) throws Exception;
	
	/**
	 * Recupera l'azienda se la Persona è Titolate/Legale Rappresentante dell'azienda stessa
	 * @param cuaa
	 * @return
	 */
	public KeyValueStringString getAziendaPersonaRappresentante(String cfPersona, String cuaa) throws Exception;
	
	/**
	 * Verifica se l'azienda ha un fascicolo valido in ags 
	 * @param cuaa
	 * @return
	 */
	public boolean controllaEsistenzaFascicoloValido(String cuaa) throws Exception;
	
	
	public List<Fascicolo> getFascicoliEnti(String params);

	Fascicolo getFascicolo(String cuaa) throws Exception;
}
