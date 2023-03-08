package it.tndigitale.a4g.uma.api;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.tndigitale.a4g.uma.business.service.configurazione.ConfigurazioneService;

@RestController
@RequestMapping(ApiUrls.CONFIGURAZIONI)
@Tag(name = "ConfigurazioneController", description = "API che gestisce la configurazione dei parametri UMA per l'anno di campagna in corso")
public class ConfigurazioneController {

	@Autowired
	private ConfigurazioneService configurazioneService;

	@Operation(summary = "Reperisce la data di limite per i prelievi in un anno di campagna", description = "")
	@PreAuthorize("@abilitazioniComponent.checkIstruttoreUma()")
	@GetMapping
	public LocalDateTime getDataLimitePrelievi(@RequestParam int annoCampagna) {
		return configurazioneService.getDataLimitePrelievi(annoCampagna);
	}

	@Operation(summary = "Inserisce la data di limite per i prelievi in un anno di campagna", description = "")
	@PreAuthorize("@abilitazioniComponent.checkIstruttoreUma()")
	@PostMapping
	public Long modificaDataLimitePrelievi(
			@RequestBody 
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
			@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
			LocalDateTime dataLimitePrelievi) {
		return configurazioneService.modificaDataLimitePrelievi(dataLimitePrelievi);
	}
}
