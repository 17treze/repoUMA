package it.tndigitale.a4g.fascicolo.dotazionetecnica.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.ApiUrls;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.AmbitoTipologia;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.service.TipologieService;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.ClasseFunzionaleDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.SottotipoDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.TipologiaDto;

@RestController
@RequestMapping(ApiUrls.API_V1 + ApiUrls.TIPOLOGIE)
@Tag(name = "TipologiaController", description = "Informazioni di dettaglio tipologie di macchinari")
public class TipologiaController {

	@Autowired
	private TipologieService tipologieService;

	@Operation(summary = "Recupera le tipologie di macchinari", description = "")
	@GetMapping
	public List<TipologiaDto> getTipologie(@RequestParam(required = false) AmbitoTipologia ambito) {
		return tipologieService.getTipologie(ambito);
	}

	@Operation(summary = "Recupera le classi associati ad un tipo di macchinario", description = "")
	@GetMapping("/{id}" + ApiUrls.CLASSI_FUNZIONALI)
	public ClasseFunzionaleDto getClassiFunzionali(@PathVariable Long id) {
		return tipologieService.getClasseFunzionale(id);
	}

	@Operation(summary = "Recupera i sottotipi associati ad un tipologia", description = "")
	@GetMapping("/{id}" + ApiUrls.SOTTOTIPI)
	public SottotipoDto getSottotipi(@PathVariable Long id, @RequestParam(required = false) AmbitoTipologia ambito) {
		return tipologieService.getSottotipi(id, ambito);
	}

}
