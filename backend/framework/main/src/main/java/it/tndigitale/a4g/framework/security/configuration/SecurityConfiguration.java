package it.tndigitale.a4g.framework.security.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfiguration {
		@Autowired
		protected UtenteComponent utenteCorrente;

		@Bean
		public AutowireSecurityContext createAutowireHelper() {
			return AutowireSecurityContext.build();
		}

}
