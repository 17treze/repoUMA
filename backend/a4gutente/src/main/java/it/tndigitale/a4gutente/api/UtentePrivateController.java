/**
 * 
 */
package it.tndigitale.a4gutente.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import it.tndigitale.a4gutente.dto.ReportValidazioneDto;
import it.tndigitale.a4gutente.service.IUtenteService;

@RestController
@RequestMapping(ApiUrls.UTENTI_PRIVATE)
@Api(value = "Informazioni dell'utente", description = "Rappresenta le interrogazioni che si possono fare relative ai dati dell'utente che fa la chiamata")
public class UtentePrivateController {

	@Autowired
	private IUtenteService utenteService;
	
	@Operation(summary = "Restituisce il report dei dati utente")
	@GetMapping("/{cuaa}/report-scheda-validazione")
	public ReportValidazioneDto getReportValidazione(@PathVariable(name = "cuaa", required = true) String cuaa) throws Exception {
		return utenteService.getReportValidazione(cuaa);
	}

}
