package it.tndigitale.a4g.proxy.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import it.tndigitale.a4g.proxy.dto.AntimafiaEsitoDto;
import it.tndigitale.a4g.proxy.services.EsitoAntimafiaService;

@RestController
//@RequestMapping("api/v1/esitoAntimafia")
@RequestMapping(ApiUrls.ESITOANTIMAFIA)
@Api(value = "Esito Antimafia")

public class EsitoAntimafiaController {

	@Autowired
	private EsitoAntimafiaService esitoAntimafiaService;
	
	@GetMapping("/{cuaa}")
	public AntimafiaEsitoDto getEsitoAntimafia(@PathVariable String cuaa) {
		return esitoAntimafiaService.getEsitoAntimafia(cuaa);
	}
	
	@GetMapping("/cuaa-list")
	public List<AntimafiaEsitoDto> getEsitiAntimafia(@RequestParam List<String> cuaaList) {
		return esitoAntimafiaService.getEsitiAntimafia(cuaaList);
	}
}
