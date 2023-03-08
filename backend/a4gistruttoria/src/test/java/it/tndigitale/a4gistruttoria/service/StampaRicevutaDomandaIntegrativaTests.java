package it.tndigitale.a4gistruttoria.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
// @AutoConfigureTestDatabase
public class StampaRicevutaDomandaIntegrativaTests {
	
	@Autowired
	private DomandaIntegrativaServiceImpl service;
	
	@Test
	@Transactional
	public void ricevutaDomandaIntegrativa() throws Exception {
		service.getRicevutaDomandaIntegrativa((long) 3176);
	}

}