package it.tndigitale.a4g.psr.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.psr.business.service.DomandaPsrService;
import it.tndigitale.a4g.psr.dto.DettaglioPagamentoPsr;
import it.tndigitale.a4g.psr.dto.DomandaPsr;
import it.tndigitale.a4g.psr.dto.ImpegnoRichiestoPSRSuperficie;
import it.tndigitale.a4g.psr.dto.ImpegnoZooPascoloPsr;
import it.tndigitale.a4g.psr.dto.ImportiDomandaPsr;
import it.tndigitale.a4g.psr.dto.StatoDomandaPsr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(ApiUrls.DOMANDE)
@Api(value = "Servizio per il recupero delle informazioni relative le domande PSR")
public class DomandePsrController {

    @Autowired
	private DomandaPsrService service;

    @ApiOperation("Lista delle domande in base al CUAA")
    @GetMapping("/{cuaa}/consultazione")
    @PreAuthorize("@abilitazioniComponent.checkLetturaDomandaPSR(#cuaa)")
    public List<DomandaPsr> getCarica(@ApiParam(value = "CUAA dell'azienda", required = true)
                                      @PathVariable(value="cuaa")
                                              String cuaa) {
        return service.consultazioneDomandePsr(cuaa);
    }

    @ApiOperation("Lista degli impegni richiesti per PSR Superficie")
    @GetMapping("/{idDomanda}/impegni-richiesti")
    @PreAuthorize("@abilitazioniComponent.checkLetturaDomandaPSRByIdDomanda(#idDomanda)")
    public List<ImpegnoRichiestoPSRSuperficie> getImpegniRichiesti(@PathVariable(value="idDomanda", required=true)
                                                            Integer idDomanda) {
        return service.getImpegniRichiestiPSRSuperficie(idDomanda);
    }

    @ApiOperation("Domanda Psr Superficie in base all'id ")
    @GetMapping("/{idDomanda}/psr-superficie")
    @PreAuthorize("@abilitazioniComponent.checkLetturaDomandaPSRByIdDomanda(#idDomanda)")
    public DomandaPsr getDomandaPsrSuperficieById(@ApiParam(value = "Id Domanda Psr Superficie", required = true)
                                                  @PathVariable(value="idDomanda")
                                                          Integer idDomanda) {
        return service.getDomandaPsrSuperficieById(idDomanda);
    }

    @ApiOperation("Impegni zoo pascoli in base all'id ")
    @GetMapping("/{idDomanda}/zoo-pascoli")
    @PreAuthorize("@abilitazioniComponent.checkLetturaDomandaPSRByIdDomanda(#idDomanda)")
    public List<ImpegnoZooPascoloPsr> getImpegniZooPascoliByIdDomanda(@ApiParam(value = "Id Domanda Psr Superficie", required = true)
                                                  @PathVariable(value="idDomanda")
                                                          Integer idDomanda) {
        return service.getImpegniZooPascoliByIdDomanda(idDomanda);
    }

    @ApiOperation("Lista degli stati che ha avuto la domanda")
    @GetMapping("/{idDomanda}/stato-domanda")
    @PreAuthorize("@abilitazioniComponent.checkLetturaDomandaPSRByIdDomanda(#idDomanda)")
    public List<StatoDomandaPsr> getStatoDomandaPsr(@PathVariable(value="idDomanda") Integer idDomanda) {
        return service.getStatiOperazioneByIdDomanda(idDomanda);
    }

    @ApiOperation("Impoprti totali della domanda psr")
    @GetMapping("/{cuaa}/importi-domanda/{anno}")
    @PreAuthorize("@abilitazioniComponent.checkLetturaDomandaPSR(#cuaa)")
    public ImportiDomandaPsr getImportiDomandaPsr(@PathVariable(value="cuaa") String cuaa, @PathVariable(value="anno") Integer anno) {
        return service.getImportiByIdDomandaAndAnno(cuaa, anno);
    }


    @ApiOperation("Dettaglio pagamento psr")
    @GetMapping("/{idDomanda}/dettaglio-pagamento/{anno}/operazione/{codiceOperazione}/tipo-pagamento/{tipoPagamento}")
    @PreAuthorize("@abilitazioniComponent.checkLetturaDomandaPSRByIdDomanda(#idDomanda)")
    public ResponseEntity<DettaglioPagamentoPsr> checkLetturaDomandaPSRByIdDomanda(@PathVariable(value = "idDomanda") String idDomanda, @PathVariable(value = "anno") Integer anno, @PathVariable(value = "tipoPagamento") String tipoPagamento,@PathVariable(value = "codiceOperazione") String codiceOperazione) {
        try {
           return ResponseEntity.ok(service.getDettaglioPagamento(idDomanda, anno, codiceOperazione, tipoPagamento));
        } catch (NoSuchElementException e) {
           return ResponseEntity.noContent().build();
        }
    }

    @ApiOperation("Importo calcolato operazione")
    @GetMapping("/{idDomanda}/importo-calcolato/cod-misura/{codMisura}/tipo-pagamento/{tipoPagamento}")
    @PreAuthorize("@abilitazioniComponent.checkLetturaDomandaPSRByIdDomanda(#idDomanda)")
    public BigDecimal getImportoCalcolato(@PathVariable(value = "idDomanda") Integer idDomanda,
                                          @PathVariable(value = "codMisura") String codMisura,
                                          @PathVariable(value = "tipoPagamento") String tipoPagamento) {
        return service.getImportoCalcolato(idDomanda, codMisura, tipoPagamento);
    }

    @ApiOperation("Totale calcolato operazioni")
    @GetMapping("/{idDomanda}/totale-importo-calcolato")
    @PreAuthorize("@abilitazioniComponent.checkLetturaDomandaPSRByIdDomanda(#idDomanda)")
    public BigDecimal getImportoCalcolato(@PathVariable(value = "idDomanda") Integer idDomanda) {
        return service.getTotaleImportoCalcolato(idDomanda);
    }


}
