package it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.A4gistruttoriaConfigurazione;
import it.tndigitale.a4gistruttoria.dto.Particella;
import it.tndigitale.a4gistruttoria.dto.lavorazione.ConsistenzaAllevamentoDto;
import it.tndigitale.a4gistruttoria.dto.lavorazione.ConsistenzaPascolo2015Dto;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiElaborazioneIstruttoria;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiIstruttoriaPascoli;
import it.tndigitale.a4gistruttoria.dto.lavorazione.EsitoControllo;
import it.tndigitale.a4gistruttoria.dto.lavorazione.MovimentazionePascoloOviniDto;
import it.tndigitale.a4gistruttoria.dto.lavorazione.ParticellaColtura;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
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
import it.tndigitale.a4gistruttoria.service.DatiIstruttoreService;
import it.tndigitale.a4gistruttoria.service.ElencoPascoliServiceImpl;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.visualizza", "a4gistruttoria.pac.domandaUnica.edita" })
public class PassoCalcoloAnomalieMantenimentoServiceTest {

	@SuppressWarnings("unused")
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private RestTemplate restTemplate;

	@Autowired
	private ElencoPascoliServiceImpl servicePascoli;

	@Autowired
	private PassoCalcoloAnomalieMantenimentoService serviceCalcoloMan;

	@Autowired
	private TransizioneIstruttoriaDao transizioneIstruttoriaDao;

	@Autowired
	private StatoLavSostegnoDao daoStatoLavSostegno;

	@Autowired
	private DomandaUnicaDao daoDomanda;

	@Autowired
	A4gistruttoriaConfigurazione configurazione;
	
	@Autowired
	IstruttoriaDao daoIstruttoria;
	@Autowired
	private DatiIstruttoreService datiIstruttoreService;

	private final String ESITO_POSITIVO = "POSITIVO";
	private final String ESITO_NEGATIVO = "NEGATIVO";
	private final String DOC_PRESENTE = "PRESENTE";

	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/comuniLimitrofi.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	public void testAggregazioneComuni() throws Exception {

		Map<String, List<ParticellaColtura>> mapComuneParticelle = new HashMap<String, List<ParticellaColtura>>();
		List<ParticellaColtura> listAla = new ArrayList<>();
		List<ParticellaColtura> listAvio = new ArrayList<>();
		List<ParticellaColtura> listMori = new ArrayList<>();
		List<ParticellaColtura> listLavis = new ArrayList<>();
		List<ParticellaColtura> listTrento = new ArrayList<>();
		// ALA
		ParticellaColtura p1 = new ParticellaColtura();
		p1.setParticella(new Particella("1", "ALA", "A116", "9999", "1", "b"));
		listAla.add(p1);
		// AVIO
		ParticellaColtura p2 = new ParticellaColtura();
		p2.setParticella(new Particella("1", "AVIO", "A520", "9999", "1", "b"));
		ParticellaColtura p3 = new ParticellaColtura();
		p3.setParticella(new Particella("2", "AVIO", "A520", "9999", "2", "b"));
		listAvio.add(p2);
		listAvio.add(p3);
		// MORI
		ParticellaColtura p4 = new ParticellaColtura();
		p4.setParticella(new Particella("1", "MORI", "F728", "9999", "1", "b"));
		ParticellaColtura p5 = new ParticellaColtura();
		p5.setParticella(new Particella("2", "MORI", "F728", "9999", "2", "b"));
		listMori.add(p4);
		listMori.add(p5);
		// LAVIS
		ParticellaColtura p6 = new ParticellaColtura();
		p6.setParticella(new Particella("1", "LAVIS", "E500", "9999", "1", "b"));
		ParticellaColtura p7 = new ParticellaColtura();
		p7.setParticella(new Particella("2", "LAVIS", "E500", "9999", "1", "b"));
		listLavis.add(p6);
		listLavis.add(p7);
		// TRENTO
		ParticellaColtura p8 = new ParticellaColtura();
		p8.setParticella(new Particella("1", "TRENTO", "L378", "9999", "1", "b"));
		listTrento.add(p8);

		mapComuneParticelle.put("A116", listAla);
		mapComuneParticelle.put("A520", listAvio);
		mapComuneParticelle.put("F728", listMori);
		mapComuneParticelle.put("E500", listLavis);
		mapComuneParticelle.put("L378", listTrento);

		Set<String> comuniElaborati = new HashSet<>();

		Map<Integer, List<ParticellaColtura>> mapComuniAmministrativi = new HashMap<>();

		AtomicInteger counter = new AtomicInteger(0);
		mapComuneParticelle.entrySet().stream().forEach(c -> {
			if (!comuniElaborati.contains(c.getKey())) {
				List<ParticellaColtura> listParticelle = servicePascoli.getListaParticelleComuneAmministrativo(c.getKey(), comuniElaborati, mapComuneParticelle);
				mapComuniAmministrativi.put(counter.incrementAndGet(), listParticelle);
			}

		});

		assertEquals(mapComuniAmministrativi.keySet().size(), 2);

	}

	/**
	 * Metodo privato per l'inizializzazione delle variabili di input del calcolo man
	 * 
	 * @return
	 */
	private List<VariabileCalcolo> initInputMan(List<String> descPascoli) {
		List<VariabileCalcolo> input = new ArrayList<>();

		ArrayList<ParticellaColtura> supEleggibili = new ArrayList<>();

		supEleggibili.add(
				getParticellaColtura("{\"idParticella\":2288822,\"comune\":\"SELLA GIUDICARIE - LARDARO I (TN)\",\"codNazionale\":\"P464\",\"foglio\":9999,\"particella\":\"00188\",\"sub\":\"2\"}",
						"560-461-009", "1311", Double.valueOf(3500), "P464", "PASCOLAMENTO CON ANIMALI PROPRI"));
		supEleggibili.add(
				getParticellaColtura("{\"idParticella\":2288814,\"comune\":\"SELLA GIUDICARIE - LARDARO I (TN)\",\"codNazionale\":\"P464\",\"foglio\":9999,\"particella\":\"00186\",\"sub\":\"2\"}",
						"560-461-009", "1311", Double.valueOf(3500), "P464", "PASCOLAMENTO CON ANIMALI PROPRI"));

		input.add(new VariabileCalcolo(TipoVariabile.PFSUPELE, supEleggibili));
		input.add(new VariabileCalcolo(TipoVariabile.PFSUPSIGECO, supEleggibili));

		HashMap<String, List<ParticellaColtura>> mapPartElePas = new HashMap<>();

		descPascoli.forEach(p -> {
			mapPartElePas.put(p, supEleggibili);
		});

		input.add(new VariabileCalcolo(TipoVariabile.PASSUPSIGEMAP, mapPartElePas));
		input.add(new VariabileCalcolo(TipoVariabile.PASSUPELEMAP, mapPartElePas));
		input.add(new VariabileCalcolo(TipoVariabile.ISCAMP, false));

		return input;
	}

	private ParticellaColtura getParticellaColtura(String infoCatastali, String codiceColtura3, String livello, double superficie, String valString, String descMantenimento) {
		ParticellaColtura pc = new ParticellaColtura();
		try {
			pc.setParticella(objectMapper.readValue(infoCatastali, Particella.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
		pc.setColtura(codiceColtura3);
		double d = superficie;
		pc.setValNum((float) d);
		pc.setLivello(livello);
		pc.setValBool(false);
		pc.setValString(valString);
		pc.setDescMantenimento(descMantenimento);
		return pc;
	}

	@Test
	@Transactional
	public void dut_002_noPascoli() throws Exception {


		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(180375));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = daoIstruttoria.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizione = transizioneIstruttoriaDao.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();

		List<String> pascoliRichiesti = new ArrayList<>();// Arrays.asList("AZIENDALE - P464", "158TN050");
		List<VariabileCalcolo> variabiliInput = initInputMan(pascoliRichiesti);

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = serviceCalcoloMan.eseguiPasso(dati);

		assertEquals("DUT_002", res.getCodiceEsito());

	}

	// TEST PASCOLI MALGA FUORI PROVINCIA

	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/calcoloDisaccoppiato/insertDatiTestMan.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	public void duf_018_pasf10() throws Exception {

		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(182864));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = daoIstruttoria.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizione = transizioneIstruttoriaDao.save(transizione);

		List<EsitoControllo> esitiInput = new ArrayList<>();

		List<String> pascoliRichiesti = Arrays.asList("162BZ050");
		List<VariabileCalcolo> variabiliInput = initInputMan(pascoliRichiesti);

		// getConsistenzaPascoloDaCacheBdn
		String paramsCons = "{ \"annoCampagna\": 2018 , \"codPascolo\": \"162BZ050\" }";
		String encodedParamsCons = URLEncoder.encode(paramsCons, "UTF-8");
		String resourceCons = configurazione.getUriCacheBdn().concat("consistenzaPascolo?params=").concat(encodedParamsCons);
		List<ConsistenzaPascolo2015Dto> consistenzaPascolo = new ArrayList<ConsistenzaPascolo2015Dto>();
		ResponseEntity<List<ConsistenzaPascolo2015Dto>> responseCons = new ResponseEntity<List<ConsistenzaPascolo2015Dto>>(consistenzaPascolo, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq(new URI(resourceCons)), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(new ParameterizedTypeReference<List<ConsistenzaPascolo2015Dto>>() {
		}))).thenReturn(responseCons);
		List<MovimentazionePascoloOviniDto> movimenti = new ArrayList<MovimentazionePascoloOviniDto>();
		paramsCons = "{ \"annoCampagna\": 2018 , \"codPascolo\": \"162BZ050\"}";
		encodedParamsCons = URLEncoder.encode(paramsCons, "UTF-8");
		resourceCons = configurazione.getUriCacheBdn().concat("movimentiPascolo?params=").concat(encodedParamsCons);
		ResponseEntity<List<MovimentazionePascoloOviniDto>> responseMov = new ResponseEntity<List<MovimentazionePascoloOviniDto>>(movimenti, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq(new URI(resourceCons)), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(new ParameterizedTypeReference<List<MovimentazionePascoloOviniDto>>() {
		}))).thenReturn(responseMov);
		
		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = serviceCalcoloMan.eseguiPasso(dati);

		assertEquals("DUF_018", res.getCodiceEsito());

	}

	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/calcoloDisaccoppiato/insertDatiTestMan.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	public void dut_003_pasf_08() throws Exception {

		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(182864));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = daoIstruttoria.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizione = transizioneIstruttoriaDao.save(transizione);

		List<EsitoControllo> esitiInput = new ArrayList<>();

		List<String> pascoliRichiesti = Arrays.asList("162BZ050");
		List<VariabileCalcolo> variabiliInput = initInputMan(pascoliRichiesti);

		// getConsistenzaPascoloDaCacheBdn
		String paramsCons = "{ \"annoCampagna\": 2018 , \"codPascolo\": \"162BZ050\" }";
		String encodedParamsCons = URLEncoder.encode(paramsCons, "UTF-8");
		String resourceCons = configurazione.getUriCacheBdn().concat("consistenzaPascolo?params=").concat(encodedParamsCons);
		List<ConsistenzaPascolo2015Dto> consistenzaPascolo = new ArrayList<ConsistenzaPascolo2015Dto>();
		ResponseEntity<List<ConsistenzaPascolo2015Dto>> responseCons = new ResponseEntity<List<ConsistenzaPascolo2015Dto>>(consistenzaPascolo, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq(new URI(resourceCons)), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(new ParameterizedTypeReference<List<ConsistenzaPascolo2015Dto>>() {
		}))).thenReturn(responseCons);
		List<MovimentazionePascoloOviniDto> movimenti = new ArrayList<MovimentazionePascoloOviniDto>();
		paramsCons = "{ \"annoCampagna\": 2018 , \"codPascolo\": \"162BZ050\"}";
		encodedParamsCons = URLEncoder.encode(paramsCons, "UTF-8");
		resourceCons = configurazione.getUriCacheBdn().concat("movimentiPascolo?params=").concat(encodedParamsCons);
		ResponseEntity<List<MovimentazionePascoloOviniDto>> responseMov = new ResponseEntity<List<MovimentazionePascoloOviniDto>>(movimenti, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq(new URI(resourceCons)), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(new ParameterizedTypeReference<List<MovimentazionePascoloOviniDto>>() {
		}))).thenReturn(responseMov);

		// inserimento dati istruttore
		List<DatiIstruttoriaPascoli> datiIstruttoria = new ArrayList<>();
		DatiIstruttoriaPascoli datiIstruttoriaPascolo = new DatiIstruttoriaPascoli();
		datiIstruttoriaPascolo.setDescrizionePascolo("162BZ050");
		datiIstruttoriaPascolo.setEsitoControlloMantenimento(ESITO_POSITIVO);
		datiIstruttoria.add(datiIstruttoriaPascolo);
		//serviceDomande.salvaDatiIstruttoriaDomanda(transizione.getIstruttoria().getDomandaUnica().getId(), objectMapper.writeValueAsString(datiIstruttoria), TipoDatoIstruttoria.PASCOLI);
		datiIstruttoreService.saveOrUpdateDatiIstruttoriaPascoli(transizione.getIstruttoria().getId(), datiIstruttoria);
		
		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = serviceCalcoloMan.eseguiPasso(dati);

		assertEquals("DUT_003", res.getCodiceEsito());

	}

	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/calcoloDisaccoppiato/insertDatiTestMan.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	public void dut_004_pasf_09() throws Exception {

		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(182864));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = daoIstruttoria.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizione = transizioneIstruttoriaDao.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();

		List<String> pascoliRichiesti = Arrays.asList("162BZ050");
		List<VariabileCalcolo> variabiliInput = initInputMan(pascoliRichiesti);

		// getConsistenzaPascoloDaCacheBdn
		String paramsCons = "{ \"annoCampagna\": 2018 , \"codPascolo\": \"162BZ050\" }";
		String encodedParamsCons = URLEncoder.encode(paramsCons, "UTF-8");
		String resourceCons = configurazione.getUriCacheBdn().concat("consistenzaPascolo?params=").concat(encodedParamsCons);
		List<ConsistenzaPascolo2015Dto> consistenzaPascolo = new ArrayList<ConsistenzaPascolo2015Dto>();
		ResponseEntity<List<ConsistenzaPascolo2015Dto>> responseCons = new ResponseEntity<List<ConsistenzaPascolo2015Dto>>(consistenzaPascolo, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq(new URI(resourceCons)), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(new ParameterizedTypeReference<List<ConsistenzaPascolo2015Dto>>() {
		}))).thenReturn(responseCons);
		List<MovimentazionePascoloOviniDto> movimenti = new ArrayList<MovimentazionePascoloOviniDto>();
		paramsCons = "{ \"annoCampagna\": 2018 , \"codPascolo\": \"162BZ050\"}";
		encodedParamsCons = URLEncoder.encode(paramsCons, "UTF-8");
		resourceCons = configurazione.getUriCacheBdn().concat("movimentiPascolo?params=").concat(encodedParamsCons);
		ResponseEntity<List<MovimentazionePascoloOviniDto>> responseMov = new ResponseEntity<List<MovimentazionePascoloOviniDto>>(movimenti, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq(new URI(resourceCons)), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(new ParameterizedTypeReference<List<MovimentazionePascoloOviniDto>>() {
		}))).thenReturn(responseMov);
		List<ConsistenzaAllevamentoDto> consAlle = new ArrayList<ConsistenzaAllevamentoDto>();
		paramsCons = "{ \"annoCampagna\": 2018 , \"codiceFiscale\": \"" + domanda.getCuaaIntestatario() + "\" }";
		encodedParamsCons = URLEncoder.encode(paramsCons, "UTF-8");
		resourceCons = configurazione.getUriCacheBdn().concat("consistenzaAllevamento?params=").concat(encodedParamsCons);
		ResponseEntity<List<ConsistenzaAllevamentoDto>> responseConsAlle = new ResponseEntity<List<ConsistenzaAllevamentoDto>>(consAlle, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq(new URI(resourceCons)), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(new ParameterizedTypeReference<List<ConsistenzaAllevamentoDto>>() {
		}))).thenReturn(responseConsAlle);
		
		// inserimento dati istruttore
		List<DatiIstruttoriaPascoli> datiIstruttoria = new ArrayList<>();
		DatiIstruttoriaPascoli datiIstruttoriaPascolo = new DatiIstruttoriaPascoli();
		datiIstruttoriaPascolo.setDescrizionePascolo("162BZ050");
		datiIstruttoriaPascolo.setEsitoControlloMantenimento(ESITO_NEGATIVO);
		datiIstruttoria.add(datiIstruttoriaPascolo);
		//serviceDomande.salvaDatiIstruttoriaDomanda(transizione.getIstruttoria().getDomandaUnica().getId(), objectMapper.writeValueAsString(datiIstruttoria), TipoDatoIstruttoria.PASCOLI);
		datiIstruttoreService.saveOrUpdateDatiIstruttoriaPascoli(transizione.getIstruttoria().getId(), datiIstruttoria);

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = serviceCalcoloMan.eseguiPasso(dati);

		assertEquals("DUT_004", res.getCodiceEsito());

	}

	// TEST PASCOLI MALGA PROVINCIA DI TRENTO

	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/calcoloDisaccoppiato/insertDatiTestMan.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	public void duf_018_pasf_07() throws Exception {

		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(180513));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = daoIstruttoria.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizione = transizioneIstruttoriaDao.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();

		List<String> pascoliRichiesti = Arrays.asList("158TN050");
		List<VariabileCalcolo> variabiliInput = initInputMan(pascoliRichiesti);
		
		// getConsistenzaPascoloDaCacheBdn
		String paramsCons = "{ \"annoCampagna\": 2018 , \"codPascolo\": \"158TN050\" }";
		String encodedParamsCons = URLEncoder.encode(paramsCons, "UTF-8");
		String resourceCons = configurazione.getUriCacheBdn().concat("consistenzaPascolo?params=").concat(encodedParamsCons);
		List<ConsistenzaPascolo2015Dto> consistenzaPascolo = new ArrayList<ConsistenzaPascolo2015Dto>();
		ResponseEntity<List<ConsistenzaPascolo2015Dto>> responseCons = new ResponseEntity<List<ConsistenzaPascolo2015Dto>>(consistenzaPascolo, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq(new URI(resourceCons)), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(new ParameterizedTypeReference<List<ConsistenzaPascolo2015Dto>>() {
		}))).thenReturn(responseCons);
		List<MovimentazionePascoloOviniDto> movimenti = new ArrayList<MovimentazionePascoloOviniDto>();
		paramsCons = "{ \"annoCampagna\": 2018 , \"codPascolo\": \"158TN050\"}";
		encodedParamsCons = URLEncoder.encode(paramsCons, "UTF-8");
		resourceCons = configurazione.getUriCacheBdn().concat("movimentiPascolo?params=").concat(encodedParamsCons);
		ResponseEntity<List<MovimentazionePascoloOviniDto>> responseMov = new ResponseEntity<List<MovimentazionePascoloOviniDto>>(movimenti, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq(new URI(resourceCons)), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(new ParameterizedTypeReference<List<MovimentazionePascoloOviniDto>>() {
		}))).thenReturn(responseMov);

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = serviceCalcoloMan.eseguiPasso(dati);

		assertEquals("DUF_018", res.getCodiceEsito());

	}

	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/calcoloDisaccoppiato/insertDatiTestMan.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	public void dut_004_pasf_06() throws Exception {

		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(180513));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = daoIstruttoria.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizione = transizioneIstruttoriaDao.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();

		List<String> pascoliRichiesti = Arrays.asList("158TN050");
		List<VariabileCalcolo> variabiliInput = initInputMan(pascoliRichiesti);
		
		// getConsistenzaPascoloDaCacheBdn
		String paramsCons = "{ \"annoCampagna\": 2018 , \"codPascolo\": \"158TN050\" }";
		String encodedParamsCons = URLEncoder.encode(paramsCons, "UTF-8");
		String resourceCons = configurazione.getUriCacheBdn().concat("consistenzaPascolo?params=").concat(encodedParamsCons);
		List<ConsistenzaPascolo2015Dto> consistenzaPascolo = new ArrayList<ConsistenzaPascolo2015Dto>();
		ResponseEntity<List<ConsistenzaPascolo2015Dto>> responseCons = new ResponseEntity<List<ConsistenzaPascolo2015Dto>>(consistenzaPascolo, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq(new URI(resourceCons)), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(new ParameterizedTypeReference<List<ConsistenzaPascolo2015Dto>>() {
		}))).thenReturn(responseCons);
		List<MovimentazionePascoloOviniDto> movimenti = new ArrayList<MovimentazionePascoloOviniDto>();
		paramsCons = "{ \"annoCampagna\": 2018 , \"codPascolo\": \"158TN050\"}";
		encodedParamsCons = URLEncoder.encode(paramsCons, "UTF-8");
		resourceCons = configurazione.getUriCacheBdn().concat("movimentiPascolo?params=").concat(encodedParamsCons);
		ResponseEntity<List<MovimentazionePascoloOviniDto>> responseMov = new ResponseEntity<List<MovimentazionePascoloOviniDto>>(movimenti, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq(new URI(resourceCons)), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(new ParameterizedTypeReference<List<MovimentazionePascoloOviniDto>>() {
		}))).thenReturn(responseMov);
		List<ConsistenzaAllevamentoDto> consAlle = new ArrayList<ConsistenzaAllevamentoDto>();
		paramsCons = "{ \"annoCampagna\": 2018 , \"codiceFiscale\": \"" + domanda.getCuaaIntestatario() + "\" }";
		encodedParamsCons = URLEncoder.encode(paramsCons, "UTF-8");
		resourceCons = configurazione.getUriCacheBdn().concat("consistenzaAllevamento?params=").concat(encodedParamsCons);
		ResponseEntity<List<ConsistenzaAllevamentoDto>> responseConsAlle = new ResponseEntity<List<ConsistenzaAllevamentoDto>>(consAlle, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq(new URI(resourceCons)), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(new ParameterizedTypeReference<List<ConsistenzaAllevamentoDto>>() {
		}))).thenReturn(responseConsAlle);
		

		// inserimento dati istruttore
		List<DatiIstruttoriaPascoli> datiIstruttoria = new ArrayList<>();
		DatiIstruttoriaPascoli datiIstruttoriaPascolo = new DatiIstruttoriaPascoli();
		datiIstruttoriaPascolo.setDescrizionePascolo("158TN050");
		datiIstruttoriaPascolo.setCuaaResponsabile("BNDCJD69S12C794H");
		datiIstruttoria.add(datiIstruttoriaPascolo);
		//serviceDomande.salvaDatiIstruttoriaDomanda(transizione.getIstruttoria().getDomandaUnica().getId(), objectMapper.writeValueAsString(datiIstruttoria), TipoDatoIstruttoria.PASCOLI);
		datiIstruttoreService.saveOrUpdateDatiIstruttoriaPascoli(transizione.getIstruttoria().getId(), datiIstruttoria);

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = serviceCalcoloMan.eseguiPasso(dati);

		assertEquals("DUT_004", res.getCodiceEsito());

	}

	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/calcoloDisaccoppiato/insertDatiTestMan.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	public void dut_004_pasf_05() throws Exception {

		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(180513));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = daoIstruttoria.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizione = transizioneIstruttoriaDao.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();

		List<String> pascoliRichiesti = Arrays.asList("158TN050");
		List<VariabileCalcolo> variabiliInput = initInputMan(pascoliRichiesti);
		
		// getConsistenzaPascoloDaCacheBdn
		String paramsCons = "{ \"annoCampagna\": 2018 , \"codPascolo\": \"158TN050\" }";
		String encodedParamsCons = URLEncoder.encode(paramsCons, "UTF-8");
		String resourceCons = configurazione.getUriCacheBdn().concat("consistenzaPascolo?params=").concat(encodedParamsCons);
		List<ConsistenzaPascolo2015Dto> consistenzaPascolo = new ArrayList<ConsistenzaPascolo2015Dto>();
		ResponseEntity<List<ConsistenzaPascolo2015Dto>> responseCons = new ResponseEntity<List<ConsistenzaPascolo2015Dto>>(consistenzaPascolo, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq(new URI(resourceCons)), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(new ParameterizedTypeReference<List<ConsistenzaPascolo2015Dto>>() {
		}))).thenReturn(responseCons);
		List<MovimentazionePascoloOviniDto> movimenti = new ArrayList<MovimentazionePascoloOviniDto>();
		paramsCons = "{ \"annoCampagna\": 2018 , \"codPascolo\": \"158TN050\"}";
		encodedParamsCons = URLEncoder.encode(paramsCons, "UTF-8");
		resourceCons = configurazione.getUriCacheBdn().concat("movimentiPascolo?params=").concat(encodedParamsCons);
		ResponseEntity<List<MovimentazionePascoloOviniDto>> responseMov = new ResponseEntity<List<MovimentazionePascoloOviniDto>>(movimenti, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq(new URI(resourceCons)), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(new ParameterizedTypeReference<List<MovimentazionePascoloOviniDto>>() {
		}))).thenReturn(responseMov);
		List<ConsistenzaAllevamentoDto> consAlle = new ArrayList<ConsistenzaAllevamentoDto>();
		paramsCons = "{ \"annoCampagna\": 2018 , \"codiceFiscale\": \"" + domanda.getCuaaIntestatario() + "\" }";
		encodedParamsCons = URLEncoder.encode(paramsCons, "UTF-8");
		resourceCons = configurazione.getUriCacheBdn().concat("consistenzaAllevamento?params=").concat(encodedParamsCons);
		ResponseEntity<List<ConsistenzaAllevamentoDto>> responseConsAlle = new ResponseEntity<List<ConsistenzaAllevamentoDto>>(consAlle, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq(new URI(resourceCons)), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(new ParameterizedTypeReference<List<ConsistenzaAllevamentoDto>>() {
		}))).thenReturn(responseConsAlle);
		

		// inserimento dati istruttore
		List<DatiIstruttoriaPascoli> datiIstruttoria = new ArrayList<>();
		DatiIstruttoriaPascoli datiIstruttoriaPascolo = new DatiIstruttoriaPascoli();
		datiIstruttoriaPascolo.setDescrizionePascolo("158TN050");
		datiIstruttoriaPascolo.setCuaaResponsabile("BNDCJD69S12C794E");
		datiIstruttoria.add(datiIstruttoriaPascolo);
		//serviceDomande.salvaDatiIstruttoriaDomanda(transizione.getIstruttoria().getDomandaUnica().getId(), objectMapper.writeValueAsString(datiIstruttoria), TipoDatoIstruttoria.PASCOLI);
		datiIstruttoreService.saveOrUpdateDatiIstruttoriaPascoli(transizione.getIstruttoria().getId(), datiIstruttoria);

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = serviceCalcoloMan.eseguiPasso(dati);

		assertEquals("DUT_004", res.getCodiceEsito());

	}

	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/calcoloDisaccoppiato/insertDatiTestMan.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	public void dut_003_pasf_04() throws Exception {

		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(180513));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = daoIstruttoria.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizione = transizioneIstruttoriaDao.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();

		List<String> pascoliRichiesti = Arrays.asList("158TN050");
		List<VariabileCalcolo> variabiliInput = initInputMan(pascoliRichiesti);

		// inserimento dati istruttore
		List<DatiIstruttoriaPascoli> datiIstruttoria = new ArrayList<>();
		DatiIstruttoriaPascoli datiIstruttoriaPascolo = new DatiIstruttoriaPascoli();
		datiIstruttoriaPascolo.setDescrizionePascolo("158TN050");
		datiIstruttoriaPascolo.setCuaaResponsabile("BNDCJD69S12C794E");
		datiIstruttoriaPascolo.setSuperficieDeterminata(BigDecimal.valueOf(12));
		datiIstruttoria.add(datiIstruttoriaPascolo);
		//serviceDomande.salvaDatiIstruttoriaDomanda(transizione.getIstruttoria().getDomandaUnica().getId(), objectMapper.writeValueAsString(datiIstruttoria), TipoDatoIstruttoria.PASCOLI);
		datiIstruttoreService.saveOrUpdateDatiIstruttoriaPascoli(transizione.getIstruttoria().getId(), datiIstruttoria);

		
		// getConsistenzaPascoloDaCacheBdn
		String paramsCons = "{ \"annoCampagna\": 2018 , \"codPascolo\": \"158TN050\" }";
		String encodedParamsCons = URLEncoder.encode(paramsCons, "UTF-8");
		String resourceCons = configurazione.getUriCacheBdn().concat("consistenzaPascolo?params=").concat(encodedParamsCons);
		List<ConsistenzaPascolo2015Dto> consistenzaPascolo = new ArrayList<ConsistenzaPascolo2015Dto>();
		ResponseEntity<List<ConsistenzaPascolo2015Dto>> responseCons = new ResponseEntity<List<ConsistenzaPascolo2015Dto>>(consistenzaPascolo, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq(new URI(resourceCons)), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(new ParameterizedTypeReference<List<ConsistenzaPascolo2015Dto>>() {
		}))).thenReturn(responseCons);
		String responseListMovimentazionePascolo = "[{\"idAllevamento\": 1, \"codiceFiscaleDetentore\":\"BNDCJD69S12C794E\",\"idPascolo\":5584,\"codPascolo\":\"158TN050\",\"comunePascolo\":\"VALFLORIANA\",\"annoCampagna\":2018,\"dataIngresso\":\"2018-06-18\",\"dataUscita\":\"2018-10-18\",\"giorniPascolamento\":0,\"numeroCapi\":22}, {\"idAllevamento\": 1, \"codiceFiscaleDetentore\":\"BNDCJD69S12C794E\",\"idPascolo\":5584,\"codPascolo\":\"158TN050\",\"comunePascolo\":\"VALFLORIANA\",\"annoCampagna\":2018,\"dataIngresso\":null,\"dataUscita\":\"2018-10-18\",\"giorniPascolamento\":50,\"numeroCapi\":22}]";
		List<MovimentazionePascoloOviniDto> movimentazioni = objectMapper.readValue(responseListMovimentazionePascolo, new TypeReference<List<MovimentazionePascoloOviniDto>>() {
		});
		paramsCons = "{ \"annoCampagna\": 2018 , \"codPascolo\": \"158TN050\"}";
		encodedParamsCons = URLEncoder.encode(paramsCons, "UTF-8");
		resourceCons = configurazione.getUriCacheBdn().concat("movimentiPascolo?params=").concat(encodedParamsCons);
		ResponseEntity<List<MovimentazionePascoloOviniDto>> responseMov = new ResponseEntity<List<MovimentazionePascoloOviniDto>>(movimentazioni, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq(new URI(resourceCons)), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(new ParameterizedTypeReference<List<MovimentazionePascoloOviniDto>>() {
		}))).thenReturn(responseMov);

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = serviceCalcoloMan.eseguiPasso(dati);

		// Betty da ripristinare
		assertEquals("DUT_003", res.getCodiceEsito());

	}

	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/calcoloDisaccoppiato/insertDatiTestMan.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	public void dut_004_pasf_03() throws Exception {

		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(180513));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = daoIstruttoria.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizione = transizioneIstruttoriaDao.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();

		List<String> pascoliRichiesti = Arrays.asList("158TN050");
		List<VariabileCalcolo> variabiliInput = initInputMan(pascoliRichiesti);

		// inserimento dati istruttore
		List<DatiIstruttoriaPascoli> datiIstruttoria = new ArrayList<>();
		DatiIstruttoriaPascoli datiIstruttoriaPascolo = new DatiIstruttoriaPascoli();
		datiIstruttoriaPascolo.setDescrizionePascolo("158TN050");
		datiIstruttoriaPascolo.setCuaaResponsabile("BNDCJD69S12C794E");
		datiIstruttoriaPascolo.setSuperficieDeterminata(BigDecimal.valueOf(12));
		datiIstruttoria.add(datiIstruttoriaPascolo);
		//serviceDomande.salvaDatiIstruttoriaDomanda(transizione.getIstruttoria().getDomandaUnica().getId(), objectMapper.writeValueAsString(datiIstruttoria), TipoDatoIstruttoria.PASCOLI);
		datiIstruttoreService.saveOrUpdateDatiIstruttoriaPascoli(transizione.getIstruttoria().getId(), datiIstruttoria);

		// getConsistenzaPascoloDaCacheBdn
		String paramsCons = "{ \"annoCampagna\": 2018 , \"codPascolo\": \"158TN050\" }";
		String encodedParamsCons = URLEncoder.encode(paramsCons, "UTF-8");
		String resourceCons = configurazione.getUriCacheBdn().concat("consistenzaPascolo?params=").concat(encodedParamsCons);
		String responseListConsistenzaPascolo = "[{\"codiceFiscaleSoggetto\":\"BNDCJD69S12C794H\",\"annoCampagna\":2018,\"codicePascolo\":\"158TN050\",\"fasciaEta\":\"OLTRE_24\",\"numeroCapi\":2,\"numeroCapiMedi\":2,\"giorniAlPascolo\":730,\"specie\":\"BOVINI\",\"provincia\":\"TN\",\"codiceFiscaleResponsabile\":\"BNDCJD69S12C794H\"},{\"codiceFiscaleSoggetto\":\"BNDCJD69S12C794H\",\"annoCampagna\":2018,\"codicePascolo\":\"158TN050\",\"fasciaEta\":\"OLTRE_24\",\"numeroCapi\":1,\"numeroCapiMedi\":1,\"giorniAlPascolo\":365,\"specie\":\"OVINI\",\"provincia\":\"TN\",\"codiceFiscaleResponsabile\":\"BNDCJD69S12C794H\"}]";
		List<ConsistenzaPascolo2015Dto> consistenzaPascolo = objectMapper.readValue(responseListConsistenzaPascolo, new TypeReference<List<ConsistenzaPascolo2015Dto>>() {
		});
		ResponseEntity<List<ConsistenzaPascolo2015Dto>> responseCons = new ResponseEntity<List<ConsistenzaPascolo2015Dto>>(consistenzaPascolo, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq(new URI(resourceCons)), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(new ParameterizedTypeReference<List<ConsistenzaPascolo2015Dto>>() {
		}))).thenReturn(responseCons);

		// getMovimentazionePascoloOviniDaCacheBDN
		String params = "{ \"annoCampagna\": 2018 , \"codPascolo\": \"158TN050\"}";
		String encoded = URLEncoder.encode(params, "UTF-8");
		String resource = configurazione.getUriCacheBdn().concat("movimentiPascolo?params=").concat(encoded);
		String responseListMovimentazionePascolo = "[{\"idAllevamento\": 1, \"codiceFiscaleDetentore\":\"BNDCJD69S12C794E\",\"idPascolo\":5584,\"codPascolo\":\"158TN050\",\"comunePascolo\":\"VALFLORIANA\",\"annoCampagna\":2018,\"dataIngresso\":\"2018-06-18\",\"dataUscita\":\"2018-10-18\",\"giorniPascolamento\":0,\"numeroCapi\":22}, {\"idAllevamento\": 1, \"codiceFiscaleDetentore\":\"BNDCJD69S12C794E\",\"idPascolo\":5584,\"codPascolo\":\"158TN050\",\"comunePascolo\":\"VALFLORIANA\",\"annoCampagna\":2018,\"dataIngresso\":null,\"dataUscita\":\"2018-10-18\",\"giorniPascolamento\":50,\"numeroCapi\":22}]";
		List<MovimentazionePascoloOviniDto> movimentazioni = objectMapper.readValue(responseListMovimentazionePascolo, new TypeReference<List<MovimentazionePascoloOviniDto>>() {
		});
		ResponseEntity<List<MovimentazionePascoloOviniDto>> response = new ResponseEntity<List<MovimentazionePascoloOviniDto>>(movimentazioni, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq(new URI(resource)), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(new ParameterizedTypeReference<List<MovimentazionePascoloOviniDto>>() {
		}))).thenReturn(response);
		List<ConsistenzaAllevamentoDto> consAlle = new ArrayList<ConsistenzaAllevamentoDto>();
		paramsCons = "{ \"annoCampagna\": 2018 , \"codiceFiscale\": \"" + domanda.getCuaaIntestatario() + "\" }";
		encodedParamsCons = URLEncoder.encode(paramsCons, "UTF-8");
		resourceCons = configurazione.getUriCacheBdn().concat("consistenzaAllevamento?params=").concat(encodedParamsCons);
		ResponseEntity<List<ConsistenzaAllevamentoDto>> responseConsAlle = new ResponseEntity<List<ConsistenzaAllevamentoDto>>(consAlle, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq(new URI(resourceCons)), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(new ParameterizedTypeReference<List<ConsistenzaAllevamentoDto>>() {
		}))).thenReturn(responseConsAlle);

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = serviceCalcoloMan.eseguiPasso(dati);

		assertEquals("DUT_004", res.getCodiceEsito());

	}

	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/calcoloDisaccoppiato/insertDatiTestMan.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	public void dut_004_pasf_02() throws Exception {

		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(180513));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = daoIstruttoria.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizione = transizioneIstruttoriaDao.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();

		List<String> pascoliRichiesti = Arrays.asList("158TN050");
		List<VariabileCalcolo> variabiliInput = initInputMan(pascoliRichiesti);

		// inserimento dati istruttore
		List<DatiIstruttoriaPascoli> datiIstruttoria = new ArrayList<>();
		DatiIstruttoriaPascoli datiIstruttoriaPascolo = new DatiIstruttoriaPascoli();
		datiIstruttoriaPascolo.setDescrizionePascolo("158TN050");
		datiIstruttoriaPascolo.setCuaaResponsabile("BNDCJD69S12C794E");
		datiIstruttoria.add(datiIstruttoriaPascolo);
		//serviceDomande.salvaDatiIstruttoriaDomanda(transizione.getIstruttoria().getDomandaUnica().getId(), objectMapper.writeValueAsString(datiIstruttoria), TipoDatoIstruttoria.PASCOLI);
		datiIstruttoreService.saveOrUpdateDatiIstruttoriaPascoli(transizione.getIstruttoria().getId(), datiIstruttoria);

		// getConsistenzaPascoloDaCacheBdn
		String paramsCons = "{ \"annoCampagna\": 2018 , \"codPascolo\": \"158TN050\" }";
		String encodedParamsCons = URLEncoder.encode(paramsCons, "UTF-8");
		String resourceCons = configurazione.getUriCacheBdn().concat("consistenzaPascolo?params=").concat(encodedParamsCons);
		String responseListConsistenzaPascolo = "[{\"codiceFiscaleSoggetto\":\"BNDCJD69S12C794E\",\"annoCampagna\":2018,\"codicePascolo\":\"158TN050\",\"fasciaEta\":\"OLTRE_24\",\"numeroCapi\":2,\"numeroCapiMedi\":2,\"giorniAlPascolo\":730,\"specie\":\"BOVINI\",\"provincia\":\"TN\",\"codiceFiscaleResponsabile\":\"BNDCJD69S12C794E\"},{\"codiceFiscaleSoggetto\":\"BNDCJD69S12C794E\",\"annoCampagna\":2018,\"codicePascolo\":\"158TN050\",\"fasciaEta\":\"OLTRE_24\",\"numeroCapi\":1,\"numeroCapiMedi\":1,\"giorniAlPascolo\":365,\"specie\":\"OVINI\",\"provincia\":\"TN\",\"codiceFiscaleResponsabile\":\"BNDCJD69S12C794E\"}]";
		List<ConsistenzaPascolo2015Dto> consistenzaPascolo = objectMapper.readValue(responseListConsistenzaPascolo, new TypeReference<List<ConsistenzaPascolo2015Dto>>() {
		});
		ResponseEntity<List<ConsistenzaPascolo2015Dto>> responseCons = new ResponseEntity<List<ConsistenzaPascolo2015Dto>>(consistenzaPascolo, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq(new URI(resourceCons)), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(new ParameterizedTypeReference<List<ConsistenzaPascolo2015Dto>>() {
		}))).thenReturn(responseCons);

		// getMovimentazionePascoloOviniDaCacheBDN
		String params = "{ \"annoCampagna\": 2018 , \"codPascolo\": \"158TN050\"}";
		String encoded = URLEncoder.encode(params, "UTF-8");
		String resource = configurazione.getUriCacheBdn().concat("movimentiPascolo?params=").concat(encoded);
		String responseListMovimentazionePascolo = "[{\"idAllevamento\": 5474263,\"codiceFiscaleDetentore\":\"BNDCJD69S12C794E\",\"idPascolo\":5584,\"codPascolo\":\"158TN050\",\"comunePascolo\":\"VALFLORIANA\",\"annoCampagna\":2018,\"dataIngresso\":null,\"dataUscita\":\"2018-10-18\",\"giorniPascolamento\":50,\"numeroCapi\":22}]";
		List<MovimentazionePascoloOviniDto> movimentazioni = objectMapper.readValue(responseListMovimentazionePascolo, new TypeReference<List<MovimentazionePascoloOviniDto>>() {
		});
		ResponseEntity<List<MovimentazionePascoloOviniDto>> response = new ResponseEntity<List<MovimentazionePascoloOviniDto>>(movimentazioni, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq(new URI(resource)), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(new ParameterizedTypeReference<List<MovimentazionePascoloOviniDto>>() {
		}))).thenReturn(response);
		List<ConsistenzaAllevamentoDto> consAlle = new ArrayList<ConsistenzaAllevamentoDto>();
		paramsCons = "{ \"annoCampagna\": 2018 , \"codiceFiscale\": \"" + domanda.getCuaaIntestatario() + "\" }";
		encodedParamsCons = URLEncoder.encode(paramsCons, "UTF-8");
		resourceCons = configurazione.getUriCacheBdn().concat("consistenzaAllevamento?params=").concat(encodedParamsCons);
		ResponseEntity<List<ConsistenzaAllevamentoDto>> responseConsAlle = new ResponseEntity<List<ConsistenzaAllevamentoDto>>(consAlle, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq(new URI(resourceCons)), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(new ParameterizedTypeReference<List<ConsistenzaAllevamentoDto>>() {
		}))).thenReturn(responseConsAlle);

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = serviceCalcoloMan.eseguiPasso(dati);

		assertEquals("DUT_004", res.getCodiceEsito());

	}

	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/calcoloDisaccoppiato/insertDatiTestMan.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	public void dut_003_pasf_01() throws Exception {

		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(180513));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = daoIstruttoria.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizione = transizioneIstruttoriaDao.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();

		List<String> pascoliRichiesti = Arrays.asList("158TN050");
		List<VariabileCalcolo> variabiliInput = initInputMan(pascoliRichiesti);

		// inserimento dati istruttore
		List<DatiIstruttoriaPascoli> datiIstruttoria = new ArrayList<>();
		DatiIstruttoriaPascoli datiIstruttoriaPascolo = new DatiIstruttoriaPascoli();
		datiIstruttoriaPascolo.setDescrizionePascolo("158TN050");
		datiIstruttoriaPascolo.setCuaaResponsabile("BNDCJD69S12C794E");
		datiIstruttoriaPascolo.setSuperficieDeterminata(BigDecimal.valueOf(12));
		datiIstruttoria.add(datiIstruttoriaPascolo);
		//serviceDomande.salvaDatiIstruttoriaDomanda(transizione.getIstruttoria().getDomandaUnica().getId(), objectMapper.writeValueAsString(datiIstruttoria), TipoDatoIstruttoria.PASCOLI);
		datiIstruttoreService.saveOrUpdateDatiIstruttoriaPascoli(transizione.getIstruttoria().getId(), datiIstruttoria);

		// getConsistenzaPascoloDaCacheBdn
		String paramsCons = "{ \"annoCampagna\": 2018 , \"codPascolo\": \"158TN050\" }";
		String encodedParamsCons = URLEncoder.encode(paramsCons, "UTF-8");
		String resourceCons = configurazione.getUriCacheBdn().concat("consistenzaPascolo?params=").concat(encodedParamsCons);
		String responseListConsistenzaPascolo = "[{\"codiceFiscaleSoggetto\":\"BNDCJD69S12C794E\",\"annoCampagna\":2018,\"codicePascolo\":\"158TN050\",\"fasciaEta\":\"OLTRE_24\",\"numeroCapi\":2,\"numeroCapiMedi\":2,\"giorniAlPascolo\":730,\"specie\":\"BOVINI\",\"provincia\":\"TN\",\"codiceFiscaleResponsabile\":\"BNDCJD69S12C794E\"},{\"codiceFiscaleSoggetto\":\"BNDCJD69S12C794E\",\"annoCampagna\":2018,\"codicePascolo\":\"158TN050\",\"fasciaEta\":\"OLTRE_24\",\"numeroCapi\":1,\"numeroCapiMedi\":1,\"giorniAlPascolo\":365,\"specie\":\"OVINI\",\"provincia\":\"TN\",\"codiceFiscaleResponsabile\":\"BNDCJD69S12C794E\"}]";
		List<ConsistenzaPascolo2015Dto> consistenzaPascolo = objectMapper.readValue(responseListConsistenzaPascolo, new TypeReference<List<ConsistenzaPascolo2015Dto>>() {
		});
		ResponseEntity<List<ConsistenzaPascolo2015Dto>> responseCons = new ResponseEntity<List<ConsistenzaPascolo2015Dto>>(consistenzaPascolo, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq(new URI(resourceCons)), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(new ParameterizedTypeReference<List<ConsistenzaPascolo2015Dto>>() {
		}))).thenReturn(responseCons);

		// getMovimentazionePascoloOviniDaCacheBDN
		String params = "{ \"annoCampagna\": 2018 , \"codPascolo\": \"158TN050\"}";
		String encoded = URLEncoder.encode(params, "UTF-8");
		String resource = configurazione.getUriCacheBdn().concat("movimentiPascolo?params=").concat(encoded);
		String responseListMovimentazionePascolo = "[{\"idAllevamento\": 1, \"codiceFiscaleDetentore\":\"BNDCJD69S12C794E\",\"idPascolo\":5584,\"codPascolo\":\"158TN050\",\"comunePascolo\":\"VALFLORIANA\",\"annoCampagna\":2018,\"dataIngresso\":\"2018-06-18\",\"dataUscita\":\"2018-10-18\",\"giorniPascolamento\":0,\"numeroCapi\":22}, {\"idAllevamento\": 1, \"codiceFiscaleDetentore\":\"BNDCJD69S12C794E\",\"idPascolo\":5584,\"codPascolo\":\"158TN050\",\"comunePascolo\":\"VALFLORIANA\",\"annoCampagna\":2018,\"dataIngresso\":null,\"dataUscita\":\"2018-10-18\",\"giorniPascolamento\":50,\"numeroCapi\":22}]";
		List<MovimentazionePascoloOviniDto> movimentazioni = objectMapper.readValue(responseListMovimentazionePascolo, new TypeReference<List<MovimentazionePascoloOviniDto>>() {
		});
		ResponseEntity<List<MovimentazionePascoloOviniDto>> response = new ResponseEntity<List<MovimentazionePascoloOviniDto>>(movimentazioni, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq(new URI(resource)), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(new ParameterizedTypeReference<List<MovimentazionePascoloOviniDto>>() {
		}))).thenReturn(response);

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = serviceCalcoloMan.eseguiPasso(dati);
		
		// Betty da ripristinare 
		assertEquals("DUT_003", res.getCodiceEsito());

	}
	
	@Test
	@Transactional
	public void calcoloMovimentazioneOvini2UsciteDoppie() throws Exception {
		String params = "{ \"annoCampagna\": 2018 , \"codPascolo\": \"038TN801\"}";
		String encoded = URLEncoder.encode(params, "UTF-8");
		String resource = configurazione.getUriCacheBdn().concat("movimentiPascolo?params=").concat(encoded);
		String responseListMovimentazionePascolo = "[{\"idAllevamento\":2394120,\"codiceFiscaleDetentore\":\"MNTSNO67C49B006K\",\"idPascolo\":5375,\"codPascolo\":\"038TN801\",\"comunePascolo\":\"CANAL SAN BOVO\",\"annoCampagna\":2018,\"dataIngresso\":\"2018-06-25\",\"dataUscita\":null,\"giorniPascolamento\":0,\"numeroCapi\":145},{\"idAllevamento\":2394120,\"codiceFiscaleDetentore\":\"MNTSNO67C49B006K\",\"idPascolo\":5375,\"codPascolo\":\"038TN801\",\"comunePascolo\":\"CANAL SAN BOVO\",\"annoCampagna\":2018,\"dataIngresso\":null,\"dataUscita\":\"2018-09-29\",\"giorniPascolamento\":0,\"numeroCapi\":145},{\"idAllevamento\":2394120,\"codiceFiscaleDetentore\":\"MNTSNO67C49B006K\",\"idPascolo\":5375,\"codPascolo\":\"038TN801\",\"comunePascolo\":\"CANAL SAN BOVO\",\"annoCampagna\":2018,\"dataIngresso\":null,\"dataUscita\":\"2018-09-15\",\"giorniPascolamento\":0,\"numeroCapi\":145}]";
		List<MovimentazionePascoloOviniDto> movimentazioni = objectMapper.readValue(responseListMovimentazionePascolo, new TypeReference<List<MovimentazionePascoloOviniDto>>() {
		});
		ResponseEntity<List<MovimentazionePascoloOviniDto>> response = new ResponseEntity<List<MovimentazionePascoloOviniDto>>(movimentazioni, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq(new URI(resource)), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(new ParameterizedTypeReference<List<MovimentazionePascoloOviniDto>>() {
		}))).thenReturn(response);
		
		List<MovimentazionePascoloOviniDto> movim =
				serviceCalcoloMan.getMovimentazionePascoloOviniDaCacheBdn(2018, "038TN801");
		assertNotNull(movim);
		assertEquals(1, movim.size());
		MovimentazionePascoloOviniDto movimento = movim.get(0);
		assertEquals(new BigDecimal("145"), movimento.getNumeroCapi());
		assertEquals(82L, movimento.getGiorniPascolamento().longValue());
	}

	
	@Test
	@Transactional
	public void calcoloMovimentazioneOviniCasoAnomalo() throws Exception {
		String params = "{ \"annoCampagna\": 2018 , \"codPascolo\": \"072TN035\"}";
		String encoded = URLEncoder.encode(params, "UTF-8");
		String resource = configurazione.getUriCacheBdn().concat("movimentiPascolo?params=").concat(encoded);
		String responseListMovimentazionePascolo = "[{\"idAllevamento\":5582055,\"codiceFiscaleDetentore\":\"CLLVNT84A25L174Q\",\"idPascolo\":5256,\"codPascolo\":\"072TN035\",\"comunePascolo\":\"VALDAONE\",\"annoCampagna\":2018,\"dataIngresso\":\"2018-06-24\",\"dataUscita\":null,\"giorniPascolamento\":0,\"numeroCapi\":7},{\"idAllevamento\":5582055,\"codiceFiscaleDetentore\":\"CLLVNT84A25L174Q\",\"idPascolo\":5256,\"codPascolo\":\"072TN035\",\"comunePascolo\":\"VALDAONE\",\"annoCampagna\":2018,\"dataIngresso\":null,\"dataUscita\":\"2018-09-08\",\"giorniPascolamento\":0,\"numeroCapi\":7},{\"idAllevamento\":5582055,\"codiceFiscaleDetentore\":\"CLLVNT84A25L174Q\",\"idPascolo\":5256,\"codPascolo\":\"072TN035\",\"comunePascolo\":\"VALDAONE\",\"annoCampagna\":2018,\"dataIngresso\":null,\"dataUscita\":\"2018-08-26\",\"giorniPascolamento\":0,\"numeroCapi\":7}]";
		List<MovimentazionePascoloOviniDto> movimentazioni = objectMapper.readValue(responseListMovimentazionePascolo, new TypeReference<List<MovimentazionePascoloOviniDto>>() {
		});
		ResponseEntity<List<MovimentazionePascoloOviniDto>> response = new ResponseEntity<List<MovimentazionePascoloOviniDto>>(movimentazioni, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq(new URI(resource)), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(new ParameterizedTypeReference<List<MovimentazionePascoloOviniDto>>() {
		}))).thenReturn(response);
		
		List<MovimentazionePascoloOviniDto> movim =
				serviceCalcoloMan.getMovimentazionePascoloOviniDaCacheBdn(2018, "072TN035");
		assertNotNull(movim);
		assertEquals(1, movim.size());
		MovimentazionePascoloOviniDto movimento = movim.get(0);
		assertEquals(new BigDecimal("7"), movimento.getNumeroCapi());
		assertEquals(63L, movimento.getGiorniPascolamento().longValue());
	}

	
	@Test
	@Transactional
	public void calcoloMovimentazioneOvini2UsciteCongrue() throws Exception {
		String params = "{ \"annoCampagna\": 2018 , \"codPascolo\": \"196TN090\"}";
		String encoded = URLEncoder.encode(params, "UTF-8");
		String resource = configurazione.getUriCacheBdn().concat("movimentiPascolo?params=").concat(encoded);
		String responseListMovimentazionePascolo = "[{\"idAllevamento\":4194929,\"codiceFiscaleDetentore\":\"ZWRFBA93R05C372U\",\"idPascolo\":5582,\"codPascolo\":\"196TN090\",\"comunePascolo\":\"TESERO\",\"annoCampagna\":2018,\"dataIngresso\":\"2018-06-09\",\"dataUscita\":null,\"giorniPascolamento\":0,\"numeroCapi\":230},{\"idAllevamento\":4194929,\"codiceFiscaleDetentore\":\"ZWRFBA93R05C372U\",\"idPascolo\":5582,\"codPascolo\":\"196TN090\",\"comunePascolo\":\"TESERO\",\"annoCampagna\":2018,\"dataIngresso\":null,\"dataUscita\":\"2018-09-13\",\"giorniPascolamento\":0,\"numeroCapi\":216},{\"idAllevamento\":4194929,\"codiceFiscaleDetentore\":\"ZWRFBA93R05C372U\",\"idPascolo\":5582,\"codPascolo\":\"196TN090\",\"comunePascolo\":\"TESERO\",\"annoCampagna\":2018,\"dataIngresso\":null,\"dataUscita\":\"2018-08-06\",\"giorniPascolamento\":0,\"numeroCapi\":14}]";
		List<MovimentazionePascoloOviniDto> movimentazioni = objectMapper.readValue(responseListMovimentazionePascolo, new TypeReference<List<MovimentazionePascoloOviniDto>>() {
		});
		ResponseEntity<List<MovimentazionePascoloOviniDto>> response = new ResponseEntity<List<MovimentazionePascoloOviniDto>>(movimentazioni, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq(new URI(resource)), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(new ParameterizedTypeReference<List<MovimentazionePascoloOviniDto>>() {
		}))).thenReturn(response);
		
		List<MovimentazionePascoloOviniDto> movim =
				serviceCalcoloMan.getMovimentazionePascoloOviniDaCacheBdn(2018, "196TN090");
		assertNotNull(movim);
		assertEquals(1, movim.size());
		MovimentazionePascoloOviniDto movimento = movim.get(0);
		assertEquals(new BigDecimal(230), movimento.getNumeroCapi());
		assertEquals(58L, movimento.getGiorniPascolamento().longValue());
	}
	
	@Test
	@Transactional
	public void calcoloMovimentazioneOvini2AllevamentiMovimentati() throws Exception {
		// Associo all'allevamento il numero di capi attesi
        Stream<BigDecimal[]> values = Stream.of(new BigDecimal[][] {{new BigDecimal(5658749), new BigDecimal(176)}, 
        	{new BigDecimal(1433888), new BigDecimal(20)}});
        Map<BigDecimal, BigDecimal> capiPerAllevamento = values.collect(Collectors.toMap(p -> p[0], p -> p[1])); 
        
        Integer anno = 2018;
        String codicePascolo = "145TN055";
		
		String params = "{ \"annoCampagna\": " + anno + " , \"codPascolo\": \"" + codicePascolo + "\"}";
		String encoded = URLEncoder.encode(params, "UTF-8");
		String resource = configurazione.getUriCacheBdn().concat("movimentiPascolo?params=").concat(encoded);
		String responseListMovimentazionePascolo = "[{\"idAllevamento\":1433888,\"codiceFiscaleDetentore\":\"BTTLSS59B10C372P\",\"idPascolo\":5569,\"codPascolo\":\"145TN055\",\"comunePascolo\":\"SAN GIOVANNI DI FASSA\",\"annoCampagna\":2018,\"dataIngresso\":\"2018-07-07\",\"dataUscita\":null,\"giorniPascolamento\":0,\"numeroCapi\":20},{\"idAllevamento\":1433888,\"codiceFiscaleDetentore\":\"BTTLSS59B10C372P\",\"idPascolo\":5569,\"codPascolo\":\"145TN055\",\"comunePascolo\":\"SAN GIOVANNI DI FASSA\",\"annoCampagna\":2018,\"dataIngresso\":null,\"dataUscita\":\"2018-10-10\",\"giorniPascolamento\":0,\"numeroCapi\":20},{\"idAllevamento\":5658749,\"codiceFiscaleDetentore\":\"02418430225\",\"idPascolo\":5569,\"codPascolo\":\"145TN055\",\"comunePascolo\":\"SAN GIOVANNI DI FASSA\",\"annoCampagna\":2018,\"dataIngresso\":\"2018-07-14\",\"dataUscita\":null,\"giorniPascolamento\":0,\"numeroCapi\":176},{\"idAllevamento\":5658749,\"codiceFiscaleDetentore\":\"02418430225\",\"idPascolo\":5569,\"codPascolo\":\"145TN055\",\"comunePascolo\":\"SAN GIOVANNI DI FASSA\",\"annoCampagna\":2018,\"dataIngresso\":null,\"dataUscita\":\"2018-10-10\",\"giorniPascolamento\":0,\"numeroCapi\":176}]";
		List<MovimentazionePascoloOviniDto> mockedResponse = objectMapper.readValue(responseListMovimentazionePascolo, new TypeReference<List<MovimentazionePascoloOviniDto>>() {
		});
		ResponseEntity<List<MovimentazionePascoloOviniDto>> response = new ResponseEntity<List<MovimentazionePascoloOviniDto>>(mockedResponse, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq(new URI(resource)), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(new ParameterizedTypeReference<List<MovimentazionePascoloOviniDto>>() {
		}))).thenReturn(response);
		
		List<MovimentazionePascoloOviniDto> movimentazioni =
				serviceCalcoloMan.getMovimentazionePascoloOviniDaCacheBdn(anno, codicePascolo);
		assertNotNull(movimentazioni);
		assertEquals(2, movimentazioni.size());
		
		for (MovimentazionePascoloOviniDto movimentazione : movimentazioni) {
			assertEquals(capiPerAllevamento.get(movimentazione.getIdAllevamento()), movimentazione.getNumeroCapi());
		}
	}
	
	@Test
	@Transactional
	public void calcoloMovimentazioneOviniUnaEntrataDueUsciteCongrue() throws Exception {
		Integer anno = 2018;
        String codicePascolo = "196TN159";
		
		String params = "{ \"annoCampagna\": " + anno + " , \"codPascolo\": \"" + codicePascolo + "\"}";
		String encoded = URLEncoder.encode(params, "UTF-8");
		String resource = configurazione.getUriCacheBdn().concat("movimentiPascolo?params=").concat(encoded);
		String responseListMovimentazionePascolo = "[{\"idAllevamento\":4194929,\"codiceFiscaleDetentore\":\"ZWRFBA93R05C372U\",\"idPascolo\":36404,\"codPascolo\":\"196TN159\",\"comunePascolo\":\"TESERO\",\"annoCampagna\":2018,\"dataIngresso\":\"2018-06-09\",\"dataUscita\":null,\"giorniPascolamento\":0,\"numeroCapi\":95},{\"idAllevamento\":4194929,\"codiceFiscaleDetentore\":\"ZWRFBA93R05C372U\",\"idPascolo\":36404,\"codPascolo\":\"196TN159\",\"comunePascolo\":\"TESERO\",\"annoCampagna\":2018,\"dataIngresso\":null,\"dataUscita\":\"2018-09-13\",\"giorniPascolamento\":0,\"numeroCapi\":93},{\"idAllevamento\":4194929,\"codiceFiscaleDetentore\":\"ZWRFBA93R05C372U\",\"idPascolo\":36404,\"codPascolo\":\"196TN159\",\"comunePascolo\":\"TESERO\",\"annoCampagna\":2018,\"dataIngresso\":null,\"dataUscita\":\"2018-08-06\",\"giorniPascolamento\":0,\"numeroCapi\":2}]";
		List<MovimentazionePascoloOviniDto> mockedResponse = objectMapper.readValue(responseListMovimentazionePascolo, new TypeReference<List<MovimentazionePascoloOviniDto>>() {
		});
		ResponseEntity<List<MovimentazionePascoloOviniDto>> response = new ResponseEntity<List<MovimentazionePascoloOviniDto>>(mockedResponse, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq(new URI(resource)), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(new ParameterizedTypeReference<List<MovimentazionePascoloOviniDto>>() {
		}))).thenReturn(response);
		
		List<MovimentazionePascoloOviniDto> movimentazioni =
				serviceCalcoloMan.getMovimentazionePascoloOviniDaCacheBdn(anno, codicePascolo);
		assertNotNull(movimentazioni);
		assertEquals(1, movimentazioni.size());
		
		MovimentazionePascoloOviniDto movimentazione = movimentazioni.get(0);
		assertEquals(new BigDecimal(95), movimentazione.getNumeroCapi());
		assertEquals(58L, movimentazione.getGiorniPascolamento().longValue());
	}

	// TEST PASCOLI AZIENDALI

	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/calcoloDisaccoppiato/insertDatiTestMan.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	public void dut_004_pasf_011() throws Exception {

		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(180523));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = daoIstruttoria.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizione = transizioneIstruttoriaDao.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();

		List<String> pascoliRichiesti = Arrays.asList("AZIENDALE - P464");
		List<VariabileCalcolo> variabiliInput = initInputMan(pascoliRichiesti);

		// getConsistenzaPascoloDaCacheBdn
		String paramsCons = "{ \"annoCampagna\": 2018 , \"codiceFiscale\": \"AZIENDALE - P464\" }";
		String encodedParamsCons = URLEncoder.encode(paramsCons, "UTF-8");
		String resourceCons = configurazione.getUriCacheBdn().concat("consistenzaPascolo?params=").concat(encodedParamsCons);
		List<ConsistenzaPascolo2015Dto> consistenzaPascolo = new ArrayList<ConsistenzaPascolo2015Dto>();
		ResponseEntity<List<ConsistenzaPascolo2015Dto>> responseCons = new ResponseEntity<List<ConsistenzaPascolo2015Dto>>(consistenzaPascolo, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq(new URI(resourceCons)), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(new ParameterizedTypeReference<List<ConsistenzaPascolo2015Dto>>() {
		}))).thenReturn(responseCons);
		List<MovimentazionePascoloOviniDto> movimenti = new ArrayList<MovimentazionePascoloOviniDto>();
		paramsCons = "{ \"annoCampagna\": 2018 , \"codPascolo\": \"AZIENDALE - P464\"}";
		encodedParamsCons = URLEncoder.encode(paramsCons, "UTF-8");
		resourceCons = configurazione.getUriCacheBdn().concat("movimentiPascolo?params=").concat(encodedParamsCons);
		ResponseEntity<List<MovimentazionePascoloOviniDto>> responseMov = new ResponseEntity<List<MovimentazionePascoloOviniDto>>(movimenti, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq(new URI(resourceCons)), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(new ParameterizedTypeReference<List<MovimentazionePascoloOviniDto>>() {
		}))).thenReturn(responseMov);
		List<ConsistenzaAllevamentoDto> consAlle = new ArrayList<ConsistenzaAllevamentoDto>();
		paramsCons = "{ \"annoCampagna\": 2018 , \"codiceFiscale\": \"" + domanda.getCuaaIntestatario() + "\" }";
		encodedParamsCons = URLEncoder.encode(paramsCons, "UTF-8");
		resourceCons = configurazione.getUriCacheBdn().concat("consistenzaAllevamento?params=").concat(encodedParamsCons);
		ResponseEntity<List<ConsistenzaAllevamentoDto>> responseConsAlle = new ResponseEntity<List<ConsistenzaAllevamentoDto>>(consAlle, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq(new URI(resourceCons)), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(new ParameterizedTypeReference<List<ConsistenzaAllevamentoDto>>() {
		}))).thenReturn(responseConsAlle);
		
		// inserimento dati istruttore
		List<DatiIstruttoriaPascoli> datiIstruttoria = new ArrayList<>();
		DatiIstruttoriaPascoli datiIstruttoriaPascolo = new DatiIstruttoriaPascoli();
		datiIstruttoriaPascolo.setDescrizionePascolo("AZIENDALE - P464");
		datiIstruttoriaPascolo.setCuaaResponsabile("BNDCLD69S12C794E");
		datiIstruttoriaPascolo.setSuperficieDeterminata(BigDecimal.valueOf(12));
		datiIstruttoria.add(datiIstruttoriaPascolo);
		//serviceDomande.salvaDatiIstruttoriaDomanda(transizione.getIstruttoria().getDomandaUnica().getId(), objectMapper.writeValueAsString(datiIstruttoria), TipoDatoIstruttoria.PASCOLI);
		datiIstruttoreService.saveOrUpdateDatiIstruttoriaPascoli(transizione.getIstruttoria().getId(), datiIstruttoria);

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = serviceCalcoloMan.eseguiPasso(dati);

		assertEquals("DUT_004", res.getCodiceEsito());

	}

	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/comuniLimitrofi.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/calcoloDisaccoppiato/insertDatiTestMan.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	public void dut_003_pasf_012() throws Exception {

		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(180523));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = daoIstruttoria.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizione = transizioneIstruttoriaDao.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();

		List<String> pascoliRichiesti = Arrays.asList("AZIENDALE - P464");
		List<VariabileCalcolo> variabiliInput = initInputMan(pascoliRichiesti);

		// inserimento dati istruttore
		List<DatiIstruttoriaPascoli> datiIstruttoria = new ArrayList<>();
		DatiIstruttoriaPascoli datiIstruttoriaPascolo = new DatiIstruttoriaPascoli();
		datiIstruttoriaPascolo.setDescrizionePascolo("AZIENDALE - P464");
		datiIstruttoriaPascolo.setCuaaResponsabile("BNDCLD69S12C794E");
		datiIstruttoriaPascolo.setSuperficieDeterminata(BigDecimal.valueOf(12));
		datiIstruttoria.add(datiIstruttoriaPascolo);
		//serviceDomande.salvaDatiIstruttoriaDomanda(transizione.getIstruttoria().getDomandaUnica().getId(), objectMapper.writeValueAsString(datiIstruttoria), TipoDatoIstruttoria.PASCOLI);
		datiIstruttoreService.saveOrUpdateDatiIstruttoriaPascoli(transizione.getIstruttoria().getId(), datiIstruttoria);

		// getConsistenzaAllevamentoDaCacheBdn
		String params = "{ \"annoCampagna\": 2018 , \"codiceFiscale\": \"BNDCLD69S12C794E\" }";
		String encoded = URLEncoder.encode(params, "UTF-8");
		String resource = configurazione.getUriCacheBdn().concat("consistenzaAllevamento?params=").concat(encoded);
		String responseListConsistenzaAllevamento = "[{\"idCaws\":6806,\"codiFiscSogg\":\"BNDCLD69S12C794E\",\"numeCamp\":2018,\"idAlleBdn\":2102604,\"codiSpec\":\"0121\",\"codiAsll\":\"246TN115\",\"codiFiscProp\":\"BNDCLD69S12C794E\",\"codiFiscDete\":\"BNDCLD69S12C794E\",\"consCapi06\":1,\"consCapi624\":14,\"consCapiOver24\":23,\"consTota\":38,\"consVaccOver20\":24,\"dataInizDete\":null,\"dataFineDete\":null,\"decoStat\":85,\"dataIniz\":\"2019-04-02\",\"dataFine\":\"9999-12-31\",\"dataAggi\":\"2019-05-24\",\"userName\":\"PROXY-BDN\",\"codiceComune\":\"246\"},{\"idCaws\":6807,\"codiFiscSogg\":\"BNDCLD69S12C794E\",\"numeCamp\":2018,\"idAlleBdn\":4119775,\"codiSpec\":\"0122\",\"codiAsll\":\"246TN115\",\"codiFiscProp\":\"BNDCLD69S12C794E\",\"codiFiscDete\":\"BNDCLD69S12C794E\",\"consCapi06\":0,\"consCapi624\":0,\"consCapiOver24\":0,\"consTota\":0,\"consVaccOver20\":0,\"dataInizDete\":null,\"dataFineDete\":null,\"decoStat\":85,\"dataIniz\":\"2019-04-02\",\"dataFine\":\"9999-12-31\",\"dataAggi\":\"2019-04-02\",\"userName\":\"PROXY-BDN\",\"codiceComune\":\"246\"}]";
		List<ConsistenzaAllevamentoDto> movimentazioni = objectMapper.readValue(responseListConsistenzaAllevamento, new TypeReference<List<ConsistenzaAllevamentoDto>>() {
		});
		ResponseEntity<List<ConsistenzaAllevamentoDto>> response = new ResponseEntity<List<ConsistenzaAllevamentoDto>>(movimentazioni, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq(new URI(resource)), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(new ParameterizedTypeReference<List<ConsistenzaAllevamentoDto>>() {
		}))).thenReturn(response);

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = serviceCalcoloMan.eseguiPasso(dati);

		assertEquals("DUT_003", res.getCodiceEsito());

	}

	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/comuniLimitrofi.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/calcoloDisaccoppiato/insertDatiTestMan.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	public void dut_004_pasf_013() throws Exception {

		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(180523));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = daoIstruttoria.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizione = transizioneIstruttoriaDao.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();

		List<String> pascoliRichiesti = Arrays.asList("AZIENDALE - P464");
		List<VariabileCalcolo> variabiliInput = initInputMan(pascoliRichiesti);

		// inserimento dati istruttore
		List<DatiIstruttoriaPascoli> datiIstruttoria = new ArrayList<>();
		DatiIstruttoriaPascoli datiIstruttoriaPascolo = new DatiIstruttoriaPascoli();
		datiIstruttoriaPascolo.setDescrizionePascolo("AZIENDALE - P464");
		datiIstruttoriaPascolo.setCuaaResponsabile("BNDCLD69S12C794E");
		datiIstruttoria.add(datiIstruttoriaPascolo);
		//serviceDomande.salvaDatiIstruttoriaDomanda(transizione.getIstruttoria().getDomandaUnica().getId(), objectMapper.writeValueAsString(datiIstruttoria), TipoDatoIstruttoria.PASCOLI);
		datiIstruttoreService.saveOrUpdateDatiIstruttoriaPascoli(transizione.getIstruttoria().getId(), datiIstruttoria);

		// getConsistenzaAllevamentoDaCacheBdn
		String params = "{ \"annoCampagna\": 2018 , \"codiceFiscale\": \"BNDCLD69S12C794E\" }";
		String encoded = URLEncoder.encode(params, "UTF-8");
		String resource = configurazione.getUriCacheBdn().concat("consistenzaAllevamento?params=").concat(encoded);
		String responseListConsistenzaAllevamento = "[{\"idCaws\":6806,\"codiFiscSogg\":\"BNDCLD69S12C794E\",\"numeCamp\":2018,\"idAlleBdn\":2102604,\"codiSpec\":\"0121\",\"codiAsll\":\"246TN115\",\"codiFiscProp\":\"BNDCLD69S12C794E\",\"codiFiscDete\":\"BNDCLD69S12C794E\",\"consCapi06\":1,\"consCapi624\":14,\"consCapiOver24\":23,\"consTota\":38,\"consVaccOver20\":24,\"dataInizDete\":null,\"dataFineDete\":null,\"decoStat\":85,\"dataIniz\":\"2019-04-02\",\"dataFine\":\"9999-12-31\",\"dataAggi\":\"2019-05-24\",\"userName\":\"PROXY-BDN\",\"codiceComune\":\"246\"},{\"idCaws\":6807,\"codiFiscSogg\":\"BNDCLD69S12C794E\",\"numeCamp\":2018,\"idAlleBdn\":4119775,\"codiSpec\":\"0122\",\"codiAsll\":\"246TN115\",\"codiFiscProp\":\"BNDCLD69S12C794E\",\"codiFiscDete\":\"BNDCLD69S12C794E\",\"consCapi06\":0,\"consCapi624\":0,\"consCapiOver24\":0,\"consTota\":0,\"consVaccOver20\":0,\"dataInizDete\":null,\"dataFineDete\":null,\"decoStat\":85,\"dataIniz\":\"2019-04-02\",\"dataFine\":\"9999-12-31\",\"dataAggi\":\"2019-04-02\",\"userName\":\"PROXY-BDN\",\"codiceComune\":\"246\"}]";
		List<ConsistenzaAllevamentoDto> movimentazioni = objectMapper.readValue(responseListConsistenzaAllevamento, new TypeReference<List<ConsistenzaAllevamentoDto>>() {
		});
		ResponseEntity<List<ConsistenzaAllevamentoDto>> response = new ResponseEntity<List<ConsistenzaAllevamentoDto>>(movimentazioni, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq(new URI(resource)), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(new ParameterizedTypeReference<List<ConsistenzaAllevamentoDto>>() {
		}))).thenReturn(response);

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = serviceCalcoloMan.eseguiPasso(dati);

		assertEquals("DUT_004", res.getCodiceEsito());

	}

	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/comuniLimitrofi.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/calcoloDisaccoppiato/insertDatiTestMan.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	public void duf_018_pasf_014() throws Exception {

		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(180523));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = daoIstruttoria.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizione = transizioneIstruttoriaDao.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();

		List<String> pascoliRichiesti = Arrays.asList("AZIENDALE - P464");
		List<VariabileCalcolo> variabiliInput = initInputMan(pascoliRichiesti);

		// inserimento dati istruttore
		List<DatiIstruttoriaPascoli> datiIstruttoria = new ArrayList<>();
		DatiIstruttoriaPascoli datiIstruttoriaPascolo = new DatiIstruttoriaPascoli();
		datiIstruttoriaPascolo.setDescrizionePascolo("AZIENDALE - P464");
		datiIstruttoriaPascolo.setCuaaResponsabile("BNDCLD69S12C794E");
		datiIstruttoria.add(datiIstruttoriaPascolo);
		//serviceDomande.salvaDatiIstruttoriaDomanda(transizione.getIstruttoria().getDomandaUnica().getId(), objectMapper.writeValueAsString(datiIstruttoria), TipoDatoIstruttoria.PASCOLI);
		datiIstruttoreService.saveOrUpdateDatiIstruttoriaPascoli(transizione.getIstruttoria().getId(), datiIstruttoria);

		// getConsistenzaAllevamentoDaCacheBdn
		String params = "{ \"annoCampagna\": 2018 , \"codiceFiscale\": \"BNDCLD69S12C794E\" }";
		String encoded = URLEncoder.encode(params, "UTF-8");
		String resource = configurazione.getUriCacheBdn().concat("consistenzaAllevamento?params=").concat(encoded);
		String responseListConsistenzaAllevamento = "[{\"idCaws\":6806,\"codiFiscSogg\":\"BNDCLD69S12C794E\",\"numeCamp\":2018,\"idAlleBdn\":2102604,\"codiSpec\":\"0121\",\"codiAsll\":\"555TN115\",\"codiFiscProp\":\"BNDCLD69S12C794E\",\"codiFiscDete\":\"BNDCLD69S12C794E\",\"consCapi06\":1,\"consCapi624\":14,\"consCapiOver24\":23,\"consTota\":38,\"consVaccOver20\":24,\"dataInizDete\":null,\"dataFineDete\":null,\"decoStat\":85,\"dataIniz\":\"2019-04-02\",\"dataFine\":\"9999-12-31\",\"dataAggi\":\"2019-05-24\",\"userName\":\"PROXY-BDN\",\"codiceComune\":\"555\"},{\"idCaws\":6807,\"codiFiscSogg\":\"BNDCLD69S12C794E\",\"numeCamp\":2018,\"idAlleBdn\":4119775,\"codiSpec\":\"0122\",\"codiAsll\":\"246TN115\",\"codiFiscProp\":\"BNDCLD69S12C794E\",\"codiFiscDete\":\"BNDCLD69S12C794E\",\"consCapi06\":0,\"consCapi624\":0,\"consCapiOver24\":0,\"consTota\":0,\"consVaccOver20\":0,\"dataInizDete\":null,\"dataFineDete\":null,\"decoStat\":85,\"dataIniz\":\"2019-04-02\",\"dataFine\":\"9999-12-31\",\"dataAggi\":\"2019-04-02\",\"userName\":\"PROXY-BDN\",\"codiceComune\":\"246\"}]";
		List<ConsistenzaAllevamentoDto> movimentazioni = objectMapper.readValue(responseListConsistenzaAllevamento, new TypeReference<List<ConsistenzaAllevamentoDto>>() {
		});
		ResponseEntity<List<ConsistenzaAllevamentoDto>> response = new ResponseEntity<List<ConsistenzaAllevamentoDto>>(movimentazioni, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq(new URI(resource)), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(new ParameterizedTypeReference<List<ConsistenzaAllevamentoDto>>() {
		}))).thenReturn(response);

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = serviceCalcoloMan.eseguiPasso(dati);

		assertEquals("DUF_018", res.getCodiceEsito());

	}

	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/comuniLimitrofi.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/calcoloDisaccoppiato/insertDatiTestMan.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	public void dut_003_pasf_015() throws Exception {

		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(180523));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = daoIstruttoria.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizione = transizioneIstruttoriaDao.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();

		List<String> pascoliRichiesti = Arrays.asList("AZIENDALE - P464");
		List<VariabileCalcolo> variabiliInput = initInputMan(pascoliRichiesti);

		// inserimento dati istruttore
		List<DatiIstruttoriaPascoli> datiIstruttoria = new ArrayList<>();
		DatiIstruttoriaPascoli datiIstruttoriaPascolo = new DatiIstruttoriaPascoli();
		datiIstruttoriaPascolo.setDescrizionePascolo("AZIENDALE - P464");
		datiIstruttoriaPascolo.setCuaaResponsabile("BNDCLD69S12C794E");
		datiIstruttoriaPascolo.setSuperficieDeterminata(BigDecimal.valueOf(12));
		datiIstruttoriaPascolo.setVerificaDocumentazione(DOC_PRESENTE);
		datiIstruttoria.add(datiIstruttoriaPascolo);
		//serviceDomande.salvaDatiIstruttoriaDomanda(transizione.getIstruttoria().getDomandaUnica().getId(), objectMapper.writeValueAsString(datiIstruttoria), TipoDatoIstruttoria.PASCOLI);
		datiIstruttoreService.saveOrUpdateDatiIstruttoriaPascoli(transizione.getIstruttoria().getId(), datiIstruttoria);

		// getConsistenzaAllevamentoDaCacheBdn
		String params = "{ \"annoCampagna\": 2018 , \"codiceFiscale\": \"BNDCLD69S12C794E\" }";
		String encoded = URLEncoder.encode(params, "UTF-8");
		String resource = configurazione.getUriCacheBdn().concat("consistenzaAllevamento?params=").concat(encoded);
		String responseListConsistenzaAllevamento = "[{\"idCaws\":6806,\"codiFiscSogg\":\"BNDCLD69S12C794E\",\"numeCamp\":2018,\"idAlleBdn\":2102604,\"codiSpec\":\"0121\",\"codiAsll\":\"555TN115\",\"codiFiscProp\":\"BNDCLD69S12C794E\",\"codiFiscDete\":\"BNDCLD69S12C794E\",\"consCapi06\":1,\"consCapi624\":14,\"consCapiOver24\":23,\"consTota\":38,\"consVaccOver20\":24,\"dataInizDete\":null,\"dataFineDete\":null,\"decoStat\":85,\"dataIniz\":\"2019-04-02\",\"dataFine\":\"9999-12-31\",\"dataAggi\":\"2019-05-24\",\"userName\":\"PROXY-BDN\",\"codiceComune\":\"555\"},{\"idCaws\":6807,\"codiFiscSogg\":\"BNDCLD69S12C794E\",\"numeCamp\":2018,\"idAlleBdn\":4119775,\"codiSpec\":\"0122\",\"codiAsll\":\"246TN115\",\"codiFiscProp\":\"BNDCLD69S12C794E\",\"codiFiscDete\":\"BNDCLD69S12C794E\",\"consCapi06\":0,\"consCapi624\":0,\"consCapiOver24\":0,\"consTota\":0,\"consVaccOver20\":0,\"dataInizDete\":null,\"dataFineDete\":null,\"decoStat\":85,\"dataIniz\":\"2019-04-02\",\"dataFine\":\"9999-12-31\",\"dataAggi\":\"2019-04-02\",\"userName\":\"PROXY-BDN\",\"codiceComune\":\"555\"}]";
		List<ConsistenzaAllevamentoDto> movimentazioni = objectMapper.readValue(responseListConsistenzaAllevamento, new TypeReference<List<ConsistenzaAllevamentoDto>>() {
		});
		ResponseEntity<List<ConsistenzaAllevamentoDto>> response = new ResponseEntity<List<ConsistenzaAllevamentoDto>>(movimentazioni, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq(new URI(resource)), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(new ParameterizedTypeReference<List<ConsistenzaAllevamentoDto>>() {
		}))).thenReturn(response);

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = serviceCalcoloMan.eseguiPasso(dati);

		assertEquals("DUT_003", res.getCodiceEsito());

	}

	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/comuniLimitrofi.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/calcoloDisaccoppiato/insertDatiTestMan.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	public void dut_004_pasf_016() throws Exception {

		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(180523));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = daoIstruttoria.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizione = transizioneIstruttoriaDao.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();

		List<String> pascoliRichiesti = Arrays.asList("AZIENDALE - P464");
		List<VariabileCalcolo> variabiliInput = initInputMan(pascoliRichiesti);

		// inserimento dati istruttore
		List<DatiIstruttoriaPascoli> datiIstruttoria = new ArrayList<>();
		DatiIstruttoriaPascoli datiIstruttoriaPascolo = new DatiIstruttoriaPascoli();
		datiIstruttoriaPascolo.setDescrizionePascolo("AZIENDALE - P464");
		datiIstruttoriaPascolo.setCuaaResponsabile("BNDCLD69S12C794E");
		datiIstruttoriaPascolo.setVerificaDocumentazione(DOC_PRESENTE);
		datiIstruttoria.add(datiIstruttoriaPascolo);
		//serviceDomande.salvaDatiIstruttoriaDomanda(transizione.getIstruttoria().getDomandaUnica().getId(), objectMapper.writeValueAsString(datiIstruttoria), TipoDatoIstruttoria.PASCOLI);
		datiIstruttoreService.saveOrUpdateDatiIstruttoriaPascoli(transizione.getIstruttoria().getId(), datiIstruttoria);

		// getConsistenzaAllevamentoDaCacheBdn
		String params = "{ \"annoCampagna\": 2018 , \"codiceFiscale\": \"BNDCLD69S12C794E\" }";
		String encoded = URLEncoder.encode(params, "UTF-8");
		String resource = configurazione.getUriCacheBdn().concat("consistenzaAllevamento?params=").concat(encoded);
		String responseListConsistenzaAllevamento = "[{\"idCaws\":6806,\"codiFiscSogg\":\"BNDCLD69S12C794E\",\"numeCamp\":2018,\"idAlleBdn\":2102604,\"codiSpec\":\"0121\",\"codiAsll\":\"555TN115\",\"codiFiscProp\":\"BNDCLD69S12C794E\",\"codiFiscDete\":\"BNDCLD69S12C794E\",\"consCapi06\":1,\"consCapi624\":14,\"consCapiOver24\":23,\"consTota\":38,\"consVaccOver20\":24,\"dataInizDete\":null,\"dataFineDete\":null,\"decoStat\":85,\"dataIniz\":\"2019-04-02\",\"dataFine\":\"9999-12-31\",\"dataAggi\":\"2019-05-24\",\"userName\":\"PROXY-BDN\",\"codiceComune\":\"555\"},{\"idCaws\":6807,\"codiFiscSogg\":\"BNDCLD69S12C794E\",\"numeCamp\":2018,\"idAlleBdn\":4119775,\"codiSpec\":\"0122\",\"codiAsll\":\"246TN115\",\"codiFiscProp\":\"BNDCLD69S12C794E\",\"codiFiscDete\":\"BNDCLD69S12C794E\",\"consCapi06\":0,\"consCapi624\":0,\"consCapiOver24\":0,\"consTota\":0,\"consVaccOver20\":0,\"dataInizDete\":null,\"dataFineDete\":null,\"decoStat\":85,\"dataIniz\":\"2019-04-02\",\"dataFine\":\"9999-12-31\",\"dataAggi\":\"2019-04-02\",\"userName\":\"PROXY-BDN\",\"codiceComune\":\"555\"}]";
		List<ConsistenzaAllevamentoDto> movimentazioni = objectMapper.readValue(responseListConsistenzaAllevamento, new TypeReference<List<ConsistenzaAllevamentoDto>>() {
		});
		ResponseEntity<List<ConsistenzaAllevamentoDto>> response = new ResponseEntity<List<ConsistenzaAllevamentoDto>>(movimentazioni, null, HttpStatus.OK);
		Mockito.when(restTemplate.exchange(Mockito.eq(new URI(resource)), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(new ParameterizedTypeReference<List<ConsistenzaAllevamentoDto>>() {
		}))).thenReturn(response);

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = serviceCalcoloMan.eseguiPasso(dati);

		assertEquals("DUT_004", res.getCodiceEsito());

	}

}
