package it.tndigitale.a4g.richiestamodificasuolo.config;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import it.tndigitale.a4g.richiestamodificasuolo.RichiestaModificaSuoloApplication;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(RichiestaModificaSuoloApplication.class);
	}

}
