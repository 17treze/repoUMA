package it.tndigitale.a4g.soc.api;

import it.tndigitale.a4g.soc.business.dto.ImportoLiquidatoFilter;
import it.tndigitale.a4g.soc.business.dto.ImportoLiquidato;

import java.util.List;

import it.tndigitale.a4g.soc.utente.AbilitazioniComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.tndigitale.a4g.soc.business.service.LiquidazioneService;

@RestController
@RequestMapping(ApiUrls.LIQUIDAZIONE_V1)
@Api("Gestione e recupero informazioni dal SOC")
public class LiquidazioneController {

    @Autowired
	private LiquidazioneService liquidazioneService;

    public LiquidazioneController setLiquidazioneService(LiquidazioneService liquidazioneService) {
        this.liquidazioneService = liquidazioneService;
        return this;
    }

    @GetMapping()
    @ApiOperation("Lista degli importi liquidati e degli eventuali debiti detratti dall'importo")
    @PreAuthorize("@abilitazioniComponent.checkLetturaDomandaPSRByImportoLiquidatoFilter(#importoLiquidatoFilter)")
    public List<ImportoLiquidato> getImportiLiquidazione(final ImportoLiquidatoFilter importoLiquidatoFilter) {
    	return liquidazioneService.caricaImportoLiquidazione(importoLiquidatoFilter);
    }

}
