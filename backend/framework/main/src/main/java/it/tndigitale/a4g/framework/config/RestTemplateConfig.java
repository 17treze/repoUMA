package it.tndigitale.a4g.framework.config;

import it.tndigitale.a4g.framework.client.ClientServiceBuilder;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate createRestTemplate(
            @Autowired UtenteComponent utenteCorrente,
            @Autowired ClientServiceBuilder clientServiceBuilder) {
        return clientServiceBuilder.buildWith(utenteCorrente::utenza);
    }
}
