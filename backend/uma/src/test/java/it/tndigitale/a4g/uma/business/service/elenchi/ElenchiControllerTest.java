package it.tndigitale.a4g.uma.business.service.elenchi;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;

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

import it.tndigitale.a4g.fascicolo.anagrafica.client.model.SportelloFascicoloDto;
import it.tndigitale.a4g.fascicolo.territorio.client.model.ParticellaDto;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.uma.Ruoli;
import it.tndigitale.a4g.uma.builder.ColturaTestBuilder;
import it.tndigitale.a4g.uma.builder.ParticellaTestBuilder;
import it.tndigitale.a4g.uma.business.service.client.UmaAnagraficaClient;
import it.tndigitale.a4g.uma.business.service.client.UmaDotazioneTecnicaClient;
import it.tndigitale.a4g.uma.business.service.client.UmaTerritorioClient;

@SpringBootTest
@AutoConfigureMockMvc
class ElenchiControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private UmaAnagraficaClient anagraficaClient;
	@MockBean
	private UmaTerritorioClient territorioClient;
	@MockBean
	private UmaDotazioneTecnicaClient dotazioneTecnicaClient;
	@MockBean
	private Clock clock;

	@Test
	@Sql(scripts = "/sql/elenchi/domande_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/elenchi/domande_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = {Ruoli.DOMANDE_UMA_RICERCA_ENTE_COD})
	void scaricaElencoDomandeUma_ente() throws Exception {

		var sportelloAbilitato = new SportelloFascicoloDto().cuaaList(Arrays.asList("00123890220", "BBBDNL95R14L378T")).identificativoSportello(1);

		Mockito.when(anagraficaClient.getSportelliFascicoli()).thenReturn(Arrays.asList(sportelloAbilitato));

		String csv = mockMvc.perform(get("/api/v1/elenchi/2022/stampa").param("tipo", TipoElenco.DOMANDE.name()))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		assertNotNull(csv);
	}

	@Test
	@Sql(scripts = "/sql/elenchi/domande_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/elenchi/domande_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = {Ruoli.DOMANDE_UMA_RICERCA_TUTTI_COD})
	void scaricaElencoDomandeUma_istruttore() throws Exception {

		String csv = mockMvc.perform(get("/api/v1/elenchi/2022/stampa").param("tipo", TipoElenco.DOMANDE.name()))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		assertNotNull(csv);
	}

	@Test
	@Sql(scripts = "/sql/elenchi/domande_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/elenchi/domande_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = {Ruoli.DOMANDE_UMA_RICERCA_ENTE_COD})
	void scaricaElencoInadempientiUma_ente() throws Exception {

		var sportelloAbilitato = new SportelloFascicoloDto().cuaaList(Arrays.asList("STDKRD52P15B160V")).identificativoSportello(1);
		Mockito.when(anagraficaClient.getSportelliFascicoli()).thenReturn(Arrays.asList(sportelloAbilitato));

		String csv = mockMvc.perform(get("/api/v1/elenchi/2022/stampa").param("tipo", TipoElenco.INADEMPIENTI.name()))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		assertNotNull(csv);
	}


	@Test
	@Sql(scripts = "/sql/elenchi/domande_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/elenchi/domande_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = {Ruoli.DOMANDE_UMA_RICERCA_TUTTI_COD})
	void scaricaElencoInadempientiUma_istruttore() throws Exception {

		String csv = mockMvc.perform(get("/api/v1/elenchi/2022/stampa").param("tipo", TipoElenco.INADEMPIENTI.name()))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		assertNotNull(csv);
	}


	@Test
	@Sql(scripts = "/sql/elenchi/domande_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/elenchi/domande_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = {Ruoli.DOMANDE_UMA_RICERCA_TUTTI_COD})
	void scaricaElencoAccise_istruttore() throws Exception {
		
		Mockito.when(dotazioneTecnicaClient.getFabbricati(Mockito.anyString(), Mockito.any())).thenReturn(new ArrayList<>());

		String csv = mockMvc.perform(get("/api/v1/elenchi/2022/stampa").param("tipo", TipoElenco.ACCISE.name()))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		assertNotNull(csv);
	}

	@Test
	@Sql(scripts = "/sql/elenchi/domande_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/elenchi/domande_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = {Ruoli.DOMANDE_UMA_RICERCA_ENTE_COD})
	void scaricaElencoAccise_ente() throws Exception {

		mockMvc.perform(get("/api/v1/elenchi/2022/stampa").param("tipo", TipoElenco.ACCISE.name()))
		.andExpect(status().is4xxClientError());

	}

	@Test
	@Sql(scripts = "/sql/elenchi/domande_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/elenchi/domande_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = {Ruoli.DOMANDE_UMA_RICERCA_ENTE_COD})
	void scaricaElencoLavorazioniClienti_ente() throws Exception {

		// mock territorio 
		ParticellaDto particella1 = new ParticellaTestBuilder()
				.withInfoCatastali("012", "9999", ".1522", null)
				.addColtura(new ColturaTestBuilder().descrizione("2 - colturaMaisSorgo").withCodifica("008", "001", "042", "000", "000").withSuperficie(5253).build())
				.addColtura(new ColturaTestBuilder().descrizione("6 - colturaPascolo")  .withCodifica("000", "382", "000", "009", "000").withSuperficie(1422).build())
				.build();

		Mockito.when(territorioClient.getColture(Mockito.any(), Mockito.any())).thenReturn(Arrays.asList(particella1));
		Mockito.when(clock.today()).thenReturn(LocalDate.of(2020,Month.NOVEMBER, 1));

		mockMvc.perform(get("/api/v1/elenchi/2022/stampa").param("tipo", TipoElenco.LAVORAZIONI_CLIENTI_CONTO_TERZI.name()))
		.andExpect(status().is4xxClientError());

	}

	@Test
	@Sql(scripts = "/sql/elenchi/domande_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/elenchi/domande_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = {Ruoli.DOMANDE_UMA_RICERCA_TUTTI_COD})
	void scaricaElencoLavorazioniClienti_istruttore() throws Exception {

		// mock territorio 
		ParticellaDto particella1 = new ParticellaTestBuilder()
				.withInfoCatastali("012", "9999", ".1522", null)
				.addColtura(new ColturaTestBuilder().descrizione("2 - colturaMaisSorgo").withCodifica("008", "001", "042", "000", "000").withSuperficie(5253).build())
				.addColtura(new ColturaTestBuilder().descrizione("6 - colturaPascolo")  .withCodifica("000", "382", "000", "009", "000").withSuperficie(1422).build())
				.build();

		Mockito.when(territorioClient.getColture(Mockito.any(), Mockito.any())).thenReturn(Arrays.asList(particella1));
		Mockito.when(clock.today()).thenReturn(LocalDate.of(2020,Month.NOVEMBER, 1));

		String csv = mockMvc.perform(get("/api/v1/elenchi/2022/stampa").param("tipo", TipoElenco.LAVORAZIONI_CLIENTI_CONTO_TERZI.name()))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		assertNotNull(csv);
	}
}
