package it.tndigitale.a4g.proxy.api;

import it.tndigitale.a4g.proxy.dto.sian.conduzioneterreno.ConduzioneDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.proxy.dto.fascicolo.sian.FascicoloSian;
import it.tndigitale.a4g.proxy.services.SianService;

import java.util.List;

@RestController
//@RequestMapping("/api/v1/sian")
@RequestMapping(ApiUrls.SIAN)
@Api(value = "SIAN - SERVIZI DI INTERSCAMBIO DATI E COOPERAZIONE APPLICATIVA")
public class SianController {
	
	@Autowired
	private SianService sianService;
	
	@ApiOperation("Visura dei dati anagrafici generali di un fascicolo contenuto nei sistemi SIAN")
	@GetMapping(value = "{cuaa}")
	public FascicoloSian verificaEsistenzaFascicolo(@ApiParam(value = "cuaa", required = true) @PathVariable(value = "cuaa") String cuaa) {
		return sianService.trovaFascicolo(cuaa);
	}

	@ApiOperation("Visura dei dati di consistenza di un fascicolo contenuto nei sistemi SIAN")
	@GetMapping(value = "/{cuaa}/leggi-consistenza")
	public List<ConduzioneDto> leggiConsistenza(@ApiParam(value = "cuaa", required = true) @PathVariable(value = "cuaa") String cuaa) {
		return sianService.leggiConsistenza(cuaa);
	}
}
