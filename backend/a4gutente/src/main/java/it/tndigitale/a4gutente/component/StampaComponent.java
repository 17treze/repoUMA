package it.tndigitale.a4gutente.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class StampaComponent extends A4GProxyClientComponent {

	private static Logger log = LoggerFactory.getLogger(StampaComponent.class);
	
	@Value("${a4gutente.integrazioni.stampa.path}")
	private String stampaUriPath;

	public byte[] stampaPDF_A(String dati, String templateName) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("stampaPDF_A: {}, {}", dati, templateName);
		}
		MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
		bodyMap.add("file", new ClassPathResource(templateName));
		
		bodyMap.add("dati", dati);
		bodyMap.add("formatoStampa", "PDF_A");
		HttpHeaders headers = createHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);			
		
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);
		
		ResponseEntity<byte[]> response = call(stampaUriPath, HttpMethod.POST, requestEntity, byte[].class);

		return response.getBody();
	}
	
}
