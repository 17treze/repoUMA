package it.tndigitale.a4g.fascicolo.mediator.api;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.tndigitale.a4g.fascicolo.mediator.business.service.fascicolo.SchedaValidazioneFascicoloService;
import it.tndigitale.a4g.fascicolo.mediator.business.service.fascicolo.exception.FascicoloInvalidConditionException;

@RestController
@RequestMapping(ApiUrls.FASCICOLO)
@Tag(name = "StampaFascicoloController", description = "Espone i servizi di stampa inerenti il fascicolo aziendale")
public class SchedaValidazioneFascicoloController {

	@Autowired private SchedaValidazioneFascicoloService schedaValidazioneFascicoloService;

	@Operation(summary = "Stampa il PDF della scheda di validazione del fascicolo")
	@GetMapping("/{cuaa}/report-scheda-validazione")
	@PreAuthorize("@abilitazioniComponent.checkLetturaFascicolo(#cuaa)")
	public ResponseEntity<Resource> stampaSchedaValidazioneFascicolo(
			@Parameter(description = "Cuaa azienda per cui si vuole effettuare la stampa della scheda di validazione", required = true)
			@PathVariable(required = true) final String cuaa) throws IOException, FascicoloInvalidConditionException {

		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_PDF)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=".concat("Scheda di Validazione_").concat(cuaa))
				.body(schedaValidazioneFascicoloService.stampaSchedaValidazioneFascicolo(cuaa));
	}

	@Operation(summary = "Stampa il PDF in bozza della scheda di validazione del fascicolo")
	@GetMapping("/{cuaa}/report-scheda-validazione-bozza")
	@PreAuthorize("@abilitazioniComponent.checkLetturaFascicolo(#cuaa)")
	public ResponseEntity<Resource> stampaSchedaValidazioneFascicoloBozza(
			@Parameter(description = "Cuaa azienda per cui si vuole effettuare la stampa in bozza della scheda di validazione", required = true)
			@PathVariable(required = true) final String cuaa) throws IOException {

		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_PDF)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=".concat("Scheda di Validazione_").concat(cuaa))
				.body(schedaValidazioneFascicoloService.stampaSchedaValidazioneFascicoloBozza(cuaa));
	}

}
