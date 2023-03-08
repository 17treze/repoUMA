package it.tndigitale.a4gistruttoria.component;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class A4GProxyClientComponent {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${a4gistruttoria.proxy.uri}")
	private String a4gproxyUri;

	public <T> ResponseEntity<T> call(String path, HttpMethod method, @Nullable HttpEntity<?> requestEntity, Class<T> responseType) throws Exception {
		String full = a4gproxyUri + path;

		return restTemplate.exchange(new URI(full), method, requestEntity, responseType);

	}

	protected HttpHeaders createHeaders() {
		return new HttpHeaders();
	}
}
