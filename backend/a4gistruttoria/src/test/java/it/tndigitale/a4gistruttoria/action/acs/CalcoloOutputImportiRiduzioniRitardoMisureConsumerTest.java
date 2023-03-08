package it.tndigitale.a4gistruttoria.action.acs;

import static java.lang.Double.valueOf;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4gistruttoria.action.IstruttoriaComponent;
import it.tndigitale.a4gistruttoria.dto.lavorazione.MapVariabili;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloAccoppiatoHandler;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CalcoloOutputImportiRiduzioniRitardoMisureConsumerTest {
	
	@Autowired
	private IstruttoriaComponent istruttoriaComponent;
	
	@Autowired
	private CalcoloOutputImportiRiduzioniRitardoMisureConsumer calcoloOutputImportiRiduzioniRitardoMisureConsumer;
	
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/acs/percentuale_riduzione_domande_modifica_ritardo_applicataistruttore_test_annulla_riduzione.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/acs/percentuale_riduzione_domande_modifica_ritardo_applicataistruttore_test_annulla_riduzione_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void percentuale_riduzione_domande_modifica_ritardo_applicataistruttore_test_calcolo_se_percritistr_true_allora_no_riduzione() {
		IstruttoriaModel istruttoria = istruttoriaComponent.load(144144144L);
		CalcoloAccoppiatoHandler handler = new CalcoloAccoppiatoHandler();
		handler.getVariabiliInput().add(new VariabileCalcolo(TipoVariabile.PERCRITISTR, true));
		
		
		MapVariabili outputListaVariabiliCalcolo = handler.getVariabiliOutput();
		handler.getVariabiliOutput().add(new VariabileCalcolo(TipoVariabile.ACSIMPAMM_M8, new BigDecimal(10)));
		assertThat(outputListaVariabiliCalcolo.get(TipoVariabile.ACSIMPRIDRIT_M8)).isNull();
		calcoloOutputImportiRiduzioniRitardoMisureConsumer.accept(handler, istruttoria);
		
		assertThat(outputListaVariabiliCalcolo.get(TipoVariabile.ACSIMPRIDRIT_M8)).isNotNull();
		assertThat(outputListaVariabiliCalcolo.get(TipoVariabile.ACSIMPRIDRIT_M8).getValNumber().doubleValue()).isEqualTo(valueOf(0.0));
	}

}
