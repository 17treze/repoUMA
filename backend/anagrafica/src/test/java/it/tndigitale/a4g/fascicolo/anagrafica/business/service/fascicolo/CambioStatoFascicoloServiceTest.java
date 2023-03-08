package it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import it.tndigitale.a4g.fascicolo.anagrafica.api.ApiUrls;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.FascicoloDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaUtenteClient;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.scheduler.FascicoliDormientiScheduler;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.SospensioneDto;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.StatoFascicoloEnum;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.utente.client.model.RappresentaIlModelloDelProfiloDiUnUtente;
import it.tndigitale.a4g.utente.client.model.RappresentaIlModelloPerRappresentareUnUtenteDelSistema;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente", roles = {"a4gfascicolo.fascicolo.ricerca.tutti"})
class CambioStatoFascicoloServiceTest {
	
	@MockBean AnagraficaUtenteClient anagraficaUtenteClient;
	@Autowired MockMvc mockMvc;
	
	@Autowired FascicoloDao fascicoloDao;
	@Autowired FascicoloService fascicoloService;
	@Autowired PlatformTransactionManager transactionManager;
	@Autowired Clock clock;
	@Autowired MovimentazioneFascicoloService movimentazioneFascicoloService;
	@Autowired private FascicoliDormientiScheduler fascicoliDormientiScheduler;

    private TransactionTemplate transactionTemplate;

	@BeforeEach
	void initialize() {
		transactionTemplate = new TransactionTemplate(transactionManager);
	}
	
	private void userSetupResponsabileFascicoloPat(String userCodiceFiscale) {
		RappresentaIlModelloPerRappresentareUnUtenteDelSistema user = new RappresentaIlModelloPerRappresentareUnUtenteDelSistema();
		user.setIdentificativo(userCodiceFiscale);
		user.setCodiceFiscale(userCodiceFiscale);
		user.setNome(userCodiceFiscale);
		RappresentaIlModelloDelProfiloDiUnUtente profilo = new RappresentaIlModelloDelProfiloDiUnUtente();
		profilo.setIdentificativo("responsabile_fascicolo_pat");
		user.setProfili(Arrays.asList(profilo));
		Mockito.when(anagraficaUtenteClient.getUtenteConnesso()).thenReturn(user);
	}

	@Test
	@Sql(scripts = "/sql/fascicolo/fascicolo_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void fascicoliDormienti_ok() throws Exception {

		String cuaa = "XPDNDR77B03L378Y";
		fascicoliDormientiScheduler.schedulerProcess();
		Optional<FascicoloModel> resultOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0);
		assertEquals(true, resultOpt.isPresent());
		assertEquals(StatoFascicoloEnum.IN_AGGIORNAMENTO, resultOpt.get().getStato());
	}
	
	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/fascicolo_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void sospendi_e_rimuovi_sospensione_ok() throws Exception {
		String cuaa = "XPDNDR77B03L378X";

		SospensioneDto sospensioneDto = new SospensioneDto();
		sospensioneDto.setCuaa(cuaa);
		sospensioneDto.setDataInizio(clock.now());
		sospensioneDto.setMotivazioneInizio("TEST SOSPENSIONE");
		movimentazioneFascicoloService.sospendi(sospensioneDto);
		
		FascicoloModel fascicoloSaved = transactionTemplate.execute(status -> {
			Optional<FascicoloModel> resultOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0);
			assertEquals(true, resultOpt.isPresent());
			assertEquals(false, resultOpt.get().getDetenzioni().isEmpty());
			return resultOpt.get();
		});
		assertEquals(StatoFascicoloEnum.SOSPESO, fascicoloSaved.getStato());
		
		sospensioneDto = new SospensioneDto();
		sospensioneDto.setCuaa(cuaa);
		sospensioneDto.setDataFine(clock.now());
		sospensioneDto.setMotivazioneFine("TEST RIMUOVI SOSPENSIONE");
		movimentazioneFascicoloService.rimuoviSospensione(sospensioneDto);
		
		fascicoloSaved = transactionTemplate.execute(status -> {
			Optional<FascicoloModel> resultOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0);
			assertEquals(true, resultOpt.isPresent());
			assertEquals(false, resultOpt.get().getDetenzioni().isEmpty());
			return resultOpt.get();
		});
		assertEquals(StatoFascicoloEnum.IN_AGGIORNAMENTO, fascicoloSaved.getStato());
	}
	
	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/fascicolo_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void rimuovi_sospensione_ko() throws Exception {
		String cuaa = "XPDNDR77B03L378X";
		SospensioneDto sospensioneDto = new SospensioneDto();
		sospensioneDto.setCuaa(cuaa);
		sospensioneDto.setDataFine(clock.now());
		sospensioneDto.setMotivazioneFine("TEST RIMUOVI SOSPENSIONE");

		assertThrows(MovimentazioneFascicoloException.class, () -> {
			movimentazioneFascicoloService.rimuoviSospensione(sospensioneDto);
	    });
	}
	
	@Test
	@WithMockUser(username = "responsabile_fascicolo_pat", roles = {
			"a4ganagrafica.fascicolo.visualizzazione.modalitapagamento.ente",
			"a4ganagrafica.fascicolo.visualizzazione.tutti",
			"a4gfascicolo.fascicolo.ricerca.tutti"
	})
	@Sql(scripts = "/sql/fascicolo/fascicolo_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getDatiSospensione_NoContent() throws Exception {
		String cuaa = "MSTFBA79L10H612L";
		userSetupResponsabileFascicoloPat(cuaa);
		String endpoint = String.format("/%s/dati-sospensione", cuaa);

		MockHttpServletRequestBuilder requestBuilder =
				MockMvcRequestBuilders.multipart(ApiUrls.FASCICOLO.concat(endpoint))
				.param("cuaa", cuaa)
				.contentType(MediaType.APPLICATION_JSON);

		requestBuilder.with(request -> {
			request.setMethod(HttpMethod.GET.name());
			return request;
		});

		ResultActions resultActions = this.mockMvc.perform(
				requestBuilder
				);

		resultActions.andExpect(MockMvcResultMatchers.status().isNoContent());
	}
	
	@Test
	@WithMockUser(username = "responsabile_fascicolo_pat", roles = {
			"a4ganagrafica.fascicolo.visualizzazione.modalitapagamento.ente",
			"a4ganagrafica.fascicolo.visualizzazione.tutti",
			"a4gfascicolo.fascicolo.ricerca.tutti"
	})
	@Sql(scripts = "/sql/fascicolo/fascicolo_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getDatiSospensione() throws Exception {
		String cuaa = "XPDNDR77B03L378Y";
		userSetupResponsabileFascicoloPat(cuaa);
		String endpoint = String.format("/%s/dati-sospensione", cuaa);

		MockHttpServletRequestBuilder requestBuilder =
				MockMvcRequestBuilders.multipart(ApiUrls.FASCICOLO.concat(endpoint))
				.param("cuaa", cuaa)
				.contentType(MediaType.APPLICATION_JSON);

		requestBuilder.with(request -> {
			request.setMethod(HttpMethod.GET.name());
			return request;
		});

		ResultActions resultActions = this.mockMvc.perform(
				requestBuilder
				);

		resultActions.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(jsonPath("$", hasSize(2)))
		.andExpect(jsonPath("$[0].motivazioneInizio").value("motivazioneInizio1"))
		.andExpect(jsonPath("$[0].motivazioneFine").value("motivazioneFine1"))
		.andExpect(jsonPath("$[0].utente").value("utente1"))
		.andExpect(jsonPath("$[1].motivazioneInizio").value("motivazioneInizio2"))
		.andExpect(jsonPath("$[1].utente").value("utente2"));
	}

}
