package it.tndigitale.a4g.uma.business.service.consumi;

import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.framework.client.custom.VerificaFirmaClient;
import it.tndigitale.a4g.framework.event.store.handler.EventBus;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.uma.Ruoli;
import it.tndigitale.a4g.uma.api.ApiUrls;
import it.tndigitale.a4g.uma.business.persistence.entity.DichiarazioneConsumiModel;
import it.tndigitale.a4g.uma.business.persistence.entity.StatoDichiarazioneConsumi;
import it.tndigitale.a4g.uma.business.persistence.repository.DichiarazioneConsumiDao;
import it.tndigitale.a4g.uma.business.service.client.UmaAnagraficaClient;
import it.tndigitale.a4g.uma.business.service.client.UmaDotazioneTecnicaClient;
import it.tndigitale.a4g.uma.business.service.client.UmaProxyClient;
import it.tndigitale.a4g.uma.business.service.client.UmaTerritorioClient;
import it.tndigitale.a4g.uma.business.service.client.UmaUtenteClient;
import it.tndigitale.a4g.uma.business.service.utente.AbilitazioniComponent;
import it.tndigitale.a4g.uma.dto.aual.FascicoloAualDto;
import it.tndigitale.a4g.uma.dto.consumi.DichiarazioneConsumiDto;
import it.tndigitale.a4g.uma.dto.consumi.DichiarazioneConsumiPatch;
import it.tndigitale.a4g.uma.dto.richiesta.PresentaRichiestaDto;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente")
class ConsumiControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private DichiarazioneConsumiDao dichiarazioneConsumiDao;

	@MockBean
	private AbilitazioniComponent abilitazioniComponent;
	@MockBean
	private UmaAnagraficaClient anagraficaClient;
	@MockBean
	private EventBus eventBus;
	@MockBean
	private VerificaFirmaClient verificaFirmaClient;
	@MockBean
	private UmaTerritorioClient territorioClient;
	@MockBean
	private UmaDotazioneTecnicaClient dotazioneTecnicaClient;
	@MockBean
	private UmaProxyClient stampaClient;
	@MockBean
	private UmaUtenteClient utenteClient;
	
	@Value("${it.tndigit.a4g.uma.protocollazione.firma.obbligatoria}")
    private boolean firmaObbligatoria;
	
	@SpyBean
	private UtenteComponent utenteComponent;

	@BeforeEach
	private void mockUtenteConnesso() throws Exception {
		when(utenteComponent.utenza()).thenReturn("utente");
		Mockito.when(abilitazioniComponent.checkRicercaDomandeUma()).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkRicercaEnteDomandeUma()).thenReturn(true);
		when(utenteComponent.haRuolo(Ruoli.DOMANDE_UMA_RICERCA_UTENTE)).thenReturn(false);
		when(utenteComponent.haRuolo(Ruoli.DOMANDE_UMA_RICERCA_ENTE)).thenReturn(false);
		when(utenteComponent.haRuolo(Ruoli.DOMANDE_UMA_RICERCA_TUTTI)).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkRicercaTuttiDomandeUma()).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkModificaRichiestaCarburante(Mockito.anyLong())).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkPresentaDomandaUma(Mockito.any())).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkModificaDichiarazioneConsumi(Mockito.anyLong())).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkRicercaAllegatiConsuntivoDichiarazioneConsumi(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong())).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkModificaConsuntiviDichiarazioneConsumi(Mockito.anyLong(), Mockito.anyLong())).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkRicercaDichiarazioneConsumi(Mockito.anyLong())).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkDeleteDichiarazioneConsumi(Mockito.anyLong())).thenReturn(true);
	}

	@Test
	@Sql(scripts = "/sql/consumi/consumi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consumi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getDichiarazioneConsumiByIdSuccessful() throws Exception {
		String response = mockMvc.perform(get("/api/v1/consumi/7761").param("campagnaRichiesta", "2021")) 
				.andExpect(status().is2xxSuccessful())
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		DichiarazioneConsumiDto dto = objectMapper.readValue(response, DichiarazioneConsumiDto.class);
		assertEquals("ZMBRCR96D11L174G", dto.getCuaa());
		assertEquals(2021L, dto.getCampagnaRichiesta());
		assertEquals("Denominazione", dto.getDenominazione());
		/* La rimanenza fa riferimento alla dichiarazione 7766 del 2020 */
		 assertEquals(10, dto.getRimanenza().getGasolio());
		 assertEquals(20, dto.getRimanenza().getBenzina());
		 assertEquals(30, dto.getRimanenza().getGasolioSerre());
		 assertEquals(40, dto.getRimanenza().getGasolioTerzi());
	}
	
	@Test
	@Sql(scripts = "/sql/consumi/consumi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consumi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getDichiarazioniConsumi() throws Exception {
		
		when(utenteComponent.haRuolo(Ruoli.DOMANDE_UMA_RICERCA_TUTTI)).thenReturn(true);
		
		String cuaa = "ZMBRCR96D11L174G";
		mockMvc.perform(get("/api/v1/consumi").param("cuaa", cuaa))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", Matchers.hasSize(2)))
		.andExpect(jsonPath("$.[*].cuaa", everyItem(containsStringIgnoringCase(cuaa))));
	}
	/*
	@Test
	@Sql(scripts = "/sql/consumi/consumi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consumi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getDichiarazioniConsumiOperatoreCAA() throws Exception {
		String cuaa = "ZMBRCR96D11L174G";
		SportelloFascicoloDto sportello = new SportelloFascicoloDto().addCuaaListItem(cuaa);
		when(anagraficaClient.getSportelliFascicoli()).thenReturn(Arrays.asList(sportello));
		mockMvc.perform(get("/api/v1/consumi/caa").param("campagna", "2020"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", Matchers.hasSize(1)))
		.andExpect(jsonPath("$.[*].cuaa", everyItem(containsStringIgnoringCase(cuaa))));
	}
	@Test
	@Sql(scripts = "/sql/consumi/consumi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consumi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getDichiarazioniConsumiCampagnaRichiestaOperatoreCAA() throws Exception {
		
		// SportelloFascicoloDto sportello = new SportelloFascicoloDto().addCuaaListItem("ZMBRCR96D11L174G");
		// when(anagraficaClient.getSportelliFascicoli()).thenReturn(Arrays.asList(sportello));
		when(utenteComponent.haRuolo(Ruoli.DOMANDE_UMA_RICERCA_TUTTI)).thenReturn(false);
		when(utenteComponent.haRuolo(Ruoli.DOMANDE_UMA_RICERCA_ENTE)).thenReturn(true);
		
		String response = mockMvc.perform(get("/api/v1/consumi").param("campagna", "2021"))
				.andExpect(status().is2xxSuccessful())
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();
		List<DichiarazioneConsumiDto> dtos = objectMapper.readValue(response, new TypeReference<List<DichiarazioneConsumiDto>>(){});

		assertEquals(1, dtos.size());
		assertEquals("ZMBRCR96D11L174G", dtos.get(0).getCuaa());
		assertEquals(2021L, dtos.get(0).getCampagnaRichiesta());
	}
	
	@Test
	@Sql(scripts = "/sql/consumi/consumi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consumi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getDichiarazioniConsumiCampagnaRichiestaUtenteAzienda() throws Exception {
		
		String aziendaUtente = "ZMBRCR96D11L174G";
		when(utenteClient.getAziende()).thenReturn(Arrays.asList(aziendaUtente));
		when(utenteComponent.haRuolo(Ruoli.DOMANDE_UMA_RICERCA_TUTTI)).thenReturn(false);
		when(utenteComponent.haRuolo(Ruoli.DOMANDE_UMA_RICERCA_ENTE)).thenReturn(false);
		when(utenteComponent.haRuolo(Ruoli.DOMANDE_UMA_RICERCA_UTENTE)).thenReturn(true);
		
		
		String response = mockMvc.perform(get("/api/v1/consumi").param("campagna", "2021"))
				.andExpect(status().is2xxSuccessful())
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();
		List<DichiarazioneConsumiDto> dtos = objectMapper.readValue(response, new TypeReference<List<DichiarazioneConsumiDto>>(){});

		assertEquals(1, dtos.size());
		assertEquals("ZMBRCR96D11L174G", dtos.get(0).getCuaa());
		assertEquals(2021L, dtos.get(0).getCampagnaRichiesta());
	}
	
	@Test
	@Sql(scripts = "/sql/consumi/consumi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consumi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getDichiarazioniConsumiCuaaSuccessful() throws Exception {
		when(utenteClient.getAziende()).thenReturn(Arrays.asList("MSTFBA79L10H612L"));
		when(utenteComponent.haRuolo(Ruoli.DOMANDE_UMA_RICERCA_TUTTI)).thenReturn(false);
		when(utenteComponent.haRuolo(Ruoli.DOMANDE_UMA_RICERCA_UTENTE)).thenReturn(true);

		String response = mockMvc.perform(get("/api/v1/consumi/paged").param("cuaa", "MSTFBA79L10H612L"))
				.andExpect(status().is2xxSuccessful())
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		RisultatiPaginati<DichiarazioneConsumiDto> dtos = objectMapper.readValue(response, new TypeReference<RisultatiPaginati<DichiarazioneConsumiDto>>(){});

		assertEquals(3, dtos.getRisultati().size());
		assertEquals("MSTFBA79L10H612L", dtos.getRisultati().get(0).getCuaa());
		assertEquals(2019L, dtos.getRisultati().get(0).getCampagnaRichiesta());
	}
	*/

	@Test
	@Sql(scripts = "/sql/consumi/consumi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consumi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getDichiarazioniConsumiStatiAutorizzataSuccessful() throws Exception {
		
		String response = mockMvc.perform(get("/api/v1/consumi/paged")
				.param("stati", StatoDichiarazioneConsumi.PROTOCOLLATA.name()))
				.andExpect(status().is2xxSuccessful())
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		RisultatiPaginati<DichiarazioneConsumiDto> dtos = objectMapper.readValue(response, new TypeReference<RisultatiPaginati<DichiarazioneConsumiDto>>(){});

		assertEquals(5, dtos.getRisultati().size());
		assertEquals("01464420221", dtos.getRisultati().get(0).getCuaa());
		assertEquals(2021L, dtos.getRisultati().get(0).getCampagnaRichiesta());
	}

	@Test
	@Sql(scripts = "/sql/consumi/consumi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consumi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getDichiarazioniConsumiStatiInCompilazioneSuccessful() throws Exception {
		String response = mockMvc.perform(get("/api/v1/consumi/paged")
				.param("stati", StatoDichiarazioneConsumi.IN_COMPILAZIONE.name()))
				.andExpect(status().is2xxSuccessful())
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		RisultatiPaginati<DichiarazioneConsumiDto> dtos = objectMapper.readValue(response, new TypeReference<RisultatiPaginati<DichiarazioneConsumiDto>>(){});

		assertEquals(5, dtos.getRisultati().size());
		assertEquals("ZMBRCR96D11L174G", dtos.getRisultati().get(0).getCuaa());
		assertEquals(2021L, dtos.getRisultati().get(0).getCampagnaRichiesta());
	}

	@Test
	@Sql(scripts = "/sql/consumi/consumi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consumi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getDichiarazioniConsumi204() throws Exception {
		String response = mockMvc.perform(get("/api/v1/consumi/paged")
				.param("cuaa", "ZPPMRA99R47C794B"))
				.andExpect(status().is2xxSuccessful())
				.andReturn()
				.getResponse()
				.getContentAsString();

		RisultatiPaginati<DichiarazioneConsumiDto> dtos = objectMapper.readValue(response, new TypeReference<RisultatiPaginati<DichiarazioneConsumiDto>>(){});

		assertEquals(0, dtos.getCount());
		assertEquals(dtos.getRisultati(), new ArrayList<>());
	}
	/*
	@Test
	@Sql(scripts = "/sql/consumi/consumi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consumi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void presentaDichiarazioneConsumiSuccessful() throws Exception {
		mockGetFascicolo();
		
		PresentaRichiestaDto requestBody = new PresentaRichiestaDto();
		requestBody.setCuaa("FRLGPP67A01H330V");
		requestBody.setCodiceFiscaleRichiedente("CNTBRN95T13F839K");

		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(requestBody);

		mockMvc.perform(post("/api/v1/consumi")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
		.andExpect(status().isOk());
	}
	*/
	@Test
	@Sql(scripts = "/sql/consumi/consumi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consumi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void presentaDichiarazioneConsumiErrorNoCuaa() throws Exception {
		PresentaRichiestaDto requestBody = new PresentaRichiestaDto();
		requestBody.setCodiceFiscaleRichiedente("CNTBRN95T13F839K");

		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(requestBody);

		NestedServletException exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(post("/api/v1/consumi")
					.contentType(MediaType.APPLICATION_JSON)
					.content(json));
		}); 

		assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
		assertEquals("Il CUAA non è presente", exception.getCause().getMessage());
	}

	@Test
	@Sql(scripts = "/sql/consumi/consumi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consumi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void presentaDichiarazioneConsumiErrorNoCfRichiedente() throws Exception {
		PresentaRichiestaDto requestBody = new PresentaRichiestaDto();
		requestBody.setCuaa("FRLGPP67A01H330V");

		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(requestBody);

		NestedServletException exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(post("/api/v1/consumi")
					.contentType(MediaType.APPLICATION_JSON)
					.content(json));
		}); 

		assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
		assertEquals("Il codice fiscale del rappresentante non è presente", exception.getCause().getMessage());
	}

	@Test
	@Transactional
	@Sql(scripts = "/sql/consumi/consumi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consumi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void deleteDichiarazioneConsumi() throws Exception {
		mockMvc.perform(delete("/api/v1/consumi/7761"))
		.andExpect(status().isOk());

		Optional<DichiarazioneConsumiModel> dichCoOpt = dichiarazioneConsumiDao.findById(7761L);
		assertTrue(!dichCoOpt.isPresent());
	}

	@Test
	@Transactional
	@Sql(scripts = "/sql/consumi/consumi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consumi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void aggiornaDichiarazioneConsumiByOperatoreCaaSuccessful() throws Exception {
		
		DichiarazioneConsumiPatch dichiarazioneConsumiPatch = new DichiarazioneConsumiPatch();
		dichiarazioneConsumiPatch.setDataConduzione(LocalDateTime.of(2021, 12, 12, 0, 0));
		dichiarazioneConsumiPatch.setMotivazioneAccisa("Residuo non trasferito");

		FascicoloAualDto fascicolo = new FascicoloAualDto();
		fascicolo.setCodiCuaa("");
		fascicolo.setDescDeno("");

		Mockito.when(anagraficaClient.getFascicolo(Mockito.any())).thenReturn(fascicolo);

		doReturn(false).when(utenteComponent).haRuolo(Ruoli.ISTRUTTORE_UMA);

		mockMvc.perform(put("/api/v1/consumi/9173")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dichiarazioneConsumiPatch)))
		.andExpect(status().isOk());
	}
	
	@Test
	@Transactional
	@Sql(scripts = "/sql/consumi/consumi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consumi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void aggiornaDichiarazioneConsumiByIstruttoreSuccessful() throws Exception {

		DichiarazioneConsumiPatch dichiarazioneConsumiPatch = new DichiarazioneConsumiPatch();
		dichiarazioneConsumiPatch.setDataConduzione(LocalDateTime.of(2021, 12, 12, 0, 0));
		dichiarazioneConsumiPatch.setMotivazioneAccisa("");

		FascicoloAualDto fascicolo = new FascicoloAualDto();
		fascicolo.setCodiCuaa("");
		fascicolo.setDescDeno("");

		Mockito.when(anagraficaClient.getFascicolo(Mockito.any())).thenReturn(fascicolo);

		doReturn(true).when(utenteComponent).haRuolo(Ruoli.ISTRUTTORE_UMA);

		mockMvc.perform(put("/api/v1/consumi/9173")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dichiarazioneConsumiPatch)))
		.andExpect(status().isOk());
	}
	/*
	@Test
	@Transactional
	@Sql(scripts = "/sql/consumi/consumi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consumi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void protocollaDichiarazioneSuccessful() throws Exception {
		Long idDichiarazioneConsumi = 7761L;

		MockHttpServletRequestBuilder request = buildRequest(idDichiarazioneConsumi);

		mockChiamateVerificaFirmaAndGetFascicolo();

		List<CaricaAgsDto> dtos = new ArrayList<>();
		CaricaAgsDto caricaDto = new CaricaAgsDto();
		caricaDto.setCodiceFiscale("FRLGPP67A01H330V");
		CaricaAgsDto caricaDto2 = new CaricaAgsDto();
		caricaDto.setCodiceFiscale("CNTBRN95T13F839K");
		dtos.add(caricaDto);
		dtos.add(caricaDto2);
		Mockito.when(anagraficaClient.getTitolariRappresentantiLegali(Mockito.any())).thenReturn(dtos);
		
		this.mockMvc.perform(request).andExpect(status().is2xxSuccessful())
		.andExpect(status().isOk());

		verify(eventBus, times(1)).publishEvent(Mockito.any());

		Optional<DichiarazioneConsumiModel> dichiarazioneOpt = dichiarazioneConsumiDao.findById(idDichiarazioneConsumi);

		if (!dichiarazioneOpt.isPresent()) {
			fail();
		}

		DichiarazioneConsumiModel dichiarazioneConsumi = dichiarazioneOpt.get();

		assertEquals(StatoDichiarazioneConsumi.PROTOCOLLATA, dichiarazioneConsumi.getStato());
		assertNotNull(dichiarazioneConsumi.getDocumento());
	}

	@Test
	@Sql(scripts = "/sql/consumi/consumi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consumi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void protocollaDichiarazioneError() throws Exception {
		Long idDichiarazioneConsumi = 7761L;

		MockHttpServletRequestBuilder request = buildRequest(idDichiarazioneConsumi);

		mockChiamateVerificaFirmaAndGetFascicolo();

		List<CaricaAgsDto> dtos = new ArrayList<>();
		CaricaAgsDto caricaDto = new CaricaAgsDto();
		caricaDto.setCodiceFiscale("XXXGPP67A01H330X");
		dtos.add(caricaDto);
		Mockito.when(anagraficaClient.getTitolariRappresentantiLegali(Mockito.any())).thenReturn(dtos);
		
		NestedServletException exception = assertThrows(NestedServletException.class, () -> {
			this.mockMvc.perform(request);
		}); 

		assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
	}

	@Test
	@Sql(scripts = "/sql/consumi/consumi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consumi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void stampaDichiarazioneConsumiSuccessful() throws Exception {

		// Mockito.when(territorioClient.getColture(Mockito.any(), Mockito.any())).thenReturn(new ArrayList<ParticellaDto>());
		// Mockito.when(dotazioneTecnicaClient.getFabbricati(Mockito.any(), Mockito.any())).thenReturn(null);

		Mockito.when(stampaClient.stampa(Mockito.any(), Mockito.any())).thenReturn(("AA".getBytes()));

		FascicoloAualDto fascicolo = new FascicoloAualDto();
		fascicolo.setCodiCuaa("ASDFAX66E31F187R");

		Mockito.when(anagraficaClient.getFascicolo(Mockito.any())).thenReturn(fascicolo);

		mockMvc.perform(get("/api/v1/consumi/9173/stampa"))
		.andExpect(status().is2xxSuccessful())
		.andExpect(status().isOk());
	}
	*/
	
	private MockHttpServletRequestBuilder buildRequest(Long idDichiarazioneConsumi) throws IOException {
		String endpoint = String.format("/%s/protocolla?haFirma=true", idDichiarazioneConsumi);
		Path path = Paths.get("src/test/resources/documentiFirmati/MANDATO_ftoDPDNDR77B03L378L.pdf");
		MockMultipartFile contratto = new MockMultipartFile("documento.pdf", Files.readAllBytes(path));

		MockHttpServletRequestBuilder builder =
				MockMvcRequestBuilders.multipart(ApiUrls.CONSUMI.concat(endpoint))
				.file("documento", contratto.getBytes())
				.param("id", idDichiarazioneConsumi.toString())
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE);
		return builder;
	}
	
	private void mockGetFascicolo() {
		FascicoloAualDto fascicoloValido = new FascicoloAualDto();
		fascicoloValido.setDescDeno("Denominazione Azienda");
		fascicoloValido.setDescPec("pec@gmail.com");
		/*
		fascicoloValido.setStato(StatoEnum.VALIDO);
		DetenzioneAgsDto delega = new DetenzioneAgsDto();
		delega.setTipoDetenzione(TipoDetenzioneEnum.DELEGA);
		delega.setSportello("sportello delega");
		DetenzioneAgsDto delega2 = new DetenzioneAgsDto();
		delega2.setTipoDetenzione(TipoDetenzioneEnum.DELEGA);
		delega2.setSportello("sportello delega 2");
		DetenzioneAgsDto mandato = new DetenzioneAgsDto();
		mandato.setTipoDetenzione(TipoDetenzioneEnum.MANDATO);
		mandato.setSportello("sportello mandato");
		List<DetenzioneAgsDto> detList = new ArrayList<>();
		detList.add(delega2);
		detList.add(delega);
		detList.add(mandato);
		fascicoloValido.setDetenzioni(detList);
		*/
		Mockito.when(anagraficaClient.getFascicolo(Mockito.any())).thenReturn(fascicoloValido);
	}

	private void mockChiamateVerificaFirmaAndGetFascicolo() throws Exception {
		Mockito.when(verificaFirmaClient.verificaFirma(Mockito.any(), Mockito.any())).thenReturn(null);
		mockGetFascicolo();
	}
}
