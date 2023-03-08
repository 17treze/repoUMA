package it.tndigitale.a4g.proxy.bdn;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.h2.tools.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.istruttoria.client.model.CuaaDenominazione;
import it.tndigitale.a4g.proxy.bdn.client.A4gIstruttoriaClient;
import it.tndigitale.a4g.proxy.bdn.wsbdninterrogazioni.service.InterrogazioniAziendeServiceImpl;
import it.tndigitale.a4g.proxy.ws.bdn.dsregistripascolig.DsREGISTRIPASCOLIG;
import it.tndigitale.ws.dsaziendeg.DsAZIENDEG;
import it.tndigitale.ws.dsaziendeg.DsAZIENDEG.AZIENDE;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente")
public class A4gproxyBdnApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private A4gIstruttoriaClient a4gIstruttoriaClient;

	static Server h2WebServer;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	InterrogazioniAziendeServiceImpl aziendaService;

	@BeforeClass
	public static void initTest() throws SQLException {
		h2WebServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082");
		h2WebServer.start();
	}

	@AfterClass
	public static void stopServer() throws SQLException {
		h2WebServer.stop();
	}

	@Test
	@Transactional
	public void verificaSincBdnPuntale() throws Exception {

		List<String> listPascoli = Arrays.asList("016TN022");
		Mockito.when(a4gIstruttoriaClient.getElencoPascoli(Mockito.anyInt(), Mockito.any())).thenReturn(listPascoli);

		this.mockMvc.perform(put("/api/v1/sync")
				.content("{\"annoCampagna\": 2018, \"cuaa\": \"RCCLNZ88T20L378D\"}")
				.contentType("application/json"))
		.andExpect(status().isOk());

	}

	@Test
	@Transactional
	public void verificaSincBdnMassivo() throws Exception {

		String res = "[\r\n" + 
				"  {\r\n" + 
				"    \"cuaa\": \"MSTBVN59B12L457O\",\r\n" + 
				"    \"denominazione\": \"MAISTRELLI BENVENUTO\"\r\n" + 
				"  },\r\n" + 
				"  {\r\n" + 
				"    \"cuaa\": \"GLNSFN60D01H517U\",\r\n" + 
				"    \"denominazione\": \"GIULIANI STEFANO\"\r\n" + 
				"  },\r\n" + 
				"  {\r\n" + 
				"    \"cuaa\": \"PLLPLA76E06C794R\",\r\n" + 
				"    \"denominazione\": \"PELLIZZARI PAOLO\"\r\n" + 
				"  }]";

		List<CuaaDenominazione> listaCuaa = objectMapper.readValue(res, new TypeReference<List<CuaaDenominazione>>(){});
		Mockito.when(a4gIstruttoriaClient.getElencoCuaa(Mockito.anyInt())).thenReturn(listaCuaa);

		List<String> listPascoli = Arrays.asList("016TN022");
		Mockito.when(a4gIstruttoriaClient.getElencoPascoli(Mockito.anyInt(), Mockito.any())).thenReturn(listPascoli);
		this.mockMvc.perform(post("/api/v1/sync")
				.content("{\"annoCampagna\": 2018}")
				.contentType("application/json"))
		.andExpect(status().isOk());
	}

	@Test
	@Transactional
	@Sql(scripts = { "/movimentazionePascoli/038TN801_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/movimentazionePascoli/038TN801_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void movimentazionePascolo038TN801_2018() throws Exception {
		String codPascolo = "038TN801";
		Integer annoCampagna = 2018;
		this.mockMvc.perform(get("/api/v1/sync/movimentiPascolo")
				.param("params", "{\"annoCampagna\": " + annoCampagna + ", \"codPascolo\": \"" + codPascolo + "\"}")
				.contentType("application/json"))
		// .andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.*", org.hamcrest.Matchers.hasSize(3)))
		.andExpect(jsonPath("$[*].codPascolo", hasItem(is(codPascolo))))
		.andExpect(jsonPath("$[*].annoCampagna", hasItem(is(annoCampagna))));

	}

	@Test
	@Transactional
	@Sql(scripts = { "/movimentazionePascoli/038TN801_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/movimentazionePascoli/038TN801_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void movimentazionePascolo038TN801_2017() throws Exception {
		this.mockMvc.perform(get("/api/v1/sync/movimentiPascolo")
				.param("params", "{\"annoCampagna\": 2017, \"codPascolo\": \"038TN801\"}")
				.contentType("application/json"))
		// .andDo(print())
		//.andExpect(status().isNoContent())
		.andExpect(status().isNoContent());

	}

	@Test
	@Transactional
	@Sql(scripts = { "/movimentazionePascoli/196TN090_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/movimentazionePascoli/196TN090_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void movimentazionePascolo196TN090_2018() throws Exception {
		String codPascolo = "196TN090";
		Integer annoCampagna = 2018;
		this.mockMvc.perform(get("/api/v1/sync/movimentiPascolo")
				.param("params", "{\"annoCampagna\": " + annoCampagna + ", \"codPascolo\": \"" + codPascolo + "\"}")
				.contentType("application/json"))
		// .andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.*", org.hamcrest.Matchers.hasSize(3)))
		.andExpect(jsonPath("$[*].codPascolo", hasItem(is(codPascolo))))
		.andExpect(jsonPath("$[*].annoCampagna", hasItem(is(annoCampagna))));

	}

	@Test
	@Transactional
	@Sql(scripts = { "/movimentazionePascoli/196TN159_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/movimentazionePascoli/196TN159_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void movimentazionePascolo196TN159_2018() throws Exception {
		String codPascolo = "196TN159";
		Integer annoCampagna = 2018;
		this.mockMvc.perform(get("/api/v1/sync/movimentiPascolo")
				.param("params", "{\"annoCampagna\": " + annoCampagna + ", \"codPascolo\": \"" + codPascolo + "\"}")
				.contentType("application/json"))
		// .andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.*", org.hamcrest.Matchers.hasSize(3))) // Inserisco 4 record di cui uno ha data fine chiusa: totale 4 - 1
		.andExpect(jsonPath("$[*].codPascolo", hasItem(is(codPascolo))))
		.andExpect(jsonPath("$[*].annoCampagna", hasItem(isA(Integer.class))))
		.andExpect(jsonPath("$[*].annoCampagna", hasItem(is(annoCampagna))))
		.andExpect(jsonPath("$[*].codiceFiscaleDetentore", hasItem("ZWRFBA93R05C372U")));
	}

	@Test
	@Transactional
	@Sql(scripts = { "/movimentazionePascoli/072TN035_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/movimentazionePascoli/072TN035_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void movimentazionePascolo072TN035_2018() throws Exception {
		String codPascolo = "072TN035";
		Integer annoCampagna = 2018;
		this.mockMvc.perform(get("/api/v1/sync/movimentiPascolo")
				.param("params", "{\"annoCampagna\": " + annoCampagna + ", \"codPascolo\": \"" + codPascolo + "\"}")
				.contentType("application/json"))
		// .andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.*", org.hamcrest.Matchers.hasSize(3))) // Inserisco 3 record
		.andExpect(jsonPath("$[*].codPascolo", hasItem(is(codPascolo))))
		.andExpect(jsonPath("$[*].annoCampagna", hasItem(isA(Integer.class))))
		.andExpect(jsonPath("$[*].annoCampagna", hasItem(is(annoCampagna))));
	}

	@Test
	@Transactional
	@Sql(scripts = { "/movimentazionePascoli/145TN055_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/movimentazionePascoli/145TN055_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void movimentazionePascolo145TN055_2018() throws Exception {
		String codPascolo = "145TN055";
		Integer annoCampagna = 2018;
		this.mockMvc.perform(get("/api/v1/sync/movimentiPascolo")
				.param("params", "{\"annoCampagna\": " + annoCampagna + ", \"codPascolo\": \"" + codPascolo + "\"}")
				.contentType("application/json"))
		// .andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.*", org.hamcrest.Matchers.hasSize(4))) // Inserisco 8 record di cui 4 hanno data fine chiusa: totale 8 - 4
		.andExpect(jsonPath("$[*].codPascolo", hasItem(is(codPascolo))))
		.andExpect(jsonPath("$[*].annoCampagna", hasItem(isA(Integer.class))))
		.andExpect(jsonPath("$[*].annoCampagna", hasItem(is(annoCampagna))))
		.andExpect(jsonPath("$[*].codiceFiscaleDetentore", hasItem("BTTLSS59B10C372P")))
		.andExpect(jsonPath("$[*].codiceFiscaleDetentore", hasItem("02418430225")));
	}

	@Test
	public void sincronizzaAzienda223TN045() throws Exception {
		String codAzienda = "223TN045";

		DsAZIENDEG.AZIENDE result = new AZIENDE();
		result.setCODICE(codAzienda);
		result.setCOMCODICE("225");
		Mockito.when(aziendaService.getAzienda((codAzienda))).thenReturn(result);


		this.mockMvc.perform(put("/api/v1/sync/azienda/" + codAzienda))		
		.andExpect(status().isOk());

	}

	@Test
	public void sincronizzaAzienda227TN001() throws Exception {
		String codAzienda = "227TN001";

		DsAZIENDEG.AZIENDE result = new AZIENDE();
		result.setCODICE(codAzienda);
		result.setCOMCODICE("225");
		Mockito.when(aziendaService.getAzienda((codAzienda))).thenReturn(result);

		this.mockMvc.perform(put("/api/v1/sync/azienda/" + codAzienda))		
		.andExpect(status().isOk());

	}
}
