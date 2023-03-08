package it.tndigitale.a4gistruttoria.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4gistruttoria.service.CUPService;

@RestController
@RequestMapping(ApiUrls.CUP_V1)
@Api(value = "Gestione interscambio CUP")
public class CUPController {

	private static final Logger logger = LoggerFactory.getLogger(CUPController.class);
	
	@Autowired
	private CUPService service;
	
	@ApiOperation("Dati i dati in input, formato csv, genera l'xml da inviare al cup")
	@PostMapping
	public byte[] generaXML(
			@ApiParam(value = "Dati di input in formato csv", required = true) 
			@RequestParam("file") MultipartFile templateFile) throws Exception {
		byte[] csv = templateFile.getBytes();
		if (logger.isDebugEnabled()) {
			logger.debug("generaXML: csv {} ", csv);
		}
		return service.generaXMLCup(csv);
	}
}
