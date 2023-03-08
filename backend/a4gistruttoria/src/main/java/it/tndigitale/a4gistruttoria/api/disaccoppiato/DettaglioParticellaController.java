package it.tndigitale.a4gistruttoria.api.disaccoppiato;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4gistruttoria.api.ApiUrls;
import it.tndigitale.a4gistruttoria.dto.CsvFile;
import it.tndigitale.a4gistruttoria.dto.DettaglioParticellaDto;
import it.tndigitale.a4gistruttoria.dto.Pagina;
import it.tndigitale.a4gistruttoria.dto.filter.DettaglioParticellaRequest;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DettaglioSuperficieCalcoloDto;
import it.tndigitale.a4gistruttoria.service.DettaglioParticellaService;

@RestController
@RequestMapping(ApiUrls.DETTAGLIO_PARTICELLA_V1)
public class DettaglioParticellaController {

    private DettaglioParticellaService dettaglioParticellaService;

    @Autowired
    public DettaglioParticellaController setComponents(DettaglioParticellaService dettaglioParticellaService) {
        this.dettaglioParticellaService = dettaglioParticellaService;
        return this;
    }

    @GetMapping("/istruttoria/{idIstruttoria}")
    @ApiOperation("Servizio per la restituzione di una pagina di dettaglio particelle per istruttoria")
    public Pagina<DettaglioParticellaDto> getDettaglioParticella(@ApiParam("Identificativo dell'istruttoria")
                                                              @PathVariable Long idIstruttoria,
                                                              @ApiParam(value = "Se true, allora prendo i record tali che il campo pascolo presente in particella sia not null")
                                                              @RequestParam(value = "pascolo", required = false) Boolean pascolo,
                                                              Paginazione paginazione,
                                                              Ordinamento ordinamento) throws Exception {
        DettaglioParticellaRequest dettaglioParticellaRequest =
                new DettaglioParticellaRequest()
                        .setIdIstruttoria(idIstruttoria)
                        .setPascolo(pascolo)
                        .setPaginazione(paginazione)
                        .setOrdinamento(ordinamento);
        return dettaglioParticellaService.getDettaglioParticellaPaginabile(dettaglioParticellaRequest);
    }

    @GetMapping("/domanda/{idDomanda}/particella/{idParticella}/codice-coltura/{codiceColtura}")
    @ApiOperation("Restituisce dettaglio su richieste disaccoppiato superficie data una domanda e particella")
    public List<DettaglioSuperficieCalcoloDto> getDettaglioSuperficiePerCalcolo(
    		@ApiParam("Identificativo della domanda") @PathVariable Long idDomanda,
    		@ApiParam("Identificativo della particella") @PathVariable Long idParticella,
    		@ApiParam("Codice coltura") @PathVariable  String codiceColtura) throws IOException {
    	List<DettaglioSuperficieCalcoloDto> dettaglioSuperficiPerCalcoloList = dettaglioParticellaService.getDettaglioSuperficiePerCalcolo(
    			idDomanda, idParticella, codiceColtura);
    	return dettaglioSuperficiPerCalcoloList;
    }
    
    @GetMapping("/istruttoria/{idIstruttoria}/csv-eleggibilita")
    @ApiOperation("Servizio per generare il CSV di dettaglio particelle per istruttoria")
    public ResponseEntity<Resource> getCsvDettaglioParticellaEleggibilità(
    		@ApiParam("Identificativo dell'istruttoria") @PathVariable
    		Long idIstruttoria) throws Exception {
        CsvFile csvFile = dettaglioParticellaService.getDettaglioParticellaCsvEleggibilita(idIstruttoria);
        ByteArrayResource byteArrayResource = new ByteArrayResource(csvFile.getCsvByteArray());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + csvFile.getCsvFileName())
                .body(byteArrayResource);
    }
    
    @GetMapping("/istruttoria/{idIstruttoria}/csv-greening")
    @ApiOperation("Servizio per generare il CSV di dettaglio particelle per istruttoria")
    public ResponseEntity<Resource> getCsvDettaglioParticellaGreening(
    		@ApiParam("Identificativo dell'istruttoria") @PathVariable
    		Long idIstruttoria) throws Exception {
        CsvFile csvFile = dettaglioParticellaService.getDettaglioParticellaCsvGreening(idIstruttoria);
        ByteArrayResource byteArrayResource = new ByteArrayResource(csvFile.getCsvByteArray());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + csvFile.getCsvFileName())
                .body(byteArrayResource);
    }

    @GetMapping("/istruttoria/{idIstruttoria}/csv-mantenimento")
    @ApiOperation("Servizio per generare il CSV di dettaglio particelle per istruttoria")
    public ResponseEntity<Resource> getCsvDettaglioParticellaMantenimento(
    		@ApiParam("Identificativo dell'istruttoria") @PathVariable
    		Long idIstruttoria) throws Exception {
        CsvFile csvFile = dettaglioParticellaService.getDettaglioParticellaCsvMantenimento(idIstruttoria);
        ByteArrayResource byteArrayResource = new ByteArrayResource(csvFile.getCsvByteArray());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + csvFile.getCsvFileName())
                .body(byteArrayResource);
    }
}
