package it.tndigitale.a4g.zootecnia.business.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import it.tndigitale.a4g.zootecnia.dto.EsitoControlloDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.zootecnia.api.ApiUrls;
import it.tndigitale.a4g.zootecnia.business.persistence.entity.AllevamentoModel;
import it.tndigitale.a4g.zootecnia.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.zootecnia.business.persistence.entity.StrutturaAllevamentoModel;
import it.tndigitale.a4g.zootecnia.business.persistence.repository.FascicoloDao;
import it.tndigitale.a4g.zootecnia.business.service.client.ZootecniaProxyClient;
import it.tndigitale.a4g.zootecnia.business.service.utente.AbilitazioniComponent;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente")
class ZootecniaTest {
	@MockBean private ZootecniaProxyClient zootecniaProxyClient;
	@MockBean Clock clock;
	@MockBean private AbilitazioniComponent abilitazioniComponent;
	
	@Autowired private ObjectMapper objectMapper;
	@Autowired private MockMvc mockMvc;
	@Autowired FascicoloDao fascicoloDao;
	
	@BeforeEach
	private void init() throws Exception {
		Mockito.when(abilitazioniComponent.checkAperturaFascicolo(Mockito.anyString())).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkLetturaFascicolo(Mockito.anyString())).thenReturn(true);
		Mockito.when(clock.now()).thenReturn(LocalDateTime.of(1996, Month.JANUARY, 1, 10, 0));
	}
	
	@Test
	@Sql(scripts = "/sql/get_allevamenti_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/get_allevamenti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getAllevamentiSuccessful() throws Exception {
		mockMvc.perform(get(ApiUrls.ZOOTECNIA + "/PDRTTR69M30C794R/anagrafica-allevamenti"))
		.andExpect(status().is2xxSuccessful())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", Matchers.hasSize(3)));
	}

	@Test
	@Sql(scripts = "/sql/get_allevamenti_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/get_allevamenti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getAllevamentiEmpty() throws Exception {
		mockMvc.perform(get(ApiUrls.ZOOTECNIA + "/PDRTTR69M30C794Z/anagrafica-allevamenti"))
		.andExpect(status().is2xxSuccessful())
		.andExpect(status().isNoContent());
	}
	
	@Test
	@Sql(scripts = "/sql/get_allevamenti_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/get_allevamenti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getAllevamentiIdValicazioneEmpty() throws Exception {
		String params = "?idValidazione=1";
		mockMvc.perform(get(ApiUrls.ZOOTECNIA + "/PDRTTR69M30C794Z/anagrafica-allevamenti" + params))
		.andExpect(status().is2xxSuccessful())
		.andExpect(status().isNoContent());
	}
	
	@Test
	@Sql(scripts = "/sql/get_allevamenti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void aggiornaAllevamentiOk() throws Exception {
		List<it.tndigitale.a4g.proxy.client.model.AnagraficaAllevamentoDto> bdnResult = new ArrayList<it.tndigitale.a4g.proxy.client.model.AnagraficaAllevamentoDto>();
		
		String jsonResponseContent01 = new String(Files.readAllBytes(Paths.get("src/test/resources/json/PDRTTR69M30C794R.json")));
		String jsonResponseContent02 = new String(Files.readAllBytes(Paths.get("src/test/resources/json/PDRTTR69M30C794R2.json")));
		String jsonResponseContent03 = new String(Files.readAllBytes(Paths.get("src/test/resources/json/PDRTTR69M30C794R3.json")));
		
		it.tndigitale.a4g.proxy.client.model.AnagraficaAllevamentoDto allevamento1 = objectMapper.readValue(jsonResponseContent01, it.tndigitale.a4g.proxy.client.model.AnagraficaAllevamentoDto.class);
		it.tndigitale.a4g.proxy.client.model.AnagraficaAllevamentoDto allevamento2 = objectMapper.readValue(jsonResponseContent02, it.tndigitale.a4g.proxy.client.model.AnagraficaAllevamentoDto.class);
		it.tndigitale.a4g.proxy.client.model.AnagraficaAllevamentoDto allevamento3 = objectMapper.readValue(jsonResponseContent03, it.tndigitale.a4g.proxy.client.model.AnagraficaAllevamentoDto.class);
		bdnResult.add(allevamento1);
		bdnResult.add(allevamento2);
		bdnResult.add(allevamento3);
		
		when(zootecniaProxyClient.getAnagraficaAllevamenti(Mockito.anyString(), Mockito.any() )).thenReturn(bdnResult);
		mockMvc.perform(put(ApiUrls.ZOOTECNIA + "/PDRTTR69M30C794R/anagrafica-allevamenti/aggiorna?dataRichiesta=" + LocalDate.now()))
		.andExpect(status().is2xxSuccessful());
		
		mockMvc.perform(get(ApiUrls.ZOOTECNIA + "/PDRTTR69M30C794R/anagrafica-allevamenti"))
		.andExpect(status().is2xxSuccessful())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", Matchers.hasSize(3)));
	}
	
	@Test
	@Sql(scripts = "/sql/get_allevamenti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void aggiornaAllevamentiEmpty() throws Exception {
		List<it.tndigitale.a4g.proxy.client.model.AnagraficaAllevamentoDto> bdnResult = new ArrayList<it.tndigitale.a4g.proxy.client.model.AnagraficaAllevamentoDto>();
		 
		when(zootecniaProxyClient.getAnagraficaAllevamenti(Mockito.anyString(), Mockito.any() )).thenReturn(bdnResult);
		mockMvc.perform(put(ApiUrls.ZOOTECNIA + "/PDRTTR69M30C794Z/anagrafica-allevamenti/aggiorna?dataRichiesta=" + LocalDate.now()))
		.andExpect(status().is2xxSuccessful());
		
		mockMvc.perform(get(ApiUrls.ZOOTECNIA + "/PDRTTR69M30C794Z/anagrafica-allevamenti"))
		.andExpect(status().is2xxSuccessful())
		.andExpect(status().isNoContent());
	}
	
	@Test
	@Sql(scripts = "/sql/get_allevamenti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void aggiornaAllevamentiDataRichiestaMancante() throws Exception {
		List<it.tndigitale.a4g.proxy.client.model.AnagraficaAllevamentoDto> bdnResult = new ArrayList<it.tndigitale.a4g.proxy.client.model.AnagraficaAllevamentoDto>();
		
		String jsonResponseContent01 = new String(Files.readAllBytes(Paths.get("src/test/resources/json/PDRTTR69M30C794R.json")));
		String jsonResponseContent02 = new String(Files.readAllBytes(Paths.get("src/test/resources/json/PDRTTR69M30C794R2.json")));
		String jsonResponseContent03 = new String(Files.readAllBytes(Paths.get("src/test/resources/json/PDRTTR69M30C794R3.json")));
		
		it.tndigitale.a4g.proxy.client.model.AnagraficaAllevamentoDto allevamento1 = objectMapper.readValue(jsonResponseContent01, it.tndigitale.a4g.proxy.client.model.AnagraficaAllevamentoDto.class);
		it.tndigitale.a4g.proxy.client.model.AnagraficaAllevamentoDto allevamento2 = objectMapper.readValue(jsonResponseContent02, it.tndigitale.a4g.proxy.client.model.AnagraficaAllevamentoDto.class);
		it.tndigitale.a4g.proxy.client.model.AnagraficaAllevamentoDto allevamento3 = objectMapper.readValue(jsonResponseContent03, it.tndigitale.a4g.proxy.client.model.AnagraficaAllevamentoDto.class);
		bdnResult.add(allevamento1);
		bdnResult.add(allevamento2);
		bdnResult.add(allevamento3);
		
		when(zootecniaProxyClient.getAnagraficaAllevamenti(Mockito.anyString(), Mockito.any() )).thenReturn(bdnResult);
		mockMvc.perform(put(ApiUrls.ZOOTECNIA + "/PDRTTR69M30C794R/anagrafica-allevamenti/aggiorna"))
		.andExpect(status().is4xxClientError());
	}
	
	@Test
	@Sql(scripts = "/sql/get_allevamenti_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/get_allevamenti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void valida_allevamenti_ok() throws Exception {
		final String cuaa = "PDRTTR69M30C794R";
		final Integer idValidazione = 1;
		mockMvc.perform(put(ApiUrls.ZOOTECNIA_PRIVATE + "/" + cuaa + "/" + idValidazione + "/validazione"))
		.andExpect(status().is2xxSuccessful())
		.andExpect(status().isOk());
		
//		reimposta la security; per qualche motivo dopo mockMvc.perform() viene cancellato
		SecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
		
		Optional<FascicoloModel> fascicoloModelValidatoOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, idValidazione);
		assertTrue(fascicoloModelValidatoOpt.isPresent());
		FascicoloModel fascicoloModelValidato = fascicoloModelValidatoOpt.get();
		List<AllevamentoModel> allevamenti = fascicoloModelValidato.getAllevamenti();
		assertEquals(3, allevamenti.size());
		for (AllevamentoModel allevamento : allevamenti) {
			StrutturaAllevamentoModel strutturaAllevamento = allevamento.getStrutturaAllevamento();
			assertNotNull(strutturaAllevamento);
			assertEquals("033TN004", strutturaAllevamento.getIdentificativo());
			assertEquals("SAN GIACOMO, 44", strutturaAllevamento.getIndirizzo());
			assertEquals("38022", strutturaAllevamento.getCap());
			assertEquals("CALDES", strutturaAllevamento.getComune());
			assertEquals("46.382120", strutturaAllevamento.getLatitudine());
			assertEquals("10.969250", strutturaAllevamento.getLongitudine());
			assertEquals(idValidazione, strutturaAllevamento.getIdValidazione());			
		}
		Optional<FascicoloModel> fascicoloModelLiveOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0);
		assertTrue(fascicoloModelLiveOpt.isPresent());
	}
	
	@Test
	@Sql(scripts = "/sql/get_allevamenti_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/get_allevamenti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getControlloCompletezzaFascicoloFalse() throws Exception {
		ResultActions results = mockMvc.perform(get(ApiUrls.ZOOTECNIA_PRIVATE + "/PDRTTR69M30C794R/controllo-completezza"))
		.andExpect(status().is2xxSuccessful())
		.andExpect(status().isOk());
		
		MvcResult result = results.andReturn();
		String contentAsString = result.getResponse().getContentAsString();
		Map<ControlliFascicoloZootecniaCompletoEnum, EsitoControlloDto> map = objectMapper.readValue(contentAsString, new TypeReference<Map<ControlliFascicoloZootecniaCompletoEnum, EsitoControlloDto>>(){});
		
		assertEquals(1, map.size());
		assertEquals(-3, map.get(ControlliFascicoloZootecniaCompletoEnum.IS_AGGIORNAMENTO_FONTI_ESTERNE_ZOOTECNIA).getEsito());
	}
	
	@Test
	@Sql(scripts = "/sql/get_allevamenti_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/get_allevamenti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getControlloCompletezzaFascicoloTrue() throws Exception {
		ResultActions results = mockMvc.perform(get(ApiUrls.ZOOTECNIA_PRIVATE + "/PDRTTR69M30C794X/controllo-completezza"))
		.andExpect(status().is2xxSuccessful())
		.andExpect(status().isOk());
		
		MvcResult result = results.andReturn();
		String contentAsString = result.getResponse().getContentAsString();
		Map<ControlliFascicoloZootecniaCompletoEnum, EsitoControlloDto> map = objectMapper.readValue(contentAsString, new TypeReference<Map<ControlliFascicoloZootecniaCompletoEnum, EsitoControlloDto>>(){});
		
		assertEquals(1, map.size());
		assertEquals(0, map.get(ControlliFascicoloZootecniaCompletoEnum.IS_AGGIORNAMENTO_FONTI_ESTERNE_ZOOTECNIA).getEsito());
	}
}
