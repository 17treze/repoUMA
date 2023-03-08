package it.tndigitale.a4gistruttoria;

import static org.awaitility.Awaitility.await;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.dto.StatisticaFilter;
import it.tndigitale.a4gistruttoria.repository.dao.ProcessoDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gtProcesso;
import it.tndigitale.a4gistruttoria.repository.model.StatoProcesso;
import it.tndigitale.a4gistruttoria.repository.model.TipoProcesso;
import it.tndigitale.a4gistruttoria.util.TipologiaStatistica;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente"/*, roles = { "a4gistruttoria.pac.domandaUnica.visualizza", "a4gistruttoria.pac.domandaUnica.edita" }*/)
// @AutoConfigureTestDatabase
@Ignore("Test momentaneamente ignorati perché la generazione dei dati statistici crea confusione nei log; inoltre viene solamente testato l'avvio del processo e non la generazione dei dati.")
public class StatisticheApplicationTest {
	
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ProcessoDao daoProcesso;


	@Transactional
	@Test
	public void avviaStatistica_CS21() throws Exception {
		// L'avvio al momento fallisce: gestire inserimento dati domanda integrativa per sostegno zootecnia
		// CustomThreadLocal.addVariable(A4gIstruttoriaConstants.HEADER_CF, "test");
		Integer campagna = 2018;
		
		StatisticaFilter avvioStatisticheFilter = new StatisticaFilter();
		// avvioStatisticheFilter.setTipologiaStatistica(TipoProcesso.STATISTICA_CS21.getTipoProcesso());
		avvioStatisticheFilter.setTipologiaStatistica(TipologiaStatistica.CS21);
		avvioStatisticheFilter.setCampagna(campagna);
		mockMvc.perform(post("/api/v1/statistiche").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(avvioStatisticheFilter))).andExpect(status().isOk());
		
		await().atMost(2, TimeUnit.MINUTES).until(processoInEsecuzione(TipoProcesso.STATISTICA_CS21));
	}
	
	@Transactional
	@Test(expected = NestedServletException.class)
	public void avviaStatisticaInEsecuzione_CS21() throws Exception {
		A4gtProcesso a4gtProcessoCS21 = new A4gtProcesso();
		Integer campagna = 2018;
		a4gtProcessoCS21.setStato(StatoProcesso.RUN); // 104 stato PROCESSO_RUN
		a4gtProcessoCS21.setTipo(TipoProcesso.STATISTICA_CS21);
		a4gtProcessoCS21.setDtInizio(new Date());
		a4gtProcessoCS21.setPercentualeAvanzamento(new BigDecimal(0));
		daoProcesso.save(a4gtProcessoCS21);
		
		StatisticaFilter avvioStatisticheFilter = new StatisticaFilter();
		// avvioStatisticheFilter.setTipologiaStatistica(TipoProcesso.STATISTICA_CS21.getTipoProcesso());
		avvioStatisticheFilter.setTipologiaStatistica(TipologiaStatistica.CS21);
		avvioStatisticheFilter.setCampagna(campagna);
		mockMvc.perform(post("/api/v1/statistiche").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(avvioStatisticheFilter))).andExpect(status().isOk());
		
		await().atMost(10, TimeUnit.SECONDS).until(processoInEsecuzione(TipoProcesso.STATISTICA_CS21));
	}
	
	private Callable<Boolean> processoInEsecuzione(TipoProcesso tipoProcesso) {
		return new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				A4gtProcesso a4gtProcesso = new A4gtProcesso();
				a4gtProcesso.setStato(StatoProcesso.OK);
				a4gtProcesso.setTipo(tipoProcesso);
				return daoProcesso.findOne(Example.of(a4gtProcesso)).isPresent();
			}
		};
	}
}
