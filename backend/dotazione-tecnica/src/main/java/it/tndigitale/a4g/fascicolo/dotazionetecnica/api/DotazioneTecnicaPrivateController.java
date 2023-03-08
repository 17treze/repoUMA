package it.tndigitale.a4g.fascicolo.dotazionetecnica.api;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.ApiUrls;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.service.ControlliFascicoloDotazioneTecnicaCompletoEnum;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.service.DotazioneTecnicaService;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.service.FascicoloService;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.service.MacchineService;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.DettaglioMacchinaDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.ReportValidazioneDto;
import it.tndigitale.a4g.framework.component.dto.EsitoControlloDto;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.FascicoloNonValidabileException;

@RestController
@RequestMapping(ApiUrls.DOTAZIONE_TECNICA_PRIVATE)
@Tag(name = "DotazioneTecnicaPrivateController", description = "API privata per dotazione tecnica")
public class DotazioneTecnicaPrivateController {

	private static final Logger logger = LoggerFactory.getLogger(DotazioneTecnicaPrivateController.class);

	@Autowired
	private DotazioneTecnicaService dotazioneTecnicaService;
	@Autowired
	private MacchineService macchineService;
	@Autowired
	private FascicoloService fascicoloService;
	@Autowired
	private ObjectMapper mapper;

	@ApiOperation("Reperisce il report della scheda di validazione")
	@GetMapping("/{cuaa}/report-scheda-validazione")
	public ReportValidazioneDto getReportValidazione(@PathVariable(value = "cuaa", required = true) final String cuaa) {
		return dotazioneTecnicaService.getReportValidazione(cuaa);
	}

	@ApiOperation("Servizio che permette di validare un fascicolo corrente")
	@PutMapping("/{cuaa}/{idValidazione}/validazione")
	public void startValidazioneFascicolo(
			@ApiParam(value = "Codice fiscale per cui si vuole validare il fascicolo corrente", required = true)
			@PathVariable(value = "cuaa", required = true) final String cuaa,
			@PathVariable(value = "idValidazione", required = true) final Integer idValidazione)
					throws FascicoloNonValidabileException {
		dotazioneTecnicaService.startValidazioneFascicolo(cuaa, idValidazione);
	}

	@Operation(summary = "Migra da AGS in A4G le macchine del fascicolo")
	@PostMapping("/{cuaa}/migra-macchine")
	public void migraMacchine(
			@PathVariable(name = "cuaa", required = true) String cuaa) {
		try {
			dotazioneTecnicaService.migraMacchine(cuaa);
		} catch (Exception e) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@Operation(summary = "Migra da AGS in A4G i fabbricati del fascicolo")
	@PostMapping("/{cuaa}/migra-fabbricati")
	public void migraFabbricati(
			@PathVariable(name = "cuaa", required = true) String cuaa) {
		try {
			dotazioneTecnicaService.migraFabbricati(cuaa);
		} catch (Exception e) {
			logger.error("Errore in fase di migrazione dei fabbricati per " + cuaa, e);
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@Operation(summary = "Inserisce o aggiorna una macchina nel fascicolo", description = "")
	@PostMapping(path = "/{cuaa}" + ApiUrls.MACCHINE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Long> postMacchina(
			@PathVariable String cuaa,
			@ApiParam(required = true) @RequestPart(name = "documento", required = true) MultipartFile documento,
			@ApiParam(required = true) @RequestPart(name = "dati", required = true) String dettaglioMacchinaDto
			) throws IOException {
		DettaglioMacchinaDto dettaglioMacchinaDtoMapped = mapper.readValue(dettaglioMacchinaDto, DettaglioMacchinaDto.class);

		ByteArrayResource documentoByteAsResource = new ByteArrayResource(documento.getBytes()) {
			@Override
			public String getFilename() {
				return documento.getOriginalFilename();
			}
		};

		try {
			return ResponseEntity.ok(macchineService.dichiaraMacchina(dettaglioMacchinaDtoMapped, cuaa, documentoByteAsResource));
		} catch (Exception e) {
			if (e.getMessage().equalsIgnoreCase(MacchineService.MACCHINA_GIA_ESISTENTE)) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body(-1L);
			}
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(-1L);
		}
	}

	@Operation(summary = "Elimina un macchinario", description = "")
	@DeleteMapping("/{cuaa}" + ApiUrls.MACCHINE + "/{id}")
	public void cancellaMacchina(
			@PathVariable(name = "cuaa", required = true) final String cuaa,
			@PathVariable Long id) {
		macchineService.cancellaMacchina(id);
	}


	@ApiOperation("Restituisce l'esito per ogni controllo di completezza del fascicolo. I controlli danno esituo TRUE se superati con successo, FALSE altrimenti.")
	@GetMapping("/{cuaa}/controllo-completezza")
	public Map<ControlliFascicoloDotazioneTecnicaCompletoEnum, EsitoControlloDto> queryControlloCompletezzaFascicolo(
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

	@ApiOperation("Restituisce l'esito per ogni controllo di completezza del fascicolo. I controlli danno esituo TRUE se superati con successo, FALSE altrimenti.")
	@PutMapping("/{cuaa}/start-controllo-completezza")
	public void startControlloCompletezzaFascicoloAsincrono(
			@PathVariable(value = "cuaa") @ApiParam(value = "CUAA azienda agricola", required = true)
			final String cuaa) {
		fascicoloService.startControlloCompletezzaFascicoloAsincrono(cuaa, 0);
	}
}
