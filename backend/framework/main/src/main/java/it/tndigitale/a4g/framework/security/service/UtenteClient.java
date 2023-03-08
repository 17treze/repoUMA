package it.tndigitale.a4g.framework.security.service;

import java.net.URI;
import java.util.List;

import it.tndigitale.a4g.framework.client.ClientServiceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UtenteClient {
	private static final String PREFISSO_UTENTE = "utenti";
	private static final String RUOLI_UTENTE = PREFISSO_UTENTE + "/utente/ruoli";
	private static final String ENTI_UTENTE = PREFISSO_UTENTE + "/utente/enti";
	private static final String AZIENDE_UTENTE = PREFISSO_UTENTE + "/utente/aziende";

	private static Logger log = LoggerFactory.getLogger(UtenteClient.class);

	@Autowired
	public ClientServiceBuilder clientServiceBuilder;

	// Da capire con utente se OK: manca utente
	@Autowired
	private RestTemplate restTemplate;

	@Value("${it.tndigit.security.utente.url:dummy_url_utente}")
	private String utenteUrl;

	public List<String> getRuoliUtente(final String username) throws Exception {
		RestTemplate restTemplate = clientServiceBuilder
				.buildWith(() -> username);
		
		return getDatiUtente(ServiziUtente.RUOLI.getPath(), restTemplate);
	}

	public List<String> getEntiUtente() throws Exception {
		return getDatiUtente(ServiziUtente.ENTI.getPath(), restTemplate);
	}

	public List<String> getAziendeUtente() throws Exception {
		return getDatiUtente(ServiziUtente.AZIENDE.getPath(), restTemplate);
	}
	
	
	protected List<String> getDatiUtente(String path, RestTemplate restTemplate) throws Exception {
		log.debug("getDatiUtente: path = " + path);
				
		ResponseEntity<List<String>> response = restTemplate.exchange(new URI(utenteUrl + path), HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {
		});

		List<String> dati = response.getBody();
		log.debug("getDatiUtente: ok");
		return dati;
	}

	enum ServiziUtente {

		RUOLI(RUOLI_UTENTE),
		ENTI(ENTI_UTENTE),
		AZIENDE(AZIENDE_UTENTE);

		private String path;

		private ServiziUtente(String path) {
			this.path = path;
		}

		public String getPath() {
			return path;
		}
	}
}
