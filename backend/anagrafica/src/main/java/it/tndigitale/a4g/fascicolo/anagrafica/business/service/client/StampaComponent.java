package it.tndigitale.a4g.fascicolo.anagrafica.business.service.client;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import it.tndigitale.a4g.framework.client.RestClientBuilder;
import it.tndigitale.a4g.proxy.client.api.StampaControllerApi;

@Component
public class StampaComponent {

	@Autowired protected RestClientBuilder restClientBuilder;
	
	@Value("${it.tndigit.a4g.client.proxy.url}")
	private String urlProxy;

	private StampaControllerApi stampaControllerApi;

	private StampaControllerApi getStampaControllerApi() {
		if (stampaControllerApi == null) {
			stampaControllerApi = restClientBuilder.from(StampaControllerApi.class)
					.setBasePath(urlProxy)
					.newInstance();
		}
		return stampaControllerApi;
	}
	
	public byte[] stampaPDFA(String dati, String templateName) throws RestClientException, IOException {
		return getStampaControllerApi().stampaUsingPOST(new ClassPathResource(templateName).getFile(), dati, "PDF_A");
	}
	
	public byte[] stampaDOCX(String dati, String templateName) throws RestClientException, IOException {
		return getStampaControllerApi().stampaUsingPOST(new ClassPathResource(templateName).getFile(), dati, "DOCX");
	}
}
