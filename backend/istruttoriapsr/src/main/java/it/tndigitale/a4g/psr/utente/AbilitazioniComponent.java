package it.tndigitale.a4g.psr.utente;

import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.framework.security.service.UtenteClient;
import it.tndigitale.a4g.psr.business.persistence.repository.DomandePsrDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Classe che si occupa di gestire i controlli autorizzativi ai servizi.
 * In una prima fase un livello 3 programmatico (controllo ruolo). E' stato fatto
 * cosi in ottica di estendere i controlli sui dati anche
 * 
 * @author it417
 *
 */
@Component("abilitazioniComponent")
public class AbilitazioniComponent {
	
	private static Logger log = LoggerFactory.getLogger(AbilitazioniComponent.class);
	
	@Autowired
	private UtenteComponent utenteComponent;
	@Autowired
	private UtenteClient abilitazioniUtente;
    @Autowired
    private DomandePsrDao domandePsrDao;
	
	public boolean checkLetturaDomandaPSR(String cuaa) throws Exception {
        log.info("user with CUAA: " + cuaa + " is requesting to read domanda PSR");
        boolean canView = utenteComponent.haRuolo(Ruoli.VISUALIZZA_DOMANDA_PSR);
		if (!canView) {
			List<String> cuaaAbilitati = abilitazioniUtente.getAziendeUtente();
			canView=cuaaAbilitati.contains(cuaa);
		}
		return canView;
	}

    public boolean checkLetturaDomandaPSRByIdDomanda(Integer idDomanda) throws Exception {
        log.info("user with a domanda with ID: " + idDomanda + " is requesting to read domanda PSR");
        Optional<String> cuaa = domandePsrDao.getCuaaByIdDomanda(idDomanda);
        return cuaa.isPresent() && checkLetturaDomandaPSR(cuaa.get());
    }
}
