package it.tndigitale.a4g.fascicolo.antimafia.service.ext;

import org.springframework.beans.factory.annotation.Autowired;

import it.tndigitale.a4g.framework.client.RestApiClient;
import it.tndigitale.a4g.framework.client.RestClientBuilder;
import it.tndigitale.a4g.framework.client.RestClientBuilder.BuilderClientService;



public abstract class ConsumeExternalRestApiAbstract {
	
	@Autowired
	protected RestClientBuilder restClientBuilder;
	
	protected <T extends RestApiClient<T>> T restClientProxy(Class<T> clazzClient, String url) {
		url = url.replace("/api/v1", ""); // TODO: workaround, da riconfigurare tutte gli url e togliere /api/v1
		return newRestClient(clazzClient, url).newInstance();
	}
	
	private <T extends RestApiClient<T>> BuilderClientService<T> newRestClient(Class<T> clazzClient, String basePath) {
		return restClientBuilder.from(clazzClient).setBasePath(basePath);
	}
}
