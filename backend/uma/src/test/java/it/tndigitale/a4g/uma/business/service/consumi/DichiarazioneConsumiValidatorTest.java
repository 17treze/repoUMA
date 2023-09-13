package it.tndigitale.a4g.uma.business.service.consumi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.NestedServletException;

import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.uma.Ruoli;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.service.client.UmaDotazioneTecnicaClient;
import it.tndigitale.a4g.uma.business.service.client.UmaTerritorioClient;
import it.tndigitale.a4g.uma.business.service.utente.AbilitazioniComponent;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente")
class DichiarazioneConsumiValidatorTest {

	private static final String BR2_UMA_03_01 = "Il sistema non ha trovato una Richiesta di carburante autorizzata!";
	private static final String BR3_UMA_03_01 = "La dichiarazione consumi è già stata protocollata!";
	private static final String BR4_UMA_03_01 = "Esiste già una dichiarazione consumi in fase di compilazione!";

	@Autowired
	private DichiarazioneConsumiValidator validator;
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private AbilitazioniComponent abilitazioniComponent;
	@MockBean
	private UmaTerritorioClient territorioClient;
	@MockBean
	private UmaDotazioneTecnicaClient dotazioneTecnicaClient;
	@SpyBean
	private UtenteComponent utenteComponent;
	
	@BeforeEach
	private void mockUtenteConnesso() throws Exception {
		when(utenteComponent.utenza()).thenReturn("utente");
		Mockito.when(abilitazioniComponent.checkRicercaDomandeUma()).thenReturn(true);
		when(utenteComponent.haRuolo(Ruoli.DOMANDE_UMA_RICERCA_UTENTE)).thenReturn(false);
		when(utenteComponent.haRuolo(Ruoli.DOMANDE_UMA_RICERCA_ENTE)).thenReturn(false);
		when(utenteComponent.haRuolo(Ruoli.DOMANDE_UMA_RICERCA_TUTTI)).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkModificaDichiarazioneConsumi(Mockito.anyLong())).thenReturn(true);
	}
	/*
	@Test
	@Sql(scripts = "/sql/consumi/consumi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consumi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void validaPresentazioneSuccessful() throws Exception {
		RichiestaCarburanteModel response = validator.validaPresentazione("FRLGPP67A01H330V");
		assertEquals(2021L, response.getCampagna());
		assertEquals("FRLGPP67A01H330V", response.getCuaa());
	}
	*/
	@Test
	@Sql(scripts = "/sql/consumi/consumi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consumi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void validaPresentazioneErrorBR2() throws Exception {
		Exception exception = assertThrows(NoSuchElementException.class, () -> {
			validator.validaPresentazione("CNTBRN95T13F839K");
		}); 

		assertEquals(BR2_UMA_03_01, exception.getMessage());
	}

	/*
	@Test
	@Transactional
	@Sql(scripts = "/sql/consumi/consumi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consumi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void validaPresentazioneErrorBR3() throws Exception {
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			validator.validaPresentazione("01464420221");
		}); 

		assertEquals(BR3_UMA_03_01, exception.getMessage());
	}

	@Test
	@Transactional
	@Sql(scripts = "/sql/consumi/consumi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consumi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void validaPresentazioneErrorBR4() throws Exception {
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			validator.validaPresentazione("ZMBRCR96D11L174G");
		}); 

		assertEquals(BR4_UMA_03_01, exception.getMessage());
	}
	*/
	@Test
	@Transactional
	@Sql(scripts = "/sql/consumi/consumi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consumi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postValidaDichiarazioneConsumi() throws Exception {
		Long id = 7763L;
//		Mockito.when(territorioClient.getColture(Mockito.anyString(), Mockito.any())).thenReturn(new ArrayList<>());
//		Mockito.when(dotazioneTecnicaClient.getFabbricati(Mockito.anyString(), Mockito.any())).thenReturn(new ArrayList<>());
		mockMvc.perform(post(String.format("/api/v1/consumi/%s/valida" , id))).andExpect(status().isOk());
	}

	@Test
	@Sql(scripts = "/sql/consumi/consumi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consumi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postValidaEsisteRichiestaRettificataInCompilazione() throws Exception {
		Long id = 7847L;
		Exception exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(post(String.format("/api/v1/consumi/%s/valida" , id)));
		}); 
		assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
		boolean contains = exception.getCause().getMessage().contains("Esiste una rettifica in compilazione per questa azienda, è necessario eliminarla per poter procedere");
		assertTrue(contains);
	}

	@Test
	@Sql(scripts = "/sql/consumi/consumi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consumi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postValidaDichiarazioneLegataARettifica() throws Exception {
		Long id = 7849L;
		Exception exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(post(String.format("/api/v1/consumi/%s/valida" , id)));
		}); 
		assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
		boolean contains = exception.getCause().getMessage().contains("La Richiesta Di Carburante è stata rettificata, è necessario eliminare la dichiarazione consumi");
		assertTrue(contains);
	}

}
