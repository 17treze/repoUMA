package it.tndigitale.a4g.uma.business.service.richiesta;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.uma.business.persistence.entity.StatoDichiarazioneConsumi;
import it.tndigitale.a4g.uma.business.persistence.entity.StatoRichiestaCarburante;
import it.tndigitale.a4g.uma.business.service.client.UmaAnagraficaClient;
import it.tndigitale.a4g.uma.business.service.client.UmaDotazioneTecnicaClient;
import it.tndigitale.a4g.uma.business.service.client.UmaUtenteClient;
import it.tndigitale.a4g.uma.business.service.consumi.RicercaDichiarazioneConsumiService;
import it.tndigitale.a4g.uma.business.service.utente.AbilitazioniComponent;
import it.tndigitale.a4g.uma.dto.consumi.DichiarazioneConsumiDto;
import it.tndigitale.a4g.uma.dto.richiesta.PresentaRichiestaDto;
import it.tndigitale.a4g.uma.dto.richiesta.RichiestaCarburanteDto;
//import it.tndigitale.a4g.utente.client.model.Utente;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente")
class RichiestaCarburanteValidatorTest {

	@SpyBean
	private RichiestaCarburanteService richiestaCarburanteService;
	@MockBean
	private UmaAnagraficaClient anagraficaClient;
	@MockBean
	private UmaDotazioneTecnicaClient dotazioneTecnicaClient;
	@MockBean
	private UmaUtenteClient umaUtenteClient;
	@MockBean
	private AbilitazioniComponent abilitazioniComponent;
	@MockBean
	private Clock clock;
	@SpyBean
	private RicercaRichiestaCarburanteService ricercaRichiestaCarburanteService;
	@SpyBean
	private RicercaDichiarazioneConsumiService ricercaDichiarazioneConsumiService;
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	
	private static final String UMA_01_01_BR1_ERR_MSG = "Solamente il titolare o il rappresentante legale di un'azienda agricola è autorizzato alla presentazione della domanda UMA on line";
	private static final String UMA_01_01_BR2_ERR_MSG = "L'azienda non dispone di un fascicolo valido. E’ necessario creare/aggiornare il fascicolo";	
	private static final String UMA_01_01_BR3_ERR_MSG = "L'azienda non risulta iscritta al registro delle imprese della Camera di Commercio oppure non risulta esente dall'iscrizione. E’ necessario verificare tale posizione";
	private static final String UMA_01_01_BR4_ERR_MSG = "L'azienda non dispone di macchinari.  E’ necessario aggiornare il fascicolo aziendale";
	private static final String UMA_01_02_BR1_ERR_MSG = "L'azienda ha già una domanda UMA in compilazione.";
	private static final String UMA_CONTROLLO_DICHIARAZIONE_CONSUMI = "L'azienda ha già una dichiarazione consumi protocollata";
	private static final String UMA_08_BR1_ERR_MSG = UMA_01_01_BR2_ERR_MSG + ", eliminare l’attuale domanda e poi presentare una nuova Richiesta di carburante.";
	private static final String TASK_UMA_46_ERR_NO_EREDI =  "Il campo data decesso risulta valorizzato. E' necessario inserire nel fascicolo un erede.";
	private static final String TASK_UMA_46_ERR_CREAZIONE_RICHIESTA =  "Il campo data decesso risulta valorizzato. Il sistema non permette l'inserimento di una richiesta di carburante.";


	private void mockUtenteConnessoPresentaRichiesta(String cuaa, String codiceFiscaleRichiedente) throws Exception {
		Mockito.when(abilitazioniComponent.checkPresentaDomandaUma(cuaa)).thenReturn(true);
//		Utente utente = new Utente();
//		utente.setCodiceFiscale(codiceFiscaleRichiedente);
//		utente.setIdentificativo(codiceFiscaleRichiedente);
//		Mockito.when(umaUtenteClient.getUtenteConnesso()).thenReturn(utente);
		Mockito.when(clock.now()).thenReturn(LocalDateTime.of(2020, Month.JANUARY, 1, 1, 1));
		Mockito.when(clock.today()).thenReturn(LocalDate.of(2020, Month.JANUARY, 1));
	}

	private void mockUtenteConnessoValida(Long idRichiesta) throws Exception {
		Mockito.when(abilitazioniComponent.checkModificaRichiestaCarburante(idRichiesta)).thenReturn(true);
	}

	/*
	@Test
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void validaPresentazioneDomandaSuccessful() throws Exception {

		String cuaa = "MSTFBA79L10H612L";
		String codiceFiscaleRichiedente = "MSTFBA79L10H612L";

		mockUtenteConnessoPresentaRichiesta(cuaa,codiceFiscaleRichiedente);
		List<CaricaAgsDto> mockPersone = new ArrayList<CaricaAgsDto>();
		mockPersone.add(new CaricaAgsDto());
		Mockito.when(anagraficaClient.getTitolariRappresentantiLegali(cuaa)).thenReturn(mockPersone);

		FascicoloAgsDto isf = new FascicoloAgsDto();
		isf.setCuaa(cuaa);
		isf.iscrittoSezioneSpecialeAgricola(true);
		isf.setNonIscrittoSezioneSpecialeAgricola(false);
		isf.setStato(StatoEnum.VALIDO);
		isf.setDataValidazione(clock.now());
		DetenzioneAgsDto mandato = new DetenzioneAgsDto();
		mandato.setTipoDetenzione(TipoDetenzioneEnum.MANDATO);
		mandato.setSportello("sportello mandato");
		List<DetenzioneAgsDto> detList = new ArrayList<>();
		detList.add(mandato);
		isf.setDetenzioni(detList);
		Mockito.when(anagraficaClient.getFascicolo(cuaa)).thenReturn(isf);
		List<MacchinaAgsDto> macchine = new ArrayList<>();
		MacchinaAgsDto macchinaAgsDto = new MacchinaAgsDto();
		macchinaAgsDto.setAlimentazione(AlimentazioneEnum.BENZINA);
		macchine.add(macchinaAgsDto);
		Mockito.when(dotazioneTecnicaClient.getMacchine(Mockito.eq(cuaa), Mockito.any())).thenReturn(macchine);

		List<FabbricatoAgsDto> fabbricati = new ArrayList<>();
		FabbricatoAgsDto fabbricato1 = new FabbricatoAgsDto();
		fabbricato1.setTipoFabbricato("CANTINA");
		fabbricato1.setTipoFabbricatoCodice("000011"); // cantine
		fabbricato1.setIdAgs(1541L);
		fabbricati.add(fabbricato1);
		Mockito.when(dotazioneTecnicaClient.getFabbricati(Mockito.eq(cuaa), Mockito.any())).thenReturn(fabbricati);

		doReturn(new RisultatiPaginati<>()).when(ricercaRichiestaCarburanteService).getRichiestePaged(Mockito.any());

		PresentaRichiestaDto presentaRichiestaDto = new PresentaRichiestaDto()
				.setCodiceFiscaleRichiedente(codiceFiscaleRichiedente)
				.setCuaa(cuaa);
		String response = mockMvc.perform(post("/api/v1/richieste").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(presentaRichiestaDto)))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		Long idDomandaUma = objectMapper.readValue(response, Long.class);
		assertNotNull(idDomandaUma);
	} 

	@Test
	void validaPresentazioneDomandaIscrizioniSpecialiAssenti() throws Exception {

		String cuaa = "MSTFBA79L10H612L";
		String codiceFiscaleRichiedente = "MSTFBA79L10H612L";

		mockUtenteConnessoPresentaRichiesta(cuaa,codiceFiscaleRichiedente);

		List<CaricaAgsDto> mockPersone = new ArrayList<CaricaAgsDto>();
		mockPersone.add(new CaricaAgsDto());
		Mockito.when(anagraficaClient.getTitolariRappresentantiLegali(cuaa)).thenReturn(mockPersone);

		FascicoloAgsDto isf = new FascicoloAgsDto();
		isf.setCuaa(cuaa);
		isf.iscrittoSezioneSpecialeAgricola(false);
		isf.setNonIscrittoSezioneSpecialeAgricola(false);
		isf.setStato(StatoEnum.VALIDO);
		isf.setDataValidazione(clock.now());
		Mockito.when(anagraficaClient.getFascicolo(cuaa)).thenReturn(isf);

		List<MacchinaAgsDto> macchine = new ArrayList<>();
		macchine.add(new MacchinaAgsDto());
		Mockito.when(dotazioneTecnicaClient.getMacchine(Mockito.eq(cuaa), Mockito.any())).thenReturn(macchine);

		PresentaRichiestaDto presentaRichiestaDto = new PresentaRichiestaDto()
				.setCodiceFiscaleRichiedente(codiceFiscaleRichiedente)
				.setCuaa(cuaa);
		Exception e = Assertions.assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(post("/api/v1/richieste").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(presentaRichiestaDto)));
		});

		assertTrue(e.getMessage().contains(UMA_01_01_BR3_ERR_MSG));	
	} 

	@Test
	void validaPresentazioneDomandaFascicoloNonValido() throws Exception {

		String cuaa = "MSTFBA79L10H612L";
		String codiceFiscaleRichiedente = "MSTFBA79L10H612L";

		mockUtenteConnessoPresentaRichiesta(cuaa,codiceFiscaleRichiedente);

		List<CaricaAgsDto> mockPersone = new ArrayList<CaricaAgsDto>();
		mockPersone.add(new CaricaAgsDto());
		Mockito.when(anagraficaClient.getTitolariRappresentantiLegali(cuaa)).thenReturn(mockPersone);

		FascicoloAgsDto fascicolo = new FascicoloAgsDto().cuaa(cuaa);
		Mockito.when(anagraficaClient.getFascicolo(cuaa)).thenReturn(fascicolo);

		List<MacchinaAgsDto> macchine = new ArrayList<>();
		macchine.add(new MacchinaAgsDto());
		Mockito.when(dotazioneTecnicaClient.getMacchine(Mockito.eq(cuaa),Mockito.any())).thenReturn(macchine);

		PresentaRichiestaDto presentaRichiestaDto = new PresentaRichiestaDto()
				.setCodiceFiscaleRichiedente(codiceFiscaleRichiedente)
				.setCuaa(cuaa);
		Exception e = Assertions.assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(post("/api/v1/richieste").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(presentaRichiestaDto)));
		});

		assertTrue(e.getMessage().contains(UMA_01_01_BR2_ERR_MSG));	
	}

	@Test
	void validaPresentazioneDomandaNoMacchine() throws Exception {
		String cuaa = "MSTFBA79L10H612L";
		String codiceFiscaleRichiedente = "MSTFBA79L10H612L";

		mockUtenteConnessoPresentaRichiesta(cuaa,codiceFiscaleRichiedente);

		List<CaricaAgsDto> mockPersone = new ArrayList<CaricaAgsDto>();
		mockPersone.add(new CaricaAgsDto());
		Mockito.when(anagraficaClient.getTitolariRappresentantiLegali(cuaa)).thenReturn(mockPersone);

		FascicoloAgsDto isf = new FascicoloAgsDto();
		isf.setCuaa(cuaa);
		isf.iscrittoSezioneSpecialeAgricola(false);
		isf.setNonIscrittoSezioneSpecialeAgricola(true);
		isf.setStato(StatoEnum.VALIDO);
		isf.setDataValidazione(clock.now());
		Mockito.when(anagraficaClient.getFascicolo(cuaa)).thenReturn(isf);

		Mockito.when(dotazioneTecnicaClient.getMacchine(Mockito.eq(cuaa), Mockito.any())).thenReturn(new ArrayList<>());

		PresentaRichiestaDto presentaRichiestaDto = new PresentaRichiestaDto()
				.setCodiceFiscaleRichiedente(codiceFiscaleRichiedente)
				.setCuaa(cuaa);
		Exception e = Assertions.assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(post("/api/v1/richieste").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(presentaRichiestaDto)));
		});

		assertTrue(e.getMessage().contains(UMA_01_01_BR4_ERR_MSG));	
	} 

	@Test
	void validaPresentazioneDomandaNoTitolariRappresentantiLegali() throws Exception {
		String cuaa = "MSTFBA79L10H612L";
		String codiceFiscaleRichiedente = "MSTFBA79L10H612L";

		mockUtenteConnessoPresentaRichiesta(cuaa,codiceFiscaleRichiedente);

		Mockito.when(anagraficaClient.getTitolariRappresentantiLegali(cuaa)).thenReturn(new ArrayList<>());

		FascicoloAgsDto isf = new FascicoloAgsDto();
		isf.setCuaa(cuaa);
		isf.iscrittoSezioneSpecialeAgricola(true);
		isf.setNonIscrittoSezioneSpecialeAgricola(false);
		isf.setStato(StatoEnum.VALIDO);
		isf.setDataValidazione(LocalDateTime.of(2020, 2, 1, 0, 0));
		Mockito.when(anagraficaClient.getFascicolo(cuaa)).thenReturn(isf);

		List<MacchinaAgsDto> macchine = new ArrayList<>();
		macchine.add(new MacchinaAgsDto());
		Mockito.when(dotazioneTecnicaClient.getMacchine(Mockito.eq(cuaa), Mockito.any())).thenReturn(macchine);

		PresentaRichiestaDto presentaRichiestaDto = new PresentaRichiestaDto()
				.setCodiceFiscaleRichiedente(codiceFiscaleRichiedente)
				.setCuaa(cuaa);
		Exception e = Assertions.assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(post("/api/v1/richieste").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(presentaRichiestaDto)));
		});

		assertTrue(e.getMessage().contains(UMA_01_01_BR1_ERR_MSG));
	}

	@Test
	void validaPresentazioneDomandaEsisteInCompilazione() throws Exception {
		String cuaa = "MSTFBA79L10H612L";
		String codiceFiscaleRichiedente = "MSTFBA79L10H612L";

		mockUtenteConnessoPresentaRichiesta(cuaa,codiceFiscaleRichiedente);

		List<CaricaAgsDto> mockPersone = new ArrayList<CaricaAgsDto>();
		mockPersone.add(new CaricaAgsDto());
		Mockito.when(anagraficaClient.getTitolariRappresentantiLegali(cuaa)).thenReturn(mockPersone);

		FascicoloAgsDto isf = new FascicoloAgsDto();
		isf.setCuaa(cuaa);
		isf.iscrittoSezioneSpecialeAgricola(true);
		isf.setNonIscrittoSezioneSpecialeAgricola(false);
		isf.setStato(StatoEnum.VALIDO);
		isf.setDataValidazione(clock.now());
		Mockito.when(anagraficaClient.getFascicolo(cuaa)).thenReturn(isf);

		List<MacchinaAgsDto> macchine = new ArrayList<>();
		macchine.add(new MacchinaAgsDto());
		Mockito.when(dotazioneTecnicaClient.getMacchine(Mockito.eq(cuaa), Mockito.any())).thenReturn(macchine);

		RisultatiPaginati<RichiestaCarburanteDto> domandeUmaPaged = new RisultatiPaginati<>();
		List<RichiestaCarburanteDto> domandeUmaDto = new ArrayList<>();
		RichiestaCarburanteDto domanda = new RichiestaCarburanteDto();
		domanda.setCuaa(cuaa);
		domanda.setCfRichiedente(codiceFiscaleRichiedente);
		domanda.setCampagna(2020L);
		domanda.setStato(StatoRichiestaCarburante.IN_COMPILAZIONE);
		domandeUmaDto.add(domanda);
		domandeUmaPaged.setRisultati(domandeUmaDto);
		doReturn(domandeUmaPaged).when(ricercaRichiestaCarburanteService).getRichiestePaged(Mockito.any());

		PresentaRichiestaDto presentaRichiestaDto = new PresentaRichiestaDto()
				.setCodiceFiscaleRichiedente(codiceFiscaleRichiedente)
				.setCuaa(cuaa);
		Exception e = Assertions.assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(post("/api/v1/richieste").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(presentaRichiestaDto)));
		});

		assertTrue(e.getMessage().contains(UMA_01_02_BR1_ERR_MSG));	
	} 


	@Test
	void validaPresentazioneDomandaEsisteAutorizzata() throws Exception {
		String cuaa = "MSTFBA79L10H612L";
		String codiceFiscaleRichiedente = "MSTFBA79L10H612L";

		mockUtenteConnessoPresentaRichiesta(cuaa,codiceFiscaleRichiedente);

		List<CaricaAgsDto> mockPersone = new ArrayList<CaricaAgsDto>();
		mockPersone.add(new CaricaAgsDto());
		Mockito.when(anagraficaClient.getTitolariRappresentantiLegali(cuaa)).thenReturn(mockPersone);

		FascicoloAgsDto isf = new FascicoloAgsDto();
		isf.setCuaa(cuaa);
		isf.iscrittoSezioneSpecialeAgricola(true);
		isf.setNonIscrittoSezioneSpecialeAgricola(false);
		isf.setStato(StatoEnum.VALIDO);
		isf.setDataValidazione(clock.now());
		Mockito.when(anagraficaClient.getFascicolo(cuaa)).thenReturn(isf);

		List<MacchinaAgsDto> macchine = new ArrayList<>();
		macchine.add(new MacchinaAgsDto());
		Mockito.when(dotazioneTecnicaClient.getMacchine(Mockito.eq(cuaa),Mockito.any())).thenReturn(macchine);

		RisultatiPaginati<RichiestaCarburanteDto> domandeUmaPaged = new RisultatiPaginati<>();
		List<RichiestaCarburanteDto> domandeUmaDto = new ArrayList<>();
		RichiestaCarburanteDto domanda = new RichiestaCarburanteDto();
		domanda.setCuaa(cuaa);
		domanda.setCfRichiedente(codiceFiscaleRichiedente);
		domanda.setCampagna(2020L);
		domanda.setStato(StatoRichiestaCarburante.RETTIFICATA);
		domandeUmaDto.add(domanda);
		domandeUmaPaged.setRisultati(domandeUmaDto);
		doReturn(domandeUmaPaged).when(ricercaRichiestaCarburanteService).getRichiestePaged(Mockito.any());

		// creazione dichiarazione consumi autorizzata - vale nel caso delle rettifiche
		RisultatiPaginati<DichiarazioneConsumiDto> dichiarazioneConsumiPaged = new RisultatiPaginati<>();
		var dichiarazioniMock = Arrays.asList(new DichiarazioneConsumiDto()
				.setCampagnaRichiesta(2020L)
				.setCuaa(cuaa)
				.setStato(StatoDichiarazioneConsumi.PROTOCOLLATA));
		dichiarazioneConsumiPaged.setRisultati(dichiarazioniMock);
		doReturn(dichiarazioneConsumiPaged).when(ricercaDichiarazioneConsumiService).getDichiarazioniConsumiPaged(Mockito.any());

		PresentaRichiestaDto presentaRichiestaDto = new PresentaRichiestaDto()
				.setCodiceFiscaleRichiedente(codiceFiscaleRichiedente)
				.setCuaa(cuaa);
		Exception e = Assertions.assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(post("/api/v1/richieste").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(presentaRichiestaDto)));
		});

		assertTrue(e.getMessage().contains(UMA_CONTROLLO_DICHIARAZIONE_CONSUMI));	
	} 

	@Test
	void validaPresentazioneDomandaFallimentoServizoGetDichiarazioni() throws Exception {
		String cuaa = "MSTFBA79L10H612L";
		String codiceFiscaleRichiedente = "MSTFBA79L10H612L";

		mockUtenteConnessoPresentaRichiesta(cuaa,codiceFiscaleRichiedente);

		List<CaricaAgsDto> mockPersone = new ArrayList<CaricaAgsDto>();
		mockPersone.add(new CaricaAgsDto());
		Mockito.when(anagraficaClient.getTitolariRappresentantiLegali(cuaa)).thenReturn(mockPersone);

		FascicoloAgsDto isf = new FascicoloAgsDto();
		isf.setCuaa(cuaa);
		isf.iscrittoSezioneSpecialeAgricola(true);
		isf.setNonIscrittoSezioneSpecialeAgricola(false);
		isf.setStato(StatoEnum.VALIDO);
		isf.setDataValidazione(clock.now());
		Mockito.when(anagraficaClient.getFascicolo(cuaa)).thenReturn(isf);

		List<MacchinaAgsDto> macchine = new ArrayList<>();
		macchine.add(new MacchinaAgsDto());
		Mockito.when(dotazioneTecnicaClient.getMacchine(Mockito.eq(cuaa),Mockito.any())).thenReturn(macchine);

		RisultatiPaginati<RichiestaCarburanteDto> domandeUmaPaged = new RisultatiPaginati<>();
		List<RichiestaCarburanteDto> domandeUmaDto = new ArrayList<>();
		RichiestaCarburanteDto domanda = new RichiestaCarburanteDto();
		domanda.setCuaa(cuaa);
		domanda.setCfRichiedente(codiceFiscaleRichiedente);
		domanda.setCampagna(2020L);
		domanda.setStato(StatoRichiestaCarburante.RETTIFICATA);
		domandeUmaDto.add(domanda);
		domandeUmaPaged.setRisultati(domandeUmaDto);
		doReturn(domandeUmaPaged).when(ricercaRichiestaCarburanteService).getRichiestePaged(Mockito.any());

		// Non riesce a reperire le dichiarazioni consumi
		doThrow(IllegalArgumentException.class).when(ricercaDichiarazioneConsumiService).getDichiarazioniConsumiPaged(Mockito.any());

		PresentaRichiestaDto presentaRichiestaDto = new PresentaRichiestaDto()
				.setCodiceFiscaleRichiedente(codiceFiscaleRichiedente)
				.setCuaa(cuaa);
		Exception e = Assertions.assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(post("/api/v1/richieste").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(presentaRichiestaDto)));
		});

		assertTrue(e.getMessage().contains("Errore nella ricerca delle dichiarazioni consumi per il cuaa"));	
	}

	@Test
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void validaPresentazioneFascicoloTitolareDecedutoSuccessful() throws Exception {

		String cuaa = "MRCSFN80S27H612G";
		String codiceFiscaleRichiedente = "MRCSFN80S27H612G";

		mockUtenteConnessoPresentaRichiesta(cuaa,codiceFiscaleRichiedente);
		Mockito.when(clock.now()).thenReturn(LocalDateTime.of(2022, 2, 2, 0, 0));

		List<CaricaAgsDto> mockPersone = new ArrayList<CaricaAgsDto>();
		mockPersone.add(new CaricaAgsDto());
		Mockito.when(anagraficaClient.getEredi(cuaa)).thenReturn(mockPersone);

		FascicoloAgsDto fascicolo = new FascicoloAgsDto()
				.cuaa(cuaa)
				.denominazione("denominazione")
				.dataMorteTitolare(LocalDateTime.of(2022, 2, 2,	0, 0))
				.stato(StatoEnum.VALIDO)
				.dataValidazione(LocalDateTime.of(2022, 2, 2, 0, 0))
				.iscrittoSezioneSpecialeAgricola(Boolean.TRUE)
				.setDetenzioni(Arrays.asList(new DetenzioneAgsDto().sportello("COLDIRETTI 1").tipoDetenzione(TipoDetenzioneEnum.MANDATO)));
		Mockito.when(anagraficaClient.getFascicolo(cuaa)).thenReturn(fascicolo);

		List<MacchinaAgsDto> macchine = new ArrayList<>();
		macchine.add(new MacchinaAgsDto().alimentazione(AlimentazioneEnum.GASOLIO).idAgs(10L));
		Mockito.when(dotazioneTecnicaClient.getMacchine(Mockito.eq(cuaa),Mockito.any())).thenReturn(macchine);

		PresentaRichiestaDto presentaRichiestaDto = new PresentaRichiestaDto()
				.setCodiceFiscaleRichiedente(codiceFiscaleRichiedente)
				.setCuaa(cuaa);

		var response = mockMvc.perform(post("/api/v1/richieste").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(presentaRichiestaDto)))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		Long idDomandaUma = objectMapper.readValue(response, Long.class);
		assertNotNull(idDomandaUma);
	}

	@Test
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void validaPresentazioneFascicoloTitolareDecedutoNoEredi() throws Exception {

		String cuaa = "MRCSFN80S27H612G";
		String codiceFiscaleRichiedente = "MRCSFN80S27H612G";

		mockUtenteConnessoPresentaRichiesta(cuaa,codiceFiscaleRichiedente);
		Mockito.when(anagraficaClient.getEredi(cuaa)).thenReturn(null);

		Mockito.when(clock.now()).thenReturn(LocalDateTime.of(2022, 2, 2, 0, 0));

		FascicoloAgsDto fascicolo = new FascicoloAgsDto()
				.cuaa(cuaa)
				.dataMorteTitolare(LocalDateTime.of(2022, 2, 2,	0, 0))
				.stato(StatoEnum.VALIDO)
				.dataValidazione(LocalDateTime.of(2022, 2, 2, 0, 0))
				.iscrittoSezioneSpecialeAgricola(Boolean.TRUE);
		Mockito.when(anagraficaClient.getFascicolo(cuaa)).thenReturn(fascicolo);

		List<MacchinaAgsDto> macchine = new ArrayList<>();
		macchine.add(new MacchinaAgsDto());
		Mockito.when(dotazioneTecnicaClient.getMacchine(Mockito.eq(cuaa),Mockito.any())).thenReturn(macchine);

		PresentaRichiestaDto presentaRichiestaDto = new PresentaRichiestaDto()
				.setCodiceFiscaleRichiedente(codiceFiscaleRichiedente)
				.setCuaa(cuaa);

		Exception e = Assertions.assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(post("/api/v1/richieste").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(presentaRichiestaDto)));
		});
		assertTrue(e.getMessage().contains(TASK_UMA_46_ERR_NO_EREDI));	
	}

	// l'erede non può presentare una richiesta di carburante
	@Test
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void validaPresentazioneEredePresentaRichiesta() throws Exception {

		String cuaa = "MSTFBA79L10H612L";
		String codiceFiscaleRichiedente = "MSTFBA79L10H612L";

		mockUtenteConnessoPresentaRichiesta(cuaa,codiceFiscaleRichiedente);
		Mockito.when(clock.now()).thenReturn(LocalDateTime.of(2022, 2, 2, 0, 0));
		List<CaricaAgsDto> mockPersone = new ArrayList<CaricaAgsDto>();
		mockPersone.add(new CaricaAgsDto());
		Mockito.when(anagraficaClient.getEredi(cuaa)).thenReturn(mockPersone);

		FascicoloAgsDto fascicolo = new FascicoloAgsDto()
				.cuaa(cuaa)
				.dataMorteTitolare(LocalDateTime.of(2022, 2, 2,	0, 0))
				.stato(StatoEnum.VALIDO)
				.dataValidazione(LocalDateTime.of(2022, 2, 2,	0, 0))
				.iscrittoSezioneSpecialeAgricola(Boolean.TRUE);
		Mockito.when(anagraficaClient.getFascicolo(cuaa)).thenReturn(fascicolo);

		List<MacchinaAgsDto> macchine = new ArrayList<>();
		macchine.add(new MacchinaAgsDto());
		Mockito.when(dotazioneTecnicaClient.getMacchine(Mockito.eq(cuaa),Mockito.any())).thenReturn(macchine);

		PresentaRichiestaDto presentaRichiestaDto = new PresentaRichiestaDto()
				.setCodiceFiscaleRichiedente(codiceFiscaleRichiedente)
				.setCuaa(cuaa);

		Exception e = Assertions.assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(post("/api/v1/richieste").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(presentaRichiestaDto)));
		});
		assertTrue(e.getMessage().contains(TASK_UMA_46_ERR_CREAZIONE_RICHIESTA));	
	}

	@Test
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void validaRichiestaSuccessful() throws Exception {

		String cuaa = "BBBDNL95R14L378T";
		Long idRichiesta = 13L;
		mockUtenteConnessoValida(idRichiesta);

		FascicoloAgsDto isf = new FascicoloAgsDto();
		isf.setCuaa(cuaa);
		isf.iscrittoSezioneSpecialeAgricola(true);
		isf.setNonIscrittoSezioneSpecialeAgricola(false);
		isf.setDataAggiornamento(LocalDateTime.of(2019, 1, 1, 22, 22, 22));
		isf.setStato(StatoEnum.VALIDO);
		isf.setDataValidazione(LocalDateTime.of(2019, 1, 1, 0, 0));
		Mockito.when(anagraficaClient.getFascicolo(cuaa)).thenReturn(isf);

		Mockito.when(clock.now()).thenReturn(LocalDateTime.of(2019, 1, 1, 0, 0));

		mockMvc.perform(post("/api/v1/richieste/".concat(String.valueOf(idRichiesta)).concat("/valida")))
		.andExpect(status().is2xxSuccessful());
	}

	@Test
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void validaRichiestaFascicoloDataValidazioneError() throws Exception {
		String cuaa = "BBBDNL95R14L378T";
		Long idRichiesta = 1L;
		mockUtenteConnessoValida(idRichiesta);

		FascicoloAgsDto fascicolo = new FascicoloAgsDto();
		fascicolo.setDataValidazione(LocalDateTime.of(2020, 9, 2, 22, 0));

		Mockito.when(anagraficaClient.getFascicolo(cuaa)).thenReturn(fascicolo);

		Exception e = Assertions.assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(post("/api/v1/richieste/".concat(String.valueOf(idRichiesta)).concat("/valida")));
		});

		assertTrue(e.getMessage().contains(UMA_08_BR1_ERR_MSG));
	}

	@Test
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void validaRichiestaFascicoloStatoNonValido() throws Exception {

		String cuaa = "BBBDNL95R14L378T";
		Long idRichiesta = 1L;
		mockUtenteConnessoValida(idRichiesta);

		FascicoloAgsDto fascicolo = new FascicoloAgsDto();
		fascicolo.setStato(StatoEnum.IN_ANOMALIA);
		fascicolo.setDataValidazione(LocalDateTime.of(2020, 8, 2, 22, 0));

		Mockito.when(anagraficaClient.getFascicolo(cuaa)).thenReturn(fascicolo);

		Exception e = Assertions.assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(post("/api/v1/richieste/".concat(String.valueOf(idRichiesta)).concat("/valida")));
		});

		assertTrue(e.getMessage().contains(UMA_01_01_BR2_ERR_MSG));
	}
	*/
}
