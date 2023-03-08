package it.tndigitale.a4g.proxy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.soap.SoapMessageFactory;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

/**
 * Classi di configurazione per il progetto.
 * Al suo interno si trovano definizioni di beans di contesto per l'allicazione.
 * 
 * @author S.DeLuca
 *
 */
@Configuration
public class A4GProxyConfig {

	/**
	 * Definizione del {@link SaajSoapMessageFactory}, necessario per invocare i WS.
	 */
	@Bean
	public SoapMessageFactory messageFactory() {
		return new SaajSoapMessageFactory();
	}

}
