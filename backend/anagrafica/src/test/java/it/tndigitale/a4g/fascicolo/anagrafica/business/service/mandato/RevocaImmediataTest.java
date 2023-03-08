package it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import it.tndigitale.a4g.fascicolo.anagrafica.api.ApiUrls;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.RevocaImmediataModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.StatoRevocaImmediata;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.UnitaTecnicoEconomicheModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.RevocaImmediataDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaUtenteClient;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.RichiestaRevocaImmediataDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.caa.SportelloCAADto;
import it.tndigitale.a4g.framework.client.custom.DocumentDto;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.utente.client.model.RappresentaIlModelloPerRappresentareUnUtenteDelSistema;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente", roles = {"a4gfascicolo.fascicolo.ricerca.tutti"})
class RevocaImmediataTest {

	@Value("#{T(java.time.LocalDate).parse('${revoca-immediata.periodo.da}' + '/' + T(java.time.LocalDate).now().getYear(), T(java.time.format.DateTimeFormatter).ofPattern('${revoca-immediata.periodo.formato}' + '/yyyy'))}")
	LocalDate periodoRevocaImmediataDa;
	
	@Value("#{T(java.time.LocalDate).parse('${revoca-immediata.periodo.a}' + '/' + T(java.time.LocalDate).now().getYear(), T(java.time.format.DateTimeFormatter).ofPattern('${revoca-immediata.periodo.formato}' + '/yyyy'))}")
	LocalDate periodoRevocaImmediataA;

	
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private RestTemplate restTemplate;
	@MockBean
	private AnagraficaUtenteClient anagraficaUtenteClient;
	@Autowired
	private Clock clock;
	@Autowired
	private RevocaImmediataMandatoService revocaImmediataMandatoService;
	@Autowired
	RevocaImmediataDao revocaImmediataDao;
	
	
	@Test
	@Sql(scripts = "/sql/fascicolo/revoca_immediata_puo_inserire_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/revoca_immediata_puo_inserire_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void utenteCorrentePuoInserireRichiesta_CuaaPersonaFisicaTrueOrReturn500WhenInvalidDate() throws Exception {
		String cuaa = "XPDNDR77B03L378X";
		String endpoint = String.format("/%s/puo-inserire-revoca-immediata", cuaa);
		
		RappresentaIlModelloPerRappresentareUnUtenteDelSistema user = new RappresentaIlModelloPerRappresentareUnUtenteDelSistema();
		user.setIdentificativo("XPDNDR77B03L378X");
		user.setCodiceFiscale("XPDNDR77B03L378X");
		Mockito.when(anagraficaUtenteClient.getUtenteConnesso()).thenReturn(user);
		
//		controllo date
		final LocalDate giornoCorrente = LocalDate.now();
		
		if(!(giornoCorrente.compareTo(periodoRevocaImmediataDa) >= 0 && giornoCorrente.compareTo(periodoRevocaImmediataA) <= 0)) {
				this.mockMvc.perform(
						get(ApiUrls.MANDATO.concat(endpoint))
						)
					.andExpect(status().isBadRequest())
					.andExpect(status().reason(RevocaImmediataMandatoNotification.DATA_NON_VALIDA.name()));
		}else {
			this.mockMvc.perform(
					get(ApiUrls.MANDATO.concat(endpoint))
					)
				.andExpect(status().isOk())
				.andExpect(content().string("true"));	
		}
	}
	
	@Test
	@Sql(scripts = "/sql/fascicolo/revoca_immediata_puo_inserire_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/revoca_immediata_puo_inserire_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void utenteCorrentePuoInserireRichiesta_CuaaPersonaFisicaConRichiestaDaValutareFalseOrReturn500WhenInvalidDate() throws Exception {
		String cuaa = "XPDNDR77B03L378Y";
		String endpoint = String.format("/%s/puo-inserire-revoca-immediata", cuaa);
		
		RappresentaIlModelloPerRappresentareUnUtenteDelSistema user = new RappresentaIlModelloPerRappresentareUnUtenteDelSistema();
		user.setIdentificativo("XPDNDR77B03L378Y");
		user.setCodiceFiscale("XPDNDR77B03L378Y");
		Mockito.when(anagraficaUtenteClient.getUtenteConnesso()).thenReturn(user);
		
//		controllo date
		final LocalDate giornoCorrente = LocalDate.now();
		
		if(!(giornoCorrente.compareTo(periodoRevocaImmediataDa) >= 0 && giornoCorrente.compareTo(periodoRevocaImmediataA) <= 0)) {
				this.mockMvc.perform(
						get(ApiUrls.MANDATO.concat(endpoint))
						)
					.andExpect(status().isBadRequest())
					.andExpect(status().reason(RevocaImmediataMandatoNotification.DATA_NON_VALIDA.name()));
		}else {
			this.mockMvc.perform(
					get(ApiUrls.MANDATO.concat(endpoint))
					)
				.andExpect(status().isBadRequest())
				.andExpect(status().reason(RevocaImmediataMandatoNotification.RICHIESTA_REVOCA_IMMEDIATA_PRESENTE_E_DA_VALUTARE.name()));
		}
	}
	
	@Test
	@Sql(scripts = "/sql/fascicolo/revoca_immediata_puo_inserire_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/revoca_immediata_puo_inserire_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void utenteCorrentePuoInserireRichiesta_CuaaPersonaGiuridicaTrueOrReturn500WhenInvalidDate() throws Exception {
		String cuaa = "00959460221";
		String endpoint = String.format("/%s/puo-inserire-revoca-immediata", cuaa);
		
		RappresentaIlModelloPerRappresentareUnUtenteDelSistema user = new RappresentaIlModelloPerRappresentareUnUtenteDelSistema();
		user.setIdentificativo("DPDNDR77B03L378W");
		user.setCodiceFiscale("DPDNDR77B03L378W");
		Mockito.when(anagraficaUtenteClient.getUtenteConnesso()).thenReturn(user);
		
//		controllo date
		final LocalDate giornoCorrente = LocalDate.now();
		
		if(!(giornoCorrente.compareTo(periodoRevocaImmediataDa) >= 0 && giornoCorrente.compareTo(periodoRevocaImmediataA) <= 0)) {
				this.mockMvc.perform(
						get(ApiUrls.MANDATO.concat(endpoint))
						)
					.andExpect(status().isBadRequest())
					.andExpect(status().reason(RevocaImmediataMandatoNotification.DATA_NON_VALIDA.name()));
		}else {
			this.mockMvc.perform(
					get(ApiUrls.MANDATO.concat(endpoint))
					)
				.andExpect(status().isOk())
				.andExpect(content().string("true"));	
		}
	}
	
	
	@Test
	@Sql(scripts = "/sql/fascicolo/revoca_immediata_puo_inserire_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/revoca_immediata_puo_inserire_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void utenteCorrentePuoInserireRichiesta_CuaaPersonaGiuridicaConRichiestaDaValutareFalseOrReturn500WhenInvalidDate() throws Exception {
		String cuaa = "00959460222";
		String endpoint = String.format("/%s/puo-inserire-revoca-immediata", cuaa);
		
		RappresentaIlModelloPerRappresentareUnUtenteDelSistema user = new RappresentaIlModelloPerRappresentareUnUtenteDelSistema();
		user.setIdentificativo("DPDNDR77B03L378J");
		user.setCodiceFiscale("DPDNDR77B03L378J");
		Mockito.when(anagraficaUtenteClient.getUtenteConnesso()).thenReturn(user);
		
//		controllo date
		final LocalDate giornoCorrente = LocalDate.now();
		
		if(!(giornoCorrente.compareTo(periodoRevocaImmediataDa) >= 0 && giornoCorrente.compareTo(periodoRevocaImmediataA) <= 0)) {
				this.mockMvc.perform(
						get(ApiUrls.MANDATO.concat(endpoint))
						)
					.andExpect(status().isBadRequest())
					.andExpect(status().reason(RevocaImmediataMandatoNotification.DATA_NON_VALIDA.name()));
		}else {
			this.mockMvc.perform(
					get(ApiUrls.MANDATO.concat(endpoint))
					)
				.andExpect(status().isBadRequest())
				.andExpect(status().reason(RevocaImmediataMandatoNotification.RICHIESTA_REVOCA_IMMEDIATA_PRESENTE_E_DA_VALUTARE.name()));
		}
	}
	
	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.mandato.revocaimmediata"})
	@Sql(scripts = "/sql/fascicolo/revoca_immediata_puo_inserire_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/revoca_immediata_puo_inserire_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getDocumentoDtoPersonaFisica() throws Exception {
		String cuaa = "XPDNDR77B03L378X";
		Path path = Paths.get("src/test/resources/fascicolo/MANDATO_ftoDPDNDR77B03L378L.pdf");
		MockMultipartFile moduloRevocaFirmato = new MockMultipartFile("moduloRevocaFirmato.pdf", Files.readAllBytes(path));
		
		SportelloCAADto sportelloCAADto = new SportelloCAADto();
		sportelloCAADto.setIdentificativo(13L);
		sportelloCAADto.setDenominazione("Sportello futuro");

		RichiestaRevocaImmediataDto richiestaRevocaImmediataDto = new RichiestaRevocaImmediataDto()
				.setCodiceFiscale(cuaa)
				.setCodiceFiscaleRappresentante(cuaa)
				.setRichiestaRevocaImmediataFirmata(new ByteArrayResource(moduloRevocaFirmato.getBytes(), moduloRevocaFirmato.getOriginalFilename()))
				.setCausaRichiesta("")
				.setIdentificativoSportello(null)
				.setSportello(sportelloCAADto)
				;
		
		richiestaRevocaImmediataDto.setId(999999L);
		DocumentDto documentDto =  revocaImmediataMandatoService.getDocumentDto(richiestaRevocaImmediataDto, 0);
		assertNotNull(documentDto.getMetadati());
		assertEquals("A4G - RICHIESTA REVOCA IMMEDIATA MANDATO - XPDNDR77B03L378X - ANDREA DEPEDRI", documentDto.getMetadati().getOggetto());
	}
	
	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.mandato.revocaimmediata"})
	@Sql(scripts = "/sql/fascicolo/revoca_immediata_puo_inserire_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/revoca_immediata_puo_inserire_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getDocumentoDtoPersonaGiuridica() throws Exception {
		String cuaa = "00959460221";
		Path path = Paths.get("src/test/resources/fascicolo/MANDATO_ftoDPDNDR77B03L378L.pdf");
		MockMultipartFile moduloRevocaFirmato = new MockMultipartFile("moduloRevocaFirmato.pdf", Files.readAllBytes(path));
		
		SportelloCAADto sportelloCAADto = new SportelloCAADto();
		sportelloCAADto.setIdentificativo(13L);
		sportelloCAADto.setDenominazione("Sportello futuro");

		RichiestaRevocaImmediataDto richiestaRevocaImmediataDto = new RichiestaRevocaImmediataDto()
				.setCodiceFiscale(cuaa)
				.setCodiceFiscaleRappresentante(cuaa)
				.setRichiestaRevocaImmediataFirmata(new ByteArrayResource(moduloRevocaFirmato.getBytes(), moduloRevocaFirmato.getOriginalFilename()))
				.setCausaRichiesta("")
				.setIdentificativoSportello(null)
				.setSportello(sportelloCAADto)
				;
		
		richiestaRevocaImmediataDto.setId(999999L);
		DocumentDto documentDto =  revocaImmediataMandatoService.getDocumentDto(richiestaRevocaImmediataDto, 0);
		assertNotNull(documentDto.getMetadati());
		assertEquals("A4G - RICHIESTA REVOCA IMMEDIATA MANDATO - 00959460221 - denominazione", documentDto.getMetadati().getOggetto());
	}
	
	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.mandato.revocaimmediata"})
	@Sql(scripts = "/sql/fascicolo/revoca_immediata_puo_inserire_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/revoca_immediata_puo_inserire_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void updateRevocaImmediataConProtocollo() throws Exception {
		String cuaa = "00959460221";
		Path path = Paths.get("src/test/resources/fascicolo/MANDATO_ftoDPDNDR77B03L378L.pdf");
		MockMultipartFile moduloRevocaFirmato = new MockMultipartFile("moduloRevocaFirmato.pdf", Files.readAllBytes(path));
		
		SportelloCAADto sportelloCAADto = new SportelloCAADto();
		sportelloCAADto.setIdentificativo(13L);
		sportelloCAADto.setDenominazione("Sportello futuro");

		RichiestaRevocaImmediataDto richiestaRevocaImmediataDto = new RichiestaRevocaImmediataDto()
				.setCodiceFiscale(cuaa)
				.setCodiceFiscaleRappresentante(cuaa)
				.setRichiestaRevocaImmediataFirmata(new ByteArrayResource(moduloRevocaFirmato.getBytes(), moduloRevocaFirmato.getOriginalFilename()))
				.setCausaRichiesta("")
				.setIdentificativoSportello(null)
				.setSportello(sportelloCAADto)
				;
		
		richiestaRevocaImmediataDto.setId(2L);
		revocaImmediataMandatoService.updateRevocaImmediataConProtocollo(richiestaRevocaImmediataDto, "PROT-00001");
		Optional<RevocaImmediataModel> revocaImmediataModelOpt = revocaImmediataDao.findById(2L);
		assertFalse(revocaImmediataModelOpt.isEmpty());
		RevocaImmediataModel revocaImmediataModel = revocaImmediataModelOpt.get();
		assertEquals("PROT-00001", revocaImmediataModel.getIdProtocollo());
		
//		[begin] per coverage: verifica uguaglianza model
		assertEquals(true, revocaImmediataModel.equals(revocaImmediataModel));
		assertEquals(false, revocaImmediataModel.equals(null));
		assertEquals(false, revocaImmediataModel.equals(new Object()));
		RevocaImmediataModel revocaImmediataModelTest = new RevocaImmediataModel();
		BeanUtils.copyProperties(revocaImmediataModel, revocaImmediataModelTest);
		revocaImmediataModelTest.setStato(StatoRevocaImmediata.ACCETTATA);
		assertEquals(false, revocaImmediataModel.equals(revocaImmediataModelTest));
//		[end] per coverage: verifica uguaglianza model
	}
	
}
