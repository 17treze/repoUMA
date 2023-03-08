package it.tndigitale.a4gistruttoria;

import static org.assertj.core.api.Assertions.fail;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4gistruttoria.dto.AccoppiatoZootecniaJobDto;
import it.tndigitale.a4gistruttoria.dto.CalcoloAccoppiatiJobDto;
import it.tndigitale.a4gistruttoria.dto.DatiElaborazioneProcesso;
import it.tndigitale.a4gistruttoria.dto.istruttoria.InputProcessoIstruttorieDto;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.dao.ProcessoDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gtProcesso;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.StatoProcesso;
import it.tndigitale.a4gistruttoria.repository.model.TipoProcesso;
import it.tndigitale.a4gistruttoria.service.businesslogic.processo.istruttoria.ElaboraBloccoIstruttorie;
import it.tndigitale.a4gistruttoria.service.businesslogic.processo.istruttoria.ElaboraSbloccoIstruttorie;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
// @AutoConfigureTestDatabase
@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.visualizza", "a4gistruttoria.pac.domandaUnica.edita" })
@Ignore
public class ProcessiApplicationTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private RestTemplate restTemplate;

	@Autowired
	private ProcessoDao processoDao;
	
	@Autowired
	private IstruttoriaDao istruttoriaDao;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private ElaboraBloccoIstruttorie processoIstruttoriaBloccoIstruttorie;
	
	@Autowired
	private ElaboraSbloccoIstruttorie processoIstruttoriaSbloccoIstruttorie;
	
	@SpyBean
	private Clock clock;

	private static final LocalDateTime NOW = LocalDateTime.of(2019,5,1,00,0);
	
	@Test
	@Transactional
	public void verificaRecuperoProcessoNonEsistente() throws Exception {
		this.mockMvc.perform(get("/api/v1/processi/5000000")).andExpect(status().isNoContent());
	}

	@Test
	@Transactional
	public void verificaRecuperoListaProcessiAttivi() throws Exception {
		A4gtProcesso processoModel = new A4gtProcesso();
		processoModel.setStato(StatoProcesso.RUN);
		processoModel.setTipo(TipoProcesso.RICEVIBILITA_AGS);
		processoModel.setDtInizio(new Date());
		processoModel.setPercentualeAvanzamento(new BigDecimal(0));
		processoDao.save(processoModel);

		this.mockMvc.perform(get("/api/v1/processi").param("params", "{\"idIstruttoria\": 4}"))
		./*  */andExpect(status().isOk())/* .andExpect(content().string(containsString("{\"idDatiSettore\": 4"))) */;
	}
	
	@Test
	@Transactional
	public void verificaRecuperoProcessoByID() throws Exception {
		A4gtProcesso processoModel = new A4gtProcesso();
		processoModel.setStato(StatoProcesso.RUN);
		processoModel.setTipo(TipoProcesso.RICEVIBILITA_AGS);
		processoModel.setDtInizio(new Date());
		processoModel.setPercentualeAvanzamento(new BigDecimal(0));
		processoDao.save(processoModel);

		this.mockMvc.perform(get("/api/v1/processi/".concat(processoModel.getId().toString()))).andExpect(status().isOk())/* .andExpect(content().string(getResponseProcessoById())) */;
	}

//	private String getBodyCreazioneProcesso() {
//		return "{\"idDatiSettore\":\"4\",\"idStatoProcesso\":\"PROCESSO_START\",\"idTipoProcesso\":\"RICEV_AGS\",\"numeroDomandeDaElaborare\":\"4\",\"domandeDaElaborare\":[{\"id\":271,\"infoGeneraliDomanda\":{\"modulo\":\"PAGAMENTI DIRETTI\",\"cuaaIntestatario\":\"00654300227\",\"ragioneSociale\":\"AZIENDA AGRICOLA PRA' TONDO DI MITTERMAIR ALOIS E C. SS\",\"numeroDomanda\":204098,\"dataPresentazione\":\"2019-04-09\",\"enteCompilatore\":\"CAA ATS - 001 - TRENTO\",\"dataProtocollazione\":\"2019-04-09\",\"stato\":\"IN_ISTRUTTORIA\"},\"controlliPresentazione\":null,\"infoIstruttoriaDomanda\":null,\"infoLiquidabilita\":null,\"richieste\":null,\"identificativoDI\":null,\"dataDI\":null,\"bloccataBool\":0,\"erroreCalcolo\":null,\"dataUltimoCalcolo\":null,\"calcoliSostegno\":null}],\"sostegno\":\"DISACCOPPIATO\"}";
//	}
//
//	private String getResponseDatiRicevibilita() {
//		return "{\"infoGeneraliDomanda\":{\"pac\":\"PAC1420\",\"tipoDomanda\":\"DU\",\"campagna\":2018,\"modulo\":\"PAGAMENTI DIRETTI\",\"codModulo\":\"BPS_2018\",\"cuaaIntestatario\":\"00318870227\",\"ragioneSociale\":\"ZAMBOTTI DANILO DENIS E RENATO\",\"numeroDomanda\":188802,\"numeroDomandaRettificata\":null,\"dataPresentazione\":\"2018-06-04T15:26:20.000+0200\",\"codEnteCompilatore\":\"5\",\"enteCompilatore\":\"CAA COLDIRETTI DEL TRENTINO - 004\"},\"controlliPresentazione\":{\"aggiornamentoFascicolo\":\"S\",\"visioneAnomalie\":\"S\",\"firmaDomanda\":\"S\",\"archiviazioneDomanda\":\"S\"}}";
//	}

	private Callable<Boolean> processoInEsecuzione(TipoProcesso tipoProcesso, List<Long> idsDomande) {
		return new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				A4gtProcesso a4gtProcesso = new A4gtProcesso();
				a4gtProcesso.setStato(StatoProcesso.OK);
				a4gtProcesso.setTipo(tipoProcesso);
				List<A4gtProcesso> a4gtProcessi = processoDao.findAll(Example.of(a4gtProcesso));
				if (a4gtProcessi.isEmpty()) {
					return Boolean.FALSE;
				} else {
					for (A4gtProcesso a4gtProcessoOut : a4gtProcessi) {
						try {
							List<String> idsDomandeString = idsDomande.stream().map(s -> String.valueOf(s)).collect(Collectors.toList());
							DatiElaborazioneProcesso datiElaborazioneProcesso = objectMapper.readValue(a4gtProcessoOut.getDatiElaborazione(), DatiElaborazioneProcesso.class);
							if (idsDomandeString.containsAll(datiElaborazioneProcesso.getGestite())) {
								return Boolean.TRUE;
							}
						} catch (Exception e) {
							fail("Si è verificato un errore durante l'await per il test", e);
						}
					}
					return Boolean.FALSE;
				}
			}
		};
	}

	@Transactional
	@Test(expected = NestedServletException.class)
	public void avviaControlliIntersostegnoProcessoInEsecuzioneACZ() throws Exception {
		A4gtProcesso a4gtProcessoInterACZ = new A4gtProcesso();
		a4gtProcessoInterACZ.setStato(StatoProcesso.RUN); // 104 stato PROCESSO_RUN
		a4gtProcessoInterACZ.setTipo(TipoProcesso.CTRL_INTERSOSTEGNO_ACC_ZOOTECNIA); // 112 tipo CTRL_INTERSOSTEGNO_ACC_ZOOTECNIA
		a4gtProcessoInterACZ.setDtInizio(new Date());
		a4gtProcessoInterACZ.setPercentualeAvanzamento(new BigDecimal(0));
		processoDao.save(a4gtProcessoInterACZ);
		A4gtProcesso a4gtProcessoInterACS = new A4gtProcesso();
		a4gtProcessoInterACS.setStato(StatoProcesso.RUN); // 104 stato PROCESSO_RUN
		a4gtProcessoInterACS.setTipo(TipoProcesso.CTRL_INTERSOSTEGNO_ACC_SUPERFICIE); // 113 tipo CTRL_INTERSOSTEGNO_ACC_SUPERFICIE
		a4gtProcessoInterACS.setDtInizio(new Date());
		a4gtProcessoInterACS.setPercentualeAvanzamento(new BigDecimal(0));
		processoDao.save(a4gtProcessoInterACS);
		AccoppiatoZootecniaJobDto accoppiatoZootecniaJobDto = new AccoppiatoZootecniaJobDto();
		accoppiatoZootecniaJobDto.setCampagna(2018L);
		accoppiatoZootecniaJobDto.setIdsDomande(Arrays.asList(3190L, 3191L));
		this.mockMvc.perform(post("/api/v1/domande/az/avviacontrollointersostegno").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(accoppiatoZootecniaJobDto)));
		await().atMost(1, TimeUnit.MINUTES).until(processoInEsecuzione(TipoProcesso.CTRL_INTERSOSTEGNO_ACC_ZOOTECNIA, accoppiatoZootecniaJobDto.getIdsDomande()));
	}

	@Transactional
	@Test
	public void avviaControlliIntersostegnoACZ() throws Exception {
		AccoppiatoZootecniaJobDto accoppiatoZootecniaJobDto = new AccoppiatoZootecniaJobDto();
		accoppiatoZootecniaJobDto.setCampagna(2018L);
		accoppiatoZootecniaJobDto.setIdsDomande(Arrays.asList(3192L));
		this.mockMvc.perform(post("/api/v1/domande/az/avviacontrollointersostegno").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(accoppiatoZootecniaJobDto)));
		await().atMost(1, TimeUnit.MINUTES).until(processoInEsecuzione(TipoProcesso.CTRL_INTERSOSTEGNO_ACC_ZOOTECNIA, accoppiatoZootecniaJobDto.getIdsDomande()));
	}

	@Transactional
	@Test(expected = NestedServletException.class)
	public void avviaControlliIntersostegnoProcessoInEsecuzioneACS() throws Exception {
		A4gtProcesso a4gtProcessoInterACZ = new A4gtProcesso();
		a4gtProcessoInterACZ.setStato(StatoProcesso.RUN); // 104 stato PROCESSO_RUN
		a4gtProcessoInterACZ.setTipo(TipoProcesso.CTRL_INTERSOSTEGNO_ACC_ZOOTECNIA); // 112 tipo CTRL_INTERSOSTEGNO_ACC_ZOOTECNIA
		a4gtProcessoInterACZ.setDtInizio(new Date());
		a4gtProcessoInterACZ.setPercentualeAvanzamento(new BigDecimal(0));
		processoDao.save(a4gtProcessoInterACZ);
		A4gtProcesso a4gtProcessoInterACS = new A4gtProcesso();
		a4gtProcessoInterACS.setStato(StatoProcesso.RUN); // 104 stato PROCESSO_RUN
		a4gtProcessoInterACS.setTipo(TipoProcesso.CTRL_INTERSOSTEGNO_ACC_SUPERFICIE); // 113 tipo CTRL_INTERSOSTEGNO_ACC_SUPERFICIE
		a4gtProcessoInterACS.setDtInizio(new Date());
		a4gtProcessoInterACS.setPercentualeAvanzamento(new BigDecimal(0));
		processoDao.save(a4gtProcessoInterACS);
		CalcoloAccoppiatiJobDto accoppiatoSuperficieJobDto = new CalcoloAccoppiatiJobDto();
		accoppiatoSuperficieJobDto.setIdsDomande(Arrays.asList(3193L, 3194L));
		accoppiatoSuperficieJobDto.setAnnoCampagna(2018l);
		this.mockMvc
			.perform(
						post("/api/v1/domande/as/avviacontrollointersostegno")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(accoppiatoSuperficieJobDto))
					)
			.andExpect(status().is2xxSuccessful());
		await().atMost(1, TimeUnit.MINUTES).until(processoInEsecuzione(TipoProcesso.CTRL_INTERSOSTEGNO_ACC_SUPERFICIE, accoppiatoSuperficieJobDto.getIdsDomande()));
	}

	@Transactional
	@Test
	public void avviaControlliIntersostegnoACS() throws Exception {
		CalcoloAccoppiatiJobDto accoppiatoSuperficieJobDto = new CalcoloAccoppiatiJobDto();
		accoppiatoSuperficieJobDto.setAnnoCampagna(2018l);
		accoppiatoSuperficieJobDto.setIdsDomande(Arrays.asList(3195L));
		this.mockMvc.perform(post("/api/v1/domande/as/avviacontrollointersostegno").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(accoppiatoSuperficieJobDto)));
		await().atMost(1, TimeUnit.MINUTES).until(processoInEsecuzione(TipoProcesso.CTRL_INTERSOSTEGNO_ACC_SUPERFICIE, accoppiatoSuperficieJobDto.getIdsDomande()));
	}
	
	private InputProcessoIstruttorieDto getBodyBloccaProcesso() {
		InputProcessoIstruttorieDto input = new InputProcessoIstruttorieDto();
		List<Long> idList = Arrays.asList(300L);
		input.setIdIstruttorie(idList);
		input.setTipoProcesso(TipoProcesso.BLOCCO_ISTRUTTORIE);
		input.setIdProcesso(986789L);
		return input;
	}
	
	private InputProcessoIstruttorieDto getBodySbloccaProcesso() {
		InputProcessoIstruttorieDto input = new InputProcessoIstruttorieDto();
		List<Long> idList = Arrays.asList(300L);
		input.setIdIstruttorie(idList);
		input.setTipoProcesso(TipoProcesso.SBLOCCO_ISTRUTTORIE);
		input.setIdProcesso(986789L);
		return input;
	}
	
	@Transactional
	@Test
	@Sql(scripts = "/IstruttoriaAntimafia/bloccaProcessoTestRun_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/IstruttoriaAntimafia/bloccaProcessoTestRun_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void bloccaProcessoIstruttoria_non_bloccato() throws Exception {
		Optional<IstruttoriaModel> istruttoriaOpt = istruttoriaDao.findById(300L);
		IstruttoriaModel istruttoria = istruttoriaOpt.get();
		assertFalse(istruttoria.getBloccataBool());
		
		processoIstruttoriaBloccoIstruttorie.avvia(getBodyBloccaProcesso());
		entityManager.refresh(istruttoria);
		assertTrue(istruttoria.getBloccataBool());
	}
	
	@Transactional
	@Test
	@Sql(scripts = "/IstruttoriaAntimafia/sbloccaProcessoTestRun_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/IstruttoriaAntimafia/sbloccaProcessoTestRun_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void sbloccaProcessoIstruttoria_bloccato() throws Exception {
		Optional<IstruttoriaModel> istruttoriaOpt = istruttoriaDao.findById(300L);
		IstruttoriaModel istruttoria = istruttoriaOpt.get();
		assertTrue(istruttoria.getBloccataBool());
		
		processoIstruttoriaSbloccoIstruttorie.avvia(getBodySbloccaProcesso());
		entityManager.refresh(istruttoria);
		assertFalse(istruttoria.getBloccataBool());
	}
}
