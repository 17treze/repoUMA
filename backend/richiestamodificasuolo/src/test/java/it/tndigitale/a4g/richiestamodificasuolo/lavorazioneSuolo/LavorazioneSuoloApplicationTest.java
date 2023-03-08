package it.tndigitale.a4g.richiestamodificasuolo.lavorazioneSuolo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.h2.tools.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.wololo.geojson.FeatureCollection;
import org.wololo.geojson.GeoJSONFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.richiestamodificasuolo.Ruoli;
import it.tndigitale.a4g.richiestamodificasuolo.api.ApiUrls;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoLavorazioneSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoRichiestaModificaSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloDichiaratoModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.WorkspaceLavSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.GrigliaSuoloDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.LavorazioneSuoloDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.StatoColtDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.SuoloDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.SuoloDichiaratoDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.UsoSuoloDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.WorkspaceLavorazioneDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.Utils.UtilsFme;
import it.tndigitale.a4g.richiestamodificasuolo.dto.GisUtils;
import it.tndigitale.a4g.richiestamodificasuolo.dto.filter.LavorazioneSuoloFilter;
import it.tndigitale.a4g.richiestamodificasuolo.dto.filter.RicercaLavorazioniSuoloFilter;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.CodDescCodificaSuoloDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.CodificheSuoloDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.GrigliaSuoloDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.LavorazioneSuoloDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.StatoColtDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.SuoloDichiaratoLavorazioneDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.SuoloDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.UsoSuoloDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.ValidazioneLavorazioneErrorDto;

@CrossOrigin(origins = "*")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class LavorazioneSuoloApplicationTest {
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

	// Creazione nuova lavorazione: OK se posso accedere al servizo e creare una nuova lavorazione
	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	void creaLavorazioneSuoloCampagna2022() throws Exception {

		LavorazioneSuoloDto lavorazioneDto = new LavorazioneSuoloDto();

		lavorazioneDto.setStato(StatoLavorazioneSuolo.IN_CREAZIONE.toString());
		lavorazioneDto.setDataInizioLavorazione(LocalDateTime.of(2022, 12, 03, 00, 00));

		String requestPayload = "{ \"annoCampagna\": 2022 }";

		mockMvc.perform(post(ApiUrls.LAVORAZIONE_SUOLO).content(requestPayload).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

	}

	// Creazione nuova lavorazione: OK se posso accedere al servizo e creare una nuova lavorazione
	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	void creaLavorazioneSuoloCampagna2023() throws Exception {

		LavorazioneSuoloDto lavorazioneDto = new LavorazioneSuoloDto();

		lavorazioneDto.setStato(StatoLavorazioneSuolo.IN_CREAZIONE.toString());
		lavorazioneDto.setDataInizioLavorazione(LocalDateTime.of(2023, 12, 03, 00, 00));

		String requestPayload = "{ \"annoCampagna\": 2023 }";
		mockMvc.perform(post(ApiUrls.LAVORAZIONE_SUOLO).content(requestPayload).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

	}

	// Copia di una lavorazione in stato diverso da CONSOLIDATA_SU_AGS
	@Test
	@WithMockUser(username = "ITE2505@ext.itad.infotn.it", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	void copyLavorazioneConsolidataSuAGS() throws Exception {
		String requestPayload = "{ \"idLavorazione\": 144099, \"campagna\": 2022 }";
		LavorazioneSuoloModel oldLavorazione = new LavorazioneSuoloModel();
		StatoLavorazioneSuolo statoLavorazione = StatoLavorazioneSuolo.IN_CREAZIONE;
		StatoLavorazioneSuolo statoRichiesto = StatoLavorazioneSuolo.CONSOLIDATA_SU_AGS;
		if (statoLavorazione == statoRichiesto) {
			mockMvc.perform(post(ApiUrls.LAVORAZIONE_SUOLO.concat("/copiaLavorazione")).content(requestPayload).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError());
		} else {
			mockMvc.perform(post(ApiUrls.LAVORAZIONE_SUOLO.concat("/copiaLavorazione")).content(requestPayload).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		}
	}

	// Copia di una lavorazione controllo se lo stato è diverso da CONSOLIDATA_SU_AGS, deve ritornare un idLavorazione
	@Test
	@WithMockUser(username = "ITE2505@ext.itad.infotn.it", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	void copyLavorazioneSuoloNonConsolidataSuAGS() throws Exception {
		LavorazioneSuoloDto oldLavorazione = new LavorazioneSuoloDto();
		String stato = StatoLavorazioneSuolo.CONSOLIDATA_SU_AGS.toString();
		oldLavorazione.setStato(stato);
		String requestPayload = "{ \"idLavorazione\": 144099, \"campagna\": 2022 }";
		if (oldLavorazione.getStato() == stato) {
			MvcResult result = mockMvc.perform(post(ApiUrls.LAVORAZIONE_SUOLO.concat("/copiaLavorazione")).content(requestPayload).contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isCreated()).andReturn();
			String content = result.getResponse().getContentAsString();
			LavorazioneSuoloDto lav = new LavorazioneSuoloDto();
			lav.setId(144199L);
		}
	}

	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.EDIT_RICHIESTA_MODIFICA_SUOLO_ENTE_COD })
	void creaLavorazioneSuoloNonAutorizzato() throws Exception {

		LavorazioneSuoloDto lavorazioneDto = new LavorazioneSuoloDto();

		lavorazioneDto.setStato(StatoLavorazioneSuolo.IN_CREAZIONE.toString());
		lavorazioneDto.setDataInizioLavorazione(LocalDateTime.of(2007, 12, 03, 00, 00));

		String requestPayload = "{ \"annoCampagna\": 2022 }";

		mockMvc.perform(post(ApiUrls.LAVORAZIONE_SUOLO).content(requestPayload).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());

	}

	// Ricercare LavorazioneSuolo per ID_LAVORAZIONE E UTENTE: OK se posso accedere al servizio e restituisce la lavorazione richiesta
	@Test
	@WithMockUser(username = "ITE2505@ext.itad.infotn.it", roles = { Ruoli.VISUALIZZA_LAVORAZIONE_SUOLO_TUTTI_COD })
	void ricercaLavorazione() throws Exception {

		Long idLavorazione = 137780L;

		MvcResult result = mockMvc.perform(get(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione)))).andExpect(status().isOk()).andReturn();

		String content = result.getResponse().getContentAsString();

		LavorazioneSuoloDto dto = objectMapper.readValue(content, new TypeReference<LavorazioneSuoloDto>() {
		});

		LavorazioneSuoloDto lav = new LavorazioneSuoloDto();
		lav.setId(137780L);
		lav.setUtente("ITE2505@ext.itad.infotn.it");
		lav.setStato(StatoLavorazioneSuolo.IN_CREAZIONE.toString());
		lav.setDataInizioLavorazione(LocalDateTime.of(2021, 03, 02, 00, 00));
		lav.setCampagna(2022);
		lav.setDataFineLavorazione(null);
		lav.setNote(null);
		lav.setTitolo(null);
		lav.setSopralluogo(null);

		assertEquals(dto.getId(), lav.getId());
		assertEquals(dto.getUtente(), lav.getUtente());
		assertEquals(dto.getStato(), lav.getStato());
		assertEquals(dto.getDataInizioLavorazione(), lav.getDataInizioLavorazione());
		assertEquals(dto.getDataFineLavorazione(), lav.getDataFineLavorazione());
		assertEquals(dto.getNote(), lav.getNote());
		assertEquals(dto.getTitolo(), lav.getTitolo());
		assertEquals(dto.getSopralluogo(), lav.getSopralluogo());
		assertEquals(dto.getCampagna(), lav.getCampagna());
		assertNotNull(dto.getDataUltimaModifica());
	}

	// Ricercare LavorazioneSuolo per ID_LAVORAZIONE E UTENTE: OK se posso accedere al servizio e restituisce la lavorazione richiesta
	@Test
	@WithMockUser(username = "VITICOLO", roles = { Ruoli.VISUALIZZA_LAVORAZIONE_SUOLO_VITE_COD })
	void ricercaLavorazioneVITICOLO() throws Exception {

		Long idLavorazione = 137829l;

		MvcResult result = mockMvc.perform(get(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione)))).andExpect(status().isOk()).andReturn();

		String content = result.getResponse().getContentAsString();

		LavorazioneSuoloDto dto = objectMapper.readValue(content, new TypeReference<LavorazioneSuoloDto>() {
		});

		LavorazioneSuoloDto lav = new LavorazioneSuoloDto();
		lav.setId(137829L);
		lav.setUtente("VITICOLO");
		lav.setStato(StatoLavorazioneSuolo.IN_CREAZIONE.toString());
		lav.setDataInizioLavorazione(LocalDateTime.of(2021, 03, 03, 00, 00));
		lav.setDataFineLavorazione(null);
		lav.setNote(null);
		lav.setTitolo(null);
		lav.setSopralluogo(null);

		assertEquals(dto.getId(), lav.getId());
		assertEquals(dto.getUtente(), lav.getUtente());
		assertEquals(dto.getStato(), lav.getStato());
		assertEquals(dto.getDataInizioLavorazione(), lav.getDataInizioLavorazione());
		assertEquals(dto.getDataFineLavorazione(), lav.getDataFineLavorazione());
		assertEquals(dto.getNote(), lav.getNote());
		assertEquals(dto.getTitolo(), lav.getTitolo());
		assertEquals(dto.getSopralluogo(), lav.getSopralluogo());
		assertNotNull(dto.getDataUltimaModifica());
	}

	/*
	 * // Ricercare LavorazioneSuolo per ID_LAVORAZIONE E UTENTE: // OK perchè alla lavorazione è associato un suolo che conviene TAG di tipo VITE
	 * 
	 * @Test
	 * 
	 * @WithMockUser(username = "VITICOLO", roles = { Ruoli.VISUALIZZA_LAVORAZIONE_SUOLO_VITE_COD })
	 * 
	 * @Sql(scripts = "/testCase/lavorazioneSuolo/scenariAlternativiLavorazioneSuolo2.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD) void ricercaLavorazioneVITICOLOConSuoliDichiaratiVite()
	 * throws Exception { Long idLavorazione = 15L; mockMvc.perform(get(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione)))).andExpect(status().isOk());
	 * 
	 * }
	 */

	// Ricercare LavorazioneSuolo per ID_LAVORAZIONE E UTENTE:
	// Forbidden perchè alla lavorazione è associato un suolo che non conviene TAG di tipo VITE
	@Test
	@WithMockUser(username = "VITICOLO", roles = { Ruoli.VISUALIZZA_LAVORAZIONE_SUOLO_VITE_COD })
	@Sql(statements = "Insert into A4ST_SUOLO_DICHIARATO (ID,VERSIONE,ID_RICHIESTA,SHAPE,ID_ISOLA,COD_ISOLA,COD_SEZIONE,ID_SUOL_GIS,TIPO_SUOLO_DICHIARATO,TIPO_SUOLO_RILEVATO,ID_LAVORAZIONE,NOTE_CARICAMENTO,AREA,CODI_RILE_DICHIARATO,CODI_PROD_RILE_DICHIARATO,AREA_ORI,ESITO) "
			+ "values ('1000','0','136449','POLYGON ((671624.340011676 5119733.97522279, 671615.522823267 5119730.76070355, 671634.108659476 5119711.44437648, 671632.869985319 5119709.54528584, 671640.499075783 5119712.49512996, 671643.258040076 5119721.16591812, 671650.846060895 5119724.52208479, 671650.345479137 5119725.44599221, 671650.159531324 5119731.48888449, 671655.208518708 5119741.22166778, 671650.576657005 5119747.97658312, 671643.117676903 5119733.75390045, 671628.362034169 5119744.96281708, 671625.465829189 5119743.68471315, 671624.340011676 5119733.97522279))',"
			+ "'4157721','IT25/02167970223/BBB61','D516','31100090','FRU','BO','137829',null,'700.812753','638','000','700.838086','DA_LAVORARE');")
	void ricercaLavorazioneVITICOLONonAutorizzato() throws Exception {

		Long idLavorazione = 1000L;

		mockMvc.perform(get(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione)))).andExpect(status().isForbidden());

	}

	// Ricercare LavorazioneSuolo per ID_LAVORAZIONE E UTENTE:
	// Forbidden
	@Test
	@WithMockUser(username = "VITICOLO")
	void ricercaLavorazione403() throws Exception {
		Long idLavorazione = 1000L;
		mockMvc.perform(get(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione)))).andExpect(status().isForbidden());

	}

	// Delete lavorazione: OK se posso accedere al servizo e posso eliminare la lavorazione poichè solo il proprietario
	@Test
	@WithMockUser(username = "ITE2505@ext.itad.infotn.it", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD, Ruoli.VISUALIZZA_LAVORAZIONE_SUOLO_TUTTI_COD })
	void deleteLavorazioneSuolo() throws Exception {

		Long idLavorazione = 137756L;
		mockMvc.perform(delete(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione)))).andExpect(status().isOk());

		mockMvc.perform(get(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione)))).andExpect(status().isNoContent());

	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD, Ruoli.VISUALIZZA_LAVORAZIONE_SUOLO_TUTTI_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/associazioneSuoloDaDichiarato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "update A4ST_LAVORAZIONE_SUOLO set stato = 'CHIUSA' where id = 2;")
	void erroreQuandoCancelloLavorazioneChiusa() throws Exception {
		Long idLavorazione = 2L;
		mockMvc.perform(delete(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione)))).andExpect(status().isNotAcceptable());

		mockMvc.perform(get(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione)))).andExpect(status().isOk()).andExpect(jsonPath("id").value(idLavorazione))
				.andExpect(jsonPath("$.stato", is(StatoLavorazioneSuolo.CHIUSA.name())));

	}

	// Delete lavorazione: OK se posso accedere al servizo e non posso eliminare la lavorazione poichè non solo il proprietario
	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	void deleteLavorazioneSuoloEntityNotFound() throws Exception {

		Long idLavorazione = 137756L;
		mockMvc.perform(delete(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione)))).andExpect(status().isNoContent());

	}

	// Delete lavorazione: OK se posso accedere al servizo e non posso eliminare la lavorazione poichè id_lavorazione is null
	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	void deleteLavorazioneSuoloEntityNotFound2() throws Exception {

		Long idLavorazione = null;
		mockMvc.perform(delete(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione)))).andExpect(status().isBadRequest());

	}

	@Test
	@WithMockUser(username = "ITE2505@ext.itad.infotn.it", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD, Ruoli.VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_TUTTI_COD })
	void removeSuoloDichiaratoDallaLavorazioneAndDeleteLavorazioneSuolo() throws Exception {

		Long idLavorazione = 137869L;
		mockMvc.perform(delete(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione)))).andExpect(status().isOk());

		mockMvc.perform(get(ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(2528)))).andExpect(status().isOk()).andExpect(jsonPath("id").value(2528L))
				.andExpect(jsonPath("$.stato", is(StatoRichiestaModificaSuolo.LAVORABILE.name())));
	}

	// Ricercare Suolo per ID_LAVORAZIONE E UTENTE: OK se posso accedere al servizio e restituisce la
	@Test
	@WithMockUser(username = "ITE2505@ext.itad.infotn.it", roles = { Ruoli.VISUALIZZA_LAVORAZIONE_SUOLO_TUTTI_COD })
	void ricercaSuolo() throws Exception {

		Long idLavorazione = 137867L;

		MvcResult result = this.mockMvc.perform(get(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione)).concat("/suolo")).param("numeroElementiPagina", "1")
				.param("pagina", "0").param("proprieta", "id").param("ordine", "DESC")).andExpect(status().isOk()).andReturn();

		String content = result.getResponse().getContentAsString();

		RisultatiPaginati<SuoloDto> risPaginati = objectMapper.readValue(content, new TypeReference<RisultatiPaginati<SuoloDto>>() {
		});

		SuoloDto responseSuoloDto = risPaginati.getRisultati().get(0);

		SuoloDto expectSuoloDto = new SuoloDto();
		expectSuoloDto.setId(142969L);
		StatoColtDto statoColt = new StatoColtDto();
		statoColt.setDataAggiornamento(null);
		statoColt.setDataFine(LocalDateTime.of(9999, 12, 31, 00, 00));
		statoColt.setDataInizio(LocalDateTime.of(2020, 05, 13, 00, 00));
		statoColt.setDescBreve(null);
		statoColt.setDescrizione("COLTIVAZIONE IN ATTO");
		statoColt.setNote(null);
		statoColt.setStatoColt("1");

		expectSuoloDto.setStatoColtSuolo(statoColt);
		expectSuoloDto.setCampagna(2022);
		expectSuoloDto.setExtent(new Double[] { 671998.917803773, 5088744.87610681, 672000.053676912, 5088748.0453544 });
		expectSuoloDto.setShape("POLYGON ((671999.688483259 5088744.87610681, 672000.053676912 5088746.91033953, 671998.917803773 5088748.0453544, 671999.688483259 5088744.87610681))");

		UsoSuoloDto usoSuolo = new UsoSuoloDto();
		usoSuolo.setId(145445L);
		usoSuolo.setCodUsoSuolo("441");

		expectSuoloDto.setCodUsoSuoloModel(usoSuolo);
		expectSuoloDto.setSorgente("CARICAMENTO_INIZIALE");
		expectSuoloDto.setDataInizioValidita(LocalDateTime.of(2021, 03, 19, 15, 00));
		expectSuoloDto.setDataFineValidita(LocalDateTime.of(9999, 12, 31, 00, 00));
		expectSuoloDto.setNote(null);

		LavorazioneSuoloDto lavorazioneInCorso = new LavorazioneSuoloDto();
		lavorazioneInCorso.setId(137867L);
		lavorazioneInCorso.setStato(StatoLavorazioneSuolo.IN_CREAZIONE.toString());
		lavorazioneInCorso.setUtente("ITE2505@ext.itad.infotn.it");
		lavorazioneInCorso.setDataInizioLavorazione(LocalDateTime.of(2021, 03, 03, 00, 00));
		lavorazioneInCorso.setDataFineLavorazione(null);
		lavorazioneInCorso.setTitolo(null);
		lavorazioneInCorso.setSopralluogo(null);
		lavorazioneInCorso.setNote(null);

		expectSuoloDto.setIdLavorazioneInCorso(lavorazioneInCorso);

		LavorazioneSuoloDto lavorazioneInizio = new LavorazioneSuoloDto();
		lavorazioneInizio.setId(142080L);
		lavorazioneInizio.setStato(StatoLavorazioneSuolo.CHIUSA.toString());
		lavorazioneInizio.setUtente("CARICAMENTO_INIZIALE");
		lavorazioneInizio.setDataInizioLavorazione(LocalDateTime.of(2021, 03, 24, 00, 00));
		lavorazioneInizio.setDataFineLavorazione(LocalDateTime.of(2021, 03, 24, 00, 00));
		lavorazioneInizio.setTitolo("Caricamento Iniziale");
		lavorazioneInizio.setSopralluogo(null);
		lavorazioneInizio.setNote(null);

		expectSuoloDto.setIdLavorazioneInizio(lavorazioneInizio);
		expectSuoloDto.setIdLavorazioneFine(null);

		GrigliaSuoloDto griglia = new GrigliaSuoloDto();

		griglia.setId(96L);
		griglia.setIntersecaSuolo(true);
		griglia.setConfine(false);

		expectSuoloDto.setIdGrid(griglia);

		// Verifica Risultati Suolo
		assertEquals(expectSuoloDto.getId(), responseSuoloDto.getId());
		assertEquals(expectSuoloDto.getCampagna(), responseSuoloDto.getCampagna());
		assertArrayEquals(expectSuoloDto.getExtent(), responseSuoloDto.getExtent());
		assertEquals(expectSuoloDto.getShape(), responseSuoloDto.getShape());

		assertEquals(expectSuoloDto.getStatoColtSuolo().getDataAggiornamento(), responseSuoloDto.getStatoColtSuolo().getDataAggiornamento());
		assertEquals(expectSuoloDto.getStatoColtSuolo().getDataInizio(), responseSuoloDto.getStatoColtSuolo().getDataInizio());
		assertEquals(expectSuoloDto.getStatoColtSuolo().getDataFine(), responseSuoloDto.getStatoColtSuolo().getDataFine());
		assertEquals(expectSuoloDto.getStatoColtSuolo().getDescBreve(), responseSuoloDto.getStatoColtSuolo().getDescBreve());
		assertEquals(expectSuoloDto.getStatoColtSuolo().getDescrizione(), responseSuoloDto.getStatoColtSuolo().getDescrizione());
		assertEquals(expectSuoloDto.getStatoColtSuolo().getNote(), responseSuoloDto.getStatoColtSuolo().getNote());
		assertEquals(expectSuoloDto.getStatoColtSuolo().getStatoColt(), responseSuoloDto.getStatoColtSuolo().getStatoColt());

		assertEquals(expectSuoloDto.getCodUsoSuoloModel().getId(), responseSuoloDto.getCodUsoSuoloModel().getId());
		assertEquals(expectSuoloDto.getCodUsoSuoloModel().getCodUsoSuolo(), responseSuoloDto.getCodUsoSuoloModel().getCodUsoSuolo());
		assertEquals(expectSuoloDto.getSorgente(), responseSuoloDto.getSorgente());
		assertEquals(expectSuoloDto.getDataInizioValidita(), responseSuoloDto.getDataInizioValidita());
		assertEquals(expectSuoloDto.getDataFineValidita(), responseSuoloDto.getDataFineValidita());
		assertEquals(expectSuoloDto.getNote(), responseSuoloDto.getNote());

		// Verifica Lavorazione in corso
		assertEquals(expectSuoloDto.getIdLavorazioneInCorso().getId(), responseSuoloDto.getIdLavorazioneInCorso().getId());
		assertEquals(expectSuoloDto.getIdLavorazioneInCorso().getStato(), responseSuoloDto.getIdLavorazioneInCorso().getStato());
		assertEquals(expectSuoloDto.getIdLavorazioneInCorso().getUtente(), responseSuoloDto.getIdLavorazioneInCorso().getUtente());
		assertEquals(expectSuoloDto.getIdLavorazioneInCorso().getDataInizioLavorazione(), responseSuoloDto.getIdLavorazioneInCorso().getDataInizioLavorazione());
		assertEquals(expectSuoloDto.getIdLavorazioneInCorso().getDataFineLavorazione(), responseSuoloDto.getIdLavorazioneInCorso().getDataFineLavorazione());
		assertEquals(expectSuoloDto.getIdLavorazioneInCorso().getTitolo(), responseSuoloDto.getIdLavorazioneInCorso().getTitolo());
		assertEquals(expectSuoloDto.getIdLavorazioneInCorso().getSopralluogo(), responseSuoloDto.getIdLavorazioneInCorso().getSopralluogo());
		assertEquals(expectSuoloDto.getIdLavorazioneInCorso().getNote(), responseSuoloDto.getIdLavorazioneInCorso().getNote());

		// Verifica Lavorazione fine
		assertEquals(expectSuoloDto.getIdLavorazioneFine(), responseSuoloDto.getIdLavorazioneFine());

		// Verifica Griglia
		assertEquals(expectSuoloDto.getIdGrid().getId(), responseSuoloDto.getIdGrid().getId());
		assertEquals(expectSuoloDto.getIdGrid().isIntersecaSuolo(), responseSuoloDto.getIdGrid().isIntersecaSuolo());
		assertEquals(expectSuoloDto.getIdGrid().isConfine(), responseSuoloDto.getIdGrid().isConfine());
	}

	// Test sulla rimozione del poligono dalla lavorazione
	@Test
	@WithMockUser(username = "ITE2505@ext.itad.infotn.it", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	void removePoligonoSuoloDaLavorazione() throws Exception {

		Long idLavorazione = 137756L;
		Long idPoligono = 142961L;

		SuoloDto suoloDto = new SuoloDto();
		LavorazioneSuoloDto inCorso = new LavorazioneSuoloDto();
		inCorso.setId(idLavorazione);
		suoloDto.setIdLavorazioneInCorso(inCorso);
		mockMvc.perform(put(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idPoligono).concat("/rimuoviAssociazionePoligono"))).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(suoloDto))).andExpect(status().isOk());
	}

	// Test sulla rimozione del poligono dalla lavorazione
	@Test
	@WithMockUser(username = "ITE2505@ext.itad.infotn.it", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	@Sql(statements = "update A4ST_LAVORAZIONE_SUOLO set STATO='IN_CORSO' WHERE ID=137756", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "update A4ST_LAVORAZIONE_SUOLO set STATO='IN_CREAZIONE' WHERE ID=137756", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void removePoligonoSuoloDaLavorazione_StatoLavorazioneDiverso_da_IN_CREAZIONE_o_IN_MODIFICA() throws Exception {

		Long idLavorazione = 137756L;
		Long idPoligono = 142961L;

		SuoloDto suoloDto = new SuoloDto();
		LavorazioneSuoloDto inCorso = new LavorazioneSuoloDto();
		inCorso.setId(idLavorazione);
		suoloDto.setIdLavorazioneInCorso(inCorso);
		mockMvc.perform(put(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idPoligono).concat("/rimuoviAssociazionePoligono"))).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(suoloDto))).andExpect(status().isOk());
	}

	// Test sulla rimozione del poligono dalla lavorazione in caso di poligoni non presenti
	@Test
	@WithMockUser(username = "ITE2505@ext.itad.infotn.it", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	void removePoligonoSuoloDaLavorazioneEntityNotFoundLavorazione() throws Exception {

		Long idLavorazione = 937756L;
		Long idPoligono = 942961L;

		SuoloDto suoloDto = new SuoloDto();
		LavorazioneSuoloDto inCorso = new LavorazioneSuoloDto();
		inCorso.setId(idLavorazione);
		suoloDto.setIdLavorazioneInCorso(inCorso);
		mockMvc.perform(put(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idPoligono).concat("/rimuoviAssociazionePoligono"))).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(suoloDto))).andExpect(status().isNoContent());
	}

	@Test
	@WithMockUser(username = "ITE2505@ext.itad.infotn.it", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	void removePoligonoSuoloDaLavorazioneEntityNotFoundSuolo() throws Exception {

		Long idLavorazione = 137756L;
		Long idPoligono = 94296111111111111L;

		SuoloDto suoloDto = new SuoloDto();
		LavorazioneSuoloDto inCorso = new LavorazioneSuoloDto();
		inCorso.setId(idLavorazione);
		suoloDto.setIdLavorazioneInCorso(inCorso);
		mockMvc.perform(put(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idPoligono).concat("/rimuoviAssociazionePoligono"))).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(suoloDto))).andExpect(status().isNoContent());
	}

	// Verifica validazione lavorazione, la lavorazione non ha:
	// uno o più poligoni dichiarati associati;
	// uno o più poligoni di suolo vigente associati;
	@Test
	@WithMockUser(username = "ITE2505@ext.itad.infotn.it", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	void validaLavorazioneConErrori() throws Exception {

		Long idLavorazione = 137870L;

		MvcResult result = this.mockMvc.perform(get(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione)).concat("/validate"))).andExpect(status().isOk()).andReturn();

		String content = result.getResponse().getContentAsString();

		ValidazioneLavorazioneErrorDto validazione = objectMapper.readValue(content, new TypeReference<ValidazioneLavorazioneErrorDto>() {
		});

		assertEquals(validazione.getMessage(), "Nessun Suolo Vigente Inserito o Nessun Suolo Dichiarato Inserito");
	}

	// Aggiorna informazioni Lavorazione (tranne i campi : id,utente,dataInizioLavorazione,stato)
	@Test
	@WithMockUser(username = "ITE2505@ext.itad.infotn.it", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD, Ruoli.VISUALIZZA_LAVORAZIONE_SUOLO_TUTTI_COD })
	void updateLavorazioneSuolo() throws Exception {

		Long idLavorazione = 137870L;
		LavorazioneSuoloDto lavorazioneDto = new LavorazioneSuoloDto();
		lavorazioneDto.setNote("NOTE AGGIORNATE");
		lavorazioneDto.setTitolo("TITOLO AGGIORNATO");
		lavorazioneDto.setUtente("ITE2505@ext.itad.infotn.it");

		mockMvc.perform(
				put(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione))).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(lavorazioneDto)))
				.andExpect(status().isOk());

		// Verifico che solo i campi passati sono stati aggiornati

		MvcResult result = mockMvc.perform(get(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione)))).andExpect(status().isOk()).andReturn();

		String content = result.getResponse().getContentAsString();

		LavorazioneSuoloDto dto = objectMapper.readValue(content, new TypeReference<LavorazioneSuoloDto>() {
		});

		lavorazioneDto.setStato(StatoLavorazioneSuolo.IN_CREAZIONE.toString());
		lavorazioneDto.setDataInizioLavorazione(LocalDateTime.of(2021, 03, 03, 00, 00));
		lavorazioneDto.setDataFineLavorazione(null);
		lavorazioneDto.setSopralluogo(null);

		assertEquals(dto.getId(), idLavorazione);
		assertEquals(dto.getUtente(), lavorazioneDto.getUtente());
		assertEquals(dto.getStato(), lavorazioneDto.getStato());
		assertEquals(dto.getDataInizioLavorazione(), lavorazioneDto.getDataInizioLavorazione());
		assertEquals(dto.getDataFineLavorazione(), lavorazioneDto.getDataFineLavorazione());

		assertEquals(dto.getNote(), lavorazioneDto.getNote());

		assertNotNull(dto.getDataUltimaModifica());
		assertEquals(dto.getTitolo(), lavorazioneDto.getTitolo());

	}

	// Aggiorna informazioni Lavorazione
	@Test
	@WithMockUser(username = "ITE2505@ext.itad.infotn.it", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	void updateLavorazioneSuoloEntityNotFound() throws Exception {

		Long idLavorazione = 1378701111L;
		LavorazioneSuoloDto lavorazioneDto = new LavorazioneSuoloDto();
		lavorazioneDto.setNote("NOTE AGGIORNATE");

		mockMvc.perform(
				put(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione))).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(lavorazioneDto)))
				.andExpect(status().isNotAcceptable());

	}

	// Copia le lavorazioni nel workspace
	@Test
	@WithMockUser(username = "ITE2505@ext.itad.infotn.it", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD, Ruoli.VISUALIZZA_LAVORAZIONE_SUOLO_TUTTI_COD })
	void insertLavorazioneInWorkspace() throws Exception {

		Long idLavorazione = 137867L;

		mockFmeCreaAreaDiLavoro(idLavorazione, HttpStatus.OK, creaAreaDiLavoro);

		mockMvc.perform(post(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/workspace"))).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

		mockMvc.perform(get(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione))).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())

				.andExpect(jsonPath("$.stato", is(StatoLavorazioneSuolo.IN_CORSO.name())));
	}

	// Verifica Intersezione pascoli true
	@Test
	@WithMockUser(username = "ITE2505@ext.itad.infotn.it", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD, Ruoli.VISUALIZZA_LAVORAZIONE_SUOLO_TUTTI_COD })
	void insertLavorazioneInWorkspaceVerificaIntersezionePascoli() throws Exception {

		Long idLavorazione = 137867L;
		Integer annoUpas = 2022;

		mockFmeVerificaIntersezionePascoli(idLavorazione, annoUpas, HttpStatus.PRECONDITION_FAILED, verificaIntersezioneUpas);

		mockMvc.perform(post(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/workspace"))).param("verificaPascoli", "true").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isPreconditionFailed());

	}

	// Verifica Intersezione pascoli true fme error
	@Test
	@WithMockUser(username = "ITE2505@ext.itad.infotn.it", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD, Ruoli.VISUALIZZA_LAVORAZIONE_SUOLO_TUTTI_COD })
	void insertLavorazioneInWorkspaceVerificaIntersezionePascoliFmeError() throws Exception {

		Long idLavorazione = 137867L;
		Integer annoUpas = 2022;

		mockFmeVerificaIntersezionePascoli(idLavorazione, annoUpas, HttpStatus.INTERNAL_SERVER_ERROR, verificaIntersezioneUpas);

		mockMvc.perform(post(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/workspace"))).param("verificaPascoli", "true").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError());
	}

	// Copia le lavorazioni nel workspace
	@Test
	@WithMockUser(username = "VITICOLO", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD, Ruoli.VISUALIZZA_LAVORAZIONE_SUOLO_TUTTI_COD })
	@Sql(statements = "Insert into A4ST_LAVORAZIONE_SUOLO (ID,VERSIONE,UTENTE,STATO,DATA_INIZIO_LAVORAZIONE,DATA_FINE_LAVORAZIONE,TITOLO,SOPRALLUOGO,NOTE,DATA_ULTIMA_MODIFICA) "
			+ "		   values ('1003','0','VITICOLO','CHIUSA',  to_date('03/03/2021','DD/MM/YYYY'),null,'Titolo',null,'Notes',to_date('03/03/2021 15:00:00','DD/MM/YYYY HH24:MI:SS'));")

	void insertLavorazioneInWorkspace_406_INVALID_ARGUMENT_EXCEPTION() throws Exception {

		Long idLavorazione = 1003L;

		mockFmeCreaAreaDiLavoro(idLavorazione, HttpStatus.OK, creaAreaDiLavoro);
		mockMvc.perform(post(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/workspace"))).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotAcceptable());

	}

	// Copia le lavorazioni nel workspace
	@Test
	@WithMockUser(username = "ITE2505@ext.itad.infotn.it", roles = { Ruoli.VISUALIZZA_LAVORAZIONE_SUOLO_TUTTI_COD })
	void insertLavorazioneInWorkspaceForbidden() throws Exception {

		Long idLavorazione = 137867L;
		mockFmeCreaAreaDiLavoro(idLavorazione, HttpStatus.OK, creaAreaDiLavoro);
		mockMvc.perform(post(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/workspace"))).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
	}

	// Copia le lavorazioni nel workspace
	@Test
	@WithMockUser(username = "ITE2505@ext.itad.infotn.it", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	void insertLavorazioneInWorkspaceEntityNotFound() throws Exception {

		Long idLavorazione = 1378671111L;
		mockFmeCreaAreaDiLavoro(idLavorazione, HttpStatus.OK, creaAreaDiLavoro);
		mockMvc.perform(post(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/workspace"))).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError());

	}

	// Copia le lavorazioni nel workspace
	@Test
	@WithMockUser(username = "ITE2505@ext.itad.infotn.it", roles = { Ruoli.EDIT_POLIGONO_TUTTI_COD, Ruoli.EDIT_LAVORAZIONE_SUOLO_COD, Ruoli.VISUALIZZA_LAVORAZIONE_SUOLO_TUTTI_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/InserisciEAggiornaWorkspace.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void aggiornaWorkspace() throws Exception {

		Long idLavorazione = 31052021L;

		Long idSuoloDichiarato = 3005202111L;

		SuoloDichiaratoLavorazioneDto suoloDto = new SuoloDichiaratoLavorazioneDto();
		suoloDto.setIdLavorazione(idLavorazione);

		// Associo alla lavorazione SuoloDichiaratoLavorazioneDto suoloDto = new SuoloDichiaratoLavorazioneDto(); System.out.println(suoloDto.toString()); suoloDto.setIdLavorazione(idLavorazione);
		mockMvc.perform(put(ApiUrls.SUOLO_DICHIARATO.concat("/").concat(String.valueOf(idSuoloDichiarato)).concat("/associaLavorazione")).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(suoloDto))).andExpect(status().isOk());

		mockMvc.perform(post(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/associaSuoloDaDichiarato"))).contentType(MediaType.APPLICATION_JSON)
				.content("{\"versione\": 0}")).andExpect(status().isOk());

		mockFmeCreaAreaDiLavoro(idLavorazione, HttpStatus.OK, creaAreaDiLavoro);
		// Insert poligoni in workspace
		mockMvc.perform(post(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/workspace"))).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

		// Controllo passaggio di stato lavorazione
		mockMvc.perform(get(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione))).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.stato", is(StatoLavorazioneSuolo.IN_CORSO.name())));

		// Aggiorno i poligoni del wokspace
		String geoJson = "{\r\n" + "	\"type\": \"FeatureCollection\",\r\n" + "	\"features\": [\r\n" + "		{\r\n" + "			\"type\": \"Feature\",\r\n" + "			\"geometry\": {\r\n"
				+ "				\"type\": \"Polygon\",\r\n" + "				\"coordinates\": [\r\n" + "					[\r\n" + "						[\r\n"
				+ "							655781.9458,\r\n" + "							5085144.447\r\n" + "						],\r\n" + "						[\r\n"
				+ "							655786.8862,\r\n" + "							5085147.7406\r\n" + "						],\r\n" + "						[\r\n"
				+ "							655799.5746,\r\n" + "							5085149.4322\r\n" + "						],\r\n" + "						[\r\n"
				+ "							655802.9582,\r\n" + "							5085157.0452\r\n" + "						],\r\n" + "						[\r\n"
				+ "							655796.6141,\r\n" + "							5085167.6189\r\n" + "						],\r\n" + "						[\r\n"
				+ "							655786.0405,\r\n" + "							5085174.809\r\n" + "						],\r\n" + "						[\r\n"
				+ "							655780.9652,\r\n" + "							5085176.5008\r\n" + "						],\r\n" + "						[\r\n"
				+ "							655772.9292,\r\n" + "							5085172.2714\r\n" + "						],\r\n" + "						[\r\n"
				+ "							655760.6638,\r\n" + "							5085166.7733\r\n" + "						],\r\n" + "						[\r\n"
				+ "							655762.3555,\r\n" + "							5085158.3144\r\n" + "						],\r\n" + "						[\r\n"
				+ "							655763.6243,\r\n" + "							5085156.6226\r\n" + "						],\r\n" + "						[\r\n"
				+ "							655768.2767,\r\n" + "							5085153.239\r\n" + "						],\r\n" + "						[\r\n"
				+ "							655769.1226,\r\n" + "							5085153.239\r\n" + "						],\r\n" + "						[\r\n"
				+ "							655779.4873,\r\n" + "							5085142.0448\r\n" + "						],\r\n" + "						[\r\n"
				+ "							655779.8862,\r\n" + "							5085142.0475\r\n" + "						],\r\n" + "						[\r\n"
				+ "							655781.8108,\r\n" + "							5085144.357\r\n" + "						],\r\n" + "						[\r\n"
				+ "							655781.9458,\r\n" + "							5085144.447\r\n" + "						]\r\n" + "					]\r\n" + "				]\r\n"
				+ "			},\r\n" + "			\"properties\": {\r\n" + "				\"ID\": 1443926,\r\n" + "				\"ID_LAVORAZIONE\": 1438020,\r\n"
				+ "				\"COD_USO_SUOLO\": \"666\",\r\n" + "				\"USO_SUOLO_DES\": \"SEMINATIVI\",\r\n" + "				\"STATO_COLT\": 0,\r\n"
				+ "				\"STATO_COLT_DES\": \"GENERICO\",\r\n" + "				\"NOTE\": \"1353383\",\r\n" + "				\"DATA_ULTIMA_MODIFICA\": \"2021-05-30Z\"\r\n" + "			},\r\n"
				+ "			\"id\": \"A4SV_WORKSPACE_LAV_SUOLO_LAYER2.fid-5356ee6d_179c258ac27_-39b6\"\r\n" + "		},\r\n" + "		{\r\n" + "			\"type\": \"Feature\",\r\n"
				+ "			\"geometry\": {\r\n" + "				\"type\": \"Polygon\",\r\n" + "				\"coordinates\": [\r\n" + "					[\r\n" + "						[\r\n"
				+ "							655781.9458,\r\n" + "							5085144.447\r\n" + "						],\r\n" + "						[\r\n"
				+ "							655781.8108,\r\n" + "							5085143.9341\r\n" + "						],\r\n" + "						[\r\n"
				+ "							655780.4483,\r\n" + "							5085139.3015\r\n" + "						],\r\n" + "						[\r\n"
				+ "							655790.364,\r\n" + "							5085138.1482\r\n" + "						],\r\n" + "						[\r\n"
				+ "							655790.474,\r\n" + "							5085138.1412\r\n" + "						],\r\n" + "						[\r\n"
				+ "							655804.1708,\r\n" + "							5085145.767\r\n" + "						],\r\n" + "						[\r\n"
				+ "							655811.6876,\r\n" + "							5085143.972\r\n" + "						],\r\n" + "						[\r\n"
				+ "							655813.7571,\r\n" + "							5085130.1505\r\n" + "						],\r\n" + "						[\r\n"
				+ "							655815.1861,\r\n" + "							5085131.513\r\n" + "						],\r\n" + "						[\r\n"
				+ "							655816.9182,\r\n" + "							5085134.0446\r\n" + "						],\r\n" + "						[\r\n"
				+ "							655816.0693,\r\n" + "							5085139.7044\r\n" + "						],\r\n" + "						[\r\n"
				+ "							655808.0334,\r\n" + "							5085149.0092\r\n" + "						],\r\n" + "						[\r\n"
				+ "							655799.927,\r\n" + "							5085150.2252\r\n" + "						],\r\n" + "						[\r\n"
				+ "							655799.5746,\r\n" + "							5085149.4322\r\n" + "						],\r\n" + "						[\r\n"
				+ "							655786.8862,\r\n" + "							5085147.7406\r\n" + "						],\r\n" + "						[\r\n"
				+ "							655781.9458,\r\n" + "							5085144.447\r\n" + "						]\r\n" + "					]\r\n" + "				]\r\n"
				+ "			},\r\n" + "			\"properties\": {\r\n" + "				\"ID\": 1443928,\r\n" + "				\"ID_LAVORAZIONE\": 1438020,\r\n"
				+ "				\"COD_USO_SUOLO\": \"651\",\r\n" + "				\"USO_SUOLO_DES\": \"COLTIVAZIONI ARBOREE SPECIALIZZATE\",\r\n" + "				\"STATO_COLT\": 0,\r\n"
				+ "				\"STATO_COLT_DES\": \"GENERICO\",\r\n" + "				\"NOTE\": \"1222367\",\r\n" + "				\"DATA_ULTIMA_MODIFICA\": \"2021-05-30Z\"\r\n" + "			},\r\n"
				+ "			\"id\": \"A4SV_WORKSPACE_LAV_SUOLO_LAYER2.fid-5356ee6d_179c258ac27_-39b4\"\r\n" + "		}\r\n" + "	]\r\n" + "}";
		FeatureCollection featureCollection = (FeatureCollection) GeoJSONFactory.create(geoJson);

		mockFmeCreaAreaDiLavoro(idLavorazione, HttpStatus.OK, creaAreaDiLavoro);

		mockMvc.perform(put(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/workspace"))).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(featureCollection))).andExpect(status().isCreated());

		String geoJsonNoProperty = "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[652835.1639466265,5075690.346357413],[652833.7501863044,5075690.346357413],[652833.7501863044,5075695.75764692],[652835.1639466265,5075695.75764692],[652835.1639466265,5075690.346357413]]]},\"properties\":null}]}";

		FeatureCollection featureCollectionNoProperty = (FeatureCollection) GeoJSONFactory.create(geoJsonNoProperty);

		mockFmeCreaAreaDiLavoro(idLavorazione, HttpStatus.OK, creaAreaDiLavoro);
		mockMvc.perform(put(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/workspace"))).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(featureCollectionNoProperty))).andExpect(status().isCreated());

	}

	@Test
	@WithMockUser(username = "ITE2505@ext.itad.infotn.it", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD, Ruoli.VISUALIZZA_LAVORAZIONE_SUOLO_TUTTI_COD })
	@Sql(statements = "insert into A4ST_LAVORAZIONE_SUOLO (ID,VERSIONE,UTENTE,STATO,DATA_INIZIO_LAVORAZIONE,DATA_FINE_LAVORAZIONE,TITOLO,SOPRALLUOGO,NOTE,DATA_ULTIMA_MODIFICA) values ('3105202104','0','ITE2505@ext.itad.infotn.it','IN_CREAZIONE',to_date('31/05/2021','DD/MM/YYYY'),null,null,null,null,to_date('31/05/2021 15:00;00','DD/MM/YYYY HH24:MI:SS'));")
	void aggiornaWorkspace_406_INVALID_ARGUMENT_EXCEPTION() throws Exception {

		Long idLavorazione = 3105202104L;

		String featureCollection = null;
		mockFmeCreaAreaDiLavoro(idLavorazione, HttpStatus.OK, creaAreaDiLavoro);
		mockMvc.perform(put(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/workspace"))).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(featureCollection))).andExpect(status().isNotAcceptable());

	}

	@Test
	@WithMockUser(username = "ITE2505@ext.itad.infotn.it", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD, Ruoli.VISUALIZZA_LAVORAZIONE_SUOLO_TUTTI_COD })
	@Sql(statements = "insert into A4ST_LAVORAZIONE_SUOLO (ID,VERSIONE,UTENTE,STATO,DATA_INIZIO_LAVORAZIONE,DATA_FINE_LAVORAZIONE,TITOLO,SOPRALLUOGO,NOTE,DATA_ULTIMA_MODIFICA) values ('3105202105','0','ITE2505@ext.itad.infotn.it','IN_CORSO',to_date('31/05/2021','DD/MM/YYYY'),null,null,null,null,to_date('31/05/2021 15:00;00','DD/MM/YYYY HH24:MI:SS'));")
	void aggiornaWorkspace_406_INVALID_ARGUMENT_EXCEPTION2() throws Exception {

		Long idLavorazione = 3105202105L;

		String featureCollection = "sss";
		mockFmeCreaAreaDiLavoro(idLavorazione, HttpStatus.OK, creaAreaDiLavoro);
		mockMvc.perform(put(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/workspace"))).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(featureCollection))).andExpect(status().isNotAcceptable());

	}

	@Test
	@WithMockUser(username = "VITICOLO", roles = {})
	@Sql(statements = "Insert into A4ST_LAVORAZIONE_SUOLO (ID,VERSIONE,UTENTE,STATO,DATA_INIZIO_LAVORAZIONE,DATA_FINE_LAVORAZIONE,TITOLO,SOPRALLUOGO,NOTE,DATA_ULTIMA_MODIFICA) "
			+ "		   values ('1001','0','VITICOLO','IN_CREAZIONE',  to_date('03/03/2021','DD/MM/YYYY'),null,'Titolo',null,'Notes',to_date('03/03/2021 15:00:00','DD/MM/YYYY HH24:MI:SS'));")
	void ricercaLavorazioniNonConcluseNonConsolidateAgsConFiltri() throws Exception {

		LavorazioneSuoloFilter filter = new LavorazioneSuoloFilter();
		filter.setNote("Notes");
		filter.setDataFineLavorazione(null);
		filter.setTitolo("Titolo");
		filter.setSopralluogo(null);
		filter.setIdLavorazione(1001L);
		filter.setDataInizioLavorazione(LocalDateTime.of(2021, 03, 03, 00, 00));
		filter.setDataUltimaModifica(LocalDateTime.of(2021, 03, 03, 15, 00));

		String filtri = filter.toString();

		MvcResult result = this.mockMvc.perform(get(ApiUrls.LAVORAZIONE_SUOLO.concat("/nonConcluse")).param("note", filter.getNote()).param("titolo", filter.getTitolo())
				.param("idLavorazione", String.valueOf(filter.getIdLavorazione())).param("dataInizioLavorazione", filter.getDataInizioLavorazione().toString())
				.param("dataUltimaModifica", filter.getDataUltimaModifica().toString()).param("numeroElementiPagina", "10").param("pagina", "0").param("proprieta", "dataUltimaModifica")
				.param("ordine", "DESC")).andExpect(status().isOk()).andReturn();

		String content = result.getResponse().getContentAsString();

		RisultatiPaginati<LavorazioneSuoloDto> risPaginati = objectMapper.readValue(content, new TypeReference<RisultatiPaginati<LavorazioneSuoloDto>>() {
		});
		risPaginati.getRisultati().forEach(lav -> {
			assertEquals(lav.getUtente(), "VITICOLO");
			assertNotEquals(lav.getStato(), StatoLavorazioneSuolo.CHIUSA.toString());
			assertNotEquals(lav.getId(), 144012L);
			assertNotNull(lav.getDataUltimaModifica());
		});

	}

	@Test
	@WithMockUser(username = "VITICOLO", roles = {})
	@Sql(statements = "Insert into A4ST_LAVORAZIONE_SUOLO (ID,VERSIONE,UTENTE,STATO,DATA_INIZIO_LAVORAZIONE,DATA_FINE_LAVORAZIONE,TITOLO,SOPRALLUOGO,NOTE,DATA_ULTIMA_MODIFICA) "
			+ "		   values ('1002','0','VITICOLO','IN_CREAZIONE',  to_date('03/03/2021','DD/MM/YYYY'),null,'Titolo',null,'Notes',to_date('03/03/2021 15:00:00','DD/MM/YYYY HH24:MI:SS'));")
	void ricercaLavorazioniNonConcluseNonConsolidateInAgs() throws Exception {

		MvcResult result = this.mockMvc
				.perform(
						get(ApiUrls.LAVORAZIONE_SUOLO.concat("/nonConcluse")).param("numeroElementiPagina", "10").param("pagina", "0").param("proprieta", "dataUltimaModifica").param("ordine", "DESC"))
				.andExpect(status().isOk()).andReturn();

		String content = result.getResponse().getContentAsString();

		RisultatiPaginati<LavorazioneSuoloDto> risPaginati = objectMapper.readValue(content, new TypeReference<RisultatiPaginati<LavorazioneSuoloDto>>() {
		});
		risPaginati.getRisultati().forEach(lav -> {
			assertEquals(lav.getUtente(), "VITICOLO");
			assertNotEquals(lav.getStato(), StatoLavorazioneSuolo.CHIUSA.toString());
			assertNotEquals(lav.getId(), 144012L);
			assertNotNull(lav.getDataUltimaModifica());
		});

	}

	/// qui
	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD, Ruoli.VISUALIZZA_LAVORAZIONE_SUOLO_TUTTI_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/associazioneSuoloDaDichiarato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void associaPoligoni() throws Exception {
		Long idLavorazione = 2L;

		this.mockMvc
				.perform(post(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/associaSuoloDaDichiarato"))).contentType(MediaType.APPLICATION_JSON)
						.content("{\"versione\": 0}"))
				.andExpect(status().isOk())
				// controllo suoli scartati
				.andExpect(content().json(
						"[{\"suoloVigente\":{\"idSuoloVigente\":3,\"idLavorazione\":1,\"utente\":\"BACKOFFICE\", \"dataUltimaLavorazione\":\"2021-03-30T00:00:00\"},\"idSuoloDichiarato\":[11]}]"));
		// controllo suoli associati
		this.mockMvc.perform(get(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione)).concat("/suolo")).param("numeroElementiPagina", "10").param("pagina", "0")
				.param("proprieta", "id").param("ordine", "DESC")).andExpect(status().isOk()).andExpect(jsonPath("$.count", is(1))).andExpect(jsonPath("$.risultati[0].id", is(1))).andReturn();
	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD, Ruoli.VISUALIZZA_LAVORAZIONE_SUOLO_TUTTI_COD })
	void cercaPoligoniVigentiDaPuntoInMappaOk() throws Exception {
		Long idLavorazione = 144007l;
		String pointCoord = "1622206,5080260";

		// Geometry puntoInMappa = GisUtils.getGeometry("POINT (" + pointCoord.replace(',', ' ') + ")");
		// SuoloModel suoli = suoloDao.findByContains(puntoInMappa);

		String geoJson = "{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[" + pointCoord + "]},\"properties\":null}";

		this.mockMvc
				.perform(put(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/aggiungiPoligonoDaPunto"))).contentType(MediaType.APPLICATION_JSON).content(geoJson))
				.andExpect(status().isCreated());
	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD, Ruoli.VISUALIZZA_LAVORAZIONE_SUOLO_TUTTI_COD })
	void cercaPoligoniVigentiDaPuntoInMappaNotFound() throws Exception {
		Long idLavorazione = 144007l;
		String pointCoord = "1622204.87,5080259.95";

		String geoJson = "{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[" + pointCoord + "]},\"properties\":null}";

		this.mockMvc
				.perform(put(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/aggiungiPoligonoDaPunto"))).contentType(MediaType.APPLICATION_JSON).content(geoJson))
				.andExpect(status().isNoContent());
	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD, Ruoli.VISUALIZZA_LAVORAZIONE_SUOLO_TUTTI_COD })
	void cercaPoligoniVigentiDaPuntoInMappaGiaAssociato() throws Exception {
		Long idLavorazione = 144007l;
		String pointCoord = "672000,5088745";

		String geoJson = "{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[" + pointCoord + "]},\"properties\":null}";

		this.mockMvc
				.perform(put(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/aggiungiPoligonoDaPunto"))).contentType(MediaType.APPLICATION_JSON).content(geoJson))
				.andExpect(status().isNoContent());
	}

	@Test

	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_POLIGONO_TUTTI_COD, Ruoli.VISUALIZZA_LAVORAZIONE_SUOLO_TUTTI_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/aggiuntaDichiarato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void cercaPoligoniDichiaratoDaPuntoInMappaOk() throws Exception {
		Long idLavorazione = 1440071l;
		String pointCoord = "1622206,5080260";

		String geoJson = "{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[" + pointCoord + "]},\"properties\":null}";

		this.mockMvc.perform(
				put(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/aggiungiPoligonoDichiaratoDaPunto"))).contentType(MediaType.APPLICATION_JSON).content(geoJson))
				.andExpect(status().isCreated());
	}

	@Test

	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_POLIGONO_TUTTI_COD, Ruoli.VISUALIZZA_LAVORAZIONE_SUOLO_TUTTI_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/aggiuntaDichiarato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void cercaPoligoniDichiaratoDaPuntoInMappaNotFound() throws Exception {
		Long idLavorazione = 1440071l;
		String pointCoord = "1622204.87,5080259.95";

		String geoJson = "{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[" + pointCoord + "]},\"properties\":null}";

		this.mockMvc.perform(
				put(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/aggiungiPoligonoDichiaratoDaPunto"))).contentType(MediaType.APPLICATION_JSON).content(geoJson))
				.andExpect(status().isNoContent());
	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_POLIGONO_TUTTI_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/aggiuntaDichiarato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void cercaPoligoniDichiaratoDaPuntoInMappaGiaAssociatoERimuovi() throws Exception {
		Long idLavorazione = 1440071l;
		String pointCoord = "1622206,5080260";

		String geoJson = "{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[" + pointCoord + "]},\"properties\":null}";

		this.mockMvc.perform(
				put(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/aggiungiPoligonoDichiaratoDaPunto"))).contentType(MediaType.APPLICATION_JSON).content(geoJson))
				.andExpect(status().isCreated());
	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD, Ruoli.VISUALIZZA_LAVORAZIONE_SUOLO_TUTTI_COD })
	@Sql(statements = "Insert into A4ST_LAVORAZIONE_SUOLO (ID,VERSIONE,UTENTE,STATO,DATA_INIZIO_LAVORAZIONE,DATA_FINE_LAVORAZIONE,TITOLO,SOPRALLUOGO,NOTE,DATA_ULTIMA_MODIFICA) "
			+ "		   values ('1004','0','BACKOFFICE','CHIUSA',  to_date('03/03/2021','DD/MM/YYYY'),null,'Titolo',null,'Notes',to_date('03/03/2021 15:00:00','DD/MM/YYYY HH24:MI:SS'));")
	void associaPoligoni_406_INVALID_ARGUMENT_EXCEPTION() throws Exception {
		Long idLavorazione = 1004L;

		this.mockMvc.perform(post(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/associaSuoloDaDichiarato"))).contentType(MediaType.APPLICATION_JSON)
				.content("{\"versione\": 0}")).andExpect(status().isNotAcceptable());
	}

	@Test
	@WithMockUser(username = "ALTROUTENTE", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/associazioneSuoloDaDichiarato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void associaPoligoniNonProprietarioLavorazione() throws Exception {
		when(clock.now()).thenReturn(LocalDateTime.of(2021, 04, 16, 00, 00));
		Long idLavorazione = 2L;

		this.mockMvc.perform(post(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/associaSuoloDaDichiarato"))).contentType(MediaType.APPLICATION_JSON)
				.content("{\"versione\": 0}")).andExpect(status().isNotAcceptable());

	}

	@Test
	@WithMockUser(username = "ALTROUTENTE", roles = { Ruoli.VISUALIZZA_LAVORAZIONE_SUOLO_TUTTI_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/associazioneSuoloDaDichiarato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void associaPoligoniNonAbilitato() throws Exception {
		when(clock.now()).thenReturn(LocalDateTime.of(2021, 04, 16, 00, 00));
		Long idLavorazione = 2L;

		this.mockMvc.perform(post(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/associaSuoloDaDichiarato"))).contentType(MediaType.APPLICATION_JSON)
				.content("{\"versione\": 0}")).andExpect(status().isForbidden());

	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/associazioneSuoloDaDichiarato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void associaPoligoniLavorazioneInesistente() throws Exception {
		when(clock.now()).thenReturn(LocalDateTime.of(2021, 04, 16, 00, 00));
		Long idLavorazione = 4L;

		this.mockMvc.perform(post(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/associaSuoloDaDichiarato"))).contentType(MediaType.APPLICATION_JSON)
				.content("{\"versione\": 0}")).andExpect(status().isNotAcceptable());

	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.VISUALIZZA_LAVORAZIONE_SUOLO_TUTTI_COD, Ruoli.EDIT_LAVORAZIONE_SUOLO_COD, Ruoli.VISUALIZZA_POLIGONO_TUTTI_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/associazioneSuoloDaDichiarato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void cicloVitaAssociaPoligoniAndCancellaLavorazione() throws Exception {
		Long idLavorazione = 2L;

		this.mockMvc
				.perform(post(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/associaSuoloDaDichiarato"))).contentType(MediaType.APPLICATION_JSON)
						.content("{\"versione\": 0}"))
				.andExpect(status().isOk())
				// controllo suoli scartati
				.andExpect(content().json(
						"[{\"suoloVigente\":{\"idSuoloVigente\":3,\"idLavorazione\":1,\"utente\":\"BACKOFFICE\", \"dataUltimaLavorazione\":\"2021-03-30T00:00:00\"},\"idSuoloDichiarato\":[11]}]"));
		// controllo suoli associati
		this.mockMvc.perform(get(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione)).concat("/suolo")).param("numeroElementiPagina", "10").param("pagina", "0")
				.param("proprieta", "id").param("ordine", "DESC")).andExpect(status().isOk()).andExpect(jsonPath("$.count", is(1))).andExpect(jsonPath("$.risultati[0].id", is(1)));

		// cancello la lavorazione
		mockMvc.perform(delete(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione)))).andExpect(status().isOk());

		// verifico effetto cancellazione
		Optional<SuoloModel> suolo = suoloDao.findById(1L);
		assertThat(suolo.isPresent()).isTrue();
		assertThat(suolo.get().getIdLavorazioneInCorso()).isNull();
		suolo = suoloDao.findById(3L);
		assertThat(suolo.isPresent()).isTrue();
		assertThat(suolo.get().getIdLavorazioneInCorso()).isNotNull();
		assertThat(suolo.get().getIdLavorazioneInCorso().getId()).isEqualTo(1L);
		Arrays.asList(11L, 12L, 13L, 14L).stream().forEach(idSuoloDich -> {
			Optional<SuoloDichiaratoModel> suoloD = suoloDichiaratoDao.findById(idSuoloDich);
			assertThat(suoloD.isPresent()).isTrue();
			assertThat(suoloD.get().getLavorazioneSuolo()).isNull();

		});

	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.VISUALIZZA_LAVORAZIONE_SUOLO_TUTTI_COD, Ruoli.EDIT_LAVORAZIONE_SUOLO_COD, Ruoli.VISUALIZZA_POLIGONO_TUTTI_COD,
			Ruoli.VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_TUTTI_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/associazioneSuoloDaDichiarato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void avvioLavorazioneImpattaRichiesteCollegate() throws Exception {
		Long idLavorazione = 2L;
		mockFmeCreaAreaDiLavoro(idLavorazione, HttpStatus.OK, creaAreaDiLavoro);
		mockMvc.perform(post(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/workspace"))).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

		Long idRichiesta = 1L;
		Long idRichiesta2 = 2L;

		mockMvc.perform(get(ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(idRichiesta)))).andExpect(status().isOk()).andExpect(jsonPath("id").value(idRichiesta))

				.andExpect(jsonPath("$.stato", is(StatoRichiestaModificaSuolo.IN_LAVORAZIONE.name())));

		mockMvc.perform(get(ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(idRichiesta2)))).andExpect(status().isOk()).andExpect(jsonPath("id").value(idRichiesta2))

				.andExpect(jsonPath("$.stato", is(StatoRichiestaModificaSuolo.IN_LAVORAZIONE.name())));

		// cancello la lavorazione
		mockMvc.perform(delete(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione)))).andExpect(status().isOk());

		mockMvc.perform(get(ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(idRichiesta)))).andExpect(status().isOk()).andExpect(jsonPath("id").value(idRichiesta))

				.andExpect(jsonPath("$.stato", is(StatoRichiestaModificaSuolo.LAVORABILE.name())));

		mockMvc.perform(get(ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(idRichiesta2)))).andExpect(status().isOk()).andExpect(jsonPath("id").value(idRichiesta2))

				.andExpect(jsonPath("$.stato", is(StatoRichiestaModificaSuolo.IN_LAVORAZIONE.name())));
	}

	@Test
	@WithMockUser(username = "BACKOFFICE")
	void getCodificheSuolo() throws Exception {

		MvcResult result = mockMvc.perform(get(ApiUrls.LAVORAZIONE_SUOLO.concat("/codificheSuolo"))).andExpect(status().isOk()).andReturn();

		String content = result.getResponse().getContentAsString();

		CodificheSuoloDto codificheSuoloDto = objectMapper.readValue(content, new TypeReference<CodificheSuoloDto>() {
		});

		assertNotNull(codificheSuoloDto);

		assertNotNull(codificheSuoloDto.getCodUsoSuolo());

		assertNotNull(codificheSuoloDto.getStatoColtSuolo());

		CodDescCodificaSuoloDto codDescCodUsoDto = codificheSuoloDto.getCodUsoSuolo().get(0);

		CodDescCodificaSuoloDto codDescStatoColtDto = codificheSuoloDto.getStatoColtSuolo().get(0);

		// Verify codDescCodUsoDto not null
		assertNotNull(codDescCodUsoDto.getCodice());
		assertNotNull(codDescCodUsoDto.getDescrizione());

		// Verify codDescCodUsoDto value
		assertEquals("020", codDescCodUsoDto.getCodice());
		assertEquals("ALTRI CEREALI DEPAUPERANTI (A PAGLIA)", codDescCodUsoDto.getDescrizione());

		// Verify codDescStatoColtDto not null
		assertNotNull(codDescStatoColtDto.getCodice());
		assertNotNull(codDescStatoColtDto.getDescrizione());

		// Verify codDescStatoColtDto value
		assertEquals("0", codDescStatoColtDto.getCodice());
		assertEquals("GENERICO", codDescStatoColtDto.getDescrizione());

	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	@Sql(statements = "insert into A4ST_LAVORAZIONE_SUOLO (ID,VERSIONE, UTENTE, STATO, DATA_INIZIO_LAVORAZIONE, DATA_FINE_LAVORAZIONE, TITOLO, data_ultima_modifica) values (26082021, 1,'BACKOFFICE', 'IN_CORSO', to_date('26/08/2021 00:00','DD/MM/YYYY HH24:MI'), null, null, to_date('26/08/2021 00:00','DD/MM/YYYY HH24:MI'))", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "delete from A4ST_LAVORAZIONE_SUOLO where id=26082021", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void cambioStatoLavorazioneInModifica() throws Exception {

		Long idLavorazione = 26082021L;

		mockFmeCreaBuchi(idLavorazione, HttpStatus.OK, "{\"status\":\"SUCCESS\"}", creaBuchi);

		mockMvc.perform(put(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/lavorazioneInModifica/?statoLavorazioneInModifica=IN_MODIFICA")))
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());

		Optional<LavorazioneSuoloModel> optLavorazione = lavorazioneSuoloDao.findById(idLavorazione);
		if (optLavorazione.isPresent()) {
			assertEquals(StatoLavorazioneSuolo.IN_MODIFICA, optLavorazione.get().getStato());
		}

	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	@Sql(statements = "insert into A4ST_LAVORAZIONE_SUOLO (ID,VERSIONE, UTENTE, STATO, DATA_INIZIO_LAVORAZIONE, DATA_FINE_LAVORAZIONE, TITOLO, data_ultima_modifica) values (26082021, 1,'BACKOFFICE', 'IN_CREAZIONE', to_date('26/08/2021 00:00','DD/MM/YYYY HH24:MI'), null, null, to_date('26/08/2021 00:00','DD/MM/YYYY HH24:MI'))", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "delete from A4ST_LAVORAZIONE_SUOLO where id=26082021", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void cambioStatoLavorazioneInModifica_da_IN_CREAZIONE_a_IN_MODIFICA_406NOT_ACCEPTABLE() throws Exception {

		Long idLavorazione = 26082021L;

		mockFmeCreaBuchi(idLavorazione, HttpStatus.OK, "{\"status\":\"SUCCESS\"}", creaBuchi);

		mockMvc.perform(put(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/lavorazioneInModifica/?statoLavorazioneInModifica=IN_MODIFICA")))
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isNotAcceptable());

	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	@Sql(statements = "insert into A4ST_LAVORAZIONE_SUOLO (ID,VERSIONE, UTENTE, STATO, DATA_INIZIO_LAVORAZIONE, DATA_FINE_LAVORAZIONE, TITOLO, data_ultima_modifica) values (26082021, 1,'BACKOFFICE', 'IN_CORSO', to_date('26/08/2021 00:00','DD/MM/YYYY HH24:MI'), null, null, to_date('26/08/2021 00:00','DD/MM/YYYY HH24:MI'))", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "delete from A4ST_LAVORAZIONE_SUOLO where id=26082021", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void cambioStatoLavorazioneInModifica_da_IN_CREAZIONE_a_statoDiversoDa_IN_MODIFICA_406NOTACCEPTABLE() throws Exception {

		Long idLavorazione = 26082021L;

		mockFmeCreaBuchi(idLavorazione, HttpStatus.OK, "{\"status\":\"SUCCESS\"}", creaBuchi);

		mockMvc.perform(put(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/lavorazioneInModifica/?statoLavorazioneInModifica=CHIUSA")))
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isNotAcceptable());

	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	void cambioStatoLavorazioneInModificaInvalidArgumentException() throws Exception {

		Long idLavorazione = 26082021L;
		mockMvc.perform(put(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/lavorazioneInModifica/?statoLavorazioneInModifica=IN_MODIFICA")))
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isNotAcceptable());

	}

	@Test
	@WithMockUser(username = "ITE3277@ext.itad.infotn.it", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/riprendiLavorazione.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void riprendiLavorazione() throws Exception {

		Long idLavorazione = 1483573L;

		LavorazioneSuoloModel lavorazioneInModifica = lavorazioneSuoloDao.findById(idLavorazione).get();
		List<WorkspaceLavSuoloModel> workspaceLavorazioneInModifica = lavorazioneInModifica.getListaLavorazioneWorkspaceModel();
		List<SuoloModel> suoloInModifica = lavorazioneInModifica.getListaSuoloInCorsoModel();
		int countWorkspaceLavorazioneInModifica = workspaceLavorazioneInModifica.size();
		int countSuoloLavorazioneInModifica = suoloInModifica.size();

		mockFmeRiprendiLavorazione(idLavorazione, HttpStatus.OK, calcolarePoligoniSuoloNonCopertiDaWorkspaceECreareNuoviPoligoniDiWorkspaceFme);

		mockFmeCreaAreaDiLavoro(idLavorazione, HttpStatus.OK, creaAreaDiLavoro);
		mockMvc.perform(put(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/riprendiLavorazione"))).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk());

		// Verifica che i poligoni di workspace siano stati dissolti
		LavorazioneSuoloModel lavorazioneInCorso = lavorazioneSuoloDao.findById(idLavorazione).get();
		assertEquals(StatoLavorazioneSuolo.IN_CORSO, lavorazioneInCorso.getStato());

		List<WorkspaceLavSuoloModel> workspaceLavorazioneInCorso = lavorazioneInCorso.getListaLavorazioneWorkspaceModel();
		int countWorkspaceLavorazioneInCorso = workspaceLavorazioneInCorso.size();

		assertEquals(3, countWorkspaceLavorazioneInModifica);
		assertEquals(4, countWorkspaceLavorazioneInCorso);

		List<SuoloModel> suoloInCorso = lavorazioneInCorso.getListaSuoloInCorsoModel();
		int countSuoloLavorazioneInCorso = suoloInCorso.size();

		assertEquals(4, countSuoloLavorazioneInModifica);
		assertEquals(4, countSuoloLavorazioneInCorso);

	}

	@Test
	@WithMockUser(username = "utente_sconosciuto", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/riprendiLavorazione.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void riprendiLavorazione204() throws Exception {

		Long idLavorazione = 1483573L;

		mockFmeCreaAreaDiLavoro(idLavorazione, HttpStatus.OK, creaAreaDiLavoro);
		mockMvc.perform(put(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/riprendiLavorazione"))).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isNoContent());

	}

	@Test
	@WithMockUser(username = "ITE3277@ext.itad.infotn.it", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/riprendiLavorazione.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void riprendiLavorazioneFmeNotAcceptable() throws Exception {

		Long idLavorazione = 1483573L;

		mockFmeCreaAreaDiLavoro(idLavorazione, HttpStatus.OK, creaAreaDiLavoro);
		mockFmeRiprendiLavorazione(idLavorazione, HttpStatus.BAD_REQUEST, calcolarePoligoniSuoloNonCopertiDaWorkspaceECreareNuoviPoligoniDiWorkspaceFme);

		mockMvc.perform(put(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/riprendiLavorazione"))).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isNotAcceptable());

	}

	@Test
	@WithMockUser(username = "ITE3277@ext.itad.infotn.it", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/riprendiLavorazione.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "update A4ST_LAVORAZIONE_SUOLO set STATO='IN_CORSO' WHERE ID=1483573", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void riprendiLavorazione_LavorazioneConStatoDiversoDa_IN_MODIFICA() throws Exception {

		Long idLavorazione = 1483573L;

		mockFmeRiprendiLavorazione(idLavorazione, HttpStatus.BAD_REQUEST, calcolarePoligoniSuoloNonCopertiDaWorkspaceECreareNuoviPoligoniDiWorkspaceFme);
		mockFmeCreaAreaDiLavoro(idLavorazione, HttpStatus.OK, creaAreaDiLavoro);
		mockMvc.perform(put(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/riprendiLavorazione"))).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isNotAcceptable());

	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	@Sql(statements = "insert into A4ST_LAVORAZIONE_SUOLO (ID,VERSIONE, UTENTE, STATO, DATA_INIZIO_LAVORAZIONE, DATA_FINE_LAVORAZIONE, TITOLO, data_ultima_modifica) values (26082021, 1,'BACKOFFICE', 'IN_MODIFICA', to_date('26/08/2021 00:00','DD/MM/YYYY HH24:MI'), null, null, to_date('26/08/2021 00:00','DD/MM/YYYY HH24:MI'))", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "delete from A4ST_LAVORAZIONE_SUOLO where id=26082021", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void riprendiLavorazioneErroreDiValidazione() throws Exception {

		Long idLavorazione = 26082021L;
		mockFmeCreaAreaDiLavoro(idLavorazione, HttpStatus.OK, creaAreaDiLavoro);
		mockMvc.perform(put(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/riprendiLavorazione"))).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isNotAcceptable());

	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	@Sql(statements = "insert into A4ST_LAVORAZIONE_SUOLO (ID,VERSIONE, UTENTE, STATO, DATA_INIZIO_LAVORAZIONE, DATA_FINE_LAVORAZIONE, TITOLO, data_ultima_modifica) values (26082021, 1,'BACKOFFICE', 'IN_CORSO', to_date('26/08/2021 00:00','DD/MM/YYYY HH24:MI'), null, null, to_date('26/08/2021 00:00','DD/MM/YYYY HH24:MI'))", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "delete from A4ST_LAVORAZIONE_SUOLO where id=26082021", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void ritagliaWorkspaceSuADL() throws Exception {

		Long idLavorazione = 26082021L;
		mockFmeRitagliaWorkspaceSuAreaDiLavoro(idLavorazione, HttpStatus.OK, ritagliaWorkspaceSuAreaDiLavoro);
		mockMvc.perform(post(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/ritagliasuADL"))).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
	@Sql(statements = "insert into A4ST_LAVORAZIONE_SUOLO (ID,VERSIONE, UTENTE, STATO, DATA_INIZIO_LAVORAZIONE, DATA_FINE_LAVORAZIONE, TITOLO, data_ultima_modifica) values (26082021, 1,'BACKOFFICE', 'IN_CREAZIONE', to_date('26/08/2021 00:00','DD/MM/YYYY HH24:MI'), null, null, to_date('26/08/2021 00:00','DD/MM/YYYY HH24:MI'))", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "delete from A4ST_LAVORAZIONE_SUOLO where id=26082021", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void ritagliaWorkspaceSuADLConStatoDiversoDa_IN_CORSO() throws Exception {

		Long idLavorazione = 26082021L;
		mockFmeRitagliaWorkspaceSuAreaDiLavoro(idLavorazione, HttpStatus.OK, ritagliaWorkspaceSuAreaDiLavoro);
		mockMvc.perform(post(ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione).concat("/ritagliasuADL"))).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotAcceptable());

	}

	// Dato un ID_LAVORAZIONE e un TIPO_JOB_FME verifica lo stato del job in FME e restituisce una lavorazione aggiornata
	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.VISUALIZZA_LAVORAZIONE_SUOLO_TUTTI_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/refreshStatoJobFME.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void refreshStatoJobFME() throws Exception {

		Long idLavorazione = 1511387L;
		String tipoJobFme = "CONSOLIDAMENTO_AGS";

		mockFmeServerJobStatus(idLavorazione, HttpStatus.OK);

		mockMvc.perform(get(
				ApiUrls.LAVORAZIONE_SUOLO.concat("/").concat(String.valueOf(idLavorazione)).concat("/").concat("refreshStatoJobFME").concat("?").concat("tipoJobFme").concat("=").concat(tipoJobFme)))
				.andExpect(status().isOk()).andReturn();

	}

	private void mockFmeRiprendiLavorazione(Long idLavorazione, HttpStatus status, String nomeProcedure) throws URISyntaxException {

		doReturn(aggiornaWorkspace(idLavorazione, status)).when(utilsFme).callProcedureFme(idLavorazione, nomeProcedure);

	}

	private ResponseEntity<String> aggiornaWorkspace(Long idLavorazione, HttpStatus status) {
		ResponseEntity<String> mockResponse = new ResponseEntity<String>(status);

		LavorazioneSuoloModel lavorazione = lavorazioneSuoloDao.findById(idLavorazione).get();

		WorkspaceLavSuoloModel workspaceAggiuntoDaFme = new WorkspaceLavSuoloModel();
		workspaceAggiuntoDaFme.setCodUsoSuoloWorkspaceLavSuolo(usoSuoloDao.findByCodUsoSuolo("666"));
		workspaceAggiuntoDaFme.setStatoColtWorkspaceLavSuolo(statoColtDao.findByStatoColt("7"));
		workspaceAggiuntoDaFme.setIdGridWorkspace(grigliaSuoloDao.findById(272L).get());
		workspaceAggiuntoDaFme.setShape(GisUtils.getGeometry(
				"POLYGON ((681793.6896 5098492.1332, 681802.9954 5098492.2879, 681801.3182 5098584.8062, 681794.2249 5098585.1969, 681765.0559 5098586.8034, 681766.8361 5098491.6812, 681791.5575 5098492.0973, 681793.6896 5098492.1332))"));
		workspaceAggiuntoDaFme.setArea(3396.079074);
		workspaceAggiuntoDaFme.setIdLavorazioneWorkspaceLavSuolo(lavorazione);
		workspaceLavorazioneDao.saveAndFlush(workspaceAggiuntoDaFme);
		lavorazione.addWorkspaceLavSuoloModel(workspaceAggiuntoDaFme);

		return mockResponse;
	}

	private void mockFmeCreaAreaDiLavoro(Long idLavorazione, HttpStatus status, String nomeProcedura) throws URISyntaxException {
		ResponseEntity<String> mockResponse = new ResponseEntity<String>(status);
		doReturn(mockResponse).when(utilsFme).callProcedureFme(idLavorazione, nomeProcedura);

	}

	private void mockFmeCreaBuchi(Long idLavorazione, HttpStatus status, String body, String nomeProcedura) throws URISyntaxException {
		ResponseEntity<String> mockResponse = new ResponseEntity<String>(body, status);

		doReturn(mockResponse).when(utilsFme).callProcedureFme(idLavorazione, nomeProcedura);
	}

	private void mockFmeRitagliaWorkspaceSuAreaDiLavoro(Long idLavorazione, HttpStatus status, String nomeProcedura) throws URISyntaxException {
		ResponseEntity<String> mockResponse = new ResponseEntity<String>(status);
		doReturn(mockResponse).when(utilsFme).callProcedureFme(idLavorazione, nomeProcedura);

	}

	private void mockFmeServerJobStatus(Long idLavorazione, HttpStatus status) throws URISyntaxException, JsonProcessingException {
		ResponseEntity<String> mockResponse = new ResponseEntity<String>("{\"id\":6031, \"status\":\"JOB_FAILURE\"}", status);
		doReturn(mockResponse).when(utilsFme).checkJobStatusFme(Mockito.anyLong());
	}

	private void mockFmeVerificaIntersezionePascoli(Long idLavorazione, Integer annoUpas, HttpStatus status, String nomeProcedura) throws URISyntaxException {

		ResponseEntity<String> mockResponse = new ResponseEntity<String>("", HttpStatus.BAD_REQUEST);
		if (status.equals(HttpStatus.PRECONDITION_FAILED))
			mockResponse = new ResponseEntity<String>("[{\"area_suolo_sum\":30.857546,\"area_dentro_upas_sum\":30.857546,\"area_dentro_upas_max\":30.857546,\"lavorazione_in_upas\":1}]", status);
		if (status.equals(HttpStatus.INTERNAL_SERVER_ERROR))
			mockResponse = new ResponseEntity<String>("", HttpStatus.INTERNAL_SERVER_ERROR);

		Map<String, String> params = new HashMap<>();
		params.put("idLavorazione", String.valueOf(idLavorazione));
		params.put("annoUpas", String.valueOf(annoUpas));

		doReturn(mockResponse).when(utilsFme).callProcedureFmeDataStreaming(verificaIntersezioneUpas, params);
	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD, Ruoli.VISUALIZZA_LAVORAZIONE_SUOLO_TUTTI_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/associazioneSuoloDaDichiarato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void ricercaLavorazioniconFiltro() throws Exception {

		RicercaLavorazioniSuoloFilter filter = new RicercaLavorazioniSuoloFilter();
		filter.setTitolo("Titolo");
		filter.setIdLavorazione(2L);

		MvcResult result = this.mockMvc.perform(get(ApiUrls.LAVORAZIONE_SUOLO.concat("/")).param("idLavorazione", String.valueOf(filter.getIdLavorazione())).param("numeroElementiPagina", "10")
				.param("pagina", "0").param("ordine", "DESC")).andExpect(status().isOk()).andReturn();

		String content = result.getResponse().getContentAsString();

		RisultatiPaginati<LavorazioneSuoloDto> risPaginati = objectMapper.readValue(content, new TypeReference<RisultatiPaginati<LavorazioneSuoloDto>>() {
		});
		risPaginati.getRisultati().forEach(lav -> {
			assertNotEquals(lav.getId(), 144012L);
			assertNotNull(lav.getDataUltimaModifica());
		});

	}

}
