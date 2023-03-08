package it.tndigitale.a4g.richiestamodificasuolo;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.h2.tools.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento.Ordine;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4g.richiestamodificasuolo.api.ApiUrls;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.ProfiloUtente;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.RichiestaModificaSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoLavorazioneSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoRichiestaModificaSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloDichiaratoLavorazioneModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.TagDichiarato;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.TagRilevato;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.RichiestaModificaSuoloDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.SuoloDichiaratoLavorazioneDao;
import it.tndigitale.a4g.richiestamodificasuolo.dto.filter.SuoloDichiaratoLavorazioneFilter;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.SuoloDichiaratoLavorazioneDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.mapper.SuoloDichiaratoLavorazioneMapper;
import it.tndigitale.a4g.richiestamodificasuolo.dto.richiestaModificaSuolo.MessaggioRichiestaDto;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SuoloDichiaratoApplicationTest {

	static Server h2WebServer;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private SuoloDichiaratoLavorazioneDao suoloDichiaratoLavorazioneDao;

	@Autowired
	private RichiestaModificaSuoloDao richiestaModificaSuoloDao;

	@Autowired
	private SuoloDichiaratoLavorazioneMapper suoloDichiaratoLavorazioneMapper;

	private Integer annoCampagnaRicerca = 2022;

	@BeforeAll
	public static void initTest() throws SQLException {

		h2WebServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082");

		h2WebServer.start();
	}

	@AfterAll
	public static void stopServer() throws SQLException {
		h2WebServer.stop();
	}

	// Ricercare SuoloDichiarato per ID_RICHIESTA e STATO_RICHIESTA=LAVORABILE: OK se posso accedere al servizio e restituisce la richiesta
	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_POLIGONO_TUTTI_COD })
	void getSuoloDichiarato() throws Exception {

		Long idRichiesta = 136449L;

		SuoloDichiaratoLavorazioneFilter filter = new SuoloDichiaratoLavorazioneFilter();
		filter.setStatoRichiesta(StatoRichiestaModificaSuolo.LAVORABILE.toString());

		System.out.println(filter.toString());

		Paginazione paginazione = new Paginazione(10, 0);

		Ordinamento ordinamento = new Ordinamento();
		ordinamento.setOrdine(Ordine.DESC);
		ordinamento.setProprieta("id");

		MvcResult result = mockMvc.perform(get(ApiUrls.SUOLO_DICHIARATO.concat("?statoRichiesta=")
				.concat(String.valueOf(filter.getStatoRichiesta()).concat("&idRichiesta=").concat(String.valueOf(idRichiesta)).concat("&numeroElementiPagina=")
						.concat(String.valueOf(paginazione.getNumeroElementiPagina())).concat("&pagina=").concat(String.valueOf(paginazione.getPagina())).concat("&valid=true"))
				.concat("&proprieta=").concat(ordinamento.getProprieta()).concat("&ordine=").concat(ordinamento.getOrdine().toString()))).andExpect(status().isOk()).andReturn();

		String content = result.getResponse().getContentAsString();

		assertTrue(content.contains("extent"));
		assertEquals(content,
				"{\"count\":6,\"risultati\":[{\"id\":136464,\"idRichiesta\":136449,\"statoRichiesta\":\"LAVORABILE\",\"aziendaAgricola\":{\"cuaa\":\"02167970223\",\"ragioneSociale\":null},\"campagna\":2022,\"codSezione\":\"D516\",\"tipoSuoloDichiarato\":\"BO\",\"tipoSuoloRilevato\":\"BO\",\"areaOri\":1160.074786,\"codiRileDichiarato\":\"638\",\"descRileDichiarato\":\"PRATO PERMANENTE (SENZA TARA)\",\"codiProdRileDichiarato\":\"000\",\"descProdRileDichiarato\":null,\"codiRilePrevalenteDich\":\"638\",\"descRilePrevalenteDich\":\"PRATO PERMANENTE (SENZA TARA)\",\"codiRileRilevato\":\"666\",\"descRileRilevato\":\"SEMINATIVI\",\"codiProdRileRilevato\":\"000\",\"descProdRileRilevato\":null,\"codiRilePrevalenteRil\":\"666\",\"descRilePrevalenteRil\":\"SEMINATIVI\",\"esito\":\"DA_LAVORARE\",\"idLavorazione\":null,\"idPoligonoDichiarato\":null,\"numeroMessaggi\":0,\"numeroDocumenti\":0,\"visible\":true,\"extent\":[671653.336461323,5119680.76715999,671729.374636155,5119735.9247515],\"utenteLavorazione\":null,\"statoLavorazione\":null,\"nuovaModificaBo\":false,\"nuovaModificaCaa\":false,\"visibileInOrtofoto\":null,\"tipoInterventoColturale\":null,\"interventoInizio\":null,\"interventoFine\":null},{\"id\":136462,\"idRichiesta\":136449,\"statoRichiesta\":\"LAVORABILE\",\"aziendaAgricola\":{\"cuaa\":\"02167970223\",\"ragioneSociale\":null},\"campagna\":2022,\"codSezione\":\"D516\",\"tipoSuoloDichiarato\":\"BO\",\"tipoSuoloRilevato\":\"BO\",\"areaOri\":12771.769668,\"codiRileDichiarato\":\"638\",\"descRileDichiarato\":\"PRATO PERMANENTE (SENZA TARA)\",\"codiProdRileDichiarato\":\"000\",\"descProdRileDichiarato\":null,\"codiRilePrevalenteDich\":\"638\",\"descRilePrevalenteDich\":\"PRATO PERMANENTE (SENZA TARA)\",\"codiRileRilevato\":\"650\",\"descRileRilevato\":\"BOSCHI\",\"codiProdRileRilevato\":\"000\",\"descProdRileRilevato\":null,\"codiRilePrevalenteRil\":\"650\",\"descRilePrevalenteRil\":\"BOSCHI\",\"esito\":\"DA_LAVORARE\",\"idLavorazione\":null,\"idPoligonoDichiarato\":null,\"numeroMessaggi\":0,\"numeroDocumenti\":0,\"visible\":true,\"extent\":[669353.304068925,5120232.98431703,669514.641050304,5120364.75067734],\"utenteLavorazione\":null,\"statoLavorazione\":null,\"nuovaModificaBo\":false,\"nuovaModificaCaa\":false,\"visibileInOrtofoto\":null,\"tipoInterventoColturale\":null,\"interventoInizio\":null,\"interventoFine\":null},{\"id\":136460,\"idRichiesta\":136449,\"statoRichiesta\":\"LAVORABILE\",\"aziendaAgricola\":{\"cuaa\":\"02167970223\",\"ragioneSociale\":null},\"campagna\":2022,\"codSezione\":\"D516\",\"tipoSuoloDichiarato\":\"BO\",\"tipoSuoloRilevato\":\"BO\",\"areaOri\":11392.369307,\"codiRileDichiarato\":\"650\",\"descRileDichiarato\":\"BOSCHI\",\"codiProdRileDichiarato\":\"000\",\"descProdRileDichiarato\":null,\"codiRilePrevalenteDich\":\"650\",\"descRilePrevalenteDich\":\"BOSCHI\",\"codiRileRilevato\":\"650\",\"descRileRilevato\":\"BOSCHI\",\"codiProdRileRilevato\":\"000\",\"descProdRileRilevato\":null,\"codiRilePrevalenteRil\":\"650\",\"descRilePrevalenteRil\":\"BOSCHI\",\"esito\":\"DA_LAVORARE\",\"idLavorazione\":null,\"idPoligonoDichiarato\":null,\"numeroMessaggi\":0,\"numeroDocumenti\":0,\"visible\":true,\"extent\":[669236.464561688,5120243.82880066,669421.520983408,5120371.90037353],\"utenteLavorazione\":null,\"statoLavorazione\":null,\"nuovaModificaBo\":false,\"nuovaModificaCaa\":false,\"visibileInOrtofoto\":null,\"tipoInterventoColturale\":null,\"interventoInizio\":null,\"interventoFine\":null},{\"id\":136458,\"idRichiesta\":136449,\"statoRichiesta\":\"LAVORABILE\",\"aziendaAgricola\":{\"cuaa\":\"02167970223\",\"ragioneSociale\":null},\"campagna\":2022,\"codSezione\":\"D516\",\"tipoSuoloDichiarato\":\"BO\",\"tipoSuoloRilevato\":\"FRU\",\"areaOri\":8795.670479,\"codiRileDichiarato\":\"638\",\"descRileDichiarato\":\"PRATO PERMANENTE (SENZA TARA)\",\"codiProdRileDichiarato\":\"000\",\"descProdRileDichiarato\":null,\"codiRilePrevalenteDich\":\"638\",\"descRilePrevalenteDich\":\"PRATO PERMANENTE (SENZA TARA)\",\"codiRileRilevato\":\"651\",\"descRileRilevato\":\"COLTIVAZIONI ARBOREE SPECIALIZZATE\",\"codiProdRileRilevato\":\"410\",\"descProdRileRilevato\":\"VITE\",\"codiRilePrevalenteRil\":\"410\",\"descRilePrevalenteRil\":\"VITE\",\"esito\":\"DA_LAVORARE\",\"idLavorazione\":null,\"idPoligonoDichiarato\":null,\"numeroMessaggi\":0,\"numeroDocumenti\":0,\"visible\":true,\"extent\":[672990.504161786,5116104.25021706,673088.228066548,5116293.50706066],\"utenteLavorazione\":null,\"statoLavorazione\":null,\"nuovaModificaBo\":false,\"nuovaModificaCaa\":false,\"visibileInOrtofoto\":null,\"tipoInterventoColturale\":null,\"interventoInizio\":null,\"interventoFine\":null},{\"id\":136456,\"idRichiesta\":136449,\"statoRichiesta\":\"LAVORABILE\",\"aziendaAgricola\":{\"cuaa\":\"02167970223\",\"ragioneSociale\":null},\"campagna\":2022,\"codSezione\":\"D516\",\"tipoSuoloDichiarato\":\"FRU\",\"tipoSuoloRilevato\":\"BO\",\"areaOri\":1558.093995,\"codiRileDichiarato\":\"650\",\"descRileDichiarato\":\"BOSCHI\",\"codiProdRileDichiarato\":\"000\",\"descProdRileDichiarato\":null,\"codiRilePrevalenteDich\":\"650\",\"descRilePrevalenteDich\":\"BOSCHI\",\"codiRileRilevato\":\"650\",\"descRileRilevato\":\"BOSCHI\",\"codiProdRileRilevato\":\"000\",\"descProdRileRilevato\":null,\"codiRilePrevalenteRil\":\"650\",\"descRilePrevalenteRil\":\"BOSCHI\",\"esito\":\"DA_LAVORARE\",\"idLavorazione\":null,\"idPoligonoDichiarato\":null,\"numeroMessaggi\":0,\"numeroDocumenti\":0,\"visible\":true,\"extent\":[669431.286968498,5120339.66359828,669502.312669583,5120394.99050178],\"utenteLavorazione\":null,\"statoLavorazione\":null,\"nuovaModificaBo\":false,\"nuovaModificaCaa\":false,\"visibileInOrtofoto\":null,\"tipoInterventoColturale\":null,\"interventoInizio\":null,\"interventoFine\":null},{\"id\":136454,\"idRichiesta\":136449,\"statoRichiesta\":\"LAVORABILE\",\"aziendaAgricola\":{\"cuaa\":\"02167970223\",\"ragioneSociale\":null},\"campagna\":2022,\"codSezione\":\"D516\",\"tipoSuoloDichiarato\":\"FRU\",\"tipoSuoloRilevato\":\"BO\",\"areaOri\":700.838086,\"codiRileDichiarato\":\"638\",\"descRileDichiarato\":\"PRATO PERMANENTE (SENZA TARA)\",\"codiProdRileDichiarato\":\"000\",\"descProdRileDichiarato\":null,\"codiRilePrevalenteDich\":\"638\",\"descRilePrevalenteDich\":\"PRATO PERMANENTE (SENZA TARA)\",\"codiRileRilevato\":\"666\",\"descRileRilevato\":\"SEMINATIVI\",\"codiProdRileRilevato\":\"000\",\"descProdRileRilevato\":null,\"codiRilePrevalenteRil\":\"666\",\"descRilePrevalenteRil\":\"SEMINATIVI\",\"esito\":\"DA_LAVORARE\",\"idLavorazione\":null,\"idPoligonoDichiarato\":null,\"numeroMessaggi\":3,\"numeroDocumenti\":1,\"visible\":true,\"extent\":[671615.522823267,5119709.54528584,671655.208518708,5119747.97658312],\"utenteLavorazione\":null,\"statoLavorazione\":null,\"nuovaModificaBo\":false,\"nuovaModificaCaa\":false,\"visibileInOrtofoto\":null,\"tipoInterventoColturale\":null,\"interventoInizio\":null,\"interventoFine\":null}]}");
	}

	// Ricercare SuoloDichiarato per ID_RICHIESTA e STATO_RICHIESTA=LAVORABILE: OK se posso accedere al servizio e restituisce la richiesta
	@Test
	@WithMockUser(username = "utente")
	void getSuoloDichiarato403() throws Exception {

		Long idRichiesta = 136449L;

		SuoloDichiaratoLavorazioneFilter filter = new SuoloDichiaratoLavorazioneFilter();
		filter.setStatoRichiesta(StatoRichiestaModificaSuolo.LAVORABILE.toString());

		System.out.println(filter.toString());

		Paginazione paginazione = new Paginazione(10, 0);

		Ordinamento ordinamento = new Ordinamento();
		ordinamento.setOrdine(Ordine.DESC);
		ordinamento.setProprieta("id");

		mockMvc.perform(get(ApiUrls.SUOLO_DICHIARATO.concat("?statoRichiesta=")
				.concat(String.valueOf(filter.getStatoRichiesta()).concat("&idRichiesta=").concat(String.valueOf(idRichiesta)).concat("&numeroElementiPagina=")
						.concat(String.valueOf(paginazione.getNumeroElementiPagina())).concat("&pagina=").concat(String.valueOf(paginazione.getPagina())).concat("&valid=true"))
				.concat("&proprieta=").concat(ordinamento.getProprieta()).concat("&ordine=").concat(ordinamento.getOrdine().toString()))).andExpect(status().isForbidden());

	}

	// Ricercare SuoloDichiarato per ID_RICHIESTA e STATO_RICHIESTA=CONCLUSA
	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_POLIGONO_TUTTI_COD })
	void getSuoloDichiaratoRichiestaConclusa() throws Exception {

		SuoloDichiaratoLavorazioneFilter filter = new SuoloDichiaratoLavorazioneFilter();
		filter.setStatoRichiesta(StatoRichiestaModificaSuolo.CONCLUSA.toString());

		System.out.println(filter.toString());

		Paginazione paginazione = new Paginazione(10, 0);

		Ordinamento ordinamento = new Ordinamento();
		ordinamento.setOrdine(Ordine.DESC);
		ordinamento.setProprieta("id");

		MvcResult result = mockMvc.perform(get(ApiUrls.SUOLO_DICHIARATO.concat("?statoRichiesta=")
				.concat(String.valueOf(filter.getStatoRichiesta()).concat("&numeroElementiPagina=").concat(String.valueOf(paginazione.getNumeroElementiPagina())).concat("&pagina=")
						.concat(String.valueOf(paginazione.getPagina())).concat("&valid=true"))
				.concat("&proprieta=").concat(ordinamento.getProprieta()).concat("&ordine=").concat(ordinamento.getOrdine().toString()))).andExpect(status().isOk()).andReturn();
		String content = result.getResponse().getContentAsString();

		assertEquals("{\"count\":0,\"risultati\":[]}", content);
	}

	// Ricercare SuoloDichiarato per ID_RICHIESTA e STATO_RICHIESTA=LAVORABILE: OK se posso accedere al servizio e restituisce una lista vuota poiche ID_RICHIESTA NON ESISTE
	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_POLIGONO_TUTTI_COD })
	void getSuoloDichiaratoResponseEmpty() throws Exception {

		Long idRichiesta = 1364741111L;

		SuoloDichiaratoLavorazioneFilter filter = new SuoloDichiaratoLavorazioneFilter();
		filter.setStatoRichiesta(StatoRichiestaModificaSuolo.LAVORABILE.toString());
		filter.setIdRichiesta(idRichiesta);

		Paginazione paginazione = new Paginazione(10, 0);

		Ordinamento ordinamento = new Ordinamento();
		ordinamento.setOrdine(Ordine.DESC);
		ordinamento.setProprieta("idRichiesta");

		MvcResult result = mockMvc.perform(get(ApiUrls.SUOLO_DICHIARATO.concat("?stato=")
				.concat(String.valueOf(filter.getStatoRichiesta()).concat("&idRichiesta=").concat(String.valueOf(filter.getIdRichiesta())).concat("&numeroElementiPagina=")
						.concat(String.valueOf(paginazione.getNumeroElementiPagina())).concat("&pagina=").concat(String.valueOf(paginazione.getPagina())).concat("&valid=true"))
				.concat("&proprieta=").concat(ordinamento.getProprieta()).concat("&ordine=").concat(ordinamento.getOrdine().toString()))).andExpect(status().isOk()).andReturn();

		String content = result.getResponse().getContentAsString();

		assertEquals("{\"count\":0,\"risultati\":[]}", content);
	}

	// Ricercare SuoloDichiarato associati alla lavorazione per ID_LAVORAZIONE: OK se posso accedere al servizio e restituisce i suoli associati
	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_POLIGONO_TUTTI_COD })
	void ricercaSuoloDichiaratoInseritiInUnaLavorazione() throws Exception {
		SuoloDichiaratoLavorazioneFilter filter = new SuoloDichiaratoLavorazioneFilter();
		filter.setStatoLavorazione(StatoLavorazioneSuolo.IN_CREAZIONE.toString());
		filter.setIdLavorazione(137869L);
		Paginazione paginazione = new Paginazione();
		paginazione.setNumeroElementiPagina(1);
		paginazione.setPagina(0);

		Ordinamento ordinamento = new Ordinamento();
		ordinamento.setOrdine(Ordine.DESC);
		ordinamento.setProprieta("idRichiesta");

		MvcResult result = mockMvc.perform(get(ApiUrls.SUOLO_DICHIARATO.concat("?statoLavorazione=").concat(filter.getStatoLavorazione()).concat("&idLavorazione=")
				.concat(String.valueOf(filter.getIdLavorazione())).concat("&numeroElementiPagina=").concat(String.valueOf(paginazione.getNumeroElementiPagina())).concat("&pagina=")
				.concat(String.valueOf(paginazione.getPagina())).concat("&valid=true&proprieta=").concat(ordinamento.getProprieta()).concat("&ordine=").concat(ordinamento.getOrdine().toString())))
				.andExpect(status().isOk()).andReturn();

		String content = result.getResponse().getContentAsString();

		RisultatiPaginati<SuoloDichiaratoLavorazioneDto> listDto = objectMapper.readValue(content, new TypeReference<RisultatiPaginati<SuoloDichiaratoLavorazioneDto>>() {
		});

		SuoloDichiaratoLavorazioneDto dto = listDto.getRisultati().get(0);

		assertEquals(136463L, dto.getId());
		assertEquals(2528L, dto.getIdRichiesta());
		assertEquals("APERTA", dto.getStatoRichiesta());
		assertEquals(Long.valueOf(2022), dto.getCampagna());
		assertEquals("D516", dto.getCodSezione());
		assertEquals(TagDichiarato.BO, dto.getTipoSuoloDichiarato());
		assertEquals(TagRilevato.BO, dto.getTipoSuoloRilevato());
		assertEquals(null, dto.getExtent());
	}

	// PROFILO BO
	// Ricercare SuoloDichiarato NON ancora associati a nessuna lavorazione: OK se posso accedere al servizio e restituisce i suoli secondo i filtri passati
	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_POLIGONO_TUTTI_COD })
	void ricercaSuoloDichiaratoDaInserireInLavorazione() throws Exception {
		SuoloDichiaratoLavorazioneFilter filter = new SuoloDichiaratoLavorazioneFilter();
		filter.setTipoSuoloDichiarato(TagDichiarato.FRU.toString());
		filter.setTipoSuoloRilevato(TagRilevato.BO.toString());
		filter.setCampagna(Long.valueOf(annoCampagnaRicerca));
		filter.setIdRichiesta(136449L);
		filter.setCuaa("02167970223");

		Paginazione paginazione = new Paginazione();
		paginazione.setNumeroElementiPagina(1);
		paginazione.setPagina(0);

		Ordinamento ordinamento = new Ordinamento();
		ordinamento.setOrdine(Ordine.DESC);
		ordinamento.setProprieta("id");

		mockMvc.perform(get(ApiUrls.SUOLO_DICHIARATO.concat("?tipoSuoloDichiarato=").concat(filter.getTipoSuoloDichiarato()).concat("&tipoSuoloRilevato=").concat(filter.getTipoSuoloRilevato())
				.concat("&campagna=").concat(String.valueOf(filter.getCampagna())).concat("&idRichiesta=").concat(String.valueOf(filter.getIdRichiesta())).concat("&cuaa=").concat(filter.getCuaa())
				.concat("&numeroElementiPagina=").concat(String.valueOf(paginazione.getNumeroElementiPagina())).concat("&pagina=").concat(String.valueOf(paginazione.getPagina()))
				.concat("&valid=true&proprieta=").concat(ordinamento.getProprieta()).concat("&ordine=").concat(ordinamento.getOrdine().toString()))).andExpect(status().isOk())

				.andExpect(jsonPath("$.count", is(2))).andExpect(jsonPath("$.risultati[0].idRichiesta", is(136449))).andExpect(content().json(
						"{\"count\":2,\"risultati\":[{\"id\":136456,\"idRichiesta\":136449,\"statoRichiesta\":LAVORABILE,\"aziendaAgricola\":{\"cuaa\":\"02167970223\",\"ragioneSociale\":null},\"campagna\":2022,\"codSezione\":\"D516\",\"tipoSuoloDichiarato\":\"FRU\",\"tipoSuoloRilevato\":\"BO\",\"codiRileDichiarato\":\"650\",\"descRileDichiarato\":\"BOSCHI\",\"codiProdRileDichiarato\":\"000\",\"descProdRileDichiarato\":null,\"codiRilePrevalenteDich\":\"650\",\"descRilePrevalenteDich\":\"BOSCHI\",\"codiRileRilevato\":\"650\",\"descRileRilevato\":\"BOSCHI\",\"codiProdRileRilevato\":\"000\",\"descProdRileRilevato\":null,\"codiRilePrevalenteRil\":\"650\",\"descRilePrevalenteRil\":\"BOSCHI\",\"idLavorazione\":null,\"visible\":true}]}"));
	}

	// Ricercare SuoloDichiarato NON ancora associati a nessuna lavorazione: OK se posso accedere al servizio e restituisce i suoli afferenti solo al VITICOLO ( tagRile o tagDich VIT)
	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_POLIGONO_VITE_COD })
	void ricercaSuoloDichiaratoDaInserireInLavorazioneProfiloViticoloNonVedePoligoniDichiarati() throws Exception {
		SuoloDichiaratoLavorazioneFilter filter = new SuoloDichiaratoLavorazioneFilter();
		filter.setCampagna(Long.valueOf(annoCampagnaRicerca));
		filter.setIdRichiesta(136449L);
		filter.setCuaa("02167970223");
		filter.setComuneCatastale("D516");
		filter.setStatoRichiesta(StatoRichiestaModificaSuolo.LAVORABILE.toString());

		Paginazione paginazione = new Paginazione();
		paginazione.setNumeroElementiPagina(1);
		paginazione.setPagina(0);

		Ordinamento ordinamento = new Ordinamento();
		ordinamento.setOrdine(Ordine.DESC);
		ordinamento.setProprieta("id");

		mockMvc.perform(get(ApiUrls.SUOLO_DICHIARATO.concat("?campagna=").concat(String.valueOf(filter.getCampagna())).concat("&idRichiesta=").concat(String.valueOf(filter.getIdRichiesta()))
				.concat("&cuaa=").concat(filter.getCuaa()).concat("&statoRichiesta=").concat(filter.getStatoRichiesta()).concat("&comuneCatastale=").concat(filter.getComuneCatastale())
				.concat("&numeroElementiPagina=").concat(String.valueOf(paginazione.getNumeroElementiPagina())).concat("&pagina=").concat(String.valueOf(paginazione.getPagina()))
				.concat("&valid=true&proprieta=").concat(ordinamento.getProprieta()).concat("&ordine=").concat(ordinamento.getOrdine().toString()))).andExpect(status().isOk())

				.andExpect(jsonPath("$.count", is(0))).andExpect(content().json("{\"count\":0,\"risultati\":[]}"));
	}

	// Ricercare SuoloDichiarato NON ancora associati a nessuna lavorazione: OK se posso accedere al servizio e restituisce i suoli afferenti solo al VITICOLO ( tagRile o tagDich VIT)
	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_POLIGONO_VITE_COD })
	void ricercaSuoloDichiaratoDaInserireInLavorazioneProfiloViticolo() throws Exception {
		SuoloDichiaratoLavorazioneFilter filter = new SuoloDichiaratoLavorazioneFilter();
		filter.setCampagna(Long.valueOf(annoCampagnaRicerca));
		filter.setIdRichiesta(136490L);
		filter.setCuaa("GNRGNN34P43E797R");
		filter.setComuneCatastale("E461");
		filter.setStatoRichiesta(StatoRichiestaModificaSuolo.LAVORABILE.toString());

		Paginazione paginazione = new Paginazione();
		paginazione.setNumeroElementiPagina(1);
		paginazione.setPagina(0);

		Ordinamento ordinamento = new Ordinamento();
		ordinamento.setOrdine(Ordine.DESC);
		ordinamento.setProprieta("id");

		mockMvc.perform(get(ApiUrls.SUOLO_DICHIARATO.concat("?campagna=").concat(String.valueOf(filter.getCampagna())).concat("&idRichiesta=").concat(String.valueOf(filter.getIdRichiesta()))
				.concat("&cuaa=").concat(filter.getCuaa()).concat("&statoRichiesta=").concat(filter.getStatoRichiesta()).concat("&comuneCatastale=").concat(filter.getComuneCatastale())
				.concat("&numeroElementiPagina=").concat(String.valueOf(paginazione.getNumeroElementiPagina())).concat("&pagina=").concat(String.valueOf(paginazione.getPagina()))
				.concat("&valid=true&proprieta=").concat(ordinamento.getProprieta()).concat("&ordine=").concat(ordinamento.getOrdine().toString()))).andExpect(status().isOk())

				.andExpect(jsonPath("$.count", is(1))).andExpect(content().json(
						"{\"count\":1,\"risultati\":[{\"id\":136493,\"idRichiesta\":136490,\"statoRichiesta\":LAVORABILE,\"aziendaAgricola\":{\"cuaa\":\"GNRGNN34P43E797R\",\"ragioneSociale\":null},\"campagna\":2022,\"codSezione\":\"E461\",\"tipoSuoloDichiarato\":\"VIT\",\"tipoSuoloRilevato\":\"BO\",\"codiRileDichiarato\":\"666\",\"descRileDichiarato\":\"SEMINATIVI\",\"codiProdRileDichiarato\":\"100\",\"descProdRileDichiarato\":\"INCOLTO PRODUTTIVO SOGGETTO A PRATICHE AGRONOMICHE A BASSO IMPATTO\",\"codiRilePrevalenteDich\":\"100\",\"descRilePrevalenteDich\":\"INCOLTO PRODUTTIVO SOGGETTO A PRATICHE AGRONOMICHE A BASSO IMPATTO\",\"codiRileRilevato\":\"651\",\"descRileRilevato\":\"COLTIVAZIONI ARBOREE SPECIALIZZATE\",\"codiProdRileRilevato\":\"000\",\"descProdRileRilevato\":null,\"codiRilePrevalenteRil\":\"651\",\"descRilePrevalenteRil\":\"COLTIVAZIONI ARBOREE SPECIALIZZATE\",\"idLavorazione\":null,\"visible\":true}]}"));
	}

	// Aggiungere un SuoloDichiarato alla lavorazione: OK se posso accedere al servizio, sono il proprietario della lavorazione e posso aggiungere il suolo alla mia lavorazione
	@Test
	@WithMockUser(username = "ITE2505@ext.itad.infotn.it", roles = { Ruoli.EDIT_POLIGONO_TUTTI_COD })
	void aggiungiSuoloDichiaratoOK() throws Exception {

		Long idSuoloDichiarato = 136462L;

		// Associo alla lavorazione
		Long idLavorazione = 137790L;
		SuoloDichiaratoLavorazioneDto suoloDto = new SuoloDichiaratoLavorazioneDto();
		System.out.println(suoloDto.toString());
		suoloDto.setIdLavorazione(idLavorazione);

		Optional<SuoloDichiaratoLavorazioneModel> opt = suoloDichiaratoLavorazioneDao.findById(idSuoloDichiarato);

		StatoRichiestaModificaSuolo statoRichiestaPrimaDiAggiuntaDichiarato = null;
		if (opt.isPresent()) {
			statoRichiestaPrimaDiAggiuntaDichiarato = opt.get().getStatoRichiesta();
		}

		mockMvc.perform(put(ApiUrls.SUOLO_DICHIARATO.concat("/").concat(String.valueOf(idSuoloDichiarato)).concat("/associaLavorazione")).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(suoloDto))).andExpect(status().isOk());

		// Aggiunto controllo di cambio di stato su richiesta modifica suolo
		Optional<RichiestaModificaSuoloModel> richiestaDopoAggiunta = richiestaModificaSuoloDao.findById(opt.get().getIdRichiesta());

		StatoRichiestaModificaSuolo statoRichiestaDopoAggiuntaDichiarato = null;
		if (richiestaDopoAggiunta.isPresent()) {
			statoRichiestaDopoAggiuntaDichiarato = richiestaDopoAggiunta.get().getStato();
		}
		assertEquals(StatoRichiestaModificaSuolo.LAVORABILE, statoRichiestaPrimaDiAggiuntaDichiarato);
		assertEquals(StatoRichiestaModificaSuolo.IN_LAVORAZIONE, statoRichiestaDopoAggiuntaDichiarato);

	}

	@Test
	@WithMockUser(username = "ITE2505@ext.itad.infotn.it", roles = { Ruoli.EDIT_POLIGONO_TUTTI_COD })
	void aggiungiSuoloDichiaratoGiaAssociatoALavorazioneDovrebbeDare406() throws Exception {

		Long idSuoloDichiarato = 136463L;
		// Associo alla lavorazione
		Long idLavorazione = 137869L;
		SuoloDichiaratoLavorazioneDto suoloDto = new SuoloDichiaratoLavorazioneDto();
		suoloDto.setIdLavorazione(idLavorazione);
		mockMvc.perform(put(ApiUrls.SUOLO_DICHIARATO.concat("/").concat(String.valueOf(idSuoloDichiarato)).concat("/associaLavorazione")).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(suoloDto))).andExpect(status().isNotAcceptable());

	}

	// Aggiungere un SuoloDichiarato alla lavorazione: OK se posso accedere al servizio, sono il proprietario della lavorazione e posso aggiungere il suolo alla mia lavorazione
	@Test
	@WithMockUser(username = "ITE2505@ext.itad.infotn.it", roles = { Ruoli.EDIT_POLIGONO_VITE_COD })
	void aggiungiSuoloDichiaratoAllaLavorazioneProfiloViticoloConTagDichiaratoVIT() throws Exception {

		Long idSuoloDichiarato = 136493L;

		// Associo alla lavorazione
		Long idLavorazione = 137790L;
		SuoloDichiaratoLavorazioneDto suoloDto = new SuoloDichiaratoLavorazioneDto();
		suoloDto.setIdLavorazione(idLavorazione);
		mockMvc.perform(put(ApiUrls.SUOLO_DICHIARATO.concat("/").concat(String.valueOf(idSuoloDichiarato)).concat("/associaLavorazione")).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(suoloDto))).andExpect(status().isOk());

	}

	// Aggiungere un SuoloDichiarato alla lavorazione: OK se posso accedere al servizio, sono il proprietario della lavorazione e posso aggiungere il suolo alla mia lavorazione
	@Test
	@WithMockUser(username = "ITE2505@ext.itad.infotn.it", roles = { Ruoli.EDIT_POLIGONO_VITE_COD })
	void aggiungiSuoloDichiaratoAllaLavorazioneProfiloViticoloConTagRilevatoVIT() throws Exception {

		Long idSuoloDichiarato = 136333L;

		// Associo alla lavorazione
		Long idLavorazione = 137790L;
		SuoloDichiaratoLavorazioneDto suoloDto = new SuoloDichiaratoLavorazioneDto();
		suoloDto.setIdLavorazione(idLavorazione);
		mockMvc.perform(put(ApiUrls.SUOLO_DICHIARATO.concat("/").concat(String.valueOf(idSuoloDichiarato)).concat("/associaLavorazione")).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(suoloDto))).andExpect(status().isOk());

	}

	// Aggiungere un SuoloDichiarato alla lavorazione: OK non trovo la lavorazione 204
	@Test
	@WithMockUser(username = "ITE2505@ext.itad.infotn.it", roles = { Ruoli.EDIT_POLIGONO_TUTTI_COD })
	void aggiungiSuoloDichiaratoAllaLavorazioneLavorazioneNotFound() throws Exception {

		Long idSuoloDichiarato = 136462L;

		// Associo alla lavorazione
		Long idLavorazione = 13772224442290L;
		SuoloDichiaratoLavorazioneDto suoloDto = new SuoloDichiaratoLavorazioneDto();
		suoloDto.setIdLavorazione(idLavorazione);

		mockMvc.perform(put(ApiUrls.SUOLO_DICHIARATO.concat("/").concat(String.valueOf(idSuoloDichiarato)).concat("/associaLavorazione")).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(suoloDto))).andExpect(status().isNoContent());

	}

	// Aggiungere un SuoloDichiarato alla lavorazione: OK non trovo la lavorazione per l'utente
	@Test
	@WithMockUser(username = "ITE2505@ext.itad.infotn.it", roles = { Ruoli.EDIT_POLIGONO_TUTTI_COD })
	void aggiungiSuoloDichiaratoAllaLavorazioneLavorazioneNonAutorizzata() throws Exception {

		Long idSuoloDichiarato = 136462L;

		// Associo alla lavorazione
		Long idLavorazione = 144007L;
		SuoloDichiaratoLavorazioneDto suoloDto = new SuoloDichiaratoLavorazioneDto();
		suoloDto.setIdLavorazione(idLavorazione);

		mockMvc.perform(put(ApiUrls.SUOLO_DICHIARATO.concat("/").concat(String.valueOf(idSuoloDichiarato)).concat("/associaLavorazione")).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(suoloDto))).andExpect(status().isForbidden());

	}

	// Aggiungere un SuoloDichiarato alla lavorazione: OK non ho permessi
	@Test
	@WithMockUser(username = "ITE2505@ext.itad.infotn.it")
	void aggiungiSuoloDichiaratoAllaLavorazioneNessunProfilo403() throws Exception {

		Long idSuoloDichiarato = 136462L;

		// Associo alla lavorazione
		Long idLavorazione = 13772222290L;
		SuoloDichiaratoLavorazioneDto suoloDto = new SuoloDichiaratoLavorazioneDto();
		suoloDto.setIdLavorazione(idLavorazione);

		mockMvc.perform(put(ApiUrls.SUOLO_DICHIARATO.concat("/").concat(String.valueOf(idSuoloDichiarato)).concat("/associaLavorazione")).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(suoloDto))).andExpect(status().isForbidden());

	}

	// Aggiungere un SuoloDichiarato alla lavorazione: OK non trovo la lavorazione = FORBIDDEN
	@Test
	@WithMockUser(username = "ITE2505@ext.itad.infotn.it", roles = { Ruoli.EDIT_POLIGONO_VITE_COD })
	void aggiungiSuoloDichiaratoAllaLavorazioneProfiloViticolo403() throws Exception {

		Long idSuoloDichiarato = 161L;

		// Associo alla lavorazione
		Long idLavorazione = 14L;
		SuoloDichiaratoLavorazioneDto suoloDto = new SuoloDichiaratoLavorazioneDto();
		suoloDto.setIdLavorazione(idLavorazione);

		mockMvc.perform(put(ApiUrls.SUOLO_DICHIARATO.concat("/").concat(String.valueOf(idSuoloDichiarato)).concat("/associaLavorazione")).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(suoloDto))).andExpect(status().isForbidden());

	}

	// Aggiungere un SuoloDichiarato alla lavorazione: OK non trovo la lavorazione = FORBIDDEN
	@Test
	@WithMockUser(username = "ITE2505@ext.itad.infotn.it", roles = { Ruoli.EDIT_POLIGONO_VITE_COD })
	void aggiungiSuoloDichiaratoAllaLavorazioneProfiloViticoloSuoloConTagDiversiDaVIT403() throws Exception {

		Long idSuoloDichiarato = 181L;

		// Associo alla lavorazione
		Long idLavorazione = 137790L;
		SuoloDichiaratoLavorazioneDto suoloDto = new SuoloDichiaratoLavorazioneDto();
		suoloDto.setIdLavorazione(idLavorazione);

		mockMvc.perform(put(ApiUrls.SUOLO_DICHIARATO.concat("/").concat(String.valueOf(idSuoloDichiarato)).concat("/associaLavorazione")).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(suoloDto))).andExpect(status().isForbidden());

	}

	// Aggiungere un SuoloDichiarato alla lavorazione: OK se posso accedere al servizio, ma non trovo la lavorazione o non sono il creatore della lavorazione
	@Test
	@WithMockUser(username = "ITE2505@ext.itad.infotn.it", roles = { Ruoli.EDIT_POLIGONO_TUTTI_COD })
	void aggiungiSuoloDichiaratoAllaLavorazioneEntityNotFoundSuoloDichiarato() throws Exception {

		Long idSuoloDichiarato = 1364621111111111L;

		Long idLavorazione = 137790L;

		SuoloDichiaratoLavorazioneDto suoloDto = new SuoloDichiaratoLavorazioneDto();
		suoloDto.setIdLavorazione(idLavorazione);

		mockMvc.perform(put(ApiUrls.SUOLO_DICHIARATO.concat("/").concat(String.valueOf(idSuoloDichiarato)).concat("/associaLavorazione")).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(suoloDto))).andExpect(status().isNoContent());

	}

	// Rimuove un SuoloDichiarato dalla lavorazione: OK se posso accedere al servizio, sono il proprietario della lavorazione e posso eliminare il suolo dalla mia lavorazione
	@Test
	@WithMockUser(username = "ITE2505@ext.itad.infotn.it", roles = { Ruoli.EDIT_POLIGONO_TUTTI_COD })
	void removeSuoloDichiaratoDallaLavorazione() throws Exception {

		Long idLavorazione = 137869L;
		Long idSuoloDichiarato = 136463L;

		SuoloDichiaratoLavorazioneDto suoloDto = new SuoloDichiaratoLavorazioneDto();
		suoloDto.setIdLavorazione(idLavorazione);

		String URL = ApiUrls.SUOLO_DICHIARATO.concat("/").concat(String.valueOf(idSuoloDichiarato)).concat("/rimuoviLavorazione");
		mockMvc.perform(put(URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(suoloDto))).andExpect(status().isOk());

	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.EDIT_POLIGONO_TUTTI_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/associazioneSuoloDaDichiarato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "update A4ST_LAVORAZIONE_SUOLO set stato = 'CHIUSA' where id = 3;")
	void removeSuoloDichiaratoDallaLavorazioneChiusaErrore() throws Exception {

		Long idLavorazione = 3L;
		Long idSuoloDichiarato = 15L;

		SuoloDichiaratoLavorazioneDto suoloDto = new SuoloDichiaratoLavorazioneDto();
		suoloDto.setIdLavorazione(idLavorazione);

		String URL = ApiUrls.SUOLO_DICHIARATO.concat("/").concat(String.valueOf(idSuoloDichiarato)).concat("/rimuoviLavorazione");
		mockMvc.perform(put(URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(suoloDto))).andExpect(status().isNotAcceptable());

	}

	@Test
	@WithMockUser(username = "ITE2505@ext.itad.infotn.it", roles = { Ruoli.EDIT_POLIGONO_TUTTI_COD })
	void removeSuoloDichiaratoDallaLavorazioneNonCorretta406() throws Exception {

		Long idLavorazione = 137756L;
		Long idSuoloDichiarato = 136463L;

		SuoloDichiaratoLavorazioneDto suoloDto = new SuoloDichiaratoLavorazioneDto();
		suoloDto.setIdLavorazione(idLavorazione);

		String URL = ApiUrls.SUOLO_DICHIARATO.concat("/").concat(String.valueOf(idSuoloDichiarato)).concat("/rimuoviLavorazione");
		mockMvc.perform(put(URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(suoloDto))).andExpect(status().isNotAcceptable());

	}

	// Rimuove un SuoloDichiarato dalla lavorazione: OK Non accedo al servizio
	@Test
	@WithMockUser(username = "ITE2505@ext.itad.infotn.it", roles = { Ruoli.EDIT_POLIGONO_TUTTI_COD })
	void removeSuoloDichiaratoDallaLavorazione403() throws Exception {

		Long idLavorazione = 144007L;
		Long idSuoloDichiarato = 136463L;

		SuoloDichiaratoLavorazioneDto suoloDto = new SuoloDichiaratoLavorazioneDto();
		suoloDto.setIdLavorazione(idLavorazione);

		mockMvc.perform(put(ApiUrls.SUOLO_DICHIARATO.concat("/").concat(String.valueOf(idSuoloDichiarato).concat("/rimuoviLavorazione"))).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(suoloDto))).andExpect(status().isForbidden());

	}

	// Rimuove un SuoloDichiarato dalla lavorazione: OK se posso accedere al servizio, ma non trovo il suolo da eliminare oppure non sono il proprietario della lavorazione e
	@Test
	@WithMockUser(username = "ITE2505@ext.itad.infotn.it", roles = { Ruoli.EDIT_POLIGONO_TUTTI_COD })
	void removeSuoloDichiaratoDallaLavorazioneEntityNotFoundSuoloDichiarato() throws Exception {

		Long idLavorazione = 137756L;
		Long idSuoloDichiarato = 136461111111113L;

		SuoloDichiaratoLavorazioneDto suoloDto = new SuoloDichiaratoLavorazioneDto();
		suoloDto.setIdLavorazione(idLavorazione);
		mockMvc.perform(put(ApiUrls.SUOLO_DICHIARATO.concat("/").concat(String.valueOf(idSuoloDichiarato).concat("/rimuoviLavorazione"))).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(suoloDto))).andExpect(status().isNoContent());
	}

	// Ricercare SuoloDichiarato associati alla lavorazione per ID_LAVORAZIONE: OK se posso accedere al servizio e restituisce i suoli associati
	@Test
	@WithMockUser(username = "BACKOFFICE_EXTENT", roles = { Ruoli.VISUALIZZA_POLIGONO_TUTTI_COD, Ruoli.EDIT_POLIGONO_TUTTI_COD })
	@Sql(scripts = "/testCase/richiestaModificaSuolo/calcoloExtentSuoliDichiarati.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void ricercaSuoloDichiaratoEVerificaCalcoloExtent() throws Exception {

		SuoloDichiaratoLavorazioneFilter filter = new SuoloDichiaratoLavorazioneFilter();
		filter.setStatoRichiesta(StatoRichiestaModificaSuolo.LAVORABILE.toString());
		filter.setIdRichiesta(1586581L);

		Paginazione paginazione = new Paginazione();
		paginazione.setNumeroElementiPagina(1);
		paginazione.setPagina(0);

		Ordinamento ordinamento = new Ordinamento();
		ordinamento.setOrdine(Ordine.DESC);
		ordinamento.setProprieta("idRichiesta");

		MvcResult result = mockMvc.perform(get(ApiUrls.SUOLO_DICHIARATO.concat("?statoRichiesta=").concat(filter.getStatoRichiesta()).concat("&numeroElementiPagina=")
				.concat(String.valueOf(paginazione.getNumeroElementiPagina())).concat("&pagina=").concat(String.valueOf(paginazione.getPagina())).concat("&valid=true&proprieta=")
				.concat(ordinamento.getProprieta()).concat("&ordine=").concat(ordinamento.getOrdine().toString()))).andExpect(status().isOk()).andReturn();

		String content = result.getResponse().getContentAsString();

		RisultatiPaginati<SuoloDichiaratoLavorazioneDto> listDto = objectMapper.readValue(content, new TypeReference<RisultatiPaginati<SuoloDichiaratoLavorazioneDto>>() {
		});

		SuoloDichiaratoLavorazioneDto dto = listDto.getRisultati().get(0);

		assertEquals(1586585L, dto.getId());
		assertArrayEquals(new Double[] { 653144.93757175, 5096131.33595038, 653259.282445594, 5096171.82709705 }, dto.getExtent());

		Long idSuoloDichiarato = dto.getId();
		// Associo alla lavorazione

		Long idLavorazione = 113l;
		SuoloDichiaratoLavorazioneDto suoloDto = new SuoloDichiaratoLavorazioneDto();
		suoloDto.setIdLavorazione(idLavorazione);
		System.out.println(suoloDto.toString());

		mockMvc.perform(put(ApiUrls.SUOLO_DICHIARATO.concat("/").concat(String.valueOf(idSuoloDichiarato)).concat("/associaLavorazione")).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(suoloDto))).andExpect(status().isOk());

		Optional<SuoloDichiaratoLavorazioneModel> opt = suoloDichiaratoLavorazioneDao.findById(dto.getId());

		if (opt.isPresent()) {
			SuoloDichiaratoLavorazioneDto afterDto = suoloDichiaratoLavorazioneMapper.convertToDto(opt.get());
			assertArrayEquals(afterDto.getExtent(), dto.getExtent());
		}
	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.VISUALIZZA_POLIGONO_TUTTI_COD, Ruoli.EDIT_POLIGONO_TUTTI_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/gestioneEsitoDichiarato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void aggiornamaentoEsitoBoOk() throws Exception {
		Long idSuoloDichiarato = 311L;

		String URL = ApiUrls.SUOLO_DICHIARATO.concat("/").concat(String.valueOf(idSuoloDichiarato)).concat("/setEsitoDichiarato/?esito=APPROVATO");
		mockMvc.perform(put(URL).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

	}

	@Test
	@WithMockUser(username = "VITICOLO", roles = { Ruoli.VISUALIZZA_POLIGONO_VITE_COD, Ruoli.EDIT_POLIGONO_VITE_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/gestioneEsitoDichiarato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void aggiornamaentoEsitoVitOk() throws Exception {
		Long idSuoloDichiarato = 321L;

		String URL = ApiUrls.SUOLO_DICHIARATO.concat("/").concat(String.valueOf(idSuoloDichiarato)).concat("/setEsitoDichiarato/?esito=APPROVATO");
		mockMvc.perform(put(URL).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

	}

	@Test
	@WithMockUser(username = "UTENTECAA", roles = { Ruoli.VISUALIZZA_POLIGONO_TUTTI_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/gestioneEsitoDichiarato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void aggiornamaentoEsitoKoUtenteNonAbilitato() throws Exception {
		Long idSuoloDichiarato = 311L;

		String URL = ApiUrls.SUOLO_DICHIARATO.concat("/").concat(String.valueOf(idSuoloDichiarato)).concat("/setEsitoDichiarato/?esito=APPROVATO");
		mockMvc.perform(put(URL).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "VITICOLO", roles = { Ruoli.VISUALIZZA_POLIGONO_VITE_COD, Ruoli.EDIT_POLIGONO_VITE_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/gestioneEsitoDichiarato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void aggiornamaentoEsitoKoUtenteViticolo() throws Exception {
		Long idSuoloDichiarato = 311L;

		String URL = ApiUrls.SUOLO_DICHIARATO.concat("/").concat(String.valueOf(idSuoloDichiarato)).concat("/setEsitoDichiarato/?esito=APPROVATO");
		mockMvc.perform(put(URL).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.VISUALIZZA_POLIGONO_TUTTI_COD, Ruoli.EDIT_POLIGONO_TUTTI_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/gestioneEsitoDichiarato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void aggiornamaentoEsitoKoPoligonoNonEsisteBo() throws Exception {
		Long idSuoloDichiarato = 3111111111L;

		String URL = ApiUrls.SUOLO_DICHIARATO.concat("/").concat(String.valueOf(idSuoloDichiarato)).concat("/setEsitoDichiarato/?esito=APPROVATO");
		mockMvc.perform(put(URL).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
	}

	@Test
	@WithMockUser(username = "VITICOLO", roles = { Ruoli.VISUALIZZA_POLIGONO_VITE_COD, Ruoli.EDIT_POLIGONO_VITE_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/gestioneEsitoDichiarato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void aggiornamaentoEsitoKoPoligonoNonEsisteVit() throws Exception {
		Long idSuoloDichiarato = 3111111111L;

		String URL = ApiUrls.SUOLO_DICHIARATO.concat("/").concat(String.valueOf(idSuoloDichiarato)).concat("/setEsitoDichiarato/?esito=APPROVATO");
		mockMvc.perform(put(URL).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "BACKOFFICE1", roles = { Ruoli.VISUALIZZA_POLIGONO_TUTTI_COD, Ruoli.EDIT_POLIGONO_TUTTI_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/gestioneEsitoDichiarato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void aggiornamaentoEsitoKoUtenteNonProprietario() throws Exception {
		Long idSuoloDichiarato = 311L;

		String URL = ApiUrls.SUOLO_DICHIARATO.concat("/").concat(String.valueOf(idSuoloDichiarato)).concat("/setEsitoDichiarato/?esito=APPROVATO");
		mockMvc.perform(put(URL).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotAcceptable());

	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.VISUALIZZA_POLIGONO_TUTTI_COD, Ruoli.EDIT_POLIGONO_TUTTI_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/gestioneEsitoDichiarato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void aggiornamaentoEsitoKoNessunaLavorazioneAssociata() throws Exception {
		Long idSuoloDichiarato = 341L;

		String URL = ApiUrls.SUOLO_DICHIARATO.concat("/").concat(String.valueOf(idSuoloDichiarato)).concat("/setEsitoDichiarato/?esito=APPROVATO");
		mockMvc.perform(put(URL).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotAcceptable());

	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.VISUALIZZA_POLIGONO_TUTTI_COD, Ruoli.EDIT_POLIGONO_TUTTI_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/gestioneEsitoDichiarato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void aggiornamaentoEsitoKoLavorazioneInCreazione() throws Exception {
		Long idSuoloDichiarato = 331L;
		String URL = ApiUrls.SUOLO_DICHIARATO.concat("/").concat(String.valueOf(idSuoloDichiarato)).concat("/setEsitoDichiarato/?esito=APPROVATO");
		mockMvc.perform(put(URL).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotAcceptable());
	}

	@Test
	@WithMockUser(username = "BACKOFFICE", roles = { Ruoli.VISUALIZZA_POLIGONO_TUTTI_COD, Ruoli.EDIT_POLIGONO_TUTTI_COD })
	@Sql(scripts = "/testCase/lavorazioneSuolo/gestioneEsitoDichiarato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void aggiornamaentoEsitoKoRichiestaCancellata() throws Exception {
		Long idSuoloDichiarato = 351L;
		String URL = ApiUrls.SUOLO_DICHIARATO.concat("/").concat(String.valueOf(idSuoloDichiarato)).concat("/setEsitoDichiarato/?esito=APPROVATO");
		mockMvc.perform(put(URL).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotAcceptable());
	}

	// Dato un idDichiarato restituisce una lista di messaggi associati
	@Test
	@WithMockUser(username = "UTENTECAA", roles = { Ruoli.VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_TUTTI_COD, Ruoli.VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_ENTE_COD })
	void ricercaMessaggiDichiarato() throws Exception {

		Long idDichiarato = 136454L;

		MvcResult result = mockMvc.perform(get(ApiUrls.SUOLO_DICHIARATO.concat("/").concat(String.valueOf(idDichiarato)).concat("/messaggiDichiarato"))).andExpect(status().isOk()).andReturn();

		String content = result.getResponse().getContentAsString();

		List<MessaggioRichiestaDto> messaggiListDto = objectMapper.readValue(content, new TypeReference<List<MessaggioRichiestaDto>>() {
		});

	}

	// Dato un idDichiarato inserisce una lista di messaggi associati
	@Test
	@WithMockUser(username = "UTENTECAA", roles = { Ruoli.EDIT_MESSAGGIO_RICHIESTA_TUTTI_COD, Ruoli.EDIT_RICHIESTA_MODIFICA_SUOLO_ENTE_COD })
	void inserisciMessaggiDichiarato() throws Exception {

		Long idDichiarato = 136454L;

		List<MessaggioRichiestaDto> messaggiListDto = new ArrayList<>();
		MessaggioRichiestaDto messaggio = new MessaggioRichiestaDto();
		messaggio.setProfiloUtente(ProfiloUtente.CAA);
		messaggio.setDataInserimento(LocalDateTime.now());
		messaggio.setTesto("Testo messaggio1");

		messaggiListDto.add(messaggio);

		messaggio = new MessaggioRichiestaDto();
		messaggio.setProfiloUtente(ProfiloUtente.CAA);
		messaggio.setDataInserimento(LocalDateTime.now());
		messaggio.setTesto("Testo messaggio2");

		messaggiListDto.add(messaggio);

		ResultActions resultAction = mockMvc.perform(post(ApiUrls.SUOLO_DICHIARATO.concat("/").concat(String.valueOf(idDichiarato)).concat("/messaggiDichiarato"))
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(messaggiListDto))).andExpect(status().isCreated());

		resultAction.andDo(print()).andExpect(status().isCreated());

	}
}
