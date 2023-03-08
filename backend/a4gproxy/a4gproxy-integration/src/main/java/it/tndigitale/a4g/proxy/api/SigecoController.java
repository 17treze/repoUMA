package it.tndigitale.a4g.proxy.api;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import it.tndigitale.a4g.proxy.services.SigecoService;

@RestController
//@RequestMapping("api/v1/sigeco")
@RequestMapping(ApiUrls.SIGECO)
@Api(value = "Controllo Sigeco")
public class SigecoController {

	private static final Logger logger = LoggerFactory.getLogger(SigecoController.class);
	
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	SigecoService serviceSigeco;

	@GetMapping("")
	public Long recuperaControlloSigeco(@RequestParam(value = "params") String parametri) throws IOException {

		JsonNode params = objectMapper.readTree(parametri);

		Long anno = params.path("anno").asLong();
		Long paramNum = params.path("numeroDomanda").asLong();
		String numeroDomanda = paramNum.toString();

		return serviceSigeco.recuperaEsitoControlloSigeco(anno, numeroDomanda);
	}
	
	@GetMapping("/flagConv")
	public Boolean recuperaFlagConv(@RequestParam(value = "params") String parametri) throws IOException {
		
		JsonNode params = objectMapper.readTree(parametri);
		Long annoCampagna = params.path("annoCampagna").asLong();
		Long numeroDomanda = params.path("numeroDomanda").asLong();
		String cuaa = params.path("cuaa").asText();

		return serviceSigeco.recuperaFlagConv(annoCampagna, numeroDomanda.toString(), cuaa)
				.orElseGet(() -> {
					logger.debug("Impossibile recuperare FLAG_CONV per i parametri inseriti"); 
					return false;
				});
	}

}
