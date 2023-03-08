package it.tndigitale.a4gistruttoria.api.ricercadomande;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4gistruttoria.api.ApiUrls;
import it.tndigitale.a4gistruttoria.dto.DatiDomandaRicerca;
import it.tndigitale.a4gistruttoria.dto.IstruttoriaDomandaUnicaFilter;
import it.tndigitale.a4gistruttoria.repository.model.StatoDomanda;
import it.tndigitale.a4gistruttoria.service.RicercaDomandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiUrls.RICERCA_DOMANDE_V1)
public class RicercaDomandeController {

    @Autowired
    private RicercaDomandeService ricercaDomandeService;

    @ApiOperation("Ricerca domande")
    @GetMapping
    @PreAuthorize("@abilitazioniPACComponent.checkVisualizzaIstruttoria()")
    public RisultatiPaginati<DatiDomandaRicerca> ricercaDomande(IstruttoriaDomandaUnicaFilter filter, @ApiParam(value = "Parametri di paginazione", required = false) Paginazione pagination, @ApiParam(value = "Parametro di ordinamento", required = false) Ordinamento ordinamento) throws Exception {
        return ricercaDomandeService.ricercaDomandeUniche(filter, pagination, ordinamento);
    }

    @ApiOperation("Lista stati domande")
    @GetMapping("/listaStati")
    @PreAuthorize("@abilitazioniPACComponent.checkVisualizzaIstruttoria()")
    public List<StatoDomanda> getListaStatiDomande() {
        return ricercaDomandeService.getListaStatiDomande();
    }

    @ApiOperation("Lista anni domande")
    @GetMapping("/listaAnni")
    @PreAuthorize("@abilitazioniPACComponent.checkVisualizzaIstruttoria()")
    public List<Integer> getListaAnniDomande() {
        return ricercaDomandeService.getListaAnniCampagne();
        }
}
