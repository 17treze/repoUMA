package it.tndigitale.a4g.fascicolo.mediator.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.tndigitale.a4g.fascicolo.mediator.business.service.fascicolo.MigrazioneFascicoloService;

@RestController
@RequestMapping(ApiUrls.FASCICOLO)
@Tag(name = "MigrazioneFascicoloController", description = "API per migrazione fascicolo AGS -> A4G")
public class MigrazioneFascicoloController {

	@Autowired private MigrazioneFascicoloService migrazioneFascicoloService;
	
	@ApiOperation("Servizio che permette di migrare un fascicolo da SIAP e resitituisce il nuovo cuaa")
	@PostMapping(value="/{cuaa}/migra", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	@PreAuthorize("@abilitazioniComponent.checkAperturaFascicolo(#cuaa)")
	public void migra(
			@ApiParam(value = "Codice fiscale per cui si vuole migrare il fascicolo", required = true)
			@PathVariable(value = "cuaa", required = true) String cuaa,
			@RequestParam String codiceFiscaleRappresentante,
			@RequestParam(required = true) Long identificativoSportello,
			@RequestParam(required = true) MultipartFile contratto,
			@RequestPart(value = "allegati") List<MultipartFile> allegati,
			@RequestParam(required = true) Boolean migraModoPagamento,
			@RequestParam(required = true) Boolean migraMacchinari,
			@RequestParam(required = true) Boolean migraFabbricati) throws Exception {
		try {
			migrazioneFascicoloService.invioEventoMigrazione(cuaa, codiceFiscaleRappresentante, identificativoSportello, contratto.getBytes(), allegati, migraModoPagamento, migraMacchinari, migraFabbricati);
			
		} catch (Exception e) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
}
