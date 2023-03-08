package it.tndigitale.a4g.proxy.ags.bdn.api;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.proxy.ags.bdn.business.service.SincronizzazioneAllevamentiService;

@RestController
@Api(value = "Servizio che sincronizza i dati degli allevamenti della BDN in SIAP")
@RequestMapping("/sincronizza")
public class DatiBDNIstrutturiaPSRController {
	private final static Logger logger = LoggerFactory.getLogger(DatiBDNIstrutturiaPSRController.class);

	@Autowired
	private SincronizzazioneAllevamentiService sincronizzazioneAllevamentiService;

	@ApiOperation("Partendo dai cuaa che hanno presentato domanda di PSR nell'anno della data di riferimento, sincronizza i dati degli allevamenti dalla BDN")
	@GetMapping
	public Boolean sincronizzaAllevamentiPerIstruttoriaPsr(@ApiParam(value = "Data di riferimento da cui si vuole sincronizzare i dati di BDN. Formato: yyyy-MM-dd", required = true)
														   @RequestParam(required = true)
														   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
																   LocalDate dataRiferimento) {
		logger.debug("inizio della sincronizzazione di tutti i cuaa che hanno presentato domanda psr con data di riferimento {}", dataRiferimento);
		sincronizzazioneAllevamentiService.sincronizzaAllevamenti(dataRiferimento);
		return Boolean.TRUE;
	}

	@ApiOperation("Partendo dal cuaa indicato, sincronizza i dati degli allevamenti dalla BDN")
	@GetMapping("/{cuaa}")
	public Boolean sincronizzaSingoloAllevamento(@ApiParam(value = "CUAA di riferimento da cui si vuole sincronizzare i dati di BDN.", required = true)
													 @PathVariable(value = "cuaa")
															 String cuaa,
												 @ApiParam(value = "Data di riferimento da cui si vuole sincronizzare i dati di BDN. Formato: yyyy-MM-dd", required = true)
														   @RequestParam(required = true)
														   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
																   LocalDate dataRiferimento) {
		sincronizzazioneAllevamentiService.sincronizzaSingoloAllevamento(cuaa, dataRiferimento);
		return Boolean.TRUE;
	}

}
