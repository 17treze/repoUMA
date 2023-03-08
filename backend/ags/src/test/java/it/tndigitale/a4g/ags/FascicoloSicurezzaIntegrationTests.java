package it.tndigitale.a4g.ags;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import it.tndigitale.a4g.ags.api.ApiUrls;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
// @AutoConfigureTestDatabase
public class FascicoloSicurezzaIntegrationTests {


	@Autowired
	private MockMvc mockMvc;


	@Test
	public void getFascicoloUtenteNonAutenticato403() throws Exception {
		this.mockMvc.perform(get(ApiUrls.FASCICOLI_V1 + "/31393")).andExpect(status().isForbidden());
	}

	@Test
	public void ricercaPerCuaaEDenominazioneNonAutenticato403() throws Exception {
		this.mockMvc.perform(get(ApiUrls.FASCICOLI_V1).param("params", "{\"cuaa\":\"TRRCST78B08C794X\",\"denominazione\":\"torre\"}")).andExpect(status().isForbidden());
	}


	@Test
	@WithMockUser(username="utente")
	public void ricercaPerCuaaEDenominazioneNonAutorizzato403() throws Exception {
		this.mockMvc.perform(get(ApiUrls.FASCICOLI_V1).param("params", "{\"cuaa\":\"TRRCST78B08C794X\",\"denominazione\":\"torre\"}")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username="utente")
	public void ricercaPerCuaaNonAutorizzato403() throws Exception {
		this.mockMvc.perform(get(ApiUrls.FASCICOLI_V1).param("params", "{\"cuaa\":\"TRRCST78B08C7\"}")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username="utente")
	public void ricercaPerCuaaParzialeNonAutorizzato403() throws Exception {
		this.mockMvc.perform(get(ApiUrls.FASCICOLI_V1).param("params", "{\"cuaa\":\"TRRC\"}")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username="utente")
	public void ricercaPerDenominazioneNonAutorizzato403() throws Exception {
		this.mockMvc.perform(get(ApiUrls.FASCICOLI_V1).param("params", "{\"denominazione\":\"torre\"}")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username="utente")
	public void ricercaSenzaRisultatoNonAutorizzato403() throws Exception {
		this.mockMvc.perform(get(ApiUrls.FASCICOLI_V1).param("params", "{\"cuaa\":\"SCRSMN75A10F520N\",\"denominazione\":\"Scriboni Simone\"}")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username="utente")
	public void ricercaSenzaInputNonAutorizzato403() throws Exception {
		this.mockMvc.perform(get(ApiUrls.FASCICOLI_V1).param("params", "{}")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username="utente")
	public void ricercaConEccezioneNonAutorizzato403() throws Exception {
		try {
			this.mockMvc.perform(get(ApiUrls.FASCICOLI_V1).param("params", "AAAAAA")).andExpect(status().isForbidden());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Test
	@WithMockUser(username="utente")
	public void getFascicoloNonAutorizzato403() throws Exception {
		this.mockMvc.perform(get(ApiUrls.FASCICOLI_V1 + "/31393")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username="utente")
	public void getFascicoloInesistenteNonAutorizzato403() throws Exception {
		this.mockMvc.perform(get(ApiUrls.FASCICOLI_V1 +"/-1")).andExpect(status().isForbidden());
	}
}
