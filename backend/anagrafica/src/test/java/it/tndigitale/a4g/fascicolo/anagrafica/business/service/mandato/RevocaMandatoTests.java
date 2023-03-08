package it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.NestedServletException;

import it.tndigitale.a4g.fascicolo.anagrafica.api.ApiUrls;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.MandatoModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.MandatoDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaUtenteClient;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.mail.EmailService;
import it.tndigitale.a4g.fascicolo.anagrafica.util.DateTimeConstants;
import it.tndigitale.a4g.framework.client.custom.MetadatiDocumentoFirmatoDto;
import it.tndigitale.a4g.framework.client.custom.VerificaFirmaClient;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicoloId;
import it.tndigitale.a4g.framework.time.Clock;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente")
class RevocaMandatoTests {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private Clock clock;
	@Autowired
	private MandatoDao mandatoDao;
	@MockBean
	private VerificaFirmaClient verificaFirmaClient;
	@MockBean
	private AnagraficaUtenteClient anagraficaUtenteClient;
    @MockBean
    public EmailService emailService;
	@Value("${revoca-ordinaria-mandato.email-dto.to}")
	private String destinatarioMail;

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.mandato.revoca.ente"})
	@Sql(scripts = "/sql/fascicolo/mandato/revocaMandatoTest_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/mandato/revocaMandatoTest_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void revocaOrdinariaSuccessful() throws Exception {
		/**
		 * Il test consiste nell'accettare una revoca ordinaria ad un mandato per il quale
		 * è già in programma un cambio di sportello (precedentemente accettato).
		 * La revoca ordinaria ha effetto dal 1 gennaio dell'anno successivo alla richiesta,
		 * il cambio sportello avviene a settembre dell'anno corrente.
		 * In questo caso il corretto funzionamento consiste nel creare nuove righe nelle tabelle
		 * mandato e detenzione per il nuovo mandato ed assegnare come data fine il 31 dicembre a
		 * quelle il cui subentro è già programmato per via del cambio di sportello fatto precedentemente.
		 * 
		 * CUAA: LRCPLA50M11H612B - ID: 50
		 * CAA Attuale: CAA COOPTN - LAVIS - ID: 93
		 * CAA Destinazione: CAA ATS - 005 - ROVERETO - ID: 57
		 */
		Long identificativoSportello = 57L;
		String cuaa = "LRCPLA50M11H612B";
		Long mandatoId = 1857L;

		MockHttpServletRequestBuilder request = buildRequest(identificativoSportello, cuaa, mandatoId);

		// MOCK
		LocalDate settembre17_2020 = LocalDate.of(2020, Month.SEPTEMBER, 17);
		Mockito.doReturn(settembre17_2020).when(clock).today();
		MetadatiDocumentoFirmatoDto datiFirma = new MetadatiDocumentoFirmatoDto();
		datiFirma.setCfFirmatario(cuaa);
		datiFirma.setDataFirma(settembre17_2020);
		Mockito.when(verificaFirmaClient.verificaFirma(Mockito.any(), Mockito.anyString())).thenReturn(datiFirma);

		List<String> entiUtenteConnesso = new ArrayList<>();
		entiUtenteConnesso.add("56");
		entiUtenteConnesso.add("57");
		Mockito.when(anagraficaUtenteClient.getEntiUtenteConnesso()).thenReturn(entiUtenteConnesso);

		// chiamata al servizio
		String response = this.mockMvc.perform(request)
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		assertNotNull(response);

		Optional<MandatoModel> mandatoNewOpt = mandatoDao.findById(new EntitaDominioFascicoloId(Long.valueOf(response), 0));
		Optional<MandatoModel> mandatoOldOpt = mandatoDao.findById(new EntitaDominioFascicoloId(1858L, 0)); // detenzione programmata
		assertTrue(mandatoNewOpt.isPresent());
		assertTrue(mandatoOldOpt.isPresent());

		MandatoModel mandatoNew = mandatoNewOpt.get();

		assertEquals(LocalDate.of(2021, Month.JANUARY, 1), mandatoNew.getDataInizio());
		assertNull(mandatoNew.getDataFine());
		assertNotNull(mandatoNew.getContratto());	
		assertNotNull(mandatoNew.getId());	
		assertEquals(cuaa, mandatoNew.getFascicolo().getCuaa());
		assertEquals(identificativoSportello, mandatoNew.getSportello().getIdentificativo());

		MandatoModel mandatoOld = mandatoOldOpt.get();

		assertEquals(LocalDate.of(2020, Month.SEPTEMBER,21), mandatoOld.getDataInizio());
		assertEquals(LocalDate.of(2020, Month.DECEMBER, 31), mandatoOld.getDataFine());
		assertNotNull(mandatoOld.getContratto());
		assertEquals(1858, mandatoOld.getId());
		assertEquals(cuaa, mandatoOld.getFascicolo().getCuaa());
		assertEquals(94, mandatoOld.getSportello().getIdentificativo());

		// invio mail APPAG
		String oggetto = "Revoca Ordinaria Mandato";
		
		final LocalDate primoGennaioAnnoSuccessivo = LocalDate.of(2021, Month.JANUARY, 1);
		String[] mailArgs = {
				primoGennaioAnnoSuccessivo.format(DateTimeFormatter.ofPattern(DateTimeConstants.DATE_PATTERN)),
				cuaa,
				"denominazione",
				"CAA COOPTRENTO SRL"
		};
		String text = String.format("Buongiorno, vi informiamo che a partire dal %s il mandato dell\u2019azienda %s - %s non sar\u00E0 pi\u00F9 in carico al %s perch\u00E8 il mandato \u00E8 stato revocato con revoca ordinaria.",
        		mailArgs);
		verify(emailService, times(1)).sendSimpleMessage(destinatarioMail,oggetto, text);
		
		// invio mail CAA
		verify(emailService, times(1)).sendSimpleMessage("pippo@pippo.com", oggetto, text);
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.mandato.revoca.ente"})
	@Sql(scripts = "/sql/fascicolo/mandato/revocaMandatoTest_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/mandato/revocaMandatoTest_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("Revoca non consentita fuori finestra temporale")
	void revocaOrdinariaFuoriFinestraTemporale() throws Exception {

		Long identificativoSportello = 57L;
		String cuaa = "LRCPLA50M11H612B";
		Long mandatoId = 1857L;

		MockHttpServletRequestBuilder request = buildRequest(identificativoSportello, cuaa, mandatoId);

		// MOCK
		LocalDate dicembre13_2020 = LocalDate.of(2020, Month.DECEMBER, 13); // SYSDATE in data non consona alla revoca ordinaria del mandato  (01/12-31/12)
		Mockito.doReturn(dicembre13_2020).when(clock).today();

		// chiamata al servizio
		Exception exception = Assertions.assertThrows(NestedServletException.class, () -> {
			this.mockMvc.perform(request).andExpect(status().is5xxServerError());
		});
		assertTrue(exception.getMessage().contains(String.format("FINESTRA_TEMPORALE_NON_VALIDA")));
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.mandato.revoca.ente"})
	@Sql(scripts = "/sql/fascicolo/mandato/revocaMandatoTest_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/mandato/revocaMandatoTest_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("Revoca non consentita allo stesso CAA")
	void revocaOrdinariaAlloStessoCAA() throws Exception {
		/**
		 * CUAA: LRCPLA50M11H612B - ID: 50
		 * CAA Attuale: CAA COOPTN - LAVIS 
		 * CAA Destinazione: CAA COOPTN - ISERA 
		 */
		Long identificativoSportello = 104L;
		String cuaa = "LRCPLA50M11H612B";
		Long mandatoId = 1857L;

		MockHttpServletRequestBuilder request = buildRequest(identificativoSportello, cuaa, mandatoId);

		// MOCK

		LocalDate settembre17_2020 = LocalDate.of(2020, Month.SEPTEMBER, 17);
		Mockito.doReturn(settembre17_2020).when(clock).today();

		MetadatiDocumentoFirmatoDto datiFirma = new MetadatiDocumentoFirmatoDto();
		datiFirma.setCfFirmatario(cuaa);
		datiFirma.setDataFirma(settembre17_2020);
		Mockito.when(verificaFirmaClient.verificaFirma(Mockito.any(), Mockito.anyString())).thenReturn(datiFirma);

		List<String> entiUtenteConnesso = new ArrayList<>();
		entiUtenteConnesso.add("104");
		entiUtenteConnesso.add("94");
		entiUtenteConnesso.add("93");
		Mockito.when(anagraficaUtenteClient.getEntiUtenteConnesso()).thenReturn(entiUtenteConnesso);
		// chiamata al servizio
		Exception exception = Assertions.assertThrows(NestedServletException.class, () -> {
			this.mockMvc.perform(request).andExpect(status().isBadRequest());
		});
		assertTrue(exception.getMessage().contains(String.format("CAA_REVOCANTE_UGUALE_REVOCATO")));
	}

	@Test
	@Sql(scripts = "/sql/fascicolo/mandato/revocaMandatoTest_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/mandato/revocaMandatoTest_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void revocaOrdinariaVerificaFirmaError() throws Exception {
		/**
		 * CUAA: LRCPLA50M11H612B - ID: 50
		 * CAA Attuale: CAA COOPTN - LAVIS 
		 * CAA Destinazione: CAA ATS - 005 - ROVERETO - ID: 57
		 */
		Long identificativoSportello = 57L;
		String cuaa = "LRCPLA50M11H612B";
		Long mandatoId = 1857L;

		MockHttpServletRequestBuilder request = buildRequest(identificativoSportello, cuaa, mandatoId);

		// MOCK
		LocalDate settembre17_2020 = LocalDate.of(2020, Month.SEPTEMBER, 17);
		Mockito.doReturn(settembre17_2020).when(clock).today();
		MetadatiDocumentoFirmatoDto datiFirma = new MetadatiDocumentoFirmatoDto();
		datiFirma.setCfFirmatario(cuaa);
		datiFirma.setDataFirma(settembre17_2020);
		Mockito.when(verificaFirmaClient.verificaFirma(Mockito.any(), Mockito.anyString())).thenThrow(new RuntimeException("Il servizio di verifica firma non è disponibile"));

		List<String> entiUtenteConnesso = new ArrayList<>();
		entiUtenteConnesso.add("56");
		entiUtenteConnesso.add("57");
		Mockito.when(anagraficaUtenteClient.getEntiUtenteConnesso()).thenReturn(entiUtenteConnesso);
		// chiamata al servizio
		this.mockMvc.perform(request).andExpect(status().is4xxClientError());
	}

	private MockHttpServletRequestBuilder buildRequest(Long identificativoSportello, String cuaa, Long mandatoId) throws IOException {
		String endpoint = String.format("/%s/revoca-mandato", cuaa);
		Path path = Paths.get("src/test/resources/fascicolo/MANDATO_ftoDPDNDR77B03L378L.pdf");
		MockMultipartFile contratto = new MockMultipartFile("contratto.pdf", Files.readAllBytes(path));
		List<MultipartFile> allegati = Arrays.asList(new MockMultipartFile("allegato_0.pdf", Files.readAllBytes(path)), new MockMultipartFile("allegato_1.pdf", Files.readAllBytes(path)));

		MockHttpServletRequestBuilder builder =
				MockMvcRequestBuilders.multipart(ApiUrls.MANDATO.concat(endpoint))
				.file("contratto", contratto.getBytes())
				.file("allegati", allegati.get(0).getBytes())
				.file("allegati", allegati.get(1).getBytes())
				.param("codiceFiscaleRappresentante", cuaa)
				.param("identificativoSportello", String.valueOf(identificativoSportello))
				.contentType(MediaType.APPLICATION_JSON);
		return builder;
	}
}
