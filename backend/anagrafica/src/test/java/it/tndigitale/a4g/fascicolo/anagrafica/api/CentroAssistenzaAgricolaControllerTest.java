package it.tndigitale.a4g.fascicolo.anagrafica.api;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaUtenteClient;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class CentroAssistenzaAgricolaControllerTest {
	
	@MockBean AnagraficaUtenteClient anagraficaUtenteClient;
	@Autowired MockMvc mockMvc;

	private final long ID_ENTE_CONNESSO = 11L;
    static final String PROV_TRENTO = "TN";
    
    
    private void userSetupEnte() {
		List<String> entiUtenteConnesso = new ArrayList<>();
		entiUtenteConnesso.add(String.valueOf(ID_ENTE_CONNESSO));
		Mockito.when(anagraficaUtenteClient.getEntiUtenteConnesso()).thenReturn(entiUtenteConnesso);
	}
	
	@Test
	@WithMockUser(username = "utente", roles = {
			"a4gfascicolo.fascicolo.ricerca.ente",
			"a4ganagrafica.fascicolo.apertura.ente",
			"a4gfascicolo.fascicolo.ricerca.tutti"
			})
	@Sql(scripts = "/sql/fascicolo/fascicolo_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getCentroAssistenzaAgricoloUtenteConnesso() throws Exception {
		userSetupEnte();
		
		ResultActions resultActions = mockMvc.perform(
				get(ApiUrls.CAA)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
		
		resultActions.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(jsonPath("$.codiceFiscale").value("01896970223"))
		.andExpect(jsonPath("$.denominazione").value("CAA COOPTRENTO SRL"));
	}
	
	@Test
	@WithMockUser(username = "utente", roles = {
			"a4gfascicolo.fascicolo.ricerca.ente",
			"a4ganagrafica.fascicolo.apertura.ente",
			"a4gfascicolo.fascicolo.ricerca.tutti"
			})
	@Sql(scripts = "/sql/fascicolo/fascicolo_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getAllCaaWithSportelli() throws Exception {
		userSetupEnte();
		
		ResultActions resultActions = mockMvc.perform(
				get(ApiUrls.CAA.concat("/sportelli"))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
		
		resultActions.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(jsonPath("$", hasSize(2)));
	}
	
	@Test
	@WithMockUser(username = "utente", roles = {
			"a4gfascicolo.fascicolo.ricerca.ente",
			"a4ganagrafica.fascicolo.apertura.ente",
			"a4gfascicolo.fascicolo.ricerca.tutti"
			})
	@Sql(scripts = "/sql/fascicolo/fascicolo_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void caaCheckAperturaFascicolo() throws Exception {
		userSetupEnte();
		String cuaa = "XPDNDR77B03L378X";
		
		MockHttpServletRequestBuilder requestBuilder =
				MockMvcRequestBuilders.multipart(ApiUrls.CAA.concat("/check-apertura-fascicolo"))
				.param("cuaa", cuaa)
				.contentType(MediaType.APPLICATION_JSON);
		
		requestBuilder.with(request -> {
            request.setMethod(HttpMethod.GET.name());
            return request;
        });
		
		ResultActions resultActions = this.mockMvc.perform(
				requestBuilder
				);
		
		resultActions.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(content().string("true"));
	}
}
