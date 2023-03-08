package it.tndigitale.a4g.fascicolo.mediator.business.service.client;

import it.tndigitale.a4g.fascicolo.mediator.dto.EsitoControlloDto;
import it.tndigitale.a4g.fascicolo.territorio.client.api.FascicoliPrivateControllerApi;
import it.tndigitale.a4g.fascicolo.territorio.client.model.ReportValidazioneTerreniAgsDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TerritorioPrivateClient extends AbstractClient {
	@Value("${it.tndigit.a4g.fascicolo.mediator.fascicolo.territorio.url}") private String url;

	private FascicoliPrivateControllerApi getFascicoliPrivateControllerApi() {
		return restClientProxy(FascicoliPrivateControllerApi.class, url);
	}

	public void rimozioneControlliCompletezza(final String cuaa) {
		getFascicoliPrivateControllerApi().rimozioneControlliCompletezza(cuaa);
	}
	
	public Map<String, EsitoControlloDto> queryControlloCompletezzaFascicolo(final String cuaa) {
		Map<String, it.tndigitale.a4g.fascicolo.territorio.client.model.EsitoControlloDto> resultMap = getFascicoliPrivateControllerApi().queryControlloCompletezzaFascicolo(cuaa);
		var resultToReturn = new HashMap<String, it.tndigitale.a4g.fascicolo.mediator.dto.EsitoControlloDto>();

		for (var k : resultMap.keySet()) {
			var esitoControlloDto = new EsitoControlloDto();
			BeanUtils.copyProperties(resultMap.get(k), esitoControlloDto);
			resultToReturn.put(k, esitoControlloDto);
		}
		return resultToReturn;
	}

	public Map<String, EsitoControlloDto>  controlloCompletezzaFascicoloSincronoUsingGET(final String cuaa) {
		Map<String, it.tndigitale.a4g.fascicolo.territorio.client.model.EsitoControlloDto> resultMap = getFascicoliPrivateControllerApi().controlloCompletezzaFascicoloSincrono(cuaa);
		var resultToReturn = new HashMap<String, EsitoControlloDto>();

		for (var k : resultMap.keySet()) {
			var esitoControlloDto = new EsitoControlloDto();
			BeanUtils.copyProperties(resultMap.get(k), esitoControlloDto);
			resultToReturn.put(k, esitoControlloDto);
		}
		return resultToReturn;
	}

	public void startControlloCompletezzaFascicoloAsincrono(final String cuaa) {
		getFascicoliPrivateControllerApi().startControlloCompletezzaFascicoloAsincrono(cuaa);
	}

	public ReportValidazioneTerreniAgsDto getReportValidazione(String cuaa) {
		return getFascicoliPrivateControllerApi().getReportValidazione(cuaa);
	}
}
