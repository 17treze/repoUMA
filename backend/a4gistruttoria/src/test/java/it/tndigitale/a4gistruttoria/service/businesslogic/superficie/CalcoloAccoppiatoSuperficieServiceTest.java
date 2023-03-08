package it.tndigitale.a4gistruttoria.service.businesslogic.superficie;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.assertj.core.api.Assertions;
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.dto.AgricoltoreSIAN;
import it.tndigitale.a4gistruttoria.dto.DatiCatastaliRegione;
import it.tndigitale.a4gistruttoria.dto.DomandaUnica;
import it.tndigitale.a4gistruttoria.dto.lavorazione.MappaEsitiFoglieAmmissibilitaAccoppiatoSuperficie.FoglieAmmissibilitaAccoppiatoSuperficie;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.dao.PassoTransizioneDao;
import it.tndigitale.a4gistruttoria.repository.model.CodiceInterventoAgs;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CalcoloAccoppiatoSuperficieServiceTest {

	@Autowired
	private CalcoloAccoppiatoSuperficieService service;

	@Autowired
	private PassoTransizioneDao passiLavorazioneSostegnoDao;

	@Autowired
	private DomandaUnicaDao daoDomanda;
	
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private RestTemplate restTemplate;

	@Value("${a4gistruttoria.proxy.agricoltore.uri}")
	private String agricoltoreAttivoUrl;

	@Value("${a4gistruttoria.proxy.sigeco.uri}")
	private String esitoSigecoUrl;

	@Value("${a4gistruttoria.proxy.anomaliecoordinamento.uri}")
	private String anomalieCoordinamentoUri;

	@Value("${a4gistruttoria.ags.uri}")
	private String agsUri;

	@Value("${superficie.controlloregistat.intervento122}")
	private String[] regioniIntervento122;

	@Value("${superficie.controlloregistat.intervento123}")
	private String[] regioniIntervento123;

	@Value("${superficie.controlloregistat.intervento124}")
	private String[] regioniIntervento124;

	@Value("${superficie.controlloregistat.intervento125}")
	private String[] regioniIntervento125;

	@Autowired
	private IstruttoriaDao istruttoriaDao;

	@PersistenceContext
	private EntityManager entityManager;


	@Test
	@Transactional
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void test1() throws Exception {
		String cuaa = "BNDCLD69S12C794E";
		Integer campagna = 2018;
		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna(cuaa,campagna);
		mockInfoIstruttoria(domanda.getNumeroDomanda().toString());
		mockInfoAgricoltoreSIAN(cuaa, this::getResponseAgricoltoreSianAttivo);
		mockDomandaSigeco(domanda.getNumeroDomanda(), this::getResponseSigecoEsito4);
		service.elabora(istruttoriaPerDomanda180523());
	}

	private Long istruttoriaPerDomanda180523() {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(180523));

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);
		return (istruttoria != null)? istruttoria.getId():null;
	}


	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/acs/duf_001_f_OK.sql", "/DomandaUnica/acs/dataMatriceCompatibilitaOlivo.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "delete from A4GD_COLTURA_INTERVENTO", "commit" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_001_f_OK() throws Exception {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(BigDecimal.valueOf(189691));
		String cuaa = domanda.getCuaaIntestatario();
		mockAnomalieCoordinamento(0);
		mockInfoIstruttoria(domanda.getNumeroDomanda().toString());
		mockInfoAgricoltoreSIAN(cuaa, this::getResponseAgricoltoreSianAttivo);
		mockDomandaSigeco(domanda.getNumeroDomanda(), this::getResponseSigecoEsito5);
		service.elabora(istruttoriaPerDomanda189691().getId());

		IstruttoriaModel statoSostegnoDomandaAggiornato = istruttoriaPerDomanda189691();
		entityManager.refresh(statoSostegnoDomandaAggiornato);
		assertNotNull(statoSostegnoDomandaAggiornato);
		assertEquals(StatoIstruttoria.CONTROLLI_CALCOLO_OK.getStatoIstruttoria(), statoSostegnoDomandaAggiornato.getA4gdStatoLavSostegno().getIdentificativo());

		// List<TransizioneIstruttoriaModel> transizioni = transizioneDao.findTransizioniByIstruttoriaModel(statoSostegnoDomandaAggiornato);
		Set<TransizioneIstruttoriaModel> transizioni = statoSostegnoDomandaAggiornato.getTransizioni();
		assertNotNull(transizioni);
		assertTrue(!transizioni.isEmpty());
		Optional<TransizioneIstruttoriaModel> optTrans = transizioni.stream().sorted(Comparator.comparing(TransizioneIstruttoriaModel::getDataEsecuzione).reversed()).findFirst();
		assertTrue(optTrans.isPresent());
		TransizioneIstruttoriaModel transizione = optTrans.get();
		List<PassoTransizioneModel> passi = passiLavorazioneSostegnoDao.findByTransizioneIstruttoria(transizione);
		assertNotNull(passi);
		assertEquals(1, passi.size());
		PassoTransizioneModel passoLavorazione = passi.get(0);
		assertEquals(TipologiaPassoTransizione.CALCOLO_ACS, passoLavorazione.getCodicePasso());
		assertEquals(FoglieAmmissibilitaAccoppiatoSuperficie.DUF_001_F_OK.getCodiceEsito(), passoLavorazione.getCodiceEsito());
		assertEquals("OK", passoLavorazione.getEsito());
	}

	private IstruttoriaModel istruttoriaPerDomanda189691() {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(189691));

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);
		return istruttoria;
	}

	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/acs/duf_001_f_KO.sql", "/DomandaUnica/acs/dataMatriceCompatibilitaOlivo.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "delete from A4GD_COLTURA_INTERVENTO", "commit" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_001_f_KO() throws Exception {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(BigDecimal.valueOf(189695));
		String cuaa = domanda.getCuaaIntestatario();
		mockAnomalieCoordinamento(0);
		mockInfoIstruttoria(domanda.getNumeroDomanda().toString());
		mockInfoAgricoltoreSIAN(cuaa, this::getResponseAgricoltoreSianAttivo);
		mockDomandaSigeco(domanda.getNumeroDomanda(), this::getResponseSigecoEsito5);
		service.elabora(istruttoriaPerDomanda189695().getId());

		IstruttoriaModel statoSostegnoDomandaAggiornato = istruttoriaPerDomanda189695();
		entityManager.refresh(statoSostegnoDomandaAggiornato);
		assertNotNull(statoSostegnoDomandaAggiornato);
		assertEquals(StatoIstruttoria.CONTROLLI_CALCOLO_KO.getStatoIstruttoria(), statoSostegnoDomandaAggiornato.getA4gdStatoLavSostegno().getIdentificativo());

		// List<TransizioneIstruttoriaModel> transizioni = transizioneDao.findTransizioniByIstruttoriaModel(statoSostegnoDomandaAggiornato);
		Set<TransizioneIstruttoriaModel> transizioni = statoSostegnoDomandaAggiornato.getTransizioni();
		assertNotNull(transizioni);
		assertTrue(!transizioni.isEmpty());
		Optional<TransizioneIstruttoriaModel> optTrans = transizioni.stream().sorted(Comparator.comparing(TransizioneIstruttoriaModel::getDataEsecuzione).reversed()).findFirst();
		assertTrue(optTrans.isPresent());
		TransizioneIstruttoriaModel transizione = optTrans.get();
		List<PassoTransizioneModel> passi = passiLavorazioneSostegnoDao.findByTransizioneIstruttoria(transizione);
		assertNotNull(passi);
		assertEquals(1, passi.size());
		PassoTransizioneModel passoLavorazione = passi.get(0);
		assertEquals(TipologiaPassoTransizione.CALCOLO_ACS, passoLavorazione.getCodicePasso());
		assertEquals(FoglieAmmissibilitaAccoppiatoSuperficie.DUF_001_F_KO.getCodiceEsito(), passoLavorazione.getCodiceEsito());
		assertEquals("KO", passoLavorazione.getEsito());
	}


	private IstruttoriaModel istruttoriaPerDomanda189695() {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(189695));

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);
		return istruttoria;
	}



	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/acs/duf_002_f.sql", "/DomandaUnica/acs/dataMatriceCompatibilitaOlivo.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "delete from A4GD_COLTURA_INTERVENTO", "commit" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_002_f() throws Exception {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(BigDecimal.valueOf(189692));
		String cuaa = domanda.getCuaaIntestatario();
		mockAnomalieCoordinamento(0);
		mockInfoIstruttoria(domanda.getNumeroDomanda().toString());
		mockInfoAgricoltoreSIAN(cuaa, this::getResponseAgricoltoreSianAttivo);
		mockDomandaSigeco(domanda.getNumeroDomanda(), this::getResponseSigecoEsito5);
		service.elabora(istruttoriaPerDomanda189692().getId());

		IstruttoriaModel statoSostegnoDomandaAggiornato = istruttoriaPerDomanda189692();
		entityManager.refresh(statoSostegnoDomandaAggiornato);
		assertNotNull(statoSostegnoDomandaAggiornato);
		assertEquals(StatoIstruttoria.CONTROLLI_CALCOLO_KO.getStatoIstruttoria(), statoSostegnoDomandaAggiornato.getA4gdStatoLavSostegno().getIdentificativo());

		// List<TransizioneIstruttoriaModel> transizioni = transizioneDao.findTransizioniByIstruttoriaModel(statoSostegnoDomandaAggiornato);
		Set<TransizioneIstruttoriaModel> transizioni = statoSostegnoDomandaAggiornato.getTransizioni();
		assertNotNull(transizioni);
		assertTrue(!transizioni.isEmpty());
		Optional<TransizioneIstruttoriaModel> optTrans = transizioni.stream().sorted(Comparator.comparing(TransizioneIstruttoriaModel::getDataEsecuzione).reversed()).findFirst();
		assertTrue(optTrans.isPresent());
		TransizioneIstruttoriaModel transizione = optTrans.get();
		List<PassoTransizioneModel> passi = passiLavorazioneSostegnoDao.findByTransizioneIstruttoria(transizione);
		assertNotNull(passi);
		assertEquals(1, passi.size());
		PassoTransizioneModel passoLavorazione = passi.get(0);
		assertEquals(TipologiaPassoTransizione.CALCOLO_ACS, passoLavorazione.getCodicePasso());
		assertEquals(FoglieAmmissibilitaAccoppiatoSuperficie.DUF_002_F.getCodiceEsito(), passoLavorazione.getCodiceEsito());
		assertEquals("KO", passoLavorazione.getEsito());
	}


	private IstruttoriaModel istruttoriaPerDomanda189692() {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(189692));

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);
		return istruttoria;
	}




	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/acs/duf_005_f_OK.sql", "/DomandaUnica/acs/dataMatriceCompatibilitaOlivo.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "delete from A4GD_COLTURA_INTERVENTO", "commit" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_005_f_OK() throws Exception {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(BigDecimal.valueOf(189693));
		String cuaa = domanda.getCuaaIntestatario();
		mockAnomalieCoordinamento(0);
		mockInfoIstruttoria(domanda.getNumeroDomanda().toString());
		mockInfoAgricoltoreSIAN(cuaa, this::getResponseAgricoltoreSianAttivo);
		mockDomandaSigeco(domanda.getNumeroDomanda(), this::getResponseSigecoNull);
		service.elabora(istruttoriaPerDomanda189693().getId());

		IstruttoriaModel statoSostegnoDomandaAggiornato = istruttoriaPerDomanda189693();
		entityManager.refresh(statoSostegnoDomandaAggiornato);
		assertNotNull(statoSostegnoDomandaAggiornato);
		assertEquals(StatoIstruttoria.CONTROLLI_CALCOLO_OK.getStatoIstruttoria(), statoSostegnoDomandaAggiornato.getA4gdStatoLavSostegno().getIdentificativo());

		// List<TransizioneIstruttoriaModel> transizioni = transizioneDao.findTransizioniByIstruttoriaModel(statoSostegnoDomandaAggiornato);
		Set<TransizioneIstruttoriaModel> transizioni = statoSostegnoDomandaAggiornato.getTransizioni();
		assertNotNull(transizioni);
		assertTrue(!transizioni.isEmpty());
		Optional<TransizioneIstruttoriaModel> optTrans = transizioni.stream().sorted(Comparator.comparing(TransizioneIstruttoriaModel::getDataEsecuzione).reversed()).findFirst();
		assertTrue(optTrans.isPresent());
		TransizioneIstruttoriaModel transizione = optTrans.get();
		List<PassoTransizioneModel> passi = passiLavorazioneSostegnoDao.findByTransizioneIstruttoria(transizione);
		assertNotNull(passi);
		assertEquals(1, passi.size());
		PassoTransizioneModel passoLavorazione = passi.get(0);
		assertEquals(TipologiaPassoTransizione.CALCOLO_ACS, passoLavorazione.getCodicePasso());
		assertEquals(FoglieAmmissibilitaAccoppiatoSuperficie.DUF_005_F_OK.getCodiceEsito(), passoLavorazione.getCodiceEsito());
		assertEquals("OK", passoLavorazione.getEsito());
	}

	private IstruttoriaModel istruttoriaPerDomanda189693() {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(189693));

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);
		return istruttoria;
	}





	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/acs/duf_005_f_m16_olionaz_ok.sql", "/DomandaUnica/acs/dataMatriceCompatibilitaOlivoTutti.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "delete from A4GD_COLTURA_INTERVENTO", "commit" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_005_f_m16_olionaz_OK() throws Exception {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(BigDecimal.valueOf(189699));
		String cuaa = domanda.getCuaaIntestatario();
		mockAnomalieCoordinamento(0);
		mockInfoIstruttoria(domanda.getNumeroDomanda().toString());
		mockInfoAgricoltoreSIAN(cuaa, this::getResponseAgricoltoreSianAttivo);
		mockDomandaSigeco(domanda.getNumeroDomanda(), this::getResponseSigecoNull);
		service.elabora(istruttoriaPerDomanda189699().getId());

		IstruttoriaModel statoSostegnoDomandaAggiornato = istruttoriaPerDomanda189699();
		entityManager.refresh(statoSostegnoDomandaAggiornato);
		assertNotNull(statoSostegnoDomandaAggiornato);
		assertEquals(StatoIstruttoria.CONTROLLI_CALCOLO_OK.getStatoIstruttoria(), statoSostegnoDomandaAggiornato.getA4gdStatoLavSostegno().getIdentificativo());

		// List<TransizioneIstruttoriaModel> transizioni = transizioneDao.findTransizioniByIstruttoriaModel(statoSostegnoDomandaAggiornato);
		Set<TransizioneIstruttoriaModel> transizioni = statoSostegnoDomandaAggiornato.getTransizioni();
		assertNotNull(transizioni);
		assertTrue(!transizioni.isEmpty());
		Optional<TransizioneIstruttoriaModel> optTrans = transizioni.stream().sorted(Comparator.comparing(TransizioneIstruttoriaModel::getDataEsecuzione).reversed()).findFirst();
		assertTrue(optTrans.isPresent());
		TransizioneIstruttoriaModel transizione = optTrans.get();
		List<PassoTransizioneModel> passi = passiLavorazioneSostegnoDao.findByTransizioneIstruttoria(transizione);
		assertNotNull(passi);
		assertEquals(1, passi.size());
		PassoTransizioneModel passoLavorazione = passi.get(0);
		assertEquals(TipologiaPassoTransizione.CALCOLO_ACS, passoLavorazione.getCodicePasso());
		assertEquals(FoglieAmmissibilitaAccoppiatoSuperficie.DUF_005_F_OK.getCodiceEsito(), passoLavorazione.getCodiceEsito());
		assertEquals("OK", passoLavorazione.getEsito());
	}

	private IstruttoriaModel istruttoriaPerDomanda189699() {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(189699));

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);
		return istruttoria;
	}





	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/acs/duf_005_f_m16_olionaz_ko.sql", "/DomandaUnica/acs/dataMatriceCompatibilitaOlivoTutti.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "delete from A4GD_COLTURA_INTERVENTO", "commit" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_005_f_m16_olionaz_KO() throws Exception {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(BigDecimal.valueOf(189698));
		String cuaa = domanda.getCuaaIntestatario();
		mockAnomalieCoordinamento(0);
		mockInfoIstruttoria(domanda.getNumeroDomanda().toString());
		mockInfoAgricoltoreSIAN(cuaa, this::getResponseAgricoltoreSianAttivo);
		mockDomandaSigeco(domanda.getNumeroDomanda(), this::getResponseSigecoNull);
		service.elabora(istruttoriaPerDomanda189698().getId());

		IstruttoriaModel statoSostegnoDomandaAggiornato = istruttoriaPerDomanda189698();
		entityManager.refresh(statoSostegnoDomandaAggiornato);
		assertNotNull(statoSostegnoDomandaAggiornato);
		assertEquals(StatoIstruttoria.CONTROLLI_CALCOLO_OK.getStatoIstruttoria(), statoSostegnoDomandaAggiornato.getA4gdStatoLavSostegno().getIdentificativo());

		// List<TransizioneIstruttoriaModel> transizioni = transizioneDao.findTransizioniByIstruttoriaModel(statoSostegnoDomandaAggiornato);
		Set<TransizioneIstruttoriaModel> transizioni = statoSostegnoDomandaAggiornato.getTransizioni();
		assertNotNull(transizioni);
		assertTrue(!transizioni.isEmpty());
		Optional<TransizioneIstruttoriaModel> optTrans = transizioni.stream().sorted(Comparator.comparing(TransizioneIstruttoriaModel::getDataEsecuzione).reversed()).findFirst();
		assertTrue(optTrans.isPresent());
		TransizioneIstruttoriaModel transizione = optTrans.get();
		List<PassoTransizioneModel> passi = passiLavorazioneSostegnoDao.findByTransizioneIstruttoria(transizione);
		assertNotNull(passi);
		assertEquals(1, passi.size());
		PassoTransizioneModel passoLavorazione = passi.get(0);
		assertEquals(TipologiaPassoTransizione.CALCOLO_ACS, passoLavorazione.getCodicePasso());
		assertEquals(FoglieAmmissibilitaAccoppiatoSuperficie.DUF_005_F_OK.getCodiceEsito(), passoLavorazione.getCodiceEsito());
		assertEquals("OK", passoLavorazione.getEsito());
	}


	private IstruttoriaModel istruttoriaPerDomanda189698() {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(189698));

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);
		return istruttoria;
	}



	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/acs/duf_005_f_KO.sql", "/DomandaUnica/acs/dataMatriceCompatibilitaOlivo.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "delete from A4GD_COLTURA_INTERVENTO", "commit" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_005_f_KO() throws Exception {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(BigDecimal.valueOf(189694));
		String cuaa = domanda.getCuaaIntestatario();
		mockAnomalieCoordinamento(0);
		mockInfoIstruttoria(domanda.getNumeroDomanda().toString());
		mockInfoAgricoltoreSIAN(cuaa, this::getResponseAgricoltoreSianAttivo);
		mockDomandaSigeco(domanda.getNumeroDomanda(), this::getResponseSigecoNull);
		mockControlloRegioni(CodiceInterventoAgs.SOIA, "L396", true);
		service.elabora(istruttoriaPerDomanda189694().getId());

		IstruttoriaModel statoSostegnoDomandaAggiornato = istruttoriaPerDomanda189694();
		entityManager.refresh(statoSostegnoDomandaAggiornato);
		assertNotNull(statoSostegnoDomandaAggiornato);
		assertEquals(StatoIstruttoria.CONTROLLI_CALCOLO_KO.getStatoIstruttoria(), statoSostegnoDomandaAggiornato.getA4gdStatoLavSostegno().getIdentificativo());

		// List<TransizioneIstruttoriaModel> transizioni = transizioneDao.findTransizioniByIstruttoriaModel(statoSostegnoDomandaAggiornato);
		Set<TransizioneIstruttoriaModel> transizioni = statoSostegnoDomandaAggiornato.getTransizioni();
		assertNotNull(transizioni);
		assertTrue(!transizioni.isEmpty());
		Optional<TransizioneIstruttoriaModel> optTrans = transizioni.stream().sorted(Comparator.comparing(TransizioneIstruttoriaModel::getDataEsecuzione).reversed()).findFirst();
		assertTrue(optTrans.isPresent());
		TransizioneIstruttoriaModel transizione = optTrans.get();
		List<PassoTransizioneModel> passi = passiLavorazioneSostegnoDao.findByTransizioneIstruttoria(transizione);
		assertNotNull(passi);
		assertEquals(1, passi.size());
		PassoTransizioneModel passoLavorazione = passi.get(0);
		assertEquals(TipologiaPassoTransizione.CALCOLO_ACS, passoLavorazione.getCodicePasso());
		assertEquals(FoglieAmmissibilitaAccoppiatoSuperficie.DUF_005_F_KO.getCodiceEsito(), passoLavorazione.getCodiceEsito());
		assertEquals("KO", passoLavorazione.getEsito());
	}

	private IstruttoriaModel istruttoriaPerDomanda189694() {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(189694));

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);
		return istruttoria;
	}



	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/acs/duf_006_f.sql", "/DomandaUnica/acs/dataMatriceCompatibilitaOlivo.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "delete from A4GD_COLTURA_INTERVENTO", "commit" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_006_f() throws Exception {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(BigDecimal.valueOf(189690));
		String cuaa = domanda.getCuaaIntestatario();
		mockAnomalieCoordinamento(0);
		mockInfoIstruttoria(domanda.getNumeroDomanda().toString());
		mockInfoAgricoltoreSIAN(cuaa, this::getResponseAgricoltoreSianAttivo);
		mockDomandaSigeco(domanda.getNumeroDomanda(), this::getResponseSigecoNull);
		service.elabora(istruttoriaPerDomanda189690().getId());

		IstruttoriaModel statoSostegnoDomandaAggiornato = istruttoriaPerDomanda189690();
		entityManager.refresh(statoSostegnoDomandaAggiornato);
		assertNotNull(statoSostegnoDomandaAggiornato);
		assertEquals(FoglieAmmissibilitaAccoppiatoSuperficie.DUF_006_F.getStatoWF().getStatoIstruttoria(), statoSostegnoDomandaAggiornato.getA4gdStatoLavSostegno().getIdentificativo());

		// List<TransizioneIstruttoriaModel> transizioni = transizioneDao.findTransizioniByIstruttoriaModel(statoSostegnoDomandaAggiornato);
		Set<TransizioneIstruttoriaModel> transizioni = statoSostegnoDomandaAggiornato.getTransizioni();
		assertNotNull(transizioni);
		assertTrue(!transizioni.isEmpty());
		Optional<TransizioneIstruttoriaModel> optTrans = transizioni.stream().sorted(Comparator.comparing(TransizioneIstruttoriaModel::getDataEsecuzione).reversed()).findFirst();
		assertTrue(optTrans.isPresent());
		TransizioneIstruttoriaModel transizione = optTrans.get();
		List<PassoTransizioneModel> passi = passiLavorazioneSostegnoDao.findByTransizioneIstruttoria(transizione);
		assertNotNull(passi);
		assertEquals(1, passi.size());
		PassoTransizioneModel passoLavorazione = passi.get(0);
		assertEquals(TipologiaPassoTransizione.CALCOLO_ACS, passoLavorazione.getCodicePasso());
		assertEquals(FoglieAmmissibilitaAccoppiatoSuperficie.DUF_006_F.getCodiceEsito(), passoLavorazione.getCodiceEsito());
		assertEquals("KO", passoLavorazione.getEsito());
	}

	private IstruttoriaModel istruttoriaPerDomanda189690() {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(189690));

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);
		return istruttoria;
	}



	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/acs/duf_005_ok_particellaripetuta.sql", "/DomandaUnica/acs/dataMatriceCompatibilitaOlivo.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "delete from A4GD_COLTURA_INTERVENTO", "commit" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_005_ok_particellaripetuta() throws Exception {
		String cuaa = "SGNNLN54T28A372J";
		Integer campagna = 2018;
		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna(cuaa,campagna);
		mockAnomalieCoordinamento(0);
		mockInfoIstruttoria(domanda.getNumeroDomanda().toString());
		mockInfoAgricoltoreSIAN(cuaa, this::getResponseAgricoltoreSianAttivo);
		mockDomandaSigeco(domanda.getNumeroDomanda(), this::getResponseSigecoEsito5);
		service.elabora(istruttoriaPerDomanda189674().getId());

		IstruttoriaModel statoSostegnoDomandaAggiornato = istruttoriaPerDomanda189674();
		entityManager.refresh(statoSostegnoDomandaAggiornato);
		assertNotNull(statoSostegnoDomandaAggiornato);
		assertEquals(StatoIstruttoria.CONTROLLI_CALCOLO_OK.getStatoIstruttoria(), statoSostegnoDomandaAggiornato.getA4gdStatoLavSostegno().getIdentificativo());

		// List<TransizioneIstruttoriaModel> transizioni = transizioneDao.findTransizioniByIstruttoriaModel(statoSostegnoDomandaAggiornato);
		Set<TransizioneIstruttoriaModel> transizioni = statoSostegnoDomandaAggiornato.getTransizioni();
		assertNotNull(transizioni);
		assertTrue(!transizioni.isEmpty());
		Optional<TransizioneIstruttoriaModel> optTrans = transizioni.stream().sorted(Comparator.comparing(TransizioneIstruttoriaModel::getDataEsecuzione).reversed()).findFirst();
		assertTrue(optTrans.isPresent());
		TransizioneIstruttoriaModel transizione = optTrans.get();
		List<PassoTransizioneModel> passi = passiLavorazioneSostegnoDao.findByTransizioneIstruttoria(transizione);
		assertNotNull(passi);
		assertEquals(1, passi.size());
		PassoTransizioneModel passoLavorazione = passi.get(0);
		assertEquals(TipologiaPassoTransizione.CALCOLO_ACS, passoLavorazione.getCodicePasso());
		assertEquals(FoglieAmmissibilitaAccoppiatoSuperficie.DUF_005_F_OK.getCodiceEsito(), passoLavorazione.getCodiceEsito());
		assertEquals("OK", passoLavorazione.getEsito());
	}

	private IstruttoriaModel istruttoriaPerDomanda189674() {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(189674));

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);
		return istruttoria;
	}


	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/acs/duf_013_f.sql", "/DomandaUnica/acs/dataMatriceCompatibilitaOlivo.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "delete from A4GD_COLTURA_INTERVENTO", "commit" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_013_f() throws Exception {
		String cuaa = "SGNNLN54T28A372W";
		Integer campagna = 2018;
		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna(cuaa,campagna);
		mockAnomalieCoordinamento(0);
		mockInfoIstruttoria(domanda.getNumeroDomanda().toString());
		mockInfoAgricoltoreSIAN(cuaa, this::getResponseAgricoltoreSianNonAttivo);
		mockDomandaSigeco(domanda.getNumeroDomanda(), this::getResponseSigecoEsito5);
		service.elabora(istruttoriaPerDomanda189677().getId());

		IstruttoriaModel statoSostegnoDomandaAggiornato = istruttoriaPerDomanda189677();
		entityManager.refresh(statoSostegnoDomandaAggiornato);
		assertNotNull(statoSostegnoDomandaAggiornato);
		assertEquals(StatoIstruttoria.CONTROLLI_CALCOLO_KO.getStatoIstruttoria(), statoSostegnoDomandaAggiornato.getA4gdStatoLavSostegno().getIdentificativo());

		// List<TransizioneIstruttoriaModel> transizioni = transizioneDao.findTransizioniByIstruttoriaModel(statoSostegnoDomandaAggiornato);
		Set<TransizioneIstruttoriaModel> transizioni = statoSostegnoDomandaAggiornato.getTransizioni();
		assertNotNull(transizioni);
		assertTrue(!transizioni.isEmpty());
		Optional<TransizioneIstruttoriaModel> optTrans = transizioni.stream().sorted(Comparator.comparing(TransizioneIstruttoriaModel::getDataEsecuzione).reversed()).findFirst();
		assertTrue(optTrans.isPresent());
		TransizioneIstruttoriaModel transizione = optTrans.get();
		List<PassoTransizioneModel> passi = passiLavorazioneSostegnoDao.findByTransizioneIstruttoria(transizione);
		assertNotNull(passi);
		assertEquals(1, passi.size());
		PassoTransizioneModel passoLavorazione = passi.get(0);
		assertEquals(TipologiaPassoTransizione.CALCOLO_ACS, passoLavorazione.getCodicePasso());
		assertEquals(FoglieAmmissibilitaAccoppiatoSuperficie.DUF_013_F.getCodiceEsito(), passoLavorazione.getCodiceEsito());
		assertEquals("KO", passoLavorazione.getEsito());
	}

	private IstruttoriaModel istruttoriaPerDomanda189677() {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(189677));

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);
		return istruttoria;
	}



	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/acs/duf_009_f.sql", "/DomandaUnica/acs/dataMatriceCompatibilitaOlivo.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "delete from A4GD_COLTURA_INTERVENTO", "commit" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_009_f() throws Exception {
		String cuaa = "SGNNLN54T28A372X";
		Integer campagna = 2018;
		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna(cuaa,campagna);
		mockAnomalieCoordinamento(0);
		mockInfoIstruttoria(domanda.getNumeroDomanda().toString());
		mockInfoAgricoltoreSIAN(cuaa, this::getResponseAgricoltoreSianNonAttivo);
		mockDomandaSigeco(domanda.getNumeroDomanda(), this::getResponseSigecoEsito5);
		service.elabora(istruttoriaPerDomanda189675().getId());

		IstruttoriaModel statoSostegnoDomandaAggiornato = istruttoriaPerDomanda189675();
		entityManager.refresh(statoSostegnoDomandaAggiornato);
		assertNotNull(statoSostegnoDomandaAggiornato);
		assertEquals(StatoIstruttoria.CONTROLLI_CALCOLO_KO.getStatoIstruttoria(), statoSostegnoDomandaAggiornato.getA4gdStatoLavSostegno().getIdentificativo());

		// List<TransizioneIstruttoriaModel> transizioni = transizioneDao.findTransizioniByIstruttoriaModel(statoSostegnoDomandaAggiornato);
		Set<TransizioneIstruttoriaModel> transizioni = statoSostegnoDomandaAggiornato.getTransizioni();
		assertNotNull(transizioni);
		assertTrue(!transizioni.isEmpty());
		Optional<TransizioneIstruttoriaModel> optTrans = transizioni.stream().sorted(Comparator.comparing(TransizioneIstruttoriaModel::getDataEsecuzione).reversed()).findFirst();
		assertTrue(optTrans.isPresent());
		TransizioneIstruttoriaModel transizione = optTrans.get();
		List<PassoTransizioneModel> passi = passiLavorazioneSostegnoDao.findByTransizioneIstruttoria(transizione);
		assertNotNull(passi);
		assertEquals(1, passi.size());
		PassoTransizioneModel passoLavorazione = passi.get(0);
		assertEquals(TipologiaPassoTransizione.CALCOLO_ACS, passoLavorazione.getCodicePasso());
		assertEquals(FoglieAmmissibilitaAccoppiatoSuperficie.DUF_009_F.getCodiceEsito(), passoLavorazione.getCodiceEsito());
		assertEquals("KO", passoLavorazione.getEsito());
	}

	private IstruttoriaModel istruttoriaPerDomanda189675() {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(189675));

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);
		return istruttoria;
	}


	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/acs/duf_010_f.sql", "/DomandaUnica/acs/dataMatriceCompatibilitaOlivo.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "delete from A4GD_COLTURA_INTERVENTO", "commit" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_010_f() throws Exception {
		String cuaa = "SGNNLN54T28A372Y";
		Integer campagna = 2018;
		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna(cuaa,campagna);
		mockAnomalieCoordinamento(0);
		mockInfoIstruttoria(domanda.getNumeroDomanda().toString());
		mockInfoAgricoltoreSIAN(cuaa, this::getResponseAgricoltoreSianNonAttivo);
		mockDomandaSigeco(domanda.getNumeroDomanda(), this::getResponseSigecoEsito5);
		service.elabora(istruttoriaPerDomanda189676().getId());

		IstruttoriaModel statoSostegnoDomandaAggiornato = istruttoriaPerDomanda189676();
		entityManager.refresh(statoSostegnoDomandaAggiornato);
		assertNotNull(statoSostegnoDomandaAggiornato);
		assertEquals(StatoIstruttoria.CONTROLLI_CALCOLO_KO.getStatoIstruttoria(), statoSostegnoDomandaAggiornato.getA4gdStatoLavSostegno().getIdentificativo());

		// List<TransizioneIstruttoriaModel> transizioni = transizioneDao.findTransizioniByIstruttoriaModel(statoSostegnoDomandaAggiornato);
		Set<TransizioneIstruttoriaModel> transizioni = statoSostegnoDomandaAggiornato.getTransizioni();
		assertNotNull(transizioni);
		assertTrue(!transizioni.isEmpty());
		Optional<TransizioneIstruttoriaModel> optTrans = transizioni.stream().sorted(Comparator.comparing(TransizioneIstruttoriaModel::getDataEsecuzione).reversed()).findFirst();
		assertTrue(optTrans.isPresent());
		TransizioneIstruttoriaModel transizione = optTrans.get();
		List<PassoTransizioneModel> passi = passiLavorazioneSostegnoDao.findByTransizioneIstruttoria(transizione);
		assertNotNull(passi);
		assertEquals(1, passi.size());
		PassoTransizioneModel passoLavorazione = passi.get(0);
		assertEquals(TipologiaPassoTransizione.CALCOLO_ACS, passoLavorazione.getCodicePasso());
		assertEquals(FoglieAmmissibilitaAccoppiatoSuperficie.DUF_010_F.getCodiceEsito(), passoLavorazione.getCodiceEsito());
		assertEquals("KO", passoLavorazione.getEsito());
	}

	private IstruttoriaModel istruttoriaPerDomanda189676() {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(189676));

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);
		return istruttoria;
	}


	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/acs/duf_014_f.sql", "/DomandaUnica/acs/dataMatriceCompatibilitaProteaginose.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "delete from A4GD_COLTURA_INTERVENTO", "commit" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_014_f() throws Exception {
		String cuaa = "01917270223";
		Integer campagna = 2018;
		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna(cuaa,campagna);
		mockAnomalieCoordinamento(0);
		mockInfoIstruttoria(domanda.getNumeroDomanda().toString());
		mockInfoAgricoltoreSIAN(cuaa, this::getResponseAgricoltoreSianNonAttivo);
		mockDomandaSigeco(domanda.getNumeroDomanda(), this::getResponseSigecoEsito5);
		mockControlloRegioni(CodiceInterventoAgs.CPROT, "G135", true);
		service.elabora(istruttoriaPerDomanda190472().getId());

		IstruttoriaModel statoSostegnoDomandaAggiornato = istruttoriaPerDomanda190472();
		entityManager.refresh(statoSostegnoDomandaAggiornato);
		assertNotNull(statoSostegnoDomandaAggiornato);
		assertEquals(StatoIstruttoria.CONTROLLI_CALCOLO_KO.getStatoIstruttoria(), statoSostegnoDomandaAggiornato.getA4gdStatoLavSostegno().getIdentificativo());

		// List<TransizioneIstruttoriaModel> transizioni = transizioneDao.findTransizioniByIstruttoriaModel(statoSostegnoDomandaAggiornato);
		Set<TransizioneIstruttoriaModel> transizioni = statoSostegnoDomandaAggiornato.getTransizioni();
		assertNotNull(transizioni);
		assertTrue(!transizioni.isEmpty());
		Optional<TransizioneIstruttoriaModel> optTrans = transizioni.stream().sorted(Comparator.comparing(TransizioneIstruttoriaModel::getDataEsecuzione).reversed()).findFirst();
		assertTrue(optTrans.isPresent());
		TransizioneIstruttoriaModel transizione = optTrans.get();
		List<PassoTransizioneModel> passi = passiLavorazioneSostegnoDao.findByTransizioneIstruttoria(transizione);
		assertNotNull(passi);
		assertEquals(1, passi.size());
		PassoTransizioneModel passoLavorazione = passi.get(0);
		assertEquals(TipologiaPassoTransizione.CALCOLO_ACS, passoLavorazione.getCodicePasso());
		assertEquals(FoglieAmmissibilitaAccoppiatoSuperficie.DUF_014_F.getCodiceEsito(), passoLavorazione.getCodiceEsito());
		assertEquals("KO", passoLavorazione.getEsito());
	}

	private IstruttoriaModel istruttoriaPerDomanda190472() {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(190472));

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);
		return istruttoria;
	}



	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/acs/duf_017_f.sql", "/DomandaUnica/acs/dataMatriceCompatibilitaFrumentoGranoDuro.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "delete from A4GD_COLTURA_INTERVENTO", "commit" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_017_f() throws Exception {
		String cuaa = "DRNNTN74E13H929R";
		Integer campagna = 2018;
		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna(cuaa,campagna);
		mockAnomalieCoordinamento(0);
		mockInfoIstruttoria(domanda.getNumeroDomanda().toString());
		mockInfoAgricoltoreSIAN(cuaa, this::getResponseAgricoltoreSianNonPresente);
		mockDomandaSigeco(domanda.getNumeroDomanda(), this::getResponseSigecoEsito5);
		mockControlloRegioni(CodiceInterventoAgs.GDURO, "H929", true);
		service.elabora(istruttoriaPerDomanda182814().getId());

		IstruttoriaModel statoSostegnoDomandaAggiornato = istruttoriaPerDomanda182814();
		entityManager.refresh(statoSostegnoDomandaAggiornato);
		assertNotNull(statoSostegnoDomandaAggiornato);
		assertEquals(StatoIstruttoria.CONTROLLI_CALCOLO_KO.getStatoIstruttoria(), statoSostegnoDomandaAggiornato.getA4gdStatoLavSostegno().getIdentificativo());

		// List<TransizioneIstruttoriaModel> transizioni = transizioneDao.findTransizioniByIstruttoriaModel(statoSostegnoDomandaAggiornato);
		Set<TransizioneIstruttoriaModel> transizioni = statoSostegnoDomandaAggiornato.getTransizioni();
		assertNotNull(transizioni);
		assertTrue(!transizioni.isEmpty());
		Optional<TransizioneIstruttoriaModel> optTrans = transizioni.stream().sorted(Comparator.comparing(TransizioneIstruttoriaModel::getDataEsecuzione).reversed()).findFirst();
		assertTrue(optTrans.isPresent());
		TransizioneIstruttoriaModel transizione = optTrans.get();
		List<PassoTransizioneModel> passi = passiLavorazioneSostegnoDao.findByTransizioneIstruttoria(transizione);
		assertNotNull(passi);
		assertEquals(1, passi.size());
		PassoTransizioneModel passoLavorazione = passi.get(0);
		assertEquals(TipologiaPassoTransizione.CALCOLO_ACS, passoLavorazione.getCodicePasso());
		assertEquals(FoglieAmmissibilitaAccoppiatoSuperficie.DUF_017_F.getCodiceEsito(), passoLavorazione.getCodiceEsito());
		assertEquals("KO", passoLavorazione.getEsito());
	}

	private IstruttoriaModel istruttoriaPerDomanda182814() {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(182814));

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);
		return istruttoria;
	}


	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/acs/duf_018_f.sql", "/DomandaUnica/acs/dataMatriceCompatibilitaSoia.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "delete from A4GD_COLTURA_INTERVENTO", "commit" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_018_f() throws Exception {
		String cuaa = "00466140225";
		Integer campagna = 2018;
		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna(cuaa,campagna);
		mockAnomalieCoordinamento(0);
		mockInfoIstruttoria(domanda.getNumeroDomanda().toString());
		mockInfoAgricoltoreSIAN(cuaa, this::getResponseAgricoltoreSianNonPresente);
		mockDomandaSigeco(domanda.getNumeroDomanda(), this::getResponseSigecoEsito5);
		mockControlloRegioni(CodiceInterventoAgs.SOIA, "I827", true);
		service.elabora(istruttoriaPerDomanda187750().getId());

		IstruttoriaModel statoSostegnoDomandaAggiornato = istruttoriaPerDomanda187750();
		entityManager.refresh(statoSostegnoDomandaAggiornato);
		assertNotNull(statoSostegnoDomandaAggiornato);
		assertEquals(StatoIstruttoria.CONTROLLI_CALCOLO_KO.getStatoIstruttoria(), statoSostegnoDomandaAggiornato.getA4gdStatoLavSostegno().getIdentificativo());

		// List<TransizioneIstruttoriaModel> transizioni = transizioneDao.findTransizioniByIstruttoriaModel(statoSostegnoDomandaAggiornato);
		Set<TransizioneIstruttoriaModel> transizioni = statoSostegnoDomandaAggiornato.getTransizioni();
		assertNotNull(transizioni);
		assertTrue(!transizioni.isEmpty());
		Optional<TransizioneIstruttoriaModel> optTrans = transizioni.stream().sorted(Comparator.comparing(TransizioneIstruttoriaModel::getDataEsecuzione).reversed()).findFirst();
		assertTrue(optTrans.isPresent());
		TransizioneIstruttoriaModel transizione = optTrans.get();
		List<PassoTransizioneModel> passi = passiLavorazioneSostegnoDao.findByTransizioneIstruttoria(transizione);
		assertNotNull(passi);
		assertEquals(1, passi.size());
		PassoTransizioneModel passoLavorazione = passi.get(0);
		assertEquals(TipologiaPassoTransizione.CALCOLO_ACS, passoLavorazione.getCodicePasso());
		assertEquals(FoglieAmmissibilitaAccoppiatoSuperficie.DUF_018_F.getCodiceEsito(), passoLavorazione.getCodiceEsito());
		assertEquals("KO", passoLavorazione.getEsito());
	}

	private IstruttoriaModel istruttoriaPerDomanda187750() {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(187750));

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);
		return istruttoria;
	}


	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/acs/duf_multiolio.sql", "/DomandaUnica/acs/dataMatriceCompatibilitaOlivoTutti.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "delete from A4GD_COLTURA_INTERVENTO", "commit" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_001_f_OK_multiolio() throws Exception {
		String cuaa = "GRCNGL64E14B809Z";
		Integer campagna = 2018;
		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna(cuaa,campagna);
		mockAnomalieCoordinamento(0);
		mockInfoIstruttoria(domanda.getNumeroDomanda().toString());
		mockInfoAgricoltoreSIAN(cuaa, this::getResponseAgricoltoreSianAttivo);
		mockDomandaSigeco(domanda.getNumeroDomanda(), this::getResponseSigecoEsito5);
		service.elabora(istruttoriaPerDomanda183448().getId());

		IstruttoriaModel statoSostegnoDomandaAggiornato = istruttoriaPerDomanda183448();
		entityManager.refresh(statoSostegnoDomandaAggiornato);
		assertNotNull(statoSostegnoDomandaAggiornato);
		assertEquals(StatoIstruttoria.CONTROLLI_CALCOLO_OK.getStatoIstruttoria(), statoSostegnoDomandaAggiornato.getA4gdStatoLavSostegno().getIdentificativo());

		// List<TransizioneIstruttoriaModel> transizioni = transizioneDao.findTransizioniByIstruttoriaModel(statoSostegnoDomandaAggiornato);
		Set<TransizioneIstruttoriaModel> transizioni = statoSostegnoDomandaAggiornato.getTransizioni();
		assertNotNull(transizioni);
		assertTrue(!transizioni.isEmpty());
		Optional<TransizioneIstruttoriaModel> optTrans = transizioni.stream().sorted(Comparator.comparing(TransizioneIstruttoriaModel::getDataEsecuzione).reversed()).findFirst();
		assertTrue(optTrans.isPresent());
		TransizioneIstruttoriaModel transizione = optTrans.get();
		List<PassoTransizioneModel> passi = passiLavorazioneSostegnoDao.findByTransizioneIstruttoria(transizione);
		assertNotNull(passi);
		assertEquals(1, passi.size());
		PassoTransizioneModel passoLavorazione = passi.get(0);
		assertEquals(TipologiaPassoTransizione.CALCOLO_ACS, passoLavorazione.getCodicePasso());
		assertEquals(FoglieAmmissibilitaAccoppiatoSuperficie.DUF_001_F_OK.getCodiceEsito(), passoLavorazione.getCodiceEsito());
		assertEquals("OK", passoLavorazione.getEsito());
	}

	private IstruttoriaModel istruttoriaPerDomanda183448() {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(183448));

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);
		return istruttoria;
	}


	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/acs/duf_021_f.sql", "/DomandaUnica/acs/dataMatriceCompatibilitaFrumentoGranoDuro.sql",
			"/DomandaUnica/acs/dataMatriceCompatibilitaLeguminose.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "delete from A4GD_COLTURA_INTERVENTO", "commit" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_021_f() throws Exception {
		String cuaa = "GNGMCR70P59H245W";
		Integer campagna = 2018;
		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna(cuaa,campagna);
		mockAnomalieCoordinamento(0);
		mockInfoIstruttoria(domanda.getNumeroDomanda().toString());
		mockInfoAgricoltoreSIAN(cuaa, this::getResponseAgricoltoreSianNonPresente);
		// mockDomandaSigeco(domanda.getNumeroDomanda(), this::getResponseSigecoEsito5);
		mockControlloRegioni(CodiceInterventoAgs.LEGUMIN, "G511", true);
		service.elabora(istruttoriaPerDomanda190886().getId());

		IstruttoriaModel statoSostegnoDomandaAggiornato = istruttoriaPerDomanda190886();
		entityManager.refresh(statoSostegnoDomandaAggiornato);
		assertNotNull(statoSostegnoDomandaAggiornato);
		assertEquals(StatoIstruttoria.CONTROLLI_CALCOLO_KO.getStatoIstruttoria(), statoSostegnoDomandaAggiornato.getA4gdStatoLavSostegno().getIdentificativo());

		// List<TransizioneIstruttoriaModel> transizioni = transizioneDao.findTransizioniByIstruttoriaModel(statoSostegnoDomandaAggiornato);
		Set<TransizioneIstruttoriaModel> transizioni = statoSostegnoDomandaAggiornato.getTransizioni();
		assertNotNull(transizioni);
		assertTrue(!transizioni.isEmpty());
		Optional<TransizioneIstruttoriaModel> optTrans = transizioni.stream().sorted(Comparator.comparing(TransizioneIstruttoriaModel::getDataEsecuzione).reversed()).findFirst();
		assertTrue(optTrans.isPresent());
		TransizioneIstruttoriaModel transizione = optTrans.get();
		List<PassoTransizioneModel> passi = passiLavorazioneSostegnoDao.findByTransizioneIstruttoria(transizione);
		assertNotNull(passi);
		assertEquals(1, passi.size());
		PassoTransizioneModel passoLavorazione = passi.get(0);
		assertEquals(TipologiaPassoTransizione.CALCOLO_ACS, passoLavorazione.getCodicePasso());
		assertEquals(FoglieAmmissibilitaAccoppiatoSuperficie.DUF_021_F.getCodiceEsito(), passoLavorazione.getCodiceEsito());
		assertEquals("KO", passoLavorazione.getEsito());
	}

	private IstruttoriaModel istruttoriaPerDomanda190886() {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(190886));

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);
		return istruttoria;
	}



	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/acs/duf_022_f.sql", "/DomandaUnica/acs/dataMatriceCompatibilitaPomodoro.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "delete from A4GD_COLTURA_INTERVENTO", "commit" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_022_f() throws Exception {
		String cuaa = "CGHVCN73D15H612G";
		Integer campagna = 2018;
		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna(cuaa,campagna);
		mockAnomalieCoordinamento(0);
		mockInfoIstruttoria(domanda.getNumeroDomanda().toString());
		mockInfoAgricoltoreSIAN(cuaa, this::getResponseAgricoltoreSianNonPresente);
		service.elabora(istruttoriaPerDomanda185294().getId());

		IstruttoriaModel statoSostegnoDomandaAggiornato = istruttoriaPerDomanda185294();
		entityManager.refresh(statoSostegnoDomandaAggiornato);
		assertNotNull(statoSostegnoDomandaAggiornato);
		assertEquals(StatoIstruttoria.CONTROLLI_CALCOLO_KO.getStatoIstruttoria(), statoSostegnoDomandaAggiornato.getA4gdStatoLavSostegno().getIdentificativo());

		// List<TransizioneIstruttoriaModel> transizioni = transizioneDao.findTransizioniByIstruttoriaModel(statoSostegnoDomandaAggiornato);
		Set<TransizioneIstruttoriaModel> transizioni = statoSostegnoDomandaAggiornato.getTransizioni();
		assertNotNull(transizioni);
		assertTrue(!transizioni.isEmpty());
		Optional<TransizioneIstruttoriaModel> optTrans = transizioni.stream().sorted(Comparator.comparing(TransizioneIstruttoriaModel::getDataEsecuzione).reversed()).findFirst();
		assertTrue(optTrans.isPresent());
		TransizioneIstruttoriaModel transizione = optTrans.get();
		List<PassoTransizioneModel> passi = passiLavorazioneSostegnoDao.findByTransizioneIstruttoria(transizione);
		assertNotNull(passi);
		assertEquals(1, passi.size());
		PassoTransizioneModel passoLavorazione = passi.get(0);
		assertEquals(TipologiaPassoTransizione.CALCOLO_ACS, passoLavorazione.getCodicePasso());
		assertEquals(FoglieAmmissibilitaAccoppiatoSuperficie.DUF_022_F.getCodiceEsito(), passoLavorazione.getCodiceEsito());
		assertEquals("KO", passoLavorazione.getEsito());
	}

	private IstruttoriaModel istruttoriaPerDomanda185294() {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(185294));

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);
		return istruttoria;
	}


	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/acs/duf_025_f.sql", "/DomandaUnica/acs/dataMatriceCompatibilitaOlivoTutti.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "delete from A4GD_COLTURA_INTERVENTO", "commit" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_025_f() throws Exception {
		String cuaa = "GRCNGL64E14B809W";
		Integer campagna = 2018;
		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna(cuaa,campagna);
		mockAnomalieCoordinamento(0);
		mockInfoIstruttoria(domanda.getNumeroDomanda().toString());
		mockInfoAgricoltoreSIAN(cuaa, this::getResponseAgricoltoreSianAttivo);
		mockDomandaSigeco(domanda.getNumeroDomanda(), this::getResponseSigecoEsito2);
		service.elabora(istruttoriaPerDomanda183440().getId());

		IstruttoriaModel statoSostegnoDomandaAggiornato = istruttoriaPerDomanda183440();
		entityManager.refresh(statoSostegnoDomandaAggiornato);
		assertNotNull(statoSostegnoDomandaAggiornato);
		assertEquals(StatoIstruttoria.CONTROLLI_CALCOLO_KO.getStatoIstruttoria(), statoSostegnoDomandaAggiornato.getA4gdStatoLavSostegno().getIdentificativo());

		// List<TransizioneIstruttoriaModel> transizioni = transizioneDao.findTransizioniByIstruttoriaModel(statoSostegnoDomandaAggiornato);
		Set<TransizioneIstruttoriaModel> transizioni = statoSostegnoDomandaAggiornato.getTransizioni();
		assertNotNull(transizioni);
		assertTrue(!transizioni.isEmpty());
		Optional<TransizioneIstruttoriaModel> optTrans = transizioni.stream().sorted(Comparator.comparing(TransizioneIstruttoriaModel::getDataEsecuzione).reversed()).findFirst();
		assertTrue(optTrans.isPresent());
		TransizioneIstruttoriaModel transizione = optTrans.get();
		List<PassoTransizioneModel> passi = passiLavorazioneSostegnoDao.findByTransizioneIstruttoria(transizione);
		assertNotNull(passi);
		assertEquals(1, passi.size());
		PassoTransizioneModel passoLavorazione = passi.get(0);
		assertEquals(TipologiaPassoTransizione.CALCOLO_ACS, passoLavorazione.getCodicePasso());
		assertEquals(FoglieAmmissibilitaAccoppiatoSuperficie.DUF_025_F.getCodiceEsito(), passoLavorazione.getCodiceEsito());
		assertEquals("KO", passoLavorazione.getEsito());
	}

	private IstruttoriaModel istruttoriaPerDomanda183440() {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(183440));

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);
		return istruttoria;
	}


	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/acs/duf_026_f.sql", "/DomandaUnica/acs/dataMatriceCompatibilitaOlivoTutti.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "delete from A4GD_COLTURA_INTERVENTO", "commit" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_026_f() throws Exception {
		String cuaa = "GRCNGL64E14B809X";
		Integer campagna = 2018;
		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna(cuaa,campagna);
		mockAnomalieCoordinamento(0);
		mockInfoIstruttoria(domanda.getNumeroDomanda().toString());
		mockInfoAgricoltoreSIAN(cuaa, this::getResponseAgricoltoreSianNonAttivo);
		mockDomandaSigeco(domanda.getNumeroDomanda(), this::getResponseSigecoEsito2);
		service.elabora(istruttoriaPerDomanda183449().getId());

		IstruttoriaModel statoSostegnoDomandaAggiornato = istruttoriaPerDomanda183449();
		entityManager.refresh(statoSostegnoDomandaAggiornato);
		assertNotNull(statoSostegnoDomandaAggiornato);
		assertEquals(StatoIstruttoria.CONTROLLI_CALCOLO_KO.getStatoIstruttoria(), statoSostegnoDomandaAggiornato.getA4gdStatoLavSostegno().getIdentificativo());


		Set<TransizioneIstruttoriaModel> transizioni = statoSostegnoDomandaAggiornato.getTransizioni();
		assertNotNull(transizioni);
		assertTrue(!transizioni.isEmpty());
		Optional<TransizioneIstruttoriaModel> optTrans = transizioni.stream().sorted(Comparator.comparing(TransizioneIstruttoriaModel::getDataEsecuzione).reversed()).findFirst();
		assertTrue(optTrans.isPresent());
		TransizioneIstruttoriaModel transizione = optTrans.get();
		List<PassoTransizioneModel> passi = passiLavorazioneSostegnoDao.findByTransizioneIstruttoria(transizione);
		assertNotNull(passi);
		assertEquals(1, passi.size());
		PassoTransizioneModel passoLavorazione = passi.get(0);
		assertEquals(TipologiaPassoTransizione.CALCOLO_ACS, passoLavorazione.getCodicePasso());
		assertEquals(FoglieAmmissibilitaAccoppiatoSuperficie.DUF_026_F.getCodiceEsito(), passoLavorazione.getCodiceEsito());
		assertEquals("KO", passoLavorazione.getEsito());
	}

	private IstruttoriaModel istruttoriaPerDomanda183449() {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(183449));

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);
		return istruttoria;
	}


	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/acs/duf_027_f.sql", "/DomandaUnica/acs/dataMatriceCompatibilitaOlivoTutti.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = { "delete from A4GD_COLTURA_INTERVENTO", "commit" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_027_f() throws Exception {
		String cuaa = "GRCNGL64E14B809Y";
		Integer campagna = 2018;
		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna(cuaa,campagna);
		mockAnomalieCoordinamento(0);
		mockInfoIstruttoria(domanda.getNumeroDomanda().toString());
		mockInfoAgricoltoreSIAN(cuaa, this::getResponseAgricoltoreSianNonPresente);
		mockDomandaSigeco(domanda.getNumeroDomanda(), this::getResponseSigecoEsito2);
		service.elabora(istruttoriaPerDomanda183447().getId());

		IstruttoriaModel statoSostegnoDomandaAggiornato = istruttoriaPerDomanda183447();
		entityManager.refresh(statoSostegnoDomandaAggiornato);
		assertNotNull(statoSostegnoDomandaAggiornato);
		assertEquals(StatoIstruttoria.CONTROLLI_CALCOLO_KO.getStatoIstruttoria(), statoSostegnoDomandaAggiornato.getA4gdStatoLavSostegno().getIdentificativo());

		Set<TransizioneIstruttoriaModel> transizioni = statoSostegnoDomandaAggiornato.getTransizioni();
		assertNotNull(transizioni);
		assertTrue(!transizioni.isEmpty());
		Optional<TransizioneIstruttoriaModel> optTrans = transizioni.stream().sorted(Comparator.comparing(TransizioneIstruttoriaModel::getDataEsecuzione).reversed()).findFirst();
		assertTrue(optTrans.isPresent());
		TransizioneIstruttoriaModel transizione = optTrans.get();
		List<PassoTransizioneModel> passi = passiLavorazioneSostegnoDao.findByTransizioneIstruttoria(transizione);
		assertNotNull(passi);
		assertEquals(1, passi.size());
		PassoTransizioneModel passoLavorazione = passi.get(0);
		assertEquals(TipologiaPassoTransizione.CALCOLO_ACS, passoLavorazione.getCodicePasso());
		assertEquals(FoglieAmmissibilitaAccoppiatoSuperficie.DUF_027_F.getCodiceEsito(), passoLavorazione.getCodiceEsito());
		assertEquals("KO", passoLavorazione.getEsito());
	}

	private IstruttoriaModel istruttoriaPerDomanda183447() {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(183447));

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);
		return istruttoria;
	}


	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/acs/CalcoloAccoppiatoSuperficieService.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/acs/CalcoloAccoppiatoSuperficieServiceDelete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void ifStatoIstruttoriaIsNotRichiestoAndCONTROLLI_CALCOLO_OKAndCONTROLLI_CALCOLO_KOThenNotChangeState() throws Exception {
		service.elabora(istruttoriaPerDomanda983447().getId());

		IstruttoriaModel statoSostegnoDomandaAggiornato = istruttoriaPerDomanda983447();
		entityManager.refresh(statoSostegnoDomandaAggiornato);
		Assertions.assertThat(statoSostegnoDomandaAggiornato.getA4gdStatoLavSostegno().getIdentificativo())
				.isEqualTo(StatoIstruttoria.INTEGRATO.getStatoIstruttoria());
	}

	private IstruttoriaModel istruttoriaPerDomanda983447() {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(983447));

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);
		return istruttoria;
	}


	protected void mockAnomalieCoordinamento(Integer numeroAnomalie) {
		Mockito.when(
				restTemplate.getForObject(Mockito.eq(anomalieCoordinamentoUri.concat("/{idParcella}?annoCampagna={annoCampagna}")), Mockito.eq(Integer.class), Mockito.anyLong(), Mockito.anyLong()))
				.thenReturn(numeroAnomalie);
	}

	protected void mockInfoIstruttoria(String numeroDomanda) throws Exception {
		String serviceUrlAgsEleggibili = "http://localhost:8080/ags/api/v1/domandeDU/" + numeroDomanda + "?expand=infoIstruttoria";
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlAgsEleggibili)), Mockito.eq(DomandaUnica.class))).thenReturn(getInfoIstruttoria(numeroDomanda));
	}

	protected void mockInfoAgricoltoreSIAN(String codiceFiscale, Function<String, String> getResponseFunction) throws Exception {
		String jsonRequest = "{ \"codFisc\":\"" + codiceFiscale + "\", \"annoCamp\":2018}";
		String serviceUrlA4GPROXYrecuperoAgricoltoreSian = agricoltoreAttivoUrl + "?params=".concat(URLEncoder.encode(jsonRequest, "UTF-8"));
		AgricoltoreSIAN responseAgricoltoreSian = objectMapper.readValue(getResponseFunction.apply(codiceFiscale), AgricoltoreSIAN.class);
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlA4GPROXYrecuperoAgricoltoreSian)), Mockito.eq(AgricoltoreSIAN.class))).thenReturn(responseAgricoltoreSian);
	}

	protected String getResponseAgricoltoreSianNonPresente(String s) {
		return getResponseAgricoltoreSian("infoAgricoltoreAttivoNonPresente");
	}

	protected String getResponseAgricoltoreSianAttivo(String s) {
		return getResponseAgricoltoreSian("agricoltoreAttivo");
	}

	protected String getResponseAgricoltoreSianNonAttivo(String s) {
		return getResponseAgricoltoreSian("agricoltoreNonAttivo");
	}

	protected String getResponseAgricoltoreSian(String s) {
		try {
			JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/" + s + ".json"));
			return objectMapper.writeValueAsString(response);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected void mockControlloRegioni(CodiceInterventoAgs codiceInterventoAgs, String codNazionale, Boolean esitoDesiderato) {
		DatiCatastaliRegione result = new DatiCatastaliRegione();
		if (esitoDesiderato) {
			switch (codiceInterventoAgs) {
			case SOIA:
				result.setCodiceIstat(regioniIntervento122[0]);
				break;
			case GDURO:
				result.setCodiceIstat(regioniIntervento123[0]);
				break;
			case CPROT:
				result.setCodiceIstat(regioniIntervento124[0]);
				break;
			case LEGUMIN:
				result.setCodiceIstat(regioniIntervento125[0]);
				break;
			default:
				break;
			}
		}
		Mockito.when(restTemplate.getForObject(Mockito.eq(agsUri.concat("daticatastali/sezioni/{codNazionale}/regione")), Mockito.eq(DatiCatastaliRegione.class), Mockito.eq(codNazionale)))
				.thenReturn(result);
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

	protected Long getResponseSigeco(Long s) {
		return s;
	}

	protected DomandaUnica getInfoIstruttoria(String numeroDomanda) throws IOException {
		return objectMapper.readValue(new File("src/test/resources/DomandaUnica/" + numeroDomanda + "_AgsExpandInfoIstruttoria.json"), DomandaUnica.class);
	}
}
