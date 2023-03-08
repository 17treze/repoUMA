package it.tndigitale.a4g.fascicolo.anagrafica.business.service.client;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.utente.client.api.PersonaControllerApi;
import it.tndigitale.a4g.utente.client.api.UtenteControllerApi;
import it.tndigitale.a4g.utente.client.model.Persona;
import it.tndigitale.a4g.utente.client.model.RappresentaIlModelloPerRappresentareUnUtenteDelSistema;

@Component
public class AnagraficaUtenteClient extends AbstractClient {
	private static final Logger logger = LoggerFactory.getLogger(AnagraficaUtenteClient.class);

    @Value("${it.tndigit.security.utente.url.anagrafica.utente.client}")
    private String urlUtente;

    public List<Persona> ricercaPerCodiceFiscale(String codiceFiscale) {
        return this.getPersonaControllerApi().ricercaUsingGET(codiceFiscale);
    }
    
    public RappresentaIlModelloPerRappresentareUnUtenteDelSistema getUtenteConnesso() {
    	try {
    		return this.getUtenteControllerApi().caricaMieiDatiUsingGET();
    	} catch (Exception e) {
    		logger.error(e.getMessage());
    		return null;
    	}
    }
    
    public List<String> getEntiUtenteConnesso() {
    	return this.getUtenteControllerApi().getEntiUsingGET();
    }

    // PersonaController
    private PersonaControllerApi getPersonaControllerApi() {
        return restClientProxy(PersonaControllerApi.class, urlUtente);
    }

    private UtenteControllerApi getUtenteControllerApi() {
        return restClientProxy(UtenteControllerApi.class, urlUtente);
    }
}
