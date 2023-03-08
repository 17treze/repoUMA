//package it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo;
//
//import static org.mockito.Mockito.timeout;
//import static org.mockito.Mockito.verify;
//
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import it.tndigitale.a4g.fascicolo.anagrafica.dto.SchedaValidazioneFascicoloDto;
//import it.tndigitale.a4g.framework.event.store.EventStoreService;
//
//@SpringBootTest
//class StartSincronizzazioneFascicoloAsincronoTest {
//	@Autowired private SincronizzazioneFascicoloService sincronizzazioneFascicoloService;
//	@MockBean private FascicoloService fascicoloService;
//	@MockBean private EventStoreService eventStoreService;
//
//	@Test
//	void invioEventoFineValidazione() throws Exception {
//		String cuaa = "DPDNDR77B03L378X";
//		Integer idValidazione = 0;
//		SchedaValidazioneFascicoloDto scheda = new SchedaValidazioneFascicoloDto();
//		scheda.setCodiceFiscale(cuaa);
//		scheda.setNextIdValidazione(idValidazione);
//		sincronizzazioneFascicoloService.invioEventoFineValidazione(scheda);
//		verify(fascicoloService, timeout(3000)).sincronizzaAgs(Mockito.any(String.class), Mockito.any(Integer.class));
//	}
//}
