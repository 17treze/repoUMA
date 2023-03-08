package it.tndigitale.a4g.srt.utente;

import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.framework.security.service.UtenteClient;
import it.tndigitale.a4g.srt.services.DomandeServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component("abilitazioniComponent")
public class AbilitazioniComponent {

	private static Logger log = LoggerFactory.getLogger(AbilitazioniComponent.class);

    private final UtenteComponent utenteComponent;

	private final UtenteClient abilitazioniUtente;

	private final DomandeServiceImpl domandeService;

    public AbilitazioniComponent(UtenteComponent utenteComponent, UtenteClient abilitazioniUtente, DomandeServiceImpl domandeService) {
        this.utenteComponent = utenteComponent;
        this.abilitazioniUtente = abilitazioniUtente;
        this.domandeService = domandeService;
    }

    public boolean checkLetturaDomandaPSR(String cuaa) throws Exception {
        log.info("user with CUAA: {} is requesting to read domanda PSR Strutturale", cuaa);
        boolean canView = utenteComponent.haRuolo("a4g.ags.domanda.psr.visualizza");
		if (!canView) {
			List<String> cuaaAbilitati = abilitazioniUtente.getAziendeUtente();
			canView=cuaaAbilitati.contains(cuaa);
            log.info("user with CUAA: {} requested to read domanda PSR Strutturale with its user limitations, the result was: {}", cuaa, canView);
		}
		return canView;
	}

    public boolean checkLetturaDomandaByIdProgetto(Integer idProgetto) throws Exception {
        log.info("user with with IdProgetto: {} is requesting to read domanda PSR Strutturale", idProgetto);
        Optional<String> cuaa = Optional.ofNullable(domandeService.getCuaaByIdProgetto(idProgetto));
        return cuaa.isPresent() && checkLetturaDomandaPSR(cuaa.get());
    }
}
