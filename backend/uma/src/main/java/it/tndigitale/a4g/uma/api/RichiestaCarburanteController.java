package it.tndigitale.a4g.uma.api;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
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
import it.tndigitale.a4g.uma.business.service.protocollo.ProtocolloService;
import it.tndigitale.a4g.uma.business.service.richiesta.RicercaRichiestaCarburanteService;
import it.tndigitale.a4g.uma.business.service.richiesta.RichiestaCarburanteService;
import it.tndigitale.a4g.uma.business.service.richiesta.StampaRichiestaCarburanteService;
import it.tndigitale.a4g.uma.dto.DomandaUmaDto;
import it.tndigitale.a4g.uma.dto.consumi.CarburanteDto;
import it.tndigitale.a4g.uma.dto.richiesta.CarburanteRichiestoDto;
import it.tndigitale.a4g.uma.dto.richiesta.CarburanteTotale;
import it.tndigitale.a4g.uma.dto.richiesta.PrelievoDto;
import it.tndigitale.a4g.uma.dto.richiesta.PresentaRichiestaDto;
import it.tndigitale.a4g.uma.dto.richiesta.RichiestaCarburanteDto;
import it.tndigitale.a4g.uma.dto.richiesta.RichiestaCarburanteFilter;
import it.tndigitale.a4g.uma.dto.richiesta.RichiestaCarburantePagedFilter;

// MIGRAZIONE A OPEN API 3: https://springdoc.org/migrating-from-springfox.html

@RestController
@RequestMapping(ApiUrls.RICHIESTE)
@Tag(name = "RichiestaCarburanteController", description = "API che gestisce le richieste di carburante ad uso agricolo")
public class RichiestaCarburanteController {
	
	private static final String NON_CI_SONO_FABBISOGNI_DA_DICHIARARE = "Non ci sono fabbisogni da dichiarare";
	
	@Autowired
	private RichiestaCarburanteService richiestaCarburanteService;
	@Autowired
	private RicercaRichiestaCarburanteService ricercaRichiestaCarburanteService;
	@Autowired
	private ProtocolloService protocolloService;
	@Autowired
	private StampaRichiestaCarburanteService stampaRichiestaCarburanteService;
	
	@Operation(summary = "Presenta una richiesta di carburante UMA", description = "All'atto della creazione vengono importati in uma, se presenti, i fabbricati associati almeno ad un gruppo di lavorazioni")
	@PreAuthorize("@abilitazioniComponent.checkPresentaDomandaUma(#presentaRichiestaDto.getCuaa())")
	@PostMapping
	public Long presentaRichiesta(@RequestBody(required = true)
	PresentaRichiestaDto presentaRichiestaDto) {
		
		if (presentaRichiestaDto.getCodiceFiscaleRichiedente() == null) {
			throw new IllegalArgumentException("Il codice fiscale del rappresentante non è presente");
		}
		return richiestaCarburanteService.presenta(presentaRichiestaDto);
	}
	
	@Operation(summary = "Dichiara il fabbisogno totale per una richiesta di carburante", description = "")
	@PreAuthorize("@abilitazioniComponent.checkModificaRichiestaCarburante(#id)")
	@PutMapping("{id}")
	public Long aggiornaRichiesta(@PathVariable
	Long id, @RequestBody(required = true)
	CarburanteRichiestoDto carburanteRichiestoDto) {
		if (carburanteRichiestoDto.getCarburanteRichiesto() == null) {
			throw new IllegalArgumentException(NON_CI_SONO_FABBISOGNI_DA_DICHIARARE);
		}
		return richiestaCarburanteService.aggiorna(id, carburanteRichiestoDto);
	}
	
	@Operation(summary = "Calcola il carburante ammissibile in base alle dichiarazioni espresse nella richiesta corrente", description = "")
	@PreAuthorize("@abilitazioniComponent.checkRicercaRichiestaDiCarburante(#id)")
	@GetMapping("{id}" + ApiUrls.CARBURANTE)
	public CarburanteDto getCarburanteAmmissibile(@PathVariable(required = true)
	Long id) {
		return richiestaCarburanteService.calcolaCarburanteAmmissibile(id);
	}
	
	@Operation(summary = "Ottiene una richiesta carburante UMA indicato l'id", description = "")
	@PreAuthorize("@abilitazioniComponent.checkRicercaRichiestaDiCarburante(#id)")
	@GetMapping("{id}")
	public RichiestaCarburanteDto getRichiestaCarburante(@PathVariable(required = true)
	Long id) {
		return ricercaRichiestaCarburanteService.getRichiesta(id);
	}
	
	@Operation(summary = "Ricerca Richieste di Carburante solo per anno di campagna", description = "Servizio performante ad uso dei CAA e utenti - contiene filtri per abilitazioni")
	@PreAuthorize("@abilitazioniComponent.checkRicercaEnteDomandeUma()")
	@GetMapping(ApiUrls.CAA)
	public List<DomandaUmaDto> getRichiesteCarburanteCaa(Long campagna) {
		return ricercaRichiestaCarburanteService.getRichieste(campagna);
	}
	
	@Operation(summary = "Ottieni delle richieste carburante in base ai filtri", description = "Ad uso dei CAA e utenti - contiene filtri per abilitazioni")
	@PreAuthorize("@abilitazioniComponent.checkRicercaDomandeUma()")
	@GetMapping
	public List<RichiestaCarburanteDto> getRichiesteCarburante(@ParameterObject
	RichiestaCarburanteFilter richiestaCarburanteFilter) {
		return ricercaRichiestaCarburanteService.getRichieste(richiestaCarburanteFilter);
	}
	
	@Operation(summary = "Ottieni delle richieste carburante in base ai filtri", description = "Ad uso per gli istruttori - recupera tutte le domande")
	@PreAuthorize("@abilitazioniComponent.checkRicercaTuttiDomandeUma()")
	@GetMapping("/paged")
	public RisultatiPaginati<RichiestaCarburanteDto> getRichiesteCarburantePaged(@ParameterObject
	RichiestaCarburantePagedFilter richiestaCarburantePagedFilter) throws Exception {
		return ricercaRichiestaCarburanteService.getRichiestePaged(richiestaCarburantePagedFilter);
	}
	
	@Operation(summary = "Cancella una richiesta di carburante in compilazione indicato l'id", description = "")
	@PreAuthorize("@abilitazioniComponent.checkDeleteRichiestaCarburante(#id)")
	@DeleteMapping("{id}")
	public void cancellaRichiesta(@PathVariable(required = true)
	Long id) {
		richiestaCarburanteService.cancellaRichiesta(id);
	}
	
	@Operation(summary = "Valida una richiesta di carburante", description = "Controlla che sia presente un fascicolo valido e validato almeno una volta durante l'anno in corso")
	@PreAuthorize("@abilitazioniComponent.checkModificaRichiestaCarburante(#id)")
	@PostMapping("{id}" + ApiUrls.VALIDA)
	public void valida(@PathVariable
	Long id) {
		richiestaCarburanteService.valida(id);
	}
	
	@Operation(summary = "Protocolla una richiesta di carburante", description = "")
	@PreAuthorize("@abilitazioniComponent.checkModificaRichiestaCarburante(#id)")
	@PostMapping(path = "{id}" + ApiUrls.PROTOCOLLA, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public void protocolla(@PathVariable
	Long id, @RequestParam(required = true)
	boolean haFirma, @RequestPart(name = "documento", required = true)
	MultipartFile documento) throws IOException {
		ByteArrayResource documentoByteAsResource = new ByteArrayResource(documento.getBytes()) {
			@Override
			public String getFilename() {
				return documento.getOriginalFilename();
			}
		};
		protocolloService.protocollaRichiesta(id, documentoByteAsResource, haFirma);
	}
	
	@Operation(summary = "Stampa il PDF della richiesta di carburante", description = "")
	@PreAuthorize("@abilitazioniComponent.checkRicercaRichiestaDiCarburante(#id)")
	@GetMapping("/{id}" + ApiUrls.STAMPA)
	public ResponseEntity<Resource> stampa(@PathVariable
	Long id) throws IOException {
		RichiestaCarburanteDto rich = ricercaRichiestaCarburanteService.getRichiesta(id);
		
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
				.header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=".concat(id.toString()).concat(
								rich.getIdRettificata() != null ? "_rettificacarburante" : "_richiestacarburante"))
				.body(stampaRichiestaCarburanteService.stampaRichiestaCarburante(id));
	}
	
	@Operation(summary = "Recupera i prelievi di carburante associati ad una richiesta, se presente la data di presentazione recupera i prelievi della campagna in corso fino a quella data", description = "")
	@PreAuthorize("@abilitazioniComponent.checkRicercaDomandaUma(#cuaa)")
	@GetMapping("/{campagna}" + ApiUrls.PRELIEVI)
	public CarburanteTotale<PrelievoDto> getPrelievi(@PathVariable
	Long campagna, @RequestParam(required = true)
	String cuaa, @RequestParam(required = false)
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	LocalDateTime dataPresentazione) {
		if (dataPresentazione != null && dataPresentazione.getYear() != campagna.intValue()) {
			throw new IllegalArgumentException(
					"L'anno di campagna non corrisponde a quello della data di presentazione");
		}
		return richiestaCarburanteService.getPrelievi(cuaa, campagna, dataPresentazione);
	}
}
