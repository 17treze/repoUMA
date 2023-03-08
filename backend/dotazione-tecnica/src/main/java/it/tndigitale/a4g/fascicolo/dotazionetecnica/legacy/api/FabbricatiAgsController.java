package it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.api;

import java.util.List;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.ApiUrls;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.business.service.FabbricatiAgsService;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.dto.FabbricatoAgsDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.dto.FabbricatoAgsFilter;

@RestController
@RequestMapping(ApiUrls.LEGACY + ApiUrls.FABBRICATI)
@Tag(name = "FabbricatiAgsController", description="Gestione fabbricati fascicolo legacy")
public class FabbricatiAgsController {

	@Autowired
	private FabbricatiAgsService fabbricatiAgsService;

	@Operation(summary = "Reperisce Informazioni sui fabbricati del fascicolo", description = "Utilizzare il seguente formato per il campo data: yyyy-MM-dd'T'HH:mm Inoltre è possibile filtrare per le province di provenienza (Sigle) e il titolo di conduzione. Valori di default per province: TN e BZ")
	@GetMapping
	public List<FabbricatoAgsDto> getFabbricati(@RequestParam(required = true) String cuaa, @ParameterObject FabbricatoAgsFilter filter) {

		if (!CollectionUtils.isEmpty(filter.getProvince())) {
			filter.getProvince().forEach(sigla -> {
				if (sigla.length() != 2) {
					throw new IllegalArgumentException("Una o più sigla una di una provincia risulta invalida: " + sigla);
				}
			});
		}
		filter.setCuaa(cuaa);
		return fabbricatiAgsService.getFabbricati(filter);
	}

}
