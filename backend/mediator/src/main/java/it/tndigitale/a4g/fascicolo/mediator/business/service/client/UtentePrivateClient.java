package it.tndigitale.a4g.fascicolo.mediator.business.service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.utente.client.api.UtentePrivateControllerApi;
import it.tndigitale.a4g.utente.client.model.ReportValidazioneDto;

@Component
public class UtentePrivateClient extends AbstractClient {
	@Value("${it.tndigit.a4g.fascicolo.mediator.utente.url}") private String urlUtente;

	private UtentePrivateControllerApi getUtentePrivateControllerApi() {
		return restClientProxy(UtentePrivateControllerApi.class, urlUtente);
	}

	public ReportValidazioneDto getReportValidazione(final String cuaa) {
		return getUtentePrivateControllerApi().getReportValidazioneUsingGET(cuaa);
	}
}
