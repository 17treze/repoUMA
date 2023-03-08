package it.tndigitale.a4gistruttoria.service.businesslogic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.api.ApiUrls;
import it.tndigitale.a4gistruttoria.dto.AgricoltoreSIAN;
import it.tndigitale.a4gistruttoria.dto.DomandaUnica;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiInput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.dao.PassoTransizioneDao;
import it.tndigitale.a4gistruttoria.repository.dao.StatoLavSostegnoDao;
import it.tndigitale.a4gistruttoria.repository.dao.TransizioneIstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gdStatoLavSostegno;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.DomandeService;
import it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato.CalcoloIstruttoriaDisaccoppiatoService;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.CalcoloSostegnoException;
import it.tndigitale.a4gistruttoria.util.CodiceEsito;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.visualizza", "a4gistruttoria.pac.domandaUnica.edita" })
public class CalcoloIstruttoriaDisaccoppiatoServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(CalcoloIstruttoriaDisaccoppiatoServiceTest.class);

	@Autowired
	CalcoloIstruttoriaDisaccoppiatoService calcoloDisaccoppiatoManager;

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private RestTemplate restTemplate;

	@Autowired
	private DomandeService serviceDomande;
	@Autowired
	private DomandaUnicaDao daoDomanda;
	@Autowired
	private IstruttoriaDao daoIstruttoria;
	
	@Autowired
	private TransizioneIstruttoriaDao transizioneIstruttoriaDao;
	@Autowired
	private StatoLavSostegnoDao daoStatoLavorazioneSostegno;
	@Autowired
	private PassoTransizioneDao daoPassiLavSostegno;

	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/dataMatriceCompatibilita.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "delete from a4gd_coltura_intervento", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testCalcoloDisaccoppiatoOk() throws Exception {
		String serviceAgs = "http://localhost:8080/ags/api/v1/domandeDU/".concat("183109")
				.concat("?expand=sostegniSuperficie,sostegniAllevamento,datiPascolo,dichiarazioni");
		DomandaUnica du = objectMapper.readValue(getDomandaUnicaAgsExpanded(), new TypeReference<DomandaUnica>() {
		});
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceAgs)), Mockito.eq(DomandaUnica.class))).thenReturn(du);

		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(183109));
		
		IstruttoriaModel istruttoria = 
				daoIstruttoria.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);

		serviceDomande.recuperaSostegniDomandaDU(domanda);

		String jsonRequest = "{ \"codFisc\":\"PLOLNZ71B07E565W\", \"annoCamp\":2018}";
		String serviceUrlA4GPROXYrecuperoAgricoltoreSian = "http://localhost:8080/a4gproxy/api/v1/agricoltore?params=".concat(URLEncoder.encode(jsonRequest, "UTF-8"));
		AgricoltoreSIAN responseAgricoltoreSian = objectMapper.readValue(getResponseAgricoltoreSian(), AgricoltoreSIAN.class);
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlA4GPROXYrecuperoAgricoltoreSian)), Mockito.eq(AgricoltoreSIAN.class))).thenReturn(responseAgricoltoreSian);

		serviceAgs = "http://localhost:8080/ags/api/v1/domandeDU/".concat(domanda.getNumeroDomanda().toString()).concat("?expand=infoIstruttoria");
		du = objectMapper.readValue(getInfoIsturttoria(), new TypeReference<DomandaUnica>() {
		});

		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceAgs)), Mockito.eq(DomandaUnica.class))).thenReturn(du);

		String jsonRequestSigeco = "{\"anno\": 2018, \"numeroDomanda\": 183109}";
		String serviceUrlEsitoSigeco = "http://localhost:8080/a4gproxy/api/v1/sigeco?params=".concat(URLEncoder.encode(jsonRequestSigeco, "UTF-8"));
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlEsitoSigeco)), Mockito.eq(Long.class))).thenReturn(null);

		String serviceUrlAnomalieCoordinamento = "http://localhost:8080/a4gproxy/api/v1/anomaliecoordinamento";
		Mockito.when(restTemplate.getForObject(Mockito.eq(serviceUrlAnomalieCoordinamento.concat("/{idParcella}?annoCampagna={annoCampagna}")), Mockito.eq(Integer.class), Mockito.anyLong(),
				Mockito.anyLong())).thenReturn(0);
		String params = URLEncoder.encode("{\"codicePac\":\"PAC1420\",\"tipoDomanda\":\"DU\",\"anno\":2017,\"cuaa\":\"PLOLNZ71B07E565W\"}", "UTF-8");

		String serviceCheckPresenzaDomandeFiltered = "http://localhost:8080/ags/api/v1/domandeDU/checkPresenzaDomandeFiltered?params=".concat(params);
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceCheckPresenzaDomandeFiltered)), Mockito.eq(Boolean.class))).thenReturn(false);

		calcoloDisaccoppiatoManager.elabora(istruttoria.getId());

		// 1. Verifico la presenza di una transizione di tipo Calcolo disaccoppiato per la domanda
		A4gdStatoLavSostegno statoLavSostegno = daoStatoLavorazioneSostegno.findByIdentificativo(StatoIstruttoria.CONTROLLI_CALCOLO_OK.getStatoIstruttoria());
		Optional<TransizioneIstruttoriaModel> transizioneSostegno = 
				transizioneIstruttoriaDao.findTransizioneCalcoloPremio(istruttoria).stream().findAny();

		if (transizioneSostegno.isPresent()) {
			assertEquals(statoLavSostegno.getIdentificativo(), transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo());
		} else {
			assertFalse(true);
		}

		// 2. Verifico il numero di passi di lavorazione eseguiti
		List<PassoTransizioneModel> passiLavSostegno = daoPassiLavSostegno.findByTransizioneIstruttoria(transizioneSostegno.get());
		assertEquals(7, passiLavSostegno.size());
		
		Map<TipologiaPassoTransizione, CodiceEsito> mappaPassoEsito = new HashMap<>();
		mappaPassoEsito.put(TipologiaPassoTransizione.AMMISSIBILITA, CodiceEsito.DUT_001);
		mappaPassoEsito.put(TipologiaPassoTransizione.ANOMALIE_MANTENIMENTO, CodiceEsito.DUT_002);
		mappaPassoEsito.put(TipologiaPassoTransizione.RIDUZIONI_BPS, CodiceEsito.DUT_005);
		mappaPassoEsito.put(TipologiaPassoTransizione.SANZIONI_BPS, CodiceEsito.DUT_019);
		mappaPassoEsito.put(TipologiaPassoTransizione.GREENING, CodiceEsito.DUT_026);
		mappaPassoEsito.put(TipologiaPassoTransizione.GIOVANE_AGRICOLTORE, CodiceEsito.DUT_034);
		mappaPassoEsito.put(TipologiaPassoTransizione.CONTROLLI_FINALI, CodiceEsito.DUF_014);
		
		

		Map<String, List<String>> mappaPassoEsitoControlli = new HashMap<>();
		mappaPassoEsitoControlli.put(TipologiaPassoTransizione.AMMISSIBILITA.name(),
				Arrays.asList("\"tipoControllo\":\"BRIDUSDC009_infoAgricoltoreAttivo\",\"esito\":true",
						"\"tipoControllo\":\"BRIDUSDC011_impegnoTitoli\",\"esito\":true",
						"\"tipoControllo\":\"BRIDUSDC010_agricoltoreAttivo\",\"esito\":true",
						"\"tipoControllo\":\"BRIDUSDC012_superficieMinima\",\"esito\":true"));
		mappaPassoEsitoControlli.put(TipologiaPassoTransizione.SANZIONI_BPS.name(),
				Arrays.asList("tipoControllo\":\"BRIDUSDC109_isDomandaLiqAnnoPrec\",\"esito\":true",
						"\"tipoControllo\":\"BRIDUSDC029_recidiva\",\"esito\":false"));
		mappaPassoEsitoControlli.put(TipologiaPassoTransizione.CONTROLLI_FINALI.name(),
				Arrays.asList("\"tipoControllo\":\"BRIDUSDC036_verificaRitardo\",\"esito\":false",
						"\"tipoControllo\":\"BRIDUSDC043_riduzioneCapping\",\"esito\":false"));

		Map<String, List<String>> mappaPassoOutput = new HashMap<>();
		mappaPassoOutput.put(TipologiaPassoTransizione.AMMISSIBILITA.name(),
				Arrays.asList("\"tipoVariabile\":\"BPSSUPRIC\",\"valNumber\":6.1900",
						"\"tipoVariabile\":\"TITVALRID\",\"valNumber\":90.68",
						"\"tipoVariabile\":\"BPSIMPRIC\",\"valNumber\":561.31",
						"\"tipoVariabile\":\"GRESUPRIC\",\"valNumber\":6.1900"));
		mappaPassoOutput.put(TipologiaPassoTransizione.RIDUZIONI_BPS.name(),
				Arrays.asList("\"tipoVariabile\":\"BPSSUPDET\",\"valNumber\":7.0867",
						"\"tipoVariabile\":\"BPSSUPAMM\",\"valNumber\":6.1900",
						"\"tipoVariabile\":\"BPSIMPAMM\",\"valNumber\":561.31"));
		mappaPassoOutput.put(TipologiaPassoTransizione.GREENING.name(),
				Arrays.asList("\"tipoVariabile\":\"GRESUPBASE\",\"valNumber\":6.1900",
						"\"tipoVariabile\":\"GREIMPBASE\",\"valNumber\":279.48",
						"\"tipoVariabile\":\"GRESUPAMM\",\"valNumber\":6.1900",
						"\"tipoVariabile\":\"GREIMPAMM\",\"valNumber\":279.48",
						"\"tipoVariabile\":\"GRESUPARB\",\"valNumber\":7.0168",
						"\"tipoVariabile\":\"GRESUPDET\",\"valNumber\":7.0867",
						"\"tipoVariabile\":\"GRESUPPP\",\"valNumber\":0.0699"));
		mappaPassoOutput.put(TipologiaPassoTransizione.CONTROLLI_FINALI.name(),
				Arrays.asList("\"tipoVariabile\":\"BPSIMPRIDRIT\",\"valNumber\":0.00",
						"\"tipoVariabile\":\"BPSIMPRIDLIN1\",\"valNumber\":28.07",
						"\"tipoVariabile\":\"BPSIMPBCCAP\",\"valNumber\":529.24",
						"\"tipoVariabile\":\"BPSIMPCALCFIN\",\"valNumber\":106.65",
						"\"tipoVariabile\":\"GREIMPCALCFIN\",\"valNumber\":55.90",
						"\"tipoVariabile\":\"BPSIMPCALCFINLORDO\",\"valNumber\":106.65",
						"\"tipoVariabile\":\"GREIMPCALCFINLORDO\",\"valNumber\":55.90",
						"\"tipoVariabile\":\"IMPCALC\",\"valNumber\":840.79",
						"\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":162.55",
						"\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":162.55"));
		for (PassoTransizioneModel passo : passiLavSostegno) {
			assertEquals(mappaPassoEsito.get(passo.getCodicePasso()).getCodiceEsito(), passo.getCodiceEsito());

			List<String> esitiControlli = mappaPassoEsitoControlli.get(passo.getCodicePasso());
			if (esitiControlli != null && !esitiControlli.isEmpty()) {
				assertTrue(esitiControlli.stream().allMatch(esito -> passo.getDatiSintesiLavorazione().contains(esito)));
			}
			List<String> output = mappaPassoOutput.get(passo.getCodicePasso());
			if (output != null && !output.isEmpty()) {
				boolean allMatch = output.stream().allMatch(o -> passo.getDatiOutput().contains(o));
				assertTrue(allMatch);
			}
		}
	}

	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/dataMatriceCompatibilita.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "delete from a4gd_coltura_intervento", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testNoSupEleggibili() throws Exception {
		String serviceAgs = "http://localhost:8080/ags/api/v1/domandeDU/".concat("183109")
				.concat("?expand=sostegniSuperficie,sostegniAllevamento,datiPascolo,dichiarazioni");
		DomandaUnica du = objectMapper.readValue(getDomandaUnicaAgsExpanded(), new TypeReference<DomandaUnica>() {
		});
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceAgs)), Mockito.eq(DomandaUnica.class))).thenReturn(du);

		DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(new BigDecimal(183109));
		serviceDomande.recuperaSostegniDomandaDU(d);
		IstruttoriaModel istruttoria = 
				daoIstruttoria.findByDomandaUnicaModelAndSostegnoAndTipologia(d, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);

		String jsonRequest = "{ \"codFisc\":\"PLOLNZ71B07E565W\", \"annoCamp\":2018}";
		String serviceUrlA4GPROXYrecuperoAgricoltoreSian = "http://localhost:8080/a4gproxy/api/v1/agricoltore?params=".concat(URLEncoder.encode(jsonRequest, "UTF-8"));
		AgricoltoreSIAN responseAgricoltoreSian = objectMapper.readValue(getResponseAgricoltoreSian(), AgricoltoreSIAN.class);
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlA4GPROXYrecuperoAgricoltoreSian)), Mockito.eq(AgricoltoreSIAN.class))).thenReturn(responseAgricoltoreSian);

		serviceAgs = "http://localhost:8080/ags/api/v1/domandeDU/".concat(d.getNumeroDomanda().toString()).concat("?expand=infoIstruttoria");
		du = objectMapper.readValue(getInfoIstruttoriaNoSupEleggibili(), new TypeReference<DomandaUnica>() {
		});

		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceAgs)), Mockito.eq(DomandaUnica.class))).thenReturn(du);

		String jsonRequestSigeco = "{\"anno\": 2018, \"numeroDomanda\": 183109}";
		String serviceUrlEsitoSigeco = "http://localhost:8080/a4gproxy/api/v1/sigeco?params=".concat(URLEncoder.encode(jsonRequestSigeco, "UTF-8"));
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlEsitoSigeco)), Mockito.eq(Long.class))).thenReturn(null);

		String serviceUrlAnomalieCoordinamento = "http://localhost:8080/a4gproxy/api/v1/anomaliecoordinamento";
		Mockito.when(restTemplate.getForObject(Mockito.eq(serviceUrlAnomalieCoordinamento.concat("/{idParcella}?annoCampagna={annoCampagna}")), Mockito.eq(Integer.class), Mockito.anyLong(),
				Mockito.anyLong())).thenReturn(0);
		String params = URLEncoder.encode("{\"codicePac\":\"PAC1420\",\"tipoDomanda\":\"DU\",\"anno\":2017,\"cuaa\":\"PLOLNZ71B07E565W\"}", "UTF-8");

		String serviceCheckPresenzaDomandeFiltered = "http://localhost:8080/ags/api/v1/domandeDU/checkPresenzaDomandeFiltered?params=".concat(params);
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceCheckPresenzaDomandeFiltered)), Mockito.eq(Boolean.class))).thenReturn(false);

		calcoloDisaccoppiatoManager.elabora(istruttoria.getId());

		// 1. Verifico la presenza di una transizione di tipo Calcolo disaccoppiato per la domanda
		A4gdStatoLavSostegno statoLavSostegno = daoStatoLavorazioneSostegno.findByIdentificativo(StatoIstruttoria.CONTROLLI_CALCOLO_KO.getStatoIstruttoria());
		Optional<TransizioneIstruttoriaModel> transizioneSostegno = transizioneIstruttoriaDao.findTransizioneCalcoloPremio(istruttoria).stream().findAny();

		if (transizioneSostegno.isPresent()) {
			assertEquals(transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo(), statoLavSostegno.getIdentificativo());
		} else {
			assertFalse(true);
		}

		// 2. Verifico il numero di passi di lavorazione eseguiti
		List<PassoTransizioneModel> passiLavSostegno = daoPassiLavSostegno.findByTransizioneIstruttoria(transizioneSostegno.get());
		assertEquals(7, passiLavSostegno.size());
		
		Map<TipologiaPassoTransizione, CodiceEsito> mappaPassoEsito = new HashMap<>();
		mappaPassoEsito.put(TipologiaPassoTransizione.AMMISSIBILITA, CodiceEsito.DUT_001);
		mappaPassoEsito.put(TipologiaPassoTransizione.ANOMALIE_MANTENIMENTO, CodiceEsito.DUT_002);
		mappaPassoEsito.put(TipologiaPassoTransizione.RIDUZIONI_BPS, CodiceEsito.DUT_011);
		mappaPassoEsito.put(TipologiaPassoTransizione.SANZIONI_BPS, CodiceEsito.DUT_021);
		mappaPassoEsito.put(TipologiaPassoTransizione.GREENING, CodiceEsito.DUT_026);
		mappaPassoEsito.put(TipologiaPassoTransizione.GIOVANE_AGRICOLTORE, CodiceEsito.DUT_034);
		mappaPassoEsito.put(TipologiaPassoTransizione.RIEPILOGO_SANZIONI, CodiceEsito.DUF_013);
		
		Map<String, List<String>> mappaPassoOutput = new HashMap<>();
		mappaPassoOutput.put(TipologiaPassoTransizione.AMMISSIBILITA.name(),
				Arrays.asList("\"tipoVariabile\":\"BPSSUPRIC\",\"valNumber\":6.1900",
						"\"tipoVariabile\":\"BPSIMPRIC\",\"valNumber\":561.31",
						"\"tipoVariabile\":\"GRESUPRIC\",\"valNumber\":6.1900",
						"\"tipoVariabile\":\"GREIMPRIC\",\"valNumber\":279.48",
						"\"tipoVariabile\":\"TITVALRID\",\"valNumber\":90.68"));
		mappaPassoOutput.put(TipologiaPassoTransizione.RIDUZIONI_BPS.name(),
				Arrays.asList("\"tipoVariabile\":\"BPSSUPSCOST\",\"valNumber\":6.1900",
						"\"tipoVariabile\":\"BPSIMPRID\",\"valNumber\":561.31",
						"\"tipoVariabile\":\"BPSPERCSCOST\",\"valNumber\":1.0000"));
		mappaPassoOutput.put(TipologiaPassoTransizione.SANZIONI_BPS.name(),
				Arrays.asList("\"tipoVariabile\":\"BPSRECIDIVA\",\"valBoolean\":false",
						"\"tipoVariabile\":\"BPSYELLOWCARD\",\"valBoolean\":false",
						"\"tipoVariabile\":\"BPSIMPSANZ\",\"valNumber\":561.31"));
		mappaPassoOutput.put(TipologiaPassoTransizione.GREENING.name(),
				Arrays.asList("\"tipoVariabile\":\"GRESUPSEM\",\"valNumber\":0.0000",
						"\"tipoVariabile\":\"GREESESEM\",\"valBoolean\":true",
						"\"tipoVariabile\":\"GRESUPBASE\",\"valNumber\":0.0000"));
		mappaPassoOutput.put(TipologiaPassoTransizione.RIEPILOGO_SANZIONI.name(),
				Arrays.asList("\"tipoVariabile\":\"SANZTOT\",\"valNumber\":561.31",
						"\"tipoVariabile\":\"IMPSANZNORISC\",\"valNumber\":561.31",
						"\"tipoVariabile\":\"BPSIMPCALCINT\",\"valNumber\":0.00",
						"\"tipoVariabile\":\"BPSIMPCALC\",\"valNumber\":0.00"));
		
		Map<String, List<String>> mappaPassoEsitoControlli = new HashMap<>();
		mappaPassoEsitoControlli.put(TipologiaPassoTransizione.AMMISSIBILITA.name(),
				Arrays.asList("\"tipoControllo\":\"BRIDUSDC012_superficieMinima\",\"esito\":true",
						"\"tipoControllo\":\"BRIDUSDC009_infoAgricoltoreAttivo\",\"esito\":true",
						"\"tipoControllo\":\"BRIDUSDC010_agricoltoreAttivo\",\"esito\":true",
						"\"tipoControllo\":\"BRIDUSDC011_impegnoTitoli\",\"esito\":true"));
		mappaPassoEsitoControlli.put(TipologiaPassoTransizione.SANZIONI_BPS.name(),
				Arrays.asList("\"tipoControllo\":\"BRIDUSDC029_recidiva\",\"esito\":false",
						"\"tipoControllo\":\"BRIDUSDC109_isDomandaLiqAnnoPrec\",\"esito\":true",
						"\"tipoControllo\":\"BRIDUSDC030_sanzioniAnnoPrec\",\"esito\":false",
						"\"tipoControllo\":\"BRIDUSDC032_yellowCard\",\"esito\":false"));
		mappaPassoEsitoControlli.put(TipologiaPassoTransizione.GREENING.name(),
				Arrays.asList("\"tipoControllo\":\"BRIDUSDC023_infoGreening\",\"esito\":true",
						"\"tipoControllo\":\"BRIDUSDC025_impegniGreening\",\"esito\":true",
						"\"tipoControllo\":\"BRIDUSDC024_aziendaBiologica\",\"esito\":false"));
		mappaPassoEsitoControlli.put(TipologiaPassoTransizione.RIEPILOGO_SANZIONI.name(),
				Arrays.asList("\"tipoControllo\":\"BRIDUSDC031_sanzioniComminate\",\"esito\":false"));
		
		for (PassoTransizioneModel passo : passiLavSostegno) {
			assertEquals(mappaPassoEsito.get(passo.getCodicePasso()).getCodiceEsito(), passo.getCodiceEsito());

			List<String> esitiControlli = mappaPassoEsitoControlli.get(passo.getCodicePasso());
			if (esitiControlli != null && !esitiControlli.isEmpty()) {
				assertTrue(esitiControlli.stream().allMatch(esito -> passo.getDatiSintesiLavorazione().contains(esito)));
			}
			List<String> output = mappaPassoOutput.get(passo.getCodicePasso());
			if (output != null && !output.isEmpty()) {
				assertTrue(output.stream().allMatch(o -> passo.getDatiOutput().contains(o)));
			}
		}
	}

	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/dataMatriceCompatibilita.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "delete from a4gd_coltura_intervento", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Ignore //Betty 14/05/2020 : modificato0 filtri ricerca non va piu
	public void testSanzione() throws Exception {

		String serviceAgs = "http://localhost:8080/ags/api/v1/domandeDU/".concat("183430")
				.concat("?expand=sostegniSuperficie,sostegniAllevamento,datiPascolo,dichiarazioni");
		DomandaUnica du = objectMapper.readValue(getDomandaUnicaAgsExpandedSanzione(), new TypeReference<DomandaUnica>() {
		});
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceAgs)), Mockito.eq(DomandaUnica.class))).thenReturn(du);

		DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(new BigDecimal(183430));
		IstruttoriaModel istruttoria = 
				daoIstruttoria.findByDomandaUnicaModelAndSostegnoAndTipologia(d, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);
		serviceDomande.recuperaSostegniDomandaDU(d);

		String jsonRequest = "{ \"codFisc\":\"BRGNGL64C30C794G\", \"annoCamp\":2018}";
		String serviceUrlA4GPROXYrecuperoAgricoltoreSian = "http://localhost:8080/a4gproxy/api/v1/agricoltore?params=".concat(URLEncoder.encode(jsonRequest, "UTF-8"));
		AgricoltoreSIAN responseAgricoltoreSian = objectMapper.readValue(getResponseAgricoltoreSianSanzione(), AgricoltoreSIAN.class);
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlA4GPROXYrecuperoAgricoltoreSian)), Mockito.eq(AgricoltoreSIAN.class))).thenReturn(responseAgricoltoreSian);

		String serviceUrlAnomalieCoordinamento = "http://localhost:8080/a4gproxy/api/v1/anomaliecoordinamento";
		Mockito.when(restTemplate.getForObject(Mockito.eq(serviceUrlAnomalieCoordinamento.concat("/{idParcella}?annoCampagna={annoCampagna}")), Mockito.eq(Integer.class), Mockito.anyLong(),
				Mockito.anyLong())).thenReturn(0);

		String resource = "http://localhost:8080/ags/api/v1/".concat(ApiUrls.AGS_DOMANDE_DU)
			    .concat("checkPresenzaDomandeFiltered/")
			    .concat("2017/")
				.concat("BRGNGL64C30C794G");
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(resource)), Mockito.eq(Boolean.class))).thenReturn(true);
		resource = "http://localhost:8080/ags/api/v1/".concat(ApiUrls.AGS_DOMANDE_DU)
				.concat("183430")
				.concat("?expand=")
				.concat("premiSostegno.BPS_2017");
		DomandaUnica duRes = objectMapper.readValue(getDomandaUnicaAgsExpandedPremiSostegnoBPS("183430"), DomandaUnica.class);
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(resource)), Mockito.eq(DomandaUnica.class))).thenReturn(duRes);

		serviceAgs = "http://localhost:8080/ags/api/v1/domandeDU/".concat(d.getNumeroDomanda().toString()).concat("?expand=infoIstruttoria");
		du = objectMapper.readValue(getInfoIsturttoriaSanzione(), new TypeReference<DomandaUnica>() {
		});
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceAgs)), Mockito.eq(DomandaUnica.class))).thenReturn(du);

		String jsonRequestSigeco = "{\"anno\": 2018, \"numeroDomanda\": 183430}";
		String serviceUrlEsitoSigeco = "http://localhost:8080/a4gproxy/api/v1/sigeco?params=".concat(URLEncoder.encode(jsonRequestSigeco, "UTF-8"));
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlEsitoSigeco)), Mockito.eq(Long.class))).thenReturn(null);

		calcoloDisaccoppiatoManager.elabora(istruttoria.getId());

		// 1. Verifico la presenza di una transizione di tipo Calcolo disaccoppiato per la domanda
		A4gdStatoLavSostegno statoLavSostegno = daoStatoLavorazioneSostegno.findByIdentificativo(StatoIstruttoria.CONTROLLI_CALCOLO_KO.getStatoIstruttoria());
		Optional<TransizioneIstruttoriaModel> transizioneSostegno = transizioneIstruttoriaDao.findTransizioneCalcoloPremio(istruttoria).stream().findAny();

		assertTrue(transizioneSostegno.isPresent());
		//assertEquals(statoLavSostegno.getIdentificativo(), transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo());

		// 2. Verifico il numero di passi di lavorazione eseguiti
		List<PassoTransizioneModel> passiLavSostegno = daoPassiLavSostegno.findByTransizioneIstruttoria(transizioneSostegno.get());
		assertEquals(7, passiLavSostegno.size());
		
		Map<TipologiaPassoTransizione, CodiceEsito> mappaPassoEsito = new HashMap<>();
		mappaPassoEsito.put(TipologiaPassoTransizione.AMMISSIBILITA, CodiceEsito.DUT_001);
		mappaPassoEsito.put(TipologiaPassoTransizione.ANOMALIE_MANTENIMENTO, CodiceEsito.DUT_002);
		mappaPassoEsito.put(TipologiaPassoTransizione.RIDUZIONI_BPS, CodiceEsito.DUT_008);
		mappaPassoEsito.put(TipologiaPassoTransizione.SANZIONI_BPS, CodiceEsito.DUT_021);
		mappaPassoEsito.put(TipologiaPassoTransizione.GREENING, CodiceEsito.DUT_026);
		mappaPassoEsito.put(TipologiaPassoTransizione.GIOVANE_AGRICOLTORE, CodiceEsito.DUT_034);
		mappaPassoEsito.put(TipologiaPassoTransizione.RIEPILOGO_SANZIONI, CodiceEsito.DUF_013);
		
		Map<String, List<String>> mappaPassoOutput = new HashMap<>();
		mappaPassoOutput.put(TipologiaPassoTransizione.AMMISSIBILITA.name(),
				Arrays.asList("\"tipoVariabile\":\"BPSSUPRIC\",\"valNumber\":3.2100",
						"\"tipoVariabile\":\"BPSIMPRIC\",\"valNumber\":291.08",
						"\"tipoVariabile\":\"GRESUPRIC\",\"valNumber\":3.2100",
						"\"tipoVariabile\":\"GREIMPRIC\",\"valNumber\":144.93",
						"\"tipoVariabile\":\"TITVALRID\",\"valNumber\":90.68"));
		mappaPassoOutput.put(TipologiaPassoTransizione.RIDUZIONI_BPS.name(),
				Arrays.asList("\"tipoVariabile\":\"BPSSUPDET\",\"valNumber\":2.7654",
						"\"tipoVariabile\":\"BPSSUPAMM\",\"valNumber\":1.0000",
						"\"tipoVariabile\":\"BPSIMPAMM\",\"valNumber\":90.68",
						"\"tipoVariabile\":\"BPSSUPSCOST\",\"valNumber\":2.2100",
						"\"tipoVariabile\":\"BPSPERCSCOST\",\"valNumber\":2.2100",
						"\"tipoVariabile\":\"BPSIMPRID\",\"valNumber\":200.40"));
		mappaPassoOutput.put(TipologiaPassoTransizione.SANZIONI_BPS.name(),
				Arrays.asList("\"tipoVariabile\":\"BPSRECIDIVA\",\"valBoolean\":false",
						"\"tipoVariabile\":\"BPSYELLOWCARD\",\"valBoolean\":false",
						"\"tipoVariabile\":\"BPSIMPSANZ\",\"valNumber\":291.08"));
		mappaPassoOutput.put(TipologiaPassoTransizione.GREENING.name(),
				Arrays.asList("\"tipoVariabile\":\"GREESESEM\",\"valBoolean\":true",
						"\"tipoVariabile\":\"GRESUPBASE\",\"valNumber\":1.0000",
						"\"tipoVariabile\":\"GREIMPBASE\",\"valNumber\":45.15",
						"\"tipoVariabile\":\"GREIMPAMM\",\"valNumber\":45.15",
						"\"tipoVariabile\":\"GRESUPARB\",\"valNumber\":2.7654",
						"\"tipoVariabile\":\"GRESUPDET\",\"valNumber\":2.7654"));
		mappaPassoOutput.put(TipologiaPassoTransizione.RIEPILOGO_SANZIONI.name(),
				Arrays.asList("\"tipoVariabile\":\"SANZTOT\",\"valNumber\":291.08",
						"\"tipoVariabile\":\"IMPSANZNORISC\",\"valNumber\":155.25",
						"\"tipoVariabile\":\"BPSIMPCALCINT\",\"valNumber\":0.00",
						"\"tipoVariabile\":\"GREIMPCALCINT\",\"valNumber\":45.15",
						"\"tipoVariabile\":\"IMPSANZINTERINT\",\"valNumber\":200.40",
						"\"tipoVariabile\":\"BPSIMPCALC\",\"valNumber\":0.00",
						"\"tipoVariabile\":\"GREIMPCALC\",\"valNumber\":0.00"));

		Map<String, List<String>> mappaPassoEsitoControlli = new HashMap<>();
		mappaPassoEsitoControlli.put(TipologiaPassoTransizione.AMMISSIBILITA.name(),
				Arrays.asList("\"tipoControllo\":\"BRIDUSDC012_superficieMinima\",\"esito\":true",
						"\"tipoControllo\":\"BRIDUSDC009_infoAgricoltoreAttivo\",\"esito\":true",
						"\"tipoControllo\":\"BRIDUSDC010_agricoltoreAttivo\",\"esito\":true",
						"\"tipoControllo\":\"BRIDUSDC011_impegnoTitoli\",\"esito\":true"));
		mappaPassoEsitoControlli.put(TipologiaPassoTransizione.SANZIONI_BPS.name(),
				Arrays.asList("\"tipoControllo\":\"BRIDUSDC029_recidiva\",\"esito\":false",
						"\"tipoControllo\":\"BRIDUSDC109_isDomandaLiqAnnoPrec\",\"esito\":true",
						"\"tipoControllo\":\"BRIDUSDC030_sanzioniAnnoPrec\",\"esito\":false",
						"\"tipoControllo\":\"BRIDUSDC032_yellowCard\",\"esito\":false"));
		mappaPassoEsitoControlli.put(TipologiaPassoTransizione.GREENING.name(),
				Arrays.asList("\"tipoControllo\":\"BRIDUSDC023_infoGreening\",\"esito\":true",
						"\"tipoControllo\":\"BRIDUSDC025_impegniGreening\",\"esito\":true",
						"\"tipoControllo\":\"BRIDUSDC024_aziendaBiologica\",\"esito\":false"));
		mappaPassoEsitoControlli.put(TipologiaPassoTransizione.RIEPILOGO_SANZIONI.name(),
				Arrays.asList("\"tipoControllo\":\"BRIDUSDC031_sanzioniComminate\",\"esito\":false"));
		
		for (PassoTransizioneModel passo : passiLavSostegno) {
			assertEquals(mappaPassoEsito.get(passo.getCodicePasso()).getCodiceEsito(), passo.getCodiceEsito());

			List<String> esitiControlli = mappaPassoEsitoControlli.get(passo.getCodicePasso());
			if (esitiControlli != null && !esitiControlli.isEmpty()) {
				assertTrue(esitiControlli.stream().allMatch(esito -> passo.getDatiSintesiLavorazione().contains(esito)));
			}
			List<String> output = mappaPassoOutput.get(passo.getCodicePasso());
			if (output != null && !output.isEmpty()) {
				assertTrue(output.stream().allMatch(o -> passo.getDatiOutput().contains(o)));
			}
		}
	}
	
	private String getDomandaUnicaAgsExpandedPremiSostegnoBPS(String numeroDomanda) throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/calcoloDisaccoppiato/" + numeroDomanda + "_AGS_premiSostegnoBPS.json"));
		return objectMapper.writeValueAsString(response);
	}


	/**
	 * Test per verificare che, nel caso in cui venga sollevata una eccezione durante il calcolo disaccoppiato di una singola domanda, le modifiche apportate a quest'ultima vengano automaticamente
	 * rimosse L'eccezione in fase di calcolo è sollevata ritornando NULL nel recupero delle superfici eleggibili da AGS.
	 * 
	 * @throws Exception
	 */
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/dataMatriceCompatibilita.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "delete from a4gd_coltura_intervento", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testTransazionalitaCalcolo() throws Exception {
		String serviceAgs = "http://localhost:8080/ags/api/v1/domandeDU/".concat("183175")
				.concat("?expand=sostegniSuperficie,sostegniAllevamento,datiPascolo,dichiarazioni");
		DomandaUnica du = objectMapper.readValue(getDomandaUnicaAgsExpandedEccezione(), new TypeReference<DomandaUnica>() {
		});
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceAgs)), Mockito.eq(DomandaUnica.class))).thenReturn(du);

		DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(new BigDecimal(183175));
		IstruttoriaModel istruttoria =
				daoIstruttoria.findByDomandaUnicaModelAndSostegnoAndTipologia(d, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);
		serviceDomande.recuperaSostegniDomandaDU(d);

		BigDecimal transizioneSostegno = transizioneIstruttoriaDao.countTransizioneCalcoloDisaccoppiato(istruttoria);

		assertEquals(BigDecimal.ZERO, transizioneSostegno);

		String jsonRequest = "{ \"codFisc\":\"MNGDNE71D07L781V\", \"annoCamp\":2018}";
		String serviceUrlA4GPROXYrecuperoAgricoltoreSian = "http://localhost:8080/a4gproxy/api/v1/agricoltore?params=".concat(URLEncoder.encode(jsonRequest, "UTF-8"));
		AgricoltoreSIAN responseAgricoltoreSian = objectMapper.readValue(getResponseAgricoltoreSianEccezione(), AgricoltoreSIAN.class);
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlA4GPROXYrecuperoAgricoltoreSian)), Mockito.eq(AgricoltoreSIAN.class))).thenReturn(responseAgricoltoreSian);

		serviceAgs = "http://localhost:8080/ags/api/v1/domandeDU/".concat(d.getNumeroDomanda().toString()).concat("?expand=infoIstruttoria");
		du = objectMapper.readValue(getInfoIsturttoriaSanzione(), new TypeReference<DomandaUnica>() {
		});
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceAgs)), Mockito.eq(DomandaUnica.class))).thenReturn(null);

		String jsonRequestSigeco = "{\"anno\": 2018, \"numeroDomanda\": 183175}";
		String serviceUrlEsitoSigeco = "http://localhost:8080/a4gproxy/api/v1/sigeco?params=".concat(URLEncoder.encode(jsonRequestSigeco, "UTF-8"));
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlEsitoSigeco)), Mockito.eq(Long.class))).thenReturn(null);

		try {
			calcoloDisaccoppiatoManager.elabora(istruttoria.getId());
		} catch (CalcoloSostegnoException e) {
			logger.error("Eccezione nell'esecuzione del calcolo disaccoppiato");
		}

		// 1. Verifico che la domanda sia rimasta nello stato RICHIESTO per il sostegno DISACCOPPIATO
		IstruttoriaModel lavSostegno = daoIstruttoria.findByDomandaUnicaModelAndSostegno(d, Sostegno.DISACCOPPIATO);
		assertEquals(StatoIstruttoria.RICHIESTO.getStatoIstruttoria(), lavSostegno.getA4gdStatoLavSostegno().getIdentificativo());

		// 2. Verifico che per la domanda non siano presenti transazioni di Calcolo per il sostegno DISACCOPPIATO
		assertEquals(BigDecimal.ZERO, transizioneSostegno);

	}

	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/dataMatriceCompatibilita.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "delete from a4gd_coltura_intervento", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testEsclusioneParcelleInf200() throws Exception {
		String serviceAgs = "http://localhost:8080/ags/api/v1/domandeDU/".concat("183109")
				.concat("?expand=sostegniSuperficie,sostegniAllevamento,datiPascolo,dichiarazioni");
		DomandaUnica du = objectMapper.readValue(getDomandaUnicaAgsExpanded_ParcellaInf200(), new TypeReference<DomandaUnica>() {
		});
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceAgs)), Mockito.eq(DomandaUnica.class))).thenReturn(du);

		DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(new BigDecimal(183109));
		IstruttoriaModel istruttoria = 
				daoIstruttoria.findByDomandaUnicaModelAndSostegnoAndTipologia(d, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);
		serviceDomande.recuperaSostegniDomandaDU(d);

		String jsonRequest = "{ \"codFisc\":\"PLOLNZ71B07E565W\", \"annoCamp\":2018}";
		String serviceUrlA4GPROXYrecuperoAgricoltoreSian = "http://localhost:8080/a4gproxy/api/v1/agricoltore?params=".concat(URLEncoder.encode(jsonRequest, "UTF-8"));
		AgricoltoreSIAN responseAgricoltoreSian = objectMapper.readValue(getResponseAgricoltoreSian(), AgricoltoreSIAN.class);
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlA4GPROXYrecuperoAgricoltoreSian)), Mockito.eq(AgricoltoreSIAN.class))).thenReturn(responseAgricoltoreSian);

		serviceAgs = "http://localhost:8080/ags/api/v1/domandeDU/".concat(d.getNumeroDomanda().toString()).concat("?expand=infoIstruttoria");
		du = objectMapper.readValue(getInfoIsturttoria(), new TypeReference<DomandaUnica>() {
		});
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceAgs)), Mockito.eq(DomandaUnica.class))).thenReturn(du);;

		String jsonRequestSigeco = "{\"anno\": 2018, \"numeroDomanda\": 183109}";
		String serviceUrlEsitoSigeco = "http://localhost:8080/a4gproxy/api/v1/sigeco?params=".concat(URLEncoder.encode(jsonRequestSigeco, "UTF-8"));
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlEsitoSigeco)), Mockito.eq(Long.class))).thenReturn(null);

		String serviceUrlAnomalieCoordinamento = "http://localhost:8080/a4gproxy/api/v1/anomaliecoordinamento";
		Mockito.when(restTemplate.getForObject(Mockito.eq(serviceUrlAnomalieCoordinamento.concat("/{idParcella}?annoCampagna={annoCampagna}")), Mockito.eq(Integer.class), Mockito.anyLong(),
				Mockito.anyLong())).thenReturn(0);

		String params = URLEncoder.encode("{\"codicePac\":\"PAC1420\",\"tipoDomanda\":\"DU\",\"anno\":2017,\"cuaa\":\"PLOLNZ71B07E565W\"}", "UTF-8");
		String serviceCheckPresenzaDomandeFiltered = "http://localhost:8080/ags/api/v1/domandeDU/checkPresenzaDomandeFiltered?params=".concat(params);
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceCheckPresenzaDomandeFiltered)), Mockito.eq(Boolean.class))).thenReturn(false);

		calcoloDisaccoppiatoManager.elabora(istruttoria.getId());

		// 1. Verifico la presenza di una transizione di tipo Calcolo disaccoppiato per la domanda
		A4gdStatoLavSostegno statoLavSostegno = daoStatoLavorazioneSostegno.findByIdentificativo(StatoIstruttoria.CONTROLLI_CALCOLO_OK.getStatoIstruttoria());
		Optional<TransizioneIstruttoriaModel> transizioneSostegno = transizioneIstruttoriaDao.findTransizioneCalcoloPremio(istruttoria).stream().findAny();

		if (transizioneSostegno.isPresent()) {
			assertEquals(transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo(), statoLavSostegno.getIdentificativo());
		} else {
			assertFalse(true);
		}

		// 2. Verifico il numero di passi di lavorazione eseguiti
		List<PassoTransizioneModel> passiLavSostegno = daoPassiLavSostegno.findByTransizioneIstruttoria(transizioneSostegno.get());

		Optional<PassoTransizioneModel> passoRiduzioniBps = passiLavSostegno.stream().filter(p -> p.getCodicePasso().equals(TipologiaPassoTransizione.RIDUZIONI_BPS)).findFirst();
		if (passoRiduzioniBps.isPresent()) {
			DatiInput input = objectMapper.readValue(passoRiduzioniBps.get().getDatiInput(), DatiInput.class);
			Optional<VariabileCalcolo> pfsupimp = input.getVariabiliCalcolo().stream().filter(v -> v.getTipoVariabile().equals(TipoVariabile.PFSUPIMP)).findFirst();
			if (pfsupimp.isPresent()) {
				assertFalse(pfsupimp.get().getValList().stream().filter(p -> p.getParticella().getIdParticella().equals(new Long(1461466))).findAny().isPresent());
			} else {
				assertTrue(false);
			}

		} else {
			assertTrue(false);
		}
		assertEquals(7, passiLavSostegno.size());
	}

	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/dataMatriceCompatibilita.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "delete from a4gd_coltura_intervento", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Ignore //Betty 09/06/2020: Eliminato controllo anomalie di coordinamento 
	public void testCalcoloDisaccoppiatoKoConAnomaliaCoordinamento() throws Exception {
		String serviceAgs = "http://localhost:8080/ags/api/v1/domandeDU/".concat("183109")
				.concat("?expand=sostegniSuperficie,sostegniAllevamento,datiPascolo,dichiarazioni");
		DomandaUnica du = objectMapper.readValue(getDomandaUnicaAgsExpanded(), new TypeReference<DomandaUnica>() {
		});
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceAgs)), Mockito.eq(DomandaUnica.class))).thenReturn(du);

		DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(new BigDecimal(183109));
		IstruttoriaModel istruttoria = 
				daoIstruttoria.findByDomandaUnicaModelAndSostegnoAndTipologia(d, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);
		serviceDomande.recuperaSostegniDomandaDU(d);

		String jsonRequest = "{ \"codFisc\":\"PLOLNZ71B07E565W\", \"annoCamp\":2018}";
		String serviceUrlA4GPROXYrecuperoAgricoltoreSian = "http://localhost:8080/a4gproxy/api/v1/agricoltore?params=".concat(URLEncoder.encode(jsonRequest, "UTF-8"));
		AgricoltoreSIAN responseAgricoltoreSian = objectMapper.readValue(getResponseAgricoltoreSian(), AgricoltoreSIAN.class);
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlA4GPROXYrecuperoAgricoltoreSian)), Mockito.eq(AgricoltoreSIAN.class))).thenReturn(responseAgricoltoreSian);

		serviceAgs = "http://localhost:8080/ags/api/v1/domandeDU/".concat(d.getNumeroDomanda().toString()).concat("?expand=infoIstruttoria");
		du = objectMapper.readValue(getInfoIsturttoria(), new TypeReference<DomandaUnica>() {
		});
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceAgs)), Mockito.eq(DomandaUnica.class))).thenReturn(du);

		String jsonRequestSigeco = "{\"anno\": 2018, \"numeroDomanda\": 183109}";
		String serviceUrlEsitoSigeco = "http://localhost:8080/a4gproxy/api/v1/sigeco?params=".concat(URLEncoder.encode(jsonRequestSigeco, "UTF-8"));
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlEsitoSigeco)), Mockito.eq(Long.class))).thenReturn(null);

		String serviceUrlAnomalieCoordinamento = "http://localhost:8080/a4gproxy/api/v1/anomaliecoordinamento";
		Mockito.when(restTemplate.getForObject(Mockito.eq(serviceUrlAnomalieCoordinamento.concat("/{idParcella}?annoCampagna={annoCampagna}")), Mockito.eq(Integer.class), Mockito.anyLong(),
				Mockito.anyLong())).thenReturn(1);

		String params = URLEncoder.encode("{\"codicePac\":\"PAC1420\",\"tipoDomanda\":\"DU\",\"anno\":2017,\"cuaa\":\"PLOLNZ71B07E565W\"}", "UTF-8");
		String serviceCheckPresenzaDomandeFiltered = "http://localhost:8080/ags/api/v1/domandeDU/checkPresenzaDomandeFiltered?params=".concat(params);
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceCheckPresenzaDomandeFiltered)), Mockito.eq(Boolean.class))).thenReturn(false);

		calcoloDisaccoppiatoManager.elabora(istruttoria.getId());

		// 1. Verifico la presenza di una transizione di tipo Calcolo disaccoppiato per la domanda
		A4gdStatoLavSostegno statoLavSostegno = daoStatoLavorazioneSostegno.findByIdentificativo(StatoIstruttoria.CONTROLLI_CALCOLO_KO.getStatoIstruttoria());
		Optional<TransizioneIstruttoriaModel> transizioneSostegno = transizioneIstruttoriaDao.findTransizioneCalcoloPremio(istruttoria).stream().findAny();

		if (transizioneSostegno.isPresent()) {
			assertEquals(statoLavSostegno.getIdentificativo(), transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo());
		} else {
			assertFalse(true);
		}

		// 2. Verifico il numero di passi di lavorazione eseguiti
		List<PassoTransizioneModel> passiLavSostegno = daoPassiLavSostegno.findByTransizioneIstruttoria(transizioneSostegno.get());
		Optional<PassoTransizioneModel> optPassoRiduzioni = passiLavSostegno.stream().filter(p -> p.getCodicePasso().equals(TipologiaPassoTransizione.RIDUZIONI_BPS)).findFirst();

		assertTrue(optPassoRiduzioni.isPresent());

		DatiInput datiInputRiduzioni = objectMapper.readValue(optPassoRiduzioni.get().getDatiInput(), DatiInput.class);

		Optional<VariabileCalcolo> optVarInpuCoord = datiInputRiduzioni.getVariabiliCalcolo().stream().filter(v -> v.getTipoVariabile().equals(TipoVariabile.BPSSUPSCOSTCOO)).findFirst();
		Optional<VariabileCalcolo> optVarInputEle = datiInputRiduzioni.getVariabiliCalcolo().stream().filter(v -> v.getTipoVariabile().equals(TipoVariabile.BPSSUPELE)).findFirst();

		assertTrue(optVarInpuCoord.isPresent());
		assertTrue(optVarInputEle.isPresent());

		// verifico che la sup. in scostamento per le anomalie di coordinamento sia uguale alla sup. eleggibile -> tutte le particelle hanno anomalie di coordinamento
		assertTrue(optVarInpuCoord.get().getValNumber().compareTo(optVarInputEle.get().getValNumber()) == 0);

		assertEquals(7, passiLavSostegno.size());
	}

	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/dataMatriceCompatibilita.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "delete from a4gd_coltura_intervento", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void testSupEleggibiliParziali() throws Exception {

		String serviceUrlAgs = "http://localhost:8080/ags/api/v1/domandeDU/".concat("183109")
				.concat("?expand=sostegniSuperficie,sostegniAllevamento,datiPascolo,dichiarazioni");
		DomandaUnica du = objectMapper.readValue(getDomandaUnicaAgsExpandedRidotto(), new TypeReference<DomandaUnica>() {
		});
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlAgs)), Mockito.eq(DomandaUnica.class))).thenReturn(du);

		DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(new BigDecimal(183109));
		IstruttoriaModel istruttoria = 
				daoIstruttoria.findByDomandaUnicaModelAndSostegnoAndTipologia(d, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);
		serviceDomande.recuperaSostegniDomandaDU(d);

		String jsonRequest = "{ \"codFisc\":\"PLOLNZ71B07E565W\", \"annoCamp\":2018}";
		String serviceUrlA4GPROXYrecuperoAgricoltoreSian = "http://localhost:8080/a4gproxy/api/v1/agricoltore?params=".concat(URLEncoder.encode(jsonRequest, "UTF-8"));
		AgricoltoreSIAN responseAgricoltoreSian = objectMapper.readValue(getResponseAgricoltoreSian(), AgricoltoreSIAN.class);
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlA4GPROXYrecuperoAgricoltoreSian)), Mockito.eq(AgricoltoreSIAN.class))).thenReturn(responseAgricoltoreSian);

		// test AGS Eleggibili
		String serviceUrlAgsEleggibili = "http://localhost:8080/ags/api/v1/domandeDU/".concat(d.getNumeroDomanda().toString()).concat("?expand=infoIstruttoria");
		DomandaUnica listDomandeAgsEleggibili = objectMapper.readValue(getInfoIsturttoriaParziali(), new TypeReference<DomandaUnica>() {
		});
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlAgsEleggibili)), Mockito.eq(DomandaUnica.class))).thenReturn(listDomandeAgsEleggibili);

		String jsonRequestSigeco = "{\"anno\": 2018, \"numeroDomanda\": 183109}";
		String serviceUrlEsitoSigeco = "http://localhost:8080/a4gproxy/api/v1/sigeco?params=".concat(URLEncoder.encode(jsonRequestSigeco, "UTF-8"));
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlEsitoSigeco)), Mockito.eq(Long.class))).thenReturn(null);

		String serviceUrlAnomalieCoordinamento = "http://localhost:8080/a4gproxy/api/v1/anomaliecoordinamento";
		Mockito.when(restTemplate.getForObject(Mockito.eq(serviceUrlAnomalieCoordinamento.concat("/{idParcella}?annoCampagna={annoCampagna}")), Mockito.eq(Integer.class), Mockito.anyLong(),
				Mockito.anyLong())).thenReturn(0);
		String params = URLEncoder.encode("{\"codicePac\":\"PAC1420\",\"tipoDomanda\":\"DU\",\"anno\":2017,\"cuaa\":\"PLOLNZ71B07E565W\"}", "UTF-8");

		String serviceCheckPresenzaDomandeFiltered = "http://localhost:8080/ags/api/v1/domandeDU/checkPresenzaDomandeFiltered?params=".concat(params);
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceCheckPresenzaDomandeFiltered)), Mockito.eq(Boolean.class))).thenReturn(false);

		calcoloDisaccoppiatoManager.elabora(istruttoria.getId());

		// 1. Verifico la presenza di una transizione di tipo Calcolo disaccoppiato per la domanda
		A4gdStatoLavSostegno statoLavSostegno = daoStatoLavorazioneSostegno.findByIdentificativo(StatoIstruttoria.CONTROLLI_CALCOLO_OK.getStatoIstruttoria());
		Optional<TransizioneIstruttoriaModel> transizioneSostegno = transizioneIstruttoriaDao.findTransizioneCalcoloPremio(istruttoria).stream().findAny();

		if (transizioneSostegno.isPresent()) {
			assertEquals(transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo(), statoLavSostegno.getIdentificativo());
		} else {
			assertFalse(true);
		}

		// 2. Verifico il numero di passi di lavorazione eseguiti
		List<PassoTransizioneModel> passiLavSostegno = daoPassiLavSostegno.findByTransizioneIstruttoria(transizioneSostegno.get());
		assertEquals(8, passiLavSostegno.size());
	}


	private String getDomandaUnicaAgsExpanded() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/calcoloDisaccoppiato/183109_AgsExpanded.json"));
		return objectMapper.writeValueAsString(response);
	}

	private String getDomandaUnicaAgsExpandedRidotto() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/calcoloDisaccoppiato/183109_AgsExpanded_2.json"));
		return objectMapper.writeValueAsString(response);
	}

	private String getResponseAgricoltoreSian() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/calcoloDisaccoppiato/2_InfoAgricoltoreSIAN.json"));
		return objectMapper.writeValueAsString(response);
	}

	private String getInfoIsturttoria() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/calcoloDisaccoppiato/183109_AgsExpandInfoIstruttoria.json"));
		return objectMapper.writeValueAsString(response);
	}

	/**
	 * Ritorna un mock della response del servizio di recupero delle sup. eleggibili da AGS, dalla quale è stata rimossa la particella con id 1461849 e coltura 360-441-000
	 * 
	 * @return
	 * @throws IOException
	 */
	private String getInfoIsturttoriaParziali() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/calcoloDisaccoppiato/183109_AgsExpandInfoIstruttoriaSupEleParziali.json"));
		return objectMapper.writeValueAsString(response);
	}

	private String getDomandaUnicaAgsExpandedSanzione() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/calcoloDisaccoppiato/183430_AgsExpanded.json"));
		return objectMapper.writeValueAsString(response);
	}

	private String getResponseAgricoltoreSianSanzione() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/calcoloDisaccoppiato/183430_InfoAgricoltoreSIAN.json"));
		return objectMapper.writeValueAsString(response);
	}

	private String getInfoIsturttoriaSanzione() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/calcoloDisaccoppiato/183430_AgsExpandInfoIstruttoria.json"));
		return objectMapper.writeValueAsString(response);
	}

	private String getDomandaUnicaAgsExpandedEccezione() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/calcoloDisaccoppiato/183430_AgsExpanded.json"));
		return objectMapper.writeValueAsString(response);
	}

	private String getResponseAgricoltoreSianEccezione() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/calcoloDisaccoppiato/183430_InfoAgricoltoreSIAN.json"));
		return objectMapper.writeValueAsString(response);
	}

	private String getInfoIstruttoriaNoSupEleggibili() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/calcoloDisaccoppiato/183109_AgsExpandNoSupEleggibili.json"));
		return objectMapper.writeValueAsString(response);
	}

	private String getDomandaUnicaAgsExpanded_ParcellaInf200() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/calcoloDisaccoppiato/183109_AgsExpanded_ParcellaInf200.json"));
		return objectMapper.writeValueAsString(response);
	}

}
