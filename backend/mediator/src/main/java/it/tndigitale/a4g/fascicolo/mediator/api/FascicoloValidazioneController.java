package it.tndigitale.a4g.fascicolo.mediator.api;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.tndigitale.a4g.fascicolo.mediator.business.service.fascicolo.FascicoloValidazioneService;

@RestController
@RequestMapping(ApiUrls.FASCICOLO)
@Tag(name = "FascicoloValidazioneController", description = "API per mediator")
public class FascicoloValidazioneController {

	@Autowired private FascicoloValidazioneService fascicoloValidazioneService;

	@ApiOperation("Upload della scheda di validazione firmata dall'azienda")
	@PostMapping(value = "/{cuaa}/validazione-detenzione-autonoma", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	@PreAuthorize("@abilitazioniComponent.isAventeDirittoFirmaOrCaa(#cuaa)")
	public void validazioneDetenzioneAutonoma(
	@ApiParam(value = "Cuaa del fascicolo al quale si riferisce la scheda di validazione")
	@PathVariable(required = true, value = "cuaa") String cuaa,
	@RequestParam(required = true, value = "schedaValidazioneFirmata") MultipartFile schedaValidazioneFirmata,
	@RequestParam(required = false) Integer nextIdValidazione) throws IOException {
		try {
			fascicoloValidazioneService.validazioneDetenzioneAutonoma(cuaa, schedaValidazioneFirmata.getBytes(), nextIdValidazione == null ? 0 : nextIdValidazione);
		} catch (NoSuchElementException e) {
			// cuaa in input non corrisponde ad un fascicolo
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@ApiOperation("Upload della scheda di validazione firmata dall'azienda")
	@PostMapping(value = "/{cuaa}/validazione-mandato", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	@PreAuthorize("@abilitazioniComponent.isAventeDirittoFirmaOrCaa(#cuaa)")
	public void validazioneMandato(
	@ApiParam(value = "Cuaa del fascicolo al quale si riferisce la scheda di validazione")
	@PathVariable(required = true, value = "cuaa") String cuaa,
	@RequestParam(required = true, value = "schedaValidazioneFirmata") MultipartFile schedaValidazioneFirmata,
	@RequestParam(required = false) Integer nextIdValidazione ) throws IOException {
		try {
			fascicoloValidazioneService.validazioneMandato(cuaa, schedaValidazioneFirmata.getBytes(), nextIdValidazione == null ? 0 : nextIdValidazione);
		} catch (NoSuchElementException e) {
			// cuaa in input non corrisponde ad un fascicolo
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary = "Servizio che permette di annullare l'iter di validazione di un fascicolo")
	@PutMapping("/{cuaa}/annulla-iter-validazione")
	@PreAuthorize("@abilitazioniComponent.checkLetturaFascicolo(#cuaa)")
	public void annullaIterValidazione(
			@Parameter(description = "Cuaa azienda per cui si vuole annullare l'iter di validazione del fascicolo", required = true)
			@PathVariable(required = true) final String cuaa) throws NoSuchElementException {
		try {
			fascicoloValidazioneService.annullaIterValidazione(cuaa);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

}
