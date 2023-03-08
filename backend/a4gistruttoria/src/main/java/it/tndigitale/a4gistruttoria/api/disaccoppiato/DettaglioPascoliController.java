package it.tndigitale.a4gistruttoria.api.disaccoppiato;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.proxy.bdn.client.api.BdnSyncControllerApi;
import it.tndigitale.a4g.proxy.bdn.client.model.StatoSincronizzazioneDO;
import it.tndigitale.a4gistruttoria.api.ApiUrls;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DettaglioPascoli;
import it.tndigitale.a4gistruttoria.service.DettaglioPascoliService;
import it.tndigitale.a4gistruttoria.util.ConsumeExternalRestApi;

@RestController
@RequestMapping(ApiUrls.DETTAGLIO_PASCOLI_V1)
public class DettaglioPascoliController {

    @Autowired
    private DettaglioPascoliService dettaglioPascoliService;
    
    @Autowired 
    private ConsumeExternalRestApi clientExternalApi;

    @GetMapping("/istruttoria/{idIstruttoria}/conesitomantenimento")
    @ApiOperation("Servizio per il recupero delle informazioni di dettaglio delle superfici a pascolo con gli esiti mantenimento")
    @PreAuthorize("@abilitazioniPACComponent.checkVisualizzaIstruttoria()")
    public List<DettaglioPascoli> getInfoDettaglioPascoliWithEsitoMantenimento(@PathVariable("idIstruttoria") Long idIstruttoria) throws Exception {
        return dettaglioPascoliService.getInfoDettaglioPascoliWithEsitoMantenimento(idIstruttoria);
    }

    @GetMapping("/istruttoria/{idIstruttoria}/condatiistruttoria")
    @ApiOperation("Servizio per il recupero delle informazioni di dettaglio delle superfici a pascolo con i dati di istruttoria")
    @PreAuthorize("@abilitazioniPACComponent.checkVisualizzaIstruttoria()")
    public List<DettaglioPascoli> getInfoDettaglioPascoliWithDatiIstruttoria(@PathVariable("idIstruttoria") Long idIstruttoria) throws Exception {
        return dettaglioPascoliService.getInfoDettaglioPascoliWithDatiIstruttoria(idIstruttoria);
    }

    @PutMapping("/{annoCampagna}/{cuaa}/sincronizzaDatiPascoloBDN")
    @ApiOperation("Sincronizza i dati relativi ai pascoli della BDN")
    @PreAuthorize("@abilitazioniPACComponent.checkEditaIstruttoria()")
    public Boolean sincronizzaDatiPascoloBDN(@ApiParam("Anno Campagna") @PathVariable Integer annoCampagna, 
                                            @ApiParam("Cuaa dell'azienda") @PathVariable String cuaa)   {
       StatoSincronizzazioneDO	statoSincronizzazione = new StatoSincronizzazioneDO();
       statoSincronizzazione.setAnnoCampagna(annoCampagna);
       statoSincronizzazione.setCuaa(cuaa);
       return clientExternalApi.restClientBDN(BdnSyncControllerApi.class).aggiornaDatiBdnPerCuaaUsingPUT(statoSincronizzazione);
    }
}
