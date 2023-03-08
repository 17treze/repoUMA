package it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.Month;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import it.tndigitale.a4g.fascicolo.anagrafica.api.ApiUrls;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.CaaService;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaUtenteClient;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.caa.CentroAssistenzaAgricolaDto;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.utente.client.model.RappresentaIlModelloPerRappresentareUnUtenteDelSistema;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente")
class VerificaRevocaMandatoTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private MandatoService mandatoService;
	@MockBean
	private Clock clock;
	@MockBean
	private CaaService caaService;
	@MockBean
	private AnagraficaUtenteClient anagraficaUtenteClient;

	@Test
	@Sql(scripts = "/sql/fascicolo/mandato/checkDatiRevocaMandato_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/mandato/checkDatiRevocaMandato_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void checkDatiRevocaSuccessful() throws Exception {
		// MOCK
		LocalDate settembre22_2020 = LocalDate.of(2020, Month.SEPTEMBER, 22);
		Mockito.doReturn(settembre22_2020).when(clock).today();

		CentroAssistenzaAgricolaDto caa = new CentroAssistenzaAgricolaDto();

		caa.setId(696L);
		Mockito.when(caaService.getCentroAssistenzaAgricoloUtenteConnesso()).thenReturn(caa);

		this.mockMvc.perform(get(ApiUrls.MANDATO.concat("/DPDNDR77B03L378L/verifica/revoca-mandato")).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().is2xxSuccessful());
	}

	@Test
	@Sql(scripts = "/sql/fascicolo/mandato/checkDatiRevocaMandato_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/mandato/checkDatiRevocaMandato_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void checkDatiRevocaFinestraTemporaleNonValida() throws Exception {
		// MOCK
		LocalDate dicembre22_2020 = LocalDate.of(2020, Month.DECEMBER, 22);
		Mockito.doReturn(dicembre22_2020).when(clock).today();

		Exception exception = Assertions.assertThrows(NestedServletException.class, () -> {
			this.mockMvc.perform(get(ApiUrls.MANDATO.concat("/DPDNDR77B03L378L/verifica/revoca-mandato")).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is5xxServerError());
		});
		assertTrue(exception.getMessage().contains(String.format("FINESTRA_TEMPORALE_NON_VALIDA")));
	}

	@Test
	@Sql(scripts = "/sql/fascicolo/mandato/checkDatiRevocaMandato_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/mandato/checkDatiRevocaMandato_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void checkDatiRevocaFascicoloNonEsistente() throws Exception {
		// MOCK
		LocalDate settembre22_2020 = LocalDate.of(2020, Month.SEPTEMBER, 22);
		Mockito.doReturn(settembre22_2020).when(clock).today();

		Exception exception = Assertions.assertThrows(NestedServletException.class, () -> {
			this.mockMvc.perform(get(ApiUrls.MANDATO.concat("/MSTFBA79L10H612L/verifica/revoca-mandato")).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is5xxServerError());
		});
		assertTrue(exception.getMessage().contains(String.format("FASCICOLO_LOCALE_NON_ESISTENTE")));
	}

	@Test
	@Sql(scripts = "/sql/fascicolo/mandato/checkDatiRevocaMandato_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/mandato/checkDatiRevocaMandato_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void checkDatiRevocaCaaRevocanteUgualeRevocato() throws Exception {
		// MOCK
		LocalDate settembre22_2020 = LocalDate.of(2020, Month.SEPTEMBER, 22);
		Mockito.doReturn(settembre22_2020).when(clock).today();

		CentroAssistenzaAgricolaDto caa = new CentroAssistenzaAgricolaDto();

		caa.setId(693L);
		Mockito.when(caaService.getCentroAssistenzaAgricoloUtenteConnesso()).thenReturn(caa);

		Exception exception = Assertions.assertThrows(NestedServletException.class, () -> {
			this.mockMvc.perform(get(ApiUrls.MANDATO.concat("/DPDNDR77B03L378L/verifica/revoca-mandato")).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is5xxServerError());
		});
		assertTrue(exception.getMessage().contains(String.format("CAA_REVOCANTE_UGUALE_REVOCATO")));
	}

	@Test
	@Sql(scripts = "/sql/fascicolo/mandato/checkDatiRevocaMandato_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/mandato/checkDatiRevocaMandato_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void checkDatiRevocaErroreGenericoChiamataUtente() throws Exception {
		// MOCK
		LocalDate settembre22_2020 = LocalDate.of(2020, Month.SEPTEMBER, 22);
		Mockito.doReturn(settembre22_2020).when(clock).today();

		RappresentaIlModelloPerRappresentareUnUtenteDelSistema user = new RappresentaIlModelloPerRappresentareUnUtenteDelSistema();
		user.setIdentificativo("XYZNDR77B03L378L");
		user.setCodiceFiscale("XYZNDR77B03L378L");
		Mockito.when(anagraficaUtenteClient.getUtenteConnesso()).thenReturn(user);
		
		Exception exception = Assertions.assertThrows(NestedServletException.class, () -> {
			this.mockMvc.perform(get(ApiUrls.MANDATO.concat("/DPDNDR77B03L378L/verifica/revoca-mandato")).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is5xxServerError());
		});
		assertTrue(exception.getMessage().contains(String.format("ERRORE_REPERIMENTO_CAA_CONNESSO")));
	}

	@Test
	@Sql(scripts = "/sql/fascicolo/mandato/verificaPresenzaRevocaOrdinariaOKTest_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/mandato/verificaPresenzaRevocaOrdinariaOKTest_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void verificaPresenzaRevocaOrdinaria_OK() throws Exception {
		LocalDate settembre22_2020 = LocalDate.of(2020, Month.SEPTEMBER, 22);
		Mockito.doReturn(settembre22_2020).when(clock).today();
		assertTrue(mandatoService.verificaPresenzaRevocaOrdinaria("LRCPLA50M11H612B"));
	}

	@Test
	@Sql(scripts = "/sql/fascicolo/mandato/verificaPresenzaRevocaOrdinariaKOTest_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/mandato/verificaPresenzaRevocaOrdinariaKOTest_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void verificaPresenzaRevocaOrdinaria_KO() throws Exception {
		LocalDate settembre22_2020 = LocalDate.of(2020, Month.SEPTEMBER, 22);
		Mockito.doReturn(settembre22_2020).when(clock).today();
		assertTrue(!mandatoService.verificaPresenzaRevocaOrdinaria("LRCPLA50M11H612B"));
	}
}
