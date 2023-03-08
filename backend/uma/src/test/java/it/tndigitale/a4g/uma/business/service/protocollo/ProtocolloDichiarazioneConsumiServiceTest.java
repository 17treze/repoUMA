package it.tndigitale.a4g.uma.business.service.protocollo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;

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

import it.tndigitale.a4g.framework.client.custom.DocumentDto;
import it.tndigitale.a4g.framework.client.custom.MetadatiDto;
import it.tndigitale.a4g.framework.client.custom.MetadatiDto.TipologiaDocumentoPrincipale;
import it.tndigitale.a4g.framework.client.custom.ProtocolloClient;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.uma.GeneralFactory;
import it.tndigitale.a4g.uma.business.persistence.entity.DichiarazioneConsumiModel;
import it.tndigitale.a4g.uma.business.persistence.repository.DichiarazioneConsumiDao;
import it.tndigitale.a4g.uma.dto.protocollo.ProtocollaDocumentoUmaDto;
import it.tndigitale.a4g.uma.dto.protocollo.TipoDocumentoUma;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente")
class ProtocolloDichiarazioneConsumiServiceTest {

	private static final String SUFFISSO_NOME_FILE_DICHIARAZIONE_CONSUMI = "_dichiarazioneconsumi";
	private static final String PREFISSO_OGGETTO_DICHIARAZIONE_CONSUMI = "A4G - DICHIARAZIONE CONSUMI UMA - ";

	@Autowired
	private DichiarazioneConsumiDao dichiarazioneConsumiDao;
	@Autowired
	private GeneralFactory protocollazioneFactory;

	@MockBean
	private ProtocolloClient clientProtocollo;
	@MockBean
	private Clock clock;

	@Test
	void buildDocumentDtoPersonaFisicaDichiarazioneConsumi() throws IOException {

		ProtocollazioneStrategy protocollazioneStrategy = protocollazioneFactory.getProtocollazioneStrategy("PROTOCOLLA_" + TipoDocumentoUma.DICHIARAZIONE_CONSUMI.name());

		ProtocollaDocumentoUmaDto dto = prepareProtocollaDocumentoUmaDto("ASDFAX66E31F187R", 9173L);
		DocumentDto documentDto = protocollazioneStrategy.buildDocumentDto(dto);
		MetadatiDto metadatiDto = documentDto.getMetadati();

		assertEquals("ASDFAX66E31F187R", metadatiDto.getMittente().getNationalIdentificationNumber());
		assertEquals("Nome", metadatiDto.getMittente().getName());
		assertEquals("Cognome", metadatiDto.getMittente().getSurname());
		assertEquals(dto.getCuaa() + " - " + dto.getDescrizioneImpresa(), metadatiDto.getMittente().getDescription());
		assertEquals("pec@gmail.com", metadatiDto.getMittente().getEmail());
		assertEquals(String.format(PREFISSO_OGGETTO_DICHIARAZIONE_CONSUMI + "%s - %s - %s %s", dto.getAnno(), dto.getCuaa(), dto.getNome(), dto.getCognome()), metadatiDto.getOggetto());
		assertEquals(TipologiaDocumentoPrincipale.RICHIESTA_CARBURANTE, metadatiDto.getTipologiaDocumentoPrincipale());
		assertEquals(dto.getDocumento().getByteArray(), documentDto.getDocumentoPrincipale().getByteArray());
		assertEquals("9173" + SUFFISSO_NOME_FILE_DICHIARAZIONE_CONSUMI + ".pdf", documentDto.getDocumentoPrincipale().getFilename());

	}

	@Test
	void buildDocumentDtoPersonaGiuridicaDichiarazioneConsumi() throws Throwable {

		ProtocollazioneStrategy protocollazioneStrategy = protocollazioneFactory.getProtocollazioneStrategy("PROTOCOLLA_" + TipoDocumentoUma.DICHIARAZIONE_CONSUMI.name());

		ProtocollaDocumentoUmaDto dto = prepareProtocollaDocumentoUmaDto("00000000000", 8236L);
		DocumentDto documentDto = protocollazioneStrategy.buildDocumentDto(dto);
		MetadatiDto metadatiDto = documentDto.getMetadati();

		assertEquals("00000000000", metadatiDto.getMittente().getNationalIdentificationNumber());
		assertEquals("Nome", metadatiDto.getMittente().getName());
		assertEquals("Cognome", metadatiDto.getMittente().getSurname());
		assertEquals(dto.getCuaa() + " - " + dto.getDescrizioneImpresa(), metadatiDto.getMittente().getDescription());
		assertEquals("pec@gmail.com", metadatiDto.getMittente().getEmail());
		assertEquals(String.format(PREFISSO_OGGETTO_DICHIARAZIONE_CONSUMI + "%s - %s - %s", dto.getAnno(), dto.getCuaa(), dto.getDescrizioneImpresa()), metadatiDto.getOggetto());
		assertEquals(TipologiaDocumentoPrincipale.RICHIESTA_CARBURANTE, metadatiDto.getTipologiaDocumentoPrincipale());
		assertEquals(dto.getDocumento().getByteArray(), documentDto.getDocumentoPrincipale().getByteArray());
		assertEquals("8236" + SUFFISSO_NOME_FILE_DICHIARAZIONE_CONSUMI + ".pdf", documentDto.getDocumentoPrincipale().getFilename());
	}

	@Test
	@Sql(scripts = "/sql/consumi/consumi_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/consumi/consumi_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void aggiornaDichiarazioneConsumiConProtocollo() throws IOException {
		Mockito.when(clock.now()).thenReturn(LocalDateTime.of(2021, 06, 15, 00, 00));

		ProtocollaDocumentoUmaDto dto = prepareProtocollaDocumentoUmaDto("ASDFAX66E31F187R", 9173L);

		ProtocollazioneStrategy protocollazioneStrategy = protocollazioneFactory.getProtocollazioneStrategy("PROTOCOLLA_" + TipoDocumentoUma.DICHIARAZIONE_CONSUMI.name());
		protocollazioneStrategy.aggiornaDomanda(dto, "protocollo");

		Optional<DichiarazioneConsumiModel> dichiarazioneOpt = dichiarazioneConsumiDao.findById(9173L);

		if (!dichiarazioneOpt.isPresent()) {
			fail();
		}

		DichiarazioneConsumiModel dichiarazioneAggiornata = dichiarazioneOpt.get();
		assertEquals("protocollo", dichiarazioneAggiornata.getProtocollo());
		assertEquals(LocalDateTime.of(2021, 06, 15, 00, 00), dichiarazioneAggiornata.getDataProtocollazione());
	}

	private ProtocollaDocumentoUmaDto prepareProtocollaDocumentoUmaDto(String cuaa, Long id) throws IOException {
		ProtocollaDocumentoUmaDto dto = new ProtocollaDocumentoUmaDto();
		dto.setCuaa(cuaa);
		dto.setAnno(2021);
		dto.setId(id);
		dto.setNome("Nome");
		dto.setCognome("Cognome");
		dto.setDescrizioneImpresa("Descrizione");
		dto.setPec("pec@gmail.com");

		Path path = Paths.get("src/test/resources/documentiFirmati/MANDATO_ftoDPDNDR77B03L378L.pdf");
		byte[] documento = Files.readAllBytes(path);
		ByteArrayResource documentoByteAsResource = new ByteArrayResource(
				documento) {
			@Override
			public String getFilename() {
				return id.toString() + "_dichiarazioneconsumi.pdf";
			}
		};

		dto.setDocumento(documentoByteAsResource);

		return dto;
	}
}
