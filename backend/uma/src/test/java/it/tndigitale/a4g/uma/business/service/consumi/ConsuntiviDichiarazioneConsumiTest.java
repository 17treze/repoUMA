package it.tndigitale.a4g.uma.business.service.consumi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockPart;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.uma.api.ApiUrls;
import it.tndigitale.a4g.uma.business.persistence.entity.MotivazioneConsuntivo;
import it.tndigitale.a4g.uma.business.persistence.entity.TipoAllegatoConsuntivo;
import it.tndigitale.a4g.uma.business.persistence.entity.TipoCarburanteConsuntivo;
import it.tndigitale.a4g.uma.business.persistence.entity.TipoConsuntivo;
import it.tndigitale.a4g.uma.business.service.utente.AbilitazioniComponent;
import it.tndigitale.a4g.uma.dto.consumi.ConsuntivoDto;
import it.tndigitale.a4g.uma.dto.consumi.InfoAllegatoConsuntivoDto;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente")
class ConsuntiviDichiarazioneConsumiTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private AbilitazioniComponent abilitazioniComponent;
	@MockBean 
	private UtenteComponent utenteComponent;

	@BeforeEach
	private void mockUtenteConnesso() throws Exception {
		Mockito.when(abilitazioniComponent.checkRicercaDomandeUma()).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkModificaRichiestaCarburante(Mockito.anyLong())).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkModificaDichiarazioneConsumi(Mockito.anyLong())).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkPresentaDomandaUma(Mockito.any())).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkRicercaDichiarazioneConsumi(Mockito.any())).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkModificaConsuntiviDichiarazioneConsumi(Mockito.any(), Mockito.any())).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkRicercaAllegatiConsuntivoDichiarazioneConsumi(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);
		Mockito.when(utenteComponent.utenza()).thenReturn("utente");
	}

	@Test
	@Sql(scripts = "/sql/consumi/consuntivi/consuntivi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consuntivi/consuntivi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getConsuntiviConsumiByIdSuccessful() throws Exception {
		String stringResponse = mockMvc.perform(get(ApiUrls.CONSUMI + "/7761" + ApiUrls.CONSUNTIVI))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		List<ConsuntivoDto> response = objectMapper.readValue(stringResponse, new TypeReference<ArrayList<ConsuntivoDto>>(){});

		assertEquals(2, response.size());

		ConsuntivoDto cons1 = response.stream().filter(cons -> cons.getId().equals(16222L)).findFirst().get();
		assertEquals(16222L, cons1.getId());
		assertEquals(TipoCarburanteConsuntivo.BENZINA, cons1.getCarburante());
		assertEquals(TipoConsuntivo.RIMANENZA, cons1.getTipo());
		assertEquals(7, cons1.getQuantita());

		ConsuntivoDto cons2 = response.stream().filter(cons -> cons.getId().equals(16706L)).findFirst().get();
		assertEquals(16706L, cons2.getId());
		assertEquals(TipoCarburanteConsuntivo.GASOLIO, cons2.getCarburante());
		assertEquals(TipoConsuntivo.CONSUMATO, cons2.getTipo());
		assertEquals(1, cons2.getQuantita());
	}
	
	@Test
	@Sql(scripts = "/sql/consumi/consuntivi/consuntivi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consuntivi/consuntivi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getConsuntiviConsumiByIdSuccessfulWithAllegati() throws Exception {
		mockMvc.perform(get(ApiUrls.CONSUMI + "/7763" + ApiUrls.CONSUNTIVI))
				.andExpect(status().isOk());
	}
	
	@Test
	@Sql(scripts = "/sql/consumi/consuntivi/consuntivi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consuntivi/consuntivi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getAllegatoSuccessful() throws Exception {
		mockMvc.perform(get(ApiUrls.CONSUMI + "/7763" + ApiUrls.CONSUNTIVI + "/325" + ApiUrls.ALLEGATI + "/167" + ApiUrls.STAMPA))
				.andExpect(status().isOk());
	}

	@Test
	@Sql(scripts = "/sql/consumi/consuntivi/consuntivi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consuntivi/consuntivi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void dichiaraConsuntiviSuccessful() throws Exception {
		ConsuntivoDto rimanenzaGasolioTerziMock = new ConsuntivoDto()
				.setInfoAllegati(null)
				.setCarburante(TipoCarburanteConsuntivo.GASOLIO_TERZI)
				.setId(333L)
				.setQuantita(33)
				.setTipo(TipoConsuntivo.RIMANENZA);
		ConsuntivoDto consumatoBenzinaMock = new ConsuntivoDto()
				.setInfoAllegati(null)
				.setCarburante(TipoCarburanteConsuntivo.BENZINA)
				.setId(336L)
				.setQuantita(36)
				.setTipo(TipoConsuntivo.CONSUMATO);

		List<ConsuntivoDto> consuntiviDaDichiarare = new ArrayList<>();
		consuntiviDaDichiarare.add(rimanenzaGasolioTerziMock);
		consuntiviDaDichiarare.add(consumatoBenzinaMock);

		mockMvc.perform(post(ApiUrls.CONSUMI + "/7761" + ApiUrls.CONSUNTIVI)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(consuntiviDaDichiarare)))
		.andExpect(status().isOk());
	}
	
	@Test
	@Sql(scripts = "/sql/consumi/consuntivi/consuntivi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consuntivi/consuntivi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void dichiaraAllegatiAmmissibileSuccessful() throws Exception {
		String fileNameAllegatoAmmissibile = "nomefile.pdf" + "$$" + TipoAllegatoConsuntivo.AUTOCERTIFICAZIONE.name() + "$$" + "Descrizione file di molti caratteri";

		List<InfoAllegatoConsuntivoDto> allegatiDaConservare = Arrays.asList(
				new InfoAllegatoConsuntivoDto().setId(168L));

		ConsuntivoDto ammissibileGasolioTerziMock = new ConsuntivoDto()
				.setInfoAllegati(allegatiDaConservare)
				.setCarburante(TipoCarburanteConsuntivo.GASOLIO_TERZI)
				.setId(null)
				.setMotivazione(MotivazioneConsuntivo.ASSEGNAZIONE_SVINCOLATA)
				.setQuantita(12)
				.setTipo(TipoConsuntivo.AMMISSIBILE);

		MockPart filePart = new MockPart("allegati", fileNameAllegatoAmmissibile, "some pdf binary data".getBytes());
		filePart.getHeaders().setContentType(MediaType.APPLICATION_PDF);

		MockPart jsonPart = new MockPart("consuntivo", "consuntivo", objectMapper.writeValueAsString(ammissibileGasolioTerziMock).getBytes());
		jsonPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);

		mockMvc.perform(multipart(ApiUrls.CONSUMI + "/7763" + ApiUrls.ALLEGATI)
				.part(filePart)
				.part(jsonPart))
		.andExpect(status().isOk());
	}
	
	@Test
	@Sql(scripts = "/sql/consumi/consuntivi/consuntivi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consuntivi/consuntivi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void dichiaraAllegatiRecuperoSuccessful() throws Exception {
		String fileNameAllegatoRecupero = "nomefile.pdf" + "$$" + TipoAllegatoConsuntivo.AUTOCERTIFICAZIONE.name() + "$$" + "Descrizione";

		List<InfoAllegatoConsuntivoDto> allegatiDaConservare = Arrays.asList(
				new InfoAllegatoConsuntivoDto().setId(168L));

		ConsuntivoDto recuperoGasolioMock = new ConsuntivoDto()
				.setInfoAllegati(allegatiDaConservare)
				.setCarburante(TipoCarburanteConsuntivo.GASOLIO)
				.setId(null)
				.setMotivazione(MotivazioneConsuntivo.FURTO)
				.setQuantita(12)
				.setTipo(TipoConsuntivo.RECUPERO);

		MockPart filePart = new MockPart("allegati", fileNameAllegatoRecupero, "some pdf binary data".getBytes());
		filePart.getHeaders().setContentType(MediaType.APPLICATION_PDF);

		MockPart jsonPart = new MockPart("consuntivo", "consuntivo", objectMapper.writeValueAsString(recuperoGasolioMock).getBytes());
		jsonPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);

		mockMvc.perform(multipart(ApiUrls.CONSUMI + "/7761" + ApiUrls.ALLEGATI)
				.part(filePart)
				.part(jsonPart))
		.andExpect(status().isOk());
	}
	
	@Test
	@Sql(scripts = "/sql/consumi/consuntivi/consuntivi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consuntivi/consuntivi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void dichiaraAllegatiError() throws Exception {
		ConsuntivoDto recuperoGasolioMock = new ConsuntivoDto()
				.setInfoAllegati(null)
				.setCarburante(TipoCarburanteConsuntivo.GASOLIO)
				.setId(null)
				.setMotivazione(null)
				.setQuantita(12)
				.setTipo(TipoConsuntivo.RIMANENZA);

		MockPart jsonPart = new MockPart("consuntivo", "consuntivo", objectMapper.writeValueAsString(recuperoGasolioMock).getBytes());
		jsonPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);

		NestedServletException exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(multipart(ApiUrls.CONSUMI + "/7761" + ApiUrls.ALLEGATI)
					.part(jsonPart));
		});
		
		assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
		assertEquals("Non è possibile salvare allegati al consuntivo RIMANENZA GASOLIO", exception.getCause().getMessage());
	}

	@Test
	@Sql(scripts = "/sql/consumi/consuntivi/consuntivi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consuntivi/consuntivi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void dichiaraAllegatiRecuperoSalvataggioError() throws Exception {
		ConsuntivoDto recuperoGasolioMock = new ConsuntivoDto()
				.setInfoAllegati(new ArrayList<>())
				.setCarburante(TipoCarburanteConsuntivo.GASOLIO)
				.setId(null)
				.setMotivazione(MotivazioneConsuntivo.FURTO)
				.setQuantita(0)
				.setTipo(TipoConsuntivo.RECUPERO);

		MockPart filePart = new MockPart("allegati", "", "some pdf binary data".getBytes());
		filePart.getHeaders().setContentType(MediaType.APPLICATION_PDF);

		MockPart jsonPart = new MockPart("consuntivo", "consuntivo", objectMapper.writeValueAsString(recuperoGasolioMock).getBytes());
		jsonPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);

		NestedServletException exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(multipart(ApiUrls.CONSUMI + "/7761" + ApiUrls.ALLEGATI)
					.part(filePart)
					.part(jsonPart));
		});

		assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
		assertEquals("Errore Salvataggio consuntivo RECUPERO GASOLIO", exception.getCause().getMessage());
	}

	@Test
	@Sql(scripts = "/sql/consumi/consuntivi/consuntivi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consuntivi/consuntivi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void dichiaraAllegatiRecuperoNoMotivazione() throws Exception {
		String fileNameAllegatoRecupero = "nomefile.pdf" + "$$" + TipoAllegatoConsuntivo.ALTRO.name() + "$$" + "Descrizione";

		List<InfoAllegatoConsuntivoDto> allegatiDaConservare = Arrays.asList(
				new InfoAllegatoConsuntivoDto().setId(168L));

		ConsuntivoDto recuperoGasolioMock = new ConsuntivoDto()
				.setInfoAllegati(allegatiDaConservare)
				.setCarburante(TipoCarburanteConsuntivo.GASOLIO)
				.setId(null)
				.setMotivazione(null)
				.setQuantita(10)
				.setTipo(TipoConsuntivo.RECUPERO);

		MockPart filePart = new MockPart("allegati", fileNameAllegatoRecupero, "some pdf binary data".getBytes());
		filePart.getHeaders().setContentType(MediaType.APPLICATION_PDF);

		MockPart jsonPart = new MockPart("consuntivo", "consuntivo", objectMapper.writeValueAsString(recuperoGasolioMock).getBytes());
		jsonPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);

		NestedServletException exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(multipart(ApiUrls.CONSUMI + "/7761" + ApiUrls.ALLEGATI)
					.part(filePart)
					.part(jsonPart));
		});

		assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
		assertEquals("Nessuna Motivazione espressa per il campo RECUPERO GASOLIO", exception.getCause().getMessage());
	}

	@Test
	@Sql(scripts = "/sql/consumi/consuntivi/consuntivi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consuntivi/consuntivi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void dichiaraAllegatiRecuperoNoAllegatiError() throws Exception {
		ConsuntivoDto recuperoGasolioMock = new ConsuntivoDto()
				.setInfoAllegati(new ArrayList<>())
				.setCarburante(TipoCarburanteConsuntivo.GASOLIO)
				.setId(null)
				.setMotivazione(MotivazioneConsuntivo.ALTRO)
				.setQuantita(1)
				.setTipo(TipoConsuntivo.RECUPERO);

		MockPart jsonPart = new MockPart("consuntivo", "consuntivo", objectMapper.writeValueAsString(recuperoGasolioMock).getBytes());
		jsonPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);

		NestedServletException exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(multipart(ApiUrls.CONSUMI + "/7761" + ApiUrls.ALLEGATI)
					.part(jsonPart));
		});

		assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
		assertEquals("Non ci sono allegati per il campo RECUPERO GASOLIO", exception.getCause().getMessage());
	}
	@Test
	@Sql(scripts = "/sql/consumi/consuntivi/consuntivi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consuntivi/consuntivi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void dichiaraAllegatiRecuperoNoAllegatiErrorQuantitaZero() throws Exception {
		String fileNameAllegatoRecupero = "nomefile.pdf" + "$$" + TipoAllegatoConsuntivo.ALTRO.name() + "$$" + "Descrizione";

		List<InfoAllegatoConsuntivoDto> allegatiDaConservare = Arrays.asList(
				new InfoAllegatoConsuntivoDto().setId(168L));
		
		ConsuntivoDto recuperoGasolioMock = new ConsuntivoDto()
				.setInfoAllegati(allegatiDaConservare)
				.setCarburante(TipoCarburanteConsuntivo.GASOLIO)
				.setId(null)
				.setMotivazione(MotivazioneConsuntivo.FURTO)
				.setQuantita(0)
				.setTipo(TipoConsuntivo.RECUPERO);
		MockPart filePart = new MockPart("allegati", fileNameAllegatoRecupero, "some pdf binary data".getBytes());
		filePart.getHeaders().setContentType(MediaType.APPLICATION_PDF);
		MockPart jsonPart = new MockPart("consuntivo", "consuntivo", objectMapper.writeValueAsString(recuperoGasolioMock).getBytes());
		jsonPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);

		NestedServletException exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(multipart(ApiUrls.CONSUMI + "/7761" + ApiUrls.ALLEGATI)
					.part(filePart)
					.part(jsonPart));
		});

		assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
		assertEquals("Errore Salvataggio consuntivo RECUPERO GASOLIO", exception.getCause().getMessage());
	}

	@Test
	@Sql(scripts = "/sql/consumi/consuntivi/consuntivi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consuntivi/consuntivi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void dichiaraAllegatiAmmissibileNoMotivazione() throws Exception {
		String fileNameAllegatoRecupero = "nomefile.pdf" + "$$" + TipoAllegatoConsuntivo.ALTRO.name() + "$$" + "Descrizione";

		List<InfoAllegatoConsuntivoDto> allegatiDaConservare = Arrays.asList(
				new InfoAllegatoConsuntivoDto().setId(168L));

		ConsuntivoDto recuperoGasolioMock = new ConsuntivoDto()
				.setInfoAllegati(allegatiDaConservare)
				.setCarburante(TipoCarburanteConsuntivo.GASOLIO_TERZI)
				.setId(null)
				.setMotivazione(null)
				.setQuantita(1)
				.setTipo(TipoConsuntivo.AMMISSIBILE);

		MockPart filePart = new MockPart("allegati", fileNameAllegatoRecupero, "some pdf binary data".getBytes());
		filePart.getHeaders().setContentType(MediaType.APPLICATION_PDF);

		MockPart jsonPart = new MockPart("consuntivo", "consuntivo", objectMapper.writeValueAsString(recuperoGasolioMock).getBytes());
		jsonPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);

		NestedServletException exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(multipart(ApiUrls.CONSUMI + "/7761" + ApiUrls.ALLEGATI)
					.part(filePart)
					.part(jsonPart));
		});

		assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
		assertEquals("Nessuna Motivazione espressa per il campo AMMISSIBILE GASOLIO_TERZI", exception.getCause().getMessage());
	}
 
	@Test
	@Sql(scripts = "/sql/consumi/consuntivi/consuntivi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consuntivi/consuntivi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void dichiaraAllegatiAmmissibileError() throws Exception {
		String fileNameAllegatoRecupero = "nomefile.pdf" + "$$" + TipoAllegatoConsuntivo.ALTRO.name() + "$$" + "Descrizione";

		List<InfoAllegatoConsuntivoDto> allegatiDaConservare = Arrays.asList(
				new InfoAllegatoConsuntivoDto().setId(168L));

		ConsuntivoDto recuperoGasolioMock = new ConsuntivoDto()
				.setInfoAllegati(allegatiDaConservare)
				.setCarburante(TipoCarburanteConsuntivo.GASOLIO_TERZI)
				.setId(null)
				.setMotivazione(MotivazioneConsuntivo.ASSEGNAZIONE_SVINCOLATA)
				.setQuantita(0)
				.setTipo(TipoConsuntivo.AMMISSIBILE);

		MockPart filePart = new MockPart("allegati", fileNameAllegatoRecupero, "some pdf binary data".getBytes());
		filePart.getHeaders().setContentType(MediaType.APPLICATION_PDF);

		MockPart jsonPart = new MockPart("consuntivo", "consuntivo", objectMapper.writeValueAsString(recuperoGasolioMock).getBytes());
		jsonPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);

		NestedServletException exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(multipart(ApiUrls.CONSUMI + "/7763" + ApiUrls.ALLEGATI)
					.part(filePart)
					.part(jsonPart));
		});

		assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
		assertEquals("Consuntivo Ammissibile Gasolio conto terzi: E' necessario modificare la quantità oppure cancellare gli allegati", exception.getCause().getMessage());
	}
	
	@Test
	@Sql(scripts = "/sql/consumi/consuntivi/consuntivi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consuntivi/consuntivi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void dichiaraAllegatiAmmissibileErrorXXX() throws Exception {
		String fileNameAllegatoRecupero = "nomefile.pdf" + "$$" + TipoAllegatoConsuntivo.ALTRO.name() + "$$" + "Descrizione";

		List<InfoAllegatoConsuntivoDto> allegatiDaConservare = Arrays.asList(
				new InfoAllegatoConsuntivoDto().setId(168L));

		ConsuntivoDto recuperoGasolioMock = new ConsuntivoDto()
				.setInfoAllegati(allegatiDaConservare)
				.setCarburante(TipoCarburanteConsuntivo.GASOLIO_TERZI)
				.setId(null)
				.setMotivazione(MotivazioneConsuntivo.ASSEGNAZIONE_SVINCOLATA)
				.setQuantita(0)
				.setTipo(TipoConsuntivo.AMMISSIBILE);

		MockPart filePart = new MockPart("allegati", fileNameAllegatoRecupero, "some pdf binary data".getBytes());
		filePart.getHeaders().setContentType(MediaType.APPLICATION_PDF);

		MockPart jsonPart = new MockPart("consuntivo", "consuntivo", objectMapper.writeValueAsString(recuperoGasolioMock).getBytes());
		jsonPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);

		NestedServletException exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(multipart(ApiUrls.CONSUMI + "/7762" + ApiUrls.ALLEGATI)
					.part(filePart)
					.part(jsonPart));
		});

		assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
		assertEquals("Errore Salvataggio consuntivo AMMISSIBILE GASOLIO_TERZI", exception.getCause().getMessage());
	}

	@Test
	@Sql(scripts = "/sql/consumi/consuntivi/consuntivi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consuntivi/consuntivi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void deleteConsuntivo() throws Exception {
		mockMvc.perform(delete("/api/v1/consumi/7761/consuntivi/16222"))
		.andExpect(status().is2xxSuccessful())
		.andExpect(status().isOk());
	}
}
