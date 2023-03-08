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
import it.tndigitale.a4gistruttoria.dto.configurazioneistruttoria.ConfIstruttoriaRicevibilitaDto;
import it.tndigitale.a4gistruttoria.util.MapperWrapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
// @AutoConfigureTestDatabase
@WithMockUser(username = "istruttore", roles = { "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
		"a4gistruttoria.pac.istruttoria.du.visualizza.utente", "a4gistruttoria.pac.istruttoria.edita" })
public class ConfigurazioneIstruttoriaRicevibilitaControllerTest {
	@Autowired private MockMvc mvc;
	@Autowired private MapperWrapper mapperWrapper;

	@Test
	@Sql(scripts = "/DomandaUnica/ricevibilita/configurazione-ricevibilita_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/ricevibilita/configurazione-ricevibilita_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void getConfIstruttoriaRicevibilita() throws Exception {
		mvc.perform(get(ApiUrls.ISTRUTTORIE_DU_CONF_V1 + "/ricevibilita/2019")).andExpect(status().is2xxSuccessful())
				.andExpect(jsonPath("$.id").isNumber()).andExpect(jsonPath("$.campagna").value(2019))
				.andExpect(jsonPath("$.dataRicevibilita").value("2019-08-01"))
				.andExpect(jsonPath("$.dataScadenzaDomandaInizialeInRitardo").value("2019-07-12"))
				.andExpect(jsonPath("$.dataScadenzaDomandaRitiroParziale").value("2020-05-15"));
	}

	@Test
	@Sql(scripts = "/DomandaUnica/ricevibilita/configurazione-ricevibilita_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/ricevibilita/configurazione-ricevibilita_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void getConfIstruttoriaRicevibilitaNoContent() throws Exception {
		mvc.perform(get(ApiUrls.ISTRUTTORIE_DU_CONF_V1 + "/disaccoppiato/2020")).andExpect(status().is2xxSuccessful())
				.andExpect(status().is(204));
	}

	@Test
	@Sql(scripts = "/DomandaUnica/ricevibilita/configurazione-ricevibilita_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/ricevibilita/configurazione-ricevibilita_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void saveConfIstruttoriaRicevibilita() throws Exception {
		mvc.perform(post(ApiUrls.ISTRUTTORIE_DU_CONF_V1 + "/ricevibilita/2019")
				.content("{\r\n" + "  \"id\": 12009973,\r\n" + "  \"campagna\": 2019,\r\n"
						+ "  \"dataRicevibilita\": \"2019-08-01\",\r\n"
						+ "  \"dataScadenzaDomandaInizialeInRitardo\": \"2019-07-12\",\r\n"
						+ "  \"dataScadenzaDomandaRitiroParziale\": \"2020-05-15\"\r\n" + "}")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful()).andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.id").value(12009973)).andExpect(jsonPath("$.campagna").value(2019))
				.andExpect(jsonPath("$.dataRicevibilita").value("2019-08-01"))
				.andExpect(jsonPath("$.dataScadenzaDomandaInizialeInRitardo").value("2019-07-12"))
				.andExpect(jsonPath("$.dataScadenzaDomandaRitiroParziale").value("2020-05-15"));
	}

	@Test
	@Sql(scripts = "/DomandaUnica/ricevibilita/configurazione-ricevibilita_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/ricevibilita/configurazione-ricevibilita_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void saveConfIstruttoriaRicevibilitaError() throws Exception {
		ConfIstruttoriaRicevibilitaDto conf = new ConfIstruttoriaRicevibilitaDto();
		conf.setId(Long.parseLong("12009973"));
		conf.setCampagna(2019);
		conf.setDataRicevibilita(LocalDate.parse("2019-08-01", DateTimeFormatter.ISO_LOCAL_DATE));
		conf.setDataScadenzaDomandaInizialeInRitardo(LocalDate.parse("2019-07-12", DateTimeFormatter.ISO_LOCAL_DATE));
		conf.setDataScadenzaDomandaRitiroParziale(LocalDate.parse("2020-05-15", DateTimeFormatter.ISO_LOCAL_DATE));
		mvc.perform(post(ApiUrls.ISTRUTTORIE_DU_CONF_V1 + "/ricevibilita/2019").content(mapperWrapper.asJsonString(conf))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful());
	}
}
