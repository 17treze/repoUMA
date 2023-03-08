package it.tndigitale.a4g.fascicolo.dotazionetecnica.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.ApiUrls;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.AmbitoTipologia;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente")
class TipologiaControllerTest {
	
	@Autowired private MockMvc mockMvc;
	
	@Test
	@Sql(scripts = "/sql/get_macchinari_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/get_macchinari_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getTipologieOk() throws Exception {
		mockMvc.perform(get(ApiUrls.API_V1 + ApiUrls.TIPOLOGIE).param("ambito", AmbitoTipologia.MACCHINE.name()))
		.andExpect(status().is2xxSuccessful())
		.andExpect(status().isOk());
	}
	
	@Test
	@Sql(scripts = "/sql/get_macchinari_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/get_macchinari_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getSottotipologieOk() throws Exception {
		mockMvc.perform(get(ApiUrls.API_V1 + ApiUrls.TIPOLOGIE + "/894" + ApiUrls.SOTTOTIPI))
		.andExpect(status().is2xxSuccessful())
		.andExpect(status().isOk());
	}
}
