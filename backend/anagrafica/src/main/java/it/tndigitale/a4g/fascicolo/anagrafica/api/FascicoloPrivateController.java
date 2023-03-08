package it.tndigitale.a4g.fascicolo.anagrafica.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.FascicoloDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.legacy.SincronizzazioneAgsException;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.*;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato.MandatoService;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.utente.AbilitazioniComponent;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.ApriFascicoloDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.EsitoControlloDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.FascicoloDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.FascicoloMigrationResultDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.ReportValidazioneDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.SchedaValidazioneFascicoloDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.TipoDetenzioneEnum;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.FascicoloNonValidabileException;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.StatoFascicoloEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping(ApiUrls.FASCICOLO_PRIVATE)
@Api(value = "Servizio che ha la responsabilita di gestire il ciclo di vita del fascicolo dalla sua apertura fino alla sua chiusura")
public class FascicoloPrivateController {

	private static final Logger logger = LoggerFactory.getLogger(FascicoloPrivateController.class);
	@Autowired private ValidazioneFascicoloService validazioneFascicoloService;
	@Autowired private MandatoService mandatoService;
	@Autowired private RicercaFascicoloService ricercaFascicoloService;
	@Autowired private ProtocollazioneSchedaValidazioneService protocollazioneSchedaValidazioneService;
	@Autowired private FascicoloService fascicoloService;
	@Autowired private AbilitazioniComponent abilitazioniComponent;
	@Autowired private ProtocollazioneSchedaValidazioneService protocollazioneSchedaService;
	@Autowired private FascicoloDao fascicoloDao;

	/**
	 * @return elenco di fascicoli in stato CONTROLLO_IN_CORSO
	 */
	@ApiOperation("Restituisce l'esito per ogni controllo di completezza del fascicolo. I controlli danno esituo TRUE se superati con successo, FALSE altrimenti.")
	@GetMapping("/controlli-in-corso")
	public List<FascicoloDto> getElencoFascicoliInStatoControlliInCorso() {
		return validazioneFascicoloService.getElencoFascicoliInStatoControlliInCorso();
	}

	/**
	 * @param cuaa
	 * @return
	 * @throws NoSuchElementException
	 */
	@ApiOperation("Restituisce l'esito per ogni controllo di completezza del fascicolo. I controlli danno esituo TRUE se superati con successo, FALSE altrimenti.")
	@GetMapping("/{cuaa}/controllo-completezza")
	public Map<ControlliFascicoloAnagraficaCompletoEnum, EsitoControlloDto> queryControlloCompletezzaFascicolo(
			@ApiParam(value = "Cuaa azienda per cui si vuole ottenere il report", required = true)
			@PathVariable(required = true) final String cuaa) throws NoSuchElementException {
		try {
			return validazioneFascicoloService.queryControlloCompletezzaFascicolo(cuaa);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT);
		}
	}

	@ApiOperation("Restituisce l'esito per ogni controllo di completezza del fascicolo. I controlli danno esito TRUE se superati con successo, FALSE altrimenti.")
	@PutMapping("/{cuaa}/start-controllo-completezza")
	public void startControlloCompletezzaFascicoloAsincrono(
			@ApiParam(value = "Cuaa azienda per cui si vuole ottenere il report", required = true)
			@PathVariable(required = true) final String cuaa) {
		validazioneFascicoloService.startControlloCompletezzaFascicoloAsincrono(cuaa, 0);
	}

	/**
	 * Ideato per cambio di stato del fascicolo da parte di Mediator.
	 * @param cuaa
	 * @throws NoSuchElementException
	 */
	@ApiOperation("Servizio utilizzato per il cambio di stato di un fascicolo aziendale ALLA_FIRMA_CAA")
	@PutMapping("/{cuaa}/stato/alla-firma-caa")
	@PreAuthorize("@abilitazioniComponent.checkAperturaFascicolo(#cuaa)")
	public void putFascicoloStatoAllaFirmaCaa(
			@ApiParam(value = "Cuaa azienda per cui si vuole movimentare lo stato in ALLA_FIRMA_CAA", required = true)
			@PathVariable(required = true) final String cuaa,
			@RequestParam MultipartFile schedaValidazione,
			@RequestParam Long idSchedaValidazione) throws NoSuchElementException {
		try {
			validazioneFascicoloService.setStatoFascicolo(cuaa, StatoFascicoloEnum.ALLA_FIRMA_CAA);
			validazioneFascicoloService.setSchedaValidazione(cuaa, schedaValidazione, idSchedaValidazione);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * Ideato per cambio di stato del fascicolo da parte di Mediator.
	 * @param cuaa
	 * @throws NoSuchElementException
	 */
	@ApiOperation("Servizio utilizzato per il cambio di stato di un fascicolo aziendale CONTROLLI_IN_CORSO")
	@PutMapping("/{cuaa}/stato/controlli-in-corso")
//	@PreAuthorize("@abilitazioniComponent.checkAperturaFascicolo(#cuaa)")
	public void putFascicoloStatoControlliInCorso(
			@ApiParam(value = "Cuaa azienda per cui si vuole movimentare lo stato in CONTROLLI_IN_CORSO", required = true)
			@PathVariable(required = true) final String cuaa) throws NoSuchElementException {
		try {
			validazioneFascicoloService.setStatoFascicolo(cuaa, StatoFascicoloEnum.CONTROLLI_IN_CORSO);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT);
		}
	}

	/**
	 * Ideato per cambio di stato del fascicolo da parte di Mediator.
	 * @param cuaa
	 * @throws NoSuchElementException
	 */
	@ApiOperation("Servizio utilizzato per il cambio di stato di un fascicolo aziendale in CONTROLLATO_OK")
	@PutMapping("/{cuaa}/stato/controllato-ok")
	public void putFascicoloStatoControllatoOk(
			@ApiParam(value = "Cuaa azienda per cui si vuole movimentare lo stato in CONTROLLATO_OK", required = true)
			@PathVariable(required = true) final String cuaa) throws NoSuchElementException {
		try {
			validazioneFascicoloService.setStatoFascicolo(cuaa, StatoFascicoloEnum.CONTROLLATO_OK);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT);
		}
	}

	/**
	 * Ideato per cambio di stato del fascicolo da parte di Mediator.
	 * @param cuaa
	 * @throws NoSuchElementException
	 */
	@ApiOperation("Servizio utilizzato per il cambio di stato di un fascicolo aziendale in IN_AGGIORNAMENTO")
	@PutMapping("/{cuaa}/stato/in-aggiornamento")
	public void putFascicoloStatoControlliInAggiornamento(
			@ApiParam(value = "Cuaa azienda per cui si vuole movimentare lo stato in IN_AGGIORNAMENTO", required = true)
			@PathVariable(required = true) final String cuaa) throws NoSuchElementException {
		try {
			validazioneFascicoloService.setStatoFascicolo(cuaa, StatoFascicoloEnum.IN_AGGIORNAMENTO);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * Ideato per cambio di stato del fascicolo da parte di Mediator a seguito di annullo iter validazione
	 * @param cuaa
	 * @throws NoSuchElementException
	 */
	@ApiOperation("Servizio che permette di annullare l'iter di validazione di un fascicolo")
	@PutMapping("/{cuaa}/annulla-iter-validazione")
	public void annullaIterValidazione(
			@ApiParam(value = "Cuaa del fascicolo per cui si vuole annullare l'iter di validazione", required = true)
			@PathVariable(value = "cuaa", required = true) final String cuaa) {
		try {
			fascicoloService.annullaIterValidazione(cuaa);
		} catch (FascicoloInvalidConditionException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@ApiOperation("Restituisce il report dei dati anagrafici del fascicolo")
	@GetMapping("/{cuaa}/report-scheda-validazione")
	@PreAuthorize("@abilitazioniComponent.checkLetturaFascicolo(#cuaa)")
	public ReportValidazioneDto getReportSchedaValidazione(
			@ApiParam(value = "Cuaa azienda per cui si vuole ottenere il report", required = true)
			@PathVariable(required = true) final String cuaa) {
		try {
			return validazioneFascicoloService.getReportValidazione(cuaa);
		} catch (RestClientException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		} catch (FascicoloInvalidConditionException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}	
		
	@ApiOperation("Servizio che permette di validare una detezione autonoma")
	@PostMapping("/{cuaa}/validazione-detenzione-autonoma")
	public void validazioneFascicoloAutonomoAsincrona(
			@ApiParam(value = "Codice fiscale per cui si vuole aprire un nuovo fascicolo", required = true)
			@PathVariable(value = "cuaa", required = true) String cuaa,
			@RequestParam(required = false) Integer idValidazione,
			@RequestParam(required = false) String username) {
		try {
			Optional<FascicoloModel> fascicoloLiveOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0);
			FascicoloModel fascicoloLive = fascicoloLiveOpt.orElseThrow();
			validazioneFascicoloService.validazioneFascicoloAutonomo(fascicoloLive, cuaa, idValidazione, username);
		} catch (NoSuchElementException e) {
			logger.error("No Such Element", e);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		} catch (EntityNotFoundException e) {
			logger.error("No content", e);
			throw new ResponseStatusException(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			logger.error("Eccezione generica", e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		}
	}

	@ApiOperation("Servizio che permette di validare un mandato")
	@PostMapping("/{cuaa}/validazione-mandato")
	public void validazioneFascicoloAsincrona(
			@ApiParam(value = "Codice fiscale per cui si vuole aprire un nuovo fascicolo", required = true)
			@PathVariable(value = "cuaa", required = true) String cuaa,
			@RequestParam(required = false) Integer idValidazione,
			@RequestParam(required = false) String username) {
		try {
			Optional<FascicoloModel> fascicoloLiveOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0);
			FascicoloModel fascicoloLive = fascicoloLiveOpt.orElseThrow();
			validazioneFascicoloService.validazioneFascicolo(fascicoloLive, cuaa, idValidazione, username);
		} catch (NoSuchElementException e) {
			logger.error("No Such Element", e);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		} catch (EntityNotFoundException e) {
			logger.error("No content", e);
			throw new ResponseStatusException(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			logger.error("Eccezione generica", e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		}
	}

	@ApiOperation("Servizio che permette di notificare al caa una richiesta di validazione accettata")
	@PostMapping("/{cuaa}/notifica-caa-validazione-accettata")
	public void notificaMailCaaRichiestaValidazioneAccettata(
			@ApiParam(value = "Codice fiscale per cui si vuole aprire un nuovo fascicolo", required = true)
			@PathVariable(value = "cuaa", required = true) String cuaa) {
		mandatoService.notificaMailCaaRichiestaValidazioneAccettata(cuaa);
	}

	@ApiOperation("Recupero fascicolo per CUAA azienda agricola")
	@GetMapping("/{cuaa}")
	public FascicoloDto getByCuaa(
			@ApiParam(value = "CUAA azienda agricola", required = true) @PathVariable(value = "cuaa") String cuaa,
			@RequestParam(required = false) Integer idValidazione) throws Exception {
		try {
			return ricercaFascicoloService.getFascicoloDto(cuaa, idValidazione == null ? 0 : idValidazione);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		} catch (EntityNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT);
		}
	}

	@ApiOperation("Servizio che permette di protocollare una scheda di validazione")
	@PostMapping("/{cuaa}/protocolla-scheda-validazione")
	public void protocollaSchedaValidazione(
			@ApiParam(value = "CUAA azienda agricola", required = true) @PathVariable(value = "cuaa") String cuaa,
			@RequestParam(required = false) Integer nextIdValidazione,
			@RequestParam MultipartFile report,
			@RequestParam TipoDetenzioneEnum tipoDetenzioneEnum,
			@RequestPart(required = false, value = "allegati") List<MultipartFile> allegati
			) throws IOException, FascicoloNonValidabileException {

		List<ByteArrayResource> attachments = null;
		if (!CollectionUtils.isEmpty(allegati)) {
			attachments = new ArrayList<>();
			for (MultipartFile allegato : allegati) {
				attachments.add(new ByteArrayResource(allegato.getBytes()) {
					@Override
					public String getFilename() {
						return allegato.getOriginalFilename();
					}
				});
			}
		}

		SchedaValidazioneFascicoloDto schedaValidazioneFascicoloDto = new SchedaValidazioneFascicoloDto();
		schedaValidazioneFascicoloDto.setCodiceFiscale(cuaa);
		schedaValidazioneFascicoloDto.setNextIdValidazione(nextIdValidazione);
		schedaValidazioneFascicoloDto.setReport(new ByteArrayResource(report.getBytes()));
		schedaValidazioneFascicoloDto.setTipoDetenzione(tipoDetenzioneEnum == null ? TipoDetenzioneEnum.MANDATO : tipoDetenzioneEnum);
		schedaValidazioneFascicoloDto.setAllegati(attachments);
		protocollazioneSchedaValidazioneService.protocolla(schedaValidazioneFascicoloDto);
	}

	@ApiOperation("Servizio che permette di sincronizzare con ags")
	@PostMapping("/{cuaa}/sincronizza-ags")
	public void sincronizzazioneAgs(
			@ApiParam(value = "Codice fiscale per cui si vuole aprire un nuovo fascicolo", required = true)
			@PathVariable(value = "cuaa", required = true) String cuaa,
			@RequestParam(required = false) Integer idValidazione) throws SincronizzazioneAgsException {
		fascicoloService.sincronizzaAgs(cuaa, idValidazione);
	}

	@ApiOperation("Verifica se l'utente connesso possiede diritti di firma o è un utente caa ")
	@GetMapping("/{cuaa}/controllo-diritti-firma-caa")
	public boolean isAventeDirittoFirmaOrCaa(
			@ApiParam(value = "CUAA azienda agricola", required = true) @PathVariable(value = "cuaa") String cuaa){
			return abilitazioniComponent.isAventeDirittoFirmaOrCaa(cuaa);
	}

	@ApiOperation("Servizio che permette di notificare al caa che la scheda di validazione firmata e' stata caricata")
	@PostMapping("/{cuaa}/notifica-caa-scheda-validazione-caricata")
	public void notificaMailCaaSchedaValidazioneAccettata(
			@ApiParam(value = "Codice fiscale per cui si vuole aprire un nuovo fascicolo", required = true)
			@PathVariable(value = "cuaa", required = true) String cuaa) {
		protocollazioneSchedaService.notificaMailCaaSchedaValidazioneAccettata(cuaa);
	}

	@ApiOperation("Rimozione dei controlli di completezza")
	@DeleteMapping("/{cuaa}/controllo-completezza")
	public void rimozioneControlliCompletezza(
			@PathVariable @ApiParam(value = "CUAA azienda agricola", required = true) final String cuaa) {
		validazioneFascicoloService.rimozioneControlliCompletezza(cuaa);
	}

	/**
	 * @param cuaa
	 * @return
	 * @throws NoSuchElementException
	 */
	@ApiOperation("Restituisce l'esito per ogni controllo di completezza del fascicolo. I controlli danno esituo TRUE se superati con successo, FALSE altrimenti.")
	@GetMapping("/{cuaa}/controllo-completezza-sincrono")
	public Map<ControlliFascicoloAnagraficaCompletoEnum, EsitoControlloDto> controlloCompletezzaFascicoloSincrono(
			@ApiParam(value = "Cuaa azienda per cui si vuole ottenere il report", required = true)
			@PathVariable(required = true) final String cuaa) throws NoSuchElementException {
		try {
			return validazioneFascicoloService.getControlloCompletezzaFascicolo(cuaa);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT);
		}
	}
	
	@ApiOperation("Servizio che permette di migrare un fascicolo da SIAP e resitituisce il nuovo cuaa")
	@PostMapping("/{cuaa}/migra")
	public FascicoloMigrationResultDto migra(
			@ApiParam(value = "Codice fiscale per cui si vuole migrare il fascicolo", required = true)
			@PathVariable(value = "cuaa", required = true) String cuaa,
			@RequestParam String codiceFiscaleRappresentante,
			@RequestParam(required = true) Long identificativoSportello,
			@RequestParam(required = true) MultipartFile contratto,
			@RequestPart(value = "allegati") List<MultipartFile> allegati,
			@RequestParam(required = true) Boolean migraModoPagamento) throws Exception {
		fascicoloService.lockFascicolo(cuaa);
		List<ByteArrayResource> attachments = null;
		if (!CollectionUtils.isEmpty(allegati)) {
			attachments = new ArrayList<>();
			for (MultipartFile allegato : allegati) {
				attachments.add(new ByteArrayResource(allegato.getBytes()) {
					@Override
					public String getFilename() {
						return allegato.getOriginalFilename();
					}
				});
			}
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					FascicoloValidazioneEnum.ALLEGATO_NON_PRESENTE.name());
		}
		ApriFascicoloDto datiApertura = new ApriFascicoloDto().setCodiceFiscale(cuaa)
				.setCodiceFiscaleRappresentante(codiceFiscaleRappresentante)
				.setIdentificativoSportello(identificativoSportello)
				.setContratto(new ByteArrayResource(contratto.getBytes(), contratto.getOriginalFilename()))
				.setAllegati(attachments);
		try {
			FascicoloMigrationResultDto fascicoloMigrato = fascicoloService.migra(datiApertura);
			if (migraModoPagamento) {
				fascicoloService.migraModiPagamento(cuaa);
			}
			
			return fascicoloMigrato;
		} catch (Exception e) {
			logger.error("Errore in fase di migrazione del fascicolo per il cuaa {}", cuaa, e);
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	/**
	 * Ideato per aggiornare l'id della scheda di validazione
	 * @return 
	 * @throws NoSuchElementException
	 */
	@ApiOperation("Servizio utilizzato per ottenere un nuovo id scheda di validazione")
	@GetMapping("/new-id-scheda-validazione")
	public Long getNewIdSchedaValidazione() throws NoSuchElementException {
		Long nextPdfId = null;
		try {
			nextPdfId = validazioneFascicoloService.getNewIdSchedaValidazione();
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT);
		}
		return nextPdfId;
	}
	
}
