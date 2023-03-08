package it.tndigitale.a4g.fascicolo.mediator.business.service.client;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.fascicolo.anagrafica.client.api.FascicoloControllerApi;
import it.tndigitale.a4g.fascicolo.anagrafica.client.api.FascicoloPrivateControllerApi;
import it.tndigitale.a4g.fascicolo.anagrafica.client.model.FascicoloDto;
import it.tndigitale.a4g.fascicolo.anagrafica.client.model.ReportValidazioneDto;
import it.tndigitale.a4g.fascicolo.mediator.dto.EsitoControlloDto;
import it.tndigitale.a4g.fascicolo.mediator.dto.TipoDetenzioneEnum;

@Component
public class AnagraficaPrivateClient extends AbstractClient {

	@Value("${it.tndigit.a4g.fascicolo.mediator.fascicolo.anagrafica.url}") private String urlAnagrafica;

	public Map<String, EsitoControlloDto> queryControlloCompletezzaFascicolo(final String cuaa) {

		Map<String, it.tndigitale.a4g.fascicolo.anagrafica.client.model.EsitoControlloDto> resultMap = getAnagraficaPrivateControllerApi().queryControlloCompletezzaFascicoloUsingGET(cuaa);
		var resultToReturn = new HashMap<String, EsitoControlloDto>();

		for (var k : resultMap.keySet()) {
			var esitoControlloDto = new EsitoControlloDto();
			BeanUtils.copyProperties(resultMap.get(k), esitoControlloDto);
			resultToReturn.put(k, esitoControlloDto);
		}
		return resultToReturn;
	}

	public Map<String, EsitoControlloDto>  controlloCompletezzaFascicoloSincronoUsingGET(final String cuaa) {
		Map<String, it.tndigitale.a4g.fascicolo.anagrafica.client.model.EsitoControlloDto> resultMap = getAnagraficaPrivateControllerApi().controlloCompletezzaFascicoloSincronoUsingGET(cuaa);
		var resultToReturn = new HashMap<String, EsitoControlloDto>();

		for (var k : resultMap.keySet()) {
			var esitoControlloDto = new EsitoControlloDto();
			BeanUtils.copyProperties(resultMap.get(k), esitoControlloDto);
			resultToReturn.put(k, esitoControlloDto);
		}
		return resultToReturn;
	}

	public List<FascicoloDto> getElencoFascicoliInStatoControlliInCorso() {
		return getAnagraficaPrivateControllerApi().getElencoFascicoliInStatoControlliInCorsoUsingGET();
	}

	public void rimozioneControlliCompletezza(final String cuaa) {
		getAnagraficaPrivateControllerApi().rimozioneControlliCompletezzaUsingDELETE(cuaa);
	}

	public void putFascicoloStatoControlliInCorsoUsingPUT(final String cuaa) {
		getAnagraficaPrivateControllerApi().putFascicoloStatoControlliInCorsoUsingPUT(cuaa);
	}

	public void putFascicoloStatoControlliInAggiornamentoUsingPUT(final String cuaa) {
		getAnagraficaPrivateControllerApi().putFascicoloStatoControlliInAggiornamentoUsingPUT(cuaa);
	}

	public void putFascicoloStatoControllatoOkUsingPUT(final String cuaa) {
		getAnagraficaPrivateControllerApi().putFascicoloStatoControllatoOkUsingPUT(cuaa);
	}

	public void startControlloCompletezzaFascicoloAsincronoUsingPUT(final String cuaa) {
		getAnagraficaPrivateControllerApi().startControlloCompletezzaFascicoloAsincronoUsingPUT(cuaa);
	}

	public void startValidazioneFascicoloAsincronaUsingPOST(final String cuaa, final Integer idValidazione, final String username) {
		getAnagraficaPrivateControllerApi().validazioneFascicoloAsincronaUsingPOST(cuaa, idValidazione, username);
	}

	public void sincronizzazioneAgsUsingPOST(final String cuaa, final Integer idValidazione) {
		getAnagraficaPrivateControllerApi().sincronizzazioneAgsUsingPOST(cuaa, idValidazione);
	}

	public void startValidazioneFascicoloAutonomoAsincronaUsingPOST(final String cuaa, final Integer idValidazione, final String username) {
		getAnagraficaPrivateControllerApi().validazioneFascicoloAutonomoAsincronaUsingPOST(cuaa, idValidazione, username);
	}

	public void protocollaSchedaValidazioneUsingPOST(final String cuaa, final File report, final List< File > allegati, final Integer nextIdValidazione, final TipoDetenzioneEnum tipoDetenzioneEnum) {
		getAnagraficaPrivateControllerApi().protocollaSchedaValidazioneUsingPOST(cuaa, report, tipoDetenzioneEnum.name(), allegati, nextIdValidazione);
	}

	public void notificaMailCaaRichiestaValidazioneAccettataUsingPOST(final String cuaa) {
		getAnagraficaPrivateControllerApi().notificaMailCaaRichiestaValidazioneAccettataUsingPOST(cuaa);
	}

	public void notificaMailCaaSchedaValidazioneAccettataUsingPOST(final String cuaa) {
		getAnagraficaPrivateControllerApi().notificaMailCaaSchedaValidazioneAccettataUsingPOST(cuaa);
	}

	public Boolean checkLetturaFascicolo(final String cuaa) {
		return getAnagraficaControllerApi().checkLetturaFascicoloUsingGET(cuaa, 0);
	}

	public Boolean checkAperturaFascicolo(final String cuaa) {
		return getAnagraficaControllerApi().checkAperturaFascicoloUsingGET(cuaa, 0);
	}

	public void aggiornaStatoFascicoloAllaFirmaCaa(String cuaa, Long idSchedaValidazione, File schedaValidazione) {
		getAnagraficaPrivateControllerApi().putFascicoloStatoAllaFirmaCaaUsingPUT(cuaa, idSchedaValidazione, schedaValidazione);
	}

	public ReportValidazioneDto getReportValidazione(String cuaa) {
		return getAnagraficaPrivateControllerApi().getReportSchedaValidazioneUsingGET(cuaa);
	}

	private FascicoloPrivateControllerApi getAnagraficaPrivateControllerApi() {
		return restClientProxy(FascicoloPrivateControllerApi.class, urlAnagrafica);
	}

	private FascicoloControllerApi getAnagraficaControllerApi() {
		return restClientProxy(FascicoloControllerApi.class, urlAnagrafica);
	}

	public Boolean isAventeDirittoFirmaOrCaaUsingGET(final String cuaa) {
		return getAnagraficaPrivateControllerApi().isAventeDirittoFirmaOrCaaUsingGET(cuaa);
	}

	public FascicoloDto getByCuaaUsingGET1(final String cuaa, final Integer idValidazione) {
//		il client di anagrafica ha template (non templatev3 che supporta metodi con suffisso WithHttpInfo)
//		ritorna null se non c'e' niente, un valore se http-200 oppure RestClientException in caso di errori 4xx e 5xx
		return getAnagraficaPrivateControllerApi().getByCuaaUsingGET1 (cuaa, idValidazione);
	}
	
	public Long getNewIdSchedaValidazioneUsingGET() {
		return getAnagraficaPrivateControllerApi().getNewIdSchedaValidazioneUsingGET();
	}

	public void annullaIterValidazione(final String cuaa) {
		getAnagraficaPrivateControllerApi().annullaIterValidazioneUsingPUT(cuaa);
	}

}
