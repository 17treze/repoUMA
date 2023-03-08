package it.tndigitale.a4gistruttoria.service.businesslogic.iotialia;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.IoItaliaMessage;

@Component
public class IoItaliaProperties {
	
	private static final String PROP_PREFIX="io-italia.";
	private static final String OGGETTO=".oggetto";
	private static final String MESSAGGIO=".messaggio";
	private Properties props;
	
	@PostConstruct
	public void loadProperties() throws IOException {
		Resource resource = new ClassPathResource("/io.italia.messaggi.properties");
		props = PropertiesLoaderUtils.loadProperties(resource);
	}
	
	public IoItaliaMessage getProperties(String messagePrefix) {
		IoItaliaMessage ioItaliaMessage =new IoItaliaMessage();
		ioItaliaMessage
			.setMessaggio(props.getProperty(PROP_PREFIX+messagePrefix+MESSAGGIO))
			.setOggetto(props.getProperty(PROP_PREFIX+messagePrefix+OGGETTO));
		return ioItaliaMessage;
	}

}
