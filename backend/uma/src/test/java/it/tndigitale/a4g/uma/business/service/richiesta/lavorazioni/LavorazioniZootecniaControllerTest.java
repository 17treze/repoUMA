package it.tndigitale.a4g.uma.business.service.richiesta.lavorazioni;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
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

import it.tndigitale.a4g.uma.business.persistence.entity.AmbitoLavorazione;
import it.tndigitale.a4g.uma.business.service.client.UmaTerritorioClient;
import it.tndigitale.a4g.uma.business.service.utente.AbilitazioniComponent;
import it.tndigitale.a4g.uma.dto.richiesta.RaggruppamentoLavorazioniDto;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente")
class LavorazioniZootecniaControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private AbilitazioniComponent abilitazioniComponent;
	@MockBean
	private UmaTerritorioClient territorioClient;

	@BeforeEach
	private void mockUtenteConnesso() throws Exception {
		Mockito.when(abilitazioniComponent.checkRicercaRichiestaDiCarburante(Mockito.anyLong())).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkRicercaDomandeUma()).thenReturn(true);
		Mockito.when(abilitazioniComponent.checkModificaRichiestaCarburante(Mockito.anyLong())).thenReturn(true);
	}

	@Test
	@Sql(scripts = "/sql/lavorazioni/get_lavorazioni_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/lavorazioni/get_lavorazioni_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getLavorazioniSuccessful() throws Exception {
		String response = mockMvc.perform(get("/api/v1/richieste/2/lavorazioni").param("ambito", AmbitoLavorazione.ZOOTECNIA.name()))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		List<RaggruppamentoLavorazioniDto> dtos = objectMapper.readValue(response, new TypeReference<ArrayList<RaggruppamentoLavorazioniDto>>(){})
				.stream()
				.sorted(Comparator.comparingInt(RaggruppamentoLavorazioniDto::getIndice))
				.collect(Collectors.toList());

		assertEquals(7, dtos.size());

		assertEquals(21, dtos.get(0).getIndice());
		assertEquals("BOVINI E BUFALINI", dtos.get(0).getNome());
		assertEquals(6, dtos.get(0).getLavorazioni().size());
		assertEquals(27, dtos.get(6).getIndice());
		assertEquals("APICOLTURA", dtos.get(6).getNome());
		assertEquals(2, dtos.get(6).getLavorazioni().size());
	}

}
