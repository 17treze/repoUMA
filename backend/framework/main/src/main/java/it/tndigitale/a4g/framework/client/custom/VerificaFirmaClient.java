package it.tndigitale.a4g.framework.client.custom;

import it.tndigitale.a4g.framework.client.DefaultUrlMicroService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class VerificaFirmaClient {
	@Value(DefaultUrlMicroService.PROXY_URL)
	private String serverUrl;

	private final static String CONTEXT_RESOURCE = "api/v1/verificafirma/singola/%s";

	private static final String CODICE_FISCALE_KEY = "codiceFiscale";

	private static final String DOCUMENT_KEY = "documentoFirmato";

	private static final Logger logger = LoggerFactory.getLogger(VerificaFirmaClient.class);

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private ObjectMapper objectMapper;

	public MetadatiDocumentoFirmatoDto verificaFirma(ByteArrayResource document, String codiceFiscaleFirmatario) throws Exception {
		MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<String, Object>();

		bodyMap.add(CODICE_FISCALE_KEY, codiceFiscaleFirmatario);
		bodyMap.add(DOCUMENT_KEY, document);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);

		String path = serverUrl + CONTEXT_RESOURCE;

		logger.debug("invio via restTemplate [" + path + "] - request[" + requestEntity + " ]");
		ResponseEntity<MetadatiDocumentoFirmatoDto> response =
				restTemplate.exchange
						(String.format(path,
								codiceFiscaleFirmatario),
								HttpMethod.POST,
								requestEntity,
								MetadatiDocumentoFirmatoDto.class);
		logger.debug("invio concluso con response = [" + response + "]");
		return response.getBody();
	}
}
