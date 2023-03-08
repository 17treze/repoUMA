package it.tndigitale.a4g.uma.business.service.client;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.client.api.FabbricatiAgsControllerApi;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.client.api.MacchineAgsControllerApi;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.client.model.FabbricatoAgsDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.client.model.MacchinaAgsDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.client.model.TipoCarburante;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.client.model.TitoloConduzioneAgs;

@Component
public class UmaDotazioneTecnicaClient extends AbstractClient {

	@Value("${it.tndigit.a4g.uma.fascicolo.dotazionetecnica.url}")
	private String urlDotazioneTecnica;

	// macchine
	public List<MacchinaAgsDto> getMacchine(String cuaa , LocalDateTime data) {
		List<TipoCarburante> carburanti = Arrays.asList(TipoCarburante.BENZINA, TipoCarburante.GASOLIO);
		return this.getMacchinaControllerApi().getMacchine1(cuaa, data, carburanti);
	}

	// reperisce fabbricati in affitto-proprietà-conduzione in provincia di TN e BZ 
	public List<FabbricatoAgsDto> getFabbricati(String cuaa, LocalDateTime data) {
		List<TitoloConduzioneAgs> titoliConduzione = Arrays.asList(TitoloConduzioneAgs.AFFITTO, TitoloConduzioneAgs.PROPRIETA, TitoloConduzioneAgs.COMODATO);
		return this.getFabbricatiControllerApi().getFabbricati(cuaa, data, null, titoliConduzione);
	}

	// Get Controller 
	private MacchineAgsControllerApi getMacchinaControllerApi() {
		return restClientProxy(MacchineAgsControllerApi.class, urlDotazioneTecnica);
	}

	private FabbricatiAgsControllerApi getFabbricatiControllerApi() {
		return restClientProxy(FabbricatiAgsControllerApi.class, urlDotazioneTecnica);
	}
}
