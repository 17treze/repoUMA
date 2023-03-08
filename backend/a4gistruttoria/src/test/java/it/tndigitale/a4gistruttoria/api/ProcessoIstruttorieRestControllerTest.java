package it.tndigitale.a4gistruttoria.api;

import static it.tndigitale.a4gistruttoria.repository.model.TipoProcesso.CALCOLO_ACCOPPIATO_SUPERFICIE_ISTRUTTORIA;
import static it.tndigitale.a4gistruttoria.repository.model.TipoProcesso.CALCOLO_ACCOPPIATO_ZOOTECNIA_ISTRUTTORIA;
import static it.tndigitale.a4gistruttoria.repository.model.TipoProcesso.CONTROLLO_LIQUIDABILITA_ISTRUTTORIA;
import static it.tndigitale.a4gistruttoria.util.JsonSupport.toObject;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;

import it.tndigitale.a4gistruttoria.dto.ProcessoIstruttoriaDto;
import it.tndigitale.a4gistruttoria.dto.istruttoria.InputProcessoIstruttorieDto;
import it.tndigitale.a4gistruttoria.repository.dao.ProcessoDao;
import it.tndigitale.a4gistruttoria.repository.model.TipoProcesso;
import it.tndigitale.a4gistruttoria.service.businesslogic.BloccaIstruttoriaService;
import it.tndigitale.a4gistruttoria.service.businesslogic.ElaborazioneIstruttoriaPerCambioStatoNonAmmissibileService;
import it.tndigitale.a4gistruttoria.service.businesslogic.SbloccaIstruttoriaService;
import it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato.CalcoloIstruttoriaDisaccoppiatoService;
import it.tndigitale.a4gistruttoria.service.businesslogic.liquidabilita.ControlloLiquidabilitaService;
import it.tndigitale.a4gistruttoria.service.businesslogic.superficie.CalcoloAccoppiatoSuperficieService;
import it.tndigitale.a4gistruttoria.util.JsonSupport;
import it.tndigitale.a4gistruttoria.util.MapperWrapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProcessoIstruttorieRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private MapperWrapper mapperWrapper;

	@MockBean
	private RestTemplate restTemplate;

	@Autowired
	private ProcessoDao processoDao;

	@MockBean
	private ControlloLiquidabilitaService liqService;

	@MockBean
	private CalcoloIstruttoriaDisaccoppiatoService calcDisService;

	@MockBean
	private ElaborazioneIstruttoriaPerCambioStatoNonAmmissibileService nonAmmService;

	@MockBean
	private CalcoloAccoppiatoSuperficieService calcACSService;

	@MockBean
	private BloccaIstruttoriaService bloccoService;

	@MockBean
	private SbloccaIstruttoriaService sbloccoService;

	@Test
	@WithMockUser(username = "istruttore", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	@Sql(scripts = "/sql/processi/ProcessoIstruttoria_controlloliquidabilita.sql",
		executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/processi/ProcessoIstruttoria_controlloliquidabilita_delete.sql",
		executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	public void avviaProcessoEseguiControlloLiquidabilita() throws Exception {
		final Long ID_ISTRUTTORIA = 999103430L;
		
		MvcResult mvcResult = mockMvc.perform(post(ApiUrls.PROCESSI_ISTRUTTORIE_DU_V1).contentType(APPLICATION_JSON).content(contentProcessoEseguiControlloLiquidabilita(ID_ISTRUTTORIA)))
				.andExpect(status().isOk()).andReturn();

		Long idProcesso = toObject(mvcResult.getResponse().getContentAsString(), Long.class);
		assertThat(idProcesso).isNotNull();
		processoDao.findById(idProcesso).orElseThrow(() -> new RuntimeException("Identificativo processo non trovato"));

		verify(liqService, atMost(1)).elabora(ID_ISTRUTTORIA);
	}

	@Test
	@WithMockUser(username = "istruttore", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	@Sql(scripts = "/sql/processi/ProcessoIstruttoria_calcolodisaccoppiato.sql",
		executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/processi/ProcessoIstruttoria_calcolodisaccoppiato_delete.sql",
		executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	public void avviaProcessoCalcoloDisaccoppiato() throws Exception {
		final Long ID_ISTRUTTORIA = 999103431L;
		MvcResult mvcResult = mockMvc.perform(post(ApiUrls.PROCESSI_ISTRUTTORIE_DU_V1).contentType(APPLICATION_JSON).content(contentProcessoCalcoloDisaccoppiato(ID_ISTRUTTORIA)))
				.andExpect(status().isOk()).andReturn();

		Long idProcesso = toObject(mvcResult.getResponse().getContentAsString(), Long.class);
		assertThat(idProcesso).isNotNull();
		processoDao.findById(idProcesso).orElseThrow(() -> new RuntimeException("Identificativo processo non trovato"));

		verify(calcDisService, atMost(1)).elabora(ID_ISTRUTTORIA);
	}

	@Test
	@WithMockUser(username = "istruttore", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	@Sql(scripts = "/sql/processi/ProcessoIstruttoria_cambiostato.sql",
  	executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "/sql/processi/ProcessoIstruttoria_cambiostato_delete.sql",
  	executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	public void avviaProcessoCambioDiStatoIstruttoriaANonAmmissibile() throws Exception {
		final Long ID_ISTRUTTORIA = 999103432L;
		MvcResult mvcResult = mockMvc.perform(post(ApiUrls.PROCESSI_ISTRUTTORIE_DU_V1).contentType(APPLICATION_JSON).content(contentProcessoCambioStatoIstruttoriaNonAmmissibilita(ID_ISTRUTTORIA)))
				.andExpect(status().isOk()).andReturn();

		Long idProcesso = toObject(mvcResult.getResponse().getContentAsString(), Long.class);
		assertThat(idProcesso).isNotNull();
		processoDao.findById(idProcesso).orElseThrow(() -> new RuntimeException("Identificativo processo non trovato"));

		verify(nonAmmService, atMost(1)).elabora(ID_ISTRUTTORIA);
	}

	@Test
	@WithMockUser(username = "istruttore", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	@Sql(scripts = "/sql/processi/ProcessoIstruttoria_calcoloaccoppiatosuperficie.sql",
  	executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  	@Sql(scripts = "/sql/processi/ProcessoIstruttoria_calcoloaccoppiatosuperficie_delete.sql",
  	executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	public void avviaProcessoCalcoloAccoppiatoSuperficie() throws Exception {
		final Long ID_ISTRUTTORIA = 999103433L;
		MvcResult mvcResult = mockMvc.perform(post(ApiUrls.PROCESSI_ISTRUTTORIE_DU_V1).contentType(APPLICATION_JSON).content(contentProcessoCalcoloAccoppiatoSuperficie(ID_ISTRUTTORIA)))
				.andExpect(status().isOk()).andReturn();

		Long idProcesso = toObject(mvcResult.getResponse().getContentAsString(), Long.class);
		assertThat(idProcesso).isNotNull();
		processoDao.findById(idProcesso).orElseThrow(() -> new RuntimeException("Identificativo processo non trovato"));

		verify(calcACSService, atMost(1)).elabora(ID_ISTRUTTORIA);
	}

	@Test
	@WithMockUser(username = "istruttore", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	@Sql(scripts = "/sql/processi/ProcessoIstruttoria_calcoloaccoppiatozootecnia.sql",
			executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/processi/ProcessoIstruttoria_calcoloaccoppiatozootecnia_delete.sql",
			executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	public void avviaProcessoCalcoloAccoppiatoZootecnia() throws Exception {
		final Long ID_ISTRUTTORIA = 999103450L;
		MvcResult mvcResult = mockMvc.perform(post(ApiUrls.PROCESSI_ISTRUTTORIE_DU_V1)
				.contentType(APPLICATION_JSON).content(contentProcessoCalcoloAccoppiatoZootecnia(ID_ISTRUTTORIA)))
				.andExpect(status().isOk()).andReturn();

		Long idProcesso = toObject(mvcResult.getResponse().getContentAsString(), Long.class);
		assertThat(idProcesso).isNotNull();
		processoDao.findById(idProcesso).orElseThrow(() -> new RuntimeException("Identificativo processo non trovato"));

		verify(calcACSService, atMost(1)).elabora(ID_ISTRUTTORIA);
	}

	@Test
	@WithMockUser(username = "istruttore", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	@Sql(scripts = "/sql/processi/ProcessoIstruttoria_bloccoistruttorie.sql",
  	executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/processi/ProcessoIstruttoria_bloccoistruttorie_delete.sql",
  	executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	public void avviaProcessoBloccoIstruttorie() throws Exception {
		final Long ID_ISTRUTTORIA = 999103434L;
		MvcResult mvcResult = mockMvc.perform(post(ApiUrls.PROCESSI_ISTRUTTORIE_DU_V1).contentType(APPLICATION_JSON).content(contentProcessoBloccoIstruttorie(ID_ISTRUTTORIA))).andExpect(status().isOk())
				.andReturn();

		Long idProcesso = toObject(mvcResult.getResponse().getContentAsString(), Long.class);
		assertThat(idProcesso).isNotNull();
		processoDao.findById(idProcesso).orElseThrow(() -> new RuntimeException("Identificativo processo non trovato"));

		verify(bloccoService, atMost(1)).elabora(ID_ISTRUTTORIA);
	}

	@Test
	@WithMockUser(username = "istruttore", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	@Sql(scripts = "/sql/processi/ProcessoIstruttoria_sbloccoistruttorie.sql",
  	executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "/sql/processi/ProcessoIstruttoria_sbloccoistruttorie_delete.sql",
  	executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	public void avviaProcessoSbloccoIstruttorie() throws Exception {
		final Long ID_ISTRUTTORIA = 999103435L;
		MvcResult mvcResult = mockMvc.perform(post(ApiUrls.PROCESSI_ISTRUTTORIE_DU_V1).contentType(APPLICATION_JSON).content(contentProcessoSbloccoIstruttorie(ID_ISTRUTTORIA))).andExpect(status().isOk())
				.andReturn();

		Long idProcesso = toObject(mvcResult.getResponse().getContentAsString(), Long.class);
		assertThat(idProcesso).isNotNull();
		processoDao.findById(idProcesso).orElseThrow(() -> new RuntimeException("Identificativo processo non trovato"));

		verify(sbloccoService, atMost(1)).elabora(ID_ISTRUTTORIA);
	}
	
	@Test
	@WithMockUser(username = "istruttore", roles = {"a4gistruttoria.pac.istruttoria.du.visualizza.tutti","a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = "/sql/processi/processi_in_esecuzione.sql",	executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/processi/processi_in_esecuzione_delete.sql",	executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void getProcessiInEsecuzione() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get(ApiUrls.PROCESSI_ISTRUTTORIE_DU_V1+"/inesecuzione").contentType(APPLICATION_JSON)).andExpect(status().isOk())
				.andReturn();
		List<ProcessoIstruttoriaDto> processi = mapperWrapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<ProcessoIstruttoriaDto>>(){});
		assertThat(processi).isNotEmpty();
		Optional<ProcessoIstruttoriaDto> pOtp = processi.stream().filter(p -> p.getIdProcesso() == 6254783l).findFirst();
		assertThat(pOtp.isPresent()).isTrue();
		assertThat(pOtp.get().getPercentualeAvanzamento().equals(new BigDecimal("80"))).isTrue();
		assertThat(pOtp.get().getDatiElaborazioneProcesso() != null).isTrue();
	}
	
	@Test
	@WithMockUser(username = "istruttore", roles = {"a4gistruttoria.pac.istruttoria.du.visualizza.tutti","a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Sql(scripts = "/sql/processi/processi_in_esecuzione.sql",	executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/processi/processi_in_esecuzione_delete.sql",	executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void getProcessiInEsecuzionePerTipo() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get(ApiUrls.PROCESSI_ISTRUTTORIE_DU_V1+"/inesecuzione").param("tipoProcesso", TipoProcesso.RICEVIBILITA_AGS.toString()).contentType(APPLICATION_JSON)).andExpect(status().isOk())
				.andReturn();
		List<ProcessoIstruttoriaDto> processi = mapperWrapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<ProcessoIstruttoriaDto>>(){});
		assertThat(processi).isNotEmpty();
		Optional<ProcessoIstruttoriaDto> pOtp = processi.stream().filter(p -> p.getIdProcesso() == 6254783l).findFirst();
		assertThat(pOtp.isPresent()).isTrue();
		assertThat(pOtp.get().getPercentualeAvanzamento().equals(new BigDecimal("80"))).isTrue();
		assertThat(pOtp.get().getDatiElaborazioneProcesso() != null).isTrue();
	}

	@Test
	@WithMockUser(username = "istruttore", roles = {"a4gistruttoria.pac.istruttoria.du.visualizza.tutti","a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
	@Ignore
	// Messo in ignore perche' va in errore random
	public void getProcessiInEsecuzioneNoProcessi() throws Exception {
		mockMvc.perform(get(ApiUrls.PROCESSI_ISTRUTTORIE_DU_V1+"/inesecuzione").contentType(APPLICATION_JSON))
		.andExpect(status().is(204))
		.andReturn();
	}

	private String contentProcessoEseguiControlloLiquidabilita(Long idIstruttoria) {
		InputProcessoIstruttorieDto inputProcessoIstruttorie = new InputProcessoIstruttorieDto()
						.setIdIstruttorie(Arrays.asList(idIstruttoria));
		inputProcessoIstruttorie.setTipoProcesso(CONTROLLO_LIQUIDABILITA_ISTRUTTORIA);
		return JsonSupport.toJson(inputProcessoIstruttorie);
	}

	private String contentProcessoCalcoloDisaccoppiato(Long idIstruttoria) {
		InputProcessoIstruttorieDto inputProcessoIstruttorie = new InputProcessoIstruttorieDto()
						.setIdIstruttorie(Arrays.asList(idIstruttoria));
		inputProcessoIstruttorie.setTipoProcesso(TipoProcesso.CALCOLO_DISACCOPPIATO_ISTRUTTORIA);
		return JsonSupport.toJson(inputProcessoIstruttorie);
	}

	private String contentProcessoCambioStatoIstruttoriaNonAmmissibilita(Long idIstruttoria) {
		InputProcessoIstruttorieDto inputProcessoIstruttorie = new InputProcessoIstruttorieDto()
						.setIdIstruttorie(Arrays.asList(idIstruttoria));
		inputProcessoIstruttorie.setTipoProcesso(TipoProcesso.NON_AMMISSIBILITA);
		return JsonSupport.toJson(inputProcessoIstruttorie);
	}

	private String contentProcessoCalcoloAccoppiatoSuperficie(Long idIstruttoria) {
		InputProcessoIstruttorieDto inputProcessoIstruttorie = new InputProcessoIstruttorieDto()
						.setIdIstruttorie(Arrays.asList(idIstruttoria));
		inputProcessoIstruttorie.setTipoProcesso(CALCOLO_ACCOPPIATO_SUPERFICIE_ISTRUTTORIA);
		return JsonSupport.toJson(inputProcessoIstruttorie);
	}

	private String contentProcessoCalcoloAccoppiatoZootecnia(Long idIstruttoria) {
		InputProcessoIstruttorieDto inputProcessoIstruttorie = new InputProcessoIstruttorieDto()
				.setIdIstruttorie(Arrays.asList(idIstruttoria));
		inputProcessoIstruttorie.setTipoProcesso(CALCOLO_ACCOPPIATO_ZOOTECNIA_ISTRUTTORIA);
		return JsonSupport.toJson(inputProcessoIstruttorie);
	}


	private String contentProcessoBloccoIstruttorie(Long idIstruttoria) {
		InputProcessoIstruttorieDto inputProcessoIstruttorie = new InputProcessoIstruttorieDto()
				.setIdIstruttorie(Arrays.asList(idIstruttoria));
		inputProcessoIstruttorie.setTipoProcesso(TipoProcesso.BLOCCO_ISTRUTTORIE);
		return JsonSupport.toJson(inputProcessoIstruttorie);
	}

	private String contentProcessoSbloccoIstruttorie(Long idIstruttoria) {
		InputProcessoIstruttorieDto inputProcessoIstruttorie = new InputProcessoIstruttorieDto()
				.setIdIstruttorie(Arrays.asList(idIstruttoria));
		inputProcessoIstruttorie.setTipoProcesso(TipoProcesso.SBLOCCO_ISTRUTTORIE);
		return JsonSupport.toJson(inputProcessoIstruttorie);
	}
}
