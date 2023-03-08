package it.tndigitale.a4g.fascicolo.dotazionetecnica.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.ApiUrls;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.service.client.DotazioneTecnicaAnagraficaClient;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.DatiCatastaliDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.DettaglioFabbricatoDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.SottotipoDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.TipologiaParticellaCatastale;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente")
class FabbricatiControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private DotazioneTecnicaAnagraficaClient anagraficaClient;
	@Autowired
	FabbricatiController fabbricatiController;

	@Test
	@Sql(scripts = "/sql/get_fabbricati_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/get_fabbricati_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postStruttura() throws Exception {
		doNothing().when(anagraficaClient).putFascicoloStatoControlliInAggiornamentoUsingPUT(Mockito.anyString());

		var sottotipo = new SottotipoDto();
		sottotipo.setId(9999L);

		// Build dto
		DettaglioFabbricatoDto dto = new DettaglioFabbricatoDto();
		dto.setDescrizione("descrizione");
		dto.setIndirizzo("indirizzo");
		dto.setComune("comune");
		dto.setSottotipo(sottotipo);
		DatiCatastaliDto dato = new DatiCatastaliDto();
		dato.setComune("comune");
		dato.setDenominatore("A");
		dato.setInTrentino(false);
		dato.setTipologia(TipologiaParticellaCatastale.EDIFICIALE);
		List<DatiCatastaliDto> dati = new ArrayList<DatiCatastaliDto>();
		dati.add(dato);
		dto.setDatiCatastali(dati);

		var request = objectMapper.writeValueAsString(dto);

		mockMvc.perform(post(ApiUrls.API_V1 + ApiUrls.FASCICOLO + "/01720090222" + ApiUrls.FABBRICATI).contentType(MediaType.APPLICATION_JSON).content(request)).andExpect(status().is2xxSuccessful())
				.andExpect(status().isOk());
	}

	@Test
	@Sql(scripts = "/sql/get_fabbricati_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/get_fabbricati_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void updateFabbricato() throws Exception {
		doNothing().when(anagraficaClient).putFascicoloStatoControlliInAggiornamentoUsingPUT(Mockito.anyString());

		var sottotipo = new SottotipoDto();
		sottotipo.setId(9999L);

		// Build dto
		DettaglioFabbricatoDto dto = new DettaglioFabbricatoDto();
		dto.setId(22L);
		dto.setDescrizione("descrizione");
		dto.setIndirizzo("indirizzo");
		dto.setComune("comune");
		dto.setSottotipo(sottotipo);
		DatiCatastaliDto dato = new DatiCatastaliDto();
		dato.setComune("comune");
		dato.setDenominatore("A");
		dato.setInTrentino(false);
		dato.setTipologia(TipologiaParticellaCatastale.EDIFICIALE);
		List<DatiCatastaliDto> dati = new ArrayList<DatiCatastaliDto>();
		dati.add(dato);
		dto.setDatiCatastali(dati);

		var request = objectMapper.writeValueAsString(dto);

		mockMvc.perform(post(ApiUrls.API_V1 + ApiUrls.FASCICOLO + "/01720090222" + ApiUrls.FABBRICATI).contentType(MediaType.APPLICATION_JSON).content(request)).andExpect(status().is2xxSuccessful())
				.andExpect(status().isOk());
	}

	@Test
	@Sql(scripts = "/sql/get_fabbricati_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/get_fabbricati_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postFabbricato() throws Exception {
		doNothing().when(anagraficaClient).putFascicoloStatoControlliInAggiornamentoUsingPUT(Mockito.anyString());

		var sottotipo = new SottotipoDto();
		sottotipo.setId(9999L);

		// Build dto
		DettaglioFabbricatoDto dto = new DettaglioFabbricatoDto();
		dto.setDescrizione("descrizione");
		dto.setIndirizzo("indirizzo");
		dto.setComune("comune");

		List<DatiCatastaliDto> catasto = new ArrayList<DatiCatastaliDto>();
		DatiCatastaliDto dato = new DatiCatastaliDto();
		dato.setComune("TN");
		catasto.add(dato);

		dto.setDatiCatastali(catasto);
		dto.setSottotipo(sottotipo);

		var request = objectMapper.writeValueAsString(dto);

		mockMvc.perform(post(ApiUrls.API_V1 + ApiUrls.FASCICOLO + "/01720090222" + ApiUrls.FABBRICATI).contentType(MediaType.APPLICATION_JSON).content(request)).andExpect(status().is2xxSuccessful())
				.andExpect(status().isOk());
	}

	@Test
	@Sql(scripts = "/sql/get_fabbricati_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/get_fabbricati_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postFabbricato_datiCatastaliMancanti() throws Exception {
		doNothing().when(anagraficaClient).putFascicoloStatoControlliInAggiornamentoUsingPUT(Mockito.anyString());

		var sottotipo = new SottotipoDto();
		sottotipo.setId(9999L);

		// Build dto
		DettaglioFabbricatoDto dto = new DettaglioFabbricatoDto();
		dto.setDescrizione("descrizione");
		dto.setIndirizzo("indirizzo");
		dto.setComune("comune");
		dto.setSottotipo(sottotipo);

		Exception exception = assertThrows(NullPointerException.class, () -> {
			fabbricatiController.postFabbricato("01720090222", dto);
		});

		assertEquals("Attenzione! Non sono presenti i dati catastali", exception.getMessage());
	}

	// @Test
	// @Sql(scripts = "/sql/get_fabbricati_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	// @Sql(scripts = "/sql/get_fabbricati_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	// void postSerre() throws Exception {
	// doNothing().when(anagraficaClient).putFascicoloStatoControlliInAggiornamentoUsingPUT(Mockito.anyString());
	//
	// var sottotipo = new SottotipoDto();
	// sottotipo.setId(211L);
	//
	// // Build dto
	// DettaglioFabbricatoDto dto = new DettaglioFabbricatoDto();
	// dto.setDescrizione("descrizione");
	// dto.setIndirizzo("indirizzo");
	// dto.setComune("comune");
	//
	// DatiSerreProtezioniDto serra = new DatiSerreProtezioniDto();
	// serra.setImpiantoRiscaldamento(true);
	//
	// dto.setSerra(serra);
	// dto.setSottotipo(sottotipo);
	//
	// var request = objectMapper.writeValueAsString(dto);
	//
	// mockMvc.perform(post(ApiUrls.API_V1 + ApiUrls.FASCICOLO + "/01720090222" + ApiUrls.FABBRICATI)
	// .contentType(MediaType.APPLICATION_JSON)
	// .content(request))
	// .andExpect(status().is2xxSuccessful())
	// .andExpect(status().isOk());
	// }

	@Test
	@Sql(scripts = "/sql/get_fabbricati_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/get_fabbricati_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getFabbricatoByIdOk() throws Exception {
		mockMvc.perform(get(ApiUrls.API_V1 + ApiUrls.FASCICOLO + "/01720090222" + ApiUrls.FABBRICATI + "/22")).andExpect(status().is2xxSuccessful()).andExpect(status().isOk());
	}

	@Test
	@Sql(scripts = "/sql/get_fabbricati_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/get_fabbricati_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getFabbricatiOk() throws Exception {
		mockMvc.perform(get(ApiUrls.API_V1 + ApiUrls.FASCICOLO + "/01720090222" + ApiUrls.FABBRICATI)).andExpect(status().is2xxSuccessful()).andExpect(status().isOk());
	}

	@Test
	@Sql(scripts = "/sql/get_fabbricati_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/get_fabbricati_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void deleteFabbricatoOk() throws Exception {
		mockMvc.perform(delete(ApiUrls.API_V1 + ApiUrls.FASCICOLO + "/01720090222" + ApiUrls.FABBRICATI + "/22")).andExpect(status().is2xxSuccessful()).andExpect(status().isOk());
	}

}
