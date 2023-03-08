package it.tndigitale.a4gistruttoria;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.soc.client.model.ImportoLiquidato;
import it.tndigitale.a4g.soc.client.model.ImportoLiquidato.CausaleEnum;
import it.tndigitale.a4gistruttoria.builder.DomandaUnicaModelBuilder;
import it.tndigitale.a4g.soc.client.model.Debito;
import it.tndigitale.a4gistruttoria.dto.domandaunica.Istruttoria;
import it.tndigitale.a4gistruttoria.dto.lavorazione.EsitoControllo;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.ElencoLiquidazioneModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.util.ConsumeExternalRestApi4Soc;
import it.tndigitale.a4gistruttoria.util.TipoControllo;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;

@RunWith(SpringRunner.class)
@SpringBootTest
// @AutoConfigureTestDatabase
@AutoConfigureMockMvc
@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.visualizza"})
public class DomandaUnicaApplicationTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private DomandaUnicaDao domandaUnicaDao;
	
	@MockBean
    private IstruttoriaDao istruttoriaDao;
	
	@MockBean
	private ConsumeExternalRestApi4Soc consumeExternalRestApi4Soc;
	
	private static final EsitoControllo BRIDUSDC009_infoAgricoltoreAttivo_false = new EsitoControllo(TipoControllo.BRIDUSDC009_infoAgricoltoreAttivo,false);
	private static final EsitoControllo BRIDUSDC021_idDomandaSanzioni_inf_10 = new EsitoControllo(TipoControllo.BRIDUSDC021_idDomandaSanzioni,"inf_10");
	private static final VariabileCalcolo IMPCALCFIN_500 = new VariabileCalcolo(TipoVariabile.IMPCALCFIN,new BigDecimal("500"));
	private static final VariabileCalcolo DFIMPLIQDIS_450 = new VariabileCalcolo(TipoVariabile.DFIMPLIQDIS,new BigDecimal("450.50"));
	
	
	
	@Test
	public void getIstruttorie() throws Exception {
		DomandaUnicaModel testCase1 = istruttorieBuilderTest();
		
		ImportoLiquidato mockSoc = new ImportoLiquidato();
		mockSoc.setIncassatoNetto(new BigDecimal("123.32"));
		List<Debito> debitiSoc = new ArrayList<>();
		Debito debitoSoc0 = new Debito();
		Debito debitoSoc1 = new Debito();
		debitoSoc0.setImporto(new BigDecimal("600.40"));
		debitoSoc0.setDescrizioneCapitolo("descrizione1");
		debitiSoc.add(debitoSoc0);
		debitoSoc1.setImporto(new BigDecimal("0.42"));
		debitoSoc1.setDescrizioneCapitolo("descrizione2");
		debitiSoc.add(debitoSoc1);
		
		mockSoc.setDebiti(debitiSoc);
		mockSoc.setIdElencoLiquidazione(500L);
		List<ImportoLiquidato> mockSocs = new ArrayList<ImportoLiquidato>();
		mockSocs.add(mockSoc);
		
		ElencoLiquidazioneModel elencoLiquidazione = new ElencoLiquidazioneModel();
		elencoLiquidazione.setId(500L);
		testCase1.getA4gtLavorazioneSostegnos().forEach(ist -> ist.setElencoLiquidazione(elencoLiquidazione));
		List<IstruttoriaModel> istruttorie = new ArrayList<IstruttoriaModel>();
		istruttorie.addAll(testCase1.getA4gtLavorazioneSostegnos());
		// mock chiamata al DAO
		Mockito.when(domandaUnicaDao.findById(Mockito.any())).thenReturn(Optional.of(testCase1));
		Mockito.when(istruttoriaDao.findByDomandaUnicaModelId(Mockito.any())).thenReturn(istruttorie);
		// mock REST SOC Module - recupero debitiRecuperati
		Mockito.when(consumeExternalRestApi4Soc.retrieveImpLiquidazioneByApi(Mockito.any())).thenReturn(mockSocs);

		String responseString = mockMvc.perform(get("/api/v1/domandaunica/142/istruttorie")).andExpect(status().is2xxSuccessful()).andReturn().getResponse().getContentAsString();
		
		List<Istruttoria> response = Arrays.asList(objectMapper.readValue(responseString, Istruttoria[].class));
		assertFalse(response.isEmpty());
		assertNotNull(response.get(0).getEsitiControlli());

		// assertions
		assertNotNull(response);
		assertTrue(response.size() == 1);
		response.forEach(istruttoria -> {
			assertEquals(Sostegno.DISACCOPPIATO, istruttoria.getSostegno());
			assertEquals(TipoIstruttoria.ANTICIPO, istruttoria.getTipoIstruttoria());

			// check importi
			assertEquals(istruttoria.getImportiIstruttoria().getImportoCalcolato(), IMPCALCFIN_500.getValNumber());
			assertEquals(istruttoria.getImportiIstruttoria().getImportoAutorizzato(), DFIMPLIQDIS_450.getValNumber());
			
			List<EsitoControllo> esitiControlli = istruttoria.getEsitiControlli();
			assertNotNull(esitiControlli);
			assertTrue(esitiControlli.size() == 4);
			// check anomalie
			assertThat(esitiControlli).extracting("TipoControllo").containsExactlyInAnyOrder(
					BRIDUSDC021_idDomandaSanzioni_inf_10.getTipoControllo(),
					BRIDUSDC009_infoAgricoltoreAttivo_false.getTipoControllo(),
					BRIDUSDC021_idDomandaSanzioni_inf_10.getTipoControllo(),
					BRIDUSDC009_infoAgricoltoreAttivo_false.getTipoControllo());
		});
	}
	
	@Test
	public void getIstruttorieDisciplinaFinanziaria() throws Exception {
		DomandaUnicaModel testCase1 = istruttorieBuilderTest();
		
		ImportoLiquidato mockSoc = new ImportoLiquidato();
		mockSoc.setCausale(CausaleEnum.DISCIPLINA_FINANZIARIA);
		mockSoc.setDataEsecuzionePagamento(LocalDateTime.now());
		mockSoc.setIncassatoNetto(new BigDecimal("123.32"));
		List<Debito> debitiSoc = new ArrayList<>();
		Debito debitoSoc0 = new Debito();
		Debito debitoSoc1 = new Debito();
		debitoSoc0.setImporto(new BigDecimal("600.40"));
		debitoSoc0.setDescrizioneCapitolo("descrizione1");
		debitiSoc.add(debitoSoc0);
		debitoSoc1.setImporto(new BigDecimal("0.42"));
		debitoSoc1.setDescrizioneCapitolo("descrizione2");
		debitiSoc.add(debitoSoc1);
		
		mockSoc.setDebiti(debitiSoc);
		mockSoc.setIdElencoLiquidazione(500L);
		List<ImportoLiquidato> mockSocs = new ArrayList<ImportoLiquidato>();
		mockSocs.add(mockSoc);
		
		ElencoLiquidazioneModel elencoLiquidazione = new ElencoLiquidazioneModel();
		elencoLiquidazione.setId(500L);
		testCase1.getA4gtLavorazioneSostegnos().forEach(ist -> ist.setElencoLiquidazione(elencoLiquidazione));
		List<IstruttoriaModel> istruttorie = new ArrayList<IstruttoriaModel>();
		istruttorie.addAll(testCase1.getA4gtLavorazioneSostegnos());
		// mock chiamata al DAO
		Mockito.when(domandaUnicaDao.findById(Mockito.any())).thenReturn(Optional.of(testCase1));
		Mockito.when(istruttoriaDao.findByDomandaUnicaModelId(Mockito.any())).thenReturn(istruttorie);
		// mock REST SOC Module - recupero debitiRecuperati
		Mockito.when(consumeExternalRestApi4Soc.retrieveImpLiquidazioneByApi(Mockito.any())).thenReturn(mockSocs);

		String responseString = mockMvc.perform(get("/api/v1/domandaunica/142/istruttorie")).andExpect(status().is2xxSuccessful()).andReturn().getResponse().getContentAsString();
		
		List<Istruttoria> response = Arrays.asList(objectMapper.readValue(responseString, Istruttoria[].class));
		assertFalse(response.isEmpty());
		assertNotNull(response.get(0).getEsitiControlli());

		// Assertions
		assertNotNull(response.get(0));
		assertEquals(Sostegno.DISACCOPPIATO, response.get(0).getSostegno());
		assertEquals(TipoIstruttoria.ANTICIPO, response.get(0).getTipoIstruttoria());

		// Check importi
		assertEquals(response.get(0).getImportiIstruttoria().getImportoCalcolato(), IMPCALCFIN_500.getValNumber());
		assertEquals(response.get(0).getImportiIstruttoria().getImportoAutorizzato(), DFIMPLIQDIS_450.getValNumber());
		
		List<EsitoControllo> esitiControlli = response.get(0).getEsitiControlli();
		assertNotNull(esitiControlli);
		assertTrue(esitiControlli.size() == 4);
		// Check anomalie
		assertThat(esitiControlli).extracting("TipoControllo").containsExactlyInAnyOrder(
				BRIDUSDC021_idDomandaSanzioni_inf_10.getTipoControllo(),
				BRIDUSDC009_infoAgricoltoreAttivo_false.getTipoControllo(),
				BRIDUSDC021_idDomandaSanzioni_inf_10.getTipoControllo(),
				BRIDUSDC009_infoAgricoltoreAttivo_false.getTipoControllo());
		
		// Disciplina Finanziaria
		assertNotNull(response.get(1));
		assertEquals(mockSoc.getIncassatoNetto(), response.get(1).getImportiIstruttoria().getImportoDisciplina());
		assertEquals(mockSoc.getDataEsecuzionePagamento(), response.get(1).getDtUltimoCalcolo());
		assertNotNull(response.get(1).getEsitiControlli());
		assertEquals(mockSoc.getDebiti().get(0).getImporto(), response.get(1).getImportiIstruttoria().getDebitiRecuperati().get(0).getImporto());
		assertEquals(mockSoc.getDebiti().get(1).getImporto(), response.get(1).getImportiIstruttoria().getDebitiRecuperati().get(1).getImporto());
		assertEquals(mockSoc.getDebiti().get(0).getDescrizioneCapitolo(), response.get(1).getImportiIstruttoria().getDebitiRecuperati().get(0).getDescrizione());
		assertEquals(mockSoc.getDebiti().get(1).getDescrizioneCapitolo(), response.get(1).getImportiIstruttoria().getDebitiRecuperati().get(1).getDescrizione());

	}
	
	/*
	 * recupero anomalie e importi 
	 * Domanda anticipo disaccoppiato
	 * 
	 */
	private DomandaUnicaModel istruttorieBuilderTest() throws JsonProcessingException {
		DomandaUnicaModel domanda = new DomandaUnicaModel() {

			private static final long serialVersionUID = -7895270478401171702L;

			@Override
			public Integer getCampagna() {
				return new Integer("2018");
			}
			
		};
		
		// dati passi - recupero anomalie
		List<EsitoControllo> anomalie = new ArrayList<>();
		anomalie.add(BRIDUSDC009_infoAgricoltoreAttivo_false);
		anomalie.add(BRIDUSDC021_idDomandaSanzioni_inf_10);
		
		// dati passi - recupero importi
		List<VariabileCalcolo> variabiliCalcoloIMPCALCFIN = new ArrayList<>();
		variabiliCalcoloIMPCALCFIN.add(IMPCALCFIN_500);
		
		List<VariabileCalcolo> variabiliCalcoloDFIMPLIQDIS = new ArrayList<>();
		variabiliCalcoloDFIMPLIQDIS.add(DFIMPLIQDIS_450);
		
		// crea passi - recupero anomalie - recupero importi
		Set<PassoTransizioneModel> passi = new HashSet<>();
		passi.add(
				DomandaUnicaModelBuilder.buildPassoLavorazione(
						TipologiaPassoTransizione.CONTROLLI_FINALI,
						DomandaUnicaModelBuilder.buildDatiSintesi(null, anomalie),
						DomandaUnicaModelBuilder.buildDatiOutput(variabiliCalcoloIMPCALCFIN)
				)
		);
		
		passi.add(
				DomandaUnicaModelBuilder.buildPassoLavorazione(
						TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA,
						DomandaUnicaModelBuilder.buildDatiSintesi(Collections.emptyList(), Collections.emptyList()),
						DomandaUnicaModelBuilder.buildDatiOutput(variabiliCalcoloDFIMPLIQDIS)
				)
		);
		
		// Creo l'istruttoria:
		// 2. creo l'istruttoria
		IstruttoriaModel istruttoria = DomandaUnicaModelBuilder.buildLavorazioneSostegno(domanda,
				Sostegno.DISACCOPPIATO,
				StatoIstruttoria.PAGAMENTO_AUTORIZZATO,
				TipoIstruttoria.ANTICIPO);
		
		// 1. creo 2 transizioni
		List<TransizioneIstruttoriaModel> transizioni = 
			Arrays.asList(
					DomandaUnicaModelBuilder.buildTransizioneSostegno(
							istruttoria, 
							StatoIstruttoria.CONTROLLI_CALCOLO_OK,
							passi
					), 
					DomandaUnicaModelBuilder.buildTransizioneSostegno(
							istruttoria, 
							StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK,
							passi
					)
			);
			
			
		
		// 3. Associo le due transizioni all'istruttoria
		istruttoria.setTransizioni(new HashSet<>(transizioni));
		
		// 4. Associo l'istruttoria alla domanda
		domanda.setA4gtLavorazioneSostegnos(
				new HashSet<>(Arrays.asList(istruttoria))
		);
		
		// crea dati settore
//		domanda.setA4gtDatiSettore(
//				DomandaUnicaModelBuilder.buildDatiSettore(TipoIstruttoria.ANTICIPO,
//						new BigDecimal("2018"))
//		);
		
		domanda.setId(142L);
		domanda.setCuaaIntestatario("CRLMRA43P07G452Z");
		domanda.setNumeroDomanda(new BigDecimal("142142"));
		
		return domanda;
	}
}
