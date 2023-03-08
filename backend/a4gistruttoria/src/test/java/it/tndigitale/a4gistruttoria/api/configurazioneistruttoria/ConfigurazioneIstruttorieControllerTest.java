package it.tndigitale.a4gistruttoria.api.configurazioneistruttoria;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import it.tndigitale.a4gistruttoria.api.ApiUrls;
import it.tndigitale.a4gistruttoria.dto.configurazioneistruttoria.ConfIstruttorieDto;
import it.tndigitale.a4gistruttoria.util.MapperWrapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
// @AutoConfigureTestDatabase
@WithMockUser(username = "istruttore", roles = { "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
		"a4gistruttoria.pac.istruttoria.du.visualizza.utente", "a4gistruttoria.pac.istruttoria.edita" })
public class ConfigurazioneIstruttorieControllerTest {

	@Autowired private MockMvc mvc;
	@Autowired private MapperWrapper mapperWrapper;

	@Test
	@Sql(scripts = "/DomandaUnica/configurazioneIstruttorie/configurazione-istruttorie_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/configurazioneIstruttorie/configurazione-istruttorie_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void getConfIstruttorie() throws Exception {
		mvc.perform(get(ApiUrls.ISTRUTTORIE_DU_CONF_V1 + "/istruttorie/2019"))
			.andExpect(status().is2xxSuccessful())
			.andExpect(jsonPath("$.id").isNumber()).andExpect(jsonPath("$.campagna").value(2019))
			.andExpect(jsonPath("$.dtScadenzaDomandeIniziali").value("2019-08-01"))
			.andExpect(jsonPath("$.percentualePagamento").value(0.5))
			.andExpect(jsonPath("$.percentualeDisciplinaFinanziaria").value(0.01252145));
	}

	@Test
	@Sql(scripts = "/DomandaUnica/configurazioneIstruttorie/configurazione-istruttorie_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/configurazioneIstruttorie/configurazione-istruttorie_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void getConfIstruttorieNoContent() throws Exception {
		mvc.perform(get(ApiUrls.ISTRUTTORIE_DU_CONF_V1 + "/istruttorie/2020"))
			.andExpect(status().is2xxSuccessful())
			.andExpect(status().is(204));
	}

	@Test
	@Sql(scripts = "/DomandaUnica/configurazioneIstruttorie/configurazione-istruttorie_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/configurazioneIstruttorie/configurazione-istruttorie_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void saveConfIstruttorie() throws Exception {
		mvc.perform(post(ApiUrls.ISTRUTTORIE_DU_CONF_V1 + "/istruttorie/2019")
				.content("{\r\n" + "  \"id\": 987654,\r\n" + "  \"campagna\": 2019,\r\n"
						+ "  \"dtScadenzaDomandeIniziali\": \"2019-08-01\",\r\n"
						+ "  \"percentualeDisciplinaFinanziaria\": \"0.5\",\r\n"
						+ "  \"percentualePagamento\": \"0.01252145\"\r\n" + "}")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is2xxSuccessful()).andExpect(jsonPath("$.id").exists())
		.andExpect(jsonPath("$.id").value(987654)).andExpect(jsonPath("$.campagna").value(2019))
		.andExpect(jsonPath("$.dtScadenzaDomandeIniziali").value("2019-08-01"))
		.andExpect(jsonPath("$.percentualeDisciplinaFinanziaria").value(0.5))
		.andExpect(jsonPath("$.percentualePagamento").value(0.01252145));
	}

	@Test
	@Sql(scripts = "/DomandaUnica/configurazioneIstruttorie/configurazione-istruttorie_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/configurazioneIstruttorie/configurazione-istruttorie_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void saveConfIstruttorieError() throws Exception {
		ConfIstruttorieDto conf = new ConfIstruttorieDto();
		conf.setId(Long.parseLong("987654"));
		conf.setCampagna(2019);
		conf.setDtScadenzaDomandeIniziali(LocalDate.parse("2019-08-01", DateTimeFormatter.ISO_LOCAL_DATE));
		conf.setPercentualePagamento(1.2);
		conf.setPercentualeDisciplinaFinanziaria(1.3);
		mvc.perform(post(ApiUrls.ISTRUTTORIE_DU_CONF_V1 + "/istruttorie/2019")
				.content(mapperWrapper.asJsonString(conf))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is2xxSuccessful());
	}
}
