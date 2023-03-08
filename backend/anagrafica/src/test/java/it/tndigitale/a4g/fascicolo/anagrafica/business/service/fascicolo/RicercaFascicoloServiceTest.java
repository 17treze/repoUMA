package it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo;

import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import it.tndigitale.a4g.fascicolo.anagrafica.api.ApiUrls;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaUtenteClient;
import it.tndigitale.a4g.utente.client.model.RappresentaIlModelloPerRappresentareUnUtenteDelSistema;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente", roles = {"a4gfascicolo.fascicolo.ricerca.tutti"})
class RicercaFascicoloServiceTest {
	
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private RestTemplate restTemplate;
	@MockBean
	private AnagraficaUtenteClient anagraficaUtenteClient;
	
	
	@Test
	@Sql(scripts = "/sql/fascicolo/ricerca_fascicolo_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/ricerca_fascicolo_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void ricercaPerCaaa() throws Exception {
		String cuaa = "DPDNDR77B03L378X";
		this.mockMvc.perform(
				get(ApiUrls.FASCICOLO)
				.param("cuaa", cuaa))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.count", is(1)))
			.andExpect(jsonPath("$.risultati[*].cuaa", everyItem(is(cuaa))));
	}
	
	@Test
	@Sql(scripts = "/sql/fascicolo/ricerca_fascicolo_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/ricerca_fascicolo_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void ricercaPerDenominazione() throws Exception {
		String ragioneSociale = "andrea";
		this.mockMvc.perform(
				get(ApiUrls.FASCICOLO)
				.param("ragioneSociale", ragioneSociale))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.count", is(1)))
			.andExpect(jsonPath("$.risultati[*].ragioneSociale", everyItem(containsStringIgnoringCase(ragioneSociale))));
	}
	
	@Test
	@Sql(scripts = "/sql/fascicolo/permessi_revoca_immediata_fascicolo_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/permessi_revoca_immediata_fascicolo_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void permessiRevocaImmediataUtenteCorrente_CuaaPersonaFisicaTrue() throws Exception {
		String cuaa = "DPDNDR77B03L378X";
		
		RappresentaIlModelloPerRappresentareUnUtenteDelSistema user = new RappresentaIlModelloPerRappresentareUnUtenteDelSistema();
		user.setIdentificativo("DPDNDR77B03L378X");
		user.setCodiceFiscale("DPDNDR77B03L378X");
		Mockito.when(anagraficaUtenteClient.getUtenteConnesso()).thenReturn(user);
		
		
		String endpoint = String.format("/%s/check-apertura-fascicolo", cuaa);
		this.mockMvc.perform(
				get(ApiUrls.FASCICOLO.concat(endpoint))
				)
			.andExpect(status().isOk())
			.andExpect(content().string("true"));
	}
	
	@Test
	@Sql(scripts = "/sql/fascicolo/permessi_revoca_immediata_fascicolo_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/permessi_revoca_immediata_fascicolo_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void permessiRevocaImmediataUtenteCorrente_CuaaPersonaFisicaFalse() throws Exception {
		String cuaa = "DPDNDR77B03L378X";
		
		RappresentaIlModelloPerRappresentareUnUtenteDelSistema user = new RappresentaIlModelloPerRappresentareUnUtenteDelSistema();
		user.setIdentificativo("XPDNDR77B03L378X");
		user.setCodiceFiscale("XPDNDR77B03L378X");
		Mockito.when(anagraficaUtenteClient.getUtenteConnesso()).thenReturn(user);
		
		String endpoint = String.format("/%s/check-apertura-fascicolo", cuaa);
		this.mockMvc.perform(
				get(ApiUrls.FASCICOLO.concat(endpoint))
				)
			.andExpect(status().isOk())
			.andExpect(content().string("false"));
	}
	
	@Test
	@Sql(scripts = "/sql/fascicolo/permessi_revoca_immediata_fascicolo_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/permessi_revoca_immediata_fascicolo_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void permessiRevocaImmediataUtenteCorrente_CuaaPersonaGiuridicaFalse() throws Exception {
		String cuaa = "00959460221";
		
		RappresentaIlModelloPerRappresentareUnUtenteDelSistema user = new RappresentaIlModelloPerRappresentareUnUtenteDelSistema();
		user.setIdentificativo("XPDNDR77B03L378X");
		user.setCodiceFiscale("XPDNDR77B03L378X");
		Mockito.when(anagraficaUtenteClient.getUtenteConnesso()).thenReturn(user);
		
		
		String endpoint = String.format("/%s/check-apertura-fascicolo", cuaa);
		this.mockMvc.perform(
				get(ApiUrls.FASCICOLO.concat(endpoint))
				)
			.andExpect(status().isOk())
			.andExpect(content().string("false"));
	}
	
}
