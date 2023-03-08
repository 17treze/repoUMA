package it.tndigitale.a4g.richiestamodificasuolo.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@EnableAutoConfiguration
@ComponentScan("it.tndigitale.a4g.richiestamodificasuolo.config")
public class RichiestaModificaSuoloConfig {

	@Bean
	@Qualifier("secureRestTemplateCustomizer")
	public SecureRestTemplateCustomizer secureRestTemplateCustomizer() {
		return new SecureRestTemplateCustomizer();
	}

	@Bean
	@DependsOn(value = { "secureRestTemplateCustomizer" })
	public RestTemplateBuilder restTemplateBuilder() {
		return new RestTemplateBuilder(secureRestTemplateCustomizer());
	}

}
