package it.tndigitale.a4g.fascicolo.mediator.config;

import it.tndigitale.a4g.fascicolo.mediator.MediatorApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MediatorApplication.class);
	}

}
