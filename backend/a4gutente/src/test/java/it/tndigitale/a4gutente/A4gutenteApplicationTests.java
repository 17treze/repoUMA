package it.tndigitale.a4gutente;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.RestTemplate;

import it.tndigitale.a4gutente.api.ApiUrls;
import it.tndigitale.a4gutente.config.Costanti;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class A4gutenteApplicationTests {

	
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private RestTemplate restTemplate;

	@Test
	public void getFunzioniAppag() throws Exception {
		String utente = "UTENTEAPPAG";
		String test = getFunzioneRicerca(utente);

		ResultActions resultActions = this.mockMvc.perform(get(ApiUrls.UTENTI_V1 + ApiUrls.UTENTE_RUOLI).header(Costanti.HEADER_CF, utente));
		resultActions.andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString(test)));
	}

	@Test
	public void getFunzioniAdmin() throws Exception {
		String utente = "UTENTEADMIN";
		String test = getFunzioneRicerca(utente);

		ResultActions resultActions = this.mockMvc.perform(get(ApiUrls.UTENTI_V1 + ApiUrls.UTENTE_RUOLI).header(Costanti.HEADER_CF, utente));
		resultActions.andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString(test)));
	}

	@Test
	public void getFunzioniCAA() throws Exception {
		String utente = "UTENTECAA";
		String test = getFunzioneRicerca(utente);

		ResultActions resultActions = this.mockMvc.perform(get(ApiUrls.UTENTI_V1 + ApiUrls.UTENTE_RUOLI).header(Costanti.HEADER_CF, utente));
		resultActions.andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString(test)));
	}

	@Test
	public void getFunzioniAzienda() throws Exception {
		String utente = "UTENTEAZIENDA";
		String test = getFunzioneRicerca(utente);

		ResultActions resultActions = this.mockMvc.perform(get(ApiUrls.UTENTI_V1 + ApiUrls.UTENTE_RUOLI).header(Costanti.HEADER_CF, utente));
		resultActions.andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString(test)));
	}

	@Test
	public void getProfiliAppag() throws Exception {
		String utente = "UTENTEAPPAG";
		String test = getProfili(utente);

		ResultActions resultActions = this.mockMvc.perform(get(ApiUrls.UTENTI_V1 + ApiUrls.UTENTE_PROFILI).header(Costanti.HEADER_CF, utente));
		resultActions.andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString(test)));
	}

	@Test
	public void getProfiliAdmin() throws Exception {
		String utente = "UTENTEADMIN";
		String test = getProfili(utente);

		ResultActions resultActions = this.mockMvc.perform(get(ApiUrls.UTENTI_V1 + ApiUrls.UTENTE_PROFILI).header(Costanti.HEADER_CF, utente));
		resultActions.andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString(test)));
	}

	@Test
	public void getProfiliCAA() throws Exception {
		String utente = "UTENTECAA";
		String test = getProfili(utente);

		ResultActions resultActions = this.mockMvc.perform(get(ApiUrls.UTENTI_V1 + ApiUrls.UTENTE_PROFILI).header(Costanti.HEADER_CF, utente));
		resultActions.andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString(test)));
	}

	@Test
	public void getProfiliAzienda() throws Exception {
		String utente = "UTENTEAZIENDA";
		String test = getProfili(utente);

		ResultActions resultActions = this.mockMvc.perform(get(ApiUrls.UTENTI_V1 + ApiUrls.UTENTE_PROFILI).header(Costanti.HEADER_CF, utente));
		resultActions.andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString(test)));
	}

	
	@Test
	public void getEntiCAA() throws Exception {
		String utente = "UTENTECAA";

		ResultActions resultActions = this.mockMvc.perform(get(ApiUrls.UTENTI_V1 + ApiUrls.UTENTE_ENTI).header(Costanti.HEADER_CF, utente));
		resultActions.andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString("4"))); // id_ente di UTENTECAA
	}

	@Test
	public void getEntiAzienda() throws Exception {
		String utente = "UTENTEAZIENDA";

		ResultActions resultActions = this.mockMvc.perform(get(ApiUrls.UTENTI_V1 + ApiUrls.UTENTE_ENTI).header(Costanti.HEADER_CF, utente));
		resultActions.andDo(print()).andExpect(status().isNoContent()).andExpect(content().string("[]"));
	}

	@Test
	public void getAziendeUtenteTORRESANI() throws Exception {
		String utente = "TRRCST78B08C794X";

		ResultActions resultActions = this.mockMvc.perform(get(ApiUrls.UTENTI_V1 + ApiUrls.UTENTE_AZIENDE).header(Costanti.HEADER_CF, utente));
		resultActions.andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString("TRRCST78B08C794X"))).andExpect(content().string(containsString("01833620220"))); // id_azienda di torresani
	}

	@Test
	public void getAziendeUtenteUTENTEAZIENDA() throws Exception {
		String utente = "UTENTEAZIENDA";

		ResultActions resultActions = this.mockMvc.perform(get(ApiUrls.UTENTI_V1 + ApiUrls.UTENTE_AZIENDE).header(Costanti.HEADER_CF, utente));
		resultActions.andDo(print()).andExpect(status().isNoContent()).andExpect(content().string("[]"));
	}

	@Test
	public void getAllUtentiProfilo() throws Exception {
		String utente = "backoffice";

		ResultActions resultActions = this.mockMvc.perform(get(ApiUrls.UTENTI_V1 + ApiUrls.UTENTE_PROFILI_BO_VITICOLO).header(Costanti.HEADER_CF, utente));
		resultActions.andDo(print());
	}

	private String getFunzioneRicerca(String utente) {
		String test;
		switch (utente) {
		case "UTENTECAA":
			test = "a4gfascicolo.fascicolo.ricerca.ente";
			break;
		case "UTENTEAPPAG":
			test = "a4gfascicolo.fascicolo.ricerca.tutti";
			break;
		case "UTENTEADMIN":
			test = "a4gfascicolo.fascicolo.ricerca.tutti";
			break;
		case "UTENTEAZIENDA":
			test = "a4gfascicolo.fascicolo.ricerca.utente";
			break;

		default:
			test = "[]";
		}
		return test;
	}

	private String getProfili(String utente) {
		String res;
		switch (utente) {
		case "UTENTECAA":
			res = "caa";
			break;
		case "UTENTEAPPAG":
			res = "appag";
			break;
		case "UTENTEADMIN":
			res = "amministratore";
			break;
		case "UTENTEAZIENDA":
			res = "azienda";
			break;

		default:
			res = "";
		}
		return res;
	}
}
