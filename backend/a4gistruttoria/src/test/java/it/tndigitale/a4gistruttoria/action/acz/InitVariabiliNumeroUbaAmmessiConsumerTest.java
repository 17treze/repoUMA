package it.tndigitale.a4gistruttoria.action.acz;

import static java.lang.Double.valueOf;
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
public class InitVariabiliNumeroUbaAmmessiConsumerTest {

	@Autowired
	private IstruttoriaComponent istruttoriaComponent;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private RestTemplate restTemplate;
	
	@Autowired
	private InitVariabiliNumeroUbaAmmessiConsumer ubaAmmessiConsumer;


	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/zootecnia/verificaCalcoloUbaAmmessi_ubaLat3_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/zootecnia/verificaCalcoloUbaAmmessi_ubaLat3_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void verificaCalcoloUbaAmmessi_ubaLat3() throws Exception {
		IstruttoriaModel istruttoria = istruttoriaComponent.load(4449871234L);
		CalcoloAccoppiatoHandler handler = new CalcoloAccoppiatoHandler();

		ubaAmmessiConsumer.accept(handler, istruttoria);

		MapVariabili inputListaVariabiliCalcolo = handler.getVariabiliInput();
		assertThat(inputListaVariabiliCalcolo.get(TipoVariabile.ACZUBA_LAT)).isNotNull();
		assertThat(inputListaVariabiliCalcolo.get(TipoVariabile.ACZUBA_MAC)).isNotNull();
		assertThat(inputListaVariabiliCalcolo.get(TipoVariabile.ACZUBA_OVI)).isNotNull();
		assertThat(inputListaVariabiliCalcolo.get(TipoVariabile.ACZUBATOT)).isNotNull();
		VariabileCalcolo vc = inputListaVariabiliCalcolo.get(TipoVariabile.ACZUBATOT);
		assertThat(valueOf(3.0)).isEqualTo(vc.getValNumber().doubleValue());
		vc = inputListaVariabiliCalcolo.get(TipoVariabile.ACZUBA_LAT);
		assertThat(valueOf(3.0)).isEqualTo(vc.getValNumber().doubleValue());
	}

}
