package it.tndigitale.a4gutente.api;

import static it.tndigitale.a4gutente.TestSupport.nonAutorizzato;
import static it.tndigitale.a4gutente.codici.StatoDomandaRegistrazioneUtente.APPROVATA;
import static it.tndigitale.a4gutente.codici.StatoDomandaRegistrazioneUtente.PROTOCOLLATA;
import static it.tndigitale.a4gutente.codici.StatoDomandaRegistrazioneUtente.RIFIUTATA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4gutente.codici.Dipartimento;
import it.tndigitale.a4gutente.codici.Ruoli;
import it.tndigitale.a4gutente.codici.StatoDomandaRegistrazioneUtente;
import it.tndigitale.a4gutente.dto.CounterStato;
import it.tndigitale.a4gutente.dto.DatiDomandaRegistrazioneUtente;
import it.tndigitale.a4gutente.dto.DatiDomandaRegistrazioneUtenteSintesi;
import it.tndigitale.a4gutente.dto.RichiestaDomandaApprovazione;
import it.tndigitale.a4gutente.dto.RichiestaDomandaRifiuta;
import it.tndigitale.a4gutente.dto.TipoDomandaRegistrazione;
import it.tndigitale.a4gutente.repository.dao.IIstruttoriaDao;
import it.tndigitale.a4gutente.repository.dao.IUtenteCompletoDao;
import it.tndigitale.a4gutente.repository.model.A4gtUtente;
import it.tndigitale.a4gutente.repository.model.IstruttoriaEntita;
import it.tndigitale.a4gutente.utility.JsonSupport;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DomandaRegistrazioneUtenteControllerTest {
	
	private Logger logger = LoggerFactory.getLogger(DomandaRegistrazioneUtenteControllerTest.class);

	private MockMvc mockMvc;

	@Autowired
	protected WebApplicationContext context;

	@MockBean
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private JavaMailSender emailSender;

	@Autowired
	private IIstruttoriaDao istruttoriaDao;

	@Autowired
	private IUtenteCompletoDao utenteCompletoDao;

	@MockBean
	private Clock clock;

	private static final Long ID_DOMANDA = 1000003L;
	private static final String NOTE = "Note facoltative";
	private static final String TESTO_MAIL = "Testo della mail";
	private static final String MOTIVAZIONE_RIFIUTO = "MOTIVAZIONE_RIFIUTO";
	private static final LocalDateTime NOW = LocalDateTime.of(2019, 10,10,10,1);

	@Before
	public void setUp() {
		// mi serve per avere lo stesso contesto perche quando chiamo il dao parte il flush ma si perde l'utente e va in errore
		mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}

	@Test
	@WithMockUser("utente")
	public void conNuovaDomandaUtenteDatiValitiAlloraSalva() throws Exception {
		File inputDataFile =
				new File("src/test/resources/domandaRegistrazioneUtente/registrazioneNuovaDomanda/nuovaDomandaRegistrazioneUtenteCompleto.json");

		DatiDomandaRegistrazioneUtente dati =
				objectMapper.readValue(inputDataFile, DatiDomandaRegistrazioneUtente.class);

		ResultActions resultActions =
				mockMvc.perform(post(ApiUrls.DOMANDE_V1)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(dati)));

		// verifico il risultato
		resultActions.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser("TRRCST78B08C794X")
	public void conDomandaProtocollataUtenteAlloraErrore() throws Exception {
		File inputDataFile =
				new File("src/test/resources/domandaRegistrazioneUtente/registrazioneNuovaDomanda/nuovaDomandaRegistrazioneUtenteCompleto.json");

		DatiDomandaRegistrazioneUtente dati =
				objectMapper.readValue(inputDataFile, DatiDomandaRegistrazioneUtente.class);

		ResultActions resultActions =
				mockMvc.perform(post(ApiUrls.DOMANDE_V1)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(dati)));

		// verifico il risultato
		resultActions.andDo(print())
				.andExpect(status().is5xxServerError())
				.andExpect(content().string("Per l'utente esiste gia' una domanda, non e' possibile registrarne una seconda"));
	}


	@Test
	@WithMockUser("1234567890")
	public void conDatiNonCompletiAlloraErrore() throws Exception {
		File inputDataFile =
				new File("src/test/resources/domandaRegistrazioneUtente/registrazioneNuovaDomanda/nuovaDomandaRegistrazioneUtenteNonCompleto.json");

		DatiDomandaRegistrazioneUtente dati =
				objectMapper.readValue(inputDataFile, DatiDomandaRegistrazioneUtente.class);

		ResultActions resultActions =
				mockMvc.perform(post(ApiUrls.DOMANDE_V1)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(dati)));

		// verifico il risultato
		resultActions.andDo(print())
				.andExpect(status().is5xxServerError());
	}

	@Test
	@Transactional
	@WithMockUser("BZZLBN21M45A178J")
	public void conModificaDomandaUtenteDatiValidiAlloraSalva() throws Exception {
		File inputDataFile =
				new File("src/test/resources/domandaRegistrazioneUtente/aggiornaDomanda/domandaAggiornaRegistrazioneUtenteCAA.json");

		DatiDomandaRegistrazioneUtente dati =
				objectMapper.readValue(inputDataFile, DatiDomandaRegistrazioneUtente.class);

		ResultActions resultActions =
				mockMvc.perform(put(ApiUrls.DOMANDE_V1)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(dati)));

		// verifico il risultato
		resultActions.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string("294"));
	}

	@Test
	@Transactional
	@WithMockUser("TRRCST78B08C794X")
	public void conPutDomandaProtocollataUtenteAlloraErrore() throws Exception {
		File inputDataFile =
				new File("src/test/resources/domandaRegistrazioneUtente/registrazioneNuovaDomanda/nuovaDomandaRegistrazioneUtenteCompleto.json");

		DatiDomandaRegistrazioneUtente dati =
				objectMapper.readValue(inputDataFile, DatiDomandaRegistrazioneUtente.class);

		ResultActions resultActions =
				mockMvc.perform(put(ApiUrls.DOMANDE_V1)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(dati)));

		// verifico il risultato
		resultActions.andDo(print())
				.andExpect(status().is5xxServerError())
				.andExpect(content().string("Per l'utente esiste gia' una domanda in stato protocollata, non e' possibile aggiornare"));
	}

	@Test
	@Transactional
	@WithMockUser("TRRCST78B08C794X")
	public void getElencoDipartimenti() throws Exception {
		ResultActions resultActions = mockMvc
				.perform(get(ApiUrls.DOMANDE_V1 + ApiUrls.ELENCO_DIPATIMENTI));
		// verifico il risultato
		resultActions.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString(Dipartimento.D317.toString())));

	}

	@Test
	@Transactional
	@WithMockUser("TRRCST78B08C794X")
	public void getElencoDistributori() throws Exception {
		ResultActions resultActions = mockMvc
				.perform(get(ApiUrls.DOMANDE_V1 + ApiUrls.ELENCO_DISTRIBUTORI));
		// verifico il risultato
		resultActions.andDo(print()).andExpect(status().isOk())
					 .andExpect(content().string(containsString("Lavis".toString())));

	}

	@Test
	@Transactional
	@WithMockUser(username="FRSLBT76H42E625Z", roles={Ruoli.VISUALIZZA_DOMANDE_COD})
	public void ricercaDomandaProtocollataEsistenteRestituisceOK() throws Exception {
		String cf = "TRRRNZ56R23F837Z";
		ResultActions resultActions = mockMvc
				.perform(get(ApiUrls.DOMANDE_V1)
						.param("codiceFiscale", cf)
						.param("stato", PROTOCOLLATA.name()));
		// verifico il risultato
		resultActions.andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.risultati[0].stato").value(PROTOCOLLATA.name()))
				.andExpect(jsonPath("$.risultati[0].datiAnagrafici.codiceFiscale").value(cf));

	}


	@Test
	@Transactional
	@WithMockUser(username="FRSLBT76H42E625Z", roles={Ruoli.VISUALIZZA_DOMANDE_COD})
	public void ricercaDomandaInesistenteRestituisce204() throws Exception {
		String cf = "XXX";
		ResultActions resultActions = mockMvc
				.perform(get(ApiUrls.DOMANDE_V1)
						.param("codiceFiscale", cf)
						.param("stato", PROTOCOLLATA.name()));
		// verifico il risultato
		resultActions.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.count").value("0"))
				.andExpect(jsonPath("$.risultati").isEmpty());

	}

	@Test
	@Transactional
	@WithMockUser(username="FRSLBT76H42E625Z", roles={Ruoli.VISUALIZZA_DOMANDE_COD})
	public void ricercaDomandeProtocollataEsistenteRestituisceOK() throws Exception {
		String params = "{\"stato\":\"" + PROTOCOLLATA + "\"}";
		ResultActions resultActions = mockMvc
				.perform(get(ApiUrls.DOMANDE_V1).param("params", params));
		// verifico il risultato
		resultActions.andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.risultati[*].stato", hasItem(is(PROTOCOLLATA.name()))));

	}

	@Test
	@Transactional
	@WithMockUser(username="FRSLBT76H42E625Z", roles={Ruoli.VISUALIZZA_DOMANDE_COD})
	public void ricercaDomandeTRRRNZ56R23F837ZEsistenteRestituisceOK() throws Exception {
		String cf = "TRRRNZ56R23F837Z";
		String params = "{\"codiceFiscale\":\"" + cf + "\"}";
		ResultActions resultActions = mockMvc
				.perform(get(ApiUrls.DOMANDE_V1).param("params", params));
		// verifico il risultato
		resultActions.andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.risultati[*].datiAnagrafici.codiceFiscale", hasItem(is(cf))));

	}

	@Test
	@Transactional
	@WithMockUser(username="FRSLBT76H42E625Z", roles={Ruoli.CREA_UTENTE_COD})
	@Sql("/customsql/ricercaDomande.sql")
	public void contaDomandeProtocollateKOPerMancanzaAbilitazioni() throws Exception {
		ResultActions resultActions = mockMvc.perform(get(ApiUrls.DOMANDE_V1 + "/counters"));

		resultActions.andDo(print()).andExpect(nonAutorizzato());
	}


	@Test
	@Transactional
	@WithMockUser(username="FRSLBT76H42E625Z", roles={Ruoli.VISUALIZZA_DOMANDE_COD})
	@Sql("/customsql/ricercaDomande.sql")
	public void contaDomandeProtocollate() throws Exception {
		ResultActions resultActions = mockMvc.perform(get(ApiUrls.DOMANDE_V1 + "/counters"));

		final MvcResult mvcResult = resultActions.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		List<CounterStato> counters = JsonSupport.toList(mvcResult.getResponse().getContentAsString(), CounterStato[].class);

		assertThat(counters).hasSize(StatoDomandaRegistrazioneUtente.values().length);
//		assertThat(counters).extracting("count").contains(3L, 0L, 0L, 1L, 0L, 0L);
		assertThat(counters).extracting("count").contains(5L, 1L, 2L, 0L, 0L, 0L);
		assertThat(counters).extracting("stato").contains(PROTOCOLLATA,
				StatoDomandaRegistrazioneUtente.IN_COMPILAZIONE,
				StatoDomandaRegistrazioneUtente.CHIUSA,
				StatoDomandaRegistrazioneUtente.IN_LAVORAZIONE,
				APPROVATA,
				StatoDomandaRegistrazioneUtente.RIFIUTATA);
	}

	@Test
	@Transactional
	@WithMockUser(username="FRSLBT76H42E625Z", roles={Ruoli.VISUALIZZA_DOMANDE_COD})
	@Sql("/customsql/ricercaDomande.sql")
	public void ricercaDomandeConPaginazioneDefaultEOrdinamentoDefault() throws Exception {
		ResultActions resultActions = mockMvc.perform(get(ApiUrls.DOMANDE_V1)
				.param("stato", PROTOCOLLATA.name()));

		final MvcResult mvcResult = resultActions.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		RisultatiPaginati<DatiDomandaRegistrazioneUtenteSintesi> results = JsonSupport.toObject(mvcResult.getResponse().getContentAsString(), RisultatiPaginati.class);

		assertThat(results.getCount()).isEqualTo(5);
		assertThat(results.getRisultati()).hasSize(5);
	}

	@Test
	@Transactional
	@WithMockUser(username="FRSLBT76H42E625Z", roles={Ruoli.VISUALIZZA_DOMANDE_COD})
	@Sql("/customsql/ricercaDomande.sql")
	public void ricercaDomandeConFiltri() throws Exception {
		ResultActions resultActions = mockMvc.perform(get(ApiUrls.DOMANDE_V1)
				.param("numeroElementiPagina", "2")
				.param("pagina", "0")
				.param("codiceFiscaleUpperLike", "RRrNz56")
				.param("nome", "abio")
				.param("cognome", "oss")
				.param("idProtocollo", "At-45")
				.param("dataInizio", "2019-01-01")
				.param("dataFine", "2019-01-10")
				.param("stato", PROTOCOLLATA.name()));

		final MvcResult mvcResult = resultActions.andDo(print())
											     .andExpect(status().isOk())
												 .andReturn();

		RisultatiPaginati<DatiDomandaRegistrazioneUtenteSintesi> results = JsonSupport
				.toObject(mvcResult.getResponse().getContentAsString(), RisultatiPaginati.class);

		assertThat(results.getCount()).isEqualTo(1);
		assertThat(results.getRisultati()).hasSize(1);
	}

	@Test
	@Transactional
	@WithMockUser(username="FRSLBT76H42E625Z", roles= {Ruoli.VISUALIZZA_DOMANDE_COD})
	@Sql("/customsql/ricercaDomande.sql")
	public void ricercaDomandeConPaginazioneSelezionataEOrdinamentoDefault() throws Exception {
		ResultActions resultActions = mockMvc.perform(get(ApiUrls.DOMANDE_V1)
				.param("numeroElementiPagina", "2")
				.param("pagina", "0")
				.param("stato", PROTOCOLLATA.name()));

		final MvcResult mvcResultPag_0 = resultActions.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		RisultatiPaginati<DatiDomandaRegistrazioneUtenteSintesi> results = JsonSupport.toObject(mvcResultPag_0.getResponse().getContentAsString(), RisultatiPaginati.class);

		assertThat(results.getCount()).isEqualTo(5);
		assertThat(results.getRisultati()).hasSize(2);

		resultActions = mockMvc.perform(get(ApiUrls.DOMANDE_V1)
				.param("numeroElementiPagina", "2")
				.param("pagina", "1")
				.param("stato", PROTOCOLLATA.name()));

		final MvcResult mvcResultPag_1 = resultActions.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		results = JsonSupport.toObject(mvcResultPag_1.getResponse().getContentAsString(), RisultatiPaginati.class);

		assertThat(results.getCount()).isEqualTo(5);
		assertThat(results.getRisultati()).hasSize(2);
	}


	@Test
	@Transactional
	@WithMockUser(username="FRSLBT76H42E625Z", roles= {Ruoli.VISUALIZZA_DOMANDE_COD})
	@Sql("/customsql/ricercaDomande.sql")
	public void ricercaDomandeConPaginazioneSelezionataEConOrdinamentoSelezionata() throws Exception {
		ResultActions resultActions = mockMvc.perform(get(ApiUrls.DOMANDE_V1)
				.param("numeroElementiPagina", "2")
				.param("pagina", "0")
				.param("ordine", "DESC")
				.param("proprieta", "email")
				.param("stato", PROTOCOLLATA.name()));

		final MvcResult mvcResult = resultActions.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.risultati[0].datiAnagrafici.email").value("robertoabate@gmail.com"))
				.andExpect(jsonPath("$.risultati[1].datiAnagrafici.email").value("giovannicometa@gmail.com"))
				.andReturn();

		RisultatiPaginati<DatiDomandaRegistrazioneUtenteSintesi> results = JsonSupport.toObject(mvcResult.getResponse().getContentAsString(), RisultatiPaginati.class);

		assertThat(results.getCount()).isEqualTo(5);
		assertThat(results.getRisultati()).hasSize(2);
	}

	@Test
	@Transactional
	@WithMockUser(username="FRSLBT76H42E625Z", roles= {Ruoli.VISUALIZZA_DOMANDE_COD})
	@Sql("/customsql/ricercaDomande.sql")
	public void ricercaDomandeConFiltroGenerico() throws Exception {
		ResultActions resultActions = mockMvc.perform(get(ApiUrls.DOMANDE_V1)
				.param("filtroGenerico", "aBiO")
				.param("stato", PROTOCOLLATA.name()));

		final MvcResult mvcResult = resultActions.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		RisultatiPaginati<DatiDomandaRegistrazioneUtenteSintesi> results = JsonSupport.toObject(mvcResult.getResponse().getContentAsString(), RisultatiPaginati.class);
		assertThat(results.getCount()).isEqualTo(1);
		assertThat(results.getRisultati()).hasSize(1);


		resultActions = mockMvc.perform(get(ApiUrls.DOMANDE_V1)
				.param("filtroGenerico", "trFb")
				.param("stato", PROTOCOLLATA.name()));

		final MvcResult mvcResult_2 = resultActions.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		results = JsonSupport.toObject(mvcResult_2.getResponse().getContentAsString(), RisultatiPaginati.class);
		assertThat(results.getCount()).isEqualTo(2);
		assertThat(results.getRisultati()).hasSize(2);

	}

	@Test
	@Transactional
	@WithMockUser(username="FRSLBT76H42E625Z", roles= {Ruoli.VISUALIZZA_DOMANDE_COD})
	@Sql("/customsql/ricercaDomandeConFiltri.sql")
	public void ricercaDomandeConAltriFiltriEStatoApprovata() throws Exception {
		ResultActions resultActions = mockMvc.perform(get(ApiUrls.DOMANDE_V1)
				.param("codiceFiscaleUpperLike", "FBA781")
				.param("nome", "giovanni")
				.param("cognome", "eTa")
				.param("idProtocollo", "PAT-46")
				.param("stato", APPROVATA.name()));

		final MvcResult mvcResult = resultActions.andDo(print())
												 .andExpect(status().isOk())
												 .andReturn();

		RisultatiPaginati<DatiDomandaRegistrazioneUtenteSintesi> results = JsonSupport.toObject(mvcResult.getResponse().getContentAsString(), RisultatiPaginati.class);
		assertThat(results.getCount()).isEqualTo(2);
		assertThat(results.getRisultati()).hasSize(2);
	}

	@Test
	@Transactional
	@WithMockUser(username="FRSLBT76H42E625Z", roles= {Ruoli.CREA_UTENTE_COD})
	@Sql("/customsql/ricercaDomande.sql")
	public void ricercaDomandeConFiltroGenericoKOPerMancanzaAbilitazioni() throws Exception {
		ResultActions resultActions = mockMvc.perform(get(ApiUrls.DOMANDE_V1)
														.param("filtroGenerico", "aBiO")
														.param("stato", PROTOCOLLATA.name()));

		resultActions.andDo(print())
				.andExpect(nonAutorizzato());
	}

	@Test
	@Transactional
	@WithMockUser(username="FRSLBT76H42E625Z", roles= {Ruoli.CREA_UTENTE_COD})
	@Sql("/customsql/ricercaDomande.sql")
	public void ricercaDomandeOKPerCodiceFiscalePresenteNelFiltro() throws Exception {
		mockMvc.perform(get(ApiUrls.DOMANDE_V1)
					.param("codiceFiscale", "FRSLBT76H42E625Z")
					.param("stato", PROTOCOLLATA.name()))
			   .andDo(print())
			   .andExpect(status().isOk());
	}


	@Transactional
	@WithMockUser(username = "FRSLBT76H42E625Z", roles= {Ruoli.VISUALIZZA_DOMANDE_COD})
	@Sql("/customsql/ricercaDomande.sql")
	@Test
	public void approvaDomandaRichiestaUtenteKOPerMancanzaAbilitazioni() throws Exception {
		when(clock.now()).thenReturn(NOW);

		mockMvc.perform(put(ApiUrls.DOMANDE_V1 + "/" + ID_DOMANDA + "/approva")
								.content(contentApprovazioneRichiestaUtente())
								.header("Content-Type", "application/json"))
			   .andDo(print())
			   .andExpect(nonAutorizzato());
	}

	@Transactional
	@WithMockUser(username = "FRSLBT76H42E625Z", roles= {Ruoli.EDITA_DOMANDE_COD})
	@Sql("/customsql/ricercaDomande.sql")
	@Test
	public void approvaDomandaRichiestaUtente() throws Exception {
		when(clock.now()).thenReturn(NOW);

		final MvcResult mvcResult = mockMvc.perform(put(ApiUrls.DOMANDE_V1 + "/" + ID_DOMANDA + "/approva")
														.content(contentApprovazioneRichiestaUtente())
														.header("Content-Type", "application/json"))
											.andDo(print())
											.andExpect(status().isOk())
											.andReturn();

		Long idDomandaApprovata = JsonSupport.toObject(mvcResult.getResponse().getContentAsString(), Long.class);
		assertThat(idDomandaApprovata).isEqualTo(ID_DOMANDA);

        IstruttoriaEntita istruttoria = istruttoriaDao.findByIdDomanda(ID_DOMANDA).orElse(new IstruttoriaEntita());
        assertThat(istruttoria.getNote()).isEqualTo(NOTE);
		assertThat(istruttoria.getUtente()).isNotNull();
        assertThat(istruttoria.getDomanda()).isNotNull();
        assertThat(istruttoria.getDomanda().getStato()).isEqualTo(APPROVATA);
		assertThat(istruttoria.getDataTermineIstruttoria()).isEqualTo(NOW);
		assertThat(istruttoria.getTestoMailInviata()).isEqualTo(TESTO_MAIL);
		assertThat(istruttoria.getIstruttore()).isNotNull();
		assertThat(istruttoria.getIstruttore().getIdentificativo()).isEqualTo("FRSLBT76H42E625Z");

        A4gtUtente utente = utenteCompletoDao.findByIdentificativo("GDAFBA7887654321");
        assertThat(utente).isNotNull();
        assertThat(utente.getA4gtEntes()).hasSize(1);
        assertThat(utente.getA4gtEntes()).extracting("id").contains(333444992L);
        assertThat(utente.getProfili()).hasSize(1);
        assertThat(utente.getProfili()).extracting("id").contains(144432336L);
	}

	@Transactional
	@WithMockUser(username = "FRSLBT76H42E625Z", roles= {Ruoli.VISUALIZZA_DOMANDE_COD})
	@Sql("/customsql/ricercaDomande.sql")
	@Test
	public void rifiutaDomandaRichiestaUtenteKOPerAbilitazioniUtente() throws Exception {
		when(clock.now()).thenReturn(NOW);

		mockMvc.perform(put(ApiUrls.DOMANDE_V1 + "/" + ID_DOMANDA + "/rifiuta")
									.content(contentRifiutaRichiestaUtente())
									.header("Content-Type", "application/json"))
			   .andDo(print())
			   .andExpect(nonAutorizzato());
	}

	@Transactional
	@WithMockUser(username = "FRSLBT76H42E625Z", roles= {Ruoli.EDITA_DOMANDE_COD})
	@Sql("/customsql/ricercaDomande.sql")
	@Test
	public void rifiutaDomandaRichiestaUtente() throws Exception {
		when(clock.now()).thenReturn(NOW);

		final MvcResult mvcResult = mockMvc.perform(put(ApiUrls.DOMANDE_V1 + "/" + ID_DOMANDA + "/rifiuta")
														 .content(contentRifiutaRichiestaUtente())
														 .header("Content-Type", "application/json"))
										   .andDo(print())
										   .andExpect(status().isOk())
										   .andReturn();

		Long idDomandaApprovata = JsonSupport.toObject(mvcResult.getResponse().getContentAsString(), Long.class);
		assertThat(idDomandaApprovata).isEqualTo(ID_DOMANDA);

		IstruttoriaEntita istruttoria = istruttoriaDao.findByIdDomanda(ID_DOMANDA)
													  .orElseThrow(() -> new Exception("Istruttoria non trovata"));
		assertThat(istruttoria.getMotivazioneRifiuto()).isEqualTo(MOTIVAZIONE_RIFIUTO);
		assertThat(istruttoria.getDomanda()).isNotNull();
		assertThat(istruttoria.getDomanda().getStato()).isEqualTo(RIFIUTATA);
		assertThat(istruttoria.getDataTermineIstruttoria()).isEqualTo(NOW);
		assertThat(istruttoria.getTestoMailInviata()).isEqualTo(TESTO_MAIL);
		assertThat(istruttoria.getIstruttore()).isNotNull();
		assertThat(istruttoria.getIstruttore().getIdentificativo()).isEqualTo("FRSLBT76H42E625Z");
	}

	@Transactional
	@WithMockUser(username = "FRSLBT76H42E625Z", roles= {Ruoli.EDITA_DOMANDE_COD})
	@Sql("/customsql/rifiutaDomandaSenzaIstruttoria.sql")
	@Test
	public void rifiutaDomandaRichiestaUtenteSenzaIstruttoria() throws Exception {
		when(clock.now()).thenReturn(NOW);

		final MvcResult mvcResult = mockMvc.perform(put(ApiUrls.DOMANDE_V1 + "/" + ID_DOMANDA + "/rifiuta")
										   				.content(contentRifiutaRichiestaUtente())
										   				.header("Content-Type", "application/json"))
										   .andDo(print())
										   .andExpect(status().isOk())
										   .andReturn();

		Long idDomandaApprovata = JsonSupport.toObject(mvcResult.getResponse().getContentAsString(), Long.class);
		assertThat(idDomandaApprovata).isEqualTo(ID_DOMANDA);

		IstruttoriaEntita istruttoria = istruttoriaDao.findByIdDomanda(ID_DOMANDA)
												      .orElseThrow(() -> new Exception("Istruttoria non trovata"));

		assertThat(istruttoria.getMotivazioneRifiuto()).isEqualTo(MOTIVAZIONE_RIFIUTO);
		assertThat(istruttoria.getDomanda()).isNotNull();
		assertThat(istruttoria.getDomanda().getStato()).isEqualTo(RIFIUTATA);
		assertThat(istruttoria.getDataTermineIstruttoria()).isEqualTo(NOW);
		assertThat(istruttoria.getTestoMailInviata()).isEqualTo(TESTO_MAIL);
		assertThat(istruttoria.getIstruttore()).isNotNull();
		assertThat(istruttoria.getIstruttore().getIdentificativo()).isEqualTo("FRSLBT76H42E625Z");

	}


	@Test
	@WithMockUser(username = "UTENTEAPPAG", roles= {Ruoli.VISUALIZZA_DOMANDE_COD})
	@Transactional
	public void presaInCaricoKoPerMancanzaAbilitazioni() throws Exception {
		Long id = new Long(999L);
		this.mockMvc.perform(put(ApiUrls.DOMANDE_V1 + "/" + id + "/presaincarico")).andExpect(nonAutorizzato());
	}

	@Test
	@WithMockUser(username = "UTENTEAPPAG", roles= {Ruoli.EDITA_DOMANDE_COD})
	@Transactional
	public void presaInCaricoOk() throws Exception {
		Long id = new Long(999L);
		this.mockMvc.perform(put(ApiUrls.DOMANDE_V1 + "/" + id + "/presaincarico")).andExpect(status().is2xxSuccessful());
	}

	@Test
	@WithMockUser(username = "UTENTEAPPAG", roles= {Ruoli.EDITA_DOMANDE_COD})
	@Transactional
	public void presaInCaricoNonProtocollata() throws Exception {
		Long id = new Long(1000L);
		this.mockMvc.perform(put(ApiUrls.DOMANDE_V1 + "/" + id + "/presaincarico")).andExpect(status().is5xxServerError());
	}

	@Test
	@WithMockUser(username = "UTENTEAPPAG", roles= {Ruoli.EDITA_DOMANDE_COD})
	@Transactional
	public void presaInCaricoNotFound() throws Exception {
		Long id = new Long(9999L);
		this.mockMvc.perform(put(ApiUrls.DOMANDE_V1 + "/" + id + "/presaincarico")).andExpect(status().is5xxServerError());
	}

	@Test
	@Transactional
//	@WithMockUser(username="AAABBB11C22D333E", roles= {Ruoli.VISUALIZZA_DOMANDE_COD})
	@WithMockUser(username="AAABBB11C22D333E")
	@Sql("/customsql/ricercaDomande.sql")
	public void getUltimaDomandaDesktopApprovata() throws Exception {
		final MvcResult mvcResult = mockMvc.perform(get(ApiUrls.DOMANDE_V1 + "/ultima-domanda-utente-corrente")
					.param("statoDomanda", APPROVATA.name())
					.param("tipoDomanda", TipoDomandaRegistrazione.COMPLETA.name())
					)
			   .andDo(print())
			   .andExpect(status().isOk())
			   .andReturn();
		
		DatiDomandaRegistrazioneUtente result = JsonSupport.toObject(mvcResult.getResponse().getContentAsString(), DatiDomandaRegistrazioneUtente.class);
		assertThat(result.getIdProtocollo().equals("PAT-50"));
		assertThat(result.getDataProtocollazione().toLocalDate().isEqual(LocalDate.of(2020,3,18)));
		
		logger.info("END");
	}
	
	
	@Test
	@Transactional
//	@WithMockUser(username="AAABBB11C22D223E", roles= {Ruoli.VISUALIZZA_DOMANDE_COD})
	@WithMockUser(username="AAABBB11C22D223E")
	@Sql("/customsql/ricercaDomande.sql")
	public void getUltimaDomandaDesktopApprovataCodiceFiscaleAssente() throws Exception {
		final MvcResult mvcResult = mockMvc.perform(get(ApiUrls.DOMANDE_V1 + "/ultima-domanda-utente-corrente")
//					.param("codiceFiscale", "FRSLBT76H42E625Z")
					.param("statoDomanda", APPROVATA.name())
					.param("tipoDomanda", TipoDomandaRegistrazione.COMPLETA.name())
				)
			   .andDo(print())
			   .andExpect(status().isNoContent())
			   .andReturn();
		
		logger.info("END");
	}

	private String contentApprovazioneRichiestaUtente() {
		return JsonSupport.toJson(new RichiestaDomandaApprovazione().setNote(NOTE).setTestoMail(TESTO_MAIL));
	}

	private String contentRifiutaRichiestaUtente() {
		return JsonSupport.toJson(new RichiestaDomandaRifiuta().setMotivazioneRifiuto(MOTIVAZIONE_RIFIUTO).setTestoMail(TESTO_MAIL));
	}
}
