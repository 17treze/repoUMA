package it.tndigitale.a4g.fascicolo.anagrafica;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class CorsConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
		.allowedOrigins("*")
		.allowedMethods("GET", "PUT", "POST", "PATCH", "DELETE", "OPTIONS", "HEAD")
		.allowCredentials(true);
	}

}