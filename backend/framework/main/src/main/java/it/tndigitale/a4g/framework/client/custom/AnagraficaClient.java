package it.tndigitale.a4g.framework.client.custom;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import it.tndigitale.a4g.framework.client.ClientServiceBuilder;
import it.tndigitale.a4g.framework.client.DefaultUrlMicroService;

@Component
public class AnagraficaClient {
	
	@Value(DefaultUrlMicroService.ANAGRAFICA_URL)
	private String serverUrl;
	
	@Autowired
	private ClientServiceBuilder clientServiceBuilder;

	private final static String CONTEXT_RESOURCE = "/api/v1/fascicolo/private";

	// @Autowired
	private RestTemplate restTemplate;

	public RestTemplate getRestTemplateUtenteConnesso(String username) {
		   return clientServiceBuilder.buildWith(() -> username);
	}		
	
	public AnagraficaDto migraUsingPOST(List<File> allegati, String codiceFiscaleRappresentante, File contratto, String cuaa, Long identificativoSportello, Boolean migraModoPagamento, String username) throws RestClientException {

		restTemplate = this.getRestTemplateUtenteConnesso(username);

		if (allegati == null) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'allegati' when calling migraUsingPOST");
		}

		if (codiceFiscaleRappresentante == null) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'codiceFiscaleRappresentante' when calling migraUsingPOST");
		}

		if (contratto == null) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'contratto' when calling migraUsingPOST");
		}

		if (cuaa == null) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'cuaa' when calling migraUsingPOST");
		}

		if (identificativoSportello == null) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'identificativoSportello' when calling migraUsingPOST");
		}
		
		if (migraModoPagamento == null) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'migraModoPagamento' when calling migraUsingPOST");
		}
				
		String path = serverUrl + CONTEXT_RESOURCE + "/" + cuaa + "/migra";
		
		final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
		formParams.add("codiceFiscaleRappresentante", codiceFiscaleRappresentante);
		formParams.add("identificativoSportello", identificativoSportello);
		formParams.add("migraModoPagamento", migraModoPagamento);
		if (allegati != null)
			allegati.forEach(allegato -> formParams.add("allegati", new FileSystemResource(allegato)));
		if (contratto != null)
			formParams.add("contratto", new FileSystemResource(contratto));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(formParams, headers);
		ResponseEntity<AnagraficaDto> response = restTemplate.exchange(path, HttpMethod.POST, requestEntity, AnagraficaDto.class);

		return response.getBody();
	}

}
