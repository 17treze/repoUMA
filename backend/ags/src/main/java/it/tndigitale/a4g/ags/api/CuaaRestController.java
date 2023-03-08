package it.tndigitale.a4g.ags.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.ags.dto.Cuaa;
import it.tndigitale.a4g.ags.dto.InfoDomandaDU;
import it.tndigitale.a4g.ags.service.CuaaService;

@RestController
@RequestMapping(ApiUrls.CUAA)
@Api(value = "Controller per il recupero delle informazioni del CUAA")
public class CuaaRestController {

	@Autowired
	CuaaService serviceCuaa;

	@GetMapping("")
	public Cuaa getInfoCuaa(@RequestParam("cuaa") String cuaa) {
		return serviceCuaa.getInfoCuaa(cuaa);

	}
	
	@GetMapping(ApiUrls.DATI_DU + "/{cuaa}")
	@ApiOperation("Servizio per il recupero delle informazioni aggiuntive della Domanda DU relative al CUAA del richiedente")
	public InfoDomandaDU getInfoDomandaDU(@PathVariable String cuaa, @RequestParam(name = "anno") @ApiParam("Anno di riferimento") Integer anno) {

		return serviceCuaa.findByCuaaIntestatarioAndAnnoRiferimento(cuaa, anno);
	}
	
}
