package it.tndigitale.a4g.ags;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.SQLException;

import org.h2.tools.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.ags.utente.Ruoli;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
// @AutoConfigureTestDatabase
public class CuaaApplicationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private RestTemplate restTemplate;

	static Server h2WebServer;

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
	@WithMockUser(username = "utenteAppag", roles = { Ruoli.VISUALIZZA_DOMANDA_DU_COD })
	public void getInfoCuaa() throws Exception {
		this.mockMvc.perform(get("/api/v1/cuaa").param("cuaa", "BNRNDR86B08H330Q")).andExpect(status().isOk()).andExpect(content().string(containsString(getCuaa())));
	}

	private static final String getCuaa() {
		return "{\"codiceFiscale\":\"BNRNDR86B08H330Q\",\"cognome\":\"BONORA\",\"nome\":\"ANDREA\",\"sesso\":\"M\",\"dataNascita\":\"1986-02-08\",\"codiceIstatNascita\":\"022153\",\"siglaProvNacita\":\"TN\",\"comuneNascita\":\"RIVA DEL GARDA\",\"indirizzoRecapito\":\"LOCALITA' DOSSI 1 INT 3\",\"codiceIstatRecapito\":22006,\"siglaProvRecapito\":\"TN\",\"comuneRecapito\":\"ARCO\",\"cap\":38062}";
	}
}
