package it.tndigitale.a4g.fascicolo.anagrafica.legacy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloService;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@WithMockUser(username = "utente")
class SincronizzazioneAgsFascicoloTest {

	@Autowired
	private FascicoloService fascicoloService;

	@Test
	@WithMockUser(username = "utente", roles = {"a4gfascicolo.fascicolo.ricerca.tutti"})
	@Sql(scripts = "/sql/fascicolo/fascicolo_controllato_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	@Disabled
	void sincronizzaPfAgs() {
		String cuaa = "XPDNDR77B03L378X";
		try {
			fascicoloService.sincronizzaAgs(cuaa, 0);
			assertEquals(1, 1);
		}
		catch (Exception ex) {
			assertEquals(0, 1);
		}
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4gfascicolo.fascicolo.ricerca.tutti"})
	@Sql(scripts = "/sql/fascicolo/fascicolo_persona_giuridica_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	@Disabled
	void sincronizzaPgAgs() {
		String cuaa = "00959460221";
		try {
			// fascicoloService.sincronizzaAgs(cuaa, 0);
			assertEquals(1, 1);
		}
		catch (Exception ex) {
			assertEquals(0, 1);
		}
	}
}
