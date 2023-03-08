package it.tndigitale.a4g.ags.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.ags.dto.DomandaPsr;
import it.tndigitale.a4g.ags.model.DomandeCollegatePsrFilter;
import it.tndigitale.a4g.ags.service.DomandaService;

/**
 * Controller per gestione operazioni su domande PSR SUPERFICIE EU
 * 
 * @author S.Caccia
 *
 */
@RestController
@RequestMapping(ApiUrls.DOMANDE_PSR_V1)
@Api(value = "Controller per gestione operazioni su domande PSR SUPERFICIE EU")
public class DomandePsrRestController {

	private static final Logger logger = LoggerFactory.getLogger(DomandePsrRestController.class);

	@Autowired
	private DomandaService serviceDomanda;
	@Autowired
	private ObjectMapper objectMapper;

	@ApiOperation("Recupare dati PSR SUPERFICIE EU.")
	@GetMapping()
	@PreAuthorize("@abilitazioniComponent.checkLetturaDomandaPSR()")
	public List<DomandaPsr> getDatiStrutturali(@RequestParam(value = "params") @ApiParam(value = "Parametri di ricerca in formato JSON: Lista CUAA, Importo e Data", required = true) String params) throws Exception{
		logger.debug("Recupero dati PSR SUPERFICIE EU: {}",params);
		DomandeCollegatePsrFilter domandeCollegatePsr=objectMapper.readValue(params, DomandeCollegatePsrFilter.class);
		return serviceDomanda.getDomandePsr(domandeCollegatePsr);
	}
}
