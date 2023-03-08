package it.tndigitale.a4g.fascicolo.anagrafica.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato.DetenzioneService;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.DetenzioneDto;

@RestController
@RequestMapping(ApiUrls.DETENZIONE)
@Api(value = "Servizio che legge la detenzione di un fascicolo")
public class DetenzioneController {
	
	@Autowired
	private DetenzioneService detenzioneService;
	
	@ApiOperation("Dato un cuaa restituisce le detenzioni di un fascicolo")
	@GetMapping("/{cuaa}")
	// @PreAuthorize("@abilitazioniComponent.checkLetturaFascicolo(#cuaa)")
	public DetenzioneDto getDetenzione(
			@ApiParam(value = "CUAA", required = true) @PathVariable(value = "cuaa") String cuaa,
			@RequestParam(required = false) Integer idValidazione) throws Exception {
		return detenzioneService.getDetenzione(cuaa, idValidazione == null ? 0 : idValidazione);
	}
}
