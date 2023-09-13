package it.tndigitale.a4g.uma.business.service.richiesta.lavorazioni;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.uma.business.persistence.entity.AmbitoLavorazione;
import it.tndigitale.a4g.uma.business.persistence.entity.TipoCarburante;
import it.tndigitale.a4g.uma.business.service.client.UmaTerritorioClient;
import it.tndigitale.a4g.uma.business.service.utente.AbilitazioniComponent;
import it.tndigitale.a4g.uma.dto.richiesta.DichiarazioneDto;
import it.tndigitale.a4g.uma.dto.richiesta.RaggruppamentoLavorazioniDto;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente")
class LavorazioniSuperficieControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private AbilitazioniComponent abilitazioniComponent;
	@MockBean
	private Clock clock;
	@MockBean
	private UmaTerritorioClient territorioClient;


	@BeforeEach
	private void mockUtenteConnesso() throws Exception {
		Mockito.when(abilitazioniComponent.checkRicercaDomandeUma()).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkRicercaRichiestaDiCarburante(Mockito.anyLong())).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkModificaRichiestaCarburante(Mockito.anyLong())).thenReturn(true);
	}
	/*
	@Test
	@Sql(scripts = "/sql/lavorazioni/get_lavorazioni_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/lavorazioni/get_lavorazioni_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getLavorazioniSuccessful() throws Exception {

		ParticellaDto particella1 = new ParticellaBuilder()
				.withInfoCatastali("012", "9999", ".1522", null)
				.addColtura(new ColturaBuilder().descrizione("2 - colturaMaisSorgo").withCodifica("008", "001", "042", "000", "000").withSuperficie(5253).build())
				.addColtura(new ColturaBuilder().descrizione("6 - colturaPascolo")  .withCodifica("000", "382", "000", "009", "000").withSuperficie(1422).build())
				.build();

		ParticellaDto particella2 = new ParticellaBuilder()
				.withInfoCatastali("621", "9999", "01514/A", "A")
				.addColtura(new ColturaBuilder().descrizione("8 - colturaLattugheInsalateRadicchi")  .withCodifica("007", "919", "000", "000", "000").withSuperficie(123).build())
				.addColtura(new ColturaBuilder().descrizione("8 - colturaLattugheInsalateRadicchi_1").withCodifica("008", "680", "000", "000", "000").withSuperficie(625).build())
				.addColtura(new ColturaBuilder().descrizione("10 - colturaViteDaVinoDaTavola")       .withCodifica("005", "410", "000", "000", "507").withSuperficie(1652).build())
				.build();

		// scelta in modo tale che non rientri nel computo della superficie massima. Coltura per cui non posso richiedere contributi
		ParticellaDto particella3 = new ParticellaBuilder()
				.withInfoCatastali("621", "9999", "01514/A", "A")
				.addColtura(new ColturaBuilder().descrizione("colturaFake").withCodifica("000", "000", "000", "000", "000").withSuperficie(99999).build())
				.build();

		Mockito.when(territorioClient.getColture(Mockito.any(), Mockito.any())).thenReturn(Arrays.asList(particella1,particella2,particella3));
		Mockito.when(clock.today()).thenReturn(LocalDate.of(2020,Month.NOVEMBER, 1));

		String stringResponse = mockMvc.perform(get("/api/v1/richieste/2/lavorazioni").param("ambito", AmbitoLavorazione.SUPERFICIE.name()))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		List<RaggruppamentoLavorazioniDto> response = objectMapper.readValue(stringResponse, new TypeReference<ArrayList<RaggruppamentoLavorazioniDto>>(){})
				.stream()
				.sorted(Comparator.comparingInt(RaggruppamentoLavorazioniDto::getIndice))
				.collect(Collectors.toList());

		assertEquals(4,response.size());

		assertEquals(2, response.get(0).getIndice());
		assertEquals(6, response.get(1).getIndice());
		assertEquals(8, response.get(2).getIndice());
		assertEquals(10, response.get(3).getIndice());

		assertEquals(14, response.get(0).getLavorazioni().size());
		assertEquals(2, response.get(1).getLavorazioni().size());
		assertEquals(16, response.get(2).getLavorazioni().size());
		assertEquals(10, response.get(3).getLavorazioni().size());

		assertEquals(5253, response.get(0).getSuperficieMassima());
		assertEquals(1422, response.get(1).getSuperficieMassima());
		assertEquals(748, response.get(2).getSuperficieMassima());
		assertEquals(1652, response.get(3).getSuperficieMassima());

	}

	@Test
	@Sql(scripts = "/sql/lavorazioni/get_lavorazioni_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/lavorazioni/get_lavorazioni_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getLavorazioniRichiestaAutorizzataSuccessful() throws Exception {

		String stringResponse = mockMvc.perform(get("/api/v1/richieste/2022/lavorazioni").param("ambito", AmbitoLavorazione.SUPERFICIE.name()))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		List<RaggruppamentoLavorazioniDto> response = objectMapper.readValue(stringResponse, new TypeReference<ArrayList<RaggruppamentoLavorazioniDto>>(){})
				.stream()
				.sorted(Comparator.comparingInt(RaggruppamentoLavorazioniDto::getIndice))
				.collect(Collectors.toList());

		assertEquals(2, response.get(0).getIndice());
		assertEquals(10, response.get(1).getIndice());
		assertEquals(1234, response.get(0).getSuperficieMassima());
		assertEquals(0, response.get(1).getSuperficieMassima());
	}

	@Test
	@Sql(scripts = "/sql/lavorazioni/get_lavorazioni_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/lavorazioni/get_lavorazioni_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getLavorazioniRispostaTerritorioVuota() throws Exception {

		Mockito.when(territorioClient.getColture(Mockito.any(), Mockito.any())).thenReturn(new ArrayList<>());
		Mockito.when(clock.today()).thenReturn(LocalDate.of(2020,Month.NOVEMBER, 1));

		mockMvc.perform(get("/api/v1/richieste/2/lavorazioni").param("ambito", AmbitoLavorazione.SUPERFICIE.name()))
		.andExpect(status().isNoContent());

	}

	@Test
	@Sql(scripts = "/sql/lavorazioni/get_lavorazioni_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/lavorazioni/get_lavorazioni_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getFabbisogniSuccesful() throws Exception {

		String stringResponse = mockMvc.perform(get("/api/v1/richieste/4/fabbisogni").param("ambito", AmbitoLavorazione.SUPERFICIE.name()))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse().getContentAsString();

		List<DichiarazioneDto> response = objectMapper.readValue(stringResponse, new TypeReference<ArrayList<DichiarazioneDto>>(){})
				.stream()
				.sorted(Comparator.comparingLong(DichiarazioneDto::getLavorazioneId))
				.collect(Collectors.toList());

		assertEquals(1,response.size());
		assertEquals(1265L, response.get(0).getLavorazioneId());
		assertEquals(2, response.get(0).getFabbisogni().size());

		assertTrue(response.get(0).getFabbisogni().stream().anyMatch(f -> {
			return TipoCarburante.GASOLIO.equals(f.getCarburante()) && 
					new BigDecimal("632").compareTo(f.getQuantita()) == 0;
		}));
		assertTrue(response.get(0).getFabbisogni().stream().anyMatch(f -> {
			return TipoCarburante.BENZINA.equals(f.getCarburante()) && 
					new BigDecimal("128").compareTo(f.getQuantita()) == 0;
		}));
	}
	private class ColturaBuilder {

		private ColturaDto colturaDto;
		ColturaBuilder() {
			colturaDto = new ColturaDto();
		}

		ColturaBuilder descrizione(String descrizioneUtileAlTest) {
			return this;
		}

		ColturaBuilder withCodifica(String destinazioneUso, String suolo, String qualita, String uso, String varieta) {
			CodificaColtura codificaColtura = new CodificaColtura();
			codificaColtura.setCodiceDestinazioneUso(destinazioneUso);
			codificaColtura.setCodiceSuolo(suolo);
			codificaColtura.setCodiceQualita(qualita);
			codificaColtura.setCodiceUso(uso);
			codificaColtura.setCodiceVarieta(varieta);
			colturaDto.setCodifica(codificaColtura);
			return this;
		}

		ColturaBuilder withSuperficie(Integer superficieAccertata) {
			colturaDto.setSuperficieAccertata(superficieAccertata);
			return this;
		}

		ColturaDto build() {
			return colturaDto;
		}
	}

	private class ParticellaBuilder {

		private ParticellaDto particellaDto;

		ParticellaBuilder() {
			particellaDto = new ParticellaDto();
			particellaDto.setColture(new ArrayList<>());
		}

		ParticellaBuilder withInfoCatastali(String codNazionale, String foglio,String numero, String sub) {
			particellaDto.setCodiceNazionale(codNazionale);
			particellaDto.setFoglio(foglio);
			particellaDto.setNumero(numero);
			particellaDto.setSubalterno(sub);
			return this;
		}

		ParticellaBuilder addColtura(ColturaDto coltura) {
			particellaDto.addColtureItem(coltura);
			return this;
		}

		ParticellaDto build() {
			return particellaDto;
		}
	}
	*/
}
