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
import java.util.stream.Collectors;

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
public class ControlloIntersostegnoServiceDisaccoppiatoTest extends MockIoItalia {

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
	// --- Importo: 2566.22
	// - Acs:
	// --- Stato: NON_RICHIESTO
	// - Acz:
	// --- Stato: NON_RICHIESTO
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
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
	}

	private Optional<PassoTransizioneModel> getPassoImportoMinimo(Set<PassoTransizioneModel> passiTransizione) {
		Optional<PassoTransizioneModel> optPassoLavImportoMinimo = passiTransizione.stream()
				.filter(p -> p.getCodicePasso().equals(TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO))
				.findFirst();
		return optPassoLavImportoMinimo;
	}

	// Dati test:
	// - Disaccoppiato:
	// --- Stato: LIQUIDABILE
	// --- Importo: 175.06
	// - Acs:
	// --- Stato: NON_RICHIESTO
	// - Acz:
	// --- Stato: NON_RICHIESTO
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2566.22}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'BRTSRG86A24L174X' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2566.22}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'BRTSRG86A24L174X' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void esecuzioneNoImportoMinimo() throws ControlloIntersostegnoException, Exception {
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
	// - Acs:
	// --- Stato: NON_RICHIESTO
	// - Acz:
	// --- Stato: NON_RICHIESTO
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2566.22}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":25001}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'BRTSRG86A24L174X' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2566.22}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":25001}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'BRTSRG86A24L174X' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void esecuzioneNoDomandeCollegateAntimafia() throws ControlloIntersostegnoException, Exception {
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
	// - Acs:
	// --- Stato: NON_RICHIESTO
	// - Acz:
	// --- Stato: NON_RICHIESTO
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2566.22}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":25001}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'BRTSRG86A24L174X' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2566.22}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":25001}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'BRTSRG86A24L174X' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void esecuzioneNoDichiarazioniAntimafia() throws ControlloIntersostegnoException, Exception {
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

	// Dati test:
	// - Disaccoppiato:
	// --- Stato: LIQUIDABILE
	// --- Importo: 25001
	// - Acs:
	// --- Stato: NON_RICHIESTO
	// - Acz:
	// --- Stato: NON_RICHIESTO
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2566.22}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":25001}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'BRTSRG86A24L174X' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2566.22}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":25001}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'BRTSRG86A24L174X' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void esecuzioneResponseDichiarazioniAntimafiaNull() throws ControlloIntersostegnoException, Exception {
		String numeroDomanda = "181662";

		String serviceUrl = "http://localhost:9001/a4gfascicolo/api/v1/antimafia?params=%7B%22azienda%22%3A%7B%22cuaa%22%3A%22BRTSRG86A24L174X%22%7D%7D";

		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrl)), Mockito.eq(String.class))).thenReturn(null);

		DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(BigDecimal.valueOf(new Long(numeroDomanda)));
		assertThat(d).isNotNull();

		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(d, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);

		controlloIntersostegnoManager.elabora(istruttoria.getId());

		Optional<TransizioneIstruttoriaModel> transizioneSostegno = 
			daoTransizioneSostegno.findTransizioneControlloIntersostegno(istruttoria).stream().findFirst();

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

	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2566.22}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":25001}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'BRTSRG86A24L174X' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void esecuzioneAntimafia() throws ControlloIntersostegnoException, Exception {
		JsonNode response1 = objectMapper.readTree(new File("src/test/resources/DomandaUnica/domandeCollegateResponse.json"));
		String resString1 = objectMapper.writeValueAsString(response1);

		IstruttoriaModel istruttoria = eseguiControlli("181662", resString1);
		Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

		assertThat(transizioneSostegno.isPresent()).isTrue();

		assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno2().getIdentificativo()).isEqualTo(StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria());
		assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo()).isEqualTo(StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK.getStatoIstruttoria());

		Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
		assertThat(passiTransizione.size()).isEqualTo(3);
		
		assertThat(passiTransizione.stream()
				.map(PassoTransizioneModel::getCodicePasso)
				.collect(Collectors.toList()))
		.anyMatch(passo -> TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO.equals(passo))
		.anyMatch(passo -> TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA.equals(passo))
		.anyMatch(passo -> TipologiaPassoTransizione.CONTROLLO_INTERSOSTEGNO.equals(passo));

		Optional<PassoTransizioneModel> optPassoLavImportoMinimo = getPassoImportoMinimo(passiTransizione);

		assertThat(optPassoLavImportoMinimo.isPresent()).isTrue();

		assertThat(optPassoLavImportoMinimo.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_042.getCodiceEsito());
	}

	/***********************************************************************************
	 * DISACCOPPIATO - ACS
	 ***********************************************************************************/

	// Dati test:
	// - Disaccoppiato:
	// --- Stato: LIQUIDABILE
	// --- Importo: 175.06
	// - Acs:
	// --- Stato: CONTROLLI_CALCOLO_OK
	// --- Importo: 500
	// - Acz:
	// --- Stato: NON_RICHIESTO
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":500.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":500.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "UPDATE A4GT_ISTRUTTORIA set ID_STATO_LAVORAZIONE = (select id from a4gd_stato_lav_sostegno where identificativo = 'CONTROLLI_CALCOLO_OK') where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 189674) and SOSTEGNO = 'SUPERFICIE'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void esecuzioneNoImportoMinimoConACS_CONTROLLI_CALCOLO_OK() throws ControlloIntersostegnoException, Exception {
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
	// --- Importo: 175.06
	// - Acs:
	// --- Stato: LIQUIDABILE
	// --- Importo: 50
	// - Acz:
	// --- Stato: NON_RICHIESTO
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":50.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":50.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void esecuzioneNoImportoMinimoConACS_LIQUIDABILE() throws ControlloIntersostegnoException, Exception {
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
	// - Acs:
	// --- Stato: NON_AMMISSIBILE
	// --- Importo: 500
	// - Acz:
	// --- Stato: NON_RICHIESTO
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":500.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":500.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "UPDATE A4GT_ISTRUTTORIA set ID_STATO_LAVORAZIONE = (select id from a4gd_stato_lav_sostegno where identificativo = 'NON_AMMISSIBILE') where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 189674) and SOSTEGNO = 'SUPERFICIE'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void esecuzioneNoImportoMinimoConACS_NON_AMMISSIBILE() throws ControlloIntersostegnoException, Exception {
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
	// - Acs:
	// --- Stato: NON_LIQUIDABILE
	// --- Importo: 500
	// - Acz:
	// --- Stato: NON_RICHIESTO
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":500.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":500.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "UPDATE A4GT_ISTRUTTORIA set ID_STATO_LAVORAZIONE = (select id from a4gd_stato_lav_sostegno where identificativo = 'NON_LIQUIDABILE') where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 189674) and SOSTEGNO = 'SUPERFICIE'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void esecuzioneNoImportoMinimoConACS_NON_LIQUIDABILE() throws ControlloIntersostegnoException, Exception {
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
	// - Acs:
	// --- Stato: LIQUIDABILE
	// --- Importo: 500
	// - Acz:
	// --- Stato: NON_RICHIESTO
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":500.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":500.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void esecuzionePagamentoAutorizzatoConACS_LIQUIDABILE() throws ControlloIntersostegnoException, Exception {
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
	}

	// Dati test:
	// - Disaccoppiato:
	// --- Stato: LIQUIDABILE
	// --- Importo: 175.06
	// - Acs:
	// --- Stato: LIQUIDABILE
	// --- Importo: 25000
	// - Acz:
	// --- Stato: NON_RICHIESTO
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":25000.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":25000.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void esecuzioneNoAntimafiaConACS_LIQUIDABILE() throws ControlloIntersostegnoException, Exception {
		IstruttoriaModel istruttoria = eseguiControlli("189674", "");
		Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

		assertThat(transizioneSostegno.isPresent()).isTrue();

		assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno2().getIdentificativo()).isEqualTo(StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria());

		assertEquals(StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK.getStatoIstruttoria(), transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo());

		Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
		assertThat(passiTransizione.size()).isEqualTo(3);

		Optional<PassoTransizioneModel> optPassoLavImportoMinimo = passiTransizione.stream().filter(p -> p.getCodicePasso().equals(TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO))
				.findFirst();

		Optional<PassoTransizioneModel> optPassoLavAntimafia = getPassoImportoAntimafia(passiTransizione);

		assertThat(optPassoLavImportoMinimo.isPresent()).isTrue();
		assertThat(optPassoLavAntimafia.isPresent()).isFalse();

		assertThat(optPassoLavImportoMinimo.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_049.getCodiceEsito());
		//assertThat(optPassoLavAntimafia-is.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_058.getCodiceEsito());
	}

	// Dati test:
	// - Disaccoppiato:
	// --- Stato: LIQUIDABILE
	// --- Importo: 500
	// - Acs:
	// --- Stato: LIQUIDABILE
	// --- Importo: 25000
	// - Acz:
	// --- Stato: NON_RICHIESTO
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":500.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":25000.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "UPDATE A4GT_ISTRUTTORIA set ID_STATO_LAVORAZIONE = (select id from a4gd_stato_lav_sostegno where identificativo = 'NON_AMMISSIBILE') where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 189674) and SOSTEGNO = 'SUPERFICIE'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void esecuzioneOkAntimafiaConACS_NON_AMMISSIBILE_ACZ_NON_RICHIESTO() throws ControlloIntersostegnoException, Exception {
		IstruttoriaModel istruttoria = eseguiControlli("189674", "");
		Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

		assertThat(transizioneSostegno.isPresent()).isTrue();

		assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno2().getIdentificativo()).isEqualTo(StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria());
		assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo()).isEqualTo(StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK.getStatoIstruttoria());

		Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
		assertEquals(3, passiTransizione.size());

		Optional<PassoTransizioneModel> optPassoLavImportoMinimo = getPassoImportoMinimo(passiTransizione);

		Optional<PassoTransizioneModel> optPassoLavAntimafia = getPassoImportoAntimafia(passiTransizione);

		assertThat(optPassoLavImportoMinimo.isPresent()).isTrue();
		assertThat(optPassoLavAntimafia.isPresent()).isFalse();

		assertThat(optPassoLavImportoMinimo.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_042.getCodiceEsito());
		//assertThat(optPassoLavAntimafia.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_059.getCodiceEsito());
	}

	/***********************************************************************************
	 * DISACCOPPIATO - ACZ
	 ***********************************************************************************/

	// Dati test:
	// - Disaccoppiato:
	// --- Stato: LIQUIDABILE
	// --- Importo: 175.06
	// - Acs:
	// --- Stato: NON_RICHIESTO
	// - Acz:
	// --- Stato: CONTROLLI_CALCOLO_OK
	// --- Importo: 500
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acz.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACZIMPCALCTOT\",\"valNumber\":180.01}', '{\"tipoVariabile\":\"ACZIMPCALCTOT\",\"valNumber\":500.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'ZOOTECNIA') and CODICE_PASSO = 'CALCOLO_ACZ'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACZIMPCALCLORDOTOT\",\"valNumber\":180.01}', '{\"tipoVariabile\":\"ACZIMPCALCLORDOTOT\",\"valNumber\":500.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'ZOOTECNIA') and CODICE_PASSO = 'CALCOLO_ACZ'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_acz.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "UPDATE A4GT_ISTRUTTORIA set ID_STATO_LAVORAZIONE = (select id from a4gd_stato_lav_sostegno where identificativo = 'CONTROLLI_CALCOLO_OK') where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 189674) and SOSTEGNO = 'ZOOTECNIA'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acz_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void esecuzioneNoImportoMinimoConACZ_CONTROLLI_CALCOLO_OK() throws ControlloIntersostegnoException, Exception {
		IstruttoriaModel istruttoria = eseguiControlli("189674", "");
		Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

		assertThat(transizioneSostegno.isPresent()).isTrue();

		assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno2().getIdentificativo()).isEqualTo(StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria());

		assertEquals(StatoIstruttoria.PAGAMENTO_NON_AUTORIZZATO.getStatoIstruttoria(), transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo());

		Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
		assertEquals(2, passiTransizione.size());

		Optional<PassoTransizioneModel> optPassoLavImportoMinimo = passiTransizione.stream().filter(p -> p.getCodicePasso().equals(TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO))
				.findFirst();

		assertThat(optPassoLavImportoMinimo.isPresent()).isTrue();

		assertThat(optPassoLavImportoMinimo.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_046.getCodiceEsito());
	}

	// Dati test:
	// - Disaccoppiato:
	// --- Stato: LIQUIDABILE
	// --- Importo: 175.06
	// - Acs:
	// --- Stato: NON_RICHIESTO
	// - Acz:
	// --- Stato: LIQUIDABILE
	// --- Importo: 50
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acz.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACZIMPCALCTOT\",\"valNumber\":180.01}', '{\"tipoVariabile\":\"ACZIMPCALCTOT\",\"valNumber\":50.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'ZOOTECNIA') and CODICE_PASSO = 'CALCOLO_ACZ'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACZIMPCALCLORDOTOT\",\"valNumber\":180.01}', '{\"tipoVariabile\":\"ACZIMPCALCLORDOTOT\",\"valNumber\":50.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'ZOOTECNIA') and CODICE_PASSO = 'CALCOLO_ACZ'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_acz.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acz_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void esecuzioneNoImportoMinimoConACZ_LIQUIDABILE() throws ControlloIntersostegnoException, Exception {
		IstruttoriaModel istruttoria = eseguiControlli("189674", "");
		Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

		assertThat(transizioneSostegno.isPresent()).isTrue();

		assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno2().getIdentificativo()).isEqualTo(StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria());

		assertEquals(StatoIstruttoria.NON_LIQUIDABILE.getStatoIstruttoria(), transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo());

		Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
		assertEquals(2, passiTransizione.size());

		Optional<PassoTransizioneModel> optPassoLavImportoMinimo = passiTransizione.stream().filter(p -> p.getCodicePasso().equals(TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO))
				.findFirst();

		assertThat(optPassoLavImportoMinimo.isPresent()).isTrue();

		assertThat(optPassoLavImportoMinimo.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_051.getCodiceEsito());
	}

	// Dati test:
	// - Disaccoppiato:
	// --- Stato: LIQUIDABILE
	// --- Importo: 175.06
	// - Acs:
	// --- Stato: NON_RICHIESTO
	// - Acz:
	// --- Stato: NON_AMMISSIBILE
	// --- Importo: 500
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acz.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACZIMPCALCTOT\",\"valNumber\":180.01}', '{\"tipoVariabile\":\"ACZIMPCALCTOT\",\"valNumber\":500.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'ZOOTECNIA') and CODICE_PASSO = 'CALCOLO_ACZ'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACZIMPCALCLORDOTOT\",\"valNumber\":180.01}', '{\"tipoVariabile\":\"ACZIMPCALCLORDOTOT\",\"valNumber\":500.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'ZOOTECNIA') and CODICE_PASSO = 'CALCOLO_ACZ'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_acz.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "UPDATE A4GT_ISTRUTTORIA set ID_STATO_LAVORAZIONE = (select id from a4gd_stato_lav_sostegno where identificativo = 'NON_AMMISSIBILE') where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 189674) and SOSTEGNO = 'ZOOTECNIA'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acz_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void esecuzioneNoImportoMinimoConACZ_NON_AMMISSIBILE() throws ControlloIntersostegnoException, Exception {
		IstruttoriaModel istruttoria = eseguiControlli("189674", "");
		Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

		assertThat(transizioneSostegno.isPresent()).isTrue();

		assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno2().getIdentificativo()).isEqualTo(StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria());

		assertEquals(StatoIstruttoria.NON_LIQUIDABILE.getStatoIstruttoria(), transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo());

		Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
		assertEquals(2, passiTransizione.size());

		Optional<PassoTransizioneModel> optPassoLavImportoMinimo = passiTransizione.stream().filter(p -> p.getCodicePasso().equals(TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO))
				.findFirst();

		assertThat(optPassoLavImportoMinimo.isPresent()).isTrue();

		assertThat(optPassoLavImportoMinimo.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_051.getCodiceEsito());
	}

	// Dati test:
	// - Disaccoppiato:
	// --- Stato: LIQUIDABILE
	// --- Importo: 175.06
	// - Acs:
	// --- Stato: NON_RICHIESTO
	// - Acz:
	// --- Stato: LIQUIDABILE
	// --- Importo: 50
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acz.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACZIMPCALCTOT\",\"valNumber\":180.01}', '{\"tipoVariabile\":\"ACZIMPCALCTOT\",\"valNumber\":50.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'ZOOTECNIA') and CODICE_PASSO = 'CALCOLO_ACZ'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACZIMPCALCLORDOTOT\",\"valNumber\":180.01}', '{\"tipoVariabile\":\"ACZIMPCALCLORDOTOT\",\"valNumber\":50.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'ZOOTECNIA') and CODICE_PASSO = 'CALCOLO_ACZ'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_acz.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "UPDATE A4GT_ISTRUTTORIA set ID_STATO_LAVORAZIONE = (select id from a4gd_stato_lav_sostegno where identificativo = 'NON_LIQUIDABILE') where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 189674) and SOSTEGNO = 'ZOOTECNIA'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acz_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void esecuzioneNoImportoMinimoConACZ_NON_LIQUIDABILE() throws ControlloIntersostegnoException, Exception {
		IstruttoriaModel istruttoria = eseguiControlli("189674", "");
		Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

		assertThat(transizioneSostegno.isPresent()).isTrue();

		assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno2().getIdentificativo()).isEqualTo(StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria());

		assertEquals(StatoIstruttoria.NON_LIQUIDABILE.getStatoIstruttoria(), transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo());

		Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
		assertEquals(2, passiTransizione.size());

		Optional<PassoTransizioneModel> optPassoLavImportoMinimo = passiTransizione.stream().filter(p -> p.getCodicePasso().equals(TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO))
				.findFirst();

		assertThat(optPassoLavImportoMinimo.isPresent()).isTrue();

		assertThat(optPassoLavImportoMinimo.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_051.getCodiceEsito());
	}

	// Dati test:
	// - Disaccoppiato:
	// --- Stato: LIQUIDABILE
	// --- Importo: 175.06
	// - Acs:
	// --- Stato: NON_RICHIESTO
	// - Acz:
	// --- Stato: LIQUIDABILE
	// --- Importo: 25000
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acz.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACZIMPCALCTOT\",\"valNumber\":180.01}', '{\"tipoVariabile\":\"ACZIMPCALCTOT\",\"valNumber\":25000.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'ZOOTECNIA') and CODICE_PASSO = 'CALCOLO_ACZ'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACZIMPCALCLORDOTOT\",\"valNumber\":180.01}', '{\"tipoVariabile\":\"ACZIMPCALCLORDOTOT\",\"valNumber\":25000.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'ZOOTECNIA') and CODICE_PASSO = 'CALCOLO_ACZ'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_acz.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acz_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void esecuzioneNoAntimafiaConACZ_LIQUIDABILE_Sup25000() throws ControlloIntersostegnoException, Exception {
		IstruttoriaModel istruttoria = eseguiControlli("189674", "");
		Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

		assertThat(transizioneSostegno.isPresent()).isTrue();

		assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno2().getIdentificativo()).isEqualTo(StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria());

		assertEquals(StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK.getStatoIstruttoria(), transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo());

		Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
		assertThat(passiTransizione.size()).isEqualTo(3);

		Optional<PassoTransizioneModel> optPassoLavImportoMinimo = passiTransizione.stream().filter(p -> p.getCodicePasso().equals(TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO))
				.findFirst();

		Optional<PassoTransizioneModel> optPassoLavAntimafia = getPassoImportoAntimafia(passiTransizione);

		assertThat(optPassoLavImportoMinimo.isPresent()).isTrue();
		assertThat(optPassoLavAntimafia.isPresent()).isFalse();

		assertThat(optPassoLavImportoMinimo.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_044.getCodiceEsito());
		//assertThat(optPassoLavAntimafia.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_056.getCodiceEsito());
	}

	// Dati test:
	// - Disaccoppiato:
	// --- Stato: LIQUIDABILE
	// --- Importo: 175.06
	// - Acs:
	// --- Stato: NON_RICHIESTO
	// - Acz:
	// --- Stato: LIQUIDABILE
	// --- Importo: 2500
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acz.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACZIMPCALCTOT\",\"valNumber\":180.01}', '{\"tipoVariabile\":\"ACZIMPCALCTOT\",\"valNumber\":2500.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'ZOOTECNIA') and CODICE_PASSO = 'CALCOLO_ACZ'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACZIMPCALCLORDOTOT\",\"valNumber\":180.01}', '{\"tipoVariabile\":\"ACZIMPCALCLORDOTOT\",\"valNumber\":2500.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'ZOOTECNIA') and CODICE_PASSO = 'CALCOLO_ACZ'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_acz.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acz_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void esecuzioneNoAntimafiaConACZ_LIQUIDABILE_Inf25000() throws ControlloIntersostegnoException, Exception {
		IstruttoriaModel istruttoria = eseguiControlli("189674", "");
		Optional<TransizioneIstruttoriaModel> transizioneSostegno = 
			daoTransizioneSostegno.findTransizioneControlloIntersostegno(istruttoria).stream().findFirst();

		assertThat(transizioneSostegno.isPresent()).isTrue();

		assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno2().getIdentificativo()).isEqualTo(StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria());

		assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo()).isEqualTo(StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK.getStatoIstruttoria());

		Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
		assertEquals(3, passiTransizione.size());

		Optional<PassoTransizioneModel> optPassoLavImportoMinimo = passiTransizione.stream().filter(p -> p.getCodicePasso().equals(TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO))
				.findFirst();

		Optional<PassoTransizioneModel> optPassoLavAntimafia = getPassoImportoAntimafia(passiTransizione);

		assertThat(optPassoLavImportoMinimo.isPresent()).isTrue();
		assertThat(optPassoLavAntimafia.isPresent()).isFalse();

		assertThat(optPassoLavImportoMinimo.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_044.getCodiceEsito());
		//assertThat(optPassoLavAntimafia.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_063.getCodiceEsito());

	}

	// Dati test:
	// - Disaccoppiato:
	// --- Stato: LIQUIDABILE
	// --- Importo: 1500
	// - Acs:
	// --- Stato: NON_RICHIESTO
	// - Acz:
	// --- Stato: NON_LIQUIDABILE
	// --- Importo: 27000
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acz.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":1500.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACZIMPCALCTOT\",\"valNumber\":180.01}', '{\"tipoVariabile\":\"ACZIMPCALCTOT\",\"valNumber\":27000.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'ZOOTECNIA') and CODICE_PASSO = 'CALCOLO_ACZ'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_acz.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "UPDATE A4GT_ISTRUTTORIA set ID_STATO_LAVORAZIONE = (select id from a4gd_stato_lav_sostegno where identificativo = 'NON_LIQUIDABILE') where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 189674) and SOSTEGNO = 'ZOOTECNIA'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acz_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void esecuzioneOkAntimafiaConACZ_NON_LIQUIDABILE_Inf25000() throws ControlloIntersostegnoException, Exception {
		IstruttoriaModel istruttoria = eseguiControlli("189674", "");
		Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

		assertThat(transizioneSostegno.isPresent()).isTrue();

		assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno2().getIdentificativo()).isEqualTo(StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria());

		assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo()).isEqualTo(StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK.getStatoIstruttoria());

		Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
		assertEquals(3, passiTransizione.size());

		Optional<PassoTransizioneModel> optPassoLavImportoMinimo = passiTransizione.stream().filter(p -> p.getCodicePasso().equals(TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO))
				.findFirst();

		Optional<PassoTransizioneModel> optPassoLavAntimafia = getPassoImportoAntimafia(passiTransizione);

		assertThat(optPassoLavImportoMinimo.isPresent()).isTrue();
		assertThat(optPassoLavAntimafia.isPresent()).isFalse();

		assertThat(optPassoLavImportoMinimo.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_042.getCodiceEsito());
		//assertThat(optPassoLavAntimafia.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_063.getCodiceEsito());

	}

	// Dati test:
	// - Disaccoppiato:
	// --- Stato: LIQUIDABILE
	// --- Importo: 1500
	// - Acs:
	// --- Stato: NON_RICHIESTO
	// - Acz:
	// --- Stato: NON_AMMISSIBILE
	// --- Importo: 27000
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acz.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":1500.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACZIMPCALCTOT\",\"valNumber\":180.01}', '{\"tipoVariabile\":\"ACZIMPCALCTOT\",\"valNumber\":27000.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'ZOOTECNIA') and CODICE_PASSO = 'CALCOLO_ACZ'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_acz.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "UPDATE A4GT_ISTRUTTORIA set ID_STATO_LAVORAZIONE = (select id from a4gd_stato_lav_sostegno where identificativo = 'NON_AMMISSIBILE') where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 189674) and SOSTEGNO = 'ZOOTECNIA'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acz_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void esecuzioneOkAntimafiaConACZ_NON_AMMISSIBILE_Inf25000() throws ControlloIntersostegnoException, Exception {
		IstruttoriaModel istruttoria = eseguiControlli("189674", "");
		Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

		assertThat(transizioneSostegno.isPresent()).isTrue();

		assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno2().getIdentificativo()).isEqualTo(StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria());

		assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo()).isEqualTo(StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK.getStatoIstruttoria());

		Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
		assertEquals(3, passiTransizione.size());

		Optional<PassoTransizioneModel> optPassoLavImportoMinimo = passiTransizione.stream().filter(p -> p.getCodicePasso().equals(TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO))
				.findFirst();

		Optional<PassoTransizioneModel> optPassoLavAntimafia = getPassoImportoAntimafia(passiTransizione);

		assertThat(optPassoLavImportoMinimo.isPresent()).isTrue();
		assertThat(optPassoLavAntimafia.isPresent()).isFalse();

		assertThat(optPassoLavImportoMinimo.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_042.getCodiceEsito());
		//assertThat(optPassoLavAntimafia.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_063.getCodiceEsito());

	}

	// Dati test:
	// - Disaccoppiato:
	// --- Stato: LIQUIDABILE
	// --- Importo: 500
	// - Acs:
	// --- Stato: NON_RICHIESTO
	// - Acz:
	// --- Stato: CONTROLLI_CALCOLO_OK
	// --- Importo: 25000
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acz.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":500.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACZIMPCALCTOT\",\"valNumber\":180.01}', '{\"tipoVariabile\":\"ACZIMPCALCTOT\",\"valNumber\":25000.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'ZOOTECNIA') and CODICE_PASSO = 'CALCOLO_ACZ'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_acz.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "UPDATE A4GT_ISTRUTTORIA set ID_STATO_LAVORAZIONE = (select id from a4gd_stato_lav_sostegno where identificativo = 'CONTROLLI_CALCOLO_OK') where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 189674) and SOSTEGNO = 'ZOOTECNIA'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acz_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void esecuzioneOkAntimafiaConACZ_CONTROLLI_CALCOLO_OK() throws ControlloIntersostegnoException, Exception {
		IstruttoriaModel istruttoria = eseguiControlli("189674", "");
		Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

		assertThat(transizioneSostegno.isPresent()).isTrue();

		assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno2().getIdentificativo()).isEqualTo(StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria());
		assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo()).isEqualTo(StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK.getStatoIstruttoria());

		Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
		assertThat(passiTransizione.size()).isEqualTo(3);

		Optional<PassoTransizioneModel> optPassoLavImportoMinimo = getPassoImportoMinimo(passiTransizione);

		Optional<PassoTransizioneModel> optPassoLavAntimafia = getPassoImportoAntimafia(passiTransizione);

		assertThat(optPassoLavImportoMinimo.isPresent()).isTrue();
		assertThat(optPassoLavAntimafia.isPresent()).isFalse();

		assertThat(optPassoLavImportoMinimo.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_042.getCodiceEsito());
		//assertThat(optPassoLavAntimafia.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_060.getCodiceEsito());
	}

	private Optional<PassoTransizioneModel> getPassoImportoAntimafia(Set<PassoTransizioneModel> passiTransizione) {
		Optional<PassoTransizioneModel> optPassoLavAntimafia = passiTransizione.stream().filter(p -> p.getCodicePasso().equals(TipologiaPassoTransizione.CONTROLLO_IMPORTO_ANTIMAFIA))
				.findFirst();
		return optPassoLavAntimafia;
	}

	/***********************************************************************************
	 * DISCIPLINA FINANZIARIA - DISACCOPPIATO - ACS
	 ***********************************************************************************/

	// Dati test:
	// - Disaccoppiato:
	// --- Stato: LIQUIDABILE
	// --- Importo: 1000
	// - Acs:
	// --- Stato: CONTROLLI_INTERSOSTEGNO_OK
	// --- Importo: 1200
	// --- Disciplina già applicata: 1200
	// - Acz:
	// --- Stato: NON_RICHIESTO
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":1000.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":1000.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":1200.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":1200.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/domanda_Disciplina_Finanziaria_Acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"DFFRPAGACS\",\"valNumber\":1746.01}', '{\"tipoVariabile\":\"DFFRPAGACS\",\"valNumber\":1200.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE' AND ID_STATO_INIZIALE = (SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'LIQUIDABILE')) and CODICE_PASSO = 'DISCIPLINA_FINANZIARIA'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void esecuzioneDisciplinaFinanziariaDisaccoppiato_AccSuperfici_CONTROLLI_INTERSOSTEGNO_OK() throws Exception {
		IstruttoriaModel istruttoria = eseguiControlli("189674", "");
		Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

		assertThat(transizioneSostegno.isPresent()).isTrue();

		Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
		Optional<PassoTransizioneModel> optPassoDisciplinaFinanziaria = getPassoDisciplinaFinanziaria(passiTransizione);

		assertThat(optPassoDisciplinaFinanziaria.isPresent()).isTrue();

		if (optPassoDisciplinaFinanziaria.isPresent()) {
			PassoTransizioneModel passo = optPassoDisciplinaFinanziaria.get();
			DatiInput variabiliInput = objectMapper.readValue(passo.getDatiInput(), DatiInput.class);
			DatiOutput variabiliOutput = objectMapper.readValue(passo.getDatiOutput(), DatiOutput.class);

			// disciplina già applicata a ACS
			Optional<VariabileCalcolo> optDffrappacs = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPACS);
			// disciplina da applicare al disaccoppiato
			Optional<VariabileCalcolo> optDffrpagdis = getVariabileOutputPasso(variabiliOutput, TipoVariabile.DFFRPAGDIS);

			assertThat(optDffrappacs).isPresent();
			assertThat(optDffrpagdis).isPresent();

			if (optDffrappacs.isPresent()) {
				VariabileCalcolo var = optDffrappacs.get();
				assertEquals(BigDecimal.valueOf(1200).setScale(2, RoundingMode.FLOOR), var.getValNumber());
			}

			if (optDffrpagdis.isPresent()) {
				VariabileCalcolo var = optDffrpagdis.get();
				assertEquals(BigDecimal.valueOf(800).setScale(2, RoundingMode.FLOOR), var.getValNumber());
			}
		}

	}

	private Optional<VariabileCalcolo> getVariabileOutputPasso(DatiOutput variabiliOutput, TipoVariabile variabile) {
		Optional<VariabileCalcolo> optDffrpagdis = variabiliOutput.getVariabiliCalcolo().stream().filter(v -> v.getTipoVariabile().equals(variabile)).findFirst();
		return optDffrpagdis;
	}

	// Dati test:
	// - Disaccoppiato:
	// --- Stato: LIQUIDABILE
	// --- Importo: 1000
	// - Acs:
	// --- Stato: PAGAMENTO_AUTORIZZATO
	// --- Importo: 1200
	// --- Disciplina già applicata: 1200
	// - Acz:
	// --- Stato: NON_RICHIESTO
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":1000.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":1200.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/domanda_Disciplina_Finanziaria_Acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"DFFRPAGACS\",\"valNumber\":1746.01}', '{\"tipoVariabile\":\"DFFRPAGACS\",\"valNumber\":1200.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and SOSTEGNO = 'SUPERFICIE' AND ID_STATO_INIZIALE = (SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'LIQUIDABILE')) and CODICE_PASSO = 'DISCIPLINA_FINANZIARIA'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "UPDATE A4GT_ISTRUTTORIA set ID_STATO_LAVORAZIONE = (select id from a4gd_stato_lav_sostegno where identificativo = 'PAGAMENTO_AUTORIZZATO') where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 189674) and SOSTEGNO = 'SUPERFICIE'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void esecuzioneDisciplinaFinanziariaDisaccoppiato_AccSuperfici_PAGAMENTO_AUTORIZZATO() throws Exception {

		IstruttoriaModel istruttoria = eseguiControlli("189674", "");
		Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

		assertThat(transizioneSostegno.isPresent()).isTrue();

		Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
		Optional<PassoTransizioneModel> optPassoDisciplinaFinanziaria = getPassoDisciplinaFinanziaria(passiTransizione);

		assertThat(optPassoDisciplinaFinanziaria.isPresent()).isTrue();

		if (optPassoDisciplinaFinanziaria.isPresent()) {
			PassoTransizioneModel passo = optPassoDisciplinaFinanziaria.get();
			DatiInput variabiliInput = objectMapper.readValue(passo.getDatiInput(), DatiInput.class);
			DatiOutput variabiliOutput = objectMapper.readValue(passo.getDatiOutput(), DatiOutput.class);

			// disciplina già applicata a ACS
			Optional<VariabileCalcolo> optDffrappacs = variabiliInput.getVariabiliCalcolo().stream().filter(v -> v.getTipoVariabile().equals(TipoVariabile.DFFRAPPACS)).findFirst();
			// disciplina da applicare al disaccoppiato
			Optional<VariabileCalcolo> optDffrpagdis = variabiliOutput.getVariabiliCalcolo().stream().filter(v -> v.getTipoVariabile().equals(TipoVariabile.DFFRPAGDIS)).findFirst();

			assertEquals(true, optDffrappacs.isPresent());
			assertEquals(true, optDffrpagdis.isPresent());

			if (optDffrappacs.isPresent()) {
				VariabileCalcolo var = optDffrappacs.get();
				assertEquals(BigDecimal.valueOf(1200).setScale(2, RoundingMode.FLOOR), var.getValNumber());
			}

			if (optDffrpagdis.isPresent()) {
				VariabileCalcolo var = optDffrpagdis.get();
				assertEquals(BigDecimal.valueOf(800).setScale(2, RoundingMode.FLOOR), var.getValNumber());
			}
		}

	}

	/***********************************************************************************
	 * DISCIPLINA FINANZIARIA - DISACCOPPIATO - ACZ
	 ***********************************************************************************/

	// Dati test:
	// - Disaccoppiato:
	// --- Stato: LIQUIDABILE
	// --- Importo: 1000
	// - Acs:
	// --- Stato: NON_RICHIESTO
	// - Acz:
	// --- Stato: CONTROLLI_INTERSOSTEGNO_OK
	// --- Importo: 1200
	// --- Disciplina già applicata: 1200
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acz.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"BPSIMPCALCFIN\",\"valNumber\":1908.61}', '{\"tipoVariabile\":\"BPSIMPCALCFIN\",\"valNumber\":900.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"GREIMPCALCFIN\",\"valNumber\":1017.93}', '{\"tipoVariabile\":\"GREIMPCALCFIN\",\"valNumber\":100.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":1000.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACZIMPCALCTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACZIMPCALCTOT\",\"valNumber\":1200.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'ZOOTECNIA') and CODICE_PASSO = 'CALCOLO_ACZ'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_acz.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/domanda_Disciplina_Finanziaria_Acz.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"DFFRPAGACZ\",\"valNumber\":2000.00}', '{\"tipoVariabile\":\"DFFRPAGACZ\",\"valNumber\":1200.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and SOSTEGNO = 'ZOOTECNIA' AND ID_STATO_INIZIALE = (SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'LIQUIDABILE')) and CODICE_PASSO = 'DISCIPLINA_FINANZIARIA'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acz_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void esecuzioneDisciplinaFinanziariaDisaccoppiato_AccZootecnia_CONTROLLI_INTERSOSTEGNO_OK() throws Exception {

		IstruttoriaModel istruttoria = eseguiControlli("189674", "");
		Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

		assertThat(transizioneSostegno.isPresent()).isTrue();

		Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
		Optional<PassoTransizioneModel> optPassoDisciplinaFinanziaria = getPassoDisciplinaFinanziaria(passiTransizione);

		assertThat(optPassoDisciplinaFinanziaria.isPresent()).isTrue();

		if (optPassoDisciplinaFinanziaria.isPresent()) {
			PassoTransizioneModel passo = optPassoDisciplinaFinanziaria.get();
			DatiInput variabiliInput = objectMapper.readValue(passo.getDatiInput(), DatiInput.class);
			DatiOutput variabiliOutput = objectMapper.readValue(passo.getDatiOutput(), DatiOutput.class);

			// disciplina già applicata a ACZ
			Optional<VariabileCalcolo> optDffrappacz = variabiliInput.getVariabiliCalcolo().stream().filter(v -> v.getTipoVariabile().equals(TipoVariabile.DFFRAPPACZ)).findFirst();
			// disciplina da applicare al disaccoppiato
			Optional<VariabileCalcolo> optDffrpagdis = variabiliOutput.getVariabiliCalcolo().stream().filter(v -> v.getTipoVariabile().equals(TipoVariabile.DFFRPAGDIS)).findFirst();

			assertEquals(true, optDffrappacz.isPresent());
			assertEquals(true, optDffrpagdis.isPresent());

			if (optDffrappacz.isPresent()) {
				VariabileCalcolo var = optDffrappacz.get();
				assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.FLOOR), var.getValNumber());
			}

			if (optDffrpagdis.isPresent()) {
				VariabileCalcolo var = optDffrpagdis.get();
				assertEquals(BigDecimal.valueOf(1000).setScale(2, RoundingMode.FLOOR), var.getValNumber());
			}
		}

	}

	// Dati test:
	// - Disaccoppiato:
	// --- Stato: LIQUIDABILE
	// --- Importo: 1000
	// - Acs:
	// --- Stato: NON_RICHIESTO
	// - Acz:
	// --- Stato: PAGAMENTO_AUTORIZZATO
	// --- Importo: 1200
	// --- Disciplina già applicata: 1200
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acz.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":1000.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACZIMPCALCTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACZIMPCALCTOT\",\"valNumber\":1200.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'ZOOTECNIA') and CODICE_PASSO = 'CALCOLO_ACZ'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_acz.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/domanda_Disciplina_Finanziaria_Acz.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"DFFRPAGACZ\",\"valNumber\":2000.00}', '{\"tipoVariabile\":\"DFFRPAGACZ\",\"valNumber\":1200.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and SOSTEGNO = 'ZOOTECNIA' AND ID_STATO_INIZIALE = (SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'LIQUIDABILE')) and CODICE_PASSO = 'DISCIPLINA_FINANZIARIA'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "UPDATE A4GT_ISTRUTTORIA set ID_STATO_LAVORAZIONE = (select id from a4gd_stato_lav_sostegno where identificativo = 'PAGAMENTO_AUTORIZZATO') where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 189674) and SOSTEGNO = 'ZOOTECNIA'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acz_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void esecuzioneDisciplinaFinanziariaDisaccoppiato_AccZootecnia_PAGAMENTO_AUTORIZZATO() throws Exception {

		IstruttoriaModel istruttoria = eseguiControlli("189674", "");
		Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

		assertThat(transizioneSostegno.isPresent()).isTrue();

		Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
		Optional<PassoTransizioneModel> optPassoDisciplinaFinanziaria = getPassoDisciplinaFinanziaria(passiTransizione);

		assertThat(optPassoDisciplinaFinanziaria.isPresent()).isTrue();

		if (optPassoDisciplinaFinanziaria.isPresent()) {
			PassoTransizioneModel passo = optPassoDisciplinaFinanziaria.get();
			DatiInput variabiliInput = objectMapper.readValue(passo.getDatiInput(), DatiInput.class);
			DatiOutput variabiliOutput = objectMapper.readValue(passo.getDatiOutput(), DatiOutput.class);

			// disciplina già applicata a ACZ
			Optional<VariabileCalcolo> optDffrappacz = variabiliInput.getVariabiliCalcolo().stream().filter(v -> v.getTipoVariabile().equals(TipoVariabile.DFFRAPPACZ)).findFirst();
			// disciplina da applicare al disaccoppiato
			Optional<VariabileCalcolo> optDffrpagdis = variabiliOutput.getVariabiliCalcolo().stream().filter(v -> v.getTipoVariabile().equals(TipoVariabile.DFFRPAGDIS)).findFirst();

			assertEquals(true, optDffrappacz.isPresent());
			assertEquals(true, optDffrpagdis.isPresent());

			if (optDffrappacz.isPresent()) {
				VariabileCalcolo var = optDffrappacz.get();
				assertEquals(BigDecimal.valueOf(1200).setScale(2, RoundingMode.FLOOR), var.getValNumber());
			}

			if (optDffrpagdis.isPresent()) {
				VariabileCalcolo var = optDffrpagdis.get();
				assertEquals(BigDecimal.valueOf(800).setScale(2, RoundingMode.FLOOR), var.getValNumber());
			}
		}

	}

	// Dati test:
	// - Disaccoppiato:
	// --- Stato: LIQUIDABILE
	// --- Importo: 1000
	// - Acs:
	// --- Stato: CONTROLLI_INTERSOSTEGNO_OK
	// - Acz:
	// --- Stato: PAGAMENTO_AUTORIZZATO
	// --- Importo: 1200
	// --- Disciplina già applicata: 1200
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs_Acz.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":700.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":700.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":1000.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":1000.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACZIMPCALCTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACZIMPCALCTOT\",\"valNumber\":1200.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'ZOOTECNIA') and CODICE_PASSO = 'CALCOLO_ACZ'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACZIMPCALCLORDOTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACZIMPCALCLORDOTOT\",\"valNumber\":1200.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'ZOOTECNIA') and CODICE_PASSO = 'CALCOLO_ACZ'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_acs_acz.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/domanda_Disciplina_Finanziaria_Acz.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"DFFRPAGACS\",\"valNumber\":1746.01}', '{\"tipoVariabile\":\"DFFRPAGACS\",\"valNumber\":700.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and SOSTEGNO = 'SUPERFICIE' AND ID_STATO_INIZIALE = (SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'LIQUIDABILE')) and CODICE_PASSO = 'DISCIPLINA_FINANZIARIA'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"DFFRPAGACZ\",\"valNumber\":2000.00}', '{\"tipoVariabile\":\"DFFRPAGACZ\",\"valNumber\":1200.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and SOSTEGNO = 'ZOOTECNIA' AND ID_STATO_INIZIALE = (SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'LIQUIDABILE')) and CODICE_PASSO = 'DISCIPLINA_FINANZIARIA'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "UPDATE A4GT_ISTRUTTORIA set ID_STATO_LAVORAZIONE = (select id from a4gd_stato_lav_sostegno where identificativo = 'PAGAMENTO_AUTORIZZATO') where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 189674) and SOSTEGNO = 'ZOOTECNIA'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acz_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void esecuzioneDisciplinaFinanziariaDisaccoppiato_Acz_PAGAMENTO_AUTORIZZATO_Acs_CONTROLLI_INTERSOSTEGNO_OK() throws Exception {

		IstruttoriaModel istruttoria = eseguiControlli("189674", "");
		Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

		assertThat(transizioneSostegno.isPresent()).isTrue();

		Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
		Optional<PassoTransizioneModel> optPassoDisciplinaFinanziaria = getPassoDisciplinaFinanziaria(passiTransizione);

		assertThat(optPassoDisciplinaFinanziaria.isPresent()).isTrue();

		if (optPassoDisciplinaFinanziaria.isPresent()) {
			PassoTransizioneModel passo = optPassoDisciplinaFinanziaria.get();
			DatiInput variabiliInput = objectMapper.readValue(passo.getDatiInput(), DatiInput.class);
			DatiOutput variabiliOutput = objectMapper.readValue(passo.getDatiOutput(), DatiOutput.class);

			// disciplina già applicata a ACZ
			Optional<VariabileCalcolo> optDffrappacz = variabiliInput.getVariabiliCalcolo().stream().filter(v -> v.getTipoVariabile().equals(TipoVariabile.DFFRAPPACZ)).findFirst();
			// disciplina già applicata a ACS
			Optional<VariabileCalcolo> optDffrappacs = variabiliInput.getVariabiliCalcolo().stream().filter(v -> v.getTipoVariabile().equals(TipoVariabile.DFFRAPPACS)).findFirst();
			// disciplina da applicare al disaccoppiato
			Optional<VariabileCalcolo> optDffrpagdis = variabiliOutput.getVariabiliCalcolo().stream().filter(v -> v.getTipoVariabile().equals(TipoVariabile.DFFRPAGDIS)).findFirst();

			assertEquals(true, optDffrappacs.isPresent());
			assertEquals(true, optDffrappacz.isPresent());
			assertEquals(true, optDffrpagdis.isPresent());

			if (optDffrappacz.isPresent()) {
				VariabileCalcolo var = optDffrappacz.get();
				assertEquals(BigDecimal.valueOf(1200).setScale(2, RoundingMode.FLOOR), var.getValNumber());
			}
			if (optDffrappacs.isPresent()) {
				VariabileCalcolo var = optDffrappacs.get();
				assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.FLOOR), var.getValNumber());
			}

			if (optDffrpagdis.isPresent()) {
				VariabileCalcolo var = optDffrpagdis.get();
				assertEquals(BigDecimal.valueOf(800).setScale(2, RoundingMode.FLOOR), var.getValNumber());
			}
		}

	}

	// Dati test:
	// - Disaccoppiato:
	// --- Stato: LIQUIDABILE
	// --- Importo: 1000
	// - Acs:
	// --- Stato: PAGAMENTO_AUTORIZZATO
	// --- Importo: 700
	// --- Disciplina già applicata: 700
	// - Acz:
	// --- Stato: PAGAMENTO_AUTORIZZATO
	// --- Importo: 1200
	// --- Disciplina già applicata: 1200
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs_Acz.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":700.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":700.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":1000.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":1000.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACZIMPCALCTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACZIMPCALCTOT\",\"valNumber\":1200.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'ZOOTECNIA') and CODICE_PASSO = 'CALCOLO_ACZ'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACZIMPCALCLORDOTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACZIMPCALCLORDOTOT\",\"valNumber\":1200.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'ZOOTECNIA') and CODICE_PASSO = 'CALCOLO_ACZ'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_acs_acz.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/domanda_Disciplina_Finanziaria_Acz.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/domanda_Disciplina_Finanziaria_Acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"DFFRPAGACS\",\"valNumber\":1746.01}', '{\"tipoVariabile\":\"DFFRPAGACS\",\"valNumber\":700.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and SOSTEGNO = 'SUPERFICIE' AND ID_STATO_INIZIALE = (SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'LIQUIDABILE')) and CODICE_PASSO = 'DISCIPLINA_FINANZIARIA'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"DFFRPAGACZ\",\"valNumber\":2000.00}', '{\"tipoVariabile\":\"DFFRPAGACZ\",\"valNumber\":1200.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and SOSTEGNO = 'ZOOTECNIA' AND ID_STATO_INIZIALE = (SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'LIQUIDABILE')) and CODICE_PASSO = 'DISCIPLINA_FINANZIARIA'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "UPDATE A4GT_ISTRUTTORIA set ID_STATO_LAVORAZIONE = (select id from a4gd_stato_lav_sostegno where identificativo = 'PAGAMENTO_AUTORIZZATO') where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 189674) and SOSTEGNO = 'SUPERFICIE'; COMMIT;")
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "UPDATE A4GT_ISTRUTTORIA set ID_STATO_LAVORAZIONE = (select id from a4gd_stato_lav_sostegno where identificativo = 'PAGAMENTO_AUTORIZZATO') where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 189674) and SOSTEGNO = 'ZOOTECNIA'; COMMIT;")
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acz_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void esecuzioneDisciplinaFinanziariaDisaccoppiato_Acz_PAGAMENTO_AUTORIZZATO_Acs_PAGAMENTO_AUTORIZZATO() throws Exception {

		IstruttoriaModel istruttoria = eseguiControlli("189674", "");
		Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

		assertThat(transizioneSostegno.isPresent()).isTrue();

		Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
		Optional<PassoTransizioneModel> optPassoDisciplinaFinanziaria = getPassoDisciplinaFinanziaria(passiTransizione);

		assertThat(optPassoDisciplinaFinanziaria.isPresent()).isTrue();

		if (optPassoDisciplinaFinanziaria.isPresent()) {
			PassoTransizioneModel passo = optPassoDisciplinaFinanziaria.get();
			DatiInput variabiliInput = objectMapper.readValue(passo.getDatiInput(), DatiInput.class);
			DatiOutput variabiliOutput = objectMapper.readValue(passo.getDatiOutput(), DatiOutput.class);

			// disciplina già applicata a ACZ
			Optional<VariabileCalcolo> optDffrappacz = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPACZ);
			// disciplina già applicata a ACS
			Optional<VariabileCalcolo> optDffrappacs = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPACS);
			// disciplina da applicare al disaccoppiato
			Optional<VariabileCalcolo> optDffrAppdis = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPDIS);
			// disciplina da applicare al disaccoppiato
			Optional<VariabileCalcolo> optDffrpagdis = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGDIS);

			assertThat(optDffrappacs).isPresent();
			assertThat(optDffrappacz).isPresent();
			assertThat(optDffrAppdis).isPresent();
			assertThat(optDffrpagdis).isPresent();

			if (optDffrappacz.isPresent()) {
				VariabileCalcolo var = optDffrappacz.get();
				assertEquals(BigDecimal.valueOf(1200).setScale(2, RoundingMode.FLOOR), var.getValNumber());
			}
			if (optDffrappacs.isPresent()) {
				VariabileCalcolo var = optDffrappacs.get();
				assertEquals(BigDecimal.valueOf(700).setScale(2, RoundingMode.FLOOR), var.getValNumber());
			}
			if (optDffrAppdis.isPresent()) {
				VariabileCalcolo var = optDffrAppdis.get();
				assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
			}

			if (optDffrpagdis.isPresent()) {
				VariabileCalcolo var = optDffrpagdis.get();
				assertEquals(BigDecimal.valueOf(100).setScale(2, RoundingMode.FLOOR), var.getValNumber());
			}
		}

	}

	private Optional<VariabileCalcolo> getVariabileInput(DatiInput variabiliInput,
			TipoVariabile tipovariabile) {
		return variabiliInput.getVariabiliCalcolo().stream().filter(v -> v.getTipoVariabile().equals(tipovariabile)).findFirst();
	}

	private Optional<VariabileCalcolo> getVariabileOutput(DatiOutput variabili,
			TipoVariabile tipovariabile) {
		return variabili.getVariabiliCalcolo().stream().filter(v -> v.getTipoVariabile().equals(tipovariabile)).findFirst();
	}

	private Optional<PassoTransizioneModel> getPassoDisciplinaFinanziaria(Set<PassoTransizioneModel> passiTransizione) {
		Optional<PassoTransizioneModel> optPassoDisciplinaFinanziaria = passiTransizione.stream().filter(p -> p.getCodicePasso().equals(TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA))
				.findFirst();
		return optPassoDisciplinaFinanziaria;
	}

//	// Il test verifica che in caso di più transizioni di calcolo per un sostegno,
//	// venga recuperata la più recente
//	@Test
//	@Transactional
//	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Doppio_Acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
//	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":1000.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
//	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":0.00}', '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":1200.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on ts.id_istruttoria = i.id join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE' and to_date(ts.data_esecuzione, 'yyyy-mm-dd') = to_date('2019-02-07', 'yyyy-mm-dd')) and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
//	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":0.00}', '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":1500.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on ts.id_istruttoria = i.id join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE' and to_date(ts.data_esecuzione, 'yyyy-mm-dd') = to_date('2019-02-11', 'yyyy-mm-dd')) and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
//	@Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_doppio_acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
//	@Sql(scripts = "/DomandaUnica/domanda_Disciplina_Finanziaria_Acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
//	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"DFFRAPPACS\",\"valNumber\":0.00}', '{\"tipoVariabile\":\"DFFRAPPACS\",\"valNumber\":1200.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on ts.id_istruttoria = i.id join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE' and ID_STATO_INIZIALE = (SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'LIQUIDABILE')) and CODICE_PASSO = 'DISCIPLINA_FINANZIARIA'; COMMIT;")
//	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "UPDATE A4GT_ISTRUTTORIA set ID_STATO_LAVORAZIONE = (select id from a4gd_stato_lav_sostegno where identificativo = 'PAGAMENTO_AUTORIZZATO') where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 189674) and SOSTEGNO = 'SUPERFICIE'; COMMIT;")
//	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
//	public void esecuzioneAcsDoppioCalcolo() throws Exception {
//
//		getSostegno identificativoSostegno = getSostegno.DISACCOPPIATO;
//
//		IstruttoriaModel istruttoria = eseguiControlli("189674", identificativoSostegno, "");
//
//		// Optional<TransizioneIstruttoriaModel> optTransizioneCalcolo =
//		// d.getA4gtTransizioneSostegnos().stream().filter(t -> {
//		Optional<TransizioneIstruttoriaModel> optTransizioneCalcolo = istruttoria.getTransizioni().stream()
//				.filter(t -> {
//					return t.getA4gdStatoLavSostegno1() != null
//							&& t.getA4gdStatoLavSostegno1().getIdentificativo()
//									.equalsIgnoreCase(StatoIstruttoria.CONTROLLI_CALCOLO_OK.getStatoIstruttoria())
//							&& t.getIstruttoria().getA4gdSostegnoDu().getIdentificativo()
//									.equalsIgnoreCase(getSostegno.SUPERFICIE.getSostegno());
//				}).sorted((t1, t2) -> t2.getDataEsecuzione().compareTo(t1.getDataEsecuzione())).findFirst();
//
//		List<PassoTransizioneModel> passiTransizione = passoTransizioneDao
//				.findByTransizioneIstruttoria(optTransizioneCalcolo.get());
//		Optional<PassoTransizioneModel> optPassoDoppioCalcolo = passiTransizione.stream()
//				.filter(p -> p.getCodicePasso().equals(TipologiaPassoTransizione.CALCOLO_ACS)).findFirst();
//
//		assertEquals(true, optPassoDoppioCalcolo.isPresent());
//
//		if (optPassoDoppioCalcolo.isPresent()) {
//			PassoTransizioneModel passo = optPassoDoppioCalcolo.get();
//			DatiOutput variabiliOutput = objectMapper.readValue(passo.getDatiOutput(), DatiOutput.class);
//
//			// recupero la ACSIMPCALCTOT
//			Optional<VariabileCalcolo> optAcscalctot = variabiliOutput.getVariabiliCalcolo().stream()
//					.filter(v -> v.getTipoVariabile().equals(TipoVariabile.ACSIMPCALCTOT)).findFirst();
//			assertEquals(true, optAcscalctot.isPresent());
//			if (optAcscalctot.isPresent()) {
//				VariabileCalcolo var = optAcscalctot.get();
//				assertEquals(BigDecimal.valueOf(1500).setScale(2, RoundingMode.FLOOR), var.getValNumber());
//
//			}
//		}
//
//	}
	
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


	private Optional<TransizioneIstruttoriaModel> caricaTransizioneIntersostegno(IstruttoriaModel istruttoria) {
		Optional<TransizioneIstruttoriaModel> transizioneSostegno = 
				istruttoria.getTransizioni().stream().filter(t -> Arrays.asList(StatoIstruttoria.NON_LIQUIDABILE,
						StatoIstruttoria.PAGAMENTO_NON_AUTORIZZATO,
						StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK,
						StatoIstruttoria.PAGAMENTO_AUTORIZZATO).contains(StatoIstruttoria.valueOfByIdentificativo(t.getA4gdStatoLavSostegno1().getIdentificativo())))
				.sorted(Comparator.comparing(TransizioneIstruttoriaModel::getDataEsecuzione))
				.findFirst();
		return transizioneSostegno;
	}
}
