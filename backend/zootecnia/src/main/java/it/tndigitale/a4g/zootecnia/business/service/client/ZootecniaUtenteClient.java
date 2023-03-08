package it.tndigitale.a4g.zootecnia.business.service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.utente.client.api.UtenteControllerApi;
import it.tndigitale.a4g.utente.client.model.RappresentaIlModelloPerRappresentareUnUtenteDelSistema;

@Component
public class ZootecniaUtenteClient extends AbstractClient {
	@Value("${it.tndigit.security.utente.url}")
    private String urlUtente;
	
	// Methods from Controllers
	public RappresentaIlModelloPerRappresentareUnUtenteDelSistema getUtenteConnesso() {
		return this.getUtenteControllerApi().caricaMieiDatiUsingGET();
	}
	
	// Get Controller
	private UtenteControllerApi getUtenteControllerApi() {
        return restClientProxy(UtenteControllerApi.class, urlUtente);
    }
}
