package it.tndigitale.a4g.fascicolo.dotazionetecnica;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.ControlloCompletezzaModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.ControlloCompletezzaDao;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.service.ControlliFascicoloDotazioneTecnicaCompletoEnum;
import it.tndigitale.a4g.framework.event.store.EventStoreService;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente")
class ControlliCompletezzaTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ControlloCompletezzaDao controlloCompletezzaDao;

	// @MockBean
	// private ControlloCompletezzaDao controlloCompletezzaDaoMock;

	@SpyBean
	private EventStoreService eventStoreService;

	@Test
	@Sql(scripts = "/sql/controlliCompletezza/controlli_completezza_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/controlliCompletezza/controlli_completezza_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void startControlliCompletezzaDotazioneTecnicaOK() throws Exception {

		String cuaa = "PDRTTR69M30C794R";
		mockMvc.perform(put(ApiUrls.DOTAZIONE_TECNICA_PRIVATE + "/" + cuaa + "/start-controllo-completezza")).andExpect(status().isOk()).andReturn();

		Awaitility.await().atMost(5, TimeUnit.SECONDS).until(isProcessoConcluso(cuaa));

		ControlloCompletezzaModel macchinariConsistenti = controlloCompletezzaDao.findByCuaaAndTipoControllo(cuaa, ControlliFascicoloDotazioneTecnicaCompletoEnum.IS_MACCHINARI_CONSISTENTI.name())
				.orElseThrow();
		assertEquals(0, macchinariConsistenti.getEsito()); // esito ok

		ControlloCompletezzaModel fabbricatiConsistenti = controlloCompletezzaDao.findByCuaaAndTipoControllo(cuaa, ControlliFascicoloDotazioneTecnicaCompletoEnum.IS_FABBRICATI_CONSISTENTI.name())
				.orElseThrow();
		assertEquals(0, fabbricatiConsistenti.getEsito()); // esito ok

		// ControlloCompletezzaModel documentiPossessoMacchinari = controlloCompletezzaDao
		// .findByCuaaAndTipoControllo(cuaa, ControlliFascicoloDotazioneTecnicaCompletoEnum.IS_DOCUMENTI_POSSESSO_MACCHINARI_OK.name()).orElseThrow();
		// assertEquals(0, documentiPossessoMacchinari.getEsito()); // esito ok

	}

	@Test
	@Sql(scripts = "/sql/controlliCompletezza/controlli_completezza_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/controlliCompletezza/controlli_completezza_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void startControlliCompletezzaDotazioneTecnicaKO() throws Exception {

		String cuaa = "01720090111";
		mockMvc.perform(put(ApiUrls.DOTAZIONE_TECNICA_PRIVATE + "/" + cuaa + "/start-controllo-completezza")).andExpect(status().isOk()).andReturn();

		Awaitility.await().atMost(5, TimeUnit.SECONDS).until(isProcessoConcluso(cuaa));

		ControlloCompletezzaModel macchinariConsistenti = controlloCompletezzaDao.findByCuaaAndTipoControllo(cuaa, ControlliFascicoloDotazioneTecnicaCompletoEnum.IS_MACCHINARI_CONSISTENTI.name())
				.orElseThrow();
		assertEquals(-3, macchinariConsistenti.getEsito()); // esito KO

		ControlloCompletezzaModel fabbricatiConsistenti = controlloCompletezzaDao.findByCuaaAndTipoControllo(cuaa, ControlliFascicoloDotazioneTecnicaCompletoEnum.IS_FABBRICATI_CONSISTENTI.name())
				.orElseThrow();
		assertEquals(-3, fabbricatiConsistenti.getEsito()); // esito ok

		// ControlloCompletezzaModel documentiPossessoMacchinari = controlloCompletezzaDao
		// .findByCuaaAndTipoControllo(cuaa, ControlliFascicoloDotazioneTecnicaCompletoEnum.IS_DOCUMENTI_POSSESSO_MACCHINARI_OK.name()).orElseThrow();
		// assertEquals(-3, documentiPossessoMacchinari.getEsito()); // esito KO

	}

	private Callable<Boolean> isProcessoConcluso(String cuaa) {
		return () -> allEsitiCompilati(cuaa) || atLeastOneCallEventStoreTriggerRetry();
	}

	private Boolean allEsitiCompilati(String cuaa) {
		var esiti = controlloCompletezzaDao.findByCuaa(cuaa);
		if (CollectionUtils.isEmpty(esiti)) {
			return false;
		}
		return esiti.stream().allMatch(x -> x.getEsito() != null);
	}

	private Boolean atLeastOneCallEventStoreTriggerRetry() {
		try {
			verify(eventStoreService, times(1)).triggerRetry(Mockito.any(), Mockito.any());
			return true;
		} catch (AssertionError ae) {
			return false;
		}
	}

}
