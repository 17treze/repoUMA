package it.tndigitale.a4g.fascicolo.anagrafica.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Schema;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.CaaAgsService;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.caa.SportelloFascicoloDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.TipoDetenzioneAgs;

@RestController
@RequestMapping(ApiUrls.CAA_AGS)
@Api(value = "Controller legacy per il recupero delle informazioni dei fascicoli associati ai CAA")
public class CaaAgsController {

	@Autowired
	private CaaAgsService caaAgsService;

	@ApiOperation("Recupera i fascicoli che hanno delega o mandato sugli enti dell'utente connesso")
	@GetMapping("/")
	@PreAuthorize("@abilitazioniComponent.isCaa()")
	public List<SportelloFascicoloDto> getFascicoliEnteUtenteConnesso(@Schema(enumAsRef = true) @RequestParam(required = false)TipoDetenzioneAgs tipoDetenzione) {
		return caaAgsService.getFascicoliEnteUtenteConnesso(tipoDetenzione);
	}
}
