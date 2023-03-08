package it.tndigitale.a4g.proxy.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.proxy.dto.DatiPagamentiDto;
import it.tndigitale.a4g.proxy.dto.Dichiarazione;
import it.tndigitale.a4g.proxy.dto.SuperficiAccertateDto;
import it.tndigitale.a4g.proxy.services.SincronizzazioneService;

@RestController
//@RequestMapping("/api/v1/sincronizzazione")
@RequestMapping(ApiUrls.SINCRONIZZAZIONE)
@Api(value = "Sincronizzazione dati verso sistemi esterni")
public class SincronizzazioneController {
	
	private static final Logger logger = LoggerFactory.getLogger(SincronizzazioneController.class);
	
	@Autowired
	private SincronizzazioneService sincronizzazioneService;

	@ApiOperation("Sincronizzazione dati antimafia verso AGEA")
	@PostMapping("/antimafia")
	public String dichiarazioneAntimafia(@RequestBody() String dichiarazioneAntimafia) throws Exception {
		sincronizzazioneService.dichiarazioneAntimafia(dichiarazioneAntimafia);
		return "OK";
	}
	
	@ApiOperation("Restituisce la dichiarazione antimafia per id")
	@GetMapping("/antimafia/{id}")
	public Dichiarazione getDatiSincronizzazione(@PathVariable @ApiParam(value = "Identificativo della dichiarazione", required = true) Long id) throws Exception {
		return sincronizzazioneService.getDichiarazione(id);
	}
	
	@ApiOperation("Aggiorna la dichiarazione antimafia per la sincronizzazione")
	@PutMapping("/antimafia/{id}")
	public Dichiarazione aggiornaDichiarazione(@PathVariable @ApiParam(value = "Identificativo della dichiarazione", required = true) Long id, @RequestBody() @ApiParam(value = "Dati della dichiarazione", required = true) Dichiarazione dichiarazione) throws Exception {
		dichiarazione.setIdAnti(id);
		return sincronizzazioneService.aggiornaDichiarazione(dichiarazione);
	}	
	
	@ApiOperation("Sincronizzazione dati superfici accertate verso AGEA")
	@PostMapping("/superfici-accertate")
	public String creaSuperficiAccertate(@RequestBody SuperficiAccertateDto superficiAccertate) throws Exception {
		sincronizzazioneService.creaSuperficiAccertate(superficiAccertate);
		return "OK";
	}
	
	@ApiOperation("Rimozione completa dati superfici accertate per AGEA")
	@PostMapping("/superfici-accertate/pulisci")
	public String pulisciSuperficiAccertate(@RequestBody Long annoCampagna) throws Exception {
		sincronizzazioneService.pulisciSuperficiAccertate(annoCampagna);
		return "OK";
	}
	
	@ApiOperation("Sincronizzazione dati pagamenti verso AGEA")
	@PostMapping("/pagamenti")
	public String creaDatiPagamenti(@RequestBody DatiPagamentiDto datiPagamenti) throws Exception {
		logger.debug("Inizio sincronizzazione pagamenti domanda numero {}", datiPagamenti.getNumeroDomanda());
		sincronizzazioneService.creaDatiPagamenti(datiPagamenti);
		logger.debug("Terminato sincronizzazione pagamenti domanda numero {}", datiPagamenti.getNumeroDomanda());
		return "OK";
	}
	
	@ApiOperation("Rimozione completa dati pagamenti per AGEA")
	@PostMapping("/pagamenti/pulisci")
	public String pulisciDatiPagamenti(@RequestBody Long annoCampagna) throws Exception {
		sincronizzazioneService.pulisciDatiPagamenti(annoCampagna);
		return "OK";
	}
}
