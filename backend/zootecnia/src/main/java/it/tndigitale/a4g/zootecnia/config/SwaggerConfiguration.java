package it.tndigitale.a4g.zootecnia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
@ComponentScan
public class SwaggerConfiguration {
	@Bean
    public OpenAPI zootecniaOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("A4G - Zootecnia")
                .description("Modulo per Zootecnia")
                .version("1.0.0"));
    }
}
