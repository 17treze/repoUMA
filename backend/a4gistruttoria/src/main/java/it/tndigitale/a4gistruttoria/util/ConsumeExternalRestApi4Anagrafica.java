package it.tndigitale.a4gistruttoria.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.fascicolo.anagrafica.client.api.FascicoloAgsControllerApi;
import it.tndigitale.a4g.fascicolo.anagrafica.client.model.CaricaAgsDto;


@Component
public class ConsumeExternalRestApi4Anagrafica extends ConsumeExternalRestApiAbstract {

	@Value("${a4gistruttoria.anagrafica.client.uri}")
	private String urlAnagrafica;
	
	private FascicoloAgsControllerApi getFascicoloControllerApi() {
		return restClientProxyNew(FascicoloAgsControllerApi.class, urlAnagrafica);
	}
	public List<CaricaAgsDto> getSoggettiFascicoloAziendale(String cuaa) {
		return getFascicoloControllerApi().getSoggettiFascicoloAziendaleUsingGET(cuaa);
	}	
}
