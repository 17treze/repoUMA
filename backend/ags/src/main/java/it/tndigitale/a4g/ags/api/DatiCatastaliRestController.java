package it.tndigitale.a4g.ags.api;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.ags.dto.RegioneCatastale;
import it.tndigitale.a4g.ags.service.DatiCatastaliService;

@RestController
@RequestMapping(ApiUrls.DATI_CATASTALI_V1)
public class DatiCatastaliRestController {

	private static final Logger logger = LoggerFactory.getLogger(DatiCatastaliRestController.class);
	
	@Autowired
	DatiCatastaliService datiCatastaliService;

	@Autowired
	ObjectMapper objectMapper;

	@ApiOperation("Recupera la regione associata ad un determinato codice nazionale (di sezione)")
	@GetMapping("/sezioni/{codNazionale}/regione")
	public ResponseEntity<RegioneCatastale> getRegioneByCodNazionale(
			@ApiParam(value = "Codice Nazionale (p.es.: P370)", required = true) 
			@PathVariable(value = "codNazionale", required = true) String codNazionale, 
			@ApiParam(value = "Data di validità del dato catastale (formato: dd-MM-yyyy)", required = false) 
			@RequestParam(value = "dataValidita", required = false) 
			@DateTimeFormat(pattern="dd-MM-yyyy") Date dataValidita) {

		logger.trace("getRegioneByCodNazionale: codNazionale: {} ", codNazionale);
		
		RegioneCatastale result = datiCatastaliService.getRegioneByCodNazionaleAndDataValidita(codNazionale, dataValidita);
		return new ResponseEntity<>(result, result != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
	}
}
