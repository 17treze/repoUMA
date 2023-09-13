package it.tndigitale.a4g.uma.business.service.clienti;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.framework.support.CustomCollectors;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.uma.api.ApiUrls;
import it.tndigitale.a4g.uma.business.persistence.entity.ClienteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.ConsumiClienteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.FatturaClienteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.TipoCarburante;
import it.tndigitale.a4g.uma.business.persistence.repository.ClienteDao;
import it.tndigitale.a4g.uma.business.persistence.repository.ConsumiClientiDao;
import it.tndigitale.a4g.uma.business.persistence.repository.FattureClientiDao;
import it.tndigitale.a4g.uma.business.service.client.UmaAnagraficaClient;
import it.tndigitale.a4g.uma.business.service.client.UmaTerritorioClient;
import it.tndigitale.a4g.uma.business.service.utente.AbilitazioniComponent;
import it.tndigitale.a4g.uma.dto.clienti.ClienteConsumiDto;
import it.tndigitale.a4g.uma.dto.clienti.ClienteDto;
import it.tndigitale.a4g.uma.dto.clienti.FatturaClienteDto;
import it.tndigitale.a4g.uma.dto.richiesta.DichiarazioneDto;
import it.tndigitale.a4g.uma.dto.richiesta.FabbisognoDto;
import it.tndigitale.a4g.uma.dto.richiesta.RaggruppamentoLavorazioniDto;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente")
class ClientiControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ClienteDao clienteDao;
	@Autowired
	private FattureClientiDao fattureClientiDao;

	@MockBean
	private Clock clock;
	@Autowired
	private ConsumiClientiDao consumiClientiDao;

	@MockBean
	private AbilitazioniComponent abilitazioniComponent;
//	@MockBean
//	private UmaAnagraficaClient anagraficaClient;
//	@MockBean
//	private UmaTerritorioClient territorioClient;
	@MockBean 
	private UtenteComponent utenteComponent;

	@BeforeEach
	private void mockUtenteConnesso() throws Exception {
		Mockito.when(abilitazioniComponent.checkRicercaDichiarazioneConsumi(Mockito.anyLong())).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkRicercaDomandeUma()).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkModificaDichiarazioneConsumi(Mockito.anyLong())).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkPresentaDomandaUma(Mockito.any())).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkRicercaClientiDichiarazioneConsumi(Mockito.any(), Mockito.any())).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkModificaClientiDichiarazioneConsumi(Mockito.any(), Mockito.any())).thenReturn(true);
		Mockito.when(utenteComponent.utenza()).thenReturn("utente");
	}

	@Transactional
	@Test
	@Sql(scripts = "/sql/consumi/clienti/clienti_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/clienti/clienti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getClientiSuccessful() throws Exception {
		String response = mockMvc.perform(get("/api/v1/consumi/7761/clienti"))
				.andExpect(status().is2xxSuccessful())
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		List<ClienteDto> dtos = objectMapper.readValue(response, new TypeReference<ArrayList<ClienteDto>>(){});

		assertEquals("BBBFBA66E31F187R", dtos.get(0).getCuaa());
		assertEquals("BEBBER FABIO", dtos.get(0).getDenominazione());
		assertEquals("1", dtos.get(0).getIdFascicolo().toString());

		assertEquals("MSTFBA79L10H612L", dtos.get(1).getCuaa());
		assertEquals("MAESTRANZI FABIO", dtos.get(1).getDenominazione());
		assertEquals("2", dtos.get(1).getIdFascicolo().toString());

	}

	@Transactional
	@Test
	@Sql(scripts = "/sql/consumi/clienti/clienti_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/clienti/clienti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getClientiErrorConsumiNotFound() throws Exception {
		Exception exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(get("/api/v1/consumi/7762/clienti"));
		}); 

		assertEquals("Non esiste una dichiarazione consumi", exception.getCause().getMessage());	
	}

	@Transactional
	@Test
	@Sql(scripts = "/sql/consumi/clienti/clienti_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/clienti/clienti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getClientiErrorClientiNotFound() throws Exception {
		mockMvc.perform(get("/api/v1/consumi/7763/clienti"))
		.andExpect(status().is2xxSuccessful())
		.andExpect(status().isNoContent());
	}

	@Transactional
	@Test
	@Sql(scripts = "/sql/consumi/clienti/clienti_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/clienti/clienti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getClienteSuccessful() throws Exception {
		String response = mockMvc.perform(get("/api/v1/consumi/7761/clienti/1"))
				.andExpect(status().is2xxSuccessful())
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		ClienteConsumiDto dto = objectMapper.readValue(response, ClienteConsumiDto.class);

		assertEquals("BBBFBA66E31F187R", dto.getCuaa());
		assertEquals("BEBBER FABIO", dto.getDenominazione());
		assertEquals("1", dto.getIdFascicolo().toString());
		assertEquals(true, dto.isGasolio());
		assertEquals(false, dto.isBenzina());
	}

	@Transactional
	@Test
	@Sql(scripts = "/sql/consumi/clienti/clienti_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/clienti/clienti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getClienteErrorClienteNotFound() throws Exception {
		Exception exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(get("/api/v1/consumi/7761/clienti/33"));
		}); 

		assertEquals("Non esiste un cliente con id 33", exception.getCause().getMessage());	
	}
	/*
	@Transactional
	@Test
	@Sql(scripts = "/sql/consumi/clienti/clienti_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/clienti/clienti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getLavorazioniSuperficieClienti() throws Exception {
		mockResponseTerritorioClient();
		LocalDate primoNovembre = LocalDate.of(2020,Month.NOVEMBER, 1);
		Mockito.when(clock.now()).thenReturn(Clock.ofEndOfDay(primoNovembre));
		Mockito.when(clock.today()).thenReturn(primoNovembre);

		String stringResponse = mockMvc.perform(get("/api/v1/consumi/7761/clienti/1/lavorazioni"))
				.andExpect(status().is2xxSuccessful())
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		List<RaggruppamentoLavorazioniDto> response = objectMapper.readValue(stringResponse, new TypeReference<ArrayList<RaggruppamentoLavorazioniDto>>(){})
				.stream()
				.sorted(Comparator.comparingInt(RaggruppamentoLavorazioniDto::getIndice))
				.collect(Collectors.toList());

		assertEquals(4,response.size());

		assertEquals(2, response.get(0).getIndice());
		assertEquals(6, response.get(1).getIndice());
		assertEquals(8, response.get(2).getIndice());
		assertEquals(10, response.get(3).getIndice());

		assertEquals(14, response.get(0).getLavorazioni().size());
		assertEquals(2, response.get(1).getLavorazioni().size());
		assertEquals(16, response.get(2).getLavorazioni().size());
		assertEquals(10, response.get(3).getLavorazioni().size());

		assertEquals(5253, response.get(0).getSuperficieMassima());
		assertEquals(1422, response.get(1).getSuperficieMassima());
		assertEquals(748, response.get(2).getSuperficieMassima());
		assertEquals(1652, response.get(3).getSuperficieMassima());
	}
	*/
	@Test
	void getLavorazioniSuperficieClientiClienteNonTrovato() throws Exception {
		NestedServletException exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(get("/api/v1/consumi/7761/clienti/123963/lavorazioni"));
		});
		assertEquals(EntityNotFoundException.class, exception.getCause().getClass());
	}

	@Test
	@Sql(scripts = "/sql/consumi/clienti/clienti_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/clienti/clienti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getFabbisogniSuperficie() throws Exception {

		String stringResponse = mockMvc.perform(get("/api/v1/consumi/7761/clienti/2/fabbisogni"))
				.andExpect(status().is2xxSuccessful())
				.andExpect(status().isOk())
				.andReturn()
				.getResponse().getContentAsString();

		List<DichiarazioneDto> response = objectMapper.readValue(stringResponse, new TypeReference<ArrayList<DichiarazioneDto>>(){})
				.stream()
				.sorted(Comparator.comparingLong(DichiarazioneDto::getLavorazioneId))
				.collect(Collectors.toList());

		assertEquals(7, response.size());
		assertEquals(1240L, response.get(0).getLavorazioneId());

		assertTrue(response.get(0).getFabbisogni().stream().anyMatch(f -> {
			return TipoCarburante.GASOLIO.equals(f.getCarburante()) && 
					new BigDecimal("1.4").compareTo(f.getQuantita()) == 0;
		}));
		assertTrue(response.get(6).getFabbisogni().stream().anyMatch(f -> {
			return TipoCarburante.BENZINA.equals(f.getCarburante()) && 
					new BigDecimal("99").compareTo(f.getQuantita()) == 0;
		}));
	}

	@Test
	@Sql(scripts = "/sql/consumi/clienti/clienti_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/clienti/clienti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getAllegatiCliente() throws Exception {
		String response = mockMvc.perform(get("/api/v1/consumi/7761/clienti/2/allegati"))
				.andExpect(status().is2xxSuccessful())
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		List<FatturaClienteDto> fatturaDtos = objectMapper.readValue(response, new TypeReference<ArrayList<FatturaClienteDto>>(){});

		assertEquals(2L, fatturaDtos.get(0).getIdCliente());
		assertEquals("allegato_1.pdf", fatturaDtos.get(0).getNomeFile());

	}
	/*
	@Test
	@Sql(scripts = "/sql/consumi/clienti/clienti_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/clienti/clienti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postClientiSuccessful() throws Exception {
		mockValidator();

		String response2 = mockMvc.perform(buildRequest(7763L, 3L, "SUCCESS"))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		Long idCliente = objectMapper.readValue(response2, Long.class);

		Optional<ClienteModel> clienteOpt = clienteDao.findById(idCliente);


		if (clienteOpt.isEmpty()) {
			fail();
		}

		ClienteModel cliente = clienteOpt.get(); 

		List<FatturaClienteModel> fatturaClienteList = fattureClientiDao.findByCliente(cliente);

		if (CollectionUtils.isEmpty(fatturaClienteList)) {
			fail();
		}

		assertEquals("DPDNDR77B03L378L", cliente.getCuaa());
		assertEquals("7763", cliente.getDichiarazioneConsumi().getId().toString());
		assertEquals("DEPEDRI ANDREA", cliente.getDenominazione());
		assertEquals("3", cliente.getIdFascicolo().toString());


		assertEquals("allegato_0.pdf", fatturaClienteList.get(0).getNomeFile());
		assertEquals("allegato_1.pdf", fatturaClienteList.get(1).getNomeFile());
	}
	*/
	@Test
	@Sql(scripts = "/sql/consumi/clienti/clienti_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/clienti/clienti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postAllegatiClientiSuccessful() throws Exception {
		mockMvc.perform(buildRequestSalvaAllegati(7761L, 2L))
		.andExpect(status().isOk());

		Optional<ClienteModel> clienteOpt = clienteDao.findById(2L);

		if (clienteOpt.isEmpty()) {
			fail();
		}

		ClienteModel cliente = clienteOpt.get(); 

		List<FatturaClienteModel> fatturaClienteList = fattureClientiDao.findByCliente(cliente);

		if (CollectionUtils.isEmpty(fatturaClienteList)) {
			fail();
		}

		assertEquals("MSTFBA79L10H612L", cliente.getCuaa());
		assertEquals("7761", cliente.getDichiarazioneConsumi().getId().toString());

		assertEquals("allegato_new.pdf", fatturaClienteList.get(0).getNomeFile());
	}
	/*
	@Test
	@Sql(scripts = "/sql/consumi/clienti/clienti_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/clienti/clienti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postValidaClientiSuccessful() throws Exception {
		mockValidator();

		mockMvc.perform(post("/api/v1/consumi/7763/clienti/valida").param("idFascicolo", "3"))
		.andExpect(status().isOk());
	}
	*/
	@Test
	@Sql(scripts = "/sql/consumi/clienti/clienti_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/clienti/clienti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postClientiErrorNotPdf() throws Exception {
		Exception exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(buildRequest(7763L, 3L, "NOT_PDF"));
		}); 

		assertEquals("Il formato del file caricato non è riconosciuto. Si prega di caricare un file pdf", exception.getCause().getMessage());	
	}

	@Test
	@Sql(scripts = "/sql/consumi/clienti/clienti_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/clienti/clienti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postClientiErrorNoAllegati() throws Exception {
		Exception exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(buildRequest(7763L, 3L, "NO_ALLEGATI"));
		}); 

		assertEquals("E’ obbligatorio inserire almeno un allegato", exception.getCause().getMessage());	
	}

	@Test
	@Sql(scripts = "/sql/consumi/clienti/clienti_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/clienti/clienti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postClientiErrorLimiteAllegati() throws Exception {
		Exception exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(buildRequest(7763L, 3L, "LIMITE_ALLEGATI"));
		}); 

		assertEquals("Non è possibile inserire più di 5 allegati", exception.getCause().getMessage());	
	}


	@Test
	@Sql(scripts = "/sql/consumi/clienti/clienti_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/clienti/clienti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postClientiErrorLimiteNomeFile() throws Exception {
		Exception exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(buildRequest(7763L, 3L, "LIMITE_NOME_FILE"));
		}); 

		assertEquals("Il nome del file supera il limite massimo di 50 caratteri", exception.getCause().getMessage());	
	}

	@Test
	@Transactional
	@Sql(scripts = "/sql/consumi/clienti/clienti_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/clienti/clienti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postConsumiClientiSuccessful() throws Exception {
		DichiarazioneDto dichiarazione1 = new DichiarazioneDto()
				.setFabbisogni(Arrays.asList(
						new FabbisognoDto().setCarburante(TipoCarburante.BENZINA).setQuantita(BigDecimal.valueOf(101))))
				.setLavorazioneId(1262L);

		DichiarazioneDto dichiarazione2 = new DichiarazioneDto()
				.setFabbisogni(Arrays.asList(
						new FabbisognoDto().setCarburante(TipoCarburante.GASOLIO).setQuantita(BigDecimal.valueOf(1.15))))
				.setLavorazioneId(1235L);

		String requestBody = objectMapper.writeValueAsString(Arrays.asList(dichiarazione1,dichiarazione2));

		mockMvc.perform(post("/api/v1/consumi/7763/clienti/1/fabbisogni")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
		.andExpect(status().isOk());

		ConsumiClienteModel consumiCliente1 = consumiClientiDao.findAll().stream()
				.filter(consumoCliente -> consumoCliente.getCliente().getId() == 1L && consumoCliente.getLavorazioneModel().getId() == 1262L)
				.collect(CustomCollectors.toSingleton());

		assertEquals(TipoCarburante.BENZINA, consumiCliente1.getCarburante());
		assertEquals("101", consumiCliente1.getQuantita().toString());
		assertEquals(1262L, consumiCliente1.getLavorazioneModel().getId());

		ConsumiClienteModel consumiCliente2 = consumiClientiDao.findAll().stream()
				.filter(consumoCliente -> consumoCliente.getCliente().getId() == 1L && consumoCliente.getLavorazioneModel().getId() == 1235L)
				.collect(CustomCollectors.toSingleton());

		assertEquals(TipoCarburante.GASOLIO, consumiCliente2.getCarburante());
		assertEquals("1.15", consumiCliente2.getQuantita().toString());
		assertEquals(1235L, consumiCliente2.getLavorazioneModel().getId());
	}

	@Transactional
	@Test
	@Sql(scripts = "/sql/consumi/clienti/clienti_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/clienti/clienti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void deleteRichiesta() throws Exception {
		mockMvc.perform(delete("/api/v1/consumi/7761/clienti/2"))
		.andExpect(status().is2xxSuccessful())
		.andExpect(status().isOk())
		.andReturn();

		Optional<ClienteModel> clienteDeleted = clienteDao.findById(2L);

		if (clienteDeleted.isPresent()) {
			fail();
		}
	}

	private void mockValidator() {
		/*
		FascicoloAgsDto fascicolo = new FascicoloAgsDto();
		fascicolo.setStato(StatoEnum.VALIDO);
		fascicolo.setCuaa("DPDNDR77B03L378L");
		fascicolo.setDenominazione("DEPEDRI ANDREA");
		fascicolo.setIdAgs(3L);

		MovimentoValidazioneFascicoloAgsDto movFas = new MovimentoValidazioneFascicoloAgsDto();
		movFas.setDataUltimaValidazionePositiva(LocalDateTime.of(LocalDateTime.now().getYear() - 1, 10, 1, 23, 59));
		movFas.setCuaa("DPDNDR77B03L378L");
		movFas.setDenominazione("DEPEDRI ANDREA");
		movFas.setIdFascicolo(3L);
		Mockito.when(anagraficaClient.getMovimentazioniValidazioneFascicolo(Mockito.any(), Mockito.any())).thenReturn(movFas);
		Mockito.when(clock.now()).thenReturn(LocalDateTime.now());
		*/
	}

	private MockHttpServletRequestBuilder buildRequestSalvaAllegati(Long idDichiarazioneConsumi, Long idCliente) throws IOException {
		String endpoint = String.format("/%s/clienti/%s/allegati", idDichiarazioneConsumi, idCliente);
		Path path = Paths.get("src/test/resources/documentiFirmati/MANDATO_ftoDPDNDR77B03L378L.pdf");


		MockHttpServletRequestBuilder builder =
				MockMvcRequestBuilders.multipart(ApiUrls.CONSUMI.concat(endpoint))
				.file(new MockMultipartFile("allegati", "allegato_new.pdf", MediaType.APPLICATION_PDF_VALUE, Files.readAllBytes(path)))
				.param("id", idDichiarazioneConsumi.toString())
				.param("idCliente", idCliente.toString())
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE);
		return builder;
	}

	private MockHttpServletRequestBuilder buildRequest(Long idDichiarazioneConsumi, Long idFascicolo, String error) throws IOException {
		String endpoint = String.format("/%s/clienti", idDichiarazioneConsumi);
		Path path = Paths.get("src/test/resources/documentiFirmati/MANDATO_ftoDPDNDR77B03L378L.pdf");

		if (error.equals("SUCCESS")) {
			MockHttpServletRequestBuilder builder =
					MockMvcRequestBuilders.multipart(ApiUrls.CONSUMI.concat(endpoint))
					.file(new MockMultipartFile("allegati", "allegato_0.pdf", MediaType.APPLICATION_PDF_VALUE, Files.readAllBytes(path)))
					.file(new MockMultipartFile("allegati", "allegato_1.pdf", MediaType.APPLICATION_PDF_VALUE, Files.readAllBytes(path)))
					.param("id", idDichiarazioneConsumi.toString())
					.param("idFascicolo", idFascicolo.toString())
					.contentType(MediaType.MULTIPART_FORM_DATA_VALUE);
			return builder;
		}

		if (error.equals("NOT_PDF")) {
			MockHttpServletRequestBuilder builder =
					MockMvcRequestBuilders.multipart(ApiUrls.CONSUMI.concat(endpoint))
					.file(new MockMultipartFile("allegati", "allegato_0.pdf", "", Files.readAllBytes(path)))
					.file(new MockMultipartFile("allegati", "allegato_1.pdf", "", Files.readAllBytes(path)))
					.param("id", idDichiarazioneConsumi.toString())
					.param("idFascicolo", idFascicolo.toString())
					.contentType(MediaType.MULTIPART_FORM_DATA_VALUE);
			return builder;
		}

		if (error.equals("NO_ALLEGATI")) {
			MockHttpServletRequestBuilder builder =
					MockMvcRequestBuilders.multipart(ApiUrls.CONSUMI.concat(endpoint))
					.param("id", idDichiarazioneConsumi.toString())
					.param("idFascicolo", idFascicolo.toString())
					.contentType(MediaType.MULTIPART_FORM_DATA_VALUE);
			return builder;
		}

		if (error.equals("LIMITE_ALLEGATI")) {
			MockHttpServletRequestBuilder builder =
					MockMvcRequestBuilders.multipart(ApiUrls.CONSUMI.concat(endpoint))
					.file(new MockMultipartFile("allegati", "allegato_0.pdf", "", Files.readAllBytes(path)))
					.file(new MockMultipartFile("allegati", "allegato_1.pdf", "", Files.readAllBytes(path)))
					.file(new MockMultipartFile("allegati", "allegato_2.pdf", "", Files.readAllBytes(path)))
					.file(new MockMultipartFile("allegati", "allegato_3.pdf", "", Files.readAllBytes(path)))
					.file(new MockMultipartFile("allegati", "allegato_4.pdf", "", Files.readAllBytes(path)))
					.file(new MockMultipartFile("allegati", "allegato_5.pdf", "", Files.readAllBytes(path)))
					.param("id", idDichiarazioneConsumi.toString())
					.param("idFascicolo", idFascicolo.toString())
					.contentType(MediaType.MULTIPART_FORM_DATA_VALUE);
			return builder;
		}


		if (error.equals("LIMITE_NOME_FILE")) {
			MockHttpServletRequestBuilder builder =
					MockMvcRequestBuilders.multipart(ApiUrls.CONSUMI.concat(endpoint))
					.file(new MockMultipartFile("allegati", "allegatiallegatiallegatiallegatiallegatiallegatiallegati.pdf", MediaType.APPLICATION_PDF_VALUE, Files.readAllBytes(path)))
					.param("id", idDichiarazioneConsumi.toString())
					.param("idFascicolo", idFascicolo.toString())
					.contentType(MediaType.MULTIPART_FORM_DATA_VALUE);
			return builder;
		}

		return null;
	}

	private void mockResponseTerritorioClient() {
		/*
		ParticellaDto particella1 = new ParticellaBuilder()
				.withInfoCatastali("012", "9999", ".1522", null)
				.addColtura(new ColturaBuilder().descrizione("2 - colturaMaisSorgo").withCodifica("008", "001", "042", "000", "000").withSuperficie(5253).build())
				.addColtura(new ColturaBuilder().descrizione("6 - colturaPascolo")  .withCodifica("000", "382", "000", "009", "000").withSuperficie(1422).build())
				.build();

		ParticellaDto particella2 = new ParticellaBuilder()
				.withInfoCatastali("621", "9999", "01514/A", "A")
				.addColtura(new ColturaBuilder().descrizione("8 - colturaLattugheInsalateRadicchi")  .withCodifica("007", "919", "000", "000", "000").withSuperficie(123).build())
				.addColtura(new ColturaBuilder().descrizione("8 - colturaLattugheInsalateRadicchi_1").withCodifica("008", "680", "000", "000", "000").withSuperficie(625).build())
				.addColtura(new ColturaBuilder().descrizione("10 - colturaViteDaVinoDaTavola")       .withCodifica("005", "410", "000", "000", "507").withSuperficie(1652).build())
				.build();

		// scelta in modo tale che non rientri nel computo della superficie massima. Coltura per cui non posso richiedere contributi
		ParticellaDto particella3 = new ParticellaBuilder()
				.withInfoCatastali("621", "9999", "01514/A", "A")
				.addColtura(new ColturaBuilder().descrizione("colturaFake").withCodifica("000", "000", "000", "000", "000").withSuperficie(99999).build())
				.build();

		Mockito.when(territorioClient.getColture(Mockito.any(), Mockito.any())).thenReturn(Arrays.asList(particella1,particella2,particella3));
		*/
	}

	/*
	private class ColturaBuilder {

		private ColturaDto colturaDto;
		ColturaBuilder() {
			colturaDto = new ColturaDto();
		}

		ColturaBuilder descrizione(String descrizioneUtileAlTest) {
			return this;
		}

		ColturaBuilder withCodifica(String destinazioneUso, String suolo, String qualita, String uso, String varieta) {
			CodificaColtura codificaColtura = new CodificaColtura();
			codificaColtura.setCodiceDestinazioneUso(destinazioneUso);
			codificaColtura.setCodiceSuolo(suolo);
			codificaColtura.setCodiceQualita(qualita);
			codificaColtura.setCodiceUso(uso);
			codificaColtura.setCodiceVarieta(varieta);
			colturaDto.setCodifica(codificaColtura);
			return this;
		}

		ColturaBuilder withSuperficie(Integer superficieAccertata) {
			colturaDto.setSuperficieAccertata(superficieAccertata);
			return this;
		}

		ColturaDto build() {
			return colturaDto;
		}
	}

	private class ParticellaBuilder {

		private ParticellaDto particellaDto;

		ParticellaBuilder() {
			particellaDto = new ParticellaDto();
			particellaDto.setColture(new ArrayList<>());
		}

		ParticellaBuilder withInfoCatastali(String codNazionale, String foglio,String numero, String sub) {
			particellaDto.setCodiceNazionale(codNazionale);
			particellaDto.setFoglio(foglio);
			particellaDto.setNumero(numero);
			particellaDto.setSubalterno(sub);
			return this;
		}

		ParticellaBuilder addColtura(ColturaDto coltura) {
			particellaDto.addColtureItem(coltura);
			return this;
		}

		ParticellaDto build() {
			return particellaDto;
		}
	}
	*/
}
