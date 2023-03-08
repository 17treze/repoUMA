package it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.AttivitaAtecoModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.CaricaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.DocumentoIdentitaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.ImportanzaAttivita;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.MandatoModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaGiuridicaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.DocumentoIdentitaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.FascicoloDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.MandatoDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.PersonaGiuridicaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaProxyClient;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.ApriFascicoloDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.FascicoloCreationResultDto;
import it.tndigitale.a4g.framework.client.custom.MetadatiDocumentoFirmatoDto;
import it.tndigitale.a4g.framework.client.custom.VerificaFirmaClient;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.StatoFascicoloEnum;
import it.tndigitale.a4g.framework.support.CustomCollectors;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.proxy.client.model.FascicoloSian;
import it.tndigitale.a4g.proxy.client.model.FascicoloSian.OrganismoPagatoreEnum;
import it.tndigitale.a4g.proxy.client.model.PersonaFisicaDto;
import it.tndigitale.a4g.proxy.client.model.PersonaGiuridicaDto;

@SpringBootTest
@WithMockUser(username = "utente")
class FascicoloPersonaGiuridicaComponentTest {

	static final String CODICE_FISCALE = "00123890220";
	static final String CODICE_FISCALE_MODIFICATO = "00123890221";
	static final String ALT_CODICE_FISCALE_ANAGRAFE_TRIBUTARIA = "11111111111";

	private static final Long IDENTIFICATIVO_SPORTELLO = 89L;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private Clock clock;

	@Autowired
	private FascicoloService fascicoloService;

	@Autowired
	private MandatoDao mandatoDao;
	
	@Autowired
	private PersonaGiuridicaDao personaGiuridicaDao;

	@MockBean
	private AnagraficaProxyClient anagraficaProxyClient;
	@MockBean
	private VerificaFirmaClient verificaFirmaClient;

	@Autowired
	private DocumentoIdentitaDao documentoIdentitaDao;
	
	@Autowired
	private FascicoloDao fascicoloDao;
	
	@Autowired
	private FascicoloComponentMethodFactory fascicoloComponentFactory;

	private void setupMockitoAperturaFascicolo(final LocalDate dataFirma) throws Exception {
		setupMockitoAperturaFascicolo(dataFirma, CODICE_FISCALE);
	}
	
	private void setupMockitoAperturaFascicoloCodiceFiscaleModificato(final LocalDate dataFirma) throws Exception {
		setupMockitoAperturaFascicoloCodiceFiscaleModificato(dataFirma, CODICE_FISCALE);
	}
	
	private void setupMockitoAperturaFascicolo(final LocalDate dataFirma, final String codiceFiscale) throws Exception {
		PersonaGiuridicaDto personaGiuridicaDtoAT =
				objectMapper.readValue(getResponseAnagrafeTributaria(codiceFiscale), PersonaGiuridicaDto.class);
		PersonaGiuridicaDto personaGiuridicaDtoParix =
				objectMapper.readValue(getResponseAnagraficaImpresa(codiceFiscale), PersonaGiuridicaDto.class);
		MetadatiDocumentoFirmatoDto infoVerificaFirma = new MetadatiDocumentoFirmatoDto();
		infoVerificaFirma.setCfFirmatario(codiceFiscale);
		infoVerificaFirma.setDataFirma(dataFirma);

		Mockito.when(anagraficaProxyClient.getPersonaGiuridicaAnagrafeTributaria(Mockito.eq(codiceFiscale))).thenReturn(personaGiuridicaDtoAT);
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagrafeTributaria(Mockito.eq("LNLMTB74A31F205S"))).thenReturn(objectMapper.readValue(getResponseAnagrafeTributaria("LNLMTB74A31F205S"), PersonaFisicaDto.class));
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagrafeTributaria(Mockito.eq("LNLMCL67R20L378W"))).thenReturn(objectMapper.readValue(getResponseAnagrafeTributaria("LNLMCL67R20L378W"), PersonaFisicaDto.class));		
		Mockito.when(anagraficaProxyClient.getPersonaGiuridicaAnagraficaImpresa(Mockito.eq(codiceFiscale), Mockito.anyString())).thenReturn(personaGiuridicaDtoParix);
		Mockito.when(verificaFirmaClient.verificaFirma(Mockito.any(), Mockito.anyString())).thenReturn(infoVerificaFirma);
	}
	
	private void setupMockitoAperturaFascicoloCodiceFiscaleModificato(final LocalDate dataFirma, final String codiceFiscale) throws Exception {
		PersonaGiuridicaDto personaGiuridicaDtoAT =
				objectMapper.readValue(getResponseAnagrafeTributaria(CODICE_FISCALE_MODIFICATO), PersonaGiuridicaDto.class);
		PersonaGiuridicaDto personaGiuridicaDtoParix =
				objectMapper.readValue(getResponseAnagraficaImpresa(codiceFiscale), PersonaGiuridicaDto.class);
		MetadatiDocumentoFirmatoDto infoVerificaFirma = new MetadatiDocumentoFirmatoDto();
		infoVerificaFirma.setCfFirmatario(codiceFiscale);
		infoVerificaFirma.setDataFirma(dataFirma);

		Mockito.when(anagraficaProxyClient.getPersonaGiuridicaAnagrafeTributaria(Mockito.eq(codiceFiscale))).thenReturn(personaGiuridicaDtoAT);
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagrafeTributaria(Mockito.eq("LNLMTB74A31F205S"))).thenReturn(objectMapper.readValue(getResponseAnagrafeTributaria("LNLMTB74A31F205S"), PersonaFisicaDto.class));
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagrafeTributaria(Mockito.eq("LNLMCL67R20L378W"))).thenReturn(objectMapper.readValue(getResponseAnagrafeTributaria("LNLMCL67R20L378W"), PersonaFisicaDto.class));		
		Mockito.when(anagraficaProxyClient.getPersonaGiuridicaAnagraficaImpresa(Mockito.eq(codiceFiscale), Mockito.anyString())).thenReturn(personaGiuridicaDtoParix);
		Mockito.when(anagraficaProxyClient.getPersonaGiuridicaAnagraficaImpresa(Mockito.eq(CODICE_FISCALE_MODIFICATO), Mockito.anyString())).thenReturn(null);
		Mockito.when(verificaFirmaClient.verificaFirma(Mockito.any(), Mockito.anyString())).thenReturn(infoVerificaFirma);
	}
	
	/**
	 * Il component ha scope Prototype: voglio verificare che NON sia un singleton
	 */
	@Test
	void istanzeDiverseComponentPersonaGiuridica() {
		FascicoloAbstractComponent<PersonaGiuridicaModel> istanzaUno = fascicoloComponentFactory.from(CODICE_FISCALE);
		FascicoloAbstractComponent<PersonaGiuridicaModel> istanzaDue = fascicoloComponentFactory.from(CODICE_FISCALE);

		assertNotEquals(istanzaUno, istanzaDue);
	}

	// Creare sportello
	// Cancellare dati
	@Test
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void apriFascicoloOK() throws Exception {
		LocalDate dataFirma = clock.today();
		setupMockitoAperturaFascicolo(dataFirma);

		// salva sul db i risultati
		callApriFascicolo();

		// verifico quanto ho salvato
		List<MandatoModel> mandati = mandatoDao.findAll();
		assertNotNull(mandati);
		assertEquals(1, mandati.size());

		MandatoModel mandatoModel = mandati.get(0);
		assertNotNull(mandatoModel);
		assertEquals(dataFirma, mandatoModel.getDataInizio());

		FascicoloModel fascicolo = mandatoModel.getFascicolo();

		PersonaGiuridicaModel personaGiuridicaModel = (PersonaGiuridicaModel) fascicolo.getPersona();

		// assert su fascicolo
		assertEquals(CODICE_FISCALE, fascicolo.getCuaa());
		assertEquals("FERRARI F.LLI LUNELLI S.P.A.", fascicolo.getDenominazione());
		assertEquals(StatoFascicoloEnum.IN_AGGIORNAMENTO, fascicolo.getStato());
		
		//Persona
		assertEquals("00123890220", personaGiuridicaModel.getCodiceFiscale());
		assertEquals("00123890220", personaGiuridicaModel.getPartitaIVA());
		assertEquals("FERRARI F.LLI LUNELLI S.P.A.", personaGiuridicaModel.getDenominazione());
		assertEquals("SOCIETA' PER AZIONI", personaGiuridicaModel.getFormaGiuridica());
		String oggSociale = "- LA PRODUZIONE, LA TRASFORMAZIONE E IL COMMERCIO DI PRODOTTI AGRICOLI EALIMENTARI IN GENERE ED IN PARTICOLARE, A SOLO TITOLO ESEMPLIFICATIVO, DI UVA,VINI, MOSTI, LIQUORI, SPUMANTI, DISTILLATI, DOLCIUMI E CAFFE' E L'ATTIVITA' DIIMPORT ED EXPORT DEI MEDESIMI PRODOTTI E LE ATTIVITA' CONNESSE;- L'ACQUISTO, LA VENDITA, LA PERMUTA E LA LOCAZIONE DI TERRENI AGRICOLI E NON;- L'ACQUISTO, LA VENDITA, LA PERMUTA, LA COSTRUZIONE E/O LA RISTRUTTURAZIONE DIBENI IMMOBILI, A SCOPO DI STABILE INVESTIMENTO CURANDONE ANCHE LA RELATIVAGESTIONE;- LA PRESTAZIONE DI SERVIZI DI ORGANIZZAZIONE AZIENDALE, AMMINISTRATIVA,COMMERCIALE E DI MARKETING NEI CONFRONTI DELLE SOCIETA' PARTECIPATE E NEICONFRONTI DELLE SOCIETA' DEL GRUPPO DI APPARTENENZA;- L'ASSUNZIONE O LA CESSIONE DI PARTECIPAZIONI IN IMPRESE, ENTI O SOCIETA',ITALIANE O ESTERE, A SCOPO DI STABILE INVESTIMENTO, PER GESTIONE E GODIMENTO,NON PROFESSIONALMENTE, NE' NEI CONFRONTI DEL PUBBLICO, BENSI' OCCASIONALMENTE EPER CONTO PROPRIO, NONCHE' COSTITUIRE O PARTECIPARE ALLA COSTITUZIONE DIASSOCIAZIONI TEMPORANEE DI IMPRESA;- LA GESTIONE DI RISTORANTI, ALBERGHI E/O COMUNQUE DI ATTIVITA' RICETTIVE;- L'ACQUISTO DI CREDITI D'IMPRESA NEI CONFRONTI DI CLIENTI, DALLA CONTROLLANTE,DA SOCIETA' CONTROLLATE E COLLEGATE E COMUNQUE DA SOCIETA' FACENTI PARTE DELLOSTESSO GRUPPO DI APPARTENENZA. IL TUTTO COME DISCIPLINATO DALLE NORME DEL CODICECIVILE.SEMPRE PER IL RAGGIUNGIMENTO DELL'OGGETTO SOCIALE, LA SOCIETA' POTRA' ALTRESI'COMPIERE OGNI OPERAZIONE COMMERCIALE, INDUSTRIALE ED IMMOBILIARE; A TALE FINEPOTRA' ALTRESI' COMPIERE IN VIA NON PREVALENTE E CON ESPRESSA ESCLUSIONE DIQUALSIASI ATTIVITA' SVOLTA NEI CONFRONTI DEL PUBBLICO, OPERAZIONI FINANZIARIE EMOBILIARI, CONCEDERE FINANZIAMENTI, ONEROSI E NON, NEI CONFRONTI DELLE SOCIETA'DEL GRUPPO DI APPARTENENZA, CONCEDERE FIDEJUSSIONI, AVALLI, CAUZIONI, GARANZIEANCHE A FAVORE DI TERZI.";
		assertEquals(22, personaGiuridicaModel.getCariche().size());
		assertEquals(1, personaGiuridicaModel.getUnitaTecnicoEconomiche().size());
		assertEquals(oggSociale, personaGiuridicaModel.getOggettoSociale());
		assertEquals(null, personaGiuridicaModel.getDataCostituzione());
		assertEquals(null, personaGiuridicaModel.getDataTermine());
		assertEquals(null, personaGiuridicaModel.getCapitaleSocialeDeliberato());
		assertEquals("LNLMTB74A31F205S", personaGiuridicaModel.getCodiceFiscaleRappresentanteLegale());
		assertEquals(LocalDate.of(1970, 11, 16),personaGiuridicaModel.getSedeLegale().getIscrizioneRegistroImprese().getDataIscrizione());
		assertEquals("TN", personaGiuridicaModel.getSedeLegale().getIscrizioneRegistroImprese().getProvinciaCameraCommercio());
		Long i = personaGiuridicaModel.getSedeLegale().getIscrizioneRegistroImprese().getNumeroRepertorioEconomicoAmministrativo();
		assertEquals(76693, i);
		assertEquals(Boolean.FALSE, personaGiuridicaModel.getSedeLegale().getIscrizioneRegistroImprese().getCessata());

		assertEquals("VIA DEL PONTE 15", personaGiuridicaModel.getSedeLegale().getIndirizzo().getDescrizioneEstesa());
		assertEquals("0461972311", personaGiuridicaModel.getSedeLegale().getTelefono());
		assertEquals(null, personaGiuridicaModel.getSedeLegale().getIndirizzo().getToponimo());		// verificare VIA
		assertEquals(null, personaGiuridicaModel.getSedeLegale().getIndirizzo().getVia());			// verificare DEL PONTE
		assertEquals(null, personaGiuridicaModel.getSedeLegale().getIndirizzo().getNumeroCivico()); // verificare 15
		assertEquals("38123", personaGiuridicaModel.getSedeLegale().getIndirizzo().getCap());
		assertEquals(null, personaGiuridicaModel.getSedeLegale().getIndirizzo().getCodiceIstat());	// verificare - 022205
		assertEquals(null, personaGiuridicaModel.getSedeLegale().getIndirizzo().getFrazione());	// verificare - RAVINA
		assertEquals("TN", personaGiuridicaModel.getSedeLegale().getIndirizzo().getProvincia());

		//Attività ATECO
		List<AttivitaAtecoModel> attivita = personaGiuridicaModel.getSedeLegale().getAttivita();
		List<String> codiciAteco = attivita.stream().map(x -> x.getCodice()).collect(Collectors.toList());
		
		assertNotNull(fascicolo.getDataApertura());
		assertTrue(codiciAteco.containsAll(Arrays.asList("0121","0126","11022","4725")));
		assertEquals(4 ,personaGiuridicaModel.getSedeLegale().getAttivita().size());
		assertEquals(ImportanzaAttivita.PRIMARIO, attivita.stream().filter(x -> x.getCodice().equals("11022")).map(x -> x.getImportanza()).findFirst().get());
		assertEquals("Produzione di vino spumante e altri vini speciali" , attivita.stream().filter(x -> x.getCodice().equals("11022")).map(x -> x.getDescrizione()).findFirst().get());
	}

	@Test
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTestFascicoloEsistentePersoneGiuridiche_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void apriFascicoloPersonaFascicoloLocaleEsistente() throws Exception {
		PersonaGiuridicaDto personaGiuridicaDto = objectMapper.readValue(getResponseAnagrafeTributaria(CODICE_FISCALE), PersonaGiuridicaDto.class);
		PersonaGiuridicaDto personaGiuridicaDtoParix = objectMapper.readValue(getResponseAnagraficaImpresa(CODICE_FISCALE), PersonaGiuridicaDto.class);
		FascicoloSian fascicoloSian = new FascicoloSian();
		fascicoloSian.setCuaa(CODICE_FISCALE);
		fascicoloSian.setOrganismoPagatore(OrganismoPagatoreEnum.APPAG);

		Mockito.when(anagraficaProxyClient.verificaEsistenzaFascicolo(Mockito.anyString())).thenReturn(fascicoloSian);
		Mockito.when(anagraficaProxyClient.getPersonaGiuridicaAnagrafeTributaria(Mockito.anyString())).thenReturn(personaGiuridicaDto);
		Mockito.when(anagraficaProxyClient.getPersonaGiuridicaAnagraficaImpresa(Mockito.anyString(), Mockito.anyString())).thenReturn(personaGiuridicaDtoParix);

		Exception exception = assertThrows(FascicoloValidazioneException.class, () -> {
			callApriFascicolo();
		});
		assertTrue(exception.getMessage().contains(FascicoloValidazioneEnum.FASCICOLO_LOCALE_ESISTENTE.name()));
	}

	@Test
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void apriFascicoloPersonaSianNonDisponibile() throws Exception {
		PersonaGiuridicaDto personaGiuridicaDto = objectMapper.readValue(getResponseAnagrafeTributaria(CODICE_FISCALE), PersonaGiuridicaDto.class);
		PersonaGiuridicaDto personaGiuridicaDtoParix = objectMapper.readValue(getResponseAnagraficaImpresa(CODICE_FISCALE), PersonaGiuridicaDto.class);

		Mockito.when(anagraficaProxyClient.verificaEsistenzaFascicolo(Mockito.anyString())).thenThrow(new RuntimeException("Sian non disponibile"));
		Mockito.when(anagraficaProxyClient.getPersonaGiuridicaAnagrafeTributaria(Mockito.anyString())).thenReturn(personaGiuridicaDto);
		Mockito.when(anagraficaProxyClient.getPersonaGiuridicaAnagraficaImpresa(Mockito.anyString(), Mockito.anyString())).thenReturn(personaGiuridicaDtoParix);

		Exception exception = assertThrows(FascicoloValidazioneException.class, () -> {
			callApriFascicolo();
		});
		assertTrue(exception.getMessage().contains(FascicoloValidazioneEnum.SIAN_NON_DISPONIBILE.name()));
	}

	@Test
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void apriFascicoloPersonaOrganismoPagatoreDiverso() throws Exception {

		PersonaGiuridicaDto personaGiuridicaDto = objectMapper.readValue(getResponseAnagrafeTributaria(CODICE_FISCALE), PersonaGiuridicaDto.class);
		PersonaGiuridicaDto personaGiuridicaDtoParix = objectMapper.readValue(getResponseAnagraficaImpresa(CODICE_FISCALE), PersonaGiuridicaDto.class);

		FascicoloSian fascicoloSian = new FascicoloSian();
		fascicoloSian.setCuaa(CODICE_FISCALE);
		fascicoloSian.setOrganismoPagatore(OrganismoPagatoreEnum.ALTRO);
		Mockito.when(anagraficaProxyClient.verificaEsistenzaFascicolo(Mockito.anyString())).thenReturn(fascicoloSian);
		Mockito.when(anagraficaProxyClient.getPersonaGiuridicaAnagrafeTributaria(Mockito.anyString())).thenReturn(personaGiuridicaDto);
		Mockito.when(anagraficaProxyClient.getPersonaGiuridicaAnagraficaImpresa(Mockito.anyString(), Mockito.anyString())).thenReturn(personaGiuridicaDtoParix);

		Exception exception = assertThrows(FascicoloValidazioneException.class, () -> {
			callApriFascicolo();
		});
		assertTrue(exception.getMessage().contains(FascicoloValidazioneEnum.COMPETENZA_ALTRO_OP.name()));
	}

	@Test
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void apriFascicoloPersonaAnagrafeTributariaNonDisponibile() throws Exception {

		PersonaGiuridicaDto personaGiuridicaDtoParix = objectMapper.readValue(getResponseAnagraficaImpresa(CODICE_FISCALE), PersonaGiuridicaDto.class);

		Mockito.when(anagraficaProxyClient.verificaEsistenzaFascicolo(Mockito.anyString())).thenReturn(null);
		Mockito.when(anagraficaProxyClient.getPersonaGiuridicaAnagrafeTributaria(Mockito.anyString())).thenThrow(new RuntimeException("Anagrafe tributaria non disponibile"));
		Mockito.when(anagraficaProxyClient.getPersonaGiuridicaAnagraficaImpresa(Mockito.anyString(), Mockito.anyString())).thenReturn(personaGiuridicaDtoParix);

		Exception exception = assertThrows(FascicoloValidazioneException.class, () -> {
			callApriFascicolo();
		});
		assertTrue(exception.getMessage().contains(FascicoloValidazioneEnum.ANAGRAFE_TRIBUTARIA_NON_DISPONIBILE.name()));
	}

	@Test
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void apriFascicoloPersonaAnagrafeTributariaCuaaNonPresente() throws Exception {

		PersonaGiuridicaDto personaGiuridicaDtoParix = objectMapper.readValue(getResponseAnagraficaImpresa(CODICE_FISCALE), PersonaGiuridicaDto.class);

		Mockito.when(anagraficaProxyClient.verificaEsistenzaFascicolo(Mockito.anyString())).thenReturn(null);
		Mockito.when(anagraficaProxyClient.getPersonaGiuridicaAnagrafeTributaria(Mockito.anyString())).thenReturn(null);
		Mockito.when(anagraficaProxyClient.getPersonaGiuridicaAnagraficaImpresa(Mockito.anyString(), Mockito.anyString())).thenReturn(personaGiuridicaDtoParix);

		Exception exception = assertThrows(FascicoloValidazioneException.class, () -> {
			callApriFascicolo();
		});
		assertTrue(exception.getMessage().contains(FascicoloValidazioneEnum.CUAA_NON_PRESENTE.name()));
	}

	@Test
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void apriFascicoloPersonaSedeNapoli() throws Exception {

		PersonaGiuridicaDto personaGiuridicaDto = objectMapper.readValue(getResponseAnagrafeTributaria(CODICE_FISCALE), PersonaGiuridicaDto.class);
		PersonaGiuridicaDto personaGiuridicaDtoParix = objectMapper.readValue(getResponseAnagraficaImpresa(CODICE_FISCALE), PersonaGiuridicaDto.class);

		personaGiuridicaDto.getSedeLegale().getIndirizzo().setProvincia("NA");

		Mockito.when(anagraficaProxyClient.verificaEsistenzaFascicolo(Mockito.anyString())).thenReturn(null);
		Mockito.when(anagraficaProxyClient.getPersonaGiuridicaAnagrafeTributaria(Mockito.anyString())).thenReturn(personaGiuridicaDto);
		Mockito.when(anagraficaProxyClient.getPersonaGiuridicaAnagraficaImpresa(Mockito.anyString(), Mockito.anyString())).thenReturn(personaGiuridicaDtoParix);

		Exception exception = assertThrows(FascicoloValidazioneException.class, () -> {
			callApriFascicolo();
		});
		assertTrue(exception.getMessage().contains(FascicoloValidazioneEnum.PROVINCIA_DIVERSA_DA_TRENTO.name()));
	}
	
	@Test
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void aggiornaFascicoloDatiPersonaOk() throws Exception {
		LocalDate dataFirma = clock.today();
		setupMockitoAperturaFascicolo(dataFirma);
		// salva sul db i risultati
		callApriFascicolo();

		// verifico quanto ho salvato
		List<MandatoModel> mandati = mandatoDao.findAll();
		MandatoModel mandatoModel = mandati.get(0);
		FascicoloModel fascicolo = mandatoModel.getFascicolo();

		PersonaGiuridicaModel personaGiuridicaModel = (PersonaGiuridicaModel) fascicolo.getPersona();
		assertEquals("FERRARI F.LLI LUNELLI S.P.A.", personaGiuridicaModel.getDenominazione());
		
		personaGiuridicaModel.setDenominazione("xyz");
		personaGiuridicaModel = personaGiuridicaDao.save(personaGiuridicaModel);
		assertEquals("xyz", personaGiuridicaModel.getDenominazione());
		// aggiorna i dati locali con le anagrafiche esterne
		FascicoloCreationResultDto aggiornaResult = fascicoloService.aggiorna(CODICE_FISCALE);
		
		assertNotEquals(null, aggiornaResult.getAnomalies());
		assertEquals(false, aggiornaResult.getAnomalies().contains(FascicoloCreationAnomalyEnum.CUAA_MISMATCH_ANAGRAFE_TRIBUTARIA_SIAN));
		
		mandati = mandatoDao.findAll();
		mandatoModel = mandati.get(0);
		fascicolo = mandatoModel.getFascicolo();
		assertEquals("FERRARI F.LLI LUNELLI S.P.A.", fascicolo.getDenominazione());
	}
	
	@Test
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void aggiornaFascicoloDatiPersonaCodiceFiscaleModificatoOk() throws Exception {
		LocalDate dataFirma = clock.today();
		setupMockitoAperturaFascicolo(dataFirma);
		// salva sul db i risultati
		callApriFascicolo();

		// verifico quanto ho salvato
		List<MandatoModel> mandati = mandatoDao.findAll();
		MandatoModel mandatoModel = mandati.get(0);
		FascicoloModel fascicolo = mandatoModel.getFascicolo();

		PersonaGiuridicaModel personaGiuridicaModel = (PersonaGiuridicaModel) fascicolo.getPersona();
		assertEquals("FERRARI F.LLI LUNELLI S.P.A.", personaGiuridicaModel.getDenominazione());
		
		personaGiuridicaModel.setDenominazione("xyz");
		personaGiuridicaModel = personaGiuridicaDao.save(personaGiuridicaModel);
		assertEquals("xyz", personaGiuridicaModel.getDenominazione());
		// aggiorna i dati locali con le anagrafiche esterne
		setupMockitoAperturaFascicoloCodiceFiscaleModificato(dataFirma);
		FascicoloCreationResultDto aggiornaResult = fascicoloService.aggiorna(CODICE_FISCALE);
		assertEquals(true, aggiornaResult.getAnomalies().contains(FascicoloCreationAnomalyEnum.CUAA_MISMATCH_ANAGRAFE_TRIBUTARIA_CAMERA_COMMERCIO));
		assertEquals(true, aggiornaResult.getAnomalies().contains(FascicoloCreationAnomalyEnum.CUAA_MISMATCH_ANAGRAFE_TRIBUTARIA_CAA));
		assertEquals(CODICE_FISCALE_MODIFICATO, aggiornaResult.getFascicoloDto().getCuaa());
	}
	
	@Test
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void aggiornaFascicoloDatiPersonaEMantieneFirmatarioPersonaFisica() throws Exception {
		LocalDate dataFirma = clock.today();
		String identificativoCaricaFirmatarioDaImpostare = "PCA";
		String codiceFiscaleFirmatarioDaImpostare = "LNLMTB74A31F205S";
		setupMockitoAperturaFascicolo(dataFirma);
		// salva sul db i risultati
		callApriFascicolo();

		// verifico quanto ho salvato
		List<MandatoModel> mandati = mandatoDao.findAll();
		MandatoModel mandatoModel = mandati.get(0);
		FascicoloModel fascicolo = mandatoModel.getFascicolo();

		PersonaGiuridicaModel personaGiuridicaModel = (PersonaGiuridicaModel) fascicolo.getPersona();
		assertEquals("FERRARI F.LLI LUNELLI S.P.A.", personaGiuridicaModel.getDenominazione());
		
		personaGiuridicaModel.setDenominazione("xyz");
//		imposta un firmatario
		Optional<CaricaModel> caricaOpt = personaGiuridicaModel.getCariche().stream().filter(carica -> carica.getIdentificativo().equals(identificativoCaricaFirmatarioDaImpostare)
				 && carica.getPersonaFisicaConCaricaModel().getCodiceFiscale().equals(codiceFiscaleFirmatarioDaImpostare) ).collect(CustomCollectors.collectOne());
		assertEquals(true, caricaOpt.isPresent());
		caricaOpt.get().setFirmatario(true);
		personaGiuridicaModel = personaGiuridicaDao.save(personaGiuridicaModel);
		
		assertEquals("xyz", personaGiuridicaModel.getDenominazione());

		
		// aggiorna i dati locali con le anagrafiche esterne
		fascicoloService.aggiorna(CODICE_FISCALE);
		
		mandati = mandatoDao.findAll();
		mandatoModel = mandati.get(0);
		fascicolo = mandatoModel.getFascicolo();
		assertEquals("FERRARI F.LLI LUNELLI S.P.A.", fascicolo.getDenominazione());
//		verifico se il firmatario impostato sopra e' stato salvato
		personaGiuridicaModel = (PersonaGiuridicaModel) fascicolo.getPersona();
		caricaOpt = personaGiuridicaModel.getCariche().stream().filter(carica -> carica.isFirmatario() == true).collect(CustomCollectors.collectOne());
		assertEquals(true, caricaOpt.isPresent());
		assertEquals(true, caricaOpt.get().getIdentificativo().equalsIgnoreCase(identificativoCaricaFirmatarioDaImpostare) && caricaOpt.get().getPersonaFisicaConCaricaModel().getCodiceFiscale().equals(codiceFiscaleFirmatarioDaImpostare));
	}
	
	@Test
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void aggiornaFascicoloDatiPersonaFisicaFirmatarioCodiceCaricaModificato() throws Exception {
		LocalDate dataFirma = clock.today();
		String identificativoCaricaFirmatarioDaImpostare = "PCA";
		String codiceFiscaleFirmatarioDaImpostare = "LNLMTB74A31F205S";
		setupMockitoAperturaFascicolo(dataFirma);
		// salva sul db i risultati
		callApriFascicolo();

		// verifico quanto ho salvato
		List<MandatoModel> mandati = mandatoDao.findAll();
		MandatoModel mandatoModel = mandati.get(0);
		FascicoloModel fascicolo = mandatoModel.getFascicolo();

		PersonaGiuridicaModel personaGiuridicaModel = (PersonaGiuridicaModel) fascicolo.getPersona();
		assertEquals("FERRARI F.LLI LUNELLI S.P.A.", personaGiuridicaModel.getDenominazione());
		
		personaGiuridicaModel.setDenominazione("xyz");
//		imposta un firmatario
		Optional<CaricaModel> caricaOpt = personaGiuridicaModel.getCariche().stream().filter(carica -> carica.getIdentificativo().equals(identificativoCaricaFirmatarioDaImpostare)
				 && carica.getPersonaFisicaConCaricaModel().getCodiceFiscale().equals(codiceFiscaleFirmatarioDaImpostare) ).collect(CustomCollectors.collectOne());
		assertEquals(true, caricaOpt.isPresent());
		CaricaModel caricaModel = caricaOpt.get();
		caricaModel.setFirmatario(true);
		caricaModel.setIdentificativo("LR2");
		personaGiuridicaModel = personaGiuridicaDao.save(personaGiuridicaModel);
		
//		imposta un documento di identita
		DocumentoIdentitaModel documentoIdentita = new DocumentoIdentitaModel();
		documentoIdentita.setCodiceFiscale(codiceFiscaleFirmatarioDaImpostare);
		documentoIdentita.setDataRilascio(LocalDate.now().minusDays(5));
		documentoIdentita.setDataScadenza(LocalDate.now().plusDays(5));
		documentoIdentita.setFascicolo(fascicolo);
		documentoIdentita.setDocumento(createMockMultipartFile().getByteArray());
		documentoIdentita = documentoIdentitaDao.save(documentoIdentita);
		fascicolo.setDocumentoIdentita(documentoIdentita);
		fascicoloDao.save(fascicolo);
//		verifica se al fascicolo e' associato un documento di identita
		mandati = mandatoDao.findAll();
		mandatoModel = mandati.get(0);
		fascicolo = mandatoModel.getFascicolo();
		assertNotNull(fascicolo.getDocumentoIdentita());
		assertNotNull(fascicolo.getDocumentoIdentita().getCodiceFiscale());
		assertEquals(codiceFiscaleFirmatarioDaImpostare, fascicolo.getDocumentoIdentita().getCodiceFiscale());
		
		assertEquals("xyz", personaGiuridicaModel.getDenominazione());

		
		// aggiorna i dati locali con le anagrafiche esterne
		fascicoloService.aggiorna(CODICE_FISCALE);
		
		mandati = mandatoDao.findAll();
		mandatoModel = mandati.get(0);
		fascicolo = mandatoModel.getFascicolo();
		assertEquals("FERRARI F.LLI LUNELLI S.P.A.", fascicolo.getDenominazione());
//		verifico se non esiste nessun firmatario
		personaGiuridicaModel = (PersonaGiuridicaModel) fascicolo.getPersona();
		caricaOpt = personaGiuridicaModel.getCariche().stream().filter(carica -> carica.isFirmatario() == true).collect(CustomCollectors.collectOne());
		assertEquals(false, caricaOpt.isPresent());
//		verifico se al fascicolo non e' associato alcun documento di identita'
		assertNull(fascicolo.getDocumentoIdentita());	
	}
	
	@Test
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void aggiornaFascicoloDatiPersonaFisicaNessunFirmatarioPrecedente() throws Exception {
		LocalDate dataFirma = clock.today();
		setupMockitoAperturaFascicolo(dataFirma);
		// salva sul db i risultati
		callApriFascicolo();

		// verifico quanto ho salvato
		List<MandatoModel> mandati = mandatoDao.findAll();
		MandatoModel mandatoModel = mandati.get(0);
		FascicoloModel fascicolo = mandatoModel.getFascicolo();

		PersonaGiuridicaModel personaGiuridicaModel = (PersonaGiuridicaModel) fascicolo.getPersona();
		assertEquals("FERRARI F.LLI LUNELLI S.P.A.", personaGiuridicaModel.getDenominazione());
		
		personaGiuridicaModel.setDenominazione("xyz");
//		imposta un firmatario
		assertEquals("xyz", personaGiuridicaModel.getDenominazione());

		
		// aggiorna i dati locali con le anagrafiche esterne
		fascicoloService.aggiorna(CODICE_FISCALE);
		
		mandati = mandatoDao.findAll();
		mandatoModel = mandati.get(0);
		fascicolo = mandatoModel.getFascicolo();
		assertEquals("FERRARI F.LLI LUNELLI S.P.A.", fascicolo.getDenominazione());
//		verifico se non esiste nessun firmatario
		personaGiuridicaModel = (PersonaGiuridicaModel) fascicolo.getPersona();
		Optional<CaricaModel> caricaOpt = personaGiuridicaModel.getCariche().stream().filter(carica -> carica.isFirmatario() == true).collect(CustomCollectors.collectOne());
		assertEquals(false, caricaOpt.isPresent());
	}
	
	@Test
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void aggiornaFascicoloDatiPersonaEMantieneFirmatarioPersonaGiuridica() throws Exception {
		LocalDate dataFirma = clock.today();
		String identificativoCaricaFirmatarioDaImpostare = "SOU";
		String codiceFiscaleFirmatarioDaImpostare = "00598990224";
		setupMockitoAperturaFascicolo(dataFirma);
		// salva sul db i risultati
		callApriFascicolo();

		// verifico quanto ho salvato
		List<MandatoModel> mandati = mandatoDao.findAll();
		MandatoModel mandatoModel = mandati.get(0);
		FascicoloModel fascicolo = mandatoModel.getFascicolo();

		PersonaGiuridicaModel personaGiuridicaModel = (PersonaGiuridicaModel) fascicolo.getPersona();
		assertEquals("FERRARI F.LLI LUNELLI S.P.A.", personaGiuridicaModel.getDenominazione());
		
		personaGiuridicaModel.setDenominazione("xyz");
//		imposta un firmatario
		Optional<CaricaModel> caricaOpt = personaGiuridicaModel.getCariche().stream().filter(carica -> carica.getIdentificativo().equals(identificativoCaricaFirmatarioDaImpostare)
				 && carica.getPersonaGiuridicaConCaricaModel().getCodiceFiscale().equals(codiceFiscaleFirmatarioDaImpostare) ).collect(CustomCollectors.collectOne());
		assertEquals(true, caricaOpt.isPresent());
		caricaOpt.get().setFirmatario(true);
		personaGiuridicaModel = personaGiuridicaDao.save(personaGiuridicaModel);
		
		assertEquals("xyz", personaGiuridicaModel.getDenominazione());

		
		// aggiorna i dati locali con le anagrafiche esterne
		fascicoloService.aggiorna(CODICE_FISCALE);
		
		mandati = mandatoDao.findAll();
		mandatoModel = mandati.get(0);
		fascicolo = mandatoModel.getFascicolo();
		assertEquals("FERRARI F.LLI LUNELLI S.P.A.", fascicolo.getDenominazione());
//		verifico se il firmatario impostato sopra e' stato salvato
		personaGiuridicaModel = (PersonaGiuridicaModel) fascicolo.getPersona();
		caricaOpt = personaGiuridicaModel.getCariche().stream().filter(carica -> carica.isFirmatario() == true).collect(CustomCollectors.collectOne());
		assertEquals(true, caricaOpt.isPresent());
		assertEquals(true, caricaOpt.get().getIdentificativo().equalsIgnoreCase(identificativoCaricaFirmatarioDaImpostare) && caricaOpt.get().getPersonaGiuridicaConCaricaModel().getCodiceFiscale().equals(codiceFiscaleFirmatarioDaImpostare));
	}

	/*
	 * caso con 1) codice fiscale anagrafe tributaria (AT) diverso da quello fornito dal CAA
	 * 2) dato presente in camera commercio (PARIX) con cuaa da AT
	 * 3) dato presente in SIAN con cuaa da AT
	 * 4) creazione fascicolo va a buon fine con dati presi da PARIX con CUAA fornito da AT
	 */
	@Test
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void apri_fascicolo_cuaa_anagrafe_tributaria_OK() throws Exception {
		LocalDate dataFirma = clock.today();
		String codiceFiscaleDaAT = ALT_CODICE_FISCALE_ANAGRAFE_TRIBUTARIA;
		setupMockitoAperturaFascicolo(dataFirma, codiceFiscaleDaAT);
		
		PersonaGiuridicaDto personaGiuridicaDto = objectMapper.readValue(getResponseAnagrafeTributaria(codiceFiscaleDaAT), PersonaGiuridicaDto.class);
		Mockito.when(anagraficaProxyClient.getPersonaGiuridicaAnagrafeTributaria(Mockito.anyString())).thenReturn(personaGiuridicaDto);
		
		FascicoloSian fascicoloSian = new FascicoloSian();
		fascicoloSian.setCuaa(codiceFiscaleDaAT);
		fascicoloSian.setOrganismoPagatore(OrganismoPagatoreEnum.APPAG);
		Mockito.when(anagraficaProxyClient.verificaEsistenzaFascicolo(Mockito.anyString())).thenReturn(fascicoloSian);

		PersonaGiuridicaDto personaGiuridicaDtoParix = objectMapper.readValue(getResponseAnagraficaImpresa(codiceFiscaleDaAT), PersonaGiuridicaDto.class);
		Mockito.when(anagraficaProxyClient.getPersonaGiuridicaAnagraficaImpresa(Mockito.anyString(), Mockito.anyString())).thenReturn(personaGiuridicaDtoParix);
		
		callApriFascicolo();
		
		List<MandatoModel> mandati = mandatoDao.findAll();
		assertNotNull(mandati);
		assertEquals(1, mandati.size());

		MandatoModel mandatoModel = mandati.get(0);
		assertNotNull(mandatoModel);
		assertEquals(dataFirma, mandatoModel.getDataInizio());
		FascicoloModel fascicolo = mandatoModel.getFascicolo();
		// assert su fascicolo
		assertEquals(codiceFiscaleDaAT, fascicolo.getCuaa());
		assertEquals("NUOVA FERRARI F.LLI LUNELLI S.P.A.", fascicolo.getDenominazione());
	}
	
	/* caso con 1) codice fiscale anagrafe tributaria diverso da cuaa
	 * 2) dato presente in camera commercio (PARIX - anagrafica impresa) con valore cuaa di AT
	 * 3) dato presente in SIAN (per verifica esistenza fasicolo) con cuaa da AT
	 * 4) cuaa da anagrafe tributaria presente su fascicolo locale (il fascicolo non va con creato)
	 */
	@Test
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTestFascicoloEsistentePersoneGiuridiche_cuaa_anagrafe_tributaria.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void apriFascicoloPersona_CuaaAnagrafeTributaria_FascicoloLocaleEsistente() throws Exception {
		LocalDate dataFirma = clock.today();
		String codiceFiscaleDaCaa = CODICE_FISCALE;
		String codiceFiscaleDaAT = ALT_CODICE_FISCALE_ANAGRAFE_TRIBUTARIA;
		setupMockitoAperturaFascicolo(dataFirma, codiceFiscaleDaAT);
		
		PersonaGiuridicaDto personaGiuridicaDto = objectMapper.readValue(getResponseAnagrafeTributaria(codiceFiscaleDaAT), PersonaGiuridicaDto.class);
		Mockito.when(anagraficaProxyClient.getPersonaGiuridicaAnagrafeTributaria(codiceFiscaleDaCaa)).thenReturn(personaGiuridicaDto);
		
		PersonaGiuridicaDto personaGiuridicaDtoParix = objectMapper.readValue(getResponseAnagraficaImpresa(codiceFiscaleDaAT), PersonaGiuridicaDto.class);
		Mockito.when(anagraficaProxyClient.getPersonaGiuridicaAnagraficaImpresa(codiceFiscaleDaAT, "TN")).thenReturn(personaGiuridicaDtoParix);
		
		FascicoloSian fascicoloSian = new FascicoloSian();
		fascicoloSian.setCuaa(codiceFiscaleDaAT);
		fascicoloSian.setOrganismoPagatore(OrganismoPagatoreEnum.APPAG);
		Mockito.when(anagraficaProxyClient.verificaEsistenzaFascicolo(codiceFiscaleDaCaa)).thenReturn(fascicoloSian);
		
		Exception exception = assertThrows(FascicoloValidazioneException.class, () -> {
			callApriFascicolo();
		});
		assertTrue(exception.getMessage().contains(FascicoloValidazioneEnum.FASCICOLO_LOCALE_ESISTENTE.name()));
	}
	
	/*
	 * caso con 1) codice fiscale anagrafe tributaria (AT) diverso da quello fornito dal CAA
	 * 2) dato presente in camera commercio (PARIX - anagrafica impresa) con cuaa da vecchio come quello fornito dal CAA
	 * 3) dato presente in SIAN (per verifica esistenza fasicolo) con cuaa da AT
	 * 4) creazione fascicolo va a buon fine con dati presi da PARIX con CUAA fornito da AT
	 */
	@Test
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void apriFascicoloPersona_CuaaAnagrafeTributaria_CuaaAnagrafeImpresaVecchio() throws Exception {
		LocalDate dataFirma = clock.today();
		String codiceFiscaleDaCaa = CODICE_FISCALE;
		String codiceFiscaleDaAT = ALT_CODICE_FISCALE_ANAGRAFE_TRIBUTARIA;
		setupMockitoAperturaFascicolo(dataFirma, codiceFiscaleDaAT);
		
		PersonaGiuridicaDto personaGiuridicaDto = objectMapper.readValue(getResponseAnagrafeTributaria(codiceFiscaleDaAT), PersonaGiuridicaDto.class);
		Mockito.when(anagraficaProxyClient.getPersonaGiuridicaAnagrafeTributaria(codiceFiscaleDaCaa)).thenReturn(personaGiuridicaDto);
		
		FascicoloSian fascicoloSian = new FascicoloSian();
		fascicoloSian.setCuaa(codiceFiscaleDaAT);
		fascicoloSian.setOrganismoPagatore(OrganismoPagatoreEnum.APPAG);
		Mockito.when(anagraficaProxyClient.verificaEsistenzaFascicolo(Mockito.anyString())).thenReturn(fascicoloSian);
		
		PersonaGiuridicaDto personaGiuridicaDtoParix = objectMapper.readValue(getResponseAnagraficaImpresa(codiceFiscaleDaCaa), PersonaGiuridicaDto.class);
		Mockito.when(anagraficaProxyClient.getPersonaGiuridicaAnagraficaImpresa(codiceFiscaleDaCaa, "TN")).thenReturn(personaGiuridicaDtoParix);
		
		callApriFascicolo();
		
		List<MandatoModel> mandati = mandatoDao.findAll();
		assertNotNull(mandati);
		assertEquals(1, mandati.size());

		MandatoModel mandatoModel = mandati.get(0);
		assertNotNull(mandatoModel);
		assertEquals(dataFirma, mandatoModel.getDataInizio());
		FascicoloModel fascicolo = mandatoModel.getFascicolo();
		// assert su fascicolo
		assertEquals(codiceFiscaleDaAT, fascicolo.getCuaa());
		assertEquals("NUOVA FERRARI F.LLI LUNELLI S.P.A.", fascicolo.getDenominazione());
	}
	
	/*
	 * caso con 1) codice fiscale anagrafe tributaria (AT) diverso da quello fornito dal CAA
	 * 2) dato presente in camera commercio (PARIX - anagrafica impresa) con cuaa da vecchio come quello fornito dal CAA
	 * 3) dato presente in SIAN (per verifica esistenza fasicolo) con cuaa da CAA
	 * 4) creazione fascicolo va a buon fine con dati presi da PARIX con CUAA fornito da AT
	 */
	@Test
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void apriFascicoloPersona_CuaaAnagrafeTributaria_CuaaAnagrafeImpresaVecchio_cuaaSianCaa() throws Exception {
		LocalDate dataFirma = clock.today();
		String codiceFiscaleDaCaa = CODICE_FISCALE;
		String codiceFiscaleDaAT = ALT_CODICE_FISCALE_ANAGRAFE_TRIBUTARIA;
		setupMockitoAperturaFascicolo(dataFirma, codiceFiscaleDaAT);
		
		PersonaGiuridicaDto personaGiuridicaDto = objectMapper.readValue(getResponseAnagrafeTributaria(codiceFiscaleDaAT), PersonaGiuridicaDto.class);
		Mockito.when(anagraficaProxyClient.getPersonaGiuridicaAnagrafeTributaria(codiceFiscaleDaCaa)).thenReturn(personaGiuridicaDto);
		
		FascicoloSian fascicoloSian = new FascicoloSian();
		fascicoloSian.setCuaa(codiceFiscaleDaCaa);
		fascicoloSian.setOrganismoPagatore(OrganismoPagatoreEnum.APPAG);
		Mockito.when(anagraficaProxyClient.verificaEsistenzaFascicolo(codiceFiscaleDaCaa)).thenReturn(fascicoloSian);
		
		PersonaGiuridicaDto personaGiuridicaDtoParix = objectMapper.readValue(getResponseAnagraficaImpresa(codiceFiscaleDaCaa), PersonaGiuridicaDto.class);
		Mockito.when(anagraficaProxyClient.getPersonaGiuridicaAnagraficaImpresa(codiceFiscaleDaCaa, "TN")).thenReturn(personaGiuridicaDtoParix);
		
		callApriFascicolo();
		
		List<MandatoModel> mandati = mandatoDao.findAll();
		assertNotNull(mandati);
		assertEquals(1, mandati.size());

		MandatoModel mandatoModel = mandati.get(0);
		assertNotNull(mandatoModel);
		assertEquals(dataFirma, mandatoModel.getDataInizio());
		FascicoloModel fascicolo = mandatoModel.getFascicolo();
		// assert su fascicolo
		assertEquals(codiceFiscaleDaAT, fascicolo.getCuaa());
		assertEquals("NUOVA FERRARI F.LLI LUNELLI S.P.A.", fascicolo.getDenominazione());
	}
	
	private FascicoloCreationResultDto callApriFascicolo() throws Exception {
		List<ByteArrayResource> mockAllegati = Arrays.asList(createMockMultipartFile(), createMockMultipartFile());
		ByteArrayResource mockMandato = createMockMultipartFile();
		ApriFascicoloDto datiDiApertura = new ApriFascicoloDto()
				.setAllegati(mockAllegati)
				.setContratto(mockMandato)
				.setCodiceFiscale(CODICE_FISCALE)
				.setCodiceFiscaleRappresentante(CODICE_FISCALE)
				.setIdentificativoSportello(IDENTIFICATIVO_SPORTELLO);
		return fascicoloService.apri(datiDiApertura);
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

	private String getResponseAnagrafeTributaria(String cf) throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/fascicolo/anagrafetributaria/".concat(cf).concat(".json")));
		return objectMapper.writeValueAsString(response);
	}

	private String getResponseAnagraficaImpresa(String cf) throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/fascicolo/anagraficaimpresa/".concat(cf).concat(".json")));
		return objectMapper.writeValueAsString(response);
	}
}
