package it.tndigitale.a4g.fascicolo.anagrafica.api;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.MandatoModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.MovimentazioneModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaGiuridicaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.TipoMovimentazioneFascicolo;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.AttivitaAtecoDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.CaricaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.DestinazioneUsoDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.DocumentoIdentitaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.FascicoloDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.FascicoloOrganizzazioneDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.MandatoDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.ModoPagamentoDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.MovimentazioneDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.OrganizzazioneDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.PersonaGiuridicaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.UnitaTecnicoEconomicheDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.legacy.FascicoloAgsDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.PersonaConCaricaService;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaProxyClient;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaUtenteClient;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.MediatorFascicoloPrivateClient;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.StampaComponent;
// import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.TerritorioFascicoloPrivateClient;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.ZootecniaPrivateClient;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.ApriFascicoloDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.EredeDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.EsitoControlloDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.FascicoloDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.FascicoloFilter;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.MandatoDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.ModoPagamentoDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.DatiAperturaFascicoloDto;
import it.tndigitale.a4g.fascicolo.anagrafica.ioitalia.IoItaliaConsumerApi;
import it.tndigitale.a4g.framework.client.custom.MetadatiDocumentoFirmatoDto;
import it.tndigitale.a4g.framework.client.custom.VerificaFirmaClient;
import it.tndigitale.a4g.framework.event.store.EventStoreService;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.StatoFascicoloEnum;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.proxy.client.model.AnagraficaDto;
import it.tndigitale.a4g.proxy.client.model.AnagraficaDto.SessoEnum;
import it.tndigitale.a4g.proxy.client.model.FascicoloSian;
import it.tndigitale.a4g.proxy.client.model.ImpresaIndividualeDto;
import it.tndigitale.a4g.proxy.client.model.IndirizzoDto;
import it.tndigitale.a4g.proxy.client.model.InfoVerificaFirma;
import it.tndigitale.a4g.proxy.client.model.PagoPaIbanDettaglioDto;
import it.tndigitale.a4g.proxy.client.model.PersonaFisicaDto;
import it.tndigitale.a4g.proxy.client.model.PersonaGiuridicaDto;
import it.tndigitale.a4g.proxy.client.model.RappresentanteLegaleDto;
import it.tndigitale.a4g.proxy.client.model.SedeDto;
import it.tndigitale.a4g.utente.client.model.Persona;
import it.tndigitale.a4g.utente.client.model.RappresentaIlModelloDelProfiloDiUnUtente;
import it.tndigitale.a4g.utente.client.model.RappresentaIlModelloPerRappresentareUnUtenteDelSistema;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class FascicoloControllerTest {
	@MockBean JavaMailSender emailSender;
	@MockBean AnagraficaUtenteClient anagraficaUtenteClient;
	@MockBean AnagraficaProxyClient anagraficaProxyClient;
	@MockBean Clock clock;
	@MockBean FascicoloAgsDao soggettoService;
	@MockBean IoItaliaConsumerApi ioItaliaConsumerApi;
	@MockBean ZootecniaPrivateClient zootecniaPrivateClient;
	//	@MockBean TerritorioFascicoloPrivateClient territorioFascicoloPrivateClient;
	@MockBean MediatorFascicoloPrivateClient mediatorFascicoloPrivateClient;
	@MockBean StampaComponent stampaComponent;
	@MockBean VerificaFirmaClient verificaFirmaClient;
	@MockBean PersonaConCaricaService personaConCaricaService;

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
	@Autowired DocumentoIdentitaDao documentoIdentitaDao;
	@Autowired private MovimentazioneDao movimentazioneDao;

	private TransactionTemplate transactionTemplate;
	private final long ID_ENTE_CONNESSO = 11L;
	private LocalDate testDayLocalDate = LocalDate.of(2020, Month.JANUARY, 1);
	private LocalDateTime testDayLocalDateTime = LocalDateTime.of(2020, Month.FEBRUARY, 12, 1, 1);
	static final String PROV_TRENTO = "TN";

	private String getResponseAnagraficaImpresa(String cf) throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/fascicolo/anagraficaimpresa/".concat(cf).concat(".json")));
		return objectMapper.writeValueAsString(response);
	}

	private String getResponseAnagrafeTributaria(String cf) throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/fascicolo/anagrafetributaria/".concat(cf).concat(".json")));
		return objectMapper.writeValueAsString(response);
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

	private void setupMockitoVerificaIbanProxy(String iban) {
		PagoPaIbanDettaglioDto modoPagamento = new PagoPaIbanDettaglioDto();
		modoPagamento.setIban(iban);
		modoPagamento.setDenominazioneIstituto("Banca di Arkham");
		modoPagamento.setDenominazioneFiliale("Filiale dell'istituto di credito Arkham");
		modoPagamento.setCittaFiliale("Citta' di Arkham");
		//		pagoPaIbanDettaglioDto = anagraficaProxyClient.verificaIbanPersonaFisica(cuaa, modoPagamento.getIban());
		Mockito.when(anagraficaProxyClient.verificaIbanPersonaFisica(Mockito.anyString(), Mockito.anyString())).thenReturn(modoPagamento);
	}

	private void setupMockitoVerificaIbanProxyThrowError(String iban) {
		HttpServerErrorException ex = new HttpServerErrorException(HttpStatus.BAD_REQUEST);

		Mockito.when(anagraficaProxyClient.verificaIbanPersonaFisica(Mockito.anyString(), Mockito.anyString())).thenThrow(ex);
	}


	private void setupMockitoAperturaFascicolo(final LocalDate dataFirma, final String codiceFiscale) throws Exception {
		if (codiceFiscale.length() != 16) {
			PersonaGiuridicaDto personaDto =
					objectMapper.readValue(getResponseAnagrafeTributaria(codiceFiscale), PersonaGiuridicaDto.class);
			PersonaGiuridicaDto personaGiuridicaDtoParix =
					objectMapper.readValue(getResponseAnagraficaImpresa(codiceFiscale), PersonaGiuridicaDto.class);
			Mockito.when(anagraficaProxyClient.getPersonaGiuridicaAnagrafeTributaria(codiceFiscale)).thenReturn(personaDto);
			Mockito.when(anagraficaProxyClient.getPersonaGiuridicaAnagraficaImpresa(
					Mockito.eq(codiceFiscale), Mockito.anyString())).thenReturn(personaGiuridicaDtoParix);    		
		} else {
			PersonaFisicaDto personaDto =
					objectMapper.readValue(getResponseAnagrafeTributaria(codiceFiscale), PersonaFisicaDto.class);
			PersonaFisicaDto personaGiuridicaDtoParix =
					objectMapper.readValue(getResponseAnagraficaImpresa(codiceFiscale), PersonaFisicaDto.class);
			Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagrafeTributaria(codiceFiscale)).thenReturn(personaDto);
			Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagraficaImpresa(
					Mockito.eq(codiceFiscale), Mockito.anyString())).thenReturn(personaGiuridicaDtoParix);
		}
		MetadatiDocumentoFirmatoDto infoVerificaFirma = new MetadatiDocumentoFirmatoDto();
		infoVerificaFirma.setCfFirmatario(codiceFiscale);
		infoVerificaFirma.setDataFirma(dataFirma);
		Mockito.when(verificaFirmaClient.verificaFirma(Mockito.any(), Mockito.anyString())).thenReturn(infoVerificaFirma);

		/*Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagrafeTributaria("LNLMTB74A31F205S")).thenReturn(
				objectMapper.readValue(getResponseAnagrafeTributaria("LNLMTB74A31F205S"), PersonaFisicaDto.class));
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagrafeTributaria("LNLMCL67R20L378W")).thenReturn(
				objectMapper.readValue(getResponseAnagrafeTributaria("LNLMCL67R20L378W"), PersonaFisicaDto.class));*/		
	}

	private void mockUserSetupPersonaFisicaATSenzaImpresaIndividuale() {
		PersonaFisicaDto personaFisicaAT = new PersonaFisicaDto();
		personaFisicaAT.setCodiceFiscale("ZPDNDR77B03L378W");
		personaFisicaAT.setDeceduta(false);
		AnagraficaDto anagrafica = new AnagraficaDto();
		anagrafica.setCognome("Rossi");
		anagrafica.setNome("Mario");
		anagrafica.setProvinciaNascita(PROV_TRENTO);
		anagrafica.setComuneNascita("Rovereto");
		anagrafica.setDataNascita(LocalDate.of(1970, Month.JANUARY, 1));
		anagrafica.setSesso(SessoEnum.MASCHIO);
		personaFisicaAT.setAnagrafica(anagrafica);

		IndirizzoDto indirizzoDto = new IndirizzoDto();
		indirizzoDto.setProvincia(PROV_TRENTO);

		personaFisicaAT.setDomicilioFiscale(indirizzoDto);

		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagrafeTributaria(Mockito.anyString())).thenReturn(personaFisicaAT);
	}

	private void mockRappresentanteLegale(String codiceFiscale, String nome, String cognome) {
		List<Persona> persone = new ArrayList<>();
		Persona p = new Persona();
		p.setCodiceFiscale(codiceFiscale);
		p.setNome(nome);
		p.setCognome(cognome);
		persone.add(p);

		Mockito.when(anagraficaUtenteClient.ricercaPerCodiceFiscale(Mockito.anyString())).thenReturn(persone);
	}

	private void mockControlloFirmaProxy(String codiceFiscale) {
		InfoVerificaFirma retVal = new InfoVerificaFirma();
		retVal.setCfFirmatario(codiceFiscale);
		retVal.setDataFirma(clock.today());
		Mockito.when(anagraficaProxyClient.verificaFirmaSingola(Mockito.any(), Mockito.anyString())).thenReturn(retVal);
	}

	private void mockUserSetupPersonaFisicaATConImpresaIndividuale() {
		PersonaFisicaDto personaFisicaAT = new PersonaFisicaDto();
		personaFisicaAT.setCodiceFiscale("ZPDNDR77B03L378W");
		personaFisicaAT.setDeceduta(false);
		AnagraficaDto anagrafica = new AnagraficaDto();
		anagrafica.setCognome("Rossi");
		anagrafica.setNome("Mario");
		anagrafica.setProvinciaNascita(PROV_TRENTO);
		anagrafica.setComuneNascita("Rovereto");
		anagrafica.setDataNascita(LocalDate.of(1970, Month.JANUARY, 1));
		anagrafica.setSesso(SessoEnum.MASCHIO);
		personaFisicaAT.setAnagrafica(anagrafica);

		ImpresaIndividualeDto impresaIndividualeDto = new ImpresaIndividualeDto();
		impresaIndividualeDto.setDenominazione("FERRARI CESARE");
		impresaIndividualeDto.setFormaGiuridica("IMPRESA INDIVIDUALE");
		impresaIndividualeDto.setPartitaIva("01243160221");

		SedeDto sedeDto = new SedeDto();
		IndirizzoDto indirizzoDtoSedeLegale = new IndirizzoDto();
		indirizzoDtoSedeLegale.setProvincia(PROV_TRENTO);
		indirizzoDtoSedeLegale.setCap("38123");
		indirizzoDtoSedeLegale.setComune("Trento");
		indirizzoDtoSedeLegale.setToponimo("38123 Trento");

		sedeDto.setIndirizzo(indirizzoDtoSedeLegale);

		impresaIndividualeDto.setSedeLegale(sedeDto);

		personaFisicaAT.setImpresaIndividuale(impresaIndividualeDto);

		IndirizzoDto indirizzoDto = new IndirizzoDto();
		indirizzoDto.setProvincia(PROV_TRENTO);

		personaFisicaAT.setDomicilioFiscale(indirizzoDto);

		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagrafeTributaria(Mockito.anyString())).thenReturn(personaFisicaAT);
	}

	private void mockSianAppag(final String cuaa, boolean isAppag) {
		FascicoloSian mockSian = new FascicoloSian();
		mockSian.setCuaa(cuaa);
		mockSian.setOrganismoPagatore(isAppag ? FascicoloSian.OrganismoPagatoreEnum.APPAG : FascicoloSian.OrganismoPagatoreEnum.ALTRO);
		Mockito.when(anagraficaProxyClient.verificaEsistenzaFascicolo(Mockito.anyString())).thenReturn(mockSian);
	}

	private void mockUserSetupPersonaGiuridicaAT(String cuaa) {
		PersonaGiuridicaDto personaGiuridicaAT = new PersonaGiuridicaDto();

		personaGiuridicaAT.setCodiceFiscale(cuaa);
		personaGiuridicaAT.setPartitaIva("000000000");
		personaGiuridicaAT.setDenominazione("Societa' fittizia");

		RappresentanteLegaleDto rappresentanteLegaleDto = new RappresentanteLegaleDto();
		rappresentanteLegaleDto.setNominativo("Paolo Rossi Inc.");
		rappresentanteLegaleDto.setCodiceFiscale("ZPDNDR77B03L378W");

		personaGiuridicaAT.setRappresentanteLegale(rappresentanteLegaleDto);


		SedeDto sedeDto = new SedeDto();
		IndirizzoDto indirizzoDto = new IndirizzoDto();
		indirizzoDto.setProvincia(PROV_TRENTO);
		indirizzoDto.setCap("38068");
		indirizzoDto.setComune("Rovereto");
		indirizzoDto.setToponimo("38068 Rovereto");

		sedeDto.setIndirizzo(indirizzoDto);

		personaGiuridicaAT.setSedeLegale(sedeDto);

		Mockito.when(anagraficaProxyClient.getPersonaGiuridicaAnagrafeTributaria(Mockito.anyString())).thenReturn(personaGiuridicaAT);
	}

	private void userSetupEnte() {
		List<String> entiUtenteConnesso = new ArrayList<>();
		entiUtenteConnesso.add(String.valueOf(ID_ENTE_CONNESSO));
		Mockito.when(anagraficaUtenteClient.getEntiUtenteConnesso()).thenReturn(entiUtenteConnesso);
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

	private void userSetupAzienda(String userCodiceFiscale) {
		RappresentaIlModelloPerRappresentareUnUtenteDelSistema user = new RappresentaIlModelloPerRappresentareUnUtenteDelSistema();
		user.setIdentificativo(userCodiceFiscale);
		user.setCodiceFiscale(userCodiceFiscale);
		user.setNome(userCodiceFiscale);
		RappresentaIlModelloDelProfiloDiUnUtente profilo = new RappresentaIlModelloDelProfiloDiUnUtente();
		profilo.setIdentificativo("azienda");
		user.setProfili(Arrays.asList(profilo));
		Mockito.when(anagraficaUtenteClient.getUtenteConnesso()).thenReturn(user);
	}

	private void mockIoItaliaStuff() {
		Mockito.when(ioItaliaConsumerApi.inviaMessaggio(Mockito.any())).thenReturn(null);
	}

	private void mockStartValidazioneZootecniaOkStuff() {
		Mockito.when(zootecniaPrivateClient.startValidazioneFascicolo(
				Mockito.anyString(), Mockito.anyInt())).thenReturn(
						new ResponseEntity<>(HttpStatus.OK));
	}

	//	private void mockControlliCompletezzaOkStuff() {
	//	var res = new HashMap<String,Boolean>();
	//	res.put("CONTROLLI_OK", true);
	//	Mockito.when(mediatorFascicoloPrivateClient.getControlloCompletezzaUsingGET(
	//			Mockito.anyString())).thenReturn(res);
	//}

	private void mockStampaComponentOkStuff() throws RestClientException, IOException {
		byte[] pdf = {'1', '2', '3'};
		Mockito.when(stampaComponent.stampaPDFA(
				Mockito.anyString(), Mockito.anyString())).thenReturn(
						pdf
						);
	}

	private void mockWebClientFailStuff() {
		Mockito.when(zootecniaPrivateClient.startValidazioneFascicolo(
				Mockito.anyString(), Mockito.anyInt())).thenReturn(
						new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
	}

	private void setupMockitoVerificaIbanFakeProxy(String iban) {
		var pagoPaIbanDettaglioDto = new PagoPaIbanDettaglioDto();
		pagoPaIbanDettaglioDto.setCittaFiliale("Test - Città");
		pagoPaIbanDettaglioDto.setDenominazioneFiliale("Test - Denominazione filiale");
		pagoPaIbanDettaglioDto.setDenominazioneIstituto("Test - Denominazione istituto");
		pagoPaIbanDettaglioDto.setIban(iban);
		Mockito.when(anagraficaProxyClient.checkIbanFake(Mockito.anyString())).thenReturn(pagoPaIbanDettaglioDto);
	}

	@BeforeEach
	void initialize() {
		transactionTemplate = new TransactionTemplate(transactionManager);
		Mockito.when(clock.today()).thenReturn(testDayLocalDate);
		Mockito.when(clock.now()).thenReturn(testDayLocalDateTime);
	}

	//	@Test
	//	@WithMockUser(username = "utente", roles = {"a4gfascicolo.fascicolo.ricerca.tutti"})
	//	void verificaIBanValido() {
	////		mappa (string, boolean) dove string e' l'iban da verificare e boolean se e' valido o meno 
	//		Map<String, Boolean> map = Stream.of(new Object[][] {
	//			{ "GB82 WEST 1234 5698 7654 32", true },
	//			{ "GB82 TEST 1234 5698 7654 32", false },
	//			{ "GB81 WEST 1234 5698 7654 32", false },
	//			{ "SA03 8000 0000 6080 1016 7519", true },
	//			{ "CH93 0076 2011 6238 5295 7", true },
	//			{ "XX00 0000", false },
	//			{ "", false },
	//			{ "DE", false },
	//			{ "DE13 äöü_ 1234 1234 1234 12", false },
	//			}).collect(Collectors.toMap(data -> (String) data[0],  data -> (Boolean) data[1]));
	//		
	//		map.forEach((iban, isValid) -> {
	//			if(isValid) {
	//				assertTrue(fascicoloController.verificaCodiceIban(iban));
	//			}
	//		});
	//	}
	//	
	//	@Test
	//	@WithMockUser(username = "utente", roles = {"a4gfascicolo.fascicolo.ricerca.tutti"})
	//	void verificaIBanInvalido() {
	////		mappa (string, boolean) dove string e' l'iban da verificare e boolean se e' valido o meno 
	//		Map<String, Boolean> map = Stream.of(new Object[][] {
	//			{ "GB82 WEST 1234 5698 7654 32", true },
	//			{ "GB82 TEST 1234 5698 7654 32", false },
	//			{ "GB81 WEST 1234 5698 7654 32", false },
	//			{ "SA03 8000 0000 6080 1016 7519", true },
	//			{ "CH93 0076 2011 6238 5295 7", true },
	//			{ "XX00 0000", false },
	//			{ "", false },
	//			{ "DE", false },
	//			{ "DE13 äöü_ 1234 1234 1234 12", false },
	//			}).collect(Collectors.toMap(data -> (String) data[0],  data -> (Boolean) data[1]));
	//		
	//		map.forEach((iban, isValid) -> {
	//			if (!isValid) {
	//				assertFalse(fascicoloController.verificaCodiceIban(iban));
	//			}
	//		});
	//	}

	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	private void fascicoloControlliCompletezzaTest() {
		String cuaa = "00123890220";
		Map<ControlliFascicoloAnagraficaCompletoEnum,EsitoControlloDto> res = new HashMap<ControlliFascicoloAnagraficaCompletoEnum,EsitoControlloDto>();
		Map<ControlliFascicoloAnagraficaCompletoEnum,EsitoControlloDto> resAtt = new HashMap<ControlliFascicoloAnagraficaCompletoEnum,EsitoControlloDto>();
		EsitoControlloDto esitoControllo = new EsitoControlloDto();
		esitoControllo.setEsito(0);
		resAtt.put(ControlliFascicoloAnagraficaCompletoEnum.IS_DOCUMENTO_IDENTITA_PRESENTE, esitoControllo);
		res = validazioneFascicoloService.getControlloCompletezzaFascicolo(cuaa);
		assertEquals(resAtt.get(ControlliFascicoloAnagraficaCompletoEnum.IS_DOCUMENTO_IDENTITA_PRESENTE).getEsito(), 
				res.get(ControlliFascicoloAnagraficaCompletoEnum.IS_DOCUMENTO_IDENTITA_PRESENTE).getEsito());
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.aggiornamento.modalitapagamento.ente"})
	@Sql(scripts = "/sql/fascicolo/fascicolo_erede_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_erede_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void inserisciIBanEredeOK() throws Exception {
		String iban = "SA03 8000 0000 6080 1016 7519";
		String cuaa = "XPDNDR77B03L378R";
		userSetupEnte();
		setupMockitoVerificaIbanProxy(iban);
		setupMockitoVerificaIbanFakeProxy(iban);

		ModoPagamentoDto modoPagamento = new ModoPagamentoDto();
		modoPagamento.setIban(iban);

		ModoPagamentoDto result = fascicoloController.inserimentoModoPagamento(cuaa, modoPagamento);
		assertNotNull(result.getId());
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.aggiornamento.modalitapagamento.ente"})
	@Sql(scripts = "/sql/fascicolo/fascicolo_erede_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_erede_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void inserisciIBanErede_INCHIUSURA_NON_DECEDUTO_KO() throws Exception {
		String iban = "SA03 8000 0000 6080 1016 7519";
		String cuaa = "XPDNDR77B03L378Z";
		userSetupEnte();
		setupMockitoVerificaIbanProxy(iban);
		setupMockitoVerificaIbanFakeProxy(iban);

		ModoPagamentoDto modoPagamento = new ModoPagamentoDto();
		modoPagamento.setIban(iban);

		assertThrows(PersonaFisicaNonDecedutaException.class, () -> {
			ModoPagamentoDto result = fascicoloController.inserimentoModoPagamento(cuaa, modoPagamento);
		});
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.aggiornamento.modalitapagamento.ente"})
	@Sql(scripts = "/sql/fascicolo/fascicolo_erede_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_erede_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void inserisciIBanErede_Molti_Eredi_Firmatari_INCHIUSURA_KO() throws Exception {
		String iban = "SA03 8000 0000 6080 1016 7519";
		String cuaa = "ZPDNDR77B03L378R";
		userSetupEnte();
		setupMockitoVerificaIbanProxy(iban);
		setupMockitoVerificaIbanFakeProxy(iban);

		ModoPagamentoDto modoPagamento = new ModoPagamentoDto();
		modoPagamento.setIban(iban);

		Exception exception = assertThrows(EredeFascicoloException.class, () -> {
			ModoPagamentoDto result = fascicoloController.inserimentoModoPagamento(cuaa, modoPagamento);
		});

		assertEquals("Deve essere specificato uno ed un solo erede firmatario. Conteggio attuale=[2]", exception.getMessage());
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.aggiornamento.modalitapagamento.ente"})
	@Sql(scripts = "/sql/fascicolo/fascicolo_erede_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_erede_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void inserisciIBanErede_PERSONA_GIURIDICA_INCHIUSURA_KO() throws Exception {
		String iban = "SA03 8000 0000 6080 1016 7519";
		String cuaa = "00123890220";
		userSetupEnte();
		setupMockitoVerificaIbanProxy(iban);
		setupMockitoVerificaIbanFakeProxy(iban);

		ModoPagamentoDto modoPagamento = new ModoPagamentoDto();
		modoPagamento.setIban(iban);

		assertThrows(PersonaNonFisicaException.class, () -> {
			ModoPagamentoDto result = fascicoloController.inserimentoModoPagamento(cuaa, modoPagamento);
		});
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.aggiornamento.modalitapagamento.ente"})
	@Sql(scripts = "/sql/fascicolo/fascicolo_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void inserisciIBanOK() throws Exception {
		String iban = "SA03 8000 0000 6080 1016 7519";
		String cuaa = "XPDNDR77B03L378X";
		userSetupEnte();
		setupMockitoVerificaIbanProxy(iban);
		setupMockitoVerificaIbanFakeProxy(iban);

		ModoPagamentoDto modoPagamento = new ModoPagamentoDto();
		modoPagamento.setIban(iban);

		ModoPagamentoDto result = fascicoloController.inserimentoModoPagamento(cuaa, modoPagamento);
		assertNotNull(result.getId());
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.aggiornamento.modalitapagamento.ente"})
	@Sql(scripts = "/sql/fascicolo/fascicolo_controllato_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void inserisci_iban_fail_stato_controllato() throws Exception {
		String iban = "GB82 WEST 1234 5698 7654 32";
		String cuaa = "XPDNDR77B03L378X";
		userSetupEnte();
		setupMockitoVerificaIbanProxy(iban);
		setupMockitoVerificaIbanFakeProxy(iban);

		ModoPagamentoDto modoPagamento = new ModoPagamentoDto();
		modoPagamento.setIban(iban);

		assertThrows(FascicoloAllaFirmaAziendaException.class, () -> {
			fascicoloController.inserimentoModoPagamento(cuaa, modoPagamento);
		});
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4gfascicolo.fascicolo.ricerca.tutti"})
	@Sql(scripts = "/sql/fascicolo/fascicolo_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void inserisciIBanRuoloNonPermesso() throws Exception {
		userSetupEnte();

		String iban = "GB82 WEST 1234 5698 7654 32";
		String cuaa = "XPDNDR77B03L378X";
		userSetupAzienda(cuaa);
		setupMockitoVerificaIbanFakeProxy(iban);

		ModoPagamentoDto modoPagamento = new ModoPagamentoDto();
		modoPagamento.setIban(iban);

		assertThrows(AccessDeniedException.class, () -> {
			fascicoloController.inserimentoModoPagamento(cuaa, modoPagamento);
		});
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.aggiornamento.modalitapagamento.ente"})
	@Sql(scripts = "/sql/fascicolo/fascicolo_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void inserisciIBanNessunFascicoloConCuaa() throws Exception {
		userSetupEnte();

		String iban = "GB82 WEST 1234 5698 7654 32";
		String cuaa = "XPDNDR77B03L378Z";
		setupMockitoVerificaIbanFakeProxy(iban);

		ModoPagamentoDto modoPagamento = new ModoPagamentoDto();
		modoPagamento.setIban(iban);

		Exception exception = assertThrows(EntityNotFoundException.class, () -> {
			fascicoloController.inserimentoModoPagamento(cuaa, modoPagamento);
		});
		assertEquals("Fascicolo CUAA " + cuaa + " non trovato", exception.getMessage());
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4gfascicolo.fascicolo.ricerca.ente"})
	@Sql(scripts = "/sql/fascicolo/fascicolo_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void inserisciIBanSportelloNonAbilitatoAlMandato() throws Exception {
		userSetupEnte();

		String iban = "GB82 WEST 1234 5698 7654 32";
		userSetupAzienda(iban);
		setupMockitoVerificaIbanFakeProxy(iban);

		String cuaa = "XPDNDR77B03L378Y";
		ModoPagamentoDto modoPagamento = new ModoPagamentoDto();
		modoPagamento.setIban(iban);

		assertThrows(AccessDeniedException.class, () -> {
			fascicoloController.inserimentoModoPagamento(cuaa, modoPagamento);
		});
	}

	@Test
	@WithMockUser(username = "utente", roles = {
			"a4ganagrafica.fascicolo.aggiornamento.modalitapagamento.ente",
			"a4ganagrafica.fascicolo.visualizzazione.modalitapagamento.ente"
	})
	@Sql(scripts = "/sql/fascicolo/fascicolo_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void cancellaIBanOK() throws Exception {
		userSetupEnte();
		String cuaa = "XPDNDR77B03L378X";
		Long id = 8375L;

		List<ModoPagamentoDto> elencoModoPagamento = fascicoloController.elencoModoPagamento(cuaa, 0);
		boolean anyMatch = elencoModoPagamento.stream().anyMatch(mp -> mp.getId().equals(id));
		assertTrue(anyMatch);

		mockMvc.perform(delete(String.format("%s/%s/modo-pagamento/%s", ApiUrls.FASCICOLO, cuaa, id)))
		.andExpect(status().isOk());
		//		reimposta la security; per qualche motivo dopo mockMvc.perform() viene cancellato
		SecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
		elencoModoPagamento = fascicoloService.elencoModoPagamento(cuaa, 0);
		anyMatch = elencoModoPagamento.stream().anyMatch(mp -> mp.getId().equals(id));
		assertFalse(anyMatch);
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4gfascicolo.fascicolo.ricerca.tutti"})
	@Sql(scripts = "/sql/fascicolo/fascicolo_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void cancellaIBanRuoloNonPermesso() {
		userSetupEnte();
		String cuaa = "XPDNDR77B03L378X";
		userSetupAzienda(cuaa);
		Long id = 8375L;

		assertThrows(AccessDeniedException.class, () -> {
			fascicoloController.rimozioneModoPagamento(cuaa, id);
		});
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.aggiornamento.modalitapagamento.ente"})
	@Sql(scripts = "/sql/fascicolo/fascicolo_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void cancellaIBanIdModoPagamentoInesistente() {
		userSetupEnte();

		String cuaa = "XPDNDR77B03L378X";
		Long id = 1L;

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			fascicoloController.rimozioneModoPagamento(cuaa, id);
		});

		assertEquals("Modo di pagamento con id= [" + id +"] non presente nel db", exception.getMessage());
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.aggiornamento.modalitapagamento.ente"})
	@Sql(scripts = "/sql/fascicolo/fascicolo_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void cancellaIBanIdModoPagamentoNonAssociatoCuaa() {
		userSetupEnte();

		String cuaa = "XPDNDR77B03L378X";
		Long id = 9000L;

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			fascicoloController.rimozioneModoPagamento(cuaa, id);
		});

		assertEquals("Modo di pagamento con id= [" + id +"] non associato al cuaa=[" + cuaa + "]", exception.getMessage());
	}

	// TODO http://localhost:8080/anagrafica/api/v1/fascicolo?pagina=0&numeroElementiPagina=10&proprieta=id&ordine=ASC
	// fare un test che passi per ricerca con sportello
	@Test
	@WithMockUser(username = "utente", roles = {"a4gfascicolo.fascicolo.ricerca.ente"})
	@Sql(scripts = "/sql/fascicolo/fascicolo_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void ricerca_filtra_per_fascicoli_utente_connesso_ok() {
		userSetupEnte();

		FascicoloFilter filter = new FascicoloFilter();
		RisultatiPaginati<FascicoloDto> ricercaFascicolo = fascicoloController.ricercaFascicolo(filter, Paginazione.of(), null);

		assertEquals(3, ricercaFascicolo.getCount());
		assertEquals(ID_ENTE_CONNESSO, ((MandatoDto)ricercaFascicolo.getRisultati().get(0).getDetenzione()).getSportello().getId());
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/fascicolo_controllato_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void aggiornamento_fail_stato_controllato() {
		userSetupEnte();
		String cuaa = "XPDNDR77B03L378X";

		var pfDto = new PersonaFisicaDto();
		pfDto.setCodiceFiscale(cuaa);
		pfDto.setDeceduta(false);
		var ii = new ImpresaIndividualeDto();
		SedeDto sedeDto = new SedeDto();
		IndirizzoDto indirizzoDtoSedeLegale = new IndirizzoDto();
		indirizzoDtoSedeLegale.setProvincia(PROV_TRENTO);
		sedeDto.setIndirizzo(indirizzoDtoSedeLegale);
		ii.setSedeLegale(sedeDto);
		pfDto.setImpresaIndividuale(ii);

		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagrafeTributaria(Mockito.anyString())).thenReturn(pfDto);

		Exception exception = Assertions.assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(get(String.format("/api/v1/fascicolo/%s/aggiorna", cuaa))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
		});
		assertEquals(FascicoloAllaFirmaAziendaException.class.getName(), exception.getCause().getClass().getName());
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void apertura_fascicolo_persona_giuridica_OK() throws Exception {
		userSetupCaa(89L);
		String cuaa = "00123890220";
		List<ByteArrayResource> mockAllegati = Arrays.asList(createMockMultipartFile(), createMockMultipartFile());
		ByteArrayResource mockMandato = createMockMultipartFile();
		setupMockitoAperturaFascicolo(clock.today(), cuaa);

		String endpoint = String.format("/%s/apri", cuaa);

		MockHttpServletRequestBuilder requestBuilder =
				MockMvcRequestBuilders.multipart(ApiUrls.FASCICOLO.concat(endpoint))
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

		FascicoloModel fascicoloSaved = transactionTemplate.execute(status -> {
			List<MandatoModel> mandati = mandatoDao.findAll();
			MandatoModel mandatoModel = mandati.get(0);
			Optional<FascicoloModel> resultOpt = fascicoloDao.findByCuaaAndIdValidazione(mandatoModel.getFascicolo().getCuaa(), 0);
			if (!resultOpt.isPresent()) {
				return new FascicoloModel();
			}
			return resultOpt.get();
		});


		assertEquals("FERRARI F.LLI LUNELLI S.P.A.", fascicoloSaved.getDenominazione());
	}


	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/persona/aggiornamentoFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aggiornamentoFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void aggiornamento_fascicolo_persona_giuridica_OK() throws Exception {
		userSetupCaa(89L);
		String cuaa = "00123890220";
		setupMockitoAperturaFascicolo(clock.today(), cuaa);

		// verifico quanto ho salvato
		List<MandatoModel> mandati = mandatoDao.findAll();
		MandatoModel mandatoModel = mandati.get(0);
		FascicoloModel fascicolo = mandatoModel.getFascicolo();

		PersonaGiuridicaModel personaGiuridicaModel = (PersonaGiuridicaModel) fascicolo.getPersona();
		assertEquals("FERRARI F.LLI LUNELLI S.P.A.", personaGiuridicaModel.getDenominazione());


		mockMvc.perform(get(String.format("/api/v1/fascicolo/%s/aggiorna", cuaa))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());

		mandati = mandatoDao.findAll();
		mandatoModel = mandati.get(0);
		fascicolo = mandatoModel.getFascicolo();
		assertEquals("FERRARI F.LLI LUNELLI S.P.A.", fascicolo.getDenominazione());
	}

	@Test
	@WithMockUser(username = "utente", roles = {
			"a4gfascicolo.fascicolo.ricerca.ente",
			"a4ganagrafica.fascicolo.apertura.ente",
			"a4gfascicolo.fascicolo.ricerca.tutti"
	})
	@Sql(scripts = "/sql/fascicolo/fascicolo_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void imposta_stato_controllato_ok() throws Exception {
		userSetupEnte();
		mockIoItaliaStuff();

		String cuaa = "XPDNDR77B03L378X";

		FascicoloDto fascicolo = fascicoloController.getByCuaa(cuaa, 0);
		assertEquals(StatoFascicoloEnum.IN_AGGIORNAMENTO, fascicolo.getStato());

		mockMvc.perform(
				put(String.format("/api/v1/fascicolo/%s/stato/alla-firma-azienda", cuaa))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());

		// verifica stato aggiornato
		fascicolo = ricercaFascicoloService.getFascicoloDto(cuaa, 0);
		assertEquals(StatoFascicoloEnum.ALLA_FIRMA_AZIENDA, fascicolo.getStato());
	}

	@Test
	@WithMockUser(username = "utente", roles = {
			"a4gfascicolo.fascicolo.ricerca.ente",
			"a4ganagrafica.fascicolo.apertura.ente",
			"a4gfascicolo.fascicolo.ricerca.tutti"
	})
	@Sql(scripts = "/sql/fascicolo/fascicolo_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void imposta_stato_in_aggiornamento_ok() throws Exception {
		userSetupEnte();
		mockIoItaliaStuff();
		//mockWebClientOkStuff();

		String cuaa = "XPDNDR77B03L378W";

		FascicoloDto fascicolo = fascicoloController.getByCuaa(cuaa, 0);
		assertEquals(StatoFascicoloEnum.ALLA_FIRMA_AZIENDA, fascicolo.getStato());

		mockMvc.perform(
				put(String.format("/api/v1/fascicolo/%s/stato/in-aggiornamento", cuaa))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());

		// verifica stato aggiornato
		fascicolo = ricercaFascicoloService.getFascicoloDto(cuaa, 0);
		assertEquals(StatoFascicoloEnum.IN_AGGIORNAMENTO, fascicolo.getStato());
	}

	//	@Test
	//	@WithMockUser(username = "XPDNDR77B03L378R")
	//	@Sql(scripts = "/sql/fascicolo/fascicolo_controllato_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	//	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	//	@Transactional
	//	void start_validazione_ok() throws Exception {
	//		String cuaa = "XPDNDR77B03L378R";
	//		userSetupAzienda(cuaa);
	//		mockStartValidazioneZootecniaOkStuff();
	//		var sv = new SchedaValidazioneFascicoloDto();
	//		sv.setCodiceFiscale(cuaa);
	//		sv.setNextIdValidazione(1);
	//		// TODO set mock per controllo firme
	//		StartValidazioneFascicoloEvent eventTest = new StartValidazioneFascicoloEvent(sv);
	//		validazioneFascicoloService.startValidazioneFascicoloAsincrona(eventTest, cuaa, 1);
	//		// verifica stato aggiornato
	//		FascicoloDto fascicoloDto = ricercaFascicoloService.getFascicoloDto(cuaa, 0);
	//		assertEquals(StatoFascicoloEnum.VALIDATO, fascicoloDto.getStato());
	//
	//		Optional<FascicoloModel> fascicoloModelStoricizzatoOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 1);
	//		FascicoloModel fascicoloModelStoricizzato = fascicoloModelStoricizzatoOpt.get();
	//		assertNotNull(fascicoloModelStoricizzato);
	//		assertEquals(StatoFascicoloEnum.VALIDATO, fascicoloModelStoricizzato.getStato());
	//		List<ModoPagamentoModel> modoPagamentoLiveList = fascicoloModelStoricizzato.getModoPagamentoList();
	//		assertEquals(1, modoPagamentoLiveList.size());
	//		assertEquals(cuaa, fascicoloModelStoricizzato.getCodiceFiscaleRappresentanteLegaleValidato());
	//		assertEquals(cuaa, fascicoloModelStoricizzato.getNominativoRappresentanteLegaleValidato());
	//		assertEquals(testDayLocalDate, fascicoloModelStoricizzato.getDataValidazione());
	//
	//		Optional<FascicoloModel> fascicoloModelValidato = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 1);
	//		List<ModoPagamentoModel> modoPagamentoValidatoList = fascicoloModelValidato.get().getModoPagamentoList();
	//		assertEquals(1, modoPagamentoValidatoList.size());
	//
	//		if (fascicoloModelStoricizzato.getDocumentoIdentita() != null) {
	//			assertEquals(fascicoloModelStoricizzato.getDocumentoIdentita().getNumero(), fascicoloModelValidato.get().getDocumentoIdentita().getNumero());
	//		}
	//	}

	//	@Test
	//	@WithMockUser(username = "XPDNDR77B03L378K")
	//	@Sql(scripts = "/sql/fascicolo/fascicolo_controllato_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	//	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	//	@Transactional
	//	void start_validazione_ko_CONTROLLATO() throws Exception {
	//		String cuaa = "XPDNDR77B03L378K";
	//		String eventClassError = "it.tndigitale.a4g.fascicolo.anagrafica.business.event.StartValidazioneFascicoloEvent";
	//		String json = "{\"data\":{\"cuaa\":\"XPDNDR77B03L378K\",\"idValidazione\":1},\"numberOfRetry\":1,\"username\":\"XPDNDR77B03L378K\"}";
	//		userSetupAzienda(cuaa);
	//		mockStartValidazioneZootecniaOkStuff();
	//
	//		var sv = new SchedaValidazioneFascicoloDto();
	//		sv.setCodiceFiscale(cuaa);
	//		sv.setNextIdValidazione(1);
	//		StartValidazioneFascicoloEvent eventTest = new StartValidazioneFascicoloEvent(sv);
	//		validazioneFascicoloService.startValidazioneFascicoloAsincrona(eventTest, cuaa, 1);
	//
	//		List<EventStoredModel> eventiFalliti = eventStoreService.findAll();
	//		List<EventStoredModel> eventiFallitiFiltrati = eventiFalliti.stream()
	//				.filter( evento -> evento.getEvent().equals(eventClassError) && evento.getJsonEvent().equals(json))
	//				.collect(Collectors.toCollection(ArrayList::new));
	//	}


	//	@Test
	//	@WithMockUser(username = "XPDNDR77B03L377S")
	//	@Sql(scripts = "/sql/fascicolo/fascicolo_controllato_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	//	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	//	@Transactional
	//	void start_validazione_ko() throws Exception {
	//		String cuaa = "XPDNDR77B03L3773";
	//		String eventClassError = "it.tndigitale.a4g.fascicolo.anagrafica.business.event.StartValidazioneFascicoloEvent";
	//		String json = "{\"data\":{\"codiceFiscale\":\"XPDNDR77B03L3773\",\"report\":null,\"allegati\":null,\"nextIdValidazione\":1,\"tipoDetenzione\":\"MANDATO\"},\"numberOfRetry\":1,\"username\":\"XPDNDR77B03L377S\"}";
	//
	//		mockStartValidazioneZootecniaOkStuff();
	//
	//		var sv = new SchedaValidazioneFascicoloDto();
	//		sv.setCodiceFiscale(cuaa);
	//		sv.setNextIdValidazione(1);
	//		StartValidazioneFascicoloEvent eventTest = new StartValidazioneFascicoloEvent(sv);
	//		validazioneFascicoloService.startValidazioneFascicoloAsincrona(eventTest, cuaa, 1);
	////		controllare se è stato salvato l'evento fallito
	//		List<EventStoredModel> eventiFalliti = eventStoreService.findAll();
	//		List<EventStoredModel> eventiFallitiFiltrati = eventiFalliti.stream().filter( evento -> evento.getEvent().equals(eventClassError) && evento.getJsonEvent().equals(json)).collect(Collectors.toCollection(ArrayList::new));
	//
	//		assertNotNull(eventiFallitiFiltrati);
	//		assertNotEquals(eventiFallitiFiltrati.size(), 0);
	//
	//	}

	//	@Test
	//	@WithMockUser(username = "XPDNDR77B03L377S")
	//	@Sql(scripts = "/sql/fascicolo/fascicolo_controllato_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	//	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	//	@Transactional
	//	void start_validazione_persona_fisica_ok() throws Exception {
	//		String cuaa = "XPDNDR77B03L377S";
	//		mockStartValidazioneZootecniaOkStuff();
	//
	//		var sv = new SchedaValidazioneFascicoloDto();
	//		sv.setCodiceFiscale(cuaa);
	//		sv.setNextIdValidazione(1);
	//		StartValidazioneFascicoloEvent eventTest = new StartValidazioneFascicoloEvent(sv);
	//		validazioneFascicoloService.startValidazioneFascicoloAsincrona(eventTest, cuaa, 1);
	//		// verifica stato aggiornato
	//		FascicoloDto fascicoloDto = ricercaFascicoloService.getFascicoloDto(cuaa, 0);
	//		assertEquals(StatoFascicoloEnum.VALIDATO, fascicoloDto.getStato());
	//
	//		Optional<FascicoloModel> fascicoloModelValidationOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 1);
	//		FascicoloModel fascicoloModelValidation = fascicoloModelValidationOpt.get();
	//		assertNotNull(fascicoloModelValidation);
	//		assertEquals(StatoFascicoloEnum.VALIDATO, fascicoloModelValidation.getStato());
	//		List<ModoPagamentoModel> modoPagamentoLiveList = fascicoloModelValidation.getModoPagamentoList();
	//		assertEquals(2, modoPagamentoLiveList.size());
	//		assertEquals(cuaa, fascicoloModelValidation.getCodiceFiscaleRappresentanteLegaleValidato());
	//		assertEquals(cuaa, fascicoloModelValidation.getNominativoRappresentanteLegaleValidato());
	//		assertEquals(testDayLocalDate, fascicoloModelValidation.getDataValidazione());
	//
	//		Optional<FascicoloModel> fascicoloModelValidatoOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 1);
	//		FascicoloModel fascicoloModelValidato = fascicoloModelValidatoOpt.get();
	//		List<ModoPagamentoModel> modoPagamentoValidatoList = fascicoloModelValidato.getModoPagamentoList();
	//		assertEquals(2, modoPagamentoValidatoList.size());
	//
	//		List<IscrizioneSezioneModel> iscrizioni = fascicoloModelValidato.getPersona().getIscrizioniSezione();
	//		assertEquals(2, iscrizioni.size());
	//
	//		PersonaFisicaModel pf = (PersonaFisicaModel)fascicoloModelValidato.getPersona();
	//		//List<AttivitaAtecoModel> attivitaAteco = pf.getImpresaIndividuale().getSedeLegale().getAttivita();
	//		List<AttivitaAtecoModel> attivitaAteco = attivitaAtecoDao.findByPersonaModelAndIdValidazione(pf, 1);
	//		assertEquals(1, attivitaAteco.size());
	//		AttivitaAtecoModel attivitaAtecoModel = attivitaAteco.get(0);
	//		assertEquals(1, attivitaAtecoModel.getIdValidazione());
	//
	////		[begin] per coverage: verifica uguaglianza model
	//		assertEquals(true, pf.equals(pf));
	//		assertEquals(false, pf.equals(null));
	//		assertEquals(false, pf.equals(new Object()));
	//		PersonaFisicaModel pfTest = new PersonaFisicaModel();
	//		BeanUtils.copyProperties(pf, pfTest);
	//		pfTest.setPec("paperoga@topolinia.us");
	//		assertEquals(false, pf.equals(pfTest));
	////		[end] per coverage: verifica uguaglianza model
	//
	//		// verifica validazione ute
	//		List<UnitaTecnicoEconomicheModel> ute = uteDao.findByPersona_CodiceFiscaleAndIdValidazione(pf.getCodiceFiscale(), 1);
	//		assertEquals(1, ute.size());
	//		UnitaTecnicoEconomicheModel uteModel = ute.get(0);
	//		assertEquals(1, uteModel.getIdValidazione());
	////		[begin] per coverage: verifica uguaglianza model
	//		assertEquals(true, uteModel.equals(uteModel));
	//		assertEquals(false, uteModel.equals(null));
	//		assertEquals(false, uteModel.equals(new Object()));
	//		UnitaTecnicoEconomicheModel uteModelTest = new UnitaTecnicoEconomicheModel();
	//		BeanUtils.copyProperties(uteModel, uteModelTest);
	//		uteModelTest.setProvincia("AR");
	//		assertEquals(false, uteModel.equals(uteModelTest));
	////		[end] per coverage: verifica uguaglianza model
	//
	//		// verifica validazione attivita ateco collegate alle ute
	//		List<AttivitaAtecoModel> attivitaAtecoUte = attivitaAtecoDao.findByUnitaTecnicoEconomicheAndIdValidazione(pf.getUnitaTecnicoEconomiche().get(0), 1);
	//		assertEquals(2, attivitaAtecoUte.size());
	//		AttivitaAtecoModel attivitaAtecoUteModel = attivitaAtecoUte.get(0);
	//		assertEquals(1, attivitaAtecoUteModel.getIdValidazione());
	//
	//		// verifica validazione destinazioni uso
	//		List<DestinazioneUsoModel> destinazioneUso = destinazioneUsoDao.findByUnitaTecnicoEconomicheAndIdValidazione(pf.getUnitaTecnicoEconomiche().get(0), 1);
	//		assertEquals(2, destinazioneUso.size());
	//		DestinazioneUsoModel destinazioneUsoModel = destinazioneUso.get(0);
	//		assertEquals(1, destinazioneUsoModel.getIdValidazione());
	//
	//		// verifica validazione documento d'indentita' solo per fascicoli di persone giuridiche
	//		List<DocumentoIdentitaModel> documentoIdentitaModelList = documentoIdentitaDao.findByFascicolo_CuaaAndIdValidazione(fascicoloModelValidation.getCuaa(), 1);
	//		assertEquals(0, documentoIdentitaModelList.size());
	////		DocumentoIdentitaModel documentoIdentitaModel = documentoIdentitaModelList.get(0);
	////		assertEquals(1, documentoIdentitaModel.getIdValidazione());
	//
	//		// test seconda validazione
	//		Optional<FascicoloModel> fascicoloModelLive2 = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0);
	//		fascicoloModelLive2.get().setStato(StatoFascicoloEnum.IN_VALIDAZIONE);
	//		fascicoloDao.save(fascicoloModelLive2.get());
	//
	//		var sv2 = new SchedaValidazioneFascicoloDto();
	//		sv2.setCodiceFiscale(cuaa);
	//		sv2.setNextIdValidazione(2);
	//		StartValidazioneFascicoloEvent eventTest2 = new StartValidazioneFascicoloEvent(sv2);
	//		validazioneFascicoloService.startValidazioneFascicoloAsincrona(eventTest2, cuaa, 2);
	//		assertEquals(1, fascicoloModelValidato.getPersona().getIdValidazione());
	//	}

	//	@Test
	//	@WithMockUser(username = "XPDNDR77B03L378R")
	//	@Sql(scripts = "/sql/fascicolo/fascicolo_controllato_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	//	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	//	void start_validazione_ok_verifica_stato() throws Exception {
	//		String cuaa = "XPDNDR77B03L378R";
	//		mockStartValidazioneZootecniaOkStuff();
	//
	//		var sv = new SchedaValidazioneFascicoloDto();
	//		sv.setCodiceFiscale(cuaa);
	//		sv.setNextIdValidazione(1);
	//		StartValidazioneFascicoloEvent eventTest = new StartValidazioneFascicoloEvent(sv);
	//		validazioneFascicoloService.startValidazioneFascicoloAsincrona(eventTest, cuaa, 1);
	//
	//		// verifica stato aggiornato
	//		FascicoloDto fascicoloDto = ricercaFascicoloService.getFascicoloDto(cuaa, 0);
	//		assertEquals(StatoFascicoloEnum.VALIDATO, fascicoloDto.getStato());
	//
	////		qui la transazione deve fallire perche' non si può modificare un fascicolo con idValidazione > 0
	//		assertThrows(TransactionSystemException.class, () -> {
	//			transactionTemplate.execute(status -> {
	//				Optional<FascicoloModel> fascicoloModelValidationOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 1);
	//				FascicoloModel fascicoloModelValidationLocal = fascicoloModelValidationOpt.get();
	//				assertNotNull(fascicoloModelValidationLocal);
	//				assertEquals(StatoFascicoloEnum.VALIDATO, fascicoloModelValidationLocal.getStato());
	//				fascicoloModelValidationLocal.setDenominazione(cuaa);
	////				TODO se imposto lo stato di un fascicolo validato (idValidazione > 0) mi permette comunque l'aggiornamento
	////				fascicoloModelValidation.setStato(StatoFascicolo.IN_AGGIORNAMENTO);
	//
	//				return fascicoloDao.save(fascicoloModelValidationLocal);
	//			});
	//	    });
	//
	//		FascicoloModel fascicoloModelLiveSaved = transactionTemplate.execute(status -> {
	//				Optional<FascicoloModel> fascicoloModelLiveOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0);
	//				FascicoloModel fascicoloModelLive = fascicoloModelLiveOpt.get();
	//				fascicoloModelLive.setDenominazione("QUALCHE DENOMINAZIONE");
	//				return fascicoloDao.save(fascicoloModelLive);
	//
	//		});
	//		assertEquals(StatoFascicoloEnum.IN_AGGIORNAMENTO, fascicoloModelLiveSaved.getStato());
	//		assertEquals("QUALCHE DENOMINAZIONE", fascicoloModelLiveSaved.getDenominazione());
	//	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.aggiornamento.modalitapagamento.ente"})
	@Sql(scripts = "/sql/fascicolo/fascicolo_validato_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void modifica_fascicolo_validato_ok_in_aggiornamento() throws Exception {
		String cuaa = "XPDNDR77B03L378X";
		userSetupEnte();
		String iban = "GB82 WEST 1234 5698 7654 32";
		setupMockitoVerificaIbanProxy(iban);
		setupMockitoVerificaIbanFakeProxy(iban);

		ModoPagamentoDto modoPagamento = new ModoPagamentoDto();
		modoPagamento.setIban(iban);

		FascicoloDto fascicoloDto = ricercaFascicoloService.getFascicoloDto(cuaa, 0);
		assertEquals(StatoFascicoloEnum.VALIDATO, fascicoloDto.getStato());

		mockMvc.perform(put(String.format("%s/%s/modo-pagamento", ApiUrls.FASCICOLO, cuaa))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(modoPagamento)))
		.andExpect(status().isOk());
		//		reimposta la security; per qualche motivo dopo mockMvc.perform() viene cancellato
		SecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
		fascicoloDto = ricercaFascicoloService.getFascicoloDto(cuaa, 0);
		assertEquals(StatoFascicoloEnum.IN_AGGIORNAMENTO, fascicoloDto.getStato());
	}

	@Test
	@WithMockUser(username = "caa", roles = {
			"a4gfascicolo.fascicolo.ricerca.ente",
			"a4ganagrafica.fascicolo.apertura.ente",
			"a4gfascicolo.fascicolo.ricerca.tutti"
	})
	@Sql(scripts = "/sql/fascicolo/fascicolo_controllato_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void get_unita_locali() throws Exception {
		String cuaa = "XPDNDR77B03L377S";
		userSetupCaa(13L);
		String endpoint = String.format("/%s/unita-tecnico-economiche", cuaa);

		MockHttpServletRequestBuilder requestBuilder =
				MockMvcRequestBuilders.multipart(ApiUrls.FASCICOLO.concat(endpoint))
				.param("cuaa", cuaa)
				.contentType(MediaType.APPLICATION_JSON);

		requestBuilder.with(request -> {
			request.setMethod(HttpMethod.GET.name());
			return request;
		});

		ResultActions resultActions = this.mockMvc.perform(
				requestBuilder
				);

		resultActions.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(jsonPath("$", hasSize(1)))
		.andExpect(jsonPath("$[0].identificativoUte").value("2"))
		.andExpect(jsonPath("$[0].provincia").value("FROSINONE"));
	}

	//	@Test
	//	@WithMockUser(username = "XPDNDR77B03L378R")
	//	@Sql(scripts = "/sql/fascicolo/fascicolo_controllato_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	//	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	//	void validazione_se_zootecnia_fail_allora_fascicolo_non_validato() throws Exception {
	//		String cuaa = "XPDNDR77B03L378R";
	//		mockWebClientFailStuff();
	//
	//		var sv = new SchedaValidazioneFascicoloDto();
	//		sv.setCodiceFiscale(cuaa);
	//		sv.setNextIdValidazione(1);
	//		StartValidazioneFascicoloEvent eventTest = new StartValidazioneFascicoloEvent(sv);
	//		validazioneFascicoloService.startValidazioneFascicoloAsincrona(eventTest, cuaa, 1);
	//
	//		// verifica stato aggiornato
	//		FascicoloDto fascicoloDto = ricercaFascicoloService.getFascicoloDto(cuaa, 0);
	//		assertEquals(StatoFascicoloEnum.IN_VALIDAZIONE, fascicoloDto.getStato());
	//
	//		/*
	//		 * La modifica deve fallire per l'eccezione:
	//		 * it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloInValidazioneException:
	//		 * Non e' possibile modificare un fascicolo in stato 'IN_VALIDAZIONE'
	//		 */
	//		assertThrows(FascicoloInValidazioneException.class, () -> {
	//			try {
	//				transactionTemplate.execute(status -> {
	//					Optional<FascicoloModel> fascicoloModelLiveOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0);
	//					FascicoloModel fascicoloModelLive = fascicoloModelLiveOpt.get();
	//					fascicoloModelLive.setDenominazione("QUALCHE DENOMINAZIONE");
	//					return fascicoloDao.save(fascicoloModelLive);
	//				});
	//			} catch (Exception e) {
	//				throw(e.getCause().getCause().getCause());
	//			}
	//		});
	//	}

	//	@Test
	//	@WithMockUser(username = "XPDNDR77B03L378X")
	//	@Sql(scripts = "/sql/fascicolo/fascicolo_persona_giuridica_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	//	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	//	@Transactional
	//	void start_validazione_persona_giuridica_ok() throws Exception {
	//		String cuaa = "00959460221";
	//		String utenteAzienda = "XPDNDR77B03L378X";
	//		userSetupAzienda(utenteAzienda);
	//		mockStartValidazioneZootecniaOkStuff();
	//
	//
	//		var sv = new SchedaValidazioneFascicoloDto();
	//		sv.setCodiceFiscale(cuaa);
	//		sv.setNextIdValidazione(1);
	//		StartValidazioneFascicoloEvent eventTest = new StartValidazioneFascicoloEvent(sv);
	//		validazioneFascicoloService.startValidazioneFascicoloAsincrona(eventTest, cuaa, 1);
	//
	//		// verifica stato aggiornato
	//		FascicoloDto fascicoloDto = ricercaFascicoloService.getFascicoloDto(cuaa, 0);
	//		assertEquals(StatoFascicoloEnum.VALIDATO, fascicoloDto.getStato());
	//
	//		Optional<FascicoloModel> fascicoloModelValidationOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 1);
	//		FascicoloModel fascicoloModelValidation = fascicoloModelValidationOpt.get();
	//		assertNotNull(fascicoloModelValidation);
	//		assertEquals(StatoFascicoloEnum.VALIDATO, fascicoloModelValidation.getStato());
	//
	//		assertEquals(utenteAzienda, fascicoloModelValidation.getCodiceFiscaleRappresentanteLegaleValidato());
	//		assertEquals(utenteAzienda, fascicoloModelValidation.getNominativoRappresentanteLegaleValidato());
	//
	//		PersonaGiuridicaModel pg = (PersonaGiuridicaModel)fascicoloModelValidation.getPersona();
	//		List<CaricaModel> cariche = caricaDao.findByPersonaGiuridicaModel(pg);
	//		assertEquals(2, cariche.size());
	//		assertTrue(cariche.stream().anyMatch(c -> c.getDescrizione().equals("SOCIO")));
	//		assertTrue(cariche.stream().anyMatch(c -> c.getDescrizione().equals("RAPPRESENTANTE")));
	////		[begin] per coverage: verifica uguaglianza model
	//		assertEquals(true, pg.equals(pg));
	//		assertEquals(false, pg.equals(null));
	//		assertEquals(false, pg.equals(new Object()));
	//		PersonaGiuridicaModel pgTest = new PersonaGiuridicaModel();
	//		BeanUtils.copyProperties(pg, pgTest);
	//		pgTest.setPec("AR@b.c");
	//		assertEquals(false, pg.equals(pgTest));
	////		-----
	//		CaricaModel caricaModel = cariche.get(0);
	//		assertEquals(true, caricaModel.equals(caricaModel));
	//		assertEquals(false, caricaModel.equals(null));
	//		assertEquals(false, caricaModel.equals(new Object()));
	//		CaricaModel caricaModelTest = new CaricaModel();
	//		BeanUtils.copyProperties(caricaModel, caricaModelTest);
	//
	//		caricaModelTest.setPersonaGiuridicaModel(pgTest);
	//		assertEquals(false, caricaModel.equals(caricaModelTest));
	////		[end] per coverage: verifica uguaglianza model
	//
	//	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4gfascicolo.fascicolo.ricerca.tutti"})
	@Sql(scripts = "/sql/fascicolo/fascicolo_insert_validati.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void get_validazioni_fascicolo_ok() throws Exception {
		userSetupEnte();
		String cuaa = "XPDNDR77B03L378X";

		ResultActions resultActions = mockMvc.perform(get(String.format("%s/%s/validati", ApiUrls.FASCICOLO, cuaa))
				.contentType(MediaType.APPLICATION_JSON));

		resultActions.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(jsonPath("$.count").value("1"))
		.andExpect(jsonPath("$.risultati[0].id").exists())
		.andExpect(jsonPath("$.risultati[0].idValidazione").value("1"))
		.andExpect(jsonPath("$.risultati[0].cuaa").value(cuaa));
	}

	@Test
	@WithMockUser(username = "XPDNDR77B03L378X", roles = {"a4gfascicolo.fascicolo.ricerca.ente"})
	@Sql(scripts = "/sql/fascicolo/fascicolo_get_persona_con_carica.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void get_persona_fisica_con_carica_ok() throws Exception {
		userSetupEnte();
		String cuaa = "00959460221";

		mockMvc.perform(get(String.format("%s/%s/carica/persona-fisica", ApiUrls.API_V1, cuaa))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(jsonPath("$.length()", is(2)))
		.andExpect(jsonPath("$[0].codiceFiscale").value("XPDNDR77B03L378X"))
		.andExpect(jsonPath("$[1].codiceFiscale").value("DVNGTN63E17G482Y"));
	}

	@Test
	@WithMockUser(username = "XPDNDR77B03L378X", roles = {"a4gfascicolo.fascicolo.ricerca.ente"})
	@Sql(scripts = "/sql/fascicolo/fascicolo_get_persona_con_carica.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void get_persona_giuridica_con_carica_ok() throws Exception {
		userSetupCaa(null);
		userSetupEnte();
		String cuaa = "00959460221";

		mockMvc.perform(get(String.format("%s/%s/carica/persona-giuridica", ApiUrls.API_V1, cuaa))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(print())
		.andExpect(jsonPath("$.length()", is(1)))
		.andExpect(jsonPath("$[0].codiceFiscale").value("12979880155"));
	}

	@Test
	@WithMockUser(username = "XPDNDR77B03L378X", roles = {"a4gfascicolo.fascicolo.ricerca.ente"})
	@Sql(scripts = "/sql/fascicolo/fascicolo_get_persona_con_carica.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void get_possibili_rappresentanti_legali_ok() throws Exception {
		userSetupEnte();
		String cuaa = "00959460221";

		mockMvc.perform(get(String.format("%s/%s/carica/possibili-rappresentanti-legali", ApiUrls.API_V1, cuaa))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(jsonPath("$.length()", is(1)))
		.andExpect(jsonPath("$[0].codiceFiscale").value("XPDNDR77B03L378X"))
		.andExpect(jsonPath("$[0].cariche[0].descrizione").value("SOCIO"));
	}

	@Test
	@WithMockUser(username = "utente", roles = {
			"a4gfascicolo.fascicolo.ricerca.ente",
			"a4ganagrafica.fascicolo.apertura.ente",
			"a4gfascicolo.fascicolo.ricerca.tutti"
	})
	@Sql(scripts = "/sql/fascicolo/fascicolo_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void verificaDatiAperturaPersonaFisicaSenzaImpresaIndividualeKO_FascicoloLocaleEsistente() throws Exception {
		userSetupEnte();
		mockUserSetupPersonaFisicaATSenzaImpresaIndividuale();

		String cuaa = "XPDNDR77B03L378W";
		String message = mockMvc.perform(get(String.format("%s/%s/verifica/dati-apertura", ApiUrls.FASCICOLO, cuaa))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andReturn().getResponse().getErrorMessage();

		assertEquals("FASCICOLO_LOCALE_ESISTENTE", message);
	}

	@Test
	@WithMockUser(username = "utente", roles = {
			"a4gfascicolo.fascicolo.ricerca.ente",
			"a4ganagrafica.fascicolo.apertura.ente",
			"a4gfascicolo.fascicolo.ricerca.tutti"
	})
	@Sql(scripts = "/sql/fascicolo/fascicolo_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void verificaDatiAperturaPersonaFisicaSenzaImpresaIndividualeOK() throws Exception {
		String cuaa = "ZPDNDR77B03L378W";
		userSetupEnte();
		mockUserSetupPersonaFisicaATSenzaImpresaIndividuale();
		mockSianAppag(cuaa, true);

		DatiAperturaFascicoloDto datiAperturaFascicoloDto = fascicoloController.verificaAperturaFascicolo(cuaa);

		assertEquals("Rossi Mario", datiAperturaFascicoloDto.getDenominazioneFascicolo());
		assertEquals("ZPDNDR77B03L378W", datiAperturaFascicoloDto.getDatiAnagraficiRappresentante().getCodiceFiscale());
		assertEquals("TN", datiAperturaFascicoloDto.getDomicilioFiscaleRappresentante().getProvincia());
	}

	@Test
	@WithMockUser(username = "utente", roles = {
			"a4gfascicolo.fascicolo.ricerca.ente",
			"a4ganagrafica.fascicolo.apertura.ente",
			"a4gfascicolo.fascicolo.ricerca.tutti"
	})
	@Sql(scripts = "/sql/fascicolo/fascicolo_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void verificaDatiAperturaPersonaFisicaConImpresaIndividualeOK() throws Exception {
		String cuaa = "ZPDNDR77B03L378W";
		userSetupEnte();
		mockUserSetupPersonaFisicaATConImpresaIndividuale();
		mockSianAppag(cuaa, true);

		DatiAperturaFascicoloDto datiAperturaFascicoloDto = fascicoloController.verificaAperturaFascicolo(cuaa);

		assertEquals("FERRARI CESARE", datiAperturaFascicoloDto.getDenominazioneFascicolo());
		assertEquals("ZPDNDR77B03L378W", datiAperturaFascicoloDto.getDatiAnagraficiRappresentante().getCodiceFiscale());
		assertEquals("TN", datiAperturaFascicoloDto.getDomicilioFiscaleRappresentante().getProvincia());
	}

	@Test
	@WithMockUser(username = "utente", roles = {
			"a4gfascicolo.fascicolo.ricerca.ente",
			"a4ganagrafica.fascicolo.apertura.ente",
			"a4gfascicolo.fascicolo.ricerca.tutti"
	})
	@Sql(scripts = "/sql/fascicolo/fascicolo_persona_giuridica_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void verificaDatiAperturaPersonaGiuridicaOK() throws Exception {
		String cuaa = "00959460222";
		userSetupEnte();
		mockUserSetupPersonaGiuridicaAT(cuaa);
		mockSianAppag(cuaa, true);


		DatiAperturaFascicoloDto datiAperturaFascicoloDto = fascicoloController.verificaAperturaFascicolo(cuaa);
		assertEquals("Societa' fittizia", datiAperturaFascicoloDto.getDenominazione());
		assertEquals("00959460222", datiAperturaFascicoloDto.getCodiceFiscale());

		assertEquals("ZPDNDR77B03L378W", datiAperturaFascicoloDto.getDatiAnagraficiRappresentante().getCodiceFiscale());
		assertEquals("Paolo Rossi Inc.", datiAperturaFascicoloDto.getDatiAnagraficiRappresentante().getNominativo());
	}

	@Test
	@Disabled
	/*
	 disabilitato: il test fallisce per un problema nell'individuare sportello e caa competenti
	 nei controlli di abilitazione. Si consiglia di verificare la correttezza di tali controlli.
	 */
	@WithMockUser(username = "utente", roles = {
			"a4gfascicolo.fascicolo.ricerca.ente",
			"a4ganagrafica.fascicolo.apertura.ente",
			"a4gfascicolo.fascicolo.ricerca.tutti"
	})
	@Sql(scripts = "/sql/fascicolo/fascicolo_persona_giuridica_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void verificaDatiAperturaPersonaGiuridicaKO_FascicoloLocaleEsistente() throws Exception {
		String cuaa = "00959460221";
		userSetupEnte();
		mockUserSetupPersonaGiuridicaAT(cuaa);
		mockSianAppag(cuaa, true);

		String message = mockMvc.perform(get(String.format("%s/%s/verifica/dati-apertura", ApiUrls.FASCICOLO, cuaa))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andReturn().getResponse().getErrorMessage();
		assertEquals("FASCICOLO_LOCALE_ESISTENTE", message);
	}

	@Test
	@WithMockUser(username = "utente", roles = {
			"a4gfascicolo.fascicolo.ricerca.ente",
			"a4ganagrafica.fascicolo.apertura.ente",
			"a4gfascicolo.fascicolo.ricerca.tutti"
	})
	@Sql(scripts = "/sql/fascicolo/fascicolo_persona_giuridica_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void rappresentanteLegaleRespingiValidazioneKO_AccessDenied() throws Exception {
		String cuaa = "00959460229";
		String utenteAzienda = "XPDNDR77B03L378X";
		userSetupAzienda(utenteAzienda);
		mockUserSetupPersonaGiuridicaAT(cuaa);
		mockSianAppag(cuaa, true);

		assertThrows(AccessDeniedException.class, () -> {
			fascicoloController.rappresentanteLegaleRespingiValidazione(cuaa);
		});
	}


	@Test
	@WithMockUser(username = "utente", roles = {
			"a4gfascicolo.fascicolo.ricerca.ente",
			"a4ganagrafica.fascicolo.apertura.ente",
			"a4gfascicolo.fascicolo.ricerca.tutti"
	})
	@Sql(scripts = "/sql/fascicolo/fascicolo_persona_giuridica_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void rappresentanteLegaleRespingiValidazioneKO_StatoDiversoDaControllato() throws Exception {
		String cuaa = "00959460221";
		String utenteAzienda = "XPDNDR77B03L378X";
		userSetupAzienda(utenteAzienda);
		mockUserSetupPersonaGiuridicaAT(cuaa);
		mockSianAppag(cuaa, true);

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			fascicoloController.rappresentanteLegaleRespingiValidazione(cuaa);
		});
		assertEquals("Fascicolo con cuaa=[" + cuaa + "] in stato diverso da FIRMATO_CAA o ALLA_FIRMA_AZIENDA. Non e' possibile quindi il respingimento", exception.getMessage());
	}

	@Test
	@WithMockUser(username = "utente", roles = {
			"a4gfascicolo.fascicolo.ricerca.ente",
			"a4ganagrafica.fascicolo.apertura.ente",
			"a4gfascicolo.fascicolo.ricerca.tutti"
	})
	@Sql(scripts = "/sql/fascicolo/fascicolo_persona_giuridica_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void rappresentanteLegaleRespingiValidazioneOK() throws Exception {
		String cuaa = "10959460221";
		String utenteAzienda = "YPDNDR77B03L378X";
		userSetupAzienda(utenteAzienda);
		mockRappresentanteLegale(utenteAzienda, "Mario", "Rossi");
		mockUserSetupPersonaGiuridicaAT(cuaa);
		mockSianAppag(cuaa, true);
		Mockito.when(clock.today()).thenReturn(LocalDate.of(2020, Month.NOVEMBER, 1));
		fascicoloController.rappresentanteLegaleRespingiValidazione(cuaa);

		// invio mail CAA
		String[] mailArgs =  {
				"Mario",
				"Rossi",
				"DEPEDRI ANDREA2",
				cuaa
		};
		String[] oggettoArgs =  {
				"DEPEDRI ANDREA2"
		};

		String text = String.format("Il Titolare rappresentante %s %s ha respinto la richiesta di validazione del fascicolo %s con CUAA %s.",
				mailArgs);
		String oggetto = String.format("Respinta richiesta di validazione del fascicolo di %s",
				oggettoArgs);		
		// invio mail CAA
		verify(emailSender, times(1)).send(Mockito.any(SimpleMailMessage.class));

		Optional<FascicoloModel> fascicoloRespintoOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0);
		assertNotNull(fascicoloRespintoOpt);
		assertEquals("IN_AGGIORNAMENTO", fascicoloRespintoOpt.get().getStato().toString());
	}

	@Test
	@WithMockUser(username = "XPDNDR77B03L378W")
	@Sql(scripts = "/sql/fascicolo/fascicolo_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void start_validazione_fail_stato_non_controllato() throws Exception {
		String cuaa = "XPDNDR77B03L378X";
		userSetupAzienda(cuaa);
		mockStartValidazioneZootecniaOkStuff();
		mockControlloFirmaProxy(cuaa);
		byte[] pdf = {'1', '2', '3'};

		mockMvc.perform(
				put(String.format("%s/%s/report-scheda-validazione-firmata-firmatario", ApiUrls.FASCICOLO, cuaa))
				.content(pdf)
				.contentType(MediaType.APPLICATION_OCTET_STREAM))
		.andExpect(status().isFailedDependency());

		Optional<FascicoloModel> fascicoloModelValidato = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 1);
		assertTrue(fascicoloModelValidato.isEmpty());
		//		verify(eventStoreService, times(2)).triggerRetry(any(), any());
	}

	@Test
	@WithMockUser(username = "utente-azienda")
	@Sql(scripts = "/sql/fascicolo/fascicolo_controllato_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void start_validazione_fascicolo_persona_fisica_ok_abilitato() throws Exception {
		String cuaa = "XPDNDR77B03L378X";
		userSetupAzienda(cuaa);
		mockControlloFirmaProxy(cuaa);
		byte[] pdf = {'1', '2', '3'};

		mockMvc.perform(put(
				String.format("%s/%s/report-scheda-validazione-firmata-firmatario", ApiUrls.FASCICOLO, cuaa))
				.content(pdf)
				.contentType(MediaType.APPLICATION_OCTET_STREAM))
		.andExpect(status().isOk());

		Optional<FascicoloModel> findByCuaaAndIdValidazioneOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0);
		FascicoloModel findByCuaaAndIdValidazione = findByCuaaAndIdValidazioneOpt.orElseThrow();

		assertEquals(StatoFascicoloEnum.IN_VALIDAZIONE, findByCuaaAndIdValidazione.getStato());
		byte[] savedPdf = findByCuaaAndIdValidazione.getSchedaValidazioneFirmata();
		assertArrayEquals(pdf, savedPdf);
	}

	@Test		
	@WithMockUser(username = "XPDNDR77B03L378X")
	@Sql(scripts = {
			"/sql/fascicolo/fascicolo_persona_giuridica_insert.sql",
			"/sql/fascicolo/fascicolo_persona_giuridica_update_alla_firma_azienda.sql"
	},
	executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void start_validazione_fascicolo_persona_giuridica_ok_abilitato() throws Exception {
		String cuaa = "00959460221";
		String utenteAzienda = "XPDNDR77B03L378X";
		userSetupAzienda(utenteAzienda);
		mockStartValidazioneZootecniaOkStuff();
		mockControlloFirmaProxy(cuaa);
		byte[] pdf = {'1', '2', '3'};

		mockMvc.perform(put(String.format("%s/%s/report-scheda-validazione-firmata-firmatario", ApiUrls.FASCICOLO, cuaa))
				.content(pdf)
				.contentType(MediaType.APPLICATION_OCTET_STREAM))
		.andExpect(status().isOk());

		Optional<FascicoloModel> findByCuaaAndIdValidazioneOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0);
		FascicoloModel findByCuaaAndIdValidazione = findByCuaaAndIdValidazioneOpt.orElseThrow();

		assertEquals(StatoFascicoloEnum.IN_VALIDAZIONE, findByCuaaAndIdValidazione.getStato());
		byte[] savedPdf = findByCuaaAndIdValidazione.getSchedaValidazioneFirmata();
		assertArrayEquals(pdf, savedPdf);
	}

	@Test		
	@WithMockUser(username = "utente", roles = {
			"a4gfascicolo.fascicolo.ricerca.utente"
	})
	@Sql(scripts = {
			"/sql/fascicolo/fascicolo_persona_giuridica_insert.sql",
			"/sql/fascicolo/fascicolo_persona_giuridica_update_alla_firma_azienda.sql"
	},
	executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void get_report_scheda_validazione_firmata() throws Exception {
		String cuaa = "02346040229";
		String utenteAzienda = "XPDNDR77B03L378X";
		userSetupAzienda(utenteAzienda);
		mockStartValidazioneZootecniaOkStuff();
		mockStampaComponentOkStuff();
		mockControlloFirmaProxy(cuaa);
		byte[] pdf = {0, '1', 0, '2', 0, '3'};

		MvcResult mvcResult = mockMvc.perform(get(String.format("%s/%s/report-scheda-validazione-firmata", ApiUrls.FASCICOLO, cuaa))
				.contentType(MediaType.APPLICATION_OCTET_STREAM))
				.andExpect(status().isOk())
				.andReturn();
		assertEquals(MediaType.APPLICATION_OCTET_STREAM_VALUE, 
				mvcResult.getResponse().getContentType());
		byte[] savedPdf = mvcResult.getResponse().getContentAsByteArray();
		assertArrayEquals(pdf, savedPdf);

		Optional<FascicoloModel> findByCuaaAndIdValidazioneOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0);
		FascicoloModel findByCuaaAndIdValidazione = findByCuaaAndIdValidazioneOpt.orElseThrow();

		assertEquals(StatoFascicoloEnum.ALLA_FIRMA_AZIENDA, findByCuaaAndIdValidazione.getStato());
	}

	@Test
	@WithMockUser(username = "caa", roles = {
			"a4gfascicolo.fascicolo.ricerca.ente",
			"a4ganagrafica.fascicolo.apertura.ente",
			"a4gfascicolo.fascicolo.ricerca.tutti"
	})
	@Sql(scripts = {
			"/sql/fascicolo/fascicolo_persona_giuridica_insert.sql",
			"/sql/fascicolo/fascicolo_persona_giuridica_update_alla_firma_azienda.sql"
	},
	executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void put_report_scheda_validazione_firmata_CAA() throws Exception {
		String cuaa = "02346040229";

		Mockito.when(clock.today()).thenReturn(LocalDate.now());

		userSetupCaa(28L);
		mockStartValidazioneZootecniaOkStuff();
		mockControlloFirmaProxy(cuaa);

		String endpoint = String.format("/%s/report-scheda-validazione-firmata", cuaa);
		Path path = Paths.get("src/test/resources/fascicolo/MANDATO_ftoDPDNDR77B03L378L.pdf");
		MockMultipartFile allegato = new MockMultipartFile("moduloRevocaFirmato.pdf", Files.readAllBytes(path));


		MockHttpServletRequestBuilder requestBuilder =
				MockMvcRequestBuilders.multipart(ApiUrls.FASCICOLO.concat(endpoint))
				.file("schedaValidazioneFirmata", allegato.getBytes())
				.param("cuaa", cuaa)
				.contentType(MediaType.APPLICATION_JSON);

		requestBuilder.with(request -> {
			request.setMethod(HttpMethod.PUT.name());
			return request;
		});

		this.mockMvc.perform(requestBuilder)
		.andExpect(status().isOk());

		Optional<FascicoloModel> findByCuaaAndIdValidazioneOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0);
		FascicoloModel findByCuaaAndIdValidazione = findByCuaaAndIdValidazioneOpt.orElseThrow();

		assertArrayEquals(allegato.getBytes(), findByCuaaAndIdValidazione.getSchedaValidazioneFirmata());
		assertEquals(StatoFascicoloEnum.FIRMATO_CAA, findByCuaaAndIdValidazione.getStato());
	}

	//	@Test
	//	@WithMockUser(username = "caa", roles = {
	//			"a4gfascicolo.fascicolo.ricerca.ente",
	//			"a4ganagrafica.fascicolo.apertura.ente",
	//			"a4gfascicolo.fascicolo.ricerca.tutti"
	//			})
	//	@Sql(scripts = {
	//			"/sql/fascicolo/fascicolo_persona_giuridica_insert.sql",
	//			"/sql/fascicolo/fascicolo_persona_giuridica_update_revoca.sql"
	//		},
	//		executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	//	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	//	@Transactional
	//	void controllo_completezza_fascicolo_all_fail() throws Exception {
	//		String cuaa = "00959460221";
	//
	//		userSetupCaa(null);
	//		Mockito.when(clock.today()).thenReturn(LocalDate.of(2020, Month.OCTOBER, 1));
	//
	//		String stringResponse = mockMvc.perform(
	//				get(String.format("/api/v1/fascicolo/private/%s/controllo-completezza", cuaa))
	//				.contentType(MediaType.APPLICATION_JSON))
	//		.andExpect(status().isOk())
	//		.andReturn().getResponse().getContentAsString();
	//
	//		Map<ControlliFascicoloAnagraficaCompletoEnum, EsitoControlloDto> retVal = objectMapper.readValue(stringResponse,new TypeReference<Map<ControlliFascicoloAnagraficaCompletoEnum, EsitoControlloDto>>(){});
	//
	//		assertEquals(-3, retVal.get(ControlliFascicoloAnagraficaCompletoEnum.IS_IN_AGGIORNAMENTO).getEsito());
	//		assertEquals(-3, retVal.get(ControlliFascicoloAnagraficaCompletoEnum.IS_NOT_REVOCA_IN_CORSO).getEsito());
	//		assertEquals(-3, retVal.get(ControlliFascicoloAnagraficaCompletoEnum.IS_MODALITA_PAGAMENTO_PRESENTE).getEsito());
	//	}

	//	@Test
	//	@WithMockUser(username = "caa", roles = {
	//			"a4gfascicolo.fascicolo.ricerca.ente",
	//			"a4ganagrafica.fascicolo.apertura.ente",
	//			"a4gfascicolo.fascicolo.ricerca.tutti"
	//			})
	//	@Sql(scripts = {
	//			"/sql/fascicolo/fascicolo_persona_giuridica_insert.sql",
	//			"/sql/fascicolo/fascicolo_persona_giuridica_update_in_aggiornamento.sql",
	//			"/sql/fascicolo/fascicolo_persona_giuridica_update_modo_pagamento.sql"
	//		},
	//		executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	//	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	//	@Transactional
	//	void controllo_completezza_fascicolo_ok() throws Exception {
	//		String cuaa = "00959460221";
	//
	//		userSetupCaa(null);
	//		Mockito.when(clock.today()).thenReturn(LocalDate.of(2020, Month.OCTOBER, 1));
	//
	//		String stringResponse = mockMvc.perform(
	//				get(String.format("/api/v1/fascicolo/private/%s/controllo-completezza", cuaa))
	//				.contentType(MediaType.APPLICATION_JSON))
	//		.andExpect(status().isOk())
	//		.andReturn().getResponse().getContentAsString();
	//
	//		Map<ControlliFascicoloAnagraficaCompletoEnum, EsitoControlloDto> retVal = objectMapper.readValue(stringResponse,new TypeReference<Map<ControlliFascicoloAnagraficaCompletoEnum, EsitoControlloDto>>(){});
	//		assertEquals(0, retVal.get(ControlliFascicoloAnagraficaCompletoEnum.IS_IN_AGGIORNAMENTO).getEsito());
	//		assertEquals(0, retVal.get(ControlliFascicoloAnagraficaCompletoEnum.IS_NOT_REVOCA_IN_CORSO).getEsito());
	//		assertEquals(0, retVal.get(ControlliFascicoloAnagraficaCompletoEnum.IS_MODALITA_PAGAMENTO_PRESENTE).getEsito());
	//	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void apertura_e_trasferimento_fascicolo_persona_giuridica_ko_provincia_attuale() throws Exception {
		userSetupCaa(89L);
		String cuaa = "00123890220";
		List<ByteArrayResource> mockAllegati = Arrays.asList(createMockMultipartFile(), createMockMultipartFile());
		ByteArrayResource mockMandato = createMockMultipartFile();
		setupMockitoAperturaFascicolo(clock.today(), cuaa);

		String endpoint = String.format("/%s/trasferisci", cuaa);

		MockHttpServletRequestBuilder requestBuilder =
				MockMvcRequestBuilders.multipart(ApiUrls.FASCICOLO.concat(endpoint))
				.file("contratto", mockMandato.getByteArray())
				.file("allegati", mockAllegati.get(0).getByteArray())
				.file("allegati", mockAllegati.get(1).getByteArray())
				.param("codiceFiscaleRappresentante", cuaa)
				.param("identificativoSportello", Long.toString(89L))
				.param("fascicoloOperationEnum", FascicoloOperationEnum.APRI_E_TRASFERISCI.toString())
				.contentType(MediaType.APPLICATION_JSON);

		requestBuilder.with(request -> {
			request.setMethod(HttpMethod.POST.name());
			return request;
		});
		String errorMessage = this.mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
				.andReturn().getResponse().getErrorMessage();
		assertEquals("PROVINCIA_ATTUALE_TRENTO", errorMessage);
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void apertura_e_trasferimento_fascicolo_persona_giuridica_ok() throws Exception {
		userSetupCaa(89L);
		String cuaa = "00123890223";
		List<ByteArrayResource> mockAllegati = Arrays.asList(createMockMultipartFile(), createMockMultipartFile());
		ByteArrayResource mockMandato = createMockMultipartFile();
		setupMockitoAperturaFascicolo(clock.today(), cuaa);

		String endpoint = String.format("/%s/trasferisci", cuaa);

		MockHttpServletRequestBuilder requestBuilder =
				MockMvcRequestBuilders.multipart(ApiUrls.FASCICOLO.concat(endpoint))
				.file("contratto", mockMandato.getByteArray())
				.file("allegati", mockAllegati.get(0).getByteArray())
				.file("allegati", mockAllegati.get(1).getByteArray())
				.param("codiceFiscaleRappresentante", cuaa)
				.param("identificativoSportello", Long.toString(89L))
				.param("fascicoloOperationEnum", FascicoloOperationEnum.APRI_E_TRASFERISCI.toString())
				.contentType(MediaType.APPLICATION_JSON);

		requestBuilder.with(request -> {
			request.setMethod(HttpMethod.POST.name());
			return request;
		});
		this.mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void apertura_e_trasferimento_fascicolo_esistente_in_a4g_fail() throws Exception {
		String cuaa = "00123890223";
		userSetupAzienda(cuaa);
		List<ByteArrayResource> mockAllegati = Arrays.asList(createMockMultipartFile(), createMockMultipartFile());
		ByteArrayResource mockMandato = createMockMultipartFile();
		setupMockitoAperturaFascicolo(clock.today(), cuaa);

		ApriFascicoloDto datiApertura = new ApriFascicoloDto().setCodiceFiscale(cuaa)
				.setCodiceFiscaleRappresentante(cuaa)
				.setIdentificativoSportello(89L)
				.setContratto(mockMandato)
				.setAllegati(mockAllegati);
		fascicoloService.trasferisci(datiApertura, FascicoloOperationEnum.APRI_E_TRASFERISCI);

		String apriETrasferisciEndpoint = String.format("/%s/trasferisci", cuaa);
		MockHttpServletRequestBuilder requestBuilder =
				MockMvcRequestBuilders.multipart(ApiUrls.FASCICOLO.concat(apriETrasferisciEndpoint))
				.file("contratto", mockMandato.getByteArray())
				.file("allegati", mockAllegati.get(0).getByteArray())
				.file("allegati", mockAllegati.get(1).getByteArray())
				.param("codiceFiscaleRappresentante", cuaa)
				.param("identificativoSportello", Long.toString(89L))
				.param("fascicoloOperationEnum", FascicoloOperationEnum.APRI_E_TRASFERISCI.toString())
				.contentType(MediaType.APPLICATION_JSON);

		requestBuilder.with(request -> {
			request.setMethod(HttpMethod.POST.name());
			return request;
		});
		String errorMessage = this.mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
				.andReturn().getResponse().getErrorMessage();
		assertEquals("FASCICOLO_LOCALE_ESISTENTE", errorMessage);
	}	

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void apertura_e_trasferimento_fascicolo_esistente_in_sian_fail() throws Exception {
		userSetupCaa(89L);
		String cuaa = "00123890223";
		List<ByteArrayResource> mockAllegati = Arrays.asList(createMockMultipartFile(), createMockMultipartFile());
		ByteArrayResource mockMandato = createMockMultipartFile();
		setupMockitoAperturaFascicolo(clock.today(), cuaa);
		mockSianAppag(cuaa, true);

		String apriETrasferisciEndpoint = String.format("/%s/trasferisci", cuaa);
		MockHttpServletRequestBuilder requestBuilder =
				MockMvcRequestBuilders.multipart(ApiUrls.FASCICOLO.concat(apriETrasferisciEndpoint))
				.file("contratto", mockMandato.getByteArray())
				.file("allegati", mockAllegati.get(0).getByteArray())
				.file("allegati", mockAllegati.get(1).getByteArray())
				.param("codiceFiscaleRappresentante", cuaa)
				.param("identificativoSportello", Long.toString(89L))
				.param("fascicoloOperationEnum", FascicoloOperationEnum.APRI_E_TRASFERISCI.toString())
				.contentType(MediaType.APPLICATION_JSON);

		requestBuilder.with(request -> {
			request.setMethod(HttpMethod.POST.name());
			return request;
		});
		String errorMessage = this.mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
				.andReturn().getResponse().getErrorMessage();
		assertEquals("FASCICOLO_SIAN_ESISTENTE", errorMessage);
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void verifica_dati_trasferimento_fascicolo_persona_giuridica_ko_provincia_attuale() throws Exception {
		userSetupCaa(89L);
		String cuaa = "00123890220";
		setupMockitoAperturaFascicolo(clock.today(), cuaa);

		String url = String.format("%s/%s/verifica/dati-trasferimento", ApiUrls.FASCICOLO, cuaa);
		String errorMessage = mockMvc.perform(get(url)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn().getResponse().getErrorMessage();

		assertEquals("PROVINCIA_ATTUALE_TRENTO", errorMessage);
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void verifica_dati_trasferimento_fascicolo_persona_giuridica_ko_competenza_appag() throws Exception {
		userSetupCaa(89L);
		String cuaa = "00123890223";
		setupMockitoAperturaFascicolo(clock.today(), cuaa);
		mockSianAppag(cuaa, true);

		String url = String.format("%s/%s/verifica/dati-trasferimento", ApiUrls.FASCICOLO, cuaa);
		String errorMessage = mockMvc.perform(get(url)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn().getResponse().getErrorMessage();

		assertEquals("COMPETENZA_APPAG", errorMessage);
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void verifica_dati_trasferimento_fascicolo_ko_not_found() throws Exception {
		userSetupCaa(89L);
		String cuaa = "00123890222";
		setupMockitoAperturaFascicolo(clock.today(), cuaa);
		mockSianAppag(cuaa, true);

		String url = String.format("%s/%s/verifica/dati-trasferimento", ApiUrls.FASCICOLO, cuaa);
		String errorMessage = mockMvc.perform(get(url)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn().getResponse().getErrorMessage();

		assertEquals("COMPETENZA_APPAG", errorMessage);
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void verifica_dati_trasferimento_fascicolo_ok() throws Exception {
		userSetupCaa(89L);
		String cuaa = "00123890299";

		String url = String.format("%s/%s/verifica/dati-trasferimento", ApiUrls.FASCICOLO, cuaa);
		String errorMessage = mockMvc.perform(get(url)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn().getResponse().getErrorMessage();

		assertEquals("CUAA_NON_PRESENTE", errorMessage);
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void verifica_dati_trasferimento_chiusura_fascicolo_ok() throws Exception {
		userSetupCaa(89L);
		String cuaa = "00123890222";
		setupMockitoAperturaFascicolo(clock.today(), cuaa);
		mockSianAppag(cuaa, false);

		String url = String.format("%s/%s/verifica/dati-trasferimento-chiusura", ApiUrls.FASCICOLO, cuaa);
		mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void verifica_dati_trasferimento_chiusura_fascicolo_ko_competenza_appag() throws Exception {
		userSetupCaa(89L);
		String cuaa = "00123890222";
		setupMockitoAperturaFascicolo(clock.today(), cuaa);
		mockSianAppag(cuaa, true);

		String url = String.format("%s/%s/verifica/dati-trasferimento-chiusura", ApiUrls.FASCICOLO, cuaa);
		String errorMessage = mockMvc.perform(get(url)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn().getResponse().getErrorMessage();

		assertEquals("COMPETENZA_APPAG", errorMessage);
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTestFascicoloEsistentePersoneFisiche_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void verifica_dati_trasferimento_persona_fisica_chiusura_fascicolo_ko_competenza_appag() throws Exception {
		userSetupCaa(89L);
		String cuaa = "BLDGDN61M17L378K";
		setupMockitoAperturaFascicolo(clock.today(), cuaa);
		mockSianAppag(cuaa, true);

		String url = String.format("%s/%s/verifica/dati-trasferimento-chiusura", ApiUrls.FASCICOLO, cuaa);
		String errorMessage = mockMvc.perform(get(url)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn().getResponse().getErrorMessage();
		// CUAA_NON_PRESENTE
		assertEquals("COMPETENZA_APPAG", errorMessage);
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTestFascicoloEsistentePersoneFisiche_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void verifica_dati_trasferimento_persona_fisica_chiusura_fascicolo_ok() throws Exception {
		userSetupCaa(89L);
		String cuaa = "BLDGDN61M17L378K";
		setupMockitoAperturaFascicolo(clock.today(), cuaa);
		mockSianAppag(cuaa, false);

		String url = String.format("%s/%s/verifica/dati-trasferimento-chiusura", ApiUrls.FASCICOLO, cuaa);
		mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4gfascicolo.fascicolo.ricerca.ente"})
	@Transactional
	void annullaIterValidazione_KO_FascicoloLocaleNonEsistente() throws Exception {
		String cuaa = "00959460221";
		userSetupEnte();

		Exception exception = assertThrows(EntityNotFoundException.class, () -> {
			fascicoloService.annullaIterValidazione(cuaa);
		});
		assertEquals("FASCICOLO_LOCALE_NON_ESISTENTE", exception.getMessage());
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4gfascicolo.fascicolo.ricerca.ente"})
	@Sql(scripts = "/sql/fascicolo/fascicolo_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void annullaIterValidazione_KO_StatoFascicoloInvalido() throws Exception {
		String cuaa = "XPDNDR77B03L378Y";
		userSetupEnte();

		Exception exception = assertThrows(FascicoloInvalidConditionException.class, () -> {
			fascicoloService.annullaIterValidazione(cuaa);
		});
		assertEquals("L'attuale stato del fascicolo non permette di annullare l'iter di validazione", exception.getMessage());
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4gfascicolo.fascicolo.ricerca.ente"})
	@Sql(scripts = "/sql/fascicolo/fascicolo_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void annullaIterValidazione_OK() throws Exception {
		String cuaa = "XPDNDR77B03L378W";

		mockMvc.perform(put(String.format("%s/%s/annulla-iter-validazione", ApiUrls.FASCICOLO_PRIVATE, cuaa))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	private void mockUserSetupPersonaFisicaATPerChiusura() {
		PersonaFisicaDto personaFisicaAT = new PersonaFisicaDto();
		personaFisicaAT.setCodiceFiscale("XPDNDR77B03L378X");
		personaFisicaAT.setDeceduta(false);
		AnagraficaDto anagrafica = new AnagraficaDto();
		anagrafica.setCognome("Xepedri");
		anagrafica.setNome("Andrea");
		anagrafica.setProvinciaNascita(PROV_TRENTO);
		anagrafica.setComuneNascita("Rovereto");
		anagrafica.setDataNascita(LocalDate.of(1970, Month.JANUARY, 1));
		anagrafica.setSesso(SessoEnum.MASCHIO);
		personaFisicaAT.setAnagrafica(anagrafica);

		IndirizzoDto indirizzoDto = new IndirizzoDto();
		indirizzoDto.setProvincia(PROV_TRENTO);

		personaFisicaAT.setDomicilioFiscale(indirizzoDto);

		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagrafeTributaria(Mockito.anyString())).thenReturn(personaFisicaAT);
	}

	@Test
	@WithMockUser(username = "utente", roles = {
			"a4gfascicolo.fascicolo.ricerca.ente",
			"a4ganagrafica.fascicolo.apertura.ente",
			"a4gfascicolo.fascicolo.ricerca.tutti"
	})
	@Sql(scripts = "/sql/fascicolo/fascicolo_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void chiudi_fasciolo_persona_fisica_ko() throws Exception {
		userSetupCaa(null);
		String cuaa = "XPDNDR77B03L378X";
		mockUserSetupPersonaFisicaATPerChiusura();

		mockMvc.perform(
				put(String.format("/api/v1/fascicolo/%s/stato/chiuso", cuaa))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());

		Optional<FascicoloModel> fascicoloModel = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0);
		Optional<MovimentazioneModel> movimentazioneOpt = movimentazioneDao.findByFascicoloAndDataFineIsNullAndTipo(cuaa, TipoMovimentazioneFascicolo.CHIUSURA);

		assertNotNull(fascicoloModel);
		assertEquals(StatoFascicoloEnum.CHIUSO, fascicoloModel.get().getStato());

		assertTrue(movimentazioneOpt.isPresent());
		assertNotNull(movimentazioneOpt.get().getDataInizio());
		assertNull(movimentazioneOpt.get().getDataFine());
	}

	@Test
	@WithMockUser(username = "utente", roles = {
			"a4gfascicolo.fascicolo.ricerca.ente",
			"a4ganagrafica.fascicolo.apertura.ente",
			"a4gfascicolo.fascicolo.ricerca.tutti"
	})
	@Sql(scripts = "/sql/fascicolo/fascicolo_persona_giuridica_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void chiudi_fasciolo_persona_giuridica_ok() throws Exception {
		userSetupCaa(null);
		String cuaa = "00959460221";

		mockMvc.perform(
				put(String.format("/api/v1/fascicolo/%s/stato/chiuso", cuaa))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());

		Optional<FascicoloModel> fascicoloModel = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0);
		Optional<MovimentazioneModel> movimentazioneOpt = movimentazioneDao.findByFascicoloAndDataFineIsNullAndTipo(cuaa, TipoMovimentazioneFascicolo.CHIUSURA);

		assertNotNull(fascicoloModel);
		assertEquals(StatoFascicoloEnum.CHIUSO, fascicoloModel.get().getStato());
		assertTrue(movimentazioneOpt.isPresent());
		assertNotNull(movimentazioneOpt.get().getDataInizio());
		assertNull(movimentazioneOpt.get().getDataFine());
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTestFascicoloEsistentePersoneFisiche_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void verifica_dati_riapertura_fascicolo_persona_fisica_ko_provincia_diversa_da_trento() throws Exception {
		userSetupCaa(89L);
		String cuaa = "AAABBB00C00D000E";
		setupMockitoAperturaFascicolo(clock.today(), cuaa);
		mockSianAppag(cuaa, true);

		String url = String.format("%s/%s/verifica/dati-riapertura", ApiUrls.FASCICOLO, cuaa);
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
	void verifica_dati_riapertura_fascicolo_persona_fisica_ko_competenza_altro_op() throws Exception {
		userSetupCaa(89L);
		String cuaa = "AAABBB00C00D000E";
		setupMockitoAperturaFascicolo(clock.today(), cuaa);
		mockSianAppag(cuaa, false);

		String url = String.format("%s/%s/verifica/dati-riapertura", ApiUrls.FASCICOLO, cuaa);
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
	void verifica_dati_riapertura_fascicolo_persona_fisica_ko_deceduto() throws Exception {
		userSetupCaa(89L);
		String cuaa = "AAABBB22C22D222E";
		setupMockitoAperturaFascicolo(clock.today(), cuaa);
		mockSianAppag(cuaa, true);

		String url = String.format("%s/%s/verifica/dati-riapertura", ApiUrls.FASCICOLO, cuaa);
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
	void verifica_dati_riapertura_fascicolo_persona_fisica_ko_fasciolo_diverso_da_chiuso() throws Exception {
		userSetupCaa(89L);
		String cuaa = "AAABBB33C33D333E";
		setupMockitoAperturaFascicolo(clock.today(), cuaa);
		mockSianAppag(cuaa, true);

		String url = String.format("%s/%s/verifica/dati-riapertura", ApiUrls.FASCICOLO, cuaa);
		String errorMessage = mockMvc.perform(get(url)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn().getResponse().getErrorMessage();

		assertEquals("FASCIOLO_LOCALE_DIVERSO_DA_CHIUSO", errorMessage);
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTestFascicoloEsistentePersoneFisiche_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void verifica_dati_riapertura_fascicolo_persona_fisica_ok() throws Exception {
		userSetupCaa(89L);
		String cuaa = "AAABBB44C44D444E";
		setupMockitoAperturaFascicolo(clock.today(), cuaa);
		mockSianAppag(cuaa, true);

		String url = String.format("%s/%s/verifica/dati-riapertura", ApiUrls.FASCICOLO, cuaa);
		mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTestFascicoloEsistentePersoneGiuridiche_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void verifica_dati_riapertura_fascicolo_persona_giuridica_ko_provincia_diversa_da_trento() throws Exception {
		userSetupCaa(89L);
		String cuaa = "00123890000";
		setupMockitoAperturaFascicolo(clock.today(), cuaa);
		mockSianAppag(cuaa, true);

		String url = String.format("%s/%s/verifica/dati-riapertura", ApiUrls.FASCICOLO, cuaa);
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
	void verifica_dati_riapertura_fascicolo_persona_giuridica_ko_competenza_altro_op() throws Exception {
		userSetupCaa(89L);
		String cuaa = "00123890220";
		setupMockitoAperturaFascicolo(clock.today(), cuaa);
		mockSianAppag(cuaa, false);

		String url = String.format("%s/%s/verifica/dati-riapertura", ApiUrls.FASCICOLO, cuaa);
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
	void verifica_dati_riapertura_fascicolo_persona_giuridica_ko_fasciolo_diverso_da_chiuso() throws Exception {
		userSetupCaa(89L);
		String cuaa = "00123890220";
		setupMockitoAperturaFascicolo(clock.today(), cuaa);
		mockSianAppag(cuaa, true);

		String url = String.format("%s/%s/verifica/dati-riapertura", ApiUrls.FASCICOLO, cuaa);
		String errorMessage = mockMvc.perform(get(url)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn().getResponse().getErrorMessage();

		assertEquals("FASCIOLO_LOCALE_DIVERSO_DA_CHIUSO", errorMessage);
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTestFascicoloEsistentePersoneGiuridiche_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void verifica_dati_riapertura_fascicolo_persona_giuridica_ok() throws Exception {
		userSetupCaa(89L);
		String cuaa = "00123890111";
		setupMockitoAperturaFascicolo(clock.today(), cuaa);
		mockSianAppag(cuaa, true);

		String url = String.format("%s/%s/verifica/dati-riapertura", ApiUrls.FASCICOLO, cuaa);
		mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente", "a4gfascicolo.fascicolo.ricerca.tutti"})
	@Sql(scripts = "/sql/fascicolo/fascicolo_erede_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_erede_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void cancellaErede_OK() throws Exception {
		userSetupEnte();
		String cuaa = "XPDNDR77B03L378X";
		Long id = 31L;

		List<EredeDto> elencoEredi = fascicoloController.getEredi(cuaa);
		boolean anyMatch = elencoEredi.stream().anyMatch(mp -> mp.getId().equals(id));
		assertTrue(anyMatch);

		mockMvc.perform(delete(String.format("%s/%s/rimuovi-erede/%s", ApiUrls.FASCICOLO, cuaa, id)))
		.andExpect(status().isOk());
		//		reimposta la security; per qualche motivo dopo mockMvc.perform() viene cancellato
		SecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
		elencoEredi = fascicoloService.getEredi(cuaa);
		anyMatch = elencoEredi.stream().anyMatch(mp -> mp.getId().equals(id));
		assertFalse(anyMatch);
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/fascicolo_erede_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_erede_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void cancellaErede_IdInesistente() {
		userSetupEnte();

		String cuaa = "XPDNDR77B03L378X";
		Long id = 1L;

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			fascicoloController.rimuoviErede(cuaa, id);
		});

		assertEquals("Erede con id= [" + id +"] non presente nel db", exception.getMessage());
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	@Sql(scripts = "/sql/fascicolo/fascicolo_erede_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_erede_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void cancellaErede_IdNonAssociatoCuaa() {
		userSetupEnte();

		String cuaa = "XPDNDR77B03L378X";
		Long id = 33L;

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			fascicoloController.rimuoviErede(cuaa, id);
		});

		assertEquals("Erede con id= [" + id +"] non associato al cuaa=[" + cuaa + "]", exception.getMessage());
	}

}
