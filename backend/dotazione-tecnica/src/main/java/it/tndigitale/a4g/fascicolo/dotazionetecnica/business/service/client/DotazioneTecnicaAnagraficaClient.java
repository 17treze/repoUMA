package it.tndigitale.a4g.fascicolo.dotazionetecnica.business.service.client;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.TipologiaParticellaCatastale;
import it.tndigitale.a4g.proxy.client.api.CatastoControllerApi;
import it.tndigitale.a4g.proxy.client.model.InformazioniImmobileDto;
import it.tndigitale.a4g.proxy.client.model.InformazioniParticellaDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.fascicolo.anagrafica.client.api.FascicoloPrivateControllerApi;

import java.util.List;

@Component
public class DotazioneTecnicaAnagraficaClient extends AbstractClient {

	@Value("${it.tndigit.a4g.dotazionetecnica.fascicolo.anagrafica.url}")
	private String urlAnagrafica;

	@Value("${it.tndigit.a4g.dotazionetecnica.a4gproxy.url}")
	private String urlProxy;

	// Methods from Controllers
	public void putFascicoloStatoControlliInAggiornamentoUsingPUT(final String cuaa) {
		getAnagraficaPrivateControllerApi().putFascicoloStatoControlliInAggiornamentoUsingPUT(cuaa);
	}
	// Get Controller 
	private FascicoloPrivateControllerApi getAnagraficaPrivateControllerApi() {
		return restClientProxy(FascicoloPrivateControllerApi.class, urlAnagrafica);
	}

	public InformazioniImmobileDto getInfoImmobile(String numeroParticella, Integer codiceComuneCatastale, Integer subalterno) {
		 return getCatastoControllerApi().getInfoImmobileUsingGET(numeroParticella, codiceComuneCatastale, subalterno);
	}

	public InformazioniParticellaDto getInfoParticella(String numeroParticella, TipologiaParticellaCatastale tipologia, Integer codiceComuneCatastale) {
		return getCatastoControllerApi().getInfoParticellaUsingGET (numeroParticella, tipologia.value(), codiceComuneCatastale);
	}
	public List<String> getElencoSubalterniParticella(String numeroParticella, Integer codiceComuneCatastale) {
		return getCatastoControllerApi().getElencoSubalterniParticellaUsingGET(numeroParticella, codiceComuneCatastale);
	}

	private CatastoControllerApi getCatastoControllerApi() {
		return restClientProxy(CatastoControllerApi.class, urlProxy);
	}
}
