package it.tndigitale.a4gistruttoria;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.api.ApiUrls;
import it.tndigitale.a4gistruttoria.dto.AgricoltoreSIAN;
import it.tndigitale.a4gistruttoria.dto.DomandaUnica;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DomandaUnicaDettaglio;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.service.DomandeService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.visualizza", "a4gistruttoria.pac.domandaUnica.edita" })
// @AutoConfigureTestDatabase
@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "delete from a4gd_coltura_intervento")
@Sql({ "/DomandaUnica/dataMatriceCompatibilita.sql" })
public class DomandaDettaglioApplicationTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private RestTemplate restTemplate;

	@Autowired
	private DomandeService serviceDomande;
	@Autowired
	private DomandaUnicaDao daoDomanda;

	@Test
	@Transactional
	public void getDomandaDettaglioCalcoli() throws Exception {

		String numeroDomanda = "181662";

		DomandaUnicaModel domanda = new DomandaUnicaModel();
		domanda.setNumeroDomanda(BigDecimal.valueOf(new Long(numeroDomanda)));
		Optional<DomandaUnicaModel> domandaOpt = daoDomanda.findOne(Example.of(domanda));
		assertEquals(true, domandaOpt.isPresent());

		if (!domandaOpt.isPresent()) {
			return;
		}

		domanda = domandaOpt.get();

		MvcResult andReturn = this.mockMvc.perform(get(ApiUrls.DOMANDE_V1 + "/" + domanda.getId() + "/" + ApiUrls.DOMANDA_DETTAGLIO)).andExpect(status().isOk()).andReturn();
		DomandaUnicaDettaglio domandaDettaglio = objectMapper.readValue(andReturn.getResponse().getContentAsString(), DomandaUnicaDettaglio.class);

		assertEquals(new Double("44060.0"), domandaDettaglio.getSupImpegnataLorda());
		assertEquals(new Double("44060.0"), domandaDettaglio.getSupImpegnataNetta());
		// assertEquals(4, domandaDettaglio.getInformazioniDomanda().size());
		assertEquals(2, domandaDettaglio.getDichiarazioni().size());
		assertNotNull(domandaDettaglio.getControlliSostegno());
		assertEquals(1, domandaDettaglio.getControlliSostegno().size());
		assertNotNull(domandaDettaglio.getDatiDomanda());
		// assertEquals(87, domandaDettaglio.getDatiDomanda().size());
		assertEquals(1, domandaDettaglio.getDatiDomanda().stream().filter(x -> x.getCodice1().equals("SINTESI")).collect(Collectors.toList()).size());
		assertEquals(18, domandaDettaglio.getDatiDomanda().stream().filter(x -> x.getCodice1().equals("AMMISSIBILITA")).collect(Collectors.toList()).size());
		assertEquals(12, domandaDettaglio.getDatiDomanda().stream().filter(x -> x.getCodice1().equals("ANOMALIE_MANTENIMENTO")).collect(Collectors.toList()).size());
		assertEquals(14, domandaDettaglio.getDatiDomanda().stream().filter(x -> x.getCodice1().equals("RIDUZIONI_BPS")).collect(Collectors.toList()).size());
		assertEquals(7, domandaDettaglio.getDatiDomanda().stream().filter(x -> x.getCodice1().equals("SANZIONI_BPS")).collect(Collectors.toList()).size());
		assertEquals(28, domandaDettaglio.getDatiDomanda().stream().filter(x -> x.getCodice1().equals("CONTROLLI_FINALI")).collect(Collectors.toList()).size());

		// List<ControlloFrontend> a = domandaDettaglio.getDatiDomanda().stream().filter(x -> x.getCodice1().equals("GIOVANE_AGRICOLTORE")).collect(Collectors.toList());
		// System.out.println(objectMapper.writeValueAsString(a));

		// assertEquals(11, domandaDettaglio.getDatiDomanda().stream().filter(x -> x.getCodice1().equals("GIOVANE_AGRICOLTORE")).collect(Collectors.toList()).size());
		assertEquals(15, domandaDettaglio.getDatiDomanda().stream().filter(x -> x.getCodice1().equals("GREENING")).collect(Collectors.toList()).size());
	}

	@Test
	@Transactional
	public void getDomandaDettaglioNoCalcoli() throws Exception {
		String numeroDomanda = "183430";

		DomandaUnicaModel domanda = new DomandaUnicaModel();
		domanda.setNumeroDomanda(BigDecimal.valueOf(new Long(numeroDomanda)));
		Optional<DomandaUnicaModel> domandaOpt = daoDomanda.findOne(Example.of(domanda));
		assertEquals(true, domandaOpt.isPresent());

		if (!domandaOpt.isPresent()) {
			return;
		}

		domanda = domandaOpt.get();
		String serviceUrlAgs = "http://localhost:8080/ags/api/v1/domandeDU/".concat(domanda.getNumeroDomanda().toString())
				.concat("?expand=sostegniSuperficie,sostegniAllevamento,datiPascolo,dichiarazioni");
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlAgs)), Mockito.eq(DomandaUnica.class))).thenReturn(getDomandaUnicaAgsExpanded());

		serviceDomande.recuperaSostegniDomandaDU(domanda);

		String jsonRequest = "{ \"codFisc\":\"" + domanda.getCuaaIntestatario() + "\", \"annoCamp\":2018}";
		String serviceUrlA4GPROXYrecuperoAgricoltoreSian = "http://localhost:8080/a4gproxy/api/v1/agricoltore?params=".concat(URLEncoder.encode(jsonRequest, "UTF-8"));
		AgricoltoreSIAN responseAgricoltoreSian = objectMapper.readValue(getResponseAgricoltoreSian(), AgricoltoreSIAN.class);
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlA4GPROXYrecuperoAgricoltoreSian)), Mockito.eq(AgricoltoreSIAN.class))).thenReturn(responseAgricoltoreSian);

		String serviceUrlAgsEleggibili = "http://localhost:8080/ags/api/v1/domandeDU/".concat(domanda.getNumeroDomanda().toString()).concat("?expand=infoIstruttoria");
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlAgsEleggibili)), Mockito.eq(DomandaUnica.class))).thenReturn(getInfoIsturttoria());

		String jsonRequestSigeco = "{\"anno\": 2018, \"numeroDomanda\": " + numeroDomanda + "}";
		String serviceUrlEsitoSigeco = "http://localhost:8080/a4gproxy/api/v1/sigeco?params=".concat(URLEncoder.encode(jsonRequestSigeco, "UTF-8"));
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlEsitoSigeco)), Mockito.eq(Long.class))).thenReturn(null);

		MvcResult andReturn1 = this.mockMvc.perform(get(ApiUrls.DOMANDE_V1 + "/" + domanda.getId() + "/" + ApiUrls.DOMANDA_DETTAGLIO)).andExpect(status().isOk()).andReturn();
		DomandaUnicaDettaglio domandaDettaglio = objectMapper.readValue(andReturn1.getResponse().getContentAsString(), DomandaUnicaDettaglio.class);

		assertEquals(new Double("141148"), domandaDettaglio.getSupImpegnataLorda());
		assertEquals(new Double("141148"), domandaDettaglio.getSupImpegnataNetta());
		// assertEquals(4, domandaDettaglio.getInformazioniDomanda().size());
		assertEquals(2, domandaDettaglio.getDichiarazioni().size());
		assertEquals(null, domandaDettaglio.getControlliSostegno());
		assertEquals(null, domandaDettaglio.getDatiDomanda());
	}


	/*
	@Test
	@Transactional
	public void getParticellaColtura() throws Exception {

		String numeroDomanda = "181662";

		DomandaUnicaModel domanda = new DomandaUnicaModel();
		domanda.setNumeroDomanda(BigDecimal.valueOf(new Long(numeroDomanda)));
		Optional<DomandaUnicaModel> domandaOpt = daoDomanda.findOne(Example.of(domanda));
		assertEquals(true, domandaOpt.isPresent());

		if (!domandaOpt.isPresent()) {
			return;
		}

		domanda = domandaOpt.get();

		MvcResult andReturn = this.mockMvc
				.perform(get(ApiUrls.DOMANDE_V1 + "/" + domanda.getId() + "/" + ApiUrls.DOMANDA_DETTAGLIO_PARTICELLE)
						.param("params", "{\"identificativoSostegno\": \"DISACCOPPIATO\", \"isPascolo\":false}").param("paginazione", "{\"numeroElementiPagina\": 20, \"pagina\": 0}")
						.param("ordinamento", ""))
				.andExpect(status().isOk()).andExpect(jsonPath("$.risultati.*", org.hamcrest.Matchers.hasSize(20))).andExpect(jsonPath("$.elementiTotali").value("64")).andReturn();
	}*/

	@Test
	@Transactional
	public void getParticelleRichieste() throws Exception {

		String numeroDomanda = "181662";

		DomandaUnicaModel domanda = new DomandaUnicaModel();
		domanda.setNumeroDomanda(BigDecimal.valueOf(new Long(numeroDomanda)));
		Optional<DomandaUnicaModel> domandaOpt = daoDomanda.findOne(Example.of(domanda));
		assertEquals(true, domandaOpt.isPresent());

		if (!domandaOpt.isPresent()) {
			return;
		}

		domanda = domandaOpt.get();

		this.mockMvc
				.perform(get(ApiUrls.DISACCOPPIATO_DOMANDA_UNICA_V1 + "/" + domanda.getId() + "/superfici")
						.param("numeroElementiPagina", "20")
						.param("pagina", "0")
						.param("proprieta", "comune")
						.param("ordine", "ASC")
						)
				.andExpect(status().isOk()).andExpect(jsonPath("$.paginaSuperfici.risultati.*", org.hamcrest.Matchers.hasSize(20))).andExpect(jsonPath("$.paginaSuperfici.count").value("67")).andReturn();
	}

	/*
	@Test
	@Transactional
	public void getParticellaColturaOrder() throws Exception {

		String numeroDomanda = "181662";

		DomandaUnicaModel domanda = new DomandaUnicaModel();
		domanda.setNumeroDomanda(BigDecimal.valueOf(new Long(numeroDomanda)));
		Optional<DomandaUnicaModel> domandaOpt = daoDomanda.findOne(Example.of(domanda));
		assertEquals(true, domandaOpt.isPresent());

		if (!domandaOpt.isPresent()) {
			return;
		}

		domanda = domandaOpt.get();

		MvcResult andReturn = this.mockMvc
				.perform(get(ApiUrls.DOMANDE_V1 + "/" + domanda.getId() + "/" + ApiUrls.DOMANDA_DETTAGLIO_PARTICELLE)
						.param("params", "{\"identificativoSostegno\": \"DISACCOPPIATO\", \"isPascolo\":false}").param("paginazione", "{\"numeroElementiPagina\": 20, \"pagina\": 0}")
						.param("ordinamento", "[{\"proprieta\":\"comune\",\"ordine\": \"ASC\"}]"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.risultati.*", org.hamcrest.Matchers.hasSize(20))).andExpect(jsonPath("$.elementiTotali").value("64")).andReturn();
	}*/

	@Test
	@Transactional
	public void getParticelleRichiesteOrder() throws Exception {

		String numeroDomanda = "181662";

		DomandaUnicaModel domanda = new DomandaUnicaModel();
		domanda.setNumeroDomanda(BigDecimal.valueOf(new Long(numeroDomanda)));
		Optional<DomandaUnicaModel> domandaOpt = daoDomanda.findOne(Example.of(domanda));
		assertEquals(true, domandaOpt.isPresent());

		if (!domandaOpt.isPresent()) {
			return;
		}

		domanda = domandaOpt.get();

		this.mockMvc
		.perform(get(ApiUrls.DISACCOPPIATO_DOMANDA_UNICA_V1 + "/" + domanda.getId() + "/superfici")
				.param("numeroElementiPagina", "20")
				.param("pagina", "0")
				.param("proprieta", "comune")
				.param("ordine", "ASC")
				)
		.andExpect(status().isOk()).andExpect(jsonPath("$.paginaSuperfici.risultati.*", org.hamcrest.Matchers.hasSize(20))).andExpect(jsonPath("$.paginaSuperfici.count").value("67")).andReturn();
	}

	private DomandaUnica getDomandaUnicaAgsExpanded() throws IOException {
		return objectMapper.readValue(new File("src/test/resources/DomandaUnica/calcoloDisaccoppiato/183109_AgsExpanded.json"), DomandaUnica.class);
	}

	private String getResponseAgricoltoreSian() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/calcoloDisaccoppiato/2_InfoAgricoltoreSIAN.json"));
		return objectMapper.writeValueAsString(response);
	}

	private DomandaUnica getInfoIsturttoria() throws IOException {
		return objectMapper.readValue(new File("src/test/resources/DomandaUnica/calcoloDisaccoppiato/183109_AgsExpandInfoIstruttoria.json"), DomandaUnica.class);
	}

	public static String jsonValue(final Connection conn, final String col1, final String col2) {
		String param = col2.replace("$.", "");
		String value1;
		String value2;

		if (col1.contains(param + "\":\"")) {
			value1 = col1.substring(col1.indexOf(param) + param.length() + 3);
			value2 = value1.substring(0, value1.indexOf("\""));

			return value2;
		} else if (col1.contains(param + "\":")) {
			value1 = col1.substring(col1.indexOf(param) + param.length() + 2);
			if (value1.contains(",")) {
				value2 = value1.substring(0, value1.indexOf(","));
				return value2;
			} else {
				value2 = value1.substring(0, value1.indexOf("}"));
				return value2;
			}
		} else {
			return "";
		}
	}
}
