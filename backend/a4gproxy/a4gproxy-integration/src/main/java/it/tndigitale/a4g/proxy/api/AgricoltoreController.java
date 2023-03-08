package it.tndigitale.a4g.proxy.api;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.tndigitale.a4g.proxy.dto.AgricoltoreSIAN;
import it.tndigitale.a4g.proxy.services.AgricoltoreService;

@RestController
//@RequestMapping("api/v1/agricoltore")
@RequestMapping(ApiUrls.AGRICOLTORE)
@Api(value = "Agricoltore Attivo")

public class AgricoltoreController {

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	AgricoltoreService serviceAgricoltore;

	@ApiOperation("Servizio per il recupero delle informazioni sincronizzate da SIAN per il codiceFiscale l' annoCampagna indicati nel parametro params.")
	@GetMapping("")
	public AgricoltoreSIAN recuperaAgricoltoreSian(@RequestParam(value = "params") String parametri) throws Exception {
		JsonNode params = objectMapper.readTree(parametri);

		BigDecimal annoCamp = params.path("annoCamp").decimalValue();
		String codFisc = params.path("codFisc").textValue();
		return serviceAgricoltore.recuperaAgricoltoreSian(codFisc, annoCamp);

	}

}
