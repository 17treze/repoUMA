package it.tndigitale.a4g.fascicolo.mediator.api;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.tndigitale.a4g.fascicolo.mediator.business.service.fascicolo.ControlliCompletezzaFascicoloService;
import it.tndigitale.a4g.fascicolo.mediator.business.service.fascicolo.exception.FascicoloInvalidConditionException;
import it.tndigitale.a4g.fascicolo.mediator.dto.EsitoControlloDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(ApiUrls.FASCICOLO)
@Tag(name = "ControlliCompletezzaController", description = "API per mediator")
public class ControlliCompletezzaFascicoloController {

	@Autowired private ControlliCompletezzaFascicoloService controlliCompletezzaFascicoloService;

	@Operation(summary = "Restituisce l'esito per ogni controllo di completezza del fascicolo. I controlli danno esituo TRUE se superati con successo, FALSE altrimenti")
	@GetMapping("/{cuaa}/controllo-completezza")
	@PreAuthorize("@abilitazioniComponent.checkLetturaFascicolo(#cuaa)")
	public Map<String, EsitoControlloDto> queryControlloCompletezzaFascicolo(
			@Parameter(description = "Cuaa azienda per cui si vogliono eseguire i controlli di completezza del fascicolo", required = true)
			@PathVariable(required = true) final String cuaa) throws NoSuchElementException {
		try {
			return controlliCompletezzaFascicoloService.queryControlloCompletezzaFascicolo(cuaa);
		} catch (NoSuchElementException e) {
			return new HashMap<>();
		}
	}

//	TODO per l'avvio di controllo completezza verificare se è più corretto il diritto di apertura del fascicolo (visto che si modifica lo stato)
	@Operation(summary = "Per il dato CUAA avvia in modo asincrono i vari controlli di completezza del fascicolo")
	@PutMapping("/{cuaa}/start-controllo-completezza")
	@PreAuthorize("@abilitazioniComponent.checkLetturaFascicolo(#cuaa)")
//	@PreAuthorize("@abilitazioniComponent.checkAperturaFascicolo(#cuaa)")
	public void startControlloCompletezzaFascicolo(
			@Parameter(description = "Cuaa azienda per cui si vogliono eseguire i controlli di completezza del fascicolo", required = true)
			@PathVariable(required = true) final String cuaa) {
		try {
			controlliCompletezzaFascicoloService.startControlloCompletezzaFascicolo(cuaa);
		} catch (FascicoloInvalidConditionException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@ApiOperation("Rimozione dei controlli di completezza dei vari microservizi")
	@DeleteMapping("/{cuaa}/controllo-completezza")
//	@PreAuthorize("@abilitazioniComponent.checkAperturaFascicolo(#cuaa)")
	@PreAuthorize("@abilitazioniComponent.checkLetturaFascicolo(#cuaa)")
	public void rimozioneControlliCompletezza(
			@PathVariable @ApiParam(value = "CUAA azienda agricola", required = true) final String cuaa) {
		controlliCompletezzaFascicoloService.rimozioneControlliCompletezza(cuaa);
	}
}
