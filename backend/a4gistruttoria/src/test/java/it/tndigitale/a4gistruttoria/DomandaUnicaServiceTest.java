package it.tndigitale.a4gistruttoria;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import it.tndigitale.a4gistruttoria.api.ApiUrls;
import it.tndigitale.a4gistruttoria.repository.model.StatoDomanda;

@RunWith(SpringRunner.class)
@SpringBootTest
// @AutoConfigureTestDatabase
@AutoConfigureMockMvc
@WithMockUser(username = "istruttore", roles = {
		"a4gistruttoria.pac.domandaUnica.visualizza",
		"a4gistruttoria.pac.domandaUnica.edita",
		"a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
		"a4gistruttoria.pac.istruttoria.du.visualizza.utente" })
public class DomandaUnicaServiceTest {

	@Autowired private MockMvc mockMvc;
	
	@Test
	public void getDomandaUnica_Rest_StatoDomanda_AnnoCampagna_Results() throws Exception {
		Integer annoCampagna = 2018;
		Integer numeroDomanda = 183190;
		StatoDomanda statoDomanda = StatoDomanda.IN_ISTRUTTORIA;
		String count = "5";
		String statoDomandaString = statoDomanda.name();
		this.mockMvc.perform(
				get(ApiUrls.DOMANDA_UNICA_V1 + "/ricerca")
				.param("annoCampagna", String.valueOf(annoCampagna))
				.param("statoDomanda", statoDomandaString)
				.param("numeroDomanda", String.valueOf(numeroDomanda))
				.param("numeroElementiPagina", count))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.risultati[*]", hasSize(1)));
	}

}
