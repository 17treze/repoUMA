package it.tndigitale.a4gistruttoria.component.dis;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CalcoloImportoErogatoDisaccoppiatoComponentTest {

	@Autowired
	private CalcoloImportoErogatoDisaccoppiatoComponent calcoloErogato;
	@Autowired
	private DomandaUnicaDao daoDomanda;
	@MockBean
	private RestTemplate restTemplate;

	@Test
	@Transactional 
	@Sql(scripts = { "/DomandaUnica/calcoloDisaccoppiato/2035021_anticipoErogato_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/calcoloDisaccoppiato/2035021_anticipoErogato_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void calcolaImportoErogatoInAnticipo() throws Exception {
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal("1203502"));
		
		
		Map<TipoVariabile, BigDecimal> mappa = 
				calcoloErogato.calcolaSommePerIstruttoriePagate(domanda, 
				TipoVariabile.BPSIMPCALCFIN,
				TipoVariabile.GREIMPCALCFIN,
				TipoVariabile.GIOIMPCALCFIN);
		
		BigDecimal erogato = mappa.get(TipoVariabile.BPSIMPCALCFIN);
		assertThat(erogato).isNotNull();
		assertThat(erogato).isEqualByComparingTo(BigDecimal.valueOf(650.10));
		
		erogato = mappa.get(TipoVariabile.GREIMPCALCFIN);
		assertThat(erogato).isNotNull();
		assertThat(erogato).isEqualByComparingTo(BigDecimal.valueOf(337.53));
		
		erogato = mappa.get(TipoVariabile.GIOIMPCALCFIN);
		assertThat(erogato).isNull();
		
	}
}
