package it.tndigitale.a4g.fascicolo.antimafia;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalMatchers.or;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.h2.tools.Server;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.hamcrest.text.IsEmptyString;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.fascicolo.antimafia.dto.AggiornaDichiarazioneEsito;
import it.tndigitale.a4g.fascicolo.antimafia.dto.AllegatoFamiliariConviventi;
import it.tndigitale.a4g.fascicolo.antimafia.dto.Azienda;
import it.tndigitale.a4g.fascicolo.antimafia.dto.DatiChiusuraExNovoDichiarazioneAntimafia;
import it.tndigitale.a4g.fascicolo.antimafia.dto.DatiDichiarazioneAntimafia;
import it.tndigitale.a4g.fascicolo.antimafia.dto.Dichiarazione;
import it.tndigitale.a4g.fascicolo.antimafia.dto.DichiarazioneFilter;
import it.tndigitale.a4g.fascicolo.antimafia.dto.Nota;
import it.tndigitale.a4g.fascicolo.antimafia.dto.NotaFilter;
import it.tndigitale.a4g.fascicolo.antimafia.dto.Richiedente;
import it.tndigitale.a4g.fascicolo.antimafia.dto.StatoDic;
import it.tndigitale.a4g.fascicolo.antimafia.dto.TipoNotaEnum;
import it.tndigitale.a4g.fascicolo.antimafia.dto.consultazione.Fascicolo;
import it.tndigitale.a4g.fascicolo.antimafia.dto.consultazione.ParamsRicercaFascicolo;
import it.tndigitale.a4g.fascicolo.antimafia.repository.dao.DichiarazioneAntimafiaDao;
import it.tndigitale.a4g.fascicolo.antimafia.repository.model.A4gtDichiarazioneAntimafia;
import it.tndigitale.a4g.fascicolo.antimafia.rest.ProxyClient;
import it.tndigitale.a4g.fascicolo.antimafia.service.AntimafiaServiceImpl;
import it.tndigitale.a4g.fascicolo.antimafia.service.EsitiAntimafiaService;
import it.tndigitale.a4g.fascicolo.antimafia.service.ext.ConsumeExternalRestApi4Proxy;
import it.tndigitale.a4g.framework.client.ClientServiceBuilder;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.proxy.client.model.AntimafiaEsitoDto;

@RunWith(SpringRunner.class)
@SpringBootTest
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"spring.h2.console.enabled=true"}) //attiva console h2 localmente
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class AntimafiaApplicationTests {

	static Server h2WebServer;
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");

	@BeforeClass
	public static void initTest() throws SQLException {
		h2WebServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082");
		h2WebServer.start();
	}


	@AfterClass
	public static void stopServer() throws SQLException {
		h2WebServer.stop();
	}




	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private RestTemplate restTemplate;
	@Value("${a4gfascicolo.integrazioni.verificafirma.uri}")
	private String urlIntegrazioniVerificaFirma;
	@Value("${a4gfascicolo.integrazioni.protocollo.documenti.uri}")
	private String urlIntegrazioniProtocolloDomanda;

	@Value("${it.tndigit.client.custom.protcollo.url}")
	private String urlIntegrazioniProtocolloDomandaClientCustom;


	@Value("${a4gfascicolo.integrazioni.sincronizzazione.antimafia.uri}")
	private String urlIntegrazioniSincronizzazioneAntimafia;
	@Value("${it.tndigit.security.utente.url}")
	private String uriA4gutente;
	@Value("${a4gfascicolo.ags.uri}")
	private String uriAgs;
	@MockBean
	private Clock clock; 

	@MockBean
	private ClientServiceBuilder clientServiceBuilder;
	@MockBean
	private ConsumeExternalRestApi4Proxy extProxy;

	@Autowired
	private EsitiAntimafiaService serviceEsitiAntimafia;

	@Autowired
	private DichiarazioneAntimafiaDao daoDichiarazioneAntimafia;

	@Autowired
	AntimafiaServiceImpl antimafiaService;

	@MockBean
	ProxyClient proxyClient;

	// In questo caso di test i valori relativi agli esiti sono null e testiamo se vengono valorizzati correttamente dal job
	@Test
	@Sql(scripts = {"/jobRecuperoEsiti/jobRecuperoEsitiSuccessful1_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = {"/jobRecuperoEsiti/jobRecuperoEsitiSuccessful1_delete.sql"}, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = {Ruoli.RICERCA_ANTIMAFIA_TUTTI_COD})
	public void jobRecuperoEsitiSuccessful1() throws Exception {
		AntimafiaEsitoDto esitoDto1 = new AntimafiaEsitoDto();
		esitoDto1.setEsitoTrasmissione("OK");
		esitoDto1.setDtElaborazione(LocalDateTime.of(2020, 06, 04, 00, 00));
		esitoDto1.setDtValidita(LocalDateTime.of(2020, 06, 01, 00, 00));
		esitoDto1.setEsitoInvioAgea("OK");
		esitoDto1.setEsitoInvioBdna("ND");
		esitoDto1.setDescrizioneEsito("AUTODICHIARAZIONE INSERITA IN AGEA");
		//        Mockito.when(extProxy.getEsitoAntimafia(ArgumentMatchers.eq("PCHNMR60D69F132C"))).thenReturn(esitoDto1);

		AntimafiaEsitoDto esitoDto2 = new AntimafiaEsitoDto();
		esitoDto2.setEsitoTrasmissione("KO");
		esitoDto2.setDtElaborazione(LocalDateTime.of(2020, 06, 04, 00, 00));
		esitoDto2.setDtValidita(LocalDateTime.of(2020, 06, 01, 00, 00));
		esitoDto2.setEsitoInvioAgea("KO");
		esitoDto2.setEsitoInvioBdna("ND");
		esitoDto2.setDescrizioneEsito("NATURA GIURIDICA NON CONGRUENTE - DICHIARANTE");
		//        Mockito.when(extProxy.getEsitoAntimafia(ArgumentMatchers.eq("02301480220"))).thenReturn(esitoDto2);
		esitoDto1.setCuaa("PCHNMR60D69F132C");
		esitoDto2.setCuaa("02301480220");
		List<AntimafiaEsitoDto> esitoDtoList = new ArrayList<AntimafiaEsitoDto>();        
		esitoDtoList.add(esitoDto1);
		esitoDtoList.add(esitoDto2);
		Mockito.when(extProxy.getEsitiAntimafia(ArgumentMatchers.anyList())).thenReturn(esitoDtoList);

		serviceEsitiAntimafia.jobRecuperoEsiti();

		A4gtDichiarazioneAntimafia dich1WithEsiti = daoDichiarazioneAntimafia.findById(961305L).get();

		assertEquals(esitoDto1.getEsitoTrasmissione(), dich1WithEsiti.getEsito());
		assertThat(esitoDto1.getDtElaborazione().isEqual(dich1WithEsiti.getEsitoDtElaborazione())).isTrue();
		assertEquals(esitoDto1.getDescrizioneEsito(), dich1WithEsiti.getEsitoDescrizione());
		assertEquals(esitoDto1.getEsitoInvioAgea(), dich1WithEsiti.getEsitoInvioAgea());
		assertEquals(esitoDto1.getEsitoInvioBdna(), dich1WithEsiti.getEsitoInvioBdna());

		A4gtDichiarazioneAntimafia dich2WithEsiti = daoDichiarazioneAntimafia.findById(961293L).get();

		assertEquals(esitoDto2.getEsitoTrasmissione(), dich2WithEsiti.getEsito());
		assertThat(esitoDto2.getDtElaborazione().isEqual(dich2WithEsiti.getEsitoDtElaborazione())).isTrue();
		assertEquals(esitoDto2.getDescrizioneEsito(), dich2WithEsiti.getEsitoDescrizione());
		assertEquals(esitoDto2.getEsitoInvioAgea(), dich2WithEsiti.getEsitoInvioAgea());
		assertEquals(esitoDto2.getEsitoInvioBdna(), dich2WithEsiti.getEsitoInvioBdna());
	}

	// In questo caso di test i valori relativi all'esito sono censiti e l'esito mockato ha la stessa data di elaborazione. Quindi verifichiamo che il sistema non aggiorni
	// i valori e che rimangano gli stessi. I dati dell'esito mockato  sono diversi rispetto quelli censiti per evidenziare come i dati già presenti non subiscano alcuna modifica.
	@Test
	@Sql(scripts = {"/jobRecuperoEsiti/jobRecuperoEsitiFail_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = {"/jobRecuperoEsiti/jobRecuperoEsitiFail_delete.sql"}, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = {Ruoli.RICERCA_ANTIMAFIA_TUTTI_COD})
	public void jobRecuperoEsitiFail() throws Exception {
		AntimafiaEsitoDto esitoDto = new AntimafiaEsitoDto();
		esitoDto.setEsitoTrasmissione("KO");
		esitoDto.setDtElaborazione(LocalDateTime.of(2020, 06, 04, 00, 00));
		esitoDto.setEsitoInvioAgea("KO");
		esitoDto.setEsitoInvioBdna("ND");
		esitoDto.setDescrizioneEsito("NATURA GIURIDICA NON CONGRUENTE - DICHIARANTE");
		//        Mockito.when(extProxy.getEsitoAntimafia(ArgumentMatchers.eq("PCHNMR60D69F132C"))).thenReturn(esitoDto);
		esitoDto.setCuaa("PCHNMR60D69F132C");
		List<AntimafiaEsitoDto> esitoDtoList = new ArrayList<AntimafiaEsitoDto>();
		esitoDtoList.add(esitoDto);
		Mockito.when(extProxy.getEsitiAntimafia(ArgumentMatchers.anyList())).thenReturn(esitoDtoList);

		serviceEsitiAntimafia.jobRecuperoEsiti();

		A4gtDichiarazioneAntimafia dich1WithEsiti = daoDichiarazioneAntimafia.findById(961305L).get();

		// Verifichiamo che i dati nell'sql non siano cambiati ma siano rimasti integri.
		assertEquals("OK", dich1WithEsiti.getEsito());
		assertThat(esitoDto.getDtElaborazione().isEqual(dich1WithEsiti.getEsitoDtElaborazione())).isTrue();
		assertEquals("AUTODICHIARAZIONE INSERITA IN AGEA", dich1WithEsiti.getEsitoDescrizione());
		assertEquals("OK", dich1WithEsiti.getEsitoInvioAgea());
		assertEquals("ND", dich1WithEsiti.getEsitoInvioBdna());
	}

	// In questo caso di test i valori relativi agli esiti non sono null, ma la data di elaborazione dell'esito mockato è diversa da quella che già è presente nella dichiarazione
	// e andiamo a verificare che il sistema capisca di dover aggiornare l'esito e lo effettui.
	@Test
	@Sql(scripts = {"/jobRecuperoEsiti/jobRecuperoEsitiSuccessful2_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = {"/jobRecuperoEsiti/jobRecuperoEsitiSuccessful2_delete.sql"}, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = {Ruoli.RICERCA_ANTIMAFIA_TUTTI_COD})
	public void jobRecuperoEsitiSuccesful2() throws Exception {
		AntimafiaEsitoDto esitoDto = new AntimafiaEsitoDto();
		esitoDto.setEsitoTrasmissione("KO");
		esitoDto.setDtElaborazione(LocalDateTime.of(2020, 06, 04, 00, 00));
		esitoDto.setEsitoInvioAgea("KO");
		esitoDto.setEsitoInvioBdna("ND");
		esitoDto.setDescrizioneEsito("NATURA GIURIDICA NON CONGRUENTE - DICHIARANTE");
		esitoDto.setDtValidita(LocalDateTime.of(2020, 06, 01, 00, 00));
		//        Mockito.when(extProxy.getEsitoAntimafia(ArgumentMatchers.eq("PCHNMR60D69F132C"))).thenReturn(esitoDto);
		esitoDto.setCuaa("PCHNMR60D69F132C");
		List<AntimafiaEsitoDto> esitoDtoList = new ArrayList<AntimafiaEsitoDto>();
		esitoDtoList.add(esitoDto);
		Mockito.when(extProxy.getEsitiAntimafia(ArgumentMatchers.anyList())).thenReturn(esitoDtoList);


		serviceEsitiAntimafia.jobRecuperoEsiti();

		A4gtDichiarazioneAntimafia dich1WithEsiti = daoDichiarazioneAntimafia.findById(961305L).get();

		// Verifichiamo che i dati nell'sql non siano cambiati ma siano rimasti integri.
		assertEquals("KO", dich1WithEsiti.getEsito());
		assertThat(esitoDto.getDtElaborazione().isEqual(dich1WithEsiti.getEsitoDtElaborazione())).isTrue();
		assertEquals("NATURA GIURIDICA NON CONGRUENTE - DICHIARANTE", dich1WithEsiti.getEsitoDescrizione());
		assertEquals("KO", dich1WithEsiti.getEsitoInvioAgea());
		assertEquals("ND", dich1WithEsiti.getEsitoInvioBdna());
	}

	@Test
	@WithMockUser(username = "utente", roles = {Ruoli.RICERCA_ANTIMAFIA_TUTTI_COD})
	public void getDichiarazioni() throws Exception {
		this.mockMvc.perform(get("/api/v1/antimafia").param("params", "{\"azienda\":{\"cuaa\":\"TRRCST78B08C794X\"}}")).andExpect(status().isOk());
		this.mockMvc.perform(get("/api/v1/antimafia").param("params", "{\"azienda\":{\"cuaa\":\"TRRCST78B08C794X\"},\"expand\":\"pdfFirmato\"}")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "utente", roles = {Ruoli.RICERCA_ANTIMAFIA_TUTTI_COD})
	public void getDichiarazioni2() throws Exception {
		this.mockMvc.perform(get("/api/v1/antimafia").param("params", "{\"stato\":{\"identificativo\":\"PROTOCOLLATA\"}}")).andExpect(status().isOk());
		this.mockMvc.perform(get("/api/v1/antimafia").param("params", "{\"stato\":{\"identificativo\":\"PROTOCOLLATA\"},\"expand\":\"pdfFirmato\"}")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "utente", roles = {Ruoli.RICERCA_ANTIMAFIA_TUTTI_COD})
	public void getDichiarazioniDecorator() throws Exception {
		String serviceGetEnti = uriA4gutente + A4gfascicoloConstants.ENTI_UTENTE;
		List<String> enti = Arrays.asList("18", "20", "4");
		ResponseEntity<List<String>> resUtente = new ResponseEntity<List<String>>(enti, null, HttpStatus.CREATED);
		Mockito.when(restTemplate.exchange(new URI(serviceGetEnti), HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {
		})).thenReturn(resUtente);
		String stringFascicoli = getFascicoliTRRCST78B08C794X();
		ParamsRicercaFascicolo filtro = new ParamsRicercaFascicolo();
		filtro.setCuaa("TRRCST78B08C794X");
		//filtro.setDenominazione("TORRESANI CRISTIAN");
		filtro.setCaacodici(enti);
		List<Fascicolo> fascioli = Arrays.asList(objectMapper.readValue(stringFascicoli, Fascicolo[].class));
		String params = objectMapper.writeValueAsString(filtro);
		String encoded = URLEncoder.encode(params, "UTF-8");
		String serviceGetFascicoliFull = uriAgs + "fascicoli/?params=" + encoded;
		ResponseEntity<List<Fascicolo>> resAgs = new ResponseEntity<List<Fascicolo>>(fascioli, null, HttpStatus.CREATED);
		Mockito.when(restTemplate.exchange(new URI(serviceGetFascicoliFull), HttpMethod.GET, null, new ParameterizedTypeReference<List<Fascicolo>>() {
		})).thenReturn(resAgs);

		this.mockMvc.perform(get("/api/v1/antimafia").param("params", "{\"filtroUtenteEnte\":true,\"statiDichiarazione\":[\"RIFIUTATA\",\"VERIFICA_PERIODICA\"]}")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "utente", roles = {Ruoli.RICERCA_ANTIMAFIA_TUTTI_COD})
	public void getDichiarazioniInesistente() throws Exception {
		this.mockMvc.perform(get("/api/v1/antimafia").param("params", "{\"azienda\":{\"cuaa\":\"LBRMRA57T19L378D\"}}")).andExpect(status().is2xxSuccessful()).andExpect(content().string("[]"));
	}

	@Test
	@WithMockUser(username = "utente", roles = {Ruoli.RICERCA_ANTIMAFIA_TUTTI_COD})
	public void getDichiarazione() throws Exception {
		this.mockMvc.perform(get("/api/v1/antimafia/32")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value("32")).andExpect(jsonPath("$.azienda.cuaa").value("01833620220"));
		this.mockMvc.perform(get("/api/v1/antimafia/40?expand=pdfFirmato")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value("40"))
		.andExpect(jsonPath("$.azienda.cuaa").value("01833620220"));
	}

	@Test
	@WithMockUser(username = "utente", roles = {Ruoli.EDITA_ANTIMAFIA_TUTTI_COD})
	public void creaDichiarazione() throws Exception {
		// Servizio anagrafica impresa PARIX
		String serviceUrlRicercaImpresePerCF = "http://localhost:8080/a4gproxy/api/v1/anagraficaimpresa/TRRCST78B08C794X";
		String serviceUrlRicercaImpresePerCFaziendaCollegata = "http://localhost:8080/a4gproxy/api/v1/anagraficaimpresa/00598990224";
		Mockito.when(restTemplate.getForObject(or(Mockito.eq(new URI(serviceUrlRicercaImpresePerCF)), Mockito.eq(new URI(serviceUrlRicercaImpresePerCFaziendaCollegata))), // Use
				// anyString()
				Mockito.eq(String.class))) // Use anyString()
		.thenReturn(getResponseRicercaImpresePerCF());
		// Servizio dettaglioCompleto impresa PARIX
		String serviceUrlDettaglioCompletoImpresa = "{\"provinciaSede\":\"TN\",\"numeroREASede\":\"173710\"}";
		String serviceUrlDettaglioCompletoImpresaEncoded = "http://localhost:8080/a4gproxy/api/v1/anagraficaimpresa/dettagliocompleto/?params="
				+ URLEncoder.encode(serviceUrlDettaglioCompletoImpresa, "UTF-8");
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlDettaglioCompletoImpresaEncoded)), // Use
				// anyString()
				Mockito.eq(String.class))) // Use anyString()
		.thenReturn(getResponseDettaglioCompletoImpresa());
		// Servizio anagrafica persona AnagrafeTributaria
		String serviceUrlAnagrafeTributariaPerCF = "http://localhost:8080/a4gproxy/api/v1/anagrafetributaria/BZZPLA64A31A178M";
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlAnagrafeTributariaPerCF)), // Use
				// anyString()
				Mockito.eq(String.class))) // Use anyString()
		.thenReturn(getResponseAnagrafeTributariaPerCF());

		Dichiarazione dichiarazione = new Dichiarazione();
		dichiarazione.setId(100L);
		Azienda azienda = new Azienda();
		azienda.setCuaa("TRRCST78B08C794X");
		dichiarazione.setAzienda(azienda);
		dichiarazione.setDtUltimoAggiornamento(new Date(java.util.Calendar.getInstance().getTimeInMillis()));
		dichiarazione.setDtInizioCompilazione(new Date(java.util.Calendar.getInstance().getTimeInMillis()));
		dichiarazione.setDatiDichiarazione(new DatiDichiarazioneAntimafia());
		dichiarazione.getDatiDichiarazione().setRichiedente(new Richiedente());
		dichiarazione.getDatiDichiarazione().getRichiedente().setCodiceFiscale("BZZPLA64A31A178M");
		this.mockMvc.perform(post("/api/v1/antimafia").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dichiarazione))).andExpect(status().isOk())
		.andExpect(content().string(IsNull.notNullValue()));
	}

	// * TESTCASE 1: anagrafica impresa KO && anagrafica impresa dettaglio completo KO
	@Test
	@WithMockUser(username = "utente", roles = {Ruoli.EDITA_ANTIMAFIA_TUTTI_COD})
	public void creaDichiarazioneEsitoNegativoTestCase1() throws Exception {

		// mock anagrafica impresa KO
		String impresaCessataCF = "MSRDRN61S24L378V";
		String serviceUrlRicercaImpresePerCF = "http://localhost:8080/a4gproxy/api/v1/anagraficaimpresa/".concat(impresaCessataCF);
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlRicercaImpresePerCF)), Mockito.eq(String.class))).thenReturn(getResponseDettaglioCompletoImpresaEsitoNegativo());

		// mock anagrafica impresa dettaglio completo KO
		String params = "{\"provinciaSede\":\"TN\",\"numeroREASede\":\"XXXXX\"}";
		String serviceUrlRicercaImpresePerCFDettaglioCompleto = "http://localhost:8080/a4gproxy/api/v1/anagraficaimpresa/dettagliocompleto/".concat("?params=")
				.concat(URLEncoder.encode(params, "UTF-8"));
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlRicercaImpresePerCFDettaglioCompleto)), Mockito.eq(String.class)))
		.thenReturn(getResponseRicercaImpresePerCFEsitoNegativo());

		// crea un oggetto dichiarazione
		Dichiarazione dichiarazione = new Dichiarazione();
		dichiarazione.setId(99L);
		Azienda azienda = new Azienda();
		azienda.setCuaa(impresaCessataCF);
		dichiarazione.setAzienda(azienda);
		dichiarazione.setDtUltimoAggiornamento(new Date(java.util.Calendar.getInstance().getTimeInMillis()));
		dichiarazione.setDtInizioCompilazione(new Date(java.util.Calendar.getInstance().getTimeInMillis()));
		dichiarazione.setDatiDichiarazione(new DatiDichiarazioneAntimafia());
		dichiarazione.getDatiDichiarazione().setRichiedente(new Richiedente());
		dichiarazione.getDatiDichiarazione().getRichiedente().setCodiceFiscale(impresaCessataCF);
		// perform post crea dichiarazione
		this.mockMvc.perform(post("/api/v1/antimafia").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dichiarazione))).andExpect(status().is5xxServerError())
		.andExpect(content().string(containsString("L'impresa ".concat(dichiarazione.getAzienda().getCuaa()).concat(" non risulta attiva presso la Camera di Commercio"))));
	}

	// * TESTCASE 2: anagrafica impresa OK && anagrafica impresa dettaglio completo KO
	@Test
	@WithMockUser(username = "utente", roles = {Ruoli.EDITA_ANTIMAFIA_TUTTI_COD})
	public void creaDichiarazioneEsitoNegativoTestCase2() throws Exception {
		String impresaCessataCF = "TRRCST78B08C794X";
		// crea un oggetto dichiarazione
		Dichiarazione dichiarazione = new Dichiarazione();
		dichiarazione.setId(99L);
		Azienda azienda = new Azienda();
		azienda.setCuaa(impresaCessataCF);
		dichiarazione.setAzienda(azienda);
		dichiarazione.setDtUltimoAggiornamento(new Date(java.util.Calendar.getInstance().getTimeInMillis()));
		dichiarazione.setDtInizioCompilazione(new Date(java.util.Calendar.getInstance().getTimeInMillis()));
		dichiarazione.setDatiDichiarazione(new DatiDichiarazioneAntimafia());
		dichiarazione.getDatiDichiarazione().setRichiedente(new Richiedente());
		dichiarazione.getDatiDichiarazione().getRichiedente().setCodiceFiscale(impresaCessataCF);

		// anagrafica impresa OK
		String serviceUrlRicercaImpresePerCF = "http://localhost:8080/a4gproxy/api/v1/anagraficaimpresa/".concat(impresaCessataCF);
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlRicercaImpresePerCF)), Mockito.eq(String.class))).thenReturn(getResponseRicercaImpresePerCF());
		// anagrafica impresa dettaglio completo KO
		String params = "{\"provinciaSede\":\"TN\",\"numeroREASede\":\"173710\"}";
		String serviceUrlRicercaImpresePerCFDettaglioCompleto = "http://localhost:8080/a4gproxy/api/v1/anagraficaimpresa/dettagliocompleto/".concat("?params=")
				.concat(URLEncoder.encode(params, "UTF-8"));
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlRicercaImpresePerCFDettaglioCompleto)), Mockito.eq(String.class)))
		.thenReturn(getResponseDettaglioCompletoImpresaEsitoNegativo());
		// perform crea dichiarazione
		this.mockMvc.perform(post("/api/v1/antimafia").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dichiarazione))).andExpect(status().is5xxServerError())
		.andExpect(content().string(containsString("L'impresa ".concat(dichiarazione.getAzienda().getCuaa()).concat(" non risulta attiva presso la Camera di Commercio"))));
	}

	// * TESTCASE 3: soggetto che non è titolato a creare una domanda antimafia (BR-CUAA-PARIX-01)
	@Test
	@WithMockUser(username = "utente", roles = {Ruoli.EDITA_ANTIMAFIA_TUTTI_COD})
	public void creaDichiarazioneEsitoNegativoTestCase3() throws Exception {
		String cfRichiedente = "TRRCST78B08C794X";
		// anagrafica impresa OK && anagrafica impresa dettaglio completo OK
		String serviceUrlRicercaImpresePerCF = "http://localhost:8080/a4gproxy/api/v1/anagraficaimpresa/".concat(cfRichiedente);
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlRicercaImpresePerCF)), Mockito.eq(String.class))).thenReturn(getResponseRicercaImpresePerCF());

		String serviceUrlDettaglioCompletoImpresa = "{\"provinciaSede\":\"TN\",\"numeroREASede\":\"173710\"}";
		String serviceUrlDettaglioCompletoImpresaEncoded = "http://localhost:8080/a4gproxy/api/v1/anagraficaimpresa/dettagliocompleto/?params="
				+ URLEncoder.encode(serviceUrlDettaglioCompletoImpresa, "UTF-8");
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlDettaglioCompletoImpresaEncoded)), Mockito.eq(String.class)))
		.thenReturn(getResponseDettaglioCompletoImpresaEsitoNegativoCariche());

		// istanzia oggetto dichiarazione
		Dichiarazione dichiarazione = new Dichiarazione();
		dichiarazione.setId(99L);
		Azienda azienda = new Azienda();
		azienda.setCuaa(cfRichiedente);
		dichiarazione.setAzienda(azienda);
		dichiarazione.setDtUltimoAggiornamento(new Date(java.util.Calendar.getInstance().getTimeInMillis()));
		dichiarazione.setDtInizioCompilazione(new Date(java.util.Calendar.getInstance().getTimeInMillis()));
		dichiarazione.setDatiDichiarazione(new DatiDichiarazioneAntimafia());
		dichiarazione.getDatiDichiarazione().setRichiedente(new Richiedente());
		dichiarazione.getDatiDichiarazione().getRichiedente().setCodiceFiscale(cfRichiedente);

		// perform crea dichiarazione
		this.mockMvc.perform(post("/api/v1/antimafia").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dichiarazione))).andExpect(status().is5xxServerError())
		.andExpect(content().string(containsString("Il Soggetto ".concat(cfRichiedente).concat(" non ricopre il ruolo indicato per l'impresa"))));

	}

	@Test
	@WithMockUser(username = "utente", roles = {Ruoli.EDITA_ANTIMAFIA_TUTTI_COD})
	public void creaDichiarazioneAnagraficaEsitoNegativo() throws Exception {
		// Servizio anagrafica impresa PARIX
		String serviceUrlRicercaImpresePerCF = "http://localhost:8080/a4gproxy/api/v1/anagraficaimpresa/TRRCST78B08C794X";
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlRicercaImpresePerCF)), // Use
				// anyString()
				Mockito.eq(String.class))) // Use anyString()
		.thenReturn(getResponseRicercaImpresePerCF());
		// Servizio dettaglioCompleto impresa PARIX
		String serviceUrlDettaglioCompletoImpresa = "{\"provinciaSede\":\"TN\",\"numeroREASede\":\"173710\"}";
		String serviceUrlDettaglioCompletoImpresaEncoded = "http://localhost:8080/a4gproxy/api/v1/anagraficaimpresa/dettagliocompleto/?params="
				+ URLEncoder.encode(serviceUrlDettaglioCompletoImpresa, "UTF-8");
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlDettaglioCompletoImpresaEncoded)), // Use
				// anyString()
				Mockito.eq(String.class))) // Use anyString()
		.thenReturn(getResponseDettaglioCompletoImpresa());
		// Servizio anagrafica persona AnagrafeTributaria
		String serviceUrlAnagrafeTributariaPerCF = "http://localhost:8080/a4gproxy/api/v1/anagrafetributaria/BZZPLA64A31A178M";
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlAnagrafeTributariaPerCF)), // Use
				// anyString()
				Mockito.eq(String.class))) // Use anyString()
		.thenReturn(getResponseAnagrafeTributariaEsitoNegativoPerCF());

		Dichiarazione dichiarazione = new Dichiarazione();
		dichiarazione.setId(100L);
		Azienda azienda = new Azienda();
		azienda.setCuaa("TRRCST78B08C794X");
		dichiarazione.setAzienda(azienda);
		// dichiarazione.setStato(stato);
		dichiarazione.setDtUltimoAggiornamento(new Date(java.util.Calendar.getInstance().getTimeInMillis()));
		dichiarazione.setDtInizioCompilazione(new Date(java.util.Calendar.getInstance().getTimeInMillis()));
		dichiarazione.setDatiDichiarazione(new DatiDichiarazioneAntimafia());
		dichiarazione.getDatiDichiarazione().setRichiedente(new Richiedente());
		dichiarazione.getDatiDichiarazione().getRichiedente().setCodiceFiscale("BZZPLA64A31A178M");
		this.mockMvc.perform(post("/api/v1/antimafia").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dichiarazione))).andExpect(status().is5xxServerError());
	}

	@Test
	@WithMockUser(username = "utente", roles = {Ruoli.EDITA_ANTIMAFIA_TUTTI_COD})
	public void aggiornaDichiarazione() throws Exception {

		Dichiarazione dichiarazione = new Dichiarazione();
		Azienda azienda = new Azienda();
		azienda.setCuaa("TRRCST78B08C794M");
		StatoDic stato = new StatoDic();
		stato.setIdentificativo(StatoDichiarazioneEnum.CHIUSA.getIdentificativoStato());
		dichiarazione.setAzienda(azienda);
		dichiarazione.setStato(stato);
		dichiarazione.setDtUltimoAggiornamento(new Date(java.util.Calendar.getInstance().getTimeInMillis()));
		dichiarazione.setDtInizioCompilazione(new Date(java.util.Calendar.getInstance().getTimeInMillis()));
		dichiarazione.setIdProtocollo("PROTOCOLLO 32");
		dichiarazione.setDatiDichiarazione(new DatiDichiarazioneAntimafia());
		dichiarazione.getDatiDichiarazione().setRichiedente(new Richiedente());
		dichiarazione.getDatiDichiarazione().getRichiedente().setCodiceFiscale("BZZPLA64A31A178X");

		this.mockMvc.perform(put("/api/v1/antimafia/32").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dichiarazione))).andExpect(status().isOk())
		.andExpect(jsonPath("dichiarazione", IsNull.notNullValue())).andExpect(jsonPath("dichiarazione.id", is(32)))
		.andExpect(content().string(containsString(StatoDichiarazioneEnum.CHIUSA.getIdentificativoStato()))).andReturn();

	}

	@Test
	@WithMockUser(username = "utente", roles = {Ruoli.EDITA_ANTIMAFIA_TUTTI_COD})
	public void aggiornaDichiarazioneConPdfFirmatoPositivo() throws Exception {
		String fileName = "MODULI_AMF.pdf.p7m";
		Path filePdfPath = Paths.get("./src/test/resources/uploadFile/" + fileName);

		Dichiarazione dichiarazione = new Dichiarazione();
		Azienda azienda = new Azienda();
		azienda.setCuaa("TRRCST78B08C794M");
		dichiarazione.setAzienda(azienda);
		// dichiarazione.setStato(stato);
		dichiarazione.setDtUltimoAggiornamento(new Date(java.util.Calendar.getInstance().getTimeInMillis()));
		dichiarazione.setDtInizioCompilazione(new Date(java.util.Calendar.getInstance().getTimeInMillis()));
		dichiarazione.setDatiDichiarazione(new DatiDichiarazioneAntimafia());
		dichiarazione.getDatiDichiarazione().setRichiedente(new Richiedente());
		dichiarazione.getDatiDichiarazione().getRichiedente().setCodiceFiscale("BZZPLA64A31A178X");
		dichiarazione.setPdfFirmato(Files.readAllBytes(filePdfPath));
		dichiarazione.setPdfFirmatoName(fileName);

		ByteArrayResource byteAsResource = new ByteArrayResource(dichiarazione.getPdfFirmato()) {
			@Override
			public String getFilename() {
				return fileName;
			}
		};

		MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
		bodyMap.add("documentoFirmato", byteAsResource);// new FileSystemResource(convFile));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);

		Mockito.when(restTemplate.exchange(Mockito.eq(urlIntegrazioniVerificaFirma), Mockito.eq(HttpMethod.POST), Mockito.eq(requestEntity), Mockito.eq(String.class)))
		.thenReturn(getResponseVerificaFirmaEsitoPositivo());

		this.mockMvc.perform(put("/api/v1/antimafia/32").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dichiarazione))).andExpect(status().isOk())
		.andExpect(jsonPath("dichiarazione", IsNull.notNullValue())).andExpect(jsonPath("dichiarazione.id", is(32))).andExpect(jsonPath("esito", IsNull.nullValue())).andReturn();

	}

	@Test
	@WithMockUser(username = "utente", roles = {Ruoli.EDITA_ANTIMAFIA_TUTTI_COD})
	public void aggiornaDichiarazioneConPdfFirmatoNegativo() throws Exception {
		String fileName = "dichiarazioneFamigliariConviventi.pdf";
		Path filePdfPath = Paths.get("./src/test/resources/uploadFile/" + fileName);

		Dichiarazione dichiarazione = new Dichiarazione();
		Azienda azienda = new Azienda();
		azienda.setCuaa("TRRCST78B08C794M");
		dichiarazione.setAzienda(azienda);
		// dichiarazione.setStato(stato);
		dichiarazione.setDtUltimoAggiornamento(new Date(java.util.Calendar.getInstance().getTimeInMillis()));
		dichiarazione.setDtInizioCompilazione(new Date(java.util.Calendar.getInstance().getTimeInMillis()));
		dichiarazione.setDatiDichiarazione(new DatiDichiarazioneAntimafia());
		dichiarazione.getDatiDichiarazione().setRichiedente(new Richiedente());
		dichiarazione.getDatiDichiarazione().getRichiedente().setCodiceFiscale("BZZPLA64A31A178X");
		dichiarazione.setPdfFirmatoName(fileName);
		dichiarazione.setPdfFirmato(Files.readAllBytes(filePdfPath));

		ByteArrayResource byteAsResource = new ByteArrayResource(dichiarazione.getPdfFirmato()) {
			@Override
			public String getFilename() {
				return fileName;
			}
		};

		MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
		bodyMap.add("documentoFirmato", byteAsResource);// new FileSystemResource(convFile));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);

		Mockito.when(restTemplate.exchange(Mockito.eq(urlIntegrazioniVerificaFirma), Mockito.eq(HttpMethod.POST), Mockito.eq(requestEntity), Mockito.eq(String.class)))
		.thenReturn(getResponseVerificaFirmaEsitoNegativo());

		this.mockMvc.perform(put("/api/v1/antimafia/32").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dichiarazione))).andExpect(status().isOk())
		.andExpect(jsonPath("dichiarazione", IsNull.nullValue())).andExpect(jsonPath("esito", IsNull.notNullValue())).andReturn();

	}

	@Test
	@WithMockUser(username = "utente", roles = {Ruoli.EDITA_ANTIMAFIA_TUTTI_COD})
	public void allegaFamiliariConviventiConPdfConFirmaAutografa() throws Exception {
		String fileName = "dichiarazioneFamigliariConviventi.pdf";
		Path filePdfPath = Paths.get("./src/test/resources/uploadFile/" + fileName);

		AllegatoFamiliariConviventi allegatoFamiliariConviventi = new AllegatoFamiliariConviventi();
		allegatoFamiliariConviventi.setCfSoggettoImpresa("TRRCST78B08C794X");
		allegatoFamiliariConviventi.setCodCarica("TIT");
		allegatoFamiliariConviventi.setTipoFile("pdf");
		allegatoFamiliariConviventi.setDtPdfDichFamConv(new java.util.Date());
		allegatoFamiliariConviventi.setFirmaDigitale(false);

		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/api/v1/antimafia/35/allegatoFamiliariConviventi");
		builder.with(request -> {
			request.setMethod(HttpMethod.PUT.name());
			return request;
		});

		MockMultipartFile documento = new MockMultipartFile("documento", Files.readAllBytes(filePdfPath));
		MockMultipartFile allegatoFamiliariConviventiJsonPart = new MockMultipartFile("datiFamiliareConvivente", "datiFamiliareConvivente", "application/json",
				objectMapper.writeValueAsString(allegatoFamiliariConviventi).getBytes(StandardCharsets.UTF_8));

		this.mockMvc.perform(builder.file(allegatoFamiliariConviventiJsonPart).file(documento)).andExpect(status().isOk()).andExpect(jsonPath("allegatoFamiliariConviventi", IsNull.notNullValue()))
		.andExpect(jsonPath("carica", IsNull.notNullValue())).andExpect(jsonPath("esito", IsNull.nullValue()));

	}


	@Test
	@WithMockUser(username = "utente", roles = {Ruoli.EDITA_ANTIMAFIA_TUTTI_COD})
	public void allegaFamiliariConviventiConPdfConFirmaDigitalePositiva() throws Exception {
		String fileName = "MODULI_AMF.pdf.p7m";
		Path filePdfPath = Paths.get("./src/test/resources/uploadFile/" + fileName);

		AllegatoFamiliariConviventi allegatoFamiliariConviventi = new AllegatoFamiliariConviventi();
		allegatoFamiliariConviventi.setCfSoggettoImpresa("TRRCST78B08C794X");
		allegatoFamiliariConviventi.setCodCarica("TIT");
		allegatoFamiliariConviventi.setTipoFile("pdf.p7m");
		allegatoFamiliariConviventi.setDtPdfDichFamConv(new java.util.Date());
		allegatoFamiliariConviventi.setFirmaDigitale(true);

		ByteArrayResource byteAsResource = new ByteArrayResource(Files.readAllBytes(filePdfPath)) {
			@Override
			public String getFilename() {
				return fileName;
			}
		};

		MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
		bodyMap.add("documentoFirmato", byteAsResource);// new FileSystemResource(convFile));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);

		Mockito.when(restTemplate.exchange(Mockito.eq(urlIntegrazioniVerificaFirma), Mockito.eq(HttpMethod.POST), Mockito.eq(requestEntity), Mockito.eq(String.class)))
		.thenReturn(getResponseVerificaFirmaEsitoPositivo());

		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/api/v1/antimafia/35/allegatoFamiliariConviventi");

		builder.with(request -> {
			request.setMethod(HttpMethod.PUT.name());
			return request;
		});

		MockMultipartFile documento = new MockMultipartFile("documento", fileName, "", Files.readAllBytes(filePdfPath));
		MockMultipartFile allegatoFamiliariConviventiJsonPart = new MockMultipartFile("datiFamiliareConvivente", "datiFamiliareConvivente", "application/json",
				objectMapper.writeValueAsString(allegatoFamiliariConviventi).getBytes(StandardCharsets.UTF_8));

		this.mockMvc.perform(builder.file(allegatoFamiliariConviventiJsonPart).file(documento)).andExpect(status().isOk())
		.andExpect(jsonPath("allegatoFamiliariConviventi.dtPdfDicFamConv", IsNull.notNullValue())).andExpect(jsonPath("carica.href", IsNull.notNullValue()))
		.andExpect(jsonPath("esito", IsNull.nullValue())).andReturn();

	}

	@Test
	@WithMockUser(username = "utente", roles = {Ruoli.EDITA_ANTIMAFIA_TUTTI_COD})
	public void allegaFamiliariConviventiConPdfConFirmaDigitaleNegativa() throws Exception {
		String fileName = "dichiarazioneFamigliariConviventi.pdf";
		Path filePdfPath = Paths.get("./src/test/resources/uploadFile/" + fileName);

		AllegatoFamiliariConviventi allegatoFamiliariConviventi = new AllegatoFamiliariConviventi();
		allegatoFamiliariConviventi.setCfSoggettoImpresa("TRRCST78B08C794X");
		allegatoFamiliariConviventi.setCodCarica("TIT");
		allegatoFamiliariConviventi.setTipoFile("pdf");
		allegatoFamiliariConviventi.setDtPdfDichFamConv(new java.util.Date());
		allegatoFamiliariConviventi.setFirmaDigitale(true);

		ByteArrayResource byteAsResource = new ByteArrayResource(Files.readAllBytes(filePdfPath)) {
			@Override
			public String getFilename() {
				return fileName;
			}
		};

		MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
		bodyMap.add("documentoFirmato", byteAsResource);// new FileSystemResource(convFile));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);

		Mockito.when(restTemplate.exchange(Mockito.eq(urlIntegrazioniVerificaFirma), Mockito.eq(HttpMethod.POST), Mockito.eq(requestEntity), Mockito.eq(String.class)))
		.thenReturn(getResponseVerificaFirmaEsitoNegativo());

		MockMultipartFile documento = new MockMultipartFile("documento", fileName, "", Files.readAllBytes(filePdfPath));
		MockMultipartFile allegatoFamiliariConviventiJsonPart = new MockMultipartFile("datiFamiliareConvivente", "datiFamiliareConvivente", "application/json",
				objectMapper.writeValueAsString(allegatoFamiliariConviventi).getBytes(StandardCharsets.UTF_8));

		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/api/v1/antimafia/35/allegatoFamiliariConviventi");

		builder.with(request -> {
			request.setMethod(HttpMethod.PUT.name());
			return request;
		});

		this.mockMvc.perform(builder.file(allegatoFamiliariConviventiJsonPart).file(documento)).andExpect(status().isOk()).andExpect(jsonPath("allegatoFamiliariConviventi", IsNull.nullValue()))
		.andExpect(jsonPath("carica", IsNull.nullValue())).andExpect(jsonPath("esito", IsNull.notNullValue())).andReturn();

	}

	@Test
	@WithMockUser(username = "utente", roles = {Ruoli.EDITA_ANTIMAFIA_TUTTI_COD})
	public void aggiungiNotaDichiarazione() throws Exception {
		Nota nota = new Nota();
		nota.setDataInserimento(new java.util.Date());
		nota.setNota("testo nota esempio");
		nota.setTipoNota(TipoNotaEnum.RIFIUTO_DICHIARAZIONE_ANTIMAFIA);
		this.mockMvc.perform(put("/api/v1/antimafia/32/note").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(nota))).andExpect(status().isOk())
		.andExpect(content().string(IsNull.notNullValue())).andReturn();

	}

	@Test
	@WithMockUser(username = "utente", roles = {Ruoli.RICERCA_ANTIMAFIA_TUTTI_COD})
	public void leggiNoteDichiarazione() throws Exception {
		NotaFilter notaFilter = new NotaFilter();
		notaFilter.setTipoNota(TipoNotaEnum.RIFIUTO_DICHIARAZIONE_ANTIMAFIA);
		this.mockMvc.perform(get("/api/v1/antimafia/26/note").param("params", objectMapper.writeValueAsString(notaFilter))).andExpect(status().isOk())
		.andExpect(content().string(IsNull.notNullValue())).andReturn();
	}

	@Test
	@WithMockUser(username = "utente", roles = {Ruoli.RICERCA_ANTIMAFIA_TUTTI_COD})
	public void leggiNoteDichiarazioneInesistenti() throws Exception {
		NotaFilter notaFilter = new NotaFilter();
		notaFilter.setTipoNota(TipoNotaEnum.RIFIUTO_DICHIARAZIONE_ANTIMAFIA);
		this.mockMvc.perform(get("/api/v1/antimafia/12589654/note").param("params", objectMapper.writeValueAsString(notaFilter))).andExpect(status().isNoContent()).andReturn();
	}

	@Test
	@WithMockUser(username = "utente", roles = {Ruoli.CANCELLA_ANTIMAFIA_TUTTI_COD})
	public void eliminaDichiarazioneEsitoNegativo() throws Exception {
		this.mockMvc.perform(delete("/api/v1/antimafia/666")).andExpect(status().is5xxServerError());
	}

	@Test
	@WithMockUser(username = "utente", roles = {Ruoli.CANCELLA_ANTIMAFIA_TUTTI_COD})
	public void eliminaDichiarazioneCheckStato() throws Exception {
		this.mockMvc.perform(delete("/api/v1/antimafia/26")).andExpect(status().is5xxServerError());
	}

	@Test
	@WithMockUser(username = "utente", roles = {Ruoli.CANCELLA_ANTIMAFIA_TUTTI_COD})
	public void eliminaDichiarazione() throws Exception {
		this.mockMvc.perform(delete("/api/v1/antimafia/25")).andExpect(status().isOk()).andExpect(content().string("25"));
	}

	@Test
	@WithMockUser(username = "utente", roles = {Ruoli.CANCELLA_ANTIMAFIA_TUTTI_COD + "Altro"})
	public void eliminaDichiarazioneSenzaRuoloKO() throws Exception {
		this.mockMvc.perform(delete("/api/v1/antimafia/25")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "utente", roles = {Ruoli.EDITA_ANTIMAFIA_TUTTI_COD})
	public void protocolla() throws Exception {
		ResponseEntity<String> response = new ResponseEntity<>("1235412", HttpStatus.ACCEPTED);

		Mockito.when(restTemplate.exchange(Mockito.eq(urlIntegrazioniProtocolloDomandaClientCustom), Mockito.eq(HttpMethod.POST), Mockito.any(), Mockito.eq(String.class))).thenReturn(response);
		Mockito.when(restTemplate.exchange(Mockito.eq(urlIntegrazioniSincronizzazioneAntimafia), Mockito.eq(HttpMethod.POST), Mockito.any(), Mockito.eq(String.class))).thenReturn(null);
		ResultActions resultActions = this.mockMvc.perform(put("/api/v1/antimafia/37/protocolla"));

		//        resultActions.andDo(print()).andExpect(status().isOk())
		resultActions.andExpect(status().isOk())
		.andExpect(jsonPath("stato.identificativo", IsEqual.equalTo(A4gfascicoloConstants.STATO_PROTOCOLLATA)));

	}

	@Test
	@WithMockUser("utente")
	public void countStatoDichiarazioni() throws Exception {
		this.mockMvc.perform(get("/api/v1/antimafia/count").param("params", "{\"statiDichiarazione\":[\"PROTOCOLLATA\",\"BOZZA\",\"PROVA\"]}")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "utente", roles = {Ruoli.RICERCA_ANTIMAFIA_TUTTI_COD})
	public void getAllegatoFamiliariConviventi() throws Exception {
		this.mockMvc.perform(get("/api/v1/antimafia/37/allegatoFamiliariConviventi/13")).andExpect(status().isOk()).andExpect(content().string(IsNull.notNullValue()));
	}

	@Test
	@WithMockUser(username = "utente", roles = {Ruoli.RICERCA_ANTIMAFIA_TUTTI_COD})
	public void getAllegatoFamiliariConviventiNoAllegato() throws Exception {
		this.mockMvc.perform(get("/api/v1/antimafia/37/allegatoFamiliariConviventi/20000000")).andExpect(status().isOk()).andExpect(content().string(IsEmptyString.isEmptyString()));
	}

	@Test
	@WithMockUser(username = "utente", roles = {Ruoli.RICERCA_ANTIMAFIA_TUTTI_COD})
	public void getAllegatoFamiliariConviventiNoAllegati() throws Exception {
		this.mockMvc.perform(get("/api/v1/antimafia/26/allegatoFamiliariConviventi/13")).andExpect(status().isOk()).andExpect(content().string(IsEmptyString.isEmptyString()));
	}

	@Test
	@WithMockUser(username = "utente", roles = {Ruoli.EDITA_ANTIMAFIA_TUTTI_COD, Ruoli.RICERCA_ANTIMAFIA_TUTTI_COD})
	public void testInserimentoProcedimenti() throws Exception {
		String procedimenti = "[\"DU\"]";
		this.mockMvc.perform(post("/api/v1/antimafia/58/procedimenti").contentType(MediaType.APPLICATION_JSON).content(procedimenti))
		.andExpect(status().isOk())
		.andExpect(content().string(IsNull.notNullValue()));
		this.mockMvc.perform(get("/api/v1/antimafia/58/procedimenti"))
		.andExpect(status().isOk())
		.andExpect(content().string(IsEqual.equalTo(procedimenti)));
	}

	@Test
	@WithMockUser(username = "utente", roles = {Ruoli.EDITA_ANTIMAFIA_TUTTI_COD, Ruoli.RICERCA_ANTIMAFIA_TUTTI_COD})
	public void testInserimentoEModificaProcedimenti() throws Exception {
		String procedimenti = "[\"DU\"]";
		this.mockMvc.perform(post("/api/v1/antimafia/58/procedimenti").contentType(MediaType.APPLICATION_JSON).content(procedimenti))
		.andExpect(status().isOk())
		.andExpect(content().string(IsNull.notNullValue()));
		this.mockMvc.perform(get("/api/v1/antimafia/58/procedimenti"))
		.andExpect(status().isOk())
		.andExpect(content().string(IsEqual.equalTo(procedimenti)));
		//modifica
		procedimenti = "[\"PSR_INVESTIMENTO\",\"PAI\"]";
		this.mockMvc.perform(post("/api/v1/antimafia/58/procedimenti").contentType(MediaType.APPLICATION_JSON).content(procedimenti))
		.andExpect(status().isOk())
		.andExpect(content().string(IsNull.notNullValue()));
		this.mockMvc.perform(get("/api/v1/antimafia/58/procedimenti"))
		//.andDo(MockMvcResultHandlers.print())
		.andExpect(status().isOk())
		.andExpect(content().string(IsEqual.equalTo(procedimenti)));
	}

	@Test
	@WithMockUser(username = "utente", roles = {Ruoli.EDITA_ANTIMAFIA_TUTTI_COD})
	public void aggiornaDichiarazioneConSelezionato() throws Exception {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/aggiornaDichiarazione/dichiarazione_3485.json"));
		String resString = objectMapper.writeValueAsString(response);

		Dichiarazione dichiarazione = objectMapper.readValue(resString, Dichiarazione.class);

		dichiarazione.getDatiDichiarazione().getDettaglioImpresa().getAziendeCollegate().get(0).setSelezionato(true);
		dichiarazione.getDatiDichiarazione().getDettaglioImpresa().getAziendeCollegate().get(1).setSelezionato(true);

		this.mockMvc.perform(put("/api/v1/antimafia/6183").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dichiarazione)))
		.andExpect(status().isOk()) //12979880155
		.andExpect(jsonPath("dichiarazione", IsNull.notNullValue()))
		.andExpect(jsonPath("dichiarazione.id", is(6183)))
		.andExpect(jsonPath("dichiarazione.datiDichiarazione.richiedente.codiceFiscale", is("LNLMTB74A31F205S")))
		.andExpect(jsonPath("dichiarazione.datiDichiarazione.dettaglioImpresa.aziendeCollegate[0].selezionato", is(true)))
		.andExpect(jsonPath("dichiarazione.datiDichiarazione.dettaglioImpresa.aziendeCollegate[0].codiceFiscale", is("00598990224")))
		.andExpect(jsonPath("dichiarazione.datiDichiarazione.dettaglioImpresa.aziendeCollegate[0].dettaglioImpresa.soggettiImpresa[0].codiceFiscale", is("LNLLGU39D14L378O")))
		.andExpect(jsonPath("dichiarazione.datiDichiarazione.dettaglioImpresa.aziendeCollegate[1].selezionato", is(true)))
		.andExpect(jsonPath("dichiarazione.datiDichiarazione.dettaglioImpresa.aziendeCollegate[1].codiceFiscale", is("12979880155")))
		.andExpect(jsonPath("dichiarazione.datiDichiarazione.dettaglioImpresa.aziendeCollegate[1].dettaglioImpresa.soggettiImpresa[0].codiceFiscale", is("CRNTTR58S15F205G")))

		;
	}

	@Ignore
	@Test
	@WithMockUser(username = "utente", roles = {Ruoli.EDITA_ANTIMAFIA_TUTTI_COD})
	@Sql(scripts = "/sql/chiudiERicreaDichiarazione_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	//	@Sql(scripts = "/sql/chiudiERicreaDichiarazione_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void chiudiRicreaDomandaAntimafia() throws Exception {

		Dichiarazione daChiudere = antimafiaService.getDichiarazione(3723L);

		Dichiarazione exNovo = new Dichiarazione();
		Azienda azienda = new Azienda();
		azienda.setCuaa("BNLFRN72P43H612A");
		exNovo.setAzienda(azienda);
		exNovo.setDatiDichiarazione(new DatiDichiarazioneAntimafia());
		exNovo.getDatiDichiarazione().setRichiedente(new Richiedente());
		exNovo.getDatiDichiarazione().getRichiedente().setCodiceFiscale("BZZPLA64A31A178M");

		// MOCK
		Mockito.when(clock.nowDate()).thenReturn(Date.valueOf(LocalDate.of(2021, 7, 6)));

		// sincronizzazione
		it.tndigitale.a4g.proxy.client.model.Dichiarazione sinc = new it.tndigitale.a4g.proxy.client.model.Dichiarazione();
		sinc.setCuaa("BNLFRN72P43H612A");
		sinc.setDataFineVali(DATE_FORMAT.format(clock.nowDate()));
		Mockito.when(proxyClient.getSincronizzazioneAntimafia(Mockito.anyLong())).thenReturn(sinc);
		Mockito.when(proxyClient.putSincronizzazioneAntimafia(Mockito.anyLong(), Mockito.any())).thenReturn(null);


		// crea dichiarazione 

		// Servizio anagrafica impresa PARIX
		String serviceUrlRicercaImpresePerCF = "http://localhost:8080/a4gproxy/api/v1/anagraficaimpresa/BNLFRN72P43H612A";
		String serviceUrlRicercaImpresePerCFaziendaCollegata = "http://localhost:8080/a4gproxy/api/v1/anagraficaimpresa/00598990224";
		Mockito.when(restTemplate.getForObject(or(Mockito.eq(new URI(serviceUrlRicercaImpresePerCF)), Mockito.eq(new URI(serviceUrlRicercaImpresePerCFaziendaCollegata))), Mockito.eq(String.class)))  
		.thenReturn(getResponseRicercaImpresePerCF());

		// Servizio dettaglioCompleto impresa PARIX
		String serviceUrlDettaglioCompletoImpresa = "{\"provinciaSede\":\"TN\",\"numeroREASede\":\"173710\"}";
		String serviceUrlDettaglioCompletoImpresaEncoded = "http://localhost:8080/a4gproxy/api/v1/anagraficaimpresa/dettagliocompleto/?params="+ URLEncoder.encode(serviceUrlDettaglioCompletoImpresa, "UTF-8");

		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlDettaglioCompletoImpresaEncoded)), Mockito.eq(String.class)))  
		.thenReturn(getResponseDettaglioCompletoImpresa());

		// Servizio anagrafica persona AnagrafeTributaria
		String serviceUrlAnagrafeTributariaPerCF = "http://localhost:8080/a4gproxy/api/v1/anagrafetributaria/BZZPLA64A31A178M";
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlAnagrafeTributariaPerCF)), Mockito.eq(String.class)))  
		.thenReturn(getResponseAnagrafeTributariaPerCF());


		// build request
		DatiChiusuraExNovoDichiarazioneAntimafia requestBody = new DatiChiusuraExNovoDichiarazioneAntimafia();

		requestBody.setDaChiudere(daChiudere);
		requestBody.setExNovo(exNovo);

		String json = objectMapper.writeValueAsString(requestBody);

		String stringResponse = mockMvc.perform(post("/api/v1/antimafia/3723/chiudiRicreaDichiarazione")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse().getContentAsString();

		objectMapper.readValue(stringResponse, AggiornaDichiarazioneEsito.class);


		daChiudere = antimafiaService.getDichiarazione(3723L);        
		assertEquals("CHIUSA", daChiudere.getStato().getIdentificativo());

		DichiarazioneFilter dichiarazioneFilter = new DichiarazioneFilter();
		dichiarazioneFilter.setAzienda(azienda);
		dichiarazioneFilter.setDatiDichiarazione(exNovo.getDatiDichiarazione());

		StatoDic statoDic = new StatoDic();
		statoDic.setIdentificativo("BOZZA");
		dichiarazioneFilter.setStato(statoDic);
		List<Dichiarazione> listaDichiarazioni = antimafiaService.getDichiarazioni(dichiarazioneFilter);

		assertEquals(1, listaDichiarazioni.size());
		assertEquals("BOZZA", listaDichiarazioni.get(0).getStato().getIdentificativo());
	}




	private String getResponseAnagrafeTributariaPerCF() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/creadichiarazione/getResponseAnagrafeTributariaPerCF.json"));
		return objectMapper.writeValueAsString(response);
	}

	private String getResponseRicercaImpresePerCF() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/creadichiarazione/getResponseRicercaImpresePerCF.json"));
		return objectMapper.writeValueAsString(response);
	}

	private String getResponseDettaglioCompletoImpresa() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/creadichiarazione/getResponseDettaglioCompletoImpresa.json"));
		return objectMapper.writeValueAsString(response);
	}

	private String getResponseAnagrafeTributariaEsitoNegativoPerCF() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/creadichiarazione/getResponseAnagrafeTributariaEsitoNegativoPerCF.json"));
		return objectMapper.writeValueAsString(response);
	}

	private ResponseEntity<String> getResponseVerificaFirmaEsitoPositivo() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/uploadFile/getResponseVerificaFirmaEsitoPositivo.json"));
		ResponseEntity<String> responseEntity = new ResponseEntity<>(objectMapper.writeValueAsString(response), HttpStatus.ACCEPTED);

		return responseEntity;
	}

	private String getResponseDettaglioCompletoImpresaEsitoNegativo() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/creadichiarazione/getResponseDettaglioCompletoImpresaEsitoNegativo.json"));
		return objectMapper.writeValueAsString(response);
	}

	private String getResponseRicercaImpresePerCFEsitoNegativo() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/creadichiarazione/getResponseRicercaImpresePerCFEsitoNegativo.json"));
		return objectMapper.writeValueAsString(response);
	}

	private String getResponseDettaglioCompletoImpresaEsitoNegativoCariche() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/creadichiarazione/getResponseDettaglioCompletoImpresaEsitoNegativoCariche.json"));
		return objectMapper.writeValueAsString(response);
	}

	private ResponseEntity<String> getResponseVerificaFirmaEsitoNegativo() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/uploadFile/getResponseVerificaFirmaEsitoNegativo.json"));
		ResponseEntity<String> responseEntity = new ResponseEntity<>(objectMapper.writeValueAsString(response), HttpStatus.ACCEPTED);

		return responseEntity;
	}

	private String getFascicoliTRRCST78B08C794X() {
		return "[{\"idFascicolo\":31393,\"denominazione\":\"TORRESANI CRISTIAN\",\"cuaa\":\"TRRCST78B08C794X\",\"stato\":\"VALIDO\",\"caa\":\"CAA COLDIRETTI DEL TRENTINO - 003\",\"caacodice\":\"4\",\"idSoggetto\":15697}]";
	}

	@Test
	@WithMockUser(username = "utente", roles = {Ruoli.RICERCA_ANTIMAFIA_TUTTI_COD})
	public void getDichiarazioniPage() throws Exception {
		this.mockMvc.perform(get("/api/v1/antimafia/page?pagSize=5&pagStart=0"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.pagSize", is(5)))
		.andExpect(jsonPath("$.pagStart", is(0)))
		.andExpect(jsonPath("$.results").isArray());
	}

}
