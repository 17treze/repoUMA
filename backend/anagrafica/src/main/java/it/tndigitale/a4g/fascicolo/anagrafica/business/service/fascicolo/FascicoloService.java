package it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.*;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.*;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.legacy.SincronizzazioneAgsDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.legacy.SincronizzazioneAgsException;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.CaaService;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.PersonaService;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.StatusMessagesEnum;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaProxyClient;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaUtenteClient;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.StampaComponent;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.legacy.FascicoloAgsService;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato.DetenzioneNotification;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato.DetenzioneService;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato.MandatoService;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.persona.PersonaFisicaOGiuridicaConCaricaService;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.*;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.*;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.mapper.FascicoloMapper;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.DatiAperturaFascicoloDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.PersonaFisicaConCaricaDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.PersonaFisicaDto;
import it.tndigitale.a4g.fascicolo.anagrafica.ioitalia.ModificaStatoFascicoloIoItaliaEvent;
import it.tndigitale.a4g.fascicolo.anagrafica.ioitalia.ModificaStatoFascicoloIoItaliaMessageBuilder;
import it.tndigitale.a4g.framework.event.store.handler.EventBus;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicoloId;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.StatoFascicoloEnum;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.framework.support.PersonaSelector;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.proxy.client.model.FascicoloSian;
import it.tndigitale.a4g.proxy.client.model.PagoPaIbanDettaglioDto;
import it.tndigitale.a4g.utente.client.model.RappresentaIlModelloDelProfiloDiUnUtente;
import it.tndigitale.a4g.utente.client.model.RappresentaIlModelloPerRappresentareUnUtenteDelSistema;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FascicoloService {
	private static final Logger logger = LoggerFactory.getLogger(FascicoloService.class);
	private static final String AT_NON_DISPONIBILE = "Anagrafe Tributaria non disponibile";

	@Autowired
	private FascicoloComponentMethodFactory fascicoloComponentFactory;
	@Autowired
	private MandatoService mandatoService;
	@Autowired
	private PersonaService personaService;
	@Autowired
	private CaaService caaService;
	@Autowired
	private AnagraficaUtenteClient anagraficaUtenteClient;
	@Autowired
	private FascicoloDao fascicoloDao;
	@Autowired
	private ModoPagamentoDao modoPagamentoDao;
	@Autowired
	private UtenteComponent utenteComponent;
	@Autowired
	private EventBus eventBus;
	@Autowired
	private UnitaTecnicoEconomicheDao unitaTecnicoEconomicheDao;
	@Autowired
	private ModificaStatoFascicoloIoItaliaMessageBuilder modificaStatoFascicoloIoItaliaMessageBuilder;
	@Autowired
	private PersonaFisicaOGiuridicaConCaricaService personaFisicaOGiuridicaConCaricaService;
	@Autowired
	private SincronizzazioneAgsDao sincronizzazioneAgsDao;
	@Autowired
	private OrganizzazioneDao organizzazioneDao;
	@Autowired
	private FascicoloOrganizzazioneDao fascicoloOrganizzazioneDao;
	@Autowired
	private DetenzioneService detenzioneService;
	@Autowired
	private DetenzioneInProprioDao detenzioneInProprioDao;
	@Autowired
	private Clock clock;
	@Autowired
	private StampaComponent stampaComponent;
	@Autowired
	private ObjectMapper mapper;
	@Autowired
	private AnagraficaProxyClient anagraficaProxyClient;
	@Autowired
	private FascicoloAgsService fascicoloAgsService;
	@Autowired
	private PersonaFisicaDao personaFisicaDao;
	@Autowired
	private EredeDao eredeDao;
	@Autowired
	private MovimentazioneFascicoloService movimentazioneFascicoloService;
	@Autowired
	private MovimentazioneDao movimentazioneDao;
	@Autowired
	private LockFascicoloDao lockFascicoloDao;

	@Value("${skip-richiesta-pago-pa}")
	private boolean skipRichiestaPagoPa;

	public DatiAperturaFascicoloDto verificaAperturaFascicoloCruscotto(String codiceFiscale) throws FascicoloValidazioneException {
		logger.debug("Verifico se s' possibile aprire il fascicolo per il codice fiscale {}", codiceFiscale);
		var fascicoloComponent = fascicoloComponentFactory.from(codiceFiscale);
		fascicoloComponent.verificaAperturaFascicoloDetenzioneAutonoma(FascicoloOperationEnum.APRI);
		return fascicoloComponent.getDatiPerAperturaFascicolo();
	}

	public DatiAperturaFascicoloDto validaOperazioneFascicolo(
			final String codiceFiscale,
			final FascicoloOperationEnum fascicoloOperationEnum) throws FascicoloValidazioneException {
		var fascicoloComponent = fascicoloComponentFactory.from(codiceFiscale);
		fascicoloComponent.validaOperazioneFascicolo(fascicoloOperationEnum);
		return fascicoloComponent.getDatiPerAperturaFascicolo();
	}

	public String getCodiceFiscaleRappresentanteLegale(final FascicoloModel fascicoloModel) {
		var personaModel = fascicoloModel.getPersona();
		String codiceFiscaleRappresentanteLegale = null;
		if (personaModel instanceof PersonaGiuridicaModel) {
			codiceFiscaleRappresentanteLegale = ((PersonaGiuridicaModel) personaModel).getCodiceFiscaleRappresentanteLegale();
		} else if (personaModel instanceof PersonaFisicaModel) {
			codiceFiscaleRappresentanteLegale = ((PersonaFisicaModel) personaModel).getCodiceFiscale();
		}
		return codiceFiscaleRappresentanteLegale;
	}

	private void setStato(final String cuaa, final StatoFascicoloEnum statoFascicolo) {
		var fascicoloModel = fascicoloDao
				.findByCuaaAndIdValidazione(cuaa, 0)
				.orElseThrow(() -> new EntityNotFoundException(StatusMessagesEnum.FASCICOLO_LOCALE_NON_ESISTENTE.name()));
		String username = utenteComponent.username();
		var statoFascicoloPrecedente = fascicoloModel.getStato();
		fascicoloModel.setStato(statoFascicolo)
				.setDataModifica(LocalDate.now())
				.setUtenteModifica(username);
		//		il fascicolo viene riportato in aggiornamento solo dallo stato CONTROLLATO(PRONTO_ALLA_FIRMA); infatti in tale stato i controlli di aggiornamento manuale sono disabilitati. 
		//		In tal caso si rimuovono i dati legati al file della scheda di validazione firmata.
		if (statoFascicolo.equals(StatoFascicoloEnum.IN_AGGIORNAMENTO) && (statoFascicoloPrecedente.equals(StatoFascicoloEnum.FIRMATO_CAA) || statoFascicoloPrecedente.equals(StatoFascicoloEnum.ALLA_FIRMA_AZIENDA) || statoFascicoloPrecedente.equals(StatoFascicoloEnum.ALLA_FIRMA_CAA) || statoFascicoloPrecedente.equals(StatoFascicoloEnum.VALIDATO))) {
			fascicoloModel.setSchedaValidazioneFirmata(null);
			fascicoloModel.setIdSchedaValidazione(null);
			fascicoloModel.setSchedaValidazione(null);
		}
		fascicoloDao.save(fascicoloModel);
		var ioItaliaMessage = modificaStatoFascicoloIoItaliaMessageBuilder
				.buildMessage(
						getCodiceFiscaleRappresentanteLegale(fascicoloModel),
						fascicoloModel.getDenominazione(),
						statoFascicolo);
		var event = new ModificaStatoFascicoloIoItaliaEvent(ioItaliaMessage);
		eventBus.publishEvent(event);
	}

	@Transactional
	public void setStatoAllaFirmaAzienda(final String cuaa) {
		setStato(cuaa, StatoFascicoloEnum.ALLA_FIRMA_AZIENDA);
	}

	@Transactional
	public void setStatoInAggiornamento(final String cuaa) {
		setStato(cuaa, StatoFascicoloEnum.IN_AGGIORNAMENTO);
	}

	@Transactional
	public void avviaProceduraChiusura(final String cuaa) throws ChiusuraFascicoloException {
		// verifico presenza fascicolo
		FascicoloModel fascicolo = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0).orElseThrow(() -> new EntityNotFoundException(StatusMessagesEnum.FASCICOLO_LOCALE_NON_ESISTENTE.name()));

		// se il fascicolo è in fase di successione allora è possibile procedere alla chiusura
		if (fascicolo.getStato().equals(StatoFascicoloEnum.IN_CHIUSURA)) {
			movimentazioneFascicoloService.chiudi(fascicolo);
			return;
		}

		if (PersonaSelector.isPersonaFisica(cuaa)) {
			try {
				it.tndigitale.a4g.proxy.client.model.PersonaFisicaDto titolare = anagraficaProxyClient.getPersonaFisicaAnagrafeTributaria(cuaa);

				// se il titolare è deceduto, avvia procedura per eredi
				if (titolare.isDeceduta().booleanValue()) {
					fascicolo.setStato(StatoFascicoloEnum.IN_CHIUSURA);
					fascicoloDao.save(fascicolo);
					// set data morte
					Optional<PersonaFisicaModel> titolareFascicolo = personaFisicaDao.findByCodiceFiscaleAndIdValidazione(titolare.getCodiceFiscale(), 0);
					if (titolareFascicolo.isPresent()) {
						titolareFascicolo.get().setDataMorte(titolare.getDataMorte());
						titolareFascicolo.get().setDeceduto(Boolean.TRUE);
						personaFisicaDao.save(titolareFascicolo.get());
					}
				} else {
					movimentazioneFascicoloService.chiudi(fascicolo);
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new ChiusuraFascicoloException(AT_NON_DISPONIBILE);
			}
		} else { // persona giuridica
			movimentazioneFascicoloService.chiudi(fascicolo);
		}
	}

	public void setRappresentanteLegaleRespingiValidazione(final String cuaa) {
		var fascicoloModel = fascicoloDao
				.findByCuaaAndIdValidazione(cuaa, 0)
				.orElseThrow(() -> new EntityNotFoundException(StatusMessagesEnum.FASCICOLO_LOCALE_NON_ESISTENTE.name()));

		if (!(fascicoloModel.getStato().equals(StatoFascicoloEnum.FIRMATO_CAA) || fascicoloModel.getStato().equals(StatoFascicoloEnum.ALLA_FIRMA_AZIENDA))) {
			throw new IllegalArgumentException("Fascicolo con cuaa=[" + cuaa + "] in stato diverso da FIRMATO_CAA o ALLA_FIRMA_AZIENDA. Non e' possibile quindi il respingimento");
		}
		fascicoloModel.setStato(StatoFascicoloEnum.IN_AGGIORNAMENTO);
		//		in caso di respingimento si deve si deve eliminare la scheda di validazione firmata dal CAA
		fascicoloModel.setSchedaValidazioneFirmata(null);
		fascicoloDao.save(fascicoloModel);

		//		invio mail al CAA detentore del mandato: se errore non deve far fallire la transazione
		try {
			mandatoService.notificaMailCaaRichiestaValidazioneRespinta(cuaa);
		} catch (Exception e) {
			logger.warn("Errore di invio mail: {} - {}", e.getMessage(), e.getCause());
		}
	}

	// Inserito il rollbackFor se mandatoService.associaMandatoANuovoFascicolo(mandatoDto) va in errore.
	@Transactional(rollbackFor = Exception.class)
	public FascicoloCreationResultDto apri(ApriFascicoloDto mandatoDto) throws Exception {
		logger.debug("Cerco di aprire il fascicolo per {} associandolo allo sportello {}",
				mandatoDto.getCodiceFiscale(), mandatoDto.getIdentificativoSportello());
		FascicoloAbstractComponent<?> fascicoloComponent = fascicoloComponentFactory.from(mandatoDto.getCodiceFiscale());
		var anomaliesList = fascicoloComponent.validaOperazioneFascicolo(FascicoloOperationEnum.APRI);
		LocalDate dataApertura = LocalDate.now();
		if (mandatoDto.getDataAperturaFascicolo() != null) {
			dataApertura = mandatoDto.getDataAperturaFascicolo();
		}
		FascicoloModel fascicolo = fascicoloComponent.apri(dataApertura);
		mandatoDto.setCodiceFiscale(fascicolo.getCuaa());
		mandatoService.associaMandatoANuovoFascicolo(mandatoDto);
		return new FascicoloCreationResultDto(anomaliesList, FascicoloMapper.fromFascicolo(fascicolo));
	}

	@Transactional(rollbackFor = Exception.class)
	public FascicoloMigrationResultDto migra(ApriFascicoloDto mandatoDto) throws Exception {
		// Lettura info da AGS
		FascicoloAgsDto fascicoloAgsDto = fascicoloAgsService.getFascicoloDaMigrare(mandatoDto.getCodiceFiscale());

		if (!fascicoloAgsDto.getDetenzioni().isEmpty()) {
			checkMigraFascicolo(fascicoloAgsDto.getCuaa(), fascicoloAgsDto.getStato(), fascicoloAgsDto.getDetenzioni().get(0).getTipoDetenzione(), fascicoloAgsDto.getDetenzioni().get(0).getIdentificativoSportello());

			mandatoDto.setDataAperturaFascicolo(fascicoloAgsDto.getDataCostituzione().toLocalDate());
			// Su AGS viene letta una sola detenzione: il mandato valido
			DetenzioneAgsDto detenzioneAgsDto = fascicoloAgsDto.getDetenzioni().get(0);
			mandatoDto.setDataInizioMandato(detenzioneAgsDto.getDataInizio().toLocalDate());
			mandatoDto.setDataFineMandato(detenzioneAgsDto.getDataFine().toLocalDate());
		} else {
			throw new FascicoloMigrazioneException(FascicoloMigrazioneEnum.DETENZIONE_ASSENTE);
		}
		// Apertura fascicolo su A4G
		FascicoloCreationResultDto fascicoloCreationResultDto = apri(mandatoDto);

		FascicoloMigrationResultDto fascicoloMigrationResultDto = new FascicoloMigrationResultDto();
		fascicoloMigrationResultDto.setAnomalies(fascicoloCreationResultDto.getAnomalies());
		fascicoloMigrationResultDto.setId(fascicoloCreationResultDto.getFascicoloDto().getId());

		return fascicoloMigrationResultDto;
	}

	public DatiAperturaFascicoloDto checkMigraFascicolo(String cuaa, StatoFascicoloLegacy stato, TipoDetenzioneAgs tipoDetenzione, Long sportello) throws Exception {
		if (!caaService.verificaSportelloIsAbilitato(sportello).booleanValue()) {
			throw new FascicoloMigrazioneException(FascicoloMigrazioneEnum.SPORTELLO_NON_ABILITATO_UTENTE_CONNESSO);
		}
		if (!(stato == StatoFascicoloLegacy.IN_LAVORAZIONE || stato == StatoFascicoloLegacy.VALIDO)) {
			throw new FascicoloMigrazioneException(FascicoloMigrazioneEnum.STATO_NON_MIGRABILE);
		}
		if (!(tipoDetenzione == TipoDetenzioneAgs.MANDATO)) {
			throw new FascicoloMigrazioneException(FascicoloMigrazioneEnum.DETENZIONE_NON_MIGRABILE);
		}
		if (fascicoloDao.existsByCuaaAndIdValidazione(cuaa, 0)) {
			throw new FascicoloValidazioneException(FascicoloValidazioneEnum.FASCICOLO_LOCALE_ESISTENTE);
		}
		var fascicoloComponent = fascicoloComponentFactory.from(cuaa);
		return fascicoloComponent.getDatiPerAperturaFascicolo();
	}

	@Transactional(rollbackFor = Exception.class)
	public void migraModiPagamento(String cuaa) {
		var fascicoloModel = getFascicoloModelOrThrow(cuaa, 0);
		// Lettura modalità di pagamento da AGS
		List<ModoPagamentoAgsDto> modiPagamentoAgsDto = fascicoloAgsService.getModiPagamentoDaMigrare(cuaa);
		for (ModoPagamentoAgsDto modoPagamentoAgsDto : modiPagamentoAgsDto) {
			PagoPaIbanDettaglioDto pagoPaIbanDettaglioDto = null;

			try {

				if (skipRichiestaPagoPa) {
					pagoPaIbanDettaglioDto = anagraficaProxyClient.checkIbanFake(modoPagamentoAgsDto.getIban());
				} else {
					PersonaModel persona = fascicoloModel.getPersona();
					if (persona instanceof PersonaGiuridicaModel) {
						String partitaIva = ((PersonaGiuridicaModel) persona).getPartitaIVA();
						pagoPaIbanDettaglioDto = anagraficaProxyClient.verificaIbanPersonaGiuridicaEPartitaIva(cuaa, modoPagamentoAgsDto.getIban(), partitaIva);
					} else {
						pagoPaIbanDettaglioDto = anagraficaProxyClient.verificaIbanPersonaFisica(cuaa, modoPagamentoAgsDto.getIban());
					}
				}

				if (pagoPaIbanDettaglioDto == null || pagoPaIbanDettaglioDto.getIban() == null) {
					logger.info("IBAN nullo per il cuaa {}: passo al successivo", cuaa);
					return;
				}


				var modoPagamentoModel = new ModoPagamentoModel();
				modoPagamentoModel.setBic(pagoPaIbanDettaglioDto.getBic());
				modoPagamentoModel.setDenominazioneIstituto(pagoPaIbanDettaglioDto.getDenominazioneIstituto());
				modoPagamentoModel.setDenominazioneFiliale(pagoPaIbanDettaglioDto.getDenominazioneFiliale());
				modoPagamentoModel.setCittaFiliale(pagoPaIbanDettaglioDto.getCittaFiliale());
				modoPagamentoModel.setIban(pagoPaIbanDettaglioDto.getIban().trim().replaceAll("\\s", "").toUpperCase(Locale.ROOT));
				modoPagamentoModel.setFascicolo(fascicoloModel);
				modoPagamentoDao.save(modoPagamentoModel);
			} catch (Exception e) {
				logger.warn("Non sono riuscito a migrare per il cuaa {} l'iban {}", cuaa, pagoPaIbanDettaglioDto, e);
				// non lancio l'eccezione perché preferisco passare all'iban successivo
			}
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public FascicoloCreationResultDto trasferisci(ApriFascicoloDto mandatoDto, FascicoloOperationEnum fascicoloOperationEnum) throws Exception {
		logger.debug("Cerco di aprire e trasferire il fascicolo per {} associandolo allo sportello {}",
				mandatoDto.getCodiceFiscale(), mandatoDto.getIdentificativoSportello());
		FascicoloAbstractComponent fascicoloComponent = fascicoloComponentFactory.from(mandatoDto.getCodiceFiscale());
		List anomaliesList = fascicoloComponent.validaOperazioneFascicolo(fascicoloOperationEnum);
		FascicoloModel fascicolo = fascicoloComponent.trasferisci();
		mandatoDto.setCodiceFiscale(fascicolo.getCuaa());
		mandatoService.associaMandatoANuovoFascicolo(mandatoDto);
		return new FascicoloCreationResultDto(
				anomaliesList,
				FascicoloMapper.fromFascicolo(fascicolo));
	}

	@Transactional
	public FascicoloCreationResultDto aggiorna(final String codiceFiscale)
			throws FascicoloAllaFirmaAziendaException, FascicoloAllaFirmaCAAException, FascicoloValidazioneException {
		var fascicoloComponent = fascicoloComponentFactory.from(codiceFiscale);
		List anomaliesList = fascicoloComponent.validaOperazioneFascicolo(FascicoloOperationEnum.AGGIORNA);
		return new FascicoloCreationResultDto(
				anomaliesList,
				FascicoloMapper.fromFascicolo(fascicoloComponent.aggiorna()));
	}

	/**
	 * Verifica se l'utente connesso ha profilo "azienda"
	 *
	 * @return
	 */
	public boolean containsProfiloAzienda(List<RappresentaIlModelloDelProfiloDiUnUtente> profiliUtenteConnesso) {
		if (profiliUtenteConnesso != null && !profiliUtenteConnesso.isEmpty()) {
			return (profiliUtenteConnesso.stream().filter(profilo -> profilo.getIdentificativo().equals("azienda")).findFirst().orElse(null) != null);
		}
		return false;
	}

	/**
	 * Verifica se l'utente connesso ha profilo "automazione_approvazione_utenza_tecnica"
	 *
	 * @return
	 */
	public boolean containsProfiloAutomazioneApprovazione(List<RappresentaIlModelloDelProfiloDiUnUtente> profiliUtenteConnesso) {
		if (profiliUtenteConnesso != null && !profiliUtenteConnesso.isEmpty()) {
			return (profiliUtenteConnesso.stream().filter(profilo -> profilo.getIdentificativo().equals("automazione_approvazione_utenza_tecnica")).findFirst().orElse(null) != null);
		}
		return false;
	}

	/**
	 * Verifica se l'utente connesso ha profilo "responsabile_fascicolo_pat"
	 *
	 * @return
	 */
	public boolean containsProfiloResponsabileFascicoloPat(List<RappresentaIlModelloDelProfiloDiUnUtente> profiliUtenteConnesso) {
		if (profiliUtenteConnesso != null && !profiliUtenteConnesso.isEmpty()) {
			return (profiliUtenteConnesso.stream().filter(profilo -> profilo.getIdentificativo().equals("responsabile_fascicolo_pat")).findFirst().orElse(null) != null);
		}
		return false;
	}


	/**
	 * Dato il cuaa verifica se l'utente connesso ha il profilo azienda ed è il primo firmatario
	 *
	 * @param cuaa
	 * @return
	 */
	public boolean controlloProfiloAziendaEPrimoFirmatario(final String cuaa) {
		try {
			//			getUtenteConnesso() restituisce solo identificativo, codice fiscale e profili dell'utente connesso
			RappresentaIlModelloPerRappresentareUnUtenteDelSistema utenteConnesso = anagraficaUtenteClient.getUtenteConnesso();
			//			verifica se è presente il profilo azienda
			if (!containsProfiloAzienda(utenteConnesso.getProfili())) {
				return false;
			}
			PersonaFisicaConCaricaDto personaFisicaConCaricaDto = personaFisicaOGiuridicaConCaricaService.getFirmatario(cuaa);
			if (personaFisicaConCaricaDto != null
					&& !StringUtils.isBlank(personaFisicaConCaricaDto.getCodiceFiscale())
					&& personaFisicaConCaricaDto.getCodiceFiscale().equals(utenteConnesso.getCodiceFiscale())) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}


	/**
	 * Dato il cuaa verifica se l'utente connesso ha il profilo azienda ed è il rappresentante legale dell'azienda (persona giuridica del cuaa)
	 *
	 * @param cuaa
	 * @return
	 */
//	public boolean controlloProfiloAziendaERappresentanteLegale(final String cuaa, final Integer idValidazione) {
//		try {
//			//			getUtenteConnesso() restituisce solo identificativo, codice fiscale e profili dell'utente connesso
//			RappresentaIlModelloPerRappresentareUnUtenteDelSistema utenteConnesso = anagraficaUtenteClient.getUtenteConnesso();
//			//			verifica se è presente il profilo azienda
//			if (!containsProfiloAzienda(utenteConnesso.getProfili())) {
//				return false;
//			}
//			String codiceFiscaleUtenteConnesso = utenteConnesso.getCodiceFiscale();
//			//			reperire il fascicolo con quel cuaa
//			var fascicoloModel = getFascicoloModelOrThrow(cuaa, idValidazione);
//			String fascicoloCUAA = fascicoloModel.getCuaa();
//			if (fascicoloCUAA.length() == 11) {
//				//				fascicolo di persona giuridica
//				var personaGiuridicaDto = personaService.getPersonaGiuridica(fascicoloCUAA, idValidazione);
//				if (personaGiuridicaDto == null) {
//					return false;
//				}
//				return personaGiuridicaDto.getRappresentanteLegale().getCodiceFiscale().equals(codiceFiscaleUtenteConnesso);
//			} else {
//				return false;
//			}
//		} catch (Exception e) {
//			return false;
//		}
//	}

	public boolean isUtenteFirmatarioFascicolo(final String cuaa, Integer idValidazione) {
		try {
			RappresentaIlModelloPerRappresentareUnUtenteDelSistema utenteConnesso = anagraficaUtenteClient.getUtenteConnesso();
			if (!containsProfiloAzienda(utenteConnesso.getProfili())) {
				return false;
			}
			String codiceFiscaleUtenteConnesso = utenteConnesso.getCodiceFiscale();
			var fascicoloModel = getFascicoloModelOrThrow(cuaa, idValidazione);
			var personaModel = fascicoloModel.getPersona();
			if (personaModel instanceof PersonaGiuridicaModel) {
				PersonaFisicaConCaricaDto firmatario = personaFisicaOGiuridicaConCaricaService.getFirmatario(cuaa);
				return firmatario.getCodiceFiscale().equals(codiceFiscaleUtenteConnesso);
			} else if (personaModel instanceof PersonaFisicaModel) {
				// Il rappresentante legale con diritto di firma esiste solo per persone giuridiche
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public FascicoloModel getFascicoloModelOrThrow(final String cuaa, final Integer idValidazione) {
		return fascicoloDao.findByCuaaAndIdValidazione(cuaa, idValidazione).orElseThrow(
				() -> new EntityNotFoundException(String.format("Fascicolo CUAA %s non trovato", cuaa)));
	}

	private OrganizzazioneModel getOrganizzazioneModelOrThrow(final Long id) {
		return organizzazioneDao.findById(id).orElseThrow(
				() -> new EntityNotFoundException(String.format("Organizzazione con id=%s non trovata", id)));
	}

	@Transactional
	public Long inserimentoModoPagamentoErede(final String cuaa, final ModoPagamentoDto modoPagamento) throws FascicoloNonInChiusuraException, PersonaNonFisicaException, PersonaFisicaNonDecedutaException, EredeFascicoloException {
//		 0. controllo se sono forniti gli estremi del modo di pagamento
		if (modoPagamento == null || StringUtils.isBlank(modoPagamento.getIban())) {
			throw new IllegalArgumentException("Per il cuaa ["+ cuaa +"] non e' stato fornito un IBAN");
		}
//		 1. controllo se esiste il fascicolo con cuaa in input
		var fascicoloModel = getFascicoloModelOrThrow(cuaa, 0);
//		 2. controllo se il fascicolo e' in uno stato corretto (IN_CHIUSURA); altrimenti errore
		if (!fascicoloModel.getStato().equals(StatoFascicoloEnum.IN_CHIUSURA)) {
			throw new FascicoloNonInChiusuraException(fascicoloModel.getStato());
		}
		PersonaModel personaModel = fascicoloModel.getPersona();
//		 3. controllo se la persona associata al fascicolo e' una persona fisica; altrimenti errore
		if (personaModel == null || !PersonaSelector.isPersonaFisica(personaModel.getCodiceFiscale())) {
			throw new PersonaNonFisicaException(personaModel == null ? null : personaModel.getCodiceFiscale());
		}

		PersonaFisicaDto personaFisicaDto = personaService.getPersonaFisica(personaModel.getCodiceFiscale(), 0);
//		 4. controllo se la persona associata il fascicolo abbia una data decesso; altrimenti errore
		if (!personaFisicaDto.getAnagrafica().isDeceduto()) {
			throw new PersonaFisicaNonDecedutaException(personaModel.getCodiceFiscale());
		}
//		 5. controllo se esiste almeno un erede associato al fascicolo; altrimenti errore
		List<EredeModel> eredeModelList = eredeDao.findByFascicolo_Cuaa(cuaa);
		if (eredeModelList == null || eredeModelList.isEmpty()) {
			throw new EredeFascicoloException("Non e' associato alcun erede al fascicolo");
		}
//		 6. controllo se esiste uno e un solo erede associato al fascicolo che sia firmatario; altrimenti errore
		long numeroErediFirmatari = eredeModelList.stream().filter(erede -> erede.isFirmatario()).count();
		if (numeroErediFirmatari != 1) {
			throw new EredeFascicoloException("Deve essere specificato uno ed un solo erede firmatario. Conteggio attuale=[" + numeroErediFirmatari + "]");
		}
		Optional<EredeModel> eredeModelOpt = eredeModelList.stream().filter(erede -> erede.isFirmatario()).findFirst();
		if (eredeModelOpt.isEmpty()) {
			throw new EredeFascicoloException("Non esiste un erede firmatario.");
		}
//		 7. chiamata (compreso skip) utilizzando come cuaa il cf dell'erede
		PagoPaIbanDettaglioDto pagoPaIbanDettaglioDto = null;
		if (skipRichiestaPagoPa) {
			pagoPaIbanDettaglioDto = anagraficaProxyClient.checkIbanFake(modoPagamento.getIban());
		} else {
			pagoPaIbanDettaglioDto = anagraficaProxyClient.verificaIbanPersonaFisica(eredeModelOpt.get().getPersonaFisica().getCodiceFiscale(), modoPagamento.getIban());
		}
		//		3. inserimento:
		//		3.1 verificare che non sia già inserito lo stesso iban altrimenti errore
		Long nTuple = modoPagamentoDao.countByFascicolo_CuaaAndIbanAndIdValidazione(
				cuaa, modoPagamento.getIban().trim().replaceAll("\\s", "").toUpperCase(Locale.ROOT), 0);
		if (nTuple > 0) {
			throw new IllegalArgumentException("IBAN [" + modoPagamento.getIban() +"] gia' presente per il cuaa ["+ cuaa +"]");
		}
		//		3.2 inserimento dati iban + banca
		//		utilizzare i dati ricevuti (pagoPaIbanDettaglioDto) da anagraficaProxyClient.verificaIbanPersonaGiuridica() o verificaIbanPersonaFisica()
		var modoPagamentoModel = new ModoPagamentoModel();
		modoPagamentoModel.setDenominazioneIstituto(pagoPaIbanDettaglioDto.getDenominazioneIstituto());
		modoPagamentoModel.setDenominazioneFiliale(pagoPaIbanDettaglioDto.getDenominazioneFiliale());
		modoPagamentoModel.setCittaFiliale(pagoPaIbanDettaglioDto.getCittaFiliale());
		modoPagamentoModel.setIban(modoPagamento.getIban().trim().replaceAll("\\s", "").toUpperCase(Locale.ROOT));
		modoPagamentoModel.setFascicolo(fascicoloModel);
		modoPagamentoDao.save(modoPagamentoModel);
		return modoPagamentoModel.getId();
	}

	@Transactional
	public ModoPagamentoModel inserimentoModoPagamento(final String cuaa, final ModoPagamentoDto modoPagamento)
			throws FascicoloAllaFirmaAziendaException, FascicoloAllaFirmaCAAException {
		if (modoPagamento == null || StringUtils.isBlank(modoPagamento.getIban())) {
			throw new IllegalArgumentException("Per il cuaa ["+ cuaa +"] non e' stato fornito un IBAN");
		}
		//		2. verifica esistenza fascicolo + eventuali diritti di lettura altrimenti errore
		// da chiarire l'utilità di caricare un fascicolo e non usarlo, vedi anche altri punti nel codice
		var fascicoloModel = getFascicoloModelOrThrow(cuaa, 0);
		if (fascicoloModel.getStato().equals(StatoFascicoloEnum.ALLA_FIRMA_AZIENDA)) {
			throw new FascicoloAllaFirmaAziendaException();
		}
		if (fascicoloModel.getStato().equals(StatoFascicoloEnum.ALLA_FIRMA_CAA)) {
			throw new FascicoloAllaFirmaCAAException();
		}
		//		al servizio pagoPa verra' inviato cuaa, iban ed eventualmente la partita iva in caso di persona giuridica
		String partitaIva = null;
		PagoPaIbanDettaglioDto pagoPaIbanDettaglioDto = null;
		PersonaModel persona = fascicoloModel.getPersona();
 
		if (skipRichiestaPagoPa) {
			pagoPaIbanDettaglioDto = anagraficaProxyClient.checkIbanFake(modoPagamento.getIban());
		} else if (persona instanceof PersonaGiuridicaModel) {
			partitaIva = ((PersonaGiuridicaModel)persona).getPartitaIVA();
			//			se persona giuridica inviare anche p.iva
			pagoPaIbanDettaglioDto = anagraficaProxyClient.verificaIbanPersonaGiuridicaEPartitaIva(cuaa, modoPagamento.getIban(), partitaIva);
		} else {
			pagoPaIbanDettaglioDto = anagraficaProxyClient.verificaIbanPersonaFisica(cuaa, modoPagamento.getIban());
		}
		//		3. inserimento:
		//		3.1 verificare che non sia già inserito lo stesso iban altrimenti errore
		Long nTuple = modoPagamentoDao.countByFascicolo_CuaaAndIbanAndIdValidazione(
				cuaa, modoPagamento.getIban().trim().replaceAll("\\s", "").toUpperCase(Locale.ROOT), 0);
		if (nTuple > 0) {
			throw new IllegalArgumentException("IBAN [" + modoPagamento.getIban() +"] gia' presente per il cuaa ["+ cuaa +"]");
		}
		//		3.2 inserimento dati iban + banca
		//		utilizzare i dati ricevuti (pagoPaIbanDettaglioDto) da anagraficaProxyClient.verificaIbanPersonaGiuridica() o verificaIbanPersonaFisica()
		var modoPagamentoModel = new ModoPagamentoModel();
		modoPagamentoModel.setDenominazioneIstituto(pagoPaIbanDettaglioDto.getDenominazioneIstituto());
		modoPagamentoModel.setDenominazioneFiliale(pagoPaIbanDettaglioDto.getDenominazioneFiliale());
		modoPagamentoModel.setCittaFiliale(pagoPaIbanDettaglioDto.getCittaFiliale());
		modoPagamentoModel.setIban(modoPagamento.getIban().trim().replaceAll("\\s", "").toUpperCase(Locale.ROOT));
		modoPagamentoModel.setFascicolo(fascicoloModel);
		modoPagamentoDao.save(modoPagamentoModel); 
		return modoPagamentoModel;
	}

	@Transactional
	public void rimozioneModoPagamento(final String cuaa, final Long id) throws FascicoloAllaFirmaAziendaException, FascicoloAllaFirmaCAAException {
		//		2. verifica esistenza fascicolo + eventuali diritti di lettura altrimenti errore
		var fascicoloModel = getFascicoloModelOrThrow(cuaa, 0);
		if (fascicoloModel.getStato().equals(StatoFascicoloEnum.ALLA_FIRMA_AZIENDA)) {
			throw new FascicoloAllaFirmaAziendaException();
		}
		if (fascicoloModel.getStato().equals(StatoFascicoloEnum.ALLA_FIRMA_CAA)) {
			throw new FascicoloAllaFirmaCAAException();
		}
		//		3. eliminazione:
		//		3.1 verificare che sia presente come metodo di pagamento id e che sia proprio associato al cuaa
		Optional<ModoPagamentoModel> modoPagamentoOpt = modoPagamentoDao.findByIdAndIdValidazione(id, 0);
		if (!modoPagamentoOpt.isPresent()) {
			throw new IllegalArgumentException("Modo di pagamento con id= [" + id +"] non presente nel db");
		}
		ModoPagamentoModel modoPagamento = modoPagamentoOpt.get();
		if (!modoPagamento.getFascicolo().getCuaa().equals(cuaa)) {
			throw new IllegalArgumentException("Modo di pagamento con id= [" + id +"] non associato al cuaa=[" + cuaa + "]");
		}
		//		3.2 eliminazione record
		modoPagamentoDao.delete(modoPagamento);
	}

	@Transactional
	public List<ModoPagamentoDto> elencoModoPagamento(final String cuaa, final Integer idValidazione) {
		//		2. verifica esistenza fascicolo + eventuali diritti di lettura altrimenti errore
		//		3. eliminazione:
		//		3.1 verificare che sia presente come metodo di pagamento id e che sia proprio associato al cuaa
		return ModoPagamentoDto.build(modoPagamentoDao.findByFascicolo_CuaaAndIdValidazione(cuaa, idValidazione));
	}

	//	TODO: non dovrebbe essere chiamato da nessuna parte dal frontend (ne' A4G, ne' cruscotto)
	//	public boolean isCodiceIbanConforme(String iban) {
	//		return IBANUtils.validateIBAN(iban);
	//	}

	public List<UnitaTecnicoEconomicheDto> unitaTecnicoEconomiche(final String cuaa, final Integer idValidazione) {
		//		2. verifica esistenza fascicolo + eventuali diritti di lettura altrimenti errore
		return UnitaTecnicoEconomicheDto.build(unitaTecnicoEconomicheDao.findByPersona_CodiceFiscaleAndIdValidazione(cuaa, idValidazione));
	}

	//G.De Vincentiis, 3 maggio 2021
	@Transactional
	public void sincronizzaAgs(final String cuaa, final Integer idValidazione) throws SincronizzazioneAgsException {
		sincronizzazioneAgsDao.sincronizzaFascicoloAgs(cuaa, idValidazione);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void fascicoloAggiornaDataFontiEsterne(Long id) {
		var key = new EntitaDominioFascicoloId(id, 0);
		Optional<FascicoloModel> fascicoloModelOpt = fascicoloDao.findById(key);
		if (fascicoloModelOpt.isPresent()) {
			var fascicoloModel = fascicoloModelOpt.get();
			fascicoloModel.setDtAggiornamentoFontiEsterne(LocalDateTime.now());
			fascicoloDao.save(fascicoloModel);
		}
	}

	public List<OrganizzazioneDto> getAllOrganizzazioni(){
		return OrganizzazioneDto.build(organizzazioneDao.findAll()); 
	}

	// G.De Vincentiis, 30 giugno 2021
	public List<DichiarazioneAssociativaDto> getDichiarazioniAssociative(String cuaa) {
		List<FascicoloOrganizzazioneModel> fascicoloOrganizzazioneList = fascicoloOrganizzazioneDao.findByCuaa(cuaa);
		return DichiarazioneAssociativaDto.build(fascicoloOrganizzazioneList); 
	}

	@Transactional
	public void chiudiDichiarazioneAssociativa(String cuaa, DichiarazioneAssociativaDto dichiarazioneAssociativaDto) {
		if (dichiarazioneAssociativaDto == null) {
			return;
		}

		// reperire relazione fascicolo - organizzazione
		var fascicoloOrganizzazioneOpt = fascicoloOrganizzazioneDao.findById(dichiarazioneAssociativaDto.getId());

		if (!fascicoloOrganizzazioneOpt.isPresent()) {
			throw new IllegalArgumentException(String.format("Autodichiarazione non trovata per il cuaa %s e l'organizzazione %s",
					cuaa, dichiarazioneAssociativaDto.getOrganizzazione().getDenominazione()));
		}

		var fascicoloOrganizzazioneModel = fascicoloOrganizzazioneOpt.get();
		fascicoloOrganizzazioneModel.setDataFineAssociazione(dichiarazioneAssociativaDto.getDataFineAssociazione());
		fascicoloOrganizzazioneModel.setDataCancellazioneAssociazione(clock.now());
		fascicoloOrganizzazioneModel.setUtenteCancellazione(utenteComponent.username());

		fascicoloOrganizzazioneDao.save(fascicoloOrganizzazioneModel);
	}

	@Transactional
	public void rappresentanteLegaleAggiungiAutodichiarazioneAssociativa(String cuaa,
			DichiarazioneAssociativaDto dichiarazioneAssociativaDto) throws FascicoloOrganizzazioneException {
		if (dichiarazioneAssociativaDto == null) {
			return;
		}

		if (dichiarazioneAssociativaDto.getOrganizzazione() == null || dichiarazioneAssociativaDto.getOrganizzazione().getId() == null) {
			throw new IllegalArgumentException(String.format("Per la dichiarazione associativa del cuaa %s non è stata fornita un'organizzazione", cuaa) );
		}
		//		reperire organizzazione
		var organizzazioneModel = getOrganizzazioneModelOrThrow(dichiarazioneAssociativaDto.getOrganizzazione().getId());

		//		controlli su eventuale esistenza di un FascicoloOrganizzazioneModel. In caso di esistenza non si aggiunge o si lancia un errore
		var fascicoloOrganizzazioneList = fascicoloOrganizzazioneDao.findByCuaaAndOrganizzazione(cuaa, organizzazioneModel).stream().filter( x -> x.getDataCancellazioneAssociazione() == null).collect(Collectors.toList());

		if (fascicoloOrganizzazioneList != null && !fascicoloOrganizzazioneList.isEmpty()) {
			throw new FascicoloOrganizzazioneException(String.format("Per il cuaa '%s' è stata già definita l'associazione con l'organizzazione '%s'", cuaa, organizzazioneModel.getDenominazione()));
		}

		//		impostazione di date di default in fase di inserimento autodichiarazione
		dichiarazioneAssociativaDto.setDataInserimentoAssociazione(clock.now());
		dichiarazioneAssociativaDto.setDataCancellazioneAssociazione(null);
		dichiarazioneAssociativaDto.setDataFineAssociazione(null);
		//		salvataggio di FascicoloOrganizzazioneModel
		fascicoloOrganizzazioneDao.save(buildFascicoloOrganizzazioneModel(cuaa, dichiarazioneAssociativaDto, organizzazioneModel));
	}

	private FascicoloOrganizzazioneModel buildFascicoloOrganizzazioneModel(String cuaa, DichiarazioneAssociativaDto dichiarazioneAssociativaDto, OrganizzazioneModel organizzazioneModel) {
		var fascicoloOrganizzazioneModel = new FascicoloOrganizzazioneModel();
		fascicoloOrganizzazioneModel.setId(dichiarazioneAssociativaDto.getId());
		fascicoloOrganizzazioneModel.setDataInserimentoAssociazione(dichiarazioneAssociativaDto.getDataInserimentoAssociazione());
		fascicoloOrganizzazioneModel.setDataCancellazioneAssociazione(dichiarazioneAssociativaDto.getDataCancellazioneAssociazione());
		fascicoloOrganizzazioneModel.setDataInizioAssociazione(dichiarazioneAssociativaDto.getDataInizioAssociazione());
		fascicoloOrganizzazioneModel.setDataFineAssociazione(dichiarazioneAssociativaDto.getDataFineAssociazione());
		fascicoloOrganizzazioneModel.setCuaa(cuaa);
		fascicoloOrganizzazioneModel.setOrganizzazione(organizzazioneModel);
		fascicoloOrganizzazioneModel.setUtenteInserimento(utenteComponent.username());

		return fascicoloOrganizzazioneModel;
	}

	@Transactional(rollbackFor = Exception.class)
	public FascicoloCreationResultDto apriFascicoloCruscotto(String codiceFiscale) throws FascicoloValidazioneException {
		logger.debug("Apro il fascicolo per {}", codiceFiscale);
		FascicoloAbstractComponent fascicoloComponent = fascicoloComponentFactory.from(codiceFiscale);
		List anomaliesList = fascicoloComponent.verificaAperturaFascicoloDetenzioneAutonoma(FascicoloOperationEnum.APRI);
		FascicoloModel fascicolo = fascicoloComponent.apri(LocalDate.now());
		// Inserimento tabella associata
		DetenzioneInProprioModel detenzioneInProprioModel = new DetenzioneInProprioModel();
		detenzioneInProprioModel.setFascicolo(fascicolo);
		detenzioneInProprioModel.setDataInizio(clock.today());
		detenzioneInProprioDao.save(detenzioneInProprioModel);
		// fine
		return new FascicoloCreationResultDto(
				anomaliesList,
				FascicoloMapper.fromFascicolo(fascicolo));
	}

	public boolean isDetenzioneInProprioModel(final String cuaa) {
		try {
			var fascicoloModel = getFascicoloModelOrThrow(cuaa, 0);			
			Optional<DetenzioneModel> optDetenzioneCorrente = detenzioneService.getDetenzioneCorrente(fascicoloModel);
			DetenzioneModel detenzioneCorrente = optDetenzioneCorrente.orElseThrow();
			return (detenzioneCorrente instanceof DetenzioneInProprioModel);
		} catch (EntityNotFoundException | NoSuchElementException e) {
			return false;
		}
	}

	// G.De Vincentiis - FAS-TRASF-01.4
	public byte[] richiestaTrasferimentoOp(final String cuaa, FascicoloOperationEnum fascicoloOperationEnum)
			throws NoSuchElementException, RestClientException, IOException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		ModuloRichiestaTrasferimentoOpDto moduloDto = new ModuloRichiestaTrasferimentoOpDto();
		var fascicoloModel = getFascicoloModelOrThrow(cuaa, 0);
		PersonaModel personaModel = fascicoloModel.getPersona();
		if (fascicoloOperationEnum.equals(FascicoloOperationEnum.TRASFERISCI)) {
			moduloDto.setCostituzione(false);
			moduloDto.setTrasferimento(true);
		} else if (fascicoloOperationEnum.equals(FascicoloOperationEnum.APRI_E_TRASFERISCI)) {
			moduloDto.setCostituzione(true);
			moduloDto.setTrasferimento(true);
		}
		moduloDto.setCuaaAzienda(fascicoloModel.getCuaa());
		moduloDto.setDenominazioneAzienda(fascicoloModel.getDenominazione());
		// dati azienda
		if (personaModel instanceof PersonaGiuridicaModel) {
			PersonaGiuridicaModel pg = (PersonaGiuridicaModel) personaModel;
			//				per i dati del rappresentante legale e azienda si deve fare un'interrogazione verso AT
			it.tndigitale.a4g.proxy.client.model.PersonaFisicaDto firmatario = anagraficaProxyClient.getPersonaFisicaAnagrafeTributaria(pg.getCodiceFiscaleRappresentanteLegale());
			SedeModel sede = pg.getSedeLegale();
			if (sede.getIndirizzoCameraCommercio() != null) {
				moduloDto.setComuneSedeAzienda(sede.getIndirizzoCameraCommercio().getComune());
				moduloDto.setProvSedeAzienda(sede.getIndirizzoCameraCommercio().getProvincia());
				if (StringUtils.isBlank(sede.getIndirizzoCameraCommercio().getVia()) || StringUtils.isBlank(sede.getIndirizzoCameraCommercio().getNumeroCivico())) {
					moduloDto.setIndirizzoSedeAzienda(sede.getIndirizzoCameraCommercio().getDescrizioneEstesa());
				} else {
					moduloDto.setIndirizzoSedeAzienda(sede.getIndirizzoCameraCommercio().getVia() + ", " + sede.getIndirizzoCameraCommercio().getNumeroCivico());	
				}
			} else if (sede.getIndirizzo() != null) {
				moduloDto.setComuneSedeAzienda(sede.getIndirizzo().getComune());
				moduloDto.setProvSedeAzienda(sede.getIndirizzo().getProvincia());
				if (StringUtils.isBlank(sede.getIndirizzo().getVia()) || StringUtils.isBlank(sede.getIndirizzo().getNumeroCivico())) {
					moduloDto.setIndirizzoSedeAzienda(sede.getIndirizzo().getDescrizioneEstesa());
				} else {
					moduloDto.setIndirizzoSedeAzienda(sede.getIndirizzo().getVia() + ", " + sede.getIndirizzo().getNumeroCivico());	
				}
			}
			moduloDto.setPecAzienda(pg.getPec());
			// R.L.
			if (firmatario != null) {
				moduloDto.setCodiceFiscaleFirmatario(firmatario.getCodiceFiscale());
				moduloDto.setNomeFirmatario(firmatario.getAnagrafica().getNome() + " " + firmatario.getAnagrafica().getCognome());
				moduloDto.setComuneNascitaFirmatario(firmatario.getAnagrafica().getComuneNascita() + " (" + firmatario.getAnagrafica().getProvinciaNascita() + ")");
				moduloDto.setDataNascitaFirmatario(firmatario.getAnagrafica().getDataNascita().format(formatter));
				if (firmatario.getDomicilioFiscale() != null) {
					moduloDto.setComuneResidenzaFirmatario(firmatario.getDomicilioFiscale().getComune());
					moduloDto.setProvResidenzaFirmatario(firmatario.getDomicilioFiscale().getProvincia());
					moduloDto.setIndirizzoResidenzaFirmatario(firmatario.getDomicilioFiscale().getVia() + ", " + firmatario.getDomicilioFiscale().getCivico());
					if (StringUtils.isBlank(firmatario.getDomicilioFiscale().getVia()) || StringUtils.isBlank(firmatario.getDomicilioFiscale().getCivico())) {
						moduloDto.setIndirizzoResidenzaFirmatario(firmatario.getDomicilioFiscale().getDenominazioneEstesa());
					}
				}
			}
		}
		if (personaModel instanceof PersonaFisicaModel) {
			PersonaFisicaModel pf = (PersonaFisicaModel)personaModel;
			PersonaFisicaDto firmatario = personaService.getPersonaFisica(pf.getCodiceFiscale(), 0);
			IndirizzoModel domicilio = pf.getDomicilioFiscale();
			if (domicilio != null) {
				moduloDto.setComuneSedeAzienda(domicilio.getComune());
				moduloDto.setProvSedeAzienda(domicilio.getProvincia());
				moduloDto.setIndirizzoSedeAzienda(domicilio.getVia() + ", " + domicilio.getNumeroCivico());
				if (StringUtils.isBlank(domicilio.getVia()) || StringUtils.isBlank(domicilio.getNumeroCivico())) {
					moduloDto.setIndirizzoSedeAzienda(domicilio.getDescrizioneEstesa());	
				}
			}
			moduloDto.setPecAzienda(pf.getPec());
			// titolare
			if (firmatario != null) {
				moduloDto.setCodiceFiscaleFirmatario(firmatario.getCodiceFiscale());
				moduloDto.setNomeFirmatario(firmatario.getAnagrafica().getNome() + " " + firmatario.getAnagrafica().getCognome());
				moduloDto.setComuneNascitaFirmatario(firmatario.getAnagrafica().getComuneNascita() + " (" + firmatario.getAnagrafica().getProvinciaNascita() + ")");
				moduloDto.setDataNascitaFirmatario(firmatario.getAnagrafica().getDataNascita().format(formatter));
				if (firmatario.getDomicilioFiscale() != null) {
					moduloDto.setComuneResidenzaFirmatario(firmatario.getDomicilioFiscale().getComune());
					moduloDto.setProvResidenzaFirmatario(firmatario.getDomicilioFiscale().getProvincia());
					moduloDto.setIndirizzoResidenzaFirmatario(firmatario.getDomicilioFiscale().getVia() + ", " + firmatario.getDomicilioFiscale().getCivico());
					if (StringUtils.isBlank(domicilio.getVia()) || StringUtils.isBlank(domicilio.getNumeroCivico())) {
						moduloDto.setIndirizzoResidenzaFirmatario(domicilio.getDescrizioneEstesa());	
					}
				}
			}
		}

		// detenzione
		DetenzioneModel detenzioneCorrente = detenzioneService.getDetenzioneCorrente(fascicoloModel).orElseThrow(
				() -> new NoSuchElementException(DetenzioneNotification.DETENZIONE_MANCANTE.name()));
		if (detenzioneCorrente instanceof MandatoModel) {
			MandatoModel mandato = (MandatoModel)detenzioneCorrente;
			moduloDto.setDescrizioneCaa(mandato.getSportello().getDenominazione());
			moduloDto.setSedeCaa(mandato.getSportello().getIndirizzo() + ", " + mandato.getSportello().getComune() + " (" + mandato.getSportello().getProvincia() + ")");
			moduloDto.setTelefonoCaa(mandato.getSportello().getTelefono());
			moduloDto.setEmailCaa(mandato.getSportello().getEmail());
		}
		moduloDto.setData(clock.now().format(formatter));

		return stampaComponent.stampaDOCX(
				mapper.writeValueAsString(moduloDto),
				"template/modulo_richiesta_cambio_OP.docx");
	}

	// G.De Vincentiis - FAS-TRASF-03.1
	public boolean isRichiestaTrasferimentoOpCompleta(final String cuaa)
			throws NoSuchElementException, FascicoloNonInAttesaTrasferimentoException {
		try {
			var fascicoloModel = getFascicoloModelOrThrow(cuaa, 0);
			if (fascicoloModel.getStato() != StatoFascicoloEnum.IN_ATTESA_TRASFERIMENTO) {
				throw new FascicoloNonInAttesaTrasferimentoException();
			}
			else {
				FascicoloSian fascicoloSian = anagraficaProxyClient.verificaEsistenzaFascicolo(cuaa);
				return (fascicoloSian != null && fascicoloSian.getOrganismoPagatore() == FascicoloSian.OrganismoPagatoreEnum.APPAG);
			}
		} catch (EntityNotFoundException | NoSuchElementException e) {
			throw e;
		}
	}

	public FascicoloCreationResultDto trasferisciEChiudi(final String cuaa, final LocalDate dataChiusura)
			throws FascicoloValidazioneException {
		validaOperazioneFascicolo(cuaa, FascicoloOperationEnum.TRASFERISCI_E_CHIUDI);
		if (dataChiusura.isAfter(clock.now().toLocalDate())) {
			throw new FascicoloValidazioneException(FascicoloValidazioneEnum.DATA_CHIUSURA_FUTURA);
		}
		FascicoloModel fascicoloModel = getFascicoloModelOrThrow(cuaa, 0);		
		fascicoloModel.setDtChiusuraTrasferimentoOp(dataChiusura);
		fascicoloModel.setStato(StatoFascicoloEnum.TRASFERITO);
		fascicoloModel.setOrganismoPagatore(OrganismoPagatoreEnum.ALTRO_OP);
		fascicoloDao.save(fascicoloModel);
		return new FascicoloCreationResultDto(Collections.emptyList(), 
				FascicoloMapper.fromFascicolo(fascicoloDao.save(fascicoloModel)));
	}

	// FAS-TRASF-03.2
	@Transactional
	public FascicoloCreationResultDto completaTrasferimento(final String codiceFiscale) 
			throws RestClientException, NoSuchElementException, FascicoloNonInAttesaTrasferimentoException, TrasferimentoSIANNonCompletatoException, FascicoloValidazioneException, FascicoloAllaFirmaAziendaException, FascicoloAllaFirmaCAAException {
		boolean result = isRichiestaTrasferimentoOpCompleta(codiceFiscale);
		if (!result) {
			throw new TrasferimentoSIANNonCompletatoException();
		}
		var fascicoloModel = getFascicoloModelOrThrow(codiceFiscale, 0);
		fascicoloModel.setOrganismoPagatore(it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.OrganismoPagatoreEnum.APPAG);
		var fascicoloComponent = fascicoloComponentFactory.from(codiceFiscale);
		var anomaliesList = fascicoloComponent.validaOperazioneFascicolo(FascicoloOperationEnum.AGGIORNA);
		return new FascicoloCreationResultDto(anomaliesList,FascicoloMapper.fromFascicolo(fascicoloComponent.aggiorna()));
	}

	@Transactional
	public void annullaIterValidazione(final String cuaa) throws FascicoloInvalidConditionException {
		var fascicoloModel = fascicoloDao
				.findByCuaaAndIdValidazione(cuaa, 0)
				.orElseThrow(() -> new EntityNotFoundException(StatusMessagesEnum.FASCICOLO_LOCALE_NON_ESISTENTE.name()));
		if (fascicoloModel.getStato().equals(StatoFascicoloEnum.ALLA_FIRMA_AZIENDA) || fascicoloModel.getStato().equals(StatoFascicoloEnum.ALLA_FIRMA_CAA) || fascicoloModel.getStato().equals(StatoFascicoloEnum.FIRMATO_CAA)) {
			setStato(cuaa, StatoFascicoloEnum.IN_AGGIORNAMENTO);
		} else {
			throw new FascicoloInvalidConditionException("L'attuale stato del fascicolo non permette di annullare l'iter di validazione");
		}
	}

	@Transactional
	public void salvaErede(final String cuaa, String cfErede) throws EredeFascicoloException {

		// verifico presenza fascicolo
		FascicoloModel fascicolo = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0).orElseThrow(() -> new EntityNotFoundException(StatusMessagesEnum.FASCICOLO_LOCALE_NON_ESISTENTE.name()));
		
		// verifico presenza persona
		PersonaFisicaModel personaModel = personaFisicaDao.findByCodiceFiscaleAndIdValidazione(cfErede, 0)
				.orElseGet(PersonaFisicaModel::new);

		// verifica esistenza erede in AT
		it.tndigitale.a4g.proxy.client.model.PersonaFisicaDto personaFisicaFromAnagrafeTributaria = null;
		if (PersonaSelector.isPersonaFisica(cfErede)) {
			try {
				personaFisicaFromAnagrafeTributaria = anagraficaProxyClient.getPersonaFisicaAnagrafeTributaria(cfErede);
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new EredeFascicoloException(AT_NON_DISPONIBILE);
			}
			if (personaFisicaFromAnagrafeTributaria != null && 
					!personaFisicaFromAnagrafeTributaria.isDeceduta().booleanValue()) {
				var indirizzo = new IndirizzoModel();
				indirizzo.setVia(personaFisicaFromAnagrafeTributaria.getDomicilioFiscale().getVia())
						.setCap(personaFisicaFromAnagrafeTributaria.getDomicilioFiscale().getCap())
						.setCodiceIstat(personaFisicaFromAnagrafeTributaria.getDomicilioFiscale().getCodiceIstat())
						.setDescrizioneEstesa(personaFisicaFromAnagrafeTributaria.getDomicilioFiscale().getDenominazioneEstesa())
						.setFrazione(personaFisicaFromAnagrafeTributaria.getDomicilioFiscale().getFrazione())
						.setNumeroCivico(personaFisicaFromAnagrafeTributaria.getDomicilioFiscale().getCivico())
						.setProvincia(personaFisicaFromAnagrafeTributaria.getDomicilioFiscale().getProvincia())
						.setToponimo(personaFisicaFromAnagrafeTributaria.getDomicilioFiscale().getToponimo())
						.setComune(personaFisicaFromAnagrafeTributaria.getDomicilioFiscale().getComune());
				personaModel.setNome(personaFisicaFromAnagrafeTributaria.getAnagrafica().getNome())
						.setCognome(personaFisicaFromAnagrafeTributaria.getAnagrafica().getCognome())
						.setComuneNascita(personaFisicaFromAnagrafeTributaria.getAnagrafica().getComuneNascita())
						.setDataNascita(personaFisicaFromAnagrafeTributaria.getAnagrafica().getDataNascita())
						.setDomicilioFiscale(indirizzo)
						.setDeceduto(personaFisicaFromAnagrafeTributaria.isDeceduta())
						.setDataMorte(personaFisicaFromAnagrafeTributaria.getDataMorte())
						.setProvinciaNascita(personaFisicaFromAnagrafeTributaria.getAnagrafica().getProvinciaNascita())
						.setSesso(Sesso.valueOf(personaFisicaFromAnagrafeTributaria.getAnagrafica().getSesso().name()))
						.setCodiceFiscale(personaFisicaFromAnagrafeTributaria.getCodiceFiscale());
				personaModel = personaFisicaDao.save(personaModel);
			}
			else {
				throw new EredeFascicoloException("Erede non trovato oppure deceduto");
			}
			// verifica iban con pagopa e salvataggio dati erede nella nuova tabella
			EredeModel eredeModel = null;
			Optional<EredeModel> eredeModelOpt = eredeDao.findByFascicolo_CuaaAndPersonaFisica_CodiceFiscale(cuaa, cfErede);
			if (eredeModelOpt.isPresent()) {
				eredeModel = eredeModelOpt.get();
			}
			else {
				eredeModel = new EredeModel();
			}
			eredeModel.setFirmatario(false);
			eredeModel.setPersonaFisica(personaModel);
			eredeModel.setFascicolo(fascicolo);
			eredeDao.save(eredeModel);
		}
		else {
			throw new EredeFascicoloException("Il codice fiscale dell'erede deve essere di una persona fisica");
		}
	}
	
	public List<EredeDto> getEredi(String cuaa) {
		return EredeDto.build(eredeDao.findByFascicolo_Cuaa(cuaa));
	}
	
	@Transactional
	public List<SospensioneDto> getSospensioniFascicolo(final String cuaa) {
		return SospensioneDto.build(movimentazioneDao.findByFascicoloAndTipo(cuaa, TipoMovimentazioneFascicolo.SOSPENSIONE));
	}
	
	@Transactional
	public void rimuoviErede(final String cuaa, final Long id) throws FascicoloAllaFirmaAziendaException, FascicoloAllaFirmaCAAException {
		var fascicoloModel = getFascicoloModelOrThrow(cuaa, 0);
		if (fascicoloModel.getStato().equals(StatoFascicoloEnum.ALLA_FIRMA_AZIENDA)) {
			throw new FascicoloAllaFirmaAziendaException();
		}
		if (fascicoloModel.getStato().equals(StatoFascicoloEnum.ALLA_FIRMA_CAA)) {
			throw new FascicoloAllaFirmaCAAException();
		}

		Optional<EredeModel> eredeOpt = eredeDao.findById(id);
		if (!eredeOpt.isPresent()) {
			throw new IllegalArgumentException("Erede con id= [" + id +"] non presente nel db");
		}
		EredeModel erede = eredeOpt.get();
		if (!erede.getFascicolo().getCuaa().equals(cuaa)) {
			throw new IllegalArgumentException("Erede con id= [" + id +"] non associato al cuaa=[" + cuaa + "]");
		}

		eredeDao.delete(erede);
	}
	
	public void lockFascicolo(String cuaa) throws Exception {
		lockFascicoloDao.lockFascicolo(cuaa);
	}
}
