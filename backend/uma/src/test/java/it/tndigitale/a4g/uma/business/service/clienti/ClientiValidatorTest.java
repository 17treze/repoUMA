package it.tndigitale.a4g.uma.business.service.clienti;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.transaction.annotation.Transactional;

//import it.tndigitale.a4g.fascicolo.anagrafica.client.model.MovimentoValidazioneFascicoloAgsDto;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.uma.business.service.client.UmaAnagraficaClient;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente")
class ClientiValidatorTest {
	
	private static final String BR2_UMA_03_04 = "Il CUAA selezionato non ha un fascicolo aziendale valido nell’anno precedente";
	private static final String UMA_03_04_STESSO_CLIENTE = "Non è possibile importare la propria azienda";
	private static final String UMA_03_04_ESISTE_CLIENTE = "Il CUAA selezionato è già stato inserito!";

	@Autowired
	private ClientiValidator validator;
	
//	@MockBean
//	private UmaAnagraficaClient anagraficaClient;
	
	@Autowired
	private Clock clock;
	/*
	@Transactional
	@Test
	void validaDichiarazioneConsumiClienteErrorNotFound() throws Exception {
		MovimentoValidazioneFascicoloAgsDto movFas = new MovimentoValidazioneFascicoloAgsDto();
		movFas.setCuaa("DPDNDR77B03L378L");
		
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			validator.validaDichiarazioneConsumiCliente(7762L, movFas);
		}); 

		assertEquals("Non è stato possibile reperire la dichiarazione consumi con id: 7762", exception.getMessage());
	}
	
	@Transactional
	@Test
	@Sql(scripts = "/sql/consumi/clienti/clienti_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/clienti/clienti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void validaDichiarazioneConsumiClienteStessoCliente() throws Exception {
		MovimentoValidazioneFascicoloAgsDto movFas = new MovimentoValidazioneFascicoloAgsDto();
		movFas.setCuaa("ZMBRCR96D11L174G");
		
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			validator.validaDichiarazioneConsumiCliente(7761L, movFas);
		}); 

		assertEquals(UMA_03_04_STESSO_CLIENTE, exception.getMessage());
	}
	
	@Transactional
	@Test
	@Sql(scripts = "/sql/consumi/clienti/clienti_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/clienti/clienti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void validaDichiarazioneConsumiClienteErrorEsisteCliente() throws Exception {
		MovimentoValidazioneFascicoloAgsDto movFas = new MovimentoValidazioneFascicoloAgsDto();
		movFas.setCuaa("BBBFBA66E31F187R");
		
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			validator.validaDichiarazioneConsumiCliente(7761L, movFas);
		}); 

		assertEquals(UMA_03_04_ESISTE_CLIENTE, exception.getMessage());
	}
	
	@Test
	void validaFascicoloClienteErrorNotFound() throws Exception {
		
		Mockito.when(anagraficaClient.getMovimentazioniValidazioneFascicolo(Mockito.any(), Mockito.any())).thenReturn(null);
		
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			validator.validaFascicoloCliente(1L, clock.now().getYear() - 1);
		}); 
		assertEquals("Non è stato possibile reperire i dati del fascicolo con id: 1", exception.getMessage());
		
	}
	
	@Test
	void validaFascicoloClienteErrorFascicoloNonValido() throws Exception {
		MovimentoValidazioneFascicoloAgsDto movFas = new MovimentoValidazioneFascicoloAgsDto();
		movFas.setDataUltimaValidazionePositiva(null);
		Mockito.when(anagraficaClient.getMovimentazioniValidazioneFascicolo(Mockito.any(), Mockito.any())).thenReturn(movFas);
		
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			validator.validaFascicoloCliente(1L, clock.now().getYear() - 1);
		}); 

		assertEquals(BR2_UMA_03_04, exception.getMessage());
	}
	*/
	
}
