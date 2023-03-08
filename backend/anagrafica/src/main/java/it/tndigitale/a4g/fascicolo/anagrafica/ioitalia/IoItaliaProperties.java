package it.tndigitale.a4g.fascicolo.anagrafica.ioitalia;

import java.io.IOException;
import java.time.Period;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

@Component
public class IoItaliaProperties {
	private static final String PROP_PREFIX = "io-italia.";
	private static final String OGGETTO = ".oggetto";
	private static final String PERIODO_SCADENZA = ".periodo-scadenza";
	private static final String MESSAGGIO = ".messaggio";
	private static final String NUM_MAX_TENTATIVI = ".num-max-tentativi";
	
	private Properties props;

	@PostConstruct
	public void loadProperties() throws IOException {
		Resource resource = new ClassPathResource("/io.italia.messaggi.properties");
		props = PropertiesLoaderUtils.loadProperties(resource);
	}

	public IoItaliaMessage getWithProperties(final String messagePrefix) {
		var ioItaliaMessage = new IoItaliaMessage();
		ioItaliaMessage
		.setMessaggio(props.getProperty(PROP_PREFIX + messagePrefix + MESSAGGIO))
		.setOggetto(props.getProperty(PROP_PREFIX + messagePrefix + OGGETTO))
		.setPeriod(Period.parse(props.getProperty(PROP_PREFIX + messagePrefix + PERIODO_SCADENZA)))
		.setNumMaxTentativi(Integer.valueOf(props.getProperty(PROP_PREFIX + messagePrefix + NUM_MAX_TENTATIVI)));
		return ioItaliaMessage;
	}
}
