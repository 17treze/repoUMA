package it.tndigitale.a4gutente.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4gutente.dto.Persona;
import it.tndigitale.a4gutente.service.IPersonaService;

@RestController
@RequestMapping(ApiUrls.PERSONE_V1)
@Api(value = "Persone fisiche", description = "Rappresenta le operazioni relative ai dati delle persone fisiche", position = 0)
public class PersonaController {

	@Autowired
	private IPersonaService service;

	@Autowired
	private ObjectMapper objectMapper;

	@ApiOperation("Restituisce i dati della persona fisica")
	@GetMapping("/{id}")
	public Persona caricaDatiPersona(@PathVariable @ApiParam(value = "Identificativo della persona", required = true) Long id) throws Exception {
		return service.caricaPersona(id);
	}

	@ApiOperation("Esegue la ricerca")
	@GetMapping
	public List<Persona> ricerca(
			@RequestParam(name = "params") @ApiParam(value = "Criteri di ricerca per persona: formato JSON prevede il filtro codiceFiscale (ricerca esatta)", required = true) String params)
			throws Exception {
		Persona filtro = objectMapper.readValue(params, Persona.class);
		return service.ricercaPersone(filtro.getCodiceFiscale());
	}

	@ApiOperation("Inserisce le informazioni relative alla persona")
	@PostMapping
	public Long inserisciPersona(@RequestBody Persona persona) throws Exception {
		return service.inserisciAggiornaPersona(persona);
	}

	@ApiOperation("Aggiorna le informazioni relative alla persona")
	@PutMapping("/{id}")
	public Persona aggiornaPersona(@PathVariable Long id, @RequestBody Persona persona) throws Exception {
		persona.setId(id);
		return service.aggiornaPersona(persona);
	}

}
