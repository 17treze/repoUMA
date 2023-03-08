package it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.api;

import java.util.List;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.ApiUrls;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.business.service.MacchineAgsService;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.dto.MacchinaAgsDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.dto.MacchinaAgsFilter;

@RestController
@RequestMapping(ApiUrls.LEGACY + ApiUrls.MACCHINE)
@Tag(name = "MacchineAgsController", description="Gestione macchine agricole fascicolo legacy")
public class MacchineAgsController {

	@Autowired
	private MacchineAgsService macchineAgsService;

	@Operation(summary = "Reperisce Informazioni sulle macchine del fascicolo", description = "Utilizzare il seguente formato per il campo data: yyyy-MM-dd'T'HH:mm")
	@GetMapping
	public List<MacchinaAgsDto> getMacchine(@ParameterObject MacchinaAgsFilter filter) throws Exception {
		return macchineAgsService.getMacchine(filter);
	}
}
