package it.tndigitale.a4gistruttoria;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.api.ApiUrls;
import it.tndigitale.a4gistruttoria.dto.AgricoltoreSIAN;
import it.tndigitale.a4gistruttoria.dto.DatiErede;
import it.tndigitale.a4gistruttoria.dto.DettaglioAccoppiatiFilter;
import it.tndigitale.a4gistruttoria.dto.Domanda;
import it.tndigitale.a4gistruttoria.dto.DomandaUnica;
import it.tndigitale.a4gistruttoria.dto.FiltroRicercaDomandeIstruttoria;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiIstruttoriaACS;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiIstruttoriaACZ;
import it.tndigitale.a4gistruttoria.repository.dao.AllevamentoImpegnatoDao;
import it.tndigitale.a4gistruttoria.repository.dao.DatiEredeDao;
import it.tndigitale.a4gistruttoria.repository.dao.DatiFiltroDomandaDao;
import it.tndigitale.a4gistruttoria.repository.dao.DatiPascoloDao;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.dao.RichiestaSuperficieDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gtDatiErede;
import it.tndigitale.a4gistruttoria.repository.model.A4gtDatiFiltroDomanda;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoDomanda;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.service.DomandeService;
import it.tndigitale.a4gistruttoria.util.TipoFiltroDomanda;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.visualizza",
"a4gistruttoria.pac.domandaUnica.edita"})
// @AutoConfigureTestDatabase
// @Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, statements = "alter sequence NXTNBR restart with 1000002")
public class DomandeApplicationTest {

	private static Logger log = LoggerFactory.getLogger(DomandeApplicationTest.class);
	

	public static String jsonQuery(final Connection conn, final String col1, final String col2) {
		return "{\"richiestaDisaccoppiato\":true,\"richiestaSuperfici\":true,\"richiestaZootecnia\":false}";
	}

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private RestTemplate restTemplate;

	@Autowired
	private DatiPascoloDao daoDatiPascolo;
	@Autowired
	private AllevamentoImpegnatoDao daoRichiestaAllevam;
	@Autowired
	private RichiestaSuperficieDao daoRichiestaSuperficie;
	@Autowired
	private DomandeService serviceDomande;
	@Autowired
	private DomandaUnicaDao daoDomanda;

	@Autowired
	private DatiFiltroDomandaDao daoDatiFiltroDomanda;
	@Autowired
	private DatiEredeDao datiEredeDao;

	@Autowired
	protected WebApplicationContext context;


	@Value("${a4gproxy.urlDomandaUnicaElencoCapi}")
	private String urlDomandaUnicaElencoCapi;

	@Value("${zootecnia.interventi.vacchenutricielatte}")
	private String[] interventiCodiciVacche;

	@Before
	public void setUp() {
		// mi serve per avere lo stesso contesto perche' quando chiamo il dao parte il flush ma si perde l'utente e va in errore
		mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}

	public HttpHeaders createHeaders(String utente) {
		String username = utente;
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set(A4gIstruttoriaConstants.HEADER_CF, username);
		return headers;
	}

	@Test
	@Transactional
	@Ignore
	public void testFiltrisemplici() throws Exception {
		this.mockMvc.perform(get("/api/v1/domande/")
				.param("params",
						"{\"sostegno\":\"DISACCOPPIATO\",\"statoDomanda\":\"RICEVIBILE\",\"statoSostegno\":\"RICHIESTO\",\"idDatiSettore\":4,\"cuaa\":\"\",\"denominazione\":\"\",\"campione\":\"TUTTI\",\"giovane\":\"TUTTI\",\"pascoli\":\"TUTTI\",\"riserva\":\"TUTTI\"}")
				.param("paginazione", "{ \"numeroElementiPagina\": 10, \"pagina\": 0}").param("ordinamento", ""))

		.andExpect(status().isOk()).andExpect(content().string(containsString("risultati")));
	}

	@Test
	@Transactional
	@Ignore
	public void testFiltriPascoliSi() throws Exception {
		this.mockMvc.perform(get("/api/v1/domande/")
				.param("params",
						"{\"sostegno\":\"DISACCOPPIATO\",\"statoDomanda\":\"RICEVIBILE\",\"statoSostegno\":\"RICHIESTO\",\"idDatiSettore\":4,\"cuaa\":\"\",\"denominazione\":\"\",\"campione\":\"TUTTI\",\"giovane\":\"TUTTI\",\"pascoli\":\"SI\",\"riserva\":\"TUTTI\"}")
				.param("paginazione", "{ \"numeroElementiPagina\": 10, \"pagina\": 0}").param("ordinamento", ""))

		.andExpect(status().isOk()).andExpect(content().string(containsString("risultati")));
	}

	@Test
	@Transactional
	@Ignore
	public void testFiltriGiovaneSi() throws Exception {
		this.mockMvc.perform(get("/api/v1/domande/")
				.param("params",
						"{\"sostegno\":\"DISACCOPPIATO\",\"statoDomanda\":\"RICEVIBILE\",\"statoSostegno\":\"RICHIESTO\",\"idDatiSettore\":4,\"cuaa\":\"\",\"denominazione\":\"\",\"campione\":\"TUTTI\",\"giovane\":\"SI\",\"pascoli\":\"TUTTI\",\"riserva\":\"TUTTI\"}")
				.param("paginazione", "{ \"numeroElementiPagina\": 10, \"pagina\": 0}").param("ordinamento", ""))

		.andExpect(status().isOk()).andExpect(content().string(containsString("risultati")));
	}

	@Test
	@Transactional
	public void testGetPascolibyAnnoandCuaa() throws Exception {
		this.mockMvc.perform(get("/api/v1/domande/elencoPascoli").param("annoCampagna", "2018").param("cuaa", "00107940223")).andExpect(status().isOk())
		.andExpect(content().string(containsString("136TN099")));
	}

	@Test
	@Transactional
	public void testGetCuaaDomandeFiltrate() throws Exception {
		this.mockMvc.perform(get("/api/v1/domande/elencoCuaa").param("params", "{\"campagna\":2018}")).andExpect(status().isOk())
		.andExpect(content().string(containsString("cuaa")));
	}

	@Test
	@Transactional
	@Ignore
	public void countDomandeRicevibili() throws Exception {
		this.mockMvc.perform(get("/api/v1/domande/count").param("params", "{\"idDatiSettore\":\"4\",\"statoDomanda\":\"RICEVIBILE\"}")).andExpect(status().isOk())
		.andExpect(content().string("1"));
	}

	@Test
	@Transactional
	@Ignore
	public void countDomandeNonRicevibili() throws Exception {
		this.mockMvc.perform(get("/api/v1/domande/count").param("params", "{\"idDatiSettore\":\"4\",\"statoDomanda\":\"NON_RICEVIBILE\"}")).andExpect(status().isOk())
		.andExpect(content().string("1"));
	}

	@Test
	@Transactional
	@Ignore
	public void countDomandeInIstruttoria() throws Exception {
		MvcResult result = this.mockMvc.perform(get("/api/v1/domande/count").param("params", "{\"idDatiSettore\":\"4\",\"statoDomanda\":\"IN_ISTRUTTORIA\"}")).andExpect(status().isOk())
				.andReturn();
		String stringResult = result.getResponse().getContentAsString();
		Long ret = Long.valueOf(stringResult);
		assertTrue(ret.compareTo(0L) > 0);
	}

	@Test
	@Transactional
	@Ignore
	public void countDomandePerStatoInesistente() throws Exception {
		this.mockMvc.perform(get("/api/v1/domande/count").param("params", "{\"idDatiSettore\":\"4\",\"statoDomanda\":\"IN COMPILAZIONE\"}")).andExpect(status().isOk())
		.andExpect(content().string("0"));
	}

	@Test
	@Transactional
	public void elencoStatiPossibiliDomanda() throws Exception {
		this.mockMvc.perform(get("/api/v1/domande/elencoStati")).andExpect(status().isOk()).andExpect(content().string(containsString("\"RICEVIBILE\",\"NON_RICEVIBILE\"")));
	}

	@Test
	@Transactional
	@Ignore
	public void verificaRecuperoDomandePaged() throws Exception {
		this.mockMvc
		.perform(get("/api/v1/domande").param("params", "{\"idDatiSettore\": 4,\"statoDomanda\":\"RICEVIBILE\"}").param("paginazione", "{\"numeroElementiPagina\": 20, \"pagina\": 0}")
				.param("ordinamento", "[{\"proprieta\":\"numeroDomanda\",\"ordine\": \"ASC\"}, {\"proprieta\":\"cuaaIntestatario\", \"ordine\": \"DESC\"}]"))
		.andExpect(status().isOk()).andExpect(content().string(containsString(getResponseDomandePerStatoPaged())));

	}

	@Test
	@Transactional
	@Ignore
	public void verificaRecuperoDomandeNotPaged() throws Exception {
		this.mockMvc.perform(get("/api/v1/domande").param("params", "{\"idDatiSettore\": 4,\"statoDomanda\":\"RICEVIBILE\"}").param("paginazione", "").param("ordinamento", ""))
		.andExpect(status().isOk()).andExpect(content().string(containsString(getResponseDomandePerStatoPaged())));

	}

	@Test
	@Transactional
	@Ignore
	public void verificaErroreRicevimentoDomandaPaginata() throws Exception {
		try {
			this.mockMvc.perform(get("/api/v1/domande").param("params", "{\"test\"}").param("paginazione", "{\"test\"}").param("ordinamento", "{\"test\"}"))
			.andExpect(status().isInternalServerError());
		} catch (Exception e) {

		}
	}

	@Test
	@Transactional
	public void recuperaSostegniDomandaDU() throws Exception {
		String serviceUrlAgs = "http://localhost:8080/ags/api/v1/domandeDU/".concat("191618").concat("?expand=sostegniSuperficie,sostegniAllevamento,datiPascolo,dichiarazioni");
		DomandaUnica domandaDU = objectMapper.readValue(getDomandaUnicaAgsExpanded(), new TypeReference<DomandaUnica>() {
		});
		Mockito.when(restTemplate.getForObject(new URI(serviceUrlAgs), DomandaUnica.class)).thenReturn(domandaDU);

		DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(domandaDU.getInfoGeneraliDomanda().getNumeroDomanda());
		serviceDomande.recuperaSostegniDomandaDU(d);
		Long numeroPascoli = daoDatiPascolo.countByDomandaUnicaModel(d);
		Long numeroSup = daoRichiestaSuperficie.countByDomandaUnicaModel(d);
		Long numeroAllevam = daoRichiestaAllevam.countByDomandaUnica(d);
		assertEquals(new Long(1), numeroPascoli);
		assertEquals(new Long(2), numeroSup);
		assertEquals(new Long(4), numeroAllevam);
	}

	@Test
	@Transactional
	public void verificaRecuperoAgricoltoreSian() throws Exception {
		String jsonRequest = "{ \"codFisc\":\"CMPCRL76L27H612L\", \"annoCamp\":2018}";
		String serviceUrlA4GPROXYrecuperoAgricoltoreSian = "http://localhost:8080/a4gproxy/api/v1/agricoltore?params=".concat(URLEncoder.encode(jsonRequest, "UTF-8"));
		AgricoltoreSIAN responseAgricoltoreSian = objectMapper.readValue(getResponseAgricoltoreSian(), AgricoltoreSIAN.class);
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlA4GPROXYrecuperoAgricoltoreSian)), Mockito.eq(AgricoltoreSIAN.class))).thenReturn(responseAgricoltoreSian);

		this.mockMvc.perform(get("/api/v1/domande/180375/informazioniSian")).andExpect(status().isOk());

	}

	@Test
	@Transactional
	public void elencoCuaaDomandeCampagnaStato() throws Exception {

		FiltroRicercaDomandeIstruttoria filter = new FiltroRicercaDomandeIstruttoria();
		filter.setCampagna(2018);
		filter.setSostegno(Sostegno.DISACCOPPIATO);
		filter.setStatoDomanda("RICEVIBILE");
		filter.setStatoSostegno("RICHIESTO");

		this.mockMvc.perform(get(ApiUrls.DOMANDE_V1 + "/" + ApiUrls.ELENCO_CUAA_CAMPAGNA).param("params", (objectMapper.writeValueAsString(filter)))).andExpect(status().isOk())
		.andExpect(content().string(containsString("CMPCRL76L27H612L"))).andExpect(content().string(containsString("MNGDNE71D07L781V")));
	}
	
	@Test
	@Transactional
	public void search_ElencoDomande_CuaaPartial_StatoSostegno_Sostegno_Anno() throws Exception {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("stato-sostegno", "RICHIESTO");
		params.add("sostegno", Sostegno.DISACCOPPIATO.name());
		params.add("anno-campagna", "2018");
		params.add("cuaa", "CMPCRL76L27");
		params.add("tipo", "SALDO");
		this.mockMvc.perform(get(ApiUrls.DOMANDE_V1 + ApiUrls.ELENCO_CUAA_FILTRATI)
				.params(params))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("CMPCRL76L27H612L")));
	}
	
	@Test
	@Transactional
	public void search_ElencoDomande_RagioneSocialePartial_StatoSostegno_Sostegno_Anno() throws Exception {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("stato-sostegno", "RICHIESTO");
		params.add("sostegno", Sostegno.DISACCOPPIATO.name());
		params.add("anno-campagna", "2018");
		params.add("ragione-sociale", "tente tes");
		params.add("tipo", "SALDO");
		this.mockMvc.perform(get(ApiUrls.DOMANDE_V1 + ApiUrls.ELENCO_RAGIONESOCIALE_FILTRATI)
				.params(params))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("TENTE TES")));
	}

	@Test
	@Transactional
	public void salvaDatiFiltratiDomanda() throws Exception {
		DomandaUnicaModel domanda = new DomandaUnicaModel();
		domanda.setNumeroDomanda(new BigDecimal(182864));
		Optional<DomandaUnicaModel> domandaOpt = daoDomanda.findOne(Example.of(domanda));
		assertEquals(true, domandaOpt.isPresent());
		domanda = domandaOpt.get();

		List<A4gtDatiFiltroDomanda> datiFiltro = daoDatiFiltroDomanda.findByDomandaUnicaModel_id(domanda.getId());
		assertEquals(0, datiFiltro.size());

		serviceDomande.salvaDatiFiltratiDomanda(domanda.getId());

		datiFiltro = daoDatiFiltroDomanda.findByDomandaUnicaModel_id(domanda.getId());
		assertEquals(TipoFiltroDomanda.values().length, datiFiltro.size());

		datiFiltro.forEach(f -> {
			switch (f.getTipoFiltro()) {
			case "PASCOLO":
			case "GIOVANE":
				assertEquals("NO", f.getValore());
				break;
			case "RISERVA_NAZIONALE":
				assertEquals("NON RICHIESTA", f.getValore());
				break;
			default:
			}

		});
	}

	@Test
	@Transactional
	public void elaboraDomandaPerIstruttoria() throws Exception {
		DomandaUnicaModel domanda = new DomandaUnicaModel();
		domanda.setNumeroDomanda(new BigDecimal(183380));
		Optional<DomandaUnicaModel> domandaOpt = daoDomanda.findOne(Example.of(domanda));
		assertEquals(true, domandaOpt.isPresent());
		domanda = domandaOpt.get();

		String serviceUrlAgs = "http://localhost:8080/ags/api/v1/domandeDU/".concat(domanda.getNumeroDomanda().toString()).concat("?expand=sostegniSuperficie,sostegniAllevamento,datiPascolo,dichiarazioni");
		DomandaUnica domandaDU = objectMapper.readValue(getDomandaUnicaAgsExpanded(), new TypeReference<DomandaUnica>() {
		});
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlAgs)), Mockito.eq(DomandaUnica.class))).thenReturn(domandaDU);

		StringBuilder outErrore = new StringBuilder();
		serviceDomande.elaboraDomandaPerIstruttoria(domanda, outErrore);

		assertEquals(domanda.getStato(), StatoDomanda.IN_ISTRUTTORIA);
	}

	@Test
	@Transactional
	public void getRichiesteAllevamDuEsito() throws Exception {
		Long idDomanda = 246L;
		Long idRichiestaAllevamentoEsito = 149149L;
		MvcResult result = this.mockMvc.perform(get("/api/v1/domande/".concat(String.valueOf(idDomanda)).concat("/richiesteallevamduesito/").concat(String.valueOf(idRichiestaAllevamentoEsito))))
				.andExpect(status().isOk()).andReturn();
		JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
		assertEquals("AT818640529", response.path("codiceCapo").textValue());
		assertEquals("AMMISSIBILE", response.path("esito").textValue());

	}

	@Test(expected = NestedServletException.class)
	@Transactional
	public void getRichiesteAllevamDuEsitoKO() throws Exception {
		Long idDomanda = 246L;
		Long idRichiestaAllevamentoEsito = 0L;
		// richiesta vuota
		this.mockMvc.perform(get("/api/v1/domande/".concat(String.valueOf(idDomanda)).concat("/richiesteallevamduesito/").concat(String.valueOf(idRichiestaAllevamentoEsito))))
		.andExpect(status().is5xxServerError());
	}


	// @formatter:off
	// @Test
	public void getDatiASSuccessExpandControlliSostegno() throws Exception {
		this.mockMvc
		.perform(get("/api/v1/domande/252/accoppiatosuperficie").param("expand", "CONTROLLI_SOSTEGNO").param("params",
				getDettaglioAccoppiatoFilter(StatoIstruttoria.CONTROLLI_CALCOLO_OK)))
		.andExpect(status().isOk()).andExpect(jsonPath("$[0].controlliSostegno.successes").isArray()).andExpect(jsonPath("$[0].controlliSostegno.successes", Matchers.hasSize(2)))
		.andExpect(jsonPath("$[0].controlliSostegno.errors").isArray()).andExpect(jsonPath("$[0].controlliSostegno.errors", Matchers.hasSize(3)))
		.andExpect(jsonPath("$[0].controlliSostegno.infos").isArray()).andExpect(jsonPath("$[0].controlliSostegno.infos", Matchers.hasSize(1)))
		.andExpect(jsonPath("$[0].controlliSostegno.warnings").isArray()).andExpect(jsonPath("$[0].controlliSostegno.warnings", Matchers.hasSize(2)))
		.andExpect(jsonPath("$[0].datiIstruttoriaACS").isEmpty()).andExpect(jsonPath("$[0].datiParticellaACS").isEmpty()).andExpect(jsonPath("$[0].datiDomandaACS").isEmpty());
	}

	// @Test
	public void getDatiASSuccessExpandInserimentoDatiIstruttoria() throws Exception {
		this.mockMvc
		.perform(get("/api/v1/domande/252/accoppiatosuperficie").param("expand", "INSERIMENTO_DATI_ISTRUTTORIA").param("params",
				getDettaglioAccoppiatoFilter(StatoIstruttoria.CONTROLLI_CALCOLO_OK)))
		.andExpect(status().isOk()).andExpect(jsonPath("$[0].controlliSostegno").isEmpty()).andExpect(jsonPath("$[0].datiIstruttoriaACS").exists())
		.andExpect(jsonPath("$[0].datiIstruttoriaACS").isNotEmpty()).andExpect(jsonPath("$[0].datiIstruttoriaACS.controlloSigecoLoco").isBoolean())
		.andExpect(jsonPath("$[0].datiIstruttoriaACS.controlloSigecoLoco").value(true)).andExpect(jsonPath("$[0].datiIstruttoriaACS.superficieDeterminataSoiaM8").value(1))
		.andExpect(jsonPath("$[0].datiIstruttoriaACS.superficieDeterminataOlivoPendenzaM16").value(0))
		.andExpect(jsonPath("$[0].datiIstruttoriaACS.superficieDeterminataOlivoQualitaM17").value(8)).andExpect(jsonPath("$[0].datiParticellaACS").isEmpty())
		.andExpect(jsonPath("$[0].datiDomandaACS").isEmpty());
	}

	@Test
	public void getDatiASSuccessExpandSuperficiImpegnate() throws Exception {
		
		this.mockMvc
		.perform(get(ApiUrls.ACC_SUP_DOMANDA_UNICA_V1+"/252/superfici").param("expand", "SUPERFICI_IMPEGNATE"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.supRichiesta").value(25572))
		.andExpect(jsonPath("$.supRichiestaNetta").value(25572))
		.andExpect(jsonPath("$.m8").isEmpty())
		.andExpect(jsonPath("$.m9").isEmpty())
		.andExpect(jsonPath("$.m10").isEmpty())
		.andExpect(jsonPath("$.m11").isEmpty())
		.andExpect(jsonPath("$.m14").isEmpty())
		.andExpect(jsonPath("$.m15").exists())
		.andExpect(jsonPath("$.m15").isNotEmpty())
		.andExpect(jsonPath("$.m15.supRichiesta").value(8524))
		.andExpect(jsonPath("$.m15.supRichiestaNetta").value(8524))
		.andExpect(jsonPath("$.m15.superficiImpegnate").exists())
		.andExpect(jsonPath("$.m15.superficiImpegnate").isNotEmpty())
		.andExpect(jsonPath("$.m16").exists())
		.andExpect(jsonPath("$.m16").isNotEmpty())
		.andExpect(jsonPath("$.m16.supRichiesta").value(8524))
		.andExpect(jsonPath("$.m16.supRichiestaNetta").value(8524))
		.andExpect(jsonPath("$.m16.superficiImpegnate").exists())
		.andExpect(jsonPath("$.m16.superficiImpegnate").isNotEmpty())
		.andExpect(jsonPath("$.m17").exists())
		.andExpect(jsonPath("$.m17").isNotEmpty())
		.andExpect(jsonPath("$.m17.supRichiesta").value(8524))
		.andExpect(jsonPath("$.m17.supRichiestaNetta").value(8524))
		.andExpect(jsonPath("$.m17.superficiImpegnate").exists())
		.andExpect(jsonPath("$.m17.superficiImpegnate").isNotEmpty());
	}

	@Test
	public void getDatiASSuccessExpandDichiarazioni() throws Exception {
		this.mockMvc
		.perform(get(ApiUrls.ACC_SUP_DOMANDA_UNICA_V1+"/252/dichiarazioni"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$").isArray())
		.andExpect(jsonPath("$", Matchers.hasSize(1)))
		.andExpect(jsonPath("$[0].codice").value("DUDICH_16"));
	}

	// @Test
	public void getDatiASSuccessExpandDatiDomanda() throws Exception {
		this.mockMvc
		.perform(get("/api/v1/domande/252/accoppiatosuperficie").param("expand", "DATI_DOMANDA").param("params", getDettaglioAccoppiatoFilter(StatoIstruttoria.CONTROLLI_CALCOLO_OK)))
		.andExpect(status().isOk()).andExpect(jsonPath("$[0].controlliSostegno").isEmpty()).andExpect(jsonPath("$[0].datiIstruttoriaACS").isEmpty())
		.andExpect(jsonPath("$[0].datiParticellaACS").isEmpty()).andExpect(jsonPath("$[0].dichiarazioni").isEmpty()).andExpect(jsonPath("$[0].datiDomandaACS").exists())
		.andExpect(jsonPath("$[0].datiDomandaACS").isNotEmpty()).andExpect(jsonPath("$[0].datiDomandaACS.sintesiCalcolo").exists())
		.andExpect(jsonPath("$[0].datiDomandaACS.sintesiCalcolo").isNotEmpty()).andExpect(jsonPath("$[0].datiDomandaACS.sintesiCalcolo.ACSIMPCALCTOT").value("0 euro"))
		.andExpect(jsonPath("$[0].datiDomandaACS.dettaglioCalcolo").exists()).andExpect(jsonPath("$[0].datiDomandaACS.dettaglioCalcolo").isNotEmpty())
		.andExpect(jsonPath("$[0].datiDomandaACS.dettaglioCalcolo.m8").exists()).andExpect(jsonPath("$[0].datiDomandaACS.dettaglioCalcolo.m8").isEmpty())
		.andExpect(jsonPath("$[0].datiDomandaACS.dettaglioCalcolo.m17").exists()).andExpect(jsonPath("$[0].datiDomandaACS.dettaglioCalcolo.m17").isNotEmpty())
		.andExpect(jsonPath("$[0].datiDomandaACS.dettaglioCalcolo.m17").isMap()).andExpect(jsonPath("$[0].datiDomandaACS.dettaglioCalcolo.m17.ACSIMPCALC_OUTPUT").isEmpty())
		.andExpect(jsonPath("$[0].datiDomandaACS.dettaglioCalcolo.m17.ACSSUPDET_OUTPUT").value("0 ha"))
		.andExpect(jsonPath("$[0].datiDomandaACS.dettaglioCalcolo.m17.ACSSUPIMP_INPUT").value("1898 ha"))
		.andExpect(jsonPath("$[0].datiDomandaACS.dettaglioCalcolo.m17.*", Matchers.hasSize(17))).andExpect(jsonPath("$[0].datiDomandaACS.dettaglioCalcolo.m17.*",
				Matchers.contains("1898 ha", null, null, "SI", "NO", "NO", "0.00 %", "NO", "NO", "1898 ha", "0 ha", "0 ha", null, null, null, null, null)));
	}

	// @Test
	public void getDatiASSuccessNoExpand() throws Exception {
		this.mockMvc.perform(get("/api/v1/domande/252/accoppiatosuperficie").param("params", getDettaglioAccoppiatoFilter(StatoIstruttoria.CONTROLLI_CALCOLO_OK))).andExpect(status().isOk())
		.andExpect(jsonPath("$[0].datiSuperficiImpegnate").exists()).andExpect(jsonPath("$[0].datiSuperficiImpegnate").isNotEmpty())
		.andExpect(jsonPath("$[0].datiSuperficiImpegnate.supRichiesta").value(25572)).andExpect(jsonPath("$[0].datiSuperficiImpegnate.supRichiestaNetta").value(25572))
		.andExpect(jsonPath("$[0].datiSuperficiImpegnate.m8").isEmpty()).andExpect(jsonPath("$[0].datiSuperficiImpegnate.m9").isEmpty())
		.andExpect(jsonPath("$[0].datiSuperficiImpegnate.m10").isEmpty()).andExpect(jsonPath("$[0].datiSuperficiImpegnate.m11").isEmpty())
		.andExpect(jsonPath("$[0].datiSuperficiImpegnate.m14").isEmpty()).andExpect(jsonPath("$[0].datiSuperficiImpegnate.m15").exists())
		.andExpect(jsonPath("$[0].datiSuperficiImpegnate.m15").isNotEmpty()).andExpect(jsonPath("$[0].datiSuperficiImpegnate.m15.supRichiesta").value(8524))
		.andExpect(jsonPath("$[0].datiSuperficiImpegnate.m15.supRichiestaNetta").value(8524)).andExpect(jsonPath("$[0].datiSuperficiImpegnate.m15.superficiImpegnate").exists())
		.andExpect(jsonPath("$[0].datiSuperficiImpegnate.m15.superficiImpegnate").isNotEmpty()).andExpect(jsonPath("$[0].datiSuperficiImpegnate.m16").exists())
		.andExpect(jsonPath("$[0].datiSuperficiImpegnate.m16").isNotEmpty()).andExpect(jsonPath("$[0].datiSuperficiImpegnate.m16.supRichiesta").value(8524))
		.andExpect(jsonPath("$[0].datiSuperficiImpegnate.m16.supRichiestaNetta").value(8524)).andExpect(jsonPath("$[0].datiSuperficiImpegnate.m16.superficiImpegnate").exists())
		.andExpect(jsonPath("$[0].datiSuperficiImpegnate.m16.superficiImpegnate").isNotEmpty()).andExpect(jsonPath("$[0].datiSuperficiImpegnate.m17").exists())
		.andExpect(jsonPath("$[0].datiSuperficiImpegnate.m17").isNotEmpty()).andExpect(jsonPath("$[0].datiSuperficiImpegnate.m17.supRichiesta").value(8524))
		.andExpect(jsonPath("$[0].datiSuperficiImpegnate.m17.supRichiestaNetta").value(8524)).andExpect(jsonPath("$[0].datiSuperficiImpegnate.m17.superficiImpegnate").exists())
		.andExpect(jsonPath("$[0].datiSuperficiImpegnate.m17.superficiImpegnate").isNotEmpty()).andExpect(jsonPath("$[0].controlliSostegno.successes").isArray())
		.andExpect(jsonPath("$[0].controlliSostegno.successes", Matchers.hasSize(2))).andExpect(jsonPath("$[0].controlliSostegno.errors").isArray())
		.andExpect(jsonPath("$[0].controlliSostegno.errors", Matchers.hasSize(3))).andExpect(jsonPath("$[0].controlliSostegno.infos").isArray())
		.andExpect(jsonPath("$[0].controlliSostegno.infos", Matchers.hasSize(1))).andExpect(jsonPath("$[0].controlliSostegno.warnings").isArray())
		.andExpect(jsonPath("$[0].controlliSostegno.warnings", Matchers.hasSize(2))).andExpect(jsonPath("$[0].datiIstruttoriaACS").exists())
		.andExpect(jsonPath("$[0].datiIstruttoriaACS").isNotEmpty()).andExpect(jsonPath("$[0].datiIstruttoriaACS.controlloSigecoLoco").isBoolean())
		.andExpect(jsonPath("$[0].datiIstruttoriaACS.controlloSigecoLoco").value(true)).andExpect(jsonPath("$[0].datiIstruttoriaACS.superficieDeterminataSoiaM8").value(1))
		.andExpect(jsonPath("$[0].datiIstruttoriaACS.superficieDeterminataOlivoPendenzaM16").value(0))
		.andExpect(jsonPath("$[0].datiIstruttoriaACS.superficieDeterminataOlivoQualitaM17").value(8)).andExpect(jsonPath("$[0].datiParticellaACS").exists())
		.andExpect(jsonPath("$[0].datiParticellaACS").isNotEmpty()).andExpect(jsonPath("$[0].datiParticellaACS.m8").isEmpty()).andExpect(jsonPath("$[0].datiParticellaACS.m10").isEmpty())
		.andExpect(jsonPath("$[0].datiParticellaACS.m17").isNotEmpty()).andExpect(jsonPath("$[0].datiParticellaACS.m17").isArray())
		.andExpect(jsonPath("$[0].datiParticellaACS.m17", Matchers.hasSize(5))).andExpect(jsonPath("$[0].datiParticellaACS.m17.[0].particella").value("00336"))
		.andExpect(jsonPath("$[0].datiParticellaACS.m17.[0].supImpegnata").value(1069)).andExpect(jsonPath("$[0].datiParticellaACS.m17.[3].controlloRegioni").value(true))
		.andExpect(jsonPath("$[0].datiParticellaACS.m17.[3].particella").value("00275")).andExpect(jsonPath("$[0].datiParticellaACS.m17.[4].particella").value("00275"))
		.andExpect(jsonPath("$[0].datiParticellaACS.m17.[3].codColtura").value("160-111-011")).andExpect(jsonPath("$[0].datiParticellaACS.m17.[4].codColtura").value("160-111-010"))
		.andExpect(jsonPath("$[0].datiDomandaACS").exists()).andExpect(jsonPath("$[0].datiDomandaACS").isNotEmpty()).andExpect(jsonPath("$[0].datiDomandaACS.sintesiCalcolo").exists())
		.andExpect(jsonPath("$[0].datiDomandaACS.sintesiCalcolo").isNotEmpty()).andExpect(jsonPath("$[0].datiDomandaACS.sintesiCalcolo.ACSIMPCALCTOT").value("0 euro"))
		.andExpect(jsonPath("$[0].datiDomandaACS.dettaglioCalcolo").exists()).andExpect(jsonPath("$[0].datiDomandaACS.dettaglioCalcolo").isNotEmpty())
		.andExpect(jsonPath("$[0].datiDomandaACS.dettaglioCalcolo.m8").exists()).andExpect(jsonPath("$[0].datiDomandaACS.dettaglioCalcolo.m8").isEmpty())
		.andExpect(jsonPath("$[0].datiDomandaACS.dettaglioCalcolo.m17").exists()).andExpect(jsonPath("$[0].datiDomandaACS.dettaglioCalcolo.m17").isNotEmpty())
		.andExpect(jsonPath("$[0].datiDomandaACS.dettaglioCalcolo.m17").isMap()).andExpect(jsonPath("$[0].datiDomandaACS.dettaglioCalcolo.m17.ACSIMPCALC_OUTPUT").isEmpty())
		.andExpect(jsonPath("$[0].datiDomandaACS.dettaglioCalcolo.m17.ACSSUPDET_OUTPUT").value("0 ha"))
		.andExpect(jsonPath("$[0].datiDomandaACS.dettaglioCalcolo.m17.ACSSUPIMP_INPUT").value("1898 ha"))
		.andExpect(jsonPath("$[0].datiDomandaACS.dettaglioCalcolo.m17.*", Matchers.hasSize(17)))
		.andExpect(jsonPath("$[0].datiDomandaACS.dettaglioCalcolo.m17.*",
				Matchers.contains("1898 ha", null, null, "SI", "NO", "NO", "0.00 %", "NO", "NO", "1898 ha", "0 ha", "0 ha", null, null, null, null, null)))
		.andExpect(jsonPath("$[0].dichiarazioni").exists()).andExpect(jsonPath("$[0].dichiarazioni").isNotEmpty()).andExpect(jsonPath("$[0].dichiarazioni").isArray())
		.andExpect(jsonPath("$[0].dichiarazioni", Matchers.hasSize(1))).andExpect(jsonPath("$[0].dichiarazioni[0].codice").value("DUDICH_16"));
	}

	// @Test(expected = NestedServletException.class)
	public void getDatiASNoDomanda() throws Exception {
		this.mockMvc.perform(get("/api/v1/domande/6546546544/accoppiatosuperficie").param("params", getDettaglioAccoppiatoFilter(StatoIstruttoria.CONTROLLI_CALCOLO_OK)));
	}

	// @Test
	public void getDatiASJsonMalformed() throws Exception {
		this.mockMvc.perform(get("/api/v1/domande/253/accoppiatosuperficie/CONTROLLI_CALCOLO_OK"));
	}

	@Test @Ignore
	public void salvaDatiIstruttoriaSuperficie() throws Exception {
		DatiIstruttoriaACS dati = new DatiIstruttoriaACS();
		dati.setControlloSigecoLoco(true);
		dati.setSuperficieDeterminataSoiaM8(BigDecimal.valueOf(1));
		dati.setSuperficieDeterminataOlivoPendenzaM16(BigDecimal.valueOf(0));
		dati.setSuperficieDeterminataOlivoQualitaM17(BigDecimal.valueOf(8));
		this.mockMvc.perform(put("/api/v1/domande/252/datiIstruttoria/datiIstruttoriaSuperficie").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dati)))
		.andExpect(status().isOk()).andExpect(jsonPath("$.controlloSigecoLoco").isBoolean()).andExpect(jsonPath("$.controlloSigecoLoco").value(true))
		.andExpect(jsonPath("$.superficieDeterminataSoiaM8").value(1));
	}

	@Test@Ignore
	public void salvaDatiIstruttoriaSuperficieNoContent() throws Exception {
		DatiIstruttoriaACS dati = new DatiIstruttoriaACS();
		dati.setControlloSigecoLoco(true);
		dati.setSuperficieDeterminataSoiaM8(BigDecimal.valueOf(1));
		this.mockMvc.perform(put("/api/v1/domande/123456789/datiIstruttoria/datiIstruttoriaSuperficie").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dati)))
		.andExpect(status().isNoContent());
	}

	@Test @Ignore
	public void getDatiAZSuccessExpandInserimentoDatiIstruttoria() throws Exception {
		this.mockMvc
		.perform(get("/api/v1/domande/3176/accoppiatozootecnia").param("expand", "INSERIMENTO_DATI_ISTRUTTORIA").param("params",
				getDettaglioAccoppiatoFilter(StatoIstruttoria.CONTROLLI_CALCOLO_OK)))
		.andExpect(status().isOk()).andExpect(jsonPath("$[0].datiIstruttoriaACZ").exists()).andExpect(jsonPath("$[0].datiIstruttoriaACZ").isNotEmpty())
		.andExpect(jsonPath("$[0].datiIstruttoriaACZ.controlloSigecoLoco").isBoolean()).andExpect(jsonPath("$[0].datiIstruttoriaACZ.controlloSigecoLoco").value(false))
		.andExpect(jsonPath("$[0].datiIstruttoriaACZ.controlloAntimafia").isBoolean()).andExpect(jsonPath("$[0].datiIstruttoriaACZ.controlloAntimafia").value(true));
	}

	@Test
	public void getDatiAZSuccessExpandDichiarazioni() throws Exception {
		this.mockMvc
			.perform(get(ApiUrls.ACC_ZOO_DOMANDA_UNICA_V1+"/3176/dichiarazioni"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$", Matchers.hasSize(1)))
			.andExpect(jsonPath("$[0].codice").value("DUDICH_16"));
	}

	// @Test
	public void getDatiAZSuccessExpandControlliSostegno() throws Exception {
		this.mockMvc
		.perform(get("/api/v1/domande/3176/accoppiatozootecnia").param("expand", "CONTROLLI_SOSTEGNO").param("params",
				getDettaglioAccoppiatoFilter(StatoIstruttoria.CONTROLLI_CALCOLO_KO)))
		.andExpect(status().isOk()).andExpect(jsonPath("$[0].controlliSostegno.successes").isArray()).andExpect(jsonPath("$[0].controlliSostegno.successes", Matchers.hasSize(2)))
		.andExpect(jsonPath("$[0].controlliSostegno.errors").isArray()).andExpect(jsonPath("$[0].controlliSostegno.errors").isEmpty())
		.andExpect(jsonPath("$[0].controlliSostegno.warnings").isArray()).andExpect(jsonPath("$[0].controlliSostegno.warnings").isEmpty())
		.andExpect(jsonPath("$[0].controlliSostegno.infos").isArray()).andExpect(jsonPath("$[0].controlliSostegno.infos", Matchers.hasSize(2)))
		.andExpect(jsonPath("$[0].dichiarazioni").isEmpty()).andExpect(jsonPath("$[0].datiDomandaACZ").isEmpty());
	}

	// @Test
	public void getDatiAZSuccessExpandDatiDomanda() throws Exception {
		this.mockMvc
		.perform(get("/api/v1/domande/3176/accoppiatozootecnia").param("expand", "DATI_DOMANDA").param("params", getDettaglioAccoppiatoFilter(StatoIstruttoria.CONTROLLI_CALCOLO_KO)))
		.andExpect(status().isOk()).andExpect(jsonPath("$[0].datiDomandaACZ").exists()).andExpect(jsonPath("$[0].datiDomandaACZ").isNotEmpty())
		.andExpect(jsonPath("$[0].datiDomandaACZ.sintesiCalcolo").exists()).andExpect(jsonPath("$[0].datiDomandaACZ.sintesiCalcolo").isNotEmpty())
		.andExpect(jsonPath("$[0].datiDomandaACZ.sintesiCalcolo.ACZIMPCALCTOT").value("0 euro")).andExpect(jsonPath("$[0].datiDomandaACZ.dettaglioCalcolo").exists())
		.andExpect(jsonPath("$[0].datiDomandaACZ.dettaglioCalcolo").isNotEmpty()).andExpect(jsonPath("$[0].datiDomandaACZ.dettaglioCalcolo.int313").exists())
		.andExpect(jsonPath("$[0].datiDomandaACZ.dettaglioCalcolo.int313").isEmpty()).andExpect(jsonPath("$[0].datiDomandaACZ.dettaglioCalcolo.int310").exists())
		.andExpect(jsonPath("$[0].datiDomandaACZ.dettaglioCalcolo.int310").isNotEmpty()).andExpect(jsonPath("$[0].datiDomandaACZ.dettaglioCalcolo.int310").isMap())
		.andExpect(jsonPath("$[0].datiDomandaACZ.dettaglioCalcolo.int310.ACZIMPRID_OUTPUT").isEmpty())
		.andExpect(jsonPath("$[0].datiDomandaACZ.dettaglioCalcolo.int310.ACZUBALAT_INPUT").value("15 UBA"))
		.andExpect(jsonPath("$[0].datiDomandaACZ.dettaglioCalcolo.int310.ACZVAL_INPUT").value("65.76 euro"))
		.andExpect(jsonPath("$[0].datiDomandaACZ.dettaglioCalcolo.int310.*", Matchers.hasSize(21)))
		.andExpect(jsonPath("$[0].datiDomandaACZ.dettaglioCalcolo.int310.*",
				Matchers.contains("17 ", "3 ", "14 ", "5 ", "9 ", "15 UBA", "65.76 euro", "SI", "NO", "NO", "0.00 %", "55.56 %", "100.00 %", null, null, null, null, null, null, null, null)))
		.andExpect(jsonPath("$[0].dichiarazioni").isEmpty()).andExpect(jsonPath("$[0].datiIstruttoriaACZ").isEmpty()).andExpect(jsonPath("$[0].controlliSostegno").isEmpty());
	}

	// @Test
	public void getDatiAZSuccessNoExpand() throws Exception {
		this.mockMvc.perform(get("/api/v1/domande/3176/accoppiatozootecnia").param("params", getDettaglioAccoppiatoFilter(StatoIstruttoria.CONTROLLI_CALCOLO_KO))).andExpect(status().isOk())
//		.andExpect(jsonPath("$[0].datiIstruttoriaACZ").exists()).andExpect(jsonPath("$[0].datiIstruttoriaACZ").isNotEmpty())
//		.andExpect(jsonPath("$[0].datiIstruttoriaACZ.controlloSigecoLoco").isBoolean()).andExpect(jsonPath("$[0].datiIstruttoriaACZ.controlloSigecoLoco").value(false))
//		.andExpect(jsonPath("$[0].datiIstruttoriaACZ.controlloAntimafia").isBoolean()).andExpect(jsonPath("$[0].datiIstruttoriaACZ.controlloAntimafia").value(true))
		.andExpect(jsonPath("$[0].controlliSostegno.successes").isArray()).andExpect(jsonPath("$[0].controlliSostegno.successes", Matchers.hasSize(2)))
		.andExpect(jsonPath("$[0].controlliSostegno.errors").isArray()).andExpect(jsonPath("$[0].controlliSostegno.errors").isEmpty())
		.andExpect(jsonPath("$[0].controlliSostegno.warnings").isArray()).andExpect(jsonPath("$[0].controlliSostegno.warnings").isEmpty())
		.andExpect(jsonPath("$[0].controlliSostegno.infos").isArray()).andExpect(jsonPath("$[0].controlliSostegno.infos", Matchers.hasSize(2)))
		.andExpect(jsonPath("$[0].datiDomandaACZ").exists()).andExpect(jsonPath("$[0].datiDomandaACZ").isNotEmpty()).andExpect(jsonPath("$[0].datiDomandaACZ.sintesiCalcolo").exists())
		.andExpect(jsonPath("$[0].datiDomandaACZ.sintesiCalcolo").isNotEmpty()).andExpect(jsonPath("$[0].datiDomandaACZ.sintesiCalcolo.ACZIMPCALCTOT").value("0 euro"))
		.andExpect(jsonPath("$[0].datiDomandaACZ.dettaglioCalcolo").exists()).andExpect(jsonPath("$[0].datiDomandaACZ.dettaglioCalcolo").isNotEmpty())
		.andExpect(jsonPath("$[0].datiDomandaACZ.dettaglioCalcolo.int313").exists()).andExpect(jsonPath("$[0].datiDomandaACZ.dettaglioCalcolo.int313").isEmpty())
		.andExpect(jsonPath("$[0].datiDomandaACZ.dettaglioCalcolo.int310").exists()).andExpect(jsonPath("$[0].datiDomandaACZ.dettaglioCalcolo.int310").isNotEmpty())
		.andExpect(jsonPath("$[0].datiDomandaACZ.dettaglioCalcolo.int310").isMap()).andExpect(jsonPath("$[0].datiDomandaACZ.dettaglioCalcolo.int310.ACZIMPRID_OUTPUT").isEmpty())
		.andExpect(jsonPath("$[0].datiDomandaACZ.dettaglioCalcolo.int310.ACZUBALAT_INPUT").value("15 UBA"))
		.andExpect(jsonPath("$[0].datiDomandaACZ.dettaglioCalcolo.int310.ACZVAL_INPUT").value("65.76 euro"))
		.andExpect(jsonPath("$[0].datiDomandaACZ.dettaglioCalcolo.int310.*", Matchers.hasSize(21)))
		.andExpect(jsonPath("$[0].datiDomandaACZ.dettaglioCalcolo.int310.*",
				Matchers.contains("17 ", "3 ", "14 ", "5 ", "9 ", "15 UBA", "65.76 euro", "SI", "NO", "NO", "0.00 %", "55.56 %", "100.00 %", null, null, null, null, null, null, null, null)))
		.andExpect(jsonPath("$[0].dichiarazioni").exists()).andExpect(jsonPath("$[0].dichiarazioni").isNotEmpty()).andExpect(jsonPath("$[0].dichiarazioni").isArray())
		.andExpect(jsonPath("$[0].dichiarazioni", Matchers.hasSize(1))).andExpect(jsonPath("$[0].dichiarazioni[0].codice").value("DUDICH_16"));
	}

	// @Test(expected = NestedServletException.class)
	public void getDatiAZNoDomanda() throws Exception {
		this.mockMvc.perform(get("/api/v1/domande/6546546544/accoppiatozootecnia").param("params", getDettaglioAccoppiatoFilter(StatoIstruttoria.CONTROLLI_CALCOLO_OK)));
	}

	@Test @Ignore
	public void salvaDatiIstruttoriaZootecnia() throws Exception {
		DatiIstruttoriaACZ dati = new DatiIstruttoriaACZ();
		dati.setControlloSigecoLoco(false);
		dati.setControlloAntimafia(true);
		this.mockMvc.perform(put("/api/v1/domande/3176/datiIstruttoria/datiIstruttoriaZootecnia").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dati)))
		.andExpect(status().isOk()).andExpect(jsonPath("$.controlloSigecoLoco").isBoolean()).andExpect(jsonPath("$.controlloSigecoLoco").value(false))
		.andExpect(jsonPath("$.controlloAntimafia").isBoolean()).andExpect(jsonPath("$.controlloAntimafia").value(true))
		.andExpect(content().string(equalTo("{\"id\":null,\"controlloSigecoLoco\":false,\"controlloAntimafia\":true}")));
	}

	@Test
	public void salvaDatiIstruttoriaZootecniaNoContent() throws Exception {
		DatiIstruttoriaACZ dati = new DatiIstruttoriaACZ();
		dati.setControlloSigecoLoco(false);

		this.mockMvc.perform(put("/api/v1/domande/123456789/datiIstruttoria/datiIstruttoriaZootecnia").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dati)))
		.andExpect(status().is4xxClientError());
	}

	private String getListaCapiDomanda(String cuaa, String numeroIntervento) throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/zootecnia/".concat(cuaa).concat("_").concat(numeroIntervento).concat(".json")));
		return objectMapper.writeValueAsString(response);
	}

	private String getResponseDatiRicevibilita() {
		return "{\"infoGeneraliDomanda\":{\"pac\":\"PAC1420\",\"tipoDomanda\":\"DU\",\"campagna\":2018,\"modulo\":\"PAGAMENTI DIRETTI\",\"codModulo\":\"BPS_2018\",\"cuaaIntestatario\":\"00318870227\",\"ragioneSociale\":\"ZAMBOTTI DANILO DENIS E RENATO\",\"numeroDomanda\":188802,\"numeroDomandaRettificata\":null,\"dataPresentazione\":\"2018-06-04T15:26:20.000+0200\",\"codEnteCompilatore\":\"5\",\"enteCompilatore\":\"CAA COLDIRETTI DEL TRENTINO - 004\"},\"controlliPresentazione\":{\"aggiornamentoFascicolo\":\"S\",\"visioneAnomalie\":\"S\",\"firmaDomanda\":\"S\",\"archiviazioneDomanda\":\"S\"},\"richieste\": {\"sintesiRichieste\": {\"richiestaDisaccoppiato\": true,\"richiestaSuperfici\":false,\"richiestaZootecnia\": true}}";
	}

	private String getResponseDatiRicevibilitaGiaPresente() {
		return "{\"infoGeneraliDomanda\":{\"pac\":\"PAC1420\",\"tipoDomanda\":\"DU\",\"campagna\":2018,\"modulo\":\"PAGAMENTI DIRETTI\",\"codModulo\":\"BPS_2018\",\"cuaaIntestatario\":\"00390360212\",\"ragioneSociale\":\"AZIENDA VITIVINICOLA CASTEL MONREALE SRL\",\"numeroDomanda\":181409,\"numeroDomandaRettificata\":null,\"dataPresentazione\":\"2018-04-18T15:26:20.000+0200\",\"codEnteCompilatore\":\"20\",\"enteCompilatore\":\"CAA ATS - 001 - TRENTO\"},\"controlliPresentazione\":{\"aggiornamentoFascicolo\":\"S\",\"visioneAnomalie\":\"S\",\"firmaDomanda\":\"S\",\"archiviazioneDomanda\":\"S\"}}";
	}

	private String getResponseDatiDomandaAgs() {
		String result = "{\"infoGeneraliDomanda\": {\"pac\": \"PAC1420\",\"tipoDomanda\": \"DU\", \"campagna\": 2018,\"modulo\": \"PAGAMENTI DIRETTI\",\r\n"
				+ "        \"codModulo\": \"BPS_2018\",\"cuaaIntestatario\": \"02187510223\",\"ragioneSociale\": \"LE MANDRE SOCIETA' AGRICOLA SEMPLICE\",\r\n"
				+ "        \"numeroDomanda\": 189562,\"numeroDomandaRettificata\": null,\"dataPresentazione\": \"2018-06-08T12:42:48.000+0200\",\r\n"
				+ "        \"codEnteCompilatore\": \"668\",\"enteCompilatore\": \"CAA COLDIRETTI DEL TRENTINO - 012\",\r\n"
				+ "        \"dataProtocollazione\": \"2018-06-08T12:44:48.000+0200\",\"dataPresentazOriginaria\": null,\"dataProtocollazOriginaria\": null\r\n"
				+ "    },\"controlliPresentazione\": {\"aggiornamentoFascicolo\": \"S\",\"visioneAnomalie\": \"S\",\r\n"
				+ "        \"firmaDomanda\": \"S\",\"archiviazioneDomanda\": \"S\"}, \"richieste\": {\"sintesiRichieste\": {\r\n"
				+ "            \"richiestaDisaccoppiato\": true,\"richiestaSuperfici\": false,\"richiestaZootecnia\": true }}\r\n" + "}";

		return result;
	}

	private String getResponseDatiDomandaAgsNonRicevibile() {
		String result = "{\"infoGeneraliDomanda\": {\"pac\": \"PAC1420\",\"tipoDomanda\": \"DU\", \"campagna\": 2018,\"modulo\": \"PAGAMENTI DIRETTI\",\r\n"
				+ "        \"codModulo\": \"BPS_2018\",\"cuaaIntestatario\": \"02187510224\",\"ragioneSociale\": \"LE MANDRE SOCIETA' AGRICOLA SEMPLICE\",\r\n"
				+ "        \"numeroDomanda\": 182859,\"numeroDomandaRettificata\": null,\"dataPresentazione\": \"2018-06-08T12:42:48.000+0200\",\r\n"
				+ "        \"codEnteCompilatore\": \"669\",\"enteCompilatore\": \"CAA COLDIRETTI DEL TRENTINO - 012\",\r\n"
				+ "        \"dataProtocollazione\": \"2018-06-08T12:44:48.000+0200\",\"dataPresentazOriginaria\": null,\"dataProtocollazOriginaria\": null\r\n"
				+ "    },\"controlliPresentazione\": {\"aggiornamentoFascicolo\": \"S\",\"visioneAnomalie\": \"S\",\r\n"
				+ "        \"firmaDomanda\": \"N\",\"archiviazioneDomanda\": \"N\"}, \"richieste\": {\"sintesiRichieste\": {\r\n"
				+ "            \"richiestaDisaccoppiato\": true,\"richiestaSuperfici\": false,\"richiestaZootecnia\": true }}\r\n" + "}";
		return result;
	}

	private String getResponseDomandePerStatoPaged() {
		return "BBBFBA66E31F187R";
	}

	private String getDomandaUnicaAgsExpanded() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/1_domandaUnicaAgsExpanded.json"));
		return objectMapper.writeValueAsString(response);
	}

	private String getResponseAgricoltoreSian() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/agricoltoreSian.json"));
		return objectMapper.writeValueAsString(response);
	}

	private String getResponseAgricoltoreAttivo() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/agricoltoreAttivo.json"));
		return objectMapper.writeValueAsString(response);
	}

	private String getResponseAgricoltoreNonAttivo() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/agricoltoreNonAttivo.json"));
		return objectMapper.writeValueAsString(response);
	}

	private String getRequestDomandaIntegrativaSalva() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaIntegrativa/requestSalva.json"));
		return objectMapper.writeValueAsString(response);
	}

	private String getRequestDomandaIntegrativaSalvaIntNonRichiesto() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaIntegrativa/requestSalvaIntNonRichiesto.json"));
		return objectMapper.writeValueAsString(response);
	}

	private String getRequestDomandaIntegrativaSalvaNoEsito() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaIntegrativa/requestSalvaNoEsito.json"));
		return objectMapper.writeValueAsString(response);
	}

	private String getRequestDomandaIntegrativaSalvaDupliceInt() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaIntegrativa/requestSalvaDupliceInt.json"));
		return objectMapper.writeValueAsString(response);
	}

	private String getRequestAggiornaStatoDomande() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/zootecnia/getRequestAggiornaStatoDomande.json"));
		return objectMapper.writeValueAsString(response);
	}

	private String getDettaglioAccoppiatoFilter(StatoIstruttoria statoLavorazioneSostegno) throws Exception {
		DettaglioAccoppiatiFilter filter = new DettaglioAccoppiatiFilter();
		filter.setStatoLavorazioneSostegno(statoLavorazioneSostegno);
		return objectMapper.writeValueAsString(filter);
	}

	@Transactional
	@Test
	public void aggiornaDomanda() throws Exception {

		Domanda domanda = new Domanda();
		domanda.setAnnulloRiduzione(true);
		domanda.setStatoDomanda(StatoDomanda.RICEVIBILE);
		domanda.setDtPresentazione(new Date());
		domanda.setDtProtocollazione(new Date());
		domanda.setCuaaIntestatario("CUAA_UPDATED");
		domanda.setNumeroDomanda(new BigDecimal("53"));
		domanda.setCodModuloDomanda("COD_MOD_UPDATED");
		domanda.setDescModuloDomanda("DESC_MOD_UPDATED");
		domanda.setCodEnteCompilatore(new BigDecimal("53"));
		domanda.setDescEnteCompilatore("DESC_ENTE_UPDATED");
		MvcResult result = mockMvc.perform(put("/api/v1/domande/53").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(domanda))).andExpect(status().is2xxSuccessful())
				.andReturn();

		Domanda domandaAggiornata = objectMapper.readValue(result.getResponse().getContentAsString(), Domanda.class);

		assertNotNull(domandaAggiornata);

		DomandaUnicaModel domandaDB = daoDomanda.getOne(53L);

		assertNull(domandaDB.getRagioneSociale());
		assertEquals(domanda.getCodModuloDomanda(), domandaDB.getCodModuloDomanda());
		assertEquals(domanda.getCuaaIntestatario(), domandaDB.getCuaaIntestatario());
		assertEquals(domanda.getDtProtocollazione(), domandaDB.getDtProtocollazione());
		assertEquals(domanda.getCodEnteCompilatore(), domandaDB.getCodEnteCompilatore());
		assertEquals(domanda.getStatoDomanda(), domandaDB.getStato());
//		assertEquals(domanda.getAnnulloRiduzione(), domandaDB.getAnnulloRiduzione());
	}

	@Transactional
	@Test(expected = NestedServletException.class)
	@Ignore
	public void aggiornaDomandaKO() throws Exception {

		Domanda domanda = new Domanda();
//		domanda.setAnnulloRiduzione(true);
//		domanda.setStatoDomanda(StatoDomandaEnum.STATO_NON_ESISTENTE);
		domanda.setDtPresentazione(new Date());
		domanda.setDtProtocollazione(new Date());
		domanda.setCuaaIntestatario("CUAA_UPDATED");
		domanda.setNumeroDomanda(new BigDecimal("53"));
		domanda.setCodModuloDomanda("COD_MOD_UPDATED");
		domanda.setDescModuloDomanda("DESC_MOD_UPDATED");
		domanda.setCodEnteCompilatore(new BigDecimal("53"));
		domanda.setDescEnteCompilatore("DESC_ENTE_UPDATED");
		mockMvc.perform(put("/api/v1/domande/53").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(domanda))).andExpect(status().is5xxServerError());
	}

	@Transactional
	@Test
	public void creaErede() throws Exception {

		DatiErede datiErede = new DatiErede();
		datiErede.setCapResidenza("80808");
		datiErede.setCertificato(false);
		datiErede.setCodiceFiscale("ABCDEFGHILMNOPQR");
		datiErede.setCodiceIstat("istatCod");
		datiErede.setCodIstatNascita("istatN");
		datiErede.setCognome("cognome");
		datiErede.setDtNascita(new Date());
		datiErede.setIban("MY_IBAN");
		datiErede.setIndirizzoResidenza("indirizzo");
		datiErede.setNome("nome");
		datiErede.setProvResidenza("NA");
		datiErede.setSesso("M");

		MvcResult result = mockMvc.perform(post("/api/v1/domande/541/eredi").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(datiErede)))
				.andExpect(status().is2xxSuccessful()).andReturn();

		A4gtDatiErede a4gtDatiErede = new A4gtDatiErede();
		a4gtDatiErede.setDomandaUnicaModel(daoDomanda.getOne(541L));
		Optional<A4gtDatiErede> a4gtDatiEredeOpt = datiEredeDao.findOne(Example.of(a4gtDatiErede));
		if (!a4gtDatiEredeOpt.isPresent()) {
			fail();
		}

		A4gtDatiErede eredeDB = a4gtDatiEredeOpt.get();

		assertEquals(datiErede.getCapResidenza(), eredeDB.getCap());
		assertEquals(datiErede.getCertificato(), eredeDB.getCertificato());
		assertEquals(datiErede.getCodiceFiscale(), eredeDB.getCodiceFiscale());
		assertEquals(datiErede.getCodiceIstat(), eredeDB.getCodiceIstat());
		assertEquals(datiErede.getCodIstatNascita(), eredeDB.getCodIstatNascita());
		assertEquals(datiErede.getCognome(), eredeDB.getCognome());
		assertEquals(datiErede.getIban(), eredeDB.getIban());
		assertEquals(datiErede.getIndirizzoResidenza(), eredeDB.getIndirizzo());
		assertEquals(datiErede.getNome(), eredeDB.getNome());
		assertEquals(datiErede.getProvNascita(), eredeDB.getProvinciaNascita());
		assertEquals(datiErede.getProvResidenza(), eredeDB.getProvincia());
		assertEquals(datiErede.getSesso(), eredeDB.getSesso());
		System.out.println(datiErede.getDtNascita());
		System.out.println(eredeDB.getDtNascita());

		assertNotNull(eredeDB.getDtUltimoAggiornamento());

		DatiErede datiEredeResult = objectMapper.readValue(result.getResponse().getContentAsString(), DatiErede.class);
		assertNotNull(datiEredeResult);
	}

	@Transactional
	@Test
	public void aggiornaErede() throws Exception {

		DatiErede datiErede = new DatiErede();
		datiErede.setCapResidenza("80808");
		datiErede.setCertificato(false);
		datiErede.setCodiceFiscale("ABCDEFGHILMNOPQR");
		datiErede.setCodiceIstat("istatCod");
		datiErede.setCodIstatNascita("istatN");
		datiErede.setCognome("cognome");
		datiErede.setDtNascita(new Date());
		datiErede.setIban("MY_IBAN");
		datiErede.setIndirizzoResidenza("indirizzo");
		datiErede.setNome("nome");
		datiErede.setProvResidenza("NA");
		datiErede.setSesso("M");

		MvcResult result = mockMvc.perform(put("/api/v1/domande/551/eredi/511").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(datiErede)))
				.andExpect(status().is2xxSuccessful()).andReturn();

		A4gtDatiErede a4gtDatiErede = new A4gtDatiErede();
		a4gtDatiErede.setDomandaUnicaModel(daoDomanda.getOne(551L));
		Optional<A4gtDatiErede> a4gtDatiEredeOpt = datiEredeDao.findOne(Example.of(a4gtDatiErede));
		if (!a4gtDatiEredeOpt.isPresent()) {
			fail();
		}

		A4gtDatiErede eredeDB = a4gtDatiEredeOpt.get();

		assertEquals(datiErede.getCapResidenza(), eredeDB.getCap());
		assertEquals(datiErede.getCertificato(), eredeDB.getCertificato());
		assertEquals(datiErede.getCodiceFiscale(), eredeDB.getCodiceFiscale());
		assertEquals(datiErede.getCodiceIstat(), eredeDB.getCodiceIstat());
		assertEquals(datiErede.getCodIstatNascita(), eredeDB.getCodIstatNascita());
		assertEquals(datiErede.getCognome(), eredeDB.getCognome());
		assertEquals(datiErede.getIban(), eredeDB.getIban());
		assertEquals(datiErede.getIndirizzoResidenza(), eredeDB.getIndirizzo());
		assertEquals(datiErede.getNome(), eredeDB.getNome());
		assertEquals(datiErede.getProvNascita(), eredeDB.getProvinciaNascita());
		assertEquals(datiErede.getProvResidenza(), eredeDB.getProvincia());
		assertEquals(datiErede.getSesso(), eredeDB.getSesso());
		System.out.println(datiErede.getDtNascita());
		System.out.println(eredeDB.getDtNascita());

		assertNotNull(eredeDB.getDtUltimoAggiornamento());

		DatiErede datiEredeResult = objectMapper.readValue(result.getResponse().getContentAsString(), DatiErede.class);
		assertNotNull(datiEredeResult);
	}

	@Transactional
	@Test(expected = NestedServletException.class)
	public void aggiornaEredeKO() throws Exception {

		DatiErede datiErede = new DatiErede();
		datiErede.setCapResidenza("80808");
		datiErede.setCertificato(false);
		datiErede.setCodiceFiscale("ABCDEFGHILMNOPQR");
		datiErede.setCodiceIstat("istatCod");
		datiErede.setCodIstatNascita("istatN");
		datiErede.setCognome("cognome");
		datiErede.setDtNascita(new Date());
		datiErede.setIban("MY_IBAN");
		datiErede.setIndirizzoResidenza("indirizzo");
		datiErede.setNome("nome");
		datiErede.setProvResidenza("NA");
		datiErede.setSesso("M");
		// dati erede che si vuole aggiornare non esistente
		mockMvc.perform(put("/api/v1/domande/551/eredi/12345").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(datiErede))).andExpect(status().is5xxServerError());
	}

	@Test
	public void checkDomandaInPagamentoReturnsFalse() throws Exception {
		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna("DPDFRZ65C21C794B", 2018);
		assertFalse(serviceDomande.checkDomandaInPagamento(domanda.getId()));
	}

	@Test
	public void checkDomandaInPagamentoReturnsTrue() throws Exception {
		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna("CODELENC11111ACZ", 2018);
		assertTrue(serviceDomande.checkDomandaInPagamento(domanda.getId()));
	}

	@Test
	@Transactional
	public void annullaIstruttoriaDomandaOK() throws Exception {
		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna("DPDFRZ65C21C794B", 2018);
		serviceDomande.annullaIstruttoriaDomanda(domanda.getId());
		domanda = daoDomanda.findByCuaaIntestatarioAndCampagna("DPDFRZ65C21C794B", 2018);
		assertNull(domanda);
	}
}