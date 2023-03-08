package it.tndigitale.a4g.fascicolo.anagrafica.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.fascicolo.anagrafica.Ruoli;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.OrganizzazioneModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.AttivitaAtecoDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.CaricaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.DestinazioneUsoDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.FascicoloDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.FascicoloOrganizzazioneDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.MandatoDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.ModoPagamentoDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.OrganizzazioneDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.PersonaGiuridicaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.UnitaTecnicoEconomicheDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.legacy.FascicoloAgsDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaProxyClient;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaUtenteClient;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.StampaComponent;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.ZootecniaPrivateClient;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloService;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.RicercaFascicoloService;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.ValidazioneFascicoloService;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.DichiarazioneAssociativaDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.OrganizzazioneDto;
import it.tndigitale.a4g.fascicolo.anagrafica.ioitalia.IoItaliaConsumerApi;
import it.tndigitale.a4g.framework.client.custom.VerificaFirmaClient;
import it.tndigitale.a4g.framework.event.store.EventStoreService;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.framework.security.service.UtenteClient;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.utente.client.model.RappresentaIlModelloDelProfiloDiUnUtente;
import it.tndigitale.a4g.utente.client.model.RappresentaIlModelloPerRappresentareUnUtenteDelSistema;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class FascicoloControllerPreAuthTest {
	@MockBean JavaMailSender emailSender;
	@MockBean AnagraficaUtenteClient anagraficaUtenteClient;
	@MockBean AnagraficaProxyClient anagraficaProxyClient;
	@MockBean Clock clock;
	@MockBean SecurityContext securityContect;
	@MockBean Authentication authentication;
	@MockBean FascicoloAgsDao soggettoService;
	@MockBean IoItaliaConsumerApi ioItaliaConsumerApi;
	@MockBean ZootecniaPrivateClient zootecniaPrivateClient;
	@MockBean StampaComponent stampaComponent;
	@MockBean VerificaFirmaClient verificaFirmaClient;
	@MockBean UtenteComponent utenteComponent;
	@MockBean UtenteClient abilitazioniUtente;
	@Autowired FascicoloController fascicoloController;
	@Autowired FascicoloService fascicoloService;
	@Autowired ValidazioneFascicoloService validazioneFascicoloService;
	@Autowired RicercaFascicoloService ricercaFascicoloService;
	@Autowired FascicoloDao fascicoloDao;
	@Autowired MandatoDao mandatoDao;
	@Autowired PersonaGiuridicaDao personaGiuridicaDao;
	@Autowired ModoPagamentoDao pagamentoDao;
	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper objectMapper;
	@Autowired EventStoreService eventStoreService;
	@Autowired AttivitaAtecoDao attivitaAtecoDao;
	@Autowired UnitaTecnicoEconomicheDao uteDao;
	@Autowired DestinazioneUsoDao destinazioneUsoDao;
	@Autowired CaricaDao caricaDao;
	@Autowired OrganizzazioneDao organizzazioneDao;
	@Autowired FascicoloOrganizzazioneDao fascicoloOrganizzazioneDao;
	@Autowired PlatformTransactionManager transactionManager;

	private LocalDate testDayLocalDate = LocalDate.of(2020, Month.JANUARY, 1);
	private LocalDateTime testDayLocalDateTime = LocalDateTime.of(2020, Month.FEBRUARY, 12, 1, 1);
	static final String PROV_TRENTO = "TN";
	
	@BeforeEach
	void initialize() {
		new TransactionTemplate(transactionManager);
		Mockito.when(clock.today()).thenReturn(testDayLocalDate);
		Mockito.when(clock.now()).thenReturn(testDayLocalDateTime);
		Mockito.when(utenteComponent.username()).thenReturn("XPDNDR77B03L378X");
		Mockito.when(utenteComponent.utenza()).thenReturn("XPDNDR77B03L378X");
	}

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

	private void mockStartValidazioneZootecniaOkStuff() {
		Mockito.when(zootecniaPrivateClient.startValidazioneFascicolo(
				Mockito.anyString(), Mockito.anyInt())).thenReturn(
						new ResponseEntity<>(HttpStatus.OK));
	}

	@Test
	@WithMockUser(username = "utente-azienda")
	@Sql(scripts = {
			"/sql/fascicolo/organizzazioni_insert.sql", 
			"/sql/fascicolo/fascicolo_controllato_insert.sql"
	}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = {
			"/sql/fascicolo/fascicolo_delete.sql",
			"/sql/fascicolo/organizzazioni_delete.sql"
	}, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void getAllOrganizzazioni() throws Exception {
		String cuaa = "XPDNDR77B03L378X";
		userSetupAzienda(cuaa);
		mockStartValidazioneZootecniaOkStuff();

		String stringResponse = mockMvc.perform(
				get(String.format("%s/organizzazioni", ApiUrls.FASCICOLO))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		List<OrganizzazioneDto> result = objectMapper.readValue(stringResponse,new TypeReference<List<OrganizzazioneDto>>(){});
		assertEquals(1, result.size());
		assertEquals("Consorzio difesa produttori agricoli Co.Di.Pr.A", result.get(0).getDenominazione());
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.dichiarazioniassociative.visualizza.utente"})
	@Sql(scripts = {
			"/sql/fascicolo/organizzazioni_insert.sql", 
			"/sql/fascicolo/fascicolo_controllato_insert.sql",
			"/sql/fascicolo/autodichiarazioni_associative_insert.sql"
	}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = {
			"/sql/fascicolo/autodichiarazioni_associative_delete.sql",
			"/sql/fascicolo/fascicolo_delete.sql",
			"/sql/fascicolo/organizzazioni_delete.sql",
	}, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void getAutodichiarazioniAssociative() throws Exception {
		String cuaa = "XPDNDR77B03L378X";
		mockAziendeUtente(cuaa);
		userSetupAzienda(cuaa);
		// mockWebClientOkStuff();

		String stringResponse = mockMvc.perform(
				get(String.format("/api/v1/fascicolo/%s/rappresentante-legale/autodichiarazioni-associative", cuaa))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		List<DichiarazioneAssociativaDto> result = objectMapper.readValue(stringResponse,new TypeReference<List<DichiarazioneAssociativaDto>>(){});
		assertEquals(1, result.size());
		assertEquals("Consorzio difesa produttori agricoli Co.Di.Pr.A", result.get(0).getOrganizzazione().getDenominazione());
	}

	@Test
	@WithMockUser(username = "XPDNDR77B03L378X", roles = {
			"a4ganagrafica.dichiarazioniassociative.visualizza.utente",
			"a4ganagrafica.dichiarazioniassociative.edita.utente"
	})
	@Sql(scripts = "/sql/fascicolo/fascicolo_autodichiarazione_associativa_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_autodichiarazione_associativa_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void aggiungiAutodichiarazioneAssociativaOk() throws Exception {
		String cuaa = "XPDNDR77B03L378X";
		mockAziendeUtente(cuaa);
		userSetupAzienda(cuaa);

		DichiarazioneAssociativaDto dichiarazioneAssociativaDto = new DichiarazioneAssociativaDto();
		dichiarazioneAssociativaDto.setDataInizioAssociazione(LocalDate.of(2020, 10, 20));
		OrganizzazioneDto organizzazioneDto = new OrganizzazioneDto();
		organizzazioneDto.setId(26529L);
		organizzazioneDto.setDenominazione("Consorzio difesa produttori agricoli Co.Di.Pr.A");
		dichiarazioneAssociativaDto.setOrganizzazione(organizzazioneDto);

		mockMvc.perform(put(
				String.format("%s/%s/rappresentante-legale/aggiungi-autodichiarazione-associativa", ApiUrls.FASCICOLO, cuaa)
				)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dichiarazioneAssociativaDto)))
		.andExpect(status().isOk());

		//           controllo salvataggio autodichiarazione
		Optional<FascicoloModel> fascicoloOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0);
		assertEquals(true, fascicoloOpt.isPresent());
		FascicoloModel fascicoloModel = fascicoloOpt.get();
		OrganizzazioneModel organizzazioneModel = organizzazioneDao.getOne(26529L);
		var fascicoloOrganizzazioneList = fascicoloOrganizzazioneDao.findByCuaaAndOrganizzazione(fascicoloModel.getCuaa(), organizzazioneModel);


		assertEquals(1, fascicoloOrganizzazioneList.size());
		assertEquals(testDayLocalDateTime, fascicoloOrganizzazioneList.get(0).getDataInserimentoAssociazione());
		assertEquals(LocalDate.of(2020, 10, 20), fascicoloOrganizzazioneList.get(0).getDataInizioAssociazione());
	}

	@Test
	@WithMockUser(username = "utente", roles = {
			"a4ganagrafica.dichiarazioniassociative.visualizza.utente",
			"a4ganagrafica.dichiarazioniassociative.edita.utente"
	})
	@Sql(scripts = {
			"/sql/fascicolo/organizzazioni_insert.sql", 
			"/sql/fascicolo/fascicolo_controllato_insert.sql",
			"/sql/fascicolo/autodichiarazioni_associative_insert.sql"
	}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = {
			"/sql/fascicolo/autodichiarazioni_associative_delete.sql",
			"/sql/fascicolo/fascicolo_delete.sql",
			"/sql/fascicolo/organizzazioni_delete.sql",
	}, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void chiudiDichiarazioneAssociativa() throws Exception {
		String cuaa = "XPDNDR77B03L378X";
		mockAziendeUtente(cuaa);
		userSetupAzienda(cuaa);
		// mockWebClientOkStuff();
		List<DichiarazioneAssociativaDto> dichiarazioniAssociative = fascicoloService.getDichiarazioniAssociative(cuaa);
		DichiarazioneAssociativaDto dichiarazioneAssociativaDto = dichiarazioniAssociative.get(0);
		dichiarazioneAssociativaDto.setDataFineAssociazione(LocalDate.now());

		mockMvc.perform(put(String.format("%s/%s/rappresentante-legale/chiudi-autodichiarazione-associativa", ApiUrls.FASCICOLO, cuaa))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dichiarazioneAssociativaDto)))
		.andExpect(status().isOk());

		SecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
		dichiarazioneAssociativaDto = fascicoloService.getDichiarazioniAssociative(cuaa).get(0);
		assertNotNull(dichiarazioneAssociativaDto.getDataFineAssociazione());
		assertNotNull(dichiarazioneAssociativaDto.getDataCancellazioneAssociazione());
	}

	@Sql(scripts = "/sql/fascicolo/fascicolo_autodichiarazione_associativa_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_autodichiarazione_associativa_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void aggiungiAutodichiarazioneAssociativaKO() throws Exception {
		String cuaa = "XPDNDR77B03L378X";
		userSetupAzienda(cuaa);

		DichiarazioneAssociativaDto dichiarazioneAssociativaDto = new DichiarazioneAssociativaDto();
		assertThrows(IllegalArgumentException.class, () -> {
			fascicoloController.rappresentanteLegaleAggiungiAutodichiarazioneAssociativa(cuaa, dichiarazioneAssociativaDto);
		});
	}

	@Test
	@WithMockUser(username = "utente", roles = {
			"a4ganagrafica.dichiarazioniassociative.visualizza.utente",
			"a4ganagrafica.dichiarazioniassociative.edita.utente"
	})
	@Sql(scripts = "/sql/fascicolo/fascicolo_autodichiarazione_associativa_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_autodichiarazione_associativa_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void aggiungiAutodichiarazioneAssociativaKO_GiaPresente() throws Exception {
		String cuaa = "XPDNDR77B03L378Y";
		mockAziendeUtente(cuaa);
		userSetupAzienda(cuaa);

		DichiarazioneAssociativaDto dichiarazioneAssociativaDto = new DichiarazioneAssociativaDto();
		dichiarazioneAssociativaDto.setDataInizioAssociazione(LocalDate.of(2020, 10, 20));
		OrganizzazioneDto organizzazioneDto = new OrganizzazioneDto();
		organizzazioneDto.setId(26530L);
		organizzazioneDto.setDenominazione("Consorzio difesa produttori agricoli Paperopoli");
		dichiarazioneAssociativaDto.setOrganizzazione(organizzazioneDto);

		assertThrows(ResponseStatusException.class, () -> {
			fascicoloController.rappresentanteLegaleAggiungiAutodichiarazioneAssociativa(cuaa, dichiarazioneAssociativaDto);
		});
	}

	void mockAziendeUtente(String cuaa) throws Exception {
		Mockito.when(utenteComponent.haUnRuolo(Ruoli.DICHIARAZIONI_ASSOCIATIVE_VISUALIZZA_UTENTE)).thenReturn(true);
		Mockito.when(utenteComponent.haUnRuolo(Ruoli.DICHIARAZIONI_ASSOCIATIVE_EDITA_UTENTE)).thenReturn(true);

		List<String> cuaaAbilitati = new ArrayList<>();
		cuaaAbilitati.add(cuaa);

		Mockito.when(abilitazioniUtente.getAziendeUtente()).thenReturn(cuaaAbilitati);
	}

}
