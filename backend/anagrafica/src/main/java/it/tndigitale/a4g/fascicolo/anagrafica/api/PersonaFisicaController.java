package it.tndigitale.a4g.fascicolo.anagrafica.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.PersonaService;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.PersonaFisicaDto;

@RestController
@RequestMapping(ApiUrls.PERSONA_FISICA)
@Api(value = "API per persone fisiche A4G")
public class PersonaFisicaController {

	@Autowired
	private PersonaService personaService;

	@GetMapping("/{codiceFiscale}")
	public PersonaFisicaDto getPersonaFisica(
			@ApiParam(value = "Codice Fiscale Persona", required = true)
			@PathVariable(value="codiceFiscale", required=true) String codiceFiscale,
			@RequestParam(required = false) Integer idValidazione) throws Exception {
		if (codiceFiscale.length() != 16) {
			throw new IllegalArgumentException("Codice Fiscale inserito non valido");
		}
		return personaService.getPersonaFisica(codiceFiscale, idValidazione == null ? 0 : idValidazione);
	}
}
