package it.tndigitale.a4gistruttoria;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import it.tndigitale.a4gistruttoria.util.RichiestaSuperficiePerCalcoloConverter;
import it.tndigitale.a4gistruttoria.util.StatisticaDtoToEntityConverter;

@Configuration
public class IstruttoriaConfigurer implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
		.allowedOrigins("*")
		.allowedMethods(HttpGet.METHOD_NAME, HttpPost.METHOD_NAME, HttpPut.METHOD_NAME, HttpDelete.METHOD_NAME, HttpHead.METHOD_NAME, HttpOptions.METHOD_NAME)
		.allowCredentials(true);
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StatisticaDtoToEntityConverter());
        registry.addConverter(new RichiestaSuperficiePerCalcoloConverter());
    }
}