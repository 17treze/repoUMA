package it.tndigitale.a4g.richiestamodificasuolo.config;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.core.io.Resource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class SecureRestTemplateCustomizer implements RestTemplateCustomizer {

	@Value("${client.proxy.ssl.trust-store}")
	private Resource trustStore;
	@Value("${client.proxy.ssl.trust-store-password}")
	private String trustStorePassword;
	@Value("${client.proxy.ssl.protocol}")
	private String sslProtocol;

	@Override
	public void customize(RestTemplate restTemplate) {

		final SSLContext sslContext;
		try {
			sslContext = SSLContextBuilder.create().loadTrustMaterial(trustStore.getURL(), trustStorePassword.toCharArray()).setProtocol(sslProtocol).build();
		} catch (Exception e) {
			throw new IllegalStateException("Failed to setup client SSL context", e);
		}

		final HttpClient httpClient = HttpClientBuilder.create().setSSLContext(sslContext).build();
		final ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
		restTemplate.setRequestFactory(requestFactory);

	}
}