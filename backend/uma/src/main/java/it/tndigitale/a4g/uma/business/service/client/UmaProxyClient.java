package it.tndigitale.a4g.uma.business.service.client;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.proxy.client.api.StampaControllerApi;

@Component
public class UmaProxyClient extends AbstractClient {

	@Value("${it.tndigit.a4g.uma.proxy.url}")
	private String urlProxy;

	public byte[] stampa(String dati, String templateName) throws IOException {
		var file = new ClassPathResource(templateName).getFile();
		return getStampaControllerApi().stampaV2UsingPOST("PDF_A", file, dati);
	}

	private StampaControllerApi getStampaControllerApi() {
		return restClientProxy(StampaControllerApi.class, urlProxy);
	}
}
