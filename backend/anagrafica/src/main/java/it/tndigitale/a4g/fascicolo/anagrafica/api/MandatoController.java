package it.tndigitale.a4g.fascicolo.anagrafica.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloValidazioneEnum;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato.MandatoService;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato.MandatoVerificaException;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato.RevocaImmediataMandatoNotification;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato.RevocaImmediataMandatoService;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.*;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.BusinessResponsesDto.Esiti;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.caa.SportelloCAADto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.DatiAperturaFascicoloDto;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicoloId;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(ApiUrls.MANDATO)
@Api(value = "Servizio che ha la responsabilita di gestire il ciclo di vita del mandato legato a un fascicolo")
public class MandatoController {
	
	private static final Logger logger = LoggerFactory.getLogger(MandatoController.class);

	@Autowired
	private MandatoService mandatoService;
	@Autowired
	private RevocaImmediataMandatoService richiestaRevocaImmediataMandatoService;
	@Autowired
	private ObjectMapper objectMapper;
	
	
	@ApiOperation("Dato un cuaa reperisce i mandati relativi ad un fascicolo")
	@GetMapping("/{cuaa}")
	@PreAuthorize("@abilitazioniComponent.checkLetturaFascicolo(#cuaa)")
	public List<MandatoDto> getMandatiByCuaaConAbilitazione(
			@ApiParam(value = "CUAA", required = true) @PathVariable(value = "cuaa") String cuaa,
			@RequestParam(required = false) Integer idValidazione) {
		try {
			return mandatoService.getMandati(cuaa, idValidazione == null ? 0 : idValidazione);
		} catch (EntityNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@ApiOperation("Dato un cuaa verifica se sia possibile revocare un mandato per quel codice")
	@GetMapping("/{cuaa}/verifica/revoca-mandato")
	public DatiAperturaFascicoloDto verificaRevocaOrdinaria(
			@ApiParam(value = "CUAA per cui si vuole verificare se vi sono le condizioni per revocare in modo ordinario il mandato", required = true)
			@PathVariable(value = "cuaa") String cuaa) throws Exception {
		return mandatoService.verificaRevocaOrdinaria(cuaa);
	}
	
	@ApiOperation("Dato un cuaa restituisce i dati del fascicolo per visualizzare mandato")
	@GetMapping("/{cuaa}/dati-fascicolo")
	@PreAuthorize("@abilitazioniComponent.checkLetturaFascicolo(#cuaa)")
	public DatiAperturaFascicoloDto fascicoloPerMandato(
			@ApiParam(value = "CUAA fascicolo", required = true)
			@PathVariable(value = "cuaa") String cuaa,
			@RequestParam(required = false) Integer idValidazione) throws Exception {
		return mandatoService.getFascicoloPerMandato(cuaa, idValidazione == null ? 0 : idValidazione);
	}

	@ApiOperation("Revoca ordinaria del fascicolo indicato")
	@PostMapping("/{cuaa}/revoca-mandato")
	@PreAuthorize("@abilitazioniComponent.checkRevocaOrdinariaMandato(#cuaa)")
	public Long revocaOrdinariaMandato(@ApiParam(value = "Cuaa del fascicolo a cui si vuole fare la revoca ordinaria del mandato")
	@PathVariable(required = true, value = "cuaa") String cuaa,
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
			throw new IllegalArgumentException(FascicoloValidazioneEnum.ALLEGATO_NON_PRESENTE.name());
		}
		ApriFascicoloDto mandatoDto = new ApriFascicoloDto()
				.setCodiceFiscale(cuaa)
				.setCodiceFiscaleRappresentante(codiceFiscaleRappresentante)
				.setIdentificativoSportello(identificativoSportello)
				.setContratto(new ByteArrayResource(contratto.getBytes(), contratto.getOriginalFilename()))
				.setAllegati(attachments);
		return mandatoService.revocaOrdinariaMandato(mandatoDto);
	}
	
	@ApiOperation("Controllo se l'utente autenticato puo' inserire una revoca immediata")
	@GetMapping("/{cuaa}/puo-inserire-revoca-immediata")
	public Boolean utenteCorrentePuoInserireRevocaImmediata(@ApiParam(value = "CUAA azienda agricola", required = true) @PathVariable(value = "cuaa") String cuaa) throws Exception {
		return mandatoService.utenteCorrentePuoInserireRevocaImmediata(cuaa);
	}
	
	@ApiOperation("Richiesta di revoca immediata del fascicolo indicato da parte del rappresentante legale/titolare")
	@PostMapping("/{cuaa}/richiesta-revoca-immediata-mandato")
	@PreAuthorize("@abilitazioniComponent.checkRichiestaRevocaImmediataMandato(#cuaa)")
	public Long richiestaRevocaImmediataMandato(@ApiParam(value = "Cuaa del fascicolo di cui il titolare/rappresentante legale vuole fare richiesta di revoca immediata del mandato")
	@PathVariable(required = true, value = "cuaa") String cuaa,
	@RequestParam(required = true) String codiceFiscaleRappresentanteLegaleOTitolare,
	@RequestParam(required = false) String causaRichiesta,
	@RequestParam(required = true) String sportello,
	@RequestParam(required = true) MultipartFile moduloRevocaFirmato) {
		try {
			SportelloCAADto sportelloDto = objectMapper.readValue(sportello, SportelloCAADto.class);
			RichiestaRevocaImmediataDto richiestaRevocaImmediataDto = new RichiestaRevocaImmediataDto()
					.setCodiceFiscale(cuaa)
					.setCodiceFiscaleRappresentante(codiceFiscaleRappresentanteLegaleOTitolare)
					.setRichiestaRevocaImmediataFirmata(new ByteArrayResource(moduloRevocaFirmato.getBytes(), moduloRevocaFirmato.getOriginalFilename()))
					.setCausaRichiesta(causaRichiesta)
					.setSportello(sportelloDto);
			return mandatoService.richiestaRevocaImmediata(richiestaRevocaImmediataDto);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		}
	}
	
	@ApiOperation("Recupera pdf contratto del mandato")
	@GetMapping("/{mandatoId}/fascicolo/{fascicoloId}/contratto")
	public ResponseEntity<Resource> getPdfAllegatoContrattoMandato(
			@PathVariable Long fascicoloId,
			@PathVariable Long mandatoId,
			@RequestParam(required = false) Integer idValidazione
			) throws Exception{
		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=contratto_" + mandatoId + ".pdf")
				.body(mandatoService.getPdfAllegatoContrattoMandato(
						new EntitaDominioFascicoloId(fascicoloId, idValidazione == null ? 0 : idValidazione),
						new EntitaDominioFascicoloId(mandatoId, idValidazione == null ? 0 : idValidazione)));
	}

	@ApiOperation("Modifica della sede operativa del CAA")
	@PutMapping("/{cuaa}/sportello/{sportelloId}")
	@PreAuthorize("@abilitazioniComponent.checkModificaSportelloMandato(#cuaa)")
	public void cambioSportello(
			@PathVariable final String cuaa,
			@PathVariable final Long sportelloId,
			@RequestBody(required = true) final CambioSportelloPatch cambioSportello) throws Exception {
		mandatoService.cambioSportello(cuaa, cambioSportello);
	}
	
	@ApiOperation("Dato un cuaa verifica se è stata presentata una revoca ordinaria")
	@GetMapping("/{cuaa}/verifica-presenza-revoca-ordinaria")
	public Boolean verificaPresenzaRevocaOrdinaria(@ApiParam(value = "CUAA", required = true) @PathVariable(value = "cuaa") String cuaa) throws Exception {
		return mandatoService.verificaPresenzaRevocaOrdinaria(cuaa);
	}
	
	@ApiOperation("Mostra tutte le richieste di revoca immediata")
	@GetMapping("/richieste-revoca-immediata-mandato")
	@PreAuthorize("@abilitazioniComponent.checkListaRichiesteRevovaImmediataMandato(#isValutata)")
	public List<DescrizioneRichiestaRevocaImmediataMandatoDto> getRichiesteRevocaImmediataMandato(
			@RequestParam(required = true, value = "valutata") final Boolean isValutata) throws Exception {
		return richiestaRevocaImmediataMandatoService.getRichiesteRevocaImmediataDto(isValutata);
	}
	
	@ApiOperation("Mostra tutte le richieste di revoca immediata per il caa connesso")
	@GetMapping("/richieste-revoca-immediata-mandato-paged")
	@PreAuthorize("@abilitazioniComponent.checkListaRichiesteRevovaImmediataMandato(#isValutata)")
	public RisultatiPaginati<DescrizioneRichiestaRevocaImmediataMandatoDto> getRichiesteRevocaImmediataMandatoPaged(
			@RequestParam(required = true, value = "valutata") final Boolean isValutata,
			Paginazione paginazione, Ordinamento ordinamento) throws Exception {
		return richiestaRevocaImmediataMandatoService.getRichiesteRevocaImmediataDto(isValutata, paginazione, ordinamento);
	}
	
	@ApiOperation("Dato un ID protocollo scarica il documento firmato di richiesta revoca mandato")
	@GetMapping(value = "/richiesta-revoca-immediata-mandato/documento-firmato", produces = MediaType.APPLICATION_PDF_VALUE)
	@PreAuthorize("@abilitazioniComponent.checkLetturaFascicoloPerRevocaImmediata(#idProtocollo)")
	public @ResponseBody byte[] getDocumentoFirmatoRichiestaRevocaImmediata(
			@ApiParam(value = "ID protocollo della richiesta di revoca immediata del mandato")
			@RequestParam(required = true, value = "idProtocollo") final String idProtocollo) {
		return richiestaRevocaImmediataMandatoService.getDocumentoRichiestaFirmato(idProtocollo);
	}

	@ApiOperation("Accettazione o rifiuto revoca immediata")
	@PutMapping("/{cuaa}/valuta-revoca-immediata")
	@PreAuthorize("@abilitazioniComponent.checkValutaRevocaImmediataMandato(#cuaa)")
	public BusinessResponsesDto valutaRichiestaRevocaImmediata(
			@PathVariable String cuaa,
			@RequestParam(required = true, value = "accettata") Boolean accettata,
			@RequestBody(required = false) String motivazioneRifiuto) {
		RichiestaRevocaImmediataDto richiestaRevocaImmediataDto = new RichiestaRevocaImmediataDto()
				.setCodiceFiscale(cuaa)
				.setMotivazioneRifiuto(motivazioneRifiuto);
		BusinessResponsesDto businessResponsesDto = new BusinessResponsesDto();
		try {
			richiestaRevocaImmediataDto = mandatoService.valutaRichiestaRevocaImmediata(richiestaRevocaImmediataDto, accettata);
		} catch (ResponseStatusException e) {
			businessResponsesDto.getResponseList().add(e.getReason());
			businessResponsesDto.setEsito(Esiti.KO);
			logger.error(Esiti.KO.toString(), e);
		} catch (Exception e) {
			businessResponsesDto.getResponseList().add(RevocaImmediataMandatoNotification.ERRORE_GENERICO.name());
			businessResponsesDto.setEsito(Esiti.KO);
			logger.error(Esiti.KO.toString(), e);
		}
		if (businessResponsesDto.getEsito() == Esiti.KO ) {
			logger.warn(Esiti.KO.toString());
			return businessResponsesDto;
		}
		try {
			mandatoService.notificaMailTitolareRappresentanteLegale(richiestaRevocaImmediataDto, accettata);
		} catch (ResponseStatusException e) {
			businessResponsesDto.getResponseList().add(e.getReason());
			businessResponsesDto.setEsito(Esiti.NON_BLOCCANTE);
			logger.error(Esiti.NON_BLOCCANTE.toString(), e);
		}
		try {
			mandatoService.notificaMailAppag(richiestaRevocaImmediataDto, accettata);
		} catch (ResponseStatusException e) {
			businessResponsesDto.getResponseList().add(e.getReason());
			businessResponsesDto.setEsito(Esiti.NON_BLOCCANTE);
			logger.error(Esiti.NON_BLOCCANTE.toString(), e);
		}
		return businessResponsesDto;
	}

	@ApiOperation("Dato un codice fiscale verifica se sia possibile acquisire un mandato da un fascicolo con revoca immediata")
	@GetMapping("/revoca-immediata/{cuaa}/verifica/dati-acquisizione")
	@PreAuthorize("@abilitazioniComponent.checkAperturaFascicolo(#cuaa)")
	public DatiAperturaFascicoloDto verificaAcquisizioneMandatoFascicoloRevocaImmediata(
			@ApiParam(value = "Codice fiscale per cui si vuole verificare se vi sono le condizioni per aprire un nuovo fascicolo", required = true)
			@PathVariable(value = "cuaa") String cuaa) {
		try {
			return mandatoService.mandatoAcquisizioneVerifica(cuaa);
		} catch (MandatoVerificaException afve) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					afve.getMessage());
		}
	}

	@ApiOperation("Servizio che permette di acquisire il mandato")
	@PostMapping("/{cuaa}/acquisisci-mandato")
	@PreAuthorize("@abilitazioniComponent.checkAperturaFascicolo(#cuaa)")
	public FascicoloCreationResultDto acquisisciMandato(
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
			return mandatoService.acquisisciMandato(datiApertura);
		} catch (Exception e) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
}
