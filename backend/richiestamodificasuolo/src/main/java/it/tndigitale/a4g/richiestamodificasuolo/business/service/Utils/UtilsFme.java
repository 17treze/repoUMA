package it.tndigitale.a4g.richiestamodificasuolo.business.service.Utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UtilsFme {
	private static final Logger log = LoggerFactory.getLogger(UtilsFme.class);

	@Value("${it.tndigit.serverFme.url}")
	private String urlServerFme;

	@Value("${it.tndigit.serverFme.repository.sync}")
	private String repositoryA4gFmeSync;

	@Value("${it.tndigit.serverFme.repository.async}")
	private String repositoryA4gFmeAsync;

	@Value("${it.tndigit.serverFme.token}")
	private String tokenAuth;

	@Autowired
	private RestTemplate restTemplate;

	public ResponseEntity<String> callProcedureFme(Long idLavorazione, String nomeProcedura) throws URISyntaxException {

		String baseUrl = MessageFormat.format("{0}/fmerest/v3/transformations/transact/{1}/{2}", urlServerFme, repositoryA4gFmeSync, nomeProcedura);
		URI uri = new URI(baseUrl);

		return restTemplate.postForEntity(uri, generateHttpFmeRequest(idLavorazione, generateFmeRequestCallProcedureHeaders()), String.class);
	}

	public ResponseEntity<String> callProcedureFmeSync(String nomeProcedura, String body) throws URISyntaxException {

		String baseUrl = MessageFormat.format("{0}/fmerest/v3/transformations/transact/{1}/{2}", urlServerFme, repositoryA4gFmeSync, nomeProcedura);
		URI uri = new URI(baseUrl);

		return restTemplate.postForEntity(uri, generateHttpFmeRequest(body, generateFmeRequestCallProcedureHeaders()), String.class);
	}

	public ResponseEntity<String> callProcedureFmeAsync(String nomeProcedura, String body) throws URISyntaxException {

		String baseUrl = MessageFormat.format("{0}/fmerest/v3/transformations/submit/{1}/{2}", urlServerFme, repositoryA4gFmeAsync, nomeProcedura);
		URI uri = new URI(baseUrl);

		return restTemplate.postForEntity(uri, generateHttpFmeRequest(body, generateFmeRequestCallProcedureHeaders()), String.class);
	}

	public ResponseEntity<String> callProcedureFmeDataStreaming(String nomeProcedura, Map<String, String> params) throws URISyntaxException {

		String baseUrl = MessageFormat.format("{0}/fmedatastreaming/{1}/{2}", urlServerFme, repositoryA4gFmeSync, nomeProcedura);

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		headers.set("Accept", "application/json");

		params.put("token", tokenAuth);
		// params.put("opt_responseformat","json");

		String parametrizedArgs = params.entrySet().stream().map(k -> String.format("%s=%s", k.getKey(), k.getValue())).collect(Collectors.joining("&"));

		return restTemplate.getForEntity(new URI(baseUrl).toString().concat("?").concat(parametrizedArgs), String.class, headers);
	}

	public ResponseEntity<String> checkJobStatusFme(Long idJob) throws URISyntaxException {

		String baseUrl = MessageFormat.format("{0}/fmerest/v3/transformations/jobs/id/{1}", urlServerFme, Long.toString(idJob));

		URI uri = new URI(baseUrl);

		HttpHeaders headers = new HttpHeaders();
		// headers.set("Content-Type", "application/json");
		headers.set("Accept", "application/json");
		headers.set("Authorization", "fmetoken token=" + tokenAuth);

		HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);

		return restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
	}

	private HttpEntity<String> generateHttpFmeRequest(Long idLavorazione, HttpHeaders headers) {
		return new HttpEntity<>(generateBody(idLavorazione), headers);
	}

	private HttpEntity<String> generateHttpFmeRequest(String body, HttpHeaders headers) {
		return new HttpEntity<>(body, headers);
	}

	private HttpHeaders generateFmeRequestCallProcedureHeaders() {

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		headers.set("Accept", "application/json");
		headers.set("Authorization", "fmetoken token=" + tokenAuth);

		return headers;
	}

	public String generateBodyTrasformataConsolidamentoAGS(Long idLavorazione, Integer campagna, LocalDateTime dataSalvataggioLavorazione, String utenteAgs, Long sogliaClipper,
			Long scostamentoAreaGruppo, Long bufferGruppo) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return String.format(
				"{ \"publishedParameters\": [  { \"name\": \"idLavorazione\", \"value\": \"%s\" }, { \"name\": \"Data_salvataggio_lav\", \"value\": \"%s\" },{ \"name\": \"Utente\", \"value\": \"%s\" } ,{ \"name\": \"soglia_clipper\", \"value\": \"%s\" },{ \"name\": \"AREA_FILTER_GRUPPO\", \"value\": \"%s\" },{ \"name\": \"BUFFER_SIZE_GRUPPO\", \"value\": \"%s\" },{ \"name\": \"checkEquals\", \"value\": \"%s\" }, { \"name\": \"AnnoCampagna\", \"value\": \"%s\" } ] }",
				idLavorazione, dataSalvataggioLavorazione.format(formatter), utenteAgs, sogliaClipper, scostamentoAreaGruppo, bufferGruppo, 1, campagna);
	}

	public String generateBodyTrasformataConsolidamentoA4S(Long idLavorazione, Integer annoUpas, Long buchiPerimetroPrevalente, String destFolderShapefile) {

		String res = String.format(
				"{ \"publishedParameters\": [  { \"name\": \"idLavorazione\", \"value\": \"%s\" }, { \"name\": \"annoUpas\", \"value\": \"%s\" },{ \"name\": \"buchiPerimetroPrevalente\", \"value\": \"%s\" } ,{ \"name\": \"destFolderShapefile\", \"value\": \"%s\" },{ \"name\": \"importEsterno\", \"value\": \"%s\" } ] }",
				idLavorazione, annoUpas, buchiPerimetroPrevalente, destFolderShapefile, 0);
		return res;
	}

	public String generateBodyTrasformataConsolidamentoA4S_ADL(Long idLavorazione, Long buchiPerimetroPrevalente) {

		String res = String.format("{ \"publishedParameters\": [  { \"name\": \"idLavorazione\", \"value\": \"%s\" },{ \"name\": \"buchiPerimetroPrevalente\", \"value\": \"%s\" } ] }", idLavorazione,
				buchiPerimetroPrevalente);
		return res;
	}

	public String generateBody(Long idLavorazione) {
		return String.format("{ \"publishedParameters\": [ { \"name\" : \"idLavorazione\", \"value\" :  \"%s\" } ] }", idLavorazione);
	}

}