package it.tndigitale.a4g.framework.client;

import org.springframework.web.client.RestTemplate;

public interface RestApiClient<T extends RestApiClient<T>> {
    T set(RestTemplate restTemplate, String basePath);
    T setRestTemplate(RestTemplate restTemplate);
    T setBasePath(String basePath);
}
