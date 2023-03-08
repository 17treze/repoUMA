package it.tndigitale.a4g.framework.client.custom;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.tndigitale.a4g.framework.client.DefaultUrlMicroService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class ProtocolloClient {

	// @Value("${it.tndigit.client.custom.protcollo.url:dummy_url_protocollo}")
	// private String protocolloUrl;
	@Value(DefaultUrlMicroService.PROXY_URL)
	private String serverUrl;

	private final static String CONTEXT_RESOURCE = "api/v1/protocollo/documenti";

	@Autowired
	private ObjectMapper objectMapper;

	private static final String METADATA_KEY = "info";

	private static final String DOCUMENT_KEY = "documento";

	private static final String ATTACHMENT_KEY = "allegati";

	private static final Logger logger = LoggerFactory.getLogger(ProtocolloClient.class);

	@Autowired
	private RestTemplate restTemplate;

	public String protocolla(DocumentDto document) {
		MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<String, Object>();

		String metadati = null;
		try {
			metadati = objectMapper.writeValueAsString(document.getMetadati());
		} catch (JsonProcessingException e) {
			logger.error("errore nella predisposizione dei metadati", e);
			throw new RuntimeException(e);
		}

		bodyMap.add(METADATA_KEY, metadati);
		bodyMap.add(DOCUMENT_KEY, document.getDocumentoPrincipale());
		if (document.getAllegati() != null)
			document.getAllegati().forEach(allegato -> bodyMap.add(ATTACHMENT_KEY, allegato));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);

		String path = serverUrl + CONTEXT_RESOURCE;

		logger.debug("invio via restTemplate [" + path + "] - request[" + requestEntity + " ]");
		ResponseEntity<String> response = restTemplate.exchange(path, HttpMethod.POST, requestEntity, String.class);
		logger.debug("invio concluso con response = [" + response + "]");
		return response.getBody();
	}
}
