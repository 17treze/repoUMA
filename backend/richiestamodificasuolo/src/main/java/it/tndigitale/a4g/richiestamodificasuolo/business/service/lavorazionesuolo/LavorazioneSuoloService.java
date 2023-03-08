package it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.framework.pagination.builder.PageableBuilder;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento.Ordine;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.richiestamodificasuolo.Ruoli;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.criteriabuilder.DocumentazioneRichiestaSpecificationBuilder;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.criteriabuilder.SuoloDichiaratoLavorazioneSpecificationBuilder;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.criteriabuilder.SuoloSpecificationBuilder;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.criteriabuilder.TempClipSuADLSpecificationBuilder;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.DocumentazioneRichiestaModificaSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.EsitoLavorazioneDichiarato;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.MessaggioRichiestaModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.ModalitaADL;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoColtModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoLavorazioneSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoRichiestaModificaSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloDichiaratoLavorazioneModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloDichiaratoModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.TempClipSuADLModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.TipoJobFME;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.UsoSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.AreaDiLavoroDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.DocumentazioneRichiestaDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.LavorazioneSuoloDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.MessaggiRichiestaDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.StatoColtDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.SuoloDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.SuoloDichiaratoDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.SuoloDichiaratoLavorazioneDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.TempClipSuADLDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.UsoSuoloDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.Utils.UtilsFme;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase.BaseInputData;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase.InputCopiaLavorazione;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase.InputCreazioneLavorazione;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase.InputDataGeoJson;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase.TipologiaAzione;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.azione.AggiornaEsitoDichiaratoBusiness.AggiornaEsitoDichiaratoInputData;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.azione.CreaBuchiLavorazioneBusiness.CreaBuchiInputData;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.azione.DissociaPoligonoDaLavorazioneBusiness.DissociaPoligonoInputData;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.azione.RimuoviDichiaratoLavorazioneBusiness.RimuoviDichiaratoInputData;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.azione.ValidLavorazioneInCorsoBusiness.TipoValidazione;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.azione.ValidLavorazioneInCorsoBusiness.ValidLavorazioneInCorsoInputData;
import it.tndigitale.a4g.richiestamodificasuolo.dto.filter.RicercaLavorazioniSuoloFilter;
import it.tndigitale.a4g.richiestamodificasuolo.dto.filter.SuoloDichiaratoLavorazioneFilter;
import it.tndigitale.a4g.richiestamodificasuolo.dto.filter.SuoloFilter;
import it.tndigitale.a4g.richiestamodificasuolo.dto.filter.TempClipSuADLFilter;
import it.tndigitale.a4g.richiestamodificasuolo.dto.fme.ResponseBodyFmeDataStreamingDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.CodDescCodificaSuoloDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.CodificheSuoloDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.CreaBuchiLavorazioneInCorsoDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.LavorazioneSuoloDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.SuoloDichiaratoLavorazioneDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.SuoloDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.SuoloNonAssociabileLavorazioneDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.TempClipSuADLDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.ValidazioneLavorazioneErrorDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.ValidazioneLavorazioneInCorsoDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.mapper.CodificheSuoloMapper;
import it.tndigitale.a4g.richiestamodificasuolo.dto.mapper.DocumentazioneRichiestaMapper;
import it.tndigitale.a4g.richiestamodificasuolo.dto.mapper.LavorazioneSuoloMapper;
import it.tndigitale.a4g.richiestamodificasuolo.dto.mapper.MessaggioRichiestaMapper;
import it.tndigitale.a4g.richiestamodificasuolo.dto.mapper.SuoloDichiaratoLavorazioneMapper;
import it.tndigitale.a4g.richiestamodificasuolo.dto.mapper.SuoloMapper;
import it.tndigitale.a4g.richiestamodificasuolo.dto.mapper.TempClipSuADLMapper;
import it.tndigitale.a4g.richiestamodificasuolo.dto.richiestaModificaSuolo.DocumentazioneRichiestaDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.richiestaModificaSuolo.MessaggioRichiestaDto;
import it.tndigitale.a4g.richiestamodificasuolo.exception.SuoloException;

@Service
public class LavorazioneSuoloService {

	public static final String USO_SUOLO_NON_DEFINITO = "999";

	private Logger log = LoggerFactory.getLogger(LavorazioneSuoloService.class);

	@Autowired
	private LavorazioneSuoloDao lavorazioneSuoloDao;

	@Autowired
	private LavorazioneSuoloMapper lavorazioneSuoloMapper;

	@Autowired
	UtenteComponent utenteComponent;

	@Autowired
	private SuoloDichiaratoLavorazioneDao suoloDichiaratoLavorazioneDao;

	@Autowired
	private SuoloDichiaratoLavorazioneMapper suoloDichiaratoLavorazioneMapper;

	@Autowired
	private SuoloDichiaratoDao suoloDichiaratoDao;

	@Autowired
	private SuoloDao suoloDao;

	@Autowired
	private UsoSuoloDao usoSuoloDao;

	@Autowired
	private StatoColtDao statoColtDao;

	@Autowired
	private MessaggiRichiestaDao messaggiRichiestaDao;

	@Autowired
	private DocumentazioneRichiestaDao documentazioneRichiestaDao;

	@Autowired
	private MessaggioRichiestaMapper messaggioMapper;

	@Autowired
	private DocumentazioneRichiestaMapper documentazioneMapper;

	@Autowired
	private SuoloMapper suoloMapper;

	@Autowired
	private CodificheSuoloMapper codificheSuoloMapper;

	@SuppressWarnings("rawtypes")
	@Autowired
	private AzioneLavorazioneFactory azioneLavorazioneFactory;

	@Autowired
	private UtilsFme utilsFme;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private TempClipSuADLMapper tempClipSuADLMapper;

	@Autowired
	private AreaDiLavoroDao areaDiLavoroDao;

	@Autowired
	private TempClipSuADLDao tempClipSuADLDao;

	@Value("${it.tndigit.serverFme.verificaIntersezioneLavorazioneUpas}")
	private String verificaIntersezioneUpas;

	@Transactional
	public LavorazioneSuoloModel createLavorazioneSuolo(Integer annoCampagna) {
		@SuppressWarnings("unchecked")
		AzioneLavorazioneBase<LavorazioneSuoloModel, InputCreazioneLavorazione> business = azioneLavorazioneFactory.inizializzaAzioneLavorazioneBase(TipologiaAzione.CREAZIONE.name());
		return business.eseguiAzione(new InputCreazioneLavorazione(utenteComponent.username(), annoCampagna, null));
	}

	public LavorazioneSuoloDto ricercaLavorazioneUtente(Long idLavorazione, String utente) {

		return lavorazioneSuoloMapper.convertToDto(ricercaLavorazioneUtenteModel(idLavorazione, utente));

	}

	public LavorazioneSuoloModel ricercaLavorazioneUtenteModel(Long idLavorazione, String utente) {
		log.debug("START - ricercaLavorazione ");
		LavorazioneSuoloModel lavorazioneModel = null;
		if (Objects.nonNull(idLavorazione)) {
			Optional<LavorazioneSuoloModel> responseLavorazione = lavorazioneSuoloDao.findById(idLavorazione);
			if (!responseLavorazione.isPresent()) {
				log.warn("LavorazioneSuoloModel non trovata per idLavorazione {} e utente {}", idLavorazione, utente);
			} else {
				lavorazioneModel = responseLavorazione.get();
			}
		}
		log.debug("END - ricercaLavorazione");
		return lavorazioneModel;
	}

	public RisultatiPaginati<SuoloDichiaratoLavorazioneDto> ricercaSuoloDichiarato(SuoloDichiaratoLavorazioneFilter criteri, Paginazione paginazione, Ordinamento ordinamento) {
		log.debug("START - ricerca - ricercaSuoloDichiaratoLavorazione con criteri {}", criteri);
		Page<SuoloDichiaratoLavorazioneModel> page = suoloDichiaratoLavorazioneDao.findAll(SuoloDichiaratoLavorazioneSpecificationBuilder.getFilter(criteri),
				PageableBuilder.build().from(paginazione, ordinamento));
		List<SuoloDichiaratoLavorazioneDto> risultati = suoloDichiaratoLavorazioneMapper.from(page);

		risultati.forEach(d -> {
			SuoloDichiaratoModel sd = new SuoloDichiaratoModel();
			sd.setId(d.getId());

			MessaggioRichiestaModel messaggio = new MessaggioRichiestaModel();
			messaggio.setRelSuoloDichiarato(sd);

			d.setNumeroMessaggi(messaggiRichiestaDao.count(Example.of(messaggio)));

			DocumentazioneRichiestaModificaSuoloModel documento = new DocumentazioneRichiestaModificaSuoloModel();
			documento.setSuoloDichiarato(sd);

			d.setNumeroDocumenti(documentazioneRichiestaDao.count(Example.of(documento)));
		});

		log.debug("END - ricerca - ricercaSuoloDichiaratoLavorazione # {} suoli trovati", page.getTotalElements());

		return RisultatiPaginati.of(risultati, page.getTotalElements());
	}

	@Transactional
	public void updateSuoloDichiarato(SuoloDichiaratoLavorazioneDto suoloDichiaratoLavorazioneDto) {
		@SuppressWarnings("unchecked")
		AzioneLavorazioneBase<LavorazioneSuoloModel, SuoloDichiaratoLavorazioneDto> business = azioneLavorazioneFactory.inizializzaAzioneLavorazioneBase(TipologiaAzione.COLLEGA_DICHIARATO.name());
		business.eseguiAzione(suoloDichiaratoLavorazioneDto);
	}

	// Usato solo in fase di @PreAuthorize (checkEditSuoloDichiaratoToLavorazioniSuolo)
	public SuoloDichiaratoModel findByIdSuoloDichiarato(Long idSuoloDichiarato) {
		SuoloDichiaratoModel suoloDich = null;
		if (Objects.nonNull(idSuoloDichiarato)) {
			Optional<SuoloDichiaratoModel> suolo = suoloDichiaratoDao.findById(idSuoloDichiarato);
			if (suolo.isPresent()) {
				suoloDich = suolo.get();
			}
		}
		return suoloDich;
	}

	@Transactional
	public void removeSuoloDichiaratoDaLavorazione(Long idLavorazione, Long idSuoloDichiarato) {
		@SuppressWarnings("unchecked")
		AzioneLavorazioneBase<LavorazioneSuoloModel, RimuoviDichiaratoInputData> business = azioneLavorazioneFactory.inizializzaAzioneLavorazioneBase(TipologiaAzione.RIMUOVI_DICHIARATO.name());
		business.eseguiAzione(new RimuoviDichiaratoInputData(idLavorazione, idSuoloDichiarato, utenteComponent.utenza()));
	}

	@Transactional
	public void removePoligonoDaLavorazione(Long idLavorazione, Long idPoligono) {
		@SuppressWarnings("unchecked")
		AzioneLavorazioneBase<LavorazioneSuoloModel, DissociaPoligonoInputData> business = azioneLavorazioneFactory.inizializzaAzioneLavorazioneBase(TipologiaAzione.DISSOCIA_POLIGONO_VIGENTE.name());
		business.eseguiAzione(new DissociaPoligonoInputData(idLavorazione, null, utenteComponent.utenza(), idPoligono));
	}

	@Transactional
	public List<SuoloNonAssociabileLavorazioneDto> associazionePoligoniLavorazioneDaDichiarato(Long idLavorazione, Integer versione, String utente) {
		@SuppressWarnings("unchecked")
		AzioneLavorazioneBase<List<SuoloNonAssociabileLavorazioneDto>, BaseInputData> business = azioneLavorazioneFactory
				.inizializzaAzioneLavorazioneBase(TipologiaAzione.ASSOCIAZIONE_POLIGONI_VIGENTE.name());
		return business.eseguiAzione(new BaseInputData(idLavorazione, null, utente));
	}

	@Transactional
	public void cercaPoligoniLavorazioneDaClickInMappa(Long idLavorazione, Integer versione, String utente, String geoJson) {
		@SuppressWarnings("unchecked")
		AzioneLavorazioneBase<?, InputDataGeoJson> business = azioneLavorazioneFactory.inizializzaAzioneLavorazioneBase(TipologiaAzione.CERCA_POLIGONI_VIGENTE_DA_CLICK_IN_MAPPA.name());
		business.eseguiAzione(new InputDataGeoJson(idLavorazione, null, utente, geoJson));
	}

	@Transactional
	public void cercaPoligonoDichiaratoDaClickInMappa(Long idLavorazione, Integer versione, String utente, String geoJson) {
		@SuppressWarnings("unchecked")
		AzioneLavorazioneBase<?, InputDataGeoJson> business = azioneLavorazioneFactory.inizializzaAzioneLavorazioneBase(TipologiaAzione.CERCA_POLIGONI_DICHIARATO_DA_CLICK_IN_MAPPA.name());
		business.eseguiAzione(new InputDataGeoJson(idLavorazione, null, utente, geoJson));
	}

	@Transactional
	public void deleteLavorazioneSuolo(Long idLavorazione) {
		log.debug("START - deleteLavorazioneSuolo {}", idLavorazione);
		@SuppressWarnings("unchecked")
		AzioneLavorazioneBase<?, BaseInputData> business = azioneLavorazioneFactory.inizializzaAzioneLavorazioneBase(TipologiaAzione.CANCELLAZIONE.name());
		business.eseguiAzione(new BaseInputData(idLavorazione, null, utenteComponent.username()));
	}

	public RisultatiPaginati<SuoloDto> getSuolo(Long idLavorazione, Paginazione paginazione, Ordinamento ordinamento) {

		log.debug("START - ricerca - getSuolo ");
		SuoloFilter criteri = new SuoloFilter();

		LavorazioneSuoloModel lavorazione = ricercaLavorazioneUtenteModel(idLavorazione, utenteComponent.username());

		criteri.setCampagna(lavorazione.getCampagna());

		if (StatoLavorazioneSuolo.CONSOLIDATA_SU_A4S.equals(lavorazione.getStato()) || StatoLavorazioneSuolo.CONSOLIDATA_SU_AGS.equals(lavorazione.getStato())) {
			criteri.setIdLavorazioneFine(idLavorazione);
		} else {
			criteri.setIdLavorazioneInCorso(idLavorazione);
		}

		log.debug("FILTRI - ricerca - getSuolo ".concat(criteri.toString()));
		Page<SuoloModel> page = suoloDao.findAll(SuoloSpecificationBuilder.getFilter(criteri), PageableBuilder.build().from(paginazione, ordinamento));
		List<SuoloDto> risultati = suoloMapper.from(page);

		log.debug("END - ricerca - getSuolo # {} suoli trovati", page.getTotalElements());

		return RisultatiPaginati.of(risultati, page.getTotalElements());

	}

	public ValidazioneLavorazioneErrorDto validaLavorazione(Long idLavorazione) {

		ValidazioneLavorazioneErrorDto errorUsoSuolo = new ValidazioneLavorazioneErrorDto();

		// Controllo se uno o più poligoni dichiarati associati;
		SuoloDichiaratoLavorazioneFilter criteri = new SuoloDichiaratoLavorazioneFilter();
		criteri.setIdLavorazione(idLavorazione);

		// List<SuoloDichiaratoLavorazioneModel> suoliDichiarati = suoloDichiaratoLavorazioneDao.findByIdLavorazioneAndStatoRichiestaNot(idLavorazione, StatoRichiestaModificaSuolo.CANCELLATA);
		RisultatiPaginati<SuoloDichiaratoLavorazioneDto> suoliDichiarati = ricercaSuoloDichiarato(criteri, null, new Ordinamento("id", Ordine.DESC));
		if (!suoliDichiarati.getRisultati().isEmpty()) {
			if (suoliDichiarati.getRisultati().stream().anyMatch(t -> t.getStatoRichiesta().equals(StatoRichiestaModificaSuolo.CANCELLATA.toString()))) {
				errorUsoSuolo.setCode("ERROR");
				errorUsoSuolo.setMessage("Suolo dichiarato collegato a richiesta cancellata");
			}
		} else {

			// Controllo se uno o più poligoni di suolo vigente associati;
			SuoloFilter criteriUsoSuolo = new SuoloFilter();
			criteriUsoSuolo.setIdLavorazioneInCorso(idLavorazione);

			RisultatiPaginati<SuoloDto> usoSuolo = getSuolo(idLavorazione, null, new Ordinamento("id", Ordine.DESC));
			if (usoSuolo.getCount() == 0) {

				errorUsoSuolo.setCode("ERROR");
				errorUsoSuolo.setMessage("Nessun Suolo Vigente Inserito o Nessun Suolo Dichiarato Inserito");
			}
		}
		return errorUsoSuolo;
	}

	@Transactional
	public void updateLavorazioneSuolo(LavorazioneSuoloDto lavorazioneSuoloDto, String utente) {
		lavorazioneSuoloDto.setUtente(utente);
		@SuppressWarnings("unchecked")
		AzioneLavorazioneBase<LavorazioneSuoloModel, LavorazioneSuoloDto> business = azioneLavorazioneFactory.inizializzaAzioneLavorazioneBase(TipologiaAzione.MODIFICA.name());
		business.eseguiAzione(lavorazioneSuoloDto);
	}

	@Transactional
	public LavorazioneSuoloModel copyLavorazioneSuolo(Long idLavorazione, Integer campagna, String utente) {
		@SuppressWarnings("unchecked")
		AzioneLavorazioneBase<LavorazioneSuoloModel, InputCopiaLavorazione> business = azioneLavorazioneFactory.inizializzaAzioneLavorazioneBase(TipologiaAzione.COPIA.name());

		LavorazioneSuoloModel oldLavorazione = lavorazioneSuoloDao.getOne(idLavorazione);
		if (oldLavorazione.getStato() == StatoLavorazioneSuolo.CONSOLIDATA_SU_AGS) {
			return business.eseguiAzione(new InputCopiaLavorazione(utenteComponent.username(), campagna, idLavorazione));
		} else {
			throw SuoloException.ExceptionType.GENERIC_EXCEPTION.newSuoloExceptionInstance("Errore, lo stato della lavorazione non è valido");
		}
	}

	@Transactional
	public LavorazioneSuoloModel cambiaCampagnaLavorazioneSuolo(Long idLavorazione, Integer campagna, String utente) {
		AzioneLavorazioneBase<LavorazioneSuoloModel, InputCopiaLavorazione> business = azioneLavorazioneFactory.inizializzaAzioneLavorazioneBase(TipologiaAzione.CAMBIA_CAMPAGNA.name());
		return business.eseguiAzione(new InputCopiaLavorazione(utenteComponent.username(), campagna, idLavorazione));
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public ResponseEntity insertOrUpdateOrRiprendiLavorazioneInWorkspace(Long idLavorazione, String utente, boolean verificaPascoli, boolean update, boolean riprendi, String geoJson) {
		LavorazioneSuoloModel lavorazione = ricercaLavorazioneUtenteModel(idLavorazione, utente);
		if (lavorazione != null && !lavorazione.getListaSuoloInCorsoModel().isEmpty() && verificaPascoli) {

			ResponseEntity<String> responseFme;
			try {
				Map<String, String> params = new HashMap<>();
				params.put("idLavorazione", String.valueOf(idLavorazione));
				params.put("annoUpas", String.valueOf(lavorazione.getCampagna()));
				responseFme = utilsFme.callProcedureFmeDataStreaming(verificaIntersezioneUpas, params);

				JsonNode responseFmeParse = objectMapper.readTree(responseFme.getBody());

				ResponseBodyFmeDataStreamingDto responseFmeDto = new ObjectMapper().readerFor(ResponseBodyFmeDataStreamingDto.class).readValue(responseFmeParse.get(0));

				if (responseFmeDto.isLavorazione_in_upas())
					return new ResponseEntity<>("Intersezione pascoli", HttpStatus.PRECONDITION_FAILED);

			} catch (Exception e) {
				log.error("Errore trasformata fme ", e);
				throw SuoloException.ExceptionType.GENERIC_EXCEPTION.newSuoloExceptionInstance("Errore trasformata fme");
			}
		}
		if (update) {
			AzioneLavorazioneBase<?, InputDataGeoJson> business = azioneLavorazioneFactory.inizializzaAzioneLavorazioneBase(TipologiaAzione.AGGIORNA_WORKSPACE.name());
			business.eseguiAzione(new InputDataGeoJson(idLavorazione, null, utente, geoJson));
		} else if (riprendi) {
			AzioneLavorazioneBase<?, BaseInputData> business = azioneLavorazioneFactory.inizializzaAzioneLavorazioneBase(TipologiaAzione.RIPRENDI_LAVORAZIONE.name());
			business.eseguiAzione(new BaseInputData(idLavorazione, null, utente));
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			AzioneLavorazioneBase<?, BaseInputData> business = azioneLavorazioneFactory.inizializzaAzioneLavorazioneBase(TipologiaAzione.AVVIO.name());
			business.eseguiAzione(new BaseInputData(idLavorazione, null, utente));
		}
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	public ResponseEntity insertOrUpdateOrADL(Long idLavorazione, String utente, String geoJson) {
		LavorazioneSuoloModel lavorazione = ricercaLavorazioneUtenteModel(idLavorazione, utente);

		AzioneLavorazioneBase<String, InputDataGeoJson> business = azioneLavorazioneFactory.inizializzaAzioneLavorazioneBase(TipologiaAzione.AGGIORNA_ADL.name());
		String result = business.eseguiAzione(new InputDataGeoJson(idLavorazione, null, utente, geoJson));

		return new ResponseEntity<String>(result, HttpStatus.CREATED);
	}

	public RisultatiPaginati<LavorazioneSuoloDto> ricercaLavorazioniNonConcluseNonConsolidateAgs(String currentUser, Paginazione paginazione, Ordinamento ordinamento) {

		log.debug("START - ricercaLavorazioniNonConcluseNonConsolidateAgs ");

		RicercaLavorazioniSuoloFilter filter = new RicercaLavorazioniSuoloFilter();
		Pageable pageable = PageableBuilder.build().from(paginazione, ordinamento);

		filter.setStatoLavorazione(filter.getStatiToSearchNotIn(Arrays.asList(StatoLavorazioneSuolo.CHIUSA, StatoLavorazioneSuolo.CONSOLIDATA_SU_AGS)));

		Page<LavorazioneSuoloModel> page = lavorazioneSuoloDao.findbyLavorazioniSuoloFilter(0L, currentUser, null, null, filter.getStatoLavorazione(), pageable);

		List<LavorazioneSuoloDto> risultati = lavorazioneSuoloMapper.from(page);

		for (LavorazioneSuoloDto lavorazioneSuoloDto : risultati) {
			if (currentUser.equals(lavorazioneSuoloDto.getUtente())) {
				lavorazioneSuoloDto.setReadOnly("NO");
			} else {
				lavorazioneSuoloDto.setReadOnly("SI");
			}
		}

		RisultatiPaginati risultatiPaginati = RisultatiPaginati.of(risultati, page.getTotalElements());
		if (risultatiPaginati.getRisultati().isEmpty()) {
			risultatiPaginati.setRisultati(new ArrayList<>());
		}

		log.debug("END - ricercaLavorazioniNonConcluseNonConsolidateAgs ");

		return risultatiPaginati;
	}

	public CodificheSuoloDto getCodificheSuolo() {

		log.debug("START - getCodificheSuolo ");

		List<UsoSuoloModel> pageUsoSuolo = usoSuoloDao.findCodUsoSuoloValido(LocalDateTime.of(9999, 12, 30, 00, 00), 1);
		List<CodDescCodificaSuoloDto> risultatiCodUso = codificheSuoloMapper.fromUsoSuolo(pageUsoSuolo);

		List<StatoColtModel> pageStatoColt = statoColtDao.findStatoColtValido(LocalDateTime.of(9999, 12, 31, 00, 00));
		List<CodDescCodificaSuoloDto> risultatiStatoColt = codificheSuoloMapper.fromStatoColt(pageStatoColt);

		CodificheSuoloDto listaCodificheSuoloDto = new CodificheSuoloDto();
		listaCodificheSuoloDto.setCodUsoSuolo(risultatiCodUso);
		listaCodificheSuoloDto.setStatoColtSuolo(risultatiStatoColt);

		log.debug("END - getCodificheSuolo");

		return listaCodificheSuoloDto;

	}

	@Transactional
	public void setEsitoDichiarato(Long idSuoloDichiarato, EsitoLavorazioneDichiarato esito) {
		@SuppressWarnings("unchecked")
		AzioneLavorazioneBase<LavorazioneSuoloModel, AggiornaEsitoDichiaratoInputData> business = azioneLavorazioneFactory
				.inizializzaAzioneLavorazioneBase(TipologiaAzione.AGGIORNA_ESITO_DICHIARATO.name());
		business.eseguiAzione(new AggiornaEsitoDichiaratoInputData(idSuoloDichiarato, esito));
	}

	@Transactional
	public ValidazioneLavorazioneInCorsoDto validaLavorazioneInCorso(Long idLavorazione) {
		@SuppressWarnings("unchecked")
		AzioneLavorazioneBase<ValidazioneLavorazioneInCorsoDto, ValidLavorazioneInCorsoInputData> business = azioneLavorazioneFactory
				.inizializzaAzioneLavorazioneBase(TipologiaAzione.VALIDA_LAVORAZIONE_IN_CORSO.name());
		return business.eseguiAzione(new ValidLavorazioneInCorsoInputData(idLavorazione, TipoValidazione.TUTTI));
	}

	public CreaBuchiLavorazioneInCorsoDto creaBuchiLavorazione(Long idLavorazione) {
		@SuppressWarnings("unchecked")
		AzioneLavorazioneBase<CreaBuchiLavorazioneInCorsoDto, CreaBuchiInputData> business = azioneLavorazioneFactory.inizializzaAzioneLavorazioneBase(TipologiaAzione.CREA_BUCHI_LAVORAZIONE.name());
		return business.eseguiAzione(new CreaBuchiInputData(idLavorazione, TipoValidazione.TUTTI));
	}

	public ResponseEntity<?> cambiaStatoLavorazioneInModifica(Long idLavorazione, StatoLavorazioneSuolo statoInModifica, String utente) {
		log.debug("Start - cambiaStatoLavorazioneInModifica per idLavorazione {}", idLavorazione);
		ResponseEntity<?> res;

		LavorazioneSuoloModel lavorazione = ricercaLavorazioneUtenteModel(idLavorazione, utente);
		if (lavorazione == null) {
			log.error("LavorazioneSuoloModel non trovata per  {} o per utente {}", idLavorazione, utente);
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION
					.newSuoloExceptionInstance("Entita non trovata per lavorazione ".concat(String.valueOf(idLavorazione).concat(" per utente ").concat(utente)));
		}
		try {
			creaBuchiLavorazione(idLavorazione);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
		}

		if (StatoLavorazioneSuolo.IN_CORSO.equals(lavorazione.getStato())) {

			if (StatoLavorazioneSuolo.IN_MODIFICA.equals(statoInModifica)) {

				lavorazione.setStato(StatoLavorazioneSuolo.IN_MODIFICA);
				lavorazioneSuoloDao.save(lavorazione);
				log.debug("END - cambiaStatoLavorazioneInModifica - Aggiornato lo stato della lavorazione da {}  a {} ", StatoLavorazioneSuolo.IN_CORSO.toString(),
						StatoLavorazioneSuolo.IN_MODIFICA.toString());
				res = new ResponseEntity<>(HttpStatus.OK);
			} else {
				log.debug("END - cambiaStatoLavorazioneInModifica - Passaggio di stato non consentito da {} a {} ", StatoLavorazioneSuolo.IN_CORSO.toString(), statoInModifica);
				res = new ResponseEntity<>("Impossibile cambiare lo stato della lavorazione da IN_CORSO A " + statoInModifica, HttpStatus.NOT_ACCEPTABLE);
			}
		} else {
			log.debug("END - cambiaStatoLavorazioneInModifica - Impossibile aggiornare lo stato della lavorazione a {} poiche' " + "lo stato attuale della lavorazione non è IN_CORSO",
					StatoLavorazioneSuolo.IN_MODIFICA.toString());
			res = new ResponseEntity<>("Passaggio di stato non consentito", HttpStatus.NOT_ACCEPTABLE);
		}
		return res;
	}

	@Transactional
	public void consolidamentoInA4S(Long idLavorazione, String utente) {
		@SuppressWarnings("unchecked")
		AzioneLavorazioneBase<?, BaseInputData> business = azioneLavorazioneFactory.inizializzaAzioneLavorazioneBase(TipologiaAzione.CONSOLIDA_IN_A4S.name());
		business.eseguiAzione(new BaseInputData(idLavorazione, null, utente));
	}

	@Transactional
	public void consolidamentoInAGS(Long idLavorazione, String utente) {
		@SuppressWarnings("unchecked")
		AzioneLavorazioneBase<?, BaseInputData> business = azioneLavorazioneFactory.inizializzaAzioneLavorazioneBase(TipologiaAzione.CONSOLIDA_IN_AGS.name());
		business.eseguiAzione(new BaseInputData(idLavorazione, null, utente));
	}

	@Transactional
	public void ritagliasuADL(Long idLavorazione, String utente) {
		@SuppressWarnings("unchecked")
		AzioneLavorazioneBase<?, BaseInputData> business = azioneLavorazioneFactory.inizializzaAzioneLavorazioneBase(TipologiaAzione.RITAGLIA_SU_ADL.name());
		business.eseguiAzione(new BaseInputData(idLavorazione, null, utente));

	}

	@Transactional
	public LavorazioneSuoloDto getRefreshStatoJobFME(Long idLavorazione, TipoJobFME tipoJobFme, String utente) {

		LavorazioneSuoloDto lavorazioneSuoloDto = lavorazioneSuoloMapper.convertToDto(ricercaLavorazioneUtenteModel(idLavorazione, utente));
		AzioneLavorazioneBase<?, AzioneLavorazioneBase.InputDataFme> business = azioneLavorazioneFactory.inizializzaAzioneLavorazioneBase(TipologiaAzione.REFRESH_STATO_JOB_FME_LAVORAZIONE.name());
		business.eseguiAzione(new AzioneLavorazioneBase.InputDataFme(idLavorazione, utente, tipoJobFme));

		return lavorazioneSuoloDto;
	}

	public List<MessaggioRichiestaDto> ricercaMessaggiDichiarato(Long idDichiarato) {
		log.debug("START - ricercaMessaggiDichiarato per idDichiarato {} ", idDichiarato);

		List<MessaggioRichiestaModel> messageListModels = messaggiRichiestaDao.findByIdDichiarato(idDichiarato);
		List<MessaggioRichiestaDto> messageListdto = new ArrayList<>();
		if (!messageListModels.isEmpty()) {
			messageListdto = messaggioMapper.fromList(messageListModels);
			log.info("Trovati {} messaggi per idDichiarato {}", messageListModels.size(), idDichiarato);
		} else {
			log.info("Non ci sono messaggi per idDichiarato {} ", idDichiarato);
		}
		log.debug("END - ricercaMessaggiDichiarato per idDichiarato {} ", idDichiarato);
		return messageListdto;
	}

	@Transactional()
	public void insertMessaggiDichiarato(List<MessaggioRichiestaDto> messaggiRichiestalistDto, Long idDichiarato, String username) {

		log.debug("START - insertMessaggiDichiarato per idDichiarato {} ", idDichiarato);

		if (messaggiRichiestalistDto.isEmpty()) {
			log.error("La lista dei messaggi da inserire per idDichiarato {} è vuota", idDichiarato);
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("La lista dei messaggi da inserire è vuota per idDichiarato".concat(String.valueOf(idDichiarato)));
		}

		messaggiRichiestalistDto.forEach(messaggio -> {
			if (messaggio.getTesto().getBytes().length > 4000) {
				log.error("Dimensione massima consentita per testo > 4000 caratteri (per idDichiarato {})", idDichiarato);
				throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION
						.newSuoloExceptionInstance("Superata dimensione massima per il test per messaggio per richiesta ".concat(String.valueOf(idDichiarato)));
			} else {
				Optional<SuoloDichiaratoModel> suoloDichiaratoModel = suoloDichiaratoDao.findById(idDichiarato);
				if (suoloDichiaratoModel.isEmpty()) {
					log.error("Impossibile recuperare il suolo dichiarato {}", idDichiarato);
					throw SuoloException.ExceptionType.NOT_FOUND_EXCEPTION.newSuoloExceptionInstance("La lista dei messaggi da inserire è vuota per idDichiarato".concat(String.valueOf(idDichiarato)));
				}

				messaggio.setIdPoligonoDichiarato(idDichiarato);
				MessaggioRichiestaModel messaggioRichiestaModel = messaggioMapper.convertToModel(messaggio);
				SuoloDichiaratoModel suoloDichiarato = suoloDichiaratoModel.get();

				messaggioRichiestaModel.setUtente(username);
				messaggioRichiestaModel.setRelSuoloDichiarato(suoloDichiarato);
				// messaggioRichiestaModel.setRichiestaModificaSuolo(suoloDichiaratoModel.get().getRichiestaModificaSuolo());
				messaggiRichiestaDao.saveAndFlush(messaggioRichiestaModel);

				setNuovaModifica(suoloDichiarato);
			}
		});
		log.debug("END - insertMessaggiDichiarato per idDichiarato {} ", idDichiarato);

	}

	public List<DocumentazioneRichiestaDto> ricercaDocumentiDichiarato(Long idDichiarato) {
		List<DocumentazioneRichiestaDto> documenti = documentazioneRichiestaDao.findAll(DocumentazioneRichiestaSpecificationBuilder.filterByIdDichiarato(idDichiarato)).stream()
				.map(documentazioneMapper::fromModelIgnoringContent).collect(Collectors.toList());

		return documenti;
	}

	public DocumentazioneRichiestaDto ricercaDocumentoDichiarato(Long idDichiarato, Long idDocumento) {
		DocumentazioneRichiestaDto documento = documentazioneRichiestaDao.findOne(DocumentazioneRichiestaSpecificationBuilder.filterByIdDichiaratoAndIdDocumento(idDichiarato, idDocumento))
				.map(documentazioneMapper::fromModel).orElseThrow(() -> {
					log.error("DocumentazioneDichiaratoModel non trovata per idDocumento e idDichiarato {}, {}", idDocumento, idDichiarato);

					return SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION
							.newSuoloExceptionInstance("DocumentazioneDichiaratoModel non trovata per idDocumento " + idDocumento + " e idDichiarato " + idDichiarato);
				});

		return documento;
	}

	@Transactional(rollbackFor = SuoloException.class)
	public void uploadDocumentoDichiarato(Long idDichiarato, DocumentazioneRichiestaDto documento, MultipartFile file) {
		if (documento == null || file == null) {
			log.error("Dati in input non validi per la richiesta {}", idDichiarato);

			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Dati in input non validi per la richiesta " + idDichiarato);
		}

		suoloDichiaratoDao.findById(idDichiarato).ifPresentOrElse(dichiarato -> uploadDocumentoDichiarato(dichiarato, documento, file), () -> {
			log.error("SuoloDichiaratoModel non trovato per idDichiarato {}", idDichiarato);

			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Entita non trovata per richiesta modifica suolo " + idDichiarato);
		});

		setNuovaModifica(idDichiarato);
	}

	private void uploadDocumentoDichiarato(SuoloDichiaratoModel dichiarato, DocumentazioneRichiestaDto dto, MultipartFile file) {
		DocumentazioneRichiestaModificaSuoloModel model = new DocumentazioneRichiestaModificaSuoloModel();

		try {
			model.setDocContent(file.getBytes());
		} catch (IOException e) {
			log.error("Errore leggendo i dati del file allegato", e);

			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Errore leggendo i dati del file allegato");
		}

		String nomeFile = StringUtils.cleanPath(file.getOriginalFilename());

		model.setDataInserimento(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
		model.setDescrizione(dto.getDescrizione());
		model.setNomeFile(nomeFile);
		model.setProfiloUtente(dto.getProfiloUtente());
		model.setUtente(utenteComponent.username());
		model.setSuoloDichiarato(dichiarato);
		model.setDimensione(dto.getDimensione());

		documentazioneRichiestaDao.save(model);
	}

	public void cancellaDocumentoDichiarato(Long idDichiarato, Long idDocumento) {
		if (idDichiarato == null || idDocumento == null) {
			log.error("Dati in input non validi per la idDichiarato e idDocumento {}", idDichiarato, idDocumento);

			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Dati in input non validi per la richiesta " + idDichiarato + " e il documento " + idDocumento);
		}

		documentazioneRichiestaDao.findOne(DocumentazioneRichiestaSpecificationBuilder.filterByIdDichiaratoAndIdDocumento(idDichiarato, idDocumento))
				.ifPresentOrElse(documentazioneRichiestaDao::delete, () -> {
					log.error("DocumentazioneDichiaratoModel non trovata per idDichiarato e idDocumento {}", idDichiarato, idDocumento);

					throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION
							.newSuoloExceptionInstance("DocumentazioneDichiaratoModel non trovata per per la richiesta " + idDichiarato + " e il documento " + idDocumento);
				});

		setNuovaModifica(idDichiarato);
	}

	private void setNuovaModifica(Long idDichiarato) {
		SuoloDichiaratoModel dichiarato = suoloDichiaratoDao.findById(idDichiarato).orElseThrow(() -> {
			log.error("SuoloDichiaratoModel non trovato per idDichiarato {}", idDichiarato);

			return SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Entita non trovata per richiesta modifica suolo " + idDichiarato);
		});

		setNuovaModifica(dichiarato);
	}

	private void setNuovaModifica(SuoloDichiaratoModel dichiarato) {
		if (utenteComponent.haRuolo(Ruoli.EDIT_RICHIESTA_MODIFICA_SUOLO_ENTE)) {
			dichiarato.setNuovaModificaCaa(true);
		} else if (utenteComponent.haRuolo(Ruoli.EDIT_DOCUMENTO_RICHIESTA_TUTTI)) {
			dichiarato.setNuovaModificaBo(true);
		}

		suoloDichiaratoDao.save(dichiarato);
	}

	public RisultatiPaginati<LavorazioneSuoloDto> ricercaLavorazioniFilter(RicercaLavorazioniSuoloFilter filter, Paginazione paginazione, Ordinamento ordinamento, String currentUser) {

		log.debug("START - ricercaLavorazioneFilter ");
		Pageable pageable = PageableBuilder.build().from(paginazione, ordinamento);

		if (filter.getIdLavorazione() == null)
			filter.setIdLavorazione(0L);
		if (filter.getTitolo() == null || filter.getTitolo().equals("")) {
			filter.setTitolo("");
		} else {
			filter.setTitolo("%".concat(filter.getTitolo().toLowerCase()).concat("%"));
		}
		if (filter.getCuaa() == null)
			filter.setCuaa("");
		if (filter.getUtente() == null)
			filter.setUtente("");

		Page<LavorazioneSuoloModel> page = lavorazioneSuoloDao.findbyLavorazioniSuoloFilter(filter.getIdLavorazione(), filter.getUtente(), filter.getCuaa(), filter.getTitolo(),
				filter.getStatoLavorazione(), pageable);

		List<LavorazioneSuoloDto> risultati = lavorazioneSuoloMapper.from(page);
		for (int x = 0; x < risultati.size(); x++) {
			for (LavorazioneSuoloDto lavorazioneSuoloDto : risultati) {
				if (currentUser.equals(lavorazioneSuoloDto.getUtente())) {
					lavorazioneSuoloDto.setReadOnly("NO");
				} else {
					lavorazioneSuoloDto.setReadOnly("SI");
				}
			}
		}

		RisultatiPaginati risultatiPaginati = RisultatiPaginati.of(risultati, page.getTotalElements());
		if (risultatiPaginati.getRisultati().isEmpty()) {
			risultatiPaginati.setRisultati(new ArrayList<>());
		}

		log.debug("END - ricercaLavorazioneFilter # {} lavorazioni trovate", page.getTotalElements());

		return risultatiPaginati;
	}

	@Transactional
	public void eliminaAreaDiLavoroLavorazione(Long idLavorazione) {
		areaDiLavoroDao.deleteByIdLavorazione(idLavorazione);
		tempClipSuADLDao.deleteByIdLavorazioneTempClipSuADL(idLavorazione);
		suoloDao.rimuoviSuoloDaLavorazioneInCorso(idLavorazione);

		Optional<LavorazioneSuoloModel> responseLavorazione = lavorazioneSuoloDao.findById(idLavorazione);
		if (responseLavorazione.isPresent() && responseLavorazione.get().getModalitaADL() == ModalitaADL.DISEGNO_ADL) {
			responseLavorazione.get().setModalitaAdl(ModalitaADL.POLIGONI_INTERI);
			lavorazioneSuoloDao.save(responseLavorazione.get());
		}
	}

	public RisultatiPaginati<TempClipSuADLDto> getTempClipSuADL(Long idLavorazione, Paginazione paginazione, Ordinamento ordinamento) {

		log.debug("START - ricerca - getTempClipSuADL ");
		TempClipSuADLFilter criteri = new TempClipSuADLFilter();

		LavorazioneSuoloModel lavorazione = ricercaLavorazioneUtenteModel(idLavorazione, utenteComponent.username());
		criteri.setIdLavorazione(idLavorazione);
		criteri.setPosizionePoligono("IN");

		log.debug("FILTRI - ricerca - getSuolo ".concat(criteri.toString()));
		Page<TempClipSuADLModel> page = tempClipSuADLDao.findAll(TempClipSuADLSpecificationBuilder.getFilter(criteri), PageableBuilder.build().from(paginazione, ordinamento));
		List<TempClipSuADLDto> risultati = tempClipSuADLMapper.from(page);

		log.debug("END - ricerca - getTempClipSuADL # {} suoli trovati", page.getTotalElements());

		return RisultatiPaginati.of(risultati, page.getTotalElements());

	}
}
