package it.tndigitale.a4g.uma.business.service.richiesta;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
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
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.h2.tools.Server;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.fascicolo.anagrafica.client.model.CaricaAgsDto;
import it.tndigitale.a4g.fascicolo.anagrafica.client.model.DetenzioneAgsDto;
import it.tndigitale.a4g.fascicolo.anagrafica.client.model.DetenzioneAgsDto.TipoDetenzioneEnum;
import it.tndigitale.a4g.fascicolo.anagrafica.client.model.FascicoloAgsDto;
import it.tndigitale.a4g.fascicolo.anagrafica.client.model.FascicoloAgsDto.StatoEnum;
import it.tndigitale.a4g.fascicolo.anagrafica.client.model.SportelloFascicoloDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.client.model.FabbricatoAgsDto;
import it.tndigitale.a4g.fascicolo.territorio.client.model.CodificaColtura;
import it.tndigitale.a4g.fascicolo.territorio.client.model.ParticellaDto;
import it.tndigitale.a4g.framework.client.custom.VerificaFirmaClient;
import it.tndigitale.a4g.framework.event.store.handler.EventBus;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.uma.Ruoli;
import it.tndigitale.a4g.uma.api.ApiUrls;
import it.tndigitale.a4g.uma.builder.ColturaTestBuilder;
import it.tndigitale.a4g.uma.builder.ParticellaTestBuilder;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.StatoRichiestaCarburante;
import it.tndigitale.a4g.uma.business.persistence.repository.RichiestaCarburanteDao;
import it.tndigitale.a4g.uma.business.service.client.UmaAnagraficaClient;
import it.tndigitale.a4g.uma.business.service.client.UmaDotazioneTecnicaClient;
import it.tndigitale.a4g.uma.business.service.client.UmaProxyClient;
import it.tndigitale.a4g.uma.business.service.client.UmaTerritorioClient;
import it.tndigitale.a4g.uma.business.service.client.UmaUtenteClient;
import it.tndigitale.a4g.uma.business.service.utente.AbilitazioniComponent;
import it.tndigitale.a4g.uma.dto.consumi.CarburanteDto;
import it.tndigitale.a4g.uma.dto.richiesta.CarburanteCompletoDto;
import it.tndigitale.a4g.uma.dto.richiesta.CarburanteRichiestoDto;
import it.tndigitale.a4g.uma.dto.richiesta.PresentaRichiestaDto;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser(username = "utente")
class RichiestaCarburanteControllerTest {
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private AbilitazioniComponent abilitazioniComponent;
	@MockBean
	private UmaDotazioneTecnicaClient dotazioneTecnicaClient;
	@MockBean
	private UmaTerritorioClient territorioClient;
	@MockBean
	private VerificaFirmaClient verificaFirmaClient;
	@MockBean
	private UmaAnagraficaClient anagraficaClient;
	@MockBean
	private RichiestaCarburanteValidator richiestaCarburanteValidator;
	@MockBean
	private EventBus eventBus;
	@MockBean
	private UmaProxyClient stampaClient;
	@MockBean
	private Clock clock;
	@MockBean
	private UtenteComponent utenteComponent;
	@MockBean
	private UmaUtenteClient utenteClient;
	@Autowired
	private RichiestaCarburanteDao richiestaCarburanteDao;

	@BeforeAll
	public static void initTest() throws SQLException {
		Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082")
		.start();
	}

	@BeforeEach
	private void mockUtenteConnesso() throws Exception {

		when(utenteComponent.utenza()).thenReturn("utente");

		when(utenteComponent.haRuolo(Ruoli.DOMANDE_UMA_RICERCA_UTENTE)).thenReturn(false);
		when(utenteComponent.haRuolo(Ruoli.DOMANDE_UMA_RICERCA_ENTE)).thenReturn(false);
		when(utenteComponent.haRuolo(Ruoli.DOMANDE_UMA_RICERCA_TUTTI)).thenReturn(true);

		Mockito.when(abilitazioniComponent.checkRicercaEnteDomandeUma()).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkRicercaTuttiDomandeUma()).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkRicercaDomandeUma()).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkRicercaDomandaUma(Mockito.anyString())).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkModificaRichiestaCarburante(Mockito.anyLong())).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkPresentaDomandaUma(Mockito.anyString())).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkDeleteRichiestaCarburante(Mockito.anyLong())).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkRicercaRichiestaDiCarburante(Mockito.anyLong())).thenReturn(true);
	}

	@Test
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void protocollaRichiestaSuccessful() throws Exception {
		Long idRichiestaCarburante = 1890L;

		MockHttpServletRequestBuilder request = buildRequest(idRichiestaCarburante);

		mockChiamateVerificaFirmaAndGetFascicolo();

		List<CaricaAgsDto> dtos = new ArrayList<>();
		CaricaAgsDto caricaDto = new CaricaAgsDto();
		caricaDto.setCodiceFiscale("BBBDNL95R14L378T");
		dtos.add(caricaDto);
		Mockito.when(anagraficaClient.getTitolariRappresentantiLegali(Mockito.any())).thenReturn(dtos);

		this.mockMvc.perform(request).andExpect(status().is2xxSuccessful())
		.andExpect(status().isOk());

		verify(eventBus, times(1)).publishEvent(Mockito.any());

		Optional<RichiestaCarburanteModel> richiestaOpt = richiestaCarburanteDao.findById(idRichiestaCarburante);

		if (!richiestaOpt.isPresent()) {
			fail();
		}

		RichiestaCarburanteModel richiestaAutorizzata = richiestaOpt.get();

		assertEquals(StatoRichiestaCarburante.AUTORIZZATA, richiestaAutorizzata.getStato());
		assertNotNull(richiestaAutorizzata.getDocumento());
	}

	@Test
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void protocollaRettificaTitolareDecedutoSuccessful() throws Exception {
		Long idRichiestaCarburante = 8475L;

		MockHttpServletRequestBuilder request = buildRequest(idRichiestaCarburante);

		Mockito.when(verificaFirmaClient.verificaFirma(Mockito.any(), Mockito.any())).thenReturn(null);
		
		FascicoloAgsDto fascicoloValido = new FascicoloAgsDto()
				.denominazione("BNLFRN72P43H612A")
				.pec("pec@gmail.com")
				.stato(StatoEnum.VALIDO)
				.dataMorteTitolare(LocalDateTime.of(2022, 2, 2, 0, 0));

		DetenzioneAgsDto delega = new DetenzioneAgsDto()
				.tipoDetenzione(TipoDetenzioneEnum.DELEGA)
				.sportello("sportello delega");

		fascicoloValido.setDetenzioni(Arrays.asList(delega));
		Mockito.when(anagraficaClient.getFascicolo(Mockito.any())).thenReturn(fascicoloValido);

		List<CaricaAgsDto> dtos = new ArrayList<>();
		CaricaAgsDto caricaDto = new CaricaAgsDto();
		caricaDto.setCodiceFiscale("BNLFRN72P43H612A");
		dtos.add(caricaDto);
		Mockito.when(anagraficaClient.getEredi(Mockito.any())).thenReturn(dtos);

		this.mockMvc.perform(request).andExpect(status().isOk());

		verify(eventBus, times(1)).publishEvent(Mockito.any());

		Optional<RichiestaCarburanteModel> richiestaOpt = richiestaCarburanteDao.findById(idRichiestaCarburante);

		if (!richiestaOpt.isPresent()) {
			fail();
		}

		RichiestaCarburanteModel richiestaAutorizzata = richiestaOpt.get();
		assertEquals(StatoRichiestaCarburante.AUTORIZZATA, richiestaAutorizzata.getStato());
		assertNotNull(richiestaAutorizzata.getDocumento());
	}

	@Test
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void protocollaRettificaSuccessful() throws Exception {
		Long idRichiestaCarburante = 1799L;

		MockHttpServletRequestBuilder request = buildRequest(idRichiestaCarburante);

		mockChiamateVerificaFirmaAndGetFascicolo();

		List<CaricaAgsDto> dtos = new ArrayList<>();
		CaricaAgsDto caricaDto = new CaricaAgsDto();
		caricaDto.setCodiceFiscale("ABBDNL95R14L378T");
		dtos.add(caricaDto);
		Mockito.when(anagraficaClient.getTitolariRappresentantiLegali(Mockito.any())).thenReturn(dtos);

		this.mockMvc.perform(request).andExpect(status().is2xxSuccessful())
		.andExpect(status().isOk());

		verify(eventBus, times(1)).publishEvent(Mockito.any());

		Optional<RichiestaCarburanteModel> richiestaOpt = richiestaCarburanteDao.findById(idRichiestaCarburante);

		if (!richiestaOpt.isPresent()) {
			fail();
		}

		RichiestaCarburanteModel richiestaAutorizzata = richiestaOpt.get();

		assertEquals(StatoRichiestaCarburante.AUTORIZZATA, richiestaAutorizzata.getStato());
		assertNotNull(richiestaAutorizzata.getDocumento());

		Optional<RichiestaCarburanteModel> richiestaRettificataOpt = richiestaCarburanteDao.findById(1864L);

		if (!richiestaRettificataOpt.isPresent()) {
			fail();
		}

		assertEquals(StatoRichiestaCarburante.RETTIFICATA, richiestaRettificataOpt.get().getStato());
	}

	@Test
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void protocollaRichiestaError() throws Exception {
		Long idRichiestaCarburante = 1890L;

		MockHttpServletRequestBuilder request = buildRequest(idRichiestaCarburante);

		mockChiamateVerificaFirmaAndGetFascicolo();

		List<CaricaAgsDto> dtos = new ArrayList<>();
		CaricaAgsDto caricaDto = new CaricaAgsDto();
		caricaDto.setCodiceFiscale("BBBDNL95R14L378X");
		dtos.add(caricaDto);
		Mockito.when(anagraficaClient.getTitolariRappresentantiLegali(Mockito.any())).thenReturn(dtos);

		NestedServletException exception = assertThrows(NestedServletException.class, () -> {
			this.mockMvc.perform(request);
		}); 

		assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
	}

	private void mockChiamateVerificaFirmaAndGetFascicolo() throws Exception {
		Mockito.when(verificaFirmaClient.verificaFirma(Mockito.any(), Mockito.any())).thenReturn(null);

		mockGetFascicolo();
	}

	private void mockGetFascicolo() throws Exception {
		FascicoloAgsDto fascicoloValido = new FascicoloAgsDto();
		fascicoloValido.setDenominazione("Denominazione Azienda");
		fascicoloValido.setPec("pec@gmail.com");
		fascicoloValido.setStato(StatoEnum.VALIDO);
		DetenzioneAgsDto delega = new DetenzioneAgsDto();
		delega.setTipoDetenzione(TipoDetenzioneEnum.DELEGA);
		delega.setSportello("sportello delega");
		List<DetenzioneAgsDto> detList = new ArrayList<>();
		detList.add(delega);
		fascicoloValido.setDetenzioni(detList);
		Mockito.when(anagraficaClient.getFascicolo(Mockito.any())).thenReturn(fascicoloValido);
	}

	@Test
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getRichiestaByCuaaUtenzeAzienda() throws Exception {
		FascicoloAgsDto fascicolo = new FascicoloAgsDto();
		fascicolo.setDenominazione("Denominazione Fascicolo");
		when(anagraficaClient.getFascicolo(Mockito.anyString())).thenReturn(fascicolo);
		String cuaa = "BBBDNL95R14L378T";

		when(utenteClient.getAziende()).thenReturn(Arrays.asList(cuaa));
		when(utenteComponent.haRuolo(Ruoli.DOMANDE_UMA_RICERCA_TUTTI)).thenReturn(false);
		when(utenteComponent.haRuolo(Ruoli.DOMANDE_UMA_RICERCA_ENTE)).thenReturn(false);
		when(utenteComponent.haRuolo(Ruoli.DOMANDE_UMA_RICERCA_UTENTE)).thenReturn(true);

		mockMvc.perform(get("/api/v1/richieste").param("cuaa", cuaa))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", Matchers.hasSize(4)))
		.andExpect(jsonPath("$.[*].cuaa", everyItem(containsStringIgnoringCase(cuaa))));
	}

	@Test
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getRichiesteOperatoreCAA() throws Exception {
		String cuaa = "BBBDNL95R14L378T";
		SportelloFascicoloDto sportello = new SportelloFascicoloDto().addCuaaListItem(cuaa);
		when(anagraficaClient.getSportelliFascicoli()).thenReturn(Arrays.asList(sportello));

		mockMvc.perform(get("/api/v1/richieste/caa").param("campagna", "2020"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", Matchers.hasSize(2)))
		.andExpect(jsonPath("$.[*].cuaa", everyItem(containsStringIgnoringCase(cuaa))));
	}

	@Test
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getRichieste() throws Exception {
		when(utenteComponent.haRuolo(Ruoli.DOMANDE_UMA_RICERCA_TUTTI)).thenReturn(true);
		var cuaa = "BBBDNL95R14L378T";
		mockMvc.perform(get("/api/v1/richieste").param("cuaa", cuaa))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", Matchers.hasSize(4)))
		.andExpect(jsonPath("$.[*].cuaa", everyItem(containsStringIgnoringCase(cuaa))));
	}

	@Test
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getRichiestaByCuaaOperatoreCAA() throws Exception {
		FascicoloAgsDto fascicolo = new FascicoloAgsDto();
		fascicolo.setDenominazione("Denominazione Fascicolo");
		when(anagraficaClient.getFascicolo(Mockito.anyString())).thenReturn(fascicolo);
		String cuaa = "BBBDNL95R14L378T";

		SportelloFascicoloDto sportello = new SportelloFascicoloDto().addCuaaListItem(cuaa);
		when(anagraficaClient.getSportelliFascicoli()).thenReturn(Arrays.asList(sportello));
		when(utenteComponent.haRuolo(Ruoli.DOMANDE_UMA_RICERCA_TUTTI)).thenReturn(false);
		when(utenteComponent.haRuolo(Ruoli.DOMANDE_UMA_RICERCA_ENTE)).thenReturn(true);

		mockMvc.perform(get("/api/v1/richieste").param("cuaa", cuaa))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", Matchers.hasSize(4)))
		.andExpect(jsonPath("$.[*].cuaa", everyItem(containsStringIgnoringCase(cuaa))));
	}

	@Test
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getRichiestaByCuaaEmpty() throws Exception {
		FascicoloAgsDto fascicolo = new FascicoloAgsDto();
		fascicolo.setDenominazione("Denominazione Fascicolo");
		when(anagraficaClient.getFascicolo(Mockito.anyString())).thenReturn(fascicolo);
		String cuaa = "ABCDERFASDBCVBAS";
		mockMvc.perform(get("/api/v1/richieste/paged").param("cuaa", cuaa))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.risultati", Matchers.hasSize(0)));
	}

	@Test
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getRichiestaByCuaa() throws Exception {
		FascicoloAgsDto fascicolo = new FascicoloAgsDto();
		fascicolo.setDenominazione("Denominazione Fascicolo");
		when(anagraficaClient.getFascicolo(Mockito.anyString())).thenReturn(fascicolo);

		String cuaa = "BBBDNL95R14L378T";
		mockMvc.perform(get("/api/v1/richieste/paged").param("cuaa", cuaa))
		.andExpect(status().is2xxSuccessful())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.risultati", Matchers.hasSize(4)))
		.andExpect(jsonPath("$.risultati.[*].cuaa", everyItem(containsStringIgnoringCase(cuaa))));
	}

	@Test
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getRichiestaByAnnoCampagna() throws Exception {
		FascicoloAgsDto fascicolo = new FascicoloAgsDto();
		fascicolo.setDenominazione("Denominazione Fascicolo");
		when(anagraficaClient.getFascicolo(Mockito.anyString())).thenReturn(fascicolo);

		Long campagna = 2020L;
		mockMvc.perform(get("/api/v1/richieste/paged").param("campagna", String.valueOf(campagna)))
		.andExpect(status().is2xxSuccessful())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.risultati", Matchers.hasSize(2)))
		.andExpect(jsonPath("$.risultati.[*].campagna", everyItem(is(campagna.intValue()))));
	}

	@Test
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getRichiestaByStati() throws Exception {
		FascicoloAgsDto fascicolo = new FascicoloAgsDto();
		fascicolo.setDenominazione("Denominazione Fascicolo");
		when(anagraficaClient.getFascicolo(Mockito.anyString())).thenReturn(fascicolo);

		mockMvc.perform(get("/api/v1/richieste/paged")
				.param("stati", StatoRichiestaCarburante.AUTORIZZATA.name()))
		.andExpect(status().is2xxSuccessful())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.risultati", Matchers.hasSize(7)))
		.andExpect(jsonPath("$.risultati.[*].stato", everyItem(anyOf(is(StatoRichiestaCarburante.AUTORIZZATA.name())))));
	}

	@Test
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getRichiestaById() throws Exception {

		FascicoloAgsDto fascicolo = new FascicoloAgsDto();
		fascicolo.setDenominazione("Denominazione Fascicolo");

		FabbricatoAgsDto fabbricato = new FabbricatoAgsDto();
		fabbricato.setTipoFabbricatoCodice("000011");
		//  CODICE_SUOLO, CODICE_DEST_USO, CODICE_USO, CODICE_QUALITA, CODICE_VARIETA
		// 15436, 0, '410', '009', '037', '000', '077', 955, 2006
		CodificaColtura coltura = new CodificaColtura();
		coltura.setCodiceSuolo("410");
		coltura.setCodiceDestinazioneUso("009");
		coltura.setCodiceUso("037");
		coltura.setCodiceQualita("000");
		coltura.setCodiceVarieta("077");
		when(anagraficaClient.getFascicolo(Mockito.anyString())).thenReturn(fascicolo);
		when(dotazioneTecnicaClient.getFabbricati(Mockito.anyString(),Mockito.any())).thenReturn(Arrays.asList(fabbricato));
		mockResponseTerritorioClient();
		//		when(territorioClient.getColture(Mockito.anyString(),Mockito.any())).thenReturn(Arrays.asList(coltura));

		mockMvc.perform(get("/api/v1/richieste/3"))
		.andExpect(status().is2xxSuccessful())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.cuaa", is("DLCNDA62E48L378G")))
		.andExpect(jsonPath("$.haMacchineBenzina" , is(true)))
		.andExpect(jsonPath("$.haMacchineGasolio" , is(false)))
		.andExpect(jsonPath("$.haSuperfici" , is(true)));
	}


	@Test
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postAggiornaRichiesta() throws Exception {
		CarburanteCompletoDto carburanteCompletoDto = new CarburanteCompletoDto()
				.setBenzina(0)
				.setGasolio(1)
				.setGasolioSerre(2)
				.setGasolioTerzi(3);

		CarburanteRichiestoDto carburanteRichiestoDto = new CarburanteRichiestoDto()
				.setCarburanteRichiesto(carburanteCompletoDto)
				.setNote("NOTE");

		mockMvc.perform(put("/api/v1/richieste/5")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(carburanteRichiestoDto)))
		.andExpect(status().isOk());

		Optional<RichiestaCarburanteModel> domandaOpt = richiestaCarburanteDao.findById(5L);
		if (!domandaOpt.isPresent()) { fail();}

		RichiestaCarburanteModel domanda = domandaOpt.get();

		assertEquals(1, domanda.getGasolio());
		assertEquals(0, domanda.getBenzina());
		assertEquals(2, domanda.getGasolioSerre());
		assertEquals(3, domanda.getGasolioTerzi());
		assertEquals("NOTE", domanda.getNote());
	}

	@Test
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postAggiornaRichiestaErrorNoFabbisogni() throws Exception {
		CarburanteRichiestoDto carburanteRichiestoDto = new CarburanteRichiestoDto()
				.setCarburanteRichiesto(null)
				.setNote("NOTE");

		Exception exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(put("/api/v1/richieste/5")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(carburanteRichiestoDto)));
		}); 

		assertEquals("Non ci sono fabbisogni da dichiarare", exception.getCause().getMessage());
	}


	@Test
	void postPresentaRichiestaSuccessful() throws Exception {
		String cuaa = "00249980228";
		String codiceFiscaleRichiedente = "LBSSFN72A14C794Y";

		PresentaRichiestaDto inputDto = new PresentaRichiestaDto();
		inputDto.setCuaa(cuaa);
		inputDto.setCodiceFiscaleRichiedente(codiceFiscaleRichiedente);

		String requestBody = objectMapper.writeValueAsString(inputDto);
		Mockito.when(clock.now()).thenReturn(LocalDateTime.of(2021, 12, 13, 12, 13));
		Mockito.doNothing().when(richiestaCarburanteValidator).validaPresentazioneRichiesta(Mockito.any());

		mockGetFascicolo();

		MvcResult response = mockMvc.perform(post("/api/v1/richieste")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andExpect(status().is2xxSuccessful())
				.andExpect(status().isOk())
				.andReturn();

		String responseString = response.getResponse().getContentAsString();

		Optional<RichiestaCarburanteModel> domandaOpt = richiestaCarburanteDao.findById(Long.valueOf(responseString));
		if (!domandaOpt.isPresent()) {
			fail();
		}

		RichiestaCarburanteModel domanda = domandaOpt.get();
		assertEquals(cuaa, domanda.getCuaa());
		assertEquals(codiceFiscaleRichiedente, domanda.getCfRichiedente());
		assertEquals(2021, domanda.getCampagna());
		assertEquals(StatoRichiestaCarburante.IN_COMPILAZIONE, domanda.getStato());
	}


	@Test
	void postPresentaRichiestaCfRichiedenteError() throws Exception {
		String cuaa = "00249980228";

		PresentaRichiestaDto inputDto = new PresentaRichiestaDto();
		inputDto.setCuaa(cuaa);
		inputDto.setCodiceFiscaleRichiedente(null);

		String requestBody = objectMapper.writeValueAsString(inputDto);

		Exception exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(post("/api/v1/richieste")
					.contentType(MediaType.APPLICATION_JSON)
					.content(requestBody));
		}); 

		assertEquals("Il codice fiscale del rappresentante non è presente", exception.getCause().getMessage());
	}

	@Test
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void deleteRichiesta() throws Exception {
		mockMvc.perform(delete("/api/v1/richieste/5"))
		.andExpect(status().is2xxSuccessful())
		.andExpect(status().isOk())
		.andReturn();

		Optional<RichiestaCarburanteModel> domandaOpt = richiestaCarburanteDao.findById(5L);

		if (domandaOpt.isPresent()) {
			fail();
		}
	}

	@Test
	@Sql(scripts = "/sql/lavorazioni/get_lavorazioni_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/lavorazioni/get_lavorazioni_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getCarburanteAmmissibileTestEmpty() throws Exception {

		String stringResponse = mockMvc.perform(get(ApiUrls.RICHIESTE.concat("/1").concat(ApiUrls.CARBURANTE)))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		CarburanteDto response = objectMapper.readValue(stringResponse, CarburanteDto.class);

		assertEquals(0, response.getBenzina());
		assertEquals(0, response.getGasolio());
		assertEquals(0, response.getGasolioSerre());

	}

	@Test
	void getCarburanteAmmissibileTestNessunaRichiesta() throws Exception {
		Exception exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(get(ApiUrls.RICHIESTE.concat("/163462").concat(ApiUrls.CARBURANTE)));
		}); 

		assertEquals(EntityNotFoundException.class, exception.getCause().getClass());
	}


	/** IDLAV			COEFF				NOME							QUANTITA'
	 * 	7147			2.77				OPERAZIONI DI ESBOSCO			183 BENZINA
		7144			69.3				ESSICCAZIONE (PRODOTTO VERDE)	0 BENZINA
		1368 (947 MAIS)	0.012628			OPERAZIONI DI RACCOLTA			26341 BENZINA + 183 GASOLIO
		16003			1.16				RISCALDAMENTO SERRA				1 GASOLIO
		1244			0.002772			SPANDIMENTO REFLUI				9999999 GASOLIO
	 */
	@Test
	@Sql(scripts = "/sql/lavorazioni/get_lavorazioni_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/lavorazioni/get_lavorazioni_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getCarburanteAmmissibileTestSuccessful() throws Exception {

		String stringResponse = mockMvc.perform(get(ApiUrls.RICHIESTE.concat("/1999").concat(ApiUrls.CARBURANTE)))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		CarburanteDto response = objectMapper.readValue(stringResponse, CarburanteDto.class);

		assertEquals(840, response.getBenzina());
		assertEquals(27722, response.getGasolio());
		assertEquals(1, response.getGasolioSerre());

	}

	@Test
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void stampaRichiestaCarburante() throws Exception {
		mockResponseTerritorioClient();
		Mockito.when(clock.today()).thenReturn(LocalDate.of(2020, 12, 31));
		Mockito.when(clock.now()).thenReturn(LocalDateTime.of(2021, 12, 13, 12, 13));
		Mockito.when(stampaClient.stampa(Mockito.any(), Mockito.any())).thenReturn(("AA".getBytes()));
		mockMvc.perform(get("/api/v1/richieste/1/stampa"))
		.andExpect(status().isOk());
	}

	@Test
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void stampaRichiestaCarburanteAutorizzata() throws Exception {
		mockMvc.perform(get("/api/v1/richieste/2/stampa")).andExpect(status().isOk());
	}

	@Test
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getPrelieviRichiestaCarburanteSuccessful() throws Exception {
		//richiesta 7809
		String endpoint = "/api/v1/richieste/2021/prelievi";
		mockMvc.perform(get(endpoint).param("cuaa", "BBBFBA66E31F187R"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.dati", Matchers.hasSize(4)))
		.andExpect(jsonPath("$.dati.[0].distributore.denominazione", is(containsStringIgnoringCase("AF PETROLI SPA"))))
		.andExpect(jsonPath("$.dati.[0].carburante.benzina", is(20)))
		.andExpect(jsonPath("$.dati.[0].carburante.gasolio", is(10)));
	}

	@Test
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getPrelieviRichiestaCarburanteRichiestaInesistente() throws Exception {
		String endpoint = "/api/v1/richieste/2021/prelievi";
		this.mockMvc.perform(get(endpoint).param("cuaa", "0"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.dati", Matchers.hasSize(0)))
		.andExpect(jsonPath("$.totale.benzina", is(0)))
		.andExpect(jsonPath("$.totale.gasolio", is(0)))
		.andExpect(jsonPath("$.totale.gasolioSerre", is(0)));
	}

	@Test
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getPrelieviRichiestaCarburanteNessunPrelievo() throws Exception {
		// id richiesta 7579 
		String endpoint = "/api/v1/richieste/2021/prelievi";
		this.mockMvc.perform(get(endpoint).param("cuaa", "GVNLSS64A06H330U"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.dati", Matchers.hasSize(0)));
	}

	private MockHttpServletRequestBuilder buildRequest(Long idRichiestaCarburante) throws IOException {
		String endpoint = String.format("/%s/protocolla?haFirma=true", idRichiestaCarburante);
		Path path = Paths.get("src/test/resources/documentiFirmati/MANDATO_ftoDPDNDR77B03L378L.pdf");
		MockMultipartFile contratto = new MockMultipartFile("documento.pdf", Files.readAllBytes(path));

		MockHttpServletRequestBuilder builder =
				MockMvcRequestBuilders.multipart(ApiUrls.RICHIESTE.concat(endpoint))
				.file("documento", contratto.getBytes())
				.param("id", idRichiestaCarburante.toString())
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE);
		return builder;
	}

	private void mockResponseTerritorioClient() {

		ParticellaDto particella1 = new ParticellaTestBuilder()
				.withInfoCatastali("012", "9999", ".1522", null)
				.addColtura(new ColturaTestBuilder().descrizione("2 - colturaMaisSorgo").withCodifica("008", "001", "042", "000", "000").withSuperficie(5253).build())
				.addColtura(new ColturaTestBuilder().descrizione("6 - colturaPascolo")  .withCodifica("000", "382", "000", "009", "000").withSuperficie(1422).build())
				.build();

		ParticellaDto particella2 = new ParticellaTestBuilder()
				.withInfoCatastali("621", "9999", "01514/A", "A")
				.addColtura(new ColturaTestBuilder().descrizione("8 - colturaLattugheInsalateRadicchi")  .withCodifica("007", "919", "000", "000", "000").withSuperficie(123).build())
				.addColtura(new ColturaTestBuilder().descrizione("8 - colturaLattugheInsalateRadicchi_1").withCodifica("008", "680", "000", "000", "000").withSuperficie(625).build())
				.addColtura(new ColturaTestBuilder().descrizione("10 - colturaViteDaVinoDaTavola")       .withCodifica("005", "410", "000", "000", "507").withSuperficie(1652).build())
				.build();

		// scelta in modo tale che non rientri nel computo della superficie massima. Coltura per cui non posso richiedere contributi
		ParticellaDto particella3 = new ParticellaTestBuilder()
				.withInfoCatastali("621", "9999", "01514/A", "A")
				.addColtura(new ColturaTestBuilder().descrizione("colturaFake").withCodifica("000", "000", "000", "000", "000").withSuperficie(99999).build())
				.build();

		Mockito.when(territorioClient.getColture(Mockito.any(), Mockito.any())).thenReturn(Arrays.asList(particella1,particella2,particella3));
	}
}
