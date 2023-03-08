package it.tndigitale.a4gistruttoria.action.acz;

import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.action.IstruttoriaComponent;
import it.tndigitale.a4gistruttoria.dto.lavorazione.MapVariabili;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloAccoppiatoHandler;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class InitVariabiliCapiRichiestiConsumerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private RestTemplate restTemplate;
	
	@Autowired
	private InitVariabiliCapiRichiestiConsumer capiRichiestiConsumer;

	@Autowired
	private IstruttoriaComponent istruttoriaComponent;

	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/zootecnia/capiRichiestiDomandaIntegrativaBLLGPR66B27C372Y.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/zootecnia/deleteCapiRichiestiDomandaIntegrativaBLLGPR66B27C372Y.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void verificoCalcoloCapiRichiestiPiuInterventi() throws Exception {
		IstruttoriaModel istruttoria = istruttoriaComponent.load(19999999991L);
		CalcoloAccoppiatoHandler handler = new CalcoloAccoppiatoHandler();

		capiRichiestiConsumer.accept(handler, istruttoria);

		MapVariabili inputListaVariabiliCalcolo = handler.getVariabiliInput();
		assertThat(inputListaVariabiliCalcolo.get(TipoVariabile.ACZCAPIRIC_310)).isNull();
		assertThat(inputListaVariabiliCalcolo.get(TipoVariabile.ACZCAPIRIC_311)).isNull();
		assertThat(inputListaVariabiliCalcolo.get(TipoVariabile.ACZCAPIRIC_313)).isNull();
		assertThat(inputListaVariabiliCalcolo.get(TipoVariabile.ACZCAPIRIC_318)).isNull();
		assertThat(inputListaVariabiliCalcolo.get(TipoVariabile.ACZCAPIRIC_320)).isNull();
		assertThat(inputListaVariabiliCalcolo.get(TipoVariabile.ACZCAPIRIC_321)).isNull();
		assertThat(inputListaVariabiliCalcolo.get(TipoVariabile.ACZCAPIRIC_322)).isNull();
		VariabileCalcolo vc = inputListaVariabiliCalcolo.get(TipoVariabile.ACZCAPIRIC_315);
		assertThat(vc.getValNumber()).isEqualTo(valueOf(2L));
		vc = inputListaVariabiliCalcolo.get(TipoVariabile.ACZCAPIRIC_316);
		assertThat(vc.getValNumber()).isEqualTo(valueOf(4L));
	}
}
