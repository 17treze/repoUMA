package it.tndigitale.a4g.fascicolo.anagrafica.api;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.FascicoloDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.PersonaGiuridicaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.PersonaConCaricaService;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaProxyClient;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaUtenteClient;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.StampaComponent;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloValidazioneEnum;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloService;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.DatiAperturaFascicoloDto;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.proxy.client.model.PersonaFisicaDto;
import it.tndigitale.a4g.proxy.client.model.PersonaGiuridicaDto;
import it.tndigitale.a4g.utente.client.model.RappresentaIlModelloDelProfiloDiUnUtente;
import it.tndigitale.a4g.utente.client.model.RappresentaIlModelloPerRappresentareUnUtenteDelSistema;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class FascicoloDetenzioneAutonomaControllerTest {
	@MockBean AnagraficaUtenteClient anagraficaUtenteClient;
	@MockBean AnagraficaProxyClient anagraficaProxyClient;
	@MockBean Clock clock;
	@MockBean PersonaConCaricaService personaConCaricaService;
	@MockBean StampaComponent stampaComponent;
	
	@Autowired FascicoloDetenzioneAutonomaController fascicoloDetenzioneAutonomaController;
	@Autowired FascicoloService fascicoloService;
	@Autowired FascicoloDao fascicoloDao;
	@Autowired PersonaGiuridicaDao personaGiuridicaDao;
	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper objectMapper;
	@Autowired PlatformTransactionManager transactionManager;
	
    private TransactionTemplate transactionTemplate;
    private LocalDate testDayLocalDate = LocalDate.of(2020, Month.JANUARY, 1);
    private LocalDateTime testDayLocalDateTime = LocalDateTime.of(2020, Month.FEBRUARY, 12, 1, 1);
    private static final String PROFILO_AZIENDA = "azienda";
    private static final String PROFILO_VITICOLO = "viticolo";
    
	@BeforeEach
	void initialize() {
		transactionTemplate = new TransactionTemplate(transactionManager);
		Mockito.when(clock.today()).thenReturn(testDayLocalDate);
		Mockito.when(clock.now()).thenReturn(testDayLocalDateTime);
	}
    
	String getResponseAnagraficaImpresa(String cf) throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/fascicolo/anagraficaimpresa/".concat(cf).concat(".json")));
		return objectMapper.writeValueAsString(response);
	}
	
	String getResponseAnagrafeTributaria(String cf) throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/fascicolo/anagrafetributaria/".concat(cf).concat(".json")));
		return objectMapper.writeValueAsString(response);
	}
	
    void setupMockitoAperturaFascicoloDetenzioneAutonomaPersonaGiuridica(String codiceFiscale) throws Exception {
		PersonaGiuridicaDto personaGiuridicaDto =
				objectMapper.readValue(getResponseAnagrafeTributaria(codiceFiscale), PersonaGiuridicaDto.class);
		PersonaGiuridicaDto personaGiuridicaDtoParix =
				objectMapper.readValue(getResponseAnagraficaImpresa(codiceFiscale), PersonaGiuridicaDto.class);

		Mockito.when(anagraficaProxyClient.getPersonaGiuridicaAnagrafeTributaria(codiceFiscale)).thenReturn(personaGiuridicaDto);
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagrafeTributaria("LNLMTB74A31F205S")).thenReturn(objectMapper.readValue(getResponseAnagrafeTributaria("LNLMTB74A31F205S"), PersonaFisicaDto.class));
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagrafeTributaria("LNLMCL67R20L378W")).thenReturn(objectMapper.readValue(getResponseAnagrafeTributaria("LNLMCL67R20L378W"), PersonaFisicaDto.class));		
		Mockito.when(anagraficaProxyClient.getPersonaGiuridicaAnagraficaImpresa(Mockito.eq(codiceFiscale), Mockito.anyString())).thenReturn(personaGiuridicaDtoParix);
	}
    
    void setupMockitoAperturaFascicoloDetenzioneAutonomaPersonaFisica(String cuaa) throws Exception {
		PersonaFisicaDto personaFisicaDto =
				objectMapper.readValue(getResponseAnagrafeTributaria(cuaa), PersonaFisicaDto.class);
		PersonaFisicaDto personaFisicaDtoParix =
				objectMapper.readValue(getResponseAnagraficaImpresa(cuaa), PersonaFisicaDto.class);

		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagrafeTributaria(cuaa)).thenReturn(personaFisicaDto);
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagraficaImpresa(Mockito.eq(cuaa), Mockito.anyString())).thenReturn(personaFisicaDtoParix);
	}
    
	void mockUserSetupUtenteConnessoEProfilo(String userCodiceFiscale, String profiloUtente) {
		RappresentaIlModelloPerRappresentareUnUtenteDelSistema user = new RappresentaIlModelloPerRappresentareUnUtenteDelSistema();
		user.setIdentificativo(userCodiceFiscale);
		user.setCodiceFiscale(userCodiceFiscale);
		user.setNome(userCodiceFiscale);
		List<RappresentaIlModelloDelProfiloDiUnUtente> profili = new ArrayList<>();
		RappresentaIlModelloDelProfiloDiUnUtente profilo = new RappresentaIlModelloDelProfiloDiUnUtente();
		profilo.setIdentificativo(profiloUtente);
		profili.add(profilo);
		user.setProfili(profili);
		Mockito.when(anagraficaUtenteClient.getUtenteConnesso()).thenReturn(user);
	}
	
	private void mockStampaComponentOkStuff() throws RestClientException, IOException {
		byte[] pdf = {'1', '2', '3'};
		Mockito.when(stampaComponent.stampaPDFA(
				Mockito.anyString(), Mockito.anyString())).thenReturn(
						pdf
						);
	}
	
	@Test
	@WithMockUser(username = "LNLMTB74A31F205S")
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void apertura_fascicolo_persona_giuridica_OK() throws Exception {
		String cuaaPersonaGiuridica = "00123890220";
		String cfRappresentanteLegale = "LNLMTB74A31F205S"; 
		setupMockitoAperturaFascicoloDetenzioneAutonomaPersonaGiuridica(cuaaPersonaGiuridica);
		mockUserSetupUtenteConnessoEProfilo(cfRappresentanteLegale, PROFILO_AZIENDA);
		String endpoint = String.format("/%s/apri", cuaaPersonaGiuridica);
		
		MockHttpServletRequestBuilder requestBuilder =
				MockMvcRequestBuilders.multipart(ApiUrls.FASCICOLO_DETENZIONE_AUTONOMA.concat(endpoint))
				.param("codiceFiscale", cuaaPersonaGiuridica)
				.contentType(MediaType.APPLICATION_JSON);
		
		requestBuilder.with(request -> {
            request.setMethod(HttpMethod.POST.name());
            return request;
        });
		this.mockMvc.perform(requestBuilder);
		
		FascicoloModel fascicoloSaved = transactionTemplate.execute(status -> {
			Optional<FascicoloModel> resultOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaaPersonaGiuridica, 0);
			assertEquals(true, resultOpt.isPresent());
			assertEquals(false, resultOpt.get().getDetenzioni().isEmpty());
			return resultOpt.get();
		});
		
		assertEquals("FERRARI F.LLI LUNELLI S.P.A.", fascicoloSaved.getDenominazione());
	}
	
	@Test
	@WithMockUser(username = "LNLMTB74A31F205X")
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void apertura_fascicolo_persona_giuridica_KO_Forbidden() throws Exception {
		String cuaaPersonaGiuridica = "00123890220";
		String cfRappresentanteLegale = "LNLMTB74A31F205X"; 
		setupMockitoAperturaFascicoloDetenzioneAutonomaPersonaGiuridica(cuaaPersonaGiuridica);
		mockUserSetupUtenteConnessoEProfilo(cfRappresentanteLegale, PROFILO_AZIENDA);
		
		ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
			fascicoloDetenzioneAutonomaController.apri(cuaaPersonaGiuridica);
		});
		assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
		assertEquals(FascicoloValidazioneEnum.CUAA_DIVERSO_CODICEFISCALE_UTENTE.toString(), exception.getReason());
	}
	
	@Test
	@WithMockUser(username = "LNLMTB74A31F205S")
	@Sql(scripts = "/sql/fascicolo/persona/aggiornamentoFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aggiornamentoFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void apertura_fascicolo_persona_giuridica_KO_Fascicolo_esistente() throws Exception {
		String cuaaPersonaGiuridica = "00123890220";
		String cfRappresentanteLegale = "LNLMTB74A31F205S"; 
		setupMockitoAperturaFascicoloDetenzioneAutonomaPersonaGiuridica(cuaaPersonaGiuridica);
		mockUserSetupUtenteConnessoEProfilo(cfRappresentanteLegale, PROFILO_AZIENDA);
		
		ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
			fascicoloDetenzioneAutonomaController.apri(cuaaPersonaGiuridica);
		});
		assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
		assertEquals(FascicoloValidazioneEnum.FASCICOLO_LOCALE_ESISTENTE.toString(), exception.getReason());
	}
	
	@Test
	@WithMockUser(username = "BLDGDN61M17L378K")
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void apertura_fascicolo_persona_fisica_OK() throws Exception {
		String cuaaPersonaFisica = "BLDGDN61M17L378K";
		setupMockitoAperturaFascicoloDetenzioneAutonomaPersonaFisica(cuaaPersonaFisica);
		mockUserSetupUtenteConnessoEProfilo(cuaaPersonaFisica, PROFILO_AZIENDA);
		String endpoint = String.format("/%s/apri", cuaaPersonaFisica);
		
		MockHttpServletRequestBuilder requestBuilder =
				MockMvcRequestBuilders.multipart(ApiUrls.FASCICOLO_DETENZIONE_AUTONOMA.concat(endpoint))
				.param("codiceFiscale", cuaaPersonaFisica)
				.contentType(MediaType.APPLICATION_JSON);
		
		requestBuilder.with(request -> {
            request.setMethod(HttpMethod.POST.name());
            return request;
        });
		this.mockMvc.perform(requestBuilder);
		
		FascicoloModel fascicoloSaved = transactionTemplate.execute(status -> {
			Optional<FascicoloModel> resultOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaaPersonaFisica, 0);
			assertEquals(true, resultOpt.isPresent());
			assertEquals(false, resultOpt.get().getDetenzioni().isEmpty());
			return resultOpt.get();
		});
		
		assertEquals("BALDESSARI               GIORDANO", fascicoloSaved.getDenominazione());
	}
	
	@Test
	@WithMockUser(username = "BLDGDN61M17L378Z")
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void apertura_fascicolo_persona_fisica_KO_Forbidden() throws Exception {
		String cuaaPersonaFisica = "BLDGDN61M17L378K";
		String cfUtenteConnessoForbidden = "BLDGDN61M17L378Z";
		setupMockitoAperturaFascicoloDetenzioneAutonomaPersonaFisica(cuaaPersonaFisica);
		mockUserSetupUtenteConnessoEProfilo(cfUtenteConnessoForbidden, PROFILO_AZIENDA);
		
		ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
			fascicoloDetenzioneAutonomaController.apri(cuaaPersonaFisica);
		});
		assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
		assertEquals(FascicoloValidazioneEnum.CUAA_DIVERSO_CODICEFISCALE_UTENTE.toString(), exception.getReason());
	}
	
	@Test
	@WithMockUser(username = "BLDGDN61M17L378K")
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void apertura_fascicolo_persona_fisica_KO_Profilo_non_valido() throws Exception {
		String cuaaPersonaFisica = "BLDGDN61M17L378K";
		setupMockitoAperturaFascicoloDetenzioneAutonomaPersonaFisica(cuaaPersonaFisica);
		mockUserSetupUtenteConnessoEProfilo(cuaaPersonaFisica, PROFILO_VITICOLO);
		
		assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> {
			fascicoloDetenzioneAutonomaController.apri(cuaaPersonaFisica);
		});
	}
	
	@Test
	@WithMockUser(username = "DPDFRZ65C21C794B")
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTestFascicoloEsistentePersoneFisiche_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloPersonaFisicaTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void apertura_fascicolo_persona_fisica_KO_Fascicolo_gia_esistente() throws Exception {
		String cuaa = "DPDFRZ65C21C794B";
		setupMockitoAperturaFascicoloDetenzioneAutonomaPersonaFisica(cuaa);
		mockUserSetupUtenteConnessoEProfilo(cuaa, PROFILO_AZIENDA);

		Exception exception = assertThrows(ResponseStatusException.class, () -> {
			fascicoloDetenzioneAutonomaController.apri(cuaa);
		});
		assertTrue(exception.getMessage().contains(FascicoloValidazioneEnum.FASCICOLO_LOCALE_ESISTENTE.name()));
	}
	
	@Test
	@WithMockUser(username = "DPDFRZ65C21C794B")
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTestFascicoloEsistentePersoneFisiche_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void verifica_apertura_fascicolo_persona_fisica_KO_Fascicolo_gia_esistente() throws Exception {
		String cuaa = "DPDFRZ65C21C794B";
		setupMockitoAperturaFascicoloDetenzioneAutonomaPersonaFisica(cuaa);
		mockUserSetupUtenteConnessoEProfilo(cuaa, PROFILO_AZIENDA);

		Exception exception = assertThrows(ResponseStatusException.class, () -> {
			fascicoloDetenzioneAutonomaController.verificaAperturaFascicolo(cuaa);
		});
		assertTrue(exception.getMessage().contains(FascicoloValidazioneEnum.FASCICOLO_LOCALE_ESISTENTE.name()));
	}
	
	@Test
	@WithMockUser(username = "BLDGDN61M17L378K")
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void verifica_apertura_fascicolo_persona_fisica_con_impresa_individuale_OK() throws Exception {
		String cuaaPersonaFisica = "BLDGDN61M17L378K";
		setupMockitoAperturaFascicoloDetenzioneAutonomaPersonaFisica(cuaaPersonaFisica);
		mockUserSetupUtenteConnessoEProfilo(cuaaPersonaFisica, PROFILO_AZIENDA);
		DatiAperturaFascicoloDto datiAperturaFascicoloDto = transactionTemplate.execute(status -> {
			DatiAperturaFascicoloDto result = fascicoloDetenzioneAutonomaController.verificaAperturaFascicolo(cuaaPersonaFisica);
			return result;
		});
		
		assertEquals("BALDESSARI               GIORDANO", datiAperturaFascicoloDto.getDenominazione());
		assertEquals("BLDGDN61M17L378K", datiAperturaFascicoloDto.getCodiceFiscale());
		assertEquals("01849820228", datiAperturaFascicoloDto.getPartitaIva());
	}
	
	@Test
	@WithMockUser(username = "LNLMTB74A31F205S")
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void verifica_apertura_fascicolo_persona_giuridica_OK() throws Exception {
		String cuaaPersonaGiuridica = "00123890220";
		String cfRappresentanteLegale = "LNLMTB74A31F205S"; 
		setupMockitoAperturaFascicoloDetenzioneAutonomaPersonaGiuridica(cuaaPersonaGiuridica);
		mockUserSetupUtenteConnessoEProfilo(cfRappresentanteLegale, PROFILO_AZIENDA);
		DatiAperturaFascicoloDto datiAperturaFascicoloDto = transactionTemplate.execute(status -> {
			DatiAperturaFascicoloDto result = fascicoloDetenzioneAutonomaController.verificaAperturaFascicolo(cuaaPersonaGiuridica);
			return result;
		});
		
		assertEquals("FERRARI F.LLI LUNELLI S.P.A.", datiAperturaFascicoloDto.getDenominazione());
		assertEquals("00123890220", datiAperturaFascicoloDto.getCodiceFiscale());
		assertEquals("00123890220", datiAperturaFascicoloDto.getPartitaIva());
	}
	
	@Test
	@WithMockUser(username = "LNLMTB74A31F205S")
	@Sql(scripts = "/sql/fascicolo/persona/aggiornamentoFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aggiornamentoFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void verifica_apertura_fascicolo_persona_giuridica_KO_Fascicolo_esistente() throws Exception {
		String cuaaPersonaGiuridica = "00123890220";
		String cfRappresentanteLegale = "LNLMTB74A31F205S"; 
		setupMockitoAperturaFascicoloDetenzioneAutonomaPersonaGiuridica(cuaaPersonaGiuridica);
		mockUserSetupUtenteConnessoEProfilo(cfRappresentanteLegale, PROFILO_AZIENDA);
		
		ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
			fascicoloDetenzioneAutonomaController.verificaAperturaFascicolo(cuaaPersonaGiuridica);
		});
		assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
		assertEquals(FascicoloValidazioneEnum.FASCICOLO_LOCALE_ESISTENTE.toString(), exception.getReason());
	}
	
	@Test
	@WithMockUser(username = "BLDLCU67M12L378O")
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void verifica_apertura_fascicolo_persona_fisica_senza_impresa_individuale_OK() throws Exception {
		String cuaaPersonaFisica = "BLDLCU67M12L378O";
		setupMockitoAperturaFascicoloDetenzioneAutonomaPersonaFisica(cuaaPersonaFisica);
		mockUserSetupUtenteConnessoEProfilo(cuaaPersonaFisica, PROFILO_AZIENDA);
		DatiAperturaFascicoloDto datiAperturaFascicoloDto = transactionTemplate.execute(status -> {
			DatiAperturaFascicoloDto result = fascicoloDetenzioneAutonomaController.verificaAperturaFascicolo(cuaaPersonaFisica);
			return result;
		});
		
		assertEquals("BALDESSARI LUCA", datiAperturaFascicoloDto.getDenominazioneFascicolo());
		assertEquals("BLDLCU67M12L378O", datiAperturaFascicoloDto.getDatiAnagraficiRappresentante().getCodiceFiscale());
	}
	
//	@Test
//	@WithMockUser(username = "utente", roles = {
//			"a4gfascicolo.fascicolo.ricerca.utente"
//			})
//	@Sql(scripts = {
//			"/sql/fascicolo/fascicolo_detenzione_in_proprio_persona_giuridica_insert.sql",
//			"/sql/fascicolo/fascicolo_persona_giuridica_update_in_aggiornamento.sql",
//			"/sql/fascicolo/fascicolo_persona_giuridica_update_modo_pagamento.sql"
//		},
//		executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
//	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
//	@Transactional
//	void get_report_scheda_validazione_bozza_detenzione_autonoma() throws Exception {
//		String cuaa = "00959460221";
//		String utenteAzienda = "XPDNDR77B03L378X";
//		
//		mockUserSetupUtenteConnessoEProfilo(utenteAzienda, PROFILO_AZIENDA);
//		mockStampaComponentOkStuff();
//		byte[] pdf = {'1', '2', '3'};
//		
//		MvcResult mvcResult = mockMvc.perform(get(String.format("%s/%s/report-scheda-validazione-bozza-detenzione-autonoma", ApiUrls.FASCICOLO_DETENZIONE_AUTONOMA, cuaa))
//				.contentType(MediaType.APPLICATION_OCTET_STREAM))
//		.andExpect(status().isOk())
//		.andReturn();
//		assertEquals(MediaType.APPLICATION_OCTET_STREAM_VALUE, 
//			      mvcResult.getResponse().getContentType());
//        byte[] savedPdf = mvcResult.getResponse().getContentAsByteArray();
//		assertArrayEquals(pdf, savedPdf);
//	}
}
