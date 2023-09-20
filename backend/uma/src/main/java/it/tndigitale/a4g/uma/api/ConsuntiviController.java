package it.tndigitale.a4g.uma.api;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.tndigitale.a4g.uma.business.persistence.entity.AllegatoConsuntivoModel;
import it.tndigitale.a4g.uma.business.service.consumi.ConsuntiviService;
import it.tndigitale.a4g.uma.dto.consumi.ConsuntivoDto;

@RestController
@RequestMapping(ApiUrls.CONSUMI)
@Tag(name = "ConsuntiviController", description = "API che gestisce i consuntivi di una dichiarazioni consumi")
public class ConsuntiviController {

	private static final String NO_CONSUNTIVI = "Non ci sono consuntivi da dichiarare";
	private static final String SUB_DIRECTORY_RICHIESTE = "/richieste-carburante";

	@Autowired
	private ConsuntiviService consuntiviService;
	
	@Value("${pathDownload}")
	private String pathDownload;

	@Operation(summary = "Recupera i consuntivi dichiarati per una dichiarazione consumi. Recupera anche le informazioni di eventuali allegati", description = "")
	@PreAuthorize("@abilitazioniComponent.checkRicercaDichiarazioneConsumi(#id)")
	@GetMapping("/{id}" + ApiUrls.CONSUNTIVI)
	public List<ConsuntivoDto> getConsuntivi(@PathVariable(required = true) Long id) {
		return consuntiviService.getConsuntivi(id);
	}

	@Operation(summary = "Ottiene il documento allegato al consuntivo indicato by id", description = "")
	@PreAuthorize("@abilitazioniComponent.checkRicercaAllegatiConsuntivoDichiarazioneConsumi(#id, #idConsuntivo,#idAllegato)")
	@GetMapping("/{id}" + ApiUrls.CONSUNTIVI + "/{idConsuntivo}" + ApiUrls.ALLEGATI + "/{idAllegato}" + ApiUrls.STAMPA)
	public ResponseEntity<Resource> getAllegatoConsuntivo(@PathVariable(required = true) Long id,
			@PathVariable(required = true) Long idConsuntivo, @PathVariable(required = true) Long idAllegato) throws IOException {
		AllegatoConsuntivoModel allegatoConsuntivo = consuntiviService.getAllegatoConsuntivo(idAllegato);

		Path fileRichiesta = Paths
				.get(this.pathDownload + this.SUB_DIRECTORY_RICHIESTE + "/" + Calendar.YEAR + "/"
						+ allegatoConsuntivo.getNomeFile());

		return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
				.header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=".concat(allegatoConsuntivo.getNomeFile()))
				.body(new ByteArrayResource(Files.readAllBytes(fileRichiesta)));
	}

	// bug swagger-ui: https://github.com/springdoc/springdoc-openapi/issues/820 -  workaround: incapsulare il json in un file .json
	@Operation(summary = "Crea o aggiorna un consuntivo di una dichiarazione consumi con i relativi allegati", description = "E' necessario passare la referenza agli allegati: tutti gli allegati presenti a sistema e non passati nel servizio (tramite id) vengono cancellati. Gli allegati dell'ammissibile devono avere il nome file così composto: nomefile.pdf$$tipoDocumento$$descrizione")
	@PreAuthorize("@abilitazioniComponent.checkModificaDichiarazioneConsumi(#id)")
	@PostMapping(path = ("/{id}" + ApiUrls.ALLEGATI), consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Long dichiaraAllegati(@PathVariable(required = true) Long id,
			@RequestPart(name = "consuntivo", required = true) @Parameter(schema = @Schema(type = "string", format = "binary")) ConsuntivoDto consuntivo,
			@RequestPart(name = "allegati") List<MultipartFile> allegati) throws IOException {

		List<ByteArrayResource> attachments = new ArrayList<>();
		if (!CollectionUtils.isEmpty(allegati)) {
			for (MultipartFile allegato : allegati) {
				attachments.add(new ByteArrayResource(allegato.getBytes()) {
					@Override
					public String getFilename() {
						return allegato.getOriginalFilename();
					}
				});
			}
		}
		return consuntiviService.dichiaraAllegati(id , consuntivo, attachments);
	}

	@Operation(summary = "Dichiara consuntivi associati alla dichiarazione consumi. Restituisce i consuntivi salvati", description = "Aggiorna i consuntivi in input se esistono, altrimenti li crea. I consuntivi per cui viene passata quantità nulla vengono eliminati. Il campo InfoAllegati viene ignorato")
	@PreAuthorize("@abilitazioniComponent.checkModificaDichiarazioneConsumi(#id)")
	@PostMapping(path = ("/{id}" + ApiUrls.CONSUNTIVI))
	public List<ConsuntivoDto> dichiaraConsuntivi(@PathVariable(required = true) Long id, @RequestBody(required = true) List<ConsuntivoDto> consuntivi) {

		if (CollectionUtils.isEmpty(consuntivi)) {
			throw new IllegalArgumentException(NO_CONSUNTIVI);
		}

		return consuntiviService.dichiaraConsuntivi(id, consuntivi);
	}

	@Operation(summary = "Elimina il consuntivo ed i relativi allegati", description = "")
	@PreAuthorize("@abilitazioniComponent.checkModificaConsuntiviDichiarazioneConsumi(#id, #idConsuntivo)")
	@DeleteMapping("/{id}" + ApiUrls.CONSUNTIVI + "/{idConsuntivo}")
	public void deleteConsuntivo(@PathVariable(required = true) Long id,
			@PathVariable(required = true) Long idConsuntivo) {
		consuntiviService.deleteConsuntivo(idConsuntivo);
	}
}
