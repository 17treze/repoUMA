package it.tndigitale.a4gutente.api;

import static it.tndigitale.a4gutente.dto.MotivazioneDisattivazione.ALTRO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import it.tndigitale.a4g.a4gutente.business.service.client.DocumentiProxyClient;
import it.tndigitale.a4g.framework.client.DefaultUrlMicroService;
import it.tndigitale.a4gutente.config.Costanti;
import it.tndigitale.a4gutente.dto.Utente;
import it.tndigitale.a4gutente.utility.JsonSupport;


@RunWith(SpringRunner.class)
@SpringBootTest
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"spring.h2.console.enabled=true"})
@AutoConfigureMockMvc
public class UtenteControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private RestTemplate restTemplate;

//	@Autowired
//	private ObjectMapper objectMapper;
	
	@MockBean
	private DocumentiProxyClient documentiProxyClient;
	
	@Value(DefaultUrlMicroService.PROXY_URL)
	private String a4gproxyUri;

	@Value("${a4gutente.integrazioni.protocollo.path}")
	private String protocolloUriPath;

	private static final String IDENTIFICATIVO_UTENTE = "STRFFF77H09E208K";

	@Test
	public void ruoliPerUtenteProfiloCAA() throws Exception {
		verificaUtenteRuolo("UTENTECAA", "a4gfascicolo.fascicolo.ricerca.ente");
	}

	@Test
	public void ruoliPerUtenteProfiloAPPAG() throws Exception {
		verificaUtenteRuolo("UTENTEAPPAG", "a4gfascicolo.fascicolo.ricerca.tutti");
	}

	@Test
	public void ruoliPerUtenteProfiloAzienda() throws Exception {
		verificaUtenteRuolo("UTENTEAZIENDA", "a4gfascicolo.fascicolo.ricerca.utente");
	}


	@Test
	public void ruoliPerUtenteProfiloAmministratore() throws Exception {
		verificaUtenteRuolo("UTENTEADMIN", "a4gfascicolo.fascicolo.ricerca.tutti");
	}

	@Test
	@WithMockUser(username="PIPPO")
	public void senzaProfiloNessunRuolo() throws Exception {
		ResultActions resultActions = mockMvc
				.perform(get(ApiUrls.UTENTI_V1 + ApiUrls.UTENTE_RUOLI));
		// verifico il risultato
		resultActions.andDo(print()).andExpect(status().isNoContent())
				.andExpect(content().string("[]")) ;
	}


	@Test
	public void senzaUtente403() throws Exception {
		ResultActions resultActions = mockMvc
				.perform(get(ApiUrls.UTENTI_V1 + ApiUrls.UTENTE_RUOLI));
		// verifico il risultato
		resultActions.andDo(print()).andExpect(status().is4xxClientError());
	}

	@Test
	@WithMockUser(username="PIPPO")
	public void ricercaUtentiKoPerFiltroNonValido() throws Exception {
		mockMvc.perform(get(ApiUrls.UTENTI_V1 + "/ricerca"))
			   .andDo(print())
			   .andExpect(status().isInternalServerError());
	}

	@Test
	@WithMockUser(username="PIPPO")
	@Sql("/customsql/ricercaUtenti.sql")
	@Transactional
	public void ricercaUtenti() throws Exception {
		final MvcResult mvcResult = mockMvc.perform(get(ApiUrls.UTENTI_V1 + "/ricerca").param("nome", "rAnc")
																							      .param("cognome", "oSs")
																						          .param("codiceFiscale", "77h09"))
									 	   .andDo(print())
									 	   .andExpect(status().isOk())
									       .andReturn();

		List<Utente> utenti = JsonSupport.toList(mvcResult.getResponse().getContentAsString(), Utente[].class);

		assertThat(utenti).hasSize(2);
		assertThat(utenti).extracting("id").contains(77710001L, 77710004L);
		assertThat(utenti).extracting("identificativo").contains("STRFFF77H09E208K", "STRGGG77H09E208K");
		assertThat(utenti).extracting("codiceFiscale").contains("STRFFF77H09E208K", "STRGGG77H09E208K");
		assertThat(utenti.get(0).getProfili()).hasSize(2);
		assertThat(utenti.get(0).getProfili().get(0).getDisabled()).isFalse();
		assertThat(utenti.get(0).getProfili().get(1).getDisabled()).isTrue();
		assertThat(utenti.get(1).getProfili()).hasSize(1);
	}
	
	@Test
	@WithMockUser("TRRCST78B08C794X")
	@Transactional
	public void avviaProtocollazionePrivacy() throws Exception {
		String documentazionePrivacyJSON = new String(Files.readAllBytes(Paths.get("src/test/resources/utente/documentazionePrivacy.json")));
		
//		String response = new String("1235412");
//		Mockito.when(documentiProxyClient.createIncomingDocument(Mockito.anyString(), Mockito.any(), Mockito.any()))
//		.thenReturn(response);
		
		ResponseEntity<String> response = new ResponseEntity<>("12354321", HttpStatus.ACCEPTED);
		Mockito.when(restTemplate.exchange(Mockito.eq(a4gproxyUri + protocolloUriPath), Mockito.eq(HttpMethod.POST), Mockito.any(), Mockito.eq(String.class)))
				.thenReturn(response);
		
		
//		String documentFileName = "informativa_privacy.pdf";
		String documentFileName = "MODULI_AMF.pdf.p7m";
		Path documento = Paths.get("src/test/resources/firma/input/" + documentFileName);
		MockMultipartFile multipartFileDocumentoPrincipale = 
				new MockMultipartFile("documento", 
									documentFileName, 
									"", 
									Files.readAllBytes(documento));

		String privacyXmlFilename = "info-privacy-firmata.xml";
		Path privacyXmlDocumento = Paths.get("src/test/resources/domandaRegistrazioneUtente/" + privacyXmlFilename);
		MockMultipartFile multipartFileXml = 
				new MockMultipartFile("allegati", 
						privacyXmlFilename, 
									"",
									Files.readAllBytes(privacyXmlDocumento));
		
		String privacyPdfFirmatoFilename = "informativa_privacy_firmata.pdf";
		Path privacyPdfFirmatoDocumento = Paths.get("src/test/resources/domandaRegistrazioneUtente/" + privacyPdfFirmatoFilename);
		MockMultipartFile multipartFilePdfFirmato = 
				new MockMultipartFile("allegati", 
						privacyPdfFirmatoFilename, 
									"", 
									Files.readAllBytes(privacyPdfFirmatoDocumento));
		
		
		
		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart(ApiUrls.UTENTI_V1 + ApiUrls.UTENTE_AVVIO_PROTOCOLLAZIONE);
	        builder.with(request -> {
	        	request.setMethod(HttpMethod.POST.name());
	            return request;
	        });
	    
	    MockMultipartFile nome = new MockMultipartFile("nome", "", "text/plain",
	    		"Massimo".getBytes());   
	    
	    MockMultipartFile cognome = new MockMultipartFile("cognome", "", "text/plain",
	    		"Nascivera".getBytes());
	    
	    MockMultipartFile codiceFiscale = new MockMultipartFile("codiceFiscale", "", "text/plain",
	    		"NSCMSM74A22H612G".getBytes());
	        
	    MockMultipartFile info = new MockMultipartFile("info", "", "application/json",
	    		documentazionePrivacyJSON.getBytes());

		
		final MvcResult mvcResult = this.mockMvc.perform(builder
				.file(info)
				.file(multipartFileDocumentoPrincipale)
				.file(multipartFileXml)
				.file(multipartFilePdfFirmato)
				.file(nome)
				.file(cognome)
				.file(codiceFiscale)
		          )
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		
		System.out.println("end");
	}

	@Test
	@WithMockUser(username="PIPPO")
	@Sql("/customsql/ricercaUtenti.sql")
	@Transactional
	public void ricercaUtentiKoPerValidazioneFiltro() throws Exception {
		mockMvc.perform(get(ApiUrls.UTENTI_V1 + "/ricerca").param("nome", "   ")
																	  .param("cognome", ""))
			   .andDo(print())
			   .andExpect(status().isInternalServerError())
			   .andReturn();
	}

    @Test
    @WithMockUser(username="PIPPO")
    @Sql("/customsql/caricaUtente.sql")
    @Transactional
    public void caricaUtentePerIdentificativo() throws Exception {
	    final MvcResult mvcResult = mockMvc.perform(get(ApiUrls.UTENTI_V1 + "/" + IDENTIFICATIVO_UTENTE))
                                           .andDo(print())
                                           .andExpect(status().isOk())
                                           .andReturn();

        Utente utente = JsonSupport.toObject(mvcResult.getResponse().getContentAsString(), Utente.class);

        assertThat(utente).isNotNull();
        assertThat(utente.getIdentificativo()).isEqualTo(IDENTIFICATIVO_UTENTE);
        assertThat(utente.getId()).isEqualTo(77710001L);
        assertThat(utente.getProfili()).hasSize(2);
        assertThat(utente.getAziende()).hasSize(1);
        assertThat(utente.getSedi()).hasSize(1);
        assertThat(utente.getNome()).isEqualTo("Fabio");
        assertThat(utente.getCognome()).isEqualTo("Cometa");
		assertThat(utente.getProfili().get(0).getDisabled()).isFalse();
        assertThat(utente.getProfili().get(1).getDisabled()).isTrue();
        assertThat(utente.getMotivazioneDisattivazione()).isEqualTo(ALTRO);
    }

    @Test
    @WithMockUser(username="PIPPO")
    @Sql("/customsql/caricaUtente.sql")
    @Transactional
    public void caricaUtentePerIdentificativoKO() throws Exception {
        mockMvc.perform(get(ApiUrls.UTENTI_V1 + "/" + IDENTIFICATIVO_UTENTE + "K"))
               .andDo(print())
               .andExpect(status().isNoContent());
//        		TODO
//               .andExpect(status().isNotFound()); //prima di integrazione con framework
	}
    
    @Test
    @WithMockUser(username="PIPPO")
    @Transactional
    public void caricaImieiDatiUtenteCompletoTest_NoNAutorizzatoENonPresente() throws Exception {
        mockMvc.perform(get(ApiUrls.UTENTI_V1 + ApiUrls.UTENTE_CORRENTE).header(Costanti.HEADER_CF, "  "))
               .andDo(print())
               .andExpect(jsonPath("$.codiceFiscale", is("")));
               
	}
    
    @Test
    @WithMockUser(username="ITE2939@ext.itad.infotn.it")
    @Sql("/customsql/caricaDatiAutenticazioneUtente.sql")
    @Transactional
    public void caricaImieiDatiUtenteCompletoTest_NoAutorizzatoEPresente() throws Exception {
        mockMvc.perform(get(ApiUrls.UTENTI_V1 + ApiUrls.UTENTE_CORRENTE).header(Costanti.HEADER_CF, ""))
               .andDo(print())
               .andExpect(jsonPath("$.identificativo", is("ITE2939@ext.itad.infotn.it")))
               .andExpect(jsonPath("$.codiceFiscale", is("TRRCST78B08C794X")));
               
	}
    
    @Test
    @WithMockUser(username="PIPPO")
    @Transactional
    public void caricaImieiDatiUtenteCompletoTest_AutorizzatoENonPresente() throws Exception {
        mockMvc.perform(get(ApiUrls.UTENTI_V1 + ApiUrls.UTENTE_CORRENTE).header(Costanti.HEADER_CF, "TRRCST78B08C794X"))
               .andDo(print())
               .andExpect(jsonPath("$.identificativo", is("PIPPO")))
               .andExpect(jsonPath("$.codiceFiscale", is("TRRCST78B08C794X")));
	}
    @Test
    @WithMockUser(username="ITE2939@ext.itad.infotn.it")
    @Transactional
    public void caricaImieiDatiUtenteCompletoTest_AutorizzatoEPresente() throws Exception {
        mockMvc.perform(get(ApiUrls.UTENTI_V1 + ApiUrls.UTENTE_CORRENTE).header(Costanti.HEADER_CF, "PIPPO"))
               .andDo(print())
               .andExpect(jsonPath("$.identificativo", is("ITE2939@ext.itad.infotn.it")))
               .andExpect(jsonPath("$.codiceFiscale", is("PIPPO")));
	}

	protected void verificaUtenteRuolo(String codiceFiscale, String ruolo) throws Exception {
		// setto il codice fiscale nell'header e chiamo
		ResultActions resultActions = mockMvc
				.perform(get(ApiUrls.UTENTI_V1 + ApiUrls.UTENTE_RUOLI)
						.header(Costanti.HEADER_CF, codiceFiscale));
		
		// verifico il risultato
		resultActions.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString(ruolo))) ;
	}
	
}
