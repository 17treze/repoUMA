package it.tndigitale.a4g.fascicolo.territorio.api;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.tndigitale.a4g.fascicolo.territorio.business.persistence.repository.legacy.AgsStoredFunctionException;
import it.tndigitale.a4g.fascicolo.territorio.business.service.ConduzioneTerrenoService;
import it.tndigitale.a4g.fascicolo.territorio.dto.conduzione.ConduzioneSianDto;
import it.tndigitale.a4g.fascicolo.territorio.dto.conduzione.SottotipoConduzioneDto;
import it.tndigitale.a4g.fascicolo.territorio.dto.conduzione.SottotipoDocumentoDto;
import it.tndigitale.a4g.fascicolo.territorio.dto.conduzione.TipoConduzioneDto;
import it.tndigitale.a4g.territorio.api.ApiUrls;
import it.tndigitale.a4g.territorio.business.service.ConduzioneService;
import it.tndigitale.a4g.territorio.dto.ConduzioneTerreniDto;

@RestController
@RequestMapping(ApiUrls.CONDUZIONE_TERRENO)
@Tag(name = "ConduzioneTerrenoController", description="Conduzione Terreno API: visura delle conduzioni terreni")
public class ConduzioneTerrenoController {
	private static final Logger logger = LoggerFactory.getLogger(ConduzioneTerrenoController.class);
	
	@Autowired
	private ConduzioneTerrenoService service;
	@Autowired
	private ConduzioneService conduzioneService;

	@Operation(summary = "Restituisce i dati relativi alle conduzione terreni", description = "Restituisce i dati relativi alle conduzione terreni")
	@GetMapping(value = "/{cuaa}")
	public List<ConduzioneSianDto> getConduzioneTerreni(@ApiParam(value = "Codice fiscale di cui si vuole ottenere i dati relativi alla conduzione dei terreni", required = true)
												@PathVariable(value = "cuaa") String cuaa) {
		return service.getConduzioneTerreni(cuaa);
	}

	@Operation(summary = "Salva i dati relativi alle conduzione terreni in AGS", description = "Salva i dati relativi alle conduzione terreni in AGS")
	@PutMapping(value = "/{cuaa}")
	public void salvaConduzioneTerreni(@PathVariable(value = "cuaa") @ApiParam(value = "CUAA azienda agricola", required = true) String cuaa,
								 @RequestBody(required = true) List<ConduzioneSianDto> conduzioneDtoList)  {
		try {
			service.salvaConduzioneTerreni(cuaa, conduzioneDtoList);
		} catch (SQLException | JsonProcessingException | AgsStoredFunctionException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@Operation(summary = "Salva i dati relativi alle conduzioni terreni", description = "Salva i dati relativi alle conduzioni terreni")
	@PutMapping(value = "/salva/{cuaa}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void salvaConduzioneTerreniA4g(@PathVariable(value = "cuaa") @ApiParam(value = "CUAA azienda agricola", required = true) String cuaa,
								@RequestBody(required = true) ConduzioneTerreniDto conduzioneTerreno) {
		try {
			conduzioneService.salvaConduzioneTerreni(cuaa, conduzioneTerreno);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@Operation(summary = "tipologie di conduzione", description = "Restituisce l'elenco delle tipologie di conduzione")
	@GetMapping(value = "/elenco-tipi-conduzione")
	public List<TipoConduzioneDto> getElencoTipologieConduzione() {
		return service.getElencoTipologieConduzione();
	}
	
	@Operation(summary = "tipologie di conduzione", description = "Restituisce l'elenco delle tipologie di conduzione")
	
	@GetMapping(value = "{idTipoConduzione}/sottotipologie")
	public List<SottotipoConduzioneDto> getElencoSottoipologieConduzione(
			@PathVariable(value = "idTipoConduzione") @ApiParam(value = "codice tipologia di conduzione", required = true) 
			Long idTipoConduzione
			) { 		
		return service.getElencoSottoipologieConduzione(idTipoConduzione);
	}

	
	
	@Operation(summary = "tipologie documenti per sottotipo", description = "Restituisce l'elenco degli id dei documenti da allegare")

	@GetMapping(value = "/sottotipologia/{idSottoTipoConduzione}/sottotipodocumenti")
	public List<SottotipoDocumentoDto> getElencoSottotipoDocumenti(
		@PathVariable(value = "idSottoTipoConduzione") @ApiParam(value = "codice id sotto tipo conduzione", required = true) 
		Long idSottoTipoConduzione
	) { 		
		return service.getElencoSottotipoDocumenti(idSottoTipoConduzione);
	}
}
