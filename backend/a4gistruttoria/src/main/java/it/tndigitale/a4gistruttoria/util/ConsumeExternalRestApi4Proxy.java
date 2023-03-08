package it.tndigitale.a4gistruttoria.util;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.proxy.client.api.AnagrafeZootecnicaControllerApi;
import it.tndigitale.a4g.proxy.client.model.ConsistenzaUbaOviniDto;

@Component
public class ConsumeExternalRestApi4Proxy extends ConsumeExternalRestApiAbstract {

	@Value("${a4gistruttoria.proxy.baseurl}")
	private String urlProxy;

	public List<ConsistenzaUbaOviniDto> getConsistenzaUbaOvini(String cuaa, Integer campagna) {
		var primoGennaio = LocalDate.of(campagna, 1, 1);
		var trentunoDicembre = LocalDate.of(campagna, 12, 31);
		return getAnagrafeZootecnicaControllerApi().getConsistenzaUbaOviniUsingGET(cuaa, primoGennaio, trentunoDicembre);
	}	

	private AnagrafeZootecnicaControllerApi getAnagrafeZootecnicaControllerApi() {
		return restClientProxyNew(AnagrafeZootecnicaControllerApi.class, urlProxy);
	}
}
