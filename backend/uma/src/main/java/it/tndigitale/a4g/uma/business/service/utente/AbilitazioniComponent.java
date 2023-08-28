package it.tndigitale.a4g.uma.business.service.utente;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.fascicolo.anagrafica.client.model.CaricaAgsDto;
import it.tndigitale.a4g.fascicolo.anagrafica.client.model.DetenzioneAgsDto;
import it.tndigitale.a4g.fascicolo.anagrafica.client.model.FascicoloAgsDto;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.framework.security.service.UtenteClient;
import it.tndigitale.a4g.framework.support.ListSupport;
import it.tndigitale.a4g.uma.Ruoli;
import it.tndigitale.a4g.uma.business.persistence.entity.AllegatoConsuntivoModel;
import it.tndigitale.a4g.uma.business.persistence.entity.ClienteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.ConsuntivoConsumiModel;
import it.tndigitale.a4g.uma.business.persistence.entity.DichiarazioneConsumiModel;
import it.tndigitale.a4g.uma.business.persistence.entity.DistributoreModel;
import it.tndigitale.a4g.uma.business.persistence.entity.PrelievoModel;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.StatoDichiarazioneConsumi;
import it.tndigitale.a4g.uma.business.persistence.entity.StatoRichiestaCarburante;
import it.tndigitale.a4g.uma.business.persistence.entity.TrasferimentoCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.repository.AllegatiConsuntivoDao;
import it.tndigitale.a4g.uma.business.persistence.repository.ClienteDao;
import it.tndigitale.a4g.uma.business.persistence.repository.ConsuntiviConsumiDao;
import it.tndigitale.a4g.uma.business.persistence.repository.DichiarazioneConsumiDao;
import it.tndigitale.a4g.uma.business.persistence.repository.DistributoriDao;
import it.tndigitale.a4g.uma.business.persistence.repository.PrelieviDao;
import it.tndigitale.a4g.uma.business.persistence.repository.RichiestaCarburanteDao;
import it.tndigitale.a4g.uma.business.persistence.repository.TrasferimentoCarburanteDao;
import it.tndigitale.a4g.uma.business.service.client.UmaAnagraficaClient;
import it.tndigitale.a4g.uma.business.service.client.UmaUtenteClient;
import it.tndigitale.a4g.uma.business.service.elenchi.TipoElenco;
import it.tndigitale.a4g.uma.dto.aual.RappresentanteLegaleAualDto;
import it.tndigitale.a4g.uma.dto.aual.SoggettoAualDto;
import it.tndigitale.a4g.uma.dto.richiesta.PrelievoDto;

/**
 * Classe che si occupa di gestire i controlli autorizzativi ai servizi. In una prima fase un livello 3 programmatico
 * (controllo ruolo). E' stato fatto cosi in ottica di estendere i controlli sui dati anche
 */
@Component("abilitazioniComponent")
public class AbilitazioniComponent {
	
	private static final Logger logger = LoggerFactory.getLogger(AbilitazioniComponent.class);
	private static final List<StatoRichiestaCarburante> STATI_SOLA_LETTURA_RICHIESTA = Arrays
			.asList(StatoRichiestaCarburante.AUTORIZZATA, StatoRichiestaCarburante.RETTIFICATA);
	private static final List<StatoDichiarazioneConsumi> STATI_SOLA_LETTURA_DICHIARAZIONE_CONSUMI = Arrays
			.asList(StatoDichiarazioneConsumi.PROTOCOLLATA);
	
	@Autowired
	private UtenteComponent utenteComponent;
	@Autowired
	private UtenteClient abilitazioniUtente;
	@Autowired
	private UmaUtenteClient umaUtenteClient;
	@Autowired
	private UmaAnagraficaClient anagraficaClient;
	@Autowired
	private RichiestaCarburanteDao richiestaCarburanteDao;
	@Autowired
	private DichiarazioneConsumiDao dichiarazioneConsumiDao;
	@Autowired
	private ClienteDao clienteDao;
	@Autowired
	private ConsuntiviConsumiDao consuntiviConsumiDao;
	@Autowired
	private AllegatiConsuntivoDao allegatiConsuntivoDao;
	@Autowired
	private PrelieviDao prelieviDao;
	@Autowired
	private DistributoriDao distributoriDao;
	@Autowired
	private TrasferimentoCarburanteDao trasferimentoCarburanteDao;
	
	/********** RICERCA **********/
	
	/**
	 * Autorizza la lettura di tutte le domande uma 1. Controlla ruoli ricerca tutti
	 */
	public boolean checkRicercaTuttiDomandeUma() {
		return utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_RICERCA_TUTTI);
	}
	
	/**
	 * Autorizza la lettura di tutte le domande uma da parte di un operatore CAA 1. Controlla ruoli ricerca ENTE
	 */
	public boolean checkRicercaEnteDomandeUma() {
		return utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_RICERCA_ENTE);
	}
	
	/**
	 * Autorizza la lettura di una generica domanda uma 1. Controlla ruoli ricerca
	 */
	public boolean checkRicercaDomandeUma() {
		return utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_RICERCA_TUTTI, Ruoli.DOMANDE_UMA_RICERCA_ENTE,
				Ruoli.DOMANDE_UMA_RICERCA_UTENTE);
	}
	
	/**
	 * Autorizza la ricerca per cuaa dei dati inerenti una o più domande uma 1. controllo dei ruoli ricerca 2. controllo
	 * correlazione utente al mandato del fascicolo
	 * 
	 * @param cuaa
	 * @return
	 * @throws Exception
	 */
	public boolean checkRicercaDomandaUma(String cuaa) throws Exception {
		
		if (utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_RICERCA_TUTTI)) {
			return true;
		}
		else if (utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_RICERCA_ENTE)) {
			return isFascicoloAbilitatoEnte(cuaa);
		}
		else if (utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_RICERCA_UTENTE)) {
			return isFascicoloAbilitatoUtente(cuaa);
		}
		return false;
	}
	
	/**
	 * Autorizza la lettura di una richiesta di carburante 1. Controlla ruoli ricerca 2. controllo correlazione utente
	 * alla detenzione del fascicolo
	 * 
	 * @throws Exception
	 */
	public boolean checkRicercaRichiestaDiCarburante(Long idRichiesta) throws Exception {
		Optional<RichiestaCarburanteModel> richiestaOpt = richiestaCarburanteDao.findById(idRichiesta);
		if (!richiestaOpt.isPresent()) {
			return false;
		}
		String cuaa = richiestaOpt.get().getCuaa();
		
		if (utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_RICERCA_TUTTI)) {
			return true;
		}
		else if (utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_RICERCA_ENTE)) {
			return isFascicoloAbilitatoEnte(cuaa);
		}
		else if (utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_RICERCA_UTENTE)) {
			return isFascicoloAbilitatoUtente(cuaa);
		}
		return false;
	}
	
	/**
	 * Autorizza la lettura di una dichiarazione consumi 1. Controlla ruoli ricerca 2. Controllo correlazione utente
	 * alla detenzione del fascicolo
	 * 
	 * @throws Exception
	 */
	public boolean checkRicercaDichiarazioneConsumi(Long idConsumi) throws Exception {
		Optional<DichiarazioneConsumiModel> dichiarazioneOpt = dichiarazioneConsumiDao.findById(idConsumi);
		if (!dichiarazioneOpt.isPresent()) {
			return false;
		}
		String cuaa = dichiarazioneOpt.get().getRichiestaCarburante().getCuaa();
		if (utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_RICERCA_TUTTI)) {
			return true;
		}
		else if (utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_RICERCA_ENTE)) {
			return isFascicoloAbilitatoEnte(cuaa);
		}
		else if (utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_RICERCA_UTENTE)) {
			return isFascicoloAbilitatoUtente(cuaa);
		}
		return false;
	}
	
	/**
	 * Autorizza la lettura dei consuntivi in dichiarazione consumi. 1. Controlla che @idConsuntivo appartenga alla
	 * dichiarazione consumi @idDichiarazione 2. Controlla ruoli ricerca 3. Controllo correlazione utente alla
	 * detenzione del fascicolo
	 * 
	 * @param idDichiarazione
	 * @param idConsuntivo
	 * @throws Exception
	 */
	public boolean checkRicercaConsuntiviDichiarazioneConsumi(Long idDichiarazione, Long idConsuntivo)
			throws Exception {
		Optional<ConsuntivoConsumiModel> consuntivoOpt = consuntiviConsumiDao.findById(idConsuntivo);
		
		if (!consuntivoOpt.isPresent()) {
			return false;
		}
		Long id = consuntivoOpt.get().getDichiarazioneConsumi().getId();
		
		if (!idDichiarazione.equals(id)) {
			logger.warn("Il consuntivo {} non e' associato alla dichiarazione consumi {}", idConsuntivo,
					idDichiarazione);
			return false;
		}
		
		return checkRicercaDichiarazioneConsumi(id);
	}
	
	/**
	 * Autorizza la lettura degli allegati associati a un consuntivo di una dichiarazione consumi. 1. Controlla
	 * che @idAllegato appartenza al consuntivo @idConsuntivo 2. Controlla che @idConsuntivo appartenga alla
	 * dichiarazione consumi @idDichiarazione 3. Controlla ruoli ricerca 4. Controllo correlazione utente alla
	 * detenzione del fascicolo
	 * 
	 * @param idDichiarazione
	 * @param idConsuntivo
	 * @param idAllegato
	 * @throws Exception
	 */
	public boolean checkRicercaAllegatiConsuntivoDichiarazioneConsumi(Long idDichiarazione, Long idConsuntivo,
			Long idAllegato) throws Exception {
		Optional<AllegatoConsuntivoModel> allegatoOpt = allegatiConsuntivoDao.findById(idAllegato);
		if (allegatoOpt.isPresent() && !allegatoOpt.get().getConsuntivoModel().getId().equals(idConsuntivo)) {
			logger.warn("L'allegato {} non e' associato al consuntivo {} della dichiarazione consumi {}", idAllegato,
					idConsuntivo, idDichiarazione);
			return false;
		}
		return checkRicercaConsuntiviDichiarazioneConsumi(idDichiarazione, idConsuntivo);
	}
	
	/**
	 * Autorizza la lettura dei clienti di una dichiarazione consumi. 1. Controlla che @idCliente appartenga alla
	 * dichiarazione consumi @idDichiarazione 2. Controlla ruoli ricerca 3. Controllo correlazione utente alla
	 * detenzione del fascicolo
	 * 
	 * @throws Exception
	 */
	public boolean checkRicercaClientiDichiarazioneConsumi(Long idDichiarazione, Long idCliente) throws Exception {
		Optional<ClienteModel> clienteOpt = clienteDao.findById(idCliente);
		if (clienteOpt.isPresent() && !clienteOpt.get().getDichiarazioneConsumi().getId().equals(idDichiarazione)) {
			logger.warn("Il cliente {} non e' associato alla dichiarazione consumi {}", idCliente, idDichiarazione);
			return false;
		}
		return checkRicercaDichiarazioneConsumi(idDichiarazione);
	}
	
	/**
	 * Autorizza la lettura del carburante ricevuto dall'azienda indicata per quell'anno di campagna 1. Controlla che
	 * l'azienda abbia una richiesta in stato autorizzata (requisito necessario per effettuare un trasferimento) 2.
	 * Controlla ruoli ricerca 3. Controllo correlazione utente alla detenzione del fascicolo
	 * 
	 * @param cuaa
	 * @param campagna
	 * @return
	 * @throws Exception
	 */
	public boolean checkRicercaCarburanteRicevuto(String cuaa, Long campagna) throws Exception {
		// controllo di abilitazione: verificare che l'utente sia abilitato a visualizzare il carburante ricevuto dal cuaa indicato
		RichiestaCarburanteModel richiesta = richiestaCarburanteDao
				.findByCuaaAndCampagnaAndStato(cuaa, campagna, StatoRichiestaCarburante.AUTORIZZATA)
				.orElseThrow(() -> new IllegalArgumentException(
						"Nessuna richiesta autorizzata trovata per il cuaa ".concat(cuaa)));
		return checkRicercaRichiestaDiCarburante(richiesta.getId());
	}
	
	/**
	 * Autorizza la lettura di dati da parte di un distributore di carburante. 1. Controlla se l'utente connesso è
	 * correlato al distributore indicato 2. Controlla ruoli 3. Controlla esistenza distributore uma
	 */
	public boolean checkRicercaPrelievoDistributore(Long idDistributore) {
		Optional<DistributoreModel> distributoreOpt = distributoriDao.findById(idDistributore);
		
		if (distributoreOpt.isPresent()) {
			return checkCreaPrelievoDistributore(distributoreOpt.get().getIdentificativo());
		}
		else {
			logger.warn("[ABILITAZIONE FALLITA] - Nessun distributore con id {}", idDistributore);
			return false;
		}
	}
	
	/**
	 * Controlla se l'utente connesso è abilitato a chiamare il servizio in base al tipo elenco fornito 1. Controlla
	 * ruoli in lettura (TUTTI, ENTE) 3. Controlla in base alla tipologia di download cosa è possibile scaricare
	 */
	public boolean checkCreazioneElenchi(TipoElenco tipo) {
		// accessibili solo agli istruttori
		if (TipoElenco.LAVORAZIONI_CLIENTI_CONTO_TERZI.equals(tipo) || TipoElenco.ACCISE.equals(tipo)) {
			return utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_RICERCA_TUTTI);
		}
		return utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_RICERCA_TUTTI, Ruoli.DOMANDE_UMA_RICERCA_ENTE);
	}
	
	/********** MODIFICA **********/
	
	/**
	 * Autorizza la modifica di una domanda uma. 1. controllo dei ruoli edita 2. controllo correlazione utente al
	 * mandato del fascicolo
	 * 
	 * @param cuaa
	 * @return
	 * @throws Exception
	 */
	public boolean checkPresentaDomandaUma(String cuaa) throws Exception {
		
		if (utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_EDITA_ENTE)) {
			return isFascicoloAbilitatoEnte(cuaa);
		}
		else if (utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_EDITA_UTENTE)) {
			return isFascicoloAbilitatoUtente(cuaa);
		}
		return false;
	}
	
	/**
	 * Autorizza la creazione di prelievo da parte di un distributore di carburante. 1. Controlla se l'utente connesso è
	 * correlato al distributore indicato 2. Controlla ruoli
	 * 
	 * @param identificativoDistributore
	 * @return
	 */
	public boolean checkCreaPrelievoDistributore(Long identificativoDistributore) {
		// istruttore uma vede tutti i distributori 
		if (utenteComponent.haRuolo(Ruoli.ISTRUTTORE_UMA)) {
			return true;
		}
		if (!utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_RICERCA_TUTTI)
				|| umaUtenteClient.getDistributoreById(identificativoDistributore) == null) {
			logger.warn("[ABILITAZIONE FALLITA] - Il distributore {} non risulta essere di competenza dell'utente {}",
					identificativoDistributore, umaUtenteClient.getUtenteConnesso().getCodiceFiscale());
			return false;
		}
		return true;
	}
	
	/**
	 * Autorizza la modifica di una richiesta di carburante. 1. controlla che non sia in uno stato di sola lettura 2.
	 * controlla i ruoli edita 3. controllo correlazione utente al mandato del fascicolo
	 * 
	 * @param idRichiesta
	 * @return
	 * @throws Exception
	 */
	public boolean checkModificaRichiestaCarburante(Long idRichiesta) throws Exception {
		Optional<RichiestaCarburanteModel> richiestaOpt = richiestaCarburanteDao.findById(idRichiesta);
		
		if (!richiestaOpt.isPresent()) {
			return false;
		}
		RichiestaCarburanteModel richiesta = richiestaOpt.get();
		if (STATI_SOLA_LETTURA_RICHIESTA.contains(richiesta.getStato())) {
			logger.warn("[Abilitazioni Component] - Si sta tentando di modificare la domanda {} che è in stato {}",
					idRichiesta, richiesta.getStato());
			return false;
		}
		return checkPresentaDomandaUma(richiesta.getCuaa());
	}
	
	/**
	 * Autorizza la modifica dei dati di una dichiarazione consumi. Se ha ruolo di modifica della data di conduzione
	 * ritorna true 1. controlla che non sia in uno stato di sola lettura 2. controlla i ruoli edita 3. controllo
	 * correlazione utente al mandato del fascicolo
	 * 
	 * @param idDichiarazione
	 * @return
	 * @throws Exception
	 */
	public boolean checkModificaDichiarazioneConsumi(Long idDichiarazione) throws Exception {
		Optional<DichiarazioneConsumiModel> dichiarazioneOpt = dichiarazioneConsumiDao.findById(idDichiarazione);
		if (!dichiarazioneOpt.isPresent()) {
			logger.warn("[Abilitazioni Component] - Nessuna dichiarazione consumi trovata con id {}", idDichiarazione);
			return false;
		}
		DichiarazioneConsumiModel dichiarazione = dichiarazioneOpt.get();
		if (STATI_SOLA_LETTURA_DICHIARAZIONE_CONSUMI.contains(dichiarazione.getStato())) {
			logger.warn(
					"[Abilitazioni Component] - Si sta tentando di modificare la Dichiarazione Consumi {} che è in stato {}",
					idDichiarazione, dichiarazione.getStato());
			return false;
		}
		
		if (utenteComponent.haUnRuolo(Ruoli.ISTRUTTORE_UMA)) {
			return true;
		}
		return checkPresentaDomandaUma(dichiarazione.getRichiestaCarburante().getCuaa());
	}
	
	/**
	 * Autorizza la modifica dei dati dei clienti conto terzi di una dichiarazione consumi. 1. controlla che @idCliente
	 * appartenga alla dichiarazione consumi @idDichiarazione 2. controlla che non sia in uno stato di sola lettura 3.
	 * controlla i ruoli edita 4. controllo correlazione utente al mandato del fascicolo
	 * 
	 * @param idDichiarazione
	 * @return
	 * @throws Exception
	 */
	public boolean checkModificaClientiDichiarazioneConsumi(Long idDichiarazione, Long idCliente) throws Exception {
		Optional<ClienteModel> clienteOpt = clienteDao.findById(idCliente);
		if (clienteOpt.isPresent() && !clienteOpt.get().getDichiarazioneConsumi().getId().equals(idDichiarazione)) {
			logger.warn("Il cliente {} non e' associato alla dichiarazione consumi {}", idCliente, idDichiarazione);
			return false;
		}
		return checkModificaDichiarazioneConsumi(idDichiarazione);
	}
	
	/**
	 * Autorizza la modifica dei dati dei consuntivi di una dichiarazione consumi. 1. controlla che @idConsuntivo
	 * appartenga alla dichiarazione consumi @idDichiarazione 2. controlla che non sia in uno stato di sola lettura 3.
	 * controlla i ruoli edita 4. controllo correlazione utente al mandato del fascicolo
	 * 
	 * @param idDichiarazione
	 * @param idConsuntivo
	 * @throws Exception
	 */
	public boolean checkModificaConsuntiviDichiarazioneConsumi(Long idDichiarazione, Long idConsuntivo)
			throws Exception {
		Optional<ConsuntivoConsumiModel> consuntivoOpt = consuntiviConsumiDao.findById(idConsuntivo);
		if (consuntivoOpt.isPresent()
				&& !consuntivoOpt.get().getDichiarazioneConsumi().getId().equals(idDichiarazione)) {
			logger.warn("Il consuntivo {} non e' associato alla dichiarazione consumi {}", idConsuntivo,
					idDichiarazione);
			return false;
		}
		return checkModificaDichiarazioneConsumi(idDichiarazione);
	}
	
	/**
	 * Autorizza la modifica di un prelievo da parte di un distributore. 1. Controlla correlazione id prelievo a id
	 * distributore 2. Controlla stato "Consegnato" prelievo 3. Controlla se l'utente connesso è correlato al
	 * distributore indicato 4. Controlla ruoli
	 * 
	 * @throws Exception
	 */
	public boolean checkModificaPrelievoDistributore(Long idDistributore, Long idPrelievo) {
		Optional<PrelievoModel> prelievoOpt = prelieviDao.findById(idPrelievo);
		
		if (prelievoOpt.isPresent()) {
			var prelievo = prelievoOpt.get();
			if (!idDistributore.equals(prelievo.getDistributore().getId())) {
				logger.warn("Il distributore {} non e' associato al prelievo {}", idDistributore, idPrelievo);
				return false;
			}
			// solo l'istruttore uma può modificare il flag consegnato
			if (!utenteComponent.haRuolo(Ruoli.ISTRUTTORE_UMA) && prelievo.getConsegnato().booleanValue()) {
				logger.warn("Il prelievo {} del distributore {} risulta già consegnato", idPrelievo, idDistributore);
				return false;
			}
		}
		return checkRicercaPrelievoDistributore(idDistributore);
	}
	
	/**
	 * Autorizza la modifica di n prelievi da parte di un distributore. 1. Controlla correlazione id prelievo a id
	 * distributore 2. Controlla stato "Consegnato" prelievo 3. Controlla se l'utente connesso è correlato al
	 * distributore indicato 4. Controlla ruoli
	 * 
	 * @throws Exception
	 */
	public boolean checkModificaPrelieviDistributore(Long idDistributore, List<PrelievoDto> prelievi) {
		return prelievi.stream().allMatch(p -> checkModificaPrelievoDistributore(idDistributore, p.getId()));
	}
	
	/**
	 * Autorizza la modifica/cancellazione di un trasferimento di carburante. 1. Solo l'operatore CAA può effettuare
	 * queste operazioni 3. E' necessario che il mittente non abbia una dichiarazione consumi PROTOCOLLATA 4. E'
	 * necessario che il destinatario non abbia una dichiarazione consumi PROTOCOLLATA
	 * 
	 * @throws Exception
	 */
	public boolean checkModificaTrasferimentoCarburante(Long idTrasferimento) {
		Optional<TrasferimentoCarburanteModel> trasferimentoOpt = trasferimentoCarburanteDao.findById(idTrasferimento);
		if (trasferimentoOpt.isPresent()) {
			RichiestaCarburanteModel richiesta = trasferimentoOpt.get().getRichiestaCarburante();
			
			Optional<DichiarazioneConsumiModel> dichiarazioneOpt = dichiarazioneConsumiDao
					.findByRichiestaCarburante_cuaaAndRichiestaCarburante_campagna(richiesta.getCuaa(),
							richiesta.getCampagna());
			if (dichiarazioneOpt.isPresent()
					&& StatoDichiarazioneConsumi.PROTOCOLLATA.equals(dichiarazioneOpt.get().getStato())) {
				return false;
			}
			Optional<DichiarazioneConsumiModel> dichiarazioneDestinatarioOpt = dichiarazioneConsumiDao
					.findByRichiestaCarburante_cuaaAndRichiestaCarburante_campagna(
							trasferimentoOpt.get().getCuaaDestinatario(), richiesta.getCampagna());
			if (dichiarazioneDestinatarioOpt.isPresent()
					&& StatoDichiarazioneConsumi.PROTOCOLLATA.equals(dichiarazioneDestinatarioOpt.get().getStato())) {
				return false;
			}
		}
		
		return utenteComponent.haRuolo(Ruoli.DOMANDE_UMA_EDITA_ENTE);
	}
	
	/********** CANCELLAZIONE **********/
	
	/**
	 * Autorizza la cancellazione di una richiesta di carburante. 1. Controlla ruoli cancella richiesta 2. Controlla
	 * stato in compilazione 3. controlla che non sia in uno stato di sola lettura 4. controlla i ruoli edita 5.
	 * controllo correlazione utente al mandato del fascicolo
	 * 
	 * @throws Exception
	 */
	public boolean checkDeleteRichiestaCarburante(Long idRichiesta) throws Exception {
		if (utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_CANCELLA_ENTE, Ruoli.DOMANDE_UMA_CANCELLA_UTENTE)) {
			Optional<RichiestaCarburanteModel> richiestaOpt = richiestaCarburanteDao.findById(idRichiesta);
			if (richiestaOpt.isPresent()
					&& StatoRichiestaCarburante.IN_COMPILAZIONE.equals(richiestaOpt.get().getStato())) {
				return true;
			}
		}
		return checkModificaRichiestaCarburante(idRichiesta);
	}
	
	/**
	 * Autorizza la cancellazione di una richiesta di carburante. 1. Controlla ruoli cancella richiesta 2. Controlla
	 * stato in compilazione 3. controlla che non sia in uno stato di sola lettura 4. controlla i ruoli edita 5.
	 * controllo correlazione utente al mandato del fascicolo
	 * 
	 * @throws Exception
	 */
	public boolean checkDeleteDichiarazioneConsumi(Long idConsumi) throws Exception {
		if (utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_CANCELLA_ENTE, Ruoli.DOMANDE_UMA_CANCELLA_UTENTE)) {
			Optional<DichiarazioneConsumiModel> dichiarazioneConsumiOpt = dichiarazioneConsumiDao.findById(idConsumi);
			if (dichiarazioneConsumiOpt.isPresent()
					&& StatoDichiarazioneConsumi.IN_COMPILAZIONE.equals(dichiarazioneConsumiOpt.get().getStato())) {
				return true;
			}
		}
		return checkModificaDichiarazioneConsumi(idConsumi);
	}
	
	/********** ABILITAZIONI ANAGRAFICA **********/
	
	// l'utente connesso è un Titolare/Rappresentate Legale dell'azienda
	private boolean isFascicoloAbilitatoUtente(String cuaa) {
		String codiceFiscaleUtenteConnesso = umaUtenteClient.getUtenteConnesso().getCodiceFiscale();
		SoggettoAualDto soggetto = anagraficaClient.getSoggetto(cuaa);
		boolean isAbilitato = soggetto.getRappresentanteLegale().stream()
				.map(RappresentanteLegaleAualDto::getCodiFisc).filter(codiceFiscaleUtenteConnesso::equals)
				.collect(Collectors.toList()).isEmpty();
		if (!isAbilitato) {
			logger.warn("[ABILITAZIONE FALLITA UTENTE] - Tentato Accesso al fascicolo {} da parte di {}", cuaa,
					umaUtenteClient.getUtenteConnesso().getCodiceFiscale());
		}
		
		return isAbilitato;
	}
	
	// lo sportello del CAA connesso appartiene al fascicolo che si vuole reperire
	private boolean isFascicoloAbilitatoEnte(String cuaa) throws Exception {
		//		Optional<FascicoloAgsDto> fascicoloAgsOpt = Optional.ofNullable(anagraficaClient.getFascicolo(cuaa));
		//		if (fascicoloAgsOpt.isPresent()) {
		//			List<Long> sportelliFascicolo = fascicoloAgsOpt.get().getDetenzioni()
		//					.stream()
		//					.map(DetenzioneAgsDto::getIdentificativoSportello)
		//					.collect(Collectors.toList());
		//
		//			List<Long> sportelliUtente = abilitazioniUtente.getEntiUtente().stream().map(Long::valueOf).collect(Collectors.toList());
		//			List<Long> sportelliAbilitati = ListSupport.intersect(sportelliFascicolo, sportelliUtente);
		//
		//			boolean isAbilitato = !CollectionUtils.isEmpty(sportelliAbilitati);
		//
		//			if (!isAbilitato) {
		//				logger.warn("[ABILITAZIONE FALLITA ENTE] - Tentato Accesso al fascicolo {} da parte di {}" , fascicoloAgsOpt.get().getCuaa() , umaUtenteClient.getUtenteConnesso().getCodiceFiscale());
		//			}
		//			return isAbilitato;
		//		}
		//		logger.warn("[ABILITAZIONE FALLITA ENTE] - Nessun fascicolo trovato per il cuaa {}" , cuaa);
		//		return false;
		return true;
	}
	
	/**
	 * Verifica se l'utente connesso è un istruttore uma
	 */
	public boolean checkIstruttoreUma() {
		return utenteComponent.haRuolo(Ruoli.ISTRUTTORE_UMA);
	}
}
