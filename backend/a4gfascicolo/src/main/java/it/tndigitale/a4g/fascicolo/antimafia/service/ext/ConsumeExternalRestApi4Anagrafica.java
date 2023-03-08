package it.tndigitale.a4g.fascicolo.antimafia.service.ext;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.fascicolo.anagrafica.client.api.FascicoloAgsControllerApi;
import it.tndigitale.a4g.fascicolo.anagrafica.client.model.CaricaAgsDto;
import it.tndigitale.a4g.fascicolo.anagrafica.client.model.FascicoloAgsDto;

@Component
public class ConsumeExternalRestApi4Anagrafica extends ConsumeExternalRestApiAbstract {

	@Value("${anagrafica.client.uri}")
	private String urlAnagrafica;
	
	private FascicoloAgsControllerApi getFascicoloControllerApi() {
		return restClientProxy(FascicoloAgsControllerApi.class,urlAnagrafica);
	}
	public List<CaricaAgsDto> getSoggettiFascicoloAziendale(String cuaa) {
		return getFascicoloControllerApi().getSoggettiFascicoloAziendaleUsingGET(cuaa);
	}
	public FascicoloAgsDto getFascicolo(String cuaa) {
		return this.getFascicoloControllerApi().getFascicoloUsingGET(cuaa);
	}
}
