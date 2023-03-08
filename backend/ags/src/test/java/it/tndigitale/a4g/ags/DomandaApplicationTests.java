package it.tndigitale.a4g.ags;

import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import it.tndigitale.a4g.ags.dto.InfoGeneraliDomanda;
import it.tndigitale.a4g.ags.repository.dao.DomandaDaoImpl;
import org.h2.tools.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.ags.api.ApiUrls;
import it.tndigitale.a4g.ags.dto.DomandaFilter;
import it.tndigitale.a4g.ags.dto.DomandaUnicaFilter;
import it.tndigitale.a4g.ags.model.StatoDomanda;
import it.tndigitale.a4g.ags.utente.Ruoli;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
// @AutoConfigureTestDatabase
public class DomandaApplicationTests {

	@SpyBean
	private DomandaDaoImpl daoDomanda;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private RestTemplate restTemplate;

	@Value("${it.tndigit.security.utente.url}")
	private String uriA4gUtente;

	static Server h2WebServer;

	public static String getLabelComune(final Connection conn, final String codNazionale, final Date data) {
		return "TRENTO (TN)";
	}

	public static String getCodIsola(final Connection conn, final Long idIsola) {
		return "IT25/02187510223/AAA81";
	}

	@BeforeClass
	public static void initTest() throws SQLException {
		h2WebServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082");
		h2WebServer.start();
	}

	@AfterClass
	public static void stopServer() throws SQLException {
		h2WebServer.stop();
	}

	@Test
	@WithMockUser(username = "utenteAppag", roles = { Ruoli.VISUALIZZA_DOMANDA_DU_COD })
	public void contaDomande() throws Exception {
		this.mockMvc.perform(get("/api/v1/domandeDU/conta")
					.param("params", "{\"anno\":\"2018\",\"stato\":\"000015\"}"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("0")));
	}

	public void initializeGetDomanda() throws ParseException {
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		InfoGeneraliDomanda infoGeneraliDomandaMocked = new InfoGeneraliDomanda();
		infoGeneraliDomandaMocked.setPac("PAC1420");
		infoGeneraliDomandaMocked.setTipoDomanda("DU");
		infoGeneraliDomandaMocked.setCampagna(new BigDecimal(2018));
		infoGeneraliDomandaMocked.setModulo("PAGAMENTI DIRETTI");
		infoGeneraliDomandaMocked.setCodModulo("BPS_2018");
		infoGeneraliDomandaMocked.setCuaaIntestatario("BBBFBA66E31F187R");
		infoGeneraliDomandaMocked.setRagioneSociale("BEBBER FABIO");
		infoGeneraliDomandaMocked.setNumeroDomanda(Long.parseLong ("183380"));
		infoGeneraliDomandaMocked.setDataPresentazione(simpleDateFormat.parse("2018-04-26"));
		infoGeneraliDomandaMocked.setCodEnteCompilatore("12");
		infoGeneraliDomandaMocked.setEnteCompilatore("CAA COLDIRETTI DEL TRENTINO - 011");
		infoGeneraliDomandaMocked.setDataProtocollazione(simpleDateFormat.parse("2018-04-26"));
		infoGeneraliDomandaMocked.setStato(StatoDomanda.IN_ISTRUTTORIA);
		infoGeneraliDomandaMocked.setDataPassaggioStato(simpleDateFormat.parse("2018-04-26"));
		doReturn(infoGeneraliDomandaMocked).when(daoDomanda).getInfoGeneraliDomanda(Mockito.anyLong());
	}
	@Test
	@WithMockUser(username = "utenteAppag", roles = { Ruoli.VISUALIZZA_DOMANDA_DU_COD })
	public void getDomanda() throws Exception {
		initializeGetDomanda();
		this.mockMvc.perform(get("/api/v1/domandeDU/183380"))
				.andExpect(status().isOk())
				.andExpect(content().string(
						"{\"infoGeneraliDomanda\":{\"pac\":\"PAC1420\",\"tipoDomanda\":\"DU\",\"campagna\":2018,\"modulo\":\"PAGAMENTI DIRETTI\",\"codModulo\":\"BPS_2018\",\"cuaaIntestatario\":\"BBBFBA66E31F187R\",\"ragioneSociale\":\"BEBBER FABIO\",\"numeroDomanda\":183380,\"numeroDomandaRettificata\":null,\"dataPresentazione\":\"2018-04-26T00:00:00.000+0200\",\"codEnteCompilatore\":\"12\",\"enteCompilatore\":\"CAA COLDIRETTI DEL TRENTINO - 011\",\"dataProtocollazione\":\"2018-04-26T00:00:00.000+0200\",\"dataPresentazOriginaria\":null,\"dataProtocollazOriginaria\":null,\"stato\":\"IN_ISTRUTTORIA\",\"dataPassaggioStato\":\"2018-04-26T00:00:00.000+0200\",\"dtProtocollazioneUltimaModifica\":null,\"numeroDomandaUltimaModifica\":null},\"controlliPresentazione\":{\"aggiornamentoFascicolo\":\"S\",\"visioneAnomalie\":\"S\",\"firmaDomanda\":\"S\",\"archiviazioneDomanda\":\"S\"},\"richieste\":{\"sintesiRichieste\":{\"richiestaDisaccoppiato\":true,\"richiestaSuperfici\":false,\"richiestaZootecnia\":false}},\"sostegnoDisaccoppiato\":{\"bpsRichiesto\":true,\"greeningRichiesto\":true,\"giovaneRichiesto\":false,\"superficieImpegnataLorda\":41676,\"superficieImpegnataNetta\":41676.0},\"sostegnoSuperfici\":{\"superficieRichiestaSoia\":0,\"superficieRichiestaFrumentoDuro\":0,\"superficieRichiestaFrumentoProteaginose\":0,\"superficieRichiestaFrumentoLeguminose\":0,\"superficieRichiestaPomodoro\":0,\"superficieRichiestaOlivoNazionale\":0,\"superficieRichiestaOlivo75\":0,\"superficieRichiestaOlivoQualita\":0,\"superficieTotaleRichiesta\":0,\"sostegnoSoiaRichiesto\":false,\"sostegnoFrumentoDuroRichiesto\":false,\"sostegnoFrumentoProteaginoseRichiesto\":false,\"sostegnoFrumentoLeguminoseRichiesto\":false,\"sostegnoPomorodoRichiesto\":false,\"sostegnoOlivoNazionareRichiesto\":false,\"sostegnoOlivo75Richiesto\":false,\"sostegnoOlivoQualitaRichiesto\":false}}"));
	}

	@Test
	@WithMockUser(username = "utenteAppag", roles = { Ruoli.VISUALIZZA_DOMANDA_DU_COD })
	public void getDomandaNulla() throws Exception {
		try {
			this.mockMvc.perform(get("/api/v1/domandeDU/183381")).andExpect(status().isInternalServerError());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Test
	@Transactional
	@WithMockUser(username = "utenteAppag", roles = { Ruoli.MOVIMENTA_DOMANDA_DU_COD })
	public void spostaDomandaInNonRicevibile() throws Exception {
		this.mockMvc.perform(put("/api/v1/domandeDU/183380/eseguiMovimento").contentType(MediaType.APPLICATION_JSON).content("NONRIC")).andExpect(status().isOk())
				.andExpect(content().string("Movimento eseguito correttamente"));
	}

	@Test
	@Transactional
	@WithMockUser(username = "utenteAppag", roles = { Ruoli.MOVIMENTA_DOMANDA_DU_COD })
	public void movimentoNonDefinito() throws Exception {
		this.mockMvc.perform(put("/api/v1/domandeDU/183380/eseguiMovimento").contentType(MediaType.APPLICATION_JSON).content("RICEVI")).andExpect(status().isNoContent());
	}

	@Test
	@Transactional
	@WithMockUser(username = "utenteAppag", roles = { Ruoli.MOVIMENTA_DOMANDA_DU_COD })
	public void movimentoNonDisponibilePerStatoDomanda() throws Exception {
		this.mockMvc.perform(put("/api/v1/domandeDU/188802/eseguiMovimento").contentType(MediaType.APPLICATION_JSON).content("A4GIST")).andExpect(status().isNoContent());
	}

	@Test
	@Transactional
	@WithMockUser(username = "utenteAppag", roles = { Ruoli.MOVIMENTA_DOMANDA_DU_COD })
	public void domandaNonEsistente() throws Exception {
		this.mockMvc.perform(put("/api/v1/domandeDU/183395/eseguiMovimento").contentType(MediaType.APPLICATION_JSON).content("A4GIST")).andExpect(status().isNoContent());
	}

	public void initializeGetDomandaRettificata() throws ParseException {
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		InfoGeneraliDomanda infoGeneraliDomandaMocked = new InfoGeneraliDomanda();
		infoGeneraliDomandaMocked.setPac("PAC1420");
		infoGeneraliDomandaMocked.setTipoDomanda("DU");
		infoGeneraliDomandaMocked.setCampagna(new BigDecimal(2018));
		infoGeneraliDomandaMocked.setModulo("PAGAMENTI DIRETTI - DOMANDA DI MODIFICA AI SENSI ART. 15 DEL REG. UE 809/2014");
		infoGeneraliDomandaMocked.setCodModulo("BPS_ART_15_2018");
		infoGeneraliDomandaMocked.setCuaaIntestatario("PRSSRN98R66L378R");
		infoGeneraliDomandaMocked.setRagioneSociale("PARIS SERENA");
		infoGeneraliDomandaMocked.setNumeroDomanda(Long.parseLong ("191194"));
		infoGeneraliDomandaMocked.setDataPresentazione(simpleDateFormat.parse("2018-06-18"));
		infoGeneraliDomandaMocked.setCodEnteCompilatore("12");
		infoGeneraliDomandaMocked.setEnteCompilatore("CAA COLDIRETTI DEL TRENTINO - 011");
		infoGeneraliDomandaMocked.setDataProtocollazione(simpleDateFormat.parse("2018-04-26"));
		infoGeneraliDomandaMocked.setStato(StatoDomanda.IN_ISTRUTTORIA);
		infoGeneraliDomandaMocked.setDataPassaggioStato(simpleDateFormat.parse("2018-06-18"));
		infoGeneraliDomandaMocked.setNumeroDomandaRettificata(Long.parseLong("190911"));

		doReturn(infoGeneraliDomandaMocked).when(daoDomanda).getInfoGeneraliDomanda(Mockito.anyLong());
	}

	@Test
	@WithMockUser(username = "utenteAppag", roles = { Ruoli.VISUALIZZA_DOMANDA_DU_COD })
	public void getDomandaRettificata() throws Exception {
		initializeGetDomandaRettificata();
		this.mockMvc.perform(get("/api/v1/domandeDU/191194")).andExpect(status().isOk()).andExpect(content().string(
				"{\"infoGeneraliDomanda\":{\"pac\":\"PAC1420\",\"tipoDomanda\":\"DU\",\"campagna\":2018,\"modulo\":\"PAGAMENTI DIRETTI - DOMANDA DI MODIFICA AI SENSI ART. 15 DEL REG. UE 809/2014\",\"codModulo\":\"BPS_ART_15_2018\",\"cuaaIntestatario\":\"PRSSRN98R66L378R\",\"ragioneSociale\":\"PARIS SERENA\",\"numeroDomanda\":191194,\"numeroDomandaRettificata\":190911,\"dataPresentazione\":\"2018-06-18T00:00:00.000+0200\",\"codEnteCompilatore\":\"12\",\"enteCompilatore\":\"CAA COLDIRETTI DEL TRENTINO - 011\",\"dataProtocollazione\":\"2018-04-26T00:00:00.000+0200\",\"dataPresentazOriginaria\":null,\"dataProtocollazOriginaria\":null,\"stato\":\"IN_ISTRUTTORIA\",\"dataPassaggioStato\":\"2018-06-18T00:00:00.000+0200\",\"dtProtocollazioneUltimaModifica\":null,\"numeroDomandaUltimaModifica\":null},\"controlliPresentazione\":{\"aggiornamentoFascicolo\":\"S\",\"visioneAnomalie\":\"S\",\"firmaDomanda\":\"S\",\"archiviazioneDomanda\":\"S\"},\"richieste\":{\"sintesiRichieste\":{\"richiestaDisaccoppiato\":false,\"richiestaSuperfici\":false,\"richiestaZootecnia\":false}},\"sostegnoDisaccoppiato\":{\"bpsRichiesto\":true,\"greeningRichiesto\":true,\"giovaneRichiesto\":false,\"superficieImpegnataLorda\":0,\"superficieImpegnataNetta\":0.0},\"sostegnoSuperfici\":{\"superficieRichiestaSoia\":0,\"superficieRichiestaFrumentoDuro\":0,\"superficieRichiestaFrumentoProteaginose\":0,\"superficieRichiestaFrumentoLeguminose\":0,\"superficieRichiestaPomodoro\":0,\"superficieRichiestaOlivoNazionale\":0,\"superficieRichiestaOlivo75\":0,\"superficieRichiestaOlivoQualita\":0,\"superficieTotaleRichiesta\":0,\"sostegnoSoiaRichiesto\":false,\"sostegnoFrumentoDuroRichiesto\":false,\"sostegnoFrumentoProteaginoseRichiesto\":false,\"sostegnoFrumentoLeguminoseRichiesto\":false,\"sostegnoPomorodoRichiesto\":false,\"sostegnoOlivoNazionareRichiesto\":false,\"sostegnoOlivo75Richiesto\":false,\"sostegnoOlivoQualitaRichiesto\":false}}"));
	}

	public void initializeGetDomandaExpanded() throws ParseException {
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		InfoGeneraliDomanda infoGeneraliDomandaMocked = new InfoGeneraliDomanda();
		infoGeneraliDomandaMocked.setPac("PAC1420");
		infoGeneraliDomandaMocked.setTipoDomanda("DU");
		infoGeneraliDomandaMocked.setCampagna(new BigDecimal(2018));
		infoGeneraliDomandaMocked.setModulo("PAGAMENTI DIRETTI");
		infoGeneraliDomandaMocked.setCodModulo("BPS_2018");
		infoGeneraliDomandaMocked.setCuaaIntestatario("BNRNDR86B08H330Q");
		infoGeneraliDomandaMocked.setRagioneSociale("BONORA ANDREA");
		infoGeneraliDomandaMocked.setNumeroDomanda(Long.parseLong ("186889"));
		infoGeneraliDomandaMocked.setDataPresentazione(simpleDateFormat.parse("2018-05-18"));
		infoGeneraliDomandaMocked.setCodEnteCompilatore("12");
		infoGeneraliDomandaMocked.setEnteCompilatore("CAA COLDIRETTI DEL TRENTINO - 011");
		infoGeneraliDomandaMocked.setDataProtocollazione(simpleDateFormat.parse("2018-05-18"));
		infoGeneraliDomandaMocked.setStato(StatoDomanda.IN_ISTRUTTORIA);
		infoGeneraliDomandaMocked.setDataPassaggioStato(simpleDateFormat.parse("2018-05-18"));

		doReturn(infoGeneraliDomandaMocked).when(daoDomanda).getInfoGeneraliDomanda(Mockito.anyLong());
	}

	@Test
	@WithMockUser(username = "utenteAppag", roles = { Ruoli.VISUALIZZA_DOMANDA_DU_COD })
	public void getDomandaExpanded() throws Exception {
		initializeGetDomandaExpanded();
		this.mockMvc.perform(get("/api/v1/domandeDU/186889").param("expand", "sostegniSuperficie,sostegniAllevamento,datiPascolo,dichiarazioni"))
				.andExpect(status().isOk()).andExpect(content().string(getDomandaUnicaAgsExpanded()));
	}

	public void initializeGetDomandaSupEleggibiliExpanded() throws ParseException {
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		InfoGeneraliDomanda infoGeneraliDomandaMocked = new InfoGeneraliDomanda();
		infoGeneraliDomandaMocked.setPac("PAC1420");
		infoGeneraliDomandaMocked.setTipoDomanda("DU");
		infoGeneraliDomandaMocked.setCampagna(new BigDecimal(2018));
		infoGeneraliDomandaMocked.setModulo("PAGAMENTI DIRETTI");
		infoGeneraliDomandaMocked.setCodModulo("BPS_2018");
		infoGeneraliDomandaMocked.setCuaaIntestatario("BBBFBA66E31F187R");
		infoGeneraliDomandaMocked.setRagioneSociale("BEBBER FABIO");
		infoGeneraliDomandaMocked.setNumeroDomanda(Long.parseLong ("183380"));
		infoGeneraliDomandaMocked.setDataPresentazione(simpleDateFormat.parse("2018-04-26"));
		infoGeneraliDomandaMocked.setCodEnteCompilatore("12");
		infoGeneraliDomandaMocked.setEnteCompilatore("CAA COLDIRETTI DEL TRENTINO - 011");
		infoGeneraliDomandaMocked.setDataProtocollazione(simpleDateFormat.parse("2018-04-26"));
		infoGeneraliDomandaMocked.setStato(StatoDomanda.IN_ISTRUTTORIA);
		infoGeneraliDomandaMocked.setDataPassaggioStato(simpleDateFormat.parse("2018-04-26"));

		doReturn(infoGeneraliDomandaMocked).when(daoDomanda).getInfoGeneraliDomanda(Mockito.anyLong());
	}
	@Test
	@WithMockUser(username = "utenteAppag", roles = { Ruoli.VISUALIZZA_DOMANDA_DU_COD })
	public void getDomandaSupEleggibiliExpanded() throws Exception {
		initializeGetDomandaSupEleggibiliExpanded();
		this.mockMvc.perform(get("/api/v1/domandeDU/183380").param("expand", "infoIstruttoria")).andExpect(status().isOk())
				.andExpect(content().string(containsString("\"superficieGis\":12019,\"superficieSigeco\":0")));
	}

	private String getDomandaUnicaAgsExpanded() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnicaExpanded.json"));
		return objectMapper.writeValueAsString(response);
	}

	private String getSostegnoBPS2017() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/SostegnoBPS2017.json"));
		return objectMapper.writeValueAsString(response);
	}

	public void initializeGetSostegnoDomandaPrecedenteAl2018() throws ParseException {
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		InfoGeneraliDomanda infoGeneraliDomandaMocked = new InfoGeneraliDomanda();
		infoGeneraliDomandaMocked.setPac("PAC1420");
		infoGeneraliDomandaMocked.setTipoDomanda("DU");
		infoGeneraliDomandaMocked.setCampagna(new BigDecimal(2018));
		infoGeneraliDomandaMocked.setModulo("PAGAMENTI DIRETTI");
		infoGeneraliDomandaMocked.setCodModulo("BPS_2018");
		infoGeneraliDomandaMocked.setCuaaIntestatario("BBBFBA66E31F187R");
		infoGeneraliDomandaMocked.setRagioneSociale("BEBBER FABIO");
		infoGeneraliDomandaMocked.setNumeroDomanda(Long.parseLong ("183380"));
		infoGeneraliDomandaMocked.setDataPresentazione(simpleDateFormat.parse("2018-04-26"));
		infoGeneraliDomandaMocked.setCodEnteCompilatore("12");
		infoGeneraliDomandaMocked.setEnteCompilatore("CAA COLDIRETTI DEL TRENTINO - 011");
		infoGeneraliDomandaMocked.setDataProtocollazione(simpleDateFormat.parse("2018-04-26"));
		infoGeneraliDomandaMocked.setStato(StatoDomanda.IN_ISTRUTTORIA);
		infoGeneraliDomandaMocked.setDataPassaggioStato(simpleDateFormat.parse("2018-04-26"));

		doReturn(infoGeneraliDomandaMocked).when(daoDomanda).getInfoGeneraliDomanda(Mockito.anyLong());
	}

	@Test
	@WithMockUser(username = "utenteAppag", roles = { Ruoli.VISUALIZZA_DOMANDA_DU_COD })
	public void getSostegnoDomandaPrecedenteAl2018() throws Exception {
		initializeGetSostegnoDomandaPrecedenteAl2018();
		this.mockMvc.perform(get("/api/v1/domandeDU/183380").param("expand", "premiSostegno.BPS_2017")).andExpect(status().isOk())
				.andExpect(content().string(getSostegnoBPS2017()));
	}

	@Test
	@WithMockUser(username = "utenteAppag", roles = { Ruoli.VISUALIZZA_DOMANDA_DU_COD })
	public void testExistCheckPresenzaDomandeFilteredFalse() throws Exception {
		this.mockMvc.perform(get("/api/v1/domandeDU/checkPresenzaDomandeFiltered").param("params", "{\"codicePac\":\"PAC1420\",\"tipoDomanda\":\"DU\", \"anno\":2017, \"cuaa\":\"PRSSRN98R66L378R\"}"))
				.andExpect(status().isOk()).andExpect(content().string("false"));
	}

	@Test
	@WithMockUser(username = "utenteAppag", roles = { Ruoli.VISUALIZZA_DOMANDA_DU_COD })
	public void testExistCheckPresenzaDomandeFilteredTrue() throws Exception {
		this.mockMvc.perform(get("/api/v1/domandeDU/checkPresenzaDomandeFiltered").param("params", "{\"codicePac\":\"PAC1420\",\"tipoDomanda\":\"DU\", \"anno\":2017, \"cuaa\":\"BBBFBA66E31F187R\"}"))
				.andExpect(status().isOk()).andExpect(content().string("true"));
	}

	public void initializeGetDomandaUnica() throws ParseException {
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		InfoGeneraliDomanda infoGeneraliDomandaMocked = new InfoGeneraliDomanda();
		infoGeneraliDomandaMocked.setPac("PAC1420");
		infoGeneraliDomandaMocked.setTipoDomanda("DU");
		infoGeneraliDomandaMocked.setCampagna(new BigDecimal(2018));
		infoGeneraliDomandaMocked.setModulo("PAGAMENTI DIRETTI - DOMANDA DI MODIFICA AI SENSI ART.15 DEL REG. UE 809/2014");
		infoGeneraliDomandaMocked.setCodModulo("BPS_ART_15_2018");
		infoGeneraliDomandaMocked.setCuaaIntestatario("PRSSRN98R66L378R");
		infoGeneraliDomandaMocked.setRagioneSociale("PARIS SERENA");
		infoGeneraliDomandaMocked.setNumeroDomanda(Long.parseLong ("191194"));
		infoGeneraliDomandaMocked.setDataPresentazione(simpleDateFormat.parse("2018-06-18"));
		infoGeneraliDomandaMocked.setCodEnteCompilatore("12");
		infoGeneraliDomandaMocked.setEnteCompilatore("CAA COLDIRETTI DEL TRENTINO - 011");
		infoGeneraliDomandaMocked.setDataProtocollazione(simpleDateFormat.parse("2018-04-26"));
		infoGeneraliDomandaMocked.setStato(StatoDomanda.IN_ISTRUTTORIA);
		infoGeneraliDomandaMocked.setDataPassaggioStato(simpleDateFormat.parse("2018-06-18"));

		doReturn(infoGeneraliDomandaMocked).when(daoDomanda).getInfoGeneraliDomanda(Mockito.anyLong());
	}

	@Test
	@WithMockUser(username = "utenteAppag", roles = { Ruoli.VISUALIZZA_DOMANDA_DU_COD })
	public void getDomandaUnica() throws Exception {
		initializeGetDomandaUnica();
		this.mockMvc.perform(get(ApiUrls.DOMANDE_UNICHE_V1)
						.param("campagna", "2018")
						.param("cuaa", "PRSSRN98R66L378R")
						.param("stati", StatoDomanda.PROTOCOLLATA.toString())
						.param("stati", StatoDomanda.NON_RICEVIBILE.toString())
						.param("expand", DomandaUnicaFilter.Expand.RICHIESTE.toString()))
				.andExpect(status().isOk());
		//		.andExpect();
	}

	public void initializeGetDomandaUnicaAziendaAbilitata() throws ParseException {
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		InfoGeneraliDomanda infoGeneraliDomandaMocked = new InfoGeneraliDomanda();
		infoGeneraliDomandaMocked.setPac("PAC1420");
		infoGeneraliDomandaMocked.setTipoDomanda("DU");
		infoGeneraliDomandaMocked.setCampagna(new BigDecimal(2018));
		infoGeneraliDomandaMocked.setModulo("PAGAMENTI DIRETTI");
		infoGeneraliDomandaMocked.setCodModulo("BPS_2018");
		infoGeneraliDomandaMocked.setCuaaIntestatario("BBBFBA66E31F187R");
		infoGeneraliDomandaMocked.setRagioneSociale("BEBBER FABIO");
		infoGeneraliDomandaMocked.setNumeroDomanda(Long.parseLong ("183380"));
		infoGeneraliDomandaMocked.setDataPresentazione(simpleDateFormat.parse("2018-04-26"));
		infoGeneraliDomandaMocked.setCodEnteCompilatore("12");
		infoGeneraliDomandaMocked.setEnteCompilatore("CAA COLDIRETTI DEL TRENTINO - 011");
		infoGeneraliDomandaMocked.setDataProtocollazione(simpleDateFormat.parse("2018-04-26"));
		infoGeneraliDomandaMocked.setStato(StatoDomanda.IN_ISTRUTTORIA);
		infoGeneraliDomandaMocked.setDataPassaggioStato(simpleDateFormat.parse("2018-04-26"));

		doReturn(infoGeneraliDomandaMocked).when(daoDomanda).getInfoGeneraliDomanda(Mockito.anyLong());
	}
	@Test
	@WithMockUser(username = "utenteAzienda", roles = { Ruoli.VISUALIZZA_DOMANDA_DU_AZD_COD })
	public void getDomandaUnicaAziendaAbilitata() throws Exception {
		mockAbilitazioniUtenteAzienda();
		initializeGetDomandaUnicaAziendaAbilitata();
		this.mockMvc.perform(get(ApiUrls.DOMANDE_UNICHE_V1)
						.param("campagna", "2018")
						.param("cuaa", "BBBFBA66E31F187R")
						.param("stati", StatoDomanda.PROTOCOLLATA.toString())
						.param("stati", StatoDomanda.NON_RICEVIBILE.toString())
						.param("expand", DomandaUnicaFilter.Expand.RICHIESTE.toString()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[*]", hasSize(1)))
				.andExpect(jsonPath("$[0].infoGeneraliDomanda.campagna", is(2018)))
				.andExpect(jsonPath("$[0].infoGeneraliDomanda.cuaaIntestatario", is("BBBFBA66E31F187R")));
	}

	@Test
	@WithMockUser(username = "utenteAzienda", roles = { Ruoli.VISUALIZZA_DOMANDA_DU_AZD_COD })
	public void getDomandaUnicaAziendaNonAbilitata() throws Exception {

		mockAbilitazioniUtenteAzienda();
		
		this.mockMvc.perform(get(ApiUrls.DOMANDE_UNICHE_V1)
				.param("campagna", "2018")
				.param("cuaa", "PRSSRN98R66L378R")
				.param("stati", StatoDomanda.PROTOCOLLATA.toString())
				.param("stati", StatoDomanda.NON_RICEVIBILE.toString())
				.param("expand", DomandaUnicaFilter.Expand.RICHIESTE.toString()))
		.andExpect(status().isForbidden());
	}

	private void initializeGetDomandaUtenteAzienda() throws ParseException {
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		InfoGeneraliDomanda infoGeneraliDomandaMocked = new InfoGeneraliDomanda();
		infoGeneraliDomandaMocked.setPac("PAC1420");
		infoGeneraliDomandaMocked.setTipoDomanda("DU");
		infoGeneraliDomandaMocked.setCampagna(new BigDecimal(2018));
		infoGeneraliDomandaMocked.setModulo("PAGAMENTI DIRETTI");
		infoGeneraliDomandaMocked.setCodModulo("BPS_2018");
		infoGeneraliDomandaMocked.setCuaaIntestatario("BBBFBA66E31F187R");
		infoGeneraliDomandaMocked.setRagioneSociale("BEBBER FABIO");
		infoGeneraliDomandaMocked.setNumeroDomanda(Long.parseLong ("183380"));
		infoGeneraliDomandaMocked.setDataPresentazione(simpleDateFormat.parse("2018-04-26"));
		infoGeneraliDomandaMocked.setCodEnteCompilatore("12");
		infoGeneraliDomandaMocked.setEnteCompilatore("CAA COLDIRETTI DEL TRENTINO - 011");
		infoGeneraliDomandaMocked.setDataProtocollazione(simpleDateFormat.parse("2018-04-26"));
		infoGeneraliDomandaMocked.setStato(StatoDomanda.IN_ISTRUTTORIA);
		infoGeneraliDomandaMocked.setDataPassaggioStato(simpleDateFormat.parse("2018-04-26"));

		doReturn(infoGeneraliDomandaMocked).when(daoDomanda).getInfoGeneraliDomanda(Mockito.anyLong());
	}

	@Test
	@WithMockUser(username = "utenteAzienda", roles = { Ruoli.VISUALIZZA_DOMANDA_DU_AZD_COD })
	public void getDomandaUtenteAzienda() throws Exception {
		initializeGetDomandaUtenteAzienda();
		mockAbilitazioniUtenteAzienda();
		this.mockMvc.perform(get("/api/v1/domandeDU/183380"))
				.andExpect(status().isOk())
				.andExpect(content().string(
						"{\"infoGeneraliDomanda\":{\"pac\":\"PAC1420\",\"tipoDomanda\":\"DU\",\"campagna\":2018,\"modulo\":\"PAGAMENTI DIRETTI\",\"codModulo\":\"BPS_2018\",\"cuaaIntestatario\":\"BBBFBA66E31F187R\",\"ragioneSociale\":\"BEBBER FABIO\",\"numeroDomanda\":183380,\"numeroDomandaRettificata\":null,\"dataPresentazione\":\"2018-04-26T00:00:00.000+0200\",\"codEnteCompilatore\":\"12\",\"enteCompilatore\":\"CAA COLDIRETTI DEL TRENTINO - 011\",\"dataProtocollazione\":\"2018-04-26T00:00:00.000+0200\",\"dataPresentazOriginaria\":null,\"dataProtocollazOriginaria\":null,\"stato\":\"IN_ISTRUTTORIA\",\"dataPassaggioStato\":\"2018-04-26T00:00:00.000+0200\",\"dtProtocollazioneUltimaModifica\":null,\"numeroDomandaUltimaModifica\":null},\"controlliPresentazione\":{\"aggiornamentoFascicolo\":\"S\",\"visioneAnomalie\":\"S\",\"firmaDomanda\":\"S\",\"archiviazioneDomanda\":\"S\"},\"richieste\":{\"sintesiRichieste\":{\"richiestaDisaccoppiato\":true,\"richiestaSuperfici\":false,\"richiestaZootecnia\":false}},\"sostegnoDisaccoppiato\":{\"bpsRichiesto\":true,\"greeningRichiesto\":true,\"giovaneRichiesto\":false,\"superficieImpegnataLorda\":41676,\"superficieImpegnataNetta\":41676.0},\"sostegnoSuperfici\":{\"superficieRichiestaSoia\":0,\"superficieRichiestaFrumentoDuro\":0,\"superficieRichiestaFrumentoProteaginose\":0,\"superficieRichiestaFrumentoLeguminose\":0,\"superficieRichiestaPomodoro\":0,\"superficieRichiestaOlivoNazionale\":0,\"superficieRichiestaOlivo75\":0,\"superficieRichiestaOlivoQualita\":0,\"superficieTotaleRichiesta\":0,\"sostegnoSoiaRichiesto\":false,\"sostegnoFrumentoDuroRichiesto\":false,\"sostegnoFrumentoProteaginoseRichiesto\":false,\"sostegnoFrumentoLeguminoseRichiesto\":false,\"sostegnoPomorodoRichiesto\":false,\"sostegnoOlivoNazionareRichiesto\":false,\"sostegnoOlivo75Richiesto\":false,\"sostegnoOlivoQualitaRichiesto\":false}}"));
	}
	
	private void mockAbilitazioniUtenteAzienda() throws Exception {
		ResponseEntity<List<String>> myEntity = new ResponseEntity<List<String>>(Arrays.asList("BBBFBA66E31F187R"), org.springframework.http.HttpStatus.ACCEPTED);
		when(restTemplate.exchange(Mockito.eq(new URI(uriA4gUtente + Costanti.AZIENDE_UTENTE)), Mockito.eq(HttpMethod.GET),  Mockito.<HttpEntity>any(), Mockito.<ParameterizedTypeReference<List<String>>>any())).thenReturn(myEntity);
		
	}

	private void initializeGetDomandaUtenteAziendaNonAbilitata() throws ParseException {
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		InfoGeneraliDomanda infoGeneraliDomandaMocked = new InfoGeneraliDomanda();
		infoGeneraliDomandaMocked.setPac("PAC1420");
		infoGeneraliDomandaMocked.setTipoDomanda("DU");
		infoGeneraliDomandaMocked.setCampagna(new BigDecimal(2018));
		infoGeneraliDomandaMocked.setModulo("PAGAMENTI DIRETTI");
		infoGeneraliDomandaMocked.setCodModulo("BPS_2018");
		infoGeneraliDomandaMocked.setCuaaIntestatario("BBBFBA66E31F187R");
		infoGeneraliDomandaMocked.setRagioneSociale("BEBBER FABIO");
		infoGeneraliDomandaMocked.setNumeroDomanda(Long.parseLong ("183380"));
		infoGeneraliDomandaMocked.setDataPresentazione(simpleDateFormat.parse("2018-04-26"));
		infoGeneraliDomandaMocked.setCodEnteCompilatore("12");
		infoGeneraliDomandaMocked.setEnteCompilatore("CAA COLDIRETTI DEL TRENTINO - 011");
		infoGeneraliDomandaMocked.setDataProtocollazione(simpleDateFormat.parse("2018-04-26"));
		infoGeneraliDomandaMocked.setStato(StatoDomanda.IN_ISTRUTTORIA);
		infoGeneraliDomandaMocked.setDataPassaggioStato(simpleDateFormat.parse("2018-04-26"));

		doReturn(infoGeneraliDomandaMocked).when(daoDomanda).getInfoGeneraliDomanda(Mockito.anyLong());
	}

	@Test
	@WithMockUser(username = "utenteAzienda", roles = { Ruoli.VISUALIZZA_DOMANDA_DU_AZD_COD })
	public void getDomandaUtenteAziendaNonAbilitata() throws Exception {
		initializeGetDomandaUtenteAziendaNonAbilitata();
		mockAbilitazioniUtenteAziendaAltra();
		this.mockMvc.perform(get("/api/v1/domandeDU/183380"))
				.andExpect(status().isForbidden());
	}
	
	private void mockAbilitazioniUtenteAziendaAltra() throws Exception {
		ResponseEntity<List<String>> myEntity = new ResponseEntity<List<String>>(Arrays.asList("XXXXXXXXXXXXXXX"), org.springframework.http.HttpStatus.ACCEPTED);
		when(restTemplate.exchange(Mockito.eq(new URI(uriA4gUtente + Costanti.AZIENDE_UTENTE)), Mockito.eq(HttpMethod.GET),  Mockito.<HttpEntity>any(), Mockito.<ParameterizedTypeReference<List<String>>>any())).thenReturn(myEntity);
		
	}

	public void initializeGetDomandaUtenteCaa() throws ParseException {
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		InfoGeneraliDomanda infoGeneraliDomandaMocked = new InfoGeneraliDomanda();
		infoGeneraliDomandaMocked.setPac("PAC1420");
		infoGeneraliDomandaMocked.setTipoDomanda("DU");
		infoGeneraliDomandaMocked.setCampagna(new BigDecimal(2018));
		infoGeneraliDomandaMocked.setModulo("PAGAMENTI DIRETTI");
		infoGeneraliDomandaMocked.setCodModulo("BPS_2018");
		infoGeneraliDomandaMocked.setCuaaIntestatario("BBBFBA66E31F187R");
		infoGeneraliDomandaMocked.setRagioneSociale("BEBBER FABIO");
		infoGeneraliDomandaMocked.setNumeroDomanda(Long.parseLong ("183380"));
		infoGeneraliDomandaMocked.setDataPresentazione(simpleDateFormat.parse("2018-04-26"));
		infoGeneraliDomandaMocked.setCodEnteCompilatore("12");
		infoGeneraliDomandaMocked.setEnteCompilatore("CAA COLDIRETTI DEL TRENTINO - 011");
		infoGeneraliDomandaMocked.setDataProtocollazione(simpleDateFormat.parse("2018-04-26"));
		infoGeneraliDomandaMocked.setStato(StatoDomanda.IN_ISTRUTTORIA);
		infoGeneraliDomandaMocked.setDataPassaggioStato(simpleDateFormat.parse("2018-04-26"));

		doReturn(infoGeneraliDomandaMocked).when(daoDomanda).getInfoGeneraliDomanda(Mockito.anyLong());
	}

	@Test
	@WithMockUser(username = "utenteCAA", roles = { Ruoli.VISUALIZZA_DOMANDA_LIQUIDABILITA_DU_COD })
	public void getDomandaUtenteCaa() throws Exception {
		initializeGetDomandaUtenteCaa();
		mockAbilitazioniUtenteCaa();
		this.mockMvc.perform(get("/api/v1/domandeDU/183380"))
				.andExpect(status().isOk())
				.andExpect(content().string(
						"{\"infoGeneraliDomanda\":{\"pac\":\"PAC1420\",\"tipoDomanda\":\"DU\",\"campagna\":2018,\"modulo\":\"PAGAMENTI DIRETTI\",\"codModulo\":\"BPS_2018\",\"cuaaIntestatario\":\"BBBFBA66E31F187R\",\"ragioneSociale\":\"BEBBER FABIO\",\"numeroDomanda\":183380,\"numeroDomandaRettificata\":null,\"dataPresentazione\":\"2018-04-26T00:00:00.000+0200\",\"codEnteCompilatore\":\"12\",\"enteCompilatore\":\"CAA COLDIRETTI DEL TRENTINO - 011\",\"dataProtocollazione\":\"2018-04-26T00:00:00.000+0200\",\"dataPresentazOriginaria\":null,\"dataProtocollazOriginaria\":null,\"stato\":\"IN_ISTRUTTORIA\",\"dataPassaggioStato\":\"2018-04-26T00:00:00.000+0200\",\"dtProtocollazioneUltimaModifica\":null,\"numeroDomandaUltimaModifica\":null},\"controlliPresentazione\":{\"aggiornamentoFascicolo\":\"S\",\"visioneAnomalie\":\"S\",\"firmaDomanda\":\"S\",\"archiviazioneDomanda\":\"S\"},\"richieste\":{\"sintesiRichieste\":{\"richiestaDisaccoppiato\":true,\"richiestaSuperfici\":false,\"richiestaZootecnia\":false}},\"sostegnoDisaccoppiato\":{\"bpsRichiesto\":true,\"greeningRichiesto\":true,\"giovaneRichiesto\":false,\"superficieImpegnataLorda\":41676,\"superficieImpegnataNetta\":41676.0},\"sostegnoSuperfici\":{\"superficieRichiestaSoia\":0,\"superficieRichiestaFrumentoDuro\":0,\"superficieRichiestaFrumentoProteaginose\":0,\"superficieRichiestaFrumentoLeguminose\":0,\"superficieRichiestaPomodoro\":0,\"superficieRichiestaOlivoNazionale\":0,\"superficieRichiestaOlivo75\":0,\"superficieRichiestaOlivoQualita\":0,\"superficieTotaleRichiesta\":0,\"sostegnoSoiaRichiesto\":false,\"sostegnoFrumentoDuroRichiesto\":false,\"sostegnoFrumentoProteaginoseRichiesto\":false,\"sostegnoFrumentoLeguminoseRichiesto\":false,\"sostegnoPomorodoRichiesto\":false,\"sostegnoOlivoNazionareRichiesto\":false,\"sostegnoOlivo75Richiesto\":false,\"sostegnoOlivoQualitaRichiesto\":false}}"));
	}
	
	private void mockAbilitazioniUtenteCaa() throws Exception {
		ResponseEntity<List<String>> myEntity = new ResponseEntity<List<String>>(Arrays.asList("12"), org.springframework.http.HttpStatus.ACCEPTED);
		when(restTemplate.exchange(Mockito.eq(new URI(uriA4gUtente + Costanti.ENTI_UTENTE)), Mockito.eq(HttpMethod.GET),  Mockito.<HttpEntity>any(), Mockito.<ParameterizedTypeReference<List<String>>>any())).thenReturn(myEntity);
		
	}

	public void initializeGetDomandaUtenteCaaNonAbilitato() throws ParseException {
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		InfoGeneraliDomanda infoGeneraliDomandaMocked = new InfoGeneraliDomanda();
		infoGeneraliDomandaMocked.setPac("PAC1420");
		infoGeneraliDomandaMocked.setTipoDomanda("DU");
		infoGeneraliDomandaMocked.setCampagna(new BigDecimal(2018));
		infoGeneraliDomandaMocked.setModulo("PAGAMENTI DIRETTI");
		infoGeneraliDomandaMocked.setCodModulo("BPS_2018");
		infoGeneraliDomandaMocked.setCuaaIntestatario("BBBFBA66E31F187R");
		infoGeneraliDomandaMocked.setRagioneSociale("BEBBER FABIO");
		infoGeneraliDomandaMocked.setNumeroDomanda(Long.parseLong ("183380"));
		infoGeneraliDomandaMocked.setDataPresentazione(simpleDateFormat.parse("2018-04-26"));
		infoGeneraliDomandaMocked.setCodEnteCompilatore("12");
		infoGeneraliDomandaMocked.setEnteCompilatore("CAA COLDIRETTI DEL TRENTINO - 011");
		infoGeneraliDomandaMocked.setDataProtocollazione(simpleDateFormat.parse("2018-04-26"));
		infoGeneraliDomandaMocked.setStato(StatoDomanda.IN_ISTRUTTORIA);
		infoGeneraliDomandaMocked.setDataPassaggioStato(simpleDateFormat.parse("2018-04-26"));

		doReturn(infoGeneraliDomandaMocked).when(daoDomanda).getInfoGeneraliDomanda(Mockito.anyLong());
	}

	@Test
	@WithMockUser(username = "utenteCAA", roles = { Ruoli.VISUALIZZA_DOMANDA_LIQUIDABILITA_DU_COD })
	public void getDomandaUtenteCaaNonAbilitato() throws Exception {
		initializeGetDomandaUtenteCaaNonAbilitato();
		mockAbilitazioniUtenteCaa();
		this.mockMvc.perform(get("/api/v1/domandeDU/183380"))
				.andExpect(status().isOk())
				.andExpect(content().string(
						"{\"infoGeneraliDomanda\":{\"pac\":\"PAC1420\",\"tipoDomanda\":\"DU\",\"campagna\":2018,\"modulo\":\"PAGAMENTI DIRETTI\",\"codModulo\":\"BPS_2018\",\"cuaaIntestatario\":\"BBBFBA66E31F187R\",\"ragioneSociale\":\"BEBBER FABIO\",\"numeroDomanda\":183380,\"numeroDomandaRettificata\":null,\"dataPresentazione\":\"2018-04-26T00:00:00.000+0200\",\"codEnteCompilatore\":\"12\",\"enteCompilatore\":\"CAA COLDIRETTI DEL TRENTINO - 011\",\"dataProtocollazione\":\"2018-04-26T00:00:00.000+0200\",\"dataPresentazOriginaria\":null,\"dataProtocollazOriginaria\":null,\"stato\":\"IN_ISTRUTTORIA\",\"dataPassaggioStato\":\"2018-04-26T00:00:00.000+0200\",\"dtProtocollazioneUltimaModifica\":null,\"numeroDomandaUltimaModifica\":null},\"controlliPresentazione\":{\"aggiornamentoFascicolo\":\"S\",\"visioneAnomalie\":\"S\",\"firmaDomanda\":\"S\",\"archiviazioneDomanda\":\"S\"},\"richieste\":{\"sintesiRichieste\":{\"richiestaDisaccoppiato\":true,\"richiestaSuperfici\":false,\"richiestaZootecnia\":false}},\"sostegnoDisaccoppiato\":{\"bpsRichiesto\":true,\"greeningRichiesto\":true,\"giovaneRichiesto\":false,\"superficieImpegnataLorda\":41676,\"superficieImpegnataNetta\":41676.0},\"sostegnoSuperfici\":{\"superficieRichiestaSoia\":0,\"superficieRichiestaFrumentoDuro\":0,\"superficieRichiestaFrumentoProteaginose\":0,\"superficieRichiestaFrumentoLeguminose\":0,\"superficieRichiestaPomodoro\":0,\"superficieRichiestaOlivoNazionale\":0,\"superficieRichiestaOlivo75\":0,\"superficieRichiestaOlivoQualita\":0,\"superficieTotaleRichiesta\":0,\"sostegnoSoiaRichiesto\":false,\"sostegnoFrumentoDuroRichiesto\":false,\"sostegnoFrumentoProteaginoseRichiesto\":false,\"sostegnoFrumentoLeguminoseRichiesto\":false,\"sostegnoPomorodoRichiesto\":false,\"sostegnoOlivoNazionareRichiesto\":false,\"sostegnoOlivo75Richiesto\":false,\"sostegnoOlivoQualitaRichiesto\":false}}"));
	}
	
	private void mockAbilitazioniUtenteCaaNonAbilitato() throws Exception {
		ResponseEntity<List<String>> myEntity = new ResponseEntity<List<String>>(Arrays.asList("9"), org.springframework.http.HttpStatus.ACCEPTED);
		when(restTemplate.exchange(Mockito.eq(new URI(uriA4gUtente + Costanti.ENTI_UTENTE)), Mockito.eq(HttpMethod.GET),  Mockito.<HttpEntity>any(), Mockito.<ParameterizedTypeReference<List<String>>>any())).thenReturn(myEntity);

	}
}

