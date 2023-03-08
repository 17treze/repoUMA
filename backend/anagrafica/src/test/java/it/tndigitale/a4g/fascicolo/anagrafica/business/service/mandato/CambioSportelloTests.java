package it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.DetenzioneModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.MandatoModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.SportelloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.FascicoloDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.MandatoDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.SportelloDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaUtenteClient;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloAllaFirmaAziendaException;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.mail.EmailService;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.CambioSportelloPatch;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicoloId;
import it.tndigitale.a4g.framework.support.CustomCollectors;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.utente.client.model.RappresentaIlModelloPerRappresentareUnAziendaAssociataAllutente;
import it.tndigitale.a4g.utente.client.model.RappresentaIlModelloPerRappresentareUnUtenteDelSistema;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente")
class CambioSportelloTests {

	@MockBean
	private AnagraficaUtenteClient anagraficaUtenteClient;

	@MockBean
	private EmailService emailService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MandatoDao mandatoDao;
	
	@Autowired
	private SportelloDao sportelloDao;

	@Autowired
	private FascicoloDao fascicoloDao;

	@MockBean
	private Clock clock;
	
	@Autowired
	private MandatoService mandatoService;

	@Value("${cambio-sportello.email-dto.to}")
	private String destinatarioMail;

	private static final String OGGETTO = "Modifica sede sportello CAA";
	private static final String TESTO = "Il fascicolo dell\u2019azienda %s, con mandato al %s, \u00E8 stato assegnato allo sportello %s in data %s con la seguente motivazione: %s";

	@Disabled
	@Transactional
	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.mandato.modificasportello"})
	@Sql(scripts = "/sql/fascicolo/verificaCambioSportelloSuccessful_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/verificaCambioSportelloSuccessful_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void verificaCambioSportelloSuccessful() throws Exception {
		// MOCK
		LocalDate dateCambio = LocalDate.of(2020, 12, 15);
		Mockito.doReturn(dateCambio.minusDays(1)).when(clock).today();
		// Long fascicoloId, Long sportelloId, CambioSportelloPatch cambioSportello
		String cuaa = "DPDNDR77B03L378L";
		Long sportelloId = 11L;
		CambioSportelloPatch patch = new CambioSportelloPatch();
		patch.setIdNuovoSportello(15L);
		patch.setMotivazione("motivazione cambio sportello");
		patch.setDataCambio(dateCambio);

		// mock verifica sportello abilitato
		List<String> entiUtenteConnesso = new ArrayList<>();
		entiUtenteConnesso.add("11");
		entiUtenteConnesso.add("13");
		entiUtenteConnesso.add("15");
		Mockito.when(anagraficaUtenteClient.getEntiUtenteConnesso()).thenReturn(entiUtenteConnesso);

		mockMvc.perform(put(String.format("/api/v1/mandato/%s/sportello/%s", cuaa, sportelloId))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(patch)))
		.andExpect(status().is2xxSuccessful());

		FascicoloModel fascicoloModel = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0).get();
		List<MandatoModel> mandatiFascicolo = mandatoDao.findByFascicolo(fascicoloModel);

		assertEquals(2 ,mandatiFascicolo.size());// mandato corrente e nuovo
		
		Optional<MandatoModel> detenzioneCorrenteOpt = mandatoService.getMandatoCorrente(fascicoloModel);
		assertTrue(detenzioneCorrenteOpt.isPresent());
		MandatoModel detenzioneCorrente = (MandatoModel)detenzioneCorrenteOpt.get();
		assertEquals(LocalDate.of(2020, Month.SEPTEMBER, 1) , detenzioneCorrente.getDataInizio());
		assertEquals(LocalDate.of(2020, Month.DECEMBER, 14) ,detenzioneCorrente.getDataFine());
		assertEquals(11L, detenzioneCorrente.getSportello().getIdentificativo());

		Optional<MandatoModel> findById = mandatoDao.findById(new EntitaDominioFascicoloId(13L, 0));
		assertFalse(findById.isPresent());

		MandatoModel cambioSportelloProgrammatoNew = mandatiFascicolo
				.stream()
				.filter(mandato -> mandato.getId() != detenzioneCorrente.getId())
				.collect(CustomCollectors.toSingleton());

		assertEquals(LocalDate.of(2020, Month.DECEMBER, 15) , cambioSportelloProgrammatoNew.getDataInizio());
		assertNull(cambioSportelloProgrammatoNew.getDataFine());
		assertEquals(15L , cambioSportelloProgrammatoNew.getSportello().getIdentificativo());

		// verify invio mail email
		String[] mailArgs =  {
				cuaa, //azienda CUAA - Denominazione
				"SPORTELLO - CORRENTE", //CAA Denominazione
				"SPORTELLO - NEW", //new Sportello CAA  - Località 
				"15/12/2020", //data gg/mm/aaaa
				"motivazione cambio sportello" //Motivazione cambio 
		};
		verify(emailService, times(1)).sendSimpleMessage(destinatarioMail, OGGETTO, String.format(TESTO, mailArgs));
	}

	@Disabled
	@Transactional
	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.mandato.modificasportello"})
	@Sql(scripts = "/sql/fascicolo/verificaCambioSportelloSuccessful_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/verificaCambioSportelloSuccessful_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void verificaCambioSportelloSuccessfulConRevocaOrdinaria() throws Exception {
		// MOCK
		LocalDate dateCambio = LocalDate.of(2020, 12, 15);
		Mockito.doReturn(dateCambio.minusDays(1)).when(clock).today();
		// Long fascicoloId, Long sportelloId, CambioSportelloPatch cambioSportello
		String cuaa = "MSTFBA79L10H612L";
		Long sportelloId = 11L;
		CambioSportelloPatch patch = new CambioSportelloPatch();
		patch.setIdNuovoSportello(15L);
		patch.setMotivazione("motivazione cambio sportello");
		patch.setDataCambio(dateCambio);

		// mock verifica sportello abilitato
		List<String> entiUtenteConnesso = new ArrayList<>();
		entiUtenteConnesso.add("11");
		entiUtenteConnesso.add("13");
		entiUtenteConnesso.add("15");
		Mockito.when(anagraficaUtenteClient.getEntiUtenteConnesso()).thenReturn(entiUtenteConnesso);

		mockMvc.perform(put(String.format("/api/v1/mandato/%s/sportello/%s", cuaa, sportelloId))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(patch)))
		.andExpect(status().is2xxSuccessful());

		FascicoloModel fascicoloModel = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0).get();
		List<MandatoModel> mandatiFascicolo = mandatoDao.findByFascicolo(fascicoloModel)
				.stream()
				.filter(mandato -> mandato.getFascicolo().getCuaa().equals(cuaa))
				.collect(Collectors.toList());

		assertEquals(2 ,mandatiFascicolo.size());// mandato corrente e nuovo

		Optional<MandatoModel> detenzioneCorrenteOpt = mandatoService.getMandatoCorrente(fascicoloModel);
		assertTrue(detenzioneCorrenteOpt.isPresent());
		MandatoModel detenzioneCorrente = (MandatoModel)detenzioneCorrenteOpt.get();

		assertEquals(LocalDate.of(2020, Month.SEPTEMBER, 1) , detenzioneCorrente.getDataInizio());
		assertEquals(LocalDate.of(2020, Month.DECEMBER, 14) ,detenzioneCorrente.getDataFine());
		assertEquals(11L, detenzioneCorrente.getSportello().getIdentificativo());

		Optional<MandatoModel> findById = mandatoDao.findById(new EntitaDominioFascicoloId(13L, 0));
		assertFalse(findById.isPresent());

		MandatoModel cambioSportelloProgrammatoNew = mandatiFascicolo
				.stream()
				.filter(mandato -> mandato.getId() != detenzioneCorrente.getId())
				.collect(CustomCollectors.toSingleton());

		assertEquals(LocalDate.of(2020, Month.DECEMBER, 15) , cambioSportelloProgrammatoNew.getDataInizio());
		assertEquals(LocalDate.of(2020, Month.DECEMBER, 31) , cambioSportelloProgrammatoNew.getDataFine());
		assertEquals(15L , cambioSportelloProgrammatoNew.getSportello().getIdentificativo());

		// verify invio mail email
		String[] mailArgs =  {
				cuaa, //azienda CUAA - Denominazione
				"SPORTELLO - CORRENTE", //CAA Denominazione
				"SPORTELLO - NEW", //new Sportello CAA  - Località 
				"15/12/2020", //data gg/mm/aaaa
				"motivazione cambio sportello" //Motivazione cambio 
		};
		verify(emailService, times(1)).sendSimpleMessage(destinatarioMail, OGGETTO, String.format(TESTO, mailArgs));
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.mandato.modificasportello"})
	@Sql(scripts = "/sql/fascicolo/verificaCambioSportelloSuccessful_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/verificaCambioSportelloSuccessful_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void verificaCambioSportelloAnnoSuccessivoConRevocaOrdinaria() throws Exception {
		// MOCK
		String cuaa = "MSTFBA79L10H612L";
		LocalDate dateCambio = LocalDate.of(2021, 1, 10);
		
		RappresentaIlModelloPerRappresentareUnUtenteDelSistema user = new RappresentaIlModelloPerRappresentareUnUtenteDelSistema();
		user.setIdentificativo("XPDNDR77B03L378X");
		user.setCodiceFiscale("XPDNDR77B03L378X");
		List<RappresentaIlModelloPerRappresentareUnAziendaAssociataAllutente> aziende = new ArrayList<>();
		RappresentaIlModelloPerRappresentareUnAziendaAssociataAllutente azienda = new RappresentaIlModelloPerRappresentareUnAziendaAssociataAllutente();
		azienda.setCuaa(cuaa);
		azienda.setDataAggiornamento(LocalDateTime.of(LocalDate.of(2020, 1, 10), LocalTime.of(1,0)));
		aziende.add(azienda);
		user.setAziende(aziende);
		Mockito.when(anagraficaUtenteClient.getUtenteConnesso()).thenReturn(user);
		
		Mockito.doReturn(LocalDate.of(2020, Month.OCTOBER, 19)).when(clock).today();
		// Long fascicoloId, Long sportelloId, CambioSportelloPatch cambioSportello
		Long sportelloId = 11L;
		CambioSportelloPatch patch = new CambioSportelloPatch();
		patch.setIdNuovoSportello(15L);
		patch.setMotivazione("motivazione cambio sportello");
		patch.setDataCambio(dateCambio);

		// mock verifica sportello abilitato
		List<String> entiUtenteConnesso = new ArrayList<>();
		entiUtenteConnesso.add("11");
		entiUtenteConnesso.add("13");
		entiUtenteConnesso.add("15");
		Mockito.when(anagraficaUtenteClient.getEntiUtenteConnesso()).thenReturn(entiUtenteConnesso);

		Exception exception = Assertions.assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(put(String.format("/api/v1/mandato/%s/sportello/%s", cuaa, sportelloId))
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(patch)))
			.andExpect(status().isBadRequest());
		});
		assertTrue(exception.getMessage().contains(String.format("Non è possibile cambiare sportello: Il Mandato è stato revocato.")));
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.mandato.modificasportello"})
	@Sql(scripts = "/sql/fascicolo/verificaCambioSportelloFailNonAbilitato_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/verificaCambioSportelloFailNonAbilitato_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void verificaCambioSportelloFailNonAbilitato() throws Exception {
		Mockito.doReturn(LocalDate.of(2020, Month.OCTOBER, 19)).when(clock).today();
		
		// uguale al test precedente con sportello non abilitato
		String cuaa = "12345678901";
		Long sportelloId = 11L;
		CambioSportelloPatch patch = new CambioSportelloPatch();
		patch.setIdNuovoSportello(13L);
		patch.setMotivazione("motivazione cambio sportello");
		patch.setDataCambio(LocalDate.of(2020, 8, 1));

		// mock verifica sportello abilitato
		List<String> entiUtenteConnesso = new ArrayList<>();
		entiUtenteConnesso.add("13");
		Mockito.when(anagraficaUtenteClient.getEntiUtenteConnesso()).thenReturn(entiUtenteConnesso);
		mockMvc.perform(put(String.format("/api/v1/mandato/%s/sportello/%s", cuaa, sportelloId))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(patch)))
		.andExpect(status().isForbidden());
		//assertTrue(exception.getMessage().contains(String.format("Non abilitato a ricercare il fascicolo del cuaa 12345678901")));
	}
	
	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.mandato.modificasportello"})
	@Sql(scripts = "/sql/fascicolo/verificaCambioSportelloFailControllato_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/verificaCambioSportelloSuccessful_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void verificaCambioSportelloFailControllato() throws Exception {
		// MOCK
		String cuaa = "MSTFBA79L10H612L";
		LocalDate dateCambio = LocalDate.of(2021, 1, 10);
		
		RappresentaIlModelloPerRappresentareUnUtenteDelSistema user = new RappresentaIlModelloPerRappresentareUnUtenteDelSistema();
		user.setIdentificativo("XPDNDR77B03L378X");
		user.setCodiceFiscale("XPDNDR77B03L378X");
		List<RappresentaIlModelloPerRappresentareUnAziendaAssociataAllutente> aziende = new ArrayList<>();
		RappresentaIlModelloPerRappresentareUnAziendaAssociataAllutente azienda = new RappresentaIlModelloPerRappresentareUnAziendaAssociataAllutente();
		azienda.setCuaa(cuaa);
		azienda.setDataAggiornamento(LocalDateTime.of(LocalDate.of(2020, 1, 10), LocalTime.of(1,0)));
		aziende.add(azienda);
		user.setAziende(aziende);
		Mockito.when(anagraficaUtenteClient.getUtenteConnesso()).thenReturn(user);
		
		Mockito.doReturn(LocalDate.of(2020, Month.OCTOBER, 19)).when(clock).today();
		// Long fascicoloId, Long sportelloId, CambioSportelloPatch cambioSportello
		Long sportelloId = 11L;
		CambioSportelloPatch patch = new CambioSportelloPatch();
		patch.setIdNuovoSportello(15L);
		patch.setMotivazione("motivazione cambio sportello");
		patch.setDataCambio(dateCambio);

		// mock verifica sportello abilitato
		List<String> entiUtenteConnesso = new ArrayList<>();
		entiUtenteConnesso.add("11");
		entiUtenteConnesso.add("13");
		entiUtenteConnesso.add("15");
		Mockito.when(anagraficaUtenteClient.getEntiUtenteConnesso()).thenReturn(entiUtenteConnesso);

		Exception exception = Assertions.assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(put(String.format("/api/v1/mandato/%s/sportello/%s", cuaa, sportelloId))
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(patch)))
			.andExpect(status().isBadRequest());
		});
		assertEquals(FascicoloAllaFirmaAziendaException.class.getName(), exception.getCause().getClass().getName());
	}
	
	@Transactional
	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.mandato.modificasportello"})
	@Sql(scripts = "/sql/fascicolo/verificaCambioSportelloSuccessful_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/verificaCambioSportelloSuccessful_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void sportelloModelCheck() throws Exception {
		Optional<SportelloModel> sportelloOpt = sportelloDao.findById(11L);
		assertEquals(true, sportelloOpt.isPresent());
		SportelloModel sportello = sportelloOpt.get(); 
//		[begin] per coverage: verifica uguaglianza model
		assertEquals(true, sportello.equals(sportello));
		assertEquals(false, sportello.equals(null));
		assertEquals(false, sportello.equals(new Object()));
		SportelloModel sportelloTest = new SportelloModel();
		BeanUtils.copyProperties(sportello, sportelloTest);
		sportelloTest.setEmail("paperoga@topolinia.us");
		assertEquals(false, sportello.equals(sportelloTest));
//		[end] per coverage: verifica uguaglianza model
	}
}
