package it.tndigitale.a4g.srt.api;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UtentiControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
    @WithMockUser(username = "utente")
	public void shouldGetOneSingleRoleForUser() throws Exception {

		this.mockMvc.perform(get("/api/v1/utenti/ABCDEF11A22B123C/ruoli")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.[0]", is("Amministratore"))).andExpect(jsonPath("$.*").isArray())
				.andExpect(jsonPath("$.length()", is(1)));
	}

	@Test
    @WithMockUser(username = "utente")
	public void shouldGetNoContentStatusIfUserCFDoesNotExist() throws Exception {

		this.mockMvc.perform(get("/api/v1/utenti/ABCDEF11A22B123X/ruoli")).andDo(print())
				.andExpect(status().isNoContent());
	}

	@Test(expected = NestedServletException.class)
    @WithMockUser(username = "utente")
	public void shouldThrowExceptionIfInvalidInput() throws Exception {

		this.mockMvc.perform(get("/api/v1/utenti/invalid/ruoli")).andDo(print())
				.andExpect(status().isInternalServerError());
	}

	@Test
    @WithMockUser(username = "utente")
	public void shouldGetNoContentStatusIfUserCFIsNotActive() throws Exception {

		this.mockMvc.perform(get("/api/v1/utenti/CCCCCC33C33C333C/ruoli")).andDo(print())
				.andExpect(status().isNoContent());
	}
	
	
	
	/*****************************/

	@Test
    @WithMockUser(username = "utente")
	public void ricercaPerCodiceFiscaleCompletoRestituisceUtenza() throws Exception {		
		String cfUtente ="TRRSRG66D28C794B";

		this.mockMvc.perform(get("/api/v1/utenti").param("codiceFiscale", cfUtente))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.[0].codiceFiscale", is(cfUtente)))
				.andExpect(jsonPath("$.*").isArray())
				.andExpect(jsonPath("$.length()", is(1)));
	}
	
	@Test
    @WithMockUser(username = "utente")
	public void ricercaPerCodiceFiscaleParzialeRestituisceUtenza() throws Exception {		
		String cfUtente ="TRRSRG66D28C794B";

		this.mockMvc.perform(get("/api/v1/utenti").param("codiceFiscale", cfUtente.substring(0, cfUtente.length() - 5)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.[0].codiceFiscale", is(cfUtente)))
				.andExpect(jsonPath("$.*").isArray())
				.andExpect(jsonPath("$.length()", is(1)));
	}

	@Test
    @WithMockUser(username = "utente")
	public void ricercaPerCognomeRestituisceUtenza() throws Exception {		
		String cognome ="TORRESANI";

		this.mockMvc.perform(get("/api/v1/utenti").param("cognome", cognome))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.[0].cognome", is(cognome)))
				.andExpect(jsonPath("$.*").isArray())
				.andExpect(jsonPath("$.length()", is(1)));
	}

	@Test
    @WithMockUser(username = "utente")
	public void ricercaPerNomeRestituisceUtenza() throws Exception {		
		String nome ="SERGIO";

		this.mockMvc.perform(get("/api/v1/utenti").param("nome", nome))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.[0].nome", is(nome)))
				.andExpect(jsonPath("$.*").isArray())
				.andExpect(jsonPath("$.length()", is(1)));
	}

	@Test
    @WithMockUser(username = "utente")
	public void ricercaPerNomeParzialeRestituisceUtenza() throws Exception {		
		String nome ="SERGIO";

		this.mockMvc.perform(get("/api/v1/utenti").param("nome", nome.substring(0, nome.length() - 2)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.[0].nome", is(nome)))
				.andExpect(jsonPath("$.*").isArray())
				.andExpect(jsonPath("$.length()", is(1)));
	}

	@Test
    @WithMockUser(username = "utente")
	public void ricercaPerNomeCognomeRestituisceUtenza() throws Exception {		
		String cognome ="TORRESANI";
		String nome ="SERGIO";

		this.mockMvc.perform(get("/api/v1/utenti").param("cognome", cognome).param("nome", nome))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.[0].cognome", is(cognome)))
				.andExpect(jsonPath("$.[0].nome", is(nome)))
				.andExpect(jsonPath("$.*").isArray())
				.andExpect(jsonPath("$.length()", is(1)));
	}

	@Test
    @WithMockUser(username = "utente")
	public void ricercaPerNomeCognomeInesistenteRestituisceVuoto() throws Exception {		
		String cognome ="TORRESANI";
		String nome ="PIPPO";

		this.mockMvc.perform(get("/api/v1/utenti").param("cognome", cognome).param("nome", nome)).
			andDo(print())
			.andExpect(status().isNoContent());
	}

	@Test
    @WithMockUser(username = "utente")
	public void ricercaPerCognomeECodiceFiscaleParzialeRestituisceUtenza() throws Exception {		
		String cognome ="TORRESANI";
		String cfUtente ="TRRSRG66D28C794B";

		this.mockMvc.perform(get("/api/v1/utenti").param("cognome", cognome).param("codiceFiscale", cfUtente.substring(0, cfUtente.length() - 10)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.[0].cognome", is(cognome)))
				.andExpect(jsonPath("$.[0].codiceFiscale", is(cfUtente)))
				.andExpect(jsonPath("$.*").isArray())
				.andExpect(jsonPath("$.length()", is(1)));
	}

	@Test
    @WithMockUser(username = "utente")
	public void ricercaPerNomeECodiceFiscaleParzialeRestituisceUtenza() throws Exception {		
		String nome ="SERGIO";
		String cfUtente ="TRRSRG66D28C794B";

		this.mockMvc.perform(get("/api/v1/utenti").param("nome", nome).param("codiceFiscale", cfUtente.substring(0, cfUtente.length() - 10)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.[0].nome", is(nome)))
				.andExpect(jsonPath("$.[0].codiceFiscale", is(cfUtente)))
				.andExpect(jsonPath("$.*").isArray())
				.andExpect(jsonPath("$.length()", is(1)));
	}

	@Test
    @WithMockUser(username = "utente")
	public void ricercaPerCognomeECodiceFiscaleErratiRestituisceNoContent() throws Exception {		
		String cognome ="TORESANI";
		String cfUtente ="TRRSRG66D28C794B";

		this.mockMvc.perform(get("/api/v1/utenti")
					.param("cognome", cognome)
					.param("codiceFiscale", cfUtente))
				.andDo(print())
				.andExpect(status().isNoContent());
	}

	@Test
    @WithMockUser(username = "utente")
	public void ricercaPerCognomeParzialeRestituisceUtenza() throws Exception {		
		String cognome ="TORRESANI";

		this.mockMvc.perform(get("/api/v1/utenti").param("cognome", cognome.substring(0, cognome.length() - 5)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.[0].cognome", is(cognome)))
				.andExpect(jsonPath("$.*").isArray())
				.andExpect(jsonPath("$.length()", is(1)));
	}
}
