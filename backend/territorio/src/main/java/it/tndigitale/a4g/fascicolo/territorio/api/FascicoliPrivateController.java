package it.tndigitale.a4g.fascicolo.territorio.api;

import java.sql.SQLException;
import java.util.EnumMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.fascicolo.territorio.business.service.ControlliFascicoloAgsCompletoEnum;
import it.tndigitale.a4g.fascicolo.territorio.business.service.FascicoloService;
import it.tndigitale.a4g.fascicolo.territorio.dto.ReportValidazioneTerreniAgsDto;
import it.tndigitale.a4g.framework.component.dto.EsitoControlloDto;
import it.tndigitale.a4g.territorio.api.ApiUrls;

@Api(value = "Controller per gestione operazioni su fascicolo dedicati alla comunicazione tra microservizi")
@RestController
@RequestMapping(ApiUrls.FASCICOLI_PRIVATE_V1)
public class FascicoliPrivateController {

	@Autowired private FascicoloService fascicoloService;
	
	@ApiOperation("Restituisce l'esito per ogni controllo di completezza del fascicolo. I controlli danno esituo TRUE se superati con successo, FALSE altrimenti.")
	@PutMapping("/{cuaa}/start-controllo-completezza")
	public void startControlloCompletezzaFascicoloAsincrono(
			@PathVariable(value = "cuaa") @ApiParam(value = "CUAA azienda agricola", required = true)
			final String cuaa) {
		fascicoloService.startControlloCompletezzaFascicoloAsincrono(cuaa, 0);
	}

	@ApiOperation("Restituisce l'esito per ogni controllo di completezza del fascicolo. I controlli danno esituo TRUE se superati con successo, FALSE altrimenti.")
	@GetMapping("/{cuaa}/controllo-completezza")
	public Map<ControlliFascicoloAgsCompletoEnum, EsitoControlloDto> queryControlloCompletezzaFascicolo(
			@ApiParam(value = "Cuaa azienda per cui si vuole l'esito dei controlli", required = true)
			@PathVariable(required = true) final String cuaa) {
		return fascicoloService.queryControlloCompletezzaFascicolo(cuaa);
	}

	@ApiOperation("Rimozione dei controlli di completezza")
	@DeleteMapping("/{cuaa}/controllo-completezza")
	public void rimozioneControlliCompletezza(
			@PathVariable @ApiParam(value = "CUAA azienda agricola", required = true) final String cuaa) {
		fascicoloService.rimozioneControlliCompletezza(cuaa);
	}

	/**
	 * @param cuaa
	 * @return
	 * @throws NoSuchElementException
	 */
	@ApiOperation("Restituisce l'esito per ogni controllo di completezza del fascicolo. I controlli danno esituo TRUE se superati con successo, FALSE altrimenti.")
	@GetMapping("/{cuaa}/controllo-completezza-sincrono")
	public Map<ControlliFascicoloAgsCompletoEnum, EsitoControlloDto> controlloCompletezzaFascicoloSincrono(
			@ApiParam(value = "Cuaa azienda per cui si vuole ottenere il report", required = true)
			@PathVariable(required = true) final String cuaa) {
		try {
			return fascicoloService.getControlloCompletezzaFascicolo(cuaa);
		} catch (SQLException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT);
		}
	}

	/**
	 * @param cuaa
	 * @return
	 * @throws NoSuchElementException
	 */
	@ApiOperation("Restituisce la componente terreni per la stampa della scheda di validazione.")
	@GetMapping("/{cuaa}/conduzione-terreni")
	public ReportValidazioneTerreniAgsDto getReportValidazione(
			@ApiParam(value = "Cuaa azienda per cui si vuole ottenere il report", required = true)
			@PathVariable(required = true) final String cuaa) {
		try {
			return fascicoloService.getReportValidazione(cuaa);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT);
		}
	}
}
