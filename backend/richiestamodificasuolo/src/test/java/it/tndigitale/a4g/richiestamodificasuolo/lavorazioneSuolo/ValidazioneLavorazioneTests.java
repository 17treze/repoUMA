package it.tndigitale.a4g.richiestamodificasuolo.lavorazioneSuolo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.h2.tools.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.richiestamodificasuolo.Ruoli;
import it.tndigitale.a4g.richiestamodificasuolo.api.ApiUrls;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.AnomaliaValidazioneModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.AnomaliaValidazioneRelModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.TipoAnomaliaValidazione;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.WorkspaceLavSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.AnomaliaValidazioneDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.AnomaliaWorkspaceRelDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.LavorazioneSuoloDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.StatoColtDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.UsoSuoloDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.WorkspaceLavorazioneDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.Utils.UtilsFme;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.azione.ValidLavorazioneInCorsoBusiness;
import it.tndigitale.a4g.richiestamodificasuolo.dto.GisUtils;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.AnomaliaPoligoniWithRootWorkspaceDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.ValidazioneLavorazioneInCorsoDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.mapper.AnomaliaValidazioneMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ValidazioneLavorazioneTests {

	static Server h2WebServer;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${it.tndigit.serverFme.workspaceValidazioneLavorazioneInCorso}")
	private String workspaceValidazioneSovrapposizioni;

	@SpyBean
	@Autowired
	private ValidLavorazioneInCorsoBusiness validaLavorazioneInCorso;

	@Mock
	private AnomaliaValidazioneDao anomaliaValidazioneDao;

	@Autowired
	private LavorazioneSuoloDao lavorazioneSuoloDao;

	@Autowired
	private UsoSuoloDao usoSuoloDao;

	@Autowired
	private StatoColtDao statoColtDao;

	@Autowired
	private WorkspaceLavorazioneDao workspaceLavorazioneDao;

	@Autowired
	private AnomaliaWorkspaceRelDao anomaliaWorkspaceRelDao;

	@MockBean
	private UtilsFme utilsFme;

	@Autowired
	private AnomaliaValidazioneMapper anomaliaValidazioneMapper;

	@BeforeAll
	public static void initTest() throws SQLException {

		h2WebServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082");

		h2WebServer.start();
	}

	@AfterAll
	public static void stopServer() throws SQLException {
		h2WebServer.stop();
	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/validazioneLavorazione.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void validazioneEsecuzioneOk() throws Exception {
		Long idLavorazione = 123L;
		mockFmeServer(idLavorazione, HttpStatus.OK, workspaceValidazioneSovrapposizioni);
		String URL = ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/validaLavorazioneInCorso"));

		mockSkipVerificheOra(idLavorazione);

		MvcResult result = mockMvc.perform(get(URL)).andExpect(status().isOk()).andReturn();

		String content = result.getResponse().getContentAsString();

		ValidazioneLavorazioneInCorsoDto validazioneObj = objectMapper.readValue(content, new TypeReference<ValidazioneLavorazioneInCorsoDto>() {
		});

		assertEquals(idLavorazione, validazioneObj.getIdLavorazione());
	}

	@Test
	@WithMockUser(username = "OPERATORECAA", roles = { Ruoli.VISUALIZZA_LAVORAZIONE_SUOLO_TUTTI_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/validazioneLavorazione.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void validazioneEsecuzioneKoUtenteNonAbilitato() throws Exception {
		Long idLavorazione = 123L;

		String URL = ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/validaLavorazioneInCorso"));
		mockSkipVerificheOra(idLavorazione);
		mockMvc.perform(get(URL)).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/validazioneLavorazione.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void validazioneEsecuzioneKoLavorazioneNonEsite() throws Exception {
		Long idLavorazione = 12311111111L;

		String URL = ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/validaLavorazioneInCorso"));
		mockSkipVerificheOra(idLavorazione);
		mockMvc.perform(get(URL)).andExpect(status().isNotAcceptable());
	}

	@Test
	@WithMockUser(username = "BACKOFFICE1", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/validazioneLavorazione.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void validazioneEsecuzioneKoUtenteNonProprietario() throws Exception {
		Long idLavorazione = 123L;

		String URL = ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/validaLavorazioneInCorso"));
		mockSkipVerificheOra(idLavorazione);
		mockMvc.perform(get(URL)).andExpect(status().isNotAcceptable());
	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/validazioneLavorazione.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void validazioneEsecuzioneKoLavorazioneNonInCorso() throws Exception {
		Long idLavorazione = 124L;

		String URL = ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/validaLavorazioneInCorso"));
		mockSkipVerificheOra(idLavorazione);
		mockMvc.perform(get(URL)).andExpect(status().isNotAcceptable());
	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/validazioneLavorazione.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void validazionePoligoniSenzaEsitoOk() throws Exception {
		Long idLavorazione = 123L;
		mockFmeServer(idLavorazione, HttpStatus.OK, workspaceValidazioneSovrapposizioni);
		String URL = ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/validaLavorazioneInCorso"));
		mockSkipVerificheOra(idLavorazione);
		MvcResult result = mockMvc.perform(get(URL)).andExpect(status().isOk()).andReturn();

		String content = result.getResponse().getContentAsString();

		ValidazioneLavorazioneInCorsoDto validazioneObj = objectMapper.readValue(content, new TypeReference<ValidazioneLavorazioneInCorsoDto>() {
		});

		assertEquals(idLavorazione, validazioneObj.getIdLavorazione());
		assertEquals(3, validazioneObj.getPoligoniDichiaratoSenzaEsito().size());
		List<Long> ids = new ArrayList<Long>(Arrays.asList(311L, 331L, 341L));

		assertEquals(true, ids.contains(validazioneObj.getPoligoniDichiaratoSenzaEsito().get(0).getId()));
		assertEquals(true, ids.contains(validazioneObj.getPoligoniDichiaratoSenzaEsito().get(1).getId()));
		assertEquals(true, ids.contains(validazioneObj.getPoligoniDichiaratoSenzaEsito().get(2).getId()));
	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/validazioneLavorazione.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void validazionePoligoniRichiestaCancellataOk() throws Exception {
		Long idLavorazione = 123L;
		mockFmeServer(idLavorazione, HttpStatus.OK, workspaceValidazioneSovrapposizioni);
		String URL = ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/validaLavorazioneInCorso"));
		mockSkipVerificheOra(idLavorazione);
		MvcResult result = mockMvc.perform(get(URL)).andExpect(status().isOk()).andReturn();

		String content = result.getResponse().getContentAsString();

		ValidazioneLavorazioneInCorsoDto validazioneObj = objectMapper.readValue(content, new TypeReference<ValidazioneLavorazioneInCorsoDto>() {
		});

		assertEquals(idLavorazione, validazioneObj.getIdLavorazione());
		assertEquals(1, validazioneObj.getPoligoniDichiaratoRichiestaCancellata().size());
		List<Long> ids = new ArrayList<Long>(Arrays.asList(351L));
		assertEquals(true, ids.contains(validazioneObj.getPoligoniDichiaratoRichiestaCancellata().get(0).getId()));
	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/validazioneLavorazione.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void validazionePoligoniSuolo999Ok() throws Exception {
		Long idLavorazione = 123L;
		mockFmeServer(idLavorazione, HttpStatus.ACCEPTED, workspaceValidazioneSovrapposizioni);
		String URL = ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/validaLavorazioneInCorso"));
		mockSkipVerificheOra(idLavorazione);
		MvcResult result = mockMvc.perform(get(URL)).andExpect(status().isOk()).andReturn();

		String content = result.getResponse().getContentAsString();

		ValidazioneLavorazioneInCorsoDto validazioneObj = objectMapper.readValue(content, new TypeReference<ValidazioneLavorazioneInCorsoDto>() {
		});

		assertEquals(idLavorazione, validazioneObj.getIdLavorazione());
		assertEquals(1, validazioneObj.getPoligoniSuoloAttributiMancanti().size());
		List<Long> ids = new ArrayList<Long>(Arrays.asList(361L));
		assertEquals(true, ids.contains(validazioneObj.getPoligoniSuoloAttributiMancanti().get(0).getId()));
	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/validazioneLavorazione.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void validazioneNoErroriOk() throws Exception {
		Long idLavorazione = 125L;

		mockFmeServer(idLavorazione, HttpStatus.OK, workspaceValidazioneSovrapposizioni);

		String URL = ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/validaLavorazioneInCorso"));
		mockSkipVerificheOra(idLavorazione);
		MvcResult result = mockMvc.perform(get(URL)).andExpect(status().isOk()).andReturn();

		String content = result.getResponse().getContentAsString();

		ValidazioneLavorazioneInCorsoDto validazioneObj = objectMapper.readValue(content, new TypeReference<ValidazioneLavorazioneInCorsoDto>() {
		});

		assertEquals(idLavorazione, validazioneObj.getIdLavorazione());
		assertEquals(0, validazioneObj.getPoligoniDichiaratoSenzaEsito().size());
		assertEquals(0, validazioneObj.getPoligoniDichiaratoRichiestaCancellata().size());
		assertEquals(0, validazioneObj.getPoligoniSuoloAttributiMancanti().size());
		assertEquals(0, validazioneObj.getPoligoniAnomaliaSovrapposizioni().size());
	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/validazioneLavorazione.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void validazioneSovrappozioneErroreServerFme() throws Exception {
		Long idLavorazione = 125L;

		mockFmeServer(idLavorazione, HttpStatus.INTERNAL_SERVER_ERROR, workspaceValidazioneSovrapposizioni);

		String URL = ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/validaLavorazioneInCorso"));
		mockSkipVerificheOra(idLavorazione);
		mockMvc.perform(get(URL)).andExpect(status().isNotAcceptable());
	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/validazioneLavorazione.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void validazioneOK() throws Exception {
		Long idLavorazione = 125L;

		mockFmeServer(idLavorazione, HttpStatus.OK, workspaceValidazioneSovrapposizioni);

		String URL = ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/validaLavorazioneInCorso"));
		mockSkipVerificheOra(idLavorazione);
		MvcResult result = mockMvc.perform(get(URL)).andExpect(status().isOk()).andReturn();

		String content = result.getResponse().getContentAsString();

		ValidazioneLavorazioneInCorsoDto validazioneObj = objectMapper.readValue(content, new TypeReference<ValidazioneLavorazioneInCorsoDto>() {
		});

		assertEquals(idLavorazione, validazioneObj.getIdLavorazione());
		assertEquals(0, validazioneObj.getPoligoniDichiaratoSenzaEsito().size());
		assertEquals(0, validazioneObj.getPoligoniDichiaratoRichiestaCancellata().size());
		assertEquals(0, validazioneObj.getPoligoniSuoloAttributiMancanti().size());
		assertEquals(0, validazioneObj.getPoligoniAnomaliaSovrapposizioni().size());
	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/validazioneSovrapposizioni.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void validazionePoligoniInSovrappozione() throws Exception {
		Long idLavorazione = 14072021L;
		Long idWorkspaceSovrapposizione = 1466243L;
		Long idWorkspaceSovrapposizioneSecond = 1466246L;

		mockFmeServerWithInsertAnomalia(idLavorazione, idWorkspaceSovrapposizione, idWorkspaceSovrapposizioneSecond, null, HttpStatus.OK, workspaceValidazioneSovrapposizioni);

		String URL = ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/validaLavorazioneInCorso"));
		mockSkipVerificheOra(idLavorazione);
		MvcResult result = mockMvc.perform(get(URL)).andExpect(status().isOk()).andReturn();

		String content = result.getResponse().getContentAsString();

		ValidazioneLavorazioneInCorsoDto validazioneObj = objectMapper.readValue(content, new TypeReference<ValidazioneLavorazioneInCorsoDto>() {
		});

		assertEquals(idLavorazione, validazioneObj.getIdLavorazione());
		assertEquals(0, validazioneObj.getPoligoniDichiaratoSenzaEsito().size());
		assertEquals(0, validazioneObj.getPoligoniDichiaratoRichiestaCancellata().size());
		assertEquals(3, validazioneObj.getPoligoniSuoloAttributiMancanti().size());
		assertEquals(1, validazioneObj.getPoligoniAnomaliaSovrapposizioni().size());
		assertEquals(0, validazioneObj.getPoligoniAnomaliaDebordanoAreaDiLavoro().size());

	}

	/*
	 * @Test
	 * 
	 * @WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	 * 
	 * @Sql(scripts = "/testCase/lavorazioneSuolo/validazioneSovrapposizioni.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD) void validazionePoligoniDeborda() throws Exception { Long
	 * idLavorazione = 14072021L; Long idWorkspaceDeborda = 1475632L;
	 * 
	 * mockFmeServerWithInsertAnomalia(idLavorazione, null, null, idWorkspaceDeborda, HttpStatus.OK);
	 * 
	 * String URL = ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/validaLavorazioneInCorso")); MvcResult result =
	 * mockMvc.perform(get(URL)).andExpect(status().isOk()).andReturn();
	 * 
	 * String content = result.getResponse().getContentAsString();
	 * 
	 * ValidazioneLavorazioneInCorsoDto validazioneObj = objectMapper.readValue(content, new TypeReference<ValidazioneLavorazioneInCorsoDto>() { });
	 * 
	 * assertEquals(idLavorazione, validazioneObj.getIdLavorazione()); assertEquals(1, validazioneObj.getPoligoniAnomaliaDebordanoAreaDiLavoro().size()); }
	 */
	private void mockFmeServer(Long idLavorazione, HttpStatus status, String nomeProcedure) throws URISyntaxException {
		ResponseEntity<String> mockResponse = new ResponseEntity<String>(status);

		Mockito.when(utilsFme.callProcedureFme(idLavorazione, nomeProcedure)).thenReturn(mockResponse);
	}

	private void mockFmeServerWithInsertAnomalia(Long idLavorazione, Long idWorkspaceSovrapposizione, Long idWorkspaceSovrapposizioneSecond, Long idWorkspaceDeborda, HttpStatus status,
			String nomeProcedure) throws URISyntaxException {

		Mockito.when(utilsFme.callProcedureFme(idLavorazione, nomeProcedure))
				.then(mockInsertAnomalie(idLavorazione, idWorkspaceSovrapposizione, idWorkspaceSovrapposizioneSecond, idWorkspaceDeborda, status));
	}

	@Transactional
	private Answer<?> mockInsertAnomalie(Long idLavorazione, Long idWorkspaceSovrapposizione, Long idWorkspaceSovrapposizioneSecond, Long idWorkspaceDeborda, HttpStatus status) {
		return new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {

				ResponseEntity<String> mockResponse = new ResponseEntity<String>(status);
				if (idWorkspaceSovrapposizione != null && idWorkspaceSovrapposizioneSecond != null) {
					insertAnomaliaSovrapposizione(idLavorazione, idWorkspaceSovrapposizione, idWorkspaceSovrapposizioneSecond);
				}
				if (idWorkspaceDeborda != null) {
					insertAnomaliaDeborda(idLavorazione, idWorkspaceDeborda);
				}
				return mockResponse;
			}
		};
	}

	private void insertAnomaliaSovrapposizione(Long idLavorazione, Long idWorkspace, Long idWorkspaceSecond) {
		LavorazioneSuoloModel lavorazione = lavorazioneSuoloDao.findById(idLavorazione).get();
		WorkspaceLavSuoloModel workspace = workspaceLavorazioneDao.findById(idWorkspace).get();

		// New Anomalia di sovrapposizione
		AnomaliaValidazioneModel anomaliaSovrapposizione = new AnomaliaValidazioneModel();

		anomaliaSovrapposizione.setLavorazioneSuoloInAnomaliaValidazione(lavorazione);
		anomaliaSovrapposizione.setTipoAnomalia(TipoAnomaliaValidazione.SOVRAPPOSIZIONE_POLIGONI_WORKSPACE);
		anomaliaSovrapposizione.setDettaglioAnomalia("DETTAGLIO");
		anomaliaSovrapposizione.setArea(1.401104);
		anomaliaSovrapposizione.setShape(GisUtils.getGeometry(
				"POLYGON ((682206.165009174 5098628.54816248, 682205.7031 5098628.4624, 			682206.1015 	5098626.37, 	682206.933124326 	5098626.52542153, 682206.165009174 5098628.54816248))"));

		anomaliaValidazioneDao.saveAndFlush(anomaliaSovrapposizione);

		// Aggiorno relazione tra workspace e anomalia
		AnomaliaValidazioneRelModel relAnomaliaWorkspace = new AnomaliaValidazioneRelModel();
		relAnomaliaWorkspace.setAnomaliaValidazioneModel(anomaliaSovrapposizione);
		relAnomaliaWorkspace.setWorkspaceLavSuoloModel(workspace);
		anomaliaWorkspaceRelDao.saveAndFlush(relAnomaliaWorkspace);

		AnomaliaValidazioneRelModel relAnomaliaWorkspace2 = new AnomaliaValidazioneRelModel();
		relAnomaliaWorkspace2.setAnomaliaValidazioneModel(anomaliaSovrapposizione);
		relAnomaliaWorkspace2.setWorkspaceLavSuoloModel(workspaceLavorazioneDao.findById(idWorkspaceSecond).get());
		anomaliaWorkspaceRelDao.saveAndFlush(relAnomaliaWorkspace2);
	}

	private void insertAnomaliaDeborda(Long idLavorazione, Long idWorkspace) {

		LavorazioneSuoloModel lavorazione = lavorazioneSuoloDao.findById(idLavorazione).get();
		WorkspaceLavSuoloModel workspace = workspaceLavorazioneDao.findById(idWorkspace).get();

		// ANOMALIA POLIGONI CHE DEBORDA DA AREA DI LAVORO
		AnomaliaValidazioneModel anomaliaDeborda = new AnomaliaValidazioneModel();

		anomaliaDeborda.setLavorazioneSuoloInAnomaliaValidazione(lavorazione);
		anomaliaDeborda.setTipoAnomalia(TipoAnomaliaValidazione.POLIGONI_DEBORDANO_AREA_DI_LAVORO);
		anomaliaDeborda.setDettaglioAnomalia("POLIGONI_DEBORDANO_AREA_DI_LAVORO");
		anomaliaDeborda.setArea(25.392451);
		anomaliaDeborda.setShape(GisUtils.getGeometry(
				"POLYGON((643625.8673 5086835.7508, 643639.6602 5086828.4677, 643635.1994 5086834.9456, 643633.9843707281 5086834.323091452, 643633.3804 5086833.4676, 643626.6735 5086836.6396, 643625.8673 5086835.7508))"));

		anomaliaValidazioneDao.saveAndFlush(anomaliaDeborda);

		// Aggiorno relazione tra workspace e anomalia
		AnomaliaValidazioneRelModel relAnomaliaWorkspace = new AnomaliaValidazioneRelModel();
		relAnomaliaWorkspace.setAnomaliaValidazioneModel(anomaliaDeborda);
		relAnomaliaWorkspace.setWorkspaceLavSuoloModel(workspace);
		anomaliaWorkspaceRelDao.saveAndFlush(relAnomaliaWorkspace);
	}

	private String generateBody(String easyConnect, Long idLavorazione) {
		return String.format("{\n" + "    \"publishedParameters\" : [\n" + "        {\n" + "            \"name\" : \"Connesione_DB\",\n" + "            \"value\" : \"%s\"\n" + "        },\n"
				+ "        {\n" + "            \"name\" : \"idLavorazione\",\n" + "            \"value\" :  \"%s\"\n" + "        }\n" + "    ]\n" + "}", easyConnect, idLavorazione);
	}

	@Test

	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })

	@Sql(scripts = "/testCase/lavorazioneSuolo/validazioniOracle.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void validazioniOracleOk() throws Exception {
		Long idLavorazione = 20820212L;

		Long idWorkspace1 = 20820212L;
		Long idWorkspace2 = 20820213L;
		Long idWorkspace3 = 20820214L;

		mockValidaOracleValidateGeometry(idWorkspace1, "TRUE");
		mockValidaOracleValidateGeometry(idWorkspace2, "13349");
		mockValidaOracleValidateGeometry(idWorkspace3, "13349");

		mockValidaOracleValidateFixedGeometry(idWorkspace1, "TRUE");
		mockValidaOracleValidateFixedGeometry(idWorkspace2, "TRUE");
		mockValidaOracleValidateFixedGeometry(idWorkspace3, "TRUE");

		mockValidaOracleGetAreaFixed(idWorkspace1, 123D);
		mockValidaOracleGetAreaFixed(idWorkspace2, 456D);
		mockValidaOracleGetAreaFixed(idWorkspace3, 789D);

		mockValidaOracleGetNumElemFixed(idWorkspace1, 1L);
		mockValidaOracleGetNumElemFixed(idWorkspace2, 1L);
		mockValidaOracleGetNumElemFixed(idWorkspace3, 1L);

		mockValidaOracleFixWorkspace(idWorkspace1, true);
		mockValidaOracleFixWorkspace(idWorkspace2, true);
		mockValidaOracleFixWorkspace(idWorkspace3, true);

		mockValidaOracleInsertFixedWorkspaceAnomalies(idWorkspace1, "", 1, true);
		mockValidaOracleInsertFixedWorkspaceAnomalies(idWorkspace2, "", 1, true);
		mockValidaOracleInsertFixedWorkspaceAnomalies(idWorkspace3, "", 1, true);

		mockValidaOracleIsertNotFixedWorkspaceErrors(idWorkspace1, "", 1, true);
		mockValidaOracleIsertNotFixedWorkspaceErrors(idWorkspace2, "", 1, true);
		mockValidaOracleIsertNotFixedWorkspaceErrors(idWorkspace3, "", 1, true);

		String URL = ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/validaLavorazioneInCorso"));

		/*
		 * MvcResult result = mockMvc.perform(get(URL)).andExpect(status().isOk()).andReturn(); String content = result.getResponse().getContentAsString();
		 * 
		 * ValidazioneLavorazioneInCorsoDto validazioneObj = objectMapper.readValue(content, new TypeReference<ValidazioneLavorazioneInCorsoDto>() { });
		 * 
		 * assertEquals(idLavorazione, validazioneObj.getIdLavorazione()); // assertEquals(2, validazioneObj.getPoligoniAnomalieOracle().size()); }
		 */
	}

	private void mockValidaOracleValidateGeometry(Long idWorkspace, String errore) {
		// Mockito.when(workspaceLavorazioneDao.validateGeometry(idWorkspace, validaLavorazioneInCorso.tolleranza)).then(mockReturn(errore));
		doReturn(errore).when(validaLavorazioneInCorso).validateGeometry(idWorkspace);
	}

	private void mockValidaOracleValidateFixedGeometry(Long idWorkspace, String errore) {
		// Mockito.when(workspaceLavorazioneDao.validateFixedGeometry(idWorkspace, validaLavorazioneInCorso.tolleranza)).then(mockReturn(errore));
		doReturn(errore).when(validaLavorazioneInCorso).validateFixedGeometry(idWorkspace);
	}

	private void mockValidaOracleGetAreaFixed(Long idWorkspace, Double area) {
		// Mockito.when(workspaceLavorazioneDao.getAreaFixed(idWorkspace, validaLavorazioneInCorso.tolleranza)).then(mockReturn(area));
		doReturn(area).when(validaLavorazioneInCorso).getAreaFixed(idWorkspace);
	}

	private void mockValidaOracleGetNumElemFixed(Long idWorkspace, Long numElem) {
		// Mockito.when(workspaceLavorazioneDao.getNumElemFixed(idWorkspace, validaLavorazioneInCorso.tolleranza)).then(mockReturn(numElem));
		doReturn(numElem).when(validaLavorazioneInCorso).getNumElemFixed(idWorkspace);
	}

	private void mockValidaOracleFixWorkspace(Long idWorkspace, boolean res) {
		// Mockito.when(validaLavorazioneInCorso.fixWorkspace(idWorkspace)).then(mockFixWorkspace(fix));
		doReturn(res).when(validaLavorazioneInCorso).fixWorkspace(idWorkspace);
	}

	private void mockValidaOracleInsertFixedWorkspaceAnomalies(Long idWorkspace, String dettaglioAnomalia, Object fix, boolean res) {
		// Mockito.when(validaLavorazioneInCorso.insertNotFixedWorkspaceErrors(idWorkspace, dettaglioAnomalia)).then(mockNotFixWorkspace(idWorkspace, dettaglioAnomalia, fix, res));
		doReturn(mockInsertFixedWorkspaceAnomalies(idWorkspace, dettaglioAnomalia, fix, res)).when(validaLavorazioneInCorso).fixWorkspace(idWorkspace);
	}

	private void mockValidaOracleIsertNotFixedWorkspaceErrors(Long idWorkspace, String dettaglioAnomalia, Object fix, boolean res) {
		// Mockito.when(validaLavorazioneInCorso.insertNotFixedWorkspaceErrors(idWorkspace, dettaglioAnomalia)).then(mockNotFixWorkspace(idWorkspace, dettaglioAnomalia, fix, res));
		doReturn(mockInsertNotFixedWorkspaceErrors(idWorkspace, dettaglioAnomalia, fix, res)).when(validaLavorazioneInCorso).fixWorkspace(idWorkspace);
	}

	@Transactional
	private boolean mockInsertFixedWorkspaceAnomalies(Long idWorkspace, String dettaglioAnomalia, Object fix, boolean res) {
		return res;
	}

	@Transactional
	private boolean mockInsertNotFixedWorkspaceErrors(Long idWorkspace, String dettaglioAnomalia, Object fix, boolean res) {
		return res;
	}

	/*
	 * private void insertAnomaliaValiditaOracle(Long idLavorazione, Long idWorkspace) {
	 * 
	 * LavorazioneSuoloModel lavorazione = lavorazioneSuoloDao.findById(idLavorazione).get(); WorkspaceLavSuoloModel workspace = workspaceLavorazioneDao.findById(idWorkspace).get();
	 * 
	 * // ANOMALIA POLIGONI CHE DEBORDA DA AREA DI LAVORO AnomaliaValidazioneModel anomaliaDeborda = new AnomaliaValidazioneModel();
	 * 
	 * anomaliaDeborda.setLavorazioneSuoloInAnomaliaValidazione(lavorazione); anomaliaDeborda.setTipoAnomalia(TipoAnomaliaValidazione.POLIGONI_DEBORDANO_AREA_DI_LAVORO);
	 * anomaliaDeborda.setDettaglioAnomalia("POLIGONI_DEBORDANO_AREA_DI_LAVORO"); anomaliaDeborda.setArea(25.392451); anomaliaDeborda.setShape(GisUtils.getGeometry(
	 * "POLYGON((643625.8673 5086835.7508, 643639.6602 5086828.4677, 643635.1994 5086834.9456, 643633.9843707281 5086834.323091452, 643633.3804 5086833.4676, 643626.6735 5086836.6396, 643625.8673 5086835.7508))"
	 * ));
	 * 
	 * anomaliaValidazioneDao.saveAndFlush(anomaliaDeborda);
	 * 
	 * // Aggiorno relazione tra workspace e anomalia AnomaliaValidazioneRelModel relAnomaliaWorkspace = new AnomaliaValidazioneRelModel();
	 * relAnomaliaWorkspace.setAnomaliaValidazioneModel(anomaliaDeborda); relAnomaliaWorkspace.setWorkspaceLavSuoloModel(workspace); anomaliaWorkspaceRelDao.saveAndFlush(relAnomaliaWorkspace); }
	 */

	private void mockSkipVerificheOra(Long idLavorazione) throws Exception {
		LavorazioneSuoloModel lavorazione = lavorazioneSuoloDao.getOne(idLavorazione);
		List<AnomaliaPoligoniWithRootWorkspaceDto> mockResponse = new ArrayList<AnomaliaPoligoniWithRootWorkspaceDto>();

		doReturn(mockResponse).when(validaLavorazioneInCorso).checkPoligoniWorkspaceAnomalieOracle(lavorazione);
	}

	private void mockValidaOracleGetNumElemFixed(Long idWorkspace, Integer numElem) {
		Mockito.when(workspaceLavorazioneDao.getNumElemFixed(idWorkspace, validaLavorazioneInCorso.tolleranza)).then(mockReturn(numElem));
	}

	private void mockValidaOracleFixWorkspace(Long idWorkspace, Object fix) {
		Mockito.when(validaLavorazioneInCorso.fixWorkspace(idWorkspace)).then(mockFixWorkspace(fix));
	}

	private void mockValidaOracleInsertAnomaliaFixed(Long idWorkspace, String dettaglioAnomalia, Object fix) {
		Mockito.when(validaLavorazioneInCorso.insertFixedWorkspaceAnomalies(idWorkspace, dettaglioAnomalia)).then(mockAnomaliaSuggerimento(dettaglioAnomalia, fix));
	}

	private void mockValidaOracleInsertAnomaliaNotFixed(Long idWorkspace, String dettaglioAnomalia, Object fix) {
		Mockito.when(validaLavorazioneInCorso.insertNotFixedWorkspaceErrors(idWorkspace, dettaglioAnomalia)).then(mockNotFixWorkspace(dettaglioAnomalia, fix));
	}

	@Transactional
	private Answer<?> mockInsertAnomalieOracle(Long idLavorazione, Long idWorkspaceSovrapposizione, Long idWorkspaceSovrapposizioneSecond, Long idWorkspaceDeborda, HttpStatus status) {
		return new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {

				ResponseEntity<String> mockResponse = new ResponseEntity<String>(status);
				if (idWorkspaceSovrapposizione != null && idWorkspaceSovrapposizioneSecond != null) {
					insertAnomaliaSovrapposizione(idLavorazione, idWorkspaceSovrapposizione, idWorkspaceSovrapposizioneSecond);
				}
				if (idWorkspaceDeborda != null) {
					insertAnomaliaDeborda(idLavorazione, idWorkspaceDeborda);
				}
				return mockResponse;
			}
		};
	}

	@Transactional
	private Answer<?> mockReturn(Object toReturn) {
		return new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				return toReturn;
			}
		};
	}

	@Transactional
	private Answer<?> mockFixWorkspace(Object toReturn) {
		return new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				return toReturn;
			}
		};
	}

	@Transactional
	private Answer<?> mockAnomaliaSuggerimento(String dettaglio, Object toReturn) {
		return new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				return toReturn;
			}
		};
	}

	@Transactional
	private Answer<?> mockNotFixWorkspace(String dettaglio, Object toReturn) {
		return new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				return toReturn;
			}
		};
	}
}
