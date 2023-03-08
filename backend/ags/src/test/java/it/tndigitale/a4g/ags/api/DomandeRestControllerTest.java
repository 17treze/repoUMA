package it.tndigitale.a4g.ags.api;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import it.tndigitale.a4g.ags.dto.DatiSettore;
import it.tndigitale.a4g.ags.utente.Ruoli;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
// @AutoConfigureTestDatabase
public class DomandeRestControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	DatiSettore datiSettore;
	
	@Test
	@WithMockUser(username = "utenteAppag", roles = { Ruoli.VISUALIZZA_DOMANDA_DU_COD })
	public void contaDomande() throws Exception {
		BigDecimal annoRiferimento = new BigDecimal(2019);
		String codicePac = "PAC1420";
		String tipoDomanda = "DU";
		
		this.mockMvc.perform(get("/api/v1/domandeDU/count").param("annoRiferimento", annoRiferimento.toString()).param("codicePAC", codicePac.toString()).param("tipoDomanda", tipoDomanda)).andExpect(status().isOk())
		.andExpect(content().string(containsString("0")));
	}
	
	@Test
	public void contaDomandeNonAutenticato403() throws Exception {
		BigDecimal annoRiferimento = new BigDecimal(2019);
		String codicePac = "PAC1420";
		String tipoDomanda = "DU";
		
		this.mockMvc.perform(get("/api/v1/domandeDU/count").param("annoRiferimento", annoRiferimento.toString()).param("codicePAC", codicePac.toString()).param("tipoDomanda", tipoDomanda))
				.andExpect(status().isForbidden());
	}
	
	@Test
	@WithMockUser(username = "utenteAppag")
	public void contaDomandeNonAutorizzato403() throws Exception {
		BigDecimal annoRiferimento = new BigDecimal(2019);
		String codicePac = "PAC1420";
		String tipoDomanda = "DU";
		
		this.mockMvc.perform(get("/api/v1/domandeDU/count").param("annoRiferimento", annoRiferimento.toString()).param("codicePAC", codicePac.toString()).param("tipoDomanda", tipoDomanda))
				.andExpect(status().isForbidden());
	}

	
}
