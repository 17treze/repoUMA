package it.tndigitale.a4gistruttoria.api.configurazioneistruttoria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4gistruttoria.api.ApiUrls;
import it.tndigitale.a4gistruttoria.dto.configurazioneistruttoria.ConfIstruttorieDto;
import it.tndigitale.a4gistruttoria.service.businesslogic.configurazioneistruttoria.ConfigurazioneIstruttoriaService;

@RestController
@RequestMapping(ApiUrls.ISTRUTTORIE_DU_CONF_V1 + "/istruttorie")
public class ConfigurazioneIstruttorieController {

	@Autowired
	private ConfigurazioneIstruttoriaService configurazioneIstruttoriaService;

	@GetMapping("/{annoCampagna}")
	@ApiOperation("Recupera i dati della configurazione istruttorie")
	@PreAuthorize("@abilitazioniPACComponent.checkVisualizzaIstruttoria()")
	public ConfIstruttorieDto getConfIstruttorie(
			@ApiParam("Anno Campagna")
			@PathVariable Integer annoCampagna
			) {
		return configurazioneIstruttoriaService.getConfIstruttorie(annoCampagna);
	}

	@PostMapping("/{annoCampagna}")
	@ApiOperation("Salvataggio dei dati della configurazione istruttorie")
	@PreAuthorize("@abilitazioniPACComponent.checkEditaIstruttoria()")
	public ConfIstruttorieDto saveConfIstruttorie(@PathVariable Integer annoCampagna, @RequestBody ConfIstruttorieDto confDto) {
		if (!annoCampagna.equals(confDto.getCampagna())) {
			throw new IllegalArgumentException("Anno campagna non congruente");
		}
		return configurazioneIstruttoriaService.saveOrUpdateConfIstruttorie(confDto);
	}
}
