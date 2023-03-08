package it.tndigitale.a4g.proxy.bdn.client;

import org.springframework.beans.factory.annotation.Autowired;

import it.tndigitale.a4g.framework.client.RestApiClient;
import it.tndigitale.a4g.framework.client.RestClientBuilder;

abstract class AbstractClient {
	
	@Autowired
	private RestClientBuilder restClientBuilder;
	
	protected <T extends RestApiClient<T>> T restClient(Class<T> clazzClient, String url) {
		return restClientBuilder.from(clazzClient)
				.setBasePath(url)
				.newInstance();
	}
}
