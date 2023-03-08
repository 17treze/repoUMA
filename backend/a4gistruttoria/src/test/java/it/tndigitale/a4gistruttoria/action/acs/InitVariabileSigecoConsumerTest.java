package it.tndigitale.a4gistruttoria.action.acs;


import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.util.function.Function;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class InitVariabileSigecoConsumerTest {

	@Autowired
	private InitVariabileSigecoConsumer ivSigecoCons;

	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private RestTemplate restTemplate;

	@Autowired
	private DomandaUnicaDao daoDomanda;

	@Autowired
	private IstruttoriaDao istruttoriaDao;

	@Value("${a4gistruttoria.proxy.sigeco.uri}")
	private String esitoSigecoUrl;
	

	@Test
	@Transactional
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void testDomandaEsitoSigeco2IsFalse() throws Exception {
		CalcoloAccoppiatoHandler handler = new CalcoloAccoppiatoHandler();
		IstruttoriaModel istruttoria = istruttoriaPerDomanda183175();
		mockDomandaSigeco(istruttoria.getDomandaUnicaModel().getNumeroDomanda(), this::getResponseSigecoEsito2);
		ivSigecoCons.accept(handler, istruttoria);
		assertEquals(Boolean.FALSE, handler.getVariabiliInput().get(InitVariabileSigecoConsumer.DOMSIGECOCHIUSA).getValBoolean());
	}

	@Test
	@Transactional
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void testDomandaEsitoSigecoNull() throws Exception {
		CalcoloAccoppiatoHandler handler = new CalcoloAccoppiatoHandler();
		IstruttoriaModel istruttoria = istruttoriaPerDomanda183175();
		mockDomandaSigeco(istruttoria.getDomandaUnicaModel().getNumeroDomanda(), this::getResponseSigecoNull);
		ivSigecoCons.accept(handler, istruttoria);
		assertEquals(Boolean.FALSE, handler.getVariabiliInput().get(InitVariabileSigecoConsumer.DOMSIGECOCHIUSA).getValBoolean());
	}

	@Test
	@Transactional
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void testDomandaEsitoSigeco4AndCheckIsTrue() throws Exception {
		CalcoloAccoppiatoHandler handler = new CalcoloAccoppiatoHandler();
		IstruttoriaModel istruttoria = istruttoriaPerDomanda183175();
		mockDomandaSigeco(istruttoria.getDomandaUnicaModel().getNumeroDomanda(), this::getResponseSigecoEsito4);
		ivSigecoCons.accept(handler, istruttoria);
		assertEquals(Boolean.TRUE, handler.getVariabiliInput().get(InitVariabileSigecoConsumer.DOMSIGECOCHIUSA).getValBoolean());
	}

	@Test
	@Transactional
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void testDomandaEsitoSigeco5AndCheckIsTrue() throws Exception {
		CalcoloAccoppiatoHandler handler = new CalcoloAccoppiatoHandler();
		IstruttoriaModel istruttoria = istruttoriaPerDomanda183175();
		mockDomandaSigeco(istruttoria.getDomandaUnicaModel().getNumeroDomanda(), this::getResponseSigecoEsito5);
		ivSigecoCons.accept(handler, istruttoria);
		assertEquals(Boolean.TRUE, handler.getVariabiliInput().get(InitVariabileSigecoConsumer.DOMSIGECOCHIUSA).getValBoolean());
	}
	
	@Test
	@Transactional
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	@Sql(statements = "Insert into A4GT_ISTRUTTORIA(ID,VERSIONE, TIPOLOGIA,ID_DOMANDA,SOSTEGNO,ID_STATO_LAVORAZIONE) values(NXTNBR.nextval,0, 'SALDO',(SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'VLNLDA81T24C794L'),'SUPERFICIE',(select id from a4gd_stato_lav_sostegno where identificativo = 'LIQUIDABILE')); commit;", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "delete from A4GT_ISTRUTTORIA where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'VLNLDA81T24C794L') and SOSTEGNO = 'SUPERFICIE'; commit;" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testDomandaEsitoSigeco5AndNotCheckIsFalse() throws Exception {
		CalcoloAccoppiatoHandler handler = new CalcoloAccoppiatoHandler();
		IstruttoriaModel istruttoria = istruttoriaPerDomanda182864();
		mockDomandaSigeco(istruttoria.getDomandaUnicaModel().getNumeroDomanda(), this::getResponseSigecoEsito5);
		ivSigecoCons.accept(handler, istruttoria);
		assertEquals(Boolean.FALSE, handler.getVariabiliInput().get(InitVariabileSigecoConsumer.DOMSIGECOCHIUSA).getValBoolean());
	}

	@Test
	@Transactional
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	@Sql(statements = "Insert into A4GT_ISTRUTTORIA(ID,VERSIONE, TIPOLOGIA,ID_DOMANDA,SOSTEGNO,ID_STATO_LAVORAZIONE) values(NXTNBR.nextval,0, 'SALDO',(SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'VLNLDA81T24C794L'),'SUPERFICIE',(select id from a4gd_stato_lav_sostegno where identificativo = 'LIQUIDABILE')); commit;", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "delete from A4GT_ISTRUTTORIA where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'VLNLDA81T24C794L') and SOSTEGNO = 'SUPERFICIE'; commit;" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testDomandaEsitoSigeco4AndNotCheckIsFalse() throws Exception {
		CalcoloAccoppiatoHandler handler = new CalcoloAccoppiatoHandler();
		IstruttoriaModel istruttoria = istruttoriaPerDomanda182864();
		mockDomandaSigeco(istruttoria.getDomandaUnicaModel().getNumeroDomanda(), this::getResponseSigecoEsito4);
		ivSigecoCons.accept(handler, istruttoria);
		assertEquals(Boolean.FALSE, handler.getVariabiliInput().get(InitVariabileSigecoConsumer.DOMSIGECOCHIUSA).getValBoolean());
	}

	protected void mockDomandaSigeco(BigDecimal numeroDomanda, Function<BigDecimal, Long> getResponseFunction) throws Exception {
		String jsonRequest = "{\"anno\":2018, \"numeroDomanda\":" + numeroDomanda + "}";
		String serviceUrlA4GPROXYSigeco = esitoSigecoUrl + "?params=".concat(URLEncoder.encode(jsonRequest, "UTF-8"));
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlA4GPROXYSigeco)), Mockito.eq(Long.class))).thenReturn(getResponseFunction.apply(numeroDomanda));		
	}

	protected Long getResponseSigecoNull(BigDecimal s) {
		return getResponseSigeco(null);
	}

	protected Long getResponseSigecoEsito2(BigDecimal s) {
		return getResponseSigeco(2L);
	}

	protected Long getResponseSigecoEsito4(BigDecimal s) {
		return getResponseSigeco(4L);
	}

	protected Long getResponseSigecoEsito5(BigDecimal s) {
		return getResponseSigeco(5L);
	}

	protected Long getResponseSigeco(Long s)  {
		return s;
	}



	private IstruttoriaModel istruttoriaPerDomanda182864() {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(182864));
		return istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);
	}


	private IstruttoriaModel istruttoriaPerDomanda183175() {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(183175));
		return istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);
	}

}

