package it.tndigitale.a4g.ags.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.ags.dto.IstruttoriaGraficaDuDto;
import it.tndigitale.a4g.ags.service.IstruttoriaGraficaDuService;

@RestController
@RequestMapping(ApiUrls.ISTRUTTORIA_GRAFICA_V1)
public class IstruttoriaGraficaDuController {
	
	@Autowired
	private IstruttoriaGraficaDuService istruttoriaGraficaDuService;
	
	@ApiOperation("Restituisce dati per la visualizzazione grafica delle parcelle in istruttoria domanda unica")
	@GetMapping("/cuaa/{cuaa}/id-domanda/{id}")
	@PreAuthorize("@abilitazioniComponent.checkLetturaDomandaDU()")
	public IstruttoriaGraficaDuDto fascicoloDetail(
			@ApiParam(value = "Identificativo della domanda unica", required = true) @PathVariable(value = "id") final Long id,
			@ApiParam(value = "Cuaa", required = true) @PathVariable(value = "cuaa") final String cuaa) throws Exception {
		return istruttoriaGraficaDuService.getByCuaaAndIdDomanda(cuaa, id);
	}
}
