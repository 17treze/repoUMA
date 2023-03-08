package it.tndigitale.a4g.fascicolo.anagrafica.business.service.client;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.fascicolo.zootecnia.client.api.ZootecniaPrivateControllerApi;

@Component
public class ZootecniaPrivateClient extends AbstractClient {
	@Value("${it.tndigit.a4g.anagrafica.zootecnia.url}") private String urlZootecnia;
	
	private ZootecniaPrivateControllerApi getZootecniaPrivateControllerApi() {
		return restClientProxy(ZootecniaPrivateControllerApi.class, urlZootecnia);
	}

	public ResponseEntity<Void> startValidazioneFascicolo(final String cuaa, final Integer idValidazione) {
		return getZootecniaPrivateControllerApi().startValidazioneFascicoloWithHttpInfo(cuaa, idValidazione);
	}
	
	public ResponseEntity<Void> invioEventoFineValidazione(final String cuaa, final Integer idValidazione) {
		return getZootecniaPrivateControllerApi().invioEventoFineValidazioneWithHttpInfo(cuaa, idValidazione);
	}
}
