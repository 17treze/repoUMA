package it.tndigitale.a4gutente.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import it.tndigitale.a4g.framework.security.model.UtenteComponent;

@Configuration
public class A4GUtenteConfig {

	@Autowired
	protected UtenteComponent utenteCorrente;
	
//	@Autowired
//	public ClientServiceBuilder clientServiceBuilder;
	
//	@Bean // Serve per far andare autowired di RestTemplate in jboss
//	public RestTemplate createRestTemplate() {
//		return clientServiceBuilder
//				.buildWith(() -> utenteCorrente.utenza());
//
//	}

//	@Bean
//	public AutowireHelper createAutowireHelper() {
//		return AutowireHelper.getInstance();
//	}
}
