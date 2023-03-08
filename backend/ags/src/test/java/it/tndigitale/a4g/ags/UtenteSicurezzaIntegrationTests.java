package it.tndigitale.a4g.ags;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import it.tndigitale.a4g.ags.api.ApiUrls;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
// @AutoConfigureTestDatabase
public class UtenteSicurezzaIntegrationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void utenzeNonAutenticato403() throws Exception {		
		this.mockMvc.perform(get((ApiUrls.UTENTI_V1 + "/" + ApiUrls.FUNZIONE_UTENZE))).andExpect(status().isForbidden());
	}
}
