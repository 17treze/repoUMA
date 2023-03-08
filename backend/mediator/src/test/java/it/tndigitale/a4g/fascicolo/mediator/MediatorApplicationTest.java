package it.tndigitale.a4g.fascicolo.mediator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.fascicolo.anagrafica.client.model.FascicoloDto;
import it.tndigitale.a4g.fascicolo.anagrafica.client.model.FascicoloDto.StatoEnum;
import it.tndigitale.a4g.fascicolo.mediator.api.ApiUrls;
import it.tndigitale.a4g.fascicolo.mediator.business.service.client.AnagraficaPrivateClient;
import it.tndigitale.a4g.fascicolo.mediator.business.service.client.DotazioneTecnicaPrivateClient;
import it.tndigitale.a4g.fascicolo.mediator.business.service.client.TerritorioPrivateClient;
import it.tndigitale.a4g.fascicolo.mediator.business.service.client.ZootecniaPrivateClient;
import it.tndigitale.a4g.fascicolo.mediator.business.service.event.StartValidazioneFascicoloEvent;
import it.tndigitale.a4g.fascicolo.mediator.business.service.fascicolo.FascicoloValidazioneService;
import it.tndigitale.a4g.fascicolo.mediator.dto.EsitoControlloDto;
import it.tndigitale.a4g.fascicolo.mediator.dto.SchedaValidazioneFascicoloDto;
import it.tndigitale.a4g.fascicolo.mediator.dto.TipoDetenzioneEnum;
import it.tndigitale.a4g.framework.event.store.EventStoreService;
import it.tndigitale.a4g.framework.event.store.EventStoredModel;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class MediatorApplicationTest {
	@Autowired MockMvc mockMvc;
	@Autowired EventStoreService eventStoreService;
	@Autowired FascicoloValidazioneService fascicoloStoricizzazioneService;
	@Autowired ObjectMapper objectMapper;

	@MockBean ZootecniaPrivateClient zootecniaPrivateClient;
	@MockBean TerritorioPrivateClient territorioPrivateClient;
	@MockBean AnagraficaPrivateClient anagraficaPrivateClient;
	@MockBean DotazioneTecnicaPrivateClient dotazioneTecnicaPrivateClient;

	private void mockControlliCompletezzaOkStuff() {

		String cuaa = "BRNGTN67E05D086L";
		var anagraficaResponse = new HashMap<String, EsitoControlloDto>();
		var territorioResponse = new HashMap<String, EsitoControlloDto>();
		var zootecniaResponse = new HashMap<String, EsitoControlloDto>();
		var esitoOk = new EsitoControlloDto();
		esitoOk.setEsito(0);
		var esitoKo = new EsitoControlloDto();
		esitoKo.setEsito(-3);
		zootecniaResponse.put("IS_AGGIORNAMENTO_FONTI_ESTERNE_ZOOTECNIA", esitoOk);

		anagraficaResponse.put("IS_AGGIORNAMENTO_FONTI_ESTERNE_ANAGRAFICA", esitoKo);
		anagraficaResponse.put("IS_NOT_REVOCA_IN_CORSO", esitoOk);
		anagraficaResponse.put("IS_AGGIORNAMENTO_FONTI_ESTERNE_ZOOTECNIA", esitoKo);
		anagraficaResponse.put("IS_MODALITA_PAGAMENTO_PRESENTE", esitoKo);
		anagraficaResponse.put("IS_IN_AGGIORNAMENTO", esitoKo);
		anagraficaResponse.put("IS_CONTROLLI_IN_CORSO", esitoOk);
		Mockito.when(anagraficaPrivateClient.queryControlloCompletezzaFascicolo(cuaa)).thenReturn(anagraficaResponse);
		Mockito.when(zootecniaPrivateClient.queryControlloCompletezzaFascicolo(cuaa)).thenReturn(zootecniaResponse);
		Mockito.when(territorioPrivateClient.queryControlloCompletezzaFascicolo(cuaa)).thenReturn(territorioResponse);
		Mockito.when(anagraficaPrivateClient.checkLetturaFascicolo(cuaa)).thenReturn(true);
		FascicoloDto fascicoloDto = new FascicoloDto();
		fascicoloDto.setStato(StatoEnum.CONTROLLATO_OK);
		Mockito.when(anagraficaPrivateClient.getByCuaaUsingGET1(cuaa, 0)).thenReturn(fascicoloDto);


	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	void getControlloCompletezza() throws Exception {
		mockControlliCompletezzaOkStuff();
		String cuaa = "BRNGTN67E05D086L";

		ResultActions resultActions = mockMvc.perform(get(String.format(ApiUrls.FASCICOLO + "/%s/controllo-completezza", cuaa))
				.contentType(MediaType.APPLICATION_JSON));

		var esitoOk = new EsitoControlloDto();
		esitoOk.setEsito(0);

		var esitoKo = new EsitoControlloDto();
		esitoKo.setEsito(-3);

		resultActions.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(jsonPath("$['IS_AGGIORNAMENTO_FONTI_ESTERNE_ANAGRAFICA']['esito']", Matchers.is(Integer.valueOf(-3))))
		.andExpect(jsonPath("$['IS_NOT_REVOCA_IN_CORSO']['esito']", Matchers.is(Integer.valueOf(0))))
		.andExpect(jsonPath("$['IS_AGGIORNAMENTO_FONTI_ESTERNE_ZOOTECNIA']['esito']", Matchers.is(Integer.valueOf(0))))
		.andExpect(jsonPath("$['IS_MODALITA_PAGAMENTO_PRESENTE']['esito']", Matchers.is(Integer.valueOf(-3))))
		.andExpect(jsonPath("$['IS_IN_AGGIORNAMENTO']['esito']", Matchers.is(Integer.valueOf(0))));
		//		.andExpect(jsonPath("$['IS_CONTROLLI_IN_CORSO']['esito']", Matchers.is(Integer.valueOf(0))));
	}

	private MockMultipartFile createMockMultipartFile() throws IOException {
		Path path = Paths.get("src/test/resources/fascicolo/MANDATO_ftoDPDNDR77B03L378L.pdf");
		String name = "MANDATO_ftoDPDNDR77B03L378L";
		return new MockMultipartFile(name, Files.readAllBytes(path));
	}


	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	void validazioneMandato_OK() throws Exception {
		String cuaa = "BRNGTN67E05D086L";
		String endpoint = String.format("/%s/validazione-mandato", cuaa);
		MockMultipartFile schedaValidazioneFirmata = createMockMultipartFile();

		//		TODO creare mock per:
		//				FascicoloDto fascicoloDto = anagraficaPrivateClient.getByCuaaUsingGET1(cuaa, 0);
		//anagraficaPrivateClient.startValidazioneFascicoloAsincronaUsingPOST(cuaa, idValidazione, event.getUsername());
		//anagraficaPrivateClient.protocollaSchedaValidazioneUsingPOST(cuaa, report, allegati,nextIdValidazione, tipoDetenzioneEnum);
		//anagraficaPrivateClient.sincronizzazioneAgsUsingPOST(cuaa, idValidazione);
		//anagraficaPrivateClient.startValidazioneFascicoloAutonomoAsincronaUsingPOST(cuaa, idValidazione, event.getUsername());
		//anagraficaPrivateClient.notificaMailCaaRichiestaValidazioneAccettataUsingPOST(cuaa);
		//
		//ResponseEntity<Void> startValidazioneFascicolo = zootecniaPrivateClient.startValidazioneFascicolo(cuaa, idValidazione);
		//ResponseEntity<Void> sincronizzazioneAgsEsito = zootecniaPrivateClient.sincronizzazioneAgsWithHttpInfo(cuaa, idValidazione);

		FascicoloDto fascicoloDto = new FascicoloDto();
		fascicoloDto.setCuaa(cuaa);
		fascicoloDto.setStato(StatoEnum.IN_VALIDAZIONE);

		Mockito.when(anagraficaPrivateClient.getByCuaaUsingGET1(Mockito.any(String.class), Mockito.any(Integer.class))).thenReturn(fascicoloDto);

		Mockito.doNothing().when(anagraficaPrivateClient).startValidazioneFascicoloAsincronaUsingPOST(Mockito.isA(String.class), Mockito.isA(Integer.class), Mockito.isA(String.class));
		Mockito.doNothing().when(anagraficaPrivateClient).protocollaSchedaValidazioneUsingPOST(Mockito.isA(String.class), Mockito.any(), Mockito.any(), Mockito.isA(Integer.class), Mockito.isA(TipoDetenzioneEnum.class));
		Mockito.doNothing().when(anagraficaPrivateClient).sincronizzazioneAgsUsingPOST(Mockito.isA(String.class), Mockito.isA(Integer.class));
		Mockito.doNothing().when(anagraficaPrivateClient).startValidazioneFascicoloAutonomoAsincronaUsingPOST(Mockito.isA(String.class), Mockito.isA(Integer.class), Mockito.isA(String.class));
		Mockito.doNothing().when(anagraficaPrivateClient).notificaMailCaaRichiestaValidazioneAccettataUsingPOST(Mockito.isA(String.class));

		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		ResponseEntity<Void> responseEntity = new ResponseEntity<Void>(
				header,
				HttpStatus.OK
				);

		Mockito.when(zootecniaPrivateClient.startValidazioneFascicolo(Mockito.any(String.class), Mockito.any(Integer.class))).thenReturn(responseEntity);
		Mockito.when(zootecniaPrivateClient.sincronizzazioneAgsWithHttpInfo(Mockito.any(String.class), Mockito.any(Integer.class))).thenReturn(responseEntity);
		Mockito.when(dotazioneTecnicaPrivateClient.startValidazioneFascicolo(Mockito.any(String.class), Mockito.any(Integer.class))).thenReturn(responseEntity);

		Mockito.when(anagraficaPrivateClient.isAventeDirittoFirmaOrCaaUsingGET(Mockito.any(String.class))).thenReturn(true);


		MockHttpServletRequestBuilder requestBuilder =
				MockMvcRequestBuilders.multipart(ApiUrls.FASCICOLO.concat(endpoint))
				//						.content(allegato.getBytes())
				.file("schedaValidazioneFirmata", schedaValidazioneFirmata.getBytes())
				.param("cuaa", cuaa)
				.param("nextIdValidazione", Integer.toString(1))
				;
		//						.contentType(MediaType.APPLICATION_OCTET_STREAM);
		//						.contentType(MediaType.APPLICATION_JSON);
		requestBuilder.with(request -> {
			//			request.setMethod(HttpMethod.PUT.name());
			request.setMethod(HttpMethod.POST.name());
			return request;
		});

		this.mockMvc.perform(requestBuilder)
		.andExpect(status().isOk());

		verify(anagraficaPrivateClient, timeout(15000).times(1)).startValidazioneFascicoloAsincronaUsingPOST(Mockito.any(String.class), Mockito.any(Integer.class), Mockito.any(String.class));
		//		verify(anagraficaPrivateClient, timeout(15000).times(1)).protocollaSchedaValidazioneUsingPOST(Mockito.any(String.class), Mockito.any(), Mockito.any(), Mockito.any(Integer.class), Mockito.any(TipoDetenzioneEnum.class));
		//		TODO verificare su tabella eventi
		//		List<EventStoredModel> eventiFalliti = eventStoreService.findAll();
		//		assertEquals(0, eventiFalliti.size());
		//		List<EventStoredModel> eventiFallitiFiltrati = eventiFalliti.stream()
		//				.filter( evento -> evento.getEvent().equals(eventClassError) && evento.getJsonEvent().equals(json))
		//				.collect(Collectors.toCollection(ArrayList::new));
	}


	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	void validazioneMandato_KO() throws Exception {
		String cuaa = "BRNGTN67E05D086L";
		MockMultipartFile documento = new MockMultipartFile("documento.pdf", "AAAAAA".getBytes());

		FascicoloDto fascicoloDto = new FascicoloDto();
		fascicoloDto.setCuaa(cuaa);
		fascicoloDto.setStato(StatoEnum.IN_VALIDAZIONE);

		Mockito.when(anagraficaPrivateClient.getByCuaaUsingGET1(Mockito.any(String.class), Mockito.any(Integer.class))).thenReturn(fascicoloDto);

		Mockito.doThrow(RestClientException.class).when(anagraficaPrivateClient).startValidazioneFascicoloAsincronaUsingPOST(Mockito.isA(String.class), Mockito.isA(Integer.class), Mockito.isA(String.class));
		Mockito.doNothing().when(anagraficaPrivateClient).protocollaSchedaValidazioneUsingPOST(Mockito.isA(String.class), Mockito.any(), Mockito.any(), Mockito.isA(Integer.class), Mockito.isA(TipoDetenzioneEnum.class));
		Mockito.doNothing().when(anagraficaPrivateClient).sincronizzazioneAgsUsingPOST(Mockito.isA(String.class), Mockito.isA(Integer.class));
		Mockito.doNothing().when(anagraficaPrivateClient).startValidazioneFascicoloAutonomoAsincronaUsingPOST(Mockito.isA(String.class), Mockito.isA(Integer.class), Mockito.isA(String.class));
		Mockito.doNothing().when(anagraficaPrivateClient).notificaMailCaaRichiestaValidazioneAccettataUsingPOST(Mockito.isA(String.class));
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		ResponseEntity<Void> responseEntity = new ResponseEntity<Void>(
				header,
				HttpStatus.OK
				);
		Mockito.when(zootecniaPrivateClient.startValidazioneFascicolo(Mockito.any(String.class), Mockito.any(Integer.class))).thenReturn(responseEntity);
		Mockito.when(zootecniaPrivateClient.sincronizzazioneAgsWithHttpInfo(Mockito.any(String.class), Mockito.any(Integer.class))).thenReturn(responseEntity);



		SchedaValidazioneFascicoloDto scheda = new SchedaValidazioneFascicoloDto();
		scheda.setCodiceFiscale(cuaa);
		scheda.setNextIdValidazione(1);
		scheda.setTipoDetenzione(TipoDetenzioneEnum.MANDATO);
		scheda.setReport(new ByteArrayResource(documento.getBytes()));
		StartValidazioneFascicoloEvent event = new StartValidazioneFascicoloEvent(scheda);

		fascicoloStoricizzazioneService.startValidazioneFascicoloAsincrona(event, cuaa, 1);


		//		verificare su tabella eventi
		String eventClassError = "it.tndigitale.a4g.fascicolo.mediator.business.service.event.StartValidazioneFascicoloEvent";
		//		String json = "{\"data\":{\"codiceFiscale\":\"BRNGTN67E05D086L\",\"report\":null,\"allegati\":null,\"nextIdValidazione\":1,\"tipoDetenzione\":\"MANDATO\"},\"numberOfRetry\":1,\"username\":\"XPDNDR77B03L377S\"}";
		String json ="{\"data\":{\"codiceFiscale\":\"BRNGTN67E05D086L\",\"report\":{\"byteArray\":\"QUFBQUFB\",\"description\":\"Byte array resource [resource loaded from byte array]\",\"filename\":null},\"nextIdValidazione\":1,\"tipoDetenzione\":\"MANDATO\"},\"numberOfRetry\":1,\"username\":\"utente\"}";
		List<EventStoredModel> eventiFalliti = eventStoreService.findAll();
		assertNotNull(eventiFalliti);
		List<EventStoredModel> eventiFallitiFiltrati = eventiFalliti.stream()
				.filter( evento -> evento.getEvent().equals(eventClassError) && evento.getJsonEvent().equals(json))
				.collect(Collectors.toCollection(ArrayList::new));
		assertEquals(1, eventiFallitiFiltrati.size());
	}




	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	void validazioneDetenzioneAutonoma() throws Exception {
		//		ResultActions resultActions = mockMvc.perform(get(String.format(ApiUrls.FASCICOLO + "/%s/validazione-detenzione-autonoma", cuaa))
		//				.contentType(MediaType.APPLICATION_JSON));
	}

}
