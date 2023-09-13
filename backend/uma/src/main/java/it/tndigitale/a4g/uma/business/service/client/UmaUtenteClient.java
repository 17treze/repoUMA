package it.tndigitale.a4g.uma.business.service.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//import it.tndigitale.a4g.utente.client.api.UtenteControllerApi;
//import it.tndigitale.a4g.utente.client.model.Distributore;
//import it.tndigitale.a4g.utente.client.model.Utente;

@Component
public class UmaUtenteClient extends AbstractClient {
	@Value("${it.tndigit.a4g.uma.utente.url}")
	private String urlUtente;

//	// Methods from Controllers
//	public Utente getUtenteConnesso() {
//		return this.getUtenteControllerApi().caricaMieiDatiUsingGET();
//	}
//
//	public Distributore getDistributoreById(Long identificativoDistributore) {
//		return this.getUtenteControllerApi().getDistributoreByIdUsingGET(identificativoDistributore);
//	}
//
//	public List<Distributore> getDistributori() {
//		return this.getUtenteControllerApi().getDistributoriUsingGET();
//	}
//	
//	public List<String> getAziende() {
//		return this.getUtenteControllerApi().getAziendeUsingGET();
//	}
//
//	// Get Controller
//	private UtenteControllerApi getUtenteControllerApi() {
//		return restClientProxy(UtenteControllerApi.class, urlUtente);
//	}
}
