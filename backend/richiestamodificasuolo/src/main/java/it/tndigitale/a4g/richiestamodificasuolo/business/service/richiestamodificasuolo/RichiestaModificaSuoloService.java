package it.tndigitale.a4g.richiestamodificasuolo.business.service.richiestamodificasuolo;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import it.tndigitale.a4g.framework.pagination.builder.PageableBuilder;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.criteriabuilder.DocumentazioneRichiestaSpecificationBuilder;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.criteriabuilder.MessaggiRichiestaSpecificationBuilder;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.criteriabuilder.RichiestaModificaSuoloSpecificationBuilder;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.DocumentazioneRichiestaModificaSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.MessaggioRichiestaModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.RichiestaModificaSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoRichiestaModificaSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloDichiaratoModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.DocumentazioneRichiestaDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.MessaggiRichiestaDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.RichiestaModificaSuoloDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.SuoloDichiaratoDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.Utils.RichiestaModificaSuoloUtils;
import it.tndigitale.a4g.richiestamodificasuolo.dto.filter.RichiestaModificaSuoloFilter;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.SuoloDichiaratoLavorazioneDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.mapper.DocumentazioneRichiestaMapper;
import it.tndigitale.a4g.richiestamodificasuolo.dto.mapper.MessaggioRichiestaMapper;
import it.tndigitale.a4g.richiestamodificasuolo.dto.mapper.RichiestaModificaSuoloMapper;
import it.tndigitale.a4g.richiestamodificasuolo.dto.mapper.SuoloDichiaratoLavorazioneMapper;
import it.tndigitale.a4g.richiestamodificasuolo.dto.richiestaModificaSuolo.DocumentazioneRichiestaDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.richiestaModificaSuolo.MessaggioRichiestaDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.richiestaModificaSuolo.RichiestaModificaSuoloDto;
import it.tndigitale.a4g.richiestamodificasuolo.exception.SuoloException;

@Service
public class RichiestaModificaSuoloService {

	private Logger log = LoggerFactory.getLogger(RichiestaModificaSuoloService.class);

	@Autowired
	private RichiestaModificaSuoloDao richiestaModificaSuoloDao;

	@Autowired
	private MessaggiRichiestaDao messaggiRichiestaDao;

	@Autowired
	private DocumentazioneRichiestaDao documentazioneRichiestaDao;

	@Autowired
	private SuoloDichiaratoDao suoloDichiaratoDao;

	@Autowired
	private UtenteComponent utenteComponent;

	@Autowired
	private RichiestaModificaSuoloMapper mapper;

	@Autowired
	private SuoloDichiaratoLavorazioneMapper suoloDichiaratoLavorazioneMapper;

	@Autowired
	private DocumentazioneRichiestaMapper documentazioneMapper;

	@Autowired
	private MessaggioRichiestaMapper messaggioMapper;

	private final String RICHIESTA_NON_TROVATA = "RichiestaModificaSuoloModel non trovata per idRichiesta {}";

	public RisultatiPaginati<RichiestaModificaSuoloDto> ricerca(RichiestaModificaSuoloFilter filtri, Paginazione paginazione, Ordinamento ordinamento) {
		log.debug("Avvio - ricerca - RichiestaModificaSuolo con filtri ", filtri);
		Page<RichiestaModificaSuoloModel> page = richiestaModificaSuoloDao.findAll(RichiestaModificaSuoloSpecificationBuilder.getFilter(filtri),
				PageableBuilder.build().from(paginazione, ordinamento));
		List<RichiestaModificaSuoloDto> risultati = mapper.from(page);

		log.debug("END - ricerca - RichiestaModificaSuolo # {} RichiesteModificaSuolo trovate", page.getTotalElements());

		return RisultatiPaginati.of(risultati, page.getTotalElements());

	}

	public RichiestaModificaSuoloDto ricercaDettaglio(Long idRichiesta) {
		log.debug("START - ricerca - ricercaDettaglio per id {}", idRichiesta);
		RichiestaModificaSuoloDto richiestaModificaSuoloDto = null;
		if (Objects.nonNull(idRichiesta)) {

			Optional<RichiestaModificaSuoloModel> cercaRichiestaModificaSuolo = richiestaModificaSuoloDao.findById(idRichiesta);
			if (!cercaRichiestaModificaSuolo.isPresent()) {
				log.error(RICHIESTA_NON_TROVATA, idRichiesta);
				throw SuoloException.ExceptionType.NOT_FOUND_EXCEPTION.newSuoloExceptionInstance("Entita non trovata per richiesta modifica suolo ".concat(String.valueOf(idRichiesta)));
			}
			richiestaModificaSuoloDto = mapper.fromRichiestaModificaSuolo(cercaRichiestaModificaSuolo.get());

		}
		log.debug("END - ricerca - ricercaDettaglio  ");
		return richiestaModificaSuoloDto;
	}

	@Transactional(rollbackFor = { SuoloException.class })
	public void updateRichiestaModificaSuolo(RichiestaModificaSuoloDto richiestaModificaSuoloDto) {

		if (Objects.nonNull(richiestaModificaSuoloDto.getId())) {
			log.debug("START - updateRichiestaModificaSuolo - {id} : {}", richiestaModificaSuoloDto.getId());
			Optional<RichiestaModificaSuoloModel> richiestaModificaSuolOpt = richiestaModificaSuoloDao.findById(richiestaModificaSuoloDto.getId());
			if (!richiestaModificaSuolOpt.isPresent()) {
				log.error(RICHIESTA_NON_TROVATA, richiestaModificaSuoloDto.getId());
				throw SuoloException.ExceptionType.NOT_FOUND_EXCEPTION
						.newSuoloExceptionInstance("Entita non trovata per richiesta modifica suolo ".concat(String.valueOf(richiestaModificaSuoloDto.getId())));
			} else {

				// Stato Richiesta non permette l'aggiornamento
				if (RichiestaModificaSuoloUtils.listaStatiRichiestaModificaSuoloNonModificabile.contains(richiestaModificaSuoloDto.getStato())) {
					throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Errore, lo stato della lavorazione non permette la modifica");
				} else {

					RichiestaModificaSuoloModel richiestaModificaSuoloModelToUpdate = richiestaModificaSuolOpt.get();

					// segnalazione ID 124.1
					if (richiestaModificaSuoloModelToUpdate.getStato() == StatoRichiestaModificaSuolo.APERTA && richiestaModificaSuoloDto.getStato() == StatoRichiestaModificaSuolo.LAVORABILE) {

						List<SuoloDichiaratoModel> suoliDichiaratiRichiesta = suoloDichiaratoDao.findByRichiestaModificaSuolo(richiestaModificaSuoloModelToUpdate);

						for (SuoloDichiaratoModel suoloDichiaratoModel : suoliDichiaratiRichiesta) {
							checkAttributiObbligatori(suoloDichiaratoModel, richiestaModificaSuoloDto.getStato(), richiestaModificaSuoloModelToUpdate.getCampagna());
						}
					}

					richiestaModificaSuoloModelToUpdate = mapper.fromRichiestaModificaSuoloDtoToModel(richiestaModificaSuoloModelToUpdate, richiestaModificaSuoloDto);

					log.debug("UPDATE RichiestaModificaSuolo", richiestaModificaSuoloModelToUpdate);

					richiestaModificaSuoloDao.save(richiestaModificaSuoloModelToUpdate);

					log.debug("END - updateRichiestaModificaSuolo - {id} : {}", richiestaModificaSuoloDto.getId());
				}
			}

		}
	}

	public RisultatiPaginati<MessaggioRichiestaDto> ricercaMessaggiRichiestaModificaSuolo(Long idRichiesta, Paginazione paginazione, Ordinamento ordinamento) {
		log.debug("START - ricercaMessaggiRichiestaModificaSuolo per {idRichiesta} ".concat(String.valueOf(idRichiesta)));

		Page<MessaggioRichiestaModel> page = messaggiRichiestaDao.findAll(MessaggiRichiestaSpecificationBuilder.getFilter(idRichiesta), PageableBuilder.build().from(paginazione, ordinamento));
		List<MessaggioRichiestaDto> risultati = messaggioMapper.from(page);
		risultati = risultati.stream().filter(x -> x.getIdPoligonoDichiarato() == null).collect(Collectors.toList());

		log.debug("END - ricercaMessaggiRichiestaModificaSuolo # {} Messaggi trovati per la idRichiesta {}", page.getTotalElements(), idRichiesta);
		return RisultatiPaginati.of(risultati, (long) risultati.size());
	}

	@Transactional(rollbackFor = { SuoloException.class })
	public void insertMessaggiRichiesta(Long idRichiesta, List<MessaggioRichiestaDto> listMessaggioRichiestaDto, String username) {

		Optional<RichiestaModificaSuoloModel> richiestaModificaSuolOpt = richiestaModificaSuoloDao.findById(idRichiesta);
		if (!richiestaModificaSuolOpt.isPresent()) {
			log.error(RICHIESTA_NON_TROVATA, idRichiesta);
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Entita non trovata per richiesta modifica suolo ".concat(String.valueOf(idRichiesta)));
		} else {
			log.debug("START - insertMessaggiRichiesta  per {idRichiesta} ".concat(String.valueOf(idRichiesta)));

			RichiestaModificaSuoloModel richiestaModificaSuoloModel = richiestaModificaSuolOpt.get();
			listMessaggioRichiestaDto.forEach(messaggio -> {
				if (messaggio.getTesto().getBytes().length > 4000) {
					log.error("Dimensione massima consentita per testo > 4000 caratteri (per richiesta {})", idRichiesta);
					throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION
							.newSuoloExceptionInstance("Superata dimensione massima per il test per messaggio per richiesta ".concat(String.valueOf(idRichiesta)));
				} else {
					MessaggioRichiestaModel newMessaggioRichiestaModel = messaggioMapper.convertToModel(messaggio);
					newMessaggioRichiestaModel.setRichiestaModificaSuolo(richiestaModificaSuoloModel);
					newMessaggioRichiestaModel.setRelSuoloDichiarato(null);
					newMessaggioRichiestaModel.setUtente(username);

					messaggiRichiestaDao.save(newMessaggioRichiestaModel);
				}
			});
			log.debug("END - insertMessaggiRichiesta # {} Messaggi inseriti per la idRichiesta {}", listMessaggioRichiestaDto.size(), idRichiesta);
		}
	}

	public RisultatiPaginati<DocumentazioneRichiestaDto> ricercaDocumentiRichiestaModificaSuolo(Long idRichiesta, Paginazione paginazione, Ordinamento ordinamento) {
		log.debug("START - ricercaDocumentiRichiestaModificaSuolo per {idRichiesta} {}", idRichiesta);

		Page<DocumentazioneRichiestaModificaSuoloModel> page = documentazioneRichiestaDao.findAll(DocumentazioneRichiestaSpecificationBuilder.filterByIdRichiestaAndIdDocumento(idRichiesta, null),
				PageableBuilder.build().from(paginazione, ordinamento));
		List<DocumentazioneRichiestaDto> risultati = documentazioneMapper.from(page);

		log.debug("END - ricercaDocumentiRichiestaModificaSuolo # {}  Messaggi trovati per la idRichiesta {}", page.getTotalElements(), idRichiesta);
		return RisultatiPaginati.of(risultati, page.getTotalElements());
	}

	public DocumentazioneRichiestaDto ricercaDocumentoRichiestaModificaSuolo(Long idRichiesta, Long idDocumento) {
		log.debug("START - ricercaDocumentoRichiestaModificaSuolo per {idDocumento} {} e {idRichiesta} {}", idDocumento, idRichiesta);
		Optional<DocumentazioneRichiestaModificaSuoloModel> documentoOpt = documentazioneRichiestaDao
				.findOne(DocumentazioneRichiestaSpecificationBuilder.filterByIdRichiestaAndIdDocumento(idRichiesta, idDocumento));
		if (!documentoOpt.isPresent()) {
			log.error("DocumentazioneRichiestaModificaSuoloModel non trovata per idDocumento e idRichiesta {}, {}", idDocumento, idRichiesta);
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance(
					"DocumentazioneRichiestaModificaSuoloModel non trovata per idDocumento ".concat(String.valueOf(idDocumento)).concat(" e idRichiesta ").concat(String.valueOf(idRichiesta)));
		} else {

			DocumentazioneRichiestaDto doc = documentazioneMapper.fromModelIgnoringContent(documentoOpt.get());
			doc.setDocContent(documentoOpt.get().getDocContent());

			log.debug("END - ricercaDocumentoRichiestaModificaSuolo");
			return doc;

		}

	}

	@Transactional(rollbackFor = { SuoloException.class })
	public void uploadDocumentiRichiestaModificaSuolo(Long idRichiesta, DocumentazioneRichiestaDto doc, MultipartFile file) {

		if (doc != null && file != null) {
			Optional<RichiestaModificaSuoloModel> richiestaModificaSuolOpt = richiestaModificaSuoloDao.findById(idRichiesta);
			if (!richiestaModificaSuolOpt.isPresent()) {
				log.error(RICHIESTA_NON_TROVATA, idRichiesta);
				throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Entita non trovata per richiesta modifica suolo ".concat(String.valueOf(idRichiesta)));
			} else {
				RichiestaModificaSuoloModel richiestaModificaSuoloModel = richiestaModificaSuolOpt.get();
				uploadDocumentiRichiestaModificaSuolo(richiestaModificaSuoloModel, doc, file);
			}
		} else {
			log.error("Dati in input non validi per la richiesta {}", idRichiesta);
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Dati in input non validi per la richiesta ".concat(String.valueOf(idRichiesta)));
		}
	}

	protected void uploadDocumentiRichiestaModificaSuolo(RichiestaModificaSuoloModel richiestaModel, DocumentazioneRichiestaDto documentazioneDto, MultipartFile file) {
		log.debug("START - uploadDocumentiRichiestaModificaSuolo ");

		String nomeFile;
		try {
			DocumentazioneRichiestaModificaSuoloModel doc = new DocumentazioneRichiestaModificaSuoloModel();
			doc.setDocContent(file.getBytes());
			nomeFile = StringUtils.cleanPath(file.getOriginalFilename());
			doc.setDataInserimento(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
			doc.setDescrizione(documentazioneDto.getDescrizione());
			doc.setNomeFile(nomeFile);
			doc.setProfiloUtente(documentazioneDto.getProfiloUtente());
			doc.setUtente(utenteComponent.username());
			doc.setRichiestaModificaSuolo(richiestaModel);
			doc.setDimensione(documentazioneDto.getDimensione());

			documentazioneRichiestaDao.save(doc);
		} catch (IOException e) {
			log.error("Errore leggendo i dati del file allegato", e);
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Errore leggendo i dati del file allegato");
		}
		log.debug("END - uploadDocumentiRichiestaModificaSuolo {}", nomeFile);

	}

	public void cancellaDocumentoRichiestaModificaSuolo(Long idRichiesta, Long idDocumento) {
		log.debug("START - cancellaDocumentoRichiestaModificaSuolo");
		if (Objects.nonNull(idDocumento) && Objects.nonNull(idRichiesta)) {
			log.debug("START - cancellaDocumentoRichiestaModificaSuolo per {idDocumento} {} e {idRichiesta} {}", idDocumento, idRichiesta);
			Optional<DocumentazioneRichiestaModificaSuoloModel> documentoOpt = documentazioneRichiestaDao
					.findOne(DocumentazioneRichiestaSpecificationBuilder.filterByIdRichiestaAndIdDocumento(idRichiesta, idDocumento));
			if (!documentoOpt.isPresent()) {
				log.error("DocumentazioneRichiestaModificaSuoloModel non trovata per idRichiesta e idDocumento {}", idRichiesta, idDocumento);
				throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("DocumentazioneRichiestaModificaSuoloModel non trovata per per la richiesta "
						.concat(String.valueOf(idRichiesta)).concat(" e il documento ").concat(String.valueOf(idDocumento)));
			} else {
				documentazioneRichiestaDao.delete(documentoOpt.get());
				log.debug("END - cancellaDocumentoRichiestaModificaSuolo");
			}
		} else {
			log.error("Dati in input non validi per la idRichiesta e idDocumento {}", idRichiesta, idDocumento);
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION
					.newSuoloExceptionInstance("Dati in input non validi per la richiesta ".concat(String.valueOf(idRichiesta)).concat(" e il documento ").concat(String.valueOf(idDocumento)));
		}
		log.debug("END - cancellaDocumentoRichiestaModificaSuolo idDocumento o idRichiesta null");
	}

	@Transactional
	public void updateDichiarati(Long idRichiesta, List<SuoloDichiaratoLavorazioneDto> suoloDichiaratoLavorazioneDtoList) {
		log.debug("START - updateDichiarati");
		try {

			RichiestaModificaSuoloModel richiestaModificaSuolo = richiestaModificaSuoloDao.getOne(idRichiesta);

			for (SuoloDichiaratoLavorazioneDto dichiaratoInput : suoloDichiaratoLavorazioneDtoList) {

				SuoloDichiaratoModel suoloDichiarato = suoloDichiaratoDao.getOne(dichiaratoInput.getId());
				if (suoloDichiarato.getLavorazioneSuolo() == null) {

					checkAttributiObbligatori(suoloDichiaratoLavorazioneMapper.convertToSuoloDichiaratoModel(dichiaratoInput), richiestaModificaSuolo.getStato(), richiestaModificaSuolo.getCampagna());

					// Aggiorna solo poligoni a cui non sono associate Lavorazioni
					suoloDichiarato.setVisibileInOrtofoto(dichiaratoInput.getVisibileInOrtofoto());
					suoloDichiarato.setTipoInterventoColturale(dichiaratoInput.getTipoInterventoColturale());
					suoloDichiarato.setNuovaModificaCaa(dichiaratoInput.getNuovaModificaCaa());
					suoloDichiarato.setInterventoInizio(dichiaratoInput.getInterventoInizio());
					suoloDichiarato.setInterventoFine(dichiaratoInput.getInterventoFine());
					suoloDichiaratoDao.saveAndFlush(suoloDichiarato);
				}
			}
		} catch (SuoloException e) {
			log.error("Errore durante update dichiarati", e);
			throw e;
		} catch (Exception e) {
			log.error("Errore durante update dichiarati", e);
			throw SuoloException.ExceptionType.NOT_FOUND_EXCEPTION.newSuoloExceptionInstance("Errore durante update dichiarati");
		}
		log.debug("END - updateDichiarati");
	}

	private void checkAttributiObbligatori(SuoloDichiaratoModel dichiaratoInput, StatoRichiestaModificaSuolo statoRichiesta, long campagna) {
		// 124
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		if (statoRichiesta != StatoRichiestaModificaSuolo.APERTA) {
			if ((dichiaratoInput.getVisibileInOrtofoto() == null || dichiaratoInput.getVisibileInOrtofoto() == false)
					&& (dichiaratoInput.getTipoInterventoColturale() == null || dichiaratoInput.getInterventoInizio() == null || dichiaratoInput.getInterventoFine() == null)) {
				throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION
						.newSuoloExceptionInstance("Errore, ci sono poligoni di dichiarato non visibili da ortofoto con tipo di intervento e periodo non valorizzati!");
			}
		}

		LocalDateTime dataFineCampagna = LocalDateTime.parse(campagna + "-12-31 23:59:59", formatter);

		if (dichiaratoInput.getInterventoInizio() != null &&  dichiaratoInput.getInterventoInizio().isAfter(dataFineCampagna)) {
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Errore, date non coerenti con campagna!");

		}

		if (dichiaratoInput.getInterventoFine() != null && dichiaratoInput.getInterventoFine().isAfter(dataFineCampagna)) {
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Errore, date non coerenti con campagna!");

		}

		if (dichiaratoInput.getInterventoInizio() != null && dichiaratoInput.getInterventoFine() != null && (dichiaratoInput.getInterventoFine().isBefore(dichiaratoInput.getInterventoInizio()))) {
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Errore, date non coerenti!");

		}
	}
}
