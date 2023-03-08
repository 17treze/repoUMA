package it.tndigitale.a4g.fascicolo.antimafia.service.ext;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.framework.client.DefaultUrlMicroService;
import it.tndigitale.a4g.framework.client.RestClientBuilder;
import it.tndigitale.a4g.proxy.client.api.EsitoAntimafiaControllerApi;
import it.tndigitale.a4g.proxy.client.model.AntimafiaEsitoDto;

@Component
public class ConsumeExternalRestApi4Proxy extends ConsumeExternalRestApiAbstract {

	@Value(DefaultUrlMicroService.PROXY_URL)
	private String urlProxy;
	@Autowired
	protected RestClientBuilder restClientBuilder;

	private EsitoAntimafiaControllerApi getEsitoAntimafiaControllerApi() {
		return restClientBuilder.from(EsitoAntimafiaControllerApi.class)
			      .setBasePath(urlProxy)
			      .newInstance();
	}

	public AntimafiaEsitoDto getEsitoAntimafia(String cuaa) {
		return getEsitoAntimafiaControllerApi().getEsitoAntimafiaUsingGET(cuaa);
	}
	
	public List<AntimafiaEsitoDto> getEsitiAntimafia(List<String> cuaaList) {
		return getEsitoAntimafiaControllerApi().getEsitiAntimafiaUsingGET(cuaaList);
	}
	
}