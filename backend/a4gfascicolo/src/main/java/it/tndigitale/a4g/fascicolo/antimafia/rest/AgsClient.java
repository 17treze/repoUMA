package it.tndigitale.a4g.fascicolo.antimafia.rest;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import it.tndigitale.a4g.fascicolo.antimafia.dto.consultazione.Fascicolo;
import it.tndigitale.a4g.fascicolo.antimafia.dto.consultazione.ParamsRicercaFascicolo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.fascicolo.antimafia.api.ApiUrls;
import it.tndigitale.a4g.fascicolo.antimafia.dto.consultazione.Fascicolo;
import it.tndigitale.a4g.fascicolo.antimafia.dto.consultazione.ParamsRicercaFascicolo;

@Component
public class AgsClient extends A4gClient {

	private static Logger log = LoggerFactory.getLogger(AgsClient.class);
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Value("${a4gfascicolo.ags.uri}")
	private String uriAgs;

	public List<Fascicolo> getFascicoli(ParamsRicercaFascicolo parametri) throws Exception {
		String params = objectMapper.writeValueAsString(parametri);
		log.debug("getFascicoli: params = {}", params);
		return getFascicoli(params);
	}
	
	public List<Fascicolo> getFascicoli(String params) throws UnsupportedEncodingException {
		try {
			String encoded = URLEncoder.encode(params, "UTF-8");
			String full = uriAgs + "fascicoli/?params=" + encoded;
			log.debug("getFascicoliDaAGS " + full + " - " + params);
			ResponseEntity<List<Fascicolo>> response = restTemplate.exchange(
					new URI(full), HttpMethod.GET, null,
					new ParameterizedTypeReference<List<Fascicolo>>() {});
			List<Fascicolo> respFascicoli = response.getBody();
			return Optional.ofNullable(respFascicoli).orElse(new ArrayList<>());
		} catch (RestClientException | URISyntaxException e) {
			log.error("Eccezione chiamando getFascicoli ",e);
		}
		return Collections.emptyList();
	}

	public Fascicolo getFascicolo(Long id) throws UnsupportedEncodingException, RestClientException {

		try {
			String full = uriAgs + "fascicoli/" + id;

			//RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<Fascicolo> response = restTemplate.exchange(new URI(full), HttpMethod.GET, null, new ParameterizedTypeReference<Fascicolo>() {
					});
			Fascicolo fascicolo = response.getBody();

			return fascicolo;
		} catch (URISyntaxException e) {
			log.error("Eccezione chiamando getFascicolo", e);
		}
		return null;
	}
	
	public boolean verificaFascicoloValido(String cuaa) throws UnsupportedEncodingException {
		try {
			String full = uriAgs + "fascicoli" + String.format(ApiUrls.AGS_FASCICOLO_VALIDO, cuaa);

			log.debug("Chiamo " + full);
			Boolean fascicoloValido = restTemplate.getForObject(
					new URI(full), Boolean.class);

			return fascicoloValido ==null ? false :fascicoloValido.booleanValue();
		} catch (RestClientException | URISyntaxException e) {
			log.error("Eccezione chiamando verificaFascicoloValido", e);
			return false;
		}
	}
}
