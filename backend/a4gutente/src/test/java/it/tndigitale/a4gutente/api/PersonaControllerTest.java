package it.tndigitale.a4gutente.api;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.hamcrest.core.IsNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gutente.config.Costanti;
import it.tndigitale.a4gutente.dto.Persona;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PersonaControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void conIdPersonaConPrivacyRestituisceDatiCompleti() throws Exception {
		String nrProtocolloPrivacyGenerale = "001";
		String codiceFiscale = "BRDMRN71L08C794F";
		ResultActions resultActions = performGetPersoneId("249", codiceFiscale);
		
		resultActions.andDo(print()).andExpect(status().isOk())
		.andExpect(jsonPath("$.codiceFiscale").value(codiceFiscale))
		.andExpect(jsonPath("$.nome").isNotEmpty())
		.andExpect(jsonPath("$.cognome").isNotEmpty())
		.andExpect(jsonPath("$.nrProtocolloPrivacyGenerale").value(nrProtocolloPrivacyGenerale));
	}

	protected ResultActions performGetPersoneId(String id, String codiceFiscaleUtente) throws Exception {
		ResultActions resultActions = mockMvc
				.perform(get(ApiUrls.PERSONE_V1 + "/" + id).header(Costanti.HEADER_CF, codiceFiscaleUtente));
		return resultActions;
	}

	@Test
	public void conIdPersonaSenzaPrivacyRestituisceDatiCompleti() throws Exception {
		String codiceFiscale = "TRRCST78B08C794X";
		ResultActions resultActions = performGetPersoneId("250", codiceFiscale);
		
		resultActions.andDo(print()).andExpect(status().isOk())
		.andExpect(jsonPath("$.codiceFiscale").value(codiceFiscale))
		.andExpect(jsonPath("$.nome").isNotEmpty())
		.andExpect(jsonPath("$.cognome").isNotEmpty())
		.andExpect(jsonPath("$.nrProtocolloPrivacyGenerale").isEmpty());
	}

	@Test
	public void conIdPersonaInesistenteRestituisceNotFound() throws Exception {
		ResultActions resultActions = performGetPersoneId("3", "utente");
		resultActions.andDo(print()).andExpect(status().isNoContent());
//		TODO
//		resultActions.andDo(print()).andExpect(status().is4xxClientError()); //prima di integrazione con framework
	}

	@Test
	public void codiceFiscalePersonaEsistenteeConPrivacyRestituisceDatiCompleti() throws Exception {
		String id = "249";
		String nrProtocolloPrivacyGenerale = "001";
		String codiceFiscale = "BRDMRN71L08C794F";

		ResultActions resultActions = performGetPersone(codiceFiscale);
		
		resultActions.andDo(print()).andExpect(status().isOk())
		.andExpect(jsonPath("$[0].id").value(id))
		.andExpect(jsonPath("$[0].codiceFiscale").value(codiceFiscale))
		.andExpect(jsonPath("$[0].nome").isNotEmpty())
		.andExpect(jsonPath("$[0].cognome").isNotEmpty())
		.andExpect(jsonPath("$[0].nrProtocolloPrivacyGenerale").value(nrProtocolloPrivacyGenerale));
	}

	protected ResultActions performGetPersone(String codiceFiscale) throws Exception {
		String params = "{\"codiceFiscale\" : \"" + codiceFiscale + "\"}";

		ResultActions resultActions = mockMvc
				.perform(get(ApiUrls.PERSONE_V1).header(Costanti.HEADER_CF, codiceFiscale).param("params", params));
		return resultActions;
	}

	@Test
	public void codiceFiscalePersonaInesistenteeRestituisceVuoto() throws Exception {
		String codiceFiscale = "BRDMRN71L08C794G";

		ResultActions resultActions = performGetPersone(codiceFiscale);
		
		resultActions
			.andDo(print())
			.andExpect(status().isNoContent())
			.andExpect(content().string("[]"));
	}

	@Test
	public void codiceFiscalePersonaEsistenteeSenzaPrivacyRestituisceDatiSenzaPrivacy() throws Exception {
		String id = "250";
		String codiceFiscale = "TRRCST78B08C794X";
		
		ResultActions resultActions = performGetPersone(codiceFiscale);
		
		resultActions.andDo(print()).andExpect(status().isOk())
		.andExpect(jsonPath("$[0].id").value(id))
		.andExpect(jsonPath("$[0].codiceFiscale").value(codiceFiscale))
		.andExpect(jsonPath("$[0].nome").isNotEmpty())
		.andExpect(jsonPath("$[0].cognome").isNotEmpty())
		.andExpect(jsonPath("$[0].nrProtocolloPrivacyGenerale").isEmpty());
	}


	@Test
	public void inserisciPersona() throws Exception {
		Persona persona = new Persona();
		persona.setNome("Giuseppe");
		persona.setCognome("Menna");
		persona.setCodiceFiscale("MNNGPP80A01H501L");
		persona.setNrProtocolloPrivacyGenerale("1234312456789");

		this.mockMvc.perform(post(ApiUrls.PERSONE_V1).headers(createHeaders("MNNGPP80A01H501L")).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(persona)))
				.andExpect(status().isOk()).andExpect(content().string(IsNull.notNullValue()));
	}

	@Test
	public void inserisciPersonaConNumeroProtocollo() throws Exception {
		Persona persona = new Persona();
		persona.setNome("Antonio");
		persona.setCognome("Menna");
		persona.setCodiceFiscale("MNNNTN80A01H501B");
		persona.setNrProtocolloPrivacyGenerale("658965478");

		this.mockMvc.perform(post(ApiUrls.PERSONE_V1).headers(createHeaders("MNNNTN80A01H501B")).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(persona)))
				.andExpect(status().isOk()).andExpect(content().string(IsNull.notNullValue()));
	}

	@Test
	public void aggiornaPersona() throws Exception {
		Persona persona = new Persona();
		persona.setNome("Mario");
		persona.setCognome("Rossi");
		persona.setCodiceFiscale("RSSMRA80A01F839W");

		this.mockMvc.perform(put(ApiUrls.PERSONE_V1 + "/251").headers(createHeaders("RSSMRA80A01F839W")).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(persona)))
				.andExpect(status().isOk()).andExpect(content().string(containsString("RSSMRA80A01F839W")));
	}

	@Test
	public void aggiornaPersonaConNumeroProtocollo() throws Exception {
		Persona persona = new Persona();
		persona.setNome("Mauro");
		persona.setCognome("Guppy");
		persona.setCodiceFiscale("GPPMRA80A01L378N");
		persona.setNrProtocolloPrivacyGenerale("123431245");

		this.mockMvc.perform(put(ApiUrls.PERSONE_V1 + "/251").headers(createHeaders("BRDMRN71L08C794F")).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(persona)))
				.andExpect(status().isOk()).andExpect(content().string(containsString("123431245")));
	}

	private HttpHeaders createHeaders(String utente) {
		String username = utente;
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set(Costanti.HEADER_CF, username);
		return headers;
	}
}
