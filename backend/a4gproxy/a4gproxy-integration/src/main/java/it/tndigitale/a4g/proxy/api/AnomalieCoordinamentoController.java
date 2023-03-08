package it.tndigitale.a4g.proxy.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.proxy.services.AnomalieCoordinamentoService;

@RestController
//@RequestMapping("api/v1/anomaliecoordinamento")
@RequestMapping(ApiUrls.ANOMALIECOORDINAMENTO)
@Api(value = "Controllo Anomalie Coordinamento")
public class AnomalieCoordinamentoController {
	
	private static final Logger logger = LoggerFactory.getLogger(AnomalieCoordinamentoController.class);

	@Autowired
	AnomalieCoordinamentoService anomalieCoordinamentoService;

	@ApiOperation("Recupera il numero di anomalie di coordinamento registrato per una determinata parcella nell'anno richiesto")
	@GetMapping("/{idParcella}")
	public Long recuperaAnomalieCoordinamento(
			@ApiParam(value = "Identificativo parcella catastale", required = true) @PathVariable(value = "idParcella") Long idParcella,
			@ApiParam(value = "Anno campagna", required = true) @RequestParam(value = "annoCampagna") Long annoCampagna) {
		
		logger.trace("recuperaAnomalieCoordinamento: idParcella: {} annoCampagna: {}", idParcella, annoCampagna);

		return anomalieCoordinamentoService.recuperaEsitoControlloAnomalieCoordinamento(annoCampagna, idParcella);
	}
}
