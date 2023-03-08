package it.tndigitale.a4g.richiestamodificasuolo;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

import it.tndigitale.a4g.fascicolo.anagrafica.client.model.SportelloFascicoloDto;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento.Ordine;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4g.richiestamodificasuolo.api.ApiUrls;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.EsitoRichiestaModificaSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.MessaggioRichiestaModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.ProfiloUtente;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoRichiestaModificaSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.TipoInterventoColturale;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.TipoRichiestaModificaSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.client.AnagraficaFascicoloClient;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.client.FascicoloClient;
import it.tndigitale.a4g.richiestamodificasuolo.dto.filter.RichiestaModificaSuoloFilter;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.SuoloDichiaratoLavorazioneDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.richiestaModificaSuolo.DatiAggiuntiviRichiestaModificaSuoloDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.richiestaModificaSuolo.PeriodoInterventoRichiestaModificaSuoloDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.richiestaModificaSuolo.RichiestaModificaSuoloDto;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class RichiestaModificaSuoloApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ApplicationCache applicationCache;

	@MockBean
	private AnagraficaFascicoloClient anagrafica;

	@MockBean
	private FascicoloClient fascicoloClient;

	@BeforeEach
	private void clearCache() {
		applicationCache.clearCacheAll();
	}

	private Integer annoCampagnaRicerca = 2022;

	/*
	 * RichiestaRiesame GET GET 403 PUT 200 PUT 400
	 */
	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_TUTTI_COD })
	void getRichiestaModificaSuoloFilter() throws Exception {

		RichiestaModificaSuoloFilter filter = new RichiestaModificaSuoloFilter();
		filter.setCuaa("FRNDLN57T64D775D");
		filter.setCampagna(Long.valueOf(annoCampagnaRicerca));
		filter.setData(LocalDateTime.of(2020, 06, 04, 15, 00));
		filter.setStato(StatoRichiestaModificaSuolo.APERTA);
		filter.setTipo(TipoRichiestaModificaSuolo.ISTANZA_DI_RIESAME);
		filter.setIdRichiesta(2528L);

		String filtri = filter.toString();
		System.out.println(filtri);

		Paginazione paginazione = new Paginazione();
		paginazione.setNumeroElementiPagina(1);
		paginazione.setPagina(0);

		MvcResult result = mockMvc
				.perform(get(ApiUrls.RICHIESTA_MODIFICA_SUOLO + "?cuaa=" + filter.getCuaa() + "&campagna=" + filter.getCampagna() + "&stato=" + filter.getStato() + "&tipo=" + filter.getTipo()
						+ "&data=" + filter.getData() + "&numeroElementiPagina=" + paginazione.getNumeroElementiPagina() + "&pagina=" + paginazione.getPagina() + "&valid=true"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.count").value(1)).andExpect(jsonPath("$.risultati[0].aziendaAgricola.cuaa").value(filter.getCuaa())).andReturn();

		String content = result.getResponse().getContentAsString();

		RisultatiPaginati<RichiestaModificaSuoloDto> listDto = objectMapper.readValue(content, new TypeReference<RisultatiPaginati<RichiestaModificaSuoloDto>>() {
		});

		RichiestaModificaSuoloDto dto = listDto.getRisultati().get(0);
		assertEquals(1L, listDto.getCount());
		assertEquals(2528L, dto.getId());
		assertEquals("USER", dto.getUtente());
		assertEquals(dto.getData(), LocalDateTime.of(2020, 06, 04, 15, 00));
		assertEquals(TipoRichiestaModificaSuolo.ISTANZA_DI_RIESAME, dto.getTipo());
		assertEquals(StatoRichiestaModificaSuolo.APERTA, dto.getStato());
		assertEquals(EsitoRichiestaModificaSuolo.KO, dto.getEsito());

		assertEquals("FRNDLN57T64D775D", dto.getAziendaAgricola().getCuaa());
		assertEquals("Azienda Agricola", dto.getAziendaAgricola().getRagioneSociale());
		assertEquals(Integer.valueOf(annoCampagnaRicerca), dto.getCampagna());

		assertEquals(true, dto.getDatiAggiuntivi().getVisibileOrtofoto());
		assertEquals(LocalDateTime.of(2021, 01, 21, 00, 00), dto.getDatiAggiuntivi().getPeriodoIntervento().getDataFine());
		assertEquals(LocalDateTime.of(2021, 01, 20, 00, 00), dto.getDatiAggiuntivi().getPeriodoIntervento().getDataInizio());
		assertEquals(TipoInterventoColturale.ESTIRPO, dto.getDatiAggiuntivi().getTipoInterventoColturale());
		assertEquals("D516", dto.getSezioniCatastali().get(0).getCodice());
		assertEquals("D516", dto.getSezioniCatastali().get(0).getDescrizione());

	}

	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_TUTTI_COD })
	@Sql(scripts = "/testCase/richiestaModificaSuolo/calcoloExtentRichiestaModificaSuolo.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	void getRichiestaModificaSuoloCalcoloExtent() throws Exception {

		RichiestaModificaSuoloFilter filter = new RichiestaModificaSuoloFilter();
		filter.setStato(StatoRichiestaModificaSuolo.APERTA);
		filter.setTipo(TipoRichiestaModificaSuolo.ISTANZA_DI_RIESAME);
		filter.setIdRichiesta(586581L);

		String filtri = filter.toString();
		System.out.println(filtri);

		Paginazione paginazione = new Paginazione();
		paginazione.setNumeroElementiPagina(1);
		paginazione.setPagina(0);

		MvcResult result = mockMvc.perform(get(ApiUrls.RICHIESTA_MODIFICA_SUOLO + "?idRichiesta=" + filter.getIdRichiesta() + "&campagna=2022" + "&stato=" + filter.getStato() + "&tipo="
				+ filter.getTipo() + "&numeroElementiPagina=" + paginazione.getNumeroElementiPagina() + "&pagina=" + paginazione.getPagina() + "&valid=true")).andExpect(status().isOk()).andReturn();

		String content = result.getResponse().getContentAsString();

		RisultatiPaginati<RichiestaModificaSuoloDto> listDto = objectMapper.readValue(content, new TypeReference<RisultatiPaginati<RichiestaModificaSuoloDto>>() {
		});

		RichiestaModificaSuoloDto dto = listDto.getRisultati().get(0);

		assertNotNull(dto.getExtent());
		Double[] extent = { 653143.129510906, 5096090.23856946, 653304.422072195, 5096195.43219242 };
		assertArrayEquals(extent, dto.getExtent());

	}

	@Test
	@WithMockUser(username = "utente")
	void getRichiestaModifica403() throws Exception {

		RichiestaModificaSuoloFilter filter = new RichiestaModificaSuoloFilter();
		filter.setStato(StatoRichiestaModificaSuolo.APERTA);
		filter.setTipo(TipoRichiestaModificaSuolo.ISTANZA_DI_RIESAME);
		filter.setIdRichiesta(586581L);

		String filtri = filter.toString();
		System.out.println(filtri);

		Paginazione paginazione = new Paginazione();
		paginazione.setNumeroElementiPagina(1);
		paginazione.setPagina(0);

		mockMvc.perform(get(ApiUrls.RICHIESTA_MODIFICA_SUOLO + "?idRichiesta=" + filter.getIdRichiesta() + "&campagna=2022" + "&stato=" + filter.getStato() + "&tipo=" + filter.getTipo()
				+ "&numeroElementiPagina=" + paginazione.getNumeroElementiPagina() + "&pagina=" + paginazione.getPagina() + "&valid=true")).andExpect(status().isForbidden());

	}

	// Ricerca RichiesteModificaSuolo con profilo CAA, non ha mandato sul CUAA richiesto
	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_ENTE_COD })
	void getRichiestaModificaSuoloFilterProfiloCAA() throws Exception {

		RichiestaModificaSuoloFilter filter = new RichiestaModificaSuoloFilter();
		filter.setCuaa("FRNDLN57T64D775D");
		filter.setCampagna(Long.valueOf(annoCampagnaRicerca));
		filter.setData(LocalDateTime.of(2020, 06, 04, 15, 00));
		filter.setStato(StatoRichiestaModificaSuolo.APERTA);
		filter.setTipo(TipoRichiestaModificaSuolo.ISTANZA_DI_RIESAME);
		filter.setIdRichiesta(2528L);

		Paginazione paginazione = new Paginazione();
		paginazione.setNumeroElementiPagina(1);
		paginazione.setPagina(0);

		utenteConMandato();

		MvcResult result = mockMvc
				.perform(get(ApiUrls.RICHIESTA_MODIFICA_SUOLO + "?cuaa=" + filter.getCuaa() + "&campagna=" + filter.getCampagna() + "&stato=" + filter.getStato() + "&tipo=" + filter.getTipo()
						+ "&data=" + filter.getData() + "&numeroElementiPagina=" + paginazione.getNumeroElementiPagina() + "&pagina=" + paginazione.getPagina() + "&valid=true"))
				.andExpect(status().isOk()).andReturn();

		String content = result.getResponse().getContentAsString();

		assertEquals("{\"count\":0,\"risultati\":[]}", content);
	}

	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_TUTTI_COD })
	void getRichiestaModificaSuoloFilterComuneCatastale() throws Exception {
		RichiestaModificaSuoloFilter filter = new RichiestaModificaSuoloFilter();
		filter.setComuneCatastale("D516");

		Paginazione paginazione = new Paginazione();
		paginazione.setNumeroElementiPagina(1);
		paginazione.setPagina(0);

		mockMvc.perform(get(ApiUrls.RICHIESTA_MODIFICA_SUOLO + "?comuneCatastale=" + filter.getComuneCatastale() + "&numeroElementiPagina=" + paginazione.getNumeroElementiPagina() + "&pagina="
				+ paginazione.getPagina() + "&valid=true")).andExpect(status().isOk()).andExpect(jsonPath("$.count").value(2))
				.andExpect(jsonPath("$.risultati[0].sezioniCatastali[0].codice").value(filter.getComuneCatastale()));
	}

	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_TUTTI_COD })
	void getRichiestaModificaSuoloFilterCampagnaOrderByDataRichiestaAsc() throws Exception {
		// In questo test voglio filtrare per anno di campagna
		// e ottenere 4 risultati ordinati per data_richiesta ascendente

		RichiestaModificaSuoloFilter filter = new RichiestaModificaSuoloFilter();
		filter.setCampagna(Long.valueOf(annoCampagnaRicerca));

		Paginazione paginazione = new Paginazione();
		paginazione.setNumeroElementiPagina(1);
		paginazione.setPagina(0);

		Ordinamento ordinamento = new Ordinamento();
		ordinamento.setProprieta("dataRichiesta");
		ordinamento.setOrdine(Ordine.ASC);

		String dataDaConfrontareOrdinamentoPiuPiccola = "2020-01-27T00:00:00";

		mockMvc.perform(get(ApiUrls.RICHIESTA_MODIFICA_SUOLO + "?campagna=" + filter.getCampagna() + "&numeroElementiPagina=" + paginazione.getNumeroElementiPagina() + "&pagina="
				+ paginazione.getPagina() + "&valid=true" + "&proprieta=" + ordinamento.getProprieta() + "&ordine=" + ordinamento.getOrdine())).andExpect(status().isOk())
				.andExpect(jsonPath("$.count").value(15)).andExpect(jsonPath("$.risultati[0].data").value(dataDaConfrontareOrdinamentoPiuPiccola)).andExpect(content().json(
						"{\"count\":15,\"risultati\":[{\"id\":136478,\"utente\":\"USER\",\"data\":\"2020-01-27T00:00:00\",\"tipo\":\"ISTANZA_DI_RIESAME\",\"stato\":\"LAVORABILE\",\"esito\":null,\"aziendaAgricola\":{\"cuaa\":\"BTTMHL56E23E565Q\",\"ragioneSociale\":\"BETTI MICHELE\"},\"campagna\":2022,\"datiAggiuntivi\":{\"visibileOrtofoto\":null,\"tipoInterventoColturale\":null,\"periodoIntervento\":{\"dataInizio\":null,\"dataFine\":null}},\"sezioniCatastali\":[]}]}"));

	}

	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_TUTTI_COD })
	void getRichiestaModificaSuoloFilterCampagnaOrderByDataRichiestaDesc() throws Exception {
		// In questo test voglio filtrare per anno di campagna
		// e ottenere 14 risultati ordinati per data_richiesta discendente

		RichiestaModificaSuoloFilter filter = new RichiestaModificaSuoloFilter();
		filter.setCampagna(Long.valueOf(annoCampagnaRicerca));

		Paginazione paginazione = new Paginazione();
		paginazione.setNumeroElementiPagina(1);
		paginazione.setPagina(0);

		Ordinamento ordinamento = new Ordinamento();
		ordinamento.setProprieta("dataRichiesta");
		ordinamento.setOrdine(Ordine.DESC);

		String dataDaConfrontareOrdinamentoPiuGrande = "2020-06-19T00:00:00";

		mockMvc.perform(get(ApiUrls.RICHIESTA_MODIFICA_SUOLO + "?campagna=" + filter.getCampagna() + "&numeroElementiPagina=" + paginazione.getNumeroElementiPagina() + "&pagina="
				+ paginazione.getPagina() + "&valid=true" + "&proprieta=" + ordinamento.getProprieta() + "&ordine=" + ordinamento.getOrdine())).andExpect(status().isOk())
				.andExpect(jsonPath("$.count").value(15)).andExpect(jsonPath("$.risultati[0].data").value(dataDaConfrontareOrdinamentoPiuGrande));

	}

	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_ENTE_COD })
	void getRicercaDettaglio() throws Exception {
		Long idRichiesta = 136449L;

		utenteConMandato();

		MvcResult result = mockMvc.perform(get(ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(idRichiesta)))).andExpect(status().isOk())
				.andExpect(jsonPath("id").value(idRichiesta)).andReturn();

		String content = result.getResponse().getContentAsString();

		RichiestaModificaSuoloDto dto = objectMapper.readValue(content, new TypeReference<RichiestaModificaSuoloDto>() {
		});

		assertEquals(136449L, dto.getId());
		assertEquals("USER", dto.getUtente());
		assertEquals(dto.getData(), LocalDateTime.of(2020, 06, 04, 00, 00));
		assertEquals(TipoRichiestaModificaSuolo.ISTANZA_DI_RIESAME, dto.getTipo());
		assertEquals(StatoRichiestaModificaSuolo.LAVORABILE, dto.getStato());
		assertEquals(null, dto.getEsito());

		assertEquals("02167970223", dto.getAziendaAgricola().getCuaa());
		assertEquals("AZIENDA AGRICOLA ZOOTECNICA DI ZANOTELLI SILVANO E TIZIANO SOCIETA' SEMPLICE AGRICOLA", dto.getAziendaAgricola().getRagioneSociale());
		assertEquals(Integer.valueOf(annoCampagnaRicerca), dto.getCampagna());

		assertEquals(true, dto.getDatiAggiuntivi().getVisibileOrtofoto());
		assertEquals(LocalDateTime.of(2021, 01, 01, 00, 00), dto.getDatiAggiuntivi().getPeriodoIntervento().getDataFine());
		assertEquals(LocalDateTime.of(2021, 01, 01, 00, 00), dto.getDatiAggiuntivi().getPeriodoIntervento().getDataInizio());
		assertEquals(TipoInterventoColturale.BONIFICA_PULIZIA_O_RECUPERO_PARZIALE, dto.getDatiAggiuntivi().getTipoInterventoColturale());
		assertEquals("D516", dto.getSezioniCatastali().get(0).getCodice());
		assertEquals("D516", dto.getSezioniCatastali().get(0).getDescrizione());
		assertNotNull(dto.getExtent());

	}

	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.EDIT_DOCUMENTO_RICHIESTA_TUTTI_COD })
	void getRicercaDettaglio403() throws Exception {
		Long idRichiesta = 2501L;
		utenteConMandato();

		mockMvc.perform(get(ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(idRichiesta)))).andExpect(status().isForbidden());

	}

	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_ENTE_COD })
	void getRicercaDettaglio403UtenteNonHaMandati() throws Exception {
		Long idRichiesta = 2501L;

		utenteSenzaMandato();

		mockMvc.perform(get(ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(idRichiesta)))).andExpect(status().isForbidden());

	}

	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.EDIT_RICHIESTA_MODIFICA_SUOLO_ENTE_COD })
	void updateRichiestaModificaSuolo() throws Exception {

		DatiAggiuntiviRichiestaModificaSuoloDto datiAggiuntiviRichiestaModificaSuoloDto = new DatiAggiuntiviRichiestaModificaSuoloDto();
		datiAggiuntiviRichiestaModificaSuoloDto.setVisibileOrtofoto(false);
		datiAggiuntiviRichiestaModificaSuoloDto.setTipoInterventoColturale(TipoInterventoColturale.ABBANDONO);

		PeriodoInterventoRichiestaModificaSuoloDto periodoInterventoRichiestaModificaSuoloDto = new PeriodoInterventoRichiestaModificaSuoloDto();
		LocalDateTime dateFine = LocalDateTime.of(2020, 01, 01, 00, 00);
		LocalDateTime dateInizio = LocalDateTime.of(2020, 01, 01, 00, 00);

		periodoInterventoRichiestaModificaSuoloDto.setDataFine(dateFine);
		periodoInterventoRichiestaModificaSuoloDto.setDataInizio(dateInizio);
		datiAggiuntiviRichiestaModificaSuoloDto.setPeriodoIntervento(periodoInterventoRichiestaModificaSuoloDto);

		RichiestaModificaSuoloDto richiestaModificaSuoloDto = new RichiestaModificaSuoloDto();
		richiestaModificaSuoloDto.setId(2501L);
		richiestaModificaSuoloDto.setStato(StatoRichiestaModificaSuolo.APERTA);

		richiestaModificaSuoloDto.setDatiAggiuntivi(datiAggiuntiviRichiestaModificaSuoloDto);

		utenteConMandato();

		mockMvc.perform(put(ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(richiestaModificaSuoloDto.getId())) + "?campagna=" + richiestaModificaSuoloDto.getCampagna() + "&id="
				+ String.valueOf(richiestaModificaSuoloDto.getId())).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(richiestaModificaSuoloDto)))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "utente")
	void updateRichiestaModificaSuolo403() throws Exception {

		RichiestaModificaSuoloDto richiestaModificaSuoloDto = new RichiestaModificaSuoloDto();
		richiestaModificaSuoloDto.setId(2501L);
		richiestaModificaSuoloDto.setStato(StatoRichiestaModificaSuolo.APERTA);

		richiestaModificaSuoloDto.setDatiAggiuntivi(null);

		utenteConMandato();

		mockMvc.perform(put(ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(richiestaModificaSuoloDto.getId())) + "?campagna=" + richiestaModificaSuoloDto.getCampagna() + "&id="
				+ String.valueOf(richiestaModificaSuoloDto.getId())).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(richiestaModificaSuoloDto)))
				.andExpect(status().isForbidden());

	}

	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.EDIT_RICHIESTA_MODIFICA_SUOLO_ENTE_COD })
	void updateRichiestaModificaSuoloStatoNonPermesso() throws Exception {

		RichiestaModificaSuoloDto richiestaModificaSuoloDto = new RichiestaModificaSuoloDto();
		richiestaModificaSuoloDto.setId(2501L);
		richiestaModificaSuoloDto.setStato(StatoRichiestaModificaSuolo.CONCLUSA);

		DatiAggiuntiviRichiestaModificaSuoloDto datiAggiuntiviRichiestaModificaSuoloDto = new DatiAggiuntiviRichiestaModificaSuoloDto();
		datiAggiuntiviRichiestaModificaSuoloDto.setVisibileOrtofoto(false);
		datiAggiuntiviRichiestaModificaSuoloDto.setTipoInterventoColturale(TipoInterventoColturale.ABBANDONO);

		PeriodoInterventoRichiestaModificaSuoloDto periodoInterventoRichiestaModificaSuoloDto = new PeriodoInterventoRichiestaModificaSuoloDto();
		LocalDateTime dateFine = LocalDateTime.of(2020, 01, 01, 00, 00);
		LocalDateTime dateInizio = LocalDateTime.of(2020, 01, 01, 00, 00);

		periodoInterventoRichiestaModificaSuoloDto.setDataFine(dateFine);
		periodoInterventoRichiestaModificaSuoloDto.setDataInizio(dateInizio);
		datiAggiuntiviRichiestaModificaSuoloDto.setPeriodoIntervento(periodoInterventoRichiestaModificaSuoloDto);

		richiestaModificaSuoloDto.setStato(StatoRichiestaModificaSuolo.CONCLUSA);

		richiestaModificaSuoloDto.setDatiAggiuntivi(datiAggiuntiviRichiestaModificaSuoloDto);

		utenteConMandato();

		mockMvc.perform(put(ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(richiestaModificaSuoloDto.getId())) + "?campagna=" + richiestaModificaSuoloDto.getCampagna() + "&id="
				+ String.valueOf(richiestaModificaSuoloDto.getId())).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(richiestaModificaSuoloDto)))
				.andExpect(status().isNotAcceptable());

	}
	/*
	 * MessaggiRichiesta GET POST 201 POST 400
	 */

	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_TUTTI_COD })
	void getMessaggiRichiestaFilterIdRichiestaOrderByDatainserimentoDesc() throws Exception {

		Long idRichiesta = 2501L;

		Paginazione paginazione = new Paginazione();
		paginazione.setNumeroElementiPagina(10);
		paginazione.setPagina(0);

		Ordinamento ordinamento = new Ordinamento();
		ordinamento.setProprieta("dataInserimento");
		ordinamento.setOrdine(Ordine.DESC);

		MvcResult result = mockMvc
				.perform(get(ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(idRichiesta)).concat("/messaggi") + "?numeroElementiPagina=" + paginazione.getNumeroElementiPagina()
						+ "&pagina=" + paginazione.getPagina() + "&valid=true" + "&proprieta=" + ordinamento.getProprieta() + "&ordine=" + ordinamento.getOrdine()))
				.andExpect(status().isOk()).andExpect(jsonPath("$.count").value(1)).andReturn();

		String content = result.getResponse().getContentAsString();

		assertEquals("{\"count\":1,\"risultati\":[{\"id\":4,\"utente\":\"USER\",\"profiloUtente\":\"CAA\"," + "\"dataInserimento\":\"2021-02-12T00:00:00\","
				+ "\"testo\":\"Le informazioni richieste sono in fondo agli allegati inseriti\",\"idPoligonoDichiarato\":null}]}", content);

	}

	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.EDIT_MESSAGGIO_RICHIESTA_TUTTI_COD })
	void insertMessaggiRichiestaFromRichiesta() throws Exception {

		Long idRichiesta = 2501L;
		List<MessaggioRichiestaModel> listMessaggioRichiestaModel = new ArrayList<MessaggioRichiestaModel>();
		MessaggioRichiestaModel mess1 = new MessaggioRichiestaModel();
		mess1.setDataInserimento(LocalDateTime.parse("2000-10-31T01:30:00.000-05:00", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")));
		mess1.setProfiloUtente(ProfiloUtente.CAA);
		mess1.setTesto("Lupus In Fabula");
		mess1.setUtente("DEL TORO");

		MessaggioRichiestaModel mess2 = new MessaggioRichiestaModel();
		mess2.setDataInserimento(LocalDateTime.parse("2000-11-31T01:30:00.000-05:00", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")));
		mess2.setProfiloUtente(ProfiloUtente.BACKOFFICE);
		mess2.setTesto("veni vidi vici");
		mess2.setUtente("ROMANO");

		listMessaggioRichiestaModel.add(mess1);
		listMessaggioRichiestaModel.add(mess2);

		mockMvc.perform(post(ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(idRichiesta)).concat("/messaggi")).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(listMessaggioRichiestaModel))).andExpect(status().isCreated());
	}

	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.EDIT_RICHIESTA_MODIFICA_SUOLO_ENTE_COD })
	void insertMessaggiRichiestaFromRichiestaProfiloCAA() throws Exception {
		Long idRichiesta = 2501L;
		List<MessaggioRichiestaModel> listMessaggioRichiestaModel = new ArrayList<MessaggioRichiestaModel>();
		MessaggioRichiestaModel mess1 = new MessaggioRichiestaModel();
		mess1.setDataInserimento(LocalDateTime.parse("2000-10-31T01:30:00.000-05:00", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")));
		mess1.setProfiloUtente(ProfiloUtente.CAA);
		mess1.setTesto("Lupus In Fabula");
		mess1.setUtente("DEL TORO");

		MessaggioRichiestaModel mess2 = new MessaggioRichiestaModel();
		mess2.setDataInserimento(LocalDateTime.parse("2000-11-31T01:30:00.000-05:00", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")));
		mess2.setProfiloUtente(ProfiloUtente.BACKOFFICE);
		mess2.setTesto("veni vidi vici");
		mess2.setUtente("ROMANO");

		listMessaggioRichiestaModel.add(mess1);
		listMessaggioRichiestaModel.add(mess2);

		Paginazione paginazione = new Paginazione();
		paginazione.setNumeroElementiPagina(10);
		paginazione.setPagina(0);

		Ordinamento ordinamento = new Ordinamento();
		ordinamento.setProprieta("dataInserimento");
		ordinamento.setOrdine(Ordine.DESC);

		String cuaa = "MCHGLN69H10L378R";
		List<SportelloFascicoloDto> responseAnagrafica = new ArrayList<SportelloFascicoloDto>();
		SportelloFascicoloDto sportelloFascicolo = new SportelloFascicoloDto();
		sportelloFascicolo.setIdentificativoSportello(18);
		List<String> listCuaa = new ArrayList<String>();
		listCuaa.add(cuaa);
		sportelloFascicolo.setCuaaList(listCuaa);
		responseAnagrafica.add(sportelloFascicolo);

		Mockito.when(anagrafica.ricercaFascicoli()).thenReturn(responseAnagrafica);

		mockMvc.perform(post(ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(idRichiesta)).concat("/messaggi")).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(listMessaggioRichiestaModel))).andExpect(status().isCreated());
	}

	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.EDIT_MESSAGGIO_RICHIESTA_TUTTI_COD })
	void insertMessaggiRichiestaFromRichiestaBadRequest() throws Exception {

		Long idRichiesta = 2501111111L;
		List<MessaggioRichiestaModel> listMessaggioRichiestaModel = new ArrayList<MessaggioRichiestaModel>();
		MessaggioRichiestaModel mess1 = new MessaggioRichiestaModel();
		mess1.setDataInserimento(LocalDateTime.parse("2000-10-31T01:30:00.000-05:00", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")));
		mess1.setProfiloUtente(ProfiloUtente.CAA);
		mess1.setTesto("Lupus In Fabula");
		mess1.setUtente("DEL TORO");

		MessaggioRichiestaModel mess2 = new MessaggioRichiestaModel();
		mess2.setDataInserimento(LocalDateTime.parse("2000-11-31T01:30:00.000-05:00", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")));
		mess2.setProfiloUtente(ProfiloUtente.BACKOFFICE);
		mess2.setTesto(
				"Nel mezzo del cammin di nostra vitami ritrovai per una selva oscura,ché la diritta via era smarrita.3Ahi quanto a dir qual era è cosa duraesta selva selvaggia e aspra e forteche nel pensier rinova la paura!6Tant’è amara che poco è più morte;ma per trattar del ben ch’i’ vi trovai,dirò de l’altre cose ch’i’ v’ ho scorte.9Io non so ben ridir com’i’ v’intrai,tant’era pien di sonno a quel puntoche la verace via abbandonai.12Ma poi ch’i’ fui al piè d’un colle giunto,là dove terminava quella valleche m’avea di paura il cor compunto,15guardai in alto e vidi le sue spallevestite già de’ raggi del pianetache mena dritto altrui per ogne calle.18Allor fu la paura un poco queta,che nel lago del cor m’era duratala notte ch’i’ passai con tanta pieta.21E come quei che con lena affannata,uscito fuor del pelago a la riva,si volge a l’acqua perigliosa e guata,24così l’animo mio, ch’ancor fuggiva,si volse a retro a rimirar lo passoche non lasciò già mai persona viva.27Poi ch’èi posato un poco il corpo lasso,ripresi via per la piaggia diserta,sì che ’l piè fermo sempre era ’l più basso.30Ed ecco, quasi al cominciar de l'erta,una lonza leggera e presta molto,che di pel macolato era coverta;33e non mi si partia dinanzi al volto,anzi ’mpediva tanto il mio cammino,ch’i’ fui per ritornar più volte vòlto.36Temp’era dal principio del mattino,e ’l sol montava ’n sù con quelle stellech’eran con lui quando l’amor divino39mosse di prima quelle cose belle;sì ch’a bene sperar m’era cagionedi quella fiera a la gaetta pelle42l’ora del tempo e la dolce stagione;ma non sì che paura non mi dessela vista che m'apparve d'un leone.45Questi parea che contra me venissecon la test’alta e con rabbiosa fame,sì che parea che l’aere ne tremesse.48Ed una lupa, che di tutte bramesembiava carca ne la sua magrezza,e molte genti fé già viver grame,51questa mi porse tanto di gravezzacon la paura ch’uscia di sua vista,ch’io perdei la speranza de l’altezza.54E qual è quei che volontieri acquista,e giugne ’l tempo che perder lo face,che ’n tutti suoi pensier piange e s’attrista;57tal mi fece la bestia sanza pace,che, venendomi ’ncontro, a poco a pocomi ripigneva là dove ’l sol tace.60Mentre ch’i’ rovinava in basso loco,dinanzi a li occhi mi si fu offertochi per lungo silenzio parea fioco.63Quando vidi costui nel gran diserto,Miserere di me, gridai a lui,qual che tu sii, od ombra od omo certo!.66Rispuosemi: Non omo, omo già fui,e li parenti miei furon lombardi,mantoani per patrïa ambedui.69Nacqui sub Iulio, ancor che fosse tardi,e vissi a Roma sotto ’l buono Augustonel tempo de li dèi falsi e bugiardi.72Poeta fui, e cantai di quel giustofigliuol d’Anchise che venne di Troia,poi che ’l superbo Ilïón fu combusto.75Ma tu perché ritorni a tanta noia?perché non sali il dilettoso montech’è principio e cagion di tutta gioia?.78Or se’ tu quel Virgilio e quella fonteche spandi di parlar sì largo fiume?,rispuos’io lui con vergognosa fronte.81O de li altri poeti onore e lume,vagliami ’l lungo studio e ’l grande amoreche m’ ha fatto cercar lo tuo volume.84Tu se’ lo mio maestro e ’l mio autore,tu se’ solo colui da cu’ io tolsilo bello stilo che m’ ha fatto onore.87Vedi la bestia per cu’ io mi volsi;aiutami da lei, famoso saggio,ch’ella mi fa tremar le vene e i polsi.90A te convien tenere altro vïaggio,rispuose, poi che lagrimar mi vide,se vuo’ campar d’esto loco selvaggio;93ché questa bestia, per la qual tu gride,non lascia altrui passar per la sua via,ma tanto lo ’mpedisce che l’uccide;96e ha natura sì malvagia e ria,che mai non empie la bramosa voglia,e dopo ’l pasto ha più fame che pria.99Molti son li animali a cui s’ammoglia,e più saranno ancora, infin che ’l veltroverrà, che la farà morir con doglia.102Questi non ciberà terra né peltro,ma sapïenza, amore e virtute,e sua nazion sarà tra feltro e feltro.105D@333");
		mess2.setUtente("ROMANO");

		listMessaggioRichiestaModel.add(mess1);
		listMessaggioRichiestaModel.add(mess2);

		Paginazione paginazione = new Paginazione();
		paginazione.setNumeroElementiPagina(10);
		paginazione.setPagina(0);

		Ordinamento ordinamento = new Ordinamento();
		ordinamento.setProprieta("dataInserimento");
		ordinamento.setOrdine(Ordine.DESC);

		mockMvc.perform(post(ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(idRichiesta)).concat("/messaggi")).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(listMessaggioRichiestaModel))).andExpect(status().isNotAcceptable());
	}

	private void utenteConMandato() throws Exception {
		String cuaa = "MCHGLN69H10L378R";
		List<SportelloFascicoloDto> responseAnagrafica = new ArrayList<SportelloFascicoloDto>();
		SportelloFascicoloDto sportelloFascicolo = new SportelloFascicoloDto();
		sportelloFascicolo.setIdentificativoSportello(18);
		List<String> listCuaa = new ArrayList<String>();
		listCuaa.add(cuaa);
		listCuaa.add("02167970223");
		sportelloFascicolo.setCuaaList(listCuaa);
		responseAnagrafica.add(sportelloFascicolo);

		Mockito.when(anagrafica.ricercaFascicoli()).thenReturn(responseAnagrafica);
	}

	private void utenteSenzaMandato() throws Exception {
		List<SportelloFascicoloDto> responseAnagrafica = new ArrayList<SportelloFascicoloDto>();

		Mockito.when(anagrafica.ricercaFascicoli()).thenReturn(responseAnagrafica);
	}

	@Test
	@WithMockUser(username = "UTENTECAA", roles = { Ruoli.VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_ENTE_COD })
	void ricercaRichiesteModificaSuolo() throws Exception {

		RichiestaModificaSuoloFilter filter = new RichiestaModificaSuoloFilter();
		filter.setTipo(TipoRichiestaModificaSuolo.ISTANZA_DI_RIESAME);

		filter.setListCuaaMandatoCaa(Arrays.asList("MCHGLN69H10L378R", "02167970223"));

		utenteConMandato();

		String URL = ApiUrls.RICHIESTA_MODIFICA_SUOLO;
		mockMvc.perform(get(URL).param("filter", String.valueOf(filter)).param("numeroElementiPagina", "10").param("ordine", "DESC").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "UTENTECAA", roles = { Ruoli.VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_ENTE_COD })
	void clearApplicationCache() throws Exception {

		applicationCache.refreshCacheAll();

	}

	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_TUTTI_COD })
	void getDichiaratiRichiesta() throws Exception {

		Long idRichiesta = 136449L;

		Paginazione paginazione = new Paginazione();
		paginazione.setNumeroElementiPagina(2);
		paginazione.setPagina(0);

		Ordinamento ordinamento = new Ordinamento();
		ordinamento.setOrdine(Ordine.DESC);

		MvcResult result = mockMvc.perform(get(ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(idRichiesta)).concat("/dichiarati") + "?numeroElementiPagina="
				+ paginazione.getNumeroElementiPagina() + "&pagina=" + paginazione.getPagina())).andExpect(jsonPath("$.count").value(6)).andReturn();

		String content = result.getResponse().getContentAsString();

		assertEquals(
				"{\"count\":6,\"risultati\":[{\"id\":136454,\"idRichiesta\":136449,\"statoRichiesta\":\"LAVORABILE\",\"aziendaAgricola\":{\"cuaa\":\"02167970223\",\"ragioneSociale\":null},\"campagna\":2022,\"codSezione\":\"D516\",\"tipoSuoloDichiarato\":\"FRU\",\"tipoSuoloRilevato\":\"BO\",\"areaOri\":700.838086,\"codiRileDichiarato\":\"638\",\"descRileDichiarato\":\"PRATO PERMANENTE (SENZA TARA)\",\"codiProdRileDichiarato\":\"000\",\"descProdRileDichiarato\":null,\"codiRilePrevalenteDich\":\"638\",\"descRilePrevalenteDich\":\"PRATO PERMANENTE (SENZA TARA)\",\"codiRileRilevato\":\"666\",\"descRileRilevato\":\"SEMINATIVI\",\"codiProdRileRilevato\":\"000\",\"descProdRileRilevato\":null,\"codiRilePrevalenteRil\":\"666\",\"descRilePrevalenteRil\":\"SEMINATIVI\",\"esito\":\"DA_LAVORARE\",\"idLavorazione\":null,\"idPoligonoDichiarato\":null,\"numeroMessaggi\":3,\"numeroDocumenti\":1,\"visible\":true,\"extent\":[671615.522823267,5119709.54528584,671655.208518708,5119747.97658312],\"utenteLavorazione\":null,\"statoLavorazione\":null,\"nuovaModificaBo\":false,\"nuovaModificaCaa\":false,\"visibileInOrtofoto\":null,\"tipoInterventoColturale\":null,\"interventoInizio\":null,\"interventoFine\":null},{\"id\":136456,\"idRichiesta\":136449,\"statoRichiesta\":\"LAVORABILE\",\"aziendaAgricola\":{\"cuaa\":\"02167970223\",\"ragioneSociale\":null},\"campagna\":2022,\"codSezione\":\"D516\",\"tipoSuoloDichiarato\":\"FRU\",\"tipoSuoloRilevato\":\"BO\",\"areaOri\":1558.093995,\"codiRileDichiarato\":\"650\",\"descRileDichiarato\":\"BOSCHI\",\"codiProdRileDichiarato\":\"000\",\"descProdRileDichiarato\":null,\"codiRilePrevalenteDich\":\"650\",\"descRilePrevalenteDich\":\"BOSCHI\",\"codiRileRilevato\":\"650\",\"descRileRilevato\":\"BOSCHI\",\"codiProdRileRilevato\":\"000\",\"descProdRileRilevato\":null,\"codiRilePrevalenteRil\":\"650\",\"descRilePrevalenteRil\":\"BOSCHI\",\"esito\":\"DA_LAVORARE\",\"idLavorazione\":null,\"idPoligonoDichiarato\":null,\"numeroMessaggi\":0,\"numeroDocumenti\":0,\"visible\":true,\"extent\":[669431.286968498,5120339.66359828,669502.312669583,5120394.99050178],\"utenteLavorazione\":null,\"statoLavorazione\":null,\"nuovaModificaBo\":false,\"nuovaModificaCaa\":false,\"visibileInOrtofoto\":null,\"tipoInterventoColturale\":null,\"interventoInizio\":null,\"interventoFine\":null}]}",
				content);
	}

	@Test
	@WithMockUser(username = "UTENTECAA", roles = { Ruoli.VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_ENTE_COD })
	void getDichiaratiRichiestaUtenteCaaConMandato() throws Exception {

		Long idRichiesta = 136449L;

		Paginazione paginazione = new Paginazione();
		paginazione.setNumeroElementiPagina(2);
		paginazione.setPagina(0);

		Ordinamento ordinamento = new Ordinamento();
		ordinamento.setOrdine(Ordine.DESC);

		utenteConMandato();

		MvcResult result = mockMvc.perform(get(ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(idRichiesta)).concat("/dichiarati") + "?numeroElementiPagina="
				+ paginazione.getNumeroElementiPagina() + "&pagina=" + paginazione.getPagina())).andExpect(jsonPath("$.count").value(6)).andReturn();

		String content = result.getResponse().getContentAsString();

		assertEquals(
				"{\"count\":6,\"risultati\":[{\"id\":136454,\"idRichiesta\":136449,\"statoRichiesta\":\"LAVORABILE\",\"aziendaAgricola\":{\"cuaa\":\"02167970223\",\"ragioneSociale\":null},\"campagna\":2022,\"codSezione\":\"D516\",\"tipoSuoloDichiarato\":\"FRU\",\"tipoSuoloRilevato\":\"BO\",\"areaOri\":700.838086,\"codiRileDichiarato\":\"638\",\"descRileDichiarato\":\"PRATO PERMANENTE (SENZA TARA)\",\"codiProdRileDichiarato\":\"000\",\"descProdRileDichiarato\":null,\"codiRilePrevalenteDich\":\"638\",\"descRilePrevalenteDich\":\"PRATO PERMANENTE (SENZA TARA)\",\"codiRileRilevato\":\"666\",\"descRileRilevato\":\"SEMINATIVI\",\"codiProdRileRilevato\":\"000\",\"descProdRileRilevato\":null,\"codiRilePrevalenteRil\":\"666\",\"descRilePrevalenteRil\":\"SEMINATIVI\",\"esito\":\"DA_LAVORARE\",\"idLavorazione\":null,\"idPoligonoDichiarato\":null,\"numeroMessaggi\":3,\"numeroDocumenti\":1,\"visible\":true,\"extent\":[671615.522823267,5119709.54528584,671655.208518708,5119747.97658312],\"utenteLavorazione\":null,\"statoLavorazione\":null,\"nuovaModificaBo\":false,\"nuovaModificaCaa\":false,\"visibileInOrtofoto\":null,\"tipoInterventoColturale\":null,\"interventoInizio\":null,\"interventoFine\":null},{\"id\":136456,\"idRichiesta\":136449,\"statoRichiesta\":\"LAVORABILE\",\"aziendaAgricola\":{\"cuaa\":\"02167970223\",\"ragioneSociale\":null},\"campagna\":2022,\"codSezione\":\"D516\",\"tipoSuoloDichiarato\":\"FRU\",\"tipoSuoloRilevato\":\"BO\",\"areaOri\":1558.093995,\"codiRileDichiarato\":\"650\",\"descRileDichiarato\":\"BOSCHI\",\"codiProdRileDichiarato\":\"000\",\"descProdRileDichiarato\":null,\"codiRilePrevalenteDich\":\"650\",\"descRilePrevalenteDich\":\"BOSCHI\",\"codiRileRilevato\":\"650\",\"descRileRilevato\":\"BOSCHI\",\"codiProdRileRilevato\":\"000\",\"descProdRileRilevato\":null,\"codiRilePrevalenteRil\":\"650\",\"descRilePrevalenteRil\":\"BOSCHI\",\"esito\":\"DA_LAVORARE\",\"idLavorazione\":null,\"idPoligonoDichiarato\":null,\"numeroMessaggi\":0,\"numeroDocumenti\":0,\"visible\":true,\"extent\":[669431.286968498,5120339.66359828,669502.312669583,5120394.99050178],\"utenteLavorazione\":null,\"statoLavorazione\":null,\"nuovaModificaBo\":false,\"nuovaModificaCaa\":false,\"visibileInOrtofoto\":null,\"tipoInterventoColturale\":null,\"interventoInizio\":null,\"interventoFine\":null}]}",
				content);
	}

	@Test
	@WithMockUser(username = "UTENTECAA", roles = { Ruoli.VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_ENTE_COD })
	void getDichiaratiRichiestaUtenteCaaSenzaMandato() throws Exception {

		Long idRichiesta = 136449L;

		Paginazione paginazione = new Paginazione();
		paginazione.setNumeroElementiPagina(2);
		paginazione.setPagina(0);

		Ordinamento ordinamento = new Ordinamento();
		ordinamento.setOrdine(Ordine.DESC);

		utenteSenzaMandato();

		MvcResult result = mockMvc.perform(get(ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(idRichiesta)).concat("/dichiarati") + "?numeroElementiPagina="
				+ paginazione.getNumeroElementiPagina() + "&pagina=" + paginazione.getPagina())).andExpect(content().string("Access is denied")).andReturn();

		String content = result.getResponse().getContentAsString();
		assertEquals("Access is denied", content);
	}

	@Test
	@WithMockUser(username = "UTENTECAA", roles = { Ruoli.VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_ENTE_COD, Ruoli.EDIT_RICHIESTA_MODIFICA_SUOLO_ENTE_COD })
	void updateDichiarati() throws Exception {

		// Aggiorna solo poligoni a cui non sono associate Lavorazioni

		Long idRichiesta = 136449L;

		Paginazione paginazione = new Paginazione();
		paginazione.setNumeroElementiPagina(2);
		paginazione.setPagina(0);

		Ordinamento ordinamento = new Ordinamento();
		ordinamento.setOrdine(Ordine.DESC);

		utenteConMandato();

		MvcResult result = mockMvc.perform(get(ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(idRichiesta)).concat("/dichiarati") + "?numeroElementiPagina="
				+ paginazione.getNumeroElementiPagina() + "&pagina=" + paginazione.getPagina())).andExpect(jsonPath("$.count").value(6)).andReturn();

		String content = result.getResponse().getContentAsString();

		RisultatiPaginati<SuoloDichiaratoLavorazioneDto> suoloDichiaratoLavorazioneDtoList = objectMapper.readValue(content, new TypeReference<RisultatiPaginati<SuoloDichiaratoLavorazioneDto>>() {
		});

		SuoloDichiaratoLavorazioneDto dichiaratoDto = suoloDichiaratoLavorazioneDtoList.getRisultati().get(0);
		dichiaratoDto.setVisibileInOrtofoto(true);
		List<SuoloDichiaratoLavorazioneDto> dichiaratoDtoInputList = new ArrayList<>();
		dichiaratoDtoInputList.add(dichiaratoDto);

		ResultActions resultAction = mockMvc.perform(put(ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(idRichiesta)).concat("/aggiornaDichiarati"))
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dichiaratoDtoInputList))).andExpect(status().isOk());

		resultAction.andDo(print()).andExpect(status().isOk());
	}

}
