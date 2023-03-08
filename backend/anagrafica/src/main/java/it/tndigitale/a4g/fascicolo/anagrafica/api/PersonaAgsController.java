package it.tndigitale.a4g.fascicolo.anagrafica.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.PersonaService;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.CaricaAgsDto;

@RestController
@RequestMapping(ApiUrls.LEGACY + ApiUrls.PERSONA)
@Api(value = "Servizio che ha la responsabilita di gestire il ciclo di vita del fascicolo dalla sua apertura fino alla sua chiusra")
public class PersonaAgsController {

	@Autowired
	private PersonaService personaService;

	@ApiOperation("Lista delle aziende in cui la persona ha una carica che gli permette di accedere ai dati di fasciolo. Se indicato anche il cuaa restituisce le cariche per quell'azienda")
	@GetMapping("/{codiceFiscale}/carica")
	public List<CaricaAgsDto> getCariche(@ApiParam(value = "Codice Fiscale Persona", required = true) @PathVariable(value="codiceFiscale", required=true) String codiceFiscale, @RequestParam(required = false) String cuaa) {
		return personaService.getCariche(codiceFiscale ,cuaa);
	}
}
