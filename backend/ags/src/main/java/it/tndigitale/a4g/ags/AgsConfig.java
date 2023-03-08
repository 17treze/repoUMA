/**
 * 
 */
package it.tndigitale.a4g.ags;

import it.tndigitale.a4g.framework.client.ClientServiceBuilder;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author S.DeLuca
 *
 */
@Configuration
public class AgsConfig {
	
	@Autowired
	protected UtenteComponent utenteCorrente;
	
	@Autowired
	public ClientServiceBuilder clientServiceBuilder;
	
	
	@Bean
	public RestTemplate createRestTemplate() {
//		return new RestTemplate();
		return clientServiceBuilder
				.buildWith(() -> utenteCorrente.utenza());

	}
	
}