package it.tndigitale.a4g.fascicolo.antimafia.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import it.tndigitale.a4g.fascicolo.antimafia.RestInterceptor;

/**
 * @author B.Conetta
 *
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer  {
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new RestInterceptor()).addPathPatterns("/**");
	}


}
