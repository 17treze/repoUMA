package it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.DatiIstruttoreDisPascoliModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;
import it.tndigitale.a4gistruttoria.service.businesslogic.avvio.AvvioIntegrazioneIstruttoriaService;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElaborazioneIstruttoriaException;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.visualizza",
"a4gistruttoria.pac.domandaUnica.edita" })
// @AutoConfigureTestDatabase
public class AvvioIntegrazioneIstruttoriaServiceTest {

	@Autowired
	private AvvioIntegrazioneIstruttoriaService integrazioneService;
	@Autowired
	private IstruttoriaDao istruttoriaDao;
	
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/integrazioneDisaccoppiato/avvioProcessoIntegrazione_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/integrazioneDisaccoppiato/avvioProcessoIntegrazione_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void avvioIntegrazioneIstruttoria() throws Exception {
		integrazioneService.elabora(12002710L);
		List<IstruttoriaModel> list = istruttoriaDao.findByDomandaUnicaModel_idAndSostegno(8669820L, Sostegno.DISACCOPPIATO);
		assertThat(list).isNotNull();
		Optional<IstruttoriaModel> istruttoriaSaldo = list.stream().filter(i -> i.getTipologia().equals(TipoIstruttoria.SALDO)).findFirst();
		Optional<IstruttoriaModel> istruttoriaIntegrazione = list.stream().filter(i -> i.getTipologia().equals(TipoIstruttoria.INTEGRAZIONE)).findFirst();
		assertTrue(istruttoriaSaldo.isPresent());
		assertTrue(istruttoriaIntegrazione.isPresent());
			checkIstruttorie(istruttoriaSaldo.get(), istruttoriaIntegrazione.get());
	}
	
	// Verifica che se già presente l'istruttoria integrata rilancia errore.
	@Test(expected = ElaborazioneIstruttoriaException.class)
	@Transactional
	@Sql(scripts = "/DomandaUnica/integrazioneDisaccoppiato/avvioProcessoIntegrazione_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/integrazioneDisaccoppiato/avvioProcessoIntegrazione_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void avvioIntegrazioneIstruttoriaError() throws Exception {
		integrazioneService.elabora(12002710L);
		integrazioneService.elabora(12002710L);
	}
	
	public void checkIstruttorie(IstruttoriaModel saldo, IstruttoriaModel integrazione) {
		// Controllo l'integrità dati istruttoria di saldo
		assertEquals(saldo.getTipologia(), TipoIstruttoria.SALDO);
		assertEquals(saldo.getStato(), StatoIstruttoria.PAGAMENTO_AUTORIZZATO);
		assertThat(saldo.getDatiIstruttoreDisModel()).isNotNull();
		assertThat(saldo.getDatiIstruttoreDisPascoli()).isNotNull();
		
		// Controllo l'integrità dati nuova istruttoria di integrazione
		assertEquals(integrazione.getTipologia(), TipoIstruttoria.INTEGRAZIONE);
		assertEquals(integrazione.getStato(), StatoIstruttoria.RICHIESTO);
		assertThat(integrazione.getDatiIstruttoreDisModel()).isNotNull();
		assertThat(integrazione.getDatiIstruttoreDisPascoli()).isNotNull();
		
		// Controllo se i dati relativi agli istruttori sono gli stessi
		assertEquals(saldo.getDatiIstruttoreDisModel().getBpsSuperficie(), integrazione.getDatiIstruttoreDisModel().getBpsSuperficie());
		assertEquals(saldo.getDatiIstruttoreDisModel().getDomAnnoPrecNonLiq(), integrazione.getDatiIstruttoreDisModel().getDomAnnoPrecNonLiq());
		assertEquals(saldo.getDatiIstruttoreDisModel().getNoteIstruttore(), integrazione.getDatiIstruttoreDisModel().getNoteIstruttore());
		assertEquals(saldo.getDatiIstruttoreDisModel().getImportoSalari(), integrazione.getDatiIstruttoreDisModel().getImportoSalari());

		// Conversione da set a list per poter accedere più facilmente ai dati
		List<DatiIstruttoreDisPascoliModel> datiDisPascoliSaldo = new ArrayList<DatiIstruttoreDisPascoliModel>();
		datiDisPascoliSaldo.addAll(saldo.getDatiIstruttoreDisPascoli());	
		List<DatiIstruttoreDisPascoliModel> datiDisPascoliIntegrazione = new ArrayList<DatiIstruttoreDisPascoliModel>();
		datiDisPascoliIntegrazione.addAll(integrazione.getDatiIstruttoreDisPascoli());
		
		// Accedo staticamente usando get(0) perchè nello script di insert è censito un solo elemento
		assertEquals(datiDisPascoliSaldo.get(0).getCuaaResponsabile(), datiDisPascoliIntegrazione.get(0).getCuaaResponsabile());
		assertEquals(datiDisPascoliSaldo.get(0).getDescrizionePascolo(), datiDisPascoliIntegrazione.get(0).getDescrizionePascolo());

		// Altri controlli
		assertEquals(saldo.getDomandaUnicaModel().getId(), integrazione.getDomandaUnicaModel().getId());
		assertEquals(saldo.getSostegno(), integrazione.getSostegno());
	}
}
