package it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.tndigitale.a4g.fascicolo.anagrafica.business.event.StartControlloCompletezzaEvent;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.*;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.*;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.specification.ValidazioneFascicoloFilter;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.specification.ValidazioneFascicoloSpecificationBuilder;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.*;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato.DetenzioneNotification;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato.DetenzioneService;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato.MandatoService;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato.RevocaImmediataMandatoService;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.persona.PersonaFisicaOGiuridicaConCaricaService;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.*;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.mapper.FascicoloMapper;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.PersonaFisicaConCaricaDto;
import it.tndigitale.a4g.fascicolo.anagrafica.util.DateTimeConstants;
import it.tndigitale.a4g.framework.event.store.EventStoreService;
import it.tndigitale.a4g.framework.event.store.handler.EventBus;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.FascicoloNonValidabileException;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.StatoFascicoloEnum;
import it.tndigitale.a4g.framework.pagination.builder.PageableBuilder;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.utente.client.model.RappresentaIlModelloPerRappresentareUnUtenteDelSistema;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ValidazioneFascicoloService {
	private static final Logger logger = LoggerFactory.getLogger(ValidazioneFascicoloService.class);

	@Autowired private ModoPagamentoDao modoPagamentoDao;
	@Autowired private IscrizioneSezioneDao iscrizioneSezioneDao;
	@Autowired private PersonaDao personaDao;
	@Autowired private EventStoreService eventStoreService;
	@Autowired private FascicoloDao fascicoloDao;
	@Autowired private MandatoService mandatoService;
	@Autowired private EventBus eventBus;
	@Autowired private Clock clock;
	@Autowired private ZootecniaPrivateClient zootecniaPrivateClient;
	@Autowired private CaricaDao caricaDao;
	@Autowired private PersonaFisicaConCaricaDao personaFisicaConCaricaDao; 
	@Autowired private PersonaGiuridicaConCaricaDao personaGiuridicaConCaricaDao;
	@Autowired private AttivitaAtecoDao attivitaAtecoDao;
	@Autowired private UnitaTecnicoEconomicheDao unitaTecnicoEconomicheDao;
	@Autowired private DestinazioneUsoDao destinazioneUsoDao;
	@Autowired private FascicoloModelValidazioneConverter fascicoloModelValidazioneConverter;
	@Autowired private ObjectMapper mapper;
	@Autowired private StampaComponent stampaComponent;
	@Autowired private AnagraficaProxyClient anagraficaProxyClient;
	@Autowired private PersonaFisicaOGiuridicaConCaricaService personaFisicaOGiuridicaConCaricaService;
	@Autowired private AnagraficaUtenteClient anagraficaUtenteClient;
	@Autowired private MandatoDao mandatoDao;
	@Autowired private ProtocollazioneSchedaValidazioneService protocollazioneSchedaService;
	@Autowired private DetenzioneService detenzioneService;
	@Autowired private DocumentoIdentitaDao documentoIdentitaDao;
	@Autowired private MediatorFascicoloPrivateClient mediatorFascicoloPrivateClient;
	@Autowired private ControlloCompletezzaDao controlloCompletezzaDao;
	@Autowired private UtenteComponent utenteComponent;
	@Autowired private EntityManager em;

	public static final String HEADER_UPN = "upn";
	public static final String HEADER_CF = "codicefiscale";
	
	private static final String NESSUNA_PERSONA_NON_ASSOCIATA_FASCICOLO = "Nessuna persona non associata al fascicolo";
	private static final String NESSUN_DOCUMENTO_IDENTITA_ASSOCIATO_FASCICOLO = "Nessun documento di identità associato al fascicolo";
	private static final String ID_VALIDAZIONE_PROPERTY = "idValidazione";
	
	private FascicoloModel getFascicoloFromCuaa(final String cuaa, int idValidazione) throws NoSuchElementException {
		Optional<FascicoloModel> fascicoloOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, idValidazione);
		return fascicoloOpt.orElseThrow();
	}
	
	private FascicoloModel getFascicoloFromCuaa(final String cuaa) throws NoSuchElementException {
		return getFascicoloFromCuaa(cuaa, 0);
	}
	
	private PersonaFisicaConCaricaModel validaPersonaFisicaConCarica(PersonaFisicaConCaricaModel personaFisicaConCaricaLive, Integer idValidazione) {
		var personaFisicaValidata = new PersonaFisicaConCaricaModel();
		BeanUtils.copyProperties(personaFisicaConCaricaLive, personaFisicaValidata, ID_VALIDAZIONE_PROPERTY);
		personaFisicaValidata.setIdValidazione(idValidazione);
		return personaFisicaConCaricaDao.save(personaFisicaValidata);
	}
	
	private PersonaGiuridicaConCaricaModel validaPersonaGiuridicaConCarica(PersonaGiuridicaConCaricaModel personaGiuridicaConCaricaLive, Integer idValidazione) {
		var personaGiuridicaFisicaValidata = new PersonaGiuridicaConCaricaModel();
		BeanUtils.copyProperties(personaGiuridicaConCaricaLive, personaGiuridicaFisicaValidata, ID_VALIDAZIONE_PROPERTY);
		personaGiuridicaFisicaValidata.setIdValidazione(idValidazione);
		return personaGiuridicaConCaricaDao.save(personaGiuridicaFisicaValidata);
	}
	
	/**
	 * storicizzazione cariche, persone fisiche e giuridiche associate ad una persona giuridica
	 * @param fascicoloLive
	 * @param personaValidata
	 * @param idValidazione
	 * @throws FascicoloNonValidabileException 
	 */
	private void validazioneCaricaEPersone(FascicoloModel fascicoloLive, PersonaModel personaValidata, Integer idValidazione)
			throws FascicoloNonValidabileException {
//		ottenere elenco carica per persona giuridica
		PersonaModel personaLive = fascicoloLive.getPersona();
		if (personaLive == null) {
			throw new FascicoloNonValidabileException(NESSUNA_PERSONA_NON_ASSOCIATA_FASCICOLO);
		}

		List<CaricaModel> caricheLive = null;
		if (personaLive instanceof PersonaFisicaModel) {
			caricheLive = caricaDao.findByPersonaFisicaModel((PersonaFisicaModel)personaLive);
		} else {
			caricheLive = caricaDao.findByPersonaGiuridicaModel((PersonaGiuridicaModel)personaLive);
		}

//		usare personaValidata.idValidazione come idValidazione
		if(caricheLive != null && !caricheLive.isEmpty()) {
			caricheLive.forEach(caricaLive -> {
				var caricaDaValidare = new CaricaModel();

				if (personaLive instanceof PersonaFisicaModel) {
					BeanUtils.copyProperties(caricaLive, caricaDaValidare, ID_VALIDAZIONE_PROPERTY, "personaFisicaConCaricaModel", "personaGiuridicaConCaricaModel", "personaFisicaModel", "firmatario");
					caricaDaValidare.setPersonaFisicaModel((PersonaFisicaModel)personaValidata);
				} else {
					BeanUtils.copyProperties(caricaLive, caricaDaValidare, ID_VALIDAZIONE_PROPERTY, "personaFisicaConCaricaModel", "personaGiuridicaConCaricaModel", "personaGiuridicaModel", "firmatario");
					caricaDaValidare.setPersonaGiuridicaModel((PersonaGiuridicaModel)personaValidata);
				}

				caricaDaValidare.setIdValidazione(idValidazione);
//				validazione persone fisiche con carica
				if(caricaLive.getPersonaFisicaConCaricaModel() != null) {
					PersonaFisicaConCaricaModel personaFisicaConCaricaValidata = validaPersonaFisicaConCarica(caricaLive.getPersonaFisicaConCaricaModel(), idValidazione);
					caricaDaValidare.setPersonaFisicaConCaricaModel(personaFisicaConCaricaValidata);
				}
//				validazione persone giuridiche con carica
				if(caricaLive.getPersonaGiuridicaConCaricaModel() != null) {
					PersonaGiuridicaConCaricaModel personaGiuridicaConCaricaValidata = validaPersonaGiuridicaConCarica(caricaLive.getPersonaGiuridicaConCaricaModel(), idValidazione);
					caricaDaValidare.setPersonaGiuridicaConCaricaModel(personaGiuridicaConCaricaValidata);
				}
//				validazione carica
				caricaDao.save(caricaDaValidare);
			});
		}
	}
	
	private void validazioneAttivitaAteco(FascicoloModel fascicoloLive, PersonaModel personaValidata, Integer idValidazione)
			throws FascicoloNonValidabileException {
		PersonaModel personaLive = fascicoloLive.getPersona();
		if (personaLive == null) {
			throw new FascicoloNonValidabileException(NESSUNA_PERSONA_NON_ASSOCIATA_FASCICOLO);
		}

		List<AttivitaAtecoModel> attivitaAtecoLive = attivitaAtecoDao.findByPersonaModelAndIdValidazione(personaLive, 0);
		if (attivitaAtecoLive != null && !attivitaAtecoLive.isEmpty()) {
			attivitaAtecoLive.forEach(attivitaSingolaLive -> {
				var attivitaAtecoDaValidare = new AttivitaAtecoModel();
				BeanUtils.copyProperties(attivitaSingolaLive, attivitaAtecoDaValidare, ID_VALIDAZIONE_PROPERTY, "personaModel");
				attivitaAtecoDaValidare.setIdValidazione(idValidazione);
				attivitaAtecoDaValidare.setPersonaModel(personaValidata);
				attivitaAtecoDao.save(attivitaAtecoDaValidare);
			});
		}
	}
	
	private List<UnitaTecnicoEconomicheModel> validazioneUnitaTecnicoEconomiche(FascicoloModel fascicoloLive, PersonaModel personaValidata, Integer idValidazione)
			throws FascicoloNonValidabileException {
		PersonaModel personaLive = fascicoloLive.getPersona();
		if (personaLive == null) {
			throw new FascicoloNonValidabileException(NESSUNA_PERSONA_NON_ASSOCIATA_FASCICOLO);
		}

		List<UnitaTecnicoEconomicheModel> uteLive = unitaTecnicoEconomicheDao.findByPersona_CodiceFiscaleAndIdValidazione(personaLive.getCodiceFiscale(), 0);
		List<UnitaTecnicoEconomicheModel> uteValidate = new ArrayList<>();
		if (uteLive != null && !uteLive.isEmpty()) {
			uteLive.forEach(uteSingolaLive -> {
				var uteDaValidare = new UnitaTecnicoEconomicheModel();
				BeanUtils.copyProperties(uteSingolaLive, uteDaValidare, ID_VALIDAZIONE_PROPERTY, "personaModel");
				uteDaValidare.setIdValidazione(idValidazione);
				uteDaValidare.setPersona(personaValidata);
				UnitaTecnicoEconomicheModel  uteSingolaValidata = unitaTecnicoEconomicheDao.save(uteDaValidare);
				validazioneAttivitaAtecoUte(uteSingolaLive, uteSingolaValidata);
				validazioneDestinazioneUso(uteSingolaLive, uteSingolaValidata);
				uteValidate.add(uteSingolaValidata);
			});
		}
		return uteValidate;
	}
	
	private void validazioneAttivitaAtecoUte(UnitaTecnicoEconomicheModel uteLive, UnitaTecnicoEconomicheModel uteValidata) {
		List<AttivitaAtecoModel> attivitaAtecoLive = uteLive.getAttivitaAteco();
		if(attivitaAtecoLive != null && !attivitaAtecoLive.isEmpty()) {
			attivitaAtecoLive.forEach(attivitaLive -> {
				var attivitaAtecoDaValidare = new AttivitaAtecoModel();
				BeanUtils.copyProperties(attivitaLive, attivitaAtecoDaValidare, ID_VALIDAZIONE_PROPERTY, "unitaTecnicoEconomiche");
				attivitaAtecoDaValidare.setIdValidazione(uteValidata.getIdValidazione());
				attivitaAtecoDaValidare.setUnitaTecnicoEconomiche(uteValidata);
				attivitaAtecoDao.save(attivitaAtecoDaValidare);	
			});
		}
	}
	
	private void validazioneDestinazioneUso(UnitaTecnicoEconomicheModel uteLive, UnitaTecnicoEconomicheModel uteValidata) {
		List<DestinazioneUsoModel> destinazioniUsoLive = uteLive.getDestinazioneUso();
		if(destinazioniUsoLive != null && !destinazioniUsoLive.isEmpty()) {
			destinazioniUsoLive.forEach(destinazioneLive -> {
				var destinazioneValidata = new DestinazioneUsoModel();
				BeanUtils.copyProperties(destinazioneLive, destinazioneValidata, ID_VALIDAZIONE_PROPERTY, "unitaTecnicoEconomiche");
				destinazioneValidata.setIdValidazione(uteValidata.getIdValidazione());
				destinazioneValidata.setUnitaTecnicoEconomiche(uteValidata);
				destinazioneUsoDao.save(destinazioneValidata);
			});
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void validazioneFascicolo(
			final FascicoloModel fascicoloLive,
			final String cuaa,
			final Integer idValidazione,
			final String username) throws FascicoloNonValidabileException {
		FascicoloModel fascicoloSnapshot = fascicoloDao
				.findByCuaaAndIdValidazione(cuaa, idValidazione)
				.orElseGet(FascicoloModel::new);
		fascicoloLive.setStato(StatoFascicoloEnum.VALIDATO);
		logger.debug("Creazione nuovo fascicolo nello stato VALIDATO");
		BeanUtils.copyProperties(fascicoloLive, fascicoloSnapshot, ID_VALIDAZIONE_PROPERTY, "detenzioni", "persona", "iscrizioniSezione", "modoPagamentoList");
//		* validazione persona (che comprende persona fisica/giuridica)
		PersonaModel personaValidata = validazionePersona(fascicoloLive, idValidazione);
		personaValidata.ignoreValidazioneCheck();
//		* validazione unità tecnico economiche, attività ateco (collegate alle ute), destinazioni uso
		personaValidata.setUnitaTecnicoEconomiche(validazioneUnitaTecnicoEconomiche(fascicoloLive, personaValidata, idValidazione));
//		* validazione codice ateco associato alla persona (che comprende persona fisica/giuridica)
		validazioneAttivitaAteco(fascicoloLive, personaValidata, idValidazione);
//		* validazione di carica, persona fisica con carica, persona giuridica con carica con idValidazione di personaValidata
		validazioneCaricaEPersone(fascicoloLive, personaValidata, idValidazione);
//		* validazione iscrizione sezione
		personaValidata.setIscrizioniSezione(validazioneIscrizioneSezione(fascicoloLive, personaValidata, idValidazione));
//		* associare a fascicolo validato la persona validata
		fascicoloSnapshot.setPersona(personaValidata);
		fascicoloSnapshot.setIdValidazione(idValidazione);
		fascicoloSnapshot.setDataValidazione(clock.today());
		fascicoloSnapshot.setUtenteValidazione(username);
		logger.debug("VALIDAZIONE FASCICOLO - SALVATAGGIO FASCICOLO DA VALIDARE {} {}", fascicoloSnapshot.getId(), fascicoloSnapshot.getIdValidazione());
		FascicoloModel fascicoloValidato = fascicoloDao.save(fascicoloSnapshot);
		fascicoloValidato.ignoreValidazioneCheck();
//		validazione mandato: all'interno viene gestita la bidirezionalita' sulla nuova entita'
		fascicoloValidato.setDetenzioni(mandatoService.validazioneMandato(fascicoloLive, fascicoloValidato, idValidazione));
//		le revoche immediate non vanno storicizzate, bensi' si deve aggiornare la FK da revoca immediata al Mandato storicizzato (e viceversa)
		mandatoService.validazioneRevocheImmediate(fascicoloLive, idValidazione);
//		validazione modo pagamento: all'interno viene gestita la bidirezionalita' sulla nuova entita'
		fascicoloValidato.setModoPagamentoList(validazioneModoPagamento(fascicoloLive, fascicoloValidato, idValidazione));
//		* validazione documento di identità
		validazioneDocumentoIdentita(fascicoloLive, fascicoloValidato, idValidazione);
		logger.debug("VALIDAZIONE FASCICOLO - SALVATAGGIO FASCICOLO LIVE {} {}", fascicoloLive.getId(), fascicoloLive.getIdValidazione());
		fascicoloDao.save(fascicoloLive);
	}

	@Transactional(rollbackFor = Exception.class)
	public void validazioneFascicoloAutonomo(
			final FascicoloModel fascicoloLive,
			final String cuaa,
			final Integer idValidazione,
			final String username) throws FascicoloNonValidabileException {
		FascicoloModel fascicoloDaValidare = fascicoloDao
				.findByCuaaAndIdValidazione(cuaa, idValidazione)
				.orElseGet(FascicoloModel::new);
		fascicoloLive.setStato(StatoFascicoloEnum.VALIDATO);
		BeanUtils.copyProperties(fascicoloLive, fascicoloDaValidare, ID_VALIDAZIONE_PROPERTY, "detenzioni", "persona", "iscrizioniSezione", "modoPagamentoList");
//		* validazione persona (che comprende persona fisica/giuridica)
		PersonaModel personaValidata = validazionePersona(fascicoloLive, idValidazione);
		personaValidata.ignoreValidazioneCheck();
//		* validazione unità tecnico economiche, attività ateco (collegate alle ute), destinazioni uso
		personaValidata.setUnitaTecnicoEconomiche(validazioneUnitaTecnicoEconomiche(fascicoloLive, personaValidata, idValidazione));
//		* validazione codice ateco associato alla persona (che comprende persona fisica/giuridica)
		validazioneAttivitaAteco(fascicoloLive, personaValidata, idValidazione);
//		* validazione di carica, persona fisica con carica, persona giuridica con carica con idValidazione di personaValidata
		validazioneCaricaEPersone(fascicoloLive, personaValidata, idValidazione);
//		* validazione iscrizione sezione
		personaValidata.setIscrizioniSezione(validazioneIscrizioneSezione(fascicoloLive, personaValidata, idValidazione));
//		* associare a fascicolo validato la persona validata
		fascicoloDaValidare.setPersona(personaValidata);
		fascicoloDaValidare.setIdValidazione(idValidazione);
		fascicoloDaValidare.setDataValidazione(clock.today());
		fascicoloDaValidare.setUtenteValidazione(username);
		FascicoloModel fascicoloValidato = fascicoloDao.save(fascicoloDaValidare);
		fascicoloValidato.ignoreValidazioneCheck();
//		validazione mandato: all'interno viene gestita la bidirezionalita' sulla nuova entita'
		fascicoloValidato.setDetenzioni(mandatoService.validazioneDetenzioneAutonoma(fascicoloLive, fascicoloValidato, idValidazione));
//		validazione modo pagamento: all'interno viene gestita la bidirezionalita' sulla nuova entita'
		fascicoloValidato.setModoPagamentoList(validazioneModoPagamento(fascicoloLive, fascicoloValidato, idValidazione));
//		* validazione documento di identità
		validazioneDocumentoIdentita(fascicoloLive, fascicoloValidato, idValidazione);
		fascicoloDao.save(fascicoloLive);
	}
	
//	/**
//	 * metodo globale di storicizzazione del fascicolo. Questo metodo dovra' gestire la storicizzazione delle
//	 * varie tabelle
//	 * @param cuaa
//	 * @return -1 in caso di errore; 0 se e' andato a buon fine
//	 * @throws IOException
//	 * @throws FascicoloNonValidabileException
//	 */
//	@Transactional
//	public Integer startValidazioneFascicoloAutonomoAsincrona(
//			final StartValidazioneFascicoloEvent event,
//			final String cuaa, final Integer idValidazione) {
////		reperire per cuaa e id_validazione = 0
//		Optional<FascicoloModel> fascicoloLiveOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0);
//		if (fascicoloLiveOpt.isEmpty()) {
//			eventStoreService.triggerRetry(
//					new FascicoloNonValidabileException(StatusMessagesEnum.FASCICOLO_LOCALE_NON_ESISTENTE.name()),
//					event);
//			return -1;
//		}
//		FascicoloModel fascicoloLive = fascicoloLiveOpt.orElseThrow();
//		if (!fascicoloLive.getStato().equals(StatoFascicoloEnum.IN_VALIDAZIONE)) {
//			eventStoreService.triggerRetry(
//					new FascicoloNonValidabileException(fascicoloLive.getStato()),
//					event);
//			return -1;
//		}
//
//		try {
//			validazioneFascicoloAutonomo(fascicoloLive, cuaa, idValidazione, event.getUsername());
//		} catch (Exception exception) {
//			eventStoreService.triggerRetry(exception, event);
//			return -1;
//		}
//		return 0;
//	}
//
//
//	/**
//	 * metodo globale di storicizzazione del fascicolo. Questo metodo dovra' gestire la storicizzazione delle
//	 * varie tabelle
//	 * @param cuaa
//	 * @return -1 in caso di errore; 0 se e' andato a buon fine
//	 * @throws IOException
//	 * @throws FascicoloNonValidabileException
//	 */
//	@Transactional
//	public Integer startValidazioneFascicoloAsincrona(
//			final StartValidazioneFascicoloEvent event,
//			final String cuaa, final Integer idValidazione) {
////		reperire per cuaa e id_validazione = 0
//		Optional<FascicoloModel> fascicoloLiveOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0);
//		if (fascicoloLiveOpt.isEmpty()) {
//			eventStoreService.triggerRetry(
//					new FascicoloNonValidabileException(StatusMessagesEnum.FASCICOLO_LOCALE_NON_ESISTENTE.name()),
//					event);
//			return -1;
//		}
//		FascicoloModel fascicoloLive = fascicoloLiveOpt.orElseThrow();
//		if (!fascicoloLive.getStato().equals(StatoFascicoloEnum.IN_VALIDAZIONE)) {
//			eventStoreService.triggerRetry(
//					new FascicoloNonValidabileException(fascicoloLive.getStato()),
//					event);
//			return -1;
//		}
//
//		/*
//		String urlValidazioneZootecnia = String.format("%s/%s/%d/validazione", urlZootecnia, cuaa, idValidazione); da aggiornare
//
//		private boolean isADDAuthentication(String username) {
//			return username.indexOf("@") > 0;
//		}
//
//		private Pair<String, String> buildUsernameHeader(Supplier<String> username) {
//				String currentUser = username.get();
//				if(currentUser == null || currentUser.trim().length() == 0) {
//					return null;
//				}
//				if (isADDAuthentication(currentUser)) {
//					return Pair.of(HEADER_UPN, currentUser);
//				} else {
//					return Pair.of(HEADER_CF, currentUser);
//				}
//		}
//
//		private HttpEntity<String> buildWith(Supplier<String> username) {
//			HttpHeaders headers = new HttpHeaders();
//			Pair<String, String> headerUsername = buildUsernameHeader(username);
//			if(headerUsername == null){
//				return new HttpEntity<>(headers);
//			} else {
//				headers.set(headerUsername.getFirst(), headerUsername.getSecond());
//				return new HttpEntity<>(headers);
//			}
//		}
//
//		private Flux<Void> getFluxValidazione(final String url) {
//			return webClientBuilderTn.buildWith(utenteCorrente::utenza)
//				.put()
//				.uri(url)
//				.retrieve()
//				.bodyToFlux(Void.class);
//		}
//
//		Flux.merge(getFluxValidazione(urlValidazioneZootecnia))
//			.doOnError(exception -> eventStoreService.triggerRetry(exception, event))
//			.all(res -> true)
//			.subscribe(res -> {
//				try {
//					validazioneFascicolo(fascicoloLive, cuaa, idValidazione, event.getUsername());
//				} catch (FascicoloNonValidabileException exception) {
//					eventStoreService.triggerRetry(exception, event);
//				}
//			});
//		*/
//		try {
//			ResponseEntity<Void> startValidazioneFascicolo = zootecniaPrivateClient.startValidazioneFascicolo(cuaa, idValidazione);
//			if (!startValidazioneFascicolo.getStatusCode().equals(HttpStatus.OK)) {
//				eventStoreService.triggerRetry(
//	    			new FascicoloNonValidabileException("Errore in validazione zootecnia"), event);
//				return -1;
//			}
//			validazioneFascicolo(fascicoloLive, cuaa, idValidazione, event.getUsername());
//		} catch (Exception exception) {
//			eventStoreService.triggerRetry(exception, event);
//			return -1;
//		}
//		return 0;
//	}

	/**
	 * storicizzazione modo pagamento
	 * @param fascicoloDaValidare
	 * @param fascicoloValidato
	 * @param idValidazione
	 */
	private List<ModoPagamentoModel> validazioneModoPagamento(FascicoloModel fascicoloDaValidare, FascicoloModel fascicoloValidato, Integer idValidazione) {
		List<ModoPagamentoModel> result = new ArrayList<>();
		if (fascicoloDaValidare.getModoPagamentoList() != null && !fascicoloDaValidare.getModoPagamentoList().isEmpty()) {
			fascicoloDaValidare.getModoPagamentoList().forEach( modoPagamento -> {
				var modoPagamentoModel = new ModoPagamentoModel();
				BeanUtils.copyProperties(modoPagamento, modoPagamentoModel, ID_VALIDAZIONE_PROPERTY, "fascicolo");
				modoPagamentoModel.setFascicolo(fascicoloValidato);
				modoPagamentoModel.setIdValidazione(idValidazione);
				result.add(modoPagamentoDao.save(modoPagamentoModel));
			});
		}
		return result;
	}
	
	/**
	 * storicizzazione documento di identita'
	 * @param fascicoloLive
	 * @param fascicoloValidato
	 * @param idValidazione
	 */
	private void validazioneDocumentoIdentita(FascicoloModel fascicoloLive, FascicoloModel fascicoloValidato, Integer idValidazione)
		throws FascicoloNonValidabileException 
	{
		PersonaModel personaLive = fascicoloLive.getPersona();
		if(personaLive == null) {
			throw new FascicoloNonValidabileException(NESSUNA_PERSONA_NON_ASSOCIATA_FASCICOLO);
		}
		if (personaLive instanceof PersonaGiuridicaModel) {
			var documentoIdentitaValidato = new DocumentoIdentitaModel();
			DocumentoIdentitaModel documentoIdentitaDaValidare = fascicoloLive.getDocumentoIdentita();
			if (documentoIdentitaDaValidare == null) {
				throw new FascicoloNonValidabileException(NESSUN_DOCUMENTO_IDENTITA_ASSOCIATO_FASCICOLO);
			}
			BeanUtils.copyProperties(documentoIdentitaDaValidare, documentoIdentitaValidato, ID_VALIDAZIONE_PROPERTY, "fascicolo");
			documentoIdentitaValidato.setFascicolo(fascicoloValidato);
			documentoIdentitaValidato.setIdValidazione(idValidazione);
			documentoIdentitaDao.save(documentoIdentitaValidato);
		}
	}

	/**
	 * storicizzazione persona fisica/giuridica
	 * @param fascicoloLive
	 * @param idValidazione
	 * @throws FascicoloNonValidabileException 
	 */
	private PersonaModel validazionePersona(FascicoloModel fascicoloLive, Integer idValidazione) throws FascicoloNonValidabileException {
		PersonaModel personaLive = fascicoloLive.getPersona();
		if(personaLive == null) {
			throw new FascicoloNonValidabileException(NESSUNA_PERSONA_NON_ASSOCIATA_FASCICOLO);
		}
		PersonaModel personaValidata = new PersonaFisicaModel();
		if(personaLive instanceof PersonaGiuridicaModel) {
			personaValidata = new PersonaGiuridicaModel();
		}
		
		BeanUtils.copyProperties(personaLive, personaValidata, ID_VALIDAZIONE_PROPERTY);
		personaValidata.setIdValidazione(idValidazione);
		return personaDao.save(personaValidata);
	}
	
	/**
	 * storicizzazione persona
	 * @param fascicoloLive
	 * @param personaValidata
	 * @param idValidazione
	 * @throws FascicoloNonValidabileException 
	 */
	private List<IscrizioneSezioneModel> validazioneIscrizioneSezione(FascicoloModel fascicoloLive, PersonaModel personaValidata, Integer idValidazione)
			throws FascicoloNonValidabileException {
		PersonaModel personaLive = fascicoloLive.getPersona();
		if (personaLive == null) {
			throw new FascicoloNonValidabileException(NESSUNA_PERSONA_NON_ASSOCIATA_FASCICOLO);
		}
		
		List<IscrizioneSezioneModel> iscrizioniLive = personaLive.getIscrizioniSezione();
		List<IscrizioneSezioneModel> iscrizioniValidate = new ArrayList<>();
		if(iscrizioniLive != null && !iscrizioniLive.isEmpty()) {
			iscrizioniLive.forEach(iscrizioneLive -> {
				var iscrizioneValidata = new IscrizioneSezioneModel();
				BeanUtils.copyProperties(iscrizioneLive, iscrizioneValidata, ID_VALIDAZIONE_PROPERTY, "persona");
//				idValidazione di iscrizione va ricavato solo una volta per l'intera lista; viene associato a idValidazione di personaValidata 
				iscrizioneValidata.setIdValidazione(idValidazione);
				iscrizioneValidata.setPersona(personaValidata);
				iscrizioniValidate.add(iscrizioneSezioneDao.save(iscrizioneValidata));
			});
		}
		return iscrizioniValidate;
	}
	
	@Transactional
	public RisultatiPaginati<ValidazioneFascicoloDto> elencoValidazioni(
			final Integer annoValidazione,
			final String cuaa, final Paginazione paginazione,
			final Ordinamento ordinamento) {
		FascicoloModel fascicoloModel = getFascicoloFromCuaa(cuaa);
		var criteri = new ValidazioneFascicoloFilter(
				fascicoloModel.getId(),
				StatoFascicoloEnum.VALIDATO,
				annoValidazione);
		Page<FascicoloModel> page = fascicoloDao.findAll(
				ValidazioneFascicoloSpecificationBuilder.getFilter(criteri),
				PageableBuilder.build().from(paginazione, ordinamento));
		List<ValidazioneFascicoloDto> validazioni = page.stream()
				.map(e -> fascicoloModelValidazioneConverter.convert(e))
				.collect(Collectors.toList());
		return RisultatiPaginati.of(validazioni, page.getTotalElements());
	}

	private ReportValidazioneFascicoloMandatoDto getReportValidazioneFascicoloMandato(FascicoloModel fascicoloModel) throws FascicoloInvalidConditionException {
		var mandatoReport = new ReportValidazioneFascicoloMandatoDto();
		for (MandatoModel mandato : mandatoDao.findByFascicolo(fascicoloModel)) {
			if (isInInterval(mandato.getDataInizio(), mandato.getDataFine(), clock.today())) {
				mandatoReport.setDataSottoscrizione(mandato.getDataInizio().format(
						DateTimeFormatter.ofPattern(DateTimeConstants.DATE_PATTERN)));
				mandatoReport.setDenominazioneSportello(mandato.getSportello().getDenominazione());
				mandatoReport.setDenominazioneCaa(mandato.getSportello().getCentroAssistenzaAgricola().getDenominazione());
				break;
			}
		}
		return mandatoReport;
	}

	private EsitoControlloDto isFascicoloCompletoStatoControlliInCorso(final FascicoloModel fascicoloModel) {
		EsitoControlloDto esitoControlloDto = new EsitoControlloDto();
		esitoControlloDto.setEsito(fascicoloModel.getStato().equals(StatoFascicoloEnum.CONTROLLI_IN_CORSO) ? 0 : -3);
		return esitoControlloDto;
	}
	
	private EsitoControlloDto isFascicoloCompletoStatoInAggiornamento(final FascicoloModel fascicoloModel) {
		EsitoControlloDto esitoControlloDto = new EsitoControlloDto();
		esitoControlloDto.setEsito(fascicoloModel.getStato().equals(StatoFascicoloEnum.IN_AGGIORNAMENTO) ? 0 : -3);
		return esitoControlloDto;
	}
	
	private EsitoControlloDto isFascicoloCompletoIsNotRevocaIncorso(final FascicoloModel fascicoloModel) {
		EsitoControlloDto esitoControlloDto = new EsitoControlloDto();
		for (MandatoModel mandato : mandatoDao.findByFascicolo(fascicoloModel)) {
			if (isInInterval(mandato.getDataInizio(), mandato.getDataFine(), clock.today())
					&& RevocaImmediataMandatoService.isRevocaInCorso(mandato)) {
				esitoControlloDto.setEsito(-3);
				return esitoControlloDto;
			}
		}
		esitoControlloDto.setEsito(0);
		return esitoControlloDto;
	}
	
	private EsitoControlloDto isFascicoloCompletoAggiornamentoFontiEsterne(final FascicoloModel fascicoloModel) {
		LocalDateTime currentDateMinus24Hours = new Clock().now().minusHours(24);
		LocalDateTime dtAggiornamentoFontiEsterne = fascicoloModel.getDtAggiornamentoFontiEsterne();

		EsitoControlloDto esitoControlloDto = new EsitoControlloDto();
		esitoControlloDto.setEsito((dtAggiornamentoFontiEsterne != null && !dtAggiornamentoFontiEsterne.isBefore(currentDateMinus24Hours)) ? 0 : -3);
		return esitoControlloDto;
	}
	
	private EsitoControlloDto isFascicoloCompletoModalitaPagamentoPresente(final FascicoloModel fascicoloModel) {
		EsitoControlloDto esitoControlloDto = new EsitoControlloDto();
		esitoControlloDto.setEsito((fascicoloModel.getModoPagamentoList() != null && !fascicoloModel.getModoPagamentoList().isEmpty()) ? 0 : -3);
		return esitoControlloDto;
	}

	@Transactional
	public void startControlloCompletezzaFascicoloAsincrono(final String cuaa, final Integer idValidazione) {
		try {
			//		controllo se esiste localmente un fascicolo associato al cuaa, altrimenti si lancia eccezione e si esce
			FascicoloModel fascicoloLive = getFascicoloFromCuaa(cuaa);
//			check dell'esistenza di un controllo di completezza esistente. In tal caso il record viene eliminato
			var controlloCompletezzaList = controlloCompletezzaDao.findByFascicolo_CuaaAndFascicolo_IdValidazione(cuaa, 0);
			if (controlloCompletezzaList.size() > 0) {
				controlloCompletezzaDao.deleteInBatch(controlloCompletezzaList);
			}

			//		salvare nel db i dati del controllo di completezza tranne l'esito che verrà gestito dal listener
			for (ControlliFascicoloAnagraficaCompletoEnum controllo : ControlliFascicoloAnagraficaCompletoEnum.values()) {
				var controlloCompletezzaModel = new ControlloCompletezzaModel();
				controlloCompletezzaModel.setFascicolo(fascicoloLive);
				controlloCompletezzaModel.setTipoControllo(controllo.name());
				controlloCompletezzaModel.setUtente(utenteComponent.username());
				controlloCompletezzaModel.setDataEsecuzione(LocalDateTime.now());
				controlloCompletezzaDao.save(controlloCompletezzaModel);
			}
			var event = new StartControlloCompletezzaEvent(cuaa, idValidazione);
			eventBus.publishEvent(event);
		} catch ( NoSuchElementException | EntityNotFoundException e) {
			logger.warn("cuaa non censito: {}", cuaa);
		}
	}
	@Transactional
	public Map<ControlliFascicoloAnagraficaCompletoEnum, EsitoControlloDto> getControlloCompletezzaFascicolo(final String cuaa)
			throws NoSuchElementException {
		return getControlloCompletezzaFascicolo(getFascicoloFromCuaa(cuaa));
	}

	private static boolean contains(String test) {
		if (StringUtils.isBlank(test)) {
			return false;
		}
		for (ControlliFascicoloAnagraficaCompletoEnum c : ControlliFascicoloAnagraficaCompletoEnum.values()) {
			if (c.name().equals(test)) {
				return true;
			}
		}
		return false;
	}

	@Transactional
	public List<FascicoloDto> getElencoFascicoliInStatoControlliInCorso() {
		List<FascicoloDto> fascicoloDtoList = new ArrayList<>();
//		reperire fascicoli in stato StatoFascicoloEnum.CONTROLLI_IN_CORSO
		List<FascicoloModel> fascicoloModelList = fascicoloDao.findByStato(StatoFascicoloEnum.CONTROLLI_IN_CORSO);
		if (fascicoloModelList != null && !fascicoloModelList.isEmpty()) {
			fascicoloModelList.forEach(fascicoloModel -> {
				fascicoloDtoList.add(FascicoloMapper.fromFascicolo(fascicoloModel));
			});
		}
		return fascicoloDtoList;
	}

	@Transactional
	public Map<ControlliFascicoloAnagraficaCompletoEnum, EsitoControlloDto> queryControlloCompletezzaFascicolo(final String cuaa)
			throws NoSuchElementException {
		var controlliList = controlloCompletezzaDao.findByFascicolo_CuaaAndFascicolo_IdValidazione(cuaa, 0);
		var resultMap = new HashMap<ControlliFascicoloAnagraficaCompletoEnum, EsitoControlloDto>();
		if (controlliList == null || controlliList.isEmpty()) {
			return resultMap;
		}
		var controlliFilteredList = controlliList.stream().filter(c -> contains(c.getTipoControllo())).collect(Collectors.toList());
		for (ControlloCompletezzaModel controlloCompletezzaModel: controlliFilteredList) {
			var esitoDto = new EsitoControlloDto();
			esitoDto.setEsito(controlloCompletezzaModel.getEsito());
			if (controlloCompletezzaModel.getIdControllo() != null) {
				esitoDto.setIdControllo(Long.valueOf(controlloCompletezzaModel.getIdControllo()));
			}
			resultMap.put(ControlliFascicoloAnagraficaCompletoEnum.valueOf(controlloCompletezzaModel.getTipoControllo()), esitoDto);
		}
		return resultMap;
	}
	
	public Map<ControlliFascicoloAnagraficaCompletoEnum, EsitoControlloDto> getControlloCompletezzaFascicoloDetenzioneAutonoma(final String cuaa)
			throws NoSuchElementException {
		return getControlloCompletezzaFascicoloDetenzioneAutonoma(getFascicoloFromCuaa(cuaa));
	}
	
	private Map<ControlliFascicoloAnagraficaCompletoEnum, EsitoControlloDto> getControlloCompletezzaFascicoloDetenzioneAutonoma(
			final FascicoloModel fascicoloModel) {
		var retList = new EnumMap<ControlliFascicoloAnagraficaCompletoEnum, EsitoControlloDto>(ControlliFascicoloAnagraficaCompletoEnum.class);
//		retList.put(ControlliFascicoloAnagraficaCompletoEnum.IS_IN_AGGIORNAMENTO,
//				isFascicoloCompletoStatoInAggiornamento(fascicoloModel));
		retList.put(ControlliFascicoloAnagraficaCompletoEnum.IS_AGGIORNAMENTO_FONTI_ESTERNE_ANAGRAFICA,
				isFascicoloCompletoAggiornamentoFontiEsterne(fascicoloModel));
		retList.put(ControlliFascicoloAnagraficaCompletoEnum.IS_MODALITA_PAGAMENTO_PRESENTE,
				isFascicoloCompletoModalitaPagamentoPresente(fascicoloModel));
		if (fascicoloModel.getPersona() instanceof PersonaGiuridicaModel) {
            retList.put(ControlliFascicoloAnagraficaCompletoEnum.IS_DOCUMENTO_IDENTITA_PRESENTE,
            		isFascicoloCompletoDocumentoIdentitaPresente(fascicoloModel));
        }
		return retList;
	}
	
	private EsitoControlloDto isFascicoloCompletoDocumentoIdentitaPresente(final FascicoloModel fascicoloModel) {
		EsitoControlloDto esitoControlloDto = new EsitoControlloDto();
		esitoControlloDto.setEsito((fascicoloModel.getDocumentoIdentita() != null 
				&& StringUtils.isNotBlank(fascicoloModel.getDocumentoIdentita().getCodiceFiscale())
				&& StringUtils.isNotBlank(fascicoloModel.getDocumentoIdentita().getNumero())
				&& StringUtils.isNotBlank(fascicoloModel.getDocumentoIdentita().getTipologia())
				&& fascicoloModel.getDocumentoIdentita().getDataRilascio() != null
				&& fascicoloModel.getDocumentoIdentita().getDataScadenza() != null 
				// G. De Vincentiis
				&& fascicoloModel.getDocumentoIdentita().getDataScadenza().isAfter(clock.today())
				&& fascicoloModel.getDocumentoIdentita().getDocumento() != null) ? 0 : -3);
		return esitoControlloDto;
	}
	
	private Map<ControlliFascicoloAnagraficaCompletoEnum, EsitoControlloDto> getControlloCompletezzaFascicolo(
			final FascicoloModel fascicoloModel) {
		var retList = new EnumMap<ControlliFascicoloAnagraficaCompletoEnum, EsitoControlloDto>(ControlliFascicoloAnagraficaCompletoEnum.class);
		retList.put(ControlliFascicoloAnagraficaCompletoEnum.IS_IN_AGGIORNAMENTO,
				isFascicoloCompletoStatoInAggiornamento(fascicoloModel));
		retList.put(ControlliFascicoloAnagraficaCompletoEnum.IS_CONTROLLI_IN_CORSO,
				isFascicoloCompletoStatoControlliInCorso(fascicoloModel));
		retList.put(ControlliFascicoloAnagraficaCompletoEnum.IS_NOT_REVOCA_IN_CORSO,
				isFascicoloCompletoIsNotRevocaIncorso(fascicoloModel));
		retList.put(ControlliFascicoloAnagraficaCompletoEnum.IS_AGGIORNAMENTO_FONTI_ESTERNE_ANAGRAFICA,
				isFascicoloCompletoAggiornamentoFontiEsterne(fascicoloModel));
		retList.put(ControlliFascicoloAnagraficaCompletoEnum.IS_MODALITA_PAGAMENTO_PRESENTE,
				isFascicoloCompletoModalitaPagamentoPresente(fascicoloModel));
		retList.put(ControlliFascicoloAnagraficaCompletoEnum.IS_DOCUMENTO_IDENTITA_PRESENTE,
					isFascicoloCompletoDocumentoIdentitaPresente(fascicoloModel));
		return retList;
	}
	
	public ReportValidazioneDto getReportValidazione(final String cuaa) throws FascicoloInvalidConditionException {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		var report = new ReportValidazioneDto();
		report.setData(clock.today().format(formatter));
		var aziendaReport = new ReportValidazioneFascicoloAziendaDto();

		var fascicoloModel = getFascicoloFromCuaa(cuaa);
		List<ControlloCompletezzaModel> controlloCompletezzaModel = controlloCompletezzaDao.findByFascicolo_CuaaAndFascicolo_IdValidazione(cuaa, 0);

		report.setIdentificativoScheda(fascicoloModel.getIdSchedaValidazione() != null ? fascicoloModel.getIdSchedaValidazione().toString() : "BOZZA");
		
		aziendaReport.setCuaa(cuaa);
		aziendaReport.setDenominazioneAzienda(fascicoloModel.getDenominazione());
		aziendaReport.setDataCostituzione(fascicoloModel.getDataApertura().format(formatter));
		aziendaReport.setOrganismoPagatore(fascicoloModel.getOrganismoPagatore().getDenominazione());
		if (fascicoloModel.getDtAggiornamentoFontiEsterne() != null) {
			aziendaReport.setDtAggiornamentoFontiEsterne(fascicoloModel.getDtAggiornamentoFontiEsterne().format(formatter));
		}
		if (controlloCompletezzaModel != null && !controlloCompletezzaModel.isEmpty()) {
			aziendaReport.setDtEsecuzioneControlliCompletezza(controlloCompletezzaModel.get(0).getDataEsecuzione().format(formatter));
		}
		
		PersonaModel persona = fascicoloModel.getPersona();
		String codiceFiscale = persona.getCodiceFiscale();
		if (persona instanceof PersonaGiuridicaModel) {
			PersonaGiuridicaModel pg = (PersonaGiuridicaModel)persona;
			ReportValidazioneFascicoloPersonaGiuridicaDto soggettoReport = new ReportValidazioneFascicoloPersonaGiuridicaDto();
			soggettoReport.setCodiceFiscale(codiceFiscale);
			soggettoReport.setPartitaIva(pg.getPartitaIVA());
			soggettoReport.setRagioneSociale(pg.getDenominazione());
			if (pg.getSedeLegale() != null) {
				StringBuilder sedeLegale = new StringBuilder();
				sedeLegale.append(pg.getSedeLegale().getIndirizzo().getDescrizioneEstesa());
				if (!StringUtils.isBlank(pg.getSedeLegale().getIndirizzo().getComune())) {
					sedeLegale.append(" - ").append(pg.getSedeLegale().getIndirizzo().getComune());
				}

				if (!StringUtils.isBlank(pg.getSedeLegale().getIndirizzo().getCap())) {
					sedeLegale.append(" - ").append(pg.getSedeLegale().getIndirizzo().getCap());
				}

				if (!StringUtils.isBlank(pg.getSedeLegale().getIndirizzo().getProvincia())) {
					sedeLegale.append(" - ").append(pg.getSedeLegale().getIndirizzo().getProvincia());
				}

				soggettoReport.setSedeLegale(sedeLegale.toString());

				if (pg.getSedeLegale().getIscrizioneRegistroImprese() != null
						&& !pg.getSedeLegale().getIscrizioneRegistroImprese().getCessata()) {
					ReportValidazioneFascicoloCciaaDto cciaaDto = new ReportValidazioneFascicoloCciaaDto();
					cciaaDto.setDataIscrizione(pg.getSedeLegale().getIscrizioneRegistroImprese().getDataIscrizione().format(formatter));
					cciaaDto.setCodiceRea("" + pg.getSedeLegale().getIscrizioneRegistroImprese().getNumeroRepertorioEconomicoAmministrativo());
					cciaaDto.setProvinciaRea(pg.getSedeLegale().getIscrizioneRegistroImprese().getProvinciaCameraCommercio());
					report.setCciaa(cciaaDto);
				}
			}
			report.setPersonaGiuridica(soggettoReport);
//			PersonaFisicaConCaricaDto firmatarioDto = personaFisicaOGiuridicaConCaricaService.getFirmatario(codiceFiscale);
//			aziendaReport.setNomeFirmatario(firmatarioDto.getNome() + " " + firmatarioDto.getCognome());
		}
		else if (persona instanceof PersonaFisicaModel) {
			PersonaFisicaModel pf = (PersonaFisicaModel)persona;
			ReportValidazioneFascicoloPersonaFisicaDto soggettoReport = new ReportValidazioneFascicoloPersonaFisicaDto();
			soggettoReport.setCodiceFiscale(codiceFiscale);
			soggettoReport.setNome(pf.getNome());
			soggettoReport.setCognome(pf.getCognome());
			soggettoReport.setDataNascita(pf.getDataNascita().format(formatter));
			if (pf.getImpresaIndividuale() != null) {
				soggettoReport.setPartitaIva(pf.getImpresaIndividuale().getPartitaIVA());
				soggettoReport.setDenominazione(pf.getImpresaIndividuale().getDenominazione());

				StringBuilder domicilioFiscale = new StringBuilder();
				if (pf.getDomicilioFiscale() != null) {
					domicilioFiscale.append(pf.getDomicilioFiscale().getDescrizioneEstesa());
					if (!StringUtils.isBlank(pf.getDomicilioFiscale().getComune())) {
						domicilioFiscale.append(" - ").append(pf.getDomicilioFiscale().getComune());
					}

					if (!StringUtils.isBlank(pf.getDomicilioFiscale().getCap())) {
						domicilioFiscale.append(" - ").append(pf.getDomicilioFiscale().getCap());
					}

					if (!StringUtils.isBlank(pf.getDomicilioFiscale().getProvincia())) {
						domicilioFiscale.append(" - ").append(pf.getDomicilioFiscale().getProvincia());
					}
				}

				soggettoReport.setDomicilioFiscale(domicilioFiscale.toString());
				if (pf.getImpresaIndividuale().getSedeLegale() != null) {
					if (pf.getImpresaIndividuale().getSedeLegale().getIndirizzo() != null) {
						StringBuilder sedeLegale = new StringBuilder();
						sedeLegale.append(pf.getImpresaIndividuale().getSedeLegale().getIndirizzo().getDescrizioneEstesa());
						if (!StringUtils.isBlank(pf.getImpresaIndividuale().getSedeLegale().getIndirizzo().getComune())) {
							sedeLegale.append(" - ").append(pf.getImpresaIndividuale().getSedeLegale().getIndirizzo().getComune());
						}

						if (!StringUtils.isBlank(pf.getImpresaIndividuale().getSedeLegale().getIndirizzo().getCap())) {
							sedeLegale.append(" - ").append(pf.getImpresaIndividuale().getSedeLegale().getIndirizzo().getCap());
						}

						if (!StringUtils.isBlank(pf.getImpresaIndividuale().getSedeLegale().getIndirizzo().getProvincia())) {
							sedeLegale.append(" - ").append(pf.getImpresaIndividuale().getSedeLegale().getIndirizzo().getProvincia());
						}
						soggettoReport.setSedeLegale(sedeLegale.toString());
					}
//					aziendaReport.setNomeFirmatario(pf.getNome() + " " + pf.getCognome());
					if (pf.getImpresaIndividuale().getSedeLegale().getIscrizioneRegistroImprese() != null 
							&& !pf.getImpresaIndividuale().getSedeLegale().getIscrizioneRegistroImprese().getCessata()) {
						ReportValidazioneFascicoloCciaaDto cciaaDto = new ReportValidazioneFascicoloCciaaDto();
						cciaaDto.setDataIscrizione(pf.getImpresaIndividuale().getSedeLegale().getIscrizioneRegistroImprese().getDataIscrizione().format(formatter));
						cciaaDto.setCodiceRea("" + pf.getImpresaIndividuale().getSedeLegale().getIscrizioneRegistroImprese().getNumeroRepertorioEconomicoAmministrativo());
						cciaaDto.setProvinciaRea(pf.getImpresaIndividuale().getSedeLegale().getIscrizioneRegistroImprese().getProvinciaCameraCommercio());
						report.setCciaa(cciaaDto);
					}
				}
			}
			report.setPersonaFisica(soggettoReport);
		}
		report.setAzienda(aziendaReport);
		
    	// Tipo detenzione
		ReportValidazioneFascicoloDetenzioneDto detenzioneDto = new ReportValidazioneFascicoloDetenzioneDto();
    	Optional<DetenzioneModel> detenzioneCorrenteOpt = detenzioneService.getDetenzioneCorrente(fascicoloModel);
    	if (!detenzioneCorrenteOpt.isPresent()) {
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, DetenzioneNotification.DETENZIONE_MANCANTE.name());
    	}
    	DetenzioneModel detenzioneCorrente = detenzioneCorrenteOpt.get();
    	if (detenzioneCorrente instanceof MandatoModel) {
    		var mandatoReport = getReportValidazioneFascicoloMandato(fascicoloModel);
			detenzioneDto.setTipoDetenzione(TipoDetenzioneEnum.MANDATO);
			detenzioneDto.setMandato(mandatoReport);
		}
		else {
			detenzioneDto.setTipoDetenzione(TipoDetenzioneEnum.DETENZIONE_IN_PROPRIO);
		}
		report.setDetenzione(detenzioneDto);
		
		// Modalità di pagamento
		List<ReportValidazioneFascicoloModPagamentoDto> modPagamentoDtoList = new ArrayList<ReportValidazioneFascicoloModPagamentoDto>();
		List<ModoPagamentoModel> modoPagamentoList = modoPagamentoDao.findByFascicolo_CuaaAndIdValidazione(cuaa, 0);
		for(ModoPagamentoModel mp : modoPagamentoList) {
			ReportValidazioneFascicoloModPagamentoDto mpDto = new ReportValidazioneFascicoloModPagamentoDto();
			mpDto.setIban(mp.getIban());
			mpDto.setDenominazioneBanca(mp.getDenominazioneIstituto());
			mpDto.setFiliale(mp.getDenominazioneFiliale());
			modPagamentoDtoList.add(mpDto);
		}
		report.setModPagamentoList(modPagamentoDtoList);
		
		// Primo firmatario
		ReportValidazioneFascicoloPrimoFirmatarioDto firmatarioReport = new ReportValidazioneFascicoloPrimoFirmatarioDto();
		PersonaFisicaConCaricaDto personaFisicaConCarica = personaFisicaOGiuridicaConCaricaService.getFirmatario(cuaa);
		firmatarioReport.setCodiceFiscale(personaFisicaConCarica.getCodiceFiscale());
		firmatarioReport.setCognome(personaFisicaConCarica.getCognome());
		firmatarioReport.setNome(personaFisicaConCarica.getNome());
		report.setFirmatario(firmatarioReport);
		
		return report;
    }

	// questo metodo verrà inserito in Clock, da rimuovere quando framework è aggiornato
	public static boolean isInInterval(final LocalDate startDate, final LocalDate endDate, final LocalDate targetDate) {
    	return ((startDate.isEqual(targetDate) || startDate.isBefore(targetDate))
				&& (endDate == null || endDate.isAfter(targetDate)));
    }

	private void verificaFirmaSingolaFile(String codiceFiscale, byte[] schedaValidazioneBytes,
            File schedaValidazioneFile) throws IOException, FirmaDocumentoException {
        FileUtils.writeByteArrayToFile(schedaValidazioneFile, schedaValidazioneBytes);
        try {
            anagraficaProxyClient.verificaFirmaSingola(schedaValidazioneFile, codiceFiscale);
        } catch (HttpServerErrorException hsee) {
            Files.delete(schedaValidazioneFile.toPath());
            throw hsee;
        } catch (HttpClientErrorException hcee) {
            Files.delete(schedaValidazioneFile.toPath());
            throw new FirmaDocumentoException("Risposta da anagrafica proxy: " + hcee.getMessage());
        }
    }
    
    private void verificaFirmaMultiplaFile(String codiceFiscaleFirmatario, String codiceFiscaleUtenteConnesso, byte[] schedaValidazioneBytes) throws FirmaDocumentoException, IOException {
    	var schedaValidazioneFile = File.createTempFile("schedaValidazioneFirmataFirmatario", ".pdf");
        try {
            FileUtils.writeByteArrayToFile(schedaValidazioneFile, schedaValidazioneBytes);
            List<String> codiceFiscaleInputList = Arrays.asList(codiceFiscaleFirmatario, codiceFiscaleUtenteConnesso);
        	anagraficaProxyClient.verificaFirmaMultipla(schedaValidazioneFile, codiceFiscaleInputList);
        } catch (HttpServerErrorException hsee) {
            throw hsee;
        } catch (HttpClientErrorException hcee) {
            throw new FirmaDocumentoException("Risposta da anagrafica proxy: " + hcee.getMessage());
        } finally {
        	if (schedaValidazioneFile != null) {
        		Files.delete(schedaValidazioneFile.toPath());	
        	}
        }
    }

	public void salvaSchedaValidazioneFirmata(String cuaa, MultipartFile schedaValidazioneFirmata)
    		throws FirmaDocumentoException, IOException {
		FascicoloModel fascicoloModel = getFascicoloFromCuaa(cuaa);

    	// controllo che sia firmata dall'utente collegato
        RappresentaIlModelloPerRappresentareUnUtenteDelSistema utenteConnesso = anagraficaUtenteClient.getUtenteConnesso();
        String codiceFiscaleUtenteConnesso = utenteConnesso.getCodiceFiscale();
        byte[] schedaValidazioneBytes = schedaValidazioneFirmata.getBytes();
        var schedaValidazioneFile = File.createTempFile("schedaValidazioneFirmata", ".pdf");
        verificaFirmaSingolaFile(codiceFiscaleUtenteConnesso, schedaValidazioneBytes, schedaValidazioneFile);
        Files.delete(schedaValidazioneFile.toPath());
        
    	fascicoloModel.setSchedaValidazioneFirmata(schedaValidazioneBytes);
		fascicoloModel.setStato(StatoFascicoloEnum.FIRMATO_CAA);
		fascicoloDao.save(fascicoloModel);
    }
	
	public void setSchedaValidazione(String cuaa, MultipartFile schedaValidazioneFile, Long idSchedaValidazione) {
		FascicoloModel fascicoloModel = getFascicoloFromCuaa(cuaa);
		fascicoloModel.setIdSchedaValidazione(idSchedaValidazione);
		try {
			fascicoloModel.setSchedaValidazione(schedaValidazioneFile.getInputStream().readAllBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//fascicoloModel.setIdSchedaValidazione(fascicoloDao.getNextPdfId());
		fascicoloDao.save(fascicoloModel);
    }
	
	public Long getNewIdSchedaValidazione() {
		return fascicoloDao.getNextPdfId();
    }

    public byte[] getReportValidazioneFascicoloFirmata(final String cuaa) {
    	FascicoloModel fascicoloModel = getFascicoloFromCuaa(cuaa);
    	byte[] schedaValidazioneBytes = fascicoloModel.getSchedaValidazioneFirmata();
    	fascicoloModel.setStato(StatoFascicoloEnum.ALLA_FIRMA_AZIENDA);
		fascicoloDao.save(fascicoloModel);
    	return schedaValidazioneBytes;
    }
    
    public byte[] getReportValidazioneFascicoloDb(final String cuaa, final Integer idValidazione) {
    	Optional<FascicoloModel> fascicoloOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, idValidazione);
    	if (!fascicoloOpt.isPresent()) {
    		throw new IllegalArgumentException("Scheda validazione non trovata per idValidazione: " + idValidazione);
    	}
    	byte[] schedaValidazioneBytes = fascicoloOpt.get().getSchedaValidazioneFirmata();
    	return schedaValidazioneBytes;
    }
    
    public byte[] getReportValidazioneFascicoloDetenzioneAutonoma(final String cuaa) throws FascicoloInvalidConditionException, RestClientException, NoSuchElementException, IOException {
    	var fascicoloModel = getFascicoloFromCuaa(cuaa);
    	
    	Map<ControlliFascicoloAnagraficaCompletoEnum, EsitoControlloDto> mapControlliCompletezza = getControlloCompletezzaFascicoloDetenzioneAutonoma(cuaa);
    	
    	for (Map.Entry<ControlliFascicoloAnagraficaCompletoEnum, EsitoControlloDto> entry : mapControlliCompletezza.entrySet()) {
    		if (entry.getValue().getEsito() < 0) {
		    	throw new FascicoloInvalidConditionException(entry.getKey());
		    }
		}
    	
    	byte[] schedaValidazioneBytes = getReportValidazioneFascicoloBozzaDetenzioneAutonoma(cuaa);
    	fascicoloModel.setStato(StatoFascicoloEnum.ALLA_FIRMA_AZIENDA);
		fascicoloDao.save(fascicoloModel);
    	return schedaValidazioneBytes;
    }

    @Transactional
    public void salvaSchedaValidazioneFirmataFirmatario(final String cuaa, final byte[] schedaValidazioneBytes)
    		throws FirmaDocumentoException, IOException, FascicoloNonValidabileException {
    	FascicoloModel fascicoloModel = getFascicoloFromCuaa(cuaa);
    	if (!fascicoloModel.getStato().equals(StatoFascicoloEnum.ALLA_FIRMA_AZIENDA) && !fascicoloModel.getStato().equals(StatoFascicoloEnum.ALLA_FIRMA_CAA)) {
    		throw new FascicoloNonValidabileException(fascicoloModel.getStato());
    	}
//    	// controllo che sia firmata dal firmatario
//        PersonaFisicaConCaricaDto firmatario = personaFisicaOGiuridicaConCaricaService.getFirmatario(cuaa);
//        String codiceFiscaleFirmatario = firmatario.getCodiceFiscale();
//        var schedaValidazioneFile = File.createTempFile("schedaValidazioneFirmataFirmatario", ".pdf");
//        verificaFirmaSingolaFile(codiceFiscaleFirmatario, schedaValidazioneBytes, schedaValidazioneFile);
//        
//        // controllo che sia firmata dall'utente collegato
//        RappresentaIlModelloPerRappresentareUnUtenteDelSistema utenteConnesso = anagraficaUtenteClient.getUtenteConnesso();
//        String codiceFiscaleUtenteConnesso = utenteConnesso.getCodiceFiscale();
//        verificaFirmaSingolaFile(codiceFiscaleUtenteConnesso, schedaValidazioneBytes, schedaValidazioneFile);
//        Files.delete(schedaValidazioneFile.toPath());
    	
        PersonaFisicaConCaricaDto firmatario = personaFisicaOGiuridicaConCaricaService.getFirmatario(cuaa);
        String codiceFiscaleFirmatario = firmatario.getCodiceFiscale();
        RappresentaIlModelloPerRappresentareUnUtenteDelSistema utenteConnesso = anagraficaUtenteClient.getUtenteConnesso();
        String codiceFiscaleUtenteConnesso = utenteConnesso.getCodiceFiscale();
        verificaFirmaMultiplaFile(codiceFiscaleFirmatario, codiceFiscaleUtenteConnesso, schedaValidazioneBytes);
    	/*
	     * Protocollazione asincrona
    	 */
    	fascicoloModel.setSchedaValidazioneFirmata(schedaValidazioneBytes);
    	fascicoloModel.setStato(StatoFascicoloEnum.IN_VALIDAZIONE);
		fascicoloDao.save(fascicoloModel);
		Integer nextIdValidazione = fascicoloDao.getNextIdValidazione(fascicoloModel.getCuaa());
		var schedaFascicoloDto = new SchedaValidazioneFascicoloDto();
    	schedaFascicoloDto.setCodiceFiscale(cuaa);
    	schedaFascicoloDto.setTipoDetenzione(TipoDetenzioneEnum.MANDATO);
    	schedaFascicoloDto.setReport(new ByteArrayResource(schedaValidazioneBytes));
    	schedaFascicoloDto.setNextIdValidazione(nextIdValidazione);
		mediatorFascicoloPrivateClient.validazioneMandato(schedaFascicoloDto);
    }
    
    public byte[] getReportValidazioneFascicoloBozzaDetenzioneAutonoma(final String cuaa)
    		throws NoSuchElementException, FascicoloInvalidConditionException, RestClientException, IOException {

    	var report = this.getReportValidazione(cuaa);
		
    	return stampaComponent.stampaPDFA(
    			mapper.writeValueAsString(report),
    			"template/scheda_validazione_fascicolo_detenzione_autonoma.docx");
    }

    @Transactional
    public void salvaSchedaValidazioneDetenzioneAutonoma(final String cuaa, final byte[] schedaValidazioneBytes)
    		throws FirmaDocumentoException, IOException, FascicoloNonValidabileException {
    	FascicoloModel fascicoloModel = getFascicoloFromCuaa(cuaa);
    	if (!fascicoloModel.getStato().equals(StatoFascicoloEnum.ALLA_FIRMA_AZIENDA) && !fascicoloModel.getStato().equals(StatoFascicoloEnum.ALLA_FIRMA_CAA)) {
    		throw new FascicoloNonValidabileException(fascicoloModel.getStato());
    	}
    	// controllo che sia firmata dal firmatario
        PersonaFisicaConCaricaDto firmatario = personaFisicaOGiuridicaConCaricaService.getFirmatario(cuaa);
        String codiceFiscaleFirmatario = firmatario.getCodiceFiscale();
        var schedaValidazioneFile = File.createTempFile("schedaValidazioneFirmata", ".pdf");
        verificaFirmaSingolaFile(codiceFiscaleFirmatario, schedaValidazioneBytes, schedaValidazioneFile);
        Files.delete(schedaValidazioneFile.toPath());
    	/*
	     * Protocollazione / validazione asincrona
    	 */
    	fascicoloModel.setSchedaValidazioneFirmata(schedaValidazioneBytes);
    	fascicoloModel.setStato(StatoFascicoloEnum.IN_VALIDAZIONE);
		fascicoloDao.save(fascicoloModel);
		Integer nextIdValidazione = fascicoloDao.getNextIdValidazione(fascicoloModel.getCuaa());
		var schedaFascicoloDto = new SchedaValidazioneFascicoloDto();
    	schedaFascicoloDto.setCodiceFiscale(cuaa);
    	schedaFascicoloDto.setTipoDetenzione(TipoDetenzioneEnum.DETENZIONE_IN_PROPRIO);
    	schedaFascicoloDto.setReport(new ByteArrayResource(schedaValidazioneBytes));
    	schedaFascicoloDto.setNextIdValidazione(nextIdValidazione);
		mediatorFascicoloPrivateClient.validazioneDetenzioneAutonoma(schedaFascicoloDto);
    }
    
    public void setStatoFascicolo(String cuaa, StatoFascicoloEnum newStato) throws NoSuchElementException {
    	FascicoloModel fascicoloModel = getFascicoloFromCuaa(cuaa);
		fascicoloModel.setStato(newStato);
		if(newStato == StatoFascicoloEnum.IN_AGGIORNAMENTO) {
			fascicoloModel.setSchedaValidazione(null);
			fascicoloModel.setIdSchedaValidazione(null);
		}
		fascicoloDao.save(fascicoloModel);
    }

	@Transactional
    public void rimozioneControlliCompletezza(String cuaa) {
		List<ControlloCompletezzaModel> resultList = controlloCompletezzaDao.findByFascicolo_CuaaAndFascicolo_IdValidazione(cuaa, 0);
		if (resultList != null && resultList.size() > 0) {
			controlloCompletezzaDao.deleteInBatch(resultList);
		}
    }
}
