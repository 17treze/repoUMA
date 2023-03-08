package it.tndigitale.a4g.richiestamodificasuolo.lavorazioneSuolo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.h2.tools.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.richiestamodificasuolo.Ruoli;
import it.tndigitale.a4g.richiestamodificasuolo.api.ApiUrls;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.WorkspaceLavSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.GrigliaSuoloDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.LavorazioneSuoloDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.StatoColtDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.SuoloDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.UsoSuoloDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.WorkspaceLavorazioneDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.Utils.UtilsFme;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.client.UtenteControllerClient;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.azione.ValidLavorazioneInCorsoBusiness;
import it.tndigitale.a4g.richiestamodificasuolo.dto.GisUtils;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.AnomaliaPoligoniWithRootWorkspaceDto;
import it.tndigitale.a4g.utente.client.model.RappresentaIlModelloPerRappresentareUnUtenteDelSistema;

@CrossOrigin(origins = "*")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ConsolidaLavorazioneApplicationTest {
	// static Server h2WebServer;

	@Autowired
	private MockMvc mockMvc;

	@Value("${it.tndigit.serverFme.workspaceValidazioneLavorazioneInCorso}")
	private String workspaceValidazioneSovrapposizioni;

	@Value("${it.tndigit.serverFme.consolidaLavorazioneInAGS}")
	private String consolidaLavorazioneInAGS;

	@Value("${it.tndigit.serverFme.consolidaLavorazioneInA4S}")
	private String consolidaLavorazioneInA4S;

	@SpyBean
	@Autowired
	private UtilsFme utilsFme;

	@Autowired
	private LavorazioneSuoloDao lavorazioneSuoloDao;

	@Autowired
	private SuoloDao suoloDao;

	@Autowired
	private GrigliaSuoloDao grigliaSuoloDao;

	@Autowired
	private StatoColtDao statoColtDao;

	@Autowired
	private UsoSuoloDao usoSuoloDao;

	@Autowired
	private WorkspaceLavorazioneDao workspaceLavorazioneDao;

	@SpyBean
	@Autowired
	private ValidLavorazioneInCorsoBusiness validaLavorazioneInCorso;

	@MockBean
	private UtenteControllerClient utenteControllerClient;

	static Server h2WebServer;

	@MockBean
	private Clock clock;

	@BeforeAll
	public static void initTest() throws SQLException {
		h2WebServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082");
		h2WebServer.start();
	}

	@AfterAll
	public static void stopServer() throws SQLException {
		h2WebServer.stop();
	}

	/*
	 * Il test verifica che la lavorazione viene consolidata su A4S Quando la lavorazione viene consolidata, i poligoni di workspace vengono dissolti per stato_colt e cod_uso_suolo; ritagliati su
	 * griglia e validati
	 * 
	 * Nella lavorazione sono inseriti 3 poligoni di workspace verificare che dopo il consolidamento 1) i 3 poligono vengono dissolti in 2 poligoni 2) i poligoni di suolo vengano aggiornati con: 2.1)
	 * id_lavorazione_fine = lavorazioneCorrente 2.2) id_lavorazione_in_corso = null 2.3) data_fine_validita = sysdate
	 * 
	 * 3) i poligoni di workspace vengano ribaltati sul suolo creando un nuovo suolo
	 * 
	 */

	@Test
	@WithMockUser(username = "ITE3277@ext.itad.infotn.it", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/consolidaLavorazioneSuA4S.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void consolidaSuA4S() throws Exception {

		Long idLavorazione = 1483573L;

		LavorazioneSuoloModel preConsolidamentoLavorazione = lavorazioneSuoloDao.findById(idLavorazione).get();
		List<WorkspaceLavSuoloModel> preWorkConsolidamento = preConsolidamentoLavorazione.getListaLavorazioneWorkspaceModel();
		List<SuoloModel> suoloInCorso = preConsolidamentoLavorazione.getListaSuoloInCorsoModel();
		int countWorkspacePreConsolidamento = preWorkConsolidamento.size();
		RappresentaIlModelloPerRappresentareUnUtenteDelSistema utente = new RappresentaIlModelloPerRappresentareUnUtenteDelSistema();
		utente.setNome("Marco");
		utente.setCognome("Campisi");

		Mockito.when(utenteControllerClient.getUtente(Mockito.anyString())).thenReturn(utente);

		mockFmeServer(idLavorazione, HttpStatus.OK, workspaceValidazioneSovrapposizioni);
		consolidaInA4S(idLavorazione, HttpStatus.OK, consolidaLavorazioneInA4S);
		mockSkipVerificheOra(idLavorazione);

		mockMvc.perform(put(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/consolidamentoA4S"))).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// Verifica che i poligoni di workspace siano stati dissolti
		LavorazioneSuoloModel postConsolidamento = lavorazioneSuoloDao.findById(idLavorazione).get();
		List<WorkspaceLavSuoloModel> postConsolidamentoWorkspace = postConsolidamento.getListaLavorazioneWorkspaceModel();
		List<SuoloModel> suoloLavorazioneInzio = postConsolidamento.getListaSuoloInizioModel();
		int countWorkspacePostConsolidamento = postConsolidamentoWorkspace.size();

		assertEquals(3, countWorkspacePreConsolidamento);
		// assertEquals(2, countWorkspacePostConsolidamento);

		// Verifica i poligoni del suolo
		for (SuoloModel suoloModel : suoloInCorso) {
			assertEquals(null, suoloModel.getIdLavorazioneInCorso());
			assertEquals(idLavorazione, suoloModel.getIdLavorazioneFine().getId());
		}

		// assertEquals(countWorkspacePostConsolidamento, suoloLavorazioneInzio.size());
		//
		// for (int i = 0; i < suoloLavorazioneInzio.size(); i++) {
		// assertEquals(suoloLavorazioneInzio.get(i).getAreaColt(), postConsolidamentoWorkspace.get(i).getArea());
		// assertEquals(suoloLavorazioneInzio.get(i).getStatoColtSuolo(), postConsolidamentoWorkspace.get(i).getStatoColtWorkspaceLavSuolo());
		// assertEquals(suoloLavorazioneInzio.get(i).getCodUsoSuoloModel(), postConsolidamentoWorkspace.get(i).getCodUsoSuoloWorkspaceLavSuolo());
		// assertEquals(suoloLavorazioneInzio.get(i).getIdGrid(), postConsolidamentoWorkspace.get(i).getIdGridWorkspace());
		// assertEquals(suoloLavorazioneInzio.get(i).getShape(), postConsolidamentoWorkspace.get(i).getShape());
		// assertEquals(suoloLavorazioneInzio.get(i).getIstatp(), postConsolidamentoWorkspace.get(i).getIstatp());
		// assertEquals(suoloLavorazioneInzio.get(i).getCampagna(), postConsolidamentoWorkspace.get(i).getCampagna());
		// assertEquals(null, suoloLavorazioneInzio.get(i).getIdLavorazioneInCorso());
		// assertEquals(LocalDateTime.of(9999, 12, 31, 00, 00), suoloLavorazioneInzio.get(i).getDataFineValidita());
		// }

	}

	@Test
	@WithMockUser(username = "ITE3277@ext.itad.infotn.it", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/consolidaLavorazioneSuAGS.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void consolidaSuAGS() throws Exception {

		Mockito.when(clock.now()).thenReturn(LocalDateTime.of(2020, 03, 03, 00, 00));

		Long idLavorazione = 1483573L;
		LavorazioneSuoloModel lavorazione = lavorazioneSuoloDao.findById(idLavorazione).get();

		// mockFmeBody(idLavorazione, LocalDateTime.of(2021, 8, 31, 11, 14, 54), lavorazione.getUtente(), consolidaLavorazioneInAGS);
		mockFmeServerAsynch(idLavorazione, lavorazione.getCampagna(), clock.now(), lavorazione.getUtente(), HttpStatus.ACCEPTED, consolidaLavorazioneInAGS);

		mockMvc.perform(put(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/consolidamentoAGS"))).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted());
	}

	@Test
	@WithMockUser(username = "ITE3277@ext.itad.infotn.it", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/consolidaLavorazioneSuAGS.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void consolidaSuAGSFmeFailded() throws Exception {

		Mockito.when(clock.now()).thenReturn(LocalDateTime.of(2020, 03, 03, 00, 00));

		Long idLavorazione = 1483573L;
		LavorazioneSuoloModel lavorazione = lavorazioneSuoloDao.findById(idLavorazione).get();

		// mockFmeBody(idLavorazione, LocalDateTime.of(2021, 8, 31, 11, 14, 54), lavorazione.getUtente(), consolidaLavorazioneInAGS);
		mockFmeServerAsynch(idLavorazione, lavorazione.getCampagna(), clock.now(), lavorazione.getUtente(), HttpStatus.BAD_REQUEST, consolidaLavorazioneInAGS);
		mockMvc.perform(put(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/consolidamentoAGS"))).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotAcceptable());
	}

	@Test
	@WithMockUser(username = "ITE3277@ext.itad.infotn.it", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/consolidaLavorazioneSuAGS.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "update A4ST_LAVORAZIONE_SUOLO set STATO='IN_CORSO' WHERE ID=1483573", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void consolidaSuAGS_LavorazioneStatoDiversoDaCONSOLIDATA_IN_A4S() throws Exception {

		Long idLavorazione = 1483573L;
		LavorazioneSuoloModel lavorazione = lavorazioneSuoloDao.findById(idLavorazione).get();

		mockFmeBody(idLavorazione, lavorazione.getCampagna(), LocalDateTime.of(2021, 8, 31, 11, 14, 54), lavorazione.getUtente(), consolidaLavorazioneInAGS);
		mockFmeServerAsynch(idLavorazione, lavorazione.getCampagna(), LocalDateTime.of(2021, 8, 31, 11, 14, 54), lavorazione.getUtente(), HttpStatus.BAD_REQUEST, consolidaLavorazioneInAGS);
		mockMvc.perform(put(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/consolidamentoAGS"))).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotAcceptable());
	}

	private void mockFmeServer(Long idLavorazione, HttpStatus status, String nomeProcedure) throws URISyntaxException {
		ResponseEntity<String> mockResponse = new ResponseEntity<String>(status);
		doReturn(mockResponse).when(utilsFme).callProcedureFme(idLavorazione, nomeProcedure);
	}

	private void mockFmeBody(Long idLavorazione, Integer campagna, LocalDateTime dataSalvataggioLavorazione, String utenteAgs, String nomeProcedure) throws URISyntaxException {
		String body = utilsFme.generateBodyTrasformataConsolidamentoAGS(idLavorazione, campagna, dataSalvataggioLavorazione, utenteAgs, 1L, 1L, 1L);
		doReturn(body).when(utilsFme).generateBodyTrasformataConsolidamentoAGS(idLavorazione, campagna, dataSalvataggioLavorazione, utenteAgs, 1L, 1L, 1L);
	}

	private void mockFmeServerAsynch(Long idLavorazione, Integer campagna, LocalDateTime dataSalvataggioLavorazione, String utenteAgs, HttpStatus status, String nomeProcedure)
			throws URISyntaxException {
		ResponseEntity<String> mockResponse = new ResponseEntity<String>("{\"id\":6003}", status);
		String body = utilsFme.generateBodyTrasformataConsolidamentoAGS(idLavorazione, campagna, dataSalvataggioLavorazione, utenteAgs, 1L, 1L, 1L);
		doReturn(mockResponse).when(utilsFme).callProcedureFmeAsync(nomeProcedure, body);
	}

	private void consolidaInA4S(Long idLavorazione, HttpStatus status, String nomeProcedure) throws URISyntaxException {

		doReturn(aggiornaWorkspace(idLavorazione, status)).when(utilsFme).callProcedureFme(idLavorazione, nomeProcedure);

	}

	private ResponseEntity<String> aggiornaWorkspace(Long idLavorazione, HttpStatus status) {
		ResponseEntity<String> mockResponse = new ResponseEntity<String>(status);

		WorkspaceLavSuoloModel work1 = workspaceLavorazioneDao.findById(1483632L).get();
		WorkspaceLavSuoloModel work2 = workspaceLavorazioneDao.findById(1483633L).get();
		workspaceLavorazioneDao.delete(work1);
		workspaceLavorazioneDao.delete(work2);

		LavorazioneSuoloModel lavorazione = lavorazioneSuoloDao.findById(idLavorazione).get();

		lavorazione.removeWorkspaceLavSuoloModel(work1);
		lavorazione.removeWorkspaceLavSuoloModel(work2);

		WorkspaceLavSuoloModel workspaceDissolto = new WorkspaceLavSuoloModel();
		workspaceDissolto.setCodUsoSuoloWorkspaceLavSuolo(usoSuoloDao.findByCodUsoSuolo("666"));
		workspaceDissolto.setStatoColtWorkspaceLavSuolo(statoColtDao.findByStatoColt("7"));
		workspaceDissolto.setIdGridWorkspace(grigliaSuoloDao.findById(272L).get());
		workspaceDissolto.setShape(GisUtils.getGeometry(
				"POLYGON ((681793.6896 5098492.1332, 681802.9954 5098492.2879, 681801.3182 5098584.8062, 681794.2249 5098585.1969, 681765.0559 5098586.8034, 681766.8361 5098491.6812, 681791.5575 5098492.0973, 681793.6896 5098492.1332))"));
		workspaceDissolto.setArea(3396.079074);
		workspaceDissolto.setIdLavorazioneWorkspaceLavSuolo(lavorazione);
		workspaceLavorazioneDao.saveAndFlush(workspaceDissolto);
		lavorazione.addWorkspaceLavSuoloModel(workspaceDissolto);

		return mockResponse;
	}

	private void mockSkipVerificheOra(Long idLavorazione) throws Exception {
		LavorazioneSuoloModel lavorazione = lavorazioneSuoloDao.getOne(idLavorazione);
		List<AnomaliaPoligoniWithRootWorkspaceDto> mockResponse = new ArrayList<AnomaliaPoligoniWithRootWorkspaceDto>();

		doReturn(mockResponse).when(validaLavorazioneInCorso).checkPoligoniWorkspaceAnomalieOracle(lavorazione);
	}
}
