package it.tndigitale.a4g.richiestamodificasuolo.lavorazioneSuolo;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.CrossOrigin;

import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.richiestamodificasuolo.Ruoli;
import it.tndigitale.a4g.richiestamodificasuolo.api.ApiUrls;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoRichiestaModificaSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.Utils.UtilsFme;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.evento.AvvioLavorazioneEvento;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.evento.CancellazioneLavorazioneEvento;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.evento.LavorazioneSuoloEventoHandler;

@CrossOrigin(origins = "*")
@SpringBootTest
@AutoConfigureMockMvc
public class LavorazioneSuoloScenariAlternativiTest {

	@MockBean
	private Clock clock;
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private LavorazioneSuoloEventoHandler lavEventHandler;

	@SpyBean
	@Autowired
	private UtilsFme utilsFme;

	@Value("${it.tndigit.serverFme.creaAreaDiLavoro}")
	private String creaAreaDiLavoro;

	@BeforeEach
	void initNowDate() {
		when(clock.now()).thenReturn(LocalDateTime.of(LocalDate.of(2021, 04, 16), LocalTime.of(10, 0)));
		Mockito.doThrow(RuntimeException.class).when(lavEventHandler).handle(ArgumentMatchers.any(CancellazioneLavorazioneEvento.class));
		Mockito.doThrow(RuntimeException.class).when(lavEventHandler).handle(ArgumentMatchers.any(AvvioLavorazioneEvento.class));
	}

	@Test
	@WithMockUser(username = "ITE2505@ext.itad.infotn.it", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD, Ruoli.VISUALIZZA_LAVORAZIONE_SUOLO_TUTTI_COD })
	void erroreInGestioneNotificaDeleteAlloraLavorazioneSuoloNonCancellata() throws Exception {

		// Precondizione: suolo OK
		Long idLavorazione = 137756L;
		mockMvc.perform(get(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione)))).andExpect(status().isOk());

		// Chiamo delete che va in errore
		mockMvc.perform(delete(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione)))).andExpect(status().isInternalServerError());

		// Verifico che la delete non è stata eseguita
		mockMvc.perform(get(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione)))).andDo(print()).andExpect(status().isOk()).andExpect(content().json(
				"{\"id\":137756,\"utente\":\"ITE2505@ext.itad.infotn.it\",\"stato\":\"IN_CREAZIONE\",\"dataInizioLavorazione\":\"2021-03-02T00:00:00\",\"dataFineLavorazione\":null,\"dataUltimaModifica\":\"2021-03-02T15:00:00\",\"note\":null,\"titolo\":null,\"sopralluogo\":null}"));

	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.VISUALIZZA_LAVORAZIONE_SUOLO_TUTTI_COD, Ruoli.EDIT_LAVORAZIONE_SUOLO_COD, Ruoli.VISUALIZZA_POLIGONO_TUTTI_COD,
			Ruoli.VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_TUTTI_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/scenariAlternativiLavorazioneSuolo.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void avvioLavorazioneImpattaRichiesteCollegate() throws Exception {
		Long idLavorazione = 12L;
		// Precondizione: suolo OK
		mockMvc.perform(get(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione)))).andDo(print()).andExpect(status().isOk()).andExpect(content().json(
				"{\"id\":12,\"utente\":\"BACKOFFICE\",\"stato\":\"IN_CREAZIONE\",\"dataInizioLavorazione\":\"2021-03-31T00:00:00\",\"dataFineLavorazione\":null,\"dataUltimaModifica\":\"2021-03-31T00:00:00\",\"note\":null,\"titolo\":null,\"sopralluogo\":null}"));

		Long idRichiesta = 11L;
		Long idRichiesta2 = 12L;

		// Precondizione: richiesta 1 lavorabile
		mockMvc.perform(get(ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(idRichiesta)))).andExpect(status().isOk()).andExpect(jsonPath("id").value(idRichiesta))
				.andExpect(jsonPath("$.stato", is(StatoRichiestaModificaSuolo.LAVORABILE.name())));

		// Precondizione: richiesta 2 IN_LAVORAZIONE
		mockMvc.perform(get(ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(idRichiesta2)))).andExpect(status().isOk()).andExpect(jsonPath("id").value(idRichiesta2))
				.andExpect(jsonPath("$.stato", is(StatoRichiestaModificaSuolo.LAVORABILE.name())));

		mockFmeCreaAreaDiLavoro(idLavorazione, HttpStatus.OK, creaAreaDiLavoro);

		// Chiamo notifica con errore
		mockMvc.perform(post(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/workspace"))).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError());

		// verifico
		mockMvc.perform(get(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione)))).andDo(print()).andExpect(status().isOk()).andExpect(content().json(
				"{\"id\":12,\"utente\":\"BACKOFFICE\",\"stato\":\"IN_CREAZIONE\",\"dataInizioLavorazione\":\"2021-03-31T00:00:00\",\"dataFineLavorazione\":null,\"dataUltimaModifica\":\"2021-03-31T00:00:00\",\"note\":null,\"titolo\":null,\"sopralluogo\":null}"));

		mockMvc.perform(get(ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(idRichiesta)))).andExpect(status().isOk()).andExpect(jsonPath("id").value(idRichiesta))
				.andExpect(jsonPath("$.stato", is(StatoRichiestaModificaSuolo.LAVORABILE.name())));

		mockMvc.perform(get(ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(idRichiesta2)))).andExpect(status().isOk()).andExpect(jsonPath("id").value(idRichiesta2))
				.andExpect(jsonPath("$.stato", is(StatoRichiestaModificaSuolo.LAVORABILE.name())));
	}

	private void mockFmeCreaAreaDiLavoro(Long idLavorazione, HttpStatus status, String nomeProcedura) throws URISyntaxException {
		ResponseEntity<String> mockResponse = new ResponseEntity<String>(status);
		doReturn(mockResponse).when(utilsFme).callProcedureFme(idLavorazione, nomeProcedura);

	}
}
