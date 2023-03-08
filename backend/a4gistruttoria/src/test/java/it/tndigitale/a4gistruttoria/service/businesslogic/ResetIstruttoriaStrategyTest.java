package it.tndigitale.a4gistruttoria.service.businesslogic;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.strategy.ResetIstruttoriaStrategy;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
// @AutoConfigureTestDatabase
public class ResetIstruttoriaStrategyTest {
	
	@Autowired
	private IstruttoriaDao istruttoriaDao;
	@Autowired
	private ResetIstruttoriaStrategy resetIstruttoriaStrategy;
	
	@Test
	@WithMockUser(username = "istruttore", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	@Sql(scripts = "/DomandaUnica/zootecnia/tornaARichiesto_insert.sql",	executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/zootecnia/tornaARichiesto_delete.sql",	executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	public void tornaARichiestoZootecnia() throws Exception {
		Long idIstruttoria = 8672251L;
		resetIstruttoriaStrategy.elabora(idIstruttoria);
			
		IstruttoriaModel istruttoria = istruttoriaDao.getOne(8672251L);	
		assertTrue(istruttoria.getStato().toString() == StatoIstruttoria.RICHIESTO.toString());
		assertTrue(istruttoria.getTransizioni().isEmpty());
		
		istruttoria.getDomandaUnicaModel().getAllevamentiImpegnati().forEach(allevamento -> {
			assertTrue(allevamento.getEsitiCalcoloCapi().isEmpty());
		});
	}

	@Test
	@WithMockUser(username = "istruttore", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	@Sql(scripts = "/DomandaUnica/calcoloDisaccoppiato/tornaARichiesto_insert.sql",	executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/calcoloDisaccoppiato/tornaARichiesto_delete.sql",	executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	public void tornaARichiestoDisaccoppiato() throws Exception {
		Long idIstruttoria = 8672251L;
		resetIstruttoriaStrategy.elabora(idIstruttoria);
			
		IstruttoriaModel istruttoria = istruttoriaDao.getOne(8672251L);	
		assertTrue(istruttoria.getStato().toString() == StatoIstruttoria.RICHIESTO.toString());
		assertTrue(istruttoria.getTransizioni().isEmpty());
	}
	
	@Test
	@WithMockUser(username = "istruttore", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	@Sql(scripts = "/DomandaUnica/acs/tornaARichiesto_insert.sql",	executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/acs/tornaARichiesto_delete.sql",	executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	public void tornaARichiestoSuperficie() throws Exception {
		Long idIstruttoria = 8672251L;
		resetIstruttoriaStrategy.elabora(idIstruttoria);
			
		IstruttoriaModel istruttoria = istruttoriaDao.getOne(8672251L);	
		assertTrue(istruttoria.getStato().toString() == StatoIstruttoria.RICHIESTO.toString());
		assertTrue(istruttoria.getTransizioni().isEmpty());
	}

}
