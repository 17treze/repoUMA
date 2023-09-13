package it.tndigitale.a4g.uma.business.service.trasferimenti;

import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;
import java.util.stream.Stream;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.uma.Ruoli;
import it.tndigitale.a4g.uma.business.persistence.entity.TrasferimentoCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.repository.TrasferimentoCarburanteDao;
import it.tndigitale.a4g.uma.business.service.client.UmaAnagraficaClient;
import it.tndigitale.a4g.uma.business.service.utente.AbilitazioniComponent;
import it.tndigitale.a4g.uma.dto.aual.FascicoloAualDto;
import it.tndigitale.a4g.uma.dto.consumi.CarburanteDto;
import it.tndigitale.a4g.uma.dto.trasferimenti.PresentaTrasferimentoDto;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente")
class TrasferimentiCarburanteControllerTest {

	private static final String UMA_07_02_BR1 = "L’azienda agricola selezionata non può ricevere carburante!";
	private static final String UMA_07_02_BR2 = "La quantità digitata eccede i litri di carburante che è possibile trasferire!";
	private static final String UMA_07_03_BR1 = "Attenzione! Non è possibile effettuare più di un trasferimento di carburante";
	private static final String UMA_07_03_BR2 = "Non è possibile modificare questo trasferimento di carburante";


	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TrasferimentoCarburanteDao trasferimentoCarburanteDao;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private AbilitazioniComponent abilitazioniComponent;
//	@MockBean
//	private UmaAnagraficaClient anagraficaClient;
	@MockBean
	private UtenteComponent utenteComponent;

	@BeforeEach
	private void mockUtenteConnesso() throws Exception {
		Mockito.when(abilitazioniComponent.checkRicercaDomandeUma()).thenReturn(true);
		Mockito.when(utenteComponent.haRuolo(Mockito.anyString())).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkModificaTrasferimentoCarburante(Mockito.anyLong())).thenReturn(true);
	}

	@Test
	@Sql(scripts = "/sql/trasferimenti/carburante_ricevuto_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/trasferimenti/carburante_ricevuto_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getCarburanteRicevutoSuccessful() throws Exception {

		mockMvc.perform(get("/api/v1/trasferimenti")
				.param("cuaaDestinatario", "00123890220")
				.param("campagna", "2021"))

		.andExpect(status().is2xxSuccessful())
		.andExpect(jsonPath("$.dati", Matchers.hasSize(2)))
		.andExpect(jsonPath("$.dati.[*].mittente.cuaa", hasItem(containsStringIgnoringCase("MSTFBA79L10H612L"))))
		.andExpect(jsonPath("$.dati.[*].mittente.denominazione", hasItem(containsStringIgnoringCase("MAESTRANZI FABIO"))))
		.andExpect(jsonPath("$.dati.[*].carburante.gasolio", hasItem(1)))
		.andExpect(jsonPath("$.dati.[*].carburante.benzina", hasItem(2)))
		.andExpect(jsonPath("$.dati.[*].carburante.gasolioSerre", hasItem(3)))
		.andExpect(jsonPath("$.totale.benzina", is(102)))
		.andExpect(jsonPath("$.totale.gasolio", is(201)))
		.andExpect(jsonPath("$.totale.gasolioSerre", is(3)));
	}

	@Test
	void getCarburanteRicevutoNoContent() throws Exception {
		mockMvc.perform(get("/api/v1/trasferimenti")
				.param("cuaaMittente", "AAAA")
				.param("campagna", "2021"))
		.andExpect(status().is2xxSuccessful())
		.andExpect(jsonPath("$.dati", Matchers.hasSize(0)))
		.andExpect(jsonPath("$.totale.benzina", is(0)))
		.andExpect(jsonPath("$.totale.gasolio", is(0)))
		.andExpect(jsonPath("$.totale.gasolioSerre", is(0)));
	}

	@Test
	void getCarburanteRicevutoCuaaObbligatorio() throws Exception {
		Exception exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(get("/api/v1/trasferimenti").param("campagna", "2021"));
		}); 

		assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
	}

	@Test
	void getCarburanteRicevutoCampagnaObbligatoria() throws Exception {
		Exception exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(get("/api/v1/trasferimenti").param("cuaaDestinatario", "AAAA"));
		}); 

		assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
	}

	@Test
	@Sql(scripts = "/sql/trasferimenti/carburante_ricevuto_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/trasferimenti/carburante_ricevuto_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getCarburanteTrasferito() throws Exception {
		mockMvc.perform(get("/api/v1/trasferimenti")
				.param("cuaaMittente", "BNLFRN72P43H612A")
				.param("cuaaDestinatario", "00123890220")
				.param("campagna", "2021"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.dati", Matchers.hasSize(1)))
		.andExpect(jsonPath("$.dati.[*].mittente.cuaa", hasItem(containsStringIgnoringCase("BNLFRN72P43H612A"))))
		.andExpect(jsonPath("$.dati.[*].mittente.denominazione", hasItem(containsStringIgnoringCase("BNLFRN72P43H612A"))))
		.andExpect(jsonPath("$.dati.[*].destinatario.cuaa", hasItem(containsStringIgnoringCase("00123890220"))))
		.andExpect(jsonPath("$.dati.[*].destinatario.denominazione", hasItem(containsStringIgnoringCase("DENOMINAZIONE 00123890220"))))
		.andExpect(jsonPath("$.dati.[*].carburante.gasolio", hasItem(200)))
		.andExpect(jsonPath("$.dati.[*].carburante.benzina", hasItem(100)))
		.andExpect(jsonPath("$.dati.[*].carburante.gasolioSerre", hasItem(0)))
		.andExpect(jsonPath("$.totale.gasolio", is(200)))
		.andExpect(jsonPath("$.totale.benzina", is(100)))
		.andExpect(jsonPath("$.totale.gasolioSerre", is(0)));

	}

	/*
	@Test
	@Sql(scripts = "/sql/trasferimenti/carburante_ricevuto_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/trasferimenti/carburante_ricevuto_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getTrasferimentoById() throws Exception {
		FascicoloAualDto fascicoloDto = new FascicoloAualDto(); //.descDeno("DENOMINAZIONE 00123890220");
		Mockito.when(anagraficaClient.getFascicolo(Mockito.anyString())).thenReturn(fascicoloDto);

		Long id = 3L;
		mockMvc.perform(get("/api/v1/trasferimenti/" + id))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id", is(3)))
		.andExpect(jsonPath("$.mittente.cuaa", is(containsStringIgnoringCase("BNLFRN72P43H612A"))))
		.andExpect(jsonPath("$.mittente.denominazione", is(containsStringIgnoringCase("BNLFRN72P43H612A"))))
		.andExpect(jsonPath("$.destinatario.cuaa", is(containsStringIgnoringCase("00123890220"))))
		.andExpect(jsonPath("$.destinatario.denominazione", is(containsStringIgnoringCase("DENOMINAZIONE 00123890220"))))
		.andExpect(jsonPath("$.carburante.gasolio", is(200)))
		.andExpect(jsonPath("$.carburante.benzina", is(100)))
		.andExpect(jsonPath("$.carburante.gasolioSerre", is(0)));

	}
	*/
	
	@Test
	@Sql(scripts = "/sql/trasferimenti/carburante_ricevuto_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/trasferimenti/carburante_ricevuto_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getTrasferimentoByIdNotFound() throws Exception {
		Long id = 3987654321L;

		Exception exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(get("/api/v1/trasferimenti/" + id));
		}); 
		assertEquals(EntityNotFoundException.class, exception.getCause().getClass());
	}

	@Test
	@Transactional
	@Sql(scripts = "/sql/trasferimenti/carburante_ricevuto_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/trasferimenti/carburante_ricevuto_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void deleteTrasferimentoTestSuccessful() throws Exception {
		Long id = 3L;
		mockMvc.perform(delete("/api/v1/trasferimenti/" + id.toString())).andExpect(status().isOk());

		assertTrue(!trasferimentoCarburanteDao.findById(id).isPresent());
	}

	@Test
	@Sql(scripts = "/sql/trasferimenti/carburante_ricevuto_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/trasferimenti/carburante_ricevuto_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void modificaTrasferimentoTestRuoloNonCaa() throws Exception {
		Mockito.when(utenteComponent.haRuolo(Ruoli.DOMANDE_UMA_EDITA_ENTE)).thenReturn(false);
		Long id = 4L;
		CarburanteDto carburanteDto = new CarburanteDto(10, 20, 30);
		mockMvc.perform(put("/api/v1/trasferimenti/" + id).content(objectMapper.writeValueAsString(carburanteDto)))
		.andExpect(status().is4xxClientError());
	}

	@Test
	@Transactional
	@Sql(scripts = "/sql/trasferimenti/carburante_ricevuto_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/trasferimenti/carburante_ricevuto_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void putTrasferimentoTestSuccessful() throws Exception {
		Long id = 4L;
		CarburanteDto carburanteDto = new CarburanteDto(700, 0, 0);
		mockMvc.perform(put("/api/v1/trasferimenti/" + id).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(carburanteDto)))
		.andExpect(status().isOk());

		Optional<TrasferimentoCarburanteModel> trasferimentoOpt = trasferimentoCarburanteDao.findById(id);

		if (!trasferimentoOpt.isPresent()) {
			fail();
		}
		var t = trasferimentoOpt.get();
		assertEquals(0, t.getBenzina());
		assertEquals(0, t.getGasolioSerre());
		assertEquals(700, t.getGasolio());
	}
	
	@Test
	@Sql(scripts = "/sql/trasferimenti/carburante_ricevuto_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/trasferimenti/carburante_ricevuto_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void putTrasferimentoTestValidationFailure() throws Exception {
		Long id = 4L;
		CarburanteDto carburanteDto = new CarburanteDto(100000, 0, 0);

		Exception exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(put("/api/v1/trasferimenti/" + id).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(carburanteDto)));
		}); 

		assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
		assertEquals(UMA_07_02_BR2, exception.getCause().getMessage());
	}

	@Test
	@Transactional
	@Sql(scripts = "/sql/trasferimenti/carburante_ricevuto_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/trasferimenti/carburante_ricevuto_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postTrasferimentoTestSuccessful() throws Exception {
		Long idMittente = 7626L;
		Long idDestinatario = 1111L;
		CarburanteDto carburanteDto = new CarburanteDto(100, 0, 0);
		PresentaTrasferimentoDto presentaTrasferimentoDto = new PresentaTrasferimentoDto()
				.setCarburanteTrasferito(carburanteDto)
				.setIdRichiestaMittente(idMittente)
				.setIdRichiestaDestinatario(idDestinatario);

		String stringResponse = mockMvc.perform(post("/api/v1/trasferimenti").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(presentaTrasferimentoDto)))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		Long idTrasferimento = Long.valueOf(stringResponse);

		Optional<TrasferimentoCarburanteModel> trasferimentoOpt = trasferimentoCarburanteDao.findById(idTrasferimento);

		assertTrue(trasferimentoOpt.isPresent());
	}

	@Test
	@Sql(scripts = "/sql/trasferimenti/carburante_ricevuto_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/trasferimenti/carburante_ricevuto_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postValidaTrasferimentoTestSuccessful() throws Exception {
		Long idMittente = 7626L;
		Long idDestinatario = 1111L;
		PresentaTrasferimentoDto presentaTrasferimentoDto = new PresentaTrasferimentoDto()
				.setIdRichiestaMittente(idMittente)
				.setIdRichiestaDestinatario(idDestinatario);

		mockMvc.perform(post("/api/v1/trasferimenti/valida").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(presentaTrasferimentoDto)))
		.andExpect(status().isOk());
	}

	@ParameterizedTest
	@MethodSource("validazioneTrasferimentoDataProvider")
	@Sql(scripts = "/sql/trasferimenti/validazione_trasferimento_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/trasferimenti/validazione_trasferimento_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postValidaTrasferimentoTestFailure(Long idMittente, Long idDestinatario, String errMsg) throws Exception {
		PresentaTrasferimentoDto presentaTrasferimentoDto = new PresentaTrasferimentoDto()
				.setIdRichiestaMittente(idMittente)
				.setIdRichiestaDestinatario(idDestinatario);

		Exception exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(post("/api/v1/trasferimenti/valida").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(presentaTrasferimentoDto)));
		}); 

		assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
		assertEquals(errMsg, exception.getCause().getMessage());

	}

	static Stream<Arguments> validazioneTrasferimentoDataProvider() {
		return Stream.of(
				Arguments.arguments(1234L, 4321L, UMA_07_03_BR2),
				Arguments.arguments(1235L, 4322L, UMA_07_03_BR2),
				Arguments.arguments(1236L, 4323L, UMA_07_02_BR1),
				Arguments.arguments(1237L, 1241L, UMA_07_02_BR1),
				Arguments.arguments(1238L, 1237L, UMA_07_03_BR1),
				Arguments.arguments(1239L, 1240L, UMA_07_02_BR1)
				);
	}
}
