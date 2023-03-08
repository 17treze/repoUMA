package it.tndigitale.a4gistruttoria.action.acz;

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
public class CalcoloOutputImportoRiduzioneRitardoConsumerTest {
	
	@Autowired
	private IstruttoriaComponent istruttoriaComponent;
	
	@Autowired
	private CalcoloOutputImportoRiduzioneRitardoConsumer calcoloOutputImportoRiduzioneRitardoConsumer;

	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/zootecnia/percentuale_riduzione_domande_modifica_ritardo_applicataistruttore_test_annulla_riduzione.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/zootecnia/percentuale_riduzione_domande_modifica_ritardo_applicataistruttore_test_annulla_riduzione_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void percentuale_riduzione_domande_modifica_ritardo_applicataistruttore_test_calcolo_se_percritistr_true_allora_no_riduzione() {
		IstruttoriaModel istruttoria = istruttoriaComponent.load(144144145L);
		CalcoloAccoppiatoHandler handler = new CalcoloAccoppiatoHandler();
		handler.getVariabiliInput().add(new VariabileCalcolo(TipoVariabile.PERCRITISTR, true));
		
		calcoloOutputImportoRiduzioneRitardoConsumer.accept(handler, istruttoria);
		
		MapVariabili outputListaVariabiliCalcolo = handler.getVariabiliOutput();
		assertThat(outputListaVariabiliCalcolo.get(TipoVariabile.ACZIMPRIDRIT_310)).isNotNull();
		assertThat(outputListaVariabiliCalcolo.get(TipoVariabile.ACZIMPRIDRIT_310).getValNumber().doubleValue()).isEqualTo(valueOf(0.0));
	}
	
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/zootecnia/percentuale_riduzione_domande_modifica_ritardo_applicataistruttore_test_annulla_riduzione.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/zootecnia/percentuale_riduzione_domande_modifica_ritardo_applicataistruttore_test_annulla_riduzione_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void percentuale_riduzione_domande_modifica_ritardo_applicataistruttore_test_calcolo_percritistr_false() {
		IstruttoriaModel istruttoria = istruttoriaComponent.load(144144145L);
		CalcoloAccoppiatoHandler handler = new CalcoloAccoppiatoHandler();
		handler.getVariabiliInput().add(new VariabileCalcolo(TipoVariabile.PERCRITISTR, false));
		handler.getVariabiliInput().add(new VariabileCalcolo(TipoVariabile.PERCRIT, new BigDecimal(.5)));
				
		handler.getVariabiliOutput().add(new VariabileCalcolo(TipoVariabile.ACZIMPRIDSANZ_310, new BigDecimal(10)));
		handler.getVariabiliOutput().add(new VariabileCalcolo(TipoVariabile.ACZIMPACC_310, new BigDecimal(20)));
		calcoloOutputImportoRiduzioneRitardoConsumer.accept(handler, istruttoria);
		
		MapVariabili outputListaVariabiliCalcolo = handler.getVariabiliOutput();
		assertThat(outputListaVariabiliCalcolo.get(TipoVariabile.ACZIMPRIDRIT_310)).isNotNull();
		assertThat(outputListaVariabiliCalcolo.get(TipoVariabile.ACZIMPRIDRIT_310).getValNumber().doubleValue()).isEqualTo(valueOf(5.0));
	}
}
