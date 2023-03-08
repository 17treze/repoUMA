package it.tndigitale.a4g.fascicolo.mediator.business.service.client;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import it.tndigitale.a4g.proxy.client.api.StampaControllerApi;

@Component
public class ProxyClient extends AbstractClient {

	@Value("${it.tndigit.a4g.fascicolo.mediator.proxy.url}") private String urlProxy;

	public byte[] stampaPdfA(String templateName, String dati) throws RestClientException, IOException {
		return getStampaControllerApi().stampaV2UsingPOST("PDF_A", new ClassPathResource(templateName).getFile(), dati);
	}

	private StampaControllerApi getStampaControllerApi() {
		return restClientProxy(StampaControllerApi.class, urlProxy);
	}
}
