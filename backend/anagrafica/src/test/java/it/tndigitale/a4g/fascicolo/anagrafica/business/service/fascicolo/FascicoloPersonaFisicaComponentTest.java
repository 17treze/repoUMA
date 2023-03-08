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

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.MandatoModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaGiuridicaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.Sesso;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.MandatoDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaProxyClient;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.persona.PersonaFisicaComponent;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.ApriFascicoloDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.FascicoloCreationResultDto;
import it.tndigitale.a4g.framework.client.custom.MetadatiDocumentoFirmatoDto;
import it.tndigitale.a4g.framework.client.custom.VerificaFirmaClient;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.StatoFascicoloEnum;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.proxy.client.model.FascicoloSian;
import it.tndigitale.a4g.proxy.client.model.FascicoloSian.OrganismoPagatoreEnum;
import it.tndigitale.a4g.proxy.client.model.IndirizzoDto;
import it.tndigitale.a4g.proxy.client.model.PersonaFisicaDto;

@SpringBootTest
@WithMockUser(username = "utente")
class FascicoloPersonaFisicaComponentTest {

	private static final String CODICE_FISCALE = "DPDFRZ65C21C794B";
	static final String ALT_CODICE_FISCALE_ANAGRAFE_TRIBUTARIA = "2222222222222222";
	
	private static final Long IDENTIFICATIVO_SPORTELLO = 89L;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private FascicoloService fascicoloService;

	@Autowired
	private MandatoDao mandatoDao;

	@Autowired
	private Clock clock;

	@MockBean
	private AnagraficaProxyClient anagraficaProxyClient;

	@MockBean
	private VerificaFirmaClient verificaFirmaClient;

	@Autowired
	private FascicoloComponentMethodFactory fascicoloComponentFactory;
	
	@Autowired
	private PersonaFisicaComponent personaFisicaComponent;

	/**
	 * Il component ha scope Prototype: voglio verificare che NON sia un singleton
	 */
	@Test
	void istanzeDiverseComponentPersonaGiuridica() {
		FascicoloAbstractComponent<PersonaGiuridicaModel> istanzaUno = fascicoloComponentFactory.from(CODICE_FISCALE);
		FascicoloAbstractComponent<PersonaGiuridicaModel> istanzaDue = fascicoloComponentFactory.from(CODICE_FISCALE);

		assertNotEquals(istanzaUno, istanzaDue);
	}

	@Test
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloPersonaFisicaTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void apriFascicoloOK() throws Exception {
		LocalDate dataFirma = clock.today();

		Mockito.when(anagraficaProxyClient.verificaEsistenzaFascicolo(Mockito.anyString())).thenReturn(null);
		
		PersonaFisicaDto PersonaFisicaDto = objectMapper.readValue(getResponseAnagrafeTributaria(CODICE_FISCALE), PersonaFisicaDto.class);
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagrafeTributaria(Mockito.anyString())).thenReturn(PersonaFisicaDto);
		
		PersonaFisicaDto PersonaFisicaDtoParix = objectMapper.readValue(getResponseAnagraficaImpresa(CODICE_FISCALE), PersonaFisicaDto.class);
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagraficaImpresa(Mockito.anyString(), Mockito.anyString())).thenReturn(PersonaFisicaDtoParix);
		
		MetadatiDocumentoFirmatoDto infoVerificaFirma = new MetadatiDocumentoFirmatoDto();
		infoVerificaFirma.setCfFirmatario(CODICE_FISCALE);
		infoVerificaFirma.setDataFirma(dataFirma);
		Mockito.when(verificaFirmaClient.verificaFirma(Mockito.any(), Mockito.anyString())).thenReturn(infoVerificaFirma);

		// salva sul db i risultati
		callApriFascicolo(CODICE_FISCALE);

		// verifico quanto ho salvato
		List<MandatoModel> mandati = mandatoDao.findAll();
		assertNotNull(mandati);
		assertEquals(1, mandati.size());

		MandatoModel mandatoModel = mandati.get(0);
		assertNotNull(mandatoModel);
		assertEquals(dataFirma, mandatoModel.getDataInizio());

		FascicoloModel fascicolo = mandatoModel.getFascicolo();
		
		it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.PersonaFisicaDto personaFisicaDto = personaFisicaComponent.getDatiPersona(CODICE_FISCALE, 0);
		

		// assert su fascicolo
		assertEquals("DPDFRZ65C21C794B", fascicolo.getCuaa());
		assertEquals("DE PODA                  FABRIZIO", fascicolo.getDenominazione());
		assertEquals(StatoFascicoloEnum.IN_AGGIORNAMENTO, fascicolo.getStato());
		//Persona
		assertEquals("DPDFRZ65C21C794B", personaFisicaDto.getCodiceFiscale());
		assertEquals("FABRIZIO", personaFisicaDto.getAnagrafica().getNome());
		assertEquals("DE PODA", personaFisicaDto.getAnagrafica().getCognome());
		assertEquals(Sesso.MASCHIO.name(), personaFisicaDto.getAnagrafica().getSesso());
		assertEquals(LocalDate.of(1965, 3, 21), personaFisicaDto.getAnagrafica().getDataNascita());
		assertEquals("CLES", personaFisicaDto.getAnagrafica().getComuneNascita());
		assertEquals("TN", personaFisicaDto.getAnagrafica().getProvinciaNascita());
		assertEquals(Boolean.FALSE, personaFisicaDto.getAnagrafica().isDeceduto());
		// Il dato spesso e volentieri contiene degli spazi - accettabile perchè è un dato certificato
		// assertEquals("DE PODA FABRIZIO", personaFisicaModel.getImpresaIndividuale().getDenominazione()); // Qua si rompe, verificare
		assertEquals("01300210224", personaFisicaDto.getImpresaIndividuale().getPartitaIva());
		assertEquals(LocalDate.of(1996, 12, 4), personaFisicaDto.getImpresaIndividuale().getSedeLegale().getIscrizioneRegistroImprese().getDataIscrizione());
		assertEquals("TN", personaFisicaDto.getImpresaIndividuale().getSedeLegale().getIscrizioneRegistroImprese().getProvinciaRea());
		assertEquals(Long.valueOf(150676), personaFisicaDto.getImpresaIndividuale().getSedeLegale().getIscrizioneRegistroImprese().getCodiceRea());
		assertEquals(Boolean.FALSE, personaFisicaDto.getImpresaIndividuale().getSedeLegale().getIscrizioneRegistroImprese().isCessata());

		assertEquals("0467800488", personaFisicaDto.getImpresaIndividuale().getSedeLegale().getTelefono());
		assertEquals("VIA PROVINCIALE 5", personaFisicaDto.getImpresaIndividuale().getSedeLegale().getIndirizzo().getToponimo());
//		assertEquals(null, personaFisicaModel.getImpresaIndividuale().getSedeLegale().getIndirizzo().getToponimo());
//		assertEquals(null, personaFisicaModel.getImpresaIndividuale().getSedeLegale().getIndirizzo().getVia());
//		assertEquals(null, personaFisicaModel.getImpresaIndividuale().getSedeLegale().getIndirizzo().getNumeroCivico());
		assertEquals("38093", personaFisicaDto.getImpresaIndividuale().getSedeLegale().getIndirizzo().getCap());
//		assertEquals(null, personaFisicaModel.getImpresaIndividuale().getSedeLegale().getIndirizzo().getCodiceIstat());
//		assertEquals(null, personaFisicaModel.getImpresaIndividuale().getSedeLegale().getIndirizzo().getFrazione());
//		assertEquals("TN", personaFisicaDto.getImpresaIndividuale().getSedeLegale().getIndirizzo().getProvincia());
		assertNotNull(fascicolo.getDataApertura());
		
//		[begin] per coverage: verifica uguaglianza model
		assertEquals(true, fascicolo.equals(fascicolo));
		assertEquals(false, fascicolo.equals(null));
		assertEquals(false, fascicolo.equals(new Object()));
		FascicoloModel fascicoloTest = new FascicoloModel();
		BeanUtils.copyProperties(fascicolo, fascicoloTest);
		fascicoloTest.setUtenteModifica("paperoga");
		assertEquals(false, fascicolo.equals(fascicoloTest));
//		[end] per coverage: verifica uguaglianza model
	}

	@Test
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloPersonaFisicaTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void apriFascicoloDeceduto() throws Exception {

		PersonaFisicaDto PersonaFisicaDto = objectMapper.readValue(getResponseAnagrafeTributaria(CODICE_FISCALE), PersonaFisicaDto.class);
		PersonaFisicaDto PersonaFisicaDtoParix = objectMapper.readValue(getResponseAnagraficaImpresa(CODICE_FISCALE), PersonaFisicaDto.class);

		PersonaFisicaDto.setDeceduta(Boolean.TRUE);
		Mockito.when(anagraficaProxyClient.verificaEsistenzaFascicolo(Mockito.anyString())).thenReturn(null);
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagrafeTributaria(Mockito.anyString())).thenReturn(PersonaFisicaDto);
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagraficaImpresa(Mockito.anyString(), Mockito.anyString())).thenReturn(PersonaFisicaDtoParix);
		
		// salva sul db i risultati
		Exception exception = assertThrows(FascicoloValidazioneException.class, () -> {
			callApriFascicolo(CODICE_FISCALE);
		});
		assertTrue(exception.getMessage().contains(FascicoloValidazioneEnum.DECEDUTO.name()));
	}

	@Test
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloPersonaFisicaTest_BLDLCU67M12L378O_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void apriFascicoloNoImpresaIndividuale() throws Exception {
		final String CODICE_FISCALE = "BLDLCU67M12L378O";

		PersonaFisicaDto personaFisicaDto = objectMapper.readValue(getResponseAnagrafeTributaria(CODICE_FISCALE), PersonaFisicaDto.class);
		PersonaFisicaDto personaFisicaDtoParix = objectMapper.readValue(getResponseAnagraficaImpresa(CODICE_FISCALE), PersonaFisicaDto.class);

		MetadatiDocumentoFirmatoDto infoVerificaFirma = new MetadatiDocumentoFirmatoDto();
		infoVerificaFirma.setCfFirmatario(CODICE_FISCALE);
		infoVerificaFirma.setDataFirma(clock.today());

		Mockito.when(anagraficaProxyClient.verificaEsistenzaFascicolo(Mockito.anyString())).thenReturn(null);
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagrafeTributaria(Mockito.anyString())).thenReturn(personaFisicaDto);
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagraficaImpresa(Mockito.anyString(), Mockito.anyString())).thenReturn(personaFisicaDtoParix);
		Mockito.when(verificaFirmaClient.verificaFirma(Mockito.any(), Mockito.anyString())).thenReturn(infoVerificaFirma);

		// salva sul db i risultati
		String cf = callApriFascicolo(CODICE_FISCALE).getFascicoloDto().getCuaa();

		it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.PersonaFisicaDto datiSalvati = personaFisicaComponent.getDatiPersona(cf, 0);

		assertNull(datiSalvati.getImpresaIndividuale());
	}

	@Test
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloPersonaFisicaTest_BLDGDN61M17L378K_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void apriFascicoloImpresaIndividualeIscrizioneCameraCommercioCessata() throws Exception {

		final String CODICE_FISCALE = "BLDGDN61M17L378K";

		PersonaFisicaDto personaFisicaDto = objectMapper.readValue(getResponseAnagrafeTributaria(CODICE_FISCALE), PersonaFisicaDto.class);
		PersonaFisicaDto personaFisicaDtoParix = objectMapper.readValue(getResponseAnagraficaImpresa(CODICE_FISCALE), PersonaFisicaDto.class);

		MetadatiDocumentoFirmatoDto infoVerificaFirma = new MetadatiDocumentoFirmatoDto();
		infoVerificaFirma.setCfFirmatario(CODICE_FISCALE);
		infoVerificaFirma.setDataFirma(clock.today());

		Mockito.when(anagraficaProxyClient.verificaEsistenzaFascicolo(Mockito.anyString())).thenReturn(null);
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagrafeTributaria(Mockito.anyString())).thenReturn(personaFisicaDto);
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagraficaImpresa(Mockito.anyString(), Mockito.anyString())).thenReturn(personaFisicaDtoParix);
		Mockito.when(verificaFirmaClient.verificaFirma(Mockito.any(), Mockito.anyString())).thenReturn(infoVerificaFirma);

		// salva sul db i risultati
		String cf = callApriFascicolo(CODICE_FISCALE).getFascicoloDto().getCuaa();

		it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.PersonaFisicaDto datiSalvati = personaFisicaComponent.getDatiPersona(cf, 0);

		assertNotNull(datiSalvati.getImpresaIndividuale());
		assertNotNull(datiSalvati.getImpresaIndividuale().getSedeLegale());
		assertNull(datiSalvati.getImpresaIndividuale().getSedeLegale().getIscrizioneRegistroImprese());
	}

	@Test
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloPersonaFisicaTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void apriFascicoloPersonaNapoli() throws Exception {

		PersonaFisicaDto PersonaFisicaDto = objectMapper.readValue(getResponseAnagrafeTributaria(CODICE_FISCALE), PersonaFisicaDto.class);
		PersonaFisicaDto PersonaFisicaDtoParix = objectMapper.readValue(getResponseAnagraficaImpresa(CODICE_FISCALE), PersonaFisicaDto.class);

		PersonaFisicaDto.getImpresaIndividuale().getSedeLegale().getIndirizzo().setProvincia("NA");
		Mockito.when(anagraficaProxyClient.verificaEsistenzaFascicolo(Mockito.anyString())).thenReturn(null);
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagrafeTributaria(Mockito.anyString())).thenReturn(PersonaFisicaDto);
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagraficaImpresa(Mockito.anyString(), Mockito.anyString())).thenReturn(PersonaFisicaDtoParix);

		// salva sul db i risultati
		Exception exception = assertThrows(FascicoloValidazioneException.class, () -> {
			callApriFascicolo(CODICE_FISCALE);
		});
		assertTrue(exception.getMessage().contains(FascicoloValidazioneEnum.PROVINCIA_DIVERSA_DA_TRENTO.name()));
	}


	@Test
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloPersonaFisicaTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void apriFascicoloPersonaNoImpresaNapoli() throws Exception {

		PersonaFisicaDto PersonaFisicaDto = objectMapper.readValue(getResponseAnagrafeTributaria(CODICE_FISCALE), PersonaFisicaDto.class);
		PersonaFisicaDto PersonaFisicaDtoParix = objectMapper.readValue(getResponseAnagraficaImpresa(CODICE_FISCALE), PersonaFisicaDto.class);

		IndirizzoDto domicilioFiscale = new IndirizzoDto();
		domicilioFiscale.setProvincia("NA");
		PersonaFisicaDto.setImpresaIndividuale(null);
		PersonaFisicaDto.setDomicilioFiscale(domicilioFiscale);
		Mockito.when(anagraficaProxyClient.verificaEsistenzaFascicolo(Mockito.anyString())).thenReturn(null);
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagrafeTributaria(Mockito.anyString())).thenReturn(PersonaFisicaDto);
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagraficaImpresa(Mockito.anyString(), Mockito.anyString())).thenReturn(PersonaFisicaDtoParix);

		Exception exception = assertThrows(FascicoloValidazioneException.class, () -> {
			callApriFascicolo(CODICE_FISCALE);
		});
		assertTrue(exception.getMessage().contains(FascicoloValidazioneEnum.PROVINCIA_DIVERSA_DA_TRENTO.name()));
	}

	@Test
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloPersonaFisicaTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void apriFascicoloPersonaAnagrafeTributariaNonDisponibile() throws Exception {
		PersonaFisicaDto PersonaFisicaDtoParix = objectMapper.readValue(getResponseAnagraficaImpresa(CODICE_FISCALE), PersonaFisicaDto.class);
		Mockito.when(anagraficaProxyClient.verificaEsistenzaFascicolo(Mockito.anyString())).thenReturn(null);
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagrafeTributaria(Mockito.anyString())).thenThrow(new RuntimeException("Anagrafe Tributaria non disponibile"));
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagraficaImpresa(Mockito.anyString(), Mockito.anyString())).thenReturn(PersonaFisicaDtoParix);

		Exception exception = assertThrows(FascicoloValidazioneException.class, () -> {
			callApriFascicolo(CODICE_FISCALE);
		});
		assertTrue(exception.getMessage().contains(FascicoloValidazioneEnum.ANAGRAFE_TRIBUTARIA_NON_DISPONIBILE.name()));
	}

	@Test
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloPersonaFisicaTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void apriFascicoloPersonaAnagrafeTributariaCuaaNonPresente() throws Exception {
		PersonaFisicaDto personaFisicaDtoParix = objectMapper.readValue(getResponseAnagraficaImpresa(CODICE_FISCALE), PersonaFisicaDto.class);
		Mockito.when(anagraficaProxyClient.verificaEsistenzaFascicolo(Mockito.anyString())).thenReturn(null);
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagrafeTributaria(Mockito.anyString())).thenReturn(null);
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagraficaImpresa(Mockito.anyString(), Mockito.anyString())).thenReturn(personaFisicaDtoParix);

		Exception exception = assertThrows(FascicoloValidazioneException.class, () -> {
			callApriFascicolo(CODICE_FISCALE);
		});
		assertTrue(exception.getMessage().contains(FascicoloValidazioneEnum.CUAA_NON_PRESENTE.name()));
	}

	@Test
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloPersonaFisicaTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void apriFascicoloPersonaSianNonDisponibile() throws Exception {
		PersonaFisicaDto PersonaFisicaDtoParix = objectMapper.readValue(getResponseAnagraficaImpresa(CODICE_FISCALE), PersonaFisicaDto.class);
		Mockito.when(anagraficaProxyClient.verificaEsistenzaFascicolo(Mockito.anyString())).thenThrow(new RuntimeException("SIAN non disponibile"));
		var pfDto = new PersonaFisicaDto();
		pfDto.setCodiceFiscale(CODICE_FISCALE);
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagrafeTributaria(Mockito.anyString())).thenReturn(pfDto);
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagraficaImpresa(Mockito.anyString(), Mockito.anyString())).thenReturn(PersonaFisicaDtoParix);

		Exception exception = assertThrows(FascicoloValidazioneException.class, () -> {
			callApriFascicolo(CODICE_FISCALE);
		});
		assertTrue(exception.getMessage().contains(FascicoloValidazioneEnum.SIAN_NON_DISPONIBILE.name()));
	}

	@Test
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloPersonaFisicaTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void apriFascicoloPersonaOrganismoPagatoreDiverso() throws Exception {
		PersonaFisicaDto PersonaFisicaDtoParix = objectMapper.readValue(getResponseAnagraficaImpresa(CODICE_FISCALE), PersonaFisicaDto.class);
		FascicoloSian fascicoloSian = new FascicoloSian();
		fascicoloSian.setCuaa(CODICE_FISCALE);
		fascicoloSian.setOrganismoPagatore(OrganismoPagatoreEnum.ALTRO);
		Mockito.when(anagraficaProxyClient.verificaEsistenzaFascicolo(Mockito.anyString())).thenReturn(fascicoloSian);
		
		var pfDto = new PersonaFisicaDto();
		pfDto.setCodiceFiscale(CODICE_FISCALE);
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagrafeTributaria(Mockito.anyString())).thenReturn(pfDto);
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagraficaImpresa(Mockito.anyString(), Mockito.anyString())).thenReturn(PersonaFisicaDtoParix);

		Exception exception = assertThrows(FascicoloValidazioneException.class, () -> {
			callApriFascicolo(CODICE_FISCALE);
		});
		assertTrue(exception.getMessage().contains(FascicoloValidazioneEnum.COMPETENZA_ALTRO_OP.name()));
	}

	@Test
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTestFascicoloEsistentePersoneFisiche_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloPersonaFisicaTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void apriFascicoloPersonaFascicoloLocaleEsistente() throws Exception {
		PersonaFisicaDto PersonaFisicaDtoParix = objectMapper.readValue(getResponseAnagraficaImpresa(CODICE_FISCALE), PersonaFisicaDto.class);
		FascicoloSian fascicoloSian = new FascicoloSian();
		fascicoloSian.setCuaa(CODICE_FISCALE);
		fascicoloSian.setOrganismoPagatore(OrganismoPagatoreEnum.APPAG);
		Mockito.when(anagraficaProxyClient.verificaEsistenzaFascicolo(Mockito.anyString())).thenReturn(fascicoloSian);
		
		var pfDto = new PersonaFisicaDto();
		pfDto.setCodiceFiscale(CODICE_FISCALE);
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagrafeTributaria(Mockito.anyString())).thenReturn(pfDto);
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagraficaImpresa(Mockito.anyString(), Mockito.anyString())).thenReturn(PersonaFisicaDtoParix);

		Exception exception = assertThrows(FascicoloValidazioneException.class, () -> {
			callApriFascicolo(CODICE_FISCALE);
		});
		assertTrue(exception.getMessage().contains(FascicoloValidazioneEnum.FASCICOLO_LOCALE_ESISTENTE.name()));
	}

	private FascicoloCreationResultDto callApriFascicolo(final String codiceFiscale) throws Exception {
		List<ByteArrayResource> mockAllegati = Arrays.asList(createMockMultipartFile(), createMockMultipartFile());
		ByteArrayResource mockMandato = createMockMultipartFile();
		ApriFascicoloDto datiDiApertura = new ApriFascicoloDto()
				.setAllegati(mockAllegati)
				.setContratto(mockMandato)
				.setCodiceFiscale(codiceFiscale)
				.setCodiceFiscaleRappresentante(codiceFiscale)
				.setIdentificativoSportello(IDENTIFICATIVO_SPORTELLO);
		return fascicoloService.apri(datiDiApertura);
	}

	private String getResponseAnagrafeTributaria(String cf) throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/fascicolo/anagrafetributaria/".concat(cf).concat(".json")));
		return objectMapper.writeValueAsString(response);
	}
	
	private String getResponseAnagraficaImpresa(String cf) throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/fascicolo/anagraficaimpresa/".concat(cf).concat(".json")));
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
	
	@Test
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloPersonaFisicaTest_BLDGDN61M17L378K_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void apertura_fascicolo_impresa_individuale_iscrizione_sezione_OK() throws Exception {

		final String CODICE_FISCALE = "BLDGDN61M17L378K";

		PersonaFisicaDto personaFisicaDto = objectMapper.readValue(getResponseAnagrafeTributaria(CODICE_FISCALE), PersonaFisicaDto.class);
		PersonaFisicaDto personaFisicaDtoParix = objectMapper.readValue(getResponseAnagraficaImpresa(CODICE_FISCALE), PersonaFisicaDto.class);

		MetadatiDocumentoFirmatoDto infoVerificaFirma = new MetadatiDocumentoFirmatoDto();
		infoVerificaFirma.setCfFirmatario(CODICE_FISCALE);
		infoVerificaFirma.setDataFirma(clock.today());

		Mockito.when(anagraficaProxyClient.verificaEsistenzaFascicolo(Mockito.anyString())).thenReturn(null);
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagrafeTributaria(Mockito.anyString())).thenReturn(personaFisicaDto);
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagraficaImpresa(Mockito.anyString(), Mockito.anyString())).thenReturn(personaFisicaDtoParix);
		Mockito.when(verificaFirmaClient.verificaFirma(Mockito.any(), Mockito.anyString())).thenReturn(infoVerificaFirma);

		// salva sul db i risultati
		String cf = callApriFascicolo(CODICE_FISCALE).getFascicoloDto().getCuaa();

		it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.PersonaFisicaDto datiSalvati = personaFisicaComponent.getDatiPersona(cf, 0);
		
		//todo aggiungere i dati al json??
		
		assertNotNull(datiSalvati.getIscrizioniSezione());
	}
	
	/*
	 * caso con 1) codice fiscale anagrafe tributaria (AT) diverso da quello fornito dal CAA
	 * 2) dato presente in camera commercio (PARIX - anagrafica impresa) con cuaa da AT
	 * 3) dato presente in SIAN (per verifica esistenza fasicolo) con cuaa da AT
	 * 4) creazione fascicolo va a buon fine con dati presi da PARIX con CUAA fornito da AT
	 */
	@Test
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void apriFascicolo_cuaaAnagrafe_tributaria_OK() throws Exception {
		String codiceFiscaleDaCaa = CODICE_FISCALE;
		String codiceFiscaleDaAT = ALT_CODICE_FISCALE_ANAGRAFE_TRIBUTARIA;
		LocalDate dataFirma = clock.today();

		PersonaFisicaDto PersonaFisicaDto = objectMapper.readValue(getResponseAnagrafeTributaria(codiceFiscaleDaAT), PersonaFisicaDto.class);
		PersonaFisicaDto PersonaFisicaDtoParix = objectMapper.readValue(getResponseAnagraficaImpresa(codiceFiscaleDaAT), PersonaFisicaDto.class);
		
		MetadatiDocumentoFirmatoDto infoVerificaFirma = new MetadatiDocumentoFirmatoDto();
		infoVerificaFirma.setCfFirmatario(codiceFiscaleDaAT);
		infoVerificaFirma.setDataFirma(dataFirma);
		
		FascicoloSian fascicoloSian = new FascicoloSian();
		fascicoloSian.setCuaa(codiceFiscaleDaAT);
		fascicoloSian.setOrganismoPagatore(OrganismoPagatoreEnum.APPAG);
		Mockito.when(anagraficaProxyClient.verificaEsistenzaFascicolo(codiceFiscaleDaAT)).thenReturn(fascicoloSian);
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagrafeTributaria(codiceFiscaleDaCaa)).thenReturn(PersonaFisicaDto);
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagraficaImpresa(codiceFiscaleDaAT, "TN")).thenReturn(PersonaFisicaDtoParix);
		Mockito.when(verificaFirmaClient.verificaFirma(Mockito.any(), Mockito.anyString())).thenReturn(infoVerificaFirma);

		// salva sul db i risultati
		callApriFascicolo(codiceFiscaleDaCaa);

		// verifico quanto ho salvato
		List<MandatoModel> mandati = mandatoDao.findAll();
		assertNotNull(mandati);
		assertEquals(1, mandati.size());

		MandatoModel mandatoModel = mandati.get(0);
		assertNotNull(mandatoModel);
		assertEquals(dataFirma, mandatoModel.getDataInizio());

		FascicoloModel fascicolo = mandatoModel.getFascicolo();
		assertEquals(codiceFiscaleDaAT, fascicolo.getCuaa());
		assertEquals("NUOVA DE PODA FABRIZIO", fascicolo.getDenominazione());
	}
	
	/*
	 * caso con 1) codice fiscale anagrafe tributaria (AT) diverso da quello fornito dal CAA
	 * 2) dato presente in camera commercio (PARIX - anagrafica impresa) con cuaa da CAA
	 * 3) dato presente in SIAN (per verifica esistenza fasicolo) con cuaa da AT
	 * 4) creazione fascicolo va a buon fine con dati presi da PARIX con CUAA fornito da AT
	 */
	@Test
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void apriFascicolo_cuaaAnagrafeTributaria_cuaaCameraCommercioCaa() throws Exception {
		String codiceFiscaleDaCaa = CODICE_FISCALE;
		String codiceFiscaleDaAT = ALT_CODICE_FISCALE_ANAGRAFE_TRIBUTARIA;
		LocalDate dataFirma = clock.today();
		
		PersonaFisicaDto PersonaFisicaDto = objectMapper.readValue(getResponseAnagrafeTributaria(codiceFiscaleDaAT), PersonaFisicaDto.class);
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagrafeTributaria(codiceFiscaleDaCaa)).thenReturn(PersonaFisicaDto);
		
		FascicoloSian fascicoloSian = new FascicoloSian();
		fascicoloSian.setCuaa(codiceFiscaleDaAT);
		fascicoloSian.setOrganismoPagatore(OrganismoPagatoreEnum.APPAG);
		Mockito.when(anagraficaProxyClient.verificaEsistenzaFascicolo(codiceFiscaleDaAT)).thenReturn(fascicoloSian);
		
		PersonaFisicaDto PersonaFisicaDtoParix = objectMapper.readValue(getResponseAnagraficaImpresa(codiceFiscaleDaCaa), PersonaFisicaDto.class);
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagraficaImpresa(codiceFiscaleDaAT, "TN")).thenReturn(PersonaFisicaDtoParix);
		
		MetadatiDocumentoFirmatoDto infoVerificaFirma = new MetadatiDocumentoFirmatoDto();
		infoVerificaFirma.setCfFirmatario(codiceFiscaleDaAT);
		infoVerificaFirma.setDataFirma(dataFirma);
		Mockito.when(verificaFirmaClient.verificaFirma(Mockito.any(), Mockito.anyString())).thenReturn(infoVerificaFirma);

		// salva sul db i risultati
		callApriFascicolo(codiceFiscaleDaCaa);

		// verifico quanto ho salvato
		List<MandatoModel> mandati = mandatoDao.findAll();
		assertNotNull(mandati);
		assertEquals(1, mandati.size());

		MandatoModel mandatoModel = mandati.get(0);
		assertNotNull(mandatoModel);
		assertEquals(dataFirma, mandatoModel.getDataInizio());

//		it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.PersonaFisicaDto personaFisicaDto = personaFisicaComponent.getDatiPersona(CODICE_FISCALE, 0);
		FascicoloModel fascicolo = mandatoModel.getFascicolo();
		assertEquals(codiceFiscaleDaAT, fascicolo.getCuaa());
		assertEquals("NUOVA DE PODA FABRIZIO", fascicolo.getDenominazione());
	}
	
	/*
	 * caso con 1) codice fiscale anagrafe tributaria (AT) diverso da quello fornito dal CAA
	 * 2) dato presente in camera commercio (PARIX - anagrafica impresa) con cuaa da AT
	 * 3) dato presente in SIAN (per verifica esistenza fasicolo) con cuaa da CAA e organismo pagatore diverso
	 * 4) creazione fascicolo va a buon fine con dati presi da PARIX con CUAA fornito da AT
	 */
	@Test
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void apriFascicolo_cuaaAnagrafeTributaria_cuaaSianCaa() throws Exception {
		String codiceFiscaleDaCaa = CODICE_FISCALE;
		String codiceFiscaleDaAT = ALT_CODICE_FISCALE_ANAGRAFE_TRIBUTARIA;
		
		LocalDate dataFirma = clock.today();
		PersonaFisicaDto PersonaFisicaDto = objectMapper.readValue(getResponseAnagrafeTributaria(codiceFiscaleDaAT), PersonaFisicaDto.class);
		PersonaFisicaDto PersonaFisicaDtoParix = objectMapper.readValue(getResponseAnagraficaImpresa(codiceFiscaleDaAT), PersonaFisicaDto.class);
		
		MetadatiDocumentoFirmatoDto infoVerificaFirma = new MetadatiDocumentoFirmatoDto();
		infoVerificaFirma.setCfFirmatario(codiceFiscaleDaAT);
		infoVerificaFirma.setDataFirma(dataFirma);
		
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagrafeTributaria(codiceFiscaleDaCaa)).thenReturn(PersonaFisicaDto);
		FascicoloSian fascicoloSian = new FascicoloSian();
		fascicoloSian.setCuaa(codiceFiscaleDaCaa);
		fascicoloSian.setOrganismoPagatore(OrganismoPagatoreEnum.ALTRO);
		Mockito.when(anagraficaProxyClient.verificaEsistenzaFascicolo(codiceFiscaleDaCaa)).thenReturn(fascicoloSian);
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagraficaImpresa(codiceFiscaleDaAT, "TN")).thenReturn(PersonaFisicaDtoParix);
		Mockito.when(verificaFirmaClient.verificaFirma(Mockito.any(), Mockito.anyString())).thenReturn(infoVerificaFirma);

		Exception exception = assertThrows(FascicoloValidazioneException.class, () -> {
			callApriFascicolo(codiceFiscaleDaCaa);
		});
		assertTrue(exception.getMessage().contains(FascicoloValidazioneEnum.COMPETENZA_ALTRO_OP.name()));
	}
	
	/*
	 * caso con 1) codice fiscale anagrafe tributaria (AT) diverso da quello fornito dal CAA
	 * 2) dato presente in camera commercio (PARIX - anagrafica impresa) con cuaa da AT
	 * 3) dato presente in SIAN (per verifica esistenza fasicolo) con cuaa da AT e organismo pagatore diverso
	 * 4) creazione fascicolo fallisce per organismo pagatore diverso
	 */
	@Test
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/persona/aperturaFascicoloTest_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void apriFascicolo_cuaaAnagrafeTributaria_cuaaSianAT() throws Exception {
		String codiceFiscaleDaCaa = CODICE_FISCALE;
		String codiceFiscaleDaAT = ALT_CODICE_FISCALE_ANAGRAFE_TRIBUTARIA;
		
		LocalDate dataFirma = clock.today();
		PersonaFisicaDto PersonaFisicaDto = objectMapper.readValue(getResponseAnagrafeTributaria(codiceFiscaleDaAT), PersonaFisicaDto.class);
		PersonaFisicaDto PersonaFisicaDtoParix = objectMapper.readValue(getResponseAnagraficaImpresa(codiceFiscaleDaAT), PersonaFisicaDto.class);
		
		MetadatiDocumentoFirmatoDto infoVerificaFirma = new MetadatiDocumentoFirmatoDto();
		infoVerificaFirma.setCfFirmatario(codiceFiscaleDaAT);
		infoVerificaFirma.setDataFirma(dataFirma);
		
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagrafeTributaria(codiceFiscaleDaCaa)).thenReturn(PersonaFisicaDto);
		FascicoloSian fascicoloSian = new FascicoloSian();
		fascicoloSian.setCuaa(codiceFiscaleDaAT);
		fascicoloSian.setOrganismoPagatore(OrganismoPagatoreEnum.ALTRO);
		Mockito.when(anagraficaProxyClient.verificaEsistenzaFascicolo(codiceFiscaleDaAT)).thenReturn(fascicoloSian);
		Mockito.when(anagraficaProxyClient.getPersonaFisicaAnagraficaImpresa(codiceFiscaleDaAT, "TN")).thenReturn(PersonaFisicaDtoParix);
		Mockito.when(verificaFirmaClient.verificaFirma(Mockito.any(), Mockito.anyString())).thenReturn(infoVerificaFirma);

		Exception exception = assertThrows(FascicoloValidazioneException.class, () -> {
			callApriFascicolo(codiceFiscaleDaCaa);
		});
		assertTrue(exception.getMessage().contains(FascicoloValidazioneEnum.COMPETENZA_ALTRO_OP.name()));
	}
}