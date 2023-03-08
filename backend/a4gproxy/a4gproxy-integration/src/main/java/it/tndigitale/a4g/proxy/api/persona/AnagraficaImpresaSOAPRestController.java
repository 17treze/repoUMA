package it.tndigitale.a4g.proxy.api.persona;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.proxy.api.ApiUrls;
import it.tndigitale.a4g.proxy.dto.DettaglioImpresa;
import it.tndigitale.a4g.proxy.services.AnagraficaImpresaService;
import it.tndigitale.ws.wssanagraficaimprese.RISPOSTA;

@RestController
//@RequestMapping("/api/v1/anagraficaimpresa")
@RequestMapping(ApiUrls.ANAGRAFICAIMPRESA_V1)
@Api(value = "Anagrafica Impresa")
public class AnagraficaImpresaSOAPRestController {
	@Autowired
	private AnagraficaImpresaService anagraficaImpresaService;
	
	@Autowired
	private ObjectMapper objectMapper;

	private static final Logger logger = LoggerFactory.getLogger(AnagraficaImpresaSOAPRestController.class);
	
	@Deprecated
	@ApiOperation("Restituisce i dati di sintesi anagrafici dell'impresa")
	@GetMapping(value = "{codiceFiscale}")
	public RISPOSTA getAnagraficaImpresa(@ApiParam(value = "codice fiscale", required = true) @PathVariable(value = "codiceFiscale") String codiceFiscale) throws Exception {
		logger.debug("Invocato metodo getAnagraficaImpresa con parametro codiceFiscale {}", codiceFiscale);
		return anagraficaImpresaService.getAnagraficaImpresaNonCessata(codiceFiscale);
	}
	
	@Deprecated
	@ApiOperation("Restituisce i dati completi dell'impresa")
	@GetMapping(value = "dettagliocompleto")
	public RISPOSTA getDettaglioCompletoImpresa(@RequestParam(value = "params") String params) throws Exception {
		logger.debug("Invocato metodo getAnagraficaImpresa con parametri {}", params);
		DettaglioImpresa dettaglioImpresa = objectMapper.readValue(params, DettaglioImpresa.class);
		return anagraficaImpresaService.getDettaglioCompletoImpresa(dettaglioImpresa);
	}
	
	@ApiOperation("Restituisce i dati di sintesi anagrafici dell'impresa")
	@GetMapping(value = "dettagliPersone/{codiceFiscale}")
	public RISPOSTA getPersoneNonCessata(@ApiParam(value = "codice fiscale", required = true) @PathVariable(value = "codiceFiscale") String codiceFiscale) throws Exception {
		logger.debug("Invocato metodo getPersoneNonCessata con parametro codiceFiscale {}", codiceFiscale);
		return anagraficaImpresaService.getPersoneNonCessata(codiceFiscale);
	}
}
