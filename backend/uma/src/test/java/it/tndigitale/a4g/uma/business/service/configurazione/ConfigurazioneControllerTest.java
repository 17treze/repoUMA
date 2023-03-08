package it.tndigitale.a4g.uma.business.service.configurazione;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

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

import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.uma.api.ApiUrls;
import it.tndigitale.a4g.uma.business.persistence.entity.ConfigurazioneModel;
import it.tndigitale.a4g.uma.business.persistence.repository.ConfigurazioneDao;
import it.tndigitale.a4g.uma.business.service.utente.AbilitazioniComponent;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente")
public class ConfigurazioneControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ConfigurazioneDao configurazioneDao;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private Clock clock;
	@MockBean
	private AbilitazioniComponent abilitazioniComponent;

	@BeforeEach
	private void mockUtenteConnesso() throws Exception {
		Mockito.when(abilitazioniComponent.checkIstruttoreUma()).thenReturn(true);
	}


	@Test
	@Sql(scripts = "/sql/configurazioni/configurazioni_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/configurazioni/configurazioni_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getConfigurazioneTest() throws Exception {
		String data = mockMvc.perform(get(ApiUrls.CONFIGURAZIONI).param("annoCampagna", "2021"))
				.andExpect(status().is2xxSuccessful())
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		LocalDateTime dataLimiteDes = objectMapper.readValue(data, LocalDateTime.class);

		if (!LocalDateTime.of(2021, 11, 18, 0, 0).equals(dataLimiteDes)) {
			fail();
		}
	}

	@Test
	@Sql(scripts = "/sql/configurazioni/configurazioni_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/configurazioni/configurazioni_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void modificaConfigurazioneTest() throws Exception {
		String idConf =  mockMvc.perform(post(ApiUrls.CONFIGURAZIONI)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(LocalDateTime.of(clock.now().getYear(), 11, 20, 0, 0).format(DateTimeFormatter.ISO_DATE_TIME))))
				.andExpect(status().is2xxSuccessful())
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		Optional<ConfigurazioneModel> confSaved = configurazioneDao.findById(Long.valueOf(idConf));
		if (confSaved.isEmpty()) {
			fail();
		}
		if (!confSaved.get().getDataPrelievi().equals(LocalDateTime.of(clock.now().getYear(), 11, 20, 0, 0))) {
			fail();
		}
	}

	@Test
	void modificaConfigurazioneTestError() throws Exception {
		NestedServletException exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(post(ApiUrls.CONFIGURAZIONI)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(LocalDateTime.of(2018, 11, 20, 0, 0).format(DateTimeFormatter.ISO_DATE_TIME))));
				
		}); 

		assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
	}

	
	@Test
	@Sql(scripts = "/sql/configurazioni/configurazioni_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void creaConfigurazioneTest() throws Exception {
		String idConf =  mockMvc.perform(post(ApiUrls.CONFIGURAZIONI)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(LocalDateTime.of(clock.now().getYear(), 11, 11, 0, 0).format(DateTimeFormatter.ISO_DATE_TIME))))
				.andExpect(status().is2xxSuccessful())
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		Optional<ConfigurazioneModel> confSaved = configurazioneDao.findById(Long.valueOf(idConf));
		if (confSaved.isEmpty()) {
			fail();
		}
	}
}
