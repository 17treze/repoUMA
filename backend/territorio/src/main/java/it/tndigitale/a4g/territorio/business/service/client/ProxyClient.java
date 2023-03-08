package it.tndigitale.a4g.territorio.business.service.client;

import it.tndigitale.a4g.proxy.client.api.SianControllerApi;
import it.tndigitale.a4g.proxy.client.model.ConduzioneDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProxyClient extends AbstractClient {
	@Value("${it.tndigit.a4g.client.proxy.url}")
    private String urlProxy;

    public List<ConduzioneDto> leggiConsistenzaUsingGET(String cuaa) {
        return getSianControllerApi().leggiConsistenzaUsingGET(cuaa);
    }

	private SianControllerApi getSianControllerApi() {
		return restClientProxy(SianControllerApi.class, urlProxy);
	}
}