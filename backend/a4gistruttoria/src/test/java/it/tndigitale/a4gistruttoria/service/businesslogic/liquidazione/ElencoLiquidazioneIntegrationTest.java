package it.tndigitale.a4gistruttoria.service.businesslogic.liquidazione;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElencoLiquidazioneException;
import it.tndigitale.a4gistruttoria.util.EmailUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.visualizza", "a4gistruttoria.pac.domandaUnica.edita" })
// @AutoConfigureTestDatabase
public class ElencoLiquidazioneIntegrationTest {
	
	@Autowired
	private ElencoLiquidazioneService elencoLiquidazioneService;
	
	@MockBean
	private InvioTracciatoSOCComponent invioTracciatoComponent;
	@MockBean
	private EmailUtils emailService;
	
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/elencoLiquidazione/invioElenco_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/elencoLiquidazione/invioElenco_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void inviaElencoAnticipo() throws Exception {
		doNothing().when(emailService).sendMessageWithAttachment(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
		
		try {
			elencoLiquidazioneService.inviaElenco(2019, Sostegno.DISACCOPPIATO, 11202559L);
		} catch (ElencoLiquidazioneException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		verify(invioTracciatoComponent).inviaTracciato(Mockito.any(), ArgumentMatchers.endsWith("02.txt"));
	}
	
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/elencoLiquidazione/invioElenco_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/elencoLiquidazione/invioElenco_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void inviaElencoSaldo() throws Exception {
		doNothing().when(emailService).sendMessageWithAttachment(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
		
		try {
			elencoLiquidazioneService.inviaElenco(2019, Sostegno.SUPERFICIE, 1842876L);
		} catch (ElencoLiquidazioneException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		verify(invioTracciatoComponent).inviaTracciato(Mockito.any(), ArgumentMatchers.endsWith("04.txt"));
	}
	
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/elencoLiquidazione/invioElenco_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/elencoLiquidazione/invioElenco_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void inviaElencoIntegrazione() throws Exception {
		doNothing().when(emailService).sendMessageWithAttachment(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
		
		try {
			elencoLiquidazioneService.inviaElenco(2019, Sostegno.DISACCOPPIATO, 8550588L);
		} catch (ElencoLiquidazioneException e) {
			e.printStackTrace();
		}
		
		verify(invioTracciatoComponent).inviaTracciato(Mockito.any(), ArgumentMatchers.endsWith("05.txt"));
	}

}
