package it.tndigitale.a4g.fascicolo.dotazionetecnica.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
@ComponentScan
public class SwaggerConfiguration {
	@Bean
    public OpenAPI applicationOpenApi() {
        return new OpenAPI()
                .info(new Info().title("A4G - Fascicolo - Dotazione Tecnica")
                .description("Modulo per l'erogazione dei servizi relativi alla dotazione tecnica del fascicolo")
                .version("1.0.0"));
    }
}
