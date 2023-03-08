/**
 * 
 */
package it.tndigitale.a4gistruttoria.action;

import java.net.URI;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import it.tndigitale.a4gistruttoria.dto.IstruttoriaAntimafiaChainHandler;

/**
 * @author S.DeLuca
 *
 */
@Component
public class SiapConsumer implements Consumer<IstruttoriaAntimafiaChainHandler> {

	private static final Logger logger = LoggerFactory.getLogger(SiapConsumer.class);

	@Autowired
	private RestTemplate restTemplate;
	@Value("${a4gistruttoria.ags.uri}")
	private String agsUrl;

	@Override
	public void accept(IstruttoriaAntimafiaChainHandler istruttoriaAntimafiaChain) {
		try {
			//controllo 2: fascicolo attivo in ags - chiamata a ags siap
			String cuaa = istruttoriaAntimafiaChain.getCuaa();
			Boolean isSiapOK = restTemplate.getForObject(new URI(agsUrl.concat("fascicoli").concat("/").concat(cuaa).concat("/").concat("controllaValidita")), Boolean.class);
			if (!isSiapOK) {
				istruttoriaAntimafiaChain.setSiapErrorMessage("L'impresa ".concat(cuaa).concat(" non ha un fascicolo attivo in SIAP"));
			}
		} catch (Exception e) {
			logger.error("istruttoria antimafia - errore durante lo step di recupero dati da siap", e);
			throw new RuntimeException("istruttoria antimafia - errore durante lo step di recupero dati da siap", e);
		}
	}

}
