package it.tndigitale.a4g.fascicolo.antimafia.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.proxy.client.api.SincronizzazioneControllerApi;
import it.tndigitale.a4g.proxy.client.model.Dichiarazione;

@Component
public class ProxyClient extends AbstractClient  {

	@Value("${it.tndigit.a4g.client.proxy.url}")
	private String urlProxy;

	// Methods from Controllers
	public Dichiarazione putSincronizzazioneAntimafia(Long idDichiarazioneAntimafia, Dichiarazione dichiarazioneAntimafia) {
		return getSincronizzazioneControllerApi().aggiornaDichiarazioneUsingPUT(idDichiarazioneAntimafia, dichiarazioneAntimafia);
	}
	
	public Dichiarazione getSincronizzazioneAntimafia(Long idDichiarazioneAntimafia) {
		return getSincronizzazioneControllerApi().getDatiSincronizzazioneUsingGET(idDichiarazioneAntimafia);
	}

	// Get Controller 
	private SincronizzazioneControllerApi getSincronizzazioneControllerApi() {
		return restClientProxy(SincronizzazioneControllerApi.class, urlProxy);
	}
}
