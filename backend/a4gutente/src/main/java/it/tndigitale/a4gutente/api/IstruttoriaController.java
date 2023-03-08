package it.tndigitale.a4gutente.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4gutente.dto.Istruttoria;
import it.tndigitale.a4gutente.dto.IstruttoriaSenzaDomanda;
import it.tndigitale.a4gutente.dto.StoricoIstruttorie;
import it.tndigitale.a4gutente.service.IstruttoriaService;

@RestController
@RequestMapping(ApiUrls.ISTRUTTORIA)
@Api("Rappresenta la lista di azione che si possono fare sull'istruttoria delle domande di richieste di accesso utente")
public class IstruttoriaController {

    @Autowired
    private IstruttoriaService istruttoriaService;

    public IstruttoriaController setComponents(IstruttoriaService istruttoriaService) {
        this.istruttoriaService = istruttoriaService;
        return this;
    }

    @ApiOperation("Restituisce l'istruttoria relativa la domanda con identificativo specificato")
    @GetMapping("/domanda/{idDomanda}")
    @PreAuthorize("@abilitazioni.checkVisualizzaIstruttoriaDomanda()")
    public Istruttoria findByIdDomanda(@ApiParam(value = "Identificativo della domanda", required = true)
                                       @PathVariable(value = "idDomanda") Long idDomanda) throws Exception {
        return istruttoriaService.findByIdDomanda(idDomanda);
    }

    @ApiOperation("Crea una nuova istruttoria relativa una domanda")
    @PostMapping("/domanda")
    @PreAuthorize("@abilitazioni.checkEditaIstruttoriaDomanda()")
    public Long crea(@RequestBody Istruttoria istruttoria) {
        return istruttoriaService.crea(istruttoria);
    }

    @ApiOperation("Modifica una esistente istruttoria relativa a una domanda")
    @PutMapping("/domanda")
    @PreAuthorize("@abilitazioni.checkEditaIstruttoriaDomanda()")
    public Long aggiorna(@RequestBody Istruttoria istruttoria) {
        return istruttoriaService.aggiorna(istruttoria);
    }

    @ApiOperation("Crea una nuova istruttoria senza domanda per una richiesta di modifica " +
                  "abilit azioni utente (profili/sedi)")
    @PostMapping("/utente/{idUtente}")
    @PreAuthorize("@abilitazioni.checkCreaUtente()")
    public Long creaSenzaDomanda(@ApiParam(value="Id dell'utente", required = true)
                                 @PathVariable(value = "idUtente") Long idUtente,
                                 @RequestBody IstruttoriaSenzaDomanda istruttoriaSenzaDomanda) {
        return istruttoriaService.creaSenzaDomanda(istruttoriaSenzaDomanda.setIdUtente(idUtente));
    }
    
	@GetMapping("/utente/{idUtente}/storico")
	@ApiOperation("Visualizza storico utente")
	@PreAuthorize("@abilitazioni.checkVisualizzaDomande()")
	public StoricoIstruttorie storico(@ApiParam(value="Identificativo dell'utente", required = true)
						              @PathVariable Long idUtente) {
		return istruttoriaService.listaStorico(idUtente);
	}

}
