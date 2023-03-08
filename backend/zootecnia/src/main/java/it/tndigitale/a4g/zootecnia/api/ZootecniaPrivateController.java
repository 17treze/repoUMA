package it.tndigitale.a4g.zootecnia.api;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.FascicoloNonValidabileException;
import it.tndigitale.a4g.zootecnia.business.persistence.repository.legacy.SincronizzazioneAgsException;
import it.tndigitale.a4g.zootecnia.business.service.ControlliFascicoloZootecniaCompletoEnum;
import it.tndigitale.a4g.zootecnia.business.service.FascicoloInvalidConditionException;
import it.tndigitale.a4g.zootecnia.business.service.ZootecniaService;
import it.tndigitale.a4g.zootecnia.dto.AnagraficaAllevamentoDto;
import it.tndigitale.a4g.zootecnia.dto.EsitoControlloDto;
import it.tndigitale.a4g.zootecnia.dto.ReportValidazioneDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(ApiUrls.ZOOTECNIA_PRIVATE)
@Tag(name = "ZootecniaPrivateController", description = "API privata per zootecnia")
public class ZootecniaPrivateController {
	@Autowired
	private ZootecniaService zootecniaService;
	
	@ExceptionHandler(FascicoloInvalidConditionException.class)
	public ResponseEntity<String> fascicoloInvalidConditionExceptionHandler(FascicoloInvalidConditionException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ApiOperation("Servizio che permette di validare un fascicolo corrente")
	@PutMapping("/{cuaa}/{idValidazione}/validazione")
	public void startValidazioneFascicolo(
			@ApiParam(value = "Codice fiscale per cui si vuole validare il fascicolo corrente", required = true)
			@PathVariable(value = "cuaa", required = true) final String cuaa,
			@PathVariable(value = "idValidazione", required = true) final Integer idValidazione)
					throws FascicoloNonValidabileException {
		zootecniaService.startValidazioneFascicolo(cuaa, idValidazione);
	}

	@Operation(summary = "Permette di ottenere la lista di allevamenti di un certo detentore,"
			+ "ritorna un errore se il fascicolo non supera i controlli di completezza")
	@GetMapping("/{cuaa}/fascicolo-completo/anagrafica-allevamenti")
	public List<AnagraficaAllevamentoDto> getAllevamentiFascicoloCompleto(
			@PathVariable(name = "cuaa", required = true) String cuaa,
			@RequestParam(required = false) Integer idValidazione) throws FascicoloInvalidConditionException {
		return zootecniaService.getAllevamentiFascicoloCompleto(cuaa, idValidazione == null ? 0 : idValidazione);
	}
	
	@Operation(summary = "Ottiene i dati del report di validazione della zootecnia")
	@GetMapping("/{cuaa}/report-scheda-validazione")
	public ReportValidazioneDto getReportValidazione(@PathVariable(name = "cuaa", required = true) String cuaa) {
		return zootecniaService.getReportValidazione(cuaa);
	}

	@ApiOperation("Servizio che permette di sincronizzare in AGS gli allevamenti posseduti dal cuaa passato in input")
	@PutMapping("/{cuaa}/{idValidazione}/fine-validazione")
	public void invioEventoFineValidazione(
			@ApiParam(value = "Codice fiscale per cui si vuole validare il fascicolo corrente", required = true)
			@PathVariable(value = "cuaa", required = true) final String cuaa,
			@PathVariable(value = "idValidazione", required = true) final Integer idValidazione) {
		zootecniaService.invioEventoFineValidazione(cuaa, idValidazione);
	}

	@ApiOperation("Restituisce l'esito per ogni controllo di completezza del fascicolo. I controlli danno esituo TRUE se superati con successo, FALSE altrimenti.")
	@GetMapping("/{cuaa}/controllo-completezza")
	public Map<ControlliFascicoloZootecniaCompletoEnum, EsitoControlloDto> queryControlloCompletezzaFascicolo(
			@ApiParam(value = "Cuaa azienda per cui si vuole ottenere il report", required = true)
			@PathVariable(required = true) final String cuaa) {
		return zootecniaService.queryControlloCompletezzaFascicolo(cuaa);
	}

	@ApiOperation("Restituisce l'esito per ogni controllo di completezza del fascicolo. I controlli danno esituo TRUE se superati con successo, FALSE altrimenti.")
	@PutMapping("/{cuaa}/start-controllo-completezza")
	public void startControlloCompletezzaFascicoloAsincrono(
			@ApiParam(value = "Cuaa azienda per cui si vuole ottenere il report", required = true)
			@PathVariable(required = true) final String cuaa) {
		zootecniaService.startControlloCompletezzaFascicoloAsincrono(cuaa, 0);
	}

	@ApiOperation("Servizio che permette di sincronizzare su AGS le informazioni di zootecnia")
	@PostMapping("/{cuaa}/sincronizza-ags")
	public void sincronizzazioneAgs(
			@ApiParam(value = "Codice fiscale di cui si vuole sincronizzare le informazioni", required = true)
			@PathVariable(value = "cuaa", required = true) String cuaa,
			@RequestParam(required = false) Integer idValidazione) throws SincronizzazioneAgsException {
		zootecniaService.sincronizzaAgs(cuaa, idValidazione == null ? 0 : idValidazione);
	}

	@ApiOperation("Rimozione dei controlli di completezza")
	@DeleteMapping("/{cuaa}/controllo-completezza")
	public void rimozioneControlliCompletezza(
			@PathVariable @ApiParam(value = "CUAA azienda agricola", required = true) final String cuaa) {
		zootecniaService.rimozioneControlliCompletezza(cuaa);
	}

	/**
	 * @param cuaa
	 * @return
	 * @throws NoSuchElementException
	 */
	@ApiOperation("Restituisce l'esito per ogni controllo di completezza del fascicolo. I controlli danno esituo TRUE se superati con successo, FALSE altrimenti.")
	@GetMapping("/{cuaa}/controllo-completezza-sincrono")
	public Map<ControlliFascicoloZootecniaCompletoEnum, EsitoControlloDto> controlloCompletezzaFascicoloSincrono(
			@ApiParam(value = "Cuaa azienda per cui si vuole ottenere il report", required = true)
			@PathVariable(required = true) final String cuaa) {
		try {
			return zootecniaService.getControlloCompletezzaFascicolo(cuaa);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT);
		}
	}
	
	@Operation(summary = "Aggiorna la lista di allevamenti e strutture associate per il proprietario alla data richiesta", description = "utilizzare il formato YYYY-MM-dd")
	@PutMapping("/{cuaa}/anagrafica-allevamenti/aggiorna")
	public void aggiornaAllevamenti(
			@PathVariable(name = "cuaa", required = true) String cuaa,
			@Schema(required = true, example = "2020-12-31" , description = "utilizzare il formato yyyy-mm-dd")
			@RequestParam(value = "dataRichiesta", required = true) 
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataRichiesta) {
		zootecniaService.aggiornaAllevamenti(cuaa, dataRichiesta);
	}
}
