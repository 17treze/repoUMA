package it.tndigitale.a4g.zootecnia.business.service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.fascicolo.anagrafica.client.api.FascicoloControllerApi;
import it.tndigitale.a4g.fascicolo.anagrafica.client.model.FascicoloDto;

@Component
public class FascicoloAnagraficaClient extends AbstractClient {
	@Value("${it.tndigit.fascicolo.anagrafica.url}")
		
    private String urlAnagrafica;

	public Boolean checkAperturaFascicolo(String cuaa) {
		return this.getFascicoloControllerApi().checkAperturaFascicoloUsingGET(cuaa, 0);
	}
	
	public Boolean checkLetturaFascicolo(String cuaa) {
		return this.getFascicoloControllerApi().checkLetturaFascicoloUsingGET(cuaa, 0);
	}

	public FascicoloDto getFascicoloLive(String cuaa) {
		return this.getFascicoloControllerApi().getByCuaaUsingGET(cuaa, 0);
	}

	private FascicoloControllerApi getFascicoloControllerApi() {
		return restClientProxy(FascicoloControllerApi.class, urlAnagrafica);
	}
}