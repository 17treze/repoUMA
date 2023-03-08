/**
 * 
 */
package it.tndigitale.a4g.proxy.bdn.wsbdninterrogazioni;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.soap.SoapMessageFactory;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

@Configuration
public class WSBDNInterrogazioniConfig {

	/**
	 * Definizione del {@link SaajSoapMessageFactory}, necessario per invocare i WS.
	 */
	@Bean
	public SoapMessageFactory messageFactory() {
		return new SaajSoapMessageFactory();
	}
}
