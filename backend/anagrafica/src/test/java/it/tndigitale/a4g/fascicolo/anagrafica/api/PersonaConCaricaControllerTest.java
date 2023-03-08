package it.tndigitale.a4g.fascicolo.anagrafica.api;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.DocumentoIdentitaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaFisicaConCaricaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaGiuridicaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.DocumentoIdentitaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.FascicoloDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.PersonaFisicaConCaricaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.PersonaGiuridicaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.PersonaConCaricaService;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaProxyClient;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaUtenteClient;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.DocumentoIdentitaDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.TipoDocumentoIdentitaEnum;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.PersonaFisicaConCaricaDto;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.StatoFascicoloEnum;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.proxy.client.model.PersonaFisicaDto;
import it.tndigitale.a4g.utente.client.model.RappresentaIlModelloDelProfiloDiUnUtente;
import it.tndigitale.a4g.utente.client.model.RappresentaIlModelloPerRappresentareUnUtenteDelSistema;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class PersonaConCaricaControllerTest {
	@MockBean AnagraficaProxyClient anagraficaProxyClient;
	@MockBean Clock clock;
	@MockBean AnagraficaUtenteClient anagraficaUtenteClient;
	
	@Autowired PersonaFisicaConCaricaDao personaFisicaConCaricaDao;
	@Autowired PersonaConCaricaService personaConCaricaService;
	@Autowired MockMvc mockMvc;
	@Autowired FascicoloDao fascicoloDao;	
	@Autowired ObjectMapper objectMapper;
	@Autowired PersonaGiuridicaDao personaGiuridicaDao;
	@Autowired DocumentoIdentitaDao documentoIdentitaDao;
	
	@Autowired private PlatformTransactionManager transactionManager;
	private TransactionTemplate transactionTemplate;
	
    private final long ID_ENTE_CONNESSO = 11L;
    private LocalDate testDay = LocalDate.of(2021, Month.APRIL, 30);
	
    private String getResponseAnagrafeTributaria(String cf) throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/fascicolo/anagrafetributaria/".concat(cf).concat(".json")));
		return objectMapper.writeValueAsString(response);
	}
    
    private ByteArrayResource createMockMultipartFile() {
		Path path = Paths.get("src/test/resources/fascicolo/MANDATO_ftoDPDNDR77B03L378L.pdf");
		String name = "MANDATO_ftoDPDNDR77B03L378L";
		byte[] content = null;
		try {
			content = Files.readAllBytes(path);
		} catch (final IOException e) {
		}
		return new ByteArrayResource(content, name);
	}
	
    void userSetupEnte() {
		List<String> entiUtenteConnesso = new ArrayList<>();
		entiUtenteConnesso.add(String.valueOf(ID_ENTE_CONNESSO));
		Mockito.when(anagraficaUtenteClient.getEntiUtenteConnesso()).thenReturn(entiUtenteConnesso);
	}
	
	 private void setupMockitoPersonaFisicaAnagrafeTributaria() throws Exception {
			Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagrafeTributaria(Mockito.eq("XPDNDR77B03L378X"))).thenReturn(objectMapper.readValue(getResponseAnagrafeTributaria("XPDNDR77B03L378X"), PersonaFisicaDto.class));
			Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagrafeTributaria(Mockito.eq("DVNGTN63E17G482Y"))).thenReturn(objectMapper.readValue(getResponseAnagrafeTributaria("DVNGTN63E17G482Y"), PersonaFisicaDto.class));		
		}

	void userSetupAzienda(String userCodiceFiscale) {
		RappresentaIlModelloPerRappresentareUnUtenteDelSistema user = new RappresentaIlModelloPerRappresentareUnUtenteDelSistema();
		user.setIdentificativo(userCodiceFiscale);
		user.setCodiceFiscale(userCodiceFiscale);
		user.setNome(userCodiceFiscale);
		List<RappresentaIlModelloDelProfiloDiUnUtente> profili = new ArrayList<>();
		RappresentaIlModelloDelProfiloDiUnUtente profilo = new RappresentaIlModelloDelProfiloDiUnUtente();
		profilo.setIdentificativo("azienda");
		profili.add(profilo);
		user.setProfili(profili);
		Mockito.when(anagraficaUtenteClient.getUtenteConnesso()).thenReturn(user);
	}
	
	@BeforeEach
	void initialize() {
		Mockito.when(clock.today()).thenReturn(testDay);
	}

	@Test
	@WithMockUser(username = "XPDNDR77B03L378X", roles = {"a4gfascicolo.fascicolo.ricerca.ente", "a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/fascicolo_get_persona_con_carica.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void testSalvaFirmatarioOk() throws Exception {
		String cuaaPersGiuridica = "00959460221";
		String codiceFiscalePersFisicaConCarica = "XPDNDR77B03L378X";
		userSetupEnte();
		ByteArrayResource mockDocumentoIdentita = createMockMultipartFile();
		String numeroDocumento = "1213232322";
		LocalDate dataRilascio = LocalDate.now().minusDays(5);
		LocalDate dataScadenza = LocalDate.now().plusDays(5);
		
		FascicoloModel fascicoloModel = fascicoloDao.findByCuaaAndIdValidazione(cuaaPersGiuridica, 0).get();
		Assert.assertEquals(StatoFascicoloEnum.VALIDATO, fascicoloModel.getStato());
		// chiamata rest che salva il firmatario
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		MockHttpServletRequestBuilder requestBuilder =
				MockMvcRequestBuilders.multipart(String.format("%s/%s/carica/firmatario", ApiUrls.API_V1, cuaaPersGiuridica))
				.file("documento", mockDocumentoIdentita.getByteArray())
				.param("codiceFiscale", codiceFiscalePersFisicaConCarica)
				.param("tipoDocumento", TipoDocumentoIdentitaEnum.CARTA_IDENTITA.name())
				.param("numeroDocumento", numeroDocumento)
				.param("dataScadenza", dataScadenza.format(formatter))
				.param("dataRilascio", dataRilascio.format(formatter))
				.contentType(MediaType.APPLICATION_JSON);
		requestBuilder.with(request -> {
            request.setMethod(HttpMethod.POST.name());
            return request;
        });
		this.mockMvc.perform(requestBuilder);
		
		// chiamata di conferma che il firmatario sia impostato
		mockMvc.perform(get(String.format("%s/%s/carica/firmatario", ApiUrls.PERSONA_GIURIDICA, cuaaPersGiuridica))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.codiceFiscale").value(codiceFiscalePersFisicaConCarica));
		fascicoloModel = fascicoloDao.findByCuaaAndIdValidazione(cuaaPersGiuridica, 0).get();
		Assert.assertEquals(StatoFascicoloEnum.IN_AGGIORNAMENTO, fascicoloModel.getStato());
		
//		verifica la presenza del  documento di identita salvato
		Optional<DocumentoIdentitaModel> result = documentoIdentitaDao.findByFascicolo_CuaaAndFascicolo_IdValidazione(cuaaPersGiuridica, 0);
		if(result.isPresent()) {
			Assert.assertEquals(numeroDocumento, result.get().getNumero());
			Assert.assertEquals(TipoDocumentoIdentitaEnum.CARTA_IDENTITA.name(), result.get().getTipologia());
			Assert.assertEquals(dataRilascio, result.get().getDataRilascio());
			Assert.assertEquals(dataScadenza, result.get().getDataScadenza());
		}
	}

	@Test
	@WithMockUser(username = "XPDNDR77B03L378X", roles = {"a4gfascicolo.fascicolo.ricerca.ente", "a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/fascicolo_get_persona_con_carica.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void testSalvaFirmatarioNonOk() throws Exception {
		String cuaaPersGiuridica = "00959460221";
		String codiceFiscalePersFisicaConCarica = "DVNGTN63E17G482Y";
		userSetupEnte();
		ByteArrayResource mockDocumentoIdentita = createMockMultipartFile();
		String numeroDocumento = "1213232322";
		LocalDate dataRilascio = LocalDate.now().minusDays(5);
		LocalDate dataScadenza = LocalDate.now().plusDays(5);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		MockHttpServletRequestBuilder requestBuilder =
				MockMvcRequestBuilders.multipart(String.format("%s/%s/carica/firmatario", ApiUrls.API_V1, cuaaPersGiuridica))
				.file("documento", mockDocumentoIdentita.getByteArray())
				.param("codiceFiscale", codiceFiscalePersFisicaConCarica)
				.param("tipoDocumento", TipoDocumentoIdentitaEnum.CARTA_IDENTITA.name())
				.param("numeroDocumento", numeroDocumento)
				.param("dataScadenza", dataScadenza.format(formatter))
				.param("dataRilascio", dataRilascio.format(formatter))
				.contentType(MediaType.APPLICATION_JSON);
		requestBuilder.with(request -> {
            request.setMethod(HttpMethod.POST.name());
            return request;
        });
		this.mockMvc.perform(requestBuilder);
		
		// rileggo il firmatario (è quello di prima)
		mockMvc.perform(get(String.format("%s/%s/carica/firmatario", ApiUrls.PERSONA_GIURIDICA, cuaaPersGiuridica))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(jsonPath("$.codiceFiscale").value(org.hamcrest.Matchers.not(codiceFiscalePersFisicaConCarica)));
		
//		verifica che il documento di identita associato al fascicolo e' quello di XPDNDR77B03L378X, in quanto DVNGTN63E17G482Y non puo' essere firmatario
		Optional<DocumentoIdentitaModel> result = documentoIdentitaDao.findByFascicolo_CuaaAndFascicolo_IdValidazione(cuaaPersGiuridica, 0);
		if(result.isPresent()) {
			Assert.assertEquals("XPDNDR77B03L378X", result.get().getCodiceFiscale());
		}
	}
	
	@Test
	@WithMockUser(username = "XPDNDR77B03L378X", roles = {"a4gfascicolo.fascicolo.ricerca.ente", "a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/fascicolo_get_persona_con_carica.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void testSalvaFirmatarioDocumentoIdentitaInvalido() throws Exception {
		String cuaaPersGiuridica = "00959460221";
		String codiceFiscalePersFisicaConCarica = "DVNGTN63E17G482Y";
		userSetupEnte();
		
//		dati di documento identita non validi
		DocumentoIdentitaDto documentoIdentitaDto = new DocumentoIdentitaDto();

		// salva il firmatario (non salva nulla perchè codiceFiscalePersFisicaConCarica ha una carica non compatibile)
		mockMvc.perform(post(
				String.format("%s/%s/carica/firmatario", ApiUrls.API_V1, cuaaPersGiuridica)
				)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(documentoIdentitaDto)))
				.andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}
	
	@Test
	@WithMockUser(username = "XPDNDR77B03L378X", roles = {"a4gfascicolo.fascicolo.ricerca.ente", "a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/fascicolo_get_persona_con_carica.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void testSalvaFirmatarioDocumentoIdentitaDateInvalide() throws Exception {
		String cuaaPersGiuridica = "00959460221";
		String codiceFiscalePersFisicaConCarica = "DVNGTN63E17G482Y";
		userSetupEnte();
		ByteArrayResource mockDocumentoIdentita = createMockMultipartFile();
		String numeroDocumento = "1213232322";
		LocalDate dataRilascio = LocalDate.now().plusDays(5);
		LocalDate dataScadenza = LocalDate.now().minusDays(5);
		
//		popolare i dati
		DocumentoIdentitaDto documentoIdentitaDto = new DocumentoIdentitaDto();
		documentoIdentitaDto.setNumeroDocumento(numeroDocumento);
		documentoIdentitaDto.setTipoDocumento(TipoDocumentoIdentitaEnum.CARTA_IDENTITA);
		documentoIdentitaDto.setDocumento(mockDocumentoIdentita.getByteArray());
		documentoIdentitaDto.setDataRilascio(dataRilascio);
		documentoIdentitaDto.setDataScadenza(dataScadenza);
		documentoIdentitaDto.setCodiceFiscale(codiceFiscalePersFisicaConCarica);

		// salva il firmatario (non salva nulla perchè codiceFiscalePersFisicaConCarica ha una carica non compatibile)
		mockMvc.perform(post(
				String.format("%s/%s/carica/firmatario/%s", ApiUrls.API_V1, cuaaPersGiuridica, codiceFiscalePersFisicaConCarica)
				)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(documentoIdentitaDto)))
				.andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}

	@Test
	@WithMockUser(username = "XPDNDR77B03L378X", roles = {"a4gfascicolo.fascicolo.ricerca.ente", "a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/fascicolo_get_persona_con_carica.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void testSalvaFirmatarioPfOk() throws Exception {
		String cuaaFascicolo = "XPDNDR77B03L378X";
		String codiceFiscalePersFisicaConCarica = "XPDNDR77B03L378X";
		userSetupEnte();
		ByteArrayResource mockDocumentoIdentita = createMockMultipartFile();
		String numeroDocumento = "1213232322";
		LocalDate dataRilascio = LocalDate.now().minusDays(5);
		LocalDate dataScadenza = LocalDate.now().plusDays(5);
		
		FascicoloModel fascicoloModel = fascicoloDao.findByCuaaAndIdValidazione(cuaaFascicolo, 0).get();
		Assert.assertEquals(StatoFascicoloEnum.VALIDATO, fascicoloModel.getStato());
		// chiamata rest che salva il firmatario
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		MockHttpServletRequestBuilder requestBuilder =
				MockMvcRequestBuilders.multipart(String.format("%s/%s/carica/firmatario", ApiUrls.API_V1, cuaaFascicolo))
				.file("documento", mockDocumentoIdentita.getByteArray())
				.param("codiceFiscale", codiceFiscalePersFisicaConCarica)
				.param("tipoDocumento", TipoDocumentoIdentitaEnum.CARTA_IDENTITA.name())
				.param("numeroDocumento", numeroDocumento)
				.param("dataScadenza", dataScadenza.format(formatter))
				.param("dataRilascio", dataRilascio.format(formatter))
				.contentType(MediaType.APPLICATION_JSON);
		requestBuilder.with(request -> {
            request.setMethod(HttpMethod.POST.name());
            return request;
        });
		this.mockMvc.perform(requestBuilder);
		
		// chiamata di conferma che il firmatario sia impostato
		mockMvc.perform(get(String.format("%s/%s/carica/firmatario", ApiUrls.PERSONA_GIURIDICA, cuaaFascicolo))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.codiceFiscale").value(codiceFiscalePersFisicaConCarica));
		fascicoloModel = fascicoloDao.findByCuaaAndIdValidazione(cuaaFascicolo, 0).get();
		Assert.assertEquals(StatoFascicoloEnum.IN_AGGIORNAMENTO, fascicoloModel.getStato());
		
//		verifica la presenza del  documento di identita salvato
		Optional<DocumentoIdentitaModel> result = documentoIdentitaDao.findByFascicolo_CuaaAndFascicolo_IdValidazione(cuaaFascicolo, 0);
		if(result.isPresent()) {
			Assert.assertEquals(numeroDocumento, result.get().getNumero());
			Assert.assertEquals(TipoDocumentoIdentitaEnum.CARTA_IDENTITA.name(), result.get().getTipologia());
			Assert.assertEquals(dataRilascio, result.get().getDataRilascio());
			Assert.assertEquals(dataScadenza, result.get().getDataScadenza());
		}
	}

	@Test
	@WithMockUser(username = "XPDNDR77B03L378X", roles = {"a4gfascicolo.fascicolo.ricerca.ente", "a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/fascicolo_get_persona_con_carica.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void verificaListenerPersoneFisicheConCarica() throws Exception {
		String cuaaPersGiuridica = "00959460221";
		userSetupEnte();
		setupMockitoPersonaFisicaAnagrafeTributaria();
		
		transactionTemplate = new TransactionTemplate(transactionManager);
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
	        @Override
	        protected void doInTransactionWithoutResult(TransactionStatus status) {
	        	FascicoloModel fascicoloModel = fascicoloDao.findByCuaaAndIdValidazione(cuaaPersGiuridica, 0).get();
	        	fascicoloModel.setDtAggiornamentoFontiEsterne(null);
	        	fascicoloDao.save(fascicoloModel);
	        	Optional<PersonaGiuridicaModel> personaGiuridicaOpt = personaGiuridicaDao.findByCodiceFiscaleAndIdValidazione(cuaaPersGiuridica, 0);
	    		
	    		if (personaGiuridicaOpt.isPresent()) {
	    			PersonaGiuridicaModel personaGiuridicaModel = personaGiuridicaOpt.get();
	    			var personaFisicaConCaricaSet = new HashSet<PersonaFisicaConCaricaModel>();
	    			personaGiuridicaModel.getCariche().forEach( carica -> {
	    				personaFisicaConCaricaSet.add(carica.getPersonaFisicaConCaricaModel());
	    			});
	    			
	    			personaConCaricaService.sendEvent(fascicoloModel, personaFisicaConCaricaSet);
	    			
	    		}
	        }
	    });
		await().atMost(5, TimeUnit.SECONDS).until(() -> fascicoloDao.findByCuaaAndIdValidazione(cuaaPersGiuridica, 0).get().getDtAggiornamentoFontiEsterne() != null);
	}
	

	@Test
	@WithMockUser(username = "XPDNDR77B03L378X", roles = {"a4gfascicolo.fascicolo.ricerca.ente", "a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/fascicolo_get_persona_con_carica.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void verificaCodiceFiscalePersoneFisicheConCarica() throws Exception {
		String cuaaPersGiuridica = "00959460221";
		userSetupEnte();
		setupMockitoPersonaFisicaAnagrafeTributaria();
		
		transactionTemplate = new TransactionTemplate(transactionManager);
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
	        @Override
	        protected void doInTransactionWithoutResult(TransactionStatus status) {
	        	FascicoloModel fascicoloModel = fascicoloDao.findByCuaaAndIdValidazione(cuaaPersGiuridica, 0).get();
	        	fascicoloModel.setDtAggiornamentoFontiEsterne(null);
	        	fascicoloDao.save(fascicoloModel);
	        	Optional<PersonaGiuridicaModel> personaGiuridicaOpt = personaGiuridicaDao.findByCodiceFiscaleAndIdValidazione(cuaaPersGiuridica, 0);
	    		
	    		if (personaGiuridicaOpt.isPresent()) {
	    			PersonaGiuridicaModel personaGiuridicaModel = personaGiuridicaOpt.get();
	    			var personaFisicaConCaricaSet = new HashSet<PersonaFisicaConCaricaModel>();
	    			personaGiuridicaModel.getCariche().forEach( carica -> {
	    				personaFisicaConCaricaSet.add(carica.getPersonaFisicaConCaricaModel());
	    			});
	    			
	    			personaConCaricaService.sendEvent(fascicoloModel, personaFisicaConCaricaSet);
	    			
	    		}
	        }
	    });
		await().atMost(5, TimeUnit.SECONDS).until(() -> fascicoloDao.findByCuaaAndIdValidazione(cuaaPersGiuridica, 0).get().getDtAggiornamentoFontiEsterne() != null);

		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
	        @Override
	        protected void doInTransactionWithoutResult(TransactionStatus status) {
		    	Optional<PersonaGiuridicaModel> personaGiuridicaOpt = personaGiuridicaDao.findByCodiceFiscaleAndIdValidazione(cuaaPersGiuridica, 0);
				if (personaGiuridicaOpt.isPresent()) {
					PersonaGiuridicaModel personaGiuridicaModel = personaGiuridicaOpt.get();
					personaGiuridicaModel.getCariche().forEach( carica -> {
						PersonaFisicaConCaricaDto dto = PersonaFisicaConCaricaDto.toDto(carica.getPersonaFisicaConCaricaModel());
						assertNotNull(dto.getVerificaCodiceFiscale().getValue());
					});
				}
	        }
	    });
	
	}
	
	@Test
	@WithMockUser(username = "XPDNDR77B03L378X", roles = {"a4gfascicolo.fascicolo.ricerca.ente", "a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/fascicolo_get_persona_con_carica.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void get_documento_identita_firmatario() throws Exception {
		userSetupEnte();
		String cuaa = "00959460221";
		
		mockMvc.perform(get(String.format("%s/%s/carica/firmatario/documento-identita", ApiUrls.API_V1, cuaa))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.codiceFiscale").value("XPDNDR77B03L378X"))
			.andExpect(jsonPath("$.numeroDocumento").value("111111"))
			.andExpect(jsonPath("$.tipoDocumento").value(TipoDocumentoIdentitaEnum.ALTRO.name()))
			.andExpect(jsonPath("$.dataRilascio").value("2019-09-01"))
			.andExpect(jsonPath("$.dataScadenza").value("2029-09-01"));
	}
	
	@Test
	@WithMockUser(username = "XPDNDR77B03L378X", roles = {"a4gfascicolo.fascicolo.ricerca.ente", "a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/fascicolo_get_persona_con_carica.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void get_documento_identita_firmatario_empty() throws Exception {
		userSetupEnte();
		String cuaa = "XPDNDR77B03L378X";
		
		mockMvc.perform(get(String.format("%s/%s/carica/firmatario/documento-identita", ApiUrls.API_V1, cuaa))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.codiceFiscale").isEmpty())
			.andExpect(jsonPath("$.numeroDocumento").isEmpty())
			.andExpect(jsonPath("$.tipoDocumento").isEmpty())
			.andExpect(jsonPath("$.dataRilascio").isEmpty())
			.andExpect(jsonPath("$.dataScadenza").isEmpty())
			.andExpect(jsonPath("$.documento").isEmpty());
	}
	
}
