package it.tndigitale.a4g.uma.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
@ComponentScan
public class SwaggerConfiguration {
	@Bean
    public OpenAPI umaOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("A4G - UMA API - Utenti Motori Agricoli")
                .description("Modulo per l'erogazione dei servizi relativi al carburante per uso agricolo")
                .version("1.0.0"));
    }
}
