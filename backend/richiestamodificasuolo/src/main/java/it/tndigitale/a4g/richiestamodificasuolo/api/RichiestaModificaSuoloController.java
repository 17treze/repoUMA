package it.tndigitale.a4g.richiestamodificasuolo.api;

import java.util.List;
import java.util.Optional;

import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.LavorazioneSuoloService;
import it.tndigitale.a4g.richiestamodificasuolo.dto.filter.SuoloDichiaratoLavorazioneFilter;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.SuoloDichiaratoLavorazioneDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento.Ordine;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.ProfiloUtente;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.richiestamodificasuolo.RichiestaModificaSuoloService;
import it.tndigitale.a4g.richiestamodificasuolo.dto.filter.RichiestaModificaSuoloFilter;
import it.tndigitale.a4g.richiestamodificasuolo.dto.richiestaModificaSuolo.DocumentazioneRichiestaDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.richiestaModificaSuolo.MessaggioRichiestaDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.richiestaModificaSuolo.RichiestaModificaSuoloDto;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(ApiUrls.RICHIESTA_MODIFICA_SUOLO)
@Api(value = "Controller per il recupero delle informazioni delle richieste modifica suolo")
public class RichiestaModificaSuoloController {

	@Autowired
	private RichiestaModificaSuoloService richiestaModificaSuoloService;

	@Autowired
	private LavorazioneSuoloService lavorazioneSuoloService;

	@Autowired
	private UtenteComponent utenteComponent;

	@GetMapping
	@ApiOperation("Ricerca paginata delle richieste di modifica suolo secondo i criteri di ricerca impostati")
	@PreAuthorize("@abilitazioniComponent.checkSearchRichiestaModificaSuolo(#filter)")
	public RisultatiPaginati<RichiestaModificaSuoloDto> ricercaRichiestaModificaSuolo(RichiestaModificaSuoloFilter filter, Paginazione paginazione, Ordinamento ordinamento) {
		return richiestaModificaSuoloService.ricerca(filter, paginazione, Optional.ofNullable(ordinamento).filter(o -> o.getProprieta() != null).orElse(Ordinamento.DEFAULT_ORDER_BY));
	}

	@GetMapping("/{idRichiesta}")
	@ApiOperation("Ricerca dettaglio della richieste di modifica suolo per l'id passato")
	@PreAuthorize("@abilitazioniComponent.checkAccessResourceRichiestaModificaSuolo(#idRichiesta)")
	public RichiestaModificaSuoloDto ricercaDettaglioRichiestaModificaSuolo(@PathVariable @ApiParam(value = "Identificativo della RichiestaModificaSuolo", required = true) Long idRichiesta) {
		return richiestaModificaSuoloService.ricercaDettaglio(idRichiesta);
	}

	@PutMapping("/{idRichiesta}")
	@ApiOperation("Aggiorna i dati aggiuntivi di una richiesta modifica suolo")
	@PreAuthorize("@abilitazioniComponent.checkEditRichiestaModificaSuolo(#idRichiesta)")
	public ResponseEntity<?> updateDatiAggiuntiviRichiestaModificaSuolo(@PathVariable @ApiParam(value = "Identificativo della RichiestaModificaSuolo", required = true) Long idRichiesta,
			@RequestBody RichiestaModificaSuoloDto richiestaModificaSuoloDto) {
		String username = utenteComponent.username();

		richiestaModificaSuoloDto.setId(idRichiesta);
		richiestaModificaSuoloDto.setUtente(username);
		richiestaModificaSuoloService.updateRichiestaModificaSuolo(richiestaModificaSuoloDto);

		return new ResponseEntity(HttpStatus.OK);
	}

	@GetMapping("/{idRichiesta}/messaggi")
	@ApiOperation("Ricerca paginata dei messaggi di una richiesta modifica suolo")
	@PreAuthorize("@abilitazioniComponent.checkAccessResourceRichiestaModificaSuolo(#idRichiesta)")
	public RisultatiPaginati<MessaggioRichiestaDto> ricercaMessaggiRichiestaModificaSuolo(@PathVariable("idRichiesta") Long idRichiesta, Paginazione paginazione, Ordinamento ordinamento) {
		return richiestaModificaSuoloService.ricercaMessaggiRichiestaModificaSuolo(idRichiesta,
				Optional.ofNullable(paginazione).filter(p -> p.getNumeroElementiPagina() != null).orElse(new Paginazione(10, 0)),
				Optional.ofNullable(ordinamento).filter(o -> o.getProprieta() != null).orElse(new Ordinamento("id", Ordine.DESC)));
	}

	@PostMapping("/{idRichiesta}/messaggi")
	@ApiOperation("Inserisce i messaggi di una richiesta modifica suolo")
	@PreAuthorize("@abilitazioniComponent.checkEditMessaggiRichiestaModificaSuolo(#idRichiesta)")
	public ResponseEntity<?> insertMessaggiRichiesta(@PathVariable @ApiParam(value = "Identificativo della RichiestaModificaSuolo", required = true) Long idRichiesta,
			@RequestBody List<MessaggioRichiestaDto> listMessaggioRichiestaDto) {
		String username = utenteComponent.username();

		richiestaModificaSuoloService.insertMessaggiRichiesta(idRichiesta, listMessaggioRichiestaDto, username);

		return new ResponseEntity(HttpStatus.CREATED);
	}

	@GetMapping("/{idRichiesta}/documenti")
	@ApiOperation("Ricerca dei documenti allegati ad una richiesta modifica suolo")
	@PreAuthorize("@abilitazioniComponent.checkAccessResourceRichiestaModificaSuolo(#idRichiesta)")
	public RisultatiPaginati<DocumentazioneRichiestaDto> ricercaDocumentiRichiestaModificaSuolo(@PathVariable("idRichiesta") Long idRichiesta, Paginazione paginazione) {
		return richiestaModificaSuoloService.ricercaDocumentiRichiestaModificaSuolo(idRichiesta, paginazione, new Ordinamento("id", Ordine.DESC));
	}

	@GetMapping("/{idRichiesta}/documenti/{idDocumento}")
	@ApiOperation("Download del documento allegato ad una richiesta modifica suolo")
	@PreAuthorize("@abilitazioniComponent.checkAccessResourceRichiestaModificaSuolo(#idRichiesta)")
	public ResponseEntity<byte[]> downloadDocumentiRichiestaModificaSuolo(@PathVariable("idRichiesta") Long idRichiesta, @PathVariable("idDocumento") Long idDocumento) {

		HttpHeaders responseHeaders = new HttpHeaders();
		DocumentazioneRichiestaDto documento = richiestaModificaSuoloService.ricercaDocumentoRichiestaModificaSuolo(idRichiesta, idDocumento);

		responseHeaders.set("Content-Disposition", "attachment; filename=".concat(documento.getNomeFile()));
		responseHeaders.set("Access-Control-Expose-Headers", "Content-Disposition");
		return new ResponseEntity<>(documento.getDocContent(), responseHeaders, HttpStatus.OK);
	}

	@PostMapping("/{idRichiesta}/documenti")
	@ApiOperation("Inserisce i documenti di una richiesta modifica suolo")
	@PreAuthorize("@abilitazioniComponent.checkEditDocumentiRichiestaModificaSuolo(#idRichiesta) && @abilitazioniComponent.extensionControlDocument(#file)")
	public ResponseEntity<?> uploadDocumentiRichiestaModificaSuolo(@PathVariable("idRichiesta") Long idRichiesta, @RequestParam("descrizione") Optional<String> descrizione,
			@RequestParam("profiloUtente") String profiloUtente, @RequestParam("dimensione") Long dimensione, @RequestParam("file") MultipartFile file) {
		DocumentazioneRichiestaDto documentazioneRichiestaDto = new DocumentazioneRichiestaDto();
		documentazioneRichiestaDto.setDescrizione(descrizione.orElse(null));
		documentazioneRichiestaDto.setDimensione(dimensione);
		documentazioneRichiestaDto.setProfiloUtente(ProfiloUtente.valueOf(profiloUtente.toUpperCase()));

		richiestaModificaSuoloService.uploadDocumentiRichiestaModificaSuolo(idRichiesta, documentazioneRichiestaDto, file);

		return new ResponseEntity(HttpStatus.CREATED);
	}

	@DeleteMapping("/{idRichiesta}/documenti/{idDocumento}")
	@ApiOperation("Cancellazione del documento")
	@PreAuthorize("@abilitazioniComponent.checkEditDocumentiRichiestaModificaSuolo(#idRichiesta)")
	public ResponseEntity<?> deleteDocumentoRichiestaModificaSuolo(@PathVariable("idRichiesta") Long idRichiesta, @PathVariable("idDocumento") Long idDocumento) {
		richiestaModificaSuoloService.cancellaDocumentoRichiestaModificaSuolo(idRichiesta, idDocumento);
		return new ResponseEntity("Ok", HttpStatus.OK);
	}

	@GetMapping("/{idRichiesta}/dichiarati")
	@ApiOperation("Ricerca suolo dichiarato con tutte le informazioni")
	@PreAuthorize("@abilitazioniComponent.checkAccessResourceRichiestaModificaSuolo(#idRichiesta)")
	public RisultatiPaginati<SuoloDichiaratoLavorazioneDto> ricercaSuoloDichiarato(@PathVariable("idRichiesta") Long idRichiesta, Paginazione paginazione, Ordinamento ordinamento) {
		SuoloDichiaratoLavorazioneFilter filter = new SuoloDichiaratoLavorazioneFilter();
		filter.setIdRichiesta(idRichiesta);

		return lavorazioneSuoloService.ricercaSuoloDichiarato(filter, paginazione,
				Optional.ofNullable(ordinamento).filter(o -> o.getProprieta() != null).orElse(new Ordinamento("id", Ordine.ASC)));
	}

	@PutMapping("/{idRichiesta}/aggiornaDichiarati")
	@ApiOperation("Aggiorna solo poligoni a cui non sono associate Lavorazioni")
	@PreAuthorize("@abilitazioniComponent.checkEditDichiaratiRichiestaModificaSuolo(#idRichiesta)")
	public ResponseEntity<?> updateDichiarati(@PathVariable("idRichiesta") Long idRichiesta,
											  @RequestBody List<SuoloDichiaratoLavorazioneDto> suoloDichiaratoLavorazioneDtoList) {
		richiestaModificaSuoloService.updateDichiarati(idRichiesta, suoloDichiaratoLavorazioneDtoList);
		return new ResponseEntity(HttpStatus.OK);
	}
}
