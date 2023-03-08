package it.tndigitale.a4g.ags;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import it.tndigitale.a4g.ags.api.ApiUrls;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
// @AutoConfigureTestDatabase
public class UtenteApplicationTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	@WithMockUser(username="TRRSRG66D28C794B")
	public void shouldGetOneSingleUtenzaForUser() throws Exception {		
		String cfUtente ="TRRSRG66D28C794B";

		this.mockMvc.perform(get(ApiUrls.UTENTI_V1 + "/" + ApiUrls.FUNZIONE_UTENZE)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.[0].cf", is(cfUtente))).andExpect(jsonPath("$.*").isArray())
				.andExpect(jsonPath("$.length()", is(1)));
	}

	@Test
	@WithMockUser(username="TRRSRG66D28C794B")
	public void ricercaPerCodiceFiscaleCompletoRestituisceUtenza() throws Exception {		
		String cfUtente ="TRRSRG66D28C794B";

		this.mockMvc.perform(get(ApiUrls.UTENTI_V1).param("codiceFiscale", cfUtente)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.[0].cf", is(cfUtente))).andExpect(jsonPath("$.*").isArray())
				.andExpect(jsonPath("$.length()", is(1)));
	}
	
	@Test
	@WithMockUser(username="TRRSRG66D28C794B")
	public void ricercaPerCodiceFiscaleParzialeestituisceUtenza() throws Exception {		
		String cfUtente ="TRRSRG66D28C794B";

		this.mockMvc.perform(get(ApiUrls.UTENTI_V1).param("codiceFiscale", cfUtente.substring(0, cfUtente.length() - 5))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.[0].cf", is(cfUtente))).andExpect(jsonPath("$.*").isArray())
				.andExpect(jsonPath("$.length()", is(1)));
	}

	@Test
	@WithMockUser(username="TRRSRG66D28C794B")
	public void ricercaPerCognomeRestituisceUtenza() throws Exception {		
		String cognome ="TORRESANI";

		this.mockMvc.perform(get(ApiUrls.UTENTI_V1).param("cognome", cognome)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.[0].cognome", is(cognome))).andExpect(jsonPath("$.*").isArray())
				.andExpect(jsonPath("$.length()", is(1)));
	}

	@Test
	@WithMockUser(username="TRRSRG66D28C794B")
	public void ricercaPerNomeRestituisceUtenza() throws Exception {		
		String nome ="SERGIO";

		this.mockMvc.perform(get(ApiUrls.UTENTI_V1).param("nome", nome)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.[0].nome", is(nome))).andExpect(jsonPath("$.*").isArray())
				.andExpect(jsonPath("$.length()", is(1)));
	}

	@Test
	@WithMockUser(username="TRRSRG66D28C794B")
	public void ricercaPerNomeParzialeRestituisceUtenza() throws Exception {		
		String nome ="SERGIO";

		this.mockMvc.perform(get(ApiUrls.UTENTI_V1).param("nome", nome.substring(0, nome.length() - 2))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.[0].nome", is(nome))).andExpect(jsonPath("$.*").isArray())
				.andExpect(jsonPath("$.length()", is(1)));
	}

	@Test
	@WithMockUser(username="TRRSRG66D28C794B")
	public void ricercaPerNomeCognomeRestituisceUtenza() throws Exception {		
		String cognome ="TORRESANI";
		String nome ="SERGIO";

		this.mockMvc.perform(get(ApiUrls.UTENTI_V1).param("cognome", cognome).param("nome", nome)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.[0].cognome", is(cognome)))
				.andExpect(jsonPath("$.[0].nome", is(nome)))
				.andExpect(jsonPath("$.*").isArray())
				.andExpect(jsonPath("$.length()", is(1)));
	}

	@Test
	@WithMockUser(username="TRRSRG66D28C794B")
	public void ricercaPerNomeCognomeInesistenteRestituisceVuoto() throws Exception {		
		String cognome ="TORRESANI";
		String nome ="GIGIO";

		this.mockMvc.perform(get(ApiUrls.UTENTI_V1).param("cognome", cognome).param("nome", nome)).andExpect(status().isNoContent());
	}

	@Test
	@WithMockUser(username="TRRSRG66D28C794B")
	public void ricercaPerCognomeECodiceFiscaleParzialeRestituisceUtenza() throws Exception {		
		String cognome ="TORRESANI";
		String cfUtente ="TRRSRG66D28C794B";

		this.mockMvc.perform(get(ApiUrls.UTENTI_V1).param("cognome", cognome).param("codiceFiscale", cfUtente.substring(0, cfUtente.length() - 10))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.[0].cognome", is(cognome)))
				.andExpect(jsonPath("$.[0].cf", is(cfUtente)))
				.andExpect(jsonPath("$.*").isArray())
				.andExpect(jsonPath("$.length()", is(1)));
	}

	@Test
	@WithMockUser(username="TRRSRG66D28C794B")
	public void ricercaPerNomeECodiceFiscaleParzialeRestituisceUtenza() throws Exception {		
		String nome ="SERGIO";
		String cfUtente ="TRRSRG66D28C794B";

		this.mockMvc.perform(get(ApiUrls.UTENTI_V1).param("nome", nome).param("codiceFiscale", cfUtente.substring(0, cfUtente.length() - 10))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.[0].nome", is(nome)))
				.andExpect(jsonPath("$.[0].cf", is(cfUtente)))
				.andExpect(jsonPath("$.*").isArray())
				.andExpect(jsonPath("$.length()", is(1)));
	}

	@Test
	@WithMockUser(username="TRRSRG66D28C794B")
	public void ricercaPerCognomeECodiceFiscaleErratiRestituisceNoContent() throws Exception {		
		String cognome ="TORESANI";
		String cfUtente ="TRRSRG66D28C794B";

		this.mockMvc.perform(get(ApiUrls.UTENTI_V1)
					.param("cognome", cognome)
					.param("codiceFiscale", cfUtente))
				.andExpect(status().isNoContent());
	}

	@Test
	@WithMockUser(username="TRRSRG66D28C794B")
	public void ricercaPerCognomeParzialeRestituisceUtenza() throws Exception {		
		String cognome ="TORRESANI";

		this.mockMvc.perform(get(ApiUrls.UTENTI_V1).param("cognome", cognome.substring(0, cognome.length() - 5))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.[0].cognome", is(cognome))).andExpect(jsonPath("$.*").isArray())
				.andExpect(jsonPath("$.length()", is(1)));
	}

	@Test
	@WithMockUser(username="TRRCST78B08C794X")
	public void shouldGetNoContentStatusIfUserDoesNotExist() throws Exception {
		this.mockMvc.perform(get((ApiUrls.UTENTI_V1 + "/" + ApiUrls.FUNZIONE_UTENZE)))
				.andExpect(status().isNoContent());
	}

	@Test
	@WithMockUser(username="aaa")
	public void shouldGetNoContentStatusIfInvalidInput() throws Exception {
		this.mockMvc.perform(get((ApiUrls.UTENTI_V1 + "/" + ApiUrls.FUNZIONE_UTENZE)))
		.andExpect(status().isNoContent());
	}

	@Test
	@WithMockUser(username="RSSFLR57S64E048J")
	public void shouldGetNoContentStatusIfUserIsNotActive() throws Exception {
		this.mockMvc.perform(get((ApiUrls.UTENTI_V1 + "/" + ApiUrls.FUNZIONE_UTENZE)))
				.andExpect(status().isNoContent());
	}
	
	@Test
	@WithMockUser(username="SMNFRC88P06L378S")
	public void shouldGet3UtenzeForUser() throws Exception {
		String cfUtente ="SMNFRC88P06L378S";

		this.mockMvc.perform(get((ApiUrls.UTENTI_V1 + "/" + ApiUrls.FUNZIONE_UTENZE))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.[0].cf", is(cfUtente))).andExpect(jsonPath("$.*").isArray())
				.andExpect(jsonPath("$.length()", is(3)));
	}
}