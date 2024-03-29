package it.tndigitale.a4gistruttoria.api.configurazioneistruttoria;

import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4gistruttoria.api.ApiUrls;
import it.tndigitale.a4gistruttoria.dto.DatiConfigurazioneAccoppiati;
import it.tndigitale.a4gistruttoria.service.businesslogic.configurazioneistruttoria.ConfigurazioneIstruttoriaService;

@RestController
@RequestMapping(ApiUrls.ISTRUTTORIE_DU_CONF_V1+"/acz")
public class ConfigurazioneIstruttoriaZootecniaController {
	@Autowired
	private  ConfigurazioneIstruttoriaService configurazioneIstruttoriaService;
	
    @GetMapping("/{annoCampagna}")
    @ApiOperation("Recupera i dati della configurazione istruttoria Accoppiato zootecnia")
    @PreAuthorize("@abilitazioniPACComponent.checkVisualizzaIstruttoria()")
    public DatiConfigurazioneAccoppiati getConfIstruttoriaAcz(@ApiParam("Anno Campagna")  @PathVariable Integer annoCampagna)   {
       return configurazioneIstruttoriaService.getImportiUnitari(annoCampagna, Sostegno.ZOOTECNIA);
    }
    
    @PostMapping("/{annoCampagna}")
    @ApiOperation("Salvataggio dei dati della configurazione istruttoria Accoppiato zootecnia")
    @PreAuthorize("@abilitazioniPACComponent.checkEditaIstruttoria()")
    public DatiConfigurazioneAccoppiati saveConfIstruttoriaAcz(@PathVariable Integer annoCampagna, @RequestBody DatiConfigurazioneAccoppiati datiConfigurazioneAccoppiati)   {
    	return configurazioneIstruttoriaService.saveOrUpdateConfImportiUnitari(annoCampagna, datiConfigurazioneAccoppiati);
    } 
}
