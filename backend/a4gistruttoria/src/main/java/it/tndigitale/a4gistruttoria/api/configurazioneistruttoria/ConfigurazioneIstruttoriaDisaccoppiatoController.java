package it.tndigitale.a4gistruttoria.api.configurazioneistruttoria;

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
import it.tndigitale.a4gistruttoria.dto.configurazioneistruttoria.ConfIstruttoriaDisaccoppiatoDto;
import it.tndigitale.a4gistruttoria.service.businesslogic.configurazioneistruttoria.ConfigurazioneIstruttoriaService;

@RestController
@RequestMapping(ApiUrls.ISTRUTTORIE_DU_CONF_V1+"/disaccoppiato")
public class ConfigurazioneIstruttoriaDisaccoppiatoController {
	

	@Autowired
	private  ConfigurazioneIstruttoriaService configurazioneIstruttoriaService;
	
    @GetMapping("/{annoCampagna}")
    @ApiOperation("Recupera i dati della configurazione istruttoria Disaccoppiato")
    @PreAuthorize("@abilitazioniPACComponent.checkVisualizzaIstruttoria()")
    public ConfIstruttoriaDisaccoppiatoDto getConfIstruttoriaDisaccoppiato(@ApiParam("Anno Campagna")
                                                              @PathVariable Integer annoCampagna)   {
       return configurazioneIstruttoriaService.getConfIstruttoriaDisaccoppiato(annoCampagna);
    }
    
    @PostMapping("/{annoCampagna}")
    @ApiOperation("Salvataggio dei dati della configurazione istruttoria Disaccoppiato")
    @PreAuthorize("@abilitazioniPACComponent.checkEditaIstruttoria()")
    public ConfIstruttoriaDisaccoppiatoDto saveConfIstruttoriaDisaccoppiato(@PathVariable Integer annoCampagna, @RequestBody ConfIstruttoriaDisaccoppiatoDto confDto)   {
    	if (!annoCampagna.equals(confDto.getCampagna())) {
    		throw new IllegalArgumentException("Anno campagna non congruente");
    	}
    	return configurazioneIstruttoriaService.saveOrUpdateConfIstruttoriaDisaccoppiato(confDto);
    }    
}
