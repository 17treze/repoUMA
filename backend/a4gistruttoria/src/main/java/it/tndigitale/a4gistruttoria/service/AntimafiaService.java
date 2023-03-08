/**
 * 
 */
package it.tndigitale.a4gistruttoria.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import it.tndigitale.a4gistruttoria.dto.CertificazioneAntimafiaFilter;
import it.tndigitale.a4gistruttoria.dto.CsvFile;
import it.tndigitale.a4gistruttoria.dto.DatiElaborazioneProcesso;
import it.tndigitale.a4gistruttoria.dto.DomandaCollegata;
import it.tndigitale.a4gistruttoria.dto.DomandaCollegataFilter;
import it.tndigitale.a4gistruttoria.dto.DomandeCollegateExport;
import it.tndigitale.a4gistruttoria.dto.DomandeCollegateImport;
import it.tndigitale.a4gistruttoria.dto.EsitiBdna;
import it.tndigitale.a4gistruttoria.dto.PageResultWrapper;
import it.tndigitale.a4gistruttoria.dto.Pagination;
import it.tndigitale.a4gistruttoria.dto.ProcessoFilter;
import it.tndigitale.a4gistruttoria.dto.SogliaAcquisizioneFilter;
import it.tndigitale.a4gistruttoria.dto.Sort;
import it.tndigitale.a4gistruttoria.dto.TrasmissioneBdnaDto;
import it.tndigitale.a4gistruttoria.dto.TrasmissioneBdnaFilter;
import it.tndigitale.a4gistruttoria.dto.antimafia.DichiarazioneAntimafiaConEsiti;
import it.tndigitale.a4gistruttoria.dto.antimafia.SogliaAcquisizione;
import it.tndigitale.a4gistruttoria.util.StatoDichiarazioneEnum;

/**
 * @author B.Conetta
 *
 */
public interface AntimafiaService {

	/**
	 * Avvia il controllo antimafia per gli id passati in input.
	 * 
	 * @param ids
	 */
	public void avviaControllo(List<Long> ids) throws Exception;

	/**
	 * Restituisce i dati elaborati dal controllo domanda antimafia
	 * 
	 * @param id
	 *            della domanda antimafia controllata
	 * @return i dati elaborati dal controllo domanda antimafia
	 */
	public DatiElaborazioneProcesso getElaborazioneControlloAntimafia(Long idAntimafia) throws Exception;

	/**
	 * Importa i dati strutturali per le domande antimafia collegate all bdn
	 * 
	 * @param ids
	 */
	public List<DomandaCollegata> importaDatiStrutturali(DomandeCollegateImport domandeCollegateImport) throws Exception;

	/**
	 * Importa i dati della domanda unica.
	 * 
	 * @param domandeCollegateImport
	 * @param csv
	 */
	public List<DomandaCollegata> importaDatiDU(DomandeCollegateImport domandeCollegateImport, MultipartFile csv) throws Exception;

	/**
	 * Importa i dati delle domande PSR superfice EU
	 * 
	 * @param domandeCollegateImport:
	 *            paramentri utilizzati per filtrare le domande
	 * @throws Exception
	 */
	public void importaDatiSuperficie(DomandeCollegateImport domandeCollegateImport) throws Exception;

	/**
	 * Recupero informazioni dichiarazioni antimafia combinate con le informazioni di istruttoria
	 * @param pagination 
	 * 
	 * @return lista delle dichiarazioni antimafia in stato POSITIVO con relative informazioni riguardanti lo stato degli import
	 * @throws Exception
	 */
	public PageResultWrapper<DichiarazioneAntimafiaConEsiti> getDomandeCollegateConEsiti(CertificazioneAntimafiaFilter filter, Pagination pagination, Sort sort) throws Exception;

	/**
	 * Recupero del dettaglio della domanda collegata
	 * 
	 * @return dettaglio della domanda collegata
	 * @throws Exception
	 */
	public DomandaCollegata getDettaglioDomandeCollegate(Long id) throws Exception;

	/**
	 * @param Crea
	 *            il file CSV dalle domande collegate filtrate per CUAA e stato NON CARICATO or ANOMALIA
	 * @return byte array scaricabile da front end
	 * @throws Exception
	 */
	public CsvFile creaCsvFile(DomandeCollegateExport domandeCollegateExport) throws Exception;

	/**
	 * Aggiorna stato di una trasmissione bdna
	 * 
	 * @param oggetto trasmissione BDNA da aggiornare
	 * @param cfOperatore - required
	 * @throws Exception
	 */
	public TrasmissioneBdnaDto aggiornaTrasmissioneBdna(TrasmissioneBdnaDto trasmissioneBdnaDto) throws Exception;

	/**
	 * Elimina trasmissione dbna
	 * 
	 * @param id trasmissione da eliminare
	 * @throws Exception
	 */
	public void cancellaTrasmissioneBdna(Long idTrasmissione) throws Exception;

	/**
	 * Aggiorna la domanda collegata passata in input.
	 * 
	 * @param domandaCollegata
	 * @return domandaCollegata aggiornata
	 * @throws Exception
	 */
	public DomandaCollegata aggiornaDomandaCollegate(DomandaCollegata domandaCollegata) throws Exception;

	/**
	 * Restituisce le domande collegate all'istruttoria antimafia che corrispono ai criteri immessi nei parametri.
	 * 
	 * @param domandaCollegataFilter
	 * @return lista di domande collegate all'istruttoria antimafia
	 * @throws Exception
	 */
	public List<DomandaCollegata> getDomandeCollegate(DomandaCollegataFilter domandaCollegataFilter) throws Exception;
	
	/**
	 * Restituisce lo satto di avanzamento del processo in esecuzione
	 * 
	 * @param processoFilter
	 * @return oggetto contenente lo stato di avanzamento del processo in esecuzione
	 * @throws Exception
	 */
	public DatiElaborazioneProcesso getStatoAvanzamento(ProcessoFilter processoFilter) throws Exception;

//	/**
//	 * Passaggio automatico a verifica periodica
//	 * 
//	 * @throws Exception
//	 */
//	public void passaggioVerificaPeriodica() throws Exception;
	
	/**
	 * PROCESSO AUTOMATICO CHE OGNI NOTTE  SINCORNIZZA CON AGS-FASCICOLO TUTTE LE CERTIFICAZIONI 
	 * ANTIMAFIA IN STATO 'IN_ISTRUTTORIA' (CONTROLLATE)
	 * @throws Exception
	 */
	public void sincronizzazioneCertificazioniAntimafiaAgs() throws Exception;
	
	public SogliaAcquisizione getSogliaAcquisizione(SogliaAcquisizioneFilter sogliaAcquisizioneFilter) throws Exception;

	public EsitiBdna importaEsitiBDNA(MultipartFile csv) throws Exception;

	
	/**
	 * Get Trasmissioni a BDNA 
	 * 
	 * @param id - identificativo trasmissione
	 * @param cfOperatore - codice fiscale operatore che effettua la richiesta
	 * @param statoTrasmissione - stato della trasmissione (NON_CONFERMATA || CONFERMATA)
	 * @throws Exception
	 */
	public List<TrasmissioneBdnaDto> getTrasmissioniBdna(TrasmissioneBdnaFilter trasmissioneBdnaIn) throws Exception;

	/**
	 * Recupero informazioni dichiarazioni antimafia combinate con le informazioni delle domande collegate
	 * @return lista delle dichiarazioni antimafia in tutti gli stati diversi da CHIUSA e relative informazioni riguardanti le domande collegate
	 * @throws Exception
	 */
	public CsvFile getDichiarazioniAntimafiaConDomandeCollegate() throws Exception;
	
	/**
	 * Recupero file csv collegato alla trasmissione 
	 * @return lista delle dichiarazioni antimafia in tutti gli stati diversi da CHIUSA e relative informazioni riguardanti le domande collegate
	 * @throws Exception
	 */
	public CsvFile scaricaCsv(Long id) throws Exception;
	
}