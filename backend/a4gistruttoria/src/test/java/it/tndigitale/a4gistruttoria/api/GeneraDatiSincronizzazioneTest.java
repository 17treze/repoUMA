package it.tndigitale.a4gistruttoria.api;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.CoreMatchers.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;

import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElaborazioneDomandaException;
import it.tndigitale.a4gistruttoria.service.businesslogic.sincronizzazione.GeneraDatiSincronizzazioneBase;
import it.tndigitale.a4gistruttoria.service.businesslogic.sincronizzazione.GeneraDatiSincronizzazionePagamenti;
import it.tndigitale.a4gistruttoria.util.TipologiaSincronizzazioneAGEA;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GeneraDatiSincronizzazioneTest {

	@Autowired
	private GeneraDatiSincronizzazionePagamenti generaDatiSincronizzazionePagamenti;
	
	@Before
	public void setUp() {

//		ConfIstruttoriaRicevibilitaDto conf=new ConfIstruttoriaRicevibilitaDto();
//		conf.setDataRicevibilita(LocalDate.of(2018, Month.JUNE, 15));
//		conf.setDataScadenzaDomandaInRitardo(LocalDate.of(2018, Month.JULY, 10));
//		Mockito.when(configurazioneIstruttoriaService.getConfIstruttoriaRicevibilita(Mockito.any()))
//			.thenReturn(conf);
	}
	
	private void simulaProcesso(GeneraDatiSincronizzazioneBase<TipologiaSincronizzazioneAGEA> generaDatiSincronizzazione,int annoCampagna) throws ElaborazioneDomandaException {
		generaDatiSincronizzazione
			.caricaIdDaElaborare(annoCampagna)
			.forEach(idIstruttoria -> {
					try {
						generaDatiSincronizzazione.elabora(idIstruttoria);
					} catch (ElaborazioneDomandaException ex) {
						throw new RuntimeException(ex);
					}
				});
	}
	
	@Test
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_190837_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_190837_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs21Domanda190837() throws ElaborazioneDomandaException {
		//mock("190837");
		simulaProcesso(generaDatiSincronizzazionePagamenti,2019);
		
		
	}
	
	@Test
	//@Ignore
	@WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_190837_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_ISTRUTTORIA set ID_STATO_LAVORAZIONE = (SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO='DEBITI') where ID = 1244739; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_ISTRUTTORIA set ID_STATO_LAVORAZIONE = (SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO='DEBITI') where ID = 1244740; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, statements = "update A4GT_ISTRUTTORIA set ID_STATO_LAVORAZIONE = (SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO='PAGAMENTO_AUTORIZZATO') where ID = 1244739; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, statements = "update A4GT_ISTRUTTORIA set ID_STATO_LAVORAZIONE = (SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO='PAGAMENTO_AUTORIZZATO') where ID = 1244740; COMMIT;")
	@Sql(scripts = { "/statistiche/statistiche_nDomanda_190837_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testStatisticheCs21Domanda190837EscludiIstruttorieInStatoDebiti() throws ElaborazioneDomandaException {
		simulaProcesso(generaDatiSincronizzazionePagamenti,2017);
		
		List<IstruttoriaModel> istruttorie = generaDatiSincronizzazionePagamenti.caricaIstruttorie(2017);
		
		istruttorie = istruttorie.stream()
		.filter(istr -> StatoIstruttoria.DEBITI.equals(istr.getStato()))
		.collect(Collectors.toList());
		
		assertThat(istruttorie, is(hasSize(0)));
		
		
		
	}
}
