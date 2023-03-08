package it.tndigitale.a4g.ags;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import it.tndigitale.a4g.ags.utente.Ruoli;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
// @AutoConfigureTestDatabase
public class PianiColtureApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void getParticelleFromPianiColtureWhenUnauthenticatedShouldReturn403() throws Exception {

		this.mockMvc.perform(get("/api/v1/pianicolture/particelle").param("anno", "2018")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username="utenzaTecnicaCatasto", roles= {Ruoli.VISUALIZZA_PIANICOLTURE_COD})
	public void getParticelleFromPianiColtureShouldReturnAllInfoParticelle() throws Exception {

		this.mockMvc.perform(get("/api/v1/pianicolture/particelle").param("anno", "2018")).andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(jsonPath("$.length()", is(17)));
	}
	
	@Test
	@WithMockUser(username="utenzaTecnicaCatasto", roles= {Ruoli.VISUALIZZA_PIANICOLTURE_COD})
	public void getPianiColtureByInfoParticelleShouldReturnAList() throws Exception {
		this.mockMvc.perform(get("/api/v1/pianicolture").param("anno", "2018").param("params", "{\"codiceComuneCatastale\":\"224\",\"particella\":\"02136\",\"sub\":\"7\"}"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.length()", is(4)));
	}
	
	@Test
	@WithMockUser(username="utenzaTecnicaCatasto", roles= {Ruoli.VISUALIZZA_PIANICOLTURE_COD})
	public void getPianiColtureByInfoParticelleShouldReturnErrorIfCodiceComuneCatastaleIsNotNumeric() throws Exception {
		this.mockMvc.perform(get("/api/v1/pianicolture").param("anno", "2018").param("params", "{\"codiceComuneCatastale\":\"invalid\",\"particella\":\"02136\",\"sub\":\"7\"}"))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser(username="utenzaTecnicaCatasto", roles= {Ruoli.VISUALIZZA_PIANICOLTURE_COD})
	public void getPianiColtureByInfoParticelleShouldReturnErrorIfCodiceComuneCatastaleOrParticellaIsEmpty() throws Exception {
		this.mockMvc.perform(get("/api/v1/pianicolture").param("anno", "2018").param("params", "{\"codiceComuneCatastale\":\"224\",\"sub\":\"7\"}"))
		.andExpect(status().isBadRequest());
		
		this.mockMvc.perform(get("/api/v1/pianicolture").param("anno", "2018").param("params", "{\"particella\":\"02136\",\"sub\":\"7\"}"))
		.andExpect(status().isBadRequest());
	}
}
