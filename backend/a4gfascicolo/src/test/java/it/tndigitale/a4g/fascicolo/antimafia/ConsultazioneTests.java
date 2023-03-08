package it.tndigitale.a4g.fascicolo.antimafia;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.fascicolo.antimafia.api.ApiUrls;
import it.tndigitale.a4g.fascicolo.antimafia.dto.consultazione.Fascicolo;
import it.tndigitale.a4g.fascicolo.antimafia.dto.consultazione.ParamsRicercaFascicolo;
import it.tndigitale.a4g.framework.client.ClientServiceBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ConsultazioneTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private RestTemplate restTemplate;
	
	@MockBean
	private ClientServiceBuilder clientServiceBuilder;
	
	@Value("${a4gfascicolo.ags.uri}")
	private String uriAgs;
	@Value("${it.tndigit.security.utente.url}")
	private String uriA4gUtente;

	@Test
	public void ricercaFascicoloUtenteAppag() throws Exception {
		String utente = "UTENTEAPPAG";
		String ricerca = "TRRC";

		String params = mockServiziRicercafascicoli(utente, ricerca);

		ResultActions resultActions = this.mockMvc.perform(get(ApiUrls.CONSULTAZIONE_V1 + ApiUrls.CONSULTAZIONE_FASCICOLI).param("params", params).header(A4gfascicoloConstants.HEADER_CF, utente));
		resultActions.andExpect(status().isOk()).andExpect(content().string(containsString("TRRCST78B08C794X")));
	}

	@Test
	//@WithMockUser(username="UTENTEADMIN", roles = "a4gfascicolo.fascicolo.ricerca.tutti")
	public void ricercaFascicoloUtenteAdmin() throws Exception {
		String utente = "UTENTEADMIN";
		String ricerca = "TRRC";

		String params = mockServiziRicercafascicoli(utente, ricerca);

		ResultActions resultActions = this.mockMvc.perform(get(ApiUrls.CONSULTAZIONE_V1 + ApiUrls.CONSULTAZIONE_FASCICOLI).param("params", params).header(A4gfascicoloConstants.HEADER_CF, utente));
		resultActions.andExpect(status().isOk()).andExpect(content().string(containsString("TRRCST78B08C794X")));
	}

	@Test
	public void ricercaFascicoloUtenteCaa() throws Exception {
		String utente = "UTENTECAA";
		String ricerca = "TRRC";

		String params = mockServiziRicercafascicoli(utente, ricerca);
		ResultActions resultActions = this.mockMvc.perform(get(ApiUrls.CONSULTAZIONE_V1 + ApiUrls.CONSULTAZIONE_FASCICOLI).param("params", params).header(A4gfascicoloConstants.HEADER_CF, utente));
		resultActions.andExpect(status().isOk()).andExpect(content().string(containsString("TRRCST78B08C794X")));
	}

	@Test
	public void ricercaFascicoloUtenteCaaEnteDiverso() throws Exception {
		String utente = "UTENTECAA";
		String ricerca = "TRRCRL47L30A372Z";

		String params = mockServiziRicercafascicoli(utente, ricerca);

		ResultActions resultActions = this.mockMvc.perform(get(ApiUrls.CONSULTAZIONE_V1 + ApiUrls.CONSULTAZIONE_FASCICOLI).param("params", params).header(A4gfascicoloConstants.HEADER_CF, utente));
		resultActions.andExpect(status().isNoContent()).andExpect(content().string("[]"));
	}

	@Test
	public void ricercaFascicoloUtenteAppagNoFiltroEnte() throws Exception {
		String utente = "UTENTEAPPAG";
		String ricerca = "TRRCRL47L30A372Z";

		String params = mockServiziRicercafascicoli(utente, ricerca);

		ResultActions resultActions = this.mockMvc.perform(get(ApiUrls.CONSULTAZIONE_V1 + ApiUrls.CONSULTAZIONE_FASCICOLI).param("params", params).header(A4gfascicoloConstants.HEADER_CF, utente));
		resultActions.andExpect(status().isOk()).andExpect(content().string(containsString(ricerca))).andExpect(jsonPath("$.*", org.hamcrest.Matchers.hasSize(1)));
	}

	@Test
	public void ricercaFascicoloUtenteAdminNoFiltroEnte() throws Exception {
		String utente = "UTENTEADMIN";
		String ricerca = "TRRCRL47L30A372Z";

		String params = mockServiziRicercafascicoli(utente, ricerca);

		ResultActions resultActions = this.mockMvc.perform(get(ApiUrls.CONSULTAZIONE_V1 + ApiUrls.CONSULTAZIONE_FASCICOLI).param("params", params).header(A4gfascicoloConstants.HEADER_CF, utente));
		resultActions.andExpect(status().isOk()).andExpect(content().string(containsString(ricerca))).andExpect(jsonPath("$.*", org.hamcrest.Matchers.hasSize(1)));
	}

	@Test
	public void ricercaFascicoloAzeindeUtenteTORRESANI() throws Exception {
		String utente = "TRRCST78B08C794X";
		mockServiziRicercafascicoli(utente, utente);
		ResultActions resultActions = this.mockMvc.perform(get(ApiUrls.CONSULTAZIONE_V1 + ApiUrls.CONSULTAZIONE_FASCICOLI_AZIENDA).header(A4gfascicoloConstants.HEADER_CF, utente));
		resultActions.andExpect(status().isOk()).andExpect(content().string(containsString("TRRCST78B08C794X")));
	}
	
	@Test
	public void ricercaFascicoloAzeindeUtenteSenzaAziende() throws Exception {
		String utente = "UTENTEAZIENDA";
		mockServiziRicercafascicoli(utente, utente);

		ResultActions resultActions = this.mockMvc.perform(get(ApiUrls.CONSULTAZIONE_V1 + ApiUrls.CONSULTAZIONE_FASCICOLI_AZIENDA).header(A4gfascicoloConstants.HEADER_CF, utente));
		resultActions.andExpect(status().isNoContent()).andExpect(content().string("[]"));
	}

	@Test
	public void ricercaFascicoloPerEnte() throws Exception {
		String utente = "UTENTECAA";
		String params = mockServiziRicercafascicoli(utente, null);
		ResultActions resultActions = this.mockMvc.perform(get(ApiUrls.CONSULTAZIONE_V1 + ApiUrls.CONSULTAZIONE_FASCICOLI_ENTE)
				.param("params", params).header(A4gfascicoloConstants.HEADER_CF, utente));
		resultActions.andExpect(status().isOk()).andExpect(content().string(containsString("TRRCRL47L30A372Z")));
	}

	private String mockServiziRicercafascicoli(final String utente, String ricerca) throws URISyntaxException, IOException, JsonParseException, JsonMappingException, UnsupportedEncodingException {
		String serviceGetRuoliUtente = uriA4gUtente + A4gfascicoloConstants.RUOLI_UTENTE;
		
		String serviceGetEnti = uriA4gUtente + A4gfascicoloConstants.ENTI_UTENTE;
		String serviceGetAziende = uriA4gUtente + A4gfascicoloConstants.AZIENDE_UTENTE;
		String serviceGetFascicoli = uriAgs;// + "fascicoli/?params=%7B%22cuaa%22%3A%22TRRC%22%7D";

		//System.out.println(serviceGetRuoliUtente);
		System.out.println(serviceGetEnti);
		System.out.println(serviceGetFascicoli);

		List<String> enti = Arrays.asList("18", "20", "4");

		ResponseEntity<List<String>> res = new ResponseEntity<List<String>>(enti, null, HttpStatus.CREATED);

		Mockito.when(restTemplate.exchange(new URI(serviceGetEnti), HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {
		})).thenReturn(res);

		String funzione;

		switch (utente) {
		case "UTENTECAA":
			funzione = Ruoli.RICERCA_FASCICOLO_FILTRO_ENTE.getCodiceRuolo();
			break;
		case "UTENTEAPPAG":
			funzione = Ruoli.RICERCA_FASCICOLO_NON_FILTRATA.getCodiceRuolo();
			break;
		case "UTENTEADMIN":
			funzione = Ruoli.RICERCA_FASCICOLO_NON_FILTRATA.getCodiceRuolo();
			break;
		case "UTENTEAZIENDA":
		case "TRRCST78B08C794X":
			funzione = Ruoli.RICERCA_FASCICOLO_FILTRO_UTENTE.getCodiceRuolo();
			break;

		default:
			funzione = "";
		}

		List<String> funzioni = Arrays.asList(funzione);

		ResponseEntity<List<String>> res2 = new ResponseEntity<List<String>>(funzioni, null, HttpStatus.CREATED);

		Mockito.when(restTemplate.exchange(new URI(serviceGetRuoliUtente), HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {
		})).thenReturn(res2);

		String stringFascicoli = "";

		ParamsRicercaFascicolo filtro = new ParamsRicercaFascicolo();
		filtro.setCuaa(ricerca);

		if (Ruoli.RICERCA_FASCICOLO_FILTRO_ENTE.getCodiceRuolo().equals(funzione)) {
			// filtro per enti 
			filtro.setCaacodici(enti);
		}
		if (ricerca == "TRRC")
			stringFascicoli = getFascicoliTRRC();
		else if (ricerca == "TRRCRL47L30A372Z") {
			if (filtro.getCaacodici() != null && !filtro.getCaacodici().isEmpty() && !filtro.getCaacodici().contains("7")) {
				stringFascicoli = getFascicoliEmpty();
			} else {
				stringFascicoli = getFascicoliTRRCRL47L30A372Z();				
			}			
		}
		else if (ricerca == "TRRCST78B08C794X")
			stringFascicoli = getFascicoliTRRCST78B08C794X();
		else if (ricerca == null && utente == "TRRCST78B08C794X") {
			stringFascicoli = getFascicoliTRRCST78B08C794X();
			filtro.setCuaa("TRRCST78B08C794X");
		} else if (ricerca == null && utente == "UTENTECAA") {
			stringFascicoli = getFascicoliTRRCRL47L30A372Z();
		}
		else
			stringFascicoli = "[]";

		System.out.println("stringFascicoli " + stringFascicoli);

		List<Fascicolo> fascioli = Arrays.asList(objectMapper.readValue(stringFascicoli, Fascicolo[].class));

		String params = objectMapper.writeValueAsString(filtro);

		String encoded = URLEncoder.encode(params, "UTF-8");

		String serviceGetFascicoliFull = serviceGetFascicoli + "fascicoli/?params=" + encoded;

		System.out.println("serviceGetFascicoliFull " + serviceGetFascicoliFull);

		ResponseEntity<List<Fascicolo>> res3 = new ResponseEntity<List<Fascicolo>>(fascioli, null, HttpStatus.CREATED);

		Mockito.when(restTemplate.exchange(new URI(serviceGetFascicoliFull), HttpMethod.GET, null, new ParameterizedTypeReference<List<Fascicolo>>() {
		})).thenReturn(res3);

		System.out.println("params " + params);

		List<String> aziende = new ArrayList<String>();
		
		if (utente == "TRRCST78B08C794X")
			aziende = Arrays.asList("TRRCST78B08C794X");
		
		ResponseEntity<List<String>> res4 = new ResponseEntity<List<String>>(aziende, null, HttpStatus.CREATED);

		Mockito.when(restTemplate.exchange(new URI(serviceGetAziende), HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {
		})).thenReturn(res4);
		
		Mockito.when(clientServiceBuilder.buildWith(any(Supplier.class))).thenReturn(restTemplate);
		
		return params;
	}

	private String getFascicoliTRRCST78B08C794X() {
		return "[{\"idFascicolo\":31393,\"denominazione\":\"TORRESANI CRISTIAN\",\"cuaa\":\"TRRCST78B08C794X\",\"stato\":\"VALIDO\",\"caa\":\"CAA COLDIRETTI DEL TRENTINO - 003\",\"caacodice\":\"4\",\"idSoggetto\":15697}]";
	}

	private String getFascicoliTRRC() {
		return "[{\"idFascicolo\":31387,\"denominazione\":\"TURRINA CARLO\",\"cuaa\":\"TRRCRL47L30A372\",\"stato\":\"CHIUSO\",\"caa\":\"CAA COLDIRETTI DEL TRENTINO - 006\",\"caacodice\":\"7\",\"idSoggetto\":15694},{\"idFascicolo\":31377,\"denominazione\":\"TURRI CLAUDIO\",\"cuaa\":\"TRRCLD60P02C794E\",\"stato\":\"VALIDO\",\"caa\":\"CAA COLDIRETTI DEL TRENTINO - 003\",\"caacodice\":\"4\",\"idSoggetto\":15689},{\"idFascicolo\":31391,\"denominazione\":\"TURRA CATERINA\",\"cuaa\":\"TRRCRN55L48L201T\",\"stato\":\"CHIUSO\",\"caa\":\"CAA COLDIRETTI DEL TRENTINO - 004\",\"caacodice\":\"5\",\"idSoggetto\":15696},{\"idFascicolo\":38793,\"denominazione\":\"AZIENDA AGRICOLA BIO BUSS DI CHIARA TORRESAN\",\"cuaa\":\"TRRCHR75A66I531N\",\"stato\":\"VALIDO\",\"caa\":\"CAA CIA - TRENTO - 001\",\"caacodice\":\"18\",\"idSoggetto\":39403},{\"idFascicolo\":31383,\"denominazione\":\"TERRAGNOLO CORNELIO\",\"cuaa\":\"TRRCNL41E13B006M\",\"stato\":\"VALIDO\",\"caa\":\"CAA COLDIRETTI DEL TRENTINO - 012\",\"caacodice\":\"668\",\"idSoggetto\":15692},{\"idFascicolo\":31381,\"denominazione\":\"TORRESANI CLAUDIO\",\"cuaa\":\"TRRCLD69P03C794P\",\"stato\":\"VALIDO\",\"caa\":\"CAA CIA - CLES - 002\",\"caacodice\":\"19\",\"idSoggetto\":15691},{\"idFascicolo\":31389,\"denominazione\":\"TORRESANI CARLO\",\"cuaa\":\"TRRCRL48B09F837M\",\"stato\":\"VALIDO\",\"caa\":\"CAA COLDIRETTI DEL TRENTINO - 003\",\"caacodice\":\"4\",\"idSoggetto\":15695},{\"idFascicolo\":31393,\"denominazione\":\"TORRESANI CRISTIAN\",\"cuaa\":\"TRRCST78B08C794X\",\"stato\":\"VALIDO\",\"caa\":\"CAA COLDIRETTI DEL TRENTINO - 003\",\"caacodice\":\"4\",\"idSoggetto\":15697},{\"idFascicolo\":31379,\"denominazione\":\"TURRINI CLAUDIO\",\"cuaa\":\"TRRCLD65R04F187L\",\"stato\":\"VALIDO\",\"caa\":\"CAA COLDIRETTI DEL TRENTINO - 003\",\"caacodice\":\"4\",\"idSoggetto\":15690},{\"idFascicolo\":31385,\"denominazione\":\"TURRI CARLO\",\"cuaa\":\"TRRCRL47L02L378Z\",\"stato\":\"VALIDO\",\"caa\":\"CAA COLDIRETTI DEL TRENTINO - 011\",\"caacodice\":\"12\",\"idSoggetto\":15693},{\"idFascicolo\":43274,\"denominazione\":\"STORANI RICCARDO\",\"cuaa\":\"STRRCR59B20A561S\",\"stato\":\"VALIDO\",\"caa\":\"CAA ATS - 001 - TRENTO\",\"caacodice\":\"20\",\"idSoggetto\":85305}]";
	}

	private String getFascicoliTRRCRL47L30A372Z() {
		return "[{\"idFascicolo\":31387,\"denominazione\":\"TURRINA CARLO\",\"cuaa\":\"TRRCRL47L30A372Z\",\"stato\":\"CHIUSO\",\"caa\":\"CAA COLDIRETTI DEL TRENTINO - 006\",\"caacodice\":\"7\",\"idSoggetto\":15694}]";
	}

	private String getFascicoliEmpty() {
		return "[]";
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
