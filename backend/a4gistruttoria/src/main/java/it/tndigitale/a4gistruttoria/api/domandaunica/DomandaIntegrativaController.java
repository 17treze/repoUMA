/**
 * 
 */
package it.tndigitale.a4gistruttoria.api.domandaunica;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4gistruttoria.api.ApiUrls;
import it.tndigitale.a4gistruttoria.dto.RicevutaDomandaIntegrativaZootecnia;
import it.tndigitale.a4gistruttoria.dto.StatisticaZootecnia;
import it.tndigitale.a4gistruttoria.service.DomandaIntegrativaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 
 * Controller per gestire operazioni sulle domande integrative
 * @author B.Conetta
 *
 */
@RestController
@RequestMapping(ApiUrls.DOMANDE_INTEGRATIVE_V1)
public class DomandaIntegrativaController {
		
	@Autowired
	private DomandaIntegrativaService domandaIntegrativaService;
	@Autowired
	private ObjectMapper objectMapper;


	@ApiOperation("Esegue l'upload della ricevuta della domanda integrativa")
	@PostMapping("/{idDomanda}/ricevuta")
	public RicevutaDomandaIntegrativaZootecnia salvaRicevuta(
			@PathVariable Long idDomanda,
			@ApiParam(value = "Ricevuta domanda integrativa", required = true) @RequestPart(value = "ricevuta") MultipartFile ricevuta)
			throws Exception {
		RicevutaDomandaIntegrativaZootecnia dto = new RicevutaDomandaIntegrativaZootecnia();
		dto.setIdDomanda(idDomanda);
		dto.setRicevuta(ricevuta.getBytes());
		dto = domandaIntegrativaService.salva(dto);
		return dto;
	}
	
	@ApiOperation("Esegue la cancellazione della ricevuta della domanda integrativa")
	@DeleteMapping("/{idDomanda}/ricevuta")
	public RicevutaDomandaIntegrativaZootecnia cancellaRicevuta(
			@PathVariable Long idDomanda)
					throws Exception {
		RicevutaDomandaIntegrativaZootecnia dto = new RicevutaDomandaIntegrativaZootecnia();
		dto.setIdDomanda(idDomanda);
		dto = domandaIntegrativaService.cancella(dto);
		return dto;
	}
	
	@ApiOperation("Restituisce il documento della ricevuta della domanda integrativa")
	@GetMapping(ApiUrls.STAMPA_RICEVUTA_DOMANDA_INTEGRATIVA)
	public byte[] stampaPdfRicevutaDomandaIntegrativa(@RequestParam("params") String parametri) throws IOException {
		JsonNode params = objectMapper.readTree(parametri);
		String idDomanda = params.get("idDomanda").textValue();
		long domanda = Long.parseLong(idDomanda);
		return domandaIntegrativaService.getRicevutaDomandaIntegrativa(domanda);
	}
}
