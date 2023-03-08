package it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import it.tndigitale.a4g.fascicolo.anagrafica.business.service.CaaService;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaUtenteClient;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.ApriFascicoloDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.MandatoDto;
import it.tndigitale.a4g.framework.client.custom.DocumentDto;
import it.tndigitale.a4g.framework.time.Clock;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente")
class OperazioniGenericheMandatoTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private MandatoService mandatoService;
	@MockBean
	private Clock clock;
	@MockBean
	private CaaService caaService;
	@MockBean
	private AnagraficaUtenteClient anagraficaUtenteClient;
	
	@Autowired
	private ProtocollazioneMandatoService protocollazioneMandatoService;

	
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

	@Test
	@Sql(scripts = "/sql/fascicolo/mandato/operazioniGenericheMandatoTest_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/mandato/operazioniGenericheMandatoTest_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void elencoMandatiNotEmpty() throws Exception {
		// MOCK
		LocalDate settembre22_2020 = LocalDate.of(2020, Month.SEPTEMBER, 22);
		Mockito.doReturn(settembre22_2020).when(clock).today();	
			
		List<MandatoDto> mandati = mandatoService.getMandati("LRCPLA50M11H612B", 0);
		
		assertNotNull(mandati);
		assertEquals(1, mandati.size());
	}
	
	@Test
	@Sql(scripts = "/sql/fascicolo/mandato/operazioniGenericheMandatoTest_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/mandato/operazioniGenericheMandatoTest_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void elencoMandatiSenzaDetenzione() throws Exception {
		// MOCK
		LocalDate settembre22_2020 = LocalDate.of(2020, Month.SEPTEMBER, 22);
		Mockito.doReturn(settembre22_2020).when(clock).today();	
		
		Exception exception = assertThrows(ResponseStatusException.class, () -> {
			mandatoService.getMandati("00123890220", 0);
		});
		
		assertTrue(exception.getMessage().contains(String.format(RevocaImmediataMandatoNotification.MANDATO_MANCANTE.name())));
	}
	
	@Test
	@Sql(scripts = "/sql/fascicolo/mandato/operazioniGenericheMandatoTest_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/mandato/operazioniGenericheMandatoTest_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void getDocumentoDtoPerPersonaFisicaService() throws Exception {
		List<ByteArrayResource> mockAllegati = Arrays.asList(createMockMultipartFile(), createMockMultipartFile());
		ByteArrayResource mockMandato = createMockMultipartFile();
		Long IDENTIFICATIVO_SPORTELLO = 93L;
		final String CODICE_FISCALE = "LRCPLA50M11H612B";
		ApriFascicoloDto datiDiApertura = new ApriFascicoloDto()
				.setAllegati(mockAllegati)
				.setContratto(mockMandato)
				.setCodiceFiscale(CODICE_FISCALE)
				.setCodiceFiscaleRappresentante(CODICE_FISCALE)
				.setIdentificativoSportello(IDENTIFICATIVO_SPORTELLO);
		

		DocumentDto docDto = protocollazioneMandatoService.getDocumentDto(datiDiApertura, 0);
		assertEquals("cognome", docDto.getMetadati().getMittente().getSurname());
		assertEquals("nome", docDto.getMetadati().getMittente().getName());
	}
	
	@Test
	@Sql(scripts = "/sql/fascicolo/mandato/operazioniGenericheMandatoTest_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/mandato/operazioniGenericheMandatoTest_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void getDocumentoDtoPerPersonaGiuridicaService() throws Exception {
		List<ByteArrayResource> mockAllegati = Arrays.asList(createMockMultipartFile(), createMockMultipartFile());
		ByteArrayResource mockMandato = createMockMultipartFile();
		Long IDENTIFICATIVO_SPORTELLO = 93L;
		final String CODICE_FISCALE = "00123890220";
		ApriFascicoloDto datiDiApertura = new ApriFascicoloDto()
				.setAllegati(mockAllegati)
				.setContratto(mockMandato)
				.setCodiceFiscale(CODICE_FISCALE)
				.setCodiceFiscaleRappresentante(CODICE_FISCALE)
				.setIdentificativoSportello(IDENTIFICATIVO_SPORTELLO);
		

		DocumentDto docDto = protocollazioneMandatoService.getDocumentDto(datiDiApertura, 0);
		assertEquals("LUNELLI MATTEO BRUNO", docDto.getMetadati().getMittente().getSurname());
		assertEquals("LUNELLI MATTEO BRUNO", docDto.getMetadati().getMittente().getName());
	}
}
