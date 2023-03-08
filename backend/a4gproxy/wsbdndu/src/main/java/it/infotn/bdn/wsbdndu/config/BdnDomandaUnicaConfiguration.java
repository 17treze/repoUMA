package it.infotn.bdn.wsbdndu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import it.infotn.bdn.wsbdndu.wsclient.DomandaUnicaClient;

@Configuration
public class BdnDomandaUnicaConfiguration {

	public final static String webServiceUri = "bdn.domandaunica.url";
	public final static String webServiceUsername = "bdn.domandaunica.username";
	public final static String webServicePassword = "bdn.domandaunica.password";
	public final static String contextPath = "it.izs.wsdl.wsBDNDomandaUnica";
	
	protected PropertyLoader loader = PropertyLoader.getInstance(); 

	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath(contextPath);
		return marshaller;
	}

	@Bean
	public DomandaUnicaClient getDomandaUnicaClient(Jaxb2Marshaller marshaller) {
		DomandaUnicaClient client = new DomandaUnicaClient();
		
		String webServiceUri = loader.getValue(BdnDomandaUnicaConfiguration.webServiceUri);
		client.setDefaultUri(webServiceUri);		
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}

}
