package it.tndigitale.a4g.proxy.bdn.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@ConfigurationProperties(prefix = "bdnparameter")
public class Bdnparameter {
	String responsabilita;

	public String getResponsabilita() {
		return responsabilita;
	}

	public void setResponsabilita(String responsabilita) {
		this.responsabilita = responsabilita;
	}

}
