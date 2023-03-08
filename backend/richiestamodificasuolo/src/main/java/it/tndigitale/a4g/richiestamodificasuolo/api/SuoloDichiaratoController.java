package it.tndigitale.a4g.richiestamodificasuolo.api;

import java.util.List;
import java.util.Optional;

import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.richiestamodificasuolo.dto.richiestaModificaSuolo.MessaggioRichiestaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento.Ordine;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.EsitoLavorazioneDichiarato;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.ProfiloUtente;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.LavorazioneSuoloService;
import it.tndigitale.a4g.richiestamodificasuolo.dto.filter.SuoloDichiaratoLavorazioneFilter;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.SuoloDichiaratoLavorazioneDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.richiestaModificaSuolo.DocumentazioneRichiestaDto;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(ApiUrls.SUOLO_DICHIARATO)
@Api(value = "Controller per il recupero delle informazioni sui suoli dichiarati")
public class SuoloDichiaratoController {

    @Autowired
    private LavorazioneSuoloService lavorazioneSuoloService;

    @Autowired
    private UtenteComponent utenteComponent;

    @GetMapping
    @ApiOperation("Ricerca suolo dichiarato con tutte le informazioni")
    @PreAuthorize("@abilitazioniComponent.searchSuoloDichiarato(#filter)")
    public RisultatiPaginati<SuoloDichiaratoLavorazioneDto> ricercaSuoloDichiarato(SuoloDichiaratoLavorazioneFilter filter, Paginazione paginazione, Ordinamento ordinamento) {

        return lavorazioneSuoloService.ricercaSuoloDichiarato(filter, paginazione,
                Optional.ofNullable(ordinamento).filter(o -> o.getProprieta() != null).orElse(new Ordinamento("idRichiesta", Ordine.DESC)));
    }

    @PutMapping("/{idSuoloDichiarato}/associaLavorazione")
    @ApiOperation("Aggiunge il suolo alla lavorazione")
    @PreAuthorize("@abilitazioniComponent.checkEditSuoloDichiarato(#idSuoloDichiarato)")
    public ResponseEntity<?> updateSuoloDichiarato(@PathVariable @ApiParam(value = "Identificativo del suolo dichiarato", required = true) Long idSuoloDichiarato,
            @RequestBody SuoloDichiaratoLavorazioneDto suoloDichiaratoLavorazioneDto) {
        suoloDichiaratoLavorazioneDto.setId(idSuoloDichiarato);
        lavorazioneSuoloService.updateSuoloDichiarato(suoloDichiaratoLavorazioneDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/{idSuoloDichiarato}/rimuoviLavorazione")
    @ApiOperation("Rimuove il suolo dichiarato dalla lavorazione")
    @PreAuthorize("@abilitazioniComponent.checkEditSuoloDichiarato(#idSuoloDichiarato)")
    public ResponseEntity<?> removeSuoloDichiaratoDaLavorazione(@PathVariable @ApiParam(value = "Identificativo del suolo dichiarato", required = true) Long idSuoloDichiarato,
            @RequestBody SuoloDichiaratoLavorazioneDto suoloDichiaratoLavorazioneDto) {
        lavorazioneSuoloService.removeSuoloDichiaratoDaLavorazione(suoloDichiaratoLavorazioneDto.getIdLavorazione(), idSuoloDichiarato);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/{idSuoloDichiarato}/setEsitoDichiarato")
    @ApiOperation("Aggiorna l'esito della lavorazione sul dichiarato")
    @PreAuthorize("@abilitazioniComponent.checkEditSuoloDichiarato(#idSuoloDichiarato)")
    public ResponseEntity<?> setEsitoDichiarato(@PathVariable @ApiParam(value = "Identificativo del  poligono dichiarato", required = true) Long idSuoloDichiarato,
            @RequestParam("esito") EsitoLavorazioneDichiarato esito) {

        lavorazioneSuoloService.setEsitoDichiarato(idSuoloDichiarato, esito);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{idDichiarato}/messaggiDichiarato")
    @ApiOperation("Ricerca paginata dei messaggi del dichiarato")
    @PreAuthorize("@abilitazioniComponent.checkReadMessaggiDichiarato()")
    public List<MessaggioRichiestaDto> ricercaMessaggiRichiestaModificaSuolo(@PathVariable @ApiParam(value = "Identificativo del dichiarato", required = true) Long idDichiarato) {
        return lavorazioneSuoloService.ricercaMessaggiDichiarato(idDichiarato);
    }

    @PostMapping("/{idDichiarato}/messaggiDichiarato")
    @ApiOperation("Inserisce i messaggi del dichiarato")
    @PreAuthorize("@abilitazioniComponent.checkEditMessaggiDichiarato()")
    public ResponseEntity<?> insertMessaggiRichiesta(@PathVariable @ApiParam(value = "Identificativo del dichiarato", required = true) Long idDichiarato,
                                                     @RequestBody List<MessaggioRichiestaDto> messaggiRichiestaDto) {
        lavorazioneSuoloService.insertMessaggiDichiarato(messaggiRichiestaDto, idDichiarato, utenteComponent.username());
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/{idDichiarato}/documentiDichiarato")
    @ApiOperation("Ricerca dei documenti allegati al dichiarato")
    @PreAuthorize("@abilitazioniComponent.checkReadDocumentiDichiarato()")
    public List<DocumentazioneRichiestaDto> ricercaDocumentiDichiarato(
        @PathVariable @ApiParam(value = "Identificativo del dichiarato", required = true) Long idDichiarato
    ) {
        return lavorazioneSuoloService.ricercaDocumentiDichiarato(idDichiarato);
    }
        
    @GetMapping("/{idDichiarato}/documentoDichiarato/{idDocumento}")
    @ApiOperation("Download del documento allegato al dichiarato")
    @PreAuthorize("@abilitazioniComponent.checkReadDocumentiDichiarato()")
    public ResponseEntity<byte[]> downloadDocumentoDichiarato(
        @PathVariable("idDichiarato") @ApiParam(value = "Identificativo del dichiarato", required = true) Long idDichiarato,
        @PathVariable("idDocumento") @ApiParam(value = "Identificativo del documento", required = true) Long idDocumento
    ) {
        HttpHeaders headers = new HttpHeaders();
        DocumentazioneRichiestaDto documento = lavorazioneSuoloService.ricercaDocumentoDichiarato(idDichiarato, idDocumento);

        headers.set("Content-Disposition", "attachment; filename=".concat(documento.getNomeFile()));
        headers.set("Access-Control-Expose-Headers", "Content-Disposition");

        return new ResponseEntity<>(documento.getDocContent(), headers, HttpStatus.OK);
    }

    @PostMapping("/{idDichiarato}/documentoDichiarato")
    @ApiOperation("Inserisce il documento al dichiarato")
    @PreAuthorize("@abilitazioniComponent.checkEditDocumentiDichiarato() && @abilitazioniComponent.extensionControlDocument(#file)")
    public ResponseEntity<?> uploadDocumentoDichiarato(
        @PathVariable("idDichiarato") @ApiParam(value = "Identificativo del dichiarato", required = true) Long idDichiarato,
        @RequestParam("descrizione") @ApiParam(value = "Descrizione del documento") Optional<String> descrizione,
        @RequestParam("profiloUtente") @ApiParam(value = "Profilo dell'utente", required = true) String profiloUtente,
        @RequestParam("dimensione") @ApiParam(value = "Dimensione del documento", required = true) Long dimensione,
        @RequestParam("file") @ApiParam(value = "Il file del documento", required = true) MultipartFile file
    ) {
        DocumentazioneRichiestaDto documento = new DocumentazioneRichiestaDto();
        
        documento.setDescrizione(descrizione.orElse(null));
        documento.setDimensione(dimensione);
        documento.setProfiloUtente(ProfiloUtente.valueOf(profiloUtente.toUpperCase()));

        lavorazioneSuoloService.uploadDocumentoDichiarato(idDichiarato, documento, file);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @DeleteMapping("/{idDichiarato}/documentoDichiarato/{idDocumento}")
    @ApiOperation("Cancellazione del documento")
    @PreAuthorize("@abilitazioniComponent.checkEditDocumentiDichiarato()")
    public ResponseEntity<?> deleteDocumentoDichiarato(
        @PathVariable("idDichiarato") @ApiParam(value = "Identificativo del dichiarato", required = true) Long idDichiarato,
        @PathVariable("idDocumento") @ApiParam(value = "Identificativo del documento", required = true) Long idDocumento
    ) {
        lavorazioneSuoloService.cancellaDocumentoDichiarato(idDichiarato, idDocumento);
        
        return new ResponseEntity(HttpStatus.OK);
    }
}
