package it.tndigitale.a4g.fascicolo.territorio.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.SQLException;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import it.tndigitale.a4g.fascicolo.territorio.business.service.ControlliFascicoloAgsCompletoEnum;
import it.tndigitale.a4g.framework.component.dto.EsitoControlloDto;
import it.tndigitale.a4g.territorio.business.persistence.repository.legacy.dao.FascicoloAgsDao;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class FascicoliPrivateControllerTest {
	@Autowired FascicoliPrivateController fascicoliPrivateController;
	@Autowired MockMvc mockMvc;

	@MockBean private FascicoloAgsDao fascicoloDao;

	private void setupFascicoloDaoMockOK() throws SQLException, JsonMappingException, JsonProcessingException {
		Mockito.when(fascicoloDao.getControlloCompletezzaFascicolo(Mockito.anyString())).thenReturn(new EsitoControlloDto(0, 123L, null));
		Mockito.when(fascicoloDao.getControlloPianoColturaleAlfanumerico(Mockito.anyString())).thenReturn(new EsitoControlloDto(0, 124L, null));
		Mockito.when(fascicoloDao.getControlloPianoColturaleGrafico(Mockito.anyString())).thenReturn(new EsitoControlloDto(0, 125L, null));
	}

	private void setupFascicoloDaoMockFAIL() throws SQLException, JsonMappingException, JsonProcessingException {
		Mockito.when(fascicoloDao.getControlloCompletezzaFascicolo(Mockito.anyString())).thenThrow(new SQLException("Test failed"));
	}

	@Test
	void controlloCompletezzaFascicolo_OK() throws Exception {
		String cuaa = "XPDNDR77B03L378X";
		setupFascicoloDaoMockOK();

		Map<ControlliFascicoloAgsCompletoEnum, EsitoControlloDto> controlloCompletezzaFascicolo = fascicoliPrivateController.controlloCompletezzaFascicoloSincrono(cuaa);

		assertEquals(3, controlloCompletezzaFascicolo.size());
		assertEquals(0, controlloCompletezzaFascicolo.get(ControlliFascicoloAgsCompletoEnum.IS_TERRENI_CONSISTENTI).getEsito());
		assertEquals(0, controlloCompletezzaFascicolo.get(ControlliFascicoloAgsCompletoEnum.IS_PCG_CONSISTENTE).getEsito());
		assertEquals(125L, controlloCompletezzaFascicolo.get(ControlliFascicoloAgsCompletoEnum.IS_PCG_CONSISTENTE).getIdControllo());
		assertEquals(0, controlloCompletezzaFascicolo.get(ControlliFascicoloAgsCompletoEnum.IS_PIANOCOLT_CONSISTENTE).getEsito());
	}

	@Test
	@WithMockUser(username = "XPDNDR77B03L378X", roles = {"a4gfascicolo.fascicolo.ricerca.ente"})
	void controlloCompletezzaFascicolo_FAIL() throws Exception {
		String cuaa = "XPDNDR77B03L378X";
		setupFascicoloDaoMockFAIL();
		mockMvc.perform(
				get(String.format("/api/v1/fascicoli/private/%s/controllo-completezza", cuaa))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}
}
