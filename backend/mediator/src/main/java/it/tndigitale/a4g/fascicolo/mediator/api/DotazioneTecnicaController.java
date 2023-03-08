package it.tndigitale.a4g.fascicolo.mediator.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.tndigitale.a4g.fascicolo.mediator.business.service.fascicolo.DotazioneTecnicaService;

@RestController
@RequestMapping(ApiUrls.DOTAZIONE_TECNICA)
@Tag(name = "DotazioneTecnicaController", description = "API per mediator verso dotazione tecnica")
public class DotazioneTecnicaController {
	
	@Autowired
	private DotazioneTecnicaService dotazioneTecnicaService;
	
	@ApiOperation("Inserisce o aggiorna una macchina nel fascicolo")
	@PostMapping(value = "/{cuaa}/macchine", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	@PreAuthorize("@abilitazioniComponent.checkLetturaFascicolo(#cuaa)")
	public Long dichiaraMacchina(
			@PathVariable String cuaa,
			@ApiParam(required = false) @RequestPart(name = "documento", required = false) MultipartFile documento,
			@ApiParam(required = true) @RequestPart(name = "dati", required = true) String dettaglioMacchinaDto
			) throws IOException {
		File tmp = File.createTempFile("documento_possesso", ".pdf");
		if(documento != null) {
			try (OutputStream os = new FileOutputStream(tmp)) {
			    os.write(documento.getBytes());
			}
		}
		
		Long idMacchinaSalvata = dotazioneTecnicaService.dichiaraMacchina(dettaglioMacchinaDto, cuaa, tmp);
		tmp.delete();
		return idMacchinaSalvata;
	}
	
	@Operation(summary = "Elimina un macchinario", description = "")
	@DeleteMapping("/{cuaa}/macchine/{id}")
	@PreAuthorize("@abilitazioniComponent.checkLetturaFascicolo(#cuaa)")
	public void cancellaMacchina(
			@PathVariable(name = "cuaa", required = true) final String cuaa,
			@PathVariable Long id) {
		dotazioneTecnicaService.cancellaMacchina(cuaa, id);
	}
}