package it.tndigitale.a4gistruttoria;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.NestedServletException;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.dto.DomandaCollegata;
import it.tndigitale.a4gistruttoria.dto.DomandeCollegateExport;
import it.tndigitale.a4gistruttoria.dto.DomandeCollegateImport;
import it.tndigitale.a4gistruttoria.dto.PageResultWrapper;
import it.tndigitale.a4gistruttoria.dto.Processo;
import it.tndigitale.a4gistruttoria.dto.ProcessoFilter;
import it.tndigitale.a4gistruttoria.dto.SogliaAcquisizioneFilter;
import it.tndigitale.a4gistruttoria.dto.TrasmissioneBdnaFilter;
import it.tndigitale.a4gistruttoria.dto.TrasmissioneBdnaFilter.StatoTrasmissioneBdna;
import it.tndigitale.a4gistruttoria.dto.antimafia.DichiarazioneAntimafia;
import it.tndigitale.a4gistruttoria.repository.model.StatoProcesso;
import it.tndigitale.a4gistruttoria.repository.model.TipoProcesso;
import it.tndigitale.a4gistruttoria.service.AntimafiaServiceImpl;
import it.tndigitale.a4gistruttoria.service.ImportaDatiSuperficieService;
import it.tndigitale.a4gistruttoria.service.ProcessoService;
import it.tndigitale.a4gistruttoria.util.ConsumeExternalRestApi;
import it.tndigitale.a4gistruttoria.util.ConsumeExternalRestApi4Ags;
import it.tndigitale.a4gistruttoria.util.MapperWrapper;
import it.tndigitale.a4gistruttoria.util.StatoDichiarazioneEnum;
import it.tndigitale.a4gistruttoria.util.StatoDomandaCollegata;
import it.tndigitale.a4gistruttoria.util.TipoDomandaCollegata;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
// @AutoConfigureTestDatabase
public class AntimafiaApplicationTest {

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired private MapperWrapper mapperWrapper;
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ProcessoService processoService;
	@MockBean
	private RestTemplate restTemplate;
	@Value("${a4gistruttoria.srt.imprese.importorichiesto.uri}")
	private String urlSrtImpreseImportoRichiesto;
	@Value("${a4gistruttoria.ags.uri}")
	private String agsUrl;
	@Value("${a4gistruttoria.a4gfascicolo.uri}")
	private String urlA4gfascicolo;
	@Value("${a4gistruttoria.proxy.uri}")
	private String a4gproxyUrl;
	@SpyBean
	private AntimafiaServiceImpl antimafiaService;
	@SpyBean
	private ConsumeExternalRestApi consumeExternalRestApi;
	@Autowired
	private ImportaDatiSuperficieService importaDatiSuperficieService;
	@MockBean
	private ConsumeExternalRestApi4Ags consumeExternalRestApiA4gs;
	
	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.EDITA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void controlloIstruttoriaAntimafiaC_01() throws Exception {
		List<Long> idsDichiarazione = new ArrayList<>();
		idsDichiarazione.add(10L);
		String cuaa = "00123890220";
		String params10 = "{\"provinciaSede\":\"TN\",\"numeroREASede\":\"76693\"}";

		mockFascicoloGetDichiarazione(idsDichiarazione.get(0));
		mockAgsControlloValidita(cuaa, true);
		mockParixDettaglioCompleto(params10, cuaa);
		this.mockMvc.perform(post("/api/v1/antimafia/controlla").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(idsDichiarazione)))
				.andExpect(status().is2xxSuccessful());
		await().atMost(1000, TimeUnit.SECONDS).until(processoInEsecuzione());
	}

	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.EDITA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void controlloIstruttoriaAntimafiaDCM_01() throws Exception {
		List<Long> idsDichiarazione = new ArrayList<>();
		idsDichiarazione.add(20L);
		String cuaa = "ZMRLSS91R12H612S";
		String params20 = "{\"provinciaSede\":\"TN\",\"numeroREASede\":\"213893\"}";

		mockFascicoloGetDichiarazione(idsDichiarazione.get(0));
		mockAgsControlloValidita(cuaa, false); // DCM_01
		mockParixDettaglioCompleto(params20, cuaa);

		this.mockMvc.perform(post("/api/v1/antimafia/controlla").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(idsDichiarazione)))
				.andExpect(status().is2xxSuccessful());

		await().atMost(1000, TimeUnit.SECONDS).until(processoInEsecuzione());
	}

	private Callable<Boolean> processoInEsecuzione() {
		return new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				ProcessoFilter processoFilter = new ProcessoFilter();
				processoFilter.setStatoProcesso(StatoProcesso.RUN);
				processoFilter.setTipoProcesso(TipoProcesso.CONTROLLO_ANTIMAFIA);
				List<Processo> processi = processoService.getProcessi(processoFilter);
				if (processi != null && !processi.isEmpty()) {
					return Boolean.FALSE;
				} else {
					return Boolean.TRUE;
				}
			}
		};
	}
	
	private Callable<Boolean> processoInEsecuzioneImportPSR() {
		return new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				ProcessoFilter processoFilter = new ProcessoFilter();
				// processoFilter.setStatoProcesso(StatoProcesso.RUN.getStatoProcesso());
				processoFilter.setTipoProcesso(TipoProcesso.IMPORTA_DOMANDE_PSR_SUPERFICIE);

				List<Processo> processi = processoService.getProcessi(processoFilter);
				if (processi.isEmpty()) {
					return Boolean.FALSE;
				} else {
					// esiste un processo in stato run
					if (processi.stream().anyMatch(processo -> processo.getStatoProcesso().equals(StatoProcesso.RUN))) {
						return Boolean.FALSE;
					} else {
						return Boolean.TRUE;
					}
				}
			}
		};
	}


	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.EDITA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void controlloIstruttoriaAntimafiaDCM_02() throws Exception {
		List<Long> idsDichiarazione = new ArrayList<>();
		idsDichiarazione.add(30L);
		String cuaa = "TRRCST78B08C794X";
		String params30 = "{\"provinciaSede\":\"TN\",\"numeroREASede\":\"173710\"}";

		mockFascicoloGetDichiarazione(idsDichiarazione.get(0));
		mockAgsControlloValidita(cuaa, true); // DCM_02
		mockParixDettaglioCompleto(params30, cuaa);
		this.mockMvc.perform(post("/api/v1/antimafia/controlla").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(idsDichiarazione)))
				.andExpect(status().is2xxSuccessful());
		await().atMost(1000, TimeUnit.SECONDS).until(processoInEsecuzione());
	}

	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.EDITA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void controlloIstruttoriaAntimafiaDCM_03() throws Exception {
		List<Long> idsDichiarazione = new ArrayList<>();
		idsDichiarazione.add(40L);
		String cuaa = "02480020227";
		String params40 = "{\"provinciaSede\":\"TN\",\"numeroREASede\":\"227723\"}";
		mockFascicoloGetDichiarazione(idsDichiarazione.get(0));
		mockAgsControlloValidita(cuaa, false); // DCM_03
		mockParixDettaglioCompleto(params40, cuaa);
		this.mockMvc.perform(post("/api/v1/antimafia/controlla").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(idsDichiarazione)))
				.andExpect(status().is2xxSuccessful());
		await().atMost(1000, TimeUnit.SECONDS).until(processoInEsecuzione());
	}

	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.EDITA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void controlloIstruttoriaAntimafiaDCM_04() throws Exception {
		List<Long> idsDichiarazione = new ArrayList<>();
		idsDichiarazione.add(50L);
		String cuaa = "01797410220";
		String params50 = "{\"provinciaSede\":\"TN\",\"numeroREASede\":\"178293\"}";
		mockFascicoloGetDichiarazione(idsDichiarazione.get(0));
		mockAgsControlloValidita(cuaa, false); // DCM_04
		mockParixDettaglioCompleto(params50, cuaa);
		this.mockMvc.perform(post("/api/v1/antimafia/controlla").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(idsDichiarazione)))
				.andExpect(status().is2xxSuccessful());
		await().atMost(1000, TimeUnit.SECONDS).until(processoInEsecuzione());
	}

	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.EDITA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void controlloIstruttoriaAntimafiaDCM_05() throws Exception {
		List<Long> idsDichiarazione = new ArrayList<>();
		idsDichiarazione.add(60L);
		String cuaa = "01567880222";
		String params60 = "{\"provinciaSede\":\"TN\",\"numeroREASede\":\"161720\"}";
		mockFascicoloGetDichiarazione(idsDichiarazione.get(0));
		mockAgsControlloValidita(cuaa, true); // DCM_05
		mockParixDettaglioCompleto(params60, cuaa);
		this.mockMvc.perform(post("/api/v1/antimafia/controlla").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(idsDichiarazione)))
				.andExpect(status().is2xxSuccessful());
		await().atMost(1000, TimeUnit.SECONDS).until(processoInEsecuzione());
	}

	@Test(expected = NestedServletException.class)
	@WithMockUser(username = "utente", roles = { Ruoli.EDITA_ISTRUTTORIA_ANTIMAFIA_COD })
	@SqlGroup({ @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:IstruttoriaAntimafia/beforeVerificaProcessoAttivoTestRun.sql"),
			@Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:IstruttoriaAntimafia/afterVerificaProcessoAttivoTestRun.sql") })
	public void verificaProcessoAttivo() throws Exception {
		List<Long> idsDichiarazione = new ArrayList<>();
		idsDichiarazione.add(60L);
		this.mockMvc.perform(post("/api/v1/antimafia/controlla").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(idsDichiarazione)))
		/* .andExpect(status().is5xxServerError()).andExpect(content().string(containsString("BRIAMPRT001"))) */;
		await().atMost(1000, TimeUnit.SECONDS).until(processoInEsecuzione());
	}

	// TODO: TEST ECCEZIONI

	// @Test
	@WithMockUser(username = "utente", roles = { Ruoli.EDITA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void controlloIstruttoriaAntimafia() throws Exception {
		List<Long> idsDichiarazione = new ArrayList<>();
		List<String> cuaaDichiarazioni = new ArrayList<>();

		// body richiesta + ESITO ATTESO:
		idsDichiarazione.add(10L); // C_01
		idsDichiarazione.add(20L); // DCM_01
		idsDichiarazione.add(30L); // DCM_02
		idsDichiarazione.add(40L); // DCM_03
		idsDichiarazione.add(50L); // ECCEZIONE
		idsDichiarazione.add(60L); // DCM_04
		idsDichiarazione.add(70L); // ECCEZIONE2
		idsDichiarazione.add(80L); // DCM_05

		// cuaa corrispondenti
		cuaaDichiarazioni.add("00123890220");
		cuaaDichiarazioni.add("ZMRLSS91R12H612S");
		cuaaDichiarazioni.add("TRRCST78B08C794X");
		cuaaDichiarazioni.add("02480020227");
		cuaaDichiarazioni.add("RSOLLN57L58H612L");
		cuaaDichiarazioni.add("01797410220");
		cuaaDichiarazioni.add("NDRLLN40R56I042K");
		cuaaDichiarazioni.add("01567880222");

		// Mock parix: params
		String params10 = "{\"provinciaSede\":\"TN\",\"numeroREASede\":\"76693\"}";
		String params20 = "{\"provinciaSede\":\"TN\",\"numeroREASede\":\"213893\"}";
		String params30 = "{\"provinciaSede\":\"TN\",\"numeroREASede\":\"173710\"}";
		String params40 = "{\"provinciaSede\":\"TN\",\"numeroREASede\":\"227723\"}";
		String params50 = "{\"provinciaSede\":\"TN\",\"numeroREASede\":\"225855\"}";
		String params60 = "{\"provinciaSede\":\"TN\",\"numeroREASede\":\"178293\"}";
		String params70 = "{\"provinciaSede\":\"TN\",\"numeroREASede\":\"145076\"}";
		String params80 = "{\"provinciaSede\":\"TN\",\"numeroREASede\":\"161720\"}";

		// MOCKS
		mockParixDettaglioCompleto(params10, cuaaDichiarazioni.get(0));
		mockParixDettaglioCompleto(params20, cuaaDichiarazioni.get(1));
		mockParixDettaglioCompleto(params30, cuaaDichiarazioni.get(2));
		mockParixDettaglioCompleto(params40, cuaaDichiarazioni.get(3));
		mockParixDettaglioCompleto(params50, cuaaDichiarazioni.get(4));
		mockParixDettaglioCompleto(params60, cuaaDichiarazioni.get(5));
		mockParixDettaglioCompleto(params70, cuaaDichiarazioni.get(6));
		mockParixDettaglioCompleto(params80, cuaaDichiarazioni.get(7));

		mockFascicoloGetDichiarazione(idsDichiarazione.get(0));
		mockFascicoloGetDichiarazione(idsDichiarazione.get(1));
		mockFascicoloGetDichiarazione(idsDichiarazione.get(2));
		mockFascicoloGetDichiarazione(idsDichiarazione.get(3));
		mockFascicoloGetDichiarazione(idsDichiarazione.get(4));
		mockFascicoloGetDichiarazione(idsDichiarazione.get(5));
		mockFascicoloGetDichiarazione(idsDichiarazione.get(6));
		mockFascicoloGetDichiarazione(idsDichiarazione.get(7));

		mockAgsControlloValidita(cuaaDichiarazioni.get(0), true); // C_01
		mockAgsControlloValidita(cuaaDichiarazioni.get(1), false); // DCM_01
		mockAgsControlloValidita(cuaaDichiarazioni.get(2), true); // DCM_02
		mockAgsControlloValidita(cuaaDichiarazioni.get(3), false); // DCM_03
		mockAgsControlloValidita(cuaaDichiarazioni.get(4), true); // ECCEZIONE
		mockAgsControlloValidita(cuaaDichiarazioni.get(5), false); // DCM_04
		mockAgsControlloValidita(cuaaDichiarazioni.get(6), true); // ECCEZIONE2
		mockAgsControlloValidita(cuaaDichiarazioni.get(7), true); // DCM_05

		// chiamata
		this.mockMvc.perform(post("/api/v1/antimafia/controlla").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(idsDichiarazione)))
				.andExpect(status().is2xxSuccessful());
		await().atMost(1000, TimeUnit.SECONDS).until(processoInEsecuzione());
	}

	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void getElaborazioniIstruttoriaAntimafia() throws Exception {
		this.mockMvc.perform(get("/api/v1/antimafia/506")).andExpect(status().isOk()).andExpect(content().string(containsString("gestite")));
	}

	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void getTrasmissioniBdna() throws Exception {
		this.mockMvc.perform(get("/api/v1/antimafia/domandecollegate/trasmissione")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.EDITA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void importaDatiStrutturali() throws Exception {
		String url = "http://localhost:8080/srt/api/v1/imprese/{cuaa}/importo-richiesto?data-modifica={param}";
		Mockito.when(restTemplate.getForObject(Mockito.eq(url), Mockito.eq(String.class), Mockito.eq("NDRFPP72P06H612P"), Mockito.eq("31/07/2018"))).thenReturn(getResponseImportaDatiStrutturali());

		DomandeCollegateImport domandeCollegateImport = new DomandeCollegateImport();
		List<String> listCuaa = new ArrayList<>();
		listCuaa.add("NDRFPP72P06H612P");
		domandeCollegateImport.setCuaa(listCuaa);
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		domandeCollegateImport.setDataPresentazione(df.parse("31/07/2018"));
		domandeCollegateImport.setImporto(new BigDecimal(30000.00));

		MvcResult result = this.mockMvc.perform(put("/api/v1/antimafia/importadatistrutturali").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(domandeCollegateImport)))
				.andExpect(status().is2xxSuccessful()).andReturn();
		String contentAsString = result.getResponse().getContentAsString();
		List<DomandaCollegata> domande = mapperWrapper.readValue(contentAsString, new TypeReference<List<DomandaCollegata>>(){});
		assertThat(domande, is(not(empty())));
		assertEquals(domande.get(0).getImportoRichiesto(), new BigDecimal("95000.12"));
		assertEquals(domande.get(0).getCuaa(),"NDRFPP72P06H612P");
	}

	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.EDITA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void importaDatiSuperficieSuccess() throws Exception {
		// creazione input api rest
		DomandeCollegateImport domandeCollegateImport = new DomandeCollegateImport();
		domandeCollegateImport.setCuaa(Arrays.asList("TRRCST78B08C794X"));
		domandeCollegateImport.setImporto(BigDecimal.TEN);
		domandeCollegateImport.setDataPresentazione(new Date());
		domandeCollegateImport.setAnniCampagna(Arrays.asList(2018));
		
		// mock chiamata AGS
		String params = "?params=".concat(URLEncoder.encode(objectMapper.writeValueAsString(domandeCollegateImport), StandardCharsets.UTF_8.name()));
		Mockito.doReturn(getDomandaPsrSuperficieDaAgs()).when(restTemplate).getForObject(Mockito.eq(new URI(agsUrl.concat("domandePSR").concat("/").concat(params))), Mockito.eq(String.class));
		Mockito.doNothing().when(consumeExternalRestApiA4gs).sincronizzaEsitiAntimafiaAgs(Mockito.any());
		// chiamata al servizio @Async
		List<DomandaCollegata> domande = importaDatiSuperficieService.importaDatiSuperficie(domandeCollegateImport);
		assertThat(domande, is(not(empty())));
		assertEquals(domande.get(0).getImportoRichiesto(), new BigDecimal("12779.53"));
		assertEquals(domande.get(0).getCuaa(),"TRRCST78B08C794X");
	}

	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.EDITA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void importaDatiSuperficieEsistente() throws Exception {
		// creazione input api rest
		DomandeCollegateImport domandeCollegateImport = new DomandeCollegateImport();
		domandeCollegateImport.setCuaa(Arrays.asList("TRRCST78B08C794X"));
		domandeCollegateImport.setImporto(BigDecimal.TEN);
		domandeCollegateImport.setDataPresentazione(new Date());
		domandeCollegateImport.setAnniCampagna(Arrays.asList(2018));
		// mock chiamata AGS
		String params = "?params=".concat(URLEncoder.encode(objectMapper.writeValueAsString(domandeCollegateImport), StandardCharsets.UTF_8.name()));
		Mockito.doReturn(getDomandaPsrSuperficieEsistente()).when(restTemplate).getForObject(Mockito.eq(new URI(agsUrl.concat("domandePSR").concat("/").concat(params))), Mockito.eq(String.class));
		Mockito.doNothing().when(consumeExternalRestApiA4gs).sincronizzaEsitiAntimafiaAgs(Mockito.any());
		// chiamata al service @async
		List<DomandaCollegata> domandeCollegate = importaDatiSuperficieService.importaDatiSuperficie(domandeCollegateImport);
		
		assertTrue(!domandeCollegate.isEmpty());
	}

	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.EDITA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void esportaDatiOK() throws Exception {
		// creazione input api rest
		DomandeCollegateExport domandeCollegateExport = new DomandeCollegateExport();
		domandeCollegateExport.setCuaa(Arrays.asList("TRRCST78B08C794X"));
		domandeCollegateExport.setTipoDomanda(TipoDomandaCollegata.PSR_SUPERFICIE_EU);
		domandeCollegateExport.setAnniCampagna(Arrays.asList(2018));
		// chiamata rest
		MvcResult result = this.mockMvc
				.perform(post("/api/v1/antimafia/domandecollegate/trasmissione").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(domandeCollegateExport)))
				.andExpect(status().isOk()).andReturn();
		org.junit.Assert.assertEquals("text/csv", result.getResponse().getContentType());
	}

	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.EDITA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void importaDatiSuperficieNoData() throws Exception {
		// creazione input api rest
		DomandeCollegateImport domandeCollegateImport = new DomandeCollegateImport();
		domandeCollegateImport.setCuaa(Arrays.asList("TRRCST78B08C794X"));
		domandeCollegateImport.setImporto(BigDecimal.TEN);
		domandeCollegateImport.setDataPresentazione(new Date());
		domandeCollegateImport.setAnniCampagna(Arrays.asList(2018));
		// mock chiamata AGS
		String params = "?params=".concat(URLEncoder.encode(objectMapper.writeValueAsString(domandeCollegateImport), StandardCharsets.UTF_8.name()));
		Mockito.doReturn(null).when(restTemplate).getForObject(Mockito.eq(new URI(agsUrl.concat("domandePSR").concat("/").concat(params))), Mockito.eq(String.class));
		Mockito.doNothing().when(consumeExternalRestApiA4gs).sincronizzaEsitiAntimafiaAgs(Mockito.any());
		// chiamata rest
		List<DomandaCollegata> domande = importaDatiSuperficieService.importaDatiSuperficie(domandeCollegateImport);
		assertTrue(domande.isEmpty());
	}

	@Ignore
	@Test(expected = RuntimeException.class)
	@WithMockUser(username = "utente", roles = { Ruoli.EDITA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void importaDatiSuperficieInvalidInput() throws Exception {
		// creazione input api rest
		DomandeCollegateImport domandeCollegateImport = new DomandeCollegateImport();
		domandeCollegateImport.setImporto(BigDecimal.TEN);
		domandeCollegateImport.setDataPresentazione(new Date());
		domandeCollegateImport.setAnniCampagna(Arrays.asList(2018));
		// mock chiamata AGS
		String params = "?params=".concat(URLEncoder.encode(objectMapper.writeValueAsString(domandeCollegateImport), StandardCharsets.UTF_8.name()));
		Mockito.doReturn(getDomandaPsrSuperficieDaAgs()).when(restTemplate).getForObject(Mockito.eq(new URI(agsUrl.concat("domandePSR").concat("/").concat(params))), Mockito.eq(String.class));
		// chiamata rest
		antimafiaService.importaDatiSuperficie(domandeCollegateImport);
	}

	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void getDomandeCollegateControllate() throws Exception {
		// mock chiamata AGS
		StatoDichiarazioneEnum filter = StatoDichiarazioneEnum.CONTROLLATA;
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(urlA4gfascicolo.concat("antimafia").concat("/page/"))
		        .queryParam("statiDichiarazione", filter).queryParam("pagSize", 5).queryParam("pagStart", 0);
		List<DichiarazioneAntimafia> dichiarazioni = objectMapper.readValue(getDichiarazioniAntimafia(), new TypeReference<List<DichiarazioneAntimafia>>() {
		});
		PageResultWrapper<DichiarazioneAntimafia> pageResultWrapper=new PageResultWrapper<>();
		pageResultWrapper.setResults(dichiarazioni);
		pageResultWrapper.setPagSize(5);
		pageResultWrapper.setPagStart(0l);
		pageResultWrapper.setTotal(0l);
		ResponseEntity<PageResultWrapper<DichiarazioneAntimafia>> response = new ResponseEntity<PageResultWrapper<DichiarazioneAntimafia>>(pageResultWrapper, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq( new URI(uriBuilder.build().toUriString())), Mockito.eq(HttpMethod.GET), Mockito.any(),
				Mockito.eq(new ParameterizedTypeReference<PageResultWrapper<DichiarazioneAntimafia>>() {
				}))).thenReturn(response);
		// chiamata rest
		this.mockMvc.perform(get("/api/v1/antimafia/certificazioni?pagSize=5&pagStart=0&stato=CONTROLLATA").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void getDomandeCollegateEsitoPositivo() throws Exception {
		Mockito.doCallRealMethod().when(consumeExternalRestApi).getDichiarazioniAntimafiaPage(Mockito.any(),Mockito.any(),Mockito.any());
		// mock chiamata AGS
		StatoDichiarazioneEnum filter = StatoDichiarazioneEnum.POSITIVO;
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(urlA4gfascicolo.concat("antimafia").concat("/page/"))
		        .queryParam("statiDichiarazione", filter).queryParam("pagSize", 5).queryParam("pagStart", 0);
		
		List<DichiarazioneAntimafia> dichiarazioni = objectMapper.readValue(getDichiarazioniAntimafia(), new TypeReference<List<DichiarazioneAntimafia>>() {
		});
		PageResultWrapper<DichiarazioneAntimafia> pageResultWrapper=new PageResultWrapper<>();
		pageResultWrapper.setResults(dichiarazioni);
		pageResultWrapper.setPagSize(5);
		pageResultWrapper.setPagStart(0l);
		pageResultWrapper.setTotal(0l);
		ResponseEntity<PageResultWrapper<DichiarazioneAntimafia>> response = new ResponseEntity<PageResultWrapper<DichiarazioneAntimafia>>(pageResultWrapper, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq( new URI(uriBuilder.build().toUriString())), Mockito.eq(HttpMethod.GET), Mockito.any(),
				Mockito.eq(new ParameterizedTypeReference<PageResultWrapper<DichiarazioneAntimafia>>() {
				}))).thenReturn(response);
		// chiamata rest
		this.mockMvc.perform(get("/api/v1/antimafia/certificazioni?pagSize=5&pagStart=0&stato=POSITIVO").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.EDITA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void importaDatiDUSuccess() throws Exception {
		DomandeCollegateImport domandeCollegateImport = objectMapper.readValue(new File("src/test/resources/domandecontrollate/domandeduok.json"), DomandeCollegateImport.class);
		MockMultipartFile info = new MockMultipartFile("info", "", "application/json", objectMapper.writeValueAsString(domandeCollegateImport).getBytes());
		String fileName = "successload.csv";
		Path filePdfPath = Paths.get("./src/test/resources/domandecontrollate/" + fileName);
		MockMultipartFile file = new MockMultipartFile("csv", Files.readAllBytes(filePdfPath));
		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/api/v1/antimafia/importadatidu");
		builder.with(request -> {
			request.setMethod(HttpMethod.PUT.name());
			return request;
		});

		MvcResult result = this.mockMvc.perform(builder.file(info).file(file)).andExpect(status().is2xxSuccessful()).andReturn();
		String contentAsString = result.getResponse().getContentAsString();
		List<DomandaCollegata> domande = mapperWrapper.readValue(contentAsString, new TypeReference<List<DomandaCollegata>>(){});
		assertThat(domande, is(not(empty())));
		assertThat(
				domande,
				  hasItem(anyOf(
					hasProperty("cuaa", is("TRRCST78B08C794X")),
					hasProperty("importoRichiesto", is(51000.05))
				  ))
				);
		assertThat(
				domande,
				  hasItem(anyOf(
					hasProperty("cuaa", is("00123890220")),
					hasProperty("importoRichiesto", is(42000.15))
				  ))
				);		
	}

	@Test(expected = NestedServletException.class)
	@WithMockUser(username = "utente", roles = { Ruoli.EDITA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void importaDatiDUNoCuaa() throws Exception {
		DomandeCollegateImport domandeCollegateImport = objectMapper.readValue(new File("src/test/resources/domandecontrollate/domandeduok.json"), DomandeCollegateImport.class);
		MockMultipartFile info = new MockMultipartFile("info", "", "application/json", objectMapper.writeValueAsString(domandeCollegateImport).getBytes());
		String fileName = "failureloadcuaa.csv";
		Path filePdfPath = Paths.get("./src/test/resources/domandecontrollate/" + fileName);
		MockMultipartFile file = new MockMultipartFile("csv", Files.readAllBytes(filePdfPath));
		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/api/v1/antimafia/importadatidu");
		builder.with(request -> {
			request.setMethod(HttpMethod.PUT.name());
			return request;
		});

		this.mockMvc.perform(builder.file(info).file(file));
	}

	@Test(expected = NestedServletException.class)
	@WithMockUser(username = "utente", roles = { Ruoli.EDITA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void importaDatiDUNoData() throws Exception {
		DomandeCollegateImport domandeCollegateImport = objectMapper.readValue(new File("src/test/resources/domandecontrollate/domandeduok.json"), DomandeCollegateImport.class);
		MockMultipartFile info = new MockMultipartFile("info", "", "application/json", objectMapper.writeValueAsString(domandeCollegateImport).getBytes());
		String fileName = "failureloaddata.csv";
		Path filePdfPath = Paths.get("./src/test/resources/domandecontrollate/" + fileName);
		MockMultipartFile file = new MockMultipartFile("csv", Files.readAllBytes(filePdfPath));
		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/api/v1/antimafia/importadatidu");
		builder.with(request -> {
			request.setMethod(HttpMethod.PUT.name());
			return request;
		});

		this.mockMvc.perform(builder.file(info).file(file));
	}

	@Test(expected = NestedServletException.class)
	@WithMockUser(username = "utente", roles = { Ruoli.EDITA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void importaDatiDUNoDomanda() throws Exception {
		DomandeCollegateImport domandeCollegateImport = objectMapper.readValue(new File("src/test/resources/domandecontrollate/domandeduok.json"), DomandeCollegateImport.class);
		MockMultipartFile info = new MockMultipartFile("info", "", "application/json", objectMapper.writeValueAsString(domandeCollegateImport).getBytes());
		String fileName = "failureloaddomanda.csv";
		Path filePdfPath = Paths.get("./src/test/resources/domandecontrollate/" + fileName);
		MockMultipartFile file = new MockMultipartFile("csv", Files.readAllBytes(filePdfPath));
		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/api/v1/antimafia/importadatidu");
		builder.with(request -> {
			request.setMethod(HttpMethod.PUT.name());
			return request;
		});

		this.mockMvc.perform(builder.file(info).file(file));
	}

	@Test(expected = NestedServletException.class)
	@WithMockUser(username = "utente", roles = { Ruoli.EDITA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void importaDatiDUNoImporto() throws Exception {
		DomandeCollegateImport domandeCollegateImport = objectMapper.readValue(new File("src/test/resources/domandecontrollate/domandeduok.json"), DomandeCollegateImport.class);
		MockMultipartFile info = new MockMultipartFile("info", "", "application/json", objectMapper.writeValueAsString(domandeCollegateImport).getBytes());
		String fileName = "failureloadimporto.csv";
		Path filePdfPath = Paths.get("./src/test/resources/domandecontrollate/" + fileName);
		MockMultipartFile file = new MockMultipartFile("csv", Files.readAllBytes(filePdfPath));
		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/api/v1/antimafia/importadatidu");
		builder.with(request -> {
			request.setMethod(HttpMethod.PUT.name());
			return request;
		});

		this.mockMvc.perform(builder.file(info).file(file));
	}

	@Test(expected = NestedServletException.class)
	@WithMockUser(username = "utente", roles = { Ruoli.EDITA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void importaDatiDUNoCampagna() throws Exception {
		DomandeCollegateImport domandeCollegateImport = objectMapper.readValue(new File("src/test/resources/domandecontrollate/domandeduok.json"), DomandeCollegateImport.class);
		MockMultipartFile info = new MockMultipartFile("info", "", "application/json", objectMapper.writeValueAsString(domandeCollegateImport).getBytes());
		String fileName = "failureloadcampagna.csv";
		Path filePdfPath = Paths.get("./src/test/resources/domandecontrollate/" + fileName);
		MockMultipartFile file = new MockMultipartFile("csv", Files.readAllBytes(filePdfPath));
		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/api/v1/antimafia/importadatidu");
		builder.with(request -> {
			request.setMethod(HttpMethod.PUT.name());
			return request;
		});

		this.mockMvc.perform(builder.file(info).file(file));
	}

	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.EDITA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void importaDatiDUCambioImportoSuccess() throws Exception {
		DomandeCollegateImport domandeCollegateImport = objectMapper.readValue(new File("src/test/resources/domandecontrollate/domandeduok.json"), DomandeCollegateImport.class);
		MockMultipartFile info = new MockMultipartFile("info", "", "application/json", objectMapper.writeValueAsString(domandeCollegateImport).getBytes());
		String fileName = "successloadcambioimporto.csv";
		Path filePdfPath = Paths.get("./src/test/resources/domandecontrollate/" + fileName);
		MockMultipartFile file = new MockMultipartFile("csv", Files.readAllBytes(filePdfPath));
		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/api/v1/antimafia/importadatidu");
		builder.with(request -> {
			request.setMethod(HttpMethod.PUT.name());
			return request;
		});

		this.mockMvc.perform(builder.file(info).file(file)).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.EDITA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void importaDatiDUNoCambioImporto() throws Exception {
		DomandeCollegateImport domandeCollegateImport = objectMapper.readValue(new File("src/test/resources/domandecontrollate/domandeduok.json"), DomandeCollegateImport.class);
		MockMultipartFile info = new MockMultipartFile("info", "", "application/json", objectMapper.writeValueAsString(domandeCollegateImport).getBytes());
		String fileName = "successloadnocambioimporto.csv";
		Path filePdfPath = Paths.get("./src/test/resources/domandecontrollate/" + fileName);
		MockMultipartFile file = new MockMultipartFile("csv", Files.readAllBytes(filePdfPath));
		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/api/v1/antimafia/importadatidu");
		builder.with(request -> {
			request.setMethod(HttpMethod.PUT.name());
			return request;
		});

		this.mockMvc.perform(builder.file(info).file(file)).andExpect(status().isOk());
	}

	// TODO in attesa di logica dal business
	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.EDITA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void importaDatiDUFailureDataPresentazionePrecedente() throws Exception {
		DomandeCollegateImport domandeCollegateImport = objectMapper.readValue(new File("src/test/resources/domandecontrollate/domandeduok.json"), DomandeCollegateImport.class);
		MockMultipartFile info = new MockMultipartFile("info", "", "application/json", objectMapper.writeValueAsString(domandeCollegateImport).getBytes());
		String fileName = "failuredatapresentazioneprecedente.csv";
		Path filePdfPath = Paths.get("./src/test/resources/domandecontrollate/" + fileName);
		MockMultipartFile file = new MockMultipartFile("csv", Files.readAllBytes(filePdfPath));
		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/api/v1/antimafia/importadatidu");
		builder.with(request -> {
			request.setMethod(HttpMethod.PUT.name());
			return request;
		});

		this.mockMvc.perform(builder.file(info).file(file)).andExpect(status().isOk());
	}

	// TODO in attesa di logica dal business
	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.EDITA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void importaDatiDUFailureImportoInferiore() throws Exception {
		DomandeCollegateImport domandeCollegateImport = objectMapper.readValue(new File("src/test/resources/domandecontrollate/domandeduok.json"), DomandeCollegateImport.class);
		MockMultipartFile info = new MockMultipartFile("info", "", "application/json", objectMapper.writeValueAsString(domandeCollegateImport).getBytes());
		String fileName = "failureimportoinferiore.csv";
		Path filePdfPath = Paths.get("./src/test/resources/domandecontrollate/" + fileName);
		MockMultipartFile file = new MockMultipartFile("csv", Files.readAllBytes(filePdfPath));
		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/api/v1/antimafia/importadatidu");
		builder.with(request -> {
			request.setMethod(HttpMethod.PUT.name());
			return request;
		});

		this.mockMvc.perform(builder.file(info).file(file)).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.EDITA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void aggiornaDomandaCollegata() throws Exception {

		DomandaCollegata domanda = new DomandaCollegata();
		domanda.setId(10l);
		domanda.setCuaa("TRRCST78B08C794X");
		domanda.setTipoDomanda(TipoDomandaCollegata.PSR_SUPERFICIE_EU.toString());
		domanda.setStatoBdna(StatoDomandaCollegata.NON_CARICATO.toString());
		domanda.setIdDomanda(666l);
		domanda.setProtocollo("PROTOCOLLO");
		domanda.setDtDomanda(new Date());
		domanda.setImportoRichiesto(new BigDecimal(5000.0));
		this.mockMvc.perform(put("/api/v1/antimafia/domandecollegate/10").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(domanda))).andExpect(status().isOk())
				.andExpect(jsonPath("id", is(10))).andExpect(jsonPath("protocollo", is("PROTOCOLLO")));
	}

	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void getDomandaCollegata() throws Exception {
		this.mockMvc.perform(get("/api/v1/antimafia/domandecollegate/1")).andExpect(jsonPath("$.id").value("1"));
	}

	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.EDITA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void aggiornaTrasmissioneBdna() throws Exception {
		TrasmissioneBdnaFilter trasmissioneBdnaFilter = new TrasmissioneBdnaFilter();
		trasmissioneBdnaFilter.setCfOperatore("TEST_CF");
		trasmissioneBdnaFilter.setId(5050L);
		trasmissioneBdnaFilter.setStatoTrasmissione(StatoTrasmissioneBdna.CONFERMATA);
		this.mockMvc.perform(put("/api/v1/antimafia/domandecollegate/trasmissione/564").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(trasmissioneBdnaFilter))).andExpect(status().isOk());
	}

	@Test(expected = NestedServletException.class)
	@WithMockUser(username = "utente", roles = { Ruoli.EDITA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void confermaDomandeCollegateInesistente() throws Exception {
		TrasmissioneBdnaFilter trasmissioneBdnaFilter = new TrasmissioneBdnaFilter();
		trasmissioneBdnaFilter.setId(9999L);
		trasmissioneBdnaFilter.setStatoTrasmissione(StatoTrasmissioneBdna.NON_CONFERMATA);
		trasmissioneBdnaFilter.setCfOperatore(null);
		this.mockMvc.perform(put("/api/v1/antimafia/domandecollegate/trasmissione/564")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(trasmissioneBdnaFilter)));
	}

	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.EDITA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void cancellaTrasmissioneBdna() throws Exception {
		this.mockMvc.perform(delete("/api/v1/antimafia/domandecollegate/trasmissione/565")).andExpect(status().isOk());
	}

	@Test(expected = NestedServletException.class)
	@WithMockUser(username = "utente", roles = { Ruoli.EDITA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void cancellaTrasmissioneInesistente() throws Exception {
		this.mockMvc.perform(delete("/api/v1/antimafia/domandecollegate/trasmissione/9998"));
	}

	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void getDomandeCollegate() throws Exception {
		String params = "{\"cuaa\": \"02224320222\", \"tipoDomanda\" : \"DOMANDA_UNICA\", \"idDomanda\" : \"789\"}";
		this.mockMvc.perform(get("/api/v1/antimafia/domandecollegate").param("params", params)).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void getDomandeCollegateInesistente() throws Exception {
		String params = "{\"cuaa\": \"ABC\" }";
		this.mockMvc.perform(get("/api/v1/antimafia/domandecollegate").param("params", params)).andExpect(status().isNoContent()).andExpect(content().string("[]"));
	}

//	@Test(expected = Test.None.class /* no exception expected */)
//	@WithMockUser("UTENTEAPPAG")
//	public void passaggioVerificaPeriodica() throws Exception {
//		Mockito.when(antimafiaService.getRestTemplateUtenzaApplicativa()).thenReturn(restTemplate);
//		Mockito.doCallRealMethod().when(antimafiaService).passaggioVerificaPeriodica();
//
//		String params = "?params=%7B%22statiDichiarazione%22%3A+%5B%22CONTROLLATA%22%2C%22CONTROLLO_MANUALE%22%2C%22PROTOCOLLATA%22%5D%7D";
//		List<DichiarazioneAntimafia> entities = objectMapper.readValue(getDichiarazioniAntimafia(), new TypeReference<List<DichiarazioneAntimafia>>() {
//		});
//		// get rifiutate
//		Mockito.doReturn(new ResponseEntity<List<DichiarazioneAntimafia>>(entities, HttpStatus.OK)).when(restTemplate).exchange(
//				Mockito.eq(new URI(urlA4gfascicolo.concat("antimafia").concat("/").concat(params))), Mockito.eq(HttpMethod.GET), Mockito.any(),
//				Mockito.eq(new ParameterizedTypeReference<List<DichiarazioneAntimafia>>() {
//				}));// getForObject(Mockito.contains("a4gfascicolo"), Mockito.eq(String.class));
//		JsonNode responseSinc = objectMapper.readTree(new File("src/test/resources/IstruttoriaAntimafia/sincAntimafia.json"));
//		// get sincronizzazione
//		Mockito.doReturn(objectMapper.writeValueAsString(responseSinc)).when(restTemplate).getForObject(Mockito.eq(new URI(a4gproxyUrl.concat("sincronizzazione/antimafia/").concat("8072"))),
//				Mockito.eq(String.class));
//		Mockito.when(restTemplate.exchange(Mockito.contains("a4gfascicolo"), Mockito.eq(HttpMethod.PUT), Mockito.any(), Mockito.eq(String.class)))
//				.thenReturn(new ResponseEntity<String>(HttpStatus.OK));
//		Mockito.when(restTemplate.exchange(Mockito.contains("a4gproxy"), Mockito.eq(HttpMethod.PUT), Mockito.any(), Mockito.eq(String.class))).thenReturn(new ResponseEntity<String>(HttpStatus.OK));
//		antimafiaService.passaggioVerificaPeriodica();
//	}
	
	@Test(expected = Test.None.class /* no exception expected */)
	@WithMockUser("UTENTEAPPAG")
	public void sincronizzazioneCertificazioniAntimafiaAgs() throws Exception {
		List<DichiarazioneAntimafia> entities = objectMapper.readValue(getDichiarazioniAntimafia(), new TypeReference<List<DichiarazioneAntimafia>>() {});
		PageResultWrapper<DichiarazioneAntimafia> dichiarazioniPage = new PageResultWrapper<>();
		dichiarazioniPage.setResults(entities);
		Mockito.doReturn(dichiarazioniPage).when(consumeExternalRestApi).getDichiarazioniAntimafiaPage(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any());
		Mockito.doNothing().when(consumeExternalRestApi).sincronizzaConAgs(Mockito.any(),Mockito.any());
		antimafiaService.sincronizzazioneCertificazioniAntimafiaAgs();
	}


	private String getDomandaPsrSuperficieDaAgs() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/IstruttoriaAntimafia/domandaPsrSuperficie.json"));
		return objectMapper.writeValueAsString(response);
	}

	private String getDomandaPsrSuperficieEsistente() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/IstruttoriaAntimafia/domandaPsrSuperficieEsistente.json"));
		return objectMapper.writeValueAsString(response);
	}

	// JSON FILES
	private ResponseEntity<String> getDichiarazioneAntimafia(Long idDichiarazione) throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/IstruttoriaAntimafia/dichiarazioneAntimafia" + idDichiarazione + ".json"));
		ResponseEntity<String> responseEntity = new ResponseEntity<>(objectMapper.writeValueAsString(response), HttpStatus.ACCEPTED);
		// return objectMapper.writeValueAsString(response);
		return responseEntity;
	}

	private String getAnagraficaImpresaDettaglioCompleto(String cuaa) throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/IstruttoriaAntimafia/anagraficaImpresaDettaglioCompleto".concat(cuaa).concat(".json")));
		return objectMapper.writeValueAsString(response);
	}

	// MOCKS
	private void mockParixDettaglioCompleto(String params, String cuaa) throws Exception {
		String parixDettaglioCompleto = "http://localhost:8080/a4gproxy/api/v1/anagraficaimpresa/dettagliocompleto/?params=";
		String parixUrl = parixDettaglioCompleto + URLEncoder.encode(params, "UTF-8");
		// Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(parixUrl)), Mockito.eq(String.class))).thenReturn(getAnagraficaImpresaDettaglioCompleto(cuaa));
		Mockito.doReturn(getAnagraficaImpresaDettaglioCompleto(cuaa)).when(restTemplate).getForObject(Mockito.eq(new URI(parixUrl)), Mockito.eq(String.class));
	}

	private void mockFascicoloGetDichiarazione(Long idDichiarazione) throws Exception {
		String fascicoloGetDichiarazione = "http://localhost:9001/a4gfascicolo/api/v1/antimafia/" + idDichiarazione;
		// Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(fascicoloGetDichiarazione)), Mockito.eq(String.class))).thenReturn(getDichiarazioneAntimafia(idDichiarazione));
		// Mockito.doReturn(getDichiarazioneAntimafia(idDichiarazione)).when(restTemplate).getForObject(Mockito.eq(new URI(fascicoloGetDichiarazione)), Mockito.eq(String.class));
		when(restTemplate.exchange(Mockito.anyString(), Mockito.any(HttpMethod.class), Mockito.any(), Mockito.<Class<String>>any())).thenReturn(getDichiarazioneAntimafia(idDichiarazione));
	}

	private void mockAgsControlloValidita(String cuaa, Boolean result) throws Exception {
		String agsControllaFascicolo = "http://localhost:8080/ags/api/v1/fascicoli/".concat(cuaa).concat("/controllaValidita");
		// Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(agsControllaFascicolo)), Mockito.eq(Boolean.class))).thenReturn(result);
		Mockito.doReturn(result).when(restTemplate).getForObject(Mockito.eq(new URI(agsControllaFascicolo)), Mockito.eq(Boolean.class));
	}

	private String getResponseImportaDatiStrutturali() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/domandeCollegate/responseImportaDatiStrutturali.json"));
		return objectMapper.writeValueAsString(response);
	}

	private String getDichiarazioniAntimafia() throws Exception {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/IstruttoriaAntimafia/dichiarazioniAntimafia.json"));
		return objectMapper.writeValueAsString(response);
	}

	@Test
	@WithMockUser(username = "utente")
	public void getSogliaAcquisizione() throws Exception {
		SogliaAcquisizioneFilter sogliaAcquisizioneFilter = new SogliaAcquisizioneFilter();
		sogliaAcquisizioneFilter.setSettore("PSR_STRUTTURALI_EU");
		this.mockMvc.perform(get("/api/v1/antimafia/soglie").param("params", (objectMapper.writeValueAsString(sogliaAcquisizioneFilter)))).andExpect(status().isOk());
	}

	@Test(expected = NestedServletException.class)
	@WithMockUser(username = "utente")
	public void getSogliaAcquisizioneError() throws Exception {
		SogliaAcquisizioneFilter sogliaAcquisizioneFilter = new SogliaAcquisizioneFilter();
		sogliaAcquisizioneFilter.setSettore("STRUTTURALI_EU");
		this.mockMvc.perform(get("/api/v1/antimafia/soglie").param("params", (objectMapper.writeValueAsString(sogliaAcquisizioneFilter))));
	}

	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.EDITA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void importaEsitiBDNASuccess() throws Exception {
		Mockito.when(antimafiaService.getRestTemplateUtenzaApplicativa()).thenReturn(restTemplate);
		String fileName = "importaesitibdnasuccess.csv";
		Path filePdfPath = Paths.get("./src/test/resources/domandecontrollate/" + fileName);
		MockMultipartFile file = new MockMultipartFile("csv", Files.readAllBytes(filePdfPath));
		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/api/v1/antimafia/importaesitibdna");
		builder.with(request -> {
			request.setMethod(HttpMethod.PUT.name());
			return request;
		});
		// mock chiamata AGS
		Mockito.when(restTemplate.exchange(Mockito.contains("esiti/antimafia"), Mockito.eq(HttpMethod.PUT), Mockito.any(), Mockito.eq(String.class)))
		.thenReturn(new ResponseEntity<String>(HttpStatus.OK));
		this.mockMvc.perform(builder.file(file)).andExpect(status().is2xxSuccessful());
	}
	
	@Test(expected = NestedServletException.class)
	@WithMockUser(username = "utente", roles = { Ruoli.EDITA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void importaEsitiBDNAError() throws Exception {
		Mockito.when(antimafiaService.getRestTemplateUtenzaApplicativa()).thenReturn(restTemplate);
		String fileName = "importaesitibdnaerrorline.csv";
		Path filePdfPath = Paths.get("./src/test/resources/domandecontrollate/" + fileName);
		MockMultipartFile file = new MockMultipartFile("csv", Files.readAllBytes(filePdfPath));
		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/api/v1/antimafia/importaesitibdna");
		builder.with(request -> {
			request.setMethod(HttpMethod.PUT.name());
			return request;
		});
		// mock chiamata AGS
		Mockito.when(restTemplate.exchange(Mockito.contains("esiti/antimafia"), Mockito.eq(HttpMethod.PUT), Mockito.any(), Mockito.eq(String.class)))
		.thenReturn(new ResponseEntity<String>(HttpStatus.OK));
		this.mockMvc.perform(builder.file(file));
	}
	
	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_ISTRUTTORIA_ANTIMAFIA_COD, Ruoli.EDITA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void downloadCsv() throws Exception {
		String id = "566";
		// chiamata rest
		MvcResult result = this.mockMvc
				.perform(get("/api/v1/antimafia/domandecollegate/trasmissione/"+id+"/esporta").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		org.junit.Assert.assertEquals("text/csv", result.getResponse().getContentType());
	}
	
	@Test(expected = NestedServletException.class)
	@WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_ISTRUTTORIA_ANTIMAFIA_COD, Ruoli.EDITA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void downloadCsvError() throws Exception {
		String id = "99999";
		// chiamata rest
		this.mockMvc.perform(get("/api/v1/antimafia/domandecollegate/trasmissione/"+id+"/esporta").contentType(MediaType.APPLICATION_JSON));
	}
	
	@Test(expected = NestedServletException.class)
	@WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_ISTRUTTORIA_ANTIMAFIA_COD, Ruoli.EDITA_ISTRUTTORIA_ANTIMAFIA_COD })
	public void downloadCsvErrorNoDomandaCollegata() throws Exception {
		String id = "999";
		// chiamata rest
		this.mockMvc.perform(get("/api/v1/antimafia/domandecollegate/trasmissione/"+id+"/esporta").contentType(MediaType.APPLICATION_JSON));
	}
}
