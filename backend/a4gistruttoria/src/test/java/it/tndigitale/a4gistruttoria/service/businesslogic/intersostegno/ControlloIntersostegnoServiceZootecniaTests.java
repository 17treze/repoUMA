package it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.transaction.Transactional;

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

import it.tndigitale.a4g.framework.time.Clock;
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
// @AutoConfigureTestDatabase
public class ControlloIntersostegnoServiceZootecniaTests extends ControlloIntersostegnoServiceBaseTest {

	public static final int ANNO_CAMPAGNA = 2019;

	@Autowired
	private ControlloIntersostegnoService service;
	
	@Autowired
	private IstruttoriaDao istruttoriaDao;

	@Autowired
	private DomandaUnicaDao daoDomanda;

	@MockBean
	private Clock clock;

	public void mockDate() throws Exception {
	   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	    Date NOW = sdf.parse("2020-03-02 00:00:00");
        
        Mockito.when(clock.nowDate()).thenReturn(NOW);
    }
	
	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_031_soloACZ_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_031_soloACZ_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_031_soloACZ() throws Exception {
		String cuaa = "DLCSVR70S20B006-";

		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna(cuaa, ANNO_CAMPAGNA);

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.ZOOTECNIA, TipoIstruttoria.SALDO);

		mockAntimafia(domanda);
		
		service.elabora(istruttoria.getId());
		
		checkStatoSostegno(istruttoria, StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK);
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO, "OK", CodiceEsito.DUT_042.getCodiceEsito());
//		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_IMPORTO_ANTIMAFIA, "KO", CodiceEsito.DUT_062.getCodiceEsito());
		checkPasso(istruttoria, TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA, "OK", null);
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_INTERSOSTEGNO, "OK", CodiceEsito.DUF_033.getCodiceEsito());
		
		checkPassoValori(istruttoria, TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA);
	}
	
	
	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_031_soloACZ_valori_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_031_soloACZ_valori_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_031_soloACZ_valori() throws Exception {
		String cuaa = "DLCSVR70S20B005-";

		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna(cuaa,ANNO_CAMPAGNA);

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.ZOOTECNIA, TipoIstruttoria.SALDO);

		mockAntimafia(domanda);
		
		service.elabora(istruttoria.getId());
		
		checkStatoSostegno(istruttoria, StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK);
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO, "OK", CodiceEsito.DUT_042.getCodiceEsito());
		checkPasso(istruttoria, TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA, "OK", null);
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_INTERSOSTEGNO, "OK", CodiceEsito.DUF_033.getCodiceEsito());
		
		checkPassoValori(istruttoria, TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA);
	}

	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_031_ACZ_DIS_PAGAUT_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_031_ACZ_DIS_PAGAUT_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_031_ACZ_DIS_PAGAUT() throws Exception {
		String cuaa = "02413290228X";

		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna(cuaa,ANNO_CAMPAGNA);

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.ZOOTECNIA, TipoIstruttoria.SALDO);

		mockAntimafia(domanda);
		
		service.elabora(istruttoria.getId());
		
		checkStatoSostegno(istruttoria, StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK);
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO, "OK", CodiceEsito.DUT_042.getCodiceEsito());
//		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_IMPORTO_ANTIMAFIA, "KO", CodiceEsito.DUT_062.getCodiceEsito());
		checkPasso(istruttoria, TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA, "OK", null);
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_INTERSOSTEGNO, "OK", CodiceEsito.DUF_031.getCodiceEsito());
		
		checkPassoValori(istruttoria, TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA);
	}
	
	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_031_ACZ_DIS_PAGAUT_antimafiaistruttore_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_031_ACZ_DIS_PAGAUT_antimafiaistruttore_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_031_ACZ_antimafiaistruttore_DIS_PAGAUT() throws Exception {
		String cuaa = "02413290228Y";

		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna(cuaa,ANNO_CAMPAGNA);

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.ZOOTECNIA, TipoIstruttoria.SALDO);

		mockAntimafia(domanda);
		
		service.elabora(istruttoria.getId());
		
		checkStatoSostegno(istruttoria, StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK);
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO, "OK", CodiceEsito.DUT_042.getCodiceEsito());
//		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_IMPORTO_ANTIMAFIA, "KO", CodiceEsito.DUT_062.getCodiceEsito());
		checkPasso(istruttoria, TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA, "OK", null);
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_INTERSOSTEGNO, "OK", CodiceEsito.DUF_031.getCodiceEsito());
		
		checkPassoValori(istruttoria, TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA);
	}

	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_030_ACZ_DIS_PAGAUT_noantimafiaistruttore_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_030_ACZ_DIS_PAGAUT_noantimafiaistruttore_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_030_ACZ_noantimafiaistruttore_DIS_PAGAUT() throws Exception {
		String cuaa = "02413290228Z";

		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna(cuaa,ANNO_CAMPAGNA);

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.ZOOTECNIA, TipoIstruttoria.SALDO);

		mockAntimafia(domanda);
		
		service.elabora(istruttoria.getId());
		
		checkStatoSostegno(istruttoria, StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK);
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO, "OK", CodiceEsito.DUT_042.getCodiceEsito());
		//checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_IMPORTO_ANTIMAFIA, "KO", CodiceEsito.DUT_058.getCodiceEsito());
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_INTERSOSTEGNO, "OK", CodiceEsito.DUF_031.getCodiceEsito());
	}
	
	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_031_ACZ_DIS_PAG_AUT_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/intersostegno/duf_031_ACZ_DIS_PAG_AUT_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_031_acz_dis_pag_aut() throws Exception {
		//fatto per la correttiva #127
		String cuaa = "02413290228";

		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna(cuaa,ANNO_CAMPAGNA);

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.ZOOTECNIA, TipoIstruttoria.SALDO);

		mockAntimafia(domanda);
		
		service.elabora(istruttoria.getId());
		
		checkStatoSostegno(istruttoria, StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK);
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO, "OK", CodiceEsito.DUT_042.getCodiceEsito());
		checkPasso(istruttoria, TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA, "OK", null);
		checkPasso(istruttoria, TipologiaPassoTransizione.CONTROLLO_INTERSOSTEGNO, "OK", CodiceEsito.DUF_031.getCodiceEsito());
		
	}
	
	
	@Override
	protected void mockAntimafia(DomandaUnicaModel domanda) throws Exception {
		mockDate();
		super.mockAntimafia(domanda);
	}

	@Override
	protected Sostegno getIdentificativoSostegno() {
		return Sostegno.ZOOTECNIA;
	}

}
