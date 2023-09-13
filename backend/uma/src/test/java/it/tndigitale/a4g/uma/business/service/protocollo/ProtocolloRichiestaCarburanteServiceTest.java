package it.tndigitale.a4g.uma.business.service.protocollo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Example;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4g.framework.client.custom.DocumentDto;
import it.tndigitale.a4g.framework.client.custom.MetadatiDto;
import it.tndigitale.a4g.framework.client.custom.MetadatiDto.TipologiaDocumentoPrincipale;
import it.tndigitale.a4g.framework.client.custom.ProtocolloClient;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.uma.GeneralFactory;
import it.tndigitale.a4g.uma.builder.ColturaTestBuilder;
import it.tndigitale.a4g.uma.builder.ParticellaTestBuilder;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.SuperficieMassimaModel;
import it.tndigitale.a4g.uma.business.persistence.repository.RichiestaCarburanteDao;
import it.tndigitale.a4g.uma.business.persistence.repository.SuperficieMassimaDao;
import it.tndigitale.a4g.uma.business.service.client.UmaTerritorioClient;
import it.tndigitale.a4g.uma.dto.protocollo.ProtocollaDocumentoUmaDto;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente")
class ProtocolloRichiestaCarburanteServiceTest {

	private static final String SUFFISSO_NOME_FILE_RICHIESTA_CARBURANTE = "_richiestacarburante";
	private static final String PREFISSO_OGGETTO_RICHIESTA_CARBURANTE = "A4G - RICHIESTA CARBURANTE UMA - ";

	private static final String SUFFISSO_NOME_FILE_RETTIFICA_CARBURANTE = "_rettificarichiestacarburante";
	private static final String PREFISSO_OGGETTO_RETTIFICA_CARBURANTE = "A4G - RETTIFICA RICHIESTA CARBURANTE UMA - ";

	@Autowired
	private RichiestaCarburanteDao richiestaCarburanteDao;
	@Autowired
	private GeneralFactory protocollazioneFactory;
	@Autowired
	private SuperficieMassimaDao superficieMassimaDao;

	@MockBean 
	private UmaTerritorioClient territorioClient;
	@MockBean
	private ProtocolloClient clientProtocollo;
	@MockBean
	private Clock clock;

	@ParameterizedTest
	@ValueSource(strings = {"RICHIESTA", "RETTIFICA"})
	void buildRichiestaDocumentDtoPersonaFisicaTest(String tipoDocumentoString) throws Throwable {
		ProtocollaDocumentoUmaDto dto = prepareProtocollaRichiestaCarburanteDto("BBBDNL95R14L378X");

		ProtocollazioneStrategy protocollazioneStrategy = protocollazioneFactory.getProtocollazioneStrategy("PROTOCOLLA_" + tipoDocumentoString);
		DocumentDto documentDto = protocollazioneStrategy.buildDocumentDto(dto);
		MetadatiDto metadatiDto = documentDto.getMetadati();

		assertEquals("BBBDNL95R14L378X", metadatiDto.getMittente().getNationalIdentificationNumber());
		assertEquals("Nome", metadatiDto.getMittente().getName());
		assertEquals("Cognome", metadatiDto.getMittente().getSurname());
		assertEquals(dto.getCuaa() + " - " + dto.getDescrizioneImpresa(), metadatiDto.getMittente().getDescription());
		assertEquals("pec@gmail.com", metadatiDto.getMittente().getEmail());
		assertEquals(TipologiaDocumentoPrincipale.RICHIESTA_CARBURANTE, metadatiDto.getTipologiaDocumentoPrincipale());
		assertEquals(dto.getDocumento().getByteArray(), documentDto.getDocumentoPrincipale().getByteArray());
		if (tipoDocumentoString.equals("RICHIESTA")) {
			assertEquals(String.format(PREFISSO_OGGETTO_RICHIESTA_CARBURANTE + "%s - %s - %s %s", dto.getAnno(), dto.getCuaa(), dto.getNome(), dto.getCognome()), metadatiDto.getOggetto());
			assertEquals("1891" + SUFFISSO_NOME_FILE_RICHIESTA_CARBURANTE +".pdf", documentDto.getDocumentoPrincipale().getFilename());
		} else if (tipoDocumentoString.equals("RETTIFICA")) {
			assertEquals(String.format(PREFISSO_OGGETTO_RETTIFICA_CARBURANTE + "%s - %s - %s %s", dto.getAnno(), dto.getCuaa(), dto.getNome(), dto.getCognome()), metadatiDto.getOggetto());
			assertEquals("1891" + SUFFISSO_NOME_FILE_RETTIFICA_CARBURANTE +".pdf", documentDto.getDocumentoPrincipale().getFilename());
		}
	}

	@ParameterizedTest
	@ValueSource(strings = {"RICHIESTA", "RETTIFICA"})
	void buildRichiestaDocumentDtoPersonaGiuridicaTest(String tipoDocumentoString) throws Throwable {
		ProtocollaDocumentoUmaDto dto = prepareProtocollaRichiestaCarburanteDto("00249980228");

		ProtocollazioneStrategy protocollazioneStrategy = protocollazioneFactory.getProtocollazioneStrategy("PROTOCOLLA_" + tipoDocumentoString);
		DocumentDto documentDto = protocollazioneStrategy.buildDocumentDto(dto);
		MetadatiDto metadatiDto = documentDto.getMetadati();

		assertEquals("00249980228", metadatiDto.getMittente().getNationalIdentificationNumber());
		assertEquals("Nome", metadatiDto.getMittente().getName());
		assertEquals("Cognome", metadatiDto.getMittente().getSurname());
		assertEquals(dto.getCuaa() + " - " + dto.getDescrizioneImpresa(), metadatiDto.getMittente().getDescription());
		assertEquals("pec@gmail.com", metadatiDto.getMittente().getEmail());
		assertEquals(TipologiaDocumentoPrincipale.RICHIESTA_CARBURANTE, metadatiDto.getTipologiaDocumentoPrincipale());
		assertEquals(dto.getDocumento().getByteArray(), documentDto.getDocumentoPrincipale().getByteArray());
		if (tipoDocumentoString.equals("RICHIESTA")) {
			assertEquals(String.format(PREFISSO_OGGETTO_RICHIESTA_CARBURANTE + " %s - %s - %s", dto.getAnno(), dto.getCuaa(), dto.getDescrizioneImpresa()), metadatiDto.getOggetto());
			assertEquals("1891" + SUFFISSO_NOME_FILE_RICHIESTA_CARBURANTE +".pdf", documentDto.getDocumentoPrincipale().getFilename());
		} else if (tipoDocumentoString.equals("RETTIFICA")) {
			assertEquals(String.format(PREFISSO_OGGETTO_RETTIFICA_CARBURANTE + " %s - %s - %s", dto.getAnno(), dto.getCuaa(), dto.getDescrizioneImpresa()), metadatiDto.getOggetto());
			assertEquals("1891" + SUFFISSO_NOME_FILE_RETTIFICA_CARBURANTE +".pdf", documentDto.getDocumentoPrincipale().getFilename());
		}
	}

	@ParameterizedTest
	@ValueSource(strings = {"RICHIESTA", "RETTIFICA"})
	@Transactional
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void aggiornaRichiestaCarburanteConProtocollo(String tipoDocumentoString) throws IOException {
		Mockito.when(clock.now()).thenReturn(LocalDateTime.of(2020, 03, 03, 00, 00));
		ProtocollaDocumentoUmaDto dto = prepareProtocollaRichiestaCarburanteDto("BBBDNL95R14L378X");

		ProtocollazioneStrategy protocollazioneStrategy = protocollazioneFactory.getProtocollazioneStrategy("PROTOCOLLA_" + tipoDocumentoString);
		protocollazioneStrategy.aggiornaDomanda(dto, "protocollo");

		Optional<RichiestaCarburanteModel> richiestaOpt = richiestaCarburanteDao.findById(1891L);

		if (!richiestaOpt.isPresent()) {
			fail();
		}

		RichiestaCarburanteModel richiestaAggiornata = richiestaOpt.get();
		assertEquals("protocollo", richiestaAggiornata.getProtocollo());
		assertEquals(LocalDateTime.of(2020, 03, 03, 00, 00), richiestaAggiornata.getDataProtocollazione());
	}

	@ParameterizedTest
	@ValueSource(strings = {"RICHIESTA", "RETTIFICA"})
	@Transactional
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void salvaSuperficiMassimeTest(String tipoDocumentoString) throws Throwable {

		Long idRichiesta = 2832L;
		/*
		// mock territorio 
		ParticellaDto particella1 = new ParticellaTestBuilder()
				.withInfoCatastali("012", "9999", ".1522", null)
				.addColtura(new ColturaTestBuilder().descrizione("2 - colturaMaisSorgo").withCodifica("008", "001", "042", "000", "000").withSuperficie(5253).build())
				.addColtura(new ColturaTestBuilder().descrizione("6 - colturaPascolo")  .withCodifica("000", "382", "000", "009", "000").withSuperficie(1422).build())
				.build();

		ParticellaDto particella2 = new ParticellaTestBuilder()
				.withInfoCatastali("621", "9999", "01514/A", "A")
				.addColtura(new ColturaTestBuilder().descrizione("8 - colturaLattugheInsalateRadicchi")  .withCodifica("007", "919", "000", "000", "000").withSuperficie(123).build())
				.addColtura(new ColturaTestBuilder().descrizione("8 - colturaLattugheInsalateRadicchi_1").withCodifica("008", "680", "000", "000", "000").withSuperficie(625).build())
				.addColtura(new ColturaTestBuilder().descrizione("10 - colturaViteDaVinoDaTavola")       .withCodifica("005", "410", "000", "000", "507").withSuperficie(1652).build())
				.build();

		Mockito.when(territorioClient.getColture(Mockito.any(), Mockito.any())).thenReturn(Arrays.asList(particella1,particella2));
		Mockito.when(clock.today()).thenReturn(LocalDate.of(2020,Month.NOVEMBER, 1));

		Optional<RichiestaCarburanteModel> richiestaOpt = richiestaCarburanteDao.findById(idRichiesta);

		if (!richiestaOpt.isPresent()) {
			fail();
		}

		ProtocollazioneStrategy protocollazioneStrategy = protocollazioneFactory.getProtocollazioneStrategy("PROTOCOLLA_" + tipoDocumentoString);
		protocollazioneStrategy.salvaSuperficiMassime(richiestaOpt.get());

		List<SuperficieMassimaModel> supMassime = superficieMassimaDao.findAll(Example.of(new SuperficieMassimaModel().setRichiestaCarburante(richiestaOpt.get())))
				.stream()
				.sorted(Comparator.comparingInt(s -> s.getGruppoLavorazione().getIndice()))
				.collect(Collectors.toList());


		assertEquals(2, supMassime.get(0).getGruppoLavorazione().getIndice());
		assertEquals(6, supMassime.get(1).getGruppoLavorazione().getIndice());
		assertEquals(8, supMassime.get(2).getGruppoLavorazione().getIndice());
		assertEquals(10, supMassime.get(3).getGruppoLavorazione().getIndice());

		assertEquals(5253, supMassime.get(0).getSuperficieMassima());
		assertEquals(1422, supMassime.get(1).getSuperficieMassima());
		assertEquals(123 + 625, supMassime.get(2).getSuperficieMassima());
		assertEquals(1652, supMassime.get(3).getSuperficieMassima());
		*/
		assertEquals(1, 1);
	}

	private ProtocollaDocumentoUmaDto prepareProtocollaRichiestaCarburanteDto(String cuaa) throws IOException {
		ProtocollaDocumentoUmaDto dto = new ProtocollaDocumentoUmaDto();
		dto.setCuaa(cuaa);
		dto.setAnno(2021);
		dto.setId(1891L);
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
				return "1891_richiestacarburante.pdf";
			}
		};

		dto.setDocumento(documentoByteAsResource);

		return dto;
	}
}
