package it.tndigitale.a4g.proxy.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.proxy.dto.FormatoStampa;
import it.tndigitale.a4g.proxy.services.StampaService;

@RestController
@RequestMapping
@Api(value = "Servizi per la produzione delle stampe")
public class StampaController {

	private StampaService servizioStampa;

	private static final Logger logger = LoggerFactory.getLogger(StampaController.class);

	private Map<FormatoStampa, BiFunction<String, InputStream, byte[]>> mappaFormatoElaborazione;

	@PostConstruct
	protected void init() throws Exception {
		mappaFormatoElaborazione = new HashMap<FormatoStampa, BiFunction<String, InputStream, byte[]>>();
		mappaFormatoElaborazione.put(FormatoStampa.PDF, (inputData, template) -> stampaJSON2PDF(inputData, template));
		mappaFormatoElaborazione.put(FormatoStampa.PDF_A, (inputData, template) -> stampaJSON2PDFA(inputData, template));
		mappaFormatoElaborazione.put(FormatoStampa.DOCX, (inputData, template) -> stampaJSON2DOCX(inputData, template));
	}

	@ApiOperation("Produce una stampa nel formato richiesot a partire dal template e dai dati in input")
	@PostMapping(ApiUrls.STAMPA)
	public byte[] stampa(
			@ApiParam(value = "Template docx della stampa", required = true) 
			@RequestParam("file") MultipartFile templateFile, 
			@ApiParam(value = "Dati in formato json per produrre la stampa", required = true) 
			@RequestParam("dati") String inputData,
			@ApiParam(value = "Formato di output", required = true) 
			@RequestParam("formatoStampa") FormatoStampa formatoStampa) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("stampa: inputData = {} ", inputData);
		}
		InputStream template = templateFile.getInputStream();
		if (logger.isDebugEnabled()) {
			logger.debug("stampa: dati di input {} ", inputData);
		}
		return mappaFormatoElaborazione.get(formatoStampa).apply(inputData, template);
	}

	@ApiOperation("Produce una stampa nel formato richiesto a partire dal template e dai dati in input. V2: Parametri passati nel body della richiesta.")
	@PostMapping(path = ApiUrls.STAMPA_V2, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<byte[]> stampaV2(
			@ApiParam(value = "Formato di output", required = true) 
			@RequestParam("formatoStampa") FormatoStampa formatoStampa,
			@ApiParam(value = "Template docx della stampa", required = true) 
			@RequestPart(name = "template") MultipartFile template, 
			@ApiParam(value = "Dati in formato json per produrre la stampa", required = true) 
			@RequestPart("dati") String dati
			) throws IOException  {
		
		String extension;
		MediaType mediaType;
		if (formatoStampa.equals(FormatoStampa.DOCX)) {
			extension = ".docx";
			mediaType = MediaType.APPLICATION_OCTET_STREAM;
		} else {
			extension = ".pdf";
			mediaType = MediaType.APPLICATION_PDF;
		}
		return ResponseEntity.ok()
				.contentType(mediaType)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=stampa" + extension)
				.body(mappaFormatoElaborazione.get(formatoStampa).apply(dati, template.getInputStream()));
	}

	protected byte[] stampaJSON2PDF(String inputData, InputStream template) {
		try {
			return servizioStampa.stampaJSON2PDF(inputData, template);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected byte[] stampaJSON2PDFA(String inputData, InputStream template) {
		try {
			return servizioStampa.stampaJSON2PDFA(inputData, template);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected byte[] stampaJSON2DOCX(String inputData, InputStream template) {
		try {
			return servizioStampa.stampaJSON2DOCX(inputData, template);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Autowired
	public void setServizioStampa(StampaService servizioStampa) {
		this.servizioStampa = servizioStampa;
	}
}
