package it.tndigitale.a4g.uma.api;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.tndigitale.a4g.framework.model.Attachment;
import it.tndigitale.a4g.uma.business.service.elenchi.ElenchiService;
import it.tndigitale.a4g.uma.business.service.elenchi.TipoElenco;

@RestController
@RequestMapping(ApiUrls.API_V1 + ApiUrls.ELENCHI)
@Tag(name = "ElenchiController", description = "API per il download di elenchi di dati UMA")
public class ElenchiController {

	@Autowired
	private ElenchiService elenchiService;

	@GetMapping("/{campagna}" + ApiUrls.STAMPA)
	@PreAuthorize("@abilitazioniComponent.checkCreazioneElenchi(#tipo)")
	public ResponseEntity<Resource> stampaElenco(@PathVariable(required = true) Long campagna, TipoElenco tipo) throws IOException {

		Attachment file = elenchiService.stampaElenco(tipo, campagna);

		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s.csv", file.getFileName()))
				.body(new ByteArrayResource(file.getFile()));
	}
}
