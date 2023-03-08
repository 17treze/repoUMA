package it.tndigitale.a4g.uma.business.service.richiesta;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.uma.business.persistence.entity.TipoCarburante;
import it.tndigitale.a4g.uma.business.service.client.UmaDotazioneTecnicaClient;
import it.tndigitale.a4g.uma.business.service.utente.AbilitazioniComponent;
import it.tndigitale.a4g.uma.dto.richiesta.MacchinaDto;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente")
class MacchineUmaControllerTest {

	private static final String UMA_01_02_05_BR3_ERR_MSG = "Per poter procedere è necessario attivare almeno una macchina";

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private AbilitazioniComponent abilitazioniComponent;
	@MockBean
	private UmaDotazioneTecnicaClient dotazioneTecnicaClient;

	@BeforeEach
	private void mockUtenteConnesso() throws Exception {
		Mockito.when(abilitazioniComponent.checkRicercaRichiestaDiCarburante(Mockito.anyLong())).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkModificaRichiestaCarburante(Mockito.anyLong())).thenReturn(true);
	}

	@Test
	@Sql(scripts = "/sql/richiesta/macchine_uma_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/macchine_uma_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getMacchineFromA4GSuccessful() throws Exception {
		mockMvc.perform(get("/api/v1/richieste/100/macchine"))
		.andExpect(status().is2xxSuccessful())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", Matchers.hasSize(2)));
	}

	@Test
	@Sql(scripts = "/sql/richiesta/macchine_uma_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/macchine_uma_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getMacchineNoDomanda() throws Exception {
		Exception exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(get("/api/v1/richieste/12344321/macchine"));
		});
		String errMsg = String.format("Nessuna Richiesta con id %s trovata", 12344321);
		assertTrue(exception.getMessage().contains(errMsg));
	}

	@Test
	@Sql(scripts = "/sql/richiesta/macchine_uma_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/macchine_uma_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postMacchineSuccessful() throws Exception {

		List<MacchinaDto> macchineDaSalvare = new ArrayList<>();
		macchineDaSalvare.add(new MacchinaDto()
				.setAlimentazione(TipoCarburante.BENZINA)
				.setClasse("classe")
				.setDescrizione("descrizione")
				.setIdentificativoAgs(100L)
				.setIsUtilizzata(true)
				.setMarca("marca")
				.setPossesso("proprieta'")
				.setTarga("targa"));

		mockMvc.perform(post("/api/v1/richieste/100/macchine").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(macchineDaSalvare)))
		.andExpect(status().is2xxSuccessful());
	}

	@Test
	@Sql(scripts = "/sql/richiesta/macchine_uma_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/macchine_uma_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postMacchineNessunaMacchinaUtilizzata() throws Exception {

		List<MacchinaDto> macchineDaSalvare = new ArrayList<>();
		macchineDaSalvare.add(new MacchinaDto()
				.setAlimentazione(TipoCarburante.BENZINA)
				.setClasse("classe")
				.setDescrizione("descrizione")
				.setIdentificativoAgs(100L)
				.setIsUtilizzata(false)
				.setMarca("marca")
				.setPossesso("proprieta'")
				.setTarga("targa"));
		Exception exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(post("/api/v1/richieste/100/macchine").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(macchineDaSalvare)));
		});
		assertTrue(exception.getMessage().contains(UMA_01_02_05_BR3_ERR_MSG));
	}

	@Test
	@Sql(scripts = "/sql/richiesta/macchine_uma_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/macchine_uma_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postMacchineNoMacchineDaSalvare() throws Exception {

		List<MacchinaDto> macchineDaSalvare = new ArrayList<>();
		Exception exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(post("/api/v1/richieste/100/macchine").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(macchineDaSalvare)));
		});
		assertTrue(exception.getMessage().contains("Non ci sono macchinari da dichiarare"));
	}
}
