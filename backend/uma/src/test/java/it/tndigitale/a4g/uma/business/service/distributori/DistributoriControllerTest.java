package it.tndigitale.a4g.uma.business.service.distributori;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.uma.Ruoli;
import it.tndigitale.a4g.uma.business.persistence.entity.DistributoreModel;
import it.tndigitale.a4g.uma.business.persistence.entity.PrelievoModel;
import it.tndigitale.a4g.uma.business.persistence.repository.DistributoriDao;
import it.tndigitale.a4g.uma.business.persistence.repository.PrelieviDao;
import it.tndigitale.a4g.uma.business.service.client.UmaUtenteClient;
import it.tndigitale.a4g.uma.business.service.utente.AbilitazioniComponent;
import it.tndigitale.a4g.uma.dto.consumi.CarburanteDto;
import it.tndigitale.a4g.uma.dto.distributori.DistributoreDto;
import it.tndigitale.a4g.uma.dto.distributori.PresentaPrelievoDto;
import it.tndigitale.a4g.uma.dto.richiesta.PrelievoDto;
// import it.tndigitale.a4g.utente.client.model.Distributore;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente")
class DistributoriControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private Clock clock;
	@Autowired
	private DistributoriDao distributoriDao;
	@Autowired
	private PrelieviDao prelieviDao;

	@MockBean
	private AbilitazioniComponent abilitazioniComponent;
	@MockBean
	private UmaUtenteClient utenteClient;
	@MockBean 
	private UtenteComponent utenteComponent;

	@BeforeEach
	private void mockUtenteConnesso() throws Exception {
		Mockito.when(utenteComponent.utenza()).thenReturn("utente");
		// Mockito.when(abilitazioniComponent.checkModificaPrelieviDistributore(Mockito.any(), Mockito.any())).thenReturn(true);
		// Mockito.when(abilitazioniComponent.checkRicercaPrelievoDistributore(Mockito.any())).thenReturn(true);
		// Mockito.when(abilitazioniComponent.checkCreaPrelievoDistributore(Mockito.any())).thenReturn(true);
		// Mockito.when(abilitazioniComponent.checkModificaPrelievoDistributore(Mockito.any(), Mockito.any())).thenReturn(true);
	}

	/*
	@Test
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postPrelievoNoDataError() throws Exception {
		PresentaPrelievoDto request = new PresentaPrelievoDto()
				.setIdRichiesta(7579L)
				.setPrelievo(new PrelievoDto().setData(null));

		String endpoint = "/api/v1/distributori/" + String.valueOf(1L) + "/prelievi";

		NestedServletException exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(post(endpoint)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isNoContent());
		});

		assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
		assertEquals("La data del prelievo è obbligatoria!", exception.getCause().getMessage());
	}
	
	@Test
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postPrelievoBR3Error() throws Exception {

		PresentaPrelievoDto request = new PresentaPrelievoDto()
				.setIdRichiesta(7579L)
				.setPrelievo(new PrelievoDto()
						.setData(clock.now())
						.setEstremiDocumentoFiscale("ESTREMI DOCUMENTO FISCALE")
						.setIsConsegnato(false)
						.setCarburante(new CarburanteDto()				
								.setGasolio(0)
								.setBenzina(0)
								.setGasolioSerre(0)));

		String endpoint = "/api/v1/distributori/" + String.valueOf(1L) + "/prelievi";

		NestedServletException exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(post(endpoint)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isNoContent());
		});

		assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
		assertEquals("Attenzione! E’ necessario digitare la quantità di carburante prelevata", exception.getCause().getMessage());
	}
	
	@Test
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postPrelievoBR4Error() throws Exception {

		PresentaPrelievoDto request = new PresentaPrelievoDto()
				.setIdRichiesta(7579L)
				.setPrelievo(new PrelievoDto()
						.setData(clock.now())
						.setEstremiDocumentoFiscale("ESTREMI DOCUMENTO FISCALE")
						.setIsConsegnato(false)
						.setCarburante(new CarburanteDto()
								.setGasolio(999999)
								.setBenzina(0)
								.setGasolioSerre(0)));

		String endpoint = "/api/v1/distributori/" + String.valueOf(1L) + "/prelievi";

		NestedServletException exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(post(endpoint)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isNoContent());
		});

		assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
		assertEquals("La quantità di carburante prelevato non può superare la quantità di carburante prelevabile", exception.getCause().getMessage());
	}
	
	@Test
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postPrelievoRichiestaAutorizzataError() throws Exception {

		PresentaPrelievoDto request = new PresentaPrelievoDto()
				.setIdRichiesta(7616L)
				.setPrelievo(new PrelievoDto()
						.setData(clock.now())
						.setEstremiDocumentoFiscale("ESTREMI DOCUMENTO FISCALE")
						.setIsConsegnato(false)
						.setCarburante(new CarburanteDto()
								.setGasolio(999999)
								.setBenzina(0)
								.setGasolioSerre(0)));

		String endpoint = "/api/v1/distributori/" + String.valueOf(1L) + "/prelievi";

		NestedServletException exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(post(endpoint)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isNoContent());
		});

		assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
		assertEquals("La richiesta di carburante indicata per il prelievo è nello stato IN_COMPILAZIONE", exception.getCause().getMessage());
	}

	@Test
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postPrelievoDichiarazioneConsumiProtocollataError() throws Exception {

		PresentaPrelievoDto request = new PresentaPrelievoDto()
				.setIdRichiesta(3333L)
				.setPrelievo(new PrelievoDto()
						.setData(clock.now())
						.setEstremiDocumentoFiscale("ESTREMI DOCUMENTO FISCALE")
						.setIsConsegnato(false)
						.setCarburante(new CarburanteDto()
								.setGasolio(999999)
								.setBenzina(0)
								.setGasolioSerre(0)));

		String endpoint = "/api/v1/distributori/" + String.valueOf(1L) + "/prelievi";

		NestedServletException exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(post(endpoint)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isNoContent());
		});

		assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
		assertEquals("Esiste una dichiarazione consumi protocollata per la richiesta di carburante indicata per il prelievo", exception.getCause().getMessage());
	}

	@Test
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postPrelievoDataError() throws Exception {

		PresentaPrelievoDto request = new PresentaPrelievoDto()
				.setIdRichiesta(7579L)
				.setPrelievo(new PrelievoDto()
						.setData(clock.now().withYear(2020))
						.setEstremiDocumentoFiscale("ESTREMI DOCUMENTO FISCALE")
						.setIsConsegnato(false)
						.setCarburante(new CarburanteDto()
								.setGasolio(999999)
								.setBenzina(0)
								.setGasolioSerre(0)));

		String endpoint = "/api/v1/distributori/" + String.valueOf(1L) + "/prelievi";

		NestedServletException exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(post(endpoint)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isNoContent());
		});

		assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
		assertEquals("La data indicata per il prelievo non rientra nell'anno di campagna attuale", exception.getCause().getMessage());
	}

	@Test
	@Transactional
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postPrelievoSuccessfulWithImportDistributoreFromUtente() throws Exception {

		PresentaPrelievoDto request = new PresentaPrelievoDto()
				.setIdRichiesta(7579L)
				.setPrelievo(new PrelievoDto()
						.setDistributore(new DistributoreDto().setIdentificativo(1L))
						.setData(clock.now())
						.setEstremiDocumentoFiscale("ESTREMI DOCUMENTO FISCALE")
						.setIsConsegnato(false)
						.setCarburante(new CarburanteDto()
								.setGasolio(1)
								.setBenzina(0)
								.setGasolioSerre(0)));

		Distributore dist = new Distributore();
		dist.setId(1L);
		dist.setDenominazioneAzienda("CRISTOFORETTI - TRENTO");
		dist.setProvincia("TRENTO");
		dist.setIndirizzo("INDIRIZZO");
		dist.setComune("TRENTO");

		Mockito.when(utenteClient.getDistributoreById(Mockito.any())).thenReturn(dist);

		String endpoint = "/api/v1/distributori/" + String.valueOf(1L) + "/prelievi";

		String stringResponse = mockMvc.perform(post(endpoint)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andReturn().getResponse().getContentAsString();

		assertNotNull(stringResponse);


		Optional<DistributoreModel> distributoreOpt = distributoriDao.findByIdentificativo(1L);

		if (!distributoreOpt.isPresent()) {
			fail();
		}
		List<PrelievoModel> prelieviExpected = prelieviDao.findByDistributore_id(distributoreOpt.get().getId());

		assertEquals(1, prelieviExpected.size());
		assertEquals(false, prelieviExpected.get(0).getConsegnato());
		assertEquals(0, prelieviExpected.get(0).getBenzina());
		assertEquals(1, prelieviExpected.get(0).getGasolio());
		assertEquals(0, prelieviExpected.get(0).getGasolioSerre());
		assertEquals(1, prelieviExpected.get(0).getDistributore().getIdentificativo());
		assertEquals(Long.valueOf(stringResponse), prelieviExpected.get(0).getId());		
	}
	
	@Test
	@Transactional
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postPrelievoSuccessful() throws Exception {

		PresentaPrelievoDto request = new PresentaPrelievoDto()
				.setIdRichiesta(7579L)
				.setPrelievo(new PrelievoDto()
						.setData(clock.now())
						.setEstremiDocumentoFiscale("ESTREMI DOCUMENTO FISCALE")
						.setIsConsegnato(false)
						.setCarburante(new CarburanteDto()
								.setGasolio(1)
								.setBenzina(0)
								.setGasolioSerre(0)));


		Distributore dist = new Distributore();
		dist.setId(1L);
		dist.setDenominazioneAzienda("CRISTOFORETTI - TRENTO");
		dist.setProvincia("TRENTO");
		dist.setIndirizzo("INDIRIZZO");
		dist.setComune("TRENTO");

		Mockito.when(utenteClient.getDistributoreById(Mockito.any())).thenReturn(dist);

		String endpoint = "/api/v1/distributori/" + String.valueOf(22L) + "/prelievi";

		String stringResponse = mockMvc.perform(post(endpoint)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andReturn().getResponse().getContentAsString();


		Optional<PrelievoModel> prelievoOpt = prelieviDao.findById(Long.valueOf(stringResponse));

		if (!prelievoOpt.isPresent()) {
			fail();
		}
		var prelievoExpected = prelievoOpt.get();
		assertEquals(false, prelievoExpected.getConsegnato());
		assertEquals(0, prelievoExpected.getBenzina());
		assertEquals(1, prelievoExpected.getGasolio());
		assertEquals(0, prelievoExpected.getGasolioSerre());
		assertEquals(22, prelievoExpected.getDistributore().getIdentificativo());
	}
	
	@Test
	@Transactional
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void putPrelievoSuccessful() throws Exception {
		DistributoreDto dist = new DistributoreDto();
		dist.setIdentificativo(22L);
		dist.setDenominazione("CRISTOFORETTI - TRENTO");
		dist.setProvincia("TRENTO");
		dist.setIndirizzo("INDIRIZZO");
		dist.setComune("TRENTO");

		PrelievoDto request = new PrelievoDto()
				.setData(clock.now())
				.setEstremiDocumentoFiscale("ESTREMI DOCUMENTO FISCALE - MODIFICATO")
				.setIsConsegnato(true)
				.setDistributore(dist)
				.setCarburante(new CarburanteDto()
						.setGasolio(2)
						.setBenzina(0)
						.setGasolioSerre(0));

		String endpoint = "/api/v1/distributori/" + String.valueOf(22L) + "/prelievi/" + String.valueOf(333L);

		String stringResponse = mockMvc.perform(put(endpoint)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();


		Optional<PrelievoModel> prelievoOpt = prelieviDao.findById(Long.valueOf(stringResponse));

		if (!prelievoOpt.isPresent()) {
			fail();
		}
		var prelievoExpected = prelievoOpt.get();
		assertEquals(true, prelievoExpected.getConsegnato());
		assertEquals(0, prelievoExpected.getBenzina());
		assertEquals(2, prelievoExpected.getGasolio());
		assertEquals(0, prelievoExpected.getGasolioSerre());
		assertEquals(22, prelievoExpected.getDistributore().getIdentificativo());
	}
	
	@Test
	@Transactional
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getDistributoriUtenteDistributoreSuccessful() throws Exception {

		var distributoreUtente = new Distributore();
		distributoreUtente.setId(22L);
		distributoreUtente.setProvincia("NA");
		distributoreUtente.setDenominazioneAzienda("DISTRIBUTORE CARBURANTE");

		Mockito.when(utenteClient.getDistributori()).thenReturn(Arrays.asList(distributoreUtente));


		String endpoint = "/api/v1/distributori";

		String stringResponse = mockMvc.perform(get(endpoint).param("campagna", "2021")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		var distributori = objectMapper.readValue(stringResponse, new TypeReference<ArrayList<DistributoreDto>>(){});

		assertNotNull(distributori);
		assertEquals(1, distributori.size());
		prelieviDao.findByDistributore_id(distributori.get(0).getId())
		.stream().allMatch(p -> !p.getConsegnato());

	}
	
	@Test
	@Transactional
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getDistributoriUtenteIstruttoreUmaSuccessful() throws Exception {

		String endpoint = "/api/v1/distributori";
		Mockito.when(utenteComponent.haRuolo(Ruoli.ISTRUTTORE_UMA)).thenReturn(true);

		String stringResponse = mockMvc.perform(get(endpoint).param("campagna", "2020")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		var distributori = objectMapper.readValue(stringResponse, new TypeReference<ArrayList<DistributoreDto>>(){});

		assertNotNull(distributori);
		assertEquals(1, distributori.size());
		Optional<PrelievoModel> prelievoOpt = prelieviDao.findById(1002L);

		if (!prelievoOpt.isPresent()) {fail();}

		assertEquals(8162L, prelievoOpt.get().getDistributore().getId());
		assertEquals(Boolean.TRUE, prelievoOpt.get().getConsegnato());
		assertEquals(1002L, prelievoOpt.get().getId());
	}

	@Test
	void getDistributoriNoDistributoriUtente() throws Exception {

		Mockito.when(utenteClient.getDistributori()).thenReturn(null);

		String endpoint = "/api/v1/distributori";

		mockMvc.perform(get(endpoint)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());

	}
	
	@Test
	@Transactional
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void deletePrelievoSuccessful() throws Exception {

		String endpoint = "/api/v1/distributori/8162/prelievi/5";
		mockMvc.perform(delete(endpoint).contentType(MediaType.APPLICATION_JSON));

		assertTrue(!prelieviDao.findById(5L).isPresent());
	}

	@Test
	@Transactional
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getPrelievi() throws Exception {
		Long idDistributore = 8162L;
		String endpoint = "/api/v1/distributori/%s/prelievi";

		String stringResponse = mockMvc.perform(get(String.format(endpoint, idDistributore))
				.param("dataPrelievo", LocalDateTime.of(2020, 1, 1, 0, 0).toString())
				.param("isConsegnato", Boolean.TRUE.toString())
				.param("campagna", "2020"))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		List<PrelievoDto> response = objectMapper.readValue(stringResponse, new TypeReference<List<PrelievoDto>>(){});

		assertEquals(1, response.size());
		assertEquals(1002, response.get(0).getId());
		assertEquals(0, response.get(0).getCarburante().getBenzina());
		assertEquals(200, response.get(0).getCarburante().getGasolio());
		assertEquals(0, response.get(0).getCarburante().getGasolioSerre());
		assertTrue(response.get(0).getIsConsegnato());

	}
	
	@Test
	@Transactional
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void putPrelieviSuccessful() throws Exception {
		DistributoreDto dist = new DistributoreDto();
		dist.setIdentificativo(8162L);
		dist.setDenominazione("CRISTOFORETTI - TRENTO");
		dist.setProvincia("TRENTO");
		dist.setIndirizzo("INDIRIZZO");
		dist.setComune("TRENTO");

		PrelievoDto request = new PrelievoDto()
				.setId(333L)
				.setIsConsegnato(true)
				.setDistributore(dist)
				.setCarburante(new CarburanteDto()
						.setGasolio(4)
						.setBenzina(0)
						.setGasolioSerre(0));

		String endpoint = "/api/v1/distributori/" + String.valueOf(8162L) + "/prelievi";

		mockMvc.perform(put(endpoint)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(Arrays.asList(request))))
				.andExpect(status().isOk());


		Optional<PrelievoModel> prelievoOpt = prelieviDao.findById(333L);

		if (!prelievoOpt.isPresent()) {
			fail();
		}
		var prelievoExpected = prelievoOpt.get();
		assertEquals(true, prelievoExpected.getConsegnato());
		assertEquals(0, prelievoExpected.getBenzina());
		assertEquals(4, prelievoExpected.getGasolio());
		assertEquals(0, prelievoExpected.getGasolioSerre());
		assertEquals(22, prelievoExpected.getDistributore().getIdentificativo());
	}
	*/
	
}
