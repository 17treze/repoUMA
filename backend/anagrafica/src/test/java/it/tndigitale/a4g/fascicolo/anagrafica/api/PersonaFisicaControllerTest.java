package it.tndigitale.a4g.fascicolo.anagrafica.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaUtenteClient;
import it.tndigitale.a4g.framework.time.Clock;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class PersonaFisicaControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	Clock clock;

	@MockBean
	AnagraficaUtenteClient anagraficaUtenteClient;

	private final long ID_ENTE_CONNESSO = 11L;

	void userSetupEnte() {
		List<String> entiUtenteConnesso = new ArrayList<>();
		entiUtenteConnesso.add(String.valueOf(ID_ENTE_CONNESSO));
		Mockito.when(anagraficaUtenteClient.getEntiUtenteConnesso()).thenReturn(entiUtenteConnesso);
	}

	@Test
	@WithMockUser(username = "utente")
	@Sql(scripts = "/sql/fascicolo/fascicolo_persona_fisica_validazione_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_persona_fisica_validazione_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void get_persona_fisica_live() throws Exception {
		userSetupEnte();
		String codiceFiscale = "FNTDNL86E05L378J";
		String params = "?idValidazione=0";

		mockMvc.perform(get(String.format("%s/%s%s", ApiUrls.PERSONA_FISICA, codiceFiscale, params))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.codiceFiscale").value(codiceFiscale))
		.andExpect(jsonPath("$.domicilioFiscale.comune").value("TRENTO"));
	}
	
	@Test
	@WithMockUser(username = "utente")
	@Sql(scripts = "/sql/fascicolo/fascicolo_persona_fisica_validazione_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_persona_fisica_validazione_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void get_persona_fisica_storico() throws Exception {
		userSetupEnte();
		String codiceFiscale = "FNTDNL86E05L378J";
		String params = "?idValidazione=1";

		mockMvc.perform(get(String.format("%s/%s%s", ApiUrls.PERSONA_FISICA, codiceFiscale, params))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.codiceFiscale").value(codiceFiscale))
		.andExpect(jsonPath("$.domicilioFiscale.comune").value("ROVERETO"));
	}

}
