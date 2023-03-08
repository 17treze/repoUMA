package it.tndigitale.a4g.fascicolo.dotazionetecnica.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.ApiUrls;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.service.MacchineService;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.DettaglioMacchinaDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.MacchinaDto;

@RestController
@RequestMapping(ApiUrls.API_V1 + ApiUrls.FASCICOLO)
@Tag(name = "MacchineController", description="Gestione macchine agricole fascicolo")
public class MacchineController {

	@Autowired
	private MacchineService macchineService;

	@Operation(summary = "Reperimento dati generici inerenti alle macchine del fascicolo", description = "")
	@GetMapping("/{cuaa}" + ApiUrls.MACCHINE)
	public List<MacchinaDto> getMacchine(@PathVariable String cuaa,
			@RequestParam(required = false) Integer idValidazione) {
		return macchineService.getMacchine(cuaa, idValidazione == null ? 0 : idValidazione);
	}

	@Operation(summary = "Reperimento dati di dettaglio di una macchina", description = "")
	@GetMapping("/{cuaa}" + ApiUrls.MACCHINE + "/{id}")
	public DettaglioMacchinaDto getMacchina(
			@PathVariable(name = "cuaa", required = true) final String cuaa,
			@PathVariable final Long id,
			@RequestParam(required = false) Integer idValidazione) {
		return macchineService.getMacchina(id, idValidazione == null ? 0 : idValidazione);
	}

	@Operation(summary = "Recupera il documento di possesso associato ad una macchina", description = "")
	@GetMapping("/{cuaa}" + ApiUrls.MACCHINE + "/{id}" + ApiUrls.ALLEGATI)
	public ResponseEntity<Resource> getAllegato(
			@PathVariable(name = "cuaa", required = true) final String cuaa,
			@PathVariable final Long id,
			@RequestParam(required = false) Integer idValidazione) {
		return macchineService.getAllegato(id, idValidazione == null ? 0 : idValidazione);
	}
	
}
