package it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;
import it.tndigitale.a4gistruttoria.util.CodiceEsito;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ControlloIntersostegnoServiceSuperficieTests extends ControlloIntersostegnoServiceBaseTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private IstruttoriaDao istruttoriaDao;
	
	@Autowired
	private ControlloIntersostegnoService service;

	@Autowired
	private DomandaUnicaDao daoDomanda;

	
	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_031_soloACS_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_031_soloACS_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_031_soloACS() throws Exception {
		String cuaa = "01801280221";
		Integer campagna = 2018;
		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna(cuaa,campagna);
		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);

		mockAntimafia(domanda);
		
		service.elabora(istruttoria.getId());
		
		checkStatoSostegno(istruttoria, StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK);
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO, "OK", CodiceEsito.DUT_042.getCodiceEsito());
		checkPasso(istruttoria, TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA, "OK", null);
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_INTERSOSTEGNO, "OK", CodiceEsito.DUF_031.getCodiceEsito());
		
		checkPassoValori(istruttoria, TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA);
	}

	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_033_soloACS_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_033_soloACS_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_033_soloACS() throws Exception {
		String cuaa = "01801280221X";
		Integer campagna = 2018;
		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna(cuaa,campagna);

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);

		mockAntimafia(domanda);
		
		service.elabora(istruttoria.getId());
		
		checkStatoSostegno(istruttoria, StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK);
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO, "OK", CodiceEsito.DUT_042.getCodiceEsito());
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_IMPORTO_ANTIMAFIA, "OK", CodiceEsito.DUT_062.getCodiceEsito());
		checkPasso(istruttoria, TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA, "OK", null);
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_INTERSOSTEGNO, "OK", CodiceEsito.DUF_033.getCodiceEsito());
		
	}

	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_030_soloACS_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_030_soloACS_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_030_soloACS() throws Exception {
		String cuaa = "01801280221Y";
		Integer campagna = 2018;
		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna(cuaa,campagna);

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);

		mockAntimafia(domanda);
		
		service.elabora(istruttoria.getId());
		
		checkStatoSostegno(istruttoria, StatoIstruttoria.PAGAMENTO_NON_AUTORIZZATO);
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO, "OK", CodiceEsito.DUT_042.getCodiceEsito());
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_IMPORTO_ANTIMAFIA, "KO", CodiceEsito.DUT_061.getCodiceEsito());
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_INTERSOSTEGNO, "KO", CodiceEsito.DUF_030.getCodiceEsito());
	}

	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_035_ACS_DIS_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_035_ACS_DIS_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_035_ACS_DIS() throws Exception {
		String cuaa = "SGNNLN54T28A3721";
		Integer campagna = 2018;
		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna(cuaa,campagna);

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);

		mockAntimafia(domanda);
		
		service.elabora(istruttoria.getId());
		
		checkStatoSostegno(istruttoria, StatoIstruttoria.PAGAMENTO_NON_AUTORIZZATO);
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO, "KO", CodiceEsito.DUT_047.getCodiceEsito());
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_INTERSOSTEGNO, "KO", CodiceEsito.DUF_035.getCodiceEsito());
	}

	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_035_ACS_DIS_CALCOLI_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_035_ACS_DIS_CALCOLI_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_035_ACS_DIS_CALCOLI() throws Exception {
		String cuaa = "SGNNLN54T28A3722";
		Integer campagna = 2018;
		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna(cuaa,campagna);

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);

		mockAntimafia(domanda);
		
		service.elabora(istruttoria.getId());
		
		checkStatoSostegno(istruttoria, StatoIstruttoria.PAGAMENTO_NON_AUTORIZZATO);
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO, "KO", CodiceEsito.DUT_047.getCodiceEsito());
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_INTERSOSTEGNO, "KO", CodiceEsito.DUF_035.getCodiceEsito());
	}
	
	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_030_ACS_DIS_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_030_ACS_DIS_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_030_ACS_DIS() throws Exception {
		String cuaa = "SGNNLN54T28A3723";
		Integer campagna = 2018;
		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna(cuaa,campagna);

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);

		mockAntimafia(domanda);
		
		service.elabora(istruttoria.getId());
		
		checkStatoSostegno(istruttoria, StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK);
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO, "OK", CodiceEsito.DUT_042.getCodiceEsito());
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_IMPORTO_ANTIMAFIA, "OK", CodiceEsito.DUT_057.getCodiceEsito());
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_INTERSOSTEGNO, "OK", CodiceEsito.DUF_033.getCodiceEsito());
	}

	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_031_ACS_DIS_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_031_ACS_DIS_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_031_ACS_DIS() throws Exception {
		String cuaa = "SGNNLN54T28A3724";
		Integer campagna = 2018;
		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna(cuaa,campagna);

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);

		mockAntimafia(domanda);
		
		service.elabora(istruttoria.getId());
		
		checkStatoSostegno(istruttoria, StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK);
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO, "OK", CodiceEsito.DUT_042.getCodiceEsito());
		checkPasso(istruttoria, TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA, "OK", null);
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_INTERSOSTEGNO, "OK", CodiceEsito.DUF_031.getCodiceEsito());
	}

	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_030_ACS_DIS_antimafiaScaduta_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_030_ACS_DIS_antimafiaScaduta_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_030_ACS_DIS_antimafiaScaduta() throws Exception {
		String cuaa = "SGNNLN54T28A3725";
		Integer campagna = 2018;
		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna(cuaa,campagna);

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);

		mockAntimafia(domanda);
		
		service.elabora(istruttoria.getId());
		
		checkStatoSostegno(istruttoria, StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK);
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO, "OK", CodiceEsito.DUT_042.getCodiceEsito());
		//checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_IMPORTO_ANTIMAFIA, "KO", CodiceEsito.DUT_057.getCodiceEsito());
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_INTERSOSTEGNO, "OK", CodiceEsito.DUF_031.getCodiceEsito());
	}

	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_028_ACS_DIS_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_028_ACS_DIS_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_028_ACS_DIS() throws Exception {
		String cuaa = "SGNNLN54T28A3726";
		Integer campagna = 2018;
		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna(cuaa,campagna);

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);

		mockAntimafia(domanda);
		
		service.elabora(istruttoria.getId());
		
		checkStatoSostegno(istruttoria, StatoIstruttoria.NON_LIQUIDABILE);
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO, "KO", CodiceEsito.DUT_050.getCodiceEsito());
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_INTERSOSTEGNO, "KO", CodiceEsito.DUF_028.getCodiceEsito());
	}
	
	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_031_ACS_DIS_LIQ_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_031_ACS_DIS_LIQ_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_031_ACS_DIS_LIQ() throws Exception {
		String cuaa = "SGNNLN54T28A3727";
		Integer campagna = 2018;
		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna(cuaa,campagna);

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);

		mockAntimafia(domanda);
		
		service.elabora(istruttoria.getId());
		
		checkStatoSostegno(istruttoria, StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK);
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO, "OK", CodiceEsito.DUT_049.getCodiceEsito());
		checkPasso(istruttoria, TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA, "OK", null);
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_INTERSOSTEGNO, "OK", CodiceEsito.DUF_031.getCodiceEsito());
	}

	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_033_ACS_DIS_LIQ_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_033_ACS_DIS_LIQ_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_033_ACS_DIS_LIQ() throws Exception {
		String cuaa = "SGNNLN54T28A3728";
		Integer campagna = 2018;
		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna(cuaa,campagna);

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);

		mockAntimafia(domanda);
		
		service.elabora(istruttoria.getId());
		
		checkStatoSostegno(istruttoria, StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK);
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO, "OK", CodiceEsito.DUT_049.getCodiceEsito());
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_IMPORTO_ANTIMAFIA, "OK", CodiceEsito.DUT_059.getCodiceEsito());
		checkPasso(istruttoria, TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA, "OK", null);
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_INTERSOSTEGNO, "OK", CodiceEsito.DUF_033.getCodiceEsito());
	}
	
	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_030_ACS_DIS_LIQ_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_030_ACS_DIS_LIQ_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_030_ACS_DIS_LIQ() throws Exception {
		String cuaa = "SGNNLN54T28A3729";
		Integer campagna = 2018;
		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna(cuaa,campagna);

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);

		mockAntimafia(domanda);
		
		service.elabora(istruttoria.getId());
		
		checkStatoSostegno(istruttoria, StatoIstruttoria.PAGAMENTO_NON_AUTORIZZATO);
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO, "OK", CodiceEsito.DUT_049.getCodiceEsito());
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_IMPORTO_ANTIMAFIA, "KO", CodiceEsito.DUT_058.getCodiceEsito());
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_INTERSOSTEGNO, "KO", CodiceEsito.DUF_030.getCodiceEsito());
	}

	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_035_ACS_ACZ_CALOK_DIS_NON_AMM_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_035_ACS_ACZ_CALOK_DIS_NON_AMM_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_035_ACS_ACZ_CALOK_DIS_NON_AMM() throws Exception {
		String cuaa = "MSTFBA79L10H612L";
		Integer campagna = 2018;
		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna(cuaa,campagna);

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);

		mockAntimafia(domanda);
		
		service.elabora(istruttoria.getId());
		
		checkStatoSostegno(istruttoria, StatoIstruttoria.PAGAMENTO_NON_AUTORIZZATO);
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO, "KO", CodiceEsito.DUT_048.getCodiceEsito());
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_INTERSOSTEGNO, "KO", CodiceEsito.DUF_035.getCodiceEsito());
	}

	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_035_ACS_ACZ_CALOK_DIS_CALCKO_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_035_ACS_ACZ_CALOK_DIS_CALCKO_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_035_ACS_ACZ_CALOK_DIS_CALCKO() throws Exception {
		String cuaa = "MSTFBA79L10H612L";
		Integer campagna = 2018;
		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna(cuaa,campagna);

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);

		mockAntimafia(domanda);
		
		service.elabora(istruttoria.getId());
		
		checkStatoSostegno(istruttoria, StatoIstruttoria.PAGAMENTO_NON_AUTORIZZATO);
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO, "KO", CodiceEsito.DUT_046.getCodiceEsito());
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_INTERSOSTEGNO, "KO", CodiceEsito.DUF_035.getCodiceEsito());
	}
	
	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_033_ACS_ACZ_LIQ_DIS_NON_AMM_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_033_ACS_ACZ_LIQ_DIS_NON_AMM_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_033_ACS_ACZ_LIQ_DIS_NON_AMM() throws Exception {
		String cuaa = "MSTFBA79L10H612L";
		Integer campagna = 2018;
		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna(cuaa,campagna);

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);

		mockAntimafia(domanda);
		
		service.elabora(istruttoria.getId());
		
		checkStatoSostegno(istruttoria, StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK);
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO, "OK", CodiceEsito.DUT_044.getCodiceEsito());
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_IMPORTO_ANTIMAFIA, "OK", CodiceEsito.DUT_059.getCodiceEsito());
		checkPasso(istruttoria, TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA, "OK", null);
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_INTERSOSTEGNO, "OK", CodiceEsito.DUF_033.getCodiceEsito());
		
	}
	

	/**
	 * Istruttoria di anticipo disaccoppiato pagata
	 * Istruttoria di saldo disaccoppiato pagata
	 * 
	 */
	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_031_ACS_DIS_ANTPAGAUT_SALPAGAUT_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_031_ACS_DIS_ANTPAGAUT_SALPAGAUT_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_031_ACS_DIS_ANT_PAGAUT_DS_SAL_PAGAUT() throws Exception {
		String cuaa = "LNGNTN64B03H7641";
		Integer campagna = 2018;
		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna(cuaa,campagna);

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);

		mockAntimafia(domanda);
		
		service.elabora(istruttoria.getId());
		
		checkStatoSostegno(istruttoria, StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK);
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO, "OK", CodiceEsito.DUT_042.getCodiceEsito());
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_INTERSOSTEGNO, "OK", CodiceEsito.DUF_031.getCodiceEsito());

		checkPassoValori(istruttoria, TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA);
	}
	
	
	@Override
	protected Sostegno getIdentificativoSostegno() {
		return Sostegno.SUPERFICIE;
	}
}
