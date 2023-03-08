package it.tndigitale.a4g.ags.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.tndigitale.a4g.ags.dto.DomandaUnica;
import it.tndigitale.a4g.ags.dto.DomandaUnicaFilter;
import it.tndigitale.a4g.ags.service.DomandaService;

@Api(value = "Controller per gestione operazioni su domande du")
@RestController
@RequestMapping(ApiUrls.DOMANDE_UNICHE_V1)
public class DomandeUnicheRestController {
	
	private static final Logger logger = LoggerFactory.getLogger(DomandeRestController.class);
	
	@Autowired
	private DomandaService serviceDomanda;

	
	@ApiOperation("Restituisce la lista di domande du")
	@GetMapping
	@PreAuthorize("@abilitazioniComponent.checkRicercaDomandaDU(#domandaUnicaFilter)")
	public List<DomandaUnica> getDomande(DomandaUnicaFilter domandaUnicaFilter) {
		logger.debug("Recupera domande: {}", domandaUnicaFilter.getCuaa());
		return serviceDomanda.getDomandeUniche(domandaUnicaFilter);
	}

}
