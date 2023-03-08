package it.tndigitale.a4gistruttoria.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.framework.client.RestApiClient;
import it.tndigitale.a4g.framework.client.RestClientBuilder;
import it.tndigitale.a4g.framework.client.RestClientBuilder.BuilderClientService;
import it.tndigitale.a4gistruttoria.A4gistruttoriaConfigurazione;
import it.tndigitale.a4gistruttoria.dto.CertificazioneAntimafiaFilter;
import it.tndigitale.a4gistruttoria.dto.PageResultWrapper;
import it.tndigitale.a4gistruttoria.dto.Pagination;
import it.tndigitale.a4gistruttoria.dto.Sort;
import it.tndigitale.a4gistruttoria.dto.antimafia.DichiarazioneAntimafia;

@Component
public class ConsumeExternalRestApi {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private A4gistruttoriaConfigurazione configurazione;

	private RestClientBuilder restClientBuilder;

	@Autowired
	protected void setRestClientBuilder(RestClientBuilder restClientBuilder) {
		this.restClientBuilder = restClientBuilder;
	}

	public <T extends RestApiClient<T>> T restClientProxy(Class<T> clazzClient) {
		String url = configurazione.getUriProxy().replace("/api/v1", ""); // TODO: workaround, da riconfigurare tutte gli url e togliere /api/v1
		return newRestClient(clazzClient, url).newInstance();
	}

	public <T extends RestApiClient<T>> T restClientBDN(Class<T> clazzClient) {
		String url = configurazione.getUriCacheBdn().replace("/api/v1/sync", ""); // TODO: workaround, da riconfigurare tutte gli url e togliere /api/v1
		return newRestClient(clazzClient, url).newInstance();
	}

	public <T extends RestApiClient<T>> T restClientDomandaUnica(Class<T> clazzClient) {
		String url = configurazione.getUriAgs().replace("/api/v1", ""); // TODO: workaround, da riconfigurare tutte gli url e togliere /api/v1
		return newRestClient(clazzClient, url).newInstance();
	}
	

	private <T extends RestApiClient<T>> BuilderClientService<T> newRestClient(Class<T> clazzClient, String basePath) {
		return restClientBuilder.from(clazzClient).setBasePath(basePath);
	}

	public ResponseEntity<String> getDichiarazioneAntimafia(String idDichiarazioneAntimafia) {
		HttpEntity<String> entity = new HttpEntity<>("parameters", createHeaders());
		String url = configurazione.getUriFascicolo().concat("antimafia").concat("/").concat(idDichiarazioneAntimafia);
		return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
	}

	public ResponseEntity<String> putDichiarazioneAntimafia(String idDichiarazioneAntimafia, JsonNode jsonDomandaAntimafia) throws URISyntaxException, JsonProcessingException {
		URI uriFascicolo = new URI(configurazione.getUriFascicolo().concat("antimafia").concat("/").concat(idDichiarazioneAntimafia));
		HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(jsonDomandaAntimafia), createHeaders());
		return restTemplate.exchange(uriFascicolo, HttpMethod.PUT, entity, String.class);
	}

	public List<DichiarazioneAntimafia> getDichiarazioniAntimafia(String params) throws URISyntaxException {
		HttpEntity<String> entity = new HttpEntity<>("parameters", createHeaders());
		return restTemplate.exchange(new URI(configurazione.getUriFascicolo().concat("antimafia").concat("/").concat(params)), HttpMethod.GET, entity, new ParameterizedTypeReference<List<DichiarazioneAntimafia>>() {
		}).getBody();
	}
	
	public PageResultWrapper<DichiarazioneAntimafia> getDichiarazioniAntimafiaPage(CertificazioneAntimafiaFilter filter, Pagination pagination, Sort sort) throws URISyntaxException, RestClientException, UnsupportedEncodingException {
		return getDichiarazioniAntimafiaPage(filter, pagination, sort,null);
	}
	
	public PageResultWrapper<DichiarazioneAntimafia> getDichiarazioniAntimafiaPage(CertificazioneAntimafiaFilter filter, Pagination pagination, Sort sort, RestTemplate restTemplateUtenzaTecnica) throws URISyntaxException, RestClientException, UnsupportedEncodingException {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(configurazione.getUriFascicolo().concat("antimafia").concat("/page/"))
				.queryParam("statiDichiarazione", filter.getStato());
		if (pagination!= null && pagination.getPagSize() != null)
			builder.queryParam("pagSize", pagination.getPagSize());
		if (pagination!= null && pagination.getPagStart() != null)
			builder.queryParam("pagStart", pagination.getPagStart());
		if (sort!= null && sort.getSortBy() != null)
			builder.queryParam("sortBy", URLEncoder.encode(sort.getSortBy()[0], StandardCharsets.UTF_8.toString()) );
			//builder.queryParam("sortBy", sort.getSortBy()[0]);
		if (filter!= null && filter.getFiltroGenerico() != null)
			builder.queryParam("filtroGenerico", URLEncoder.encode(filter.getFiltroGenerico(), StandardCharsets.UTF_8.toString()) );
		HttpEntity<String> entity = new HttpEntity<>("parameters", createHeaders());
		RestTemplate rt=restTemplate;
		if (restTemplateUtenzaTecnica!= null) rt=restTemplateUtenzaTecnica;
		return rt
				.exchange(new URI (builder.build(false).toUriString()), HttpMethod.GET, entity, new ParameterizedTypeReference<PageResultWrapper<DichiarazioneAntimafia>>() {})
				.getBody();
	}

	public void sincronizzaConAgs(List<String> listaCuaa, RestTemplate restTemplateUtenzaTecnica) throws Exception {
		HttpEntity<String> entitySincronizzazione = new HttpEntity<>(objectMapper.writeValueAsString(listaCuaa), createHeaders());
		URI uriAgs = new URI(configurazione.getUriAgs().concat("certificazioni/antimafia"));
		restTemplateUtenzaTecnica.exchange(uriAgs, HttpMethod.PUT, entitySincronizzazione, String.class);
	}

	private HttpHeaders createHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}
}
