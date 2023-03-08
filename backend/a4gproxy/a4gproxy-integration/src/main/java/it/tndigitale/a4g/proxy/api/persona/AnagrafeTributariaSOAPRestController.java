package it.tndigitale.a4g.proxy.api.persona;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.proxy.api.ApiUrls;
import it.tndigitale.a4g.proxy.services.AnagrafeTributariaService;
import it.tndigitale.ws.isvalidazioneanagrafe.RispostaRichiestaRispostaSincronaRicercaAnagraficaAll;

@RestController
//@RequestMapping("/api/v1/anagrafetributaria")
@RequestMapping(ApiUrls.ANAGRAFETRIBUTARIA_V1)
@Api(value = "Anagrafe tributaria")
public class AnagrafeTributariaSOAPRestController {

	@Autowired
	private AnagrafeTributariaService anagrafeTributariaService;
	
	@Deprecated
	@ApiOperation("Restituisce i dati anagrafici di una persona fisica")
	@GetMapping(value = "{codiceFiscale}", produces = { "application/json" })
	public RispostaRichiestaRispostaSincronaRicercaAnagraficaAll getAnagrafeTributaria(
			@ApiParam(value = "codice fiscale", required = true) @PathVariable(value = "codiceFiscale") String codiceFiscale) throws Exception {
		return anagrafeTributariaService.caricaAnagraficaPersonaFisica(codiceFiscale);
	}

}
