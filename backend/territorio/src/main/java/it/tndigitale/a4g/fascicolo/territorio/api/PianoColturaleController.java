package it.tndigitale.a4g.fascicolo.territorio.api;

import java.util.List;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.tndigitale.a4g.fascicolo.territorio.business.service.PianoColturaleService;
import it.tndigitale.a4g.fascicolo.territorio.dto.ParticellaDto;
import it.tndigitale.a4g.fascicolo.territorio.dto.PianoColturaleFilter;
import it.tndigitale.a4g.territorio.api.ApiUrls;

@RestController
@RequestMapping(ApiUrls.COLTURE)
@Tag(name = "PianoColturaleController", description="Piano Colturale API: gestione piani colturali di un fascicolo")
public class PianoColturaleController {

	@Autowired
	private PianoColturaleService pianoColturaleService;

	@Operation(summary = "Restituisce gli usi del suolo previsti dal piano colturale del fascicolo dell'anno e validi alla data indicata", description = "utilizzare il formato yyyy-MM-dd'T'HH:mm ; Usare la sigla di una provincia per il campo provincia ; Sigla Provincia Campo opzionale: Valori di default TN e BZ")
	@GetMapping("/{cuaa}")
	public List<ParticellaDto> getPianoColturale(@PathVariable(required = true) String cuaa,@ParameterObject PianoColturaleFilter filter) throws Exception {

		if (!CollectionUtils.isEmpty(filter.getProvince())) {
			filter.getProvince().forEach(sigla -> {
				if (sigla.length() != 2) {
					throw new IllegalArgumentException("Una o più sigla una di una provincia risulta invalida: " + sigla);
				}
			});
		}

		filter.setCuaa(cuaa);
		return pianoColturaleService.getPianoColturale(filter);
	}
}
