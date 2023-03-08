package it.tndigitale.a4gistruttoria.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.ags.client.api.AntimafiaRestControllerApi;
import it.tndigitale.a4g.ags.client.api.DomandeRestControllerApi;
import it.tndigitale.a4g.ags.client.model.EsitoAntimafia;

@Component
public class ConsumeExternalRestApi4Ags extends ConsumeExternalRestApiAbstract {

	@Value("${a4gistruttoria.ags.uri}")
	private String urlAgs;
	
	public Boolean spostaDomandaInProtocollata(Long numeroDomanda) {
		Boolean protocollata = getAgsDomandeRestController().spostaInProtocollatoUsingPOST(numeroDomanda);
		if (!protocollata) throw new RuntimeException("Errore AGS. Non è stato possibile l'annullo della domanda in AGS");
		return protocollata;
	}
	
	// TODO: refactor this. 
	private DomandeRestControllerApi getAgsDomandeRestController() {
		return restClientProxy(DomandeRestControllerApi.class,urlAgs);
	}
	
	private AntimafiaRestControllerApi getAntimafiaRestControllerApi() {
		return restClientProxyNew(AntimafiaRestControllerApi.class,getUrlSenzaVersione());
	}
	public void sincronizzaEsitiAntimafiaAgs(List<EsitoAntimafia> esitiAntimafia) {
		getAntimafiaRestControllerApi().salvaUsingPUT(esitiAntimafia);
	}
	
	private String getUrlSenzaVersione() {
		return urlAgs.replace("/api/v1", "");
	}
}
