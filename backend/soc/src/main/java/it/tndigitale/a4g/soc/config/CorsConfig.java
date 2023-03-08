package it.tndigitale.a4g.soc.config;

import org.apache.http.client.methods.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("*")
				.allowedMethods(HttpGet.METHOD_NAME,
								HttpPost.METHOD_NAME,
								HttpPut.METHOD_NAME,
								HttpDelete.METHOD_NAME,
								HttpHead.METHOD_NAME,
								HttpOptions.METHOD_NAME)
				.allowCredentials(true);
	}

}
