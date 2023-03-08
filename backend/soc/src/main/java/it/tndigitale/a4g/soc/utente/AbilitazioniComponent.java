package it.tndigitale.a4g.soc.utente;

import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.framework.security.service.UtenteClient;
import it.tndigitale.a4g.soc.business.dto.ImportoLiquidatoFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("abilitazioniComponent")
public class AbilitazioniComponent {

    private static Logger log = LoggerFactory.getLogger(AbilitazioniComponent.class);

    private final UtenteComponent utenteComponent;

    private final UtenteClient abilitazioniUtente;

    public AbilitazioniComponent(UtenteComponent utenteComponent, UtenteClient abilitazioniUtente) {
        this.utenteComponent = utenteComponent;
        this.abilitazioniUtente = abilitazioniUtente;
    }

    public boolean checkLetturaDomandaPSRByImportoLiquidatoFilter(ImportoLiquidatoFilter importoLiquidatoFilter) throws Exception {
        String cuaa= importoLiquidatoFilter.getCuaa();
        log.info("user with CUAA: {} is requesting to read domanda PSR Strutturale", cuaa);
        boolean canView = utenteComponent.haRuolo("a4g.ags.domanda.psr.visualizza");
        if (!canView) {
            List<String> cuaaAbilitati = abilitazioniUtente.getAziendeUtente();
            canView=cuaaAbilitati.contains(cuaa);
            log.info("user with CUAA: {} requested to read domanda PSR Strutturale with its user limitations, the result was: {}", cuaa, canView);
        }
        return canView;
    }
}