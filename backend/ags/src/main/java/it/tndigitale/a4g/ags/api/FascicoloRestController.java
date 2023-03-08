/**
 * 
 */
package it.tndigitale.a4g.ags.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.ags.dto.AnagraficaAzienda;
import it.tndigitale.a4g.ags.dto.Fascicolo;
import it.tndigitale.a4g.ags.dto.FascicoloFilter;
import it.tndigitale.a4g.ags.repository.dao.exceptions.CUAANotFoundException;
import it.tndigitale.a4g.ags.repository.dao.exceptions.MultipleMatchesForCUAAException;
import it.tndigitale.a4g.ags.service.FascicoloAziendaleService;
import it.tndigitale.a4g.ags.service.FascicoloService;

/**
 * Controller per gestione operazioni su fascicolo
 * 
 * @author S.DeLuca
 *
 */
@RestController
@RequestMapping(ApiUrls.FASCICOLI_V1)
@Api(value = "Controller per gestione operazioni su fascicolo")
public class FascicoloRestController {

	// private static final Logger logger = LoggerFactory.getLogger(FascicoloRestController.class);

	private final FascicoloService fascicoloService;
	private final FascicoloAziendaleService fascicoloAziendaleService;
	private final ObjectMapper objectMapper;

	@Autowired
	public FascicoloRestController(FascicoloService fascicoloService,
			FascicoloAziendaleService fascicoloAziendaleService,
			ObjectMapper objectMapper) {
		this.fascicoloService = fascicoloService;
		this.fascicoloAziendaleService = fascicoloAziendaleService;
		this.objectMapper = objectMapper;
	}

	@ApiOperation("Recupare i dati di dettaglio di un fascicolo dato l'id in siap(ags)")
	@GetMapping("/{id}")
	@PreAuthorize("@abilitazioniComponent.checkLetturaFascicolo()")
	public Fascicolo fascicoloDetail(@ApiParam(value = "Identificativo del fascicolo in siap", required = true) @PathVariable(value = "id") Long id) throws Exception {
		return fascicoloService.getFascicolo(id);
	}

	@ApiOperation("Ricerca i dati di fascicolo")
	@GetMapping
	@PreAuthorize("@abilitazioniComponent.checkLetturaFascicolo()")
	public List<Fascicolo> fascicoli(@ApiParam(value = "Criteri di ricerca (cuaa e denominazione) in formato json", required = true) @RequestParam(value = "params") String params) throws Exception {
		// costruisco l'oggetto per la ricerca partendo dalla mappa di parametri ottenuti
		FascicoloFilter fascicolo = objectMapper.readValue(params, FascicoloFilter.class);
		return fascicoloService.getFascicoli(fascicolo);
	}

	@ApiOperation("Verifica se al cuaa in input risulta associato un fascicolo valido su siap(ags)")
	@GetMapping("/{cuaa}/" + ApiUrls.FUNZIONE_FASCICOLO_VALIDO)
	public boolean fascicoloValido(@ApiParam(value = "cuaa da verificare", required = true) @PathVariable(value = "cuaa") String cuaa) {
		return fascicoloService.checkFascicoloValido(cuaa);
	}

	@ApiOperation("Recupera l'anagrafica aziendale dato il codice univoco CUAA")
	@GetMapping("{cuaa}/anagrafica")
	// TODO: @PreAuthorize("@abilitazioniComponent.checkLetturaFascicolo")
	public AnagraficaAzienda findAnagraficaAziendale(@PathVariable("cuaa") String cuaa) {
		return fascicoloAziendaleService.findAnagraficaAziendaByCuaa(cuaa);
	}

	@ExceptionHandler(CUAANotFoundException.class)
	public ResponseEntity<String> cuaaNotFoundExceptionHandler() {
		return new ResponseEntity<>("Anagrafica Azienda not found", HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MultipleMatchesForCUAAException.class)
	public ResponseEntity<String> moreThanOneCuaaFoundExceptionHandler() {
		return new ResponseEntity<>("Anagrafica Azienda not found", HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
