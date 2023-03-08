package it.infotn.bdn.wsbdnagea.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import it.infotn.bdn.wsbdnagea.config.PropertyLoader;
import it.infotn.bdn.wsbdnagea.wsclient.AnagraficaAllevamentiClient;
import it.infotn.bdn.wsbdnagea.wsclient.ConsistenzaAlPascoloPAC2015Client;
import it.infotn.bdn.wsbdnagea.wsclient.ConsistenzaAllevamentoClient;
import it.infotn.bdn.wsbdnagea.wsclient.ConsistenzaUBACensimentoOvini2012Client;
import it.infotn.bdn.wsbdnagea.wsclient.ListaIngressiAlPascoloPartiteOviniClient;
import it.infotn.bdn.wsbdnagea.wsclient.ListaUsciteDaPascoloPartiteOviniClient;
import it.infotn.bdn.wsbdnagea.wsclient.TrovaPascoloPAC2015Client;

@Configuration
public class BdnAgeaConfiguration {
	
	public final static String webServiceUri = "bdn.agea.url";
	public final static String webServiceUsername = "bdn.username";
	public final static String webServicePassword = "bdn.password";
	public final static String contextPath = "it.izs.wsdl.wsBDNAgea";
	
	protected PropertyLoader loader = PropertyLoader.getInstance(); 

	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath(contextPath);
		return marshaller;
	}

	@Bean
	public ConsistenzaUBACensimentoOvini2012Client getConsistenzaUBACensimentoOvini2012Client(Jaxb2Marshaller marshaller) {
		ConsistenzaUBACensimentoOvini2012Client client = new ConsistenzaUBACensimentoOvini2012Client();
		
		String webServiceUri = loader.getValue(BdnAgeaConfiguration.webServiceUri);
		client.setDefaultUri(webServiceUri);		
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}

	@Bean
	public ConsistenzaAlPascoloPAC2015Client getConsistenzaAlPascoloPAC2015Client(Jaxb2Marshaller marshaller) {
		ConsistenzaAlPascoloPAC2015Client client = new ConsistenzaAlPascoloPAC2015Client();
		
		String webServiceUri = loader.getValue(BdnAgeaConfiguration.webServiceUri);
		client.setDefaultUri(webServiceUri);		
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}

	@Bean
	public TrovaPascoloPAC2015Client getTrovaPascoloPAC2015Client(Jaxb2Marshaller marshaller) {
		TrovaPascoloPAC2015Client client = new TrovaPascoloPAC2015Client();
		
		String webServiceUri = loader.getValue(BdnAgeaConfiguration.webServiceUri);
		client.setDefaultUri(webServiceUri);		
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}

	@Bean
	public ListaIngressiAlPascoloPartiteOviniClient getListaIngressiAlPascoloPartiteOviniClient(Jaxb2Marshaller marshaller) {
		ListaIngressiAlPascoloPartiteOviniClient client = new ListaIngressiAlPascoloPartiteOviniClient();
		
		String webServiceUri = loader.getValue(BdnAgeaConfiguration.webServiceUri);
		client.setDefaultUri(webServiceUri);		
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}

	@Bean
	public ListaUsciteDaPascoloPartiteOviniClient getListaUsciteDaPascoloPartiteOviniClient(Jaxb2Marshaller marshaller) {
		ListaUsciteDaPascoloPartiteOviniClient client = new ListaUsciteDaPascoloPartiteOviniClient();
		
		String webServiceUri = loader.getValue(BdnAgeaConfiguration.webServiceUri);
		client.setDefaultUri(webServiceUri);		
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}

	@Bean
	public ConsistenzaAllevamentoClient inviaConsistenzaAllevamento(Jaxb2Marshaller marshaller) {
		ConsistenzaAllevamentoClient client = new ConsistenzaAllevamentoClient();
		
		String webServiceUri = loader.getValue(BdnAgeaConfiguration.webServiceUri);
		client.setDefaultUri(webServiceUri);		
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}

	@Bean
	public AnagraficaAllevamentiClient getAnagraficaAllevamenti(Jaxb2Marshaller marshaller) {
		AnagraficaAllevamentiClient client = new AnagraficaAllevamentiClient();
		
		String webServiceUri = loader.getValue(BdnAgeaConfiguration.webServiceUri);
		client.setDefaultUri(webServiceUri);		
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}

}
