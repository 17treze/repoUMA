package it.tndigitale.a4g.richiestamodificasuolo.lavorazioneSuolo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.h2.tools.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.richiestamodificasuolo.Ruoli;
import it.tndigitale.a4g.richiestamodificasuolo.api.ApiUrls;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.ModalitaADL;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoLavorazioneSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.GrigliaSuoloDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.LavorazioneSuoloDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.StatoColtDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.SuoloDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.SuoloDichiaratoDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.UsoSuoloDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.WorkspaceLavorazioneDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.Utils.UtilsFme;

@CrossOrigin(origins = "*")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class LavorazioneSuoloCampagnaApplicationTest {
	@MockBean
	private Clock clock;

	@BeforeEach
	void initNowDate() {
		when(clock.now()).thenReturn(LocalDateTime.of(LocalDate.of(2021, 04, 16), LocalTime.of(10, 0)));
	}

	static Server h2WebServer;

	@BeforeAll
	public static void initTest() throws SQLException {

		h2WebServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082");

		h2WebServer.start();
	}

	@AfterAll
	public static void stopServer() throws SQLException {
		h2WebServer.stop();
	}

	@Value("${it.tndigit.srid.etrs89}")
	private int sridEtrs89;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private SuoloDao suoloDao;
	@Autowired
	private SuoloDichiaratoDao suoloDichiaratoDao;

	@Autowired
	private LavorazioneSuoloDao lavorazioneSuoloDao;

	@Autowired
	private WorkspaceLavorazioneDao workspaceLavorazioneDao;

	@Autowired
	private GrigliaSuoloDao grigliaSuoloDao;

	@Autowired
	private StatoColtDao statoColtDao;

	@Autowired
	private UsoSuoloDao usoSuoloDao;

	@SpyBean
	@Autowired
	private UtilsFme utilsFme;

	@Value("${it.tndigit.serverFme.calcolarePoligoniSuoloNonCopertiDaWorkspaceECreareNuoviPoligoniDiWorkspaceFme}")
	private String calcolarePoligoniSuoloNonCopertiDaWorkspaceECreareNuoviPoligoniDiWorkspaceFme;

	@Value("${it.tndigit.serverFme.creaAreaDiLavoro}")
	private String creaAreaDiLavoro;

	@Value("${it.tndigit.serverFme.verificaIntersezioneLavorazioneUpas.creazioneBuchiLavorazione}")
	private String creaBuchi;

	@Value("${it.tndigit.serverFme.verificaIntersezioneLavorazioneUpas}")
	private String verificaIntersezioneUpas;

	@Value("${it.tndigit.serverFme.ritagliaWorkspaceSuAreaDiLavoro}")
	private String ritagliaWorkspaceSuAreaDiLavoro;

	private String urlCambioCampagna = "/cambioCampagna";

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/cambioCampagnaLavorazione_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/testCase/lavorazioneSuolo/cambioCampagnaLavorazione_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void cambioCampagna2022_2023_OK() throws Exception {

		Long idLavorazione = 99299990L;

		LavorazioneSuoloModel lavorazione = lavorazioneSuoloDao.getOne(idLavorazione);
		assertEquals(2022, lavorazione.getCampagna());

		String requestPayload = "{ \"annoCampagna\": 2023 }";

		mockMvc.perform(put(ApiUrls.LAVORAZIONE_SUOLO + "/" + idLavorazione + urlCambioCampagna).content(requestPayload).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/cambioCampagnaLavorazione_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/testCase/lavorazioneSuolo/cambioCampagnaLavorazione_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void cambioCampagna2023_2022_OK() throws Exception {
		Long idLavorazione = 99239990L;

		LavorazioneSuoloModel lavorazione = lavorazioneSuoloDao.getOne(idLavorazione);
		assertEquals(2023, lavorazione.getCampagna());

		String requestPayload = "{ \"annoCampagna\": 2022 }";

		mockMvc.perform(put(ApiUrls.LAVORAZIONE_SUOLO + "/" + idLavorazione + urlCambioCampagna).content(requestPayload).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/cambioCampagnaLavorazione_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/testCase/lavorazioneSuolo/cambioCampagnaLavorazione_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void cambioCampagna_2022_2022_KO() throws Exception {
		Long idLavorazione = 99299990L;

		LavorazioneSuoloModel lavorazione = lavorazioneSuoloDao.getOne(idLavorazione);
		assertEquals(2022, lavorazione.getCampagna());

		String requestPayload = "{ \"annoCampagna\": 2022}";

		mockMvc.perform(put(ApiUrls.LAVORAZIONE_SUOLO + "/" + idLavorazione + urlCambioCampagna).content(requestPayload).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotAcceptable());
	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_RICHIESTA_MODIFICA_SUOLO_ENTE_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/cambioCampagnaLavorazione_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/testCase/lavorazioneSuolo/cambioCampagnaLavorazione_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void cambioCampagna_NonAutorizzato() throws Exception {
		Long idLavorazione = 99299990L;

		String requestPayload = "{ \"annoCampagna\": 2023}";

		mockMvc.perform(put(ApiUrls.LAVORAZIONE_SUOLO + "/" + idLavorazione + urlCambioCampagna).content(requestPayload).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "BACKOFFICE1", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/cambioCampagnaLavorazione_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/testCase/lavorazioneSuolo/cambioCampagnaLavorazione_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void cambioCampagna_UtenteNonAutorizzato() throws Exception {
		Long idLavorazione = 99299990L;

		LavorazioneSuoloModel lavorazione = lavorazioneSuoloDao.getOne(idLavorazione);
		assertNotEquals("BACKOFFICE1", lavorazione.getUtente());

		String requestPayload = "{ \"annoCampagna\": 2023}";

		mockMvc.perform(put(ApiUrls.LAVORAZIONE_SUOLO + "/" + idLavorazione + urlCambioCampagna).content(requestPayload).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/cambioCampagnaLavorazione_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/testCase/lavorazioneSuolo/cambioCampagnaLavorazione_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void cambioCampagna_LavorazioneInesistente() throws Exception {
		Long idLavorazione = 9923999999990L;

		String requestPayload = "{ \"annoCampagna\": 2022 }";

		mockMvc.perform(put(ApiUrls.LAVORAZIONE_SUOLO + "/" + idLavorazione + urlCambioCampagna).content(requestPayload).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/cambioCampagnaLavorazione_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/testCase/lavorazioneSuolo/cambioCampagnaLavorazione_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void cambioCampagna_LavorazioneInCreazione() throws Exception {
		Long idLavorazione = 99299990L;

		LavorazioneSuoloModel lavorazione = lavorazioneSuoloDao.getOne(idLavorazione);
		assertEquals(StatoLavorazioneSuolo.IN_CREAZIONE, lavorazione.getStato());

		String requestPayload = "{ \"annoCampagna\": 2023 }";

		mockMvc.perform(put(ApiUrls.LAVORAZIONE_SUOLO + "/" + idLavorazione + urlCambioCampagna).content(requestPayload).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/cambioCampagnaLavorazione_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/testCase/lavorazioneSuolo/cambioCampagnaLavorazione_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void cambioCampagna_LavorazioneInModifica() throws Exception {
		Long idLavorazione = 99239990L;

		LavorazioneSuoloModel lavorazione = lavorazioneSuoloDao.getOne(idLavorazione);
		assertEquals(StatoLavorazioneSuolo.IN_MODIFICA, lavorazione.getStato());

		String requestPayload = "{ \"annoCampagna\": 2022 }";

		mockMvc.perform(put(ApiUrls.LAVORAZIONE_SUOLO + "/" + idLavorazione + urlCambioCampagna).content(requestPayload).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/cambioCampagnaLavorazione_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/testCase/lavorazioneSuolo/cambioCampagnaLavorazione_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void cambioCampagna_LavorazioneInCorso() throws Exception {
		Long idLavorazione = 99249990L;

		LavorazioneSuoloModel lavorazione = lavorazioneSuoloDao.getOne(idLavorazione);
		assertEquals(StatoLavorazioneSuolo.IN_CORSO, lavorazione.getStato());

		String requestPayload = "{ \"annoCampagna\": 2022 }";

		mockMvc.perform(put(ApiUrls.LAVORAZIONE_SUOLO + "/" + idLavorazione + urlCambioCampagna).content(requestPayload).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotAcceptable());
	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/cambioCampagnaLavorazione_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/testCase/lavorazioneSuolo/cambioCampagnaLavorazione_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void cambioCampagna_ADL() throws Exception {
		Long idLavorazione = 99299990L;

		String requestPayload = "{ \"annoCampagna\": 2023 }";

		LavorazioneSuoloModel lavorazione = lavorazioneSuoloDao.getOne(idLavorazione);

		Integer lengthListAdl = lavorazione.getListaAreadiLavoro().size();
		Integer lengthListPoligoniDichiarato = lavorazione.getSuoloDichiaratoModel().size();
		Integer lengthListPoligoniSuolo = lavorazione.getListaSuoloInCorsoModel().size();
		Integer lengthListWorkspace = lavorazione.getListaLavorazioneWorkspaceModel().size();
		Integer lengthListAnomalie = lavorazione.getListaAnomalieValidazione().size();
		Integer lengthListTmpWorkspace = lavorazione.getListaLavorazioneWorkspaceTmpModel().size();

		// status dichiarati

		MvcResult result = mockMvc.perform(put(ApiUrls.LAVORAZIONE_SUOLO + "/" + idLavorazione + urlCambioCampagna).content(requestPayload).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		String content = result.getResponse().getContentAsString();

		Long idLav = objectMapper.readValue(content, new TypeReference<Long>() {
		});

		LavorazioneSuoloModel lavorazioneNew = lavorazioneSuoloDao.getOne(idLav);

		Integer lengthListAdl1 = lavorazioneNew.getListaAreadiLavoro().size();
		Integer lengthListPoligoniDichiarato1 = lavorazioneNew.getSuoloDichiaratoModel().size();
		Integer lengthListPoligoniSuolo1 = lavorazioneNew.getListaSuoloInCorsoModel().size();
		Integer lengthListWorkspace1 = lavorazioneNew.getListaLavorazioneWorkspaceModel().size();
		Integer lengthListAnomalie1 = lavorazioneNew.getListaAnomalieValidazione().size();
		Integer lengthListTmpWorkspace1 = lavorazioneNew.getListaLavorazioneWorkspaceTmpModel().size();

		assertEquals(ModalitaADL.DISEGNO_ADL, lavorazioneNew.getModalitaADL());

		assertEquals(lengthListAdl1, lengthListAdl);
		// assertEquals(lengthListPoligoniDichiarato1, lengthListPoligoniDichiarato);
		// assertEquals(lengthListPoligoniDichiarato1, 0);
		// assertEquals(lengthListPoligoniSuolo1, 0);
		assertEquals(lengthListWorkspace1, lengthListWorkspace);
		// assertNotEquals(lengthListWorkspace1, 0);
		// assertEquals(lengthListAnomalie1, 0);
		// assertEquals(lengthListTmpWorkspace1, 0);
	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/cambioCampagnaLavorazione_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/testCase/lavorazioneSuolo/cambioCampagnaLavorazione_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void cambioCampagna_SuoliInteri() throws Exception {
		Long idLavorazione = 99229990L;

		String requestPayload = "{ \"annoCampagna\": 2023 }";

		mockMvc.perform(put(ApiUrls.LAVORAZIONE_SUOLO + "/" + idLavorazione + urlCambioCampagna).content(requestPayload).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

}
