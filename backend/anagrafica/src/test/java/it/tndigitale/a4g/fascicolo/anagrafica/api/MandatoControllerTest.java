package it.tndigitale.a4g.fascicolo.anagrafica.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.MandatoModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.RevocaImmediataModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.StatoRevocaImmediata;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.MandatoDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.RevocaImmediataDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaProxyClient;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaUtenteClient;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.mail.*;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato.RevocaImmediataMandatoNotification;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato.RevocaImmediataMandatoService;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.BusinessResponsesDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.BusinessResponsesDto.Esiti;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.DescrizioneRichiestaRevocaImmediataMandatoDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.caa.SportelloCAADto;
import it.tndigitale.a4g.framework.client.custom.MetadatiDocumentoFirmatoDto;
import it.tndigitale.a4g.framework.client.custom.VerificaFirmaClient;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicoloId;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento.Ordine;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.proxy.client.model.*;
import it.tndigitale.a4g.utente.client.model.RappresentaIlModelloDelProfiloDiUnUtente;
import it.tndigitale.a4g.utente.client.model.RappresentaIlModelloPerRappresentareUnUtenteDelSistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente", roles = {"a4gfascicolo.fascicolo.ricerca.tutti"})
class MandatoControllerTest {
	
	@Value("#{T(java.time.LocalDate).parse('${revoca-immediata.periodo.da}' + '/' + T(java.time.LocalDate).now().getYear(), T(java.time.format.DateTimeFormatter).ofPattern('${revoca-immediata.periodo.formato}' + '/yyyy'))}")
	LocalDate periodoRevocaImmediataDa;
	
	@Value("#{T(java.time.LocalDate).parse('${revoca-immediata.periodo.a}' + '/' + T(java.time.LocalDate).now().getYear(), T(java.time.format.DateTimeFormatter).ofPattern('${revoca-immediata.periodo.formato}' + '/yyyy'))}")
	LocalDate periodoRevocaImmediataA;

	private final long ID_ENTE_CONNESSO = 11L;

	private TransactionTemplate transactionTemplate;
	@Autowired
	PlatformTransactionManager transactionManager;
	@MockBean
	AnagraficaProxyClient anagraficaProxyClient;
	@MockBean
	VerificaFirmaClient verificaFirmaClient;
	@Autowired
	private RevocaImmediataMandatoService richiestaRevocaImmediataMandatoService;
	@Autowired
	MandatoController mandatoController;
	@Autowired
	MandatoDao mandatoDao;
	@MockBean
	private AnagraficaUtenteClient anagraficaUtenteClient;
	@MockBean
	EmailTemplateRevocaOrdinariaMandato emailTemplateRevocaOrdinariaMandato;
	@MockBean
	EmailTemplateRichiestaRevocaImmediataAccettataAppag emailTemplateRichiestaRevocaImmediataAccettataAppag;
	@MockBean
	EmailTemplateRichiestaRevocaImmediataAccettataTitolare emailTemplateRichiestaRevocaImmediataAccettataTitolare;
	@MockBean
	EmailTemplateRichiestaRevocaImmediataRifiutataAppag emailTemplateRichiestaRevocaImmediataRifiutataAppag;
	@MockBean
	EmailTemplateRichiestaRevocaImmediataRifiutataTitolare emailTemplateRichiestaRevocaImmediataRifiutataTitolare;
	@Autowired
	RevocaImmediataDao revocaImmediataDao;
	@MockBean
	Clock clock;
	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper objectMapper;
	
	private LocalDate testDay;
	
	public 	MandatoControllerTest() {
		testDay = LocalDate.of(2021, Month.JANUARY, 1);
	}
	
	@BeforeEach
	void testSetup() {
		List<String> entiUtenteConnesso = new ArrayList<String>();
		entiUtenteConnesso.add("94");
		Mockito.when(anagraficaUtenteClient.getEntiUtenteConnesso()).thenReturn(entiUtenteConnesso);
		
		Mockito.when(clock.today()).thenReturn(testDay);
	}

	private ByteArrayResource createMockMultipartFile() {
		Path path = Paths.get("src/test/resources/fascicolo/MANDATO_ftoDPDNDR77B03L378L.pdf");
		String name = "MANDATO_ftoDPDNDR77B03L378L";
		byte[] content = null;
		try {
			content = Files.readAllBytes(path);
		} catch (final IOException e) {
		}
		return new ByteArrayResource(content, name);
	}

	private void userSetupCaa(Long idEnte) {
		List<String> entiUtenteConnesso = new ArrayList<>();
		String idEnteConnesso = String.valueOf(ID_ENTE_CONNESSO);
		if (idEnte != null) {
			idEnteConnesso = String.valueOf(idEnte);
		}
		entiUtenteConnesso.add(idEnteConnesso);

		Mockito.when(anagraficaUtenteClient.getEntiUtenteConnesso()).thenReturn(entiUtenteConnesso);
		RappresentaIlModelloPerRappresentareUnUtenteDelSistema user = new RappresentaIlModelloPerRappresentareUnUtenteDelSistema();
		user.setIdentificativo("FRRMSM80A22H612G");
		user.setCodiceFiscale("FRRMSM80A22H612G");
		user.setNome("FRRMSM80A22H612G");
		List<RappresentaIlModelloDelProfiloDiUnUtente> profili = new ArrayList<>();
		RappresentaIlModelloDelProfiloDiUnUtente profilo = new RappresentaIlModelloDelProfiloDiUnUtente();
		profilo.setIdentificativo("caa");
		profili.add(profilo);
		user.setProfili(profili);
		Mockito.when(anagraficaUtenteClient.getUtenteConnesso()).thenReturn(user);
	}

	private void mockSianAppag(final String cuaa, boolean isAppag) {
		FascicoloSian mockSian = new FascicoloSian();
		mockSian.setCuaa(cuaa);
		mockSian.setOrganismoPagatore(isAppag ? FascicoloSian.OrganismoPagatoreEnum.APPAG : FascicoloSian.OrganismoPagatoreEnum.ALTRO);
		Mockito.when(anagraficaProxyClient.verificaEsistenzaFascicolo(Mockito.anyString())).thenReturn(mockSian);
	}

//	TODO
	private void mockAnagrafeTributariaPersonaFisica(final String cuaa, final Boolean deceduta,
													 final Boolean provTN) {
		var mockPersonaFisica = new PersonaFisicaDto();
		mockPersonaFisica.setCodiceFiscale(cuaa);
		AnagraficaDto anagraficaDto = new AnagraficaDto();
		anagraficaDto.setCognome("Cognome");
		anagraficaDto.setNome("Nome");
		anagraficaDto.setComuneNascita("Rovereto");
		anagraficaDto.setProvinciaNascita("TN");
		anagraficaDto.setDataNascita(LocalDate.of(1970, 1, 1));
		IndirizzoDto indirizzoDto = new IndirizzoDto();
		indirizzoDto.setCap("38068");
		indirizzoDto.setComune("Rovereto");
		indirizzoDto.setDenominazioneEstesa("via Rossi");
		indirizzoDto.setProvincia("TN");
		mockPersonaFisica.setDomicilioFiscale(indirizzoDto);
		mockPersonaFisica.setAnagrafica(anagraficaDto);
		mockPersonaFisica.setDeceduta(deceduta);
		mockPersonaFisica.setImpresaIndividuale(new ImpresaIndividualeDto());
		mockPersonaFisica.getImpresaIndividuale().setSedeLegale(new SedeDto());
		mockPersonaFisica.getImpresaIndividuale().getSedeLegale().setIndirizzo(new IndirizzoDto());
		mockPersonaFisica.getImpresaIndividuale().getSedeLegale().getIndirizzo().setProvincia("BZ");
		mockPersonaFisica.getImpresaIndividuale().setDenominazione("Impresa individuale");
		if (provTN) {
			mockPersonaFisica.getImpresaIndividuale().getSedeLegale().getIndirizzo().setProvincia("TN");
		}

		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagrafeTributaria(Mockito.anyString())).thenReturn(mockPersonaFisica);
	}

	//	TODO
	private void mockAnagrafeTributariaPersonaGiuridica(final String cuaa,
														final Boolean provTN) {
		var mockPersonaGiuridica = new PersonaGiuridicaDto();
		RappresentanteLegaleDto rappresentanteLegaleDto = new RappresentanteLegaleDto();
		rappresentanteLegaleDto.setNominativo("Nominativo");
		rappresentanteLegaleDto.setCodiceFiscale("BBBBBBBBBBBBBBBB");
		mockPersonaGiuridica.setRappresentanteLegale(rappresentanteLegaleDto);
		mockPersonaGiuridica.setCodiceFiscale(cuaa);
		mockPersonaGiuridica.setPartitaIva(cuaa);
		mockPersonaGiuridica.setDenominazione("Denominazione");
		mockPersonaGiuridica.setFormaGiuridica("Forma giuridica");
		mockPersonaGiuridica.setSedeLegale(new SedeDto());
		mockPersonaGiuridica.getSedeLegale().setIndirizzo(new IndirizzoDto());
		mockPersonaGiuridica.getSedeLegale().getIndirizzo().setProvincia("BZ");
		if (provTN) {
			mockPersonaGiuridica.getSedeLegale().getIndirizzo().setProvincia("TN");
		}
		Mockito.when(anagraficaProxyClient.getPersonaGiuridicaAnagrafeTributaria(Mockito.anyString())).thenReturn(mockPersonaGiuridica);
	}
	
	@Test
	@Sql(scripts = "/sql/fascicolo/mandato/revocaMandatoTest_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/mandato/revocaMandatoTest_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "caa", roles = {"a4ganagrafica.mandato.revocaimmediata.cruscotto.ente"})
	@Transactional
	void lista_richieste_revoca_immediata_come_caa_not_empty() throws Exception {
		String stringResponse = mockMvc.perform(get(ApiUrls.MANDATO + "/richieste-revoca-immediata-mandato?valutata=false"))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();
		List<DescrizioneRichiestaRevocaImmediataMandatoDto> richiesteNonValutate = objectMapper.readValue(stringResponse, new TypeReference<List<DescrizioneRichiestaRevocaImmediataMandatoDto>>(){});
		assertEquals(2, richiesteNonValutate.size());
		
		stringResponse = mockMvc.perform(get(ApiUrls.MANDATO + "/richieste-revoca-immediata-mandato?valutata=true"))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();
		List<DescrizioneRichiestaRevocaImmediataMandatoDto> richiesteValutate = objectMapper.readValue(stringResponse, new TypeReference<List<DescrizioneRichiestaRevocaImmediataMandatoDto>>(){});
		assertEquals(2, richiesteValutate.size());
	}
	
	@Test
	@Sql(scripts = "/sql/fascicolo/mandato/revocaMandatoTest_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/mandato/revocaMandatoTest_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "caa", roles = {"a4ganagrafica.mandato.revocaimmediata.cruscotto.ente"})
	@Transactional
	void lista_richieste_revoca_immediata_valutate_come_caa_empty() throws Exception {
		List<String> entiUtenteConnesso = new ArrayList<String>();
		entiUtenteConnesso.add("1");
		Mockito.when(anagraficaUtenteClient.getEntiUtenteConnesso()).thenReturn(entiUtenteConnesso);
		
		mockMvc.perform(get(ApiUrls.MANDATO + "/richieste-revoca-immediata-mandato?valutata=true"))
				.andExpect(status().isNoContent());
	}
	
	/**
	 * test commentato per modifica AbilitazioniComponent.checkListaRichiesteRevovaImmediataMandato
	 * (rimosso controllo per ruolo 'appag')
	 * creato test alternativo 'lista_tutte_richieste_revoca_immediata_valutate_come_appag_accesso_negato'
	 */
	// @Test
	// @Sql(scripts = "/sql/fascicolo/mandato/revocaMandatoTest_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	// @Sql(scripts = "/sql/fascicolo/mandato/revocaMandatoTest_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	// @WithMockUser(username = "appag", roles = {"a4ganagrafica.mandato.revocaimmediata.cruscotto.valutate.tutti"})
	// @Transactional
	// void lista_tutte_richieste_revoca_immediata_valutate_come_appag_not_empty() throws Exception {
	// 	String stringResponse = mockMvc.perform(get(ApiUrls.MANDATO + "/richieste-revoca-immediata-mandato?valutata=true"))
	// 			.andExpect(status().is2xxSuccessful())
	// 			.andReturn().getResponse().getContentAsString();
	// 	List<DescrizioneRichiestaRevocaImmediataMandatoDto> richiesteValutate = objectMapper.readValue(
	// 			stringResponse, new TypeReference<List<DescrizioneRichiestaRevocaImmediataMandatoDto>>(){});
	// 	assertEquals(3, richiesteValutate.size());
	// }
	
	@Test
	@Sql(scripts = "/sql/fascicolo/mandato/revocaMandatoTest_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/mandato/revocaMandatoTest_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "responsabile_fascicolo_pat", roles = {"a4ganagrafica.mandato.revocaimmediata.cruscotto.valutate.tutti"})
	@Transactional
	void lista_tutte_richieste_revoca_immediata_valutate_come_responsabile_fascicolo_pat_accesso_consentito() throws Exception {
		mockMvc.perform(get(ApiUrls.MANDATO + "/richieste-revoca-immediata-mandato?valutata=true"))
				.andExpect(status().is2xxSuccessful());
	}
	
	@Test
	@Sql(scripts = "/sql/fascicolo/mandato/revocaMandatoTest_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/mandato/revocaMandatoTest_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "responsabile_fascicolo_pat", roles = {"a4ganagrafica.mandato.revocaimmediata.cruscotto.valutate.tutti"})
	@Transactional
	void lista_richieste_revoca_immediata_non_valutate_come_responsabile_fascicolo_pat_accesso_consentito() throws Exception {
		mockMvc.perform(get(ApiUrls.MANDATO + "/richieste-revoca-immediata-mandato?valutata=false"))
				.andExpect(status().is2xxSuccessful());
	}
	
	@Test
	@Sql(scripts = "/sql/fascicolo/mandato/revocaMandatoTest_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/mandato/revocaMandatoTest_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void lista_richieste_revoca_immediataValutate_is_paged() throws Exception {
		Ordinamento ordinamento = new Ordinamento("idProtocollo", Ordine.ASC);
		Paginazione paginazione = Paginazione.of().setNumeroElementiPagina(1).setPagina(0);
		RisultatiPaginati<DescrizioneRichiestaRevocaImmediataMandatoDto> richiesteNonValutatePaged =
				richiestaRevocaImmediataMandatoService.getRichiesteRevocaImmediataDto(false, paginazione, ordinamento);
		assertEquals(StatoRevocaImmediata.DA_VALUTARE, richiesteNonValutatePaged.getRisultati().get(0).getEsito());
		assertEquals("PAT_TEST/XXXXXX-XX/XX/XXXX-XXXXXX1", richiesteNonValutatePaged.getRisultati().get(0).getIdProtocollo());
		assertEquals(1, richiesteNonValutatePaged.getRisultati().size());
		assertEquals(2, richiesteNonValutatePaged.getCount());
		
		RisultatiPaginati<DescrizioneRichiestaRevocaImmediataMandatoDto> richiesteValutatePaged =
				richiestaRevocaImmediataMandatoService.getRichiesteRevocaImmediataDto(true, paginazione, ordinamento);
		richiesteValutatePaged.getRisultati();
		assertNotEquals(StatoRevocaImmediata.DA_VALUTARE, richiesteValutatePaged.getRisultati().get(0).getEsito());
		assertEquals("PAT_TEST/XXXXXX-XX/XX/XXXX-XXXXXX2", richiesteValutatePaged.getRisultati().get(0).getIdProtocollo());
		assertEquals(1, richiesteValutatePaged.getRisultati().size());
		assertEquals(2, richiesteValutatePaged.getCount());

		paginazione = Paginazione.of().setNumeroElementiPagina(1).setPagina(1);
		RisultatiPaginati<DescrizioneRichiestaRevocaImmediataMandatoDto> richiesteNonValutatePaged2 =
				richiestaRevocaImmediataMandatoService.getRichiesteRevocaImmediataDto(false, paginazione, ordinamento);
		assertEquals(StatoRevocaImmediata.DA_VALUTARE, richiesteNonValutatePaged2.getRisultati().get(0).getEsito());
		assertEquals("PAT_TEST/XXXXXX-XX/XX/XXXX-XXXXXX3", richiesteNonValutatePaged2.getRisultati().get(0).getIdProtocollo());
		assertEquals(1, richiesteNonValutatePaged2.getRisultati().size());
		assertEquals(2, richiesteNonValutatePaged2.getCount());
		
		RisultatiPaginati<DescrizioneRichiestaRevocaImmediataMandatoDto> richiesteValutatePaged2 =
				richiestaRevocaImmediataMandatoService.getRichiesteRevocaImmediataDto(true, paginazione, ordinamento);
		assertNotEquals(StatoRevocaImmediata.DA_VALUTARE, richiesteValutatePaged2.getRisultati().get(0).getEsito());
		assertEquals("PAT_TEST/XXXXXX-XX/XX/XXXX-XXXXXX4", richiesteValutatePaged2.getRisultati().get(0).getIdProtocollo());
		assertEquals(1, richiesteValutatePaged2.getRisultati().size());
		assertEquals(2, richiesteValutatePaged2.getCount());
	}
	
	@Test
	@Sql(scripts = "/sql/fascicolo/mandato/revocaMandatoTest_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/mandato/revocaMandatoTest_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.mandato.revocaimmediata.accettazione"})
	@Transactional
	void accettaRichiesteRevocaImmediataValutateOK() {
		List<RevocaImmediataModel> revocaImmediataList = revocaImmediataDao.findByCodiceFiscale("LRCPLA50M11H612B");
		Optional<RevocaImmediataModel> revocaImmediataOptional = revocaImmediataList.stream().filter(
				revocaImmediata -> revocaImmediata.getStato().equals(StatoRevocaImmediata.DA_VALUTARE)).findFirst();
		assertTrue(revocaImmediataOptional.isPresent());
		RevocaImmediataModel revocaImmediataModel = revocaImmediataOptional.get();
		assertEquals(StatoRevocaImmediata.DA_VALUTARE, revocaImmediataModel.getStato());
		
		BusinessResponsesDto valutaRichiestaRevocaImmediata = mandatoController.valutaRichiestaRevocaImmediata("LRCPLA50M11H612B", true, null);
		assertEquals(Esiti.OK, valutaRichiestaRevocaImmediata.getEsito());

		revocaImmediataList = revocaImmediataDao.findByCodiceFiscale("LRCPLA50M11H612B");
		revocaImmediataOptional = revocaImmediataList.stream().filter(
				revocaImmediata -> revocaImmediata.getStato().equals(StatoRevocaImmediata.ACCETTATA)).findFirst();
		assertTrue(revocaImmediataOptional.isPresent());
	}
	
	@Test
	@Sql(scripts = "/sql/fascicolo/mandato/revocaMandatoTest_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/mandato/revocaMandatoTest_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.mandato.revocaimmediata.accettazione"})
	@Transactional
	void rifiutaRichiesteRevocaImmediataValutateOK() {
		LocalDate localTestDate = LocalDate.of(2020, Month.SEPTEMBER, 23);
		Mockito.when(clock.today()).thenReturn(localTestDate);
		
		List<RevocaImmediataModel> revocaImmediataList = revocaImmediataDao.findByCodiceFiscale("LRCPLA50M11H612B");
		Optional<RevocaImmediataModel> revocaImmediataOptional = revocaImmediataList.stream().filter(
				revocaImmediata -> revocaImmediata.getStato().equals(StatoRevocaImmediata.DA_VALUTARE)).findFirst();
		assertTrue(revocaImmediataOptional.isPresent());
		RevocaImmediataModel revocaImmediataModel = revocaImmediataOptional.get();
		assertEquals(StatoRevocaImmediata.DA_VALUTARE, revocaImmediataModel.getStato());
		
		BusinessResponsesDto valutaRichiestaRevocaImmediata = mandatoController.valutaRichiestaRevocaImmediata("LRCPLA50M11H612B", false, "Test motivazione");
		assertEquals(Esiti.OK, valutaRichiestaRevocaImmediata.getEsito());
		
		revocaImmediataList = revocaImmediataDao.findByCodiceFiscale("LRCPLA50M11H612B");
		revocaImmediataOptional = revocaImmediataList.stream().filter(
				revocaImmediata -> revocaImmediata.getStato().equals(StatoRevocaImmediata.RIFIUTATA)).findFirst();
		assertTrue(revocaImmediataOptional.isPresent());
		revocaImmediataModel = revocaImmediataOptional.get();
		assertEquals(StatoRevocaImmediata.RIFIUTATA, revocaImmediataModel.getStato());
		assertEquals("Test motivazione", revocaImmediataModel.getMotivazioneRifiuto());
		assertEquals(localTestDate, revocaImmediataModel.getDataValutazione());
	}
	
	private MockHttpServletRequestBuilder buildRequestRichiestaRevocaImmediata(SportelloCAADto sportello, String cuaa) throws IOException {
		String endpoint = String.format("/%s/richiesta-revoca-immediata-mandato", cuaa);
		Path path = Paths.get("src/test/resources/fascicolo/MANDATO_ftoDPDNDR77B03L378L.pdf");
		MockMultipartFile moduloRevocaFirmato = new MockMultipartFile("moduloRevocaFirmato.pdf", Files.readAllBytes(path));
		
		MockHttpServletRequestBuilder builder =
				MockMvcRequestBuilders.multipart(ApiUrls.MANDATO.concat(endpoint))
				.file("moduloRevocaFirmato", moduloRevocaFirmato.getBytes())
				.param("codiceFiscaleRappresentanteLegaleOTitolare", cuaa)
				.param("sportello", objectMapper.writeValueAsString(sportello))
				.contentType(MediaType.APPLICATION_JSON);
		return builder;
	}
	
	private MockHttpServletRequestBuilder buildRequestRichiestaRevocaImmediataKO(String cuaa) throws IOException {
		String endpoint = String.format("/%s/richiesta-revoca-immediata-mandato", cuaa);
		Path path = Paths.get("src/test/resources/fascicolo/MANDATO_ftoDPDNDR77B03L378L.pdf");
		MockMultipartFile moduloRevocaFirmato = new MockMultipartFile("moduloRevocaFirmato.pdf", Files.readAllBytes(path));
		
		MockHttpServletRequestBuilder builder =
				MockMvcRequestBuilders.multipart(ApiUrls.MANDATO.concat(endpoint))
				.file("moduloRevocaFirmato", moduloRevocaFirmato.getBytes())
				.param("codiceFiscaleRappresentanteLegaleOTitolare", cuaa)
				.param("sportello", objectMapper.writeValueAsString(13))
				.contentType(MediaType.APPLICATION_JSON);
		return builder;
	}
	
	
	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.mandato.revocaimmediata"})
	@Transactional
	@Sql(scripts = "/sql/fascicolo/revoca_immediata_puo_inserire_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/revoca_immediata_puo_inserire_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void creazioneRichiestaRevocaImmediataMandatoOK() throws Exception {
		String cuaa = "XPDNDR77B03L378X";
		
		RappresentaIlModelloPerRappresentareUnUtenteDelSistema user = new RappresentaIlModelloPerRappresentareUnUtenteDelSistema();
		user.setIdentificativo(cuaa);
		user.setCodiceFiscale(cuaa);
		Mockito.when(anagraficaUtenteClient.getUtenteConnesso()).thenReturn(user);
		
		SportelloCAADto sportello = new SportelloCAADto();
		sportello.setIdentificativo(13L);
		sportello.setDenominazione("Sportello futuro");
		
		MockHttpServletRequestBuilder requestBuilder = buildRequestRichiestaRevocaImmediata(sportello, cuaa);

		requestBuilder.with(request -> {
            request.setMethod(HttpMethod.POST.name());
            return request;
        });
		
//		controllo date
		if (!(testDay.compareTo(periodoRevocaImmediataDa) >= 0 && testDay.compareTo(periodoRevocaImmediataA) <= 0)) {
			this.mockMvc.perform(requestBuilder)
				.andExpect(status().is5xxServerError())
				.andExpect(status().reason(org.hamcrest.Matchers.containsString(RevocaImmediataMandatoNotification.DATA_NON_VALIDA.name())));
		} else {
			this.mockMvc.perform(requestBuilder)
				.andExpect(status().isOk());	
		}
	}
	
	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.mandato.revocaimmediata"})
	@Transactional
	@Sql(scripts = "/sql/fascicolo/revoca_immediata_puo_inserire_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/revoca_immediata_puo_inserire_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void creazioneRichiestaRevocaImmediataMandatoKO() throws Exception {
		String cuaa = "XPDNDR77B03L378X";
		
		RappresentaIlModelloPerRappresentareUnUtenteDelSistema user = new RappresentaIlModelloPerRappresentareUnUtenteDelSistema();
		user.setIdentificativo(cuaa);
		user.setCodiceFiscale(cuaa);
		Mockito.when(anagraficaUtenteClient.getUtenteConnesso()).thenReturn(user);
		
		SportelloCAADto sportello = new SportelloCAADto();
		sportello.setIdentificativo(13L);
		sportello.setDenominazione("Sportello futuro");
		
		MockHttpServletRequestBuilder requestBuilder = buildRequestRichiestaRevocaImmediataKO(cuaa);

		requestBuilder.with(request -> {
            request.setMethod(HttpMethod.POST.name());
            return request;
        });
//		controllo date
		if (!(testDay.compareTo(periodoRevocaImmediataDa) >= 0 && testDay.compareTo(periodoRevocaImmediataA) <= 0)) {
			this.mockMvc.perform(requestBuilder).andExpect(status().is5xxServerError());
		} else {
			this.mockMvc.perform(requestBuilder).andExpect(status().is5xxServerError());	
		}
	}
	
	@Test
	@Sql(scripts = "/sql/fascicolo/mandato/revocaMandatoTest_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/mandato/revocaMandatoTest_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.mandato.revocaimmediata.accettazione"})
	@Transactional
	void getPdfAllegatoContrattoMandato() throws Exception {
		Long mandatoId = 1857L;
		ResponseEntity<Resource> result = mandatoController.getPdfAllegatoContrattoMandato(11L, mandatoId, 0);
		assertEquals(HttpStatus.OK, result.getStatusCode());
		ByteArrayResource contratto = (ByteArrayResource)result.getBody();
		
		Optional<MandatoModel> mandato = mandatoDao.findById(new EntitaDominioFascicoloId(mandatoId, 0));
		assertFalse(mandato.isEmpty());
		assertEquals(mandato.get().getContratto(), contratto.getByteArray());
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTestFascicoloEsistentePersoneFisiche_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void verifica_dati_acquisizione_mandato_persona_fisica_ko_provincia_diversa_da_trento() throws Exception {
		userSetupCaa(89L);
		String cuaa = "AAABBB00C00D000F";
		mockAnagrafeTributariaPersonaFisica(cuaa, false, false);
		mockSianAppag(cuaa, true);

		String url = String.format("%s/revoca-immediata/%s/verifica/dati-acquisizione", ApiUrls.MANDATO, cuaa);
		String errorMessage = mockMvc.perform(get(url)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn().getResponse().getErrorMessage();

		assertEquals("PROVINCIA_DIVERSA_DA_TRENTO", errorMessage);
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTestFascicoloEsistentePersoneFisiche_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void verifica_dati_acquisizione_mandato_persona_fisica_ko_competenza_altro_op() throws Exception {
		userSetupCaa(89L);
		String cuaa = "AAABBB00C00D000F";
		mockAnagrafeTributariaPersonaFisica(cuaa, false, true);
		mockSianAppag(cuaa, false);

		String url = String.format("%s/revoca-immediata/%s/verifica/dati-acquisizione", ApiUrls.MANDATO, cuaa);
		String errorMessage = mockMvc.perform(get(url)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn().getResponse().getErrorMessage();

		assertEquals("COMPETENZA_ALTRO_OP", errorMessage);
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTestFascicoloEsistentePersoneFisiche_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void verifica_dati_acquisizione_mandato_persona_fisica_ko_deceduto() throws Exception {
		userSetupCaa(89L);
		String cuaa = "AAABBB00C00D000F";
		mockAnagrafeTributariaPersonaFisica(cuaa, true, true);
		mockSianAppag(cuaa, true);

		String url = String.format("%s/revoca-immediata/%s/verifica/dati-acquisizione", ApiUrls.MANDATO, cuaa);
		String errorMessage = mockMvc.perform(get(url)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn().getResponse().getErrorMessage();

		assertEquals("DECEDUTO", errorMessage);
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTestFascicoloEsistentePersoneFisiche_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void verifica_dati_acquisizione_mandato_persona_fisica_ko_fascicolo_chiuso() throws Exception {
		userSetupCaa(89L);
		String cuaa = "AAABBB00C00D000G";
		mockAnagrafeTributariaPersonaFisica(cuaa, false, true);
		mockSianAppag(cuaa, true);

		String url = String.format("%s/revoca-immediata/%s/verifica/dati-acquisizione", ApiUrls.MANDATO, cuaa);
		String errorMessage = mockMvc.perform(get(url)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn().getResponse().getErrorMessage();

		assertEquals("FASCICOLO_CHIUSO", errorMessage);
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTestFascicoloEsistentePersoneFisiche_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void verifica_dati_acquisizione_mandato_persona_fisica_ok() throws Exception {
		userSetupCaa(89L);
		String cuaa = "AAABBB00C00D000F";
		mockAnagrafeTributariaPersonaFisica(cuaa, false, true);
		mockSianAppag(cuaa, true);

		String url = String.format("%s/revoca-immediata/%s/verifica/dati-acquisizione", ApiUrls.MANDATO, cuaa);
		mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTestFascicoloEsistentePersoneGiuridiche_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void verifica_dati_acquisizione_mandato_persona_giuridica_ko_provincia_diversa_da_trento() throws Exception {
		userSetupCaa(89L);
		String cuaa = "00123890001";
		mockAnagrafeTributariaPersonaGiuridica(cuaa, false);
		mockSianAppag(cuaa, true);

		String url = String.format("%s/revoca-immediata/%s/verifica/dati-acquisizione", ApiUrls.MANDATO, cuaa);
		String errorMessage = mockMvc.perform(get(url)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn().getResponse().getErrorMessage();

		assertEquals("PROVINCIA_DIVERSA_DA_TRENTO", errorMessage);
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTestFascicoloEsistentePersoneGiuridiche_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void verifica_dati_acquisizione_mandato_persona_giuridica_ko_competenza_altro_organismo_pagatore() throws Exception {
		userSetupCaa(89L);
		String cuaa = "00123890001";
		mockAnagrafeTributariaPersonaGiuridica(cuaa, true);
		mockSianAppag(cuaa, false);

		String url = String.format("%s/revoca-immediata/%s/verifica/dati-acquisizione", ApiUrls.MANDATO, cuaa);
		String errorMessage = mockMvc.perform(get(url)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn().getResponse().getErrorMessage();

		assertEquals("COMPETENZA_ALTRO_OP", errorMessage);
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTestFascicoloEsistentePersoneGiuridiche_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void verifica_dati_acquisizione_mandato_persona_giuridica_ko_fascicolo_chiuso() throws Exception {
		userSetupCaa(89L);
		String cuaa = "00123890002";
		mockAnagrafeTributariaPersonaGiuridica(cuaa, true);
		mockSianAppag(cuaa, true);

		String url = String.format("%s/revoca-immediata/%s/verifica/dati-acquisizione", ApiUrls.MANDATO, cuaa);
		String errorMessage = mockMvc.perform(get(url)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn().getResponse().getErrorMessage();

		assertEquals("FASCICOLO_CHIUSO", errorMessage);
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTestFascicoloEsistentePersoneGiuridiche_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void verifica_dati_acquisizione_mandato_persona_giuridica_ok() throws Exception {
		userSetupCaa(89L);
		String cuaa = "00123890001";
		mockAnagrafeTributariaPersonaGiuridica(cuaa, true);
		mockSianAppag(cuaa, true);

		String url = String.format("%s/revoca-immediata/%s/verifica/dati-acquisizione", ApiUrls.MANDATO, cuaa);
		mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTestFascicoloEsistentePersoneGiuridiche_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void acquisizione_mandato_persona_giuridica_OK() throws Exception {
		transactionTemplate = new TransactionTemplate(transactionManager);
		userSetupCaa(89L);
		String cuaa = "00123890001";
		mockAnagrafeTributariaPersonaGiuridica(cuaa, true);
		mockSianAppag(cuaa, true);
		List<ByteArrayResource> mockAllegati = Arrays.asList(createMockMultipartFile(), createMockMultipartFile());
		ByteArrayResource mockMandato = createMockMultipartFile();
		String endpoint = String.format("/%s/acquisisci-mandato", cuaa);
		MetadatiDocumentoFirmatoDto infoVerificaFirma = new MetadatiDocumentoFirmatoDto();
		infoVerificaFirma.setCfFirmatario(cuaa);
		infoVerificaFirma.setDataFirma(clock.today());
		Mockito.when(verificaFirmaClient.verificaFirma(Mockito.any(), Mockito.anyString())).thenReturn(infoVerificaFirma);

		MockHttpServletRequestBuilder requestBuilder =
				MockMvcRequestBuilders.multipart(ApiUrls.MANDATO.concat(endpoint))
						.file("contratto", mockMandato.getByteArray())
						.file("allegati", mockAllegati.get(0).getByteArray())
						.file("allegati", mockAllegati.get(1).getByteArray())
						.param("codiceFiscale", cuaa)
						.param("codiceFiscaleRappresentante", cuaa)
						.param("identificativoSportello", Long.toString(89L))
						.contentType(MediaType.APPLICATION_JSON);

		requestBuilder.with(request -> {
			request.setMethod(HttpMethod.POST.name());
			return request;
		});
		this.mockMvc.perform(requestBuilder);

		MandatoModel mandatoSaved = transactionTemplate.execute(status -> {
			List<MandatoModel> mandati = mandatoDao.findAll();
			MandatoModel mandatoModel = mandati.stream().filter( mandato -> mandato.getFascicolo().getCuaa().equals(cuaa)).findFirst().orElseThrow();

			return mandatoModel;
		});


		assertNotEquals(null, mandatoSaved);
		assertNotEquals(null, mandatoSaved.getFascicolo());
		assertEquals(cuaa, mandatoSaved.getFascicolo().getCuaa());
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTestFascicoloEsistentePersoneGiuridiche_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void acquisizione_mandato_persona_giuridica_KO_fascicolo_chiuso() throws Exception {
		transactionTemplate = new TransactionTemplate(transactionManager);
		userSetupCaa(89L);
		String cuaa = "00123890002";
		mockAnagrafeTributariaPersonaGiuridica(cuaa, true);
		mockSianAppag(cuaa, true);
		List<ByteArrayResource> mockAllegati = Arrays.asList(createMockMultipartFile(), createMockMultipartFile());
		ByteArrayResource mockMandato = createMockMultipartFile();
		String endpoint = String.format("/%s/acquisisci-mandato", cuaa);
		MetadatiDocumentoFirmatoDto infoVerificaFirma = new MetadatiDocumentoFirmatoDto();
		infoVerificaFirma.setCfFirmatario(cuaa);
		infoVerificaFirma.setDataFirma(clock.today());
		Mockito.when(verificaFirmaClient.verificaFirma(Mockito.any(), Mockito.anyString())).thenReturn(infoVerificaFirma);

		MockHttpServletRequestBuilder requestBuilder =
				MockMvcRequestBuilders.multipart(ApiUrls.MANDATO.concat(endpoint))
						.file("contratto", mockMandato.getByteArray())
						.file("allegati", mockAllegati.get(0).getByteArray())
						.file("allegati", mockAllegati.get(1).getByteArray())
						.param("codiceFiscale", cuaa)
						.param("codiceFiscaleRappresentante", cuaa)
						.param("identificativoSportello", Long.toString(89L))
						.contentType(MediaType.APPLICATION_JSON);

		requestBuilder.with(request -> {
			request.setMethod(HttpMethod.POST.name());
			return request;
		});
		String errorMessage = this.mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
				.andReturn().getResponse().getErrorMessage();
		assertEquals("FASCICOLO_CHIUSO", errorMessage);
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTestFascicoloEsistentePersoneGiuridiche_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void acquisizione_mandato_persona_giuridica_KO_competenza_altro_organismo_pagatore() throws Exception {
		transactionTemplate = new TransactionTemplate(transactionManager);
		userSetupCaa(89L);
		String cuaa = "00123890001";
		mockAnagrafeTributariaPersonaGiuridica(cuaa, true);
		mockSianAppag(cuaa, false);
		List<ByteArrayResource> mockAllegati = Arrays.asList(createMockMultipartFile(), createMockMultipartFile());
		ByteArrayResource mockMandato = createMockMultipartFile();
		String endpoint = String.format("/%s/acquisisci-mandato", cuaa);
		MetadatiDocumentoFirmatoDto infoVerificaFirma = new MetadatiDocumentoFirmatoDto();
		infoVerificaFirma.setCfFirmatario(cuaa);
		infoVerificaFirma.setDataFirma(clock.today());
		Mockito.when(verificaFirmaClient.verificaFirma(Mockito.any(), Mockito.anyString())).thenReturn(infoVerificaFirma);

		MockHttpServletRequestBuilder requestBuilder =
				MockMvcRequestBuilders.multipart(ApiUrls.MANDATO.concat(endpoint))
						.file("contratto", mockMandato.getByteArray())
						.file("allegati", mockAllegati.get(0).getByteArray())
						.file("allegati", mockAllegati.get(1).getByteArray())
						.param("codiceFiscale", cuaa)
						.param("codiceFiscaleRappresentante", cuaa)
						.param("identificativoSportello", Long.toString(89L))
						.contentType(MediaType.APPLICATION_JSON);

		requestBuilder.with(request -> {
			request.setMethod(HttpMethod.POST.name());
			return request;
		});
		String errorMessage = this.mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
				.andReturn().getResponse().getErrorMessage();
		assertEquals("COMPETENZA_ALTRO_OP", errorMessage);
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTestFascicoloEsistentePersoneGiuridiche_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void acquisizione_mandato_persona_giuridica_KO_provincia_diversa_da_tn() throws Exception {
		transactionTemplate = new TransactionTemplate(transactionManager);
		userSetupCaa(89L);
		String cuaa = "00123890001";
		mockAnagrafeTributariaPersonaGiuridica(cuaa, false);
		mockSianAppag(cuaa, true);
		List<ByteArrayResource> mockAllegati = Arrays.asList(createMockMultipartFile(), createMockMultipartFile());
		ByteArrayResource mockMandato = createMockMultipartFile();
		String endpoint = String.format("/%s/acquisisci-mandato", cuaa);
		MetadatiDocumentoFirmatoDto infoVerificaFirma = new MetadatiDocumentoFirmatoDto();
		infoVerificaFirma.setCfFirmatario(cuaa);
		infoVerificaFirma.setDataFirma(clock.today());
		Mockito.when(verificaFirmaClient.verificaFirma(Mockito.any(), Mockito.anyString())).thenReturn(infoVerificaFirma);

		MockHttpServletRequestBuilder requestBuilder =
				MockMvcRequestBuilders.multipart(ApiUrls.MANDATO.concat(endpoint))
						.file("contratto", mockMandato.getByteArray())
						.file("allegati", mockAllegati.get(0).getByteArray())
						.file("allegati", mockAllegati.get(1).getByteArray())
						.param("codiceFiscale", cuaa)
						.param("codiceFiscaleRappresentante", cuaa)
						.param("identificativoSportello", Long.toString(89L))
						.contentType(MediaType.APPLICATION_JSON);

		requestBuilder.with(request -> {
			request.setMethod(HttpMethod.POST.name());
			return request;
		});
		String errorMessage = this.mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
				.andReturn().getResponse().getErrorMessage();
		assertEquals("PROVINCIA_DIVERSA_DA_TRENTO", errorMessage);
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTestFascicoloEsistentePersoneFisiche_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void acquisizione_mandato_persona_fisica_OK() throws Exception {
		transactionTemplate = new TransactionTemplate(transactionManager);
		userSetupCaa(89L);
		String cuaa = "AAABBB00C00D000F";
		mockAnagrafeTributariaPersonaFisica(cuaa, false, true);
		mockSianAppag(cuaa, true);
		List<ByteArrayResource> mockAllegati = Arrays.asList(createMockMultipartFile(), createMockMultipartFile());
		ByteArrayResource mockMandato = createMockMultipartFile();
		String endpoint = String.format("/%s/acquisisci-mandato", cuaa);
		MetadatiDocumentoFirmatoDto infoVerificaFirma = new MetadatiDocumentoFirmatoDto();
		infoVerificaFirma.setCfFirmatario(cuaa);
		infoVerificaFirma.setDataFirma(clock.today());
		Mockito.when(verificaFirmaClient.verificaFirma(Mockito.any(), Mockito.anyString())).thenReturn(infoVerificaFirma);

		MockHttpServletRequestBuilder requestBuilder =
				MockMvcRequestBuilders.multipart(ApiUrls.MANDATO.concat(endpoint))
						.file("contratto", mockMandato.getByteArray())
						.file("allegati", mockAllegati.get(0).getByteArray())
						.file("allegati", mockAllegati.get(1).getByteArray())
						.param("codiceFiscale", cuaa)
						.param("codiceFiscaleRappresentante", cuaa)
						.param("identificativoSportello", Long.toString(89L))
						.contentType(MediaType.APPLICATION_JSON);

		requestBuilder.with(request -> {
			request.setMethod(HttpMethod.POST.name());
			return request;
		});
		this.mockMvc.perform(requestBuilder);

		MandatoModel mandatoSaved = transactionTemplate.execute(status -> {
			List<MandatoModel> mandati = mandatoDao.findAll();
			MandatoModel mandatoModel = mandati.stream().filter( mandato -> mandato.getFascicolo().getCuaa().equals(cuaa)).findFirst().orElseThrow();

			return mandatoModel;
		});


		assertNotEquals(null, mandatoSaved);
		assertNotEquals(null, mandatoSaved.getFascicolo());
		assertEquals(cuaa, mandatoSaved.getFascicolo().getCuaa());
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTestFascicoloEsistentePersoneFisiche_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void acquisizione_mandato_persona_fisica_KO_fascicolo_chiuso() throws Exception {
		transactionTemplate = new TransactionTemplate(transactionManager);
		userSetupCaa(89L);
		String cuaa = "AAABBB00C00D000G";
		mockAnagrafeTributariaPersonaFisica(cuaa, false, true);
		mockSianAppag(cuaa, true);
		List<ByteArrayResource> mockAllegati = Arrays.asList(createMockMultipartFile(), createMockMultipartFile());
		ByteArrayResource mockMandato = createMockMultipartFile();
		String endpoint = String.format("/%s/acquisisci-mandato", cuaa);
		MetadatiDocumentoFirmatoDto infoVerificaFirma = new MetadatiDocumentoFirmatoDto();
		infoVerificaFirma.setCfFirmatario(cuaa);
		infoVerificaFirma.setDataFirma(clock.today());
		Mockito.when(verificaFirmaClient.verificaFirma(Mockito.any(), Mockito.anyString())).thenReturn(infoVerificaFirma);

		MockHttpServletRequestBuilder requestBuilder =
				MockMvcRequestBuilders.multipart(ApiUrls.MANDATO.concat(endpoint))
						.file("contratto", mockMandato.getByteArray())
						.file("allegati", mockAllegati.get(0).getByteArray())
						.file("allegati", mockAllegati.get(1).getByteArray())
						.param("codiceFiscale", cuaa)
						.param("codiceFiscaleRappresentante", cuaa)
						.param("identificativoSportello", Long.toString(89L))
						.contentType(MediaType.APPLICATION_JSON);

		requestBuilder.with(request -> {
			request.setMethod(HttpMethod.POST.name());
			return request;
		});
		String errorMessage = this.mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
				.andReturn().getResponse().getErrorMessage();
		assertEquals("FASCICOLO_CHIUSO", errorMessage);
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTestFascicoloEsistentePersoneFisiche_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void acquisizione_mandato_persona_fisica_KO_competenza_altro_organismo_pagatore() throws Exception {
		transactionTemplate = new TransactionTemplate(transactionManager);
		userSetupCaa(89L);
		String cuaa = "AAABBB00C00D000F";
		mockAnagrafeTributariaPersonaFisica(cuaa, false, true);
		mockSianAppag(cuaa, false);
		List<ByteArrayResource> mockAllegati = Arrays.asList(createMockMultipartFile(), createMockMultipartFile());
		ByteArrayResource mockMandato = createMockMultipartFile();
		String endpoint = String.format("/%s/acquisisci-mandato", cuaa);
		MetadatiDocumentoFirmatoDto infoVerificaFirma = new MetadatiDocumentoFirmatoDto();
		infoVerificaFirma.setCfFirmatario(cuaa);
		infoVerificaFirma.setDataFirma(clock.today());
		Mockito.when(verificaFirmaClient.verificaFirma(Mockito.any(), Mockito.anyString())).thenReturn(infoVerificaFirma);

		MockHttpServletRequestBuilder requestBuilder =
				MockMvcRequestBuilders.multipart(ApiUrls.MANDATO.concat(endpoint))
						.file("contratto", mockMandato.getByteArray())
						.file("allegati", mockAllegati.get(0).getByteArray())
						.file("allegati", mockAllegati.get(1).getByteArray())
						.param("codiceFiscale", cuaa)
						.param("codiceFiscaleRappresentante", cuaa)
						.param("identificativoSportello", Long.toString(89L))
						.contentType(MediaType.APPLICATION_JSON);

		requestBuilder.with(request -> {
			request.setMethod(HttpMethod.POST.name());
			return request;
		});
		String errorMessage = this.mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
				.andReturn().getResponse().getErrorMessage();
		assertEquals("COMPETENZA_ALTRO_OP", errorMessage);
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTestFascicoloEsistentePersoneFisiche_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void acquisizione_mandato_persona_fisica_KO_provincia_diversa_da_tn() throws Exception {
		transactionTemplate = new TransactionTemplate(transactionManager);
		userSetupCaa(89L);
		String cuaa = "AAABBB00C00D000F";
		mockAnagrafeTributariaPersonaFisica(cuaa, false, false);
		mockSianAppag(cuaa, true);
		List<ByteArrayResource> mockAllegati = Arrays.asList(createMockMultipartFile(), createMockMultipartFile());
		ByteArrayResource mockMandato = createMockMultipartFile();
		String endpoint = String.format("/%s/acquisisci-mandato", cuaa);
		MetadatiDocumentoFirmatoDto infoVerificaFirma = new MetadatiDocumentoFirmatoDto();
		infoVerificaFirma.setCfFirmatario(cuaa);
		infoVerificaFirma.setDataFirma(clock.today());
		Mockito.when(verificaFirmaClient.verificaFirma(Mockito.any(), Mockito.anyString())).thenReturn(infoVerificaFirma);

		MockHttpServletRequestBuilder requestBuilder =
				MockMvcRequestBuilders.multipart(ApiUrls.MANDATO.concat(endpoint))
						.file("contratto", mockMandato.getByteArray())
						.file("allegati", mockAllegati.get(0).getByteArray())
						.file("allegati", mockAllegati.get(1).getByteArray())
						.param("codiceFiscale", cuaa)
						.param("codiceFiscaleRappresentante", cuaa)
						.param("identificativoSportello", Long.toString(89L))
						.contentType(MediaType.APPLICATION_JSON);

		requestBuilder.with(request -> {
			request.setMethod(HttpMethod.POST.name());
			return request;
		});
		String errorMessage = this.mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
				.andReturn().getResponse().getErrorMessage();
		assertEquals("PROVINCIA_DIVERSA_DA_TRENTO", errorMessage);
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTestFascicoloEsistentePersoneFisiche_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void acquisizione_mandato_persona_fisica_KO_deceduto() throws Exception {
		transactionTemplate = new TransactionTemplate(transactionManager);
		userSetupCaa(89L);
		String cuaa = "AAABBB00C00D000F";
		mockAnagrafeTributariaPersonaFisica(cuaa, true, true);
		mockSianAppag(cuaa, true);
		List<ByteArrayResource> mockAllegati = Arrays.asList(createMockMultipartFile(), createMockMultipartFile());
		ByteArrayResource mockMandato = createMockMultipartFile();
		String endpoint = String.format("/%s/acquisisci-mandato", cuaa);
		MetadatiDocumentoFirmatoDto infoVerificaFirma = new MetadatiDocumentoFirmatoDto();
		infoVerificaFirma.setCfFirmatario(cuaa);
		infoVerificaFirma.setDataFirma(clock.today());
		Mockito.when(verificaFirmaClient.verificaFirma(Mockito.any(), Mockito.anyString())).thenReturn(infoVerificaFirma);

		MockHttpServletRequestBuilder requestBuilder =
				MockMvcRequestBuilders.multipart(ApiUrls.MANDATO.concat(endpoint))
						.file("contratto", mockMandato.getByteArray())
						.file("allegati", mockAllegati.get(0).getByteArray())
						.file("allegati", mockAllegati.get(1).getByteArray())
						.param("codiceFiscale", cuaa)
						.param("codiceFiscaleRappresentante", cuaa)
						.param("identificativoSportello", Long.toString(89L))
						.contentType(MediaType.APPLICATION_JSON);

		requestBuilder.with(request -> {
			request.setMethod(HttpMethod.POST.name());
			return request;
		});
		String errorMessage = this.mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
				.andReturn().getResponse().getErrorMessage();
		assertEquals("DECEDUTO", errorMessage);
	}
}
