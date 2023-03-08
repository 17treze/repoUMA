package it.tndigitale.a4g.fascicolo.territorio.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

import it.tndigitale.a4g.fascicolo.territorio.business.persistence.repository.legacy.ConduzioneTerrenoDao;
import it.tndigitale.a4g.fascicolo.territorio.business.service.ControlliFascicoloAgsCompletoEnum;
import it.tndigitale.a4g.framework.component.dto.EsitoControlloDto;
import it.tndigitale.a4g.territorio.business.persistence.repository.legacy.dao.FascicoloAgsDao;
import it.tndigitale.a4g.territorio.business.service.client.AnagraficaUtenteClient;
import it.tndigitale.a4g.territorio.business.service.client.FascicoloAnagraficaClient;
import it.tndigitale.a4g.utente.client.model.RappresentaIlModelloDelProfiloDiUnUtente;
import it.tndigitale.a4g.utente.client.model.RappresentaIlModelloPerRappresentareUnUtenteDelSistema;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class FascicoliControllerTest {
	@Autowired FascicoliController fascicoliController;
	@Autowired ConduzioneTerrenoDao conduzioneTerrenoDao;
	@Autowired MockMvc mockMvc;

	@MockBean private FascicoloAgsDao fascicoloDao;
	@MockBean AnagraficaUtenteClient anagraficaUtenteClient;
	@MockBean FascicoloAnagraficaClient fascicoloAnagraficaClient;

	void userSetupAzienda(String userCodiceFiscale) {
		RappresentaIlModelloPerRappresentareUnUtenteDelSistema user = new RappresentaIlModelloPerRappresentareUnUtenteDelSistema();
		user.setIdentificativo(userCodiceFiscale);
		user.setCodiceFiscale(userCodiceFiscale);
		user.setNome(userCodiceFiscale);
		List<RappresentaIlModelloDelProfiloDiUnUtente> profili = new ArrayList<>();
		RappresentaIlModelloDelProfiloDiUnUtente profilo = new RappresentaIlModelloDelProfiloDiUnUtente();
		profilo.setIdentificativo("azienda");
		profili.add(profilo);
		user.setProfili(profili);
		Mockito.when(anagraficaUtenteClient.getUtenteConnesso()).thenReturn(user);
	}

	private void setupAbilitazioneUtenteOK() {
		Mockito.when(fascicoloAnagraficaClient.checkAperturaFascicolo(Mockito.anyString())).thenReturn(true);
		Mockito.when(fascicoloAnagraficaClient.checkLetturaFascicolo(Mockito.anyString())).thenReturn(true);
	}

	private void setupAbilitazioneUtenteFail() {
		Mockito.when(fascicoloAnagraficaClient.checkAperturaFascicolo(Mockito.anyString())).thenReturn(false);
		Mockito.when(fascicoloAnagraficaClient.checkLetturaFascicolo(Mockito.anyString())).thenReturn(false);
	}

	private void setupFascicoloDaoMockOK() throws SQLException, JsonMappingException, JsonProcessingException {
		Mockito.when(fascicoloDao.getControlloCompletezzaFascicolo(Mockito.anyString())).thenReturn(new EsitoControlloDto(0, 123L, null));
	}

	private void setupFascicoloDaoMockNoData() throws SQLException, JsonMappingException, JsonProcessingException {
		Mockito.when(fascicoloDao.getControlloCompletezzaFascicolo(Mockito.anyString())).thenReturn(new EsitoControlloDto(-2, 123L, null));
	}

	private void setupFascicoloDaoMockTerreniNonConsistentiNonBloccante() throws SQLException, JsonMappingException, JsonProcessingException {
		Mockito.when(fascicoloDao.getControlloCompletezzaFascicolo(Mockito.anyString())).thenReturn(new EsitoControlloDto(-1, 123L, null));
	}

	private void setupFascicoloDaoMockTerreniNonConsistentiBloccante() throws SQLException, JsonMappingException, JsonProcessingException {
		Mockito.when(fascicoloDao.getControlloCompletezzaFascicolo(Mockito.anyString())).thenReturn(new EsitoControlloDto(-3, 123L, null));
	}

	@Test
	@WithMockUser(username = "XPDNDR77B03L378X", roles = {"a4gfascicolo.fascicolo.ricerca.utente"})
	void controlloCompletezzaFascicolo_OK() throws Exception {
		userSetupAzienda("XPDNDR77B03L378X");
		setupAbilitazioneUtenteOK();
		String cuaa = "XPDNDR77B03L378X";
		setupFascicoloDaoMockOK();
		mockMvc.perform(
				get(String.format("/api/v1/fascicoli/%s/controllo-completezza", cuaa))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "XPDNDR77B03L378Y", roles = {"a4gfascicolo.fascicolo.ricerca.utente"})
	void controlloCompletezzaFascicolo_utente_non_abilitato() throws Exception {
		userSetupAzienda("XPDNDR77B03L378Y");
		setupAbilitazioneUtenteFail();
		String cuaa = "XPDNDR77B03L378X";
		setupFascicoloDaoMockOK();
		mockMvc.perform(
				get(String.format("/api/v1/fascicoli/%s/controllo-completezza", cuaa))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "XPDNDR77B03L378X", roles = {"a4gfascicolo.fascicolo.ricerca.utente"})
	void controlloCompletezzaFascicolo_no_data() throws Exception {
		userSetupAzienda("XPDNDR77B03L378X");
		setupAbilitazioneUtenteOK();
		String cuaa = "XPDNDR77B03L378X";
		setupFascicoloDaoMockNoData();

		mockMvc.perform(
				get(String.format("/api/v1/fascicoli/%s/controllo-completezza", cuaa))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "XPDNDR77B03L378X", roles = {"a4gfascicolo.fascicolo.ricerca.utente"})
	void controlloCompletezzaFascicolo_terreni_non_consistenti_non_bloccante() throws Exception {
		userSetupAzienda("XPDNDR77B03L378X");
		setupAbilitazioneUtenteOK();
		String cuaa = "XPDNDR77B03L378X";
		setupFascicoloDaoMockTerreniNonConsistentiNonBloccante();

		Map<ControlliFascicoloAgsCompletoEnum, EsitoControlloDto> controlloCompletezzaFascicolo = fascicoliController.getControlloCompletezzaFascicolo(cuaa);

		assertEquals(3, controlloCompletezzaFascicolo.size());
		assertEquals(-1, controlloCompletezzaFascicolo.get(ControlliFascicoloAgsCompletoEnum.IS_TERRENI_CONSISTENTI).getEsito());
	}

	@Test
	@WithMockUser(username = "XPDNDR77B03L378X", roles = {"a4gfascicolo.fascicolo.ricerca.utente"})
	void controlloCompletezzaFascicolo_terreni_non_consistenti_bloccante() throws Exception {
		userSetupAzienda("XPDNDR77B03L378X");
		setupAbilitazioneUtenteOK();
		String cuaa = "XPDNDR77B03L378X";
		setupFascicoloDaoMockTerreniNonConsistentiBloccante();

		Map<ControlliFascicoloAgsCompletoEnum, EsitoControlloDto> controlloCompletezzaFascicolo = fascicoliController.getControlloCompletezzaFascicolo(cuaa);

		assertEquals(3, controlloCompletezzaFascicolo.size());
		assertEquals(-3, controlloCompletezzaFascicolo.get(ControlliFascicoloAgsCompletoEnum.IS_TERRENI_CONSISTENTI).getEsito());
	}

	//	@Test
	//	void salvaConduzioneTerreno_ok() throws Exception {
	//		String cuaa = "00185820222";
	//		int result = conduzioneTerrenoDao.salvaConduzioneTerreno(cuaa, setupConduzioneTerreni());
	//		assertEquals(0, result);
	//	}

}
