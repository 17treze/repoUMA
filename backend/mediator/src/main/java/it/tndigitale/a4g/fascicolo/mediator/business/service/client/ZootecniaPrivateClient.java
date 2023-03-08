package it.tndigitale.a4g.fascicolo.mediator.business.service.client;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.fascicolo.mediator.dto.EsitoControlloDto;
import it.tndigitale.a4g.fascicolo.zootecnia.client.api.ZootecniaPrivateControllerApi;
import it.tndigitale.a4g.fascicolo.zootecnia.client.model.ReportValidazioneDto;

@Component
public class ZootecniaPrivateClient extends AbstractClient {
	@Value("${it.tndigit.a4g.fascicolo.mediator.fascicolo.zootecnia.url}") private String urlZootecnia;

	public Map<String, EsitoControlloDto> queryControlloCompletezzaFascicolo(final String cuaa) {
		Map<String, it.tndigitale.a4g.fascicolo.zootecnia.client.model.EsitoControlloDto> resultMap = getZootecniaPrivateControllerApi().queryControlloCompletezzaFascicolo(cuaa);
		var resultToReturn = new HashMap<String, EsitoControlloDto>();

		for (var k : resultMap.keySet()) {
			var esitoControlloDto = new EsitoControlloDto();
			BeanUtils.copyProperties(resultMap.get(k), esitoControlloDto);
			resultToReturn.put(k, esitoControlloDto);
		}
		return resultToReturn;
	}

	public Map<String, EsitoControlloDto>  controlloCompletezzaFascicoloSincronoUsingGET(final String cuaa) {
		Map<String, it.tndigitale.a4g.fascicolo.zootecnia.client.model.EsitoControlloDto> resultMap = getZootecniaPrivateControllerApi().controlloCompletezzaFascicoloSincrono(cuaa);
		var resultToReturn = new HashMap<String, EsitoControlloDto>();

		for (var k : resultMap.keySet()) {
			var esitoControlloDto = new EsitoControlloDto();
			BeanUtils.copyProperties(resultMap.get(k), esitoControlloDto);
			resultToReturn.put(k, esitoControlloDto);
		}
		return resultToReturn;
	}

	public void rimozioneControlliCompletezza(final String cuaa) {
		getZootecniaPrivateControllerApi().rimozioneControlliCompletezza(cuaa);
	}

	public void startControlloCompletezzaFascicoloAsincrono(final String cuaa) {
		getZootecniaPrivateControllerApi().startControlloCompletezzaFascicoloAsincrono(cuaa);
	}

	public ReportValidazioneDto getReportValidazione(String cuaa) {
		return getZootecniaPrivateControllerApi().getReportValidazione(cuaa);
	}

	private ZootecniaPrivateControllerApi getZootecniaPrivateControllerApi() {
		return restClientProxy(ZootecniaPrivateControllerApi.class, urlZootecnia);
	}
	
	public ResponseEntity<Void> sincronizzazioneAgsWithHttpInfo(final String cuaa, final Integer idValidazione) {
		return getZootecniaPrivateControllerApi().sincronizzazioneAgsWithHttpInfo(cuaa, idValidazione);
	}

	public ResponseEntity<Void> startValidazioneFascicolo(final String cuaa, final Integer idValidazione) {
		return getZootecniaPrivateControllerApi().startValidazioneFascicoloWithHttpInfo(cuaa, idValidazione);
	}
	
	public ResponseEntity<Void> aggiornaAllevamenti(final String cuaa, final LocalDate dataRichiesta) {
		return getZootecniaPrivateControllerApi().aggiornaAllevamenti1WithHttpInfo(cuaa, dataRichiesta);
	}
}
