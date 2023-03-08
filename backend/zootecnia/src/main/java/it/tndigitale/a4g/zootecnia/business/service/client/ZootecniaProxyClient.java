package it.tndigitale.a4g.zootecnia.business.service.client;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.framework.client.DefaultUrlMicroService;
import it.tndigitale.a4g.proxy.client.api.AnagrafeZootecnicaControllerApi;
import it.tndigitale.a4g.proxy.client.model.AnagraficaAllevamentoDto;

@Component
public class ZootecniaProxyClient extends AbstractClient {

	@Value(DefaultUrlMicroService.PROXY_URL)
	private String serverProxyPath;
	

	public List<AnagraficaAllevamentoDto> getAnagraficaAllevamenti(String cuaa, LocalDate dataRichiesta) {
		return getAnagrafeZootecnicaControllerApi().getAllevamentiDetenutiUsingGET(cuaa, dataRichiesta);
	}
	
	private AnagrafeZootecnicaControllerApi getAnagrafeZootecnicaControllerApi() {
		return restClientProxy(AnagrafeZootecnicaControllerApi.class, serverProxyPath);
	}
}
