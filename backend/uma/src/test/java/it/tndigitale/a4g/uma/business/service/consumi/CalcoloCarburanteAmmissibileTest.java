package it.tndigitale.a4g.uma.business.service.consumi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityNotFoundException;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.client.model.FabbricatoAgsDto;
import it.tndigitale.a4g.fascicolo.territorio.client.model.ColturaDto;
import it.tndigitale.a4g.fascicolo.territorio.client.model.ParticellaDto;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.uma.builder.ColturaTestBuilder;
import it.tndigitale.a4g.uma.builder.ParticellaTestBuilder;
import it.tndigitale.a4g.uma.business.persistence.entity.DichiarazioneConsumiModel;
import it.tndigitale.a4g.uma.business.persistence.repository.DichiarazioneConsumiDao;
import it.tndigitale.a4g.uma.business.service.client.UmaDotazioneTecnicaClient;
import it.tndigitale.a4g.uma.business.service.client.UmaTerritorioClient;
import it.tndigitale.a4g.uma.business.service.utente.AbilitazioniComponent;
import it.tndigitale.a4g.uma.dto.consumi.CarburanteDto;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente")
class CalcoloCarburanteAmmissibileTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private AbilitazioniComponent abilitazioniComponent;
	@MockBean
	private UmaTerritorioClient territorioClient;
	@MockBean
	private UmaDotazioneTecnicaClient dotazioneTecnicaClient;
	@Autowired
	private DichiarazioneConsumiDao dichiarazioneConsumiDao;

	@BeforeEach
	private void mockUtenteConnesso() throws Exception {
		Mockito.when(abilitazioniComponent.checkRicercaDichiarazioneConsumi(Mockito.anyLong())).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkRicercaDomandeUma()).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkModificaRichiestaCarburante(Mockito.anyLong())).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkPresentaDomandaUma(Mockito.any())).thenReturn(true);
	}

	@Transactional
	@Test
	@Sql(scripts = "/sql/consumi/consumi_ammissibile_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consumi_ammissibile_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void calcolaCarburanteAmmissibileSuperficiSuccessful() throws Exception {
		ColturaTestBuilder cb = new ColturaTestBuilder();
		ParticellaTestBuilder pb = new ParticellaTestBuilder();

		// destinazioneUso, suolo, qualita, uso, varieta 
		ColturaDto viteDaVino1 = cb.newDto().descrizione("15483 - 10_ViteDaVino").withCodifica("009","410","000","037","437").withSuperficie(150).build();
		ColturaDto viteDaVino2 = cb.newDto().descrizione("15519 - 10_ViteDaVino").withCodifica("005","410","000","037","593").withSuperficie(400).build();
		ColturaDto lattuga = cb.newDto().descrizione("15519 - 8_LattugaInsalate").withCodifica("007","919","000","000","000").withSuperficie(500000).build();

		ParticellaDto particella1 = pb.newDto().withInfoCatastali("NA", "9999", "A184", null).addColtura(viteDaVino1).addColtura(viteDaVino2).build();
		ParticellaDto particella2 = pb.newDto().withInfoCatastali("TN", "9999", "A184", "CE").addColtura(viteDaVino1).addColtura(lattuga).build();
		ParticellaDto particella3 = pb.newDto().withInfoCatastali("TN", "9998", "A200", "CA").addColtura(viteDaVino1).addColtura(lattuga).build();

		// per validare l'equals su particella
		List<ParticellaDto> pianoColturalePrimoNovembre = Arrays.asList(particella1, particella3);

		List<ParticellaDto> pianoColturaleDataProtocollazioneRichesta = Arrays.asList(particella1, particella2);

		DichiarazioneConsumiModel dichiarazioneConsumi = dichiarazioneConsumiDao.findById(3000L).get();

		// data protocollazione richiesta 7728 - 2021-02-22 12:29:51.000000
		Mockito.when(territorioClient.getColture(Mockito.any(), Mockito.eq(dichiarazioneConsumi.getRichiestaCarburante().getDataProtocollazione()))).thenReturn(pianoColturaleDataProtocollazioneRichesta);
		// data conduzione dichiarazione consumi - 3000 - 2021-11-01 23:59:59.000000
		// data presentazione dichiarazione consumi - 
		LocalDateTime dataConduzione = dichiarazioneConsumi.getDataConduzione();
		Mockito.when(territorioClient.getColture(Mockito.any(), Mockito.eq(dataConduzione))).thenReturn(pianoColturalePrimoNovembre);

		// mock fabbricati 
		Mockito.when(dotazioneTecnicaClient.getFabbricati(Mockito.any(), Mockito.any())).thenReturn(null);

		String response = mockMvc.perform(get("/api/v1/consumi/3000/carburante"))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		CarburanteDto dto = objectMapper.readValue(response, CarburanteDto.class);
		// 550 benzina ->  550 lavorazione 1248 
		// 1050 gasolio -> 550 lavorazione 1264 + 500 lavorazione 1247
		// 0 serre
		assertEquals(14, dto.getBenzina());
		assertEquals(9, dto.getGasolio());
		assertEquals(0, dto.getGasolioSerre());
	}

	@Transactional
	@Test
	@Sql(scripts = "/sql/consumi/consumi_ammissibile_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consumi_ammissibile_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void calcolaCarburanteAmmissibileFabbricatiSuccessful() throws Exception {
		DichiarazioneConsumiModel dichiarazioneConsumi = dichiarazioneConsumiDao.findById(3000L).get();

		// data protocollazione richiesta 7728 - 2021-02-22 12:29:51.000000
		Mockito.when(territorioClient.getColture(Mockito.any(), Mockito.eq(dichiarazioneConsumi.getRichiestaCarburante().getDataProtocollazione()))).thenReturn(null);

		// data conduzione dichiarazione consumi - 3000 - 2021-11-01 23:59:59.000000
		LocalDateTime dataConduzione = Clock.ofEndOfDay(LocalDate.of(2021, Month.NOVEMBER, 1));
		Mockito.when(territorioClient.getColture(Mockito.any(), Mockito.eq(dataConduzione))).thenReturn(null);

		// mock fabbricati 
		var fab1 = new FabbricatoAgsDto();
		fab1.setIdAgs(17L);	// serra
		var fab3 = new FabbricatoAgsDto();
		fab3.setIdAgs(20L);	// caseificio
		var fab2 = new FabbricatoAgsDto();
		fab2.setIdAgs(999L); //da scartare (presente al primo novembre ma non alla richiesta di carburante)

		// fab 4 da scartare (non presente al primo novembre ma presente alla data di creazione richiesta)
		List<FabbricatoAgsDto> fabbricatiMock = Arrays.asList(fab1,fab2,fab3);

		Mockito.when(dotazioneTecnicaClient.getFabbricati(Mockito.any(), Mockito.any())).thenReturn(fabbricatiMock);

		String response = mockMvc.perform(get("/api/v1/consumi/7762/carburante"))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		CarburanteDto dto = objectMapper.readValue(response, CarburanteDto.class);

		assertEquals(0, dto.getBenzina());
		assertEquals(9029, dto.getGasolio());
		assertEquals(1431, dto.getGasolioSerre());
	}

	@Test
	void calcolaCarburanteAmmissibileSuperficiNoDichiarazione() throws Exception {
		NestedServletException exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(get("/api/v1/consumi/9876532234/carburante"));
		});
		assertEquals(EntityNotFoundException.class, exception.getCause().getClass());
	}

}
