package it.tndigitale.a4g.fascicolo.anagrafica.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.tndigitale.a4g.fascicolo.anagrafica.Ruoli;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.CaaService;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.caa.CentroAssistenzaAgricolaDto;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;

@RestController
@RequestMapping(ApiUrls.CAA)
@Api(value = "Controller per il recupero delle informazioni dei centri di assistenza agricola")
public class CentroAssistenzaAgricolaController {
	
	@Autowired
	private CaaService caaService;
	
	@Autowired
	private UtenteComponent utenteComponent;

	@ApiOperation("Restituisce i dati dell'utente caa connesso con gli sportelli a cui è abilitato")
	@GetMapping()
	public CentroAssistenzaAgricolaDto getCentroAssistenzaAgricoloUtenteConnesso() {
		return caaService.getCentroAssistenzaAgricoloUtenteConnesso();
	}
	
	@ApiOperation("Restituisce i dati dei CAA con gli sportelli associati")
	@GetMapping("/sportelli")
	public List<CentroAssistenzaAgricolaDto> getAllCaaWithSportelli() {
		return caaService.getAllCaaWhitSportelli();
	}
	
	@ApiOperation("Per il fascicolo verifica se il caa connesso ha ruoli di apertura")
	@GetMapping("/check-apertura-fascicolo")
	public boolean caaCheckAperturaFascicolo(final String cuaa) {
		if (utenteComponent.haUnRuolo(Ruoli.APERTURA_FASCICOLO_ENTE)) {
			return caaService.isAccessoCaa(cuaa, 0);
		}
		return false;
	}
}
