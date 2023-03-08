package it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.*;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.*;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.StatusMessagesEnum;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaUtenteClient;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloAllaFirmaAziendaException;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloAllaFirmaCAAException;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloService;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.RicercaFascicoloService;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.mail.EmailTemplate;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.mail.EmailTemplateFactoryFactory;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.mail.EmailTemplateList;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.persona.PersonaMethodFactory;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.validation.CambioSportelloValidation;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.*;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.builder.MandatoBuilder;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.mapper.FascicoloMapper;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.mapper.MandatoMapper;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.DatiAperturaFascicoloDto;
import it.tndigitale.a4g.fascicolo.anagrafica.util.DateTimeConstants;
import it.tndigitale.a4g.framework.event.store.handler.EventBus;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicoloId;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.StatoFascicoloEnum;
import it.tndigitale.a4g.framework.support.PersonaSelector;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.utente.client.model.Persona;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MandatoService {

	private static final Logger logger = LoggerFactory.getLogger(MandatoService.class);

	@Value("#{T(java.time.LocalDate).parse('${revoca-immediata.periodo.da}' + '/' + T(java.time.LocalDate).now().getYear(), T(java.time.format.DateTimeFormatter).ofPattern('${revoca-immediata.periodo.formato}' + '/yyyy'))}")
	LocalDate periodoRevocaImmediataDa;

	@Value("#{T(java.time.LocalDate).parse('${revoca-immediata.periodo.a}' + '/' + T(java.time.LocalDate).now().getYear(), T(java.time.format.DateTimeFormatter).ofPattern('${revoca-immediata.periodo.formato}' + '/yyyy'))}")
	LocalDate periodoRevocaImmediataA;

	@Autowired
	private SportelloDao sportelloDao;
	@Autowired
	private EmailTemplateFactoryFactory emailTemplateFactoryFactory;
	@Autowired
	private CambioSportelloValidation cambioSportelloValidation;
	@Autowired
	private MandatoDao mandatoDao;
	@Autowired
	private EventBus eventBus;
	@Autowired
	private MandatoComponent mandatoComponent;
	@Autowired
	private PersonaMethodFactory personaMethodFactory;
	@Autowired
	private RicercaFascicoloService ricercaFascicoloService;
	@Autowired
	private FascicoloDao fascicoloDao;
	@Autowired
	private Clock clock;
	@Autowired
	RevocaImmediataDao revocaImmediataDao;
	@Autowired
	private PersonaFisicaDao personaFisicaDao;
	@Autowired
	private PersonaGiuridicaDao personaGiuridicaDao;
	@Autowired
	private AnagraficaUtenteClient anagraficaUtenteClient;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private DetenzioneService detenzioneService;
	@Autowired
	private FascicoloService fascicoloService;
	@Autowired
	private DetenzioneInProprioDao detenzioneInProprioDao;

	@Autowired private MandatoVerificaComponentMethodFactory mandatoComponentFactory;

	public DatiAperturaFascicoloDto mandatoAcquisizioneVerifica(
			final String codiceFiscale) throws MandatoVerificaException {
		var mandatoVerificaComponent = mandatoComponentFactory.from(codiceFiscale);
		mandatoVerificaComponent.verificaAperturaMandato();

		return mandatoVerificaComponent.getDatiPerAcquisizioneMandato();

	}

	public List<MandatoDto> getMandati(final String cuaa, final int idValidazione) throws EntityNotFoundException {
		var fascicoloModel = ricercaFascicoloService.getFascicoloModel(cuaa, idValidazione);
		Optional<MandatoModel> detenzioneCorrenteOpt = getMandatoCorrente(fascicoloModel);
		if (!detenzioneCorrenteOpt.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, RevocaImmediataMandatoNotification.MANDATO_MANCANTE.name());
		}
		MandatoModel mandatoCorrente = detenzioneCorrenteOpt.get();
		List<MandatoModel> mandatiFuturi = fascicoloModel.getDetenzioni()
				.stream()
				.map(MandatoModel.class::cast)
				.filter(mandato -> mandato.getDataInizio().compareTo(clock.today()) > 0)
				.collect(Collectors.toList());
		List<MandatoModel> mandatiModel = new ArrayList<>();
		mandatiModel.add(mandatoCorrente);
		mandatiModel.addAll(mandatiFuturi);
		List<MandatoDto> mandatiDto = new ArrayList<>();
		mandatiModel.forEach(mandato -> mandatiDto.add(MandatoBuilder.from(mandato)));
		return mandatiDto;
	}

	public void associaMandatoANuovoFascicolo(ApriFascicoloDto mandatoDto) throws Exception {
		mandatoComponent.associa(mandatoDto);
		publish(mandatoDto);
	}

	public DatiAperturaFascicoloDto verificaRevocaOrdinaria(final String cuaa) {
		mandatoComponent.verificaRevocaOrdinaria(cuaa);
		return getFascicoloPerMandato(cuaa, 0);
	}

	public DatiAperturaFascicoloDto getFascicoloPerMandato(final String cuaa, final Integer idValidazione) {
		return personaMethodFactory.from(cuaa).getDatiAnagraficiSintesi(cuaa, idValidazione);
	}

	public void cambioSportello(final String cuaa, final CambioSportelloPatch cambioSportello) throws Exception {
		var fascicoloModel = ricercaFascicoloService.getFascicoloModel(cuaa, 0);
		if (fascicoloModel.getStato().equals(StatoFascicoloEnum.ALLA_FIRMA_AZIENDA)) {
			throw new FascicoloAllaFirmaAziendaException();
		}
		if (fascicoloModel.getStato().equals(StatoFascicoloEnum.ALLA_FIRMA_CAA)) {
			throw new FascicoloAllaFirmaCAAException();
		}
		Optional<MandatoModel> detenzioneCorrenteOpt = getMandatoCorrente(fascicoloModel);
		if (!detenzioneCorrenteOpt.isPresent()) {
			return; 
		}
		MandatoModel detenzioneCorrente = detenzioneCorrenteOpt.get();
		var sportelloModel = detenzioneCorrente.getSportello();
		SportelloModel newSportello = cambioSportelloValidation.validaCambioSportello(
				cambioSportello, 
				sportelloModel.getCentroAssistenzaAgricola().getCodiceFiscale(),
				sportelloModel);
		//copio campi nel nuovo mandato
		MandatoModel detenzioneModelNew = MandatoMapper.copyMandato(newSportello, detenzioneCorrente); 
		detenzioneCorrente.setDataFine(cambioSportello.getDataCambio().minusDays(1));
		detenzioneModelNew.setDataInizio(cambioSportello.getDataCambio());
		//TASK - Refactoring Cambio sportello: necessario verificare se c'è una revoca ordinaria in corso al fine di modificare opportunamente le date.
		Optional<MandatoModel> mandatoDiRevocaOrdinaria = mandatoComponent.getMandatoDiRevocaOrdinaria(fascicoloModel);
		if (mandatoDiRevocaOrdinaria.isPresent()) {
			var trentunoDicembre = LocalDate.of(clock.today().getYear(), Month.DECEMBER, 31);
			if (cambioSportello.getDataCambio().compareTo(trentunoDicembre) >= 0) {
				throw new IllegalArgumentException("Non è possibile cambiare sportello: Il Mandato è stato revocato.");
			}
			detenzioneModelNew.setDataFine(trentunoDicembre);
		} else {
			detenzioneModelNew.setDataFine(null);
		}
		Optional<MandatoModel> sportelloSuccessivoAlCorrente = mandatoComponent.getSportelloSuccessivoAlCorrente(fascicoloModel);
		if (sportelloSuccessivoAlCorrente.isPresent()) {
			mandatoDao.delete(sportelloSuccessivoAlCorrente.get());
		}
		mandatoDao.save(detenzioneCorrente);
		mandatoDao.save(detenzioneModelNew);

		//invio mail
		var  emailTemplateByName = emailTemplateFactoryFactory.getEmailTemplateByName(EmailTemplate.getNomeQualificatore(EmailTemplateList.CAMBIO_SPORTELLO.name()));
		String[] mailArgs =  {
				fascicoloModel.getCuaa(), //azienda CUAA - Denominazione
				sportelloModel.getDenominazione(), //CAA Denominazione
				newSportello.getDenominazione(), //new Sportello CAA  - Località 
				cambioSportello.getDataCambio().format(DateTimeFormatter.ofPattern(DateTimeConstants.DATE_PATTERN)),
				cambioSportello.getMotivazione() //Motivazione cambio 
		};
		emailTemplateByName.sendMail(mailArgs);
		logger.info("Cambio sportello effettuato: fascicolo {} - dallo sportello {} allo sportello {} a partire da {}", cuaa, sportelloModel.getDenominazione(), cambioSportello.getIdNuovoSportello() , cambioSportello.getDataCambio().format(DateTimeFormatter.ofPattern(DateTimeConstants.DATE_PATTERN)));
	}

	public Resource getPdfAllegatoContrattoMandato(final EntitaDominioFascicoloId fascicoloId, final EntitaDominioFascicoloId mandatoId) throws Exception {
		MandatoModel mandato = mandatoDao.findById(mandatoId).orElseThrow(() -> new EntityNotFoundException("Mandato non trovato: " + mandatoId));
		return new ByteArrayResource(mandato.getContratto());
	}

	@Transactional
	public Long revocaOrdinariaMandato(ApriFascicoloDto mandatoDto) throws Exception {
		MandatoModel mandatoNew = mandatoComponent.revocaOrdinaria(mandatoDto);
		publish(mandatoDto);
		return mandatoNew.getId();
	}

	public boolean utenteCorrentePuoInserireRevocaImmediata(final String cuaa) {
		//		controllo date
		LocalDate today = clock.today();
		if (today.isBefore(periodoRevocaImmediataDa) || today.isAfter(periodoRevocaImmediataA)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, RevocaImmediataMandatoNotification.DATA_NON_VALIDA.name());
		}
//		sono sicuro della presenza in virtu' del controllo controlloPermessiUtenteCorrenteFascicolo()
		Optional<FascicoloModel> optFascicolo = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0);
		var fascicoloModel = optFascicolo.orElseThrow(
				() -> new EntityNotFoundException(StatusMessagesEnum.FASCICOLO_LOCALE_NON_ESISTENTE.name()));
		//		controlli sullo stato della revoca immediata e mandato attuale:
		//		1. se non esiste un mandato attivo ritorno false
		//		2. se per quel cuaa non esiste nessuna richiesta di revoca immediata od esistono solamente revoche rifiutate allora ritorno true
		Optional<MandatoModel> detenzioneCorrenteOpt = getMandatoCorrente(fascicoloModel);
		if (!detenzioneCorrenteOpt.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, RevocaImmediataMandatoNotification.MANDATO_MANCANTE.name());
		}
		MandatoModel mandatoCorrente = detenzioneCorrenteOpt.get();
		List<RevocaImmediataModel> revocaImmediataList = revocaImmediataDao.findByMandato(mandatoCorrente);
		if (revocaImmediataList.stream().anyMatch(revocaImmediata -> revocaImmediata.getStato().equals(StatoRevocaImmediata.DA_VALUTARE))) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, RevocaImmediataMandatoNotification.RICHIESTA_REVOCA_IMMEDIATA_PRESENTE_E_DA_VALUTARE.name());
		}
		return true;
	}

	@Transactional
	public Long richiestaRevocaImmediata(RichiestaRevocaImmediataDto richiestaRevocaImmediataDto) throws Exception {
		utenteCorrentePuoInserireRevocaImmediata(richiestaRevocaImmediataDto.getCodiceFiscale());

		var richiestaRevocaImmediataModelNew = mandatoComponent.salvaRichiestaRevocaImmediata(richiestaRevocaImmediataDto);
		//		invio informazioni per protocollazione asincrona
		richiestaRevocaImmediataDto.setId(richiestaRevocaImmediataModelNew.getId());
		publishRichiestaRevocaImmediata(richiestaRevocaImmediataDto);
		return richiestaRevocaImmediataModelNew.getId();
	}

	private void publishRichiestaRevocaImmediata(RichiestaRevocaImmediataDto richiestaRevocaImmediataDto) {
		var event = new RevocaImmediataMandatoEvent(richiestaRevocaImmediataDto);
		event.setData(richiestaRevocaImmediataDto);
		eventBus.publishEvent(event);
	}

	private void publish(ApriFascicoloDto mandatoDto) {
		var event = new StipulaNuovoMandatatoEvent(mandatoDto);
		event.setData(mandatoDto);
		eventBus.publishEvent(event);
	}

	private FascicoloModel getFascicoloModel (String cuaa) {
		Optional<FascicoloModel> fascicoloModelOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0);
		if (!fascicoloModelOpt.isPresent()) {
			var err = String.format("Fascicolo CUAA %s non trovato" , cuaa);
			logger.error(err);
			throw new EntityNotFoundException(err);
		}
		return fascicoloModelOpt.get();
	}

	@Transactional
	public boolean verificaPresenzaRevocaOrdinaria(String cuaa) {
		var fascicoloModel = getFascicoloModel(cuaa);
		Optional<MandatoModel> mandatoDiRevocaOrdinaria = mandatoComponent.getMandatoDiRevocaOrdinaria(fascicoloModel);
		return mandatoDiRevocaOrdinaria.isPresent();
	}


	public void notificaMailTitolareRappresentanteLegale(RichiestaRevocaImmediataDto richiestaRevocaImmediataDto, boolean isAccettata) throws NoSuchElementException {
		//		denominazione azienda: in base alla lunghezza reperisco persona fisica o giuridica
		String titolarePec = null;
		if (PersonaSelector.isPersonaFisica(richiestaRevocaImmediataDto.getCodiceFiscale())) {
			PersonaFisicaModel persona = personaFisicaDao.findByCodiceFiscaleAndIdValidazione(richiestaRevocaImmediataDto.getCodiceFiscale(), 0).orElseThrow();
			titolarePec = persona.getPec();
		} else {
			Optional<PersonaGiuridicaModel> personaGiuridicaModelOpt = personaGiuridicaDao.findByCodiceFiscaleAndIdValidazione(richiestaRevocaImmediataDto.getCodiceFiscale(), 0);
			if (!personaGiuridicaModelOpt.isPresent()) {
				throw new IllegalArgumentException("Codice Fiscale di persona giuridica inserito non valido");
			}
			var personaGiuridicaModel = personaGiuridicaModelOpt.get();
			titolarePec = personaGiuridicaModel.getPec();
		}
		//		denominazione caa
		Optional<SportelloModel> sportelloModelOptional = sportelloDao.findByIdentificativo(richiestaRevocaImmediataDto.getIdentificativoSportello());
		if(!sportelloModelOptional.isPresent()) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, RevocaImmediataMandatoNotification.SPORTELLO_NON_TROVATO.name());
		}
		var sportelloModel = sportelloModelOptional.get();
		//		 Invio mail al titolare
		if (titolarePec != null && titolarePec.trim().length() > 0) {
			try {
				String templateName;
				if (isAccettata) {
					templateName = EmailTemplateList.NOTIFICA_ACCETTA_REVOCA_IMMEDIATA_AL_TITOLARE.name();
				} else {
					templateName = EmailTemplateList.NOTIFICA_RIFIUTA_REVOCA_IMMEDIATA_AL_TITOLARE.name();
				}
				var emailTemplateByName = emailTemplateFactoryFactory.getEmailTemplateByName(
						EmailTemplate.getNomeQualificatore(templateName));
				String[] mailArgs = {
						sportelloModel.getCentroAssistenzaAgricola().getDenominazione(), //CAA Denominazione 
						richiestaRevocaImmediataDto.getDataValutazione().format(DateTimeFormatter.ofPattern(DateTimeConstants.DATE_PATTERN)),
						sportelloModel.getCentroAssistenzaAgricola().getDenominazione(), //CAA Denominazione
				};
				emailTemplateByName.sendMail(titolarePec, mailArgs);
			} catch (Exception e) {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, RevocaImmediataMandatoNotification.NOTIFICA_TITOLARE_FALLITA.name());
			}
		} else {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, RevocaImmediataMandatoNotification.TITOLARE_NO_PEC.name());
		}
	}

	public void notificaMailAppag(RichiestaRevocaImmediataDto richiestaRevocaImmediataDto, boolean isAccettata) throws NoSuchElementException {
		//		denominazione azienda: in base alla lunghezza reperisco persona fisica o giuridica
		String denominazioneAzienda = null;
		if (PersonaSelector.isPersonaFisica(richiestaRevocaImmediataDto.getCodiceFiscale())) {
			PersonaFisicaModel persona = personaFisicaDao.findByCodiceFiscaleAndIdValidazione(richiestaRevocaImmediataDto.getCodiceFiscale(), 0).orElseThrow();
			denominazioneAzienda = persona.getImpresaIndividuale().getDenominazione();
			if (persona.getImpresaIndividuale().getDenominazione() == null) {
				denominazioneAzienda = persona.getNome() + " " + persona.getCognome();
			}
		} else {
			Optional<PersonaGiuridicaModel> personaGiuridicaModelOpt = personaGiuridicaDao.findByCodiceFiscaleAndIdValidazione(richiestaRevocaImmediataDto.getCodiceFiscale(), 0);
			if (!personaGiuridicaModelOpt.isPresent()) {
				throw new IllegalArgumentException("Codice Fiscale di persona giuridica inserito non valido");
			}
			var personaGiuridicaModel = personaGiuridicaModelOpt.get();
			denominazioneAzienda =  personaGiuridicaModel.getDenominazione();
		}
		//		denominazione caa
		Optional<SportelloModel> sportelloModelOptional = sportelloDao.findByIdentificativo(richiestaRevocaImmediataDto.getIdentificativoSportello());
		if (!sportelloModelOptional.isPresent()) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, RevocaImmediataMandatoNotification.SPORTELLO_NON_TROVATO.name());
		}
		var sportelloModel = sportelloModelOptional.get();
		//invio mail appag
		try {
			String templateName;
			if (isAccettata) {
				templateName = EmailTemplateList.NOTIFICA_ACCETTA_REVOCA_IMMEDIATA_A_APPAG.name();
			} else {
				templateName = EmailTemplateList.NOTIFICA_RIFIUTA_REVOCA_IMMEDIATA_A_APPAG.name();
			}			
			var emailTemplateByName = emailTemplateFactoryFactory.getEmailTemplateByName(
					EmailTemplate.getNomeQualificatore(templateName));
			String[] mailArgs =  {
					sportelloModel.getCentroAssistenzaAgricola().getDenominazione(), //CAA Denominazione 
					denominazioneAzienda, //azienda Denominazione
					richiestaRevocaImmediataDto.getDataValutazione().format(DateTimeFormatter.ofPattern(DateTimeConstants.DATE_PATTERN)),
					richiestaRevocaImmediataDto.getMotivazioneRifiuto(), // motivazione rifiuto
					sportelloModel.getCentroAssistenzaAgricola().getDenominazione(), //CAA Denominazione
			};
			emailTemplateByName.sendMail(mailArgs);

		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, RevocaImmediataMandatoNotification.NOTIFICA_APPAG_FALLITA.name());
		}
	}

	public void notificaMailCaaRichiestaValidazioneRespinta(String cuaa){
		try {
			var fascicoloModel = fascicoloDao
					.findByCuaaAndIdValidazione(cuaa, 0)
					.orElseThrow(() -> new EntityNotFoundException(StatusMessagesEnum.FASCICOLO_LOCALE_NON_ESISTENTE.name()));
			String codiceFiscale = fascicoloService.getCodiceFiscaleRappresentanteLegale(fascicoloModel);
			var p = new Persona();
			p.setCodiceFiscale(codiceFiscale);
			List<Persona> persone = anagraficaUtenteClient.ricercaPerCodiceFiscale(objectMapper.writeValueAsString(p));
			if(persone == null || persone.isEmpty() || persone.size() > 1) {
				throw new IllegalArgumentException("per cuaa=[" + cuaa + "] il Rappresentante legale =[" + codiceFiscale + "] non e' censito nella base dati A4g" );
			}

			Optional<MandatoModel> detenzioneCorrenteOpt = getMandatoCorrente(fascicoloModel);
			if (!detenzioneCorrenteOpt.isPresent()) {
				return; 
			}
			MandatoModel detenzioneCorrente = detenzioneCorrenteOpt.get();
			var sportelloModel = detenzioneCorrente.getSportello();

			//		invio mail caa dello sportello mandatario di cui fa parte

			String caaEmail = sportelloModel.getCentroAssistenzaAgricola().getEmail();
			var emailTemplateByName = emailTemplateFactoryFactory.getEmailTemplateByName(
					EmailTemplate.getNomeQualificatore(EmailTemplateList.NOTIFICA_RESPINTA_VALIDAZIONE_CAA.name()));

			String[] oggettoArgs =  {
					fascicoloModel.getDenominazione()
			};

			String[] mailArgs =  {
					persone.get(0).getNome(),
					persone.get(0).getCognome(),
					fascicoloModel.getDenominazione(),
					cuaa
			};

			emailTemplateByName.sendMail(caaEmail, oggettoArgs, mailArgs); 

		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "NOTIFICA_MAIL_CAA_RICHIESTA_VALIDAZIONE_RESPINTA_FALLITA");
		}
	}
	
	@Transactional
	public void notificaMailCaaRichiestaValidazioneAccettata(String cuaa){
		try {
			var fascicoloModel = fascicoloDao
					.findByCuaaAndIdValidazione(cuaa, 0)
					.orElseThrow(() -> new EntityNotFoundException(StatusMessagesEnum.FASCICOLO_LOCALE_NON_ESISTENTE.name()));
			String codiceFiscale = fascicoloService.getCodiceFiscaleRappresentanteLegale(fascicoloModel);
			Persona p = new Persona();
			p.setCodiceFiscale(codiceFiscale);
			List<Persona> persone = anagraficaUtenteClient.ricercaPerCodiceFiscale(objectMapper.writeValueAsString(p));
			if(persone == null || persone.isEmpty() || persone.size() > 1) {
				throw new IllegalArgumentException("per cuaa=[" + cuaa + "] il Rappresentante legale =[" + codiceFiscale + "] non e' censito nella base dati A4g" );
			}

			Optional<MandatoModel> detenzioneCorrenteOpt = getMandatoCorrente(fascicoloModel);
			if (!detenzioneCorrenteOpt.isPresent()) {
				return; 
			}
			MandatoModel detenzioneCorrente = detenzioneCorrenteOpt.get();
			var sportelloModel = detenzioneCorrente.getSportello();

			//		invio mail caa dello sportello mandatario di cui fa parte
			String caaEmail = sportelloModel.getCentroAssistenzaAgricola().getEmail();
			var emailTemplateByName = emailTemplateFactoryFactory.getEmailTemplateByName(
					EmailTemplate.getNomeQualificatore(EmailTemplateList.NOTIFICA_VALIDAZIONE_APPROVATA_CAA.name()));

			String[] oggettoArgs =  {
					fascicoloModel.getDenominazione()
			};

			String[] mailArgs =  {
					persone.get(0).getNome(),
					persone.get(0).getCognome(),
					fascicoloModel.getDenominazione(),
					cuaa
			};

			emailTemplateByName.sendMail(caaEmail, oggettoArgs, mailArgs); 

		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "NOTIFICA_MAIL_CAA_VALIDAZIONE_ACCETTATA_FALLITA");
		}
	}

	@Transactional
	public RichiestaRevocaImmediataDto valutaRichiestaRevocaImmediata(
			final RichiestaRevocaImmediataDto richiestaRevocaImmediataDto,
			boolean isAccettata) {
		var fascicoloModel = getFascicoloModel(richiestaRevocaImmediataDto.getCodiceFiscale());
		Optional<MandatoModel> detenzioneCorrenteOpt = getMandatoCorrente(fascicoloModel);
		if (!detenzioneCorrenteOpt.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, RevocaImmediataMandatoNotification.MANDATO_MANCANTE.name());
		}
		MandatoModel mandatoCorrente = detenzioneCorrenteOpt.get();
		List<RevocaImmediataModel> revocaImmediataList = revocaImmediataDao.findByMandato(mandatoCorrente);
		if (revocaImmediataList.isEmpty()) {
			throw new ResponseStatusException(
					HttpStatus.INTERNAL_SERVER_ERROR,
					RevocaImmediataMandatoNotification.RICHIESTA_REVOCA_IMMEDIATA_ASSENTE.name());
		}
		Optional<RevocaImmediataModel> revocaImmediataOptional = revocaImmediataList.stream().filter(
				revocaImmediata -> revocaImmediata.getStato().equals(StatoRevocaImmediata.DA_VALUTARE)).findFirst();
		if (!revocaImmediataOptional.isPresent()) {
			throw new ResponseStatusException(
					HttpStatus.INTERNAL_SERVER_ERROR,
					RevocaImmediataMandatoNotification.NESSUNA_RICHIESTA_REVOCA_IMMEDIATA_DA_VALUTARE.name());
		}
		var revocaImmediataModel = revocaImmediataOptional.get();
		revocaImmediataModel.setMotivazioneRifiuto(richiestaRevocaImmediataDto.getMotivazioneRifiuto());
		if (isAccettata) {
			revocaImmediataModel.setStato(StatoRevocaImmediata.ACCETTATA);
			mandatoCorrente.setDataFine(clock.today());
		} else {
			revocaImmediataModel.setStato(StatoRevocaImmediata.RIFIUTATA);			
		}
		revocaImmediataModel.setDataValutazione(clock.today());
		richiestaRevocaImmediataDto.setIdentificativoSportello(revocaImmediataModel.getMandato().getSportello().getIdentificativo());
		richiestaRevocaImmediataDto.setDataValutazione(revocaImmediataModel.getDataValutazione());
		return richiestaRevocaImmediataDto;
	}
	
	/**
	 * STORICIZZAZIONE
	 * @param fascicoloLive
	 * @param fascicoloValidato
	 */
	@Transactional
	public List<DetenzioneModel> validazioneDetenzioneAutonoma(FascicoloModel fascicoloLive, FascicoloModel fascicoloValidato, Integer idValidazione) {
		List<DetenzioneModel> result = new ArrayList<>(); 
		if (fascicoloLive.getDetenzioni() != null && !fascicoloLive.getDetenzioni().isEmpty()) {
			fascicoloLive.getDetenzioni().forEach(detenzioneLive -> {
				var idCompostoLive = new EntitaDominioFascicoloId(detenzioneLive.getId(), detenzioneLive.getIdValidazione());
				Optional<DetenzioneInProprioModel> detenzioneInProprioLiveOpt = detenzioneInProprioDao.findById(idCompostoLive);
				if (detenzioneInProprioLiveOpt.isPresent()) {
					var detenzioneInProprioLive = detenzioneInProprioLiveOpt.get();
					var detenzioneInProprioValidato = new DetenzioneInProprioModel();
					BeanUtils.copyProperties(detenzioneInProprioLive, detenzioneInProprioValidato, "idValidazione", "fascicolo");
//					idValidazione va ricavato solo una volta per l'intera lista
					detenzioneInProprioValidato.setIdValidazione(idValidazione);
					detenzioneInProprioValidato.setFascicolo(fascicoloValidato);
					result.add((DetenzioneModel)detenzioneInProprioDao.save(detenzioneInProprioValidato));
				}
			});
		}
		return result;
	}
	
	/**
	 * STORICIZZAZIONE
	 * @param fascicoloValidato
	 * @param fascicoloLive
	 */
	@Transactional
	public List<DetenzioneModel> validazioneMandato(FascicoloModel fascicoloLive, FascicoloModel fascicoloValidato, Integer idValidazione) {
		List<DetenzioneModel> result = new ArrayList<>(); 
		if (fascicoloLive.getDetenzioni() != null && !fascicoloLive.getDetenzioni().isEmpty()) {
			fascicoloLive.getDetenzioni().forEach(detenzioneLive -> {
				var idCompostoLive = new EntitaDominioFascicoloId(detenzioneLive.getId(), detenzioneLive.getIdValidazione());
				Optional<MandatoModel> mandatoLiveOpt = mandatoDao.findById(idCompostoLive);
				if (mandatoLiveOpt.isPresent()) {
					var mandatoLive = mandatoLiveOpt.get();
					var mandatoValidato = new MandatoModel();
					BeanUtils.copyProperties(mandatoLive, mandatoValidato, "idValidazione", "fascicolo");
//					idValidazione va ricavato solo una volta per l'intera lista
					mandatoValidato.setIdValidazione(idValidazione);
					mandatoValidato.setFascicolo(fascicoloValidato);
					result.add((DetenzioneModel)mandatoDao.save(mandatoValidato));
				}
			});
		}
		return result;
	}
	
	/**
	 * Gestione ad hoc per la storicizzazione delle revoche immediate.
	 * Esse non vanno storicizzate, bensi' si deve aggiornare la FK da revoca immediata al Mandato storicizzato(e viceversa)
	 * @param fascicoloLive
	 * @param idValidazione
	 */
	@Transactional
	public void validazioneRevocheImmediate(FascicoloModel fascicoloLive, Integer idValidazione) {
		if (fascicoloLive.getDetenzioni() != null && !fascicoloLive.getDetenzioni().isEmpty()) {
			fascicoloLive.getDetenzioni().forEach(detenzioneLive -> {
				var idCompostoLive = new EntitaDominioFascicoloId(detenzioneLive.getId(), detenzioneLive.getIdValidazione());
				Optional<MandatoModel> mandatoLiveOpt = mandatoDao.findById(idCompostoLive);
				if (mandatoLiveOpt.isPresent()) {
					var mandatoLive = mandatoLiveOpt.get();
					var idCompostoValidato = new EntitaDominioFascicoloId(detenzioneLive.getId(), idValidazione);
					var mandatoValidatoOpt =  mandatoDao.findById(idCompostoValidato);
					if (mandatoValidatoOpt.isPresent()) {
						var mandatoValidato = mandatoValidatoOpt.get();
//					    quando il mandato è storicizzato viene aggiornata anche la foreign key in A4GT_REVOCA_IMMEDIATA
						List<RevocaImmediataModel> revoche = mandatoLive.getRevocheImmediate();
						if (!revoche.isEmpty()) {
							mandatoLive.setRevocheImmediate(null);
							mandatoDao.save(mandatoLive);
							List<RevocaImmediataModel> revocheUpdated = new ArrayList<>();
							revoche.forEach(revoca -> {
								revoca.setMandato(mandatoValidato);
								revocheUpdated.add(revocaImmediataDao.save(revoca));
							});
							mandatoValidato.ignoreValidazioneCheck();		
							mandatoValidato.setRevocheImmediate(revocheUpdated);
							mandatoDao.save(mandatoValidato);
						}
					}
				}
			});
		}
	}
	
	public Optional<MandatoModel> getMandatoCorrente(final FascicoloModel fascicoloModel) {
		Optional<DetenzioneModel> optDetenzioneCorrente = detenzioneService.getDetenzioneCorrente(fascicoloModel);
		if (optDetenzioneCorrente.isPresent() && optDetenzioneCorrente.get() instanceof MandatoModel) {
			return Optional.of((MandatoModel)optDetenzioneCorrente.get());
		} else {
			return Optional.empty();
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public FascicoloCreationResultDto acquisisciMandato(ApriFascicoloDto mandatoDto) throws Exception {
		logger.debug("Cerco di acquisire il mandato per {} associandolo allo sportello {}",
				mandatoDto.getCodiceFiscale(), mandatoDto.getIdentificativoSportello());

		var mandatoVerificaComponent = mandatoComponentFactory.from(mandatoDto.getCodiceFiscale());
		var anomaliesList = mandatoVerificaComponent.verificaAperturaMandato();

//		reperire fascicolo esistente
		FascicoloModel foundFascicolo = fascicoloDao.findByCuaaAndIdValidazione(mandatoDto.getCodiceFiscale(), 0).orElseThrow(() -> new EntityNotFoundException(StatusMessagesEnum.FASCICOLO_LOCALE_NON_ESISTENTE.name()));
		mandatoDto.setCodiceFiscale(foundFascicolo.getCuaa());
		associaMandatoANuovoFascicolo(mandatoDto);
		foundFascicolo.setStato(StatoFascicoloEnum.IN_AGGIORNAMENTO);
		return new FascicoloCreationResultDto(anomaliesList, FascicoloMapper.fromFascicolo(foundFascicolo));
	}
}
