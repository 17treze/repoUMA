package it.tndigitale.a4g.fascicolo.mediator.business.service.client;

import it.tndigitale.a4g.framework.client.RestApiClient;
import it.tndigitale.a4g.framework.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;

abstract class AbstractClient {
	
	@Autowired
	private RestClientBuilder restClientBuilder;
	
	protected <T extends RestApiClient<T>> T restClientProxy(Class<T> clazzClient, String url) {
		return restClientBuilder.from(clazzClient)
				.setBasePath(url)
				.newInstance();
	}
}

