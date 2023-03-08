package it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
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
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.MockIoItalia;
import it.tndigitale.a4gistruttoria.action.IstruttoriaComponent;
import it.tndigitale.a4gistruttoria.dto.DomandaCollegata;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiInput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiOutput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.dao.TransizioneIstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ControlloIntersostegnoException;
import it.tndigitale.a4gistruttoria.util.CodiceEsito;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.visualizza", "a4gistruttoria.pac.domandaUnica.edita" })
// @AutoConfigureTestDatabase
public class ControlloIntersostegnoServiceSaldoDisaccoppiatoConAnticipoTest extends MockIoItalia {

	@Autowired
	ControlloIntersostegnoService controlloIntersostegnoManager;

	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private RestTemplate restTemplate;

	@Autowired
	private TransizioneIstruttoriaDao daoTransizioneSostegno;
	
	@Autowired
	private DomandaUnicaDao daoDomanda;
	
	@Autowired
	IstruttoriaComponent istruttoriaComponent;
	
	@Autowired
	private IstruttoriaDao istruttoriaDao;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	
	/***********************************************************************************
	 * SOLO DISACCOPPIATO
	 ***********************************************************************************/

	// Dati test:
	// - Disaccoppiato:
	// --- Stato: LIQUIDABILE
	// --- Importo: 2566.22 (lordo e netto)
	// - Anticipo disaccoppiato:
	// -- Stato: CALCOLO_OK
	// -- Importo: 1566.22
	// - Acs:
	// --- Stato: NON_RICHIESTO
	// - Acz:
	// --- Stato: NON_RICHIESTO
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void soloDisaccoppiatoConEsitoAntimafiaAlloraControlloIntersostegnoOK() throws ControlloIntersostegnoException, Exception {
		IstruttoriaModel istruttoria = eseguiControlli("181662", "");
		
		
		Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

		assertThat(transizioneSostegno.isPresent()).isTrue();

		assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno2().getIdentificativo()).isEqualTo(StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria());
		assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo()).isEqualTo(StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK.getStatoIstruttoria());

		Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
		assertThat(passiTransizione.size()).isEqualTo(3);

		Optional<PassoTransizioneModel> optPassoImportoMinimo = getPassoImportoMinimo(passiTransizione);

		assertThat(optPassoImportoMinimo.isPresent()).isTrue();

		assertThat(optPassoImportoMinimo.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_042.getCodiceEsito());

		Optional<PassoTransizioneModel> optPassoLavAntimafia = getPassoImportoAntimafia(passiTransizione);

		assertThat(optPassoLavAntimafia.isPresent()).isFalse();

		//assertThat(optPassoLavAntimafia.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_062.getCodiceEsito());

		Optional<PassoTransizioneModel> optPassoDisciplina = getPassoDisciplina(passiTransizione);

		assertThat(optPassoDisciplina.isPresent()).isTrue();
		
		PassoTransizioneModel passo = optPassoDisciplina.get();
		DatiInput variabiliInput = objectMapper.readValue(passo.getDatiInput(), DatiInput.class);
		DatiOutput variabiliOutput = objectMapper.readValue(passo.getDatiOutput(), DatiOutput.class);

		// disciplina già applicata
		Optional<VariabileCalcolo> optDiscGiaApp = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPACS);
		assertThat(optDiscGiaApp).isPresent();
		VariabileCalcolo var = optDiscGiaApp.get();
		assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

		optDiscGiaApp = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPACZ);
		assertThat(optDiscGiaApp).isPresent();
		var = optDiscGiaApp.get();
		assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

		optDiscGiaApp = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPDIS);
		assertThat(optDiscGiaApp).isPresent();
		var = optDiscGiaApp.get();
		assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

		// disciplina da applicare al disaccoppiato
		Optional<VariabileCalcolo> optDffrpagdis = getVariabileOutputPasso(variabiliOutput, TipoVariabile.DFFRPAGDIS);

		assertThat(optDffrpagdis).isPresent();

		var = optDffrpagdis.get();
		assertEquals(BigDecimal.valueOf(2000).setScale(2, RoundingMode.FLOOR), var.getValNumber());
	}

	// Dati test:
	// - Disaccoppiato:
	// --- Stato: LIQUIDABILE
	// --- Importo: 2566.22 (lordo)
	// - Anticipo disaccoppiato:
	// -- Stato: PAGAMENTO_AUTORIZZATO
	// -- Importo: 1566.22
	// - Acs:
	// --- Stato: NON_RICHIESTO
	// - Acz:
	// --- Stato: NON_RICHIESTO
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/istruttoria_Anticipo_Liquidata_d181662.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2566.22}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":1000.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'BRTSRG86A24L174X' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"BPSIMPCALCFIN\",\"valNumber\":2368.79}', '{\"tipoVariabile\":\"BPSIMPCALCFIN\",\"valNumber\":1000.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'BRTSRG86A24L174X' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void soloDisaccoppiatoAnticipoPagatoEsitoAntimafiaAlloraControlloIntersostegnoOK() throws ControlloIntersostegnoException, Exception {
		IstruttoriaModel istruttoria = eseguiControlli("181662", "");
		
		
		Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

		assertThat(transizioneSostegno.isPresent()).isTrue();

		assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno2().getIdentificativo()).isEqualTo(StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria());
		assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo()).isEqualTo(StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK.getStatoIstruttoria());

		Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
		assertThat(passiTransizione.size()).isEqualTo(3);

		Optional<PassoTransizioneModel> optPassoImportoMinimo = getPassoImportoMinimo(passiTransizione);

		assertThat(optPassoImportoMinimo.isPresent()).isTrue();

		assertThat(optPassoImportoMinimo.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_042.getCodiceEsito());

		Optional<PassoTransizioneModel> optPassoLavAntimafia = getPassoImportoAntimafia(passiTransizione);

		assertThat(optPassoLavAntimafia.isPresent()).isFalse();

		//assertThat(optPassoLavAntimafia.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_062.getCodiceEsito());

		Optional<PassoTransizioneModel> optPassoDisciplina = getPassoDisciplina(passiTransizione);

		assertThat(optPassoDisciplina.isPresent()).isTrue();
		
		PassoTransizioneModel passo = optPassoDisciplina.get();
		DatiInput variabiliInput = objectMapper.readValue(passo.getDatiInput(), DatiInput.class);
		DatiOutput variabiliOutput = objectMapper.readValue(passo.getDatiOutput(), DatiOutput.class);

		// disciplina già applicata
		Optional<VariabileCalcolo> optDiscGiaApp = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPACS);
		assertThat(optDiscGiaApp).isPresent();
		VariabileCalcolo var = optDiscGiaApp.get();
		assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

		optDiscGiaApp = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPACZ);
		assertThat(optDiscGiaApp).isPresent();
		var = optDiscGiaApp.get();
		assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

		optDiscGiaApp = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPDIS);
		assertThat(optDiscGiaApp).isPresent();
		var = optDiscGiaApp.get();
		assertEquals(BigDecimal.valueOf(1566.22).setScale(2, RoundingMode.FLOOR), var.getValNumber());

		// disciplina da applicare al disaccoppiato
		Optional<VariabileCalcolo> optDffrpagdis = getVariabileOutputPasso(variabiliOutput, TipoVariabile.DFFRPAGDIS);

		assertThat(optDffrpagdis).isPresent();

		var = optDffrpagdis.get();
		assertEquals(BigDecimal.valueOf(433.78).setScale(2, RoundingMode.FLOOR), var.getValNumber());
		
		optDffrpagdis = getVariabileOutputPasso(variabiliOutput, TipoVariabile.DFFRPAGDISBPS);

		assertThat(optDffrpagdis).isPresent();

		var = optDffrpagdis.get();
		assertEquals(BigDecimal.valueOf(433.78).setScale(2, RoundingMode.FLOOR), var.getValNumber());

		optDffrpagdis = getVariabileOutputPasso(variabiliOutput, TipoVariabile.DFIMPDFDISBPS);

		assertThat(optDffrpagdis).isPresent();

		var = optDffrpagdis.get();
		assertEquals(BigDecimal.valueOf(558.23).setScale(2, RoundingMode.FLOOR), var.getValNumber());
	}
	
	// Dati test:
	// - Disaccoppiato:
	// --- Stato: LIQUIDABILE
	// --- Importo: 175.06
	// - Anticipo disaccoppiato:
	// -- Stato: PAGAMENTO_AUTORIZZATO
	// -- Importo: 1566.22
	// - Acs:
	// --- Stato: NON_RICHIESTO
	// - Acz:
	// --- Stato: NON_RICHIESTO
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/istruttoria_Anticipo_Liquidata_d181662.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2566.22}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'BRTSRG86A24L174X' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2566.22}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'BRTSRG86A24L174X' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void anticipoPagatoMaCalcoloSaldoImportoNonLiquidabile() throws ControlloIntersostegnoException, Exception {
		IstruttoriaModel istruttoria = eseguiControlli("181662", "");
		Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

		assertThat(transizioneSostegno.isPresent()).isTrue();

		assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno2().getIdentificativo()).isEqualTo(StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria());
		assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo()).isEqualTo(StatoIstruttoria.NON_LIQUIDABILE.getStatoIstruttoria());

		Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
		assertThat(passiTransizione.size()).isEqualTo(2);

		Optional<PassoTransizioneModel> optPassoLavImportoMinimo = getPassoImportoMinimo(passiTransizione);

		assertThat(optPassoLavImportoMinimo.isPresent()).isTrue();

		assertEquals("DUT_043", optPassoLavImportoMinimo.get().getCodiceEsito());

	}
	
	// Dati test:
	// - Disaccoppiato:
	// --- Stato: LIQUIDABILE
	// --- Importo: 25001
	// - Anticipo disaccoppiato:
	// -- Stato: PAGAMENTO_AUTORIZZATO
	// -- Importo: 1566.22
	// - Acs:
	// --- Stato: NON_RICHIESTO
	// - Acz:
	// --- Stato: NON_RICHIESTO
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/istruttoria_Anticipo_Liquidata_d181662.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2566.22}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":25001}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'BRTSRG86A24L174X' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2566.22}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":25001}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'BRTSRG86A24L174X' and i.SOSTEGNO = 'DISACCOPPIATO' and  i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void anticipoPagatoMaNoDomandeCollegateAntimafiaConImportoOltreSogliaAlloraPagamentoNonAutorizzato() throws ControlloIntersostegnoException, Exception {
		IstruttoriaModel istruttoria = eseguiControlli("181662", "");
		Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

		assertThat(transizioneSostegno.isPresent()).isTrue();

		assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno2().getIdentificativo()).isEqualTo(StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria());

		assertEquals(StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK.getStatoIstruttoria(), transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo());

		Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
		assertThat(passiTransizione.size()).isEqualTo(3);

		Optional<PassoTransizioneModel> optPassoLavImportoMinimo = getPassoImportoMinimo(passiTransizione);

		Optional<PassoTransizioneModel> optPassoLavAntimafia = getPassoImportoAntimafia(passiTransizione);

		assertThat(optPassoLavImportoMinimo.isPresent()).isTrue();
		assertThat(optPassoLavAntimafia.isPresent()).isFalse();

		assertThat(optPassoLavImportoMinimo.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_042.getCodiceEsito());
		//assertThat(optPassoLavAntimafia.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_061.getCodiceEsito());
	}

	// Dati test:
	// - Disaccoppiato:
	// --- Stato: LIQUIDABILE
	// --- Importo: 25001
	// - Anticipo disaccoppiato:
	// -- Stato: PAGAMENTO_AUTORIZZATO
	// -- Importo: 1566.22
	// - Acs:
	// --- Stato: NON_RICHIESTO
	// - Acz:
	// --- Stato: NON_RICHIESTO
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/istruttoria_Anticipo_Liquidata_d181662.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2566.22}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":25001}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'BRTSRG86A24L174X' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2566.22}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":25001}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'BRTSRG86A24L174X' and i.SOSTEGNO = 'DISACCOPPIATO' and  i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void anticipoPagatoMaNoDichiarazioneAntimafiaConImportoOltreSogliaAlloraPagamentoNonAutorizzato() throws ControlloIntersostegnoException, Exception {
		String numeroDomanda = "181662";

		String serviceUrl = "http://localhost:9001/a4gfascicolo/api/v1/antimafia?params=%7B%22azienda%22%3A%7B%22cuaa%22%3A%22BRTSRG86A24L174X%22%7D%7D";
		String resString = "[]";

		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrl)), Mockito.eq(String.class))).thenReturn(resString);

		DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(BigDecimal.valueOf(new Long(numeroDomanda)));
		assertThat(d).isNotNull();

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(d, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);

		controlloIntersostegnoManager.elabora(istruttoria.getId());

		Optional<TransizioneIstruttoriaModel> transizioneSostegno = 
			daoTransizioneSostegno.findTransizioneControlloIntersostegno(
				istruttoria).stream().findFirst();

		assertThat(transizioneSostegno.isPresent()).isTrue();

		assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno2().getIdentificativo()).isEqualTo(StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria());

		assertEquals(StatoIstruttoria.PAGAMENTO_NON_AUTORIZZATO.getStatoIstruttoria(), transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo());

		Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
		assertThat(passiTransizione.size()).isEqualTo(3);

		Optional<PassoTransizioneModel> optPassoLavImportoMinimo = getPassoImportoMinimo(passiTransizione);

		Optional<PassoTransizioneModel> optPassoLavAntimafia =
				passiTransizione.stream().filter(
						p -> p.getCodicePasso().equals(TipologiaPassoTransizione.CONTROLLO_IMPORTO_ANTIMAFIA)).findFirst();

		assertThat(optPassoLavImportoMinimo.isPresent()).isTrue();
		assertThat(optPassoLavAntimafia.isPresent()).isTrue();

		assertThat(optPassoLavImportoMinimo.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_042.getCodiceEsito());
		assertThat(optPassoLavAntimafia.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_061.getCodiceEsito());
	}
	
	/***********************************************************************************
	 * DISACCOPPIATO - ACS
	 ***********************************************************************************/

	// Dati test:
	// - Disaccoppiato:
	// --- Stato: LIQUIDABILE
	// --- Importo: 175.06
	// - Anticipo disaccoppiato:
	// -- Stato: PAGAMENTO_AUTORIZZATO
	// -- Importo: 1566.22 
	// - Acs:
	// --- Stato: CONTROLLI_CALCOLO_OK
	// --- Importo: 500
	// - Acz:
	// --- Stato: NON_RICHIESTO
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato_Acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/istruttoria_Anticipo_Liquidata_d189674.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":500.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":500.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "UPDATE A4GT_ISTRUTTORIA set ID_STATO_LAVORAZIONE = (select id from a4gd_stato_lav_sostegno where identificativo = 'CONTROLLI_CALCOLO_OK') where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 189674) and SOSTEGNO = 'SUPERFICIE'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void conAnticipoPagatoAcsCalcoloMaImportoSaldoNoSufficienteAlloraPagamentoNonAutorizzato() throws ControlloIntersostegnoException, Exception {
		IstruttoriaModel istruttoria = eseguiControlli("189674", "");
		
		
		Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

		assertThat(transizioneSostegno.isPresent()).isTrue();

		assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno2().getIdentificativo()).isEqualTo(StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria());
		assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo()).isEqualTo(StatoIstruttoria.PAGAMENTO_NON_AUTORIZZATO.getStatoIstruttoria());

		Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
		assertThat(passiTransizione.size()).isEqualTo(2);

		Optional<PassoTransizioneModel> optPassoLavImportoMinimo = getPassoImportoMinimo(passiTransizione);

		assertThat(optPassoLavImportoMinimo.isPresent()).isTrue();

		assertThat(optPassoLavImportoMinimo.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_047.getCodiceEsito());
	}

	// Dati test:
	// - Disaccoppiato:
	// --- Stato: LIQUIDABILE
	// --- Importo Lordo: 1192.58
	// --- Giovane (netto): -16.71
	// --- Greening (netto): 23.83
	// --- BPS (netto): 45.9
	// --- Importo netto: 53.02
	// - Anticipo disaccoppiato:
	// -- Stato: PAGAMENTO_AUTORIZZATO
	// -- Importo: ‭1139.56‬ 
	// - Acs:
	// --- Stato: LIQUIDABILE
	// --- Importo: 500
	// - Acz:
	// --- Stato: NON_RICHIESTO
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato_Acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/istruttoria_Anticipo_Liquidata_d189674.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"BPSIMPCALCFIN\",\"valNumber\":1908.61}', '{\"tipoVariabile\":\"BPSIMPCALCFIN\",\"valNumber\":45.9}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"GREIMPCALCFIN\",\"valNumber\":1017.93}', '{\"tipoVariabile\":\"GREIMPCALCFIN\",\"valNumber\":23.83},{\"tipoVariabile\":\"GIOIMPCALCFIN\",\"valNumber\":-16.71}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":53.02}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":1192.58}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":500.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":500.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	//@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "UPDATE A4GT_ISTRUTTORIA set ID_STATO_LAVORAZIONE = (select id from a4gd_stato_lav_sostegno where identificativo = 'CONTROLLI_CALCOLO_OK') where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 189674) and SOSTEGNO = 'SUPERFICIE'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void conAnticipoPagatoAcsCalcoloMaConUnPremioNegativoMaControlloIntersostegnoOK() throws ControlloIntersostegnoException, Exception {
		IstruttoriaModel istruttoria = eseguiControlli("189674", "");
		
		
		Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

		assertThat(transizioneSostegno.isPresent()).isTrue();

		assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno2().getIdentificativo()).isEqualTo(StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria());
		assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo()).isEqualTo(StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK.getStatoIstruttoria());

		Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
		assertThat(passiTransizione.size()).isEqualTo(3);

		Optional<PassoTransizioneModel> optPassoDisciplinaFinanziaria = getPassoDisciplina(passiTransizione);

		assertThat(optPassoDisciplinaFinanziaria.isPresent()).isTrue();

		PassoTransizioneModel passo = optPassoDisciplinaFinanziaria.get();
		DatiInput variabiliInput = objectMapper.readValue(passo.getDatiInput(), DatiInput.class);
		DatiOutput variabiliOutput = objectMapper.readValue(passo.getDatiOutput(), DatiOutput.class);

		// disciplina da applicare al disaccoppiato
		Optional<VariabileCalcolo> optDffrpagdis = getVariabileOutputPasso(variabiliOutput, TipoVariabile.DFFRPAGDIS);

		assertThat(optDffrpagdis).isPresent();

		VariabileCalcolo var = optDffrpagdis.get();
		assertEquals(BigDecimal.valueOf(69.73).setScale(2, RoundingMode.FLOOR), var.getValNumber());

		// controllo le variabili calcolate sui premi
		// BPS
		Optional<VariabileCalcolo> optPremio = getVariabileInput(variabiliInput, TipoVariabile.BPSIMPCALCFIN);

		assertThat(optPremio).isPresent();

		VariabileCalcolo varPremio = optPremio.get();
		assertEquals(BigDecimal.valueOf(45.9).setScale(2, RoundingMode.FLOOR), varPremio.getValNumber());
		
		// GIO
		optPremio = getVariabileInput(variabiliInput, TipoVariabile.GIOIMPCALCFIN);
		assertThat(optPremio).isPresent();
		varPremio = optPremio.get();
		assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), varPremio.getValNumber());

		// GRE
		optPremio = getVariabileInput(variabiliInput, TipoVariabile.GREIMPCALCFIN);
		assertThat(optPremio).isPresent();
		varPremio = optPremio.get();
		assertEquals(BigDecimal.valueOf(23.83).setScale(2, RoundingMode.FLOOR), varPremio.getValNumber());

		// DISIMPCALC
		optPremio = getVariabileInput(variabiliInput, TipoVariabile.DISIMPCALC);
		assertThat(optPremio).isPresent();
		varPremio = optPremio.get();
		assertEquals(BigDecimal.valueOf(69.73).setScale(2, RoundingMode.FLOOR), var.getValNumber());
	}
	
	// Dati test:
	// - Disaccoppiato:
	// --- Stato: LIQUIDABILE
	// --- Importo: 175.06
	// - Anticipo disaccoppiato:
	// -- Stato: CALCOLO_OK
	// -- Importo: 1566.22 
	// - Acs:
	// --- Stato: LIQUIDABILE
	// --- Importo: 50
	// - Acz:
	// --- Stato: NON_RICHIESTO
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato_Acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":50.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":50.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void conAnticipoCalcolatoAcsLiquidabileMaImportoSaldoNoSufficienteAlloraNonLiquidabile() throws ControlloIntersostegnoException, Exception {
		IstruttoriaModel istruttoria = eseguiControlli("189674", "");
		
		
		Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

		assertThat(transizioneSostegno.isPresent()).isTrue();

		assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno2().getIdentificativo()).isEqualTo(StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria());
		assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo()).isEqualTo(StatoIstruttoria.NON_LIQUIDABILE.getStatoIstruttoria());

		Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
		assertThat(passiTransizione.size()).isEqualTo(2);

		Optional<PassoTransizioneModel> optPassoLavImportoMinimo = getPassoImportoMinimo(passiTransizione);

		assertThat(optPassoLavImportoMinimo.isPresent()).isTrue();

		assertThat(optPassoLavImportoMinimo.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_050.getCodiceEsito());
	}

	// Dati test:
	// - Disaccoppiato:
	// --- Stato: LIQUIDABILE
	// --- Importo: 175.06
	// - Anticipo disaccoppiato:
	// -- Stato: CALCOLO_OK
	// -- Importo: 1566.22 
	// - Acs:
	// --- Stato: NON_AMMISSIBILE
	// --- Importo: 500
	// - Acz:
	// --- Stato: NON_RICHIESTO
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato_Acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":500.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":500.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "UPDATE A4GT_ISTRUTTORIA set ID_STATO_LAVORAZIONE = (select id from a4gd_stato_lav_sostegno where identificativo = 'NON_AMMISSIBILE') where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 189674) and SOSTEGNO = 'SUPERFICIE'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void conAnticipoCalcolatoAcsNonAmmissibileMaImportoSaldoNoSufficienteAlloraLiquidabile() throws ControlloIntersostegnoException, Exception {
		IstruttoriaModel istruttoria = eseguiControlli("189674", "");
		
		
		Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

		assertThat(transizioneSostegno.isPresent()).isTrue();

		assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno2().getIdentificativo()).isEqualTo(StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria());
		assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo()).isEqualTo(StatoIstruttoria.NON_LIQUIDABILE.getStatoIstruttoria());

		Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
		assertThat(passiTransizione.size()).isEqualTo(2);

		Optional<PassoTransizioneModel> optPassoLavImportoMinimo = getPassoImportoMinimo(passiTransizione);

		assertThat(optPassoLavImportoMinimo.isPresent()).isTrue();

		assertThat(optPassoLavImportoMinimo.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_050.getCodiceEsito());
	}
	
	// Dati test:
	// - Disaccoppiato:
	// --- Stato: LIQUIDABILE
	// --- Importo: 175.06
	// - Anticipo disaccoppiato:
	// -- Stato: CALCOLO_OK
	// -- Importo: 1566.22 
	// - Acs:
	// --- Stato: NON_LIQUIDABILE
	// --- Importo: 500
	// - Acz:
	// --- Stato: NON_RICHIESTO
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato_Acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":500.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":500.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "UPDATE A4GT_ISTRUTTORIA set ID_STATO_LAVORAZIONE = (select id from a4gd_stato_lav_sostegno where identificativo = 'NON_LIQUIDABILE') where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 189674) and SOSTEGNO = 'SUPERFICIE'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void conAnticipoCalcolatoAcsNonLiquidabileMaImportoSaldoNoSufficienteAlloraLiquidabile() throws ControlloIntersostegnoException, Exception {
		IstruttoriaModel istruttoria = eseguiControlli("189674", "");
		
		
		Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

		assertThat(transizioneSostegno.isPresent()).isTrue();

		assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno2().getIdentificativo()).isEqualTo(StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria());
		assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo()).isEqualTo(StatoIstruttoria.NON_LIQUIDABILE.getStatoIstruttoria());

		Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
		assertThat(passiTransizione.size()).isEqualTo(2);

		Optional<PassoTransizioneModel> optPassoLavImportoMinimo = getPassoImportoMinimo(passiTransizione);

		assertThat(optPassoLavImportoMinimo.isPresent()).isTrue();

		assertThat(optPassoLavImportoMinimo.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_050.getCodiceEsito());

	}
	
	// Dati test:
	// - Disaccoppiato:
	// --- Stato: LIQUIDABILE
	// --- Importo: 175.06
	// - Anticipo disaccoppiato:
	// -- Stato: CALCOLO_OK
	// -- Importo: 1566.22 
	// - Acs:
	// --- Stato: LIQUIDABILE
	// --- Importo: 500
	// - Acz:
	// --- Stato: NON_RICHIESTO
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato_Acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"BPSIMPCALCFIN\",\"valNumber\":1908.61}', '{\"tipoVariabile\":\"BPSIMPCALCFIN\",\"valNumber\":150.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"GREIMPCALCFIN\",\"valNumber\":1017.93}', '{\"tipoVariabile\":\"GREIMPCALCFIN\",\"valNumber\":25.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":500.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":500.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void conAnticipoCalcolatoAcsLiquidabileMaImportoRaggiuntoAlloraControlliIntersosegnoOK() throws ControlloIntersostegnoException, Exception {
		IstruttoriaModel istruttoria = eseguiControlli("189674", "");
		Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

		assertThat(transizioneSostegno.isPresent()).isTrue();

		assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno2().getIdentificativo()).isEqualTo(StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria());

		assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo()).isEqualTo(StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK.getStatoIstruttoria());

		Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
		assertEquals(3, passiTransizione.size());

		Optional<PassoTransizioneModel> optPassoLavImportoMinimo = passiTransizione.stream().filter(
				p -> p.getCodicePasso().equals(TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO)).findFirst();

		Optional<PassoTransizioneModel> optPassoLavAntimafia = getPassoImportoAntimafia(passiTransizione);

		assertThat(optPassoLavImportoMinimo.isPresent()).isTrue();
		assertThat(optPassoLavAntimafia.isPresent()).isFalse();

		assertThat(optPassoLavImportoMinimo.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_049.getCodiceEsito());
		//assertThat(optPassoLavAntimafia.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_059.getCodiceEsito());
		
		Optional<PassoTransizioneModel> optPassoDisciplinaFinanziaria = getPassoDisciplina(passiTransizione);

		assertThat(optPassoDisciplinaFinanziaria.isPresent()).isTrue();

		PassoTransizioneModel passo = optPassoDisciplinaFinanziaria.get();
		DatiInput variabiliInput = objectMapper.readValue(passo.getDatiInput(), DatiInput.class);
		DatiOutput variabiliOutput = objectMapper.readValue(passo.getDatiOutput(), DatiOutput.class);

		// disciplina già applicata a ACS
		Optional<VariabileCalcolo> optDffrappacs = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPACS);
		// disciplina da applicare al disaccoppiato
		Optional<VariabileCalcolo> optDffrpagdis = getVariabileOutputPasso(variabiliOutput, TipoVariabile.DFFRPAGDIS);

		assertThat(optDffrappacs).isPresent();
		assertThat(optDffrpagdis).isPresent();

		VariabileCalcolo var = optDffrappacs.get();
		assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

		var = optDffrpagdis.get();
		assertEquals(BigDecimal.valueOf(175.06).setScale(2, RoundingMode.FLOOR), var.getValNumber());
		
	}
	
	// Dati test:
	// - Disaccoppiato:
	// --- Stato: LIQUIDABILE
	// --- Importo: 1000
	// - Anticipo disaccoppiato:
	// -- Stato: CALCOLO_OK
	// -- Importo: 1566.22 
	// - Acs:
	// --- Stato: PAGAMENTO_AUTORIZZATO
	// --- Importo: 1200
	// --- Disciplina già applicata: 1200
	// - Acz:
	// --- Stato: NON_RICHIESTO
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato_Acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":1000.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":1000.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":1200.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":1200.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/domanda_Disciplina_Finanziaria_Acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"DFFRPAGACS\",\"valNumber\":1746.01}', '{\"tipoVariabile\":\"DFFRPAGACS\",\"valNumber\":1200.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE' AND ID_STATO_INIZIALE = (SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'LIQUIDABILE')) and CODICE_PASSO = 'DISCIPLINA_FINANZIARIA'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void conAnticipoCalcolatoACSErogatoControlloIntersostegnoOKDisciplinaResidua() throws Exception {
		IstruttoriaModel istruttoria = eseguiControlli("189674", "");
		Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

		assertThat(transizioneSostegno.isPresent()).isTrue();

		Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
		Optional<PassoTransizioneModel> optPassoDisciplinaFinanziaria = getPassoDisciplina(passiTransizione);

		assertThat(optPassoDisciplinaFinanziaria.isPresent()).isTrue();

		PassoTransizioneModel passo = optPassoDisciplinaFinanziaria.get();
		DatiInput variabiliInput = objectMapper.readValue(passo.getDatiInput(), DatiInput.class);
		DatiOutput variabiliOutput = objectMapper.readValue(passo.getDatiOutput(), DatiOutput.class);

		// disciplina già applicata a ACS
		Optional<VariabileCalcolo> optDffrappacs = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPACS);
		// disciplina da applicare al disaccoppiato
		Optional<VariabileCalcolo> optDffrpagdis = getVariabileOutputPasso(variabiliOutput, TipoVariabile.DFFRPAGDIS);

		assertThat(optDffrappacs).isPresent();
		assertThat(optDffrpagdis).isPresent();

		VariabileCalcolo var = optDffrappacs.get();
		assertEquals(BigDecimal.valueOf(1200).setScale(2, RoundingMode.FLOOR), var.getValNumber());

		var = optDffrpagdis.get();
		assertEquals(BigDecimal.valueOf(800).setScale(2, RoundingMode.FLOOR), var.getValNumber());

	}
	
	// Dati test:
	// - Disaccoppiato:
	// --- Stato: LIQUIDABILE
	// --- Importo: 1000
	// - Anticipo disaccoppiato:
	// -- Stato: CALCOLO_OK
	// -- Importo: 1566.22 
	// - Acs:
	// --- Stato: CONTROLLI_INTERSOSTEGNO_OK
	// --- Importo: 1200
	// --- Disciplina calcolata applicata: 1200
	// - Acz:
	// --- Stato: NON_RICHIESTO
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato_Acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"BPSIMPCALCFIN\",\"valNumber\":1908.61}', '{\"tipoVariabile\":\"BPSIMPCALCFIN\",\"valNumber\":900.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"GREIMPCALCFIN\",\"valNumber\":1017.93}', '{\"tipoVariabile\":\"GREIMPCALCFIN\",\"valNumber\":100.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":1000.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":1000.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":1200.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":1200.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/domanda_Disciplina_Finanziaria_Acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"DFFRPAGACS\",\"valNumber\":1746.01}', '{\"tipoVariabile\":\"DFFRPAGACS\",\"valNumber\":1200.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE' AND ID_STATO_INIZIALE = (SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'LIQUIDABILE')) and CODICE_PASSO = 'DISCIPLINA_FINANZIARIA'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "UPDATE A4GT_ISTRUTTORIA set ID_STATO_LAVORAZIONE = (select id from a4gd_stato_lav_sostegno where identificativo = 'CONTROLLI_INTERSOSTEGNO_OK') where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 189674) and SOSTEGNO = 'SUPERFICIE'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void conAnticipoCalcolatoACSControlliIntersostegnoOKAlloraControlloIntersostegnoOKDisciplinaTutta() throws Exception {
		IstruttoriaModel istruttoria = eseguiControlli("189674", "");
		Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

		assertThat(transizioneSostegno.isPresent()).isTrue();

		Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
		Optional<PassoTransizioneModel> optPassoDisciplinaFinanziaria = getPassoDisciplina(passiTransizione);

		assertThat(optPassoDisciplinaFinanziaria.isPresent()).isTrue();

		PassoTransizioneModel passo = optPassoDisciplinaFinanziaria.get();
		DatiInput variabiliInput = objectMapper.readValue(passo.getDatiInput(), DatiInput.class);
		DatiOutput variabiliOutput = objectMapper.readValue(passo.getDatiOutput(), DatiOutput.class);

		// disciplina già applicata a ACS
		Optional<VariabileCalcolo> optDffrappacs = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPACS);
		// disciplina da applicare al disaccoppiato
		Optional<VariabileCalcolo> optDffrpagdis = getVariabileOutputPasso(variabiliOutput, TipoVariabile.DFFRPAGDIS);

		assertThat(optDffrappacs).isPresent();
		assertThat(optDffrpagdis).isPresent();

		VariabileCalcolo var = optDffrappacs.get();
		assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

		var = optDffrpagdis.get();
		assertEquals(BigDecimal.valueOf(1000).setScale(2, RoundingMode.FLOOR), var.getValNumber());

	}
	

	private IstruttoriaModel eseguiControlli(String numeroDomanda, String response1) throws Exception {

		if (numeroDomanda.equals("181662")) {

			String serviceUrl = "http://localhost:9001/a4gfascicolo/api/v1/antimafia?params=%7B+%22azienda%22%3A%7B%22cuaa%22%3A%22BRTSRG86A24L174X%22%7D%2C+%22stato%22+%3A+%7B%22identificativo%22%3A+%22CONTROLLATA%22%7D%7D";
			JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/antimafia.json"));
			String resString = objectMapper.writeValueAsString(response);
			Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrl)), Mockito.eq(String.class))).thenReturn(resString);

			String serviceUrl1 = "http://localhost:8080/a4gistruttoria/api/v1/antimafia/domandecollegate?params=%7B+%22cuaa%22%3A%22BRTSRG86A24L174X%22%2C+%22tipoDomanda%22%3A+%22DOMANDA_UNICA%22%2C+%22idDomanda%22%3A%22181662%22+%7D";
			String resString1 = response1;
			if (response1 == null || response1.isEmpty()) {
				resString1 = objectMapper.writeValueAsString(new ArrayList<DomandaCollegata>());
			}
			Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrl1)), Mockito.eq(String.class))).thenReturn(resString1);
		} else {

			String serviceUrl = "http://localhost:9001/a4gfascicolo/api/v1/antimafia?params=%7B+%22azienda%22%3A%7B%22cuaa%22%3A%22SGNNLN54T28A372J%22%7D%2C+%22stato%22+%3A+%7B%22identificativo%22%3A+%22CONTROLLATA%22%7D%7D";
			JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/antimafia.json"));
			String resString = objectMapper.writeValueAsString(response);
			Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrl)), Mockito.eq(String.class))).thenReturn(resString);

			String serviceUrl1 = "http://localhost:8080/a4gistruttoria/api/v1/antimafia/domandecollegate?params=%7B+%22cuaa%22%3A%22SGNNLN54T28A372JX%22%2C+%22tipoDomanda%22%3A+%22DOMANDA_UNICA%22%2C+%22idDomanda%22%3A%22189674%22+%7D";
			String resString1 = response1;
			if (response1 == null || response1.isEmpty()) {
				resString1 = objectMapper.writeValueAsString(new ArrayList<DomandaCollegata>());
			}
			Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrl1)), Mockito.eq(String.class))).thenReturn(resString1);
		}

		DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(BigDecimal.valueOf(new Long(numeroDomanda)));
		assertThat(d).isNotNull();

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(
				d, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);

		controlloIntersostegnoManager.elabora(istruttoria.getId());
		
		istruttoria = istruttoriaDao.getOne(istruttoria.getId());
		entityManager.refresh(istruttoria);

		// return d;
		return istruttoria;
	}

	private Optional<VariabileCalcolo> getVariabileInput(DatiInput variabiliInput,
			TipoVariabile tipovariabile) {
		return variabiliInput.getVariabiliCalcolo().stream().filter(v -> v.getTipoVariabile().equals(tipovariabile)).findFirst();
	}

	private Optional<VariabileCalcolo> getVariabileOutput(DatiOutput variabili,
			TipoVariabile tipovariabile) {
		return variabili.getVariabiliCalcolo().stream().filter(v -> v.getTipoVariabile().equals(tipovariabile)).findFirst();
	}

	private Optional<VariabileCalcolo> getVariabileOutputPasso(DatiOutput variabiliOutput, TipoVariabile variabile) {
		Optional<VariabileCalcolo> optDffrpagdis = variabiliOutput.getVariabiliCalcolo().stream().filter(v -> v.getTipoVariabile().equals(variabile)).findFirst();
		return optDffrpagdis;
	}

	private Optional<PassoTransizioneModel> getPassoImportoAntimafia(Set<PassoTransizioneModel> passiTransizione) {
		Optional<PassoTransizioneModel> optPassoLavAntimafia = passiTransizione.stream().filter(p -> p.getCodicePasso().equals(TipologiaPassoTransizione.CONTROLLO_IMPORTO_ANTIMAFIA))
				.findFirst();
		return optPassoLavAntimafia;
	}

	private Optional<PassoTransizioneModel> getPassoImportoMinimo(Set<PassoTransizioneModel> passiTransizione) {
		Optional<PassoTransizioneModel> optPassoLavImportoMinimo = passiTransizione.stream()
				.filter(p -> p.getCodicePasso().equals(TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO))
				.findFirst();
		return optPassoLavImportoMinimo;
	}

	private Optional<PassoTransizioneModel> getPassoDisciplina(Set<PassoTransizioneModel> passiTransizione) {
		Optional<PassoTransizioneModel> optPassoLavImportoMinimo = passiTransizione.stream()
				.filter(p -> p.getCodicePasso().equals(TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA))
				.findFirst();
		return optPassoLavImportoMinimo;
	}

	private Optional<TransizioneIstruttoriaModel> caricaTransizioneIntersostegno(IstruttoriaModel istruttoria) {
		Optional<TransizioneIstruttoriaModel> transizioneSostegno = 
				istruttoria.getTransizioni().stream().filter(t -> Arrays.asList(StatoIstruttoria.NON_LIQUIDABILE,
						StatoIstruttoria.PAGAMENTO_NON_AUTORIZZATO,
						StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK).contains(StatoIstruttoria.valueOfByIdentificativo(t.getA4gdStatoLavSostegno1().getIdentificativo())))
				.sorted(Comparator.comparing(TransizioneIstruttoriaModel::getDataEsecuzione))
				.findFirst();
		return transizioneSostegno;
	}
}
