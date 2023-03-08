package it.tndigitale.a4g.proxy;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.transaction.Transactional;

import org.h2.tools.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql({ "/agricoltore/schema.sql", "/agricoltore/data.sql" })
@WithMockUser
public class AgricoltoreControllerTest {

	private static Server h2WebServer;

	@BeforeClass
	public static void initTest() throws SQLException {
		h2WebServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082");
		h2WebServer.start();
	}

	@AfterClass
	public static void stopServer() throws SQLException {
		h2WebServer.stop();
	}

	@Autowired
	private MockMvc mockMvc;
	@MockBean(name = "createRestTemplate")
	private RestTemplate restTemplate;
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@Transactional
	public void verificaAgricoltoreAttivo() throws Exception {

		this.mockMvc.perform(get("/api/v1/agricoltore").param("params", "{\"codFisc\":\"TRRCST78B08C794X\",\"annoCamp\":2018}")).andExpect(status().isOk())
				.andExpect(content().string(containsString(getMockInfoAgricoltoreSIAN())));
	}

	@Test
	@Transactional
	public void verificaAgricoltoreAttivoInesistente() throws Exception {
		this.mockMvc.perform(get("/api/v1/agricoltore").param("params", "{\"codFisc\":\"TRRCST78B08C778\",\"annoCamp\":2018}")).andExpect(status().isOk())
				.andExpect(content().string(containsString("{\"infoAgricoltoreSIAN\":null")));
	}

	@Test
	@Transactional
	public void verificaTitoliSIAN() throws Exception {
		this.mockMvc.perform(get("/api/v1/agricoltore").param("params", "{\"codFisc\":\"TRRCST78B08C794X\",\"annoCamp\":2018}")).andExpect(status().isOk())
				.andExpect(content().string(containsString(getMockTitoliSIAN())));
	}

	@Test
	@Transactional
	public void verificaTitoliAgricoltoreInesistente() throws Exception {
		this.mockMvc.perform(get("/api/v1/agricoltore").param("params", "{\"codFisc\":\"TRRCST78B08C778\",\"annoCamp\":2018}")).andExpect(status().isOk())
				.andExpect(content().string(containsString("\"titoliSIAN\":[]")));
	}

	@Test
	@Transactional
	public void verificaGiovaneAgricoltore() throws Exception {

		this.mockMvc.perform(get("/api/v1/agricoltore").param("params", "{\"codFisc\":\"TRRCST78B08C794X\",\"annoCamp\":2018}")).andExpect(status().isOk())
				.andExpect(content().string(containsString("\"flagGiovAgri\":0")));
	}

	private String getMockInfoAgricoltoreSIAN() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/agricoltore/infoAgricoltoreSIAN.json"));
		return objectMapper.writeValueAsString(response);
	}

	private String getMockTitoliSIAN() throws IOException {
		return "[{\"idTitolare\":149949075,\"cuaaTitolare\":null,\"numeroTitolare\":\"000008339867\",\"valoreTitolo\":298.22,\"superficieTitolo\":1.00,\"dataInizioPossesso\":\"20180602\",\"dataFinePossesso\":\"99991231\",\"annoCampagnaInizio\":2017,\"annoCampagnaFine\":9999},{\"idTitolare\":149949077,\"cuaaTitolare\":null,\"numeroTitolare\":\"000008339868\",\"valoreTitolo\":298.22,\"superficieTitolo\":1.00,\"dataInizioPossesso\":\"20180602\",\"dataFinePossesso\":\"99991231\",\"annoCampagnaInizio\":2017,\"annoCampagnaFine\":9999},{\"idTitolare\":149949079,\"cuaaTitolare\":null,\"numeroTitolare\":\"000008339869\",\"valoreTitolo\":86.49,\"superficieTitolo\":0.29,\"dataInizioPossesso\":\"20180602\",\"dataFinePossesso\":\"99991231\",\"annoCampagnaInizio\":2017,\"annoCampagnaFine\":9999},{\"idTitolare\":149949081,\"cuaaTitolare\":null,\"numeroTitolare\":\"000010679296\",\"valoreTitolo\":182.21,\"superficieTitolo\":1.00,\"dataInizioPossesso\":\"20180602\",\"dataFinePossesso\":\"99991231\",\"annoCampagnaInizio\":2017,\"annoCampagnaFine\":9999},{\"idTitolare\":149949083,\"cuaaTitolare\":null,\"numeroTitolare\":\"000010679297\",\"valoreTitolo\":182.21,\"superficieTitolo\":1.00,\"dataInizioPossesso\":\"20180602\",\"dataFinePossesso\":\"99991231\",\"annoCampagnaInizio\":2017,\"annoCampagnaFine\":9999},{\"idTitolare\":149949085,\"cuaaTitolare\":null,\"numeroTitolare\":\"000010679298\",\"valoreTitolo\":182.21,\"superficieTitolo\":1.00,\"dataInizioPossesso\":\"20180602\",\"dataFinePossesso\":\"99991231\",\"annoCampagnaInizio\":2017,\"annoCampagnaFine\":9999},{\"idTitolare\":149949087,\"cuaaTitolare\":null,\"numeroTitolare\":\"000010679299\",\"valoreTitolo\":182.21,\"superficieTitolo\":1.00,\"dataInizioPossesso\":\"20180602\",\"dataFinePossesso\":\"99991231\",\"annoCampagnaInizio\":2017,\"annoCampagnaFine\":9999},{\"idTitolare\":149949089,\"cuaaTitolare\":null,\"numeroTitolare\":\"000010679300\",\"valoreTitolo\":182.21,\"superficieTitolo\":1.00,\"dataInizioPossesso\":\"20180602\",\"dataFinePossesso\":\"99991231\",\"annoCampagnaInizio\":2017,\"annoCampagnaFine\":9999},{\"idTitolare\":149949091,\"cuaaTitolare\":null,\"numeroTitolare\":\"000010679301\",\"valoreTitolo\":182.21,\"superficieTitolo\":1.00,\"dataInizioPossesso\":\"20180602\",\"dataFinePossesso\":\"99991231\",\"annoCampagnaInizio\":2017,\"annoCampagnaFine\":9999},{\"idTitolare\":149949093,\"cuaaTitolare\":null,\"numeroTitolare\":\"000010679302\",\"valoreTitolo\":182.21,\"superficieTitolo\":1.00,\"dataInizioPossesso\":\"20180602\",\"dataFinePossesso\":\"99991231\",\"annoCampagnaInizio\":2017,\"annoCampagnaFine\":9999},{\"idTitolare\":149949095,\"cuaaTitolare\":null,\"numeroTitolare\":\"000010679303\",\"valoreTitolo\":182.21,\"superficieTitolo\":1.00,\"dataInizioPossesso\":\"20180602\",\"dataFinePossesso\":\"99991231\",\"annoCampagnaInizio\":2017,\"annoCampagnaFine\":9999},{\"idTitolare\":149949097,\"cuaaTitolare\":null,\"numeroTitolare\":\"000010679304\",\"valoreTitolo\":182.21,\"superficieTitolo\":1.00,\"dataInizioPossesso\":\"20180602\",\"dataFinePossesso\":\"99991231\",\"annoCampagnaInizio\":2017,\"annoCampagnaFine\":9999},{\"idTitolare\":149949099,\"cuaaTitolare\":null,\"numeroTitolare\":\"000010679305\",\"valoreTitolo\":182.21,\"superficieTitolo\":1.00,\"dataInizioPossesso\":\"20180602\",\"dataFinePossesso\":\"99991231\",\"annoCampagnaInizio\":2017,\"annoCampagnaFine\":9999},{\"idTitolare\":149949101,\"cuaaTitolare\":null,\"numeroTitolare\":\"000010679306\",\"valoreTitolo\":182.21,\"superficieTitolo\":1.00,\"dataInizioPossesso\":\"20180602\",\"dataFinePossesso\":\"99991231\",\"annoCampagnaInizio\":2017,\"annoCampagnaFine\":9999},{\"idTitolare\":149949103,\"cuaaTitolare\":null,\"numeroTitolare\":\"000010679307\",\"valoreTitolo\":182.21,\"superficieTitolo\":1.00,\"dataInizioPossesso\":\"20180602\",\"dataFinePossesso\":\"99991231\",\"annoCampagnaInizio\":2017,\"annoCampagnaFine\":9999},{\"idTitolare\":149949105,\"cuaaTitolare\":null,\"numeroTitolare\":\"000010679308\",\"valoreTitolo\":182.21,\"superficieTitolo\":1.00,\"dataInizioPossesso\":\"20180602\",\"dataFinePossesso\":\"99991231\",\"annoCampagnaInizio\":2017,\"annoCampagnaFine\":9999},{\"idTitolare\":149949107,\"cuaaTitolare\":null,\"numeroTitolare\":\"000010679309\",\"valoreTitolo\":182.21,\"superficieTitolo\":1.00,\"dataInizioPossesso\":\"20180602\",\"dataFinePossesso\":\"99991231\",\"annoCampagnaInizio\":2017,\"annoCampagnaFine\":9999},{\"idTitolare\":149949109,\"cuaaTitolare\":null,\"numeroTitolare\":\"000010679310\",\"valoreTitolo\":182.21,\"superficieTitolo\":1.00,\"dataInizioPossesso\":\"20180602\",\"dataFinePossesso\":\"99991231\",\"annoCampagnaInizio\":2017,\"annoCampagnaFine\":9999},{\"idTitolare\":149949111,\"cuaaTitolare\":null,\"numeroTitolare\":\"000010679311\",\"valoreTitolo\":182.21,\"superficieTitolo\":1.00,\"dataInizioPossesso\":\"20180602\",\"dataFinePossesso\":\"99991231\",\"annoCampagnaInizio\":2017,\"annoCampagnaFine\":9999},{\"idTitolare\":149949113,\"cuaaTitolare\":null,\"numeroTitolare\":\"000010679312\",\"valoreTitolo\":182.21,\"superficieTitolo\":1.00,\"dataInizioPossesso\":\"20180602\",\"dataFinePossesso\":\"99991231\",\"annoCampagnaInizio\":2017,\"annoCampagnaFine\":9999},{\"idTitolare\":149949115,\"cuaaTitolare\":null,\"numeroTitolare\":\"000010679313\",\"valoreTitolo\":182.21,\"superficieTitolo\":1.00,\"dataInizioPossesso\":\"20180602\",\"dataFinePossesso\":\"99991231\",\"annoCampagnaInizio\":2017,\"annoCampagnaFine\":9999},{\"idTitolare\":149949117,\"cuaaTitolare\":null,\"numeroTitolare\":\"000010679314\",\"valoreTitolo\":182.21,\"superficieTitolo\":1.00,\"dataInizioPossesso\":\"20180602\",\"dataFinePossesso\":\"99991231\",\"annoCampagnaInizio\":2017,\"annoCampagnaFine\":9999},{\"idTitolare\":149949119,\"cuaaTitolare\":null,\"numeroTitolare\":\"000010679315\",\"valoreTitolo\":182.21,\"superficieTitolo\":1.00,\"dataInizioPossesso\":\"20180602\",\"dataFinePossesso\":\"99991231\",\"annoCampagnaInizio\":2017,\"annoCampagnaFine\":9999},{\"idTitolare\":149949121,\"cuaaTitolare\":null,\"numeroTitolare\":\"000010679316\",\"valoreTitolo\":182.21,\"superficieTitolo\":1.00,\"dataInizioPossesso\":\"20180602\",\"dataFinePossesso\":\"99991231\",\"annoCampagnaInizio\":2017,\"annoCampagnaFine\":9999},{\"idTitolare\":149949123,\"cuaaTitolare\":null,\"numeroTitolare\":\"000010679317\",\"valoreTitolo\":182.21,\"superficieTitolo\":1.00,\"dataInizioPossesso\":\"20180602\",\"dataFinePossesso\":\"99991231\",\"annoCampagnaInizio\":2017,\"annoCampagnaFine\":9999},{\"idTitolare\":149949125,\"cuaaTitolare\":null,\"numeroTitolare\":\"000010679318\",\"valoreTitolo\":61.95,\"superficieTitolo\":0.34,\"dataInizioPossesso\":\"20180602\",\"dataFinePossesso\":\"99991231\",\"annoCampagnaInizio\":2017,\"annoCampagnaFine\":9999},{\"idTitolare\":149949127,\"cuaaTitolare\":null,\"numeroTitolare\":\"000008339630\",\"valoreTitolo\":361.67,\"superficieTitolo\":0.88,\"dataInizioPossesso\":\"20180602\",\"dataFinePossesso\":\"99991231\",\"annoCampagnaInizio\":2017,\"annoCampagnaFine\":9999},{\"idTitolare\":149949129,\"cuaaTitolare\":null,\"numeroTitolare\":\"000008339631\",\"valoreTitolo\":57.54,\"superficieTitolo\":0.14,\"dataInizioPossesso\":\"20180602\",\"dataFinePossesso\":\"99991231\",\"annoCampagnaInizio\":2017,\"annoCampagnaFine\":9999}]";
	}

}
