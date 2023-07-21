package it.tndigitale.a4g.framework.security.service;

import java.net.URI;
import java.util.List;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.framework.client.ClientServiceBuilder;

@Component
public class UtenteClient {
	// private static final String PREFISSO_UTENTE = "utente";
	private static final String RUOLI_UTENTE = "ruoli";
	private static final String ENTI_UTENTE = "N.D.";
	private static final String AZIENDE_UTENTE = "aziendeDelegate";
	
	private static Logger log = LoggerFactory.getLogger(UtenteClient.class);
	
	@Autowired
	public ClientServiceBuilder clientServiceBuilder;
	
	// Da capire con utente se OK: manca utente
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${it.tndigit.security.utente.url:dummy_url_utente}")
	private String utenteUrl;
	
	public List<String> getRuoliUtente(final String username) throws Exception {
		RestTemplate restTemplate = clientServiceBuilder.buildWith(() -> username);
		return getDatiUtente(RUOLI_UTENTE, restTemplate);
	}
	
	public List<String> getEntiUtente() throws Exception {
		return getDatiUtente(ENTI_UTENTE, restTemplate);
	}
	
	public List<String> getAziendeUtente() throws Exception {
		return getDatiUtente(AZIENDE_UTENTE, restTemplate);
	}
	
	protected List<String> getDatiUtente(String path, RestTemplate restTemplate) throws Exception {
		log.info("getDatiUtente: path = " + path);
		
		ObjectMapper mapper = new ObjectMapper();
		List<String> keys = new ArrayList<String>();
		
		//		ResponseEntity<List<String>> response = restTemplate.exchange(new URI(utenteUrl + path), HttpMethod.GET, null,
		//				new ParameterizedTypeReference<List<String>>() {
		//				});
		ResponseEntity<String> response = restTemplate.getForEntity(new URI(utenteUrl + "utente/getInfoUtente?applicazione=UMA"), String.class);
		
		//		List<String> dati = response.getBody();
		JsonNode dati = mapper.readTree(response.getBody());
		JsonNode keysApp = dati.get(path);
		if (keysApp.isArray()) {
			for (JsonNode keyApp : keysApp) {
				if (keyApp.get("applicazione").textValue().equals("UMA")) {
					JsonNode values = keyApp.get(path);
					if (values.isArray()) {
						for (JsonNode value : values) {
							if (path.equals(RUOLI_UTENTE)) {
								keys.add(value.get("descrizione").textValue());
							} else if (path.equals(AZIENDE_UTENTE)) {
								keys.add(value.get("cuaa").textValue());
							}
						}
					}
				}
			}
		}
		log.info("getDatiUtente: " + keys);
		return keys;
	}
}
