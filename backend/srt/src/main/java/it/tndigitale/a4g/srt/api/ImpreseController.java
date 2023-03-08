package it.tndigitale.a4g.srt.api;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.tndigitale.a4g.srt.dto.ImportoRichiesto;
import it.tndigitale.a4g.srt.services.ImpreseService;

@RestController
@RequestMapping("/api/v1/imprese")
@Api(value = "ImpreseController")
public class ImpreseController {

	private static final Logger logger = LoggerFactory.getLogger(ImpreseController.class);
	
	@Autowired
	private ImpreseService impreseService;

	@ApiOperation("")
	@GetMapping(path = "{cuaa}/importo-richiesto", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ImportoRichiesto> getImportoRichiesto(
			@PathVariable(value = "cuaa") String cuaa,
			@RequestParam(name = "data-modifica") Date dataModifica) //@DateTimeFormat(pattern="ddMMyyyy")
			throws Exception {

		logger.trace("ImpreseController getImportoRichiesto");
		
		//if (cuaa != null && cuaa.length() != 16 )
		//	throw new Exception("Codice CUAA non valido");

		return impreseService.getImportoRichiesto(cuaa,dataModifica);
	}
	
	
}
