package it.tndigitale.a4g.a4gutente.business.service.client;

import it.tndigitale.a4g.fascicolo.anagrafica.client.api.FascicoloDetenzioneAutonomaControllerApi;
import it.tndigitale.a4g.fascicolo.anagrafica.client.model.FascicoloCreationResultDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DetenzioneAutonomaClient extends AbstractClient {

	@Value("${it.tndigit.a4g.fascicolo.utente.fascicolo.anagrafica.url}")
	private String urlAnagrafica;
	
	private FascicoloDetenzioneAutonomaControllerApi getFascicoloDetenzioneAutonomaControllerApi() {
		return restClientProxy(FascicoloDetenzioneAutonomaControllerApi.class, urlAnagrafica);
	}
	
	public FascicoloCreationResultDto apri(String cuaa) {
		return getFascicoloDetenzioneAutonomaControllerApi().apriUsingPOST1(cuaa);
	}
}
