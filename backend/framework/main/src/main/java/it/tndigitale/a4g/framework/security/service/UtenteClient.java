package it.tndigitale.a4g.framework.security.service;

import java.net.URI;
import java.util.List;

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
	private static final String PREFISSO_UTENTE = "utente";
	private static final String RUOLI_UTENTE = PREFISSO_UTENTE + "/getInfoUtente?applicazione=UMA";
	private static final String ENTI_UTENTE = PREFISSO_UTENTE + "/getInfoUtente?applicazione=UMA";
	private static final String AZIENDE_UTENTE = PREFISSO_UTENTE + "/getInfoUtente?applicazione=UMA";
	
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
		
		return getDatiUtente(ServiziUtente.RUOLI.getPath(), restTemplate);
	}
	
	public List<String> getEntiUtente() throws Exception {
		return getDatiUtente(ServiziUtente.ENTI.getPath(), restTemplate);
	}
	
	public List<String> getAziendeUtente() throws Exception {
		return getDatiUtente(ServiziUtente.AZIENDE.getPath(), restTemplate);
	}
	
	protected List<String> getDatiUtente(String path, RestTemplate restTemplate) throws Exception {
		log.info("getDatiUtente: path = " + path);
		
		ObjectMapper mapper = new ObjectMapper();
		
		ResponseEntity<String> response = restTemplate.getForEntity(new URI(utenteUrl + path), String.class);
		//		ResponseEntity<List<String>> response = restTemplate.exchange(new URI(utenteUrl + path), HttpMethod.GET, null,
		//				new ParameterizedTypeReference<List<String>>() {
		//				});
		
		JsonNode dati = mapper.readTree(response.getBody());
		//		List<String> dati = response.getBody();
		log.info("getDatiUtente: " + dati);
		return null;
	}
	
	enum ServiziUtente {
		
		RUOLI(RUOLI_UTENTE), ENTI(ENTI_UTENTE), AZIENDE(AZIENDE_UTENTE);
		
		private String path;
		
		private ServiziUtente(String path) {
			this.path = path;
		}
		
		public String getPath() {
			return path;
		}
	}
}
