package it.tndigitale.a4g.ags.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import it.tndigitale.a4g.ags.repository.dao.DomandaDaoImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import it.tndigitale.a4g.ags.dto.ControlliPresentazione;
import it.tndigitale.a4g.ags.dto.DomandaUnica;
import it.tndigitale.a4g.ags.dto.ErroreControlloRicevibilitaDomanda;
import it.tndigitale.a4g.ags.dto.InfoGeneraliDomanda;
import it.tndigitale.a4g.ags.model.StatoDomanda;
import it.tndigitale.a4g.ags.repository.dao.DomandaDao;
import it.tndigitale.a4g.ags.utente.Ruoli;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
// @AutoConfigureTestDatabase
public class DomandeServiceImplTests {
	
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private DomandaDaoImpl daoDomanda;
	@Autowired
	private DomandaServiceImpl service;

	@MockBean
	private RestTemplate restTemplate;

	public void initialize() throws ParseException {
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		InfoGeneraliDomanda infoGeneraliDomandaMocked = new InfoGeneraliDomanda();
		infoGeneraliDomandaMocked.setPac("PAC1420");
		infoGeneraliDomandaMocked.setTipoDomanda("DU");
		infoGeneraliDomandaMocked.setCampagna(new BigDecimal(2018));
		infoGeneraliDomandaMocked.setModulo("PAGAMENTI DIRETTI");
		infoGeneraliDomandaMocked.setCodModulo("BPS_2018");
		infoGeneraliDomandaMocked.setCuaaIntestatario("BBBFBA66E31F187R");
		infoGeneraliDomandaMocked.setRagioneSociale("BEBBER FABIO");
		infoGeneraliDomandaMocked.setNumeroDomanda(Long.parseLong ("112233"));
		infoGeneraliDomandaMocked.setDataPresentazione(simpleDateFormat.parse("2019-07-12"));
		infoGeneraliDomandaMocked.setCodEnteCompilatore("12");
		infoGeneraliDomandaMocked.setEnteCompilatore("CAA COLDIRETTI DEL TRENTINO - 011");
		infoGeneraliDomandaMocked.setDataProtocollazione(simpleDateFormat.parse("2019-07-12"));
		infoGeneraliDomandaMocked.setStato(StatoDomanda.PROTOCOLLATA);
		infoGeneraliDomandaMocked.setDataPassaggioStato(simpleDateFormat.parse("2019-07-12"));

		Mockito.when(daoDomanda.getInfoGeneraliDomanda(Mockito.anyLong())).thenReturn(infoGeneraliDomandaMocked);
	}

	public void initialize_ko() throws ParseException {
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		InfoGeneraliDomanda infoGeneraliDomandaMocked = new InfoGeneraliDomanda();
		infoGeneraliDomandaMocked.setPac("PAC1420");
		infoGeneraliDomandaMocked.setTipoDomanda("DU");
		infoGeneraliDomandaMocked.setCampagna(new BigDecimal(2018));
		infoGeneraliDomandaMocked.setModulo("PAGAMENTI DIRETTI");
		infoGeneraliDomandaMocked.setCodModulo("BPS_2018");
		infoGeneraliDomandaMocked.setCuaaIntestatario("BBBFBA66E31F187R");
		infoGeneraliDomandaMocked.setRagioneSociale("BEBBER FABIO");
		infoGeneraliDomandaMocked.setNumeroDomanda(Long.parseLong ("112233"));
		infoGeneraliDomandaMocked.setDataPresentazione(simpleDateFormat.parse("2019-07-13"));
		infoGeneraliDomandaMocked.setCodEnteCompilatore("12");
		infoGeneraliDomandaMocked.setEnteCompilatore("CAA COLDIRETTI DEL TRENTINO - 011");
		infoGeneraliDomandaMocked.setDataProtocollazione(simpleDateFormat.parse("2019-07-13"));
		infoGeneraliDomandaMocked.setStato(StatoDomanda.PROTOCOLLATA);
		infoGeneraliDomandaMocked.setDataPassaggioStato(simpleDateFormat.parse("2019-07-13"));

		Mockito.when(daoDomanda.getInfoGeneraliDomanda(Mockito.anyLong())).thenReturn(infoGeneraliDomandaMocked);
	}

		private void initialize_ritiro_parziale() throws ParseException {
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		InfoGeneraliDomanda infoGeneraliDomandaMocked = new InfoGeneraliDomanda();
		infoGeneraliDomandaMocked.setPac("PAC1420");
		infoGeneraliDomandaMocked.setTipoDomanda("DU");
		infoGeneraliDomandaMocked.setCampagna(new BigDecimal(2018));
		infoGeneraliDomandaMocked.setModulo("PAGAMENTI DIRETTI");
		infoGeneraliDomandaMocked.setCodModulo("BPS_RITPRZ_2018");
		infoGeneraliDomandaMocked.setCuaaIntestatario("BBBFBA66E31F187R");
		infoGeneraliDomandaMocked.setRagioneSociale("BEBBER FABIO");
		infoGeneraliDomandaMocked.setNumeroDomanda(Long.parseLong ("112233"));
		infoGeneraliDomandaMocked.setDataPresentazione(simpleDateFormat.parse("2020-05-16"));
		infoGeneraliDomandaMocked.setCodEnteCompilatore("12");
		infoGeneraliDomandaMocked.setEnteCompilatore("CAA COLDIRETTI DEL TRENTINO - 011");
		infoGeneraliDomandaMocked.setDataProtocollazione(simpleDateFormat.parse("2019-07-13"));
		infoGeneraliDomandaMocked.setStato(StatoDomanda.PROTOCOLLATA);
		infoGeneraliDomandaMocked.setDataPassaggioStato(simpleDateFormat.parse("2020-05-16"));

		Mockito.when(daoDomanda.getInfoGeneraliDomanda(Mockito.anyLong())).thenReturn(infoGeneraliDomandaMocked);
	}

//	@Test
//	@Sql(scripts = "/duProtocollate/insert_du_protocollata.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
//	@Sql(scripts = "/duProtocollate/delete_du_protocollata.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
//	@WithMockUser(username = "utenteAppag", roles = { Ruoli.VISUALIZZA_DOMANDA_DU_COD })
//	public void getDomandaProtocollata() throws Exception {
////		TODO http 204 invece di http 200
//		this.mockMvc.perform(get("/api/v1/domandeDU/protocollate/2019"))
//			.andExpect(status().isOk())
//			.andExpect(content()
//					.string(containsString("202882")));
//	}
	
	@Test
	@Transactional
	@Sql(scripts = { "/criteriRicevibilita/112233_NOritiroParziale_presentazioneUltimoGiornoOK_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/criteriRicevibilita/112233_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void tuttiIControlliDiPresentazione_OK_conDataPresentazioneUltimoGiornoConsentito_OK_NORitiroParziale() throws Exception {
		initialize();
		InfoGeneraliDomanda infoGeneraliDomanda = daoDomanda.getInfoGeneraliDomanda(new Long ("112233"));
		ControlliPresentazione controlli = getControlliPresentazione_SSSS();
		DomandaUnica domandaUnica = new DomandaUnica();
		domandaUnica.setInfoGeneraliDomanda(infoGeneraliDomanda);
		domandaUnica.setControlliPresentazione(controlli);
		service.verificaRicevibilita(domandaUnica);
		assertTrue(domandaUnica.getListaErroriRicevibilita() == null || domandaUnica.getListaErroriRicevibilita().isEmpty());
	}
	
	@Test
	@Transactional
	@Sql(scripts = { "/criteriRicevibilita/112233_ritiroParziale_presentazioneUltimoGiornoOK_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/criteriRicevibilita/112233_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void tuttiIControlliDiPresentazione_OK_conDataPresentazioneUltimoGiornoConsentito_OK_RitiroParziale() throws Exception {
		initialize();
		InfoGeneraliDomanda infoGeneraliDomanda = daoDomanda.getInfoGeneraliDomanda(new Long ("112233"));
		ControlliPresentazione controlli = getControlliPresentazione_SSSS();
		DomandaUnica domandaUnica = new DomandaUnica();
		domandaUnica.setInfoGeneraliDomanda(infoGeneraliDomanda);
		domandaUnica.setControlliPresentazione(controlli);
		service.verificaRicevibilita(domandaUnica);
		assertTrue(domandaUnica.getListaErroriRicevibilita() == null || domandaUnica.getListaErroriRicevibilita().isEmpty());
	}
	
	@Test
	@Transactional
	@Sql(scripts = { "/criteriRicevibilita/112233_NOritiroParziale_presentazioneUltimoGiornoKO_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/criteriRicevibilita/112233_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void tuttiIControlliDiPresentazione_OK_conDataPresentazioneUltimoGiornoConsentito_KO_NORitiroParziale() throws Exception {
//		TODO fail
		initialize_ko();
		InfoGeneraliDomanda infoGeneraliDomanda = daoDomanda.getInfoGeneraliDomanda(new Long ("112233"));
		ControlliPresentazione controlli = getControlliPresentazione_SSSS();
		DomandaUnica domandaUnica = new DomandaUnica();
		domandaUnica.setInfoGeneraliDomanda(infoGeneraliDomanda);
		domandaUnica.setControlliPresentazione(controlli);
		service.verificaRicevibilita(domandaUnica);
		// DomandaUnica domandaUnica = service.riceviDomanda(new Long ("112233"));
		assertTrue(domandaUnica.getListaErroriRicevibilita().contains(ErroreControlloRicevibilitaDomanda.PRESENTAZIONE_NEI_TERMINI));
	}
	
	@Test
	@Transactional
	@Sql(scripts = { "/criteriRicevibilita/112233_ritiroParziale_presentazioneUltimoGiornoKO_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/criteriRicevibilita/112233_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void tuttiIControlliDiPresentazione_OK_conDataPresentazioneUltimoGiornoConsentito_KO_RitiroParziale() throws Exception {
		initialize_ritiro_parziale();
		InfoGeneraliDomanda infoGeneraliDomanda = daoDomanda.getInfoGeneraliDomanda(112233L);
		
		// 2020-05-15
		infoGeneraliDomanda.setDtProtocollazioneUltimaModifica(Date.valueOf(LocalDate.of(2020, 5, 16)));
		infoGeneraliDomanda.setDataProtocollazOriginaria(Date.valueOf(LocalDate.of(2019, 7, 11))); 
		ControlliPresentazione controlli = getControlliPresentazione_SSSS();
		DomandaUnica domandaUnica = new DomandaUnica();
		domandaUnica.setInfoGeneraliDomanda(infoGeneraliDomanda);
		domandaUnica.setControlliPresentazione(controlli);
		service.verificaRicevibilita(domandaUnica);
		assertTrue(domandaUnica.getListaErroriRicevibilita().contains(ErroreControlloRicevibilitaDomanda.PRESENTAZIONE_NEI_TERMINI));
	}
	
	@Test
	@Transactional
	@Sql(scripts = { "/criteriRicevibilita/112233_NOritiroParziale_presentazioneUltimoGiornoOK_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/criteriRicevibilita/112233_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void tuttiIControlliDiPresentazione_KO() throws Exception {
		initialize();
		InfoGeneraliDomanda infoGeneraliDomanda = daoDomanda.getInfoGeneraliDomanda(new Long ("112233"));
		ControlliPresentazione controlli = getControlliPresentazione_NNNN();
		DomandaUnica domandaUnica = new DomandaUnica();
		domandaUnica.setInfoGeneraliDomanda(infoGeneraliDomanda);
		domandaUnica.setControlliPresentazione(controlli);
		service.verificaRicevibilita(domandaUnica);
		assertTrue(domandaUnica.getListaErroriRicevibilita().contains(ErroreControlloRicevibilitaDomanda.FASCICOLO_AGGIORNATO));
		assertTrue(domandaUnica.getListaErroriRicevibilita().contains(ErroreControlloRicevibilitaDomanda.ARCHIVIAZIONE_DOCUMENTI_OK));
		assertTrue(domandaUnica.getListaErroriRicevibilita().contains(ErroreControlloRicevibilitaDomanda.DOMANDA_CARTACEA_FIRMATA));
		assertTrue(domandaUnica.getListaErroriRicevibilita().contains(ErroreControlloRicevibilitaDomanda.ANOMALIE_COMPILAZIONE_AGS));
	}
	
	@Test
	@Transactional
	@Sql(scripts = { "/criteriRicevibilita/112233_NOritiroParziale_presentazioneUltimoGiornoOK_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/criteriRicevibilita/112233_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void controlloFascicoloAggiornato_KO_altriControlliOk() throws Exception {
		initialize();
		InfoGeneraliDomanda infoGeneraliDomanda = daoDomanda.getInfoGeneraliDomanda(new Long ("112233"));
		ControlliPresentazione controlli = getFascicoloAggiornato_N();
		DomandaUnica domandaUnica = new DomandaUnica();
		domandaUnica.setInfoGeneraliDomanda(infoGeneraliDomanda);
		domandaUnica.setControlliPresentazione(controlli);
		service.verificaRicevibilita(domandaUnica);
		List<ErroreControlloRicevibilitaDomanda> list = Arrays.asList(ErroreControlloRicevibilitaDomanda.FASCICOLO_AGGIORNATO);
		assertEquals(domandaUnica.getListaErroriRicevibilita(), list);
	}
	
	@Test
	@Transactional
	@Sql(scripts = { "/criteriRicevibilita/112233_NOritiroParziale_presentazioneUltimoGiornoOK_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/criteriRicevibilita/112233_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void controlloArchiviazioneDocumenti_KO_altriControlliOk() throws Exception {
		initialize();
		InfoGeneraliDomanda infoGeneraliDomanda = daoDomanda.getInfoGeneraliDomanda(new Long ("112233"));
		ControlliPresentazione controlli = getArchiviazioneDocumenti_N();
		DomandaUnica domandaUnica = new DomandaUnica();
		domandaUnica.setInfoGeneraliDomanda(infoGeneraliDomanda);
		domandaUnica.setControlliPresentazione(controlli);
		service.verificaRicevibilita(domandaUnica);
		List<ErroreControlloRicevibilitaDomanda> list = Arrays.asList(ErroreControlloRicevibilitaDomanda.ARCHIVIAZIONE_DOCUMENTI_OK);
		assertEquals(domandaUnica.getListaErroriRicevibilita(), list);
	}
	
	@Test
	@Transactional
	@Sql(scripts = { "/criteriRicevibilita/112233_NOritiroParziale_presentazioneUltimoGiornoOK_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/criteriRicevibilita/112233_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void controlloDomandaCartaceaFirmata_KO_altriControlliOk() throws Exception {
		initialize();
		InfoGeneraliDomanda infoGeneraliDomanda = daoDomanda.getInfoGeneraliDomanda(new Long ("112233"));
		ControlliPresentazione controlli = getDomandaCartaceaFirmata_N();
		DomandaUnica domandaUnica = new DomandaUnica();
		domandaUnica.setInfoGeneraliDomanda(infoGeneraliDomanda);
		domandaUnica.setControlliPresentazione(controlli);
		service.verificaRicevibilita(domandaUnica);
		List<ErroreControlloRicevibilitaDomanda> list = Arrays.asList(ErroreControlloRicevibilitaDomanda.DOMANDA_CARTACEA_FIRMATA);
		assertEquals(domandaUnica.getListaErroriRicevibilita(), list);
	}
	
	@Test
	@Transactional
	@Sql(scripts = { "/criteriRicevibilita/112233_NOritiroParziale_presentazioneUltimoGiornoOK_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/criteriRicevibilita/112233_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void controlloAnomalieCompilazioneAgs_KO_altriControlliOk() throws Exception {
		initialize();
		InfoGeneraliDomanda infoGeneraliDomanda = daoDomanda.getInfoGeneraliDomanda(new Long ("112233"));
		ControlliPresentazione controlli = getAnomalieCompilazioneAgs_N();
		DomandaUnica domandaUnica = new DomandaUnica();
		domandaUnica.setInfoGeneraliDomanda(infoGeneraliDomanda);
		domandaUnica.setControlliPresentazione(controlli);
		service.verificaRicevibilita(domandaUnica);
		List<ErroreControlloRicevibilitaDomanda> list = Arrays.asList(ErroreControlloRicevibilitaDomanda.ANOMALIE_COMPILAZIONE_AGS);
		assertEquals(domandaUnica.getListaErroriRicevibilita(), list);
	}
	
	@Test
	@Transactional
	@Sql(scripts = { "/criteriRicevibilita/112233_NOritiroParziale_presentazioneUltimoGiornoOK_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/criteriRicevibilita/112233_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void aggiornaStatoDomanda_InIstruttoria() throws Exception {
		initialize();
		InfoGeneraliDomanda infoGeneraliDomanda = daoDomanda.getInfoGeneraliDomanda(new Long ("112233"));
		assertThat(infoGeneraliDomanda.getStato()).isEqualTo(StatoDomanda.PROTOCOLLATA);
		ControlliPresentazione controlli = getControlliPresentazione_SSSS();
		DomandaUnica domandaUnica = new DomandaUnica();
		domandaUnica.setInfoGeneraliDomanda(infoGeneraliDomanda);
		domandaUnica.setControlliPresentazione(controlli);
		service.verificaRicevibilita(domandaUnica);
		service.aggiornaStatoDomanda(domandaUnica);
		// DomandaUnica domandaUnica = service.riceviDomanda(new Long ("112233"));
		assertThat(domandaUnica.getInfoGeneraliDomanda().getStato()).isEqualTo(StatoDomanda.RICEVIBILE);
	}
	
	@Test
	@Transactional
	@Sql(scripts = { "/criteriRicevibilita/112233_NOritiroParziale_presentazioneUltimoGiornoOK_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/criteriRicevibilita/112233_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void aggiornaStatoDomanda_NonRicevibile() throws Exception {
		initialize();
		InfoGeneraliDomanda infoGeneraliDomanda = daoDomanda.getInfoGeneraliDomanda(new Long ("112233"));
		assertThat(infoGeneraliDomanda.getStato()).isEqualTo(StatoDomanda.PROTOCOLLATA);
		ControlliPresentazione controlli = getFascicoloAggiornato_N();
		DomandaUnica domandaUnica = new DomandaUnica();
		domandaUnica.setInfoGeneraliDomanda(infoGeneraliDomanda);
		domandaUnica.setControlliPresentazione(controlli);
		service.verificaRicevibilita(domandaUnica);
		service.aggiornaStatoDomanda(domandaUnica);
		// DomandaUnica domandaUnica = service.riceviDomanda(new Long ("112233"));
		assertThat(domandaUnica.getInfoGeneraliDomanda().getStato()).isEqualTo(StatoDomanda.NON_RICEVIBILE);
	}
	
	private ControlliPresentazione getControlliPresentazione_SSSS() {
		ControlliPresentazione controlli = new ControlliPresentazione();
		controlli.setAggiornamentoFascicolo("S");
		controlli.setArchiviazioneDomanda("S");
		controlli.setFirmaDomanda("S");
		controlli.setVisioneAnomalie("S");
		return controlli;
	}
	
	private ControlliPresentazione getControlliPresentazione_NNNN() {
		ControlliPresentazione controlli = new ControlliPresentazione();
		controlli.setAggiornamentoFascicolo("N");
		controlli.setArchiviazioneDomanda("N");
		controlli.setFirmaDomanda("N");
		controlli.setVisioneAnomalie("N");
		return controlli;
	}
	
	private ControlliPresentazione getFascicoloAggiornato_N() {
		ControlliPresentazione controlli = new ControlliPresentazione();
		controlli.setAggiornamentoFascicolo("N");
		controlli.setArchiviazioneDomanda("S");
		controlli.setFirmaDomanda("S");
		controlli.setVisioneAnomalie("S");
		return controlli;
	}
	
	private ControlliPresentazione getArchiviazioneDocumenti_N() {
		ControlliPresentazione controlli = new ControlliPresentazione();
		controlli.setAggiornamentoFascicolo("S");
		controlli.setArchiviazioneDomanda("N");
		controlli.setFirmaDomanda("S");
		controlli.setVisioneAnomalie("S");
		return controlli;
	}
	
	private ControlliPresentazione getDomandaCartaceaFirmata_N() {
		ControlliPresentazione controlli = new ControlliPresentazione();
		controlli.setAggiornamentoFascicolo("S");
		controlli.setArchiviazioneDomanda("S");
		controlli.setFirmaDomanda("N");
		controlli.setVisioneAnomalie("S");
		return controlli;
	}
	
	private ControlliPresentazione getAnomalieCompilazioneAgs_N() {
		ControlliPresentazione controlli = new ControlliPresentazione();
		controlli.setAggiornamentoFascicolo("S");
		controlli.setArchiviazioneDomanda("S");
		controlli.setFirmaDomanda("S");
		controlli.setVisioneAnomalie("N");
		return controlli;
	}
	
}
