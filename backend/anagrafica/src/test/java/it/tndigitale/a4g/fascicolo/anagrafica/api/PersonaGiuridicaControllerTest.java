package it.tndigitale.a4g.fascicolo.anagrafica.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaFisicaConCaricaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaGiuridicaConCaricaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.Sesso;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.PersonaFisicaConCaricaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.PersonaGiuridicaConCaricaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaUtenteClient;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicoloId;
import it.tndigitale.a4g.framework.time.Clock;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class PersonaGiuridicaControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	Clock clock;

	@MockBean
	AnagraficaUtenteClient anagraficaUtenteClient;
	
	@Autowired
	PersonaFisicaConCaricaDao personaFisicaConCaricaDao;
	
	@Autowired
	PersonaGiuridicaConCaricaDao personaGiuridicaConCaricaDao;

	private final long ID_ENTE_CONNESSO = 11L;

	void userSetupEnte() {
		List<String> entiUtenteConnesso = new ArrayList<>();
		entiUtenteConnesso.add(String.valueOf(ID_ENTE_CONNESSO));
		Mockito.when(anagraficaUtenteClient.getEntiUtenteConnesso()).thenReturn(entiUtenteConnesso);
	}

	@Test
	@WithMockUser(username = "utente")
	@Sql(scripts = "/sql/fascicolo/fascicolo_persona_giuridica_validazione_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_persona_giuridica_validazione_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void get_persona_giuridica_live() throws Exception {
		userSetupEnte();
		String codiceFiscale = "00112233445";
		String params = "?idValidazione=0";

		mockMvc.perform(get(String.format("%s/%s%s", ApiUrls.PERSONA_GIURIDICA, codiceFiscale, params))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.codiceFiscale").value(codiceFiscale))
		.andExpect(jsonPath("$.oggettoSociale").value("OGGETTO LIVE"));
	}
	
	@Test
	@WithMockUser(username = "utente")
	@Sql(scripts = "/sql/fascicolo/fascicolo_persona_giuridica_validazione_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_persona_giuridica_validazione_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void get_persona_giuridica_storico() throws Exception {
		userSetupEnte();
		String codiceFiscale = "00112233445";
		String params = "?idValidazione=1";

		mockMvc.perform(get(String.format("%s/%s%s", ApiUrls.PERSONA_GIURIDICA, codiceFiscale, params))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.codiceFiscale").value(codiceFiscale))
		.andExpect(jsonPath("$.oggettoSociale").value("OGGETTO VALIDATO"));
	}
	
	@Test
	@WithMockUser(username = "XPDNDR77B03L378X", roles = {"a4gfascicolo.fascicolo.ricerca.ente"})
	@Sql(scripts = "/sql/fascicolo/fascicolo_get_persona_con_carica.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void getPersonaFisicaConCarica() throws Exception {
		
		Optional<PersonaFisicaConCaricaModel> personaFisicaConCaricaModelOpt = personaFisicaConCaricaDao.findByIdAndIdValidazione(5L, 0);
		assertEquals(true, personaFisicaConCaricaModelOpt.isPresent());
		PersonaFisicaConCaricaModel personaFisicaConCaricaModel = personaFisicaConCaricaModelOpt.get();
//		[begin] per coverage: verifica uguaglianza model
		assertEquals(true, personaFisicaConCaricaModel.equals(personaFisicaConCaricaModel));
		assertEquals(false, personaFisicaConCaricaModel.equals(null));
		assertEquals(false, personaFisicaConCaricaModel.equals(new Object()));
		PersonaFisicaConCaricaModel personaFisicaConCaricaModelTest = new PersonaFisicaConCaricaModel();
		BeanUtils.copyProperties(personaFisicaConCaricaModel, personaFisicaConCaricaModelTest);
		personaFisicaConCaricaModelTest.setSesso(Sesso.FEMMINA);
		assertEquals(false, personaFisicaConCaricaModel.equals(personaFisicaConCaricaModelTest));
//		[end] per coverage: verifica uguaglianza model
	}
	
	@Test
	@WithMockUser(username = "XPDNDR77B03L378X", roles = {"a4gfascicolo.fascicolo.ricerca.ente"})
	@Sql(scripts = "/sql/fascicolo/fascicolo_get_persona_con_carica.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void getPersonaGiuridicaConCarica() throws Exception {
		
		Optional<PersonaGiuridicaConCaricaModel> personaGiuridicaConCaricaModelOpt = personaGiuridicaConCaricaDao.findById(new EntitaDominioFascicoloId(50L, 0));
		assertEquals(true, personaGiuridicaConCaricaModelOpt.isPresent());
		PersonaGiuridicaConCaricaModel personaFisicaConCaricaModel = personaGiuridicaConCaricaModelOpt.get();
//		[begin] per coverage: verifica uguaglianza model
		assertEquals(true, personaFisicaConCaricaModel.equals(personaFisicaConCaricaModel));
		assertEquals(false, personaFisicaConCaricaModel.equals(null));
		assertEquals(false, personaFisicaConCaricaModel.equals(new Object()));
		PersonaGiuridicaConCaricaModel personaGiuridicaConCaricaModelTest = new PersonaGiuridicaConCaricaModel();
		BeanUtils.copyProperties(personaFisicaConCaricaModel, personaGiuridicaConCaricaModelTest);
		personaGiuridicaConCaricaModelTest.setDenominazione("Paperino e Paperoga inc.");
		assertEquals(false, personaFisicaConCaricaModel.equals(personaGiuridicaConCaricaModelTest));
//		[end] per coverage: verifica uguaglianza model
	}

}
