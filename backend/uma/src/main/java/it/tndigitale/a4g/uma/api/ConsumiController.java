package it.tndigitale.a4g.uma.api;

import java.io.IOException;
import java.util.List;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4g.uma.business.service.consumi.DichiarazioneConsumiService;
import it.tndigitale.a4g.uma.business.service.consumi.RicercaDichiarazioneConsumiService;
import it.tndigitale.a4g.uma.business.service.consumi.StampaDichiarazioneConsumiService;
import it.tndigitale.a4g.uma.business.service.protocollo.ProtocolloService;
import it.tndigitale.a4g.uma.dto.DomandaUmaDto;
import it.tndigitale.a4g.uma.dto.consumi.CarburanteDto;
import it.tndigitale.a4g.uma.dto.consumi.DichiarazioneConsumiDto;
import it.tndigitale.a4g.uma.dto.consumi.DichiarazioneConsumiFilter;
import it.tndigitale.a4g.uma.dto.consumi.DichiarazioneConsumiPagedFilter;
import it.tndigitale.a4g.uma.dto.consumi.DichiarazioneConsumiPatch;
import it.tndigitale.a4g.uma.dto.richiesta.PresentaRichiestaDto;

@RestController
@RequestMapping(ApiUrls.CONSUMI)
@Tag(name = "ConsumiController", description = "API che gestisce le dichiarazioni consumi")
public class ConsumiController {

	@Autowired
	private RicercaDichiarazioneConsumiService ricercaDichiarazioneConsumiService;
	@Autowired
	private DichiarazioneConsumiService dichiarazioneConsumiService;
	@Autowired
	private ProtocolloService protocolloService;
	@Autowired
	private StampaDichiarazioneConsumiService stampaDichiarazioneConsumiService;
	
	@Operation(summary = "Ricerca Dichiarazioni Consumi solo per anno di campagna", description = "Servizio performante ad uso dei CAA e utenti - contiene filtri per abilitazioni")
	@PreAuthorize("@abilitazioniComponent.checkRicercaEnteDomandeUma()")
	@GetMapping(ApiUrls.CAA)
	public List<DomandaUmaDto> getDichiarazioniConsumiCaa(Long campagna) {
		return ricercaDichiarazioneConsumiService.getDichiarazioniConsumi(campagna);
	}

	@Operation(summary = "Ottiene delle dichiarazioni consumi in base ai filtri indicati", description = "Ad uso dei CAA e utenti - contiene filtri per abilitazioni")
	@PreAuthorize("@abilitazioniComponent.checkRicercaDomandeUma()")
	@GetMapping
	public List<DichiarazioneConsumiDto> getDichiarazioniConsumi(@ParameterObject DichiarazioneConsumiFilter filter) {
		return ricercaDichiarazioneConsumiService.getDichiarazioniConsumi(filter);
	}

	@Operation(summary = "Ottiene delle dichiarazioni consumi indicati cuaa, anno e stati", description = "Ad uso per gli istruttori - recupera tutte le domande")
	@PreAuthorize("@abilitazioniComponent.checkRicercaTuttiDomandeUma()")
	@GetMapping("/paged")
	public RisultatiPaginati<DichiarazioneConsumiDto> getDichiarazioniConsumiPaged(@ParameterObject DichiarazioneConsumiPagedFilter filter) throws Exception {
		return ricercaDichiarazioneConsumiService.getDichiarazioniConsumiPaged(filter);
	}

	@Operation(summary = "Presenta una dichiarazione consumi", description = "")
	@PreAuthorize("@abilitazioniComponent.checkPresentaDomandaUma(#presentaRichiestaDto.getCuaa())")
	@PostMapping
	public Long presentaDichiarazione(@RequestBody(required = true) PresentaRichiestaDto presentaRichiestaDto) {

		if (presentaRichiestaDto.getCuaa() == null) {
			throw new IllegalArgumentException("Il CUAA non è presente");
		}
		if (presentaRichiestaDto.getCodiceFiscaleRichiedente() == null) {
			throw new IllegalArgumentException("Il codice fiscale del rappresentante non è presente");
		}
		return dichiarazioneConsumiService.presentaDichiarazione(presentaRichiestaDto);
	}

	@Operation(summary = "Aggiorna una dichiarazione consumi", description = "")
	@PreAuthorize("@abilitazioniComponent.checkModificaDichiarazioneConsumi(#id)")
	@PutMapping("/{id}")
	public void aggiornaDichiarazione(@PathVariable(required = true) Long id, @RequestBody(required = true) DichiarazioneConsumiPatch dichiarazionePatch) {
		dichiarazioneConsumiService.aggiornaDichiarazione(id ,dichiarazionePatch);
	}

	@Operation(summary = "Ottiene una dichiarazioni consumi indicato l'id", description = "")
	@PreAuthorize("@abilitazioniComponent.checkRicercaDichiarazioneConsumi(#id)")
	@GetMapping("/{id}")
	public DichiarazioneConsumiDto getDichiarazioneConsumi(@PathVariable(required = true) Long id) {
		return ricercaDichiarazioneConsumiService.getDichiarazioneConsumi(id);
	}

	@Operation(summary = "Calcola il carburante ammissibile di una dichiarazione consumi", description = "")
	@PreAuthorize("@abilitazioniComponent.checkRicercaDichiarazioneConsumi(#id)")
	@GetMapping("/{id}" + ApiUrls.CARBURANTE)
	public CarburanteDto calcolaCarburanteAmmissibile(@PathVariable(required = true) Long id) {
		return dichiarazioneConsumiService.calcolaCarburanteAmmissibile(id);
	}

	@Operation(summary = "Elimina la dichiarazione consumi indicata (solo se nello stato in compilazione) e tutti i suoi dati", description = "")
	@PreAuthorize("@abilitazioniComponent.checkDeleteDichiarazioneConsumi(#id)")
	@DeleteMapping("/{id}")
	public void deleteDichiarazioneConsumi(@PathVariable(required = true) Long id) {
		dichiarazioneConsumiService.deleteDichiarazioneConsumi(id);
	}

	@Operation(summary = "Ottiene una dichiarazioni consumi indicato l'id", description = "")
	@PreAuthorize("@abilitazioniComponent.checkRicercaDichiarazioneConsumi(#id)")
	@GetMapping("/{id}" + ApiUrls.STAMPA)
	public ResponseEntity<Resource> stampaDichiarazioneConsumi(@PathVariable(required = true) Long id) throws IOException {
		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_PDF)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=".concat(id.toString()).concat("_dichiarazioneconsumi"))
				.body(stampaDichiarazioneConsumiService.stampaDichiarazioneConsumi(id));
	}

	@Operation(summary = "Verifica che una dichiarazione consumi sia valida", description = "")
	@PreAuthorize("@abilitazioniComponent.checkModificaDichiarazioneConsumi(#id)")
	@PostMapping("/{id}" + ApiUrls.VALIDA)
	public void valida(@PathVariable(required = true) Long id) {
		dichiarazioneConsumiService.valida(id);
	}

	@Operation(summary = "Protocolla una dichiarazione consumi", description = "")
	@PreAuthorize("@abilitazioniComponent.checkModificaDichiarazioneConsumi(#id)")
	@PostMapping(path = "{id}" + ApiUrls.PROTOCOLLA, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public void protocolla(@PathVariable Long id, @RequestParam(required = true) boolean haFirma,
			@RequestPart(name = "documento", required = true) MultipartFile documento) throws IOException {		
		ByteArrayResource documentoByteAsResource = new ByteArrayResource(
				documento.getBytes()) {
			@Override
			public String getFilename() {
				return documento.getOriginalFilename();
			}
		};
		protocolloService.protocollaDichiarazione(id, documentoByteAsResource, haFirma);
	}
}
