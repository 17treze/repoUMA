package it.tndigitale.a4g.richiestamodificasuolo.lavorazioneSuolo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.h2.tools.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

import it.tndigitale.a4g.richiestamodificasuolo.Ruoli;
import it.tndigitale.a4g.richiestamodificasuolo.api.ApiUrls;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.AreaDiLavoroModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.TempClipSuADLModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.AreaDiLavoroDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.LavorazioneSuoloDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.SuoloDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.TempClipSuADLDao;

@CrossOrigin(origins = "*")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class DefinizioneAreaDiLavoroTest {

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

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private LavorazioneSuoloDao lavorazioneDao;

	@Autowired
	private AreaDiLavoroDao areaDiLavoroDao;

	@Autowired
	private TempClipSuADLDao tempClipSuADLDao;

	@Autowired
	private SuoloDao suoloDao;

	@Test
	@Sql(scripts = "/testCase/lavorazioneSuolo/definizioneAreaDiLavoro.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	void creaLavorazioneSuolo() throws Exception {

		// 1. Chiamata al servizio REST delete /{idLavorazione}/areaDiLavoro
		Long idLavorazione = 99999990L;

		mockMvc.perform(delete(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione)).concat("/areaDiLavoro"))).andExpect(status().isOk());

		// 2. Verificare che al termine dell'esecuzione:
		// - Non ci siano dati nella tabella A4ST_AREA_DI_LAVORO per la lavorazione in input
		// - Non ci siano dati nella tabella A4ST_TEMP_CLIP_ADL per la lavorazione in input
		// - Non ci siano suoli con ID_LAVORAZIONE_IN_CORSO = lavorazione in input sulla tabella A4ST_SUOLO_LAYER

		Optional<LavorazioneSuoloModel> lavorazione = lavorazioneDao.findById(idLavorazione);

		List<AreaDiLavoroModel> listAreaLavoro = areaDiLavoroDao.findByLavorazioneSuolo(lavorazione.get());
		assertEquals(listAreaLavoro.size(), 0);

		List<TempClipSuADLModel> listTempClipSuADL = tempClipSuADLDao.findByLavorazioneSuolo(lavorazione.get());
		assertEquals(listTempClipSuADL.size(), 0);

		List<SuoloModel> listSuoliPrenotati = suoloDao.findByIdLavorazioneInCorso(lavorazione.get());
		assertEquals(listSuoliPrenotati.size(), 0);

	}

}
