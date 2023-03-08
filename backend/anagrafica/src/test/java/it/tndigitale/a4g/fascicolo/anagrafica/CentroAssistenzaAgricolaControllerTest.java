package it.tndigitale.a4g.fascicolo.anagrafica;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaUtenteClient;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.caa.CentroAssistenzaAgricolaDto;
import it.tndigitale.a4g.utente.client.model.RappresentaIlModelloPerRappresentareUnUtenteDelSistema;

@Disabled
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente")
class CentroAssistenzaAgricolaControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private AnagraficaUtenteClient anagraficaUtenteClient;

	@Test
	@Sql(scripts = "/sql/centroassistenzaagricola/getCaaConnessoTestOk_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/centroassistenzaagricola/getCaaConnessoTestOk_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getCaaConnessoTestOk() throws Exception {
		// recupera sportelli a cui il caa è abilitato
		List<String> entiUtenteConnesso = new ArrayList<String>();
		entiUtenteConnesso.add("13");
		Mockito.when(anagraficaUtenteClient.getEntiUtenteConnesso()).thenReturn(entiUtenteConnesso);

		String stringResponse = mockMvc.perform(get("/api/v1/caa"))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		CentroAssistenzaAgricolaDto caaDto = objectMapper.readValue(stringResponse, CentroAssistenzaAgricolaDto.class);

		assertEquals("CAA - TEST", caaDto.getDenominazione());
		assertEquals("07343031006", caaDto.getCodiceFiscale());

		caaDto.getSportelli();
		assertEquals(1, caaDto.getSportelli().size());
		assertEquals("CAA ACLI - TRENTO - 001", caaDto.getSportelli().get(0).getDenominazione());
	}


	@Test
	void getCaaConnessoTestKo() throws Exception {
		Assertions.assertThrows(Exception.class, () -> {
			// manca a4g_persona corrispondente all'utente connesso
			// mock servizi a4g utente - recupera utente
			RappresentaIlModelloPerRappresentareUnUtenteDelSistema user = new RappresentaIlModelloPerRappresentareUnUtenteDelSistema();
			user.setIdentificativo("07343031006");
			user.setCodiceFiscale("07343031006");
			Mockito.when(anagraficaUtenteClient.getUtenteConnesso()).thenReturn(user);
			// recupera sportelli a cui il caa è abilitato
			List<String> entiUtenteConnesso = new ArrayList<String>();
			entiUtenteConnesso.add("13");
			Mockito.when(anagraficaUtenteClient.getEntiUtenteConnesso()).thenReturn(entiUtenteConnesso);

			mockMvc.perform(get("/api/v1/caa")).andExpect(status().is5xxServerError());
		});
	}

}
