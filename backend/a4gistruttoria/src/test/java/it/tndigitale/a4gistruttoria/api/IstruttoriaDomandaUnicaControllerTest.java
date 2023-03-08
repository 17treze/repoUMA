package it.tndigitale.a4gistruttoria.api;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4gistruttoria.api.disaccoppiato.IstruttoriaDisaccoppiatoController;
import it.tndigitale.a4gistruttoria.dto.istruttoria.IstruttoriaDomandaUnicaCsv;
import it.tndigitale.a4gistruttoria.dto.lavorazione.ControlloFrontend;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiIstruttoria;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiIstruttoriaACS;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiIstruttoriaACZ;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiIstruttoriaPascoli;
import it.tndigitale.a4gistruttoria.util.CodicePac;
import it.tndigitale.a4gistruttoria.util.MapperWrapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
// @AutoConfigureTestDatabase
@WithMockUser(username = "istruttore", roles = { 
		"a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
		"a4gistruttoria.pac.istruttoria.du.visualizza.utente",
		"a4gistruttoria.pac.istruttoria.edita"})
@Sql(scripts = { "/DomandaUnica/istruttoria/dati-istruttore_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = { "/DomandaUnica/istruttoria/dati-istruttore_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
public class IstruttoriaDomandaUnicaControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private Clock clock;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired private MapperWrapper mapperWrapper;
	
	@Test
	public void insertDatiIstruttoriaDatiIstruttoreConDati() throws Exception  {
		mvc.perform( MockMvcRequestBuilders
			      .get(ApiUrls.ISTRUTTORIE_DISACCOPPIATO_V1 + "/300/datiIstruttore")
			      .accept(MediaType.APPLICATION_JSON))
			      .andExpect(status().is(204)); //no content
			      
		insertDatiIstruttoria();
	}
	
	@Test
	public void insertDatiIstruttoriaDatiPascoli() throws Exception  {
		insertDatiIstruttoriaPascoli();
	}
	
	@Test
	public void updateDatiIstruttoriaDatiIstruttoreConDati() throws Exception {
		
		insertDatiIstruttoria();
		
		MvcResult result = mvc.perform( MockMvcRequestBuilders
						      .get(ApiUrls.ISTRUTTORIE_DISACCOPPIATO_V1 +"/300/datiIstruttore")
						      .accept(MediaType.APPLICATION_JSON))
						      .andExpect(status().isOk())
						      .andReturn();
		String contentAsString = result.getResponse().getContentAsString();
		
		DatiIstruttoria response = asObject(contentAsString,DatiIstruttoria.class);// objectMapper.readValue(contentAsString, DatiIstruttoria.class);
		response.setbPSSuperficie(new BigDecimal(4.5));
		mvc.perform( MockMvcRequestBuilders
			      .post(ApiUrls.ISTRUTTORIE_DISACCOPPIATO_V1 + "/300/datiIstruttore")
			      .content(asJsonString(response))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
			      .andExpect(status().is2xxSuccessful())
			      .andExpect(jsonPath("$.id").isNumber())
			      .andExpect(jsonPath("$.bPSSuperficie").value(4.5));
	}
	
	@Test
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void getDatiDomanda() throws Exception {
		//564564564
		MvcResult result = mvc.perform( MockMvcRequestBuilders
			      .get(ApiUrls.ISTRUTTORIE_DISACCOPPIATO_V1 + "/564564564" + IstruttoriaDisaccoppiatoController.DATI_DOMANDA)
			      .accept(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk())
			      .andReturn();
		String contentAsString = result.getResponse().getContentAsString();
		
		List<ControlloFrontend> controlli = mapperWrapper.readValue(contentAsString, new TypeReference<List<ControlloFrontend>>(){});
		assertThat(controlli, is(not(empty())));
		assertThat(
					controlli,
				  	hasItem(allOf(hasProperty("codice1", equalTo("AMMISSIBILITA"))))
				  );
		assertThat(
				controlli,
			  	hasItem(allOf(hasProperty("codice1", equalTo("ANOMALIE_MANTENIMENTO"))))
			  );	
		assertThat(
				controlli,
			  	hasItem(allOf(hasProperty("codice1", equalTo("RIDUZIONI_BPS"))))
			  );	
		assertThat(
				controlli,
			  	hasItem(allOf(hasProperty("codice1", equalTo("SANZIONI_BPS"))))
			  );	
		assertThat(
				controlli,
			  	hasItem(allOf(hasProperty("codice1", equalTo("GREENING"))))
			  );	
		assertThat(
				controlli,
			  	hasItem(allOf(hasProperty("codice1", equalTo("GIOVANE_AGRICOLTORE"))))
			  );	
		assertThat(
				controlli,
			  	hasItem(allOf(hasProperty("codice1", equalTo("CONTROLLI_FINALI"))))
			  );	
	}
	
	@Test
	@Sql(scripts = "/DomandaUnica/dettaglio/datiDettaglio_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/dettaglio/datiDettaglio_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void getDatiDomandaConDisciplinaFinanziaria() throws Exception {
		//564564564
		MvcResult result = mvc.perform( MockMvcRequestBuilders
			      .get(ApiUrls.ISTRUTTORIE_DISACCOPPIATO_V1 + "/564564564" + IstruttoriaDisaccoppiatoController.DATI_DOMANDA)
			      .accept(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk())
			      .andReturn();
		String contentAsString = result.getResponse().getContentAsString();
		
		List<ControlloFrontend> controlli = mapperWrapper.readValue(contentAsString, new TypeReference<List<ControlloFrontend>>(){});
		assertThat(controlli, is(not(empty())));
		assertThat(
					controlli,
				  	hasItem(allOf(hasProperty("codice1", equalTo("AMMISSIBILITA"))))
				  );
		assertThat(
				controlli,
			  	hasItem(allOf(hasProperty("codice1", equalTo("ANOMALIE_MANTENIMENTO"))))
			  );	
		assertThat(
				controlli,
			  	hasItem(allOf(hasProperty("codice1", equalTo("RIDUZIONI_BPS"))))
			  );	
		assertThat(
				controlli,
			  	hasItem(allOf(hasProperty("codice1", equalTo("SANZIONI_BPS"))))
			  );	
		assertThat(
				controlli,
			  	hasItem(allOf(hasProperty("codice1", equalTo("GREENING"))))
			  );	
		assertThat(
				controlli,
			  	hasItem(allOf(hasProperty("codice1", equalTo("GIOVANE_AGRICOLTORE"))))
			  );	
		assertThat(
				controlli,
			  	hasItem(allOf(hasProperty("codice1", equalTo("CONTROLLI_FINALI"))))
			  );
		assertThat(
				controlli,
			  	hasItem(allOf(hasProperty("codice1", equalTo("DISCIPLINA_FINANZIARIA"))))
			  );			
		
	}
	
	@Test
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void getControlliSostegno() throws Exception {
		//564564564
		MvcResult result = mvc.perform( MockMvcRequestBuilders
			      .get(ApiUrls.ISTRUTTORIE_DISACCOPPIATO_V1 + "/564564564/controlli")
			      .accept(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk())
			      .andReturn();
		String contentAsString = result.getResponse().getContentAsString();
		
		List<ControlloFrontend> controlli = mapperWrapper.readValue(contentAsString, new TypeReference<List<ControlloFrontend>>(){});
		assertThat(controlli, is(not(empty())));
		assertThat(
				controlli,
			  	hasItem(allOf(hasProperty("codice1", equalTo("INFORMAZIONI"))))
			  );		
	}
	
	
	@Test
	public void insertDatiIstruttoriaDatiIstruttoreAcs() throws Exception  {
		mvc.perform( MockMvcRequestBuilders
			      .get(ApiUrls.ISTRUTTORIE_ACCOPPIATO_SUPERFICIE_V1 + "/300/datiIstruttore")
			      .accept(MediaType.APPLICATION_JSON))
			      .andExpect(status().is(204)); //no content
			      
		insertDatiIstruttoriaAcs();
	}
	
	@Test
	public void updateDatiIstruttoriaDatiIstruttoreAcs() throws Exception {
		
		insertDatiIstruttoriaAcs();
		
		MvcResult result = mvc.perform( MockMvcRequestBuilders
						      .get(ApiUrls.ISTRUTTORIE_ACCOPPIATO_SUPERFICIE_V1 + "/300/datiIstruttore")
						      .accept(MediaType.APPLICATION_JSON))
						      .andExpect(status().isOk())
						      .andReturn();
		String contentAsString = result.getResponse().getContentAsString();
		
		DatiIstruttoriaACS response = asObject(contentAsString,DatiIstruttoriaACS.class);// objectMapper.readValue(contentAsString, DatiIstruttoria.class);
		response.setSuperficieDeterminataFrumentoM9(new BigDecimal("4.5"));
		mvc.perform( MockMvcRequestBuilders
			      .post(ApiUrls.ISTRUTTORIE_ACCOPPIATO_SUPERFICIE_V1 + "/300/datiIstruttore")
			      .content(asJsonString(response))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
			      .andExpect(status().is2xxSuccessful())
			      .andExpect(jsonPath("$.id").isNumber())
			      .andExpect(jsonPath("$.superficieDeterminataFrumentoM9").value(4.5));
	}	
	
	@Test
	public void insertDatiIstruttoriaDatiIstruttoreAcz() throws Exception  {
		mvc.perform( MockMvcRequestBuilders
			      .get(ApiUrls.ISTRUTTORIE_ACCOPPIATO_ZOOTECNIA_V1 + "/300/datiIstruttore")
			      .accept(MediaType.APPLICATION_JSON))
			      .andExpect(status().is(204)); //no content
			      
		insertDatiIstruttoriaAcz();
	}
	
	@Test
	public void updateDatiIstruttoriaDatiIstruttoreAcz() throws Exception {
		
		insertDatiIstruttoriaAcz();
		
		MvcResult result = mvc.perform( MockMvcRequestBuilders
						      .get(ApiUrls.ISTRUTTORIE_ACCOPPIATO_ZOOTECNIA_V1 + "/300/datiIstruttore")
						      .accept(MediaType.APPLICATION_JSON))
						      .andExpect(status().isOk())
						      .andReturn();
		String contentAsString = result.getResponse().getContentAsString();
		
		DatiIstruttoriaACZ response = asObject(contentAsString,DatiIstruttoriaACZ.class);// objectMapper.readValue(contentAsString, DatiIstruttoria.class);
		response.setControlloAntimafia(false);
		response.setControlloSigecoLoco(true);
		mvc.perform( MockMvcRequestBuilders
			      .post(ApiUrls.ISTRUTTORIE_ACCOPPIATO_ZOOTECNIA_V1 + "/300/datiIstruttore")
			      .content(asJsonString(response))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
			      .andExpect(status().is2xxSuccessful())
			      .andExpect(jsonPath("$.id").isNumber())
			      .andExpect(jsonPath("$.controlloAntimafia").value(false))
			      .andExpect(jsonPath("$.controlloSigecoLoco").value(true));
	}

	@Test
	public void getIstruttorieAnomalieWarningError() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("campagna", "2018")
				.param("anomalie", "YES")
				.param("cuaa", "CHNRLB69M46F000I")
				.contentType(MediaType.APPLICATION_JSON))
				
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.count", equalTo(5)))
				.andExpect(jsonPath("$.risultati[0].id").value(408))
				.andExpect(jsonPath("$.risultati[1].id").value(407))
				.andExpect(jsonPath("$.risultati[2].id").value(406))
				.andExpect(jsonPath("$.risultati[3].id").value(401))
				.andExpect(jsonPath("$.risultati[4].id").value(400));
	}

	@Test
	public void getIstruttorieNoAnomalie() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("campagna", "2018")
				.param("anomalie", "NO")
				.param("cuaa", "CHNRLB69M46F000I")
				.contentType(MediaType.APPLICATION_JSON))
				// deve tirare u istruttoria 402 403 404 405
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.count", equalTo(5)));
	}

	@Test
	public void getIstruttorieCodiciAnomalieInfo() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("campagna", "2018")
				.param("codiciAnomalieInfo", "BRIDUSDC025_NO_IMPEGNI","BRIDUSDC025_DIV")
				.contentType(MediaType.APPLICATION_JSON))
				
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.risultati", hasSize(2)))
				.andExpect(jsonPath("$.risultati[0].id").value(405))
				.andExpect(jsonPath("$.risultati[1].id").value(404));
	}

	@Test
	public void getIstruttorieNoCodiciAnomalieInfo() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("campagna", "2018")
				.param("codiciAnomalieInfo", "")
				.contentType(MediaType.APPLICATION_JSON))
				
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.risultati", hasSize(10)));
	}

	@Test
	public void getIstruttorieCodiciAnomalieWarning() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("campagna", "2018")
				.param("codiciAnomalieWarning", "BRIDUSDC011","BRIDUSDC025_DIV")
				.contentType(MediaType.APPLICATION_JSON))
				
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.risultati", hasSize(2)))
				.andExpect(jsonPath("$.risultati[0].id").value(401))
				.andExpect(jsonPath("$.risultati[1].id").value(400));
	}

	@Test
	public void getIstruttorieNoCodiciAnomalieWarning() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("campagna", "2018")
				.param("codiciAnomalieWarning", "")
				.contentType(MediaType.APPLICATION_JSON))
				
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.risultati", hasSize(10)));
	}

	@Test
	public void getIstruttorieCodiciAnomalieError() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("campagna", "2018")
				.param("codiciAnomalieError", "BRIDUSDC024", "BRIDUSDC025_NO_IMPEGNI")
				.contentType(MediaType.APPLICATION_JSON))
				
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.risultati", hasSize(3)))
				.andExpect(jsonPath("$.risultati[0].id").value(408))
				.andExpect(jsonPath("$.risultati[1].id").value(407))
				.andExpect(jsonPath("$.risultati[2].id").value(406));
	}

	@Test
	public void getIstruttorieNoCodiciAnomalieError() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get(ApiUrls.ISTRUTTORIE_DU_V1)
				.param("campagna", "2018")
				.param("codiciAnomalieError", "")
				.contentType(MediaType.APPLICATION_JSON))
				
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.risultati", hasSize(10)));
	}
	
	@Test
	public void getAnniCampagna() throws Exception {
		//a4gistruttoria/api/v1/istruttorie/du/anniCampagna
		Mockito.when(clock.now()).thenReturn(LocalDateTime.of(2020, Month.FEBRUARY, 12, 1, 1));
		mvc.perform(MockMvcRequestBuilders.get(ApiUrls.ISTRUTTORIE_DU_V1 + "/anniCampagna").param("codicePac", CodicePac.PAC_2014_2020.name()))
		.andExpect(status().isOk());
	}
	
	@Test
	@Sql(scripts = "/DomandaUnica/downloadCsvIstruttorie_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/downloadCsvIstruttorie_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void downloadCsv() throws Exception {
		List<Long> idIstruttorie = new ArrayList<Long>();
		idIstruttorie.add(953364L);
		idIstruttorie.add(969997L);
		MvcResult result = this.mvc.perform(post("/api/v1/istruttorie/du/csv").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(idIstruttorie)))
				.andExpect(status().is2xxSuccessful())
                .andExpect(header().string("Content-Disposition","attachment; filename=SCARICA_DATI_ISTRUTTORIE.csv" ))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn();
		
		CsvMapper mapper = new CsvMapper();
	    CsvSchema istruttorieSchema = CsvSchema.builder()
	     		.addColumn("CUAA", CsvSchema.ColumnType.STRING)
				.addColumn("NUMERO_DOMANDA", CsvSchema.ColumnType.STRING)
				.addColumn("DESCRIZIONE_IMPRESA", CsvSchema.ColumnType.STRING)
				.build().withColumnSeparator(';').withoutQuoteChar().withHeader();
	    MappingIterator<IstruttoriaDomandaUnicaCsv> istruttorieWriter = mapper.readerFor(IstruttoriaDomandaUnicaCsv.class).with(istruttorieSchema).readValues(result.getResponse().getContentAsByteArray());
	    List<IstruttoriaDomandaUnicaCsv> allRows = new ArrayList<IstruttoriaDomandaUnicaCsv>();
	    while (istruttorieWriter.hasNext()) {
	    	IstruttoriaDomandaUnicaCsv row = istruttorieWriter.next();
        	allRows.add(row);
	    }
	    assertTrue(allRows.get(0).getCuaa().equals("MRTNDR86P10H501G"));
	    assertTrue(allRows.get(0).getNumeroDomanda().equals("185400"));
	    assertTrue(allRows.get(0).getDescrizioneImpresa().equals("MARTINELLI ANDREA"));
	    
	    assertTrue(allRows.get(1).getCuaa().equals("BRSMNC68R58H330G"));
	    assertTrue(allRows.get(1).getNumeroDomanda().equals("187720"));
	    assertTrue(allRows.get(1).getDescrizioneImpresa().equals("BRESCIANI MONICA"));
	}

	private void insertDatiIstruttoriaPascoli() throws Exception{
		DatiIstruttoriaPascoli dati = new DatiIstruttoriaPascoli();
		dati.setCuaaResponsabile("MSRMRA76S12C372W");
		dati.setSuperficieDeterminata(new BigDecimal(3.2));
		mvc.perform( MockMvcRequestBuilders
			      .post(ApiUrls.ISTRUTTORIE_DISACCOPPIATO_V1 + "/300/datiIstruttore/pascoli")
			      .content(asJsonString(Arrays.asList(dati)))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
			      .andExpect(status().is2xxSuccessful())
	              .andExpect(jsonPath("$").isArray())
	              .andExpect(jsonPath("$", hasSize(1)))
			      .andExpect(jsonPath("$[0].id").isNumber())
			      .andExpect(jsonPath("$[0].cuaaResponsabile").value("MSRMRA76S12C372W"))
			      .andExpect(jsonPath("$[0].superficieDeterminata").value(3.2));
	}
	
	private void insertDatiIstruttoria() throws Exception {
		DatiIstruttoria dati = new DatiIstruttoria();
		dati.setbPSSuperficie(new BigDecimal(3.2));
		dati.setGreeningSuperficie(new BigDecimal(2));
		dati.setImportoSalari(new BigDecimal(3));
		dati.setDomAnnoPrecNonLiquidabile(false);
		dati.setBpsSanzioniAnnoPrecedente(false);
		dati.setBpsImportoSanzioniAnnoPrecedente(BigDecimal.ZERO);
		dati.setGioSanzioniAnnoPrecedente(false);
		dati.setGioImportoSanzioniAnnoPrecedente(BigDecimal.ZERO);
		
		mvc.perform( MockMvcRequestBuilders
			      .post(ApiUrls.ISTRUTTORIE_DISACCOPPIATO_V1 + "/300/datiIstruttore")
			      .content(asJsonString(dati))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
			      .andExpect(status().is2xxSuccessful())
			      .andExpect(jsonPath("$.id").isNumber())
			      .andExpect(jsonPath("$.bpsSanzioniAnnoPrecedente").value(false))
			      .andExpect(jsonPath("$.bPSSuperficie").value(3.2));
	}
	
	private void insertDatiIstruttoriaAcs() throws Exception {
		DatiIstruttoriaACS dati = new DatiIstruttoriaACS();
		dati.setControlloAntimafia(true);
		dati.setControlloSigecoLoco(false);
		dati.setSuperficieDeterminataFrumentoM9(new BigDecimal("1.1"));
		dati.setSuperficieDeterminataLeguminoseM11(new BigDecimal("2.2"));
		dati.setSuperficieDeterminataOlivoPendenzaM16(new BigDecimal("3.3"));
		dati.setSuperficieDeterminataOlivoQualitaM17(new BigDecimal("4.4"));
		dati.setSuperficieDeterminataOlivoStandardM15(new BigDecimal("5.5"));
		dati.setSuperficieDeterminataPomodoroM14(new BigDecimal("7.7"));
		dati.setSuperficieDeterminataSoiaM8(new BigDecimal("8.8"));
		
		mvc.perform( MockMvcRequestBuilders
			      .post(ApiUrls.ISTRUTTORIE_ACCOPPIATO_SUPERFICIE_V1 + "/300/datiIstruttore")
			      .content(asJsonString(dati))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
			      .andExpect(status().is2xxSuccessful())
			      .andExpect(jsonPath("$.id").isNumber())
			      .andExpect(jsonPath("$.controlloAntimafia").value(true))
			      .andExpect(jsonPath("$.superficieDeterminataFrumentoM9").value(1.1))
			      .andExpect(jsonPath("$.superficieDeterminataOlivoQualitaM17").value(4.4))
			      .andExpect(jsonPath("$.superficieDeterminataPomodoroM14").value(7.7));
	}
	
	private void insertDatiIstruttoriaAcz() throws Exception {
		DatiIstruttoriaACZ dati = new DatiIstruttoriaACZ();
		dati.setControlloAntimafia(true);
		dati.setControlloSigecoLoco(false);
		
		mvc.perform( MockMvcRequestBuilders
			      .post(ApiUrls.ISTRUTTORIE_ACCOPPIATO_ZOOTECNIA_V1 + "/300/datiIstruttore")
			      .content(asJsonString(dati))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
			      .andExpect(status().is2xxSuccessful())
			      .andExpect(jsonPath("$.id").isNumber())
			      .andExpect(jsonPath("$.controlloAntimafia").value(true))
			      .andExpect(jsonPath("$.controlloSigecoLoco").value(false));
	}	
	
	public static String asJsonString(final Object obj) {
	    try {
	        return new ObjectMapper().writeValueAsString(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
	
	public static <T> T asObject(final String contentAsString, Class<T> contentClass) {
	    try {
	    	return  new ObjectMapper().readValue(contentAsString, contentClass);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
}
