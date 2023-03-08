package it.tndigitale.a4g.srt.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.srt.dto.Utente;
import it.tndigitale.a4g.srt.dto.UtenteFilter;
import it.tndigitale.a4g.srt.services.RuoliService;
import it.tndigitale.a4g.srt.services.UtentiServiceImpl;

@RestController
@RequestMapping("/api/v1/utenti")
@Api(value = "UtentiController")
public class UtentiController {

	private static final Logger logger = LoggerFactory.getLogger(UtentiController.class);
	
	@Autowired
	private RuoliService ruoliService;
	
	@Autowired
	private UtentiServiceImpl utentiService;

	@ApiOperation("Recupera i ruoli dell'utente identificato dal codice fiscale")
	@GetMapping(path = "{codiceFiscale}/ruoli", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> getRuoliPerUtente(
			@ApiParam(value = "Codice fiscale dell'utente", required = true) @PathVariable(value = "codiceFiscale") String codiceFiscale)
			throws Exception {

		logger.trace("UtentiController getRuoliPerUtente");
		
		if (codiceFiscale != null && codiceFiscale.length() != 16)
			throw new Exception("Codice fiscale non valido");

		try {
			return ruoliService.getRuoliPerUtente(codiceFiscale);
		} catch (Exception e) {
			// workaround per evitare di bloccare l'intero A4G in caso di down del DB di SR Trento:
			// sicuramente codice migliorabile lanciando opportuno errore HTTP al FE che poi lo gestisce.
			// analiga fix probabilmente va fatta sull'integrazione di AGS
			logger.warn("ruoli non reperiti per l'utente {}", codiceFiscale, e);
			return null;
		}
	}
	
	@ApiOperation("Esegue una ricerca utenti filtrata")
	@GetMapping
	public List<Utente> findByFilter(UtenteFilter filter) throws Exception {
		return utentiService.findByFilter(filter);
	}
}
