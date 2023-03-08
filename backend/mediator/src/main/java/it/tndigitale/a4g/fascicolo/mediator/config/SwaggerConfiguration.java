package it.tndigitale.a4g.fascicolo.mediator.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class SwaggerConfiguration {
	@Bean
    public OpenAPI mediatorOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("A4G - Fascicolo Mediator")
                .description("Componente applicativo concepito per isolare le logiche trasversali ai vari moduli del fascicolo")
                .version("1.0.0"));
    }
}
