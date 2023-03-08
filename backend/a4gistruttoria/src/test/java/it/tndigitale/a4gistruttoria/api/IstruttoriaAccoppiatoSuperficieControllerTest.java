package it.tndigitale.a4gistruttoria.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
// @AutoConfigureTestDatabase
@WithMockUser(username = "istruttore", roles = { 
		"a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
		"a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
public class IstruttoriaAccoppiatoSuperficieControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@Test
	@Sql(scripts = "/DomandaUnica/acs/istruttoria_acs_dettaglio.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/acs/istruttoria_acs_dettaglio_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void getEsitiCalcoli() throws Exception {
		//564564564
		MvcResult result = mvc.perform( MockMvcRequestBuilders
			      .get(ApiUrls.ISTRUTTORIE_ACCOPPIATO_SUPERFICIE_V1+"/564564564/esiticalcoli")
			      .accept(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk())
			      .andExpect(jsonPath("$.sintesiCalcolo.ACSIMPCALCTOT",Matchers.containsString("95")))
			      .andExpect(jsonPath("$.sintesiCalcolo.ACSIMPRIDTOT",Matchers.containsString("0.2")))
			      .andExpect(jsonPath("$.dettaglioCalcolo.m8").isEmpty())
			      .andExpect(jsonPath("$.dettaglioCalcolo.m9").isEmpty())
			      .andExpect(jsonPath("$.dettaglioCalcolo.m10").isEmpty())
			      .andExpect(jsonPath("$.dettaglioCalcolo.m11").isEmpty())
			      .andExpect(jsonPath("$.dettaglioCalcolo.m14").isEmpty())
			      .andExpect(jsonPath("$.dettaglioCalcolo.m15").isEmpty())
			      .andExpect(jsonPath("$.dettaglioCalcolo.m17").isNotEmpty())
			      .andExpect(jsonPath("$.dettaglioCalcolo.m17.*", Matchers.hasSize(17)))
			      .andExpect(jsonPath("$.dettaglioCalcolo.m17.*",
							Matchers.contains(
									"1.904 ha",
									null,
									"50 euro",
									"SI",
									"NO",
									"NO",
									"0.00 %",
									"SI",
									"SI",
									"1.904 ha",
									"1.8999 ha",
									"1.8999 ha",
									"95.2 euro",
									"0.2 euro",
									"95 euro",
									"0 euro",
									"95 euro"
									)))
			      .andReturn();
	}
	
	@Test
	@Sql(scripts = "/DomandaUnica/acs/istruttoria_acs_dettaglio.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/acs/istruttoria_acs_dettaglio_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void getDisciplina() throws Exception {
		//564564564
		MvcResult result = mvc.perform( MockMvcRequestBuilders
			      .get(ApiUrls.ISTRUTTORIE_ACCOPPIATO_SUPERFICIE_V1+"/564564564/disciplina")
			      .accept(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk())
			      .andExpect(jsonPath("$.BPSIMPCALCFIN_INPUT",Matchers.containsString("1167.06")))
			      .andExpect(jsonPath("$.GREIMPCALCFIN_INPUT",Matchers.containsString("658.12")))
			      .andExpect(jsonPath("$.GIOIMPCALCFIN_INPUT",Matchers.containsString("434.6")))
			      .andExpect(jsonPath("$.DFDISIMPCALC_INPUT",Matchers.containsString("2259.78")))
			      .andReturn();
		// System.out.println(result.getResponse().getContentAsString());
	}
	
	@Test
	@Sql(scripts = "/DomandaUnica/acs/istruttoria_acs_dettaglio.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/acs/istruttoria_acs_dettaglio_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void getDatiASSuccessExpandDatiParticella() throws Exception {
		this.mvc.perform(
				get(ApiUrls.ISTRUTTORIE_ACCOPPIATO_SUPERFICIE_V1+"/564564564/esitiparticelle"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.m8").isEmpty())
				.andExpect(jsonPath("$.m10").isEmpty())
				.andExpect(jsonPath("$.m17").isNotEmpty())
				.andExpect(jsonPath("$.m17").isArray())
				.andExpect(jsonPath("$.m17", Matchers.hasSize(12)))
				.andExpect(jsonPath("$.m17.[0].particella").value("01747"))
				.andExpect(jsonPath("$.m17.[0].supImpegnata").value(3827))
				.andExpect(jsonPath("$.m17.[3].controlloRegioni").value(true))
				// Controllo che possano esistere particelle uguali con colture diverse
				.andExpect(jsonPath("$.m17.[5].particella").value("01747"))
				.andExpect(jsonPath("$.m17.[6].particella").value("01747"))
				.andExpect(jsonPath("$.m17.[5].codColtura").value("160-111-011"))
				.andExpect(jsonPath("$.m17.[6].codColtura").value("160-111-032"));
	}

}
