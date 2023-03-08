package it.tndigitale.a4g.richiestamodificasuolo.api;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.ProfiloUtente;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.layer.LayerService;
import it.tndigitale.a4g.richiestamodificasuolo.dto.layer.LayerSwitcherDto;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(ApiUrls.LAYER)
@Api(value = "Controller per il recupero delle informazioni dei layer")
public class LayerController {

    @Autowired
    private LayerService layerService;

    @GetMapping
    @ApiOperation("Recupero dei layer associati al profilo_utente")
    @PreAuthorize("@abilitazioniComponent.checkProfiloUtenteConUtente(#profiloUtente)")
    public List<LayerSwitcherDto> getLayerProfiloUtente(@RequestParam(name = "profilo_utente") ProfiloUtente profiloUtente) {
        return layerService.getLayerProfiloUtente(profiloUtente);
    }
    
    @GetMapping("/abilitati")
    @ApiOperation("Restituisce la mappa layers filtri a cui l'utente è abilitato")
    @PreAuthorize("permitAll()")
    public Map<String, String> getLayerFilterMapUtente(@RequestParam(name = "codiceFiscale", required = false) String codiceFiscale, @RequestParam(name = "upn", required = false) String upn) {
        String utente = StringUtils.firstNonBlank(codiceFiscale, upn);
        
        return layerService
            .getLayersUtente(utente)
            .stream()
            .collect(HashMap::new, (map, value) -> map.put(value, null), HashMap::putAll);
    }
}