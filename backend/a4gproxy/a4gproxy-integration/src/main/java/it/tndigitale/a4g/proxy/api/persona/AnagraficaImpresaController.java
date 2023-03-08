package it.tndigitale.a4g.proxy.api.persona;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.proxy.api.ApiUrls;
import it.tndigitale.a4g.proxy.dto.persona.PersonaFisicaDto;
import it.tndigitale.a4g.proxy.dto.persona.PersonaGiuridicaDto;
import it.tndigitale.a4g.proxy.dto.persona.UnitaLocaleDto;
import it.tndigitale.a4g.proxy.services.AnagraficaImpresaService;

@RestController
@RequestMapping(ApiUrls.ANAGRAFICAIMPRESA_V2)
@Api(value = "Anagrafica Impresa")
public class AnagraficaImpresaController {
	@Autowired
	private AnagraficaImpresaService anagraficaImpresaService;

	private static final Logger logger = LoggerFactory.getLogger(AnagraficaImpresaController.class);

	@ApiOperation("Restituisce i dati anagrafici di una persona fisica reperendoli dalla Camera di commercio")
	@GetMapping(value = "/personafisica/{codiceFiscale}", produces = { "application/json" })
	public PersonaFisicaDto getPersonaFisica(@ApiParam(value = "Codice fiscale della persona fisica da ricercare", required = true) @PathVariable(value = "codiceFiscale") String codiceFiscale,
											 @ApiParam(value = "Provincia di iscrizione di interesse; TN di default") @RequestParam(required = false) String provinciaIscrizione) throws Exception {
		if (codiceFiscale.length() == 16) {
			return anagraficaImpresaService.getPersonaFisica(codiceFiscale, provinciaIscrizione);
		} else {
			String errMsg = String.format("Chiamato servizio anagrafica impresa con con codice fiscale di persona fisica invalido: %s" , codiceFiscale);
			logger.error(errMsg);
			throw new IllegalArgumentException(errMsg);
		}
	}

	@ApiOperation("Restituisce i dati anagrafici di una persona giuridica reperendoli dalla Camera di commercio")
	@GetMapping(value = "/personagiuridica/{codiceFiscale}", produces = { "application/json" })
	public PersonaGiuridicaDto getPersonaGiuridica(@ApiParam(value = "Codice fiscale della persona giuridica da ricercare", required = true) @PathVariable(value = "codiceFiscale") String codiceFiscale,
												   @ApiParam(value = "Provincia di iscrizione di interesse; TN di default") @RequestParam(required = false) String provinciaIscrizione) throws Exception {
		if (codiceFiscale.length() == 11) {
			return anagraficaImpresaService.getPersonaGiuridica(codiceFiscale, provinciaIscrizione);
		} else {
			String errMsg = String.format("Chiamato servizio anagrafica impresa con con codice fiscale di persona giuridica invalido: %s" , codiceFiscale);
			logger.error(errMsg);
			throw new IllegalArgumentException(errMsg);
		}
	}
	
//	@ApiOperation("Restituisce le unita locali di una persona giuridica reperendoli dalla Camera di commercio")
//	@GetMapping(value = "/personagiuridica/{codiceFiscale}/unitalocali", produces = { "application/json" })
//	public List<UnitaLocaleDto> getUnitaLocaliPersonaGiuridica(@ApiParam(value = "Codice fiscale della persona giuridica da ricercare", required = true) @PathVariable(value = "codiceFiscale") String codiceFiscale,
//												   @ApiParam(value = "Provincia di iscrizione di interesse; TN di default") @RequestParam(required = false) String provinciaIscrizione) throws Exception {
//		if (codiceFiscale.length() == 11) {
//			return anagraficaImpresaService.getUnitaLocaliPersonaGiuridica(codiceFiscale, provinciaIscrizione);
//		} else {
//			String errMsg = String.format("Chiamato servizio anagrafica impresa con con codice fiscale di persona giuridica invalido: %s" , codiceFiscale);
//			logger.error(errMsg);
//			throw new IllegalArgumentException(errMsg);
//		}
//	}
//	
//	@ApiOperation("Restituisce le unita locali di una persona fisica con impresa individuale reperendoli dalla Camera di commercio")
//	@GetMapping(value = "/personafisica/{codiceFiscale}/unitalocali", produces = { "application/json" })
//	public List<UnitaLocaleDto> getUnitaLocaliPersonaFisica(@ApiParam(value = "Codice fiscale della persona fisica da ricercare", required = true) @PathVariable(value = "codiceFiscale") String codiceFiscale,
//											 @ApiParam(value = "Provincia di iscrizione di interesse; TN di default") @RequestParam(required = false) String provinciaIscrizione) throws Exception {
//		
//		if (codiceFiscale.length() == 16) {
//			return anagraficaImpresaService.getUnitaLocaliPersonaFisica(codiceFiscale, provinciaIscrizione);
//		} else {
//			String errMsg = String.format("Chiamato servizio anagrafica impresa con con codice fiscale di persona fisica invalido: %s" , codiceFiscale);
//			logger.error(errMsg);
//			throw new IllegalArgumentException(errMsg);
//		}
//	}
}
