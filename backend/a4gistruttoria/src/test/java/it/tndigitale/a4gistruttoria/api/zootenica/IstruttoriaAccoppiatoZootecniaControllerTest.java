package it.tndigitale.a4gistruttoria.api.zootenica;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.hamcrest.core.IsNull;
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
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import it.tndigitale.a4gistruttoria.api.ApiUrls;
import it.tndigitale.a4gistruttoria.component.StampaComponent;
import it.tndigitale.a4gistruttoria.dto.zootecnia.EsitoCapiFilter;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.ElencoCapiCsv;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapoModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;
import it.tndigitale.a4gistruttoria.util.MapperWrapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
// @AutoConfigureTestDatabase
@WithMockUser(username = "istruttore", roles = { "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
		"a4gistruttoria.pac.istruttoria.du.visualizza.utente" })
public class IstruttoriaAccoppiatoZootecniaControllerTest {
	@Autowired
	private MockMvc mvc;
	
	@Autowired private MapperWrapper mapperWrapper;
	
	@Autowired
	private IstruttoriaDao istruttoriaDao;

	@Autowired
	private DomandaUnicaDao domandaDao;
	
	@MockBean
	private StampaComponent serviceStampa;
	

	@Test
	@Sql(scripts = "/DomandaUnica/zootecnia/dettaglio/istruttoria_acz_dettaglio.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/zootecnia/dettaglio/istruttoria_acz_dettaglio_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void getEsitiCalcoli() throws Exception {
		// 564564564
		MvcResult result = mvc
				.perform(MockMvcRequestBuilders
						.get(ApiUrls.ISTRUTTORIE_ACCOPPIATO_ZOOTECNIA_V1 + "/564564564/esiticalcoli")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.sintesiCalcolo.ACZIMPCALCTOT", Matchers.containsString("33620.04")))
				.andExpect(jsonPath("$.sintesiCalcolo.ACZIMPRIDTOT", Matchers.containsString("360")))
				.andExpect(jsonPath("$.dettaglioCalcolo.int313").isEmpty())
				.andExpect(jsonPath("$.dettaglioCalcolo.int315").isEmpty())
				.andExpect(jsonPath("$.dettaglioCalcolo.int320").isEmpty())
				.andExpect(jsonPath("$.dettaglioCalcolo.int316").isEmpty())
				.andExpect(jsonPath("$.dettaglioCalcolo.int318").isEmpty())
				.andExpect(jsonPath("$.dettaglioCalcolo.int322").isEmpty())
				.andExpect(jsonPath("$.dettaglioCalcolo.int321").isEmpty())
				.andExpect(jsonPath("$.dettaglioCalcolo.int310").isNotEmpty())
				.andExpect(jsonPath("$.dettaglioCalcolo.int310.*", Matchers.hasSize(21)))
				.andExpect(jsonPath("$.dettaglioCalcolo.int310.*",
						Matchers.contains("16 ", null, "16 ", null, "16 ", "16 UBA", "2000 euro", "SI", "NO", "NO",
								"0.00 %", "0.00 %", "0.00 %", null, "32000 euro", "0 euro", "32000 euro", "0 euro",
								"0 euro", "32000 euro", null)))
				.andReturn();
	}

	@Test
	@Sql(scripts = "/DomandaUnica/zootecnia/dettaglio/istruttoria_acz_dettaglio.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/zootecnia/dettaglio/istruttoria_acz_dettaglio_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void getDisciplina() throws Exception {
		// 564564564
		MvcResult result = mvc
				.perform(MockMvcRequestBuilders
						.get(ApiUrls.ISTRUTTORIE_ACCOPPIATO_ZOOTECNIA_V1 + "/564564564/disciplina")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.DFPERC_INPUT", Matchers.containsString("1.411917")))
				.andExpect(jsonPath("$.DFFR_INPUT", Matchers.containsString("2000")))
				.andExpect(jsonPath("$.DFDISIMPCALC_INPUT", Matchers.containsString("6084.83")))
				.andExpect(jsonPath("$.ACZIMPCALCTOT_INPUT", Matchers.containsString("3527.76")))
				.andExpect(jsonPath("$.DFFRPAGACZ_310_OUTPUT", Matchers.containsString("1874.64")))
				.andExpect(jsonPath("$.DFFRPAGACZ_311_OUTPUT", Matchers.containsString("125.36")))
				.andExpect(jsonPath("$.DFIMPDFDISACZ_311_OUTPUT", Matchers.containsString("1506.19"))).andReturn();
		// System.out.println(result.getResponse().getContentAsString());
	}

	@Test
	@Sql(scripts = "/DomandaUnica/zootecnia/dettaglio/istruttoria_acz_dettaglio_esiticontrolli.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/zootecnia/dettaglio/istruttoria_acz_dettaglio_esiticontrolli_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void getEsitiControlli() throws Exception {
		// 564564564
		MvcResult result = mvc
				.perform(MockMvcRequestBuilders
						.get(ApiUrls.ISTRUTTORIE_ACCOPPIATO_ZOOTECNIA_V1 + "/564564564/esiticontrolli")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.errors", hasItem("importoMinimoAntimafia_RAGGIUNTO")))
				.andExpect(jsonPath("$.errors", hasItem("importoMinimoAntimafia_RAGGIUNTO")))
				.andExpect(jsonPath("$.infos", hasItem("BRIDUSDS050_esitoAntimafia_NO")))
				.andExpect(jsonPath("$.infos", hasItem("BRIDUSDS040_importoMinimo_SI"))).andReturn();
		// System.out.println(result.getResponse().getContentAsString());
	}

	@Test
	@Sql(scripts = "/DomandaUnica/zootecnia/dettaglio/istruttoria_acz_dettaglio_esiticontrolli.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/zootecnia/dettaglio/istruttoria_acz_dettaglio_esiticontrolli_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void stampaVerbaleIstruttoriaZootecnia() throws Exception {
		// 564564564
		byte[] expectedResult = Files.readAllBytes(Paths.get("./src/test/resources/DomandaUnica/intersostegno/stampa/output/verbaleLiquidazioneDisaccoppiato.pdf"));
		Mockito.when(serviceStampa.stampaPDF_A(Mockito.anyString(), Mockito.anyString())).thenReturn(expectedResult);

		MvcResult result = mvc
				.perform(MockMvcRequestBuilders
						.get(ApiUrls.ISTRUTTORIE_ACCOPPIATO_ZOOTECNIA_V1 + "/564564564/verbale")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
	}
	
	@Test
	@Transactional
	public void getAllevamentiImpegnati() throws Exception {
		IstruttoriaModel istruttoria = getIstruttoria_ZZNRLB69M46F205A_2018();

		this.mvc.perform(get("/api/v1/istruttorie/du/zootecnia/".concat(String.valueOf(istruttoria.getId())).concat("/allevamenti")))
		.andExpect(jsonPath("$[0].richiesteAllevamentoDuEsito", is(IsNull.nullValue())))
		.andExpect(jsonPath("$[1].richiesteAllevamentoDuEsito", is(IsNull.nullValue())))
		.andExpect(jsonPath("$[*].datiAllevamento", is(IsNull.notNullValue())))
		.andExpect(jsonPath("$[*].datiAllevamento.descrizioneAllevamento", is(IsNull.notNullValue())))
		.andExpect(jsonPath("$[*].datiAllevamento.codiceAllevamento", is(IsNull.notNullValue())))
		.andExpect(jsonPath("$[*].codiceintervento", is(IsNull.notNullValue())))
		.andExpect(jsonPath("$[*].count", is(IsNull.notNullValue())))
		.andExpect(status().isOk());
	}
	
	@Test
	@Transactional
	public void ricercaEsitiCapi() throws Exception {
		IstruttoriaModel istruttoria = getIstruttoria_ZZNRLB69M46F205A_2018();
		EsitoCapiFilter filter = new EsitoCapiFilter();
		filter.setIdAllevamento(Long.parseLong("17420"));
		this.mvc.perform(get("/api/v1/istruttorie/du/zootecnia/".concat(String.valueOf(istruttoria.getId())).concat("/allevamenti/capi?").concat(String.valueOf(filter.getIdAllevamento()))))
		.andExpect(jsonPath("$.risultati").isArray())
		.andExpect(jsonPath("$.risultati", hasSize(10)))
		.andExpect(jsonPath("$.count", is(IsNull.notNullValue())))
		.andExpect(status().isOk());
	}
	
	@Test
	public void modificaCapo() throws Exception {
		EsitoCalcoloCapoModel capo = new EsitoCalcoloCapoModel();
		capo.setId(Long.parseLong("987654321"));
		capo.setDuplicato(true);
		capo.setControlloNonSuperato(true);
		mvc.perform(put(ApiUrls.ISTRUTTORIE_ACCOPPIATO_ZOOTECNIA_V1 + "/3333333666" + ApiUrls.CAPI + "/987654321")
				.content(mapperWrapper.asJsonString(capo))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").exists())
		.andExpect(jsonPath("$.duplicato").value(true))
		.andExpect(jsonPath("$.controlloNonSuperato").value(true));
	}
	
		@Test
		@Transactional
		@Sql(scripts = "/DomandaUnica/zootecnia/getCapiImpegnatiPerAGEA_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
		@Sql(scripts = "/DomandaUnica/zootecnia/getCapiImpegnatiPerAGEA_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
		public void getCapiImpegnatiPerAGEA() throws Exception {
			Integer annoCampagna = 2019;		
			MvcResult result = this.mvc.perform(get("/api/v1/istruttorie/du/zootecnia/".concat(String.valueOf(annoCampagna)).concat( "/allevamenti/capi/impegnati")))
					.andExpect(status().is2xxSuccessful())
	                .andExpect(header().string("Content-Disposition","attachment; filename=CAPI_ZOOTECNIA_APPAG.csv" ))
	                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
	                .andReturn();
			
			// System.out.println(result.getResponse().getContentAsString());
	        CsvMapper mapper = new CsvMapper();
	        CsvSchema elencoCapiSchema = CsvSchema.builder()
					.addColumn("id_capo_bdn", CsvSchema.ColumnType.STRING)
					.addColumn("codice_marca", CsvSchema.ColumnType.STRING)
					.addColumn("codice_intervento", CsvSchema.ColumnType.STRING)
					.addColumn("codice_fiscale_richiedente", CsvSchema.ColumnType.STRING)
					.addColumn("codice_organismo_pagatore", CsvSchema.ColumnType.STRING)
					.addColumn("codice_asl", CsvSchema.ColumnType.STRING)
					.addColumn("id_alle_bdn", CsvSchema.ColumnType.STRING)
					.addColumn("data", CsvSchema.ColumnType.STRING)
					.addColumn("anno", CsvSchema.ColumnType.STRING)
					.build().withColumnSeparator(';').withoutQuoteChar().withHeader();
	        MappingIterator<ElencoCapiCsv> elencoCapiWriter = mapper.readerFor(ElencoCapiCsv.class).with(elencoCapiSchema).readValues(result.getResponse().getContentAsByteArray());
	        List<ElencoCapiCsv> allRows = new ArrayList<ElencoCapiCsv>();
	        while (elencoCapiWriter.hasNext()) {
	        	ElencoCapiCsv row = elencoCapiWriter.next();
	        	allRows.add(row);
	        	assertThat(row.getIdCapoBdn(), is(notNullValue()));
	        	assertThat(row.getCodiceMarca(), is(notNullValue()));
	        	assertThat(row.getCodiceIntervento(), is(notNullValue()));
	        	assertTrue(row.getCodiceIntervento().contains("313"));
	        	assertThat(row.getCodiceFiscaleRichiedente(), is(notNullValue()));
	        	assertTrue(row.getCodiceFiscaleRichiedente().contains("ZTTMNL65P51B006O"));
	        	assertThat(row.getCodiceOrganismoPagatore(), is(notNullValue()));
	        	assertTrue(row.getCodiceOrganismoPagatore().contains("IT25"));
	        	assertThat(row.getCodiceAsl(), is(notNullValue()));
	        	assertTrue(row.getCodiceAsl().contains("168TN055"));
	        	assertThat(row.getIdAlleBdn(), is(notNullValue()));
	        	assertTrue(row.getIdAlleBdn().contains("17337"));
	        	assertThat(row.getData(), is(notNullValue()));
	        	assertThat(row.getAnno(), is(notNullValue()));
	        	assertTrue(row.getAnno().contains("2019"));
	        }
	        
	        assertTrue(allRows.get(0).getIdCapoBdn().contains("111830279"));
	        assertTrue(allRows.get(0).getCodiceMarca().contains("AT206901522"));
	        assertTrue(allRows.get(0).getData().contains("01/07/2019"));
	        
	        assertTrue(allRows.get(1).getIdCapoBdn().contains("119799984"));
	        assertTrue(allRows.get(1).getCodiceMarca().contains("DE0951745025"));
	        assertTrue(allRows.get(1).getData().contains("29/04/2019"));
	        
	        assertTrue(allRows.get(2).getIdCapoBdn().contains("114077286"));
	        assertTrue(allRows.get(2).getCodiceMarca().contains("IT022990171993"));
	        assertTrue(allRows.get(2).getData().contains("14/09/2019"));
	        
	        assertTrue(allRows.get(3).getIdCapoBdn().contains("102195234"));
	        assertTrue(allRows.get(3).getCodiceMarca().contains("AT326906817"));
	        assertTrue(allRows.get(3).getData().contains("20/03/2019"));
	        
	        assertTrue(allRows.get(4).getIdCapoBdn().contains("104573161"));
	        assertTrue(allRows.get(4).getCodiceMarca().contains("AT629764218"));
	        assertTrue(allRows.get(4).getData().contains("04/04/2019"));
		}
		
		@Test
		public void getProduzioniLatte() throws Exception {
			String annoCampagna = "2018";
			String cuaa = "SSNLSN69R19L378C";
			this.mvc.perform(get("/api/v1/istruttorie/du/zootecnia/".concat(annoCampagna).concat("/produzionelatte?cuaa=").concat(cuaa)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.[*].mesiProduzione").exists())
			.andExpect(jsonPath("$.[*].mesiProduzione.settembre").value(true))
			.andExpect(jsonPath("$.[*].mesiProduzione.gennaio").value(true))
			.andExpect(jsonPath("$.[*].mesiAnalisi").exists())
			.andExpect(jsonPath("$.[*].mesiAnalisi.gennaio").value(true))
			.andExpect(jsonPath("$.[*].tenoreCellule").exists())
			.andExpect(jsonPath("$.[*].tenoreCellule").value(0.0))
			.andExpect(jsonPath("$.[*].tenoreBatteri").exists())
			.andExpect(jsonPath("$.[*].tenoreBatteri").value(1.0))
			.andExpect(jsonPath("$.[*].contenutoProteine").exists())
			.andExpect(jsonPath("$.[*].contenutoProteine").value(1.0));
		}

		
		@Test
		public void getRegistrazioniAlpeggio() throws Exception {
			String annoCampagna = "2018";
			String cuaa = "SSNLSN69R19L378C";
			this.mvc.perform(get("/api/v1/istruttorie/du/zootecnia/".concat(annoCampagna).concat("/alpeggio?cuaa=").concat(cuaa)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.[*].id").exists())
			// .andExpect(jsonPath("$.[*].id").value(100135))
			.andExpect(jsonPath("$.[*].dtFine").exists())
			.andExpect(jsonPath("$.[*].dtFine").value("2018-09-01"))
			.andExpect(jsonPath("$.[*].dtInizio").exists())
			.andExpect(jsonPath("$.[*].dtInizio").value("2018-02-01"))
			.andExpect(jsonPath("$.[*].cuaa").exists())
			.andExpect(jsonPath("$.[*].cuaa").value(cuaa))
			.andExpect(jsonPath("$.[*].campagna").exists())
			.andExpect(jsonPath("$.[*].campagna").value(2018));
		}

		
		@Test
		public void getEtichettaturaCarne() throws Exception {
			String annoCampagna = "2018";
			String cuaa = "NDRLSN58T05H612X";
			this.mvc.perform(get("/api/v1/istruttorie/du/zootecnia/".concat(annoCampagna).concat("/etichettaturacarne?cuaa=").concat(cuaa)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.[*].id").exists())
			// .andExpect(jsonPath("$.[*].id").value(100147)) <-- Con le sequence è un controllo inutile e rischioso quando cambia il nextval
			.andExpect(jsonPath("$.[*].cuaa").exists())
			.andExpect(jsonPath("$.[*].cuaa").value(cuaa))
			.andExpect(jsonPath("$.[*].codiceAsl").exists())
			.andExpect(jsonPath("$.[*].codiceAsl").value("003FE330"))
			.andExpect(jsonPath("$.[*].dtInizioValidita").exists())
			.andExpect(jsonPath("$.[*].dtInizioValidita").value("2015-12-31T23:00:00.000+0000"))
			.andExpect(jsonPath("$.[*].dtFineValidita").exists())
			.andExpect(jsonPath("$.[*].dtFineValidita").value("2018-12-31T23:00:00.000+0000"));		
		}
		
		@Test
		public void getEtichettaturaCarneDtFineValiditaNull() throws Exception {
			String annoCampagna = "2018";
			String cuaa = "NDRLSN58T05H612Y";
			this.mvc.perform(get("/api/v1/istruttorie/du/zootecnia/".concat(annoCampagna).concat("/etichettaturacarne?cuaa=").concat(cuaa)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.[*].id").exists())
			// .andExpect(jsonPath("$.[*].id").value(100384)) <-- commentato perché è un test troppo rischioso: basta CUAA e codice ASL
			.andExpect(jsonPath("$.[*].cuaa").exists())
			.andExpect(jsonPath("$.[*].cuaa").value(cuaa))
			.andExpect(jsonPath("$.[*].codiceAsl").exists())
			.andExpect(jsonPath("$.[*].codiceAsl").value("003FE330"))
			.andExpect(jsonPath("$.[*].dtInizioValidita").exists())
			.andExpect(jsonPath("$.[*].dtInizioValidita").value("2015-12-31T23:00:00.000+0000"))
			.andExpect(jsonPath("$.[*].dtFineValidita").exists())
			.andExpect(jsonPath("$.[0].dtFineValidita", is(IsNull.nullValue())));		
		}

		private IstruttoriaModel getIstruttoria_ZZNRLB69M46F205A_2018() {
			String cuaa = "ZZNRLB69M46F205A";
			Integer campagna = 2018;

			DomandaUnicaModel domanda =
					domandaDao.findByCuaaIntestatarioAndCampagna(cuaa, campagna);
			IstruttoriaModel istruttoria =
					istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.ZOOTECNIA, TipoIstruttoria.SALDO);

			return istruttoria;
		}
}
