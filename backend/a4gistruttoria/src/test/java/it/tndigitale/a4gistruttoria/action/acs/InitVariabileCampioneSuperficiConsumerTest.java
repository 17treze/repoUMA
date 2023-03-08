package it.tndigitale.a4gistruttoria.action.acs;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloAccoppiatoHandler;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class InitVariabileCampioneSuperficiConsumerTest {

	@Autowired
	private InitVariabileCampioneSuperficiConsumer initDomandaCampioneCons;

	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private RestTemplate restTemplate;

	@Autowired
	private DomandaUnicaDao daoDomanda;

	@Autowired
	private IstruttoriaDao istruttoriaDao;
	
	
	@Test
	@Transactional
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void testDomandaNonCampione() throws Exception {
		CalcoloAccoppiatoHandler handler = new CalcoloAccoppiatoHandler();
		IstruttoriaModel istruttoria = istruttoriaPerDomanda183109();
		initDomandaCampioneCons.accept(handler, istruttoria);
		assertEquals(Boolean.FALSE, handler.getVariabiliInput().get(TipoVariabile.ISCAMP).getValBoolean());
	}

	@Test
	@Transactional
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void testDomandaCampione() throws Exception {
		CalcoloAccoppiatoHandler handler = new CalcoloAccoppiatoHandler();
		IstruttoriaModel istruttoria = istruttoriaPerDomanda183175();
		initDomandaCampioneCons.accept(handler, istruttoria);
		assertEquals(Boolean.TRUE, handler.getVariabiliInput().get(TipoVariabile.ISCAMP).getValBoolean());
	}


	private IstruttoriaModel istruttoriaPerDomanda183109() {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(183109));
		return istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.DISACCOPPIATO, TipoIstruttoria.ANTICIPO);
	}

	private IstruttoriaModel istruttoriaPerDomanda183175() {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(183175));
		return istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);
	}

}
