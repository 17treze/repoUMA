package it.tndigitale.a4g.proxy.api.persona;

import it.tndigitale.a4g.proxy.api.ApiUrls;
import it.tndigitale.a4g.proxy.dto.persona.PersonaFisicaDto;
import it.tndigitale.a4g.proxy.dto.persona.PersonaGiuridicaDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.proxy.services.AnagrafeTributariaService;
import it.tndigitale.a4g.proxy.services.AnagraficaImpresaServiceImpl;

@RestController
//@RequestMapping("/api/v2/anagrafetributaria")
@RequestMapping(ApiUrls.ANAGRAFETRIBUTARIA_V2)
@Api(value = "Anagrafe tributaria")
public class AnagrafeTributariaController {
	
	private static final Logger log = LoggerFactory.getLogger(AnagraficaImpresaServiceImpl.class);

	@Autowired
	private AnagrafeTributariaService anagrafeTributariaService;

	@ApiOperation("Restituisce i dati anagrafici di una persona fisica reperendoli dall'Anagrfe tributaria")
	@GetMapping(value = "/personafisica/{codiceFiscale}", produces = { "application/json" })
	public PersonaFisicaDto getPersonaFisica(@ApiParam(value = "codice fiscale", required = true) @PathVariable(value = "codiceFiscale") String codiceFiscale) throws Exception {
		if (codiceFiscale.length() == 16) {
			return anagrafeTributariaService.getPersonaFisica(codiceFiscale);
		} else {
			log.error("Chiamato servizio anagrafe tributaria con con codice fiscale di persona fisica invalido: {}" , codiceFiscale);
			throw new IllegalArgumentException("Chiamato servizio anagrafe tributaria con con codice fiscale di persona fisica invalido ".concat(codiceFiscale));
		}
	}

	@ApiOperation("Restituisce i dati anagrafici di una persona giuridica reperendoli dall'Anagrfe tributaria")
	@GetMapping(value = "/personagiuridica/{codiceFiscale}", produces = { "application/json" })
	public PersonaGiuridicaDto getPersonaGiuridica(@ApiParam(value = "codice fiscale", required = true) @PathVariable(value = "codiceFiscale") String codiceFiscale) throws Exception {
		if (codiceFiscale.length() == 11) {
			return anagrafeTributariaService.getPersonaGiuridica(codiceFiscale);
		} else {
			log.error("Chiamato servizio anagrafe tributaria con con codice fiscale di persona giuridica invalido: {}" , codiceFiscale);
			throw new IllegalArgumentException("Chiamato servizio anagrafe tributaria con con codice fiscale di persona giuridica invalido ".concat(codiceFiscale));
		}
	}
}

