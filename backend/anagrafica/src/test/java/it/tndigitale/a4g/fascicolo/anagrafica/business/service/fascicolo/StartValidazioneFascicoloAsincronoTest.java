package it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

//import it.tndigitale.a4g.fascicolo.anagrafica.business.event.StartValidazioneFascicoloEvent;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaUtenteClient;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato.MandatoService;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.SchedaValidazioneFascicoloDto;
import it.tndigitale.a4g.framework.event.store.EventStoreService;
import it.tndigitale.a4g.framework.event.store.handler.EventBus;
import it.tndigitale.a4g.utente.client.model.RappresentaIlModelloDelProfiloDiUnUtente;
import it.tndigitale.a4g.utente.client.model.RappresentaIlModelloPerRappresentareUnUtenteDelSistema;

@SpringBootTest
class StartValidazioneFascicoloAsincronoTest {
//	@MockBean private SincronizzazioneFascicoloService sincronizzazioneFascicoloService;
	@MockBean private FascicoloService fascicoloService;
	@MockBean private EventStoreService eventStoreService;
	@MockBean private ValidazioneFascicoloService validazioneFascicoloService;
	@MockBean private MandatoService mandatoService;
	@MockBean
	AnagraficaUtenteClient anagraficaUtenteClient;
	@Autowired private EventBus eventBus;
	
	@Autowired private PlatformTransactionManager transactionManager;
	private TransactionTemplate transactionTemplate;
	
//	@Test
//	@WithMockUser(username = "utente-azienda")
//	@Sql(scripts = "/sql/fascicolo/fascicolo_controllato_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
//	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
//	void invioEventoStartValidazione() throws Exception {
//		String cuaa = "XPDNDR77B03L378X";
//		Integer nextIdValidazione = 1;
//		userSetupAzienda(cuaa);
//		transactionTemplate = new TransactionTemplate(transactionManager);
//
//		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
//	        @Override
//	        protected void doInTransactionWithoutResult(TransactionStatus status) {
//
//	        	var sv = new SchedaValidazioneFascicoloDto();
//	    		sv.setCodiceFiscale(cuaa);
//	    		sv.setNextIdValidazione(nextIdValidazione);
//	        	StartValidazioneFascicoloEvent event = new StartValidazioneFascicoloEvent(sv);
//	    		eventBus.publishEvent(event);
//	        }
//	    });
//
//		verify(sincronizzazioneFascicoloService, timeout(3000)).invioEventoFineValidazione(Mockito.any(SchedaValidazioneFascicoloDto.class));
//	}
	
	void userSetupAzienda(String userCodiceFiscale) {
		RappresentaIlModelloPerRappresentareUnUtenteDelSistema user = new RappresentaIlModelloPerRappresentareUnUtenteDelSistema();
		user.setIdentificativo(userCodiceFiscale);
		user.setCodiceFiscale(userCodiceFiscale);
		user.setNome(userCodiceFiscale);
		List<RappresentaIlModelloDelProfiloDiUnUtente> profili = new ArrayList<>();
		RappresentaIlModelloDelProfiloDiUnUtente profilo = new RappresentaIlModelloDelProfiloDiUnUtente();
		profilo.setIdentificativo("azienda");
		profili.add(profilo);
		user.setProfili(profili);
		Mockito.when(anagraficaUtenteClient.getUtenteConnesso()).thenReturn(user);
	}
}
