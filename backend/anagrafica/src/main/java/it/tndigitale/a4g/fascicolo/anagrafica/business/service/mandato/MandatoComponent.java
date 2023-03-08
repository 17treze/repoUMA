package it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.DetenzioneModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.MandatoModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.RevocaImmediataModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.SportelloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.StatoRevocaImmediata;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.FascicoloDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.MandatoDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.RevocaImmediataDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.SportelloDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.CaaService;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.StatusMessagesEnum;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.mail.EmailTemplate;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.mail.EmailTemplateFactoryFactory;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.mail.EmailTemplateList;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.utente.AbilitazioniComponent;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.ApriFascicoloDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.RichiestaRevocaImmediataDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.caa.CentroAssistenzaAgricolaDto;
import it.tndigitale.a4g.fascicolo.anagrafica.util.DateTimeConstants;
import it.tndigitale.a4g.framework.client.custom.MetadatiDocumentoFirmatoDto;
import it.tndigitale.a4g.framework.client.custom.VerificaFirmaClient;
import it.tndigitale.a4g.framework.support.CustomCollectors;
import it.tndigitale.a4g.framework.time.Clock;

@Component
class MandatoComponent {

	private static final Logger logger = LoggerFactory.getLogger(MandatoComponent.class);

	@Autowired
	private VerificaFirmaClient verificaFirmaClient;
	@Autowired
	private MandatoDao mandatoDao;
	@Autowired
	private FascicoloDao fascicoloDao;
	@Autowired
	private SportelloDao sportelloDao;
	@Autowired
	RevocaImmediataDao revocaImmediataDao;
	@Autowired
	private CaaService caaService;
	@Autowired
	private Clock clock;
	@Autowired
	private EmailTemplateFactoryFactory emailTemplateFactoryFactory;
	@Autowired
	private AbilitazioniComponent abilitazioniComponent;
	
	@Autowired private MandatoService mandatoService;

	void associa(ApriFascicoloDto mandatoDto) throws Exception {
		FascicoloModel fascicolo = getFascicoloOrThrowException(mandatoDto.getCodiceFiscale());
		if (fascicolo.getDetenzioni() != null) {
			List<DetenzioneModel> detenzioni = fascicolo.getDetenzioni().stream().filter(z -> z.getDataFine() == null)
					.collect(Collectors.toList());
			if (detenzioni != null && !detenzioni.isEmpty()) {
				String err = String.format("Il fascicolo %s ha già delle detenzioni", mandatoDto.getCodiceFiscale());
				logger.error(err);
				throw new IllegalArgumentException(err);			
			}
		}

//		Cercare la detenzione con la data_fine piu' recente ed impostare data_inizio nuovo mandato a data_fine + 1.
//		L'impostazione di data inizio deve avvenire se esiste almeno una detenzione chiusa e non sia impostata la
//		dataInizioMandato (valorizzato in caso di migrazione da AGS) del corrente mandato
		if (fascicolo.getDetenzioni() != null && !fascicolo.getDetenzioni().isEmpty()) {
			List<DetenzioneModel> detenzioneModelList = fascicolo.getDetenzioni().stream().sorted(Comparator.comparing(DetenzioneModel::getDataFine).reversed()).collect(Collectors.toList());
			if (detenzioneModelList != null && !detenzioneModelList.isEmpty() && mandatoDto.getDataInizioMandato() == null) {
				mandatoDto.setDataInizioMandato(detenzioneModelList.get(0).getDataFine().plusDays(1L));
			}
		}

		MetadatiDocumentoFirmatoDto verificaFirma = getFirmaContratto(mandatoDto);
		LocalDate dataInizioMandato = verificaFirma.getDataFirma();
		LocalDate dataFineMandato = null;
//		la condizione sotto e' vera in caso di migrazione fascicolo da AGS
		if (mandatoDto.getDataInizioMandato() != null) {
			dataInizioMandato = mandatoDto.getDataInizioMandato();
			dataFineMandato = mandatoDto.getDataFineMandato();
		}
		saveNewMandato(fascicolo, mandatoDto, verificaFirma.getDataFirma(), dataInizioMandato, dataFineMandato);
	}


	MandatoModel revocaOrdinaria(ApriFascicoloDto mandatoDto) throws Exception {

		FascicoloModel fascicolo = getFascicoloOrThrowException(mandatoDto.getCodiceFiscale());

		final LocalDate primoGennaioAnnoSuccessivo = LocalDate.of(clock.today().plusYears(1).getYear(), Month.JANUARY, 1);
		final LocalDate trentunoDicembre = LocalDate.of(clock.today().getYear(),Month.DECEMBER, 31);
		List<MandatoModel> mandati = mandatoDao.findByFascicolo(fascicolo);

		// Verifica finestra temporale 
		checkFinestraTemporaleRevocaOrdinaria();

		// Verifica consistenza revoca
		checkCaaConnessoDiversoCaaRevocato(fascicolo);

		// Verifica firma contratto caricato
		MetadatiDocumentoFirmatoDto verificaFirma = getFirmaContratto(mandatoDto);

		// Cancello tutti i mandati programmati per l'anno successivo
		cancellaMandatiAnnoSuccessivo(MandatoHelper.getMandatiStartingFromLocalDate(mandati, primoGennaioAnnoSuccessivo));

		// Aggiorna l'ultimo mandato dell'anno corrente
		MandatoModel mandatoRevocato = aggiornaUltimoMandatoAnnoCorrente(MandatoHelper.getLastMandatoStartingStrictlyBeforeLocalDate(mandati, primoGennaioAnnoSuccessivo), trentunoDicembre);

		// Crea nuovo mandato con decorrenza dal 1° gennaio anno successivo.
		MandatoModel mandatoNew = saveNewMandato(fascicolo, mandatoDto, verificaFirma.getDataFirma(), primoGennaioAnnoSuccessivo, null);

		// Invio mail
		SportelloModel sportelloCaaRevocato = mandatoRevocato.getSportello();
		String emailCaa = sportelloCaaRevocato.getCentroAssistenzaAgricola().getEmail();
		String[] mailArgs = {
				primoGennaioAnnoSuccessivo.format(DateTimeFormatter.ofPattern(DateTimeConstants.DATE_PATTERN)),
				fascicolo.getCuaa(),
				fascicolo.getDenominazione(),
				sportelloCaaRevocato.getCentroAssistenzaAgricola().getDenominazione()
		};

		// Invio mail a APPAG
		EmailTemplate emailTemplateByName = emailTemplateFactoryFactory.getEmailTemplateByName(EmailTemplate.getNomeQualificatore(EmailTemplateList.REVOCA_ORDINARIA_MANDATO.name()));
		emailTemplateByName.sendMail(mailArgs);

		// Invio mail al CAA revocato
		if (emailCaa != null) {
			emailTemplateByName.sendMail(sportelloCaaRevocato.getCentroAssistenzaAgricola().getEmail(), mailArgs);
		} else {
			logger.warn(String.format("Non è possibile inviare una mail al %s perchè ne è sprovvisto", sportelloCaaRevocato.getCentroAssistenzaAgricola().getDenominazione()));
		}

		return mandatoNew;
	}

	RevocaImmediataModel salvaRichiestaRevocaImmediata(RichiestaRevocaImmediataDto richiestaRevocaImmediataDto) throws Exception {
		RevocaImmediataModel revocaImmediata = new RevocaImmediataModel();
		revocaImmediata.setCodiceFiscale(richiestaRevocaImmediataDto.getCodiceFiscaleRappresentante());
		revocaImmediata.setDataSottoscrizione(LocalDate.now()).setStato(StatoRevocaImmediata.DA_VALUTARE);
		revocaImmediata.setCausaRichiesta(richiestaRevocaImmediataDto.getCausaRichiesta());
		
//		3. reperire il mandato e associarlo a revoca immediata
//		i.  verificare esistenza fascicolo
		FascicoloModel fascicoloModel = getFascicoloOrThrowException(richiestaRevocaImmediataDto.getCodiceFiscale());
		Optional<MandatoModel> detenzioneCorrenteOpt = mandatoService.getMandatoCorrente(fascicoloModel);
		detenzioneCorrenteOpt.ifPresent(detenzioneCorrente -> revocaImmediata.setMandato(detenzioneCorrente));
		return revocaImmediataDao.save(revocaImmediata);
	}

	void verificaRevocaOrdinaria(final String codiceFiscale) {
		//1 check finestra temporale
		checkFinestraTemporaleRevocaOrdinaria();

		//2 deve esistere un fascicolo 
		FascicoloModel fascicolo = getFascicoloOrThrowException(codiceFiscale);

		//3 CAA connesso diverso da CAA revocato
		checkCaaConnessoDiversoCaaRevocato(fascicolo);
	}

	private void checkCaaConnessoDiversoCaaRevocato(FascicoloModel fascicolo) {
		CentroAssistenzaAgricolaDto caa = caaService.getCentroAssistenzaAgricoloUtenteConnesso();
		if (caa == null) {
			boolean isAccessoPersona = abilitazioniComponent.isAccessoPersonaFisica(fascicolo.getCuaa());
			if (!isAccessoPersona) {
				throw new EntityNotFoundException(StatusMessagesEnum.ERRORE_REPERIMENTO_CAA_CONNESSO.name());				
			}
		} else {
			mandatoService.getMandatoCorrente(fascicolo).ifPresent(detenzioneCorrente -> {
				SportelloModel sportello = ((MandatoModel)detenzioneCorrente).getSportello();
				if (sportello == null) return;
				if (sportello.getCentroAssistenzaAgricola().getId().equals(caa.getId())) {
					throw new IllegalArgumentException(RevocaMandatoNotification.CAA_REVOCANTE_UGUALE_REVOCATO.name());
				}
			});
		}
	}

	// se non si è nel range 01/01 - 30/11 non è possibile revocare ordinariamente il mandato
	private void checkFinestraTemporaleRevocaOrdinaria() {
		int currentYear = clock.today().getYear();
		LocalDate primoGennaio = LocalDate.of(currentYear, Month.JANUARY, 1);
		LocalDate trentaNovembre = LocalDate.of(currentYear, Month.NOVEMBER, 30);

		if (!(clock.today().isAfter(primoGennaio) && clock.today().isBefore(trentaNovembre))) {
			throw new IllegalArgumentException(StatusMessagesEnum.FINESTRA_TEMPORALE_NON_VALIDA.name());
		}
	}

	// aggiorna l'ultimo mandato dell'anno corrente
	private MandatoModel aggiornaUltimoMandatoAnnoCorrente(MandatoModel mandatoDaAggiornare, LocalDate date) {
		mandatoDaAggiornare.setDataFine(date);
		return mandatoDao.save(mandatoDaAggiornare);
	}

	// cancello tutti i mandati programmati per l'anno successivo
	private void cancellaMandatiAnnoSuccessivo(List<MandatoModel> mandatiFuturi) {
		if (!mandatiFuturi.isEmpty()) {
			mandatoDao.deleteAll(mandatiFuturi);
		}
	}

	private MetadatiDocumentoFirmatoDto getFirmaContratto(ApriFascicoloDto mandatoDto) throws Exception {
		ByteArrayResource documentoByteAsResource =
				new ByteArrayResource(mandatoDto.getContratto().getByteArray(), mandatoDto.getContratto().getDescription()) {
			@Override
			public String getFilename() {
				return "VerificaFirma.pdf";
			}
		};

		return verificaFirmaClient.verificaFirma(documentoByteAsResource,
				mandatoDto.getCodiceFiscaleRappresentante());
	}

	private MandatoModel saveNewMandato(FascicoloModel fascicolo, ApriFascicoloDto mandatoDto, LocalDate dataSottoscrizione, LocalDate dataInizio, LocalDate dataFine) {
		MandatoModel mandato = new MandatoModel();
		Optional<SportelloModel> sportelloOpt = sportelloDao.findByIdentificativo(mandatoDto.getIdentificativoSportello());
		if(!sportelloOpt.isPresent()) {
			throw new IllegalArgumentException(StatusMessagesEnum.SPORTELLO_NON_TROVATO.name());
		}
		mandato.setSportello(sportelloOpt.get())
		.setContratto(mandatoDto.getContratto().getByteArray())
		.setDataSottoscrizione(dataSottoscrizione)
		.setDataInizio(dataInizio)
		.setDataFine(dataFine)
		.setFascicolo(fascicolo);

		return mandatoDao.save(mandato);
	}
	// recupera fascicolo senza essere necessaria l'abilitazione 
	FascicoloModel getFascicoloOrThrowException(String codiceFiscale) {
		return fascicoloDao
				.findByCuaaAndIdValidazione(codiceFiscale, 0)
				.orElseThrow(() -> new EntityNotFoundException(StatusMessagesEnum.FASCICOLO_LOCALE_NON_ESISTENTE.name()));
	}


	/**
	 * Verifica se il fascicolo è stato revocato in maniera ordinaria e restituisce il mandatoModel di revoca.
	 * 
	 * @param fascicolo
	 * @return il mandato della revoca ordinaria, se esiste
	 *  
	 */
	Optional<MandatoModel> getMandatoDiRevocaOrdinaria(FascicoloModel fascicolo) {
		List<MandatoModel> mandati = mandatoDao.findByFascicolo(fascicolo);
		
		Optional<MandatoModel> detenzioneCorrenteOpt = mandatoService.getMandatoCorrente(fascicolo);
		if (!detenzioneCorrenteOpt.isPresent()) {
			return Optional.empty(); 
		}
		MandatoModel mandatoCorrente = detenzioneCorrenteOpt.get();
		SportelloModel sportello = mandatoCorrente.getSportello();
		Long idCaaCorrente = sportello.getCentroAssistenzaAgricola().getId();
		LocalDate primoGennaioAnnoSuccessivo = LocalDate.of(clock.today().plusYears(1).getYear(), Month.JANUARY, 1);
		return mandati.stream()
				.filter(mandato -> primoGennaioAnnoSuccessivo.equals(mandato.getDataInizio()))
				.filter(mandato -> {
					// se il 1* gennaio il caa è diverso da quello attuale allora si tratta di revoca ordinaria
					SportelloModel sportelloMandatoRevocaOrdinaria = mandato.getSportello();
					Long idCaaFuturo = sportelloMandatoRevocaOrdinaria.getCentroAssistenzaAgricola().getId();
					return !idCaaCorrente.equals(idCaaFuturo);
				})
				.collect(CustomCollectors.collectOne());
	}

	/**
	 * Prende lo sportello successivo a quello corrente, se esiste. 
	 * 
	 * @param fascicolo
	 * @return Optional<MandatoModel> della prossimo sportello
	 */
	Optional<MandatoModel> getSportelloSuccessivoAlCorrente(FascicoloModel fascicolo) {
		Optional<DetenzioneModel> detenzioneSuccessiva = fascicolo.getDetenzioni().stream()
				.filter(detenzione -> detenzione.getDataInizio().compareTo(clock.today()) > 0)
				.min(Comparator.comparing(DetenzioneModel::getDataInizio));
		if (detenzioneSuccessiva.isPresent()) {
			Optional<MandatoModel> detenzioneCorrenteOpt = mandatoService.getMandatoCorrente(fascicolo);
			if (detenzioneCorrenteOpt.isPresent()) {
				MandatoModel mandatoAttuale = detenzioneCorrenteOpt.get();
				SportelloModel sportelloAttuale = mandatoAttuale.getSportello();
				MandatoModel mandatoSuccessivo = (MandatoModel)detenzioneSuccessiva.get();
				SportelloModel sportelloSuccessivo = mandatoSuccessivo.getSportello();
				if (sportelloAttuale.getCentroAssistenzaAgricola().getId().equals(sportelloSuccessivo.getCentroAssistenzaAgricola().getId())) {
					return Optional.of(mandatoSuccessivo);
				}
			}
		}
		return Optional.empty();
	}

	enum RevocaMandatoNotification { 
		CAA_REVOCANTE_UGUALE_REVOCATO , 
		SPORTELLO_REVOCA_IN_CORSO, 
	}
}
