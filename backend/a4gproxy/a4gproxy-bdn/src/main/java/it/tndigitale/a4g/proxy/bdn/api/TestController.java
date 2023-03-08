package it.tndigitale.a4g.proxy.bdn.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.tndigitale.a4g.proxy.bdn.client.A4gIstruttoriaClient;

@RestController
@RequestMapping("api/v1/test")
@Api(value = "Servizio per la sincronizzazione con BDN")
public class TestController {


	@Autowired
	private A4gIstruttoriaClient a4gIstruttoriaClient;

	@ApiOperation("Metodo di test per il recupero dei registri stalla degli equidi")
	@GetMapping("/test/istruttoria")
	public List<String> getPascoli() {
		return a4gIstruttoriaClient.getElencoPascoli(2020, "PNZSRG63B15L769C");
	}
	
	@ApiOperation("Metodo di test per il recupero dei registri stalla degli equidi")
	@GetMapping("/test/elencocuaa")
	public List<String> getElencoCuaa() {
		return null;
	}
	
}
