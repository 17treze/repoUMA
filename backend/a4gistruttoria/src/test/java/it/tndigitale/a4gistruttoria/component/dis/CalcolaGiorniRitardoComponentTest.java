package it.tndigitale.a4gistruttoria.component.dis;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CalcolaGiorniRitardoComponentTest {
	
	@Autowired
	private CalcolaGiorniRitardoComponent comp;
	@Autowired
	private DomandaUnicaDao daoDomanda;
	@MockBean
	private RestTemplate restTemplate;

	@Test
	@Transactional
	public void checkGiorniRitardo() throws Exception {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(BigDecimal.valueOf(180375));
		int giorniRitardo = comp.calcolaGiorniLavorativiRitardo(domanda);
		assertEquals(4, giorniRitardo);
	}
}
