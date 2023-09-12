package it.tndigitale.a4g.uma.api;

import java.util.List;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.tndigitale.a4g.uma.business.service.comuni.ComuniService;
import it.tndigitale.a4g.uma.business.service.distributori.DistributoriService;
import it.tndigitale.a4g.uma.dto.ComuneDto;
import it.tndigitale.a4g.uma.dto.distributori.DistributoreDto;
import it.tndigitale.a4g.uma.dto.distributori.PresentaPrelievoDto;
import it.tndigitale.a4g.uma.dto.richiesta.PrelieviFilter;
import it.tndigitale.a4g.uma.dto.richiesta.PrelievoDto;

@RestController
@RequestMapping(ApiUrls.API_V1 + ApiUrls.COMUNI)
@Tag(name = "ComuniController", description = "API che gestisce richieste dei comuni")
public class ComuniController {

	@Autowired
	private ComuniService comuniService;

	@Operation(summary = "Recupera i comuni capofila", description = "Vengono recuperati i comuni capofila")
	@GetMapping
	public List<ComuneDto> getComuniCapofila() {
		return comuniService.getComuniCapofila();
	}

}
