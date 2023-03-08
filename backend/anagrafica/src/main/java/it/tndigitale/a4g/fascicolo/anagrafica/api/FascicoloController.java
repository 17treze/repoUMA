package it.tndigitale.a4g.fascicolo.anagrafica.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.ModoPagamentoModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.*;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.utente.AbilitazioniComponent;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.*;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.StatoFascicoloLegacy;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.TipoDetenzioneAgs;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.DatiAperturaFascicoloDto;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.FascicoloNonValidabileException;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.StatoFascicoloEnum;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping(ApiUrls.FASCICOLO)
@Api(value = "Servizio che ha la responsabilita di gestire il ciclo di vita del fascicolo dalla sua apertura fino alla sua chiusura")
public class FascicoloController {

	@Autowired private FascicoloService fascicoloService;
	@Autowired private RicercaFascicoloService ricercaFascicoloService;
	@Autowired private ValidazioneFascicoloService validazioneFascicoloService;
	@Autowired private AbilitazioniComponent abilitazioniComponent;
	@Autowired private MovimentazioneFascicoloService movimentazioneFascicoloService;
	
	@ApiOperation("Dato un codice fiscale verifica se sia possibile aprire un fascicolo per quel codice")
	@GetMapping("/{cuaa}/verifica/dati-apertura")
	@PreAuthorize("@abilitazioniComponent.checkAperturaFascicolo(#cuaa)")
	public DatiAperturaFascicoloDto verificaAperturaFascicolo(
			@ApiParam(value = "Codice fiscale per cui si vuole verificare se vi sono le condizioni per aprire un nuovo fascicolo", required = true)
			@PathVariable(value = "cuaa") String cuaa)
					throws FascicoloValidazioneException {
		try {
			return fascicoloService.validaOperazioneFascicolo(cuaa, FascicoloOperationEnum.VERIFICA_APERTURA);
		} catch (FascicoloValidazioneException afve) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					afve.getMessage());
		}
	}

	@ApiOperation("Dato un codice fiscale verifica se sia possibile aprire un fascicolo per quel codice")
	@GetMapping("/{cuaa}/verifica/dati-apertura-trasferimento")
	@PreAuthorize("@abilitazioniComponent.checkAperturaFascicolo(#cuaa)")
	public DatiAperturaFascicoloDto verificaAperturaTrasferimentoFascicolo(
			@ApiParam(value = "Codice fiscale per cui si vuole verificare se vi sono le condizioni per aprire un nuovo fascicolo", required = true)
			@PathVariable(value = "cuaa") String cuaa) throws FascicoloValidazioneException {
		try {
			return fascicoloService.validaOperazioneFascicolo(cuaa, FascicoloOperationEnum.VERIFICA_APERTURA_TRASFERIMENTO);
		} catch (FascicoloValidazioneException afve) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, afve.getMessage());
		}
	}

	@ApiOperation("Dato un codice fiscale verifica se sia possibile trasferire un fascicolo per quel codice")
	@GetMapping("/{cuaa}/verifica/dati-trasferimento")
	@PreAuthorize("@abilitazioniComponent.checkAperturaFascicolo(#cuaa)")
	public DatiAperturaFascicoloDto verificaTrasferimentoFascicolo(
			@ApiParam(value = "Codice fiscale per cui si vuole verificare se vi sono le condizioni per trasferire un fascicolo", required = true)
			@PathVariable(value = "cuaa") String cuaa)
					throws FascicoloValidazioneException {
		try {
			return fascicoloService.validaOperazioneFascicolo(cuaa, FascicoloOperationEnum.VERIFICA_TRASFERIMENTO);			
		} catch (FascicoloValidazioneException afve) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, afve.getMessage());
		}
	}

	@ApiOperation("Dato un codice fiscale verifica se sia possibile trasferire un fascicolo per quel codice")
	@GetMapping("/{cuaa}/verifica/dati-trasferimento-chiusura")
	@PreAuthorize("@abilitazioniComponent.checkAperturaFascicolo(#cuaa)")
	public DatiAperturaFascicoloDto verificaTrasferimentoChiusuraFascicolo(
			@ApiParam(value = "CUAA per cui si vuole verificare se vi sono le condizioni per trasferire un fascicolo", required = true)
			@PathVariable(value = "cuaa") String cuaa)
					throws FascicoloValidazioneException {
		try {
			return fascicoloService.validaOperazioneFascicolo(cuaa, FascicoloOperationEnum.VERIFICA_TRASFERIMENTO_CHIUSURA);			
		} catch (FascicoloValidazioneException afve) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, afve.getMessage());
		}
	}

	@ApiOperation("Servizio che permette di aprire un nuovo fascicolo e resitituisce il nuovo cuaa")
	@PostMapping("/{cuaa}/apri")
	@PreAuthorize("@abilitazioniComponent.checkAperturaFascicolo(#cuaa)")
	public FascicoloCreationResultDto apri(
			@ApiParam(value = "Codice fiscale per cui si vuole aprire un nuovo fascicolo", required = true)
			@PathVariable(value = "cuaa", required = true) String cuaa,
			@RequestParam String codiceFiscaleRappresentante,
			@RequestParam(required = true) Long identificativoSportello,
			@RequestParam(required = true) MultipartFile contratto,
			@RequestPart(value = "allegati") List<MultipartFile> allegati) throws Exception {
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
			return fascicoloService.apri(datiApertura);
		} catch (Exception e) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@ApiOperation("Servizio che permette di aprire e trsferire un nuovo fascicolo e resitituisce il nuovo cuaa")
	@PostMapping("/{cuaa}/trasferisci")
	@PreAuthorize("@abilitazioniComponent.checkAperturaFascicolo(#cuaa)")
	public FascicoloCreationResultDto apriETrasferisci(
			@ApiParam(value = "Codice fiscale per cui si vuole aprire e trasferire un nuovo fascicolo", required = true)
			@PathVariable(value = "cuaa", required = true) String cuaa,
			@RequestParam String codiceFiscaleRappresentante, @RequestParam(required = true) Long identificativoSportello,
			@RequestParam(required = true) MultipartFile contratto,
			@RequestPart(value = "allegati") List<MultipartFile> allegati,
			@RequestParam(required = true) FascicoloOperationEnum fascicoloOperationEnum) throws Exception {
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
			return fascicoloService.trasferisci(datiApertura, fascicoloOperationEnum);
		} catch (Exception e) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@ApiOperation("Servizio che stampa il modulo di richiesta trasferimento OP")
	@GetMapping("/{cuaa}/report-richiesta-trasferimento-op")
	@PreAuthorize("@abilitazioniComponent.checkAperturaFascicolo(#cuaa)")
	public byte[] getReportRichiestaTrasferimentoOp(
			@ApiParam(value = "Cuaa azienda per cui si vuole ottenere il modulo", required = true) 
			@PathVariable(value = "cuaa") String cuaa,
			@RequestParam(required = true) FascicoloOperationEnum fascicoloOperationEnum) {
		try {
			return fascicoloService.richiestaTrasferimentoOp(cuaa, fascicoloOperationEnum);
		} catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage());
		}
	}

	@ApiOperation("Servizio che permette di aggiornare un fascicolo con dati dai servizi di anagrafica tributaria e anagrafe impresa")
	@GetMapping("/{cuaa}/aggiorna")
	@PreAuthorize("@abilitazioniComponent.checkAperturaFascicolo(#cuaa)")
	public FascicoloCreationResultDto aggiorna(
			@ApiParam(value = "Codice fiscale per cui si vuole aggiornare un nuovo fascicolo", required = true)
			@PathVariable(value = "cuaa", required = true) final String cuaa)
					throws FascicoloAllaFirmaAziendaException, FascicoloAllaFirmaCAAException, FascicoloValidazioneException {
		return fascicoloService.aggiorna(cuaa);
	}

	@ApiOperation("Servizio che assegna lo stato 'alla firma azienda' ad un fascicolo")
	@PutMapping("/{cuaa}/stato/alla-firma-azienda")
	@PreAuthorize("@abilitazioniComponent.checkAperturaFascicolo(#cuaa)")
	public void setStatoAllaFirmaAzienda(
			@ApiParam(value = "Cuaa del fascicolo per cui si vuole impostare lo stato", required = true)
			@PathVariable(value = "cuaa", required = true) final String cuaa) {
		fascicoloService.setStatoAllaFirmaAzienda(cuaa);
	}

	@ApiOperation("Servizio che assegna lo stato 'in aggiornamento' ad un fascicolo")
	@PutMapping("/{cuaa}/stato/in-aggiornamento")
	@PreAuthorize("@abilitazioniComponent.checkAperturaFascicolo(#cuaa)")
	public void setStatoInAggiornamento(
			@ApiParam(value = "Cuaa del fascicolo per cui si vuole impostare lo stato", required = true)
			@PathVariable(value = "cuaa", required = true) final String cuaa) {
		fascicoloService.setStatoInAggiornamento(cuaa);
	}

	@ApiOperation("Servizio che assegna lo stato 'In Aggiornamento' ad un fascicolo a seguito di un respingimento da parte del rappresentante legale con diritto di firma")
	@PutMapping("/{cuaa}/rappresentante-legale/respingi-validazione")
	@PreAuthorize("@abilitazioniComponent.isAventeDirittoFirma(#cuaa)")
	public void rappresentanteLegaleRespingiValidazione(
			@ApiParam(value = "Codice fiscale per cui si vuole respingere la validazione del fascicolo", required = true)
			@PathVariable(value = "cuaa", required = true) final String cuaa){
		fascicoloService.setRappresentanteLegaleRespingiValidazione(cuaa);
	}

	@ApiOperation("Servizio che permette di reperire le unita tecnico economiche associate al cuaa")
	@GetMapping("/{cuaa}/unita-tecnico-economiche")
	//	@PreAuthorize("@abilitazioniComponent.checkAperturaFascicolo(#cuaa)")
	public List<UnitaTecnicoEconomicheDto> unitaLocali(
			@ApiParam(value = "Codice fiscale di cui si vuole reperire le unita tecnico economiche", required = true)
			@PathVariable(value = "cuaa", required = true) final String cuaa,
			@RequestParam(required = false) Integer idValidazione) {
		return fascicoloService.unitaTecnicoEconomiche(cuaa, idValidazione == null ? 0 : idValidazione);
	}

	@ApiOperation("Recupero fascicolo per CUAA azienda agricola")
	@GetMapping("/{cuaa}")
	@PreAuthorize("@abilitazioniComponent.checkLetturaFascicolo(#cuaa)")
	public FascicoloDto getByCuaa(
			@ApiParam(value = "CUAA azienda agricola", required = true) @PathVariable(value = "cuaa") String cuaa,
			@RequestParam(required = false) Integer idValidazione) throws Exception {
		try {
			return ricercaFascicoloService.getFascicoloDto(cuaa, idValidazione == null ? 0 : idValidazione);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping
	@ApiOperation("Ricerca paginata dei fascicolo secondo i criteri di ricerca impostati")
	@PreAuthorize("@abilitazioniComponent.checkRicercaFascicolo()")
	public RisultatiPaginati<FascicoloDto> ricercaFascicolo(
			FascicoloFilter filter, Paginazione paginazione, Ordinamento ordinamento) {
		return ricercaFascicoloService.ricerca(filter, paginazione,
				Optional.ofNullable(ordinamento).filter(
						o -> o.getProprieta() != null).orElse(Ordinamento.DEFAULT_ORDER_BY));
	}

	@ApiOperation("Controllo se l'utente autenticato ha i permessi sul fascicolo specificato da cuaa")
	@GetMapping("/{cuaa}/check-apertura-fascicolo")
	public Boolean checkAperturaFascicolo(
			@ApiParam(value = "CUAA azienda agricola", required = true) @PathVariable(value = "cuaa") String cuaa,
			@RequestParam(required = false) Integer idValidazione) {
		return abilitazioniComponent.checkAperturaFascicolo(cuaa, idValidazione == null ? 0 : idValidazione);
	}

	@ApiOperation("Controllo se l'utente autenticato ha i permessi sul fascicolo specificato da cuaa")
	@GetMapping("/{cuaa}/check-lettura-fascicolo")
	public Boolean checkLetturaFascicolo(
			@ApiParam(value = "CUAA azienda agricola", required = true) @PathVariable(value = "cuaa") String cuaa,
			@RequestParam(required = false) Integer idValidazione) {
		return abilitazioniComponent.checkLetturaFascicolo(cuaa, idValidazione == null ? 0 : idValidazione);
	}

	@ApiOperation("Inserimento IBAN")
	@PutMapping("/{cuaa}/modo-pagamento")
	@PreAuthorize("@abilitazioniComponent.checkPermessiAggiornamentoModoPagamento(#cuaa)")
	public ModoPagamentoDto inserimentoModoPagamento(@PathVariable(value = "cuaa") @ApiParam(value = "CUAA azienda agricola", required = true) String cuaa,
			@RequestBody(required = true) ModoPagamentoDto modoPagamento) throws FascicoloAllaFirmaAziendaException, FascicoloAllaFirmaCAAException, FascicoloNonInChiusuraException, PersonaFisicaNonDecedutaException, EredeFascicoloException, PersonaNonFisicaException {
		try {
//			se lo stato del fascicolo e' IN_CHIUSURA si invoca inserimentoModoPagamentoErede; altrimenti per tutti gli altri stati si invoca inserimentoModoPagamento
			var fascicoloModel = fascicoloService.getFascicoloModelOrThrow(cuaa, 0);
			if (fascicoloModel.getStato().equals(StatoFascicoloEnum.IN_CHIUSURA)) {
				modoPagamento.setId(fascicoloService.inserimentoModoPagamentoErede(cuaa, modoPagamento));
				return modoPagamento;
			}
			ModoPagamentoModel modoPagamentoModel =fascicoloService.inserimentoModoPagamento(cuaa, modoPagamento);
			modoPagamento.setId(modoPagamentoModel.getId());
			modoPagamento.setIban(modoPagamentoModel.getIban());
			modoPagamento.setCittaFiliale(modoPagamentoModel.getCittaFiliale());
			modoPagamento.setDenominazioneFiliale(modoPagamentoModel.getDenominazioneFiliale());
			modoPagamento.setBic(modoPagamentoModel.getBic());
			modoPagamento.setDenominazioneIstituto(modoPagamentoModel.getDenominazioneIstituto());
			return modoPagamento;
		} catch (HttpClientErrorException e) {
			throw new ResponseStatusException(e.getStatusCode(), e.getMessage());
		}
	}

	@ApiOperation("Rimozione iban dal fascicolo ")
	@DeleteMapping("/{cuaa}/modo-pagamento/{id}")
	@PreAuthorize("@abilitazioniComponent.checkPermessiAggiornamentoModoPagamento(#cuaa)")
	public void rimozioneModoPagamento(
			@PathVariable @ApiParam(value = "CUAA azienda agricola", required = true) final String cuaa,
			@PathVariable @ApiParam(value = "Identificativo modo pagamento", required = true) final Long id)
					throws FascicoloAllaFirmaAziendaException, FascicoloAllaFirmaCAAException {
		fascicoloService.rimozioneModoPagamento(cuaa, id);
	}

	@ApiOperation("Elenco modi di pagamento associato al fascicolo")
	@GetMapping("/{cuaa}/modo-pagamento")
	@PreAuthorize("@abilitazioniComponent.checkPermessiVisualizzazioneModoPagamento(#cuaa)")
	public List<ModoPagamentoDto> elencoModoPagamento(
			@PathVariable @ApiParam(value = "CUAA azienda agricola", required = true) String cuaa,
			@RequestParam(required = false) Integer idValidazione) {
		return fascicoloService.elencoModoPagamento(cuaa, idValidazione == null ? 0 : idValidazione);
	}

	@ApiOperation("Servizio che restituisce una lista di fascicoli validati")
	@GetMapping("/{cuaa}/validati")
	@PreAuthorize("@abilitazioniComponent.checkLetturaFascicolo(#cuaa)")
	public RisultatiPaginati<ValidazioneFascicoloDto> getValidazioniFascicolo(
			@ApiParam(value = "Codice fiscale per cui si voglio mostrare le validazioni", required = true)
			@PathVariable(required = true) final String cuaa,
			@RequestParam(required = false) final Integer annoValidazione,
			final Paginazione paginazione,
			final Ordinamento ordinamento) {
		return validazioneFascicoloService.elencoValidazioni(annoValidazione, cuaa, paginazione,
				Optional.ofNullable(ordinamento).filter(
						o -> o.getProprieta() != null).orElse(Ordinamento.DEFAULT_ORDER_BY));
	}

	@ApiOperation("Upload della scheda di validazione firmata")
	@PutMapping("/{cuaa}/report-scheda-validazione-firmata")
	@PreAuthorize("@abilitazioniComponent.checkAperturaFascicolo(#cuaa)")
	public void postReportSchedaValidazioneFirmata(
			@ApiParam(value = "Cuaa del fascicolo al quale si riferisce la scheda di validazione")
			@PathVariable(required = true, value = "cuaa") String cuaa,
			@RequestParam(required = true) MultipartFile schedaValidazioneFirmata) throws IOException {
		try {
			validazioneFascicoloService.salvaSchedaValidazioneFirmata(cuaa, schedaValidazioneFirmata);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		} catch (FirmaDocumentoException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@ApiOperation("Restituisce la scheda di validazione del fascicolo firmata")
	@GetMapping("/{cuaa}/report-scheda-validazione-firmata")
	@PreAuthorize("@abilitazioniComponent.checkLetturaFascicolo(#cuaa)")
	public byte[] getReportSchedaValidazioneFascicoloFirmata(
			@ApiParam(value = "Cuaa azienda per cui si vuole ottenere il report", required = true)
			@PathVariable(required = true) final String cuaa) {
		return validazioneFascicoloService.getReportValidazioneFascicoloFirmata(cuaa);
	}

	@ApiOperation("Restituisce la scheda di validazione del fascicolo salvata sul db in relazione all'idValizione indicato")
	@GetMapping("/{cuaa}/report-scheda-validazione-firmata-db")
	@PreAuthorize("@abilitazioniComponent.checkLetturaFascicolo(#cuaa)")
	public byte[] getReportSchedaValidazioneFascicoloDb(
			@ApiParam(value = "Cuaa azienda per cui si vuole ottenere il report", required = true)
			@PathVariable(required = true) final String cuaa,
			@RequestParam(required = false) Integer idValidazione) {
		return validazioneFascicoloService.getReportValidazioneFascicoloDb(cuaa, idValidazione == null ? 0 : idValidazione);
	}

	@ApiOperation("Upload della scheda di validazione firmata dall'azienda")
	@PutMapping("/{cuaa}/report-scheda-validazione-firmata-firmatario")
	@PreAuthorize("@abilitazioniComponent.isAventeDirittoFirmaOrCaa(#cuaa)")
	public void postReportSchedaValidazioneFirmataFirmatario(
			@ApiParam(value = "Cuaa del fascicolo al quale si riferisce la scheda di validazione")
			@PathVariable(required = true, value = "cuaa") String cuaa,
			@RequestBody(required = true) byte[] schedaValidazioneFirmata) throws IOException {
		try {
			validazioneFascicoloService.salvaSchedaValidazioneFirmataFirmatario(cuaa, schedaValidazioneFirmata);
		} catch (NoSuchElementException e) {
			// cuaa in input non corrisponde ad un fascicolo
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		} catch (FirmaDocumentoException e) {
			// errore nella firma del documento
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (FascicoloNonValidabileException e) {
			// stato del fascicolo non conforme
			throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, e.getMessage());
		}
	}

	@ApiOperation("Restituisce la lista di possibili organizzazioni gestite dal sistema")
	@GetMapping("/organizzazioni")
	public List<OrganizzazioneDto> getAllOrganizzazioni(){
		return fascicoloService.getAllOrganizzazioni();
	}

	// G.De Vincentiis, 30 giugno 2021
	@ApiOperation("Servizio che legge le dichiarazioni associative per un fascicolo")
	@GetMapping("/{cuaa}/rappresentante-legale/autodichiarazioni-associative")
	@PreAuthorize("@abilitazioniComponent.checkLetturaDichiarazioniAssociative(#cuaa)")
	public List<DichiarazioneAssociativaDto> getDichiarazioniAssociative(
			@ApiParam(value = "Codice fiscale per cui si vuole leggere le dichiarazioni associative", required = true)
			@PathVariable(value = "cuaa", required = true) final String cuaa){
		return fascicoloService.getDichiarazioniAssociative(cuaa);
	}

	@ApiOperation("Servizio di chiusura di una autodichiarazione associativa")
	@PutMapping("/{cuaa}/rappresentante-legale/chiudi-autodichiarazione-associativa")
	@PreAuthorize("@abilitazioniComponent.checkEditDichiarazioniAssociative(#cuaa)")
	public void chiudiDichiarazioneAssociativa(
			@PathVariable @ApiParam(value = "CUAA azienda agricola", required = true) final String cuaa,
			@RequestBody(required = true) DichiarazioneAssociativaDto dichiarazioneAssociativaDto) {
		fascicoloService.chiudiDichiarazioneAssociativa(cuaa, dichiarazioneAssociativaDto);
	}

	@ApiOperation("Servizio di aggiunta di un'autodichiarazione associativa presentata da un rappresentante legale con diritto di firma")
	@PutMapping("/{cuaa}/rappresentante-legale/aggiungi-autodichiarazione-associativa")
	@PreAuthorize("@abilitazioniComponent.checkEditDichiarazioniAssociative(#cuaa)")
	public void rappresentanteLegaleAggiungiAutodichiarazioneAssociativa(
			@ApiParam(value = "Codice fiscale del rappresentante legale che presenta l'autodichiarazione associativa", required = true)
			@PathVariable(value = "cuaa", required = true) final String cuaa,
			@RequestBody(required = true) DichiarazioneAssociativaDto dichiarazioneAssociativaDto){
		try {
			fascicoloService.rappresentanteLegaleAggiungiAutodichiarazioneAssociativa(cuaa, dichiarazioneAssociativaDto);
		} catch (FascicoloOrganizzazioneException e) {
			throw new ResponseStatusException(HttpStatus.FOUND, e.getMessage());
		}
	}

	@ApiOperation("Servizio che verifica i requisiti per completare l'iter di trasferimento OP")
	@GetMapping("/{cuaa}/esito-richiesta-trasferimento-op")
	@PreAuthorize("@abilitazioniComponent.checkAperturaFascicolo(#cuaa)")
	public boolean getEsitoRichiestaTrasferimentoOp(
			@ApiParam(value = "Cuaa azienda per cui si vuole eseguire il controllo", required = true) 
			@PathVariable(value = "cuaa") String cuaa) {
		try {
			return fascicoloService.isRichiestaTrasferimentoOpCompleta(cuaa);
		} catch (NoSuchElementException | EntityNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage());
		} catch (FascicoloNonInAttesaTrasferimentoException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@ApiOperation("Servizio che permette di trasferire e chiudere un fascicolo")
	@PostMapping("/{cuaa}/trasferimento-chiusura")
	@PreAuthorize("@abilitazioniComponent.checkAperturaFascicolo(#cuaa)")
	public FascicoloCreationResultDto trasferisciEChiudi(
			@ApiParam(value = "Codice fiscale per cui si vuole aprire e trasferire un nuovo fascicolo", required = true)
			@PathVariable(value = "cuaa", required = true) String cuaa,
			@ApiParam(value = "Data chiusura fascicolo", required = true)
			@RequestBody(required = true)
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
			LocalDate dataChiusura) {
		try {
			return fascicoloService.trasferisciEChiudi(cuaa, dataChiusura);
		} catch (FascicoloValidazioneException afve) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, afve.getMessage());
		}
	}

	@ApiOperation("Servizio che completa l'iter di trasferimento OP")
	@GetMapping("/{cuaa}/completa-trasferimento-da-altro-op")
	@PreAuthorize("@abilitazioniComponent.checkAperturaFascicolo(#cuaa)")
	public FascicoloCreationResultDto completaTrasferimentoDaAltroOp(
			@ApiParam(value = "Codice fiscale per cui si vuole aggiornare un nuovo fascicolo", required = true)
			@PathVariable(value = "cuaa", required = true) final String cuaa) {
		try {
			return fascicoloService.completaTrasferimento(cuaa);
		} catch (NoSuchElementException | EntityNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage());
		} catch (FascicoloNonInAttesaTrasferimentoException | TrasferimentoSIANNonCompletatoException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@ApiOperation("Controllo se il fascicolo letto da AGS può essere migrato in A4G")
	@GetMapping("/{cuaa}/check-migra-fascicolo")
	public DatiAperturaFascicoloDto checkMigraFascicolo(
			@ApiParam(value = "CUAA azienda agricola", required = true) @PathVariable(value = "cuaa") String cuaa,
			@RequestParam(required = false) StatoFascicoloLegacy stato,
			@RequestParam(required = false) TipoDetenzioneAgs tipoDetenzione,
			@RequestParam(required = false) Long sportello) {
		try {
			return fascicoloService.checkMigraFascicolo(cuaa, stato, tipoDetenzione, sportello);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@ApiOperation("Servizio che assegna lo stato 'chiuso' ad un fascicolo")
	@PutMapping("/{cuaa}/stato/chiuso")
	@PreAuthorize("@abilitazioniComponent.checkAperturaFascicolo(#cuaa)")
	public void avviaProceduraChiusura(
			@ApiParam(value = "Cuaa del fascicolo per cui si vuole impostare lo stato", required = true)
			@PathVariable(value = "cuaa", required = true) final String cuaa) {
		try {
			fascicoloService.avviaProceduraChiusura(cuaa);
		}
		catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
		}
	}

	@ApiOperation("Servizio che assegna lo stato 'sospeso' ad un fascicolo")
	@PutMapping("/{cuaa}/sospendi")
	@PreAuthorize("@abilitazioniComponent.hasProfiloResponsabileFascicoloPat()")
	public void inserisciSospensione(
			@ApiParam(value = "Cuaa del fascicolo per cui si vuole impostare lo stato", required = true)
			@PathVariable(value = "cuaa", required = true) final String cuaa,
			@RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dataSospensione,
			@RequestParam(required = true) String motivazioneSospensione) {

		try {
			SospensioneDto sospensioneDto = new SospensioneDto()
					.setCuaa(cuaa)
					.setDataInizio(dataSospensione.atStartOfDay())
					.setMotivazioneInizio(motivazioneSospensione);
			movimentazioneFascicoloService.sospendi(sospensioneDto);
		}
		catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
		}
	}

	@ApiOperation("Servizio che rimuove lo stato 'sospeso' da un fascicolo")
	@PutMapping("/{cuaa}/rimuovi-sospensione")
	@PreAuthorize("@abilitazioniComponent.hasProfiloResponsabileFascicoloPat()")
	public void rimuoviSospensione(
			@ApiParam(value = "Cuaa del fascicolo per cui si vuole impostare lo stato", required = true)
			@PathVariable(value = "cuaa", required = true) final String cuaa,
			@RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dataSospensione,
			@RequestParam(required = true) String motivazioneSospensione) {

		try {
			SospensioneDto sospensioneDto = new SospensioneDto()
					.setCuaa(cuaa)
					.setDataFine(dataSospensione.atStartOfDay())
					.setMotivazioneFine(motivazioneSospensione);

			movimentazioneFascicoloService.rimuoviSospensione(sospensioneDto);
		}
		catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
		}
	}

	@ApiOperation("Dato un codice fiscale verifica se sia possibile riaprire un fascicolo per quel codice")
	@GetMapping("/{cuaa}/verifica/dati-riapertura")
	@PreAuthorize("@abilitazioniComponent.checkAperturaFascicolo(#cuaa)")
	public DatiAperturaFascicoloDto verificaRiaperturaFascicolo(
			@ApiParam(value = "CUAA per cui si vuole verificare se vi sono le condizioni per riaprire un fascicolo", required = true)
			@PathVariable(value = "cuaa") String cuaa)
					throws FascicoloValidazioneException {
		try {
			return fascicoloService.validaOperazioneFascicolo(cuaa, FascicoloOperationEnum.VERIFICA_RIAPERTURA);			
		} catch (FascicoloValidazioneException afve) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, afve.getMessage());
		}
	}

	@ApiOperation("Servizio che permette di ricostituire un fascicolo CHIUSO e resitituisce il nuovo cuaa")
	@PostMapping("/{cuaa}/ricostituisci")
	@PreAuthorize("@abilitazioniComponent.checkAperturaFascicolo(#cuaa)")
	public FascicoloCreationResultDto ricostituisci(
			@ApiParam(value = "Codice fiscale per cui si vuole ricostituire un fascicolo", required = true)
			@PathVariable(value = "cuaa", required = true) String cuaa,
			@RequestParam String codiceFiscaleRappresentante,
			@RequestParam(required = true) Long identificativoSportello,
			@RequestParam(required = true) MultipartFile contratto,
			@RequestPart(value = "allegati") List<MultipartFile> allegati) throws Exception {
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
			return movimentazioneFascicoloService.ricostituisci(datiApertura);
		} catch (Exception e) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@ApiOperation("Servizio che legge gli eredi del titolare di un fascicolo")
	@GetMapping("/{cuaa}/eredi")
	@PreAuthorize("@abilitazioniComponent.checkLetturaFascicolo(#cuaa)")
	public List<EredeDto> getEredi(
			@ApiParam(value = "CUAA del fascicolo", required = true)
			@PathVariable(value = "cuaa", required = true) final String cuaa){
		return fascicoloService.getEredi(cuaa);
	}

	@ApiOperation("Servizio che salva un erede del titolare del fascicolo")
	@PutMapping("/{cuaa}/salva-erede")
	@PreAuthorize("@abilitazioniComponent.checkAperturaFascicolo(#cuaa)")
	public void salvaErede(
			@ApiParam(value = "Cuaa del fascicolo ", required = true)
			@PathVariable(value = "cuaa", required = true) final String cuaa,
			@RequestParam(required = true) String cfErede) {

		try {
			fascicoloService.salvaErede(cuaa, cfErede);
		}
		catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
		}
	}
	
	@ApiOperation("Rimozione erede dal fascicolo ")
	@DeleteMapping("/{cuaa}/rimuovi-erede/{id}")
	@PreAuthorize("@abilitazioniComponent.checkAperturaFascicolo(#cuaa)")
	public void rimuoviErede(
			@PathVariable @ApiParam(value = "cuaa", required = true) final String cuaa,
			@PathVariable @ApiParam(value = "Identificativo erede", required = true) final Long id) throws FascicoloAllaFirmaAziendaException, FascicoloAllaFirmaCAAException {
		fascicoloService.rimuoviErede(cuaa, id);
	}
	
	@ApiOperation("Elenco sospensioni di un fascicolo")
	@GetMapping("/{cuaa}/dati-sospensione")
	@PreAuthorize("@abilitazioniComponent.hasProfiloResponsabileFascicoloPat()")
	public List<SospensioneDto> getSospensioniFascicolo(
			@PathVariable @ApiParam(value = "CUAA azienda agricola", required = true) String cuaa) {
		return fascicoloService.getSospensioniFascicolo(cuaa);
	}

}
