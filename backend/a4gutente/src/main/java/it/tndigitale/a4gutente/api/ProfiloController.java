package it.tndigitale.a4gutente.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.tndigitale.a4gutente.dto.Profilo;
import it.tndigitale.a4gutente.service.ProfiloService;

@RestController
@RequestMapping(ApiUrls.PROFILI_V1)
@Api(value = "Profili", description = "Rappresenta la lista dei profili", position = 0)
public class ProfiloController {

	@Autowired
	private ProfiloService service;

	@ApiOperation("Esegue la ricerca dei profili")
	@GetMapping
	public List<Profilo> ricerca() throws Exception {
		return service.ricercaProfili();
	}
	
	@ApiOperation("Esegue la ricerca dei profili utente")
	@GetMapping("/utente")
	public List<Profilo> ricercaProfiliUtente() throws Exception {
		return service.ricercaProfiliUtente();
	}
}
