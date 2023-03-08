package it.tndigitale.a4g.fascicolo.antimafia;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.fascicolo.antimafia.api.ApiUrls;
import it.tndigitale.a4g.framework.client.ClientServiceBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class ConsultazioneProfilazioneTests {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private RestTemplate restTemplate;
	
	@MockBean
	private ClientServiceBuilder clientServiceBuilder;
	
	@Value("${a4gfascicolo.integrazioni.anagraficaimpresa.uri}")
	private String urlIntegrazioniAnagraficaImpresa;
	
	@Value("${a4gfascicolo.ags.uri}")
	private String uriAgs;
	
	@Value("${it.tndigit.security.utente.url}")
	private String uriA4gUtente;

	@Test
	public void checkRuoloPersonaAziendaTitolareOK() throws Exception {
		String utente = "TRRCST78B08C794X";
		String cuaa = "TRRCST78B08C794X";

		mockLogin(utente);
		mockServizicheckRuoloPersonaAzienda(utente, cuaa);
		
		ResultActions resultActions = this.mockMvc.perform(get(ApiUrls.CONSULTAZIONE_V1 + ApiUrls.AMBITO_PERSONE + "/"
				+ utente + ApiUrls.AMBITO_AZIENDE + "/" + cuaa + ApiUrls.FUNZIONE_IS_RAPPRESENTANTE_LEGALE)
						.header(A4gfascicoloConstants.HEADER_CF, utente));
		resultActions.andExpect(status().isOk()).andExpect(content().string(containsString("TORRESANI CRISTIAN")));
	}

	@Test
	public void checkRuoloPersonaAziendaRLegaleOK() throws Exception {
		String utente = "TRRCST78B08C794X";
		String cuaa = "92009950228";

		mockLogin(utente);
		mockServizicheckRuoloPersonaAzienda(utente, cuaa);

		ResultActions resultActions = this.mockMvc.perform(get(ApiUrls.CONSULTAZIONE_V1 + ApiUrls.AMBITO_PERSONE + "/"
				+ utente + ApiUrls.AMBITO_AZIENDE + "/" + cuaa + ApiUrls.FUNZIONE_IS_RAPPRESENTANTE_LEGALE)
						.header(A4gfascicoloConstants.HEADER_CF, utente));
		resultActions.andExpect(status().isOk())
				.andExpect(content().string(containsString("SOCIETA' MALGA VAL SOCIETA' AGRICOLA")));
	}

	@Test
	public void checkRuoloPersonaAziendaRLegaleKO() throws Exception {
		{
			String utente = "TRRCST78B08C794X";
			String cuaa = "00126080225";

			mockLogin(utente);
			mockServizicheckRuoloPersonaAzienda(utente, cuaa);

			ResultActions resultActions = this.mockMvc.perform(get(ApiUrls.CONSULTAZIONE_V1 + ApiUrls.AMBITO_PERSONE
					+ "/" + utente + ApiUrls.AMBITO_AZIENDE + "/" + cuaa + ApiUrls.FUNZIONE_IS_RAPPRESENTANTE_LEGALE)
							.header(A4gfascicoloConstants.HEADER_CF, utente));
			resultActions.andExpect(status().is5xxServerError()).andExpect(content().string(
					containsString(String.format(MessaggiErrori.COERENZA_SOGGETTO_CARICA_AZIENDA, utente, cuaa))));
		}
	}

	@Test
	public void checkRuoloPersonaAziendaInesistente() throws Exception {
		{
			String utente = "TRRCST78B08C794X";
			String cuaa = "XXXCST78B08C794X";

			mockLogin(utente);
			mockServizicheckRuoloPersonaAzienda(utente, cuaa);

			ResultActions resultActions = this.mockMvc.perform(get(ApiUrls.CONSULTAZIONE_V1 + ApiUrls.AMBITO_PERSONE
					+ "/" + utente + ApiUrls.AMBITO_AZIENDE + "/" + cuaa + ApiUrls.FUNZIONE_IS_RAPPRESENTANTE_LEGALE)
							.header(A4gfascicoloConstants.HEADER_CF, utente));
			resultActions.andExpect(status().is5xxServerError()).andExpect(content().string(
					containsString(String.format(MessaggiErrori.COERENZA_SOGGETTO_CARICA_AZIENDA, utente, cuaa))));
		}

	}

	@Test
	public void checkRuoloPersonaInesistenteAzienda() throws Exception {
		{
			String utente = "XXXCST78B08C794X";
			String cuaa = "TRRCST78B08C794X";

			mockLogin(utente);
			mockServizicheckRuoloPersonaAzienda(utente, cuaa);

			ResultActions resultActions = this.mockMvc.perform(get(ApiUrls.CONSULTAZIONE_V1 + ApiUrls.AMBITO_PERSONE
					+ "/" + utente + ApiUrls.AMBITO_AZIENDE + "/" + cuaa + ApiUrls.FUNZIONE_IS_RAPPRESENTANTE_LEGALE)
							.header(A4gfascicoloConstants.HEADER_CF, utente));
			resultActions.andExpect(status().is5xxServerError()).andExpect(content().string(
					containsString(String.format(MessaggiErrori.COERENZA_SOGGETTO_CARICA_AZIENDA, utente, cuaa))));
		}
	}

	@Test
	public void checkRuoloPersonaCuaaErratoAzienda() throws Exception {
		{
			String utente = "XXXCST78B08C794";
			String cuaa = "TRRCST78B08C794X";

			mockLogin(utente);
			mockServizicheckRuoloPersonaAzienda(utente, cuaa);

			ResultActions resultActions = this.mockMvc.perform(get(ApiUrls.CONSULTAZIONE_V1 + ApiUrls.AMBITO_PERSONE
					+ "/" + utente + ApiUrls.AMBITO_AZIENDE + "/" + cuaa + ApiUrls.FUNZIONE_IS_RAPPRESENTANTE_LEGALE)
							.header(A4gfascicoloConstants.HEADER_CF, utente));
			resultActions.andExpect(status().is5xxServerError()).andExpect(content().string(
					containsString(String.format(MessaggiErrori.COERENZA_SOGGETTO_CARICA_AZIENDA, utente, cuaa))));
		}
	}

	private void mockServizicheckRuoloPersonaAzienda(String cfPersona, String cuaa) throws Exception {
		String service = urlIntegrazioniAnagraficaImpresa
				.concat(String.format(ApiUrls.INTEGRAZIONI_ANAGRAFICA_IMPRESA_PERSONENONCESSATEPERCF, cfPersona));

		System.out.println("service" + service);

		JsonNode response;

		if (cfPersona == "TRRCST78B08C794X") {
			response = objectMapper.readTree(
					new File("src/test/resources/consultazione/getResponseRicercaPersonaPerCFTorresani.json"));
			Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(service)), // Use anyString()
					Mockito.eq(String.class))) // Use anyString()
					.thenReturn(objectMapper.writeValueAsString(response));
		} else if (cfPersona == "XXXCST78B08C794X") {
			response = objectMapper.readTree(
					new File("src/test/resources/consultazione/getResponseRicercaPersonaPerCFNonTrovato.json"));
			Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(service)), // Use anyString()
					Mockito.eq(String.class))) // Use anyString()
					.thenReturn(objectMapper.writeValueAsString(response));
		} else if (cfPersona == "XXXCST78B08C794") {
			response = objectMapper.readTree(
					new File("src/test/resources/consultazione/getResponseRicercaPersonaPerCFNonValido.json"));
			Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(service)), // Use anyString()
					Mockito.eq(String.class))) // Use anyString()
					.thenReturn(objectMapper.writeValueAsString(response));
		} else
			throw new Exception("");
	}

	@Test
	public void checkFascicoloAziendaAttivo() throws Exception {
		String utente = "TRRCST78B08C794X";
		String cuaa = "TRRCST78B08C794X";

		mockLogin(utente);
		mockServizicheckAzienda(cuaa);

		ResultActions resultActions = this.mockMvc
				.perform(get(ApiUrls.CONSULTAZIONE_V1 + ApiUrls.CONSULTAZIONE_FASCICOLI + "/" + cuaa
						+ ApiUrls.FUNZIONE_FASCICOLO_VALIDO).header(A4gfascicoloConstants.HEADER_CF, utente));
		resultActions.andExpect(status().isOk()).andExpect(content().string(containsString("true")));
	}

	@Test
	public void checkFascicoloAziendaInesistente() throws Exception {
		{
			String utente = "TRRCST78B08C794X";
			String cuaa = "TRRCST78B08C79XX";

			mockLogin(utente);
			mockServizicheckAzienda(cuaa);

			ResultActions resultActions = this.mockMvc
					.perform(get(ApiUrls.CONSULTAZIONE_V1 + ApiUrls.CONSULTAZIONE_FASCICOLI + "/" + cuaa
							+ ApiUrls.FUNZIONE_FASCICOLO_VALIDO).header(A4gfascicoloConstants.HEADER_CF, utente));
			resultActions.andExpect(status().is5xxServerError()).andExpect(
					content().string(containsString(String.format(MessaggiErrori.FASCICOLO_NON_VALIDO_AGS, cuaa))));
		}
	}

	@Test
	public void checkFascicoloAziendaChiuso() throws Exception {
		{
			String utente = "TRRCST78B08C794X";
			String cuaa = "PVISLV26B08G452L";

			mockLogin(utente);
			mockServizicheckAzienda(cuaa);

			ResultActions resultActions = this.mockMvc
					.perform(get(ApiUrls.CONSULTAZIONE_V1 + ApiUrls.CONSULTAZIONE_FASCICOLI + "/" + cuaa
							+ ApiUrls.FUNZIONE_FASCICOLO_VALIDO).header(A4gfascicoloConstants.HEADER_CF, utente));
			resultActions.andExpect(status().is5xxServerError()).andExpect(
					content().string(containsString(String.format(MessaggiErrori.FASCICOLO_NON_VALIDO_AGS, cuaa))));
		}
	}

	private void mockServizicheckAzienda(String cuaa) throws Exception {
		String service = uriAgs
				.concat("fascicoli" + String.format(ApiUrls.AGS_FASCICOLO_VALIDO, cuaa));
		
		System.out.println("service " + service);

		if (cuaa == "TRRCST78B08C794X") {
			
			Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(service)), // Use anyString()
					Mockito.eq(Boolean.class))) // Use anyString()
					.thenReturn(true);
		} else if (cuaa == "TRRCST78B08C79XX") {
			Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(service)), // Use anyString()
					Mockito.eq(Boolean.class))) // Use anyString()
					.thenReturn(false);
		} else if (cuaa == "PVISLV26B08G452L") {
			Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(service)), // Use anyString()
					Mockito.eq(Boolean.class))) // Use anyString()
					.thenReturn(false);
		} else
			throw new Exception("");
	}
	
	private void mockLogin(String utente) throws Exception {
		String serviceGetRuoliUtente = uriA4gUtente + A4gfascicoloConstants.RUOLI_UTENTE;
		List<String> funzioni = Arrays.asList(Ruoli.RICERCA_FASCICOLO_NON_FILTRATA.getCodiceRuolo());

		ResponseEntity<List<String>> res2 = new ResponseEntity<List<String>>(funzioni, null, HttpStatus.CREATED);
		
		Mockito.when(clientServiceBuilder.buildWith(any(Supplier.class))).thenReturn(restTemplate);

		Mockito.when(restTemplate.exchange(new URI(serviceGetRuoliUtente), HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {
		})).thenReturn(res2);
		
	}

	public HttpHeaders createHeaders(String utente) {
		String username = utente;
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set(A4gfascicoloConstants.HEADER_CF, username);
		return headers;
	}
}
