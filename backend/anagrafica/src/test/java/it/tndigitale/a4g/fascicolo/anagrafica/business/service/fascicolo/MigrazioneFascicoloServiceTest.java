package it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.fascicolo.anagrafica.api.ApiUrls;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.CentroAssistenzaAgricolaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.SportelloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.FascicoloDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.SportelloDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaProxyClient;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaUtenteClient;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.legacy.FascicoloAgsService;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.DetenzioneAgsDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.FascicoloAgsDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.StatoFascicoloLegacy;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.TipoDetenzioneAgs;
import it.tndigitale.a4g.framework.client.custom.MetadatiDocumentoFirmatoDto;
import it.tndigitale.a4g.framework.client.custom.VerificaFirmaClient;
import it.tndigitale.a4g.proxy.client.model.PersonaFisicaDto;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente", roles = {"a4gfascicolo.fascicolo.ricerca.tutti"})
class MigrazioneFascicoloServiceTest {
	
	@Autowired MockMvc mockMvc;
	@MockBean RestTemplate restTemplate;
	@MockBean AnagraficaUtenteClient anagraficaUtenteClient;
	@MockBean AnagraficaProxyClient anagraficaProxyClient;
	@Autowired FascicoloDao fascicoloDao;
	@Autowired ObjectMapper objectMapper;
	@Autowired PlatformTransactionManager transactionManager;
	@MockBean FascicoloAgsService fascicoloAgsService;
	@MockBean SportelloDao sportelloDao;
	@MockBean VerificaFirmaClient verificaFirmaClient;

    private TransactionTemplate transactionTemplate;

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
	
	private String getResponseAnagraficaImpresa(String cf) throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/fascicolo/anagraficaimpresa/".concat(cf).concat(".json")));
		return objectMapper.writeValueAsString(response);
	}
	
	private String getResponseAnagrafeTributaria(String cf) throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/fascicolo/anagrafetributaria/".concat(cf).concat(".json")));
		return objectMapper.writeValueAsString(response);
	}
	
    private void setupMockitoMigrazioneFascicolo(final LocalDate dataFirma, final String codiceFiscale) throws Exception {

    	PersonaFisicaDto personaDto =
				objectMapper.readValue(getResponseAnagrafeTributaria(codiceFiscale), PersonaFisicaDto.class);
		PersonaFisicaDto personaGiuridicaDtoParix =
				objectMapper.readValue(getResponseAnagraficaImpresa(codiceFiscale), PersonaFisicaDto.class);
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagrafeTributaria(codiceFiscale)).thenReturn(personaDto);
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagraficaImpresa(
				Mockito.eq(codiceFiscale), Mockito.anyString())).thenReturn(personaGiuridicaDtoParix);

		MetadatiDocumentoFirmatoDto infoVerificaFirma = new MetadatiDocumentoFirmatoDto();
		infoVerificaFirma.setCfFirmatario(codiceFiscale);
		infoVerificaFirma.setDataFirma(dataFirma);
		Mockito.when(verificaFirmaClient.verificaFirma(Mockito.any(), Mockito.anyString())).thenReturn(infoVerificaFirma);
	}
    
    private FascicoloAgsDto getFascicolodaMigrare() {
    	FascicoloAgsDto fascicolodaMigrare = new FascicoloAgsDto();
		List<DetenzioneAgsDto> detenzioni = new ArrayList<DetenzioneAgsDto>();
		DetenzioneAgsDto detenzione = new DetenzioneAgsDto();
		detenzione.setCaa("CAA CIA SRL");
		detenzione.setDataFine(LocalDateTime.now());
		detenzione.setDataInizio(LocalDateTime.now());
		detenzione.setIdentificativoSportello(18L);
		detenzione.setSportello("CAA CIA - TRENTO - 001");
		detenzione.setTipoDetenzione(TipoDetenzioneAgs.MANDATO);
		detenzioni.add(detenzione);
		fascicolodaMigrare.setCuaa("FRRRLA56A19H607K");
		fascicolodaMigrare.setDataAggiornamento(LocalDateTime.now());
		fascicolodaMigrare.setDataCostituzione(LocalDateTime.now());
		fascicolodaMigrare.setDataValidazione(LocalDateTime.now());
		fascicolodaMigrare.setDenominazione("FERRARI AURELIO");
		fascicolodaMigrare.setDetenzioni(detenzioni);
		fascicolodaMigrare.setIdAgs(18L);
		fascicolodaMigrare.setIscrittoSezioneSpecialeAgricola(false);
		fascicolodaMigrare.setNonIscrittoSezioneSpecialeAgricola(false);
		fascicolodaMigrare.setOrganismoPagatore("APPAG");
		fascicolodaMigrare.setPec("AURELIO.FERRARI@CIA.LEGALMAIL.IT");
		fascicolodaMigrare.setStato(StatoFascicoloLegacy.VALIDO);
		return fascicolodaMigrare;
	}
    
    private Optional<SportelloModel> getSportello() {
    	Optional<SportelloModel> sportello = Optional.ofNullable(new SportelloModel());
		CentroAssistenzaAgricolaModel caa = new CentroAssistenzaAgricolaModel();
		caa.setCodiceFiscale("05804771003");
		caa.setDenominazione("CAA - CIA S.R.L.");
		caa.setFormaGiuridica("SOCIETA A RESPONSABILITA LIMITATA");
		caa.setId(696L);
		caa.setPartitaIVA("05804771003");
		caa.setVersion(0);
		sportello.get().setCentroAssistenzaAgricola(caa);
		sportello.get().setComune("TRENTO");
		sportello.get().setDenominazione("CAA CIA - TRENTO - 001");
		sportello.get().setId(712L);
		sportello.get().setIdentificativo(18L);
		sportello.get().setVersion(0);
		return sportello;
	}

	@BeforeEach
	void initialize() {
		transactionTemplate = new TransactionTemplate(transactionManager);
	}
    
	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/sportello_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void migraFascicolo_ok() throws Exception {
		String cuaa = "FRRRLA56A19H607K";
		
		List<String> entiUtenteConnesso = new ArrayList<String>();
		entiUtenteConnesso.add("18");
		Mockito.when(anagraficaUtenteClient.getEntiUtenteConnesso()).thenReturn(entiUtenteConnesso);
		Mockito.when(fascicoloAgsService.getFascicoloDaMigrare(Mockito.anyString())).thenReturn(getFascicolodaMigrare());
		Mockito.when(sportelloDao.findByIdentificativo(Mockito.anyLong())).thenReturn(getSportello());

		List<ByteArrayResource> mockAllegati = Arrays.asList(createMockMultipartFile(), createMockMultipartFile());
		ByteArrayResource mockMandato = createMockMultipartFile();
		setupMockitoMigrazioneFascicolo(LocalDate.now(), cuaa);
		String endpoint = String.format("/%s/migra", cuaa);

		MockHttpServletRequestBuilder requestBuilder =
				MockMvcRequestBuilders.multipart(ApiUrls.FASCICOLO_PRIVATE.concat(endpoint))
				.file("contratto", mockMandato.getByteArray())
				.file("allegati", mockAllegati.get(0).getByteArray())
				.file("allegati", mockAllegati.get(1).getByteArray())
				.param("codiceFiscaleRappresentante", cuaa)
				.param("identificativoSportello", Long.toString(18L))
				.param("migraModoPagamento", "true")
				.param("migraMacchinari", "true")
				.contentType(MediaType.APPLICATION_JSON);
		
		requestBuilder.with(request -> {
            request.setMethod(HttpMethod.POST.name());
            return request;
        });
		
		this.mockMvc.perform(requestBuilder);
		
		FascicoloModel fascicoloSaved = transactionTemplate.execute(status -> {
			Optional<FascicoloModel> resultOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0);
			assertEquals(true, resultOpt.isPresent());
			assertEquals(false, resultOpt.get().getDetenzioni().isEmpty());
			return resultOpt.get();
		});
		
		assertEquals("ferrari aurelio", fascicoloSaved.getDenominazione());
	}
	
}
