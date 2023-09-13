package it.tndigitale.a4g.uma;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.framework.security.service.UtenteClient;
import it.tndigitale.a4g.uma.business.service.client.UmaAnagraficaClient;
import it.tndigitale.a4g.uma.business.service.client.UmaUtenteClient;
import it.tndigitale.a4g.uma.business.service.utente.AbilitazioniComponent;
import it.tndigitale.a4g.uma.dto.aual.FascicoloAualDto;
import it.tndigitale.a4g.uma.dto.richiesta.PrelievoDto;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente")
class AbilitazioniComponentTest {
	@Autowired
	private AbilitazioniComponent abilitazioniComponent;
	@MockBean
	private UtenteComponent utenteComponent;
	@MockBean
	private UmaUtenteClient umaUtenteClient;
//	@MockBean
//	private UmaAnagraficaClient anagraficaClient;
	@MockBean
	private UtenteClient abilitazioniUtente;

	@Test
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void checkDeleteDomandaUma() throws Exception { 
		Mockito.when(utenteComponent.haUnRuolo(Mockito.any())).thenReturn(true);
		if (!abilitazioniComponent.checkDeleteRichiestaCarburante(5L)) {
			fail();
		}
	}

	@Test
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void checkDeleteDomandaUmaError() throws Exception { 
		Mockito.when(utenteComponent.haUnRuolo(Mockito.any())).thenReturn(false);
		assertFalse(abilitazioniComponent.checkDeleteRichiestaCarburante(5L));
	}
	/*
	@Test
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void checkEditaDomandaUmaUtente() throws Exception { 
		Mockito.when(utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_EDITA_UTENTE)).thenReturn(true);

		mockUtente();
		if (!abilitazioniComponent.checkModificaRichiestaCarburante(5L)) {
			fail();
		}
	}
	*/
	@Test
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void checkEditaDomandaUmaEnte() throws Exception { 
		Mockito.when(utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_EDITA_UTENTE)).thenReturn(false);
		Mockito.when(utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_EDITA_ENTE)).thenReturn(true);

		mockEnte();
		if (!abilitazioniComponent.checkModificaRichiestaCarburante(5L)) {
			fail();
		}
	}

	@Test
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void checkEditaDomandaUmaErrorReadOnly() throws Exception { 
		assertFalse(abilitazioniComponent.checkModificaRichiestaCarburante(2L));
	}

	@Test
	@Transactional
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void checkRicercaRichiestaDiCarburanteTutti() throws Exception { 
		Mockito.when(utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_RICERCA_TUTTI)).thenReturn(true);
		if (!abilitazioniComponent.checkRicercaRichiestaDiCarburante(1L)) {
			fail();
		}
	}
	/*
	@Test
	@Transactional
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void checkRicercaRichiestaDiCarburanteUtente() throws Exception { 
		Mockito.when(utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_RICERCA_UTENTE)).thenReturn(true);

		mockUtente();
		if (!abilitazioniComponent.checkRicercaRichiestaDiCarburante(1L)) {
			fail();
		}
	}
	*/
	@Test
	@Transactional
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void checkRicercaRichiestaDiCarburanteEnte() throws Exception { 
		Mockito.when(utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_RICERCA_ENTE)).thenReturn(true);

		mockEnte();
		if (!abilitazioniComponent.checkRicercaRichiestaDiCarburante(1L)) {
			fail();
		}
	}

	@Test
	@Transactional
	@Sql(scripts = "/sql/consumi/clienti/clienti_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/clienti/clienti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void checkRicercaDichiarazioneConsumiTutti() throws Exception { 
		Mockito.when(utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_RICERCA_TUTTI)).thenReturn(true);
		if (!abilitazioniComponent.checkRicercaDichiarazioneConsumi(7761L)) {
			fail();
		}
	}

	@Test
	@Transactional
	@Sql(scripts = "/sql/consumi/clienti/clienti_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/clienti/clienti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void checkRicercaDichiarazioneConsumiEnte() throws Exception { 
		Mockito.when(utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_RICERCA_ENTE)).thenReturn(true);

		mockEnte();
		if (!abilitazioniComponent.checkRicercaDichiarazioneConsumi(7761L)) {
			fail();
		}
	}
	/*
	@Test
	@Transactional
	@Sql(scripts = "/sql/consumi/clienti/clienti_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/clienti/clienti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void checkRicercaDichiarazioneConsumiUtente() throws Exception { 
		Mockito.when(utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_RICERCA_UTENTE)).thenReturn(true);

		mockUtente();
		if (!abilitazioniComponent.checkRicercaDichiarazioneConsumi(7761L)) {
			fail();
		}
	}
	*/
	@Test
	@Transactional
	@Sql(scripts = "/sql/consumi/consuntivi/consuntivi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consuntivi/consuntivi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void checkRicercaConsuntiviDichiarazioneConsumi() throws Exception { 
		Mockito.when(utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_RICERCA_TUTTI)).thenReturn(true);

		if (!abilitazioniComponent.checkRicercaConsuntiviDichiarazioneConsumi(7761L, 16222L)) {
			fail();
		}
	}

	@Test
	@Transactional
	@Sql(scripts = "/sql/consumi/consuntivi/consuntivi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consuntivi/consuntivi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void checkRicercaAllegatiConsuntiviDichiarazioneConsumi() throws Exception { 
		Mockito.when(utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_RICERCA_TUTTI)).thenReturn(true);

		if (!abilitazioniComponent.checkRicercaAllegatiConsuntivoDichiarazioneConsumi(7763L, 325L, 167L)) {
			fail();
		}
	}

	@Test
	@Transactional
	@Sql(scripts = "/sql/consumi/consuntivi/consuntivi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consuntivi/consuntivi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void checkModificaConsuntiviDichiarazioneConsumi() throws Exception {
		Mockito.when(utenteComponent.haUnRuolo(Ruoli.ISTRUTTORE_UMA)).thenReturn(true);

		if (!abilitazioniComponent.checkModificaConsuntiviDichiarazioneConsumi(7761L, 16222L)) {
			fail();
		}
	}


	@Test
	@Transactional
	@Sql(scripts = "/sql/consumi/consuntivi/consuntivi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consuntivi/consuntivi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void checkDeleteDichiarazioneConsumi() throws Exception {
		Mockito.when(utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_CANCELLA_ENTE, Ruoli.DOMANDE_UMA_CANCELLA_UTENTE)).thenReturn(true);
		Mockito.when(utenteComponent.haUnRuolo(Ruoli.ISTRUTTORE_UMA)).thenReturn(true);

		if (!abilitazioniComponent.checkDeleteDichiarazioneConsumi(7761L)) {
			fail();
		}
	}

	@Test
	@Transactional
	@Sql(scripts = "/sql/consumi/clienti/clienti_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/clienti/clienti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void checkEditaDichiarazioneConsumiUtente() throws Exception {
		Mockito.when(utenteComponent.haUnRuolo(Ruoli.ISTRUTTORE_UMA)).thenReturn(true);

		mockUtente();
		if (!abilitazioniComponent.checkModificaDichiarazioneConsumi(7763L)) {
			fail();
		}
	}

	@Test
	@Transactional
	@Sql(scripts = "/sql/consumi/clienti/clienti_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/clienti/clienti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void checkEditaDichiarazioneConsumiEnte() throws Exception { 
		Mockito.when(utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_EDITA_UTENTE)).thenReturn(false);
		Mockito.when(utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_EDITA_ENTE)).thenReturn(true);

		mockEnte();
		if (!abilitazioniComponent.checkModificaDichiarazioneConsumi(7763L)) {
			fail();
		}
	}

	@Test
	@Sql(scripts = "/sql/consumi/clienti/clienti_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/clienti/clienti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void checkEditaDichiarazioneConsumiErrorReadOnly() throws Exception {
		assertFalse(abilitazioniComponent.checkModificaDichiarazioneConsumi(7764L));
	}

	@Test
	@Transactional
	@Sql(scripts = "/sql/consumi/clienti/clienti_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/clienti/clienti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void checkRicercaClientiDichiarazioneConsumi() throws Exception { 
		Mockito.when(utenteComponent.haUnRuolo(Mockito.any())).thenReturn(true);
		if (!abilitazioniComponent.checkRicercaClientiDichiarazioneConsumi(7761L, 1L)) {
			fail();
		}
	}

	@Test
	@Sql(scripts = "/sql/consumi/clienti/clienti_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/clienti/clienti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void checkRicercaClientiDichiarazioneConsumiErrorMismatch() throws Exception { 
		assertFalse(abilitazioniComponent.checkRicercaClientiDichiarazioneConsumi(7761L, 5L));
	}
	/*
	@Test
	@Transactional
	@Sql(scripts = "/sql/consumi/clienti/clienti_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/clienti/clienti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void checkEditaClientiDichiarazioneConsumiUtente() throws Exception { 
		Mockito.when(utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_EDITA_UTENTE)).thenReturn(true);

		mockUtente();
		if (!abilitazioniComponent.checkModificaClientiDichiarazioneConsumi(7761L, 1L)) {
			fail();
		}
	}
	*/
	@Test
	@Transactional
	@Sql(scripts = "/sql/consumi/clienti/clienti_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/clienti/clienti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void checkEditaClientiDichiarazioneConsumiEnte() throws Exception {
		Mockito.when(utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_EDITA_UTENTE)).thenReturn(false);
		Mockito.when(utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_EDITA_ENTE)).thenReturn(true);

		mockEnte();
		if (!abilitazioniComponent.checkModificaClientiDichiarazioneConsumi(7761L, 1L)) {
			fail();
		}
	}

	@Test
	@Sql(scripts = "/sql/consumi/clienti/clienti_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/clienti/clienti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void checkEditaClientiDichiarazioneConsumiErrorMismatch() throws Exception { 
		assertFalse(abilitazioniComponent.checkModificaClientiDichiarazioneConsumi(7761L, 5L));
	}

	@Test
	void checkEditaDichiarazioneConsumiErrorNotFound() throws Exception {
		assertFalse(abilitazioniComponent.checkModificaDichiarazioneConsumi(9999L));
	}

	@Test
	void checkEditaDomandaUmaErrorNotFound() throws Exception { 
		assertFalse(abilitazioniComponent.checkModificaRichiestaCarburante(9999L));
	}

	@Test
	void checkRicercaDomandaUma() throws Exception { 
		Mockito.when(utenteComponent.haUnRuolo(Mockito.any())).thenReturn(true);
		if (!abilitazioniComponent.checkRicercaDomandeUma()) {
			fail();
		}
	}

	@Test
	void checkRicercaDomandaUmaError() throws Exception { 
		Mockito.when(utenteComponent.haUnRuolo(Mockito.any())).thenReturn(false);
		assertFalse(abilitazioniComponent.checkRicercaDomandeUma());
	}
	/*
	@Test
	void checkPresentaDomandaUmaUtente() throws Exception {
		Mockito.when(utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_EDITA_UTENTE)).thenReturn(true);

		mockUtente();
		if	(!abilitazioniComponent.checkPresentaDomandaUma("MSTFBA79L10H612L")) {
			fail();
		}
	}
	*/
	@Test
	void checkPresentaDomandaUmaEnte() throws Exception {
		Mockito.when(utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_EDITA_UTENTE)).thenReturn(false);
		Mockito.when(utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_EDITA_ENTE)).thenReturn(true);

		mockEnte();
		if	(!abilitazioniComponent.checkPresentaDomandaUma("MSTFBA79L10H612L")) {
			fail();
		}
	}
	/*
	@Test
	void checkPresentaDomandaUmaError() throws Exception {
		Mockito.when(utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_EDITA_UTENTE)).thenReturn(false);
		Mockito.when(utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_EDITA_ENTE)).thenReturn(false);

		Utente user = new Utente();
		user.setCodiceFiscale("MSTFBA79L10H612L");
		Mockito.when(umaUtenteClient.getUtenteConnesso()).thenReturn(user);

		assertFalse(abilitazioniComponent.checkPresentaDomandaUma("MSTFBA79L10H612L"));
	}
	*/
	@Test
	@Sql(scripts = "/sql/trasferimenti/carburante_ricevuto_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/trasferimenti/carburante_ricevuto_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void checkRicercaCarburanteRicevuto() throws Exception {
		String cuaa = "BNLFRN72P43H612A";
		Long campagna = 2021L;
		Mockito.when(utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_RICERCA_TUTTI)).thenReturn(true);
		assertTrue(this.abilitazioniComponent.checkRicercaCarburanteRicevuto(cuaa,campagna));
	}
	/*
	@Test
	void checkRicercaPrelievoDistributore() throws Exception {
		Long idDistributore = 8162L;
		Mockito.when(utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_RICERCA_TUTTI)).thenReturn(true);
		Mockito.when(umaUtenteClient.getDistributoreById(22L)).thenReturn(new Distributore());
		assertTrue(this.abilitazioniComponent.checkRicercaPrelievoDistributore(idDistributore));
	}

	@Test
	void checkRicercaPrelievoDistributoreNonEsistente() throws Exception {
		Long idDistributore = 999L;
		Mockito.when(utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_RICERCA_TUTTI)).thenReturn(true);
		Mockito.when(umaUtenteClient.getDistributoreById(22L)).thenReturn(new Distributore());
		assertFalse(this.abilitazioniComponent.checkRicercaPrelievoDistributore(idDistributore));
	}
	*/
	@Test
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void checkModificaPrelievoDistributorePrelievoErrato() throws Exception {
		Long idDistributore = 8111L;
		Long idPrelievo = 0L;
		assertFalse(this.abilitazioniComponent.checkModificaPrelievoDistributore(idDistributore, idPrelievo));
	}

	@Test
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void checkModificaPrelievoDistributorePrelievoConsegnato() throws Exception {
		Long idDistributore = 8162L;
		Long idPrelievo = 6L;
		assertFalse(this.abilitazioniComponent.checkModificaPrelievoDistributore(idDistributore, idPrelievo));
	}
	/*
	@Test
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void checkCreaPrelievoDistributoreNonAssociatoUtente() throws Exception {
		Mockito.when(utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_RICERCA_TUTTI)).thenReturn(true);
		Long identificativoDistributore = 22L;
		var utente = new Utente();
		utente.setCodiceFiscale("AAAA BBBB CCCC");
		Mockito.when(umaUtenteClient.getUtenteConnesso()).thenReturn(utente);
		Mockito.when(umaUtenteClient.getDistributoreById(22L)).thenReturn(null);
		assertFalse(this.abilitazioniComponent.checkCreaPrelievoDistributore(identificativoDistributore));
	}
	*/
	@Test
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/prelievi_richiesta_carburante_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void checkModificaPrelieviDistributore() throws Exception {
		Long idDistributore = 22L;
		List<PrelievoDto> prelievi = new ArrayList<>();
		assertTrue(this.abilitazioniComponent.checkModificaPrelieviDistributore(idDistributore, prelievi));
	}

	@Test
	void checkRicercaDomandaUmaRuoloTutti() throws Exception {
		String cuaa = "AAAA";
		Mockito.when(utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_RICERCA_TUTTI)).thenReturn(true);
		assertTrue(this.abilitazioniComponent.checkRicercaDomandaUma(cuaa));
	}

	@Test
	void checkRicercaDomandaUmaRuoloEnte() throws Exception {
		String cuaa = "AAAA";

		Mockito.when(utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_RICERCA_TUTTI)).thenReturn(false);
		Mockito.when(utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_RICERCA_ENTE)).thenReturn(true);
		mockEnte();
		assertTrue(this.abilitazioniComponent.checkRicercaDomandaUma(cuaa));
	}
	/*
	@Test
	void checkRicercaDomandaUmaRuoloUtente() throws Exception {
		String cuaa = "AAAA";
		Mockito.when(utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_RICERCA_TUTTI)).thenReturn(false);
		Mockito.when(utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_RICERCA_ENTE)).thenReturn(false);
		Mockito.when(utenteComponent.haUnRuolo(Ruoli.DOMANDE_UMA_RICERCA_UTENTE)).thenReturn(true);
		mockUtente();
		assertTrue(this.abilitazioniComponent.checkRicercaDomandaUma(cuaa));
	}
	*/
	@Test 
	@Transactional
	@Sql(scripts = "/sql/trasferimenti/carburante_ricevuto_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/trasferimenti/carburante_ricevuto_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void checkModificaTrasferimentoCarburanteSuccessful() throws Exception {
		Long idTrasferimento = 4L;
		Mockito.when(utenteComponent.haRuolo(Mockito.any(Ruoli.class))).thenReturn(true);
		// dichiarazione consumi destinatario protocollata
		assertTrue(this.abilitazioniComponent.checkModificaTrasferimentoCarburante(idTrasferimento));
	}

	@Test 
	@Transactional
	@Sql(scripts = "/sql/trasferimenti/carburante_ricevuto_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/trasferimenti/carburante_ricevuto_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void checkModificaTrasferimentoCarburanteDichiarazioneMittenteProtocollata() throws Exception {
		Long idTrasferimento = 5L;
		Mockito.when(utenteComponent.haRuolo(Mockito.any(Ruoli.class))).thenReturn(true);
		// dichiarazione consumi mittente protocollata
		assertTrue(!this.abilitazioniComponent.checkModificaTrasferimentoCarburante(idTrasferimento));
	}

	@Test 
	@Transactional
	@Sql(scripts = "/sql/trasferimenti/carburante_ricevuto_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/trasferimenti/carburante_ricevuto_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void checkModificaTrasferimentoCarburanteDichiarazioneDestinatarioProtocollata() throws Exception {
		Long idTrasferimento = 6L;
		Mockito.when(utenteComponent.haRuolo(Mockito.any(Ruoli.class))).thenReturn(true);
		// dichiarazione consumi destinatario protocollata
		assertTrue(!this.abilitazioniComponent.checkModificaTrasferimentoCarburante(idTrasferimento));
	}


	void mockUtente() {
		/*
		Utente user = new Utente();
		user.setCodiceFiscale("MSTFBA79L10H612L");
		Mockito.when(umaUtenteClient.getUtenteConnesso()).thenReturn(user);
		List<CaricaAgsDto> responseAnag = new ArrayList<>();
		CaricaAgsDto carica = new CaricaAgsDto();
		carica.setCodiceFiscale("MSTFBA79L10H612L");
		responseAnag.add(carica);
		Mockito.when(anagraficaClient.getTitolariRappresentantiLegali(Mockito.any())).thenReturn(responseAnag);
		*/
	}

	void mockEnte() throws Exception {
		FascicoloAualDto fascicolo = new FascicoloAualDto();
		/*
		var detenzione = new DetenzioneAgsDto()
				.setCaa("CAA COLDIRETTI DEL TRENTINO")
				.setIdentificativoSportello(13L)
				.setSportello("001")
				.setTipoDetenzione(TipoDetenzioneEnum.DELEGA);
		fascicolo.setDetenzioni(Arrays.asList(detenzione));
		Mockito.when(anagraficaClient.getFascicolo(Mockito.anyString())).thenReturn(fascicolo);

		List<String> responseAbilitazioniUtente = new ArrayList<>();
		responseAbilitazioniUtente.add("13");
		Utente a = new Utente();
		a.setCodiceFiscale("CODICE_FISCALE");

		Mockito.when(umaUtenteClient.getUtenteConnesso()).thenReturn(a);
		Mockito.when(abilitazioniUtente.getEntiUtente()).thenReturn(responseAbilitazioniUtente);
		*/
	}
}
