package it.tndigitale.a4g.uma.business.service.logging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente")
class LoggingServiceTest {
	
	@Autowired
	private LoggingService service;

	@Test
	@Sql(scripts = "/sql/logging/logging_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/logging/logging_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getOperatoreCreazioneOk() throws Exception {
		String output = service.getOperatoreCreazione(RichiestaCarburanteModel.class, 8008L);
		assertEquals("CPRMRT96H59B006Y",output);
	}
	
	@Test
	@Sql(scripts = "/sql/logging/logging_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/logging/logging_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getOperatoreUltimoAggiornamentoOk() throws Exception {
		String output = service.getOperatoreUltimoAggiornamento(RichiestaCarburanteModel.class, 8008L);
		assertEquals("CPRMRT96H59B006Y",output);
	}

	@Test
	@Sql(scripts = "/sql/logging/logging_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/logging/logging_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getOperatoreCreazioneNull() throws Exception {
		String output = service.getOperatoreCreazione(RichiestaCarburanteModel.class, 8888L);
		assertNull(output);
	}
	
	@Test
	@Sql(scripts = "/sql/logging/logging_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/logging/logging_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getOperatoreUltimoAggiornamentoNull() throws Exception {
		String output = service.getOperatoreUltimoAggiornamento(RichiestaCarburanteModel.class, 8888L);
		assertNull(output);
	}
}
