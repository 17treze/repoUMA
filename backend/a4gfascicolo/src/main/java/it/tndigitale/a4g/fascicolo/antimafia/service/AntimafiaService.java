package it.tndigitale.a4g.fascicolo.antimafia.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import it.tndigitale.a4g.fascicolo.antimafia.dto.AggiornaDichiarazioneEsito;
import it.tndigitale.a4g.fascicolo.antimafia.dto.AllegatoFamiliareConviventeFilter;
import it.tndigitale.a4g.fascicolo.antimafia.dto.AllegatoFamiliariConviventi;
import it.tndigitale.a4g.fascicolo.antimafia.dto.AllegatoFamiliariConviventiResult;
import it.tndigitale.a4g.fascicolo.antimafia.dto.Dichiarazione;
import it.tndigitale.a4g.fascicolo.antimafia.dto.DichiarazioneFilter;
import it.tndigitale.a4g.fascicolo.antimafia.dto.DichiarazionePaginataFilter;
import it.tndigitale.a4g.fascicolo.antimafia.dto.Nota;
import it.tndigitale.a4g.fascicolo.antimafia.dto.NotaFilter;
import it.tndigitale.a4g.fascicolo.antimafia.dto.PageResultWrapper;
import it.tndigitale.a4g.fascicolo.antimafia.dto.Pagination;
import it.tndigitale.a4g.fascicolo.antimafia.dto.ProcedimentiEnum;
import it.tndigitale.a4g.fascicolo.antimafia.dto.ProtocollaCertificazioneAntimafiaDto;
import it.tndigitale.a4g.fascicolo.antimafia.dto.Sort;
import it.tndigitale.a4g.fascicolo.antimafia.dto.StatoDichiarazioneCounter;
import it.tndigitale.a4g.fascicolo.antimafia.dto.StatoDichiarazioneFilter;

/**
 * The Interface AntimafiaService.
 */
public interface AntimafiaService {
	/**
	 * Cerca la dichiarazione per id.
	 *
	 * @param id
	 *            the id
	 * @return the dichiarazione
	 * @throws Exception
	 *             the exception
	 */
	public Dichiarazione getDichiarazione(Long id) throws Exception;

	/**
	 * Crea la dichiarazione passata in input, interrogando PARIX, Anagrafe tributaria per il recupero dei dati utili alla dichiarazione antimafia.
	 *
	 * @param dichiarazione
	 *            the dichiarazione
	 * @return the long
	 * @throws Exception
	 *             the exception
	 */
	public Long creaDichiarazione(Dichiarazione dichiarazione) throws Exception;

	/**
	 * Aggiorna la dichiarazione passata in input.
	 *
	 * @param dichiarazione
	 *            the dichiarazione
	 * @return the long
	 * @throws Exception
	 *             the exception
	 */
	public AggiornaDichiarazioneEsito aggiornaDichiarazione(Dichiarazione dichiarazione) throws Exception;
	
	public AggiornaDichiarazioneEsito chiudiRecreaDichiarazione(Dichiarazione daChiudere,Dichiarazione exNovo) throws Exception;

	/**
	 * Aggiorna pdf dichiarazione.
	 *
	 * @param id
	 *            the id
	 * @return the long
	 * @throws Exception
	 *             the exception
	 */
	public Long aggiornaPdfDichiarazione(Long id) throws Exception;

	/**
	 * Cerca le dichiarazioni basandosi sul fultro in input.
	 *
	 * @param dichiarazioneInput
	 *            the dichiarazione input
	 * @return the dichiarazioni
	 * @throws Exception
	 *             the exception
	 */
	public List<Dichiarazione> getDichiarazioni(DichiarazioneFilter dichiarazioneInput) throws Exception;
	
	/**
	 * Cerca le dichiarazioni basandosi sul fultro in input.
	 *
	 * @param dichiarazioneInput
	 *            the dichiarazione input
	 * @return the dichiarazioni
	 * @throws Exception
	 *             the exception
	 */
	public PageResultWrapper<Dichiarazione> getDichiarazioniPaginata(DichiarazionePaginataFilter dichiarazioneInput, Pagination pagination, Sort sort) throws Exception;

	/**
	 * Eliminazione della dichiarazione antimafia per id.
	 *
	 * @param id
	 *            = identificativo della dichiarazione antimafia
	 * @return identificativo della dichiarazione antimafia eliminato
	 */
	public Long eliminaDichiarazione(Long id);

	/**
	 * Caricameto dell'allegato per la fichiarazione dei familiari conviventi.
	 *
	 * @param allegatoFamiliariConviventi
	 *            the allegato familiari conviventi
	 * @param documento
	 *            the documento
	 * @param idDichiarazione
	 *            the id dichiarazione
	 * @return id dell' entity salavata
	 * @throws Exception
	 *             the exception
	 */
	public AllegatoFamiliariConviventiResult allegaFamiliariConviventi(AllegatoFamiliariConviventi allegatoFamiliariConviventi, MultipartFile documento, Long idDichiarazione) throws Exception;

	/**
	 * Effettua le operazioni di salvataggio, protocollazione e sincronizzazione della domanda antimafia
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Dichiarazione processoProtocollazioneDomanda(Long id) throws Exception;

	/**
	 * Salva nel DB le informazioni della dichiarazione antimafia
	 *
	 * @param id
	 *            della dichiarazione antimafia
	 * @return dichiarazione antimafia
	 * @throws Exception
	 */
	public ProtocollaCertificazioneAntimafiaDto saveDomanda(Long id) throws Exception;

	/**
	 * Protocolla i documenti della dichiarazione antimafia
	 * @param protocollaCertificazioneAntimafiaDto
	 * @throws Exception
	 */
	public void protocollaDomanda(ProtocollaCertificazioneAntimafiaDto protocollaCertificazioneAntimafiaDto) throws Exception;

	/**
	 * Conta quante sono le dichiarazioni in ciascuno stato
	 *
	 *
	 * @return restituisce un oggetto che contiene il count delle dichiarazioni in ciascuno stato
	 * @throws Exception
	 */
	public StatoDichiarazioneCounter countEsitoDichiarazioni(StatoDichiarazioneFilter stati) throws Exception;

	/**
	 * Download del file allegato per i familiari conviventi
	 * 
	 * @param allegatoFamiliareConviventeFilter
	 * @return
	 */
	public byte[] downloadAllegatoFamiliareConvivente(AllegatoFamiliareConviventeFilter allegatoFamiliareConviventeFilter);

	/**
	 * Crea la nota relativa alla dichiarazione antimafia.
	 * 
	 * @param nota
	 * @return
	 * @throws Exception
	 */
	public Long creaNota(Nota nota) throws Exception;

	/**
	 * Legge le note della dichiarazione antimafia.
	 * 
	 * @param notaFilter
	 * @return
	 * @throws Exception
	 */
	public List<Nota> leggiNote(NotaFilter notaFilter) throws Exception;

	/**
	 * recupera le informazioni dal servizio esterno di Anagrafica Impresa (Parix)
	 * @param cuaa
	 * @return
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 * @throws JsonProcessingException
	 */
	JsonNode getAnagraficaImpresa(String cuaa) throws URISyntaxException, IOException, Exception, UnsupportedEncodingException, JsonProcessingException;

	/**
	 * crea i procedimenti e li collega alla rispettiva dichiarazione antimafia. Per semplicità di gestione frontend 
	 * e backend vengono prima eliminati quelli esistenti e poi inseriti quelli nuovi
	 * @param idDichiarazione 
	 * @return
	 */
	public List<ProcedimentiEnum> creaProcedimenti(Long idDichiarazione, List<ProcedimentiEnum> procedimenti);

	/**
	 * recupera i procediment di una dichiarazione antimafia
	 * @param idDichiarazione
	 * @return
	 */
	public List<ProcedimentiEnum> recuperaProcedimenti(Long idDichiarazione);

	/**
	 * Crea un csv della dichiarazione antimafia
	 * @param dichiarazioniAntimafia
	 * @return
	 */
	byte[] creazioneCsv(final List<Dichiarazione> dichiarazioniAntimafia) throws IOException;	
	
	/**
	 * Sincronizzazione della domanda
	 * @param dichiarazione
	 * @return
	 */
	public void sincronizzazioneProtocollaDomanda(Dichiarazione dichiarazione) throws Exception;
	
	/**
	 * 
	 * @param dichiarazione
	 * @throws Exception
	 */
	public void saveOrUpdateDichiarazioneAntimafia(Long idDomandaAntimafia, String idProtocollazione, Date dataProtocollazione) throws Exception;

	Dichiarazione getDichiarazioneByCuaa(String cuaa) throws Exception;
}
