package it.tndigitale.a4gistruttoria.api;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.h2.tools.Server;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import it.tndigitale.a4gistruttoria.dto.configurazioneistruttoria.ConfIstruttoriaRicevibilitaDto;
import it.tndigitale.a4gistruttoria.dto.configurazioneistruttoria.ConfIstruttorieDto;
import it.tndigitale.a4gistruttoria.dto.lavorazione.Cuaa;
import it.tndigitale.a4gistruttoria.repository.dao.CampioneDao;
import it.tndigitale.a4gistruttoria.repository.dao.NutsDao;
import it.tndigitale.a4gistruttoria.repository.dao.StatisticheDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gtStatisticaDu;
import it.tndigitale.a4gistruttoria.repository.model.AmbitoCampione;
import it.tndigitale.a4gistruttoria.repository.model.CampioneModel;
import it.tndigitale.a4gistruttoria.repository.model.CampioneStatistico;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.service.businesslogic.configurazioneistruttoria.ConfigurazioneIstruttoriaService;
import it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato.DisaccoppiatoService;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElaborazioneDomandaException;
import it.tndigitale.a4gistruttoria.service.businesslogic.statistica.GeneraStatisticaBase;
import it.tndigitale.a4gistruttoria.service.businesslogic.statistica.GeneraStatisticaCs21;
import it.tndigitale.a4gistruttoria.service.businesslogic.statistica.GeneraStatisticaCs22;
import it.tndigitale.a4gistruttoria.service.businesslogic.statistica.GeneraStatisticaCs25;
import it.tndigitale.a4gistruttoria.service.businesslogic.statistica.GeneraStatisticaCs27Superficie;
import it.tndigitale.a4gistruttoria.service.businesslogic.statistica.GeneraStatisticaCs27Zootecnia;
import it.tndigitale.a4gistruttoria.util.TipologiaStatistica;



@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GeneraStatisticheTest {

	@Autowired
	private GeneraStatisticaCs21 generaStatisticaCs21;
	
	@Autowired
	private GeneraStatisticaCs22 generaStatisticaCs22;
	
	@Autowired
	private GeneraStatisticaCs25 generaStatisticaCs25;
	
	@Autowired
	private GeneraStatisticaCs27Zootecnia generaStatisticaCs27Zootecnia;
	
	@Autowired
	private GeneraStatisticaCs27Superficie generaStatisticaCs27Superficie;
	
	@Autowired
	private StatisticheDao daoStatistiche;
	
	@MockBean
	private RestTemplate restTemplate;
	
	@MockBean
	private NutsDao nutsDao;
	
	@MockBean
	private CampioneDao campioneDao;
	
	@MockBean
	private DisaccoppiatoService disaccoppiatoService;
	
	@MockBean
	private ConfigurazioneIstruttoriaService configurazioneIstruttoriaService;
	
	@Before
	public void setUp() {

		ConfIstruttoriaRicevibilitaDto conf=new ConfIstruttoriaRicevibilitaDto();
		conf.setDataRicevibilita(LocalDate.of(2018, Month.JUNE, 15));
		conf.setDataScadenzaDomandaInizialeInRitardo(LocalDate.of(2018, Month.JULY, 10));
		Mockito.when(configurazioneIstruttoriaService.getConfIstruttoriaRicevibilita(Mockito.any()))
			.thenReturn(conf);
		
		
		ConfIstruttorieDto confIstruttoria=new ConfIstruttorieDto();
		confIstruttoria.setDtScadenzaDomandeIniziali(LocalDate.of(2018, Month.JUNE, 15));
		Mockito.when(configurazioneIstruttoriaService.getConfIstruttorie(Mockito.any()))
		.thenReturn(confIstruttoria);
	
	}
	
	
	@Test
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_190837_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_190837_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs21Domanda190837() throws ElaborazioneDomandaException {
		mock("190837");
		simulaProcesso(generaStatisticaCs21);
		
		A4gtStatisticaDu statEx = new A4gtStatisticaDu();
		statEx.setIdDomanda(190837L);
		Optional<A4gtStatisticaDu> statOpt = daoStatistiche.findOne(Example.of(statEx));
		if (statOpt.isPresent()) {
			A4gtStatisticaDu stat = statOpt.get();
			assertEquals(Integer.valueOf(2017), stat.getC110());
			assertEquals("00386500227", stat.getF200());
			assertTrue(stat.getF300().endsWith("190837"));
			assertEquals("1", stat.getC300a());
			assertEquals(Date.valueOf("2018-06-14") , stat.getF300b());
			assertEquals("2", stat.getC400());
			assertEquals(379.94f, stat.getC551(), 0.00f);
			assertEquals(377.69f, stat.getC552(), 0.00f);
			assertEquals(144874.33f, stat.getC554(), 0.00f);
			assertEquals(281.18f, stat.getC558(), 0.00f);
			assertEquals(107854.07f, stat.getC559(), 0.00f);
			assertEquals(96.51f, stat.getC560(), 0.00f);
			assertEquals(37020.26f, stat.getC561(), 0.00f);
			assertEquals("N", stat.getC600());
			assertEquals("4", stat.getC620());
			assertEquals(-55530.40f, stat.getC640(), 0.00f);
		} else {
			fail("Statistiche per numero domanda 190837 non trovate");
		}
	}

	private void simulaProcesso(GeneraStatisticaBase generaStatistica) throws ElaborazioneDomandaException {
		generaStatistica
			.caricaIdDaElaborare(2017)
			.forEach(idIstruttoria -> {
					try {
						generaStatistica.elabora(idIstruttoria);
					} catch (ElaborazioneDomandaException ex) {
						throw new RuntimeException(ex);
					}
				});
	}
	
	private void simulaProcesso2019(GeneraStatisticaBase generaStatistica) throws ElaborazioneDomandaException {
		generaStatistica
			.caricaIdDaElaborare(2019)
			.forEach(idIstruttoria -> {
					try {
						generaStatistica.elabora(idIstruttoria);
					} catch (ElaborazioneDomandaException ex) {
						throw new RuntimeException(ex);
					}
				});
	}
	
	@Test
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_190837_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_190837_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs22Domanda190837() throws ElaborazioneDomandaException {
		mock("190837");
		simulaProcesso(generaStatisticaCs22);
		
		A4gtStatisticaDu statEx = new A4gtStatisticaDu();
		statEx.setIdDomanda(190837L);
		Optional<A4gtStatisticaDu> statOpt = daoStatistiche.findOne(Example.of(statEx));
		if (statOpt.isPresent()) {
			A4gtStatisticaDu stat = statOpt.get();
			assertEquals(Integer.valueOf(2017), stat.getC110());
			assertEquals("00386500227", stat.getF200());
			assertTrue(stat.getF300().endsWith("190837"));
			assertEquals("1", stat.getC300a());
			assertEquals(Date.valueOf("2018-06-14") , stat.getF300b());
			assertEquals("1", stat.getC400());
			assertEquals(377.69f, stat.getC552(), 0.00f);
			assertEquals(75218.75f, stat.getC554(), 0.00f);
			assertEquals(281.18f, stat.getC558(), 0.00f);
			assertEquals(8.58f, stat.getC558f(), 0.00f);
			assertEquals(55997.83f, stat.getC559(), 0.00f);
			assertEquals(96.51f, stat.getC560(), 0.00f);
			assertEquals(19220.92f, stat.getC561(), 0.00f);
			assertEquals("N", stat.getC600());
			assertEquals("4", stat.getC620());
			assertEquals(0.00f, stat.getC640(), 0.00f);
		} else {
			fail("Statistiche per numero domanda 190837 non trovate");
		}
	}
	
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_190837_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_190837_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs27Domanda190837M04() throws ElaborazioneDomandaException {
		mock("190837_27");
		simulaProcesso(generaStatisticaCs27Zootecnia);
		
		A4gtStatisticaDu statEx = new A4gtStatisticaDu();
		statEx.setIdDomanda(190837L);
		statEx.setC109a("M04");
		Optional<A4gtStatisticaDu> statOpt1 = daoStatistiche.findOne(Example.of(statEx));
		if (statOpt1.isPresent()) {
			A4gtStatisticaDu stat = statOpt1.get();
			assertEquals("M04", stat.getC109a());
			assertEquals(Integer.valueOf(2017), stat.getC110());
			assertEquals("00386500227", stat.getF200());
			assertTrue(stat.getF300().endsWith("190837"));
			assertEquals("1", stat.getC300a());
			assertEquals(Date.valueOf("2018-06-14") , stat.getF300b());
			assertEquals(2.00f, stat.getC551(), 0.00f);
			assertEquals(2.00f, stat.getC552(), 0.00f);
			assertEquals(249.32f, stat.getC554(), 0.00f);
			assertEquals(2.00f, stat.getC558(), 0.00f);
			assertEquals(249.32f, stat.getC559(), 0.00f);
			assertEquals(0.00f, stat.getC560(), 0.00f);
			assertEquals(0.00f, stat.getC561(), 0.00f);
			assertEquals("N", stat.getC600());
			assertEquals("4", stat.getC620());
			assertEquals(0.00f, stat.getC640(), 0.00f);
		} else {
			fail("Statistiche per numero domanda 190837 non trovate");
		}
	}
	
	@Test
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_190837_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_190837_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs27Domanda190837M19() throws ElaborazioneDomandaException {
		mock("190837_27");
		simulaProcesso(generaStatisticaCs27Zootecnia);
		
		A4gtStatisticaDu statEx = new A4gtStatisticaDu();
		statEx.setIdDomanda(190837L);
		statEx.setC109a("M19");
		Optional<A4gtStatisticaDu> statOpt1 = daoStatistiche.findOne(Example.of(statEx));
		if (statOpt1.isPresent()) {
			A4gtStatisticaDu stat = statOpt1.get();
			assertEquals("M19", stat.getC109a());
			assertEquals(Integer.valueOf(2017), stat.getC110());
			assertEquals("00386500227", stat.getF200());
			assertTrue(stat.getF300().endsWith("190837"));
			assertEquals("1", stat.getC300a());
			assertEquals(Date.valueOf("2018-06-14") , stat.getF300b());
			assertEquals(583.00f, stat.getC551(), 0.00f);
			assertEquals(583.00f, stat.getC552(), 0.00f);
			assertEquals(35073.28f, stat.getC554(), 0.00f);
			assertEquals(583.00f, stat.getC558(), 0.00f);
			assertEquals(35073.28f, stat.getC559(), 0.00f);
			assertEquals(0.00f, stat.getC560(), 0.00f);
			assertEquals(0.00f, stat.getC561(), 0.00f);
			assertEquals("N", stat.getC600()); 
			assertEquals("4", stat.getC620());
			assertEquals(0.00f, stat.getC640(), 0.00f);
		} else {
			fail("Statistiche per numero domanda 190837 non trovate");
		}
	}
	
	@Test
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_183546_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_183546_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs21Domanda183546() throws ElaborazioneDomandaException {
		mock("183546");
		simulaProcesso(generaStatisticaCs21);
		
		A4gtStatisticaDu statEx = new A4gtStatisticaDu();
		statEx.setIdDomanda(183546L);
		Optional<A4gtStatisticaDu> statOpt = daoStatistiche.findOne(Example.of(statEx));
		if (statOpt.isPresent()) {
			A4gtStatisticaDu stat = statOpt.get();
			assertEquals(Integer.valueOf(2017), stat.getC110());
			assertEquals("MZZDVD76H05H612H", stat.getF200());
			assertTrue(stat.getF300().endsWith("183546"));
			assertEquals("1", stat.getC300a());
			assertEquals(Date.valueOf("2018-04-27") , stat.getF300b());
			assertEquals("2", stat.getC400());
			assertEquals(2.63f, stat.getC551(), 0.00f);
			assertEquals(2.63f, stat.getC552(), 0.00f);
			assertEquals(557.03f, stat.getC554(), 0.00f);
//			assertEquals(2.23f, stat.getC557(), 0.00f);
			assertEquals(2.23f, stat.getC558(), 0.00f);
			assertEquals(473.68f, stat.getC559(), 0.00f);
			assertEquals(0.39f, stat.getC560(), 0.00f);
			assertEquals(83.35f, stat.getC561(), 0.00f);
			assertEquals("FT", stat.getC600());
			assertEquals("2", stat.getC611());
			assertEquals("1", stat.getC621());
			assertEquals("4", stat.getC620());
			assertEquals(-125.02f, stat.getC640(), 0.00f);
		} else {
			fail("Statistiche per numero domanda 183546 non trovate");
		}
	}
	
	@Test
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_183546_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_183546_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs22Domanda183546() throws ElaborazioneDomandaException {
		mock("183546");
		simulaProcesso(generaStatisticaCs22);
		
		A4gtStatisticaDu statEx = new A4gtStatisticaDu();
		statEx.setIdDomanda(183546L);
		Optional<A4gtStatisticaDu> statOpt = daoStatistiche.findOne(Example.of(statEx));
		if (statOpt.isPresent()) {
			A4gtStatisticaDu stat = statOpt.get();
			assertEquals(Integer.valueOf(2017), stat.getC110());
			assertEquals("MZZDVD76H05H612H", stat.getF200());
			assertTrue(stat.getF300().endsWith("183546"));
			assertEquals("1", stat.getC300a());
			assertEquals(Date.valueOf("2018-04-27") , stat.getF300b());
			assertEquals("1", stat.getC400());
			assertEquals(2.63f, stat.getC552(), 0.00f);
			assertEquals(289.21f, stat.getC554(), 0.00f);
			assertEquals(2.23f, stat.getC558(), 0.00f);
			assertEquals(0.00f, stat.getC558f(), 0.00f);
			assertEquals(245.94f, stat.getC559(), 0.00f);
			assertEquals(0.39f, stat.getC560(), 0.00f);
			assertEquals(43.27f, stat.getC561(), 0.00f);
			assertEquals("FT", stat.getC600());
			assertEquals("2", stat.getC611());
			assertEquals("1", stat.getC621());
			assertEquals("4", stat.getC620());
			assertEquals(0.00f, stat.getC640(), 0.00f);
		} else {
			fail("Statistiche per numero domanda 183546 non trovate");
		}
	}
	
	@Test
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_185977_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_185977_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs21Domanda185977() throws ElaborazioneDomandaException {
		mock("185977");		
		simulaProcesso(generaStatisticaCs21);
		
		A4gtStatisticaDu statEx = new A4gtStatisticaDu();
		statEx.setIdDomanda(185977L);
		Optional<A4gtStatisticaDu> statOpt = daoStatistiche.findOne(Example.of(statEx));
		if (statOpt.isPresent()) {
			A4gtStatisticaDu stat = statOpt.get();
			assertEquals(Integer.valueOf(2017), stat.getC110());
			assertEquals("LNRSDR51S08C794T", stat.getF200());
			assertTrue(stat.getF300().endsWith("185977"));
			assertEquals("1", stat.getC300a());
			assertEquals(Date.valueOf("2018-05-11") , stat.getF300b());
			assertEquals("2", stat.getC400());
			assertEquals(4.67f, stat.getC551(), 0.00f);
			assertEquals(4.67f, stat.getC552(), 0.00f);
			assertEquals(453.50f, stat.getC554(), 0.00f);
//			assertEquals(4.70f, stat.getC557(), 0.00f);
			assertEquals(4.67f, stat.getC558(), 0.00f);
//			assertEquals(453.50f, stat.getC559(), 0.00f);
			assertEquals(0.00f, stat.getC560(), 0.00f);
//			assertEquals(0.00f, stat.getC561(), 0.00f);
			assertEquals("T", stat.getC600());
			assertEquals("2", stat.getC611());
//			assertEquals("4", stat.getC620());
//			assertEquals("N", stat.getC621());
			assertEquals(0.00f, stat.getC640(), 0.00f);
		} else {
			fail("Statistiche per numero domanda 185977 non trovate");
		}
	}
	
	@Test
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_185977_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_185977_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs22Domanda185977() throws ElaborazioneDomandaException {
		mock("185977");
		simulaProcesso(generaStatisticaCs22);
		
		A4gtStatisticaDu statEx = new A4gtStatisticaDu();
		statEx.setIdDomanda(185977L);
		Optional<A4gtStatisticaDu> statOpt = daoStatistiche.findOne(Example.of(statEx));
		if (statOpt.isPresent()) {
			A4gtStatisticaDu stat = statOpt.get();
			assertEquals(Integer.valueOf(2017), stat.getC110());
			assertEquals("LNRSDR51S08C794T", stat.getF200());
			assertTrue(stat.getF300().endsWith("185977"));
			assertEquals("1", stat.getC300a());
			assertEquals(Date.valueOf("2018-05-11") , stat.getF300b());
			assertEquals("1", stat.getC400());
			assertEquals(4.67f, stat.getC552(), 0.00f);
			assertEquals(235.46f, stat.getC554(), 0.00f);
			assertEquals(4.67f, stat.getC558(), 0.00f);
			assertEquals(0.00f, stat.getC558f(), 0.00f);
			assertEquals(235.46f, stat.getC559(), 0.00f);
			assertEquals(0.00f, stat.getC560(), 0.00f);
			assertEquals(0.00f, stat.getC561(), 0.00f);
			assertEquals("T", stat.getC600());
			assertEquals("2", stat.getC611());
			assertEquals("4", stat.getC620());
			assertEquals("3", stat.getC621());
			assertEquals(0.00f, stat.getC640(), 0.00f);
		} else {
			fail("Statistiche per numero domanda 185977 non trovate");
		}
	}
	
	@Test
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_183606_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_183606_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs21Domanda183606() throws ElaborazioneDomandaException {
		mock("183606");
		simulaProcesso(generaStatisticaCs21);
		
		A4gtStatisticaDu statEx = new A4gtStatisticaDu();
		statEx.setIdDomanda(183606L);
		Optional<A4gtStatisticaDu> statOpt = daoStatistiche.findOne(Example.of(statEx));
		if (statOpt.isPresent()) {
			A4gtStatisticaDu stat = statOpt.get();
			assertEquals(Integer.valueOf(2017), stat.getC110());
			assertEquals("01449060225", stat.getF200());
			assertTrue(stat.getF300().endsWith("183606"));
			assertEquals("1", stat.getC300a());
			assertEquals(Date.valueOf("2018-04-27") , stat.getF300b());
			assertEquals("1", stat.getC400());
			assertEquals(48.45f, stat.getC551(), 0.00f);
			assertEquals(39.25f, stat.getC552(), 0.00f);
			assertEquals(6786.72f, stat.getC554(), 0.00f);
			assertEquals(39.25f, stat.getC558(), 0.00f);
//			assertEquals(6786.72f, stat.getC559(), 0.00f);
			assertEquals(0.00f, stat.getC560(), 0.00f);
//			assertEquals(0.00f, stat.getC561(), 0.00f);
			assertEquals("N", stat.getC600());
//			assertEquals("4", stat.getC620());
			assertEquals(0.00f, stat.getC640(), 0.00f);
		} else {
			fail("Statistiche per numero domanda 183606 non trovate");
		}
	}
	
	@Test
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_183606_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_183606_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs22Domanda183606() throws ElaborazioneDomandaException {
		mock("183606");
		simulaProcesso(generaStatisticaCs22);
		
		A4gtStatisticaDu statEx = new A4gtStatisticaDu();
		statEx.setIdDomanda(183606L);
		Optional<A4gtStatisticaDu> statOpt = daoStatistiche.findOne(Example.of(statEx));
		if (statOpt.isPresent()) {
			A4gtStatisticaDu stat = statOpt.get();
			assertEquals(Integer.valueOf(2017), stat.getC110());
			assertEquals("01449060225", stat.getF200());
			assertTrue(stat.getF300().endsWith("183606"));
			assertEquals("1", stat.getC300a());
			assertEquals(Date.valueOf("2018-04-27") , stat.getF300b());
			assertEquals("1", stat.getC400());
			assertEquals("2", stat.getC405());
			assertEquals(0.82f, stat.getC406(), 0.00f);
			assertEquals(39.25f, stat.getC552(), 0.00f);
			assertEquals(3523.66f, stat.getC554(), 0.00f);
			assertEquals(39.25f, stat.getC558(), 0.00f);
			assertEquals(7.31f, stat.getC558a(), 0.00f);
			assertEquals(9.17f, stat.getC558b(), 0.00f);
			assertEquals(0.00f, stat.getC558c(), 0.00f);
			assertEquals(31.36f, stat.getC558d(), 0.00f);
			//assertEquals(1.19f, stat.getC558e(), 0.00f); TODO: non ancora implementato in attesa di specifiche
			assertEquals(16.48f, stat.getC558f(), 0.00f);
			assertEquals(2784.11f, stat.getC559(), 0.00f);
			assertEquals(8.24f, stat.getC560(), 0.00f);
			assertEquals(0.82f, stat.getC560a(), 0.00f);
			assertEquals(739.55f, stat.getC561(), 0.00f);
			assertEquals("N", stat.getC600());
			assertEquals("4", stat.getC620());
			assertEquals(null, stat.getC621());
			assertEquals(-2784.11f, stat.getC640(), 0.00f);
		} else {
			fail("Statistiche per numero domanda 183606 non trovate");
		}
	}
	
	@Test
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_183606_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_183606_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs27Domanda183606M01() throws ElaborazioneDomandaException {
		mock("183606_27");
		simulaProcesso(generaStatisticaCs27Zootecnia);
		
		A4gtStatisticaDu statEx = new A4gtStatisticaDu();
		statEx.setIdDomanda(183606L);
		statEx.setC109a("M01");
		Optional<A4gtStatisticaDu> statOpt = daoStatistiche.findOne(Example.of(statEx));
		if (statOpt.isPresent()) {
			A4gtStatisticaDu stat = statOpt.get();
			assertEquals(Integer.valueOf(2017), stat.getC110());
			assertEquals("01449060225", stat.getF200());
			assertTrue(stat.getF300().endsWith("183606"));
			assertEquals("1", stat.getC300a());
			assertEquals(Date.valueOf("2018-04-27") , stat.getF300b());
			assertEquals(47.00f, stat.getC551(), 0.00f);
			assertEquals(47.00f, stat.getC552(), 0.00f);
			assertEquals(3671.17f, stat.getC554(), 0.00f);
			assertEquals(47.00f, stat.getC558(), 0.00f);
			assertEquals(3671.17f, stat.getC559(), 0.00f);
			assertEquals(0.00f, stat.getC560(), 0.00f);
			assertEquals(0.00f, stat.getC561(), 0.00f);
			assertEquals("N", stat.getC600());
			assertEquals("4", stat.getC620());
			assertEquals(0.00f, stat.getC640(), 0.00f);
		} else {
			fail("Statistiche per numero domanda 183606 non trovate");
		}
	}
	
	@Test
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_183606_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_183606_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs27Domanda183606M02() throws ElaborazioneDomandaException {
		mock("183606_27");
		simulaProcesso(generaStatisticaCs27Zootecnia);
		
		A4gtStatisticaDu statEx = new A4gtStatisticaDu();
		statEx.setIdDomanda(183606L);
		statEx.setC109a("M02");
		Optional<A4gtStatisticaDu> statOpt = daoStatistiche.findOne(Example.of(statEx));
		if (statOpt.isPresent()) {
			A4gtStatisticaDu stat = statOpt.get();
			assertEquals(Integer.valueOf(2017), stat.getC110());
			assertEquals("01449060225", stat.getF200());
			assertTrue(stat.getF300().endsWith("183606"));
			assertEquals("1", stat.getC300a());
			assertEquals(Date.valueOf("2018-04-27") , stat.getF300b());
			assertEquals(47.00f, stat.getC551(), 0.00f);
			assertEquals(47.00f, stat.getC552(), 0.00f);
			assertEquals(3237.36f, stat.getC554(), 0.00f);
			assertEquals(47.00f, stat.getC558(), 0.00f);
			assertEquals(3237.36f, stat.getC559(), 0.00f);
			assertEquals(0.00f, stat.getC560(), 0.00f);
			assertEquals(0.00f, stat.getC561(), 0.00f);
			assertEquals("N", stat.getC600());
			assertEquals("4", stat.getC620());
			assertEquals(0.00f, stat.getC640(), 0.00f);
		} else {
			fail("Statistiche per numero domanda 183606 non trovate");
		}
	}
	
	@Test
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_182206_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_182206_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs21Domanda182206() throws ElaborazioneDomandaException {
		mock("182206");
		simulaProcesso(generaStatisticaCs21);
		
		A4gtStatisticaDu statEx = new A4gtStatisticaDu();
		statEx.setIdDomanda(182206L);
		Optional<A4gtStatisticaDu> statOpt = daoStatistiche.findOne(Example.of(statEx));
		if (statOpt.isPresent()) {
			A4gtStatisticaDu stat = statOpt.get();
			assertEquals(Integer.valueOf(2017), stat.getC110());
			assertEquals("CTTRNZ30T14E565L", stat.getF200());
			assertTrue(stat.getF300().endsWith("182206"));
			assertEquals("1", stat.getC300a());
			assertEquals(Date.valueOf("2018-04-20") , stat.getF300b());
			assertEquals("2", stat.getC400());
			assertEquals(1.73f, stat.getC551(), 0.00f);
			assertEquals(1.71f, stat.getC552(), 0.00f);
			assertEquals(189.49f, stat.getC554(), 0.00f);
			assertEquals(1.59f, stat.getC558(), 0.00f);
			assertEquals(176.03f, stat.getC559(), 0.00f);
			assertEquals(0.12f, stat.getC560(), 0.00f);
			assertEquals(13.46f, stat.getC561(), 0.00f);
			assertEquals("N", stat.getC600());
			assertEquals("4", stat.getC620());
			assertEquals(-10.09f, stat.getC640(), 0.00f);
		} else {
			fail("Statistiche per numero domanda 182206 non trovate");
		}
	}
	
	@Test
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_182206_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_182206_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs22Domanda182206() throws ElaborazioneDomandaException {
		mock("182206");
		simulaProcesso(generaStatisticaCs22);
		
		A4gtStatisticaDu statEx = new A4gtStatisticaDu();
		statEx.setIdDomanda(182206L);
		Optional<A4gtStatisticaDu> statOpt = daoStatistiche.findOne(Example.of(statEx));
		if (statOpt.isPresent()) {
			A4gtStatisticaDu stat = statOpt.get();
			assertEquals(Integer.valueOf(2017), stat.getC110());
			assertEquals("CTTRNZ30T14E565L", stat.getF200());
			assertTrue(stat.getF300().endsWith("182206"));
			assertEquals("1", stat.getC300a());
			assertEquals(Date.valueOf("2018-04-20") , stat.getF300b());
			assertEquals("1", stat.getC400());
			assertEquals(1.71f, stat.getC552(), 0.00f);
			assertEquals(98.38f, stat.getC554(), 0.00f);
			assertEquals(1.59f, stat.getC558(), 0.00f);
			assertEquals(0.04f, stat.getC558f(), 0.00f);
			assertEquals(91.40f, stat.getC559(), 0.00f);
			assertEquals(0.12f, stat.getC560(), 0.00f);
			assertEquals(6.98f, stat.getC561(), 0.00f);
			assertEquals("N", stat.getC600());
			assertEquals("4", stat.getC620());
			assertEquals(0.00f, stat.getC640(), 0.00f);
		} else {
			fail("Statistiche per numero domanda 182206 non trovate");
		}
	}
	
	@Test
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_189624_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_189624_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs21Domanda189624() throws ElaborazioneDomandaException {
		mock("189624");	
		simulaProcesso(generaStatisticaCs21);
		
		A4gtStatisticaDu statEx = new A4gtStatisticaDu();
		statEx.setIdDomanda(189624L);
		Optional<A4gtStatisticaDu> statOpt = daoStatistiche.findOne(Example.of(statEx));
		if (statOpt.isPresent()) {
			A4gtStatisticaDu stat = statOpt.get();
			assertEquals(Integer.valueOf(2017), stat.getC110());
			assertEquals("02238970228", stat.getF200());
			assertTrue(stat.getF300().endsWith("189624"));
			assertEquals("1", stat.getC300a());
			assertEquals(Date.valueOf("2018-06-08") , stat.getF300b());
			assertEquals("2", stat.getC400());
			assertEquals(130.45f, stat.getC551(), 0.00f);
			assertEquals(128.54f, stat.getC552(), 0.00f);
			assertEquals(27115.51f, stat.getC554(), 0.00f);
//			assertEquals(110.18f, stat.getC557(), 0.00f);
			assertEquals(110.18f, stat.getC558(), 0.00f);
			assertEquals(23241.42f, stat.getC559(), 0.00f);
			assertEquals(18.37f, stat.getC560(), 0.00f);
			assertEquals(3874.09f, stat.getC561(), 0.00f);
			assertEquals("FT", stat.getC600());
			assertEquals("2", stat.getC611());
			assertEquals("4", stat.getC620());
			assertEquals("1", stat.getC621());
			assertEquals(-5811.15f, stat.getC640(), 0.00f);
		} else {
			fail("Statistiche per numero domanda 189624 non trovate");
		}
	}
	
	@Test
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_189624_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_189624_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs22Domanda189624() throws ElaborazioneDomandaException {
		mock("189624");
		simulaProcesso(generaStatisticaCs22);
		
		A4gtStatisticaDu statEx = new A4gtStatisticaDu();
		statEx.setIdDomanda(189624L);
		Optional<A4gtStatisticaDu> statOpt = daoStatistiche.findOne(Example.of(statEx));
		if (statOpt.isPresent()) {
			A4gtStatisticaDu stat = statOpt.get();
			assertEquals(Integer.valueOf(2017), stat.getC110());
			assertEquals("02238970228", stat.getF200());
			assertTrue(stat.getF300().endsWith("189624"));
			assertEquals("1", stat.getC300a());
			assertEquals(Date.valueOf("2018-06-08") , stat.getF300b());
			assertEquals("1", stat.getC400());
			assertEquals(128.54f, stat.getC552(), 0.00f);
			assertEquals(14078.37f, stat.getC554(), 0.00f);
			assertEquals(110.18f, stat.getC558(), 0.00f);
			assertEquals(0.00f, stat.getC558f(), 0.00f);
			assertEquals(12066.94f, stat.getC559(), 0.00f);
			assertEquals(18.37f, stat.getC560(), 0.00f);
			assertEquals(2011.43f, stat.getC561(), 0.00f);
			assertEquals("FT", stat.getC600());
			assertEquals("2", stat.getC611());
			assertEquals("4", stat.getC620());
			assertEquals("1", stat.getC621());
			assertEquals(0.00f, stat.getC640(), 0.00f);
		} else {
			fail("Statistiche per numero domanda 189624 non trovate");
		}
	}
	
	@Test
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_189624_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_189624_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs25Domanda189624() throws ElaborazioneDomandaException {
		mock("189624");
		
		Mockito.when(disaccoppiatoService.isGiovane(Mockito.any())).thenReturn(true);
		
		simulaProcesso(generaStatisticaCs25);
		
		A4gtStatisticaDu statEx = new A4gtStatisticaDu();
		statEx.setIdDomanda(189624L);
		Optional<A4gtStatisticaDu> statOpt = daoStatistiche.findOne(Example.of(statEx));
		if (statOpt.isPresent()) {
			A4gtStatisticaDu stat = statOpt.get();
			assertEquals(Integer.valueOf(2017), stat.getC110());
			assertEquals("02238970228", stat.getF200());
			assertTrue(stat.getF300().endsWith("189624"));
			assertEquals("1", stat.getC300a());
			assertEquals(Date.valueOf("2018-06-08") , stat.getF300b());
			assertEquals(130.45f, stat.getC551(), 0.00f);
			assertEquals(90.00f, stat.getC552(), 0.00f);
			assertEquals(9492.75f, stat.getC554(), 0.00f);
			assertEquals(90.00f, stat.getC558(), 0.00f);
			assertEquals(9492.75f, stat.getC559(), 0.00f);
			assertEquals(0.00f, stat.getC560(), 0.00f);
			assertEquals(0.00f, stat.getC561(), 0.00f);
			assertEquals("FT", stat.getC600());
			assertEquals("2", stat.getC611());
			assertEquals("4", stat.getC620());
			assertEquals("3", stat.getC621());
			assertEquals(0.00f, stat.getC640(), 0.00f);
		} else {
			fail("Statistiche per numero domanda 189624 non trovate");
		}
	}
	
	@Test
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_189624_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_189624_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs27Domanda189624M01() throws ElaborazioneDomandaException {
		mock("189624_27");
		simulaProcesso(generaStatisticaCs27Zootecnia);
		
		A4gtStatisticaDu statEx = new A4gtStatisticaDu();
		statEx.setIdDomanda(189624L);
		statEx.setC109a("M01");
		Optional<A4gtStatisticaDu> statOpt = daoStatistiche.findOne(Example.of(statEx));
		if (statOpt.isPresent()) {
			A4gtStatisticaDu stat = statOpt.get();
			assertEquals(Integer.valueOf(2017), stat.getC110());
			assertEquals("02238970228", stat.getF200());
			assertTrue(stat.getF300().endsWith("189624"));
			assertEquals("1", stat.getC300a());
			assertEquals(Date.valueOf("2018-06-08") , stat.getF300b());
			assertEquals(42.00f, stat.getC551(), 0.00f);
			assertEquals(42.00f, stat.getC552(), 0.00f);
			assertEquals(3280.62f, stat.getC554(), 0.00f);
			assertEquals(42.00f, stat.getC558(), 0.00f);
			assertEquals(3280.62f, stat.getC559(), 0.00f);
			assertEquals(0.00f, stat.getC560(), 0.00f);
			assertEquals(0.00f, stat.getC561(), 0.00f);
			assertEquals("F", stat.getC600());
			assertEquals("2", stat.getC611());
			assertEquals("4", stat.getC620());
			assertEquals("3", stat.getC621());
			assertEquals(0.00f, stat.getC640(), 0.00f);
		} else {
			fail("Statistiche per numero domanda 189624 non trovate");
		}
	}
	
	@Test
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_189624_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_189624_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs27Domanda189624M02() throws ElaborazioneDomandaException {
		mock("189624_27");
		simulaProcesso(generaStatisticaCs27Zootecnia);
		
		A4gtStatisticaDu statEx = new A4gtStatisticaDu();
		statEx.setIdDomanda(189624L);
		statEx.setC109a("M02");
		Optional<A4gtStatisticaDu> statOpt = daoStatistiche.findOne(Example.of(statEx));
		if (statOpt.isPresent()) {
			A4gtStatisticaDu stat = statOpt.get();
			assertEquals(Integer.valueOf(2017), stat.getC110());
			assertEquals("02238970228", stat.getF200());
			assertTrue(stat.getF300().endsWith("189624"));
			assertEquals("1", stat.getC300a());
			assertEquals(Date.valueOf("2018-06-08") , stat.getF300b());
			assertEquals(40.00f, stat.getC551(), 0.00f);
			assertEquals(40.00f, stat.getC552(), 0.00f);
			assertEquals(2755.20f, stat.getC554(), 0.00f);
			assertEquals(40.00f, stat.getC558(), 0.00f);
			assertEquals(2755.20f, stat.getC559(), 0.00f);
			assertEquals(0.00f, stat.getC560(), 0.00f);
			assertEquals(0.00f, stat.getC561(), 0.00f);
			assertEquals("F", stat.getC600());
			assertEquals("2", stat.getC611());
			assertEquals("4", stat.getC620());
			assertEquals("3", stat.getC621());
			assertEquals(0.00f, stat.getC640(), 0.00f);
		} else {
			fail("Statistiche per numero domanda 189624 non trovate");
		}
	}
	
	@Test
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_184437_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_184437_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs21Domanda184437() throws ElaborazioneDomandaException {
		mock("184437");
		simulaProcesso(generaStatisticaCs21);
		
		A4gtStatisticaDu statEx = new A4gtStatisticaDu();
		statEx.setIdDomanda(184437L);
		Optional<A4gtStatisticaDu> statOpt = daoStatistiche.findOne(Example.of(statEx));
		if (statOpt.isPresent()) {
			A4gtStatisticaDu stat = statOpt.get();
			assertEquals(Integer.valueOf(2017), stat.getC110());
			assertEquals("RTNRNI58E05L174G", stat.getF200());
			assertTrue(stat.getF300().endsWith("184437"));
			assertEquals("1", stat.getC300a());
			assertEquals(Date.valueOf("2018-05-03") , stat.getF300b());
			assertEquals("2", stat.getC400());
			assertEquals(163.22f, stat.getC551(), 0.00f);
			assertEquals(153.16f, stat.getC552(), 0.00f);
			assertEquals(18362.35f, stat.getC554(), 0.00f);
			assertEquals(153.16f, stat.getC558(), 0.00f);
//			assertEquals(18362.35f, stat.getC559(), 0.00f);
			assertEquals(0.00f, stat.getC560(), 0.00f);
//			assertEquals(0.00f, stat.getC561(), 0.00f);
			assertEquals("N", stat.getC600());
//			assertEquals("4", stat.getC620());
			assertEquals(0.00f, stat.getC640(), 0.00f);
		} else {
			fail("Statistiche per numero domanda 184437 non trovate");
		}
	}
	
	@Test
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_184437_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_184437_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs22Domanda184437() throws ElaborazioneDomandaException {
		mock("184437");
		simulaProcesso(generaStatisticaCs22);
		
		A4gtStatisticaDu statEx = new A4gtStatisticaDu();
		statEx.setIdDomanda(184437L);
		Optional<A4gtStatisticaDu> statOpt = daoStatistiche.findOne(Example.of(statEx));
		if (statOpt.isPresent()) {
			A4gtStatisticaDu stat = statOpt.get();
			assertEquals(Integer.valueOf(2017), stat.getC110());
			assertEquals("RTNRNI58E05L174G", stat.getF200());
			assertTrue(stat.getF300().endsWith("184437"));
			assertEquals("1", stat.getC300a());
			assertEquals(Date.valueOf("2018-05-03") , stat.getF300b());
			assertEquals("1", stat.getC400());
			assertEquals(153.16f, stat.getC552(), 0.00f);
			assertEquals(9533.73f, stat.getC554(), 0.00f);
			assertEquals(153.16f, stat.getC558(), 0.00f);
			assertEquals(0.72f, stat.getC558f(), 0.00f);
			assertEquals(9533.73f, stat.getC559(), 0.00f);
			assertEquals(0.00f, stat.getC560(), 0.00f);
			assertEquals(0.00f, stat.getC561(), 0.00f);
			assertEquals("N", stat.getC600());
			assertEquals("4", stat.getC620());
			assertEquals(0.00f, stat.getC640(), 0.00f);
		} else {
			fail("Statistiche per numero domanda 184437 non trovate");
		}
	}
	
	
	@Test
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_184437_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_184437_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs27Domanda184437M01() throws ElaborazioneDomandaException {
		mock("184437_27");
		simulaProcesso(generaStatisticaCs27Zootecnia);
		
		A4gtStatisticaDu statEx = new A4gtStatisticaDu();
		statEx.setIdDomanda(184437L);
		statEx.setC109a("M01");
		Optional<A4gtStatisticaDu> statOpt = daoStatistiche.findOne(Example.of(statEx));
		if (statOpt.isPresent()) {
			A4gtStatisticaDu stat = statOpt.get();
			assertEquals(Integer.valueOf(2017), stat.getC110());
			assertEquals("RTNRNI58E05L174G", stat.getF200());
			assertTrue(stat.getF300().endsWith("184437"));
			assertEquals("1", stat.getC300a());
			assertEquals(Date.valueOf("2018-05-03") , stat.getF300b());
			assertEquals(59.00f, stat.getC551(), 0.00f);
			assertEquals(59.00f, stat.getC552(), 0.00f);
			assertEquals(4608.49f, stat.getC554(), 0.00f);
			assertEquals(59.00f, stat.getC558(), 0.00f);
			assertEquals(4608.49f, stat.getC559(), 0.00f);
			assertEquals(0.00f, stat.getC560(), 0.00f);
			assertEquals(0.00f, stat.getC561(), 0.00f);
			assertEquals("N", stat.getC600());
			assertEquals("4", stat.getC620());
			assertEquals(null, stat.getC621());
			assertEquals(0.00f, stat.getC640(), 0.00f);
		} else {
			fail("Statistiche per numero domanda 184437 non trovate");
		}
	}
	
	@Test
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_184437_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_184437_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs27Domanda184437M02() throws ElaborazioneDomandaException {
		mock("184437_27");
		simulaProcesso(generaStatisticaCs27Zootecnia);
		
		A4gtStatisticaDu statEx = new A4gtStatisticaDu();
		statEx.setIdDomanda(184437L);
		statEx.setC109a("M02");
		Optional<A4gtStatisticaDu> statOpt = daoStatistiche.findOne(Example.of(statEx));
		if (statOpt.isPresent()) {
			A4gtStatisticaDu stat = statOpt.get();
			assertEquals(Integer.valueOf(2017), stat.getC110());
			assertEquals("RTNRNI58E05L174G", stat.getF200());
			assertTrue(stat.getF300().endsWith("184437"));
			assertEquals("1", stat.getC300a());
			assertEquals(Date.valueOf("2018-05-03") , stat.getF300b());
			assertEquals(59.00f, stat.getC551(), 0.00f);
			assertEquals(59.00f, stat.getC552(), 0.00f);
			assertEquals(4063.92f, stat.getC554(), 0.00f);
			assertEquals(59.00f, stat.getC558(), 0.00f);
			assertEquals(4063.92f, stat.getC559(), 0.00f);
			assertEquals(0.00f, stat.getC560(), 0.00f);
			assertEquals(0.00f, stat.getC561(), 0.00f);
			assertEquals("N", stat.getC600());
			assertEquals("4", stat.getC620());
			assertEquals(null, stat.getC621());
			assertEquals(0.00f, stat.getC640(), 0.00f);
		} else {
			fail("Statistiche per numero domanda 184437 non trovate");
		}
	}
	
	@Test
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_184437_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_184437_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs27Domanda184437M19() throws ElaborazioneDomandaException {
		
		mock("184437_27");
		simulaProcesso(generaStatisticaCs27Zootecnia);
		
		A4gtStatisticaDu statEx = new A4gtStatisticaDu();
		statEx.setIdDomanda(184437L);
		statEx.setC109a("M19");
		Optional<A4gtStatisticaDu> statOpt = daoStatistiche.findOne(Example.of(statEx));
		if (statOpt.isPresent()) {
			A4gtStatisticaDu stat = statOpt.get();
			assertEquals(Integer.valueOf(2017), stat.getC110());
			assertEquals("RTNRNI58E05L174G", stat.getF200());
			assertTrue(stat.getF300().endsWith("184437"));
			assertEquals("1", stat.getC300a());
			assertEquals(Date.valueOf("2018-05-03") , stat.getF300b());
			assertEquals(39.00f, stat.getC551(), 0.00f);
			assertEquals(39.00f, stat.getC552(), 0.00f);
			assertEquals(2346.24f, stat.getC554(), 0.00f);
			assertEquals(37.00f, stat.getC558(), 0.00f);
			assertEquals(2225.92f, stat.getC559(), 0.00f);
			assertEquals(2.00f, stat.getC560(), 0.00f);
			assertEquals(120.32f, stat.getC561(), 0.00f);
			assertEquals("N", stat.getC600());
			assertEquals("4", stat.getC620());
			assertEquals(null, stat.getC621());
			assertEquals(-120.42f, stat.getC640(), 0.00f);
		} else {
			fail("Statistiche per numero domanda 184437 non trovate");
		}
	}
	
	@Test
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_186823_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_186823_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs21Domanda186823() throws ElaborazioneDomandaException {
		mock("186823");
		simulaProcesso(generaStatisticaCs21);
		
		A4gtStatisticaDu statEx = new A4gtStatisticaDu();
		statEx.setIdDomanda(186823L);
		Optional<A4gtStatisticaDu> statOpt = daoStatistiche.findOne(Example.of(statEx));
		if (statOpt.isPresent()) {
			A4gtStatisticaDu stat = statOpt.get();
			assertEquals(Integer.valueOf(2017), stat.getC110());
			assertEquals("PLLPLA69R24C794N", stat.getF200());
			assertTrue(stat.getF300().endsWith("186823"));
			assertEquals("1", stat.getC300a());
			assertEquals(Date.valueOf("2018-05-17") , stat.getF300b());
			assertEquals("2", stat.getC400());
			assertEquals(3.75f, stat.getC551(), 0.00f);
			assertEquals(3.70f, stat.getC552(), 0.00f);
			assertEquals(358.94f, stat.getC554(), 0.00f);
			assertEquals(3.70f, stat.getC558(), 0.00f);
//			assertEquals(358.94f, stat.getC559(), 0.00f);
			assertEquals(0.00f, stat.getC560(), 0.00f);
//			assertEquals(0.00f, stat.getC561(), 0.00f);
			assertEquals("N", stat.getC600());
//			assertEquals("4", stat.getC620());
			assertEquals(0.00f, stat.getC640(), 0.00f);
		} else {
			fail("Statistiche per numero domanda 186823 non trovate");
		}
	}
	
	@Test
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_186823_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_186823_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs22Domanda186823() throws ElaborazioneDomandaException {
		mock("186823");
		simulaProcesso(generaStatisticaCs22);
		
		A4gtStatisticaDu statEx = new A4gtStatisticaDu();
		statEx.setIdDomanda(186823L);
		Optional<A4gtStatisticaDu> statOpt = daoStatistiche.findOne(Example.of(statEx));
		if (statOpt.isPresent()) {
			A4gtStatisticaDu stat = statOpt.get();
			assertEquals(Integer.valueOf(2017), stat.getC110());
			assertEquals("PLLPLA69R24C794N", stat.getF200());
			assertTrue(stat.getF300().endsWith("186823"));
			assertEquals("1", stat.getC300a());
			assertEquals(Date.valueOf("2018-05-17") , stat.getF300b());
			assertEquals("2", stat.getC400());
			assertEquals(3.70f, stat.getC552(), 0.00f);
			assertEquals(186.36f, stat.getC554(), 0.00f);
			assertEquals(3.70f, stat.getC558(), 0.00f);
			assertEquals(0.00f, stat.getC558f(), 0.00f);
			assertEquals(186.36f, stat.getC559(), 0.00f);
			assertEquals(0.00f, stat.getC560(), 0.00f);
			assertEquals(0.00f, stat.getC561(), 0.00f);
			assertEquals("N", stat.getC600());
			assertEquals("4", stat.getC620());
			assertEquals(0.00f, stat.getC640(), 0.00f);
		} else {
			fail("Statistiche per numero domanda 186823 non trovate");
		}
	}
	
	@Test
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_189199_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_189199_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs21Domanda189199() throws ElaborazioneDomandaException {
		mock("189199");
		simulaProcesso(generaStatisticaCs21);
		
		A4gtStatisticaDu statEx = new A4gtStatisticaDu();
		statEx.setIdDomanda(189199L);
		Optional<A4gtStatisticaDu> statOpt = daoStatistiche.findOne(Example.of(statEx));
		if (statOpt.isPresent()) {
			A4gtStatisticaDu stat = statOpt.get();
			assertEquals(Integer.valueOf(2017), stat.getC110());
			assertEquals("PLNGRG59R10H330A", stat.getF200());
			assertTrue(stat.getF300().endsWith("189199"));
			assertEquals("1", stat.getC300a());
			assertEquals(Date.valueOf("2018-06-08") , stat.getF300b());
			assertEquals("2", stat.getC400());
			assertEquals(10.25f, stat.getC551(), 0.00f);
			assertEquals(9.08f, stat.getC552(), 0.00f);
			assertEquals(1152.71f, stat.getC554(), 0.00f);
			assertEquals(9.08f, stat.getC558(), 0.00f);
//			assertEquals(1152.71f, stat.getC559(), 0.00f);
			assertEquals(0.00f, stat.getC560(), 0.00f);
//			assertEquals(0.00f, stat.getC561(), 0.00f);
			assertEquals("N", stat.getC600());
//			assertEquals("4", stat.getC620());
			assertEquals(0.00f, stat.getC640(), 0.00f);
		} else {
			fail("Statistiche per numero domanda 189199 non trovate");
		}
	}
	
	@Test
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_189199_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_189199_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs22Domanda189199() throws ElaborazioneDomandaException {
		mock("189199");
		simulaProcesso(generaStatisticaCs22);
		
		A4gtStatisticaDu statEx = new A4gtStatisticaDu();
		statEx.setIdDomanda(189199L);
		Optional<A4gtStatisticaDu> statOpt = daoStatistiche.findOne(Example.of(statEx));
		if (statOpt.isPresent()) {
			A4gtStatisticaDu stat = statOpt.get();
			assertEquals(Integer.valueOf(2017), stat.getC110());
			assertEquals("PLNGRG59R10H330A", stat.getF200());
			assertTrue(stat.getF300().endsWith("189199"));
			assertEquals("1", stat.getC300a());
			assertEquals(Date.valueOf("2018-06-08") , stat.getF300b());
			assertEquals("2", stat.getC400());
			assertEquals(9.08f, stat.getC552(), 0.00f);
			assertEquals(598.48f, stat.getC554(), 0.00f);
			assertEquals(9.08f, stat.getC558(), 0.00f);
			assertEquals(0.56f, stat.getC558f(), 0.00f);
			assertEquals(598.48f, stat.getC559(), 0.00f);
			assertEquals(0.00f, stat.getC560(), 0.00f);
			assertEquals(0.00f, stat.getC561(), 0.00f);
			assertEquals("N", stat.getC600());
			assertEquals("4", stat.getC620());
			assertEquals(0.00f, stat.getC640(), 0.00f);
		} else {
			fail("Statistiche per numero domanda 189199 non trovate");
		}
	}
	
	
	@Test
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_189199_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_189199_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs27Domanda189199M17() throws ElaborazioneDomandaException {
		mock("189199");
		simulaProcesso(generaStatisticaCs27Superficie);
		
		A4gtStatisticaDu statEx = new A4gtStatisticaDu();
		statEx.setIdDomanda(189199L);
		statEx.setC109a("M17");
		Optional<A4gtStatisticaDu> statOpt = daoStatistiche.findOne(Example.of(statEx));
		if (statOpt.isPresent()) {
			A4gtStatisticaDu stat = statOpt.get();
			assertEquals(Integer.valueOf(2017), stat.getC110());
			assertEquals("PLNGRG59R10H330A", stat.getF200());
			assertTrue(stat.getF300().endsWith("189199"));
			assertEquals("1", stat.getC300a());
			assertEquals(Date.valueOf("2018-06-08") , stat.getF300b());
			assertEquals(2.71f, stat.getC551(), 0.00f);
			assertEquals(2.71f, stat.getC552(), 0.00f);
			assertEquals(347.69f, stat.getC554(), 0.00f);
			assertEquals(2.69f, stat.getC558(), 0.00f);
			assertEquals(344.21f, stat.getC559(), 0.00f);
			assertEquals(0.03f, stat.getC560(), 0.00f);
			assertEquals(3.48f, stat.getC561(), 0.00f);
			assertEquals("N", stat.getC600());
			assertEquals("4", stat.getC620());
			assertEquals(0.00f, stat.getC640(), 0.00f);
		} else {
			fail("Statistiche per numero domanda 189199 non trovate");
		}
	}
	

	@Test
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_184017_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_184017_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs21Domanda184017() throws ElaborazioneDomandaException {
		mock("184017");
		simulaProcesso(generaStatisticaCs21);
		
		A4gtStatisticaDu statEx = new A4gtStatisticaDu();
		statEx.setIdDomanda(184017L);
		Optional<A4gtStatisticaDu> statOpt = daoStatistiche.findOne(Example.of(statEx));
		if (statOpt.isPresent()) {
			A4gtStatisticaDu stat = statOpt.get();
			assertEquals(Integer.valueOf(2017), stat.getC110());
			assertEquals("02333890222", stat.getF200());
			assertTrue(stat.getF300().endsWith("184017"));
			assertEquals("1", stat.getC300a());
			assertEquals(Date.valueOf("2018-05-02") , stat.getF300b());
			assertEquals("2", stat.getC400());
			assertEquals(85.35f, stat.getC551(), 0.00f);
			assertEquals(84.73f, stat.getC552(), 0.00f);
			assertEquals(17344.23f, stat.getC554(), 0.00f);
			assertEquals(19.15f, stat.getC558(), 0.00f);
//			assertEquals(0.00f, stat.getC559(), 0.00f);
			assertEquals(65.58f, stat.getC560(), 0.00f);
//			assertEquals(17344.23f, stat.getC561(), 0.00f);
			assertEquals("N", stat.getC600());
//			assertEquals("2", stat.getC620());
			assertEquals(-17344.23f, stat.getC640(), 0.00f);
		} else {
			fail("Statistiche per numero domanda 184017 non trovate");
		}
	}
	
	@Test
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_184017_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_184017_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs22Domanda184017() throws ElaborazioneDomandaException {
		mock("184017");
		simulaProcesso(generaStatisticaCs22);
		
		A4gtStatisticaDu statEx = new A4gtStatisticaDu();
		statEx.setIdDomanda(184017L);
		Optional<A4gtStatisticaDu> statOpt = daoStatistiche.findOne(Example.of(statEx));
		if (statOpt.isPresent()) {
			A4gtStatisticaDu stat = statOpt.get();
			assertEquals(Integer.valueOf(2017), stat.getC110());
			assertEquals("02333890222", stat.getF200());
			assertTrue(stat.getF300().endsWith("184017"));
			assertEquals("1", stat.getC300a());
			assertEquals(Date.valueOf("2018-05-02") , stat.getF300b());
			assertEquals("1", stat.getC400());
			assertEquals(84.73f, stat.getC552(), 0.00f);
			assertEquals(9005.12f, stat.getC554(), 0.00f);
			assertEquals(19.15f, stat.getC558(), 0.00f);
			assertEquals(7.61f, stat.getC558f(), 0.00f);
			assertEquals(2035.22f, stat.getC559(), 0.00f);
			assertEquals(65.58f, stat.getC560(), 0.00f);
			assertEquals(6969.90f, stat.getC561(), 0.00f);
			assertEquals("N", stat.getC600());
			assertEquals("4", stat.getC620());
			assertEquals(0.00f, stat.getC640(), 0.00f);
		} else {
			fail("Statistiche per numero domanda 184017 non trovate");
		}
	}
	
	@Test
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_184017_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_184017_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs25Domanda184017() throws ElaborazioneDomandaException {
		mock("184017");
		
		Mockito.when(disaccoppiatoService.isGiovane(Mockito.any())).thenReturn(true);
		
		simulaProcesso(generaStatisticaCs25);
		
		A4gtStatisticaDu statEx = new A4gtStatisticaDu();
		statEx.setIdDomanda(184017L);
		Optional<A4gtStatisticaDu> statOpt = daoStatistiche.findOne(Example.of(statEx));
		if (statOpt.isPresent()) {
			A4gtStatisticaDu stat = statOpt.get();
			assertEquals(Integer.valueOf(2017), stat.getC110());
			assertEquals("02333890222", stat.getF200());
			assertTrue(stat.getF300().endsWith("184017"));
			assertEquals("1", stat.getC300a());
			assertEquals(Date.valueOf("2018-05-02") , stat.getF300b());
			assertEquals(85.35f, stat.getC551(), 0.00f);
			assertEquals(84.73f, stat.getC552(), 0.00f);
			assertEquals(8672.12f, stat.getC554(), 0.00f);
			assertEquals(0.00f, stat.getC558(), 0.00f);
			assertEquals(0.00f, stat.getC559(), 0.00f);
			assertEquals(84.73f, stat.getC560(), 0.00f);
			assertEquals(8672.12f, stat.getC561(), 0.00f);
			assertEquals("N", stat.getC600());
			assertEquals("2", stat.getC620());
			assertEquals(0.00f, stat.getC640(), 0.00f);
		} else {
			fail("Statistiche per numero domanda 184017 non trovate");
		}
	}
	
	@Test
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_184017_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_184017_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs27Domanda184017M01() throws ElaborazioneDomandaException {
		mock("184017_27");
		simulaProcesso(generaStatisticaCs27Zootecnia);
		
		A4gtStatisticaDu statEx = new A4gtStatisticaDu();
		statEx.setIdDomanda(184017L);
		statEx.setC109a("M01");
		Optional<A4gtStatisticaDu> statOpt = daoStatistiche.findOne(Example.of(statEx));
		if (statOpt.isPresent()) {
			A4gtStatisticaDu stat = statOpt.get();
			assertEquals(Integer.valueOf(2017), stat.getC110());
			assertEquals("02333890222", stat.getF200());
			assertTrue(stat.getF300().endsWith("184017"));
			assertEquals("1", stat.getC300a());
			assertEquals(Date.valueOf("2018-05-02") , stat.getF300b());
			assertEquals(65.00f, stat.getC551(), 0.00f);
			assertEquals(65.00f, stat.getC552(), 0.00f);
			assertEquals(5077.15f, stat.getC554(), 0.00f);
			assertEquals(65.00f, stat.getC558(), 0.00f);
			assertEquals(5077.15f, stat.getC559(), 0.00f);
			assertEquals(0.00f, stat.getC560(), 0.00f);
			assertEquals(0.00f, stat.getC561(), 0.00f);
			assertEquals("N", stat.getC600());
			assertEquals("4", stat.getC620());
			assertEquals(0.00f, stat.getC640(), 0.00f);
		} else {
			fail("Statistiche per numero domanda 184017 non trovate");
		}
	}
	
	@Test
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_184017_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_184017_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs27Domanda184017M02() throws ElaborazioneDomandaException {
		mock("184017_27");
		simulaProcesso(generaStatisticaCs27Zootecnia);
		
		A4gtStatisticaDu statEx = new A4gtStatisticaDu();
		statEx.setIdDomanda(184017L);
		statEx.setC109a("M02");
		Optional<A4gtStatisticaDu> statOpt = daoStatistiche.findOne(Example.of(statEx));
		if (statOpt.isPresent()) {
			A4gtStatisticaDu stat = statOpt.get();
			assertEquals(Integer.valueOf(2017), stat.getC110());
			assertEquals("02333890222", stat.getF200());
			assertTrue(stat.getF300().endsWith("184017"));
			assertEquals("1", stat.getC300a());
			assertEquals(Date.valueOf("2018-05-02") , stat.getF300b());
			assertEquals(63.00f, stat.getC551(), 0.00f);
			assertEquals(63.00f, stat.getC552(), 0.00f);
			assertEquals(4339.44f, stat.getC554(), 0.00f);
			assertEquals(63.00f, stat.getC558(), 0.00f);
			assertEquals(4339.44f, stat.getC559(), 0.00f);
			assertEquals(0.00f, stat.getC560(), 0.00f);
			assertEquals(0.00f, stat.getC561(), 0.00f);
			assertEquals("N", stat.getC600());
			assertEquals("4", stat.getC620());
			assertEquals(0.00f, stat.getC640(), 0.00f);
		} else {
			fail("Statistiche per numero domanda 184017 non trovate");
		}
	}
	
	@Test
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_184017_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_184017_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs27Domanda184017M04() throws ElaborazioneDomandaException {
		mock("184017_27");
		simulaProcesso(generaStatisticaCs27Zootecnia);
		
		A4gtStatisticaDu statEx = new A4gtStatisticaDu();
		statEx.setIdDomanda(184017L);
		statEx.setC109a("M04");
		Optional<A4gtStatisticaDu> statOpt = daoStatistiche.findOne(Example.of(statEx));
		if (statOpt.isPresent()) {
			A4gtStatisticaDu stat = statOpt.get();
			assertEquals(Integer.valueOf(2017), stat.getC110());
			assertEquals("02333890222", stat.getF200());
			assertTrue(stat.getF300().endsWith("184017"));
			assertEquals("1", stat.getC300a());
			assertEquals(Date.valueOf("2018-05-02") , stat.getF300b());
			assertEquals(1.00f, stat.getC551(), 0.00f);
			assertEquals(1.00f, stat.getC552(), 0.00f);
			assertEquals(124.66f, stat.getC554(), 0.00f);
			assertEquals(1.00f, stat.getC558(), 0.00f);
			assertEquals(124.66f, stat.getC559(), 0.00f);
			assertEquals(0.00f, stat.getC560(), 0.00f);
			assertEquals(0.00f, stat.getC561(), 0.00f);
			assertEquals("N", stat.getC600());
			assertEquals("4", stat.getC620());
			assertEquals(0.00f, stat.getC640(), 0.00f);
		} else {
			fail("Statistiche per numero domanda 184017 non trovate");
		}
	}
		
		// Test campi C559 per CS21 - Segnalazioni ERROR LIST del 9 Luglio 2020
		@Test
		@WithMockUser(username = "istruttore", roles = {
	            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
	            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
		@Sql(scripts = { "/statistiche/statistiche_nDomanda_213209_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
		@Sql(scripts = { "/statistiche/statistiche_nDomanda_213209_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
		public void testStatisticheCs21Domanda213209() throws ElaborazioneDomandaException {
			mock("213209");
			simulaProcesso(generaStatisticaCs21);
			
			A4gtStatisticaDu statEx = new A4gtStatisticaDu();
			statEx.setIdDomanda(213209L);
			Optional<A4gtStatisticaDu> statOpt = daoStatistiche.findOne(Example.of(statEx));
			if (statOpt.isPresent()) {
				A4gtStatisticaDu stat = statOpt.get();
				assertEquals(Integer.valueOf(2017), stat.getC110());
				assertEquals("PRSSRN98R66L378R", stat.getF200());
				assertTrue(stat.getF300().endsWith("213209"));
				assertEquals(613.86f, stat.getC559(), 0.00f);
			} else {
				fail("Statistiche per numero domanda 213209 non trovate");
			}
		}
		
		// Test campo C400 per CS21 - Segnalazioni ERROR LIST del 9 Luglio 2020
		@Test
		@WithMockUser(username = "istruttore", roles = {
	            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
	            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
		@Sql(scripts = { "/statistiche/statistiche_nDomanda_210586_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
		@Sql(scripts = { "/statistiche/statistiche_nDomanda_210586_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
		public void testStatisticheCs21Domanda210586() throws ElaborazioneDomandaException {
			mock("210586");
			simulaProcesso(generaStatisticaCs21);
			
			A4gtStatisticaDu statEx = new A4gtStatisticaDu();
			statEx.setIdDomanda(210586L);
			Optional<A4gtStatisticaDu> statOpt = daoStatistiche.findOne(Example.of(statEx));
			if (statOpt.isPresent()) {
				A4gtStatisticaDu stat = statOpt.get();
				assertEquals(Integer.valueOf(2017), stat.getC110());
				assertEquals("VLLLCU91R46H612I", stat.getF200());
				assertEquals("3", stat.getC400());
			} else {
				fail("Statistiche per numero domanda 210586 non trovate");
			}
		}
	
	// Test campo C560A per CS22 - Segnalazioni ERROR LIST del 9 Luglio 2020
	@Test
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_211512_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	//@Sql(scripts = { "/statistiche/statistiche_nDomanda_211512_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs22Domanda211512() throws ElaborazioneDomandaException {
		mock("211512");
		simulaProcesso(generaStatisticaCs22);
		
		A4gtStatisticaDu statEx = new A4gtStatisticaDu();
		statEx.setIdDomanda(211512L);
		Optional<A4gtStatisticaDu> statOpt = daoStatistiche.findOne(Example.of(statEx));
		if (statOpt.isPresent()) {
			A4gtStatisticaDu stat = statOpt.get();
			assertEquals(Integer.valueOf(2017), stat.getC110());
			assertEquals("CMARTI52E68B006T", stat.getF200());
			assertTrue(stat.getC560a() > 0);
		} else {
			fail("Statistiche per numero domanda 211512 non trovate");
		}
	}
	
	
	@Test
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_209576_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_209576_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs21_C557_Domanda_209576() throws ElaborazioneDomandaException {
		mock("209576");
		simulaProcesso2019(generaStatisticaCs21);
		
		A4gtStatisticaDu statEx = new A4gtStatisticaDu();
		statEx.setIdDomanda(209576L);
		statEx.setStato(StatoIstruttoria.PAGAMENTO_AUTORIZZATO);
		statEx.setTipoStatistica(TipologiaStatistica.CS21);
		statEx.setF300("009201900209576");
		Optional<A4gtStatisticaDu> statOpt = daoStatistiche.findOne(Example.of(statEx));
		List<A4gtStatisticaDu> statOptList = daoStatistiche.findAll(Example.of(statEx));
		if (statOpt.isPresent()) {
			A4gtStatisticaDu stat = statOpt.get();
			assertEquals(Integer.valueOf(2019), stat.getC110());
			assertEquals(stat.getC557(), Float.valueOf(("16.15")));//16.1493
		} else {
			fail("Statistiche per numero domanda 209576 non trovate");
		}
	}
	
	@Test
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_214069_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_214069_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs21_C300A_Domanda_214069() throws ElaborazioneDomandaException {
		mock("214069");
		simulaProcesso2019(generaStatisticaCs21);
		
		A4gtStatisticaDu statEx = new A4gtStatisticaDu();
		statEx.setIdDomanda(214069L);
		statEx.setStato(StatoIstruttoria.PAGAMENTO_AUTORIZZATO);
		statEx.setTipoStatistica(TipologiaStatistica.CS21);
		Optional<A4gtStatisticaDu> statOpt = daoStatistiche.findOne(Example.of(statEx));
		List<A4gtStatisticaDu> statOptList = daoStatistiche.findAll(Example.of(statEx));
		if (statOpt.isPresent()) {
			A4gtStatisticaDu stat = statOpt.get();
			//assertEquals(Integer.valueOf(2019), stat.getC110());
			//assertEquals(stat.getF300b(), 2);//TODO CAPIRE analisi
		} else {
			fail("Statistiche per numero domanda 214069 non trovate");
		}
	}
	
	
	
	private void mock(String numeroDomanda) {
		Mockito.when(restTemplate.getForObject(Mockito.any(), ArgumentMatchers.eq(Cuaa.class))).thenReturn(null);

		Mockito.when(nutsDao.findBySiglaProvincia(Mockito.any())).thenReturn(null);
		
		List<CampioneModel> listCamp = new ArrayList<CampioneModel>();
		CampioneModel camp = new CampioneModel();
		camp.setAnnoCampagna(2017);
		switch(numeroDomanda) {
		case "189199":
		case "186823":
		case "184437":
		case "184017":
		case "182206":
		case "183606":
		case "190837":
		case "213209":
		case "210586":
		case "211512":
			Mockito.when(restTemplate.getForObject(Mockito.any(), ArgumentMatchers.eq(Boolean.class))).thenReturn(false);
			camp.setAmbitoCampione(AmbitoCampione.ZOOTECNIA);
			camp.setCampioneStatistico(CampioneStatistico.CASUALE);
			break;
		case "189624_27":
			Mockito.when(restTemplate.getForObject(Mockito.any(), ArgumentMatchers.eq(Boolean.class))).thenReturn(false);
			camp.setAmbitoCampione(AmbitoCampione.ZOOTECNIA);
			camp.setCampioneStatistico(CampioneStatistico.RISCHIO);
			break;
		case "189624":
		case "183546":
		case "184017_27":
		case "190837_27":
		case "183606_27":
			Mockito.when(restTemplate.getForObject(Mockito.any(), ArgumentMatchers.eq(Boolean.class))).thenReturn(true);
			camp.setAmbitoCampione(AmbitoCampione.SUPERFICIE);
			camp.setCampioneStatistico(CampioneStatistico.RISCHIO);
			break;
		case "185977":
		case "184437_27":
			Mockito.when(restTemplate.getForObject(Mockito.any(), ArgumentMatchers.eq(Boolean.class))).thenReturn(false);
			camp.setAmbitoCampione(AmbitoCampione.SUPERFICIE);
			camp.setCampioneStatistico(CampioneStatistico.RISCHIO);
			break;
		case "209576":{
			Mockito.when(restTemplate.getForObject(Mockito.any(), ArgumentMatchers.eq(Boolean.class))).thenReturn(false);
			camp.setAnnoCampagna(2019);
			camp.setAmbitoCampione(AmbitoCampione.SUPERFICIE);
			camp.setCampioneStatistico(CampioneStatistico.CASUALE);
			//all'interno del controllo C557 
			Mockito.when(disaccoppiatoService.isCampione(Mockito.any())).thenReturn(true);
		}
		case "214069" : {
			
			Mockito.when(restTemplate.getForObject(Mockito.any(), ArgumentMatchers.eq(Boolean.class))).thenReturn(false);
			camp.setAnnoCampagna(2019);
			camp.setAmbitoCampione(AmbitoCampione.SUPERFICIE);
			camp.setCampioneStatistico(CampioneStatistico.CASUALE);
			//all'interno del controllo C557 
			Mockito.when(disaccoppiatoService.isCampione(Mockito.any())).thenReturn(true);
			
			//sovrascrive quella del setup iniziale
			ConfIstruttoriaRicevibilitaDto conf=new ConfIstruttoriaRicevibilitaDto();
			conf.setDataRicevibilita(LocalDate.of(2019, Month.AUGUST, 19));
			conf.setDataScadenzaDomandaInizialeInRitardo(LocalDate.of(2019, Month.JULY, 12));
			conf.setDataScadenzaDomandaRitiroParziale(LocalDate.of(2020, Month.MAY, 15));
			Mockito.when(configurazioneIstruttoriaService.getConfIstruttoriaRicevibilita(2019))
				.thenReturn(conf);
		}
			
		default:
			break;
		}
		listCamp.add(camp);
		Mockito.when(campioneDao.findByCuaaAndAnnoCampagna(Mockito.any(), Mockito.any())).thenReturn(listCamp);
	}
}
