package it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia.calcolocapi;

import static java.util.stream.Collectors.groupingBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.MockIoItalia;
import it.tndigitale.a4gistruttoria.action.CallElencoCapiAction;
import it.tndigitale.a4gistruttoria.dto.AccoppiatoZootecniaJobDto;
import it.tndigitale.a4gistruttoria.dto.DomandaFilter;
import it.tndigitale.a4gistruttoria.dto.Processo;
import it.tndigitale.a4gistruttoria.dto.ProcessoFilter;
import it.tndigitale.a4gistruttoria.dto.zootecnia.CapiAziendaPerInterventoFilter;
import it.tndigitale.a4gistruttoria.repository.dao.AllevamentoImpegnatoDao;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.dao.ImportoUnitarioInterventoDao;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.dao.ProcessoDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gtProcesso;
import it.tndigitale.a4gistruttoria.repository.model.AllevamentoImpegnatoModel;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapo;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapoModel;
import it.tndigitale.a4gistruttoria.repository.model.ImportoUnitarioInterventoModel;
import it.tndigitale.a4gistruttoria.repository.model.InterventoZootecnico;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoDomanda;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.StatoProcesso;
import it.tndigitale.a4gistruttoria.repository.model.TipoProcesso;
import it.tndigitale.a4gistruttoria.service.ProcessoService;
import it.tndigitale.a4gistruttoria.util.EmailUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.visualizza", "a4gistruttoria.pac.domandaUnica.edita" })
// @AutoConfigureTestDatabase
public class CalcoloCapiServiceConsumerTest extends MockIoItalia {

	private static Logger log = LoggerFactory.getLogger(CalcoloCapiServiceConsumerTest.class);

	@Autowired
	CalcoloCapiService calcoloCapiService;
	@MockBean
	private RestTemplate restTemplate;
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private IstruttoriaDao istruttoriaDao;
	@Autowired
	private DomandaUnicaDao daoDomanda;
	@Autowired
	private AllevamentoImpegnatoDao daoRichiestaAllevam;
	@Value("${a4gproxy.urlDomandaUnicaElencoCapi}")
	private String urlDomandaUnicaElencoCapi;
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private ProcessoDao processoDao;
	@Autowired
	private ImportoUnitarioInterventoDao importoUnitarioDao;
	@Autowired
	private ProcessoService processoService;
	@MockBean
	private EmailUtils emailService;
	@Value("${zootecnia.interventi.vacchenutricielatte}")
	private String[] interventiCodiciVacche;
	@Autowired
	private CallElencoCapiAction callElencoCapiAction;

	// TODO: scritta versione 2 in seguito a IDU-ACZ.A-02.02 Controlli di etichettatura dell'allevamento e veterinari sui capi macellati
	@Ignore
	@Test
	@Transactional
	public void avviaAccoppiatoZootecniaCapiDaMacellazione() throws Exception {
		String url = urlDomandaUnicaElencoCapi.concat("?params=");
		// params_idDomanda_codiceAgea
		String params245_316 = "{\"cuaa\":\"CHNRLB69M46F205I\",\"annoCampagna\":2018,\"codiceIntervento\":316}";
		String params246_318 = "{\"cuaa\":\"NDRLSN58T05H612N\",\"annoCampagna\":2018,\"codiceIntervento\":318}";
		String params247_318 = "{\"cuaa\":\"BNGLLN27H70A520F\",\"annoCampagna\":2018,\"codiceIntervento\":318}";
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(url.concat(URLEncoder.encode(params245_316, StandardCharsets.UTF_8.name())))), Mockito.eq(String.class)))
		.thenReturn(getListaCapiDomanda("CHNRLB69M46F205I", "316"));
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(url.concat(URLEncoder.encode(params246_318, StandardCharsets.UTF_8.name())))), Mockito.eq(String.class)))
		.thenReturn(getListaCapiDomanda("NDRLSN58T05H612N", "318"));
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(url.concat(URLEncoder.encode(params247_318, StandardCharsets.UTF_8.name())))), Mockito.eq(String.class)))
		.thenReturn(getListaCapiDomanda("BNGLLN27H70A520F", "318"));
		// CustomThreadLocal.addVariable(A4gIstruttoriaConstants.HEADER_CF, "betty");

		calcoloCapiService.elabora(istruttoriaDao.findByDomandaUnicaModel_idAndSostegno(245L,Sostegno.ZOOTECNIA).get(0).getId());
		calcoloCapiService.elabora(istruttoriaDao.findByDomandaUnicaModel_idAndSostegno(246L,Sostegno.ZOOTECNIA).get(0).getId());
		calcoloCapiService.elabora(istruttoriaDao.findByDomandaUnicaModel_idAndSostegno(247L,Sostegno.ZOOTECNIA).get(0).getId());

		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.ZOOTECNIA);


		// controllo cambiamento di stato sostegno
		daoDomanda.findAllById(Arrays.asList(245L, 246L, 247L)).forEach(domanda -> {
			istruttoria.setDomandaUnicaModel(domanda);
			Optional<IstruttoriaModel> statoLavorazioneSostegno = domanda.getA4gtLavorazioneSostegnos().stream()
					.filter(x -> x.getSostegno().equals(Sostegno.ZOOTECNIA))
					.findFirst();
			if (statoLavorazioneSostegno.isPresent()) {
				entityManager.refresh(statoLavorazioneSostegno.get());
				if (domanda.getId().equals(245L) || domanda.getId().equals(247L)) {
					assertEquals(StatoIstruttoria.NON_AMMISSIBILE.getStatoIstruttoria(), statoLavorazioneSostegno.get().getA4gdStatoLavSostegno().getIdentificativo());
				}
				if (domanda.getId().equals(246L)) {
					assertEquals(StatoIstruttoria.INTEGRATO.getStatoIstruttoria(), statoLavorazioneSostegno.get().getA4gdStatoLavSostegno().getIdentificativo());
				}
			} else {
				log.info("Domanda ".concat(domanda.getId().toString()).concat(" non presente in A4GT_ISTRUTTORIA"));
				fail();
			}
		});


		// controllo esiti capi
		daoRichiestaAllevam.findAllById(Arrays.asList(348L, 354L, 355L)).forEach(allevamento -> {
			log.info("Allevamento id :" + allevamento.getId());
			assertTrue(!allevamento.getEsitiCalcoloCapi().isEmpty());
			allevamento.getEsitiCalcoloCapi().forEach(capo -> {
				log.info("codiceCapo :" + capo.getCodiceCapo() + " " + capo.getEsito().toString());
				log.info("messaggio :" + capo.getMessaggio());


				switch (capo.getCodiceCapo()) {
				case "CAPO_50": // br6 false - (dtuscita - dtinserimentobdningresso) < 180
				case "CAPO_100": // br6 false - (dtuscita - dtinserimentobdningresso) < 360
				case "CAPO_110": // br9 false - dtiniziodetenzione < dtiniziodetenzione_h2
				case "CAPO_120": // br9 false - dtfinedetentezione > dtfinedetentezione_h2
				case "CAPO_130": // br9 false - non presente sul registro etichettatura (TPAGA_ZOOT_ETICHETTATURA)
				case "CAPO_X1": // campi null o non esistenti
				case "CAPO_X2":
				case "CAPO_X3":
				case "CAPO_X4":
				case "CAPO_X5":
				case "CAPO_X6":
				case "CAPO_X7":
				case "CAPO_X9":
					assertEquals(EsitoCalcoloCapo.NON_AMMISSIBILE, capo.getEsito());
					break;
				case "CAPO_X8": // dt fine detenzione non esistente equivale a dt fine detenzione infinito -> capo ammissibile
				case "CAPO_10":
				case "CAPO_60": // br5 true
				case "CAPO_90": // br5 true
				case "CAPO_40":
					assertEquals(EsitoCalcoloCapo.AMMISSIBILE, capo.getEsito());
					break;
				case "CAPO_20":
				case "CAPO_30":
				case "CAPO_70": // br5 false - (dtInserimentoBdnIngresso - dtIngresso) > 14
				case "CAPO_80": // br5 false - (dtInserimentoBdnIngresso - dtIngresso) > 27
					assertEquals(EsitoCalcoloCapo.AMMISSIBILE_CON_SANZIONE, capo.getEsito());
					break;
				default:
					log.debug("il capo ".concat(capo.getCodiceCapo()).concat(" non ha assertion implementati"));
					fail();
					break;
				}
			});
		});
	}

	@Ignore
	@Test
	@Transactional
	public void avviaAccoppiatoZootecniaCapiDaMacellazione_2() throws Exception {
		// params_idDomanda_codiceAgea
		//String params261_318 = "{\"cuaa\":\"CHNRLB69M46F205A\",\"annoCampagna\":2018,\"codiceIntervento\":318}"; // 3 detenzioni
		CapiAziendaPerInterventoFilter params261_318= new CapiAziendaPerInterventoFilter(
				"CHNRLB69M46F205A",
				2018,
				InterventoZootecnico.BOVINO_MACELLATO_ETICHETTATO
				);
		//String params261_316 = "{\"cuaa\":\"CHNRLB69M46F205A\",\"annoCampagna\":2018,\"codiceIntervento\":316}"; // 4 detenzioni
		CapiAziendaPerInterventoFilter params261_316= new CapiAziendaPerInterventoFilter(
				"CHNRLB69M46F205A",
				2018,
				InterventoZootecnico.BOVINO_MACELLATO_12MESI
				);
		// singola detenzione   {"cuaa":"CHNRLB69M46F205I","annoCampagna":2018,"codiceIntervento":316} 
		//String params261_315 = "{\"cuaa\":\"CHNRLB69M46F205A\",\"annoCampagna\":2018,\"codiceIntervento\":315}"; // 1 detenzioni
		CapiAziendaPerInterventoFilter params261_315= new CapiAziendaPerInterventoFilter(
				"CHNRLB69M46F205A",
				2018,
				InterventoZootecnico.BOVINO_MACELLATO
				);
		Mockito.when(restTemplate.getForObject(Mockito.eq(buildUrl(params261_318, "/bovini/macellati")), Mockito.eq(String.class)))
		.thenReturn(getListaCapiDomanda("CHNRLB69M46F205A", "318"));
		Mockito.when(restTemplate.getForObject(Mockito.eq(buildUrl(params261_316, "/bovini/macellati")), Mockito.eq(String.class)))
		.thenReturn(getListaCapiDomanda("CHNRLB69M46F205A", "316"));
		Mockito.when(restTemplate.getForObject(Mockito.eq(buildUrl(params261_315, "/bovini/macellati")), Mockito.eq(String.class)))
		.thenReturn(getListaCapiDomanda("CHNRLB69M46F205A", "315"));
		// CustomThreadLocal.addVariable(A4gIstruttoriaConstants.HEADER_CF, "betty");

		calcoloCapiService.elabora(istruttoriaDao.findByDomandaUnicaModel_idAndSostegno(261L,Sostegno.ZOOTECNIA).get(0).getId());

		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.ZOOTECNIA);

		// controllo cambiamento di stato sostegno
		daoDomanda.findAllById(Arrays.asList(261L)).forEach(domanda -> {
			istruttoria.setDomandaUnicaModel(domanda);
			Optional<IstruttoriaModel> statoLavorazioneSostegno = istruttoriaDao.findOne(Example.of(istruttoria));
			if (statoLavorazioneSostegno.isPresent()) {
				entityManager.refresh(statoLavorazioneSostegno.get());
				String statoLavSostegno = statoLavorazioneSostegno.get().getA4gdStatoLavSostegno().getIdentificativo();
				if (domanda.getId().equals(261L)) {
					assertEquals(StatoIstruttoria.NON_AMMISSIBILE.getStatoIstruttoria(), statoLavSostegno);
				}
			} else {
				log.info("Domanda ".concat(domanda.getId().toString()).concat(" non presente in A4GT_ISTRUTTORIA"));
				fail();
			}
		});

		// controllo esiti capi
		daoRichiestaAllevam.findAllById(Arrays.asList(371L)).forEach(allevamento -> {
			log.info("Allevamento id :" + allevamento.getId());
			assertTrue(!allevamento.getEsitiCalcoloCapi().isEmpty());
			allevamento.getEsitiCalcoloCapi().forEach(capo -> {
				log.info("codiceCapo :" + capo.getCodiceCapo() + " " + capo.getEsito().toString());
				log.info("messaggio :" + capo.getMessaggio());
				assertThat(capo.getTransizione()).isNotNull();
				switch (capo.getCodiceCapo()) {
				case "CAPO_MACELLO_1":
				case "CAPO_MACELLO_3":
				case "CAPO_MACELLO_v2.2":
					assertEquals(EsitoCalcoloCapo.AMMISSIBILE, capo.getEsito());
					break;
				case "CAPO_MACELLO_2":
				case "CAPO_MACELLO_4":
				case "CAPO_MACELLO_5": //(ggDetenzione.getGgAmministrativi() < ggDetenzioniMinimi && ggDetenzione.getGgReali()>=ggDetenzioniMinimi)
				case "CAPO_MACELLO_v2.1":
				case "CAPO_MACELLO_v2.3":
					assertEquals(EsitoCalcoloCapo.AMMISSIBILE_CON_SANZIONE, capo.getEsito());
					break;
				case "CAPO_MACELLO_v2.4":					
				case "CAPO_MACELLO_6": // detenzione singola ko 318
					assertEquals(EsitoCalcoloCapo.NON_AMMISSIBILE, capo.getEsito());
					break;
				default:
					log.debug("il capo ".concat(capo.getCodiceCapo()).concat(" non ha assertion implementati"));
					fail();
					break;
				}
			});
		});
	}

	private String buildUrl(CapiAziendaPerInterventoFilter filter, String specie) {
		return callElencoCapiAction.buildUrl(filter,specie).toUriString();
	}

	//23/03/2020 valutare se togliere. Dall'history vedo che e' stato ignorato da piu' di un anno
	@Ignore
	@Test
	@Transactional
	public void avviaAccoppiatoZootecniaVaccheLatteENutriciInMontagna() throws Exception {
		String url = urlDomandaUnicaElencoCapi.concat("?params=");
		// id_domanda_tipoIntervento
		String params248_311 = "{\"cuaa\":\"DPDFRZ65C21C794B\",\"annoCampagna\":2018,\"codiceIntervento\":311,\"idAlleBDN\":1279014}";
		String params249_311 = "{\"cuaa\":\"MLNLSN70P26L378K\",\"annoCampagna\":2018,\"codiceIntervento\":311,\"idAlleBDN\":1279014}";
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(url.concat(URLEncoder.encode(params248_311, StandardCharsets.UTF_8.name())))), Mockito.eq(String.class)))
		.thenReturn(getListaCapiDomanda("DPDFRZ65C21C794B", "311"));
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(url.concat(URLEncoder.encode(params249_311, StandardCharsets.UTF_8.name())))), Mockito.eq(String.class)))
		.thenReturn(getListaCapiDomanda("MLNLSN70P26L378K", "311"));
		// mock email
		Mockito.doNothing().when(emailService).sendSimpleMessage(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		// CustomThreadLocal.addVariable(A4gIstruttoriaConstants.HEADER_CF, "betty");
		AccoppiatoZootecniaJobDto accoppiatoZootecniaJobDto = new AccoppiatoZootecniaJobDto();
		accoppiatoZootecniaJobDto.setCampagna(2018L);
		accoppiatoZootecniaJobDto.setIdsDomande(Arrays.asList(248L, 249L));
		this.mockMvc.perform(post("/api/v1/domande/az/avvia").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(accoppiatoZootecniaJobDto)));
		await().atMost(2, TimeUnit.MINUTES).until(processoInEsecuzione());

		// controllo cambiamento di stato sostegno

		IstruttoriaModel a4gtLavorazioneSostegno = new IstruttoriaModel();
		a4gtLavorazioneSostegno.setSostegno(Sostegno.ZOOTECNIA);
		daoDomanda.findAllById(Arrays.asList(248L, 249L)).forEach(domanda -> {
			a4gtLavorazioneSostegno.setDomandaUnicaModel(domanda);
			Optional<IstruttoriaModel> statoLavorazioneSostegno = istruttoriaDao.findOne(Example.of(a4gtLavorazioneSostegno));
			if (statoLavorazioneSostegno.isPresent()) {
				if (domanda.getId().equals(248L)) {
					assertEquals(StatoIstruttoria.NON_AMMISSIBILE.getStatoIstruttoria(), statoLavorazioneSostegno.get().getA4gdStatoLavSostegno().getIdentificativo());
				}
				if (domanda.getId().equals(249L)) { // CAPO_LATTE_4 - rollback
					assertEquals(StatoIstruttoria.RICHIESTO.getStatoIstruttoria(), statoLavorazioneSostegno.get().getA4gdStatoLavSostegno().getIdentificativo());
				}
			} else {
				log.info("Domanda ".concat(domanda.getId().toString()).concat(" non presente in A4GT_ISTRUTTORIA"));
				fail();
			}
		});

		daoRichiestaAllevam.findAllById(Arrays.asList(346L, 357L)).forEach(allevamento -> {
			allevamento.getEsitiCalcoloCapi().forEach(capo -> {
				log.info("codiceCapo :" + capo.getCodiceCapo() + " " + capo.getEsito().toString());
				log.info("messaggio :" + capo.getMessaggio());
				// TODO: ex assert per allevid e capo: ex. capo_latte_0 solo di allev 346.
				switch (capo.getCodiceCapo()) {
				case "CAPO_LATTE_0":
					assertEquals(EsitoCalcoloCapo.AMMISSIBILE, capo.getEsito());
					break;
				case "CAPO_LATTE_1": // br4 false - il parto non e' avvenuto nell'allev per cui si richiede il 311
				case "CAPO_LATTE_2": // br4 false - non trova vitelli rispondenti ai requisiti dtnascita e detenzione
				case "CAPO_LATTE_3": // br5 false - primo parto gemellare e non tutti i vitelli rispettano br5
				case "CAPO_LATTE_5": // br6 false - somma detenzioni allevamenti di montagna e non conosciuti comunque < 180
				case "CAPO_LATTE_6": // br2.1 false - allevamento non di montagna
					assertEquals(EsitoCalcoloCapo.NON_AMMISSIBILE, capo.getEsito());
					break;
				default:
					log.info("il capo ".concat(capo.getCodiceCapo().toString()).concat(" non presenta assertion"));
					// fail();
					break;
				}
			});
		});
	}








	@Ignore
	@Test
	@Transactional
	public void avviaAccoppiatoZootecniaVaccheLatteENutriciInMontagna_2() throws Exception {
		//String url = urlDomandaUnicaElencoCapi.concat("?params=");
		// id_domanda_tipoIntervento
		//String params248_311 = "{\"cuaa\":\"DPDFRZ65C21C794B\",\"annoCampagna\":2018,\"codiceIntervento\":311,\"idAlleBDN\":1279014}";
		CapiAziendaPerInterventoFilter params248_311= new CapiAziendaPerInterventoFilter(
				"DPDFRZ65C21C794B",
				2018,
				InterventoZootecnico.VACCA_LATTE_MONTANA,
				1279014,
				null
				);
		Mockito.when(restTemplate.getForObject(Mockito.eq(buildUrl(params248_311, "/bovini/lattecarne")), Mockito.eq(String.class)))
		.thenReturn(getListaCapiDomanda("DPDFRZ65C21C794B", "311_2"));
		// CustomThreadLocal.addVariable(A4gIstruttoriaConstants.HEADER_CF, "betty");

		calcoloCapiService.elabora(istruttoriaDao.findByDomandaUnicaModel_idAndSostegno(248L,Sostegno.ZOOTECNIA).get(0).getId());
		daoRichiestaAllevam.findAllById(Arrays.asList(346L)).forEach(allevamento -> {
			assertTrue(!allevamento.getEsitiCalcoloCapi().isEmpty());
			allevamento.getEsitiCalcoloCapi().forEach(capo -> {
				log.info("codiceCapo :" + capo.getCodiceCapo() + " " + capo.getEsito().toString());
				log.info("messaggio :" + capo.getMessaggio());
				switch (capo.getCodiceCapo()) {
				case "AT320399111": // ok perche' primo parto e detenzione precedente e successiva primo parto superano i 180 gg
				case "AT320399420": // ok perche' primo parto e detenzione precedente primo parto superano i 180 gg
					assertEquals(EsitoCalcoloCapo.AMMISSIBILE, capo.getEsito());
					break;
				case "AT320399419": //
				case "AT320399000": //
				case "AT320399222": //
					assertEquals(EsitoCalcoloCapo.NON_AMMISSIBILE, capo.getEsito());
					break;
				case "AT320399333": // con sanzione perche' vaccaDtComAutoritaIngresso-vaccaDtIngresso=8giorni
					assertEquals(EsitoCalcoloCapo.AMMISSIBILE_CON_SANZIONE, capo.getEsito());
					break;
				default:
					log.info("il capo ".concat(capo.getCodiceCapo().toString()).concat(" non presenta assertion"));
					// fail();
					break;
				}
			});
		});
	}
	
	@Ignore
	@Test
	@Transactional(readOnly = true)
	public void avviaAccoppiatoZootecniaCapiLatteENutrici() throws Exception {
		// DA LATTE E NUTRICI - interventi 310-312-313-314-322
		// params_idDomanda_tipoInvervento
		//String url = urlDomandaUnicaElencoCapi.concat("?params=");
		//String params260_322 = "{\"cuaa\":\"ZMBLSN64H21L378Y\",\"annoCampagna\":2018,\"codiceIntervento\":322,\"idAlleBDN\":1279014}";
		CapiAziendaPerInterventoFilter params260_322= new CapiAziendaPerInterventoFilter(
				"ZMBLSN64H21L378Y",
				2018,
				InterventoZootecnico.VACCA_NUTRICE_NON_ISCRITTA,
				1279014,
				null
				);
		//String params250_310 = "{\"cuaa\":\"ZMBLSN64H21L378X\",\"annoCampagna\":2018,\"codiceIntervento\":310,\"idAlleBDN\":1279014}";
		CapiAziendaPerInterventoFilter params250_310= new CapiAziendaPerInterventoFilter(
				"ZMBLSN64H21L378X",
				2018,
				InterventoZootecnico.VACCA_LATTE,
				1279014,
				null
				);
		//String params270_322 = "{\"cuaa\":\"ZMBLSN64H21L378Z\",\"annoCampagna\":2018,\"codiceIntervento\":322,\"idAlleBDN\":1279014}";
		CapiAziendaPerInterventoFilter params270_322= new CapiAziendaPerInterventoFilter(
				"ZMBLSN64H21L378Z",
				2018,
				InterventoZootecnico.VACCA_NUTRICE_NON_ISCRITTA,
				1279014,
				null
				);
		// CustomThreadLocal.addVariable(A4gIstruttoriaConstants.HEADER_CF, "betty");

		Mockito.when(restTemplate.getForObject(Mockito.eq(buildUrl(params260_322, "/bovini/lattecarne")), Mockito.eq(String.class)))
		.thenReturn(getListaCapiDomanda("ZMBLSN64H21L378Y", "322"));
		Mockito.when(restTemplate.getForObject(Mockito.eq(buildUrl(params250_310, "/bovini/lattecarne")), Mockito.eq(String.class)))
		.thenReturn(getListaCapiDomanda("ZMBLSN64H21L378X", "310"));
		Mockito.when(restTemplate.getForObject(Mockito.eq(buildUrl(params270_322, "/bovini/lattecarne")), Mockito.eq(String.class)))
		.thenReturn(getListaCapiDomanda("ZMBLSN64H21L378Z", "322"));

		calcoloCapiService.elabora(istruttoriaDao.findByDomandaUnicaModel_idAndSostegno(250L,Sostegno.ZOOTECNIA).get(0).getId());
		calcoloCapiService.elabora(istruttoriaDao.findByDomandaUnicaModel_idAndSostegno(260L,Sostegno.ZOOTECNIA).get(0).getId());
		calcoloCapiService.elabora(istruttoriaDao.findByDomandaUnicaModel_idAndSostegno(270L,Sostegno.ZOOTECNIA).get(0).getId());

		IstruttoriaModel a4gtLavorazioneSostegno = new IstruttoriaModel();
		a4gtLavorazioneSostegno.setSostegno(Sostegno.ZOOTECNIA);
		// controllo cambiamento di stato sostegno
		daoDomanda.findAllById(Arrays.asList(250L, 260L)).forEach(domanda -> {
			a4gtLavorazioneSostegno.setDomandaUnicaModel(domanda);
			Optional<IstruttoriaModel> statoLavorazioneSostegno = istruttoriaDao.findOne(Example.of(a4gtLavorazioneSostegno));
			if (statoLavorazioneSostegno.isPresent()) {
				entityManager.refresh(statoLavorazioneSostegno.get());
				if (domanda.getId().equals(260L)) {
					assertEquals(StatoIstruttoria.INTEGRATO.getStatoIstruttoria(), statoLavorazioneSostegno.get().getA4gdStatoLavSostegno().getIdentificativo());
				} else if (domanda.getId().equals(250L)) {
					assertEquals(StatoIstruttoria.NON_AMMISSIBILE.getStatoIstruttoria(), statoLavorazioneSostegno.get().getA4gdStatoLavSostegno().getIdentificativo());
				}
			} else {
				log.info("Domanda ".concat(domanda.getId().toString()).concat(" non presente in A4GT_ISTRUTTORIA"));
				fail();
			}
		});
		// capo con primo parto deve essere scartato "CAPO_ERR_X12" 358
		//i capi che non hanno primo parto vanno scartati
		//24 03 2020 non ha piu' senso. Va in non ammissibile perche' i dati non sono congruenti
		//			List<EsitoCalcoloCapoModel> capiPrimoPartoNonTrovato = daoRichiestaAllevam.findById(358L).get().getEsitiCalcoloCapi().stream().filter(x -> x.getCodiceCapo().equals("CAPO_ERR_X12"))
		//					.collect(Collectors.toList());
		//			assertTrue(capiPrimoPartoNonTrovato.isEmpty());

		daoRichiestaAllevam.findAllById(Arrays.asList(358L, 359L, 370L)).forEach(allevamento -> {
			assertTrue(!allevamento.getEsitiCalcoloCapi().isEmpty());

			log.info("Allevamento id :" + allevamento.getId());
			allevamento.getEsitiCalcoloCapi().forEach(capo -> {
				log.info("codiceCapo :" + capo.getCodiceCapo() + " " + capo.getEsito().toString());
				log.info("messaggio :" + capo.getMessaggio());
				switch (capo.getCodiceCapo()) {
				case "CAPO_1": // br4 false
				case "CAPO_3": // br4 false


				case "CAPO_NUTRICE_2": // br3 false periodo vacca non fertile
				case "CAPO_NUTRICE_3": // br3 false periodo vacca non fertile
				case "CAPO_NUTRICE_4": // br non valide per tutti i vitelli gemelli
				case "CAPO_NUTRICE_5": // INTERVENTO 322 ammissibile false - analisi latte presente nel db
					assertEquals(EsitoCalcoloCapo.NON_AMMISSIBILE, capo.getEsito());
					break;
				case "CAPO_9":
					assertEquals(EsitoCalcoloCapo.AMMISSIBILE_CON_SANZIONE, capo.getEsito());
					break;
				case "CAPO_2": // br5 true
				case "CAPO_4": // calcolo uba test: capo 4 sia per 310 sia per 322. lo considera una volta
				case "CAPO_6": // br5 false
				case "CAPO_8": // controllo su tutti i gemelli
				case "CAPO_NUTRICE_0": // vitello1 non considerato
				case "CAPO_NUTRICE_1": // vitello1 non considerato
				case "CAPO_5": // br4 false
				case "CAPO_7": // br4 false
					assertEquals(EsitoCalcoloCapo.AMMISSIBILE, capo.getEsito());
					break;
				default:
					log.error("il capo ".concat(capo.getCodiceCapo()).concat(" non ha assertion implementati"));
					fail();
					break;
				}
			});
		});
	}

//	@Test
	@Transactional
	public void avviaAccoppiatoZootecniaOviCaprini() throws Exception {
		String url = urlDomandaUnicaElencoCapi.concat("?params=");
		//String params251_320 = "{\"cuaa\":\"BSTLSN70E31L378L\",\"annoCampagna\":2018,\"codiceIntervento\":320,\"idAlleBDN\":1279014}";
		CapiAziendaPerInterventoFilter params251_320= new CapiAziendaPerInterventoFilter(
				"BSTLSN70E31L378L",
				2018,
				InterventoZootecnico.AGNELLA,
				1279014,
				null
				);

		//String params251_321 = "{\"cuaa\":\"BSTLSN70E31L378L\",\"annoCampagna\":2018,\"codiceIntervento\":321,\"idAlleBDN\":1279014}";
		CapiAziendaPerInterventoFilter params251_321= new CapiAziendaPerInterventoFilter(
				"BSTLSN70E31L378L",
				2018,
				InterventoZootecnico.OVICAPRINO_MACELLATO,
				1279014,
				null
				);
		// params non adattato perchè codice intervento inesistente
		//String params251_555 = "{\"cuaa\":\"BSTLSN70E31L378L\",\"annoCampagna\":2018,\"codiceIntervento\":555,\"idAlleBDN\":1279014}";// lista vuota

		Mockito.when(restTemplate.getForObject(Mockito.eq(buildUrl(params251_320, "/ovicaprini")), Mockito.eq(String.class)))
		.thenReturn(getListaCapiDomanda("BSTLSN70E31L378L", "320"));
		Mockito.when(restTemplate.getForObject(Mockito.eq(buildUrl(params251_321, "/ovicaprini")), Mockito.eq(String.class)))
		.thenReturn(getListaCapiDomanda("BSTLSN70E31L378L", "321"));
		// Mockito vecchio non adattato perchè codice intervento inesistente
		//			Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(url.concat(URLEncoder.encode(params251_555, StandardCharsets.UTF_8.name())))), Mockito.eq(String.class)))
		//			.thenReturn(getListaCapiDomanda("BSTLSN70E31L378L", "555"));
		// CustomThreadLocal.addVariable(A4gIstruttoriaConstants.HEADER_CF, "betty");

		calcoloCapiService.elabora(istruttoriaDao.findByDomandaUnicaModel_idAndSostegno(251L,Sostegno.ZOOTECNIA).get(0).getId());


		// controllo cambiamento di stato sostegno
		IstruttoriaModel a4gtLavorazioneSostegno = new IstruttoriaModel();
		a4gtLavorazioneSostegno.setSostegno(Sostegno.ZOOTECNIA);
		daoDomanda.findAllById(Arrays.asList(251L)).forEach(domanda -> {
			a4gtLavorazioneSostegno.setDomandaUnicaModel(domanda);
			Optional<IstruttoriaModel> statoLavorazioneSostegno = istruttoriaDao.findOne(Example.of(a4gtLavorazioneSostegno));
			if (statoLavorazioneSostegno.isPresent()) {
				entityManager.refresh(statoLavorazioneSostegno.get());
				assertEquals(StatoIstruttoria.NON_AMMISSIBILE.getStatoIstruttoria(), statoLavorazioneSostegno.get().getA4gdStatoLavSostegno().getIdentificativo());
			} else {
				log.info("Domanda ".concat(domanda.getId().toString()).concat(" non presente in A4GT_ISTRUTTORIA"));
				fail();
			}
		});
		// 356L ha il 320 -> tutti ammissibili
		// 353L ha il 321 -> tutti non ammissibili
		// UBA = numerocapi ammissibili * 0.15 < 3
		daoRichiestaAllevam.findAllById(Arrays.asList(356L, 353L)).forEach(allevamento -> {
			assertTrue(!allevamento.getEsitiCalcoloCapi().isEmpty());
			allevamento.getEsitiCalcoloCapi().forEach(capo -> {
				log.info("codiceCapo :" + capo.getCodiceCapo() + " " + capo.getEsito().toString());
				log.info("messaggio :" + capo.getMessaggio());
				String codiceAgea = allevamento.getIntervento().getCodiceAgea();
				if (codiceAgea.equals("320")) { // tutti ammissibili
					assertEquals(EsitoCalcoloCapo.AMMISSIBILE, capo.getEsito());
				}
				if (codiceAgea.equals("321")) { // tutti non ammissibili
					assertEquals(EsitoCalcoloCapo.NON_AMMISSIBILE, capo.getEsito());
				}
			});
		});
	}

	@Ignore
	@Test
	@Transactional
	public void avviaAccoppiatoZootecniaControlliLatte() throws Exception {

		// INSERT H2 SU REGISTRO
		// 254 - tutto ok (br 4 due condizioni su tre sono ok) (ha venduto due volte latte) (alpeggio ritondante)
		// 255 - non vende latte (br1 - cuaa non presente)
		// 256 - non ha fatto analisi nei mesi non di alpeggio (br2 false)
		// 257 - ha fatto analisi tranne per 2 mesi - alpeggio presente non per quei 2 mesi (br2 false + br3 false)
		// 258 - ha fatto le analisi per tutti i mesi e tutte le medie sono fuori limite (br4 false)

		String url = urlDomandaUnicaElencoCapi.concat("?params=");
		//			String params254_310 = "{\"cuaa\":\"SSNLSN69R19L378C\",\"annoCampagna\":2018,\"codiceIntervento\":310,\"idAlleBDN\":1279014}";
		CapiAziendaPerInterventoFilter params254_310= new CapiAziendaPerInterventoFilter(
				"SSNLSN69R19L378C",
				2018,
				InterventoZootecnico.VACCA_LATTE,
				1279014,
				null
				);
		//			String params255_310 = "{\"cuaa\":\"SSNLSN69R19L378B\",\"annoCampagna\":2018,\"codiceIntervento\":310,\"idAlleBDN\":1279014}";
		CapiAziendaPerInterventoFilter params255_310= new CapiAziendaPerInterventoFilter(
				"SSNLSN69R19L378B",
				2018,
				InterventoZootecnico.VACCA_LATTE,
				1279014,
				null
				);
		//			String params256_310 = "{\"cuaa\":\"SSNLSN69R19L378A\",\"annoCampagna\":2018,\"codiceIntervento\":310,\"idAlleBDN\":1279014}";
		CapiAziendaPerInterventoFilter params256_310= new CapiAziendaPerInterventoFilter(
				"SSNLSN69R19L378A",
				2018,
				InterventoZootecnico.VACCA_LATTE,
				1279014,
				null
				);
		//			String params257_310 = "{\"cuaa\":\"SSNLSN69R19L378D\",\"annoCampagna\":2018,\"codiceIntervento\":310,\"idAlleBDN\":1279014}";
		CapiAziendaPerInterventoFilter params257_310= new CapiAziendaPerInterventoFilter(
				"SSNLSN69R19L378D",
				2018,
				InterventoZootecnico.VACCA_LATTE,
				1279014,
				null
				);
		//			String params258_310 = "{\"cuaa\":\"SSNLSN69R19L378E\",\"annoCampagna\":2018,\"codiceIntervento\":310,\"idAlleBDN\":1279014}";
		CapiAziendaPerInterventoFilter params258_310= new CapiAziendaPerInterventoFilter(
				"SSNLSN69R19L378E",
				2018,
				InterventoZootecnico.VACCA_LATTE,
				1279014,
				null
				);
		Mockito.when(restTemplate.getForObject(Mockito.eq(buildUrl(params254_310, "/bovini/lattecarne")), Mockito.eq(String.class)))
		.thenReturn(getListaCapiDomanda("SSNLSN69R19L378C", "310"));
		Mockito.when(restTemplate.getForObject(Mockito.eq(buildUrl(params255_310, "/bovini/lattecarne")), Mockito.eq(String.class)))
		.thenReturn(getListaCapiDomanda("SSNLSN69R19L378B", "310"));
		Mockito.when(restTemplate.getForObject(Mockito.eq(buildUrl(params256_310, "/bovini/lattecarne")), Mockito.eq(String.class)))
		.thenReturn(getListaCapiDomanda("SSNLSN69R19L378A", "310"));
		Mockito.when(restTemplate.getForObject(Mockito.eq(buildUrl(params257_310, "/bovini/lattecarne")), Mockito.eq(String.class)))
		.thenReturn(getListaCapiDomanda("SSNLSN69R19L378D", "310"));
		Mockito.when(restTemplate.getForObject(Mockito.eq(buildUrl(params258_310, "/bovini/lattecarne")), Mockito.eq(String.class)))
		.thenReturn(getListaCapiDomanda("SSNLSN69R19L378E", "310"));
		// CustomThreadLocal.addVariable(A4gIstruttoriaConstants.HEADER_CF, "betty");

		calcoloCapiService.elabora(istruttoriaDao.findByDomandaUnicaModel_idAndSostegno(254L,Sostegno.ZOOTECNIA).get(0).getId());
		calcoloCapiService.elabora(istruttoriaDao.findByDomandaUnicaModel_idAndSostegno(255L,Sostegno.ZOOTECNIA).get(0).getId());
		calcoloCapiService.elabora(istruttoriaDao.findByDomandaUnicaModel_idAndSostegno(256L,Sostegno.ZOOTECNIA).get(0).getId());
		calcoloCapiService.elabora(istruttoriaDao.findByDomandaUnicaModel_idAndSostegno(257L,Sostegno.ZOOTECNIA).get(0).getId());
		calcoloCapiService.elabora(istruttoriaDao.findByDomandaUnicaModel_idAndSostegno(258L,Sostegno.ZOOTECNIA).get(0).getId());



		daoRichiestaAllevam.findAllById(Arrays.asList(361L, 362L, 363L, 364L, 365L)).forEach(allevamento -> {
			assertTrue(!allevamento.getEsitiCalcoloCapi().isEmpty());
			log.info("allevamento id :" + allevamento.getId());
			allevamento.getEsitiCalcoloCapi().forEach(capo -> {
				log.info("codiceCapo :" + capo.getCodiceCapo() + " " + capo.getEsito().toString());
				log.info("messaggio :" + capo.getMessaggio());
				if (allevamento.getId().equals(361L)) { // controlli latte ok
					assertEquals(EsitoCalcoloCapo.AMMISSIBILE, capo.getEsito());
				} else { // controlli latte ko
					assertEquals(EsitoCalcoloCapo.NON_AMMISSIBILE, capo.getEsito());
				}
			});
		});
	}

	/*La chiamata al servizio ../domandaunica/elencocapi con parametri [{"cuaa":"ZZNRLB69M46F205A","annoCampagna":2018,"codiceIntervento":310,"idAlleBDN":1279014}] ha restituito i seguenti codici di errore. errCod=something goes wrong,  errDescr=bdn non disponibile. + 
	 * Eccezione gestita lanciata qui it.tndigitale.a4gistruttoria.action.CallElencoCapiAction.getElencoCapi(CallElencoCapiAction.java:52)*/
	@Ignore
	@Test
	@Transactional
	public void avviaAccoppiatoZootecniaServizioBdnErrore() throws Exception {



		// Imposto nel thread local l'utente per far funzionare quando parte il flush la parte asincrona
		// CustomThreadLocal.addVariable(A4gIstruttoriaConstants.HEADER_CF, "betty");
		String url = urlDomandaUnicaElencoCapi.concat("?params=");
		String params262_310 = "{\"cuaa\":\"ZZNRLB69M46F205A\",\"annoCampagna\":2018,\"codiceIntervento\":310,\"idAlleBDN\":1279014}";
		String params262_320 = "{\"cuaa\":\"ZZNRLB69M46F205A\",\"annoCampagna\":2018,\"codiceIntervento\":320,\"idAlleBDN\":1279014}";
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(url.concat(URLEncoder.encode(params262_310, StandardCharsets.UTF_8.name())))), Mockito.eq(String.class)))
		.thenReturn(getListaCapiDomanda("ZZNRLB69M46F205A", "310"));
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(url.concat(URLEncoder.encode(params262_320, StandardCharsets.UTF_8.name())))), Mockito.eq(String.class)))
		.thenReturn(getListaCapiDomanda("ZZNRLB69M46F205A", "320"));

		calcoloCapiService.elabora(istruttoriaDao.findByDomandaUnicaModel_idAndSostegno(262L,Sostegno.ZOOTECNIA).get(0).getId());



		//	        AccoppiatoZootecniaJobDto accoppiatoZootecniaJobDto = new AccoppiatoZootecniaJobDto();
		//	        accoppiatoZootecniaJobDto.setCampagna(2018L);
		//	        accoppiatoZootecniaJobDto.setIdsDomande(Arrays.asList(262L));
		//	        this.mockMvc.perform(post("/api/v1/domande/az/avvia").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(accoppiatoZootecniaJobDto)));
		//	        await().atMost(1, TimeUnit.MINUTES).until(processoInEsecuzione());
		// FIXME da capire il perche' "idProcesso" risulta null dopo aver disabilitato "CustomThreadLocal.addVariable(A4gIstruttoriaConstants.HEADER_CF, "betty")"



		// Long idProcesso = (Long) CustomThreadLocal.getVariable("idProcesso");
		// // controllo sullo stato del processo
		// A4gtProcesso processo = processoDao.getOne(idProcesso);
		// JsonNode datiElaborazione = objectMapper.readTree(processo.getDatiElaborazione());
		//
		// assertNotNull(processo.getDtFine());
		// assertEquals(new BigDecimal(100), processo.getPercentualeAvanzamento());
		// assertEquals(1, datiElaborazione.path("totaleDomandeGestite").asLong());
		// assertEquals(1, datiElaborazione.path("numeroDomandeDaElaborare").asLong());
		// assertTrue(datiElaborazione.path("domandeConProblemi").elements().hasNext());
		// assertFalse((datiElaborazione.findValue("domandeGestite").elements().hasNext()));



		// controllo cambiamento di stato sostegno
		IstruttoriaModel a4gtLavorazioneSostegno = new IstruttoriaModel();
		a4gtLavorazioneSostegno.setSostegno(Sostegno.ZOOTECNIA);
		daoDomanda.findAllById(Arrays.asList(262L)).forEach(domanda -> {
			a4gtLavorazioneSostegno.setDomandaUnicaModel(domanda);
			Optional<IstruttoriaModel> statoLavorazioneSostegno = istruttoriaDao.findOne(Example.of(a4gtLavorazioneSostegno));
			entityManager.refresh(statoLavorazioneSostegno.get());
			if (statoLavorazioneSostegno.isPresent()) {
				// domanda rollback
				assertEquals(StatoIstruttoria.RICHIESTO.getStatoIstruttoria(), statoLavorazioneSostegno.get().getA4gdStatoLavSostegno().getIdentificativo());
			} else {
				log.info("Domanda ".concat(domanda.getId().toString()).concat(" non presente in A4GT_ISTRUTTORIA"));
				fail();
			}
		});
	}

	@Ignore
	@Test
	@Transactional
	public void avviaAccoppiatoZootecniaServizioExtNonDisponibile() throws Exception {

		Mockito.when(restTemplate.getForObject(Mockito.any(), Mockito.any())).thenThrow(new RuntimeException("Servizio BDN non disponibile"));
		// Imposto nel thread local l'utente per far funzionare quando parte il flush la parte asincrona
		// CustomThreadLocal.addVariable(A4gIstruttoriaConstants.HEADER_CF, "betty");
		AccoppiatoZootecniaJobDto accoppiatoZootecniaJobDto = new AccoppiatoZootecniaJobDto();
		accoppiatoZootecniaJobDto.setCampagna(2018L);
		accoppiatoZootecniaJobDto.setIdsDomande(Arrays.asList(248L, 250L, 245L));
		this.mockMvc.perform(post("/api/v1/domande/az/avvia").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(accoppiatoZootecniaJobDto)));
		await().atMost(1, TimeUnit.MINUTES).until(processoInEsecuzione());

		DomandaFilter domandaFilter = new DomandaFilter(); // per ogni domanda in istruttoria
		domandaFilter.setStati(Arrays.asList(StatoDomanda.IN_ISTRUTTORIA));
		// interventi da 310 a 314 e 322 (vacche da latte e nutrici)
		domandaFilter.setCodiciAgea(Arrays.asList(interventiCodiciVacche));
	}







	//Da rivedere
	@Ignore
	@Test(expected = NestedServletException.class)
	@Transactional
	public void avviaAccoppiatoZootecniaProcessoInEsecuzione() throws Exception {

		A4gtProcesso a4gtProcesso = new A4gtProcesso();

		a4gtProcesso.setStato(StatoProcesso.RUN); // 104 stato PROCESSO_RUN
		a4gtProcesso.setTipo(TipoProcesso.CALCOLO_ACCOPPIATO_ZOOTECNIA); // 109 tipo CLC_ACCOPPIATO_ZOOTENIA
		a4gtProcesso.setDtInizio(new Date());
		a4gtProcesso.setPercentualeAvanzamento(new BigDecimal(0));
		a4gtProcesso = processoDao.save(a4gtProcesso);

		// Imposto nel thread local l'utente per far funzionare quando parte il flush la parte asincrona
		// CustomThreadLocal.addVariable(A4gIstruttoriaConstants.HEADER_CF, "betty");
		AccoppiatoZootecniaJobDto accoppiatoZootecniaJobDto = new AccoppiatoZootecniaJobDto();
		accoppiatoZootecniaJobDto.setCampagna(2018L);
		accoppiatoZootecniaJobDto.setIdsDomande(Arrays.asList(248L, 250L, 245L));
		this.mockMvc.perform(post("/api/v1/domande/az/avvia").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(accoppiatoZootecniaJobDto)));
		await().atMost(1, TimeUnit.MINUTES).until(processoInEsecuzione());
		processoDao.delete(a4gtProcesso);
	}

	@Ignore
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/zootecnia/capiRichiesti_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/zootecnia/capiRichiesti_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void calcoloCapiRichiesti() throws Exception {
		//String params8664187_310 = "{\"cuaa\":\"ZPPPLA84L17C794M\",\"annoCampagna\":2019,\"codiceIntervento\":310,\"idAlleBDN\":}";
		CapiAziendaPerInterventoFilter params8664187_310= new CapiAziendaPerInterventoFilter(
				"ZPPPLA84L17C794M",
				2019,
				InterventoZootecnico.VACCA_LATTE,
				2020029,
				null
				);
		//String params8664187_311 = "{\"cuaa\":\"ZPPPLA84L17C794M\",\"annoCampagna\":2019,\"codiceIntervento\":311,\"idAlleBDN\":2020029}";
		CapiAziendaPerInterventoFilter params8664187_311= new CapiAziendaPerInterventoFilter(
				"ZPPPLA84L17C794M",
				2019,
				InterventoZootecnico.VACCA_LATTE_MONTANA,
				2020029,
				null
				);
		//String params8664187_313 = "{\"cuaa\":\"ZPPPLA84L17C794M\",\"annoCampagna\":2019,\"codiceIntervento\":313,\"idAlleBDN\":2020029}";
		CapiAziendaPerInterventoFilter params8664187_313= new CapiAziendaPerInterventoFilter(
				"ZPPPLA84L17C794M",
				2019,
				InterventoZootecnico.VACCA_NUTRICE,
				2020029,
				null
				);
		Mockito.when(restTemplate.getForObject(Mockito.eq(buildUrl(params8664187_310, "/bovini/lattecarne")), Mockito.eq(String.class)))
		.thenReturn(getListaCapiDomanda("ZPPPLA84L17C794M", "310"));
		Mockito.when(restTemplate.getForObject(Mockito.eq(buildUrl(params8664187_311, "/bovini/lattecarne")), Mockito.eq(String.class)))
		.thenReturn(getListaCapiDomanda("ZPPPLA84L17C794M", "311"));
		Mockito.when(restTemplate.getForObject(Mockito.eq(buildUrl(params8664187_313, "/bovini/lattecarne")), Mockito.eq(String.class)))
		.thenReturn(getListaCapiDomanda("ZPPPLA84L17C794M", "313"));

		calcoloCapiService.elabora(istruttoriaDao.findByDomandaUnicaModel_idAndSostegno(8664187L,Sostegno.ZOOTECNIA).get(0).getId());

		IstruttoriaModel istruttoria = istruttoriaDao.findById(8675888L).get();
		Set<AllevamentoImpegnatoModel> sostegniZootecnici = istruttoria.getDomandaUnicaModel().getAllevamentiImpegnati();
		String[] interventi =  {"310", "311", "313"};
		checkBRCalcoloCapiRichiestiStream(sostegniZootecnici, 2019, interventi);
	}

	private void checkBRCalcoloCapiRichiestiStream(Set<AllevamentoImpegnatoModel> sostegniZootecnici, Integer campagna, String[] interventi) {
		Map<String, List<EsitoCalcoloCapoModel>> capiByCodice = sostegniZootecnici.stream()
				.filter(allevamento -> Arrays.asList(interventi).contains(allevamento.getIntervento().getCodiceAgea()))
				.flatMap(allevamento -> allevamento.getEsitiCalcoloCapi().stream())
				.filter(esito -> !EsitoCalcoloCapo.NON_AMMISSIBILE.equals(esito.getEsito()))
				.collect(groupingBy(EsitoCalcoloCapoModel::getCodiceCapo));
		capiByCodice.entrySet().stream()
		.forEach(e -> {
			List<EsitoCalcoloCapoModel> capoInterventi = e.getValue();

			Long count = capoInterventi.stream()
					.filter(cap -> cap.getRichiesto() != null && cap.getRichiesto() == true).count();

			if (count == 0 || count > 1) {
				fail();
			}

			capoInterventi.stream()
			.sorted((EsitoCalcoloCapoModel a, EsitoCalcoloCapoModel b ) -> {
				ImportoUnitarioInterventoModel importoUnitarioA = importoUnitarioDao.findByCampagnaAndIntervento_identificativoIntervento(
						campagna, 
						a.getAllevamentoImpegnato().getIntervento().getIdentificativoIntervento());
				ImportoUnitarioInterventoModel importoUnitarioB = importoUnitarioDao.findByCampagnaAndIntervento_identificativoIntervento(
						campagna, 
						b.getAllevamentoImpegnato().getIntervento().getIdentificativoIntervento());							
				return importoUnitarioA.getPriorita().compareTo(importoUnitarioB.getPriorita());
			})
			.findFirst()
			.ifPresent(capo -> {
				if (capo.getRichiesto() == null || capo.getRichiesto() != true) {
					fail();
				}
			});
		});
	}


	@Ignore
	@Test
	@Transactional(readOnly = true)
	@Sql(scripts = { "/DomandaUnica/zootecnia/cuaa_subentrante.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/zootecnia/cuaa_subentrante_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void avviaAccoppiatoZootecnia_CuaSubentrante() throws Exception {
		CapiAziendaPerInterventoFilter filtro310= new CapiAziendaPerInterventoFilter(
				"TRRLNI67A43L329X",
				2018,
				InterventoZootecnico.VACCA_LATTE,
				1576919,
				"02523180228"
				);
		Mockito
		.when(restTemplate.getForObject(Mockito.eq(buildUrl(filtro310, "/bovini/lattecarne")), Mockito.eq(String.class)))
		.thenReturn(getListaCapiDomanda("TRRLNI67A43L329X", "310"));

		calcoloCapiService.elabora(202004091l);
		Optional<IstruttoriaModel> statoLavorazioneSostegno = istruttoriaDao.findById(202004091l);
		if (statoLavorazioneSostegno.isPresent()) {
			entityManager.refresh(statoLavorazioneSostegno.get());
			assertEquals(StatoIstruttoria.INTEGRATO, statoLavorazioneSostegno.get().getStato());
		} else {
			log.info("Istruttoria 202004091l  non presente in A4GT_ISTRUTTORIA");
			fail();
		}
		daoRichiestaAllevam.findById(202004091l)
		.ifPresent(allevamento -> {
			assertTrue(!allevamento.getEsitiCalcoloCapi().isEmpty());
			allevamento.getEsitiCalcoloCapi().forEach(capo -> {
				log.info("codiceCapo :" + capo.getCodiceCapo() + " " + capo.getEsito().toString());
				log.info("messaggio :" + capo.getMessaggio());
			});
			long countAmm=
					allevamento.getEsitiCalcoloCapi().stream()
					.filter(capo -> EsitoCalcoloCapo.AMMISSIBILE.equals(capo.getEsito()))
					.count();
			long countRichiesti=
					allevamento.getEsitiCalcoloCapi().stream()
					.filter(capo -> Boolean.TRUE.equals(capo.getRichiesto()))
					.count();	
			assertSame(countAmm,countRichiesti);
			//Il capo non è ammissibile perché non sono stati rispettati i tempi di identificazione e registrazione del vitello in quanto è stato superato il limite 34 tra la data di registrazione della nascita 01/10/2018 e la data di nascita 25/08/2018 che è pari a 37 ( delegata la registrazione nascita; allevamento non autorizzato a proroga marcatura)
			long countNonAmm=
					allevamento.getEsitiCalcoloCapi().stream()
					.filter(capo -> EsitoCalcoloCapo.NON_AMMISSIBILE.equals(capo.getEsito()))
					.count();
			assertSame(countNonAmm,1l);
		});

	}


	private String getListaCapiDomanda(String cuaa, String numeroIntervento) throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/zootecnia/".concat(cuaa).concat("_").concat(numeroIntervento).concat(".json")));
		return objectMapper.writeValueAsString(response);
	}

	private Callable<Boolean> processoInEsecuzione() {
		return new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				ProcessoFilter processoFilter = new ProcessoFilter();
				// processoFilter.setStatoProcesso(StatoProcesso.RUN.getStatoProcesso());
				processoFilter.setTipoProcesso(TipoProcesso.CALCOLO_ACCOPPIATO_ZOOTECNIA);

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
}
