package it.tndigitale.a4g.fascicolo.dotazionetecnica.config;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.DotazioneTecnicaApplication;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(DotazioneTecnicaApplication.class);
	}

}
