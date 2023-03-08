package it.tndigitale.a4g.uma.business.service.richiesta.lavorazioni;

import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.uma.business.persistence.entity.AmbitoLavorazione;
import it.tndigitale.a4g.uma.business.persistence.entity.FabbisognoModel;
import it.tndigitale.a4g.uma.business.persistence.entity.TipoCarburante;
import it.tndigitale.a4g.uma.business.persistence.repository.FabbisognoDao;
import it.tndigitale.a4g.uma.business.service.client.UmaTerritorioClient;
import it.tndigitale.a4g.uma.business.service.utente.AbilitazioniComponent;
import it.tndigitale.a4g.uma.dto.richiesta.DichiarazioneDto;
import it.tndigitale.a4g.uma.dto.richiesta.FabbisognoDto;
import it.tndigitale.a4g.uma.dto.richiesta.RaggruppamentoLavorazioniDto;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente")
class LavorazioniAltroControllerTest {
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private FabbisognoDao fabbisognoDao;
	@MockBean
	private AbilitazioniComponent abilitazioniComponent;
	@MockBean
	private UmaTerritorioClient territorioClient;
	@MockBean 
	private UtenteComponent utenteComponent;

	@BeforeEach
	private void mockUtenteConnesso() throws Exception {
		Mockito.when(abilitazioniComponent.checkRicercaRichiestaDiCarburante(Mockito.anyLong())).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkRicercaDomandeUma()).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkModificaRichiestaCarburante(Mockito.anyLong())).thenReturn(true);
		Mockito.when(utenteComponent.utenza()).thenReturn("utente");
	}


	@Test
	@Sql(scripts = "/sql/lavorazioni/get_lavorazioni_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/lavorazioni/get_lavorazioni_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getLavorazioniSuccessful() throws Exception {
		String response = mockMvc.perform(get("/api/v1/richieste/2/lavorazioni").param("ambito", AmbitoLavorazione.ALTRO.name()))
				.andExpect(status().is2xxSuccessful())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(3)))
				.andExpect(jsonPath("$.[*].superficieMassima", everyItem(is(nullValue()))))
				.andExpect(jsonPath("$.[*].fabbricati", everyItem(is(nullValue()))))
				.andReturn()
				.getResponse()
				.getContentAsString();

		List<RaggruppamentoLavorazioniDto> dtos = objectMapper.readValue(response, new TypeReference<ArrayList<RaggruppamentoLavorazioniDto>>(){})
				.stream()
				.sorted(Comparator.comparingInt(RaggruppamentoLavorazioniDto::getIndice))
				.collect(Collectors.toList());


		assertEquals(16, dtos.get(0).getIndice());
		assertEquals("PIANTE OFFICINALI-AROMATICHE", dtos.get(0).getNome());
		assertEquals(2, dtos.get(0).getLavorazioni().size());

		assertEquals(18, dtos.get(1).getIndice());
		assertEquals("SILVICOLTURA E MANUTENZIONE BOSCHI", dtos.get(1).getNome());
		assertEquals(3, dtos.get(1).getLavorazioni().size());
	}

	@Test
	void getLavorazioniChiamataIncorretta() throws Exception {
		NestedServletException exception = assertThrows(NestedServletException.class, () -> {
			this.mockMvc.perform(get("/api/v1/richieste/2/lavorazioni"));
		}); 
		assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
	}

	@Test
	void getLavorazioniNessunaRichiesta() throws Exception {
		NestedServletException exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(get("/api/v1/richieste/1243412451/lavorazioni").param("ambito", AmbitoLavorazione.ALTRO.name()));
		}); 
		assertEquals(EntityNotFoundException.class, exception.getCause().getClass());
	}

	@Test
	@Sql(scripts = "/sql/lavorazioni/get_lavorazioni_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/lavorazioni/get_lavorazioni_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getFabbisogniSuccessful() throws Exception {	
		String response = mockMvc.perform(get("/api/v1/richieste/4/fabbisogni").param("ambito", AmbitoLavorazione.ALTRO.name()))
				.andExpect(status().is2xxSuccessful())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(1)))
				.andReturn()
				.getResponse()
				.getContentAsString();

		List<DichiarazioneDto> dtos = objectMapper.readValue(response, new TypeReference<ArrayList<DichiarazioneDto>>(){})
				.stream()
				.sorted(Comparator.comparingLong(DichiarazioneDto::getLavorazioneId))
				.collect(Collectors.toList());

		assertEquals(7146, dtos.get(0).getLavorazioneId());
		assertEquals(1, dtos.get(0).getFabbisogni().size());
		assertEquals(TipoCarburante.GASOLIO, dtos.get(0).getFabbisogni().get(0).getCarburante());
		assertEquals(new BigDecimal("22.0"), dtos.get(0).getFabbisogni().get(0).getQuantita());

	}

	@Test
	@Sql(scripts = "/sql/lavorazioni/get_lavorazioni_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/lavorazioni/get_lavorazioni_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getFabbisogniNessunFabbisogno() throws Exception {	

		mockMvc.perform(get("/api/v1/richieste/52/fabbisogni").param("ambito", AmbitoLavorazione.ALTRO.name()))
		.andExpect(status().isNoContent());

	}

	@Test
	@Sql(scripts = "/sql/lavorazioni/get_lavorazioni_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/lavorazioni/get_lavorazioni_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getFabbisogniErrorNoAmbito() throws Exception {
		Exception exception = assertThrows(NestedServletException.class, () -> {
			mockMvc
			.perform(get("/api/v1/richieste/52/fabbisogni"));
		}); 

		assertEquals("Specificare un tipo di lavorazione", exception.getCause().getMessage());
	}

	@Test
	@Transactional
	@Sql(scripts = "/sql/lavorazioni/post_lavorazioni_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/lavorazioni/post_lavorazioni_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postDichiarazioniSuccessful() throws Exception {

		List<DichiarazioneDto> dichiarazioni = new ArrayList<>();
		List<FabbisognoDto> fabbisogni = new ArrayList<>();

		fabbisogni = Arrays.asList(new FabbisognoDto()
				.setCarburante(TipoCarburante.GASOLIO)
				.setQuantita(BigDecimal.valueOf(22.0)),
				new FabbisognoDto()
				.setCarburante(TipoCarburante.BENZINA)
				.setQuantita(BigDecimal.ZERO),
				new FabbisognoDto()
				.setCarburante(TipoCarburante.BENZINA)
				.setQuantita(null)
				);

		dichiarazioni.add(
				new DichiarazioneDto()
				.setLavorazioneId(7146L)
				.setFabbisogni(fabbisogni)
				);

		String requestBody = objectMapper.writeValueAsString(dichiarazioni);

		mockMvc.perform(post("/api/v1/richieste/311/fabbisogni")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
		.andExpect(status().is2xxSuccessful())
		.andExpect(status().isOk());

		List<FabbisognoModel> listFabbisogno = fabbisognoDao.findByRichiestaCarburante_id(311L)
				.stream().sorted(Comparator.comparingLong(FabbisognoModel::getId))
				.collect(Collectors.toList());

		if (CollectionUtils.isEmpty(listFabbisogno)) {
			fail();
		}
		assertEquals(1, listFabbisogno.size());

		assertEquals(311L, listFabbisogno.get(0).getRichiestaCarburante().getId());
		assertEquals(7146L, listFabbisogno.get(0).getLavorazioneModel().getId());
		assertEquals(new BigDecimal("22.0"), listFabbisogno.get(0).getQuantita());
		assertEquals(TipoCarburante.GASOLIO, listFabbisogno.get(0).getCarburante());

	}

	@Test
	@Transactional
	@Sql(scripts = "/sql/lavorazioni/post_lavorazioni_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/lavorazioni/post_lavorazioni_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postDichirazioniNessunFabbisogno() throws Exception {

		List<DichiarazioneDto> dichiarazioni = new ArrayList<>();
		dichiarazioni.add(
				new DichiarazioneDto()
				.setLavorazioneId(7146L)
				.setFabbisogni(null)
				);
		String requestBody = objectMapper.writeValueAsString(dichiarazioni);

		mockMvc.perform(post("/api/v1/richieste/52/fabbisogni")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
		.andExpect(status().is2xxSuccessful())
		.andExpect(status().isOk());

		List<FabbisognoModel> listFabbisogno = fabbisognoDao.findByRichiestaCarburante_id(52L)
				.stream()
				.filter(f -> AmbitoLavorazione.ALTRO.equals(f.getLavorazioneModel().getGruppoLavorazione().getAmbitoLavorazione()))
				.collect(Collectors.toList());

		assertTrue(CollectionUtils.isEmpty(listFabbisogno));
	}

	@Test
	void postDichirazioniNessunaDichiarazione() throws Exception {

		List<DichiarazioneDto> dichiarazioni = new ArrayList<>();
		String requestBody = objectMapper.writeValueAsString(dichiarazioni);

		NestedServletException exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(post("/api/v1/richieste/52/fabbisogni")
					.contentType(MediaType.APPLICATION_JSON)
					.content(requestBody));
		}); 
		assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
	}

	@Test
	void postDichirazioniNessunaRichiestaDiCarburante() throws Exception {

		List<DichiarazioneDto> dichiarazioni = Arrays.asList(new DichiarazioneDto()
				.setLavorazioneId(7146L)
				.setFabbisogni(null)
				);
		String requestBody = objectMapper.writeValueAsString(dichiarazioni);

		NestedServletException exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(post("/api/v1/richieste/12345678/fabbisogni")
					.contentType(MediaType.APPLICATION_JSON)
					.content(requestBody));
		}); 
		assertEquals(EntityNotFoundException.class, exception.getCause().getClass());
	}

	@Test
	@Sql(scripts = "/sql/lavorazioni/post_lavorazioni_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/lavorazioni/post_lavorazioni_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postDichiarazioniNessunIdLavorazione() throws Exception {

		List<DichiarazioneDto> dichiarazioni = new ArrayList<>();
		List<FabbisognoDto> fabbisogni = new ArrayList<>();

		fabbisogni = Arrays.asList(new FabbisognoDto()
				.setCarburante(TipoCarburante.GASOLIO)
				.setQuantita(BigDecimal.valueOf(22)),
				new FabbisognoDto()
				.setCarburante(TipoCarburante.BENZINA)
				.setQuantita(BigDecimal.ZERO),
				new FabbisognoDto()
				.setCarburante(TipoCarburante.BENZINA)
				.setQuantita(null)
				);

		dichiarazioni.add(
				new DichiarazioneDto()
				.setLavorazioneId(null)
				.setFabbisogni(fabbisogni)
				);

		String requestBody = objectMapper.writeValueAsString(dichiarazioni);
		NestedServletException exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(post("/api/v1/richieste/311/fabbisogni")
					.contentType(MediaType.APPLICATION_JSON)
					.content(requestBody));
		});
		assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
	}

	@Test
	@Sql(scripts = "/sql/lavorazioni/post_lavorazioni_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/lavorazioni/post_lavorazioni_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postDichiarazioniIdLavorazioneNonEsistente() throws Exception {

		List<DichiarazioneDto> dichiarazioni = new ArrayList<>();
		List<FabbisognoDto> fabbisogni = new ArrayList<>();

		fabbisogni = Arrays.asList(new FabbisognoDto()
				.setCarburante(TipoCarburante.GASOLIO)
				.setQuantita(BigDecimal.valueOf(22)),
				new FabbisognoDto()
				.setCarburante(TipoCarburante.BENZINA)
				.setQuantita(BigDecimal.ZERO),
				new FabbisognoDto()
				.setCarburante(TipoCarburante.BENZINA)
				.setQuantita(null)
				);

		dichiarazioni.add(
				new DichiarazioneDto()
				.setLavorazioneId(13084534L)
				.setFabbisogni(fabbisogni)
				);

		String requestBody = objectMapper.writeValueAsString(dichiarazioni);
		NestedServletException exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(post("/api/v1/richieste/311/fabbisogni")
					.contentType(MediaType.APPLICATION_JSON)
					.content(requestBody));
		});
		assertEquals(EntityNotFoundException.class, exception.getCause().getClass());
	}

	@Test
	@Transactional
	@Sql(scripts = "/sql/lavorazioni/post_lavorazioni_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/lavorazioni/post_lavorazioni_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void deleteDichiarazioniSuccessful() throws Exception {

		mockMvc.perform(delete("/api/v1/richieste/311/fabbisogni" + "?tipiCarburante=GASOLIO&tipiCarburante=BENZINA"))
		.andExpect(status().isOk());

		List<FabbisognoModel> listFabbisogno = fabbisognoDao.findByRichiestaCarburante_id(311L)
				.stream().sorted(Comparator.comparingLong(FabbisognoModel::getId))
				.collect(Collectors.toList());
		assertTrue(CollectionUtils.isEmpty(listFabbisogno));
	}

	@Test
	void deleteDichiarazioniNoTipoCarburante() throws Exception {
		mockMvc.perform(delete("/api/v1/richieste/311/fabbisogni")).andExpect(status().is4xxClientError());
	}

}
