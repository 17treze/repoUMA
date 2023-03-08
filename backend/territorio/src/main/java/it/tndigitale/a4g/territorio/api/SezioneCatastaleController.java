package it.tndigitale.a4g.territorio.api;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4g.territorio.business.service.SezioneCatastaleService;
import it.tndigitale.a4g.territorio.dto.SezioneCatastale;
import it.tndigitale.a4g.territorio.dto.SezioneCatastaleFilter;

@RestController
@RequestMapping(ApiUrls.SEZIONE_CATASTALE)
@Tag(name = "SezioneCatastaleController", description="Sezione catastale API: gestione delle sezioni catastali a cui afferisce il territorio")
public class SezioneCatastaleController {
	
	@Autowired
	private SezioneCatastaleService service;

	@Operation(summary = "Restituisce i dati relativi alle sezioni catastali italiane", description = "Restituisce i dati relativi alle sezioni catastali italiane")
	@GetMapping
	public RisultatiPaginati<SezioneCatastale> ricerca(@ParameterObject SezioneCatastaleFilter criteri) {
		return service.ricerca(criteri);
	}
}
