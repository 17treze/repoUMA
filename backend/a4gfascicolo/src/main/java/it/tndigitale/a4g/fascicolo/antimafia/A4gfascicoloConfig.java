/**
 * 
 */
package it.tndigitale.a4g.fascicolo.antimafia;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import it.tndigitale.a4g.framework.client.ClientServiceBuilder;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;

/**
 * @author S.DeLuca
 *
 */
@Configuration
public class A4gfascicoloConfig {
	
	@Autowired
	protected UtenteComponent utenteCorrente;
	
	@Autowired
	public ClientServiceBuilder clientServiceBuilder;
	
	
//	@Bean
//	public RestTemplate createRestTemplate() {
////		return new RestTemplate();
//		return clientServiceBuilder
//				.buildWith(() -> utenteCorrente.utenza());
//
//	}
//
//	@Bean
//	public AutowireHelper createAutowireHelper() {
//		return AutowireHelper.getInstance();
//	}
	
}