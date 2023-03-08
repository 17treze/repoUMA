package it.tndigitale.a4gistruttoria.action;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;

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

import it.tndigitale.a4gistruttoria.dto.lavorazione.MapVariabili;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloAccoppiatoHandler;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class InitVariabilePercentualeRitardataConsumerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private RestTemplate restTemplate;
	
	@Autowired
	private InitVariabilePercentualeRitardataConsumer service;
	
	@Autowired
	private DomandaUnicaDao daoDomanda;
	
	@Autowired
	private IstruttoriaComponent istruttoriaComponent;
	

	@Test
	public void calcolaGiorniLavorativiRitardoShouldReturn0() {
		LocalDate start = LocalDate.of(2019, 2, 1);
		LocalDate end = LocalDate.of(2018, 2, 1);
		BigDecimal result = service.calcolaGiorniLavorativiRitardo(start, end);
		assertEquals(0, result.intValue());
	}
	
	@Test
	public void calcolaGiorniLavorativiRitardoShouldReturn2() {
		LocalDate start = LocalDate.of(2019, 2, 5);
		LocalDate end = LocalDate.of(2019, 2, 7);
		BigDecimal result = service.calcolaGiorniLavorativiRitardo(start, end);
		assertEquals(2, result.intValue());
	}
	
	@Test
	public void calcolaGiorniLavorativiRitardoShouldExcludeWeekendAndHolidays() {
		LocalDate start = LocalDate.of(2019, 2, 1);
		LocalDate end = LocalDate.of(2019, 2, 7);
		BigDecimal result = service.calcolaGiorniLavorativiRitardo(start, end);
		assertEquals(4, result.intValue());
	}
	
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/zootecnia/percentuale_riduzione_domande_modifica_ritardo_applicataistruttore_test_annulla_riduzione.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/zootecnia/percentuale_riduzione_domande_modifica_ritardo_applicataistruttore_test_annulla_riduzione_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void percentuale_riduzione_domande_modifica_ritardo_applicataistruttore_test_annulla_riduzione() {
		IstruttoriaModel istruttoria = istruttoriaComponent.load(144144145L);
		CalcoloAccoppiatoHandler handler = new CalcoloAccoppiatoHandler();
		
		service.accept(handler, istruttoria);
		
		MapVariabili inputListaVariabiliCalcolo = handler.getVariabiliInput();
		assertThat(inputListaVariabiliCalcolo.get(TipoVariabile.PERCRITISTR)).isNotNull();
		assertEquals(Boolean.TRUE, inputListaVariabiliCalcolo.get(TipoVariabile.PERCRITISTR).getValBoolean());
	}
	
}
