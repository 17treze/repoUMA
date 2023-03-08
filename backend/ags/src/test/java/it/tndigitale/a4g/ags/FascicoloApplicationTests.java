package it.tndigitale.a4g.ags;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import it.tndigitale.a4g.ags.api.FascicoloRestController;
import it.tndigitale.a4g.ags.dto.AnagraficaAziendaBaseData;
import it.tndigitale.a4g.ags.dto.AnagraficaAziendaDocumentData;
import it.tndigitale.a4g.ags.dto.AnagraficaAziendaEnteData;
import it.tndigitale.a4g.ags.dto.AnagraficaAziendaFascicoloData;
import it.tndigitale.a4g.ags.repository.dao.FascicoloAziendaleDao;
import it.tndigitale.a4g.ags.service.FascicoloAziendaleServiceImpl;
import org.assertj.core.util.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import it.tndigitale.a4g.ags.api.ApiUrls;
import it.tndigitale.a4g.ags.utente.Ruoli;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
// @AutoConfigureTestDatabase
public class FascicoloApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	@WithMockUser(username="utente", roles= {Ruoli.VISUALIZZA_FASCICOLO_COD})
	public void ricercaPerCuaaEDenominazione() throws Exception {
		this.mockMvc.perform(get(ApiUrls.FASCICOLI_V1).param("params", "{\"cuaa\":\"TRRCST78B08C794X\",\"denominazione\":\"torre\"}")).andExpect(status().isOk())
				.andExpect(content().string(containsString("TRRCST78B08C794X")));
	}

	@Test
	@WithMockUser(username="utente", roles= {Ruoli.VISUALIZZA_FASCICOLO_COD})
	public void ricercaPerCuaa() throws Exception {
		this.mockMvc.perform(get(ApiUrls.FASCICOLI_V1).param("params", "{\"cuaa\":\"TRRCST78B08C7\"}")).andExpect(status().isOk())
				.andExpect(content().string(containsString("TRRCST78B08C794X")));
	}

	@Test
	@WithMockUser(username="utente", roles= {Ruoli.VISUALIZZA_FASCICOLO_COD})
	public void ricercaPerCuaaParziale() throws Exception {
		this.mockMvc.perform(get(ApiUrls.FASCICOLI_V1).param("params", "{\"cuaa\":\"TRRC\"}")).andExpect(status().isOk()).andExpect(content().string(containsString("TRRCST78B08C794X")))
				.andExpect(content().string(containsString("STRRCR59B20A561S")));
	}

	@Test
	@WithMockUser(username="utente", roles= {Ruoli.VISUALIZZA_FASCICOLO_COD})
	public void ricercaPerDenominazione() throws Exception {
		this.mockMvc.perform(get(ApiUrls.FASCICOLI_V1).param("params", "{\"denominazione\":\"torre\"}")).andExpect(status().isOk())
				.andExpect(content().string(containsString("TRRCST78B08C794X")));
	}

	@Test
	@WithMockUser(username="utente", roles= {Ruoli.VISUALIZZA_FASCICOLO_COD})
	public void ricercaSenzaRisultato() throws Exception {
		this.mockMvc.perform(get(ApiUrls.FASCICOLI_V1).param("params", "{\"cuaa\":\"SCRSMN75A10F520N\",\"denominazione\":\"Scriboni Simone\"}")).andExpect(status().isNoContent());
	}

	@Test
	@WithMockUser(username="utente", roles= {Ruoli.VISUALIZZA_FASCICOLO_COD})
	public void ricercaSenzaInput() throws Exception {
		this.mockMvc.perform(get(ApiUrls.FASCICOLI_V1).param("params", "{}")).andExpect(status().isNoContent());
	}

	@Test
	@WithMockUser(username="utente", roles= {Ruoli.VISUALIZZA_FASCICOLO_COD})
	public void ricercaConEccezione() throws Exception {
		try {
			this.mockMvc.perform(get(ApiUrls.FASCICOLI_V1).param("params", "AAAAAA")).andExpect(status().isInternalServerError());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Test
	@WithMockUser(username="utente", roles= {Ruoli.VISUALIZZA_FASCICOLO_COD})
	public void getFascicolo() throws Exception {
		this.mockMvc.perform(get(ApiUrls.FASCICOLI_V1 + "/31393")).andExpect(status().isOk()).andExpect(content().string(containsString("TRRCST78B08C794X")));
	}

	@Test
	@WithMockUser(username="utente", roles= {Ruoli.VISUALIZZA_FASCICOLO_COD})
	public void getFascicoloInesistente() throws Exception {
		// this.mockMvc.perform(get(ApiUrls.FASCICOLI_V1 +"/-1")).andExpect(status().isNotFound());
		this.mockMvc.perform(get(ApiUrls.FASCICOLI_V1 +"/-1")).andExpect(status().isNoContent());
	}

	@Test
	@WithMockUser(username="utente", roles= {Ruoli.VISUALIZZA_FASCICOLO_COD})
	public void getFascicoloSenzaInput() throws Exception {
		this.mockMvc.perform(get(ApiUrls.FASCICOLI_V1 + "/null")).andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(username="utente")
	public void checkValido() throws Exception {
		this.mockMvc.perform(get(ApiUrls.FASCICOLI_V1 + "/TRRCST78B08C794X/" + ApiUrls.FUNZIONE_FASCICOLO_VALIDO)).andExpect(status().isOk())
				.andExpect(content().string(containsString("true")));
	}

	@Test
	@WithMockUser(username="utente")
	public void checkChiuso() throws Exception {
		this.mockMvc.perform(get(ApiUrls.FASCICOLI_V1 + "/PVISLV26B08G452L/" + ApiUrls.FUNZIONE_FASCICOLO_VALIDO)).andExpect(status().isOk())
				.andExpect(content().string(containsString("false")));
	}

	@Test
	@WithMockUser(username="utente")
	public void checkNonEsistente() throws Exception {
		this.mockMvc.perform(get(ApiUrls.FASCICOLI_V1 + "/TRRXST78B08C794X/" + ApiUrls.FUNZIONE_FASCICOLO_VALIDO)).andExpect(status().isOk())
				.andExpect(content().string(containsString("false")));
	}

//	@Test
//	@WithMockUser(username="utente", roles= {Ruoli.VISUALIZZA_FASCICOLO_COD})
//	public void ricercaPerCuaaSenzaEnteMandato() throws Exception {
//		this.mockMvc.perform(get(ApiUrls.FASCICOLI_V1).param("params", "{\"cuaa\":\"02408130223\"}")).andExpect(status().isOk())
//				.andExpect(content().string(containsString("02408130223")));
//	}

	@Test
	@WithMockUser(username="utente", roles= {Ruoli.VISUALIZZA_FASCICOLO_COD})
	public void ricercaPerCuaaEEnteMandato() throws Exception {
		this.mockMvc.perform(get(ApiUrls.FASCICOLI_V1).param("params", "{\"cuaa\":\"TRRCST78B08C7\", \"caacodici\":[\"4\"]}")).andExpect(status().isOk())
				.andExpect(content().string(containsString("TRRCST78B08C794X")));
	}

	@Test
	@WithMockUser(username="utente", roles= {Ruoli.VISUALIZZA_FASCICOLO_COD})
	public void ricercaPerCuaaEEnteNonCorretto() throws Exception {
		this.mockMvc.perform(get(ApiUrls.FASCICOLI_V1).param("params", "{\"cuaa\":\"TRRCST78B08C7\", \"caacodici\":[\"20\"]}")).andExpect(status().isNoContent());
	}

//	@Test
//	@WithMockUser(username="utente", roles= {Ruoli.VISUALIZZA_FASCICOLO_COD})
//	public void ricercaPerCuaaEEnteDelegato() throws Exception {
//		this.mockMvc.perform(get(ApiUrls.FASCICOLI_V1).param("params", "{\"cuaa\":\"02408130223\", \"caacodici\":[\"1\"]}")).andExpect(status().isOk())
//				.andExpect(content().string(containsString("02408130223")));
//	}

	@Test
	@WithMockUser(username="utente", roles= {Ruoli.VISUALIZZA_FASCICOLO_COD})
	public void ricercaPerCuaaEEnti() throws Exception {
		this.mockMvc.perform(get(ApiUrls.FASCICOLI_V1).param("params", "{\"cuaa\":\"TRRCST78B08C7\", \"caacodici\":[\"4\", \"20\"]}")).andExpect(status().isOk())
				.andExpect(content().string(containsString("TRRCST78B08C794X")));
	}

	@Test
	@WithMockUser(username="utente", roles= {Ruoli.VISUALIZZA_FASCICOLO_COD})
	public void ricercaPerCuaaFuoriProvinciaEEntiDelega() throws Exception {
		this.mockMvc.perform(get(ApiUrls.FASCICOLI_V1).param("params", "{\"cuaa\":\"BGNDNL91L11L174K\", \"caacodici\":[\"20\"]}")).andExpect(status().isOk())
				.andExpect(content().string(containsString("BGNDNL91L11L174K")));
	}

	@Test
	@WithMockUser(username="utente", roles= {Ruoli.VISUALIZZA_ANAGRAFICA_AZIENDALE_COD})
	public void ricercaAnagraficaAziendaleConCUAA() throws Exception {
		FascicoloAziendaleDao mockDao = mock(FascicoloAziendaleDao.class);

		when(mockDao.findPkCuaaByCuaa(eq("VALID_CUAA"))).thenReturn(1L);
		when(mockDao.loadDocumentTypeMapping()).thenReturn(Maps.newHashMap(1, 5));
		when(mockDao.loadAnagraficaBaseData(eq(1L))).thenReturn(anagraficaAziendaBaseDataFixture());
		when(mockDao.loadDocumentData(eq(1L))).thenReturn(new AnagraficaAziendaDocumentData(1, "22",
																							new Date(), new Date()));
		when(mockDao.loadEnteData(eq(1L))).thenReturn(new AnagraficaAziendaEnteData("CAA Hello World",
																					"Hello World", new Date()));
		when(mockDao.loadFascicoloData(eq(1L))).thenReturn(new AnagraficaAziendaFascicoloData(new Date()));


		FascicoloAziendaleServiceImpl fascicoloAziendaleService = new FascicoloAziendaleServiceImpl(mockDao);
		FascicoloRestController fascicoloRestController = new FascicoloRestController(null, fascicoloAziendaleService, null);

		MockMvc mvc = MockMvcBuilders.standaloneSetup(fascicoloRestController)
									 .build();

		mvc.perform(get(ApiUrls.FASCICOLI_V1 + "/VALID_CUAA/anagrafica"))
		   
		   .andExpect(status().isOk())
		   .andExpect(content().string(containsString("VALID_CUAA")))
		   .andExpect(content().string(containsString("Hello World")))
		   .andExpect(content().string(containsString("tipoAzienda\":\"PF\"")));
	}

	private AnagraficaAziendaBaseData anagraficaAziendaBaseDataFixture() {
		return new AnagraficaAziendaBaseData("900", "Steve", "Jobs", "M", new Date(), "L378", "Apple", "L378", "022", "38122", "Via Silicon Valley, 42", null, null, null, null, "01234567891", "01234", "INPS", new Date(), new Date(), "pec@apple.it", "0124");
	}
}
