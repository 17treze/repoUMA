package it.tndigitale.a4g.uma.business.service.richiesta.lavorazioni;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.uma.business.persistence.entity.AmbitoLavorazione;
import it.tndigitale.a4g.uma.business.persistence.entity.FabbisognoModel;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.TipoCarburante;
import it.tndigitale.a4g.uma.business.persistence.entity.UnitaDiMisura;
import it.tndigitale.a4g.uma.business.persistence.repository.FabbisognoDao;
import it.tndigitale.a4g.uma.business.persistence.repository.RichiestaCarburanteDao;
import it.tndigitale.a4g.uma.business.service.client.UmaDotazioneTecnicaClient;
import it.tndigitale.a4g.uma.business.service.client.UmaTerritorioClient;
import it.tndigitale.a4g.uma.business.service.utente.AbilitazioniComponent;
import it.tndigitale.a4g.uma.dto.richiesta.DichiarazioneDto;
import it.tndigitale.a4g.uma.dto.richiesta.DichiarazioneFabbricatoDto;
import it.tndigitale.a4g.uma.dto.richiesta.FabbisognoDto;
import it.tndigitale.a4g.uma.dto.richiesta.LavorazioneDto;
import it.tndigitale.a4g.uma.dto.richiesta.RaggruppamentoLavorazioniDto;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente")
class LavorazioniFabbricatiControllerTest {
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private RichiestaCarburanteDao richiestaCarburanteDao;
	@Autowired
	private FabbisognoDao fabbisognoDao;
	@MockBean
	private AbilitazioniComponent abilitazioniComponent;
	@MockBean
	private Clock clock;
	@MockBean
	private UmaDotazioneTecnicaClient dotazioneTecnicaClient;
	@MockBean
	private UmaTerritorioClient territorioClient;

	static final String RISCALDAMENTO_SERRA = "RISCALDAMENTO SERRA";

	@BeforeEach
	private void mockUtenteConnesso() throws Exception {
		Mockito.when(abilitazioniComponent.checkRicercaRichiestaDiCarburante(Mockito.anyLong())).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkRicercaDomandeUma()).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkModificaRichiestaCarburante(Mockito.anyLong())).thenReturn(true);
	}

	@Test
	@Sql(scripts = "/sql/lavorazioni/get_lavorazioni_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/lavorazioni/get_lavorazioni_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getDichiarazioniSerreSuccessful() throws Exception {

		MvcResult controllerResponse = mockMvc
				.perform(get("/api/v1/richieste/52/fabbisogni-fabbricati?ambito=" + AmbitoLavorazione.SERRE.name()))
				.andExpect(status().is2xxSuccessful()).andExpect(status().isOk()).andReturn();

		List<DichiarazioneFabbricatoDto> listRaggruppamentoLavorazioniDto = objectMapper.readValue(
				controllerResponse.getResponse().getContentAsString(),
				new TypeReference<ArrayList<DichiarazioneFabbricatoDto>>() {
				});

		DichiarazioneFabbricatoDto dichiarazioneFabbricatoDto = listRaggruppamentoLavorazioniDto.get(0);

		assertEquals(9228, dichiarazioneFabbricatoDto.getIdFabbricato());
		assertEquals(1, dichiarazioneFabbricatoDto.getDichiarazioni().size());
		assertEquals(16003L, dichiarazioneFabbricatoDto.getDichiarazioni().get(0).getLavorazioneId());
		assertEquals(1, dichiarazioneFabbricatoDto.getDichiarazioni().get(0).getFabbisogni().size());
		assertEquals(TipoCarburante.GASOLIO,
				dichiarazioneFabbricatoDto.getDichiarazioni().get(0).getFabbisogni().get(0).getCarburante());
		assertEquals("12.0", dichiarazioneFabbricatoDto.getDichiarazioni().get(0).getFabbisogni().get(0).getQuantita().toString());
	}

	@Test
	@Sql(scripts = "/sql/lavorazioni/get_lavorazioni_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/lavorazioni/get_lavorazioni_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getLavorazioniSerreSuccessful() throws Exception {

		MvcResult controllerResponse = mockMvc
				.perform(get("/api/v1/richieste/52/lavorazioni?ambito=" + AmbitoLavorazione.SERRE.name()))
				.andExpect(status().is2xxSuccessful()).andExpect(status().isOk()).andReturn();

		List<RaggruppamentoLavorazioniDto> listRaggruppamentoLavorazioniDto = objectMapper.readValue(
				controllerResponse.getResponse().getContentAsString(),
				new TypeReference<ArrayList<RaggruppamentoLavorazioniDto>>() {
				});
		RaggruppamentoLavorazioniDto coltivazioniSottoSerra = listRaggruppamentoLavorazioniDto.get(0);
		LavorazioneDto riscaldamentoSerra = coltivazioniSottoSerra.getLavorazioni().get(0);
		assertEquals(19, coltivazioniSottoSerra.getIndice());
		assertEquals("COLTIVAZIONI SOTTO SERRA", coltivazioniSottoSerra.getNome());
		assertEquals(1, riscaldamentoSerra.getIndice());
		assertEquals(RISCALDAMENTO_SERRA, riscaldamentoSerra.getNome());
		assertEquals(UnitaDiMisura.MC, riscaldamentoSerra.getUnitaDiMisura());
	}

	@Test
	@Sql(scripts = "/sql/lavorazioni/get_lavorazioni_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/lavorazioni/get_lavorazioni_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getLavorazioniSerreFabbricatiErrorNoAmbito() throws Exception {
		Exception exception = assertThrows(NestedServletException.class, () -> {
			mockMvc
			.perform(get("/api/v1/richieste/52/fabbisogni-fabbricati"));
		}); 

		assertEquals("Specificare un tipo di lavorazione", exception.getCause().getMessage());
	}

	@Test
	@Sql(scripts = "/sql/lavorazioni/get_lavorazioni_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/lavorazioni/get_lavorazioni_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getLavorazioniFabbricatiSuccessful() throws Exception {
		mockGetFabbricatiDotazioneTecnica();

		MvcResult controllerResponse = mockMvc
				.perform(get("/api/v1/richieste/2/lavorazioni?ambito=" + AmbitoLavorazione.FABBRICATI.name()))
				.andExpect(status().is2xxSuccessful()).andExpect(status().isOk()).andReturn();

		List<RaggruppamentoLavorazioniDto> listRaggruppamentoLavorazioniDto = objectMapper.readValue(
				controllerResponse.getResponse().getContentAsString(),
				new TypeReference<ArrayList<RaggruppamentoLavorazioniDto>>() {
				});
		RaggruppamentoLavorazioniDto trasfOlive = listRaggruppamentoLavorazioniDto.get(0);
		LavorazioneDto riscaldamentoMasse = trasfOlive.getLavorazioni().get(0);
		assertEquals(28, trasfOlive.getIndice());
		assertEquals("TRASFORMAZIONE DELLE OLIVE IN OLIO", trasfOlive.getNome());
		assertEquals(9, riscaldamentoMasse.getIndice());
		assertEquals("RISCALDAMENTO DELLE MASSE", riscaldamentoMasse.getNome());
		assertEquals(UnitaDiMisura.QLI_OLIVE, riscaldamentoMasse.getUnitaDiMisura());
	}

	@Test
	@Sql(scripts = "/sql/lavorazioni/get_lavorazioni_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/lavorazioni/get_lavorazioni_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getDichiarazioniFabbricatiSuccessful() throws Exception {

		MvcResult controllerResponse = mockMvc
				.perform(get("/api/v1/richieste/2/fabbisogni-fabbricati?ambito=" + AmbitoLavorazione.FABBRICATI.name()))
				.andExpect(status().is2xxSuccessful()).andExpect(status().isOk()).andReturn();

		List<DichiarazioneFabbricatoDto> listRaggruppamentoLavorazioniDto = objectMapper.readValue(
				controllerResponse.getResponse().getContentAsString(),
				new TypeReference<ArrayList<DichiarazioneFabbricatoDto>>() {
				});

		DichiarazioneFabbricatoDto dichiarazioneFabbricatoDto = listRaggruppamentoLavorazioniDto.get(0);

		assertEquals(9229, dichiarazioneFabbricatoDto.getIdFabbricato());
		assertEquals(1, dichiarazioneFabbricatoDto.getDichiarazioni().size());
		assertEquals(16012L, dichiarazioneFabbricatoDto.getDichiarazioni().get(0).getLavorazioneId());
		assertEquals(1, dichiarazioneFabbricatoDto.getDichiarazioni().get(0).getFabbisogni().size());
		assertEquals(TipoCarburante.GASOLIO,
				dichiarazioneFabbricatoDto.getDichiarazioni().get(0).getFabbisogni().get(0).getCarburante());
		assertEquals("13.0", dichiarazioneFabbricatoDto.getDichiarazioni().get(0).getFabbisogni().get(0).getQuantita().toString());
	}

	@Test
	@Sql(scripts = "/sql/lavorazioni/post_lavorazioni_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/lavorazioni/post_lavorazioni_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postDichiarazioniSerre() throws Exception {	
		List<DichiarazioneFabbricatoDto> dichiarazioniFabbricati = new ArrayList<>();
		DichiarazioneFabbricatoDto dichiarazioneFabbricato = new DichiarazioneFabbricatoDto();
		dichiarazioneFabbricato.setIdFabbricato(9228L);

		List<DichiarazioneDto> dichiarazioni = new ArrayList<>();
		DichiarazioneDto dichiarazione = new DichiarazioneDto();
		dichiarazione.setLavorazioneId(16003L);

		List<FabbisognoDto> fabbisogni = new ArrayList<>();
		FabbisognoDto fabbisogno = new FabbisognoDto();
		fabbisogno.setCarburante(TipoCarburante.GASOLIO);
		fabbisogno.setQuantita(BigDecimal.valueOf(999));
		fabbisogni.add(fabbisogno);

		dichiarazione.setFabbisogni(fabbisogni);
		dichiarazioni.add(dichiarazione);
		dichiarazioneFabbricato.setDichiarazioni(dichiarazioni);
		dichiarazioniFabbricati.add(dichiarazioneFabbricato);


		String requestBody = objectMapper.writeValueAsString(dichiarazioniFabbricati);


		mockMvc.perform(
				post("/api/v1/richieste/52/fabbisogni-fabbricati").contentType(MediaType.APPLICATION_JSON).content(requestBody))
		.andExpect(status().is2xxSuccessful()).andExpect(status().isOk()).andReturn();

		RichiestaCarburanteModel richiestaCarburanteModel = getDomandaUma(52L);

		List<FabbisognoModel> listFabbisogno = fabbisognoDao
				.findByRichiestaCarburante_id(richiestaCarburanteModel.getId());

		if (CollectionUtils.isEmpty(listFabbisogno)) {
			fail();
		}

		assertEquals(TipoCarburante.GASOLIO, listFabbisogno.get(1).getCarburante());
		assertEquals("999.0", listFabbisogno.get(1).getQuantita().toString());
		assertEquals(16003L, listFabbisogno.get(1).getLavorazioneModel().getId());
	}

	@Test
	@Sql(scripts = "/sql/lavorazioni/post_lavorazioni_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/lavorazioni/post_lavorazioni_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postDichiarazioniSerreErrorNoDichiarazioni() throws Exception {	
		String requestBody = objectMapper.writeValueAsString(new ArrayList<>());

		Exception exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(
					post("/api/v1/richieste/52/fabbisogni-fabbricati").contentType(MediaType.APPLICATION_JSON).content(requestBody));
		}); 

		assertEquals("Non ci sono lavorazioni da dichiarare", exception.getCause().getMessage());
	}

	@Test
	@Sql(scripts = "/sql/lavorazioni/post_lavorazioni_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/lavorazioni/post_lavorazioni_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postDichiarazioniFabbricati() throws Exception {
		List<DichiarazioneFabbricatoDto> dichiarazioniFabbricati = new ArrayList<>();
		DichiarazioneFabbricatoDto dichiarazioneFabbricato = new DichiarazioneFabbricatoDto();
		dichiarazioneFabbricato.setIdFabbricato(9229L);

		List<DichiarazioneDto> dichiarazioni = new ArrayList<>();
		DichiarazioneDto dichiarazione = new DichiarazioneDto();
		dichiarazione.setLavorazioneId(16012L);

		List<FabbisognoDto> fabbisogni = new ArrayList<>();
		FabbisognoDto fabbisogno = new FabbisognoDto();
		fabbisogno.setCarburante(TipoCarburante.GASOLIO);
		fabbisogno.setQuantita(BigDecimal.valueOf(888));
		fabbisogni.add(fabbisogno);

		dichiarazione.setFabbisogni(fabbisogni);
		dichiarazioni.add(dichiarazione);
		dichiarazioneFabbricato.setDichiarazioni(dichiarazioni);
		dichiarazioniFabbricati.add(dichiarazioneFabbricato);

		String requestBody = objectMapper.writeValueAsString(dichiarazioniFabbricati);

		mockMvc.perform(
				post("/api/v1/richieste/2/fabbisogni-fabbricati").contentType(MediaType.APPLICATION_JSON).content(requestBody))
		.andExpect(status().is2xxSuccessful()).andExpect(status().isOk()).andReturn();

		RichiestaCarburanteModel richiestaCarburanteModel = getDomandaUma(2L);

		List<FabbisognoModel> listFabbisogno = fabbisognoDao
				.findByRichiestaCarburante_id(richiestaCarburanteModel.getId());

		if (CollectionUtils.isEmpty(listFabbisogno)) {
			fail();
		}

		assertEquals(TipoCarburante.GASOLIO, listFabbisogno.get(0).getCarburante());
		assertEquals("888.0", listFabbisogno.get(0).getQuantita().toString());
		assertEquals(16012L, listFabbisogno.get(0).getLavorazioneModel().getId());
	}

	private RichiestaCarburanteModel getDomandaUma(Long idDomanda) {
		Optional<RichiestaCarburanteModel> domandaOpt = richiestaCarburanteDao.findById(idDomanda);

		if (!domandaOpt.isPresent()) {
			fail();
		}
		return domandaOpt.get();
	}

	private void mockGetFabbricatiDotazioneTecnica() {
		/*
		List<FabbricatoAgsDto> getFabbricatiResponse = new ArrayList<>();
		FabbricatoAgsDto fabbricato = new FabbricatoAgsDto();
		fabbricato.setIdAgs(333L);
		fabbricato.setTipoFabbricato("FRANTOIO");
		fabbricato.setTipoFabbricatoCodice("000025");
		getFabbricatiResponse.add(fabbricato);

		fabbricato = new FabbricatoAgsDto();
		fabbricato.setIdAgs(334L);
		fabbricato.setTipoFabbricato("FRANTOIO");
		fabbricato.setTipoFabbricatoCodice("000025");
		getFabbricatiResponse.add(fabbricato);

		Mockito.when(dotazioneTecnicaClient.getFabbricati(Mockito.any(), Mockito.any()))
		.thenReturn(getFabbricatiResponse);
		*/
	}
}
