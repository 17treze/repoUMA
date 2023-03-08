package it.tndigitale.a4g.ags;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.ags.dto.DomandaFilter;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
// @AutoConfigureTestDatabase
public class DomandaSicurezzaIntegrationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RestTemplate restTemplate;
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void contaDomandeNonAutenticato403() throws Exception {
		this.mockMvc.perform(get("/api/v1/domandeDU/conta").param("params", "{\"annoRiferimento\":\"2018\",\"codicePac\":\"PAC1420\",\"tipoDomanda\":\"DU\"}"))
				.andExpect(status().isForbidden());
	}

	@Test
	@Transactional
	public void domandaNonEsistenteNonAutenticato403() throws Exception {
		this.mockMvc.perform(put("/api/v1/domandeDU/183395/eseguiMovimento").contentType(MediaType.APPLICATION_JSON).content("A4GIST")).andExpect(status().isForbidden());
	}

	@Test
	public void getDomandaExpandedNonAutenticato403() throws Exception {
		this.mockMvc.perform(get("/api/v1/domandeDU/186889").param("expand", "sostegniSuperficie,sostegniAllevamento,datiPascolo,dichiarazioni"))
				.andExpect(status().isForbidden());
	}

	@Test
	public void getDomandaSupEleggibiliExpandedNonAutenticato403() throws Exception {
		this.mockMvc.perform(get("/api/v1/domandeDU/183380").param("expand", "sostegniSuperficie.supEleggibili")).andExpect(status().isForbidden());
	}

	@Test
	public void getDomandaNonAutenticato403() throws Exception {
		this.mockMvc.perform(get("/api/v1/domandeDU/183380")).andExpect(status().isForbidden());
	}

	@Test
	public void getDomandePerStatoNonAutenticato403() throws Exception {
		DomandaFilter domandaFilter = new DomandaFilter();
		domandaFilter.setStati(Arrays.asList("000015"));
		String domandaFilterRequest = objectMapper.writeValueAsString(domandaFilter);
		this.mockMvc.perform(get("/api/v1/domandeDU").param("params", domandaFilterRequest)).andExpect(status().isForbidden());
	}

	@Test
	@Transactional
	public void spostaDomandaInIstruttoriaNonAutenticato403() throws Exception {
		this.mockMvc.perform(put("/api/v1/domandeDU/183380/eseguiMovimento").contentType(MediaType.APPLICATION_JSON).content("A4GIST")).andExpect(status().isForbidden());
	}

	@Test
	@Transactional
	public void movimentoNonDefinitoNonAutenticato403() throws Exception {
		this.mockMvc.perform(put("/api/v1/domandeDU/183380/eseguiMovimento").contentType(MediaType.APPLICATION_JSON).content("RICEVI")).andExpect(status().isForbidden());
	}

	@Test
	@Transactional
	public void spostaDomandaInNonRicevibileNonAutenticato403() throws Exception {
		this.mockMvc.perform(put("/api/v1/domandeDU/183380/eseguiMovimento").contentType(MediaType.APPLICATION_JSON).content("NONRIC")).andExpect(status().isForbidden());
	}

	@Test
	@Transactional
	public void movimentoNonDisponibilePerStatoDomandaNonAutenticato403() throws Exception {
		this.mockMvc.perform(put("/api/v1/domandeDU/188802/eseguiMovimento").contentType(MediaType.APPLICATION_JSON).content("A4GIST")).andExpect(status().isForbidden());
	}

	@Test
	public void getDomandaRettificataNonAutenticato403() throws Exception {
		this.mockMvc.perform(get("/api/v1/domandeDU/191194")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "utenteAppag")
	public void contaDomandeNonAutorizzato403() throws Exception {
		this.mockMvc.perform(get("/api/v1/domandeDU/conta").param("params", "{\"annoRiferimento\":\"2018\",\"codicePac\":\"PAC1420\",\"tipoDomanda\":\"DU\"}"))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "utenteAppag")
	public void getDomandaNonAutorizzato403() throws Exception {
		this.mockMvc.perform(get("/api/v1/domandeDU/183380")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "utenteAppag")
	public void getDomandaNullaNonAutorizzato403() throws Exception {
		try {
			this.mockMvc.perform(get("/api/v1/domandeDU/183381")).andExpect(status().isForbidden());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Test
	@WithMockUser(username = "utenteAppag")
	public void getDomandePerStatoNonAutorizzato403() throws Exception {
		DomandaFilter domandaFilter = new DomandaFilter();
		domandaFilter.setStati(Arrays.asList("000015"));
		String domandaFilterRequest = objectMapper.writeValueAsString(domandaFilter);
		this.mockMvc.perform(get("/api/v1/domandeDU/").param("params", domandaFilterRequest)).andExpect(status().isForbidden());
	}

	@Test
	@Transactional
	@WithMockUser(username = "utenteAppag")
	public void spostaDomandaInIstruttoriaNonAutorizzato403() throws Exception {
		this.mockMvc.perform(put("/api/v1/domandeDU/183380/eseguiMovimento").contentType(MediaType.APPLICATION_JSON).content("A4GIST")).andExpect(status().isForbidden());
	}

	@Test
	@Transactional
	@WithMockUser(username = "utenteAppag")
	public void spostaDomandaInNonRicevibileNonAutorizzato403() throws Exception {
		this.mockMvc.perform(put("/api/v1/domandeDU/183380/eseguiMovimento").contentType(MediaType.APPLICATION_JSON).content("NONRIC")).andExpect(status().isForbidden());
	}

	@Test
	@Transactional
	@WithMockUser(username = "utenteAppag")
	public void movimentoNonDefinitoNonAutorizzato403() throws Exception {
		this.mockMvc.perform(put("/api/v1/domandeDU/183380/eseguiMovimento").contentType(MediaType.APPLICATION_JSON).content("RICEVI")).andExpect(status().isForbidden());
	}

	@Test
	@Transactional
	@WithMockUser(username = "utenteAppag")
	public void movimentoNonDisponibilePerStatoDomandaNonAutorizzato403() throws Exception {
		this.mockMvc.perform(put("/api/v1/domandeDU/188802/eseguiMovimento").contentType(MediaType.APPLICATION_JSON).content("A4GIST")).andExpect(status().isForbidden());
	}

	@Test
	@Transactional
	@WithMockUser(username = "utenteAppag")
	public void domandaNonEsistenteNonAutorizzato403() throws Exception {
		this.mockMvc.perform(put("/api/v1/domandeDU/183395/eseguiMovimento").contentType(MediaType.APPLICATION_JSON).content("A4GIST")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "utenteAppag")
	public void getDomandaRettificataNonAutorizzato403() throws Exception {
		this.mockMvc.perform(get("/api/v1/domandeDU/191194")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "utenteAppag")
	public void getDomandaExpandedNonAutorizzato403() throws Exception {
		this.mockMvc.perform(get("/api/v1/domandeDU/186889").param("expand", "sostegniSuperficie,sostegniAllevamento,datiPascolo,dichiarazioni"))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "utenteAppag")
	public void getDomandaSupEleggibiliExpandedNonAutorizzato403() throws Exception {
		this.mockMvc.perform(get("/api/v1/domandeDU/183380").param("expand", "sostegniSuperficie.supEleggibili")).andExpect(status().isForbidden());
	}
}
