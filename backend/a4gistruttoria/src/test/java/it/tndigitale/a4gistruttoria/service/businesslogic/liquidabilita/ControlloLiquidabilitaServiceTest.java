package it.tndigitale.a4gistruttoria.service.businesslogic.liquidabilita;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.dto.DomandaUnica;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.dao.PassoTransizioneDao;
import it.tndigitale.a4gistruttoria.repository.dao.TransizioneIstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gdStatoLavSostegno;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;

@RunWith(SpringRunner.class)
@SpringBootTest
// @AutoConfigureTestDatabase
@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.visualizza", "a4gistruttoria.pac.domandaUnica.edita" })
public class ControlloLiquidabilitaServiceTest {

	@Autowired
	ControlloLiquidabilitaService controlloLiquidabilitaManager;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private DomandaUnicaDao daoDomanda;
	@Autowired
	private TransizioneIstruttoriaDao transizioneIstruttoriaDao;
	@Autowired
	private PassoTransizioneDao daoPassiLavSostegno;
	@Autowired
	private IstruttoriaDao istruttoriaDao;

	@MockBean
	private RestTemplate restTemplate;

	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/controlliLiquidabilita/180999_agsInfoLiquidabilitaTFF_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/controlliLiquidabilita/180999_agsInfoLiquidabilitaTFF_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void test_TitolareVivo_IbanValido_NoListaNeraAgea_DUF_019() throws Exception {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(180999));
		String serviceUrlAgs = "http://localhost:8080/ags/api/v1/domandeDU/".concat(domanda.getNumeroDomanda().toString()).concat("?expand=infoLiquidabilita");
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlAgs)), Mockito.eq(DomandaUnica.class))).thenReturn(getInfoLiquidabilitaExpanded019());

		IstruttoriaModel istruttoria =
				istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);

		controlloLiquidabilitaManager.elabora(istruttoria.getId());

		List<TransizioneIstruttoriaModel> transizioni = 
			transizioneIstruttoriaDao.findTransizioneLiquidabilita(istruttoria);

		transizioni.forEach(x -> {
			List<PassoTransizioneModel> passi = daoPassiLavSostegno.findByTransizioneIstruttoria(x);
			passi.forEach(y -> {
				assertEquals(y.getCodiceEsito(), "DUF_019");
			});
		});
		
		assertThat(istruttoria.getDomandaUnicaModel().getIban()).isEqualTo("IT79O0826335430000090330396");
	}

	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/controlliLiquidabilita/180999_acs_agsInfoLiquidabilitaTFT_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/controlliLiquidabilita/180999_agsInfoLiquidabilitaTFF_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testACS_TitolareVivo_IbanValido_NoListaNeraAgea_DUF_019() throws Exception {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(180999));
		String serviceUrlAgs = "http://localhost:8080/ags/api/v1/domandeDU/".concat(domanda.getNumeroDomanda().toString()).concat("?expand=infoLiquidabilita");
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlAgs)), Mockito.eq(DomandaUnica.class))).thenReturn(getInfoLiquidabilitaExpanded019());

		IstruttoriaModel istruttoria =
				istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);

		controlloLiquidabilitaManager.elabora(istruttoria.getId());

		List<TransizioneIstruttoriaModel> transizioni = 
			transizioneIstruttoriaDao.findTransizioneLiquidabilita(istruttoria);

		istruttoria = transizioni.get(0).getIstruttoria();
		assertThat(istruttoria.getStato()).isEqualTo(StatoIstruttoria.LIQUIDABILE);

		transizioni.forEach(x -> {
			List<PassoTransizioneModel> passi = daoPassiLavSostegno.findByTransizioneIstruttoria(x);
			passi.forEach(y -> {
				assertEquals(y.getCodiceEsito(), "DUF_019");
			});
		});
		
		assertThat(istruttoria.getDomandaUnicaModel().getIban()).isEqualTo("IT79O0826335430000090330396");
	}

	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/controlliLiquidabilita/180999_agsInfoLiquidabilitaTFT_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/controlliLiquidabilita/180999_agsInfoLiquidabilitaTFT_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void test_TitolareVivo_IbanValido_ListaNeraAgea_DUF_020() throws Exception {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(180999));
		String serviceUrlAgs = "http://localhost:8080/ags/api/v1/domandeDU/".concat(domanda.getNumeroDomanda().toString()).concat("?expand=infoLiquidabilita");
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlAgs)), Mockito.eq(DomandaUnica.class))).thenReturn(getInfoLiquidabilitaExpanded020());

		IstruttoriaModel istruttoria =
				istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);

		controlloLiquidabilitaManager.elabora(istruttoria.getId());

		List<TransizioneIstruttoriaModel> transizioni = 
			transizioneIstruttoriaDao.findTransizioneLiquidabilita(istruttoria);

		transizioni.forEach(x -> {
			List<PassoTransizioneModel> passi = daoPassiLavSostegno.findByTransizioneIstruttoria(x);
			passi.forEach(y -> {
				assertEquals(y.getCodiceEsito(), "DUF_020");
			});
		});
	}

	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/controlliLiquidabilita/180999_infoLiquidabilitaTitolareDecedutoConErede_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/controlliLiquidabilita/180999_infoLiquidabilitaTitolareDecedutoConErede_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testTitolareDecedutoConErede_DUF_021() throws Exception {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(180999));
		String serviceUrlAgs = "http://localhost:8080/ags/api/v1/domandeDU/".concat(domanda.getNumeroDomanda().toString()).concat("?expand=infoLiquidabilita");
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlAgs)), Mockito.eq(DomandaUnica.class))).thenReturn(getInfoLiquidabilitaTitolareDecedutoConErede());

		IstruttoriaModel istruttoria =
				istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);

		controlloLiquidabilitaManager.elabora(istruttoria.getId());

		List<TransizioneIstruttoriaModel> transizioni = 
			transizioneIstruttoriaDao.findTransizioneLiquidabilita(istruttoria);

		transizioni.forEach(x -> {
			List<PassoTransizioneModel> passi = daoPassiLavSostegno.findByTransizioneIstruttoria(x);
			passi.forEach(y -> {
				assertEquals(y.getCodiceEsito(), "DUF_021");
			});
		});
	}

	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/controlliLiquidabilita/180999_infoLiquidabilitaTitolareDecedutoConErede_1_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/controlliLiquidabilita/180999_infoLiquidabilitaTitolareDecedutoConErede_1_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testTitolareDecedutoConEredeListaAgea_DUF_022() throws Exception {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(180999));

		String serviceUrlAgs = "http://localhost:8080/ags/api/v1/domandeDU/".concat(domanda.getNumeroDomanda().toString()).concat("?expand=infoLiquidabilita");
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlAgs)), Mockito.eq(DomandaUnica.class))).thenReturn(getInfoLiquidabilitaTitolareDecedutoConErede_1());


		
		IstruttoriaModel istruttoria = 
				istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);

		controlloLiquidabilitaManager.elabora(istruttoria.getId());

		List<TransizioneIstruttoriaModel> transizioni = 
				transizioneIstruttoriaDao.findTransizioneLiquidabilita(istruttoria);

		transizioni.forEach(x -> {
			List<PassoTransizioneModel> passi = daoPassiLavSostegno.findByTransizioneIstruttoria(x);
			passi.forEach(y -> {
				assertEquals(y.getCodiceEsito(), "DUF_022");
			});
		});
	}

	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/controlliLiquidabilita/180999_infoLiquidabilitaTitolareDecedutoSenzaErede_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/controlliLiquidabilita/180999_infoLiquidabilitaTitolareDecedutoSenzaErede_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testEredeNonPresenteONonCertificato_DUF_036() throws Exception {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(180999));

		String serviceUrlAgs = "http://localhost:8080/ags/api/v1/domandeDU/".concat(domanda.getNumeroDomanda().toString()).concat("?expand=infoLiquidabilita");
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlAgs)), Mockito.eq(DomandaUnica.class))).thenReturn(getInfoLiquidabilitaTitolareDecedutoSenzaErede());


		
		IstruttoriaModel istruttoria = 
				istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);

		controlloLiquidabilitaManager.elabora(istruttoria.getId());

		List<TransizioneIstruttoriaModel> transizioni = 
			transizioneIstruttoriaDao.findTransizioneLiquidabilita(istruttoria);

		transizioni.forEach(x -> {
			List<PassoTransizioneModel> passi = daoPassiLavSostegno.findByTransizioneIstruttoria(x);
			passi.forEach(y -> {
				assertEquals(y.getCodiceEsito(), "DUF_036");
			});
		});
	}

	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/controlliLiquidabilita/180999_agsInfoLiquidabilitaFFF_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/controlliLiquidabilita/180999_agsInfoLiquidabilitaFFF_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void test_TitolareVivo_IbanNonValido_NoListaNeraAgea_DUF_023() throws Exception {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(180999));

		String serviceUrlAgs = "http://localhost:8080/ags/api/v1/domandeDU/".concat(domanda.getNumeroDomanda().toString()).concat("?expand=infoLiquidabilita");
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlAgs)), Mockito.eq(DomandaUnica.class))).thenReturn(getInfoLiquidabilitaExpanded023());


		
		IstruttoriaModel istruttoria = 
				istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);

		controlloLiquidabilitaManager.elabora(istruttoria.getId());

		List<TransizioneIstruttoriaModel> transizioni = 
			transizioneIstruttoriaDao.findTransizioneLiquidabilita(istruttoria);

		transizioni.forEach(x -> {
			List<PassoTransizioneModel> passi = daoPassiLavSostegno.findByTransizioneIstruttoria(x);
			passi.forEach(y -> {
				assertEquals(y.getCodiceEsito(), "DUF_023");
			});
		});
	}

	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/controlliLiquidabilita/180999_agsInfoLiquidabilitaFFT_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/controlliLiquidabilita/180999_agsInfoLiquidabilitaFFT_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void test_TitolareVivo_IbanNonValido_ListaNeraAgea_DUF_024() throws Exception {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(180999));

		String serviceUrlAgs = "http://localhost:8080/ags/api/v1/domandeDU/".concat(domanda.getNumeroDomanda().toString()).concat("?expand=infoLiquidabilita");
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlAgs)), Mockito.eq(DomandaUnica.class))).thenReturn(getInfoLiquidabilitaExpanded024());


		
		IstruttoriaModel istruttoria = 
				istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);

		controlloLiquidabilitaManager.elabora(istruttoria.getId());

		List<TransizioneIstruttoriaModel> transizioni = 
			transizioneIstruttoriaDao.findTransizioneLiquidabilita(istruttoria);

		transizioni.forEach(x -> {
			List<PassoTransizioneModel> passi = daoPassiLavSostegno.findByTransizioneIstruttoria(x);
			passi.forEach(y -> {
				assertEquals(y.getCodiceEsito(), "DUF_024");
			});
		});
	}

	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/controlliLiquidabilita/180999_DUF_037_importoErogabileNonPositivo_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/controlliLiquidabilita/180999_DUF_037_importoErogabileNonPositivo_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void test_NoImportoPositivo_ex_DUF_037() throws Exception {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(180999));
		String serviceUrlAgs = "http://localhost:8080/ags/api/v1/domandeDU/".concat(domanda.getNumeroDomanda().toString()).concat("?expand=infoLiquidabilita");
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlAgs)), Mockito.eq(DomandaUnica.class))).thenReturn(getInfoLiquidabilitaExpanded037());


		
		IstruttoriaModel istruttoria = 
				istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);

		controlloLiquidabilitaManager.elabora(istruttoria.getId());

		List<TransizioneIstruttoriaModel> transizioni = 
			transizioneIstruttoriaDao.findTransizioneLiquidabilita(istruttoria);
		
		assertTrue("DEBITI".equals(istruttoria.getA4gdStatoLavSostegno().getIdentificativo()));

		transizioni.forEach(x -> {
			List<PassoTransizioneModel> passi = daoPassiLavSostegno.findByTransizioneIstruttoria(x);
			passi.forEach(y -> {
				assertEquals(y.getCodiceEsito(), "DUF_043"); //importo nullo
			});
		});
	}
	
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/controlliLiquidabilita/180999_sostegno_disaccoppiato_DUF_043_importoErogabileNegativo_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/controlliLiquidabilita/180999_sostegno_disaccoppiato_DUF_043_importoErogabileNegativo_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testControlloLiquidabilitaSostegnoDisaccoppiato_ImportoNegativo_DUF_043() throws Exception {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(180999));
		String serviceUrlAgs = "http://localhost:8080/ags/api/v1/domandeDU/".concat(domanda.getNumeroDomanda().toString()).concat("?expand=infoLiquidabilita");
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlAgs)), Mockito.eq(DomandaUnica.class))).thenReturn(getInfoLiquidabilitaExpanded043());


		
		IstruttoriaModel istruttoria = 
				istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);

		controlloLiquidabilitaManager.elabora(istruttoria.getId());

		List<TransizioneIstruttoriaModel> transizioni = 
			transizioneIstruttoriaDao.findTransizioneLiquidabilita(istruttoria);
		
		assertFalse(transizioni.isEmpty());
		assertTrue("DEBITI".equals(istruttoria.getA4gdStatoLavSostegno().getIdentificativo()));

		transizioni.forEach(x -> {
			List<PassoTransizioneModel> passi = daoPassiLavSostegno.findByTransizioneIstruttoria(x);
			passi.forEach(y -> {
				assertEquals(y.getCodiceEsito(), "DUF_043");
			});
		});
	}
	
	
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/controlliLiquidabilita/180999_sostegno_disaccoppiato_DUF_044_importoErogabileNegativo_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/controlliLiquidabilita/180999_sostegno_disaccoppiato_DUF_044_importoErogabileNegativo_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testControlloLiquidabilitaSostegnoDisaccoppiato_ImportoNegativo_DUF_044() throws Exception {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(180999));
		String serviceUrlAgs = "http://localhost:8080/ags/api/v1/domandeDU/".concat(domanda.getNumeroDomanda().toString()).concat("?expand=infoLiquidabilita");
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlAgs)), Mockito.eq(DomandaUnica.class))).thenReturn(getInfoLiquidabilitaExpanded044());


		
		IstruttoriaModel istruttoria = 
				istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);

		controlloLiquidabilitaManager.elabora(istruttoria.getId());

		List<TransizioneIstruttoriaModel> transizioni = 
			transizioneIstruttoriaDao.findTransizioneLiquidabilita(istruttoria);
		
		
		
		assertFalse(transizioni.isEmpty());
		assertTrue("DEBITI".equals(istruttoria.getA4gdStatoLavSostegno().getIdentificativo()));

		transizioni.forEach(x -> {
			//A4gdStatoLavSostegno statoInit = x.getA4gdStatoLavSostegno1();
			//A4gdStatoLavSostegno statoFinale = x.getA4gdStatoLavSostegno2();
			List<PassoTransizioneModel> passi = daoPassiLavSostegno.findByTransizioneIstruttoria(x);
			passi.forEach(y -> {
				assertEquals(y.getCodiceEsito(), "DUF_044");
			});
		});
	}
	
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/controlliLiquidabilita/180999_sostegno_disaccoppiato_DUF_045_importoErogabileNegativo_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/controlliLiquidabilita/180999_sostegno_disaccoppiato_DUF_045_importoErogabileNegativo_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testControlloLiquidabilitaSostegnoDisaccoppiato_ImportoNegativo_DUF_045_non_specificato() throws Exception {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(180999));
		String serviceUrlAgs = "http://localhost:8080/ags/api/v1/domandeDU/".concat(domanda.getNumeroDomanda().toString()).concat("?expand=infoLiquidabilita");
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlAgs)), Mockito.eq(DomandaUnica.class))).thenReturn(getInfoLiquidabilitaExpanded045());


		
		IstruttoriaModel istruttoria = 
				istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);

		controlloLiquidabilitaManager.elabora(istruttoria.getId());

		List<TransizioneIstruttoriaModel> transizioni = 
			transizioneIstruttoriaDao.findTransizioneLiquidabilita(istruttoria);
		
		assertFalse(transizioni.isEmpty());
		assertTrue("DEBITI".equals(istruttoria.getA4gdStatoLavSostegno().getIdentificativo()));

		transizioni.forEach(x -> {
			List<PassoTransizioneModel> passi = daoPassiLavSostegno.findByTransizioneIstruttoria(x);
			passi.forEach(y -> {
				assertEquals(y.getCodiceEsito(), "DUF_045");
			});
		});
	}
	
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/controlliLiquidabilita/180999_sostegno_disaccoppiato_DUF_045_importoErogabile_0_12_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/controlliLiquidabilita/180999_sostegno_disaccoppiato_DUF_045_importoErogabileNegativo_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testControlloLiquidabilitaSostegnoDisaccoppiato_Importo_0_12_DUF_045() throws Exception {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(180999));
		String serviceUrlAgs = "http://localhost:8080/ags/api/v1/domandeDU/".concat(domanda.getNumeroDomanda().toString()).concat("?expand=infoLiquidabilita");
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlAgs)), Mockito.eq(DomandaUnica.class))).thenReturn(getInfoLiquidabilitaExpanded045());


		
		IstruttoriaModel istruttoria = 
				istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);

		controlloLiquidabilitaManager.elabora(istruttoria.getId());

		List<TransizioneIstruttoriaModel> transizioni = 
			transizioneIstruttoriaDao.findTransizioneLiquidabilita(istruttoria);
		
		assertFalse(transizioni.isEmpty());
		assertTrue("DEBITI".equals(istruttoria.getA4gdStatoLavSostegno().getIdentificativo()));

		transizioni.forEach(x -> {
			List<PassoTransizioneModel> passi = daoPassiLavSostegno.findByTransizioneIstruttoria(x);
			passi.forEach(y -> {
				assertEquals(y.getCodiceEsito(), "DUF_045");
			});
		});
	}
	
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/controlliLiquidabilita/180999_sostegno_disaccoppiato_DUF_045_importoErogabileNull_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/controlliLiquidabilita/180999_sostegno_disaccoppiato_DUF_045_importoErogabileNegativo_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testControlloLiquidabilitaSostegnoDisaccoppiato_ImportoNegativo_DUF_045_null() throws Exception {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(180999));
		String serviceUrlAgs = "http://localhost:8080/ags/api/v1/domandeDU/".concat(domanda.getNumeroDomanda().toString()).concat("?expand=infoLiquidabilita");
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlAgs)), Mockito.eq(DomandaUnica.class))).thenReturn(getInfoLiquidabilitaExpanded045());


		
		IstruttoriaModel istruttoria = 
				istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);

		controlloLiquidabilitaManager.elabora(istruttoria.getId());

		List<TransizioneIstruttoriaModel> transizioni = 
			transizioneIstruttoriaDao.findTransizioneLiquidabilita(istruttoria);
		
		assertFalse(transizioni.isEmpty());
		assertTrue("DEBITI".equals(istruttoria.getA4gdStatoLavSostegno().getIdentificativo()));

		transizioni.forEach(x -> {
			List<PassoTransizioneModel> passi = daoPassiLavSostegno.findByTransizioneIstruttoria(x);
			passi.forEach(y -> {
				assertEquals(y.getCodiceEsito(), "DUF_045");
			});
		});
	}
	
	
	

	private DomandaUnica getInfoLiquidabilitaExpanded019() throws IOException {
		return objectMapper.readValue(new File("src/test/resources/DomandaUnica/controlliLiquidabilita/180999_agsInfoLiquidabilitaTFF.json"), DomandaUnica.class);
	}

	private DomandaUnica getInfoLiquidabilitaExpanded020() throws IOException {
		return objectMapper.readValue(new File("src/test/resources/DomandaUnica/controlliLiquidabilita/180999_agsInfoLiquidabilitaTFT.json"), DomandaUnica.class);
	}

	private DomandaUnica getInfoLiquidabilitaTitolareDecedutoSenzaErede() throws IOException {
		return objectMapper.readValue(new File("src/test/resources/DomandaUnica/controlliLiquidabilita/180999_agsInfoLiquidabilitaEredeNonPresenteTTF.json"), DomandaUnica.class);
	}

	private DomandaUnica getInfoLiquidabilitaTitolareDecedutoConErede() throws IOException {
		return objectMapper.readValue(new File("src/test/resources/DomandaUnica/controlliLiquidabilita/180999_agsInfoLiquidabilitaTTF.json"), DomandaUnica.class);
	}

	private DomandaUnica getInfoLiquidabilitaTitolareDecedutoConErede_1() throws IOException {
		return objectMapper.readValue(new File("src/test/resources/DomandaUnica/controlliLiquidabilita/180999_agsInfoLiquidabilitaTTT.json"), DomandaUnica.class);
	}

	private DomandaUnica getInfoLiquidabilitaExpanded023() throws IOException {
		return objectMapper.readValue(new File("src/test/resources/DomandaUnica/controlliLiquidabilita/180999_agsInfoLiquidabilitaFFF.json"), DomandaUnica.class);
	}

	private DomandaUnica getInfoLiquidabilitaExpanded024() throws IOException {
		return objectMapper.readValue(new File("src/test/resources/DomandaUnica/controlliLiquidabilita/180999_agsInfoLiquidabilitaFFT.json"), DomandaUnica.class);
	}

	private DomandaUnica getInfoLiquidabilitaExpanded037() throws IOException {
		return objectMapper.readValue(new File("src/test/resources/DomandaUnica/controlliLiquidabilita/180999_DUF_037_importoErogabileNonPositivo.json"), DomandaUnica.class);
	}
	
	private DomandaUnica getInfoLiquidabilitaExpanded043() throws IOException {
		return objectMapper.readValue(new File("src/test/resources/DomandaUnica/controlliLiquidabilita/180999_sostegno_disaccoppiato_DUF_043_importoErogabileNegativo.json"), DomandaUnica.class);
	}
	
	private DomandaUnica getInfoLiquidabilitaExpanded044() throws IOException {
		return objectMapper.readValue(new File("src/test/resources/DomandaUnica/controlliLiquidabilita/180999_sostegno_disaccoppiato_DUF_044_importoErogabileNegativo.json"), DomandaUnica.class);
	}
	
	private DomandaUnica getInfoLiquidabilitaExpanded045() throws IOException {
		return objectMapper.readValue(new File("src/test/resources/DomandaUnica/controlliLiquidabilita/180999_sostegno_disaccoppiato_DUF_045_importoErogabileNegativo.json"), DomandaUnica.class);
	}
	
}
