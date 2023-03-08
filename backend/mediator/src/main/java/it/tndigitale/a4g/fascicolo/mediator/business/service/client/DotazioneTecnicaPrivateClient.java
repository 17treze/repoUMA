package it.tndigitale.a4g.fascicolo.mediator.business.service.client;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.client.api.DotazioneTecnicaPrivateControllerApi;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.client.model.ReportValidazioneDto;
import it.tndigitale.a4g.fascicolo.mediator.dto.EsitoControlloDto;

@Component
public class DotazioneTecnicaPrivateClient extends AbstractClient {
	@Value("${it.tndigit.a4g.fascicolo.mediator.fascicolo.dotazionetecnica.url}") private String urlDotazioneTecnica;

	public ResponseEntity<Void> startValidazioneFascicolo(final String cuaa, final Integer idValidazione) {
		return getDotazioneTecnicaPrivateControllerApi().startValidazioneFascicoloWithHttpInfo(cuaa, idValidazione);
	}

	public ResponseEntity<Void> migraMacchinari(final String cuaa) {
		return getDotazioneTecnicaPrivateControllerApi().migraMacchineWithHttpInfo(cuaa);
	}

	public ResponseEntity<Void> migraFabbricati(final String cuaa) {
		return getDotazioneTecnicaPrivateControllerApi().migraFabbricatiWithHttpInfo(cuaa);
	}

	public Long postMacchina(final String cuaa, final String dati, final File documento) {
		return getDotazioneTecnicaPrivateControllerApi().postMacchina(cuaa, documento, dati);
	}

	public void deleteMacchina(final String cuaa, final Long id) {
		getDotazioneTecnicaPrivateControllerApi().cancellaMacchina(cuaa, id);
	}

	public ReportValidazioneDto getReportValidazione(String cuaa) {
		return getDotazioneTecnicaPrivateControllerApi().getReportValidazione(cuaa);
	}
	
	public void startControlloCompletezzaFascicoloAsincrono(final String cuaa) {
		getDotazioneTecnicaPrivateControllerApi().startControlloCompletezzaFascicoloAsincrono(cuaa);
	}
	public void rimozioneControlliCompletezza(final String cuaa) {
		getDotazioneTecnicaPrivateControllerApi().rimozioneControlliCompletezza(cuaa);
	}
	
	public Map<String, EsitoControlloDto> queryControlloCompletezzaFascicolo(final String cuaa) {
		var resultMap = getDotazioneTecnicaPrivateControllerApi().queryControlloCompletezzaFascicolo(cuaa);
		var resultToReturn = new HashMap<String, EsitoControlloDto>();

		for (var k : resultMap.keySet()) {
			var esitoControlloDto = new EsitoControlloDto();
			BeanUtils.copyProperties(resultMap.get(k), esitoControlloDto);
			resultToReturn.put(k, esitoControlloDto);
		}
		return resultToReturn;
	}
	
	private DotazioneTecnicaPrivateControllerApi getDotazioneTecnicaPrivateControllerApi() {
		return restClientProxy(DotazioneTecnicaPrivateControllerApi.class, urlDotazioneTecnica);
	}
}
