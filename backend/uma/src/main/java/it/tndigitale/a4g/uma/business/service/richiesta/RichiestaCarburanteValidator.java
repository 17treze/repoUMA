package it.tndigitale.a4g.uma.business.service.richiesta;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import it.tndigitale.a4g.fascicolo.anagrafica.client.model.FascicoloAgsDto;
import it.tndigitale.a4g.fascicolo.anagrafica.client.model.FascicoloAgsDto.StatoEnum;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4g.framework.support.PersonaSelector;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.StatoDichiarazioneConsumi;
import it.tndigitale.a4g.uma.business.persistence.entity.StatoRichiestaCarburante;
import it.tndigitale.a4g.uma.business.service.client.UmaAnagraficaClient;
import it.tndigitale.a4g.uma.business.service.client.UmaDotazioneTecnicaClient;
import it.tndigitale.a4g.uma.business.service.consumi.RicercaDichiarazioneConsumiService;
import it.tndigitale.a4g.uma.dto.consumi.DichiarazioneConsumiDto;
import it.tndigitale.a4g.uma.dto.consumi.DichiarazioneConsumiFilter;
import it.tndigitale.a4g.uma.dto.consumi.DichiarazioneConsumiPagedFilter;
import it.tndigitale.a4g.uma.dto.richiesta.RichiestaCarburanteDto;
import it.tndigitale.a4g.uma.dto.richiesta.RichiestaCarburanteFilter;
import it.tndigitale.a4g.uma.dto.richiesta.RichiestaCarburantePagedFilter;

@Component
public class RichiestaCarburanteValidator {
	
	private static final String UMA_01_01_BR1_ERR_MSG = "Solamente il titolare o il rappresentante legale di un'azienda agricola è autorizzato alla presentazione della domanda UMA on line";
	private static final String UMA_01_01_BR2_ERR_MSG = "L'azienda non dispone di un fascicolo valido. E’ necessario creare/aggiornare il fascicolo";
	private static final String UMA_01_01_BR3_ERR_MSG = "L'azienda non risulta iscritta al registro delle imprese della Camera di Commercio oppure non risulta esente dall'iscrizione. E’ necessario verificare tale posizione";
	private static final String UMA_01_01_BR4_ERR_MSG = "L'azienda non dispone di macchinari.  E’ necessario aggiornare il fascicolo aziendale";
	private static final String UMA_01_02_BR1_ERR_MSG = "L'azienda ha già una domanda UMA in compilazione.";
	private static final String UMA_CONTROLLO_DICHIARAZIONE_CONSUMI = "L'azienda ha già una dichiarazione consumi protocollata";
	private static final String UMA_08_BR1_ERR_MSG = UMA_01_01_BR2_ERR_MSG
			+ ", eliminare l’attuale domanda e poi presentare una nuova Richiesta di carburante.";
	private static final String FABBISOGNO_NON_SALVATO = "Attenzione! I dati imputati non sono stati salvati, per procedere è necessario prima salvare";
	private static final String NOTE_NON_PRESENTI = "Attenzione! E’ necessario compilare il campo Note indicando le motivazioni che hanno portato alla creazione della Rettifica di carburante.";
	private static final String DICHIARAZIONE_CONSUMI_ANNO_PRECEDENTE = "Per poter procedere è necessario prima completare la Dichiarazione consumi dell’anno precedente";
	
	private static final String TASK_UMA_46_ERR_NO_EREDI = "Il campo data decesso risulta valorizzato. E' necessario inserire nel fascicolo un erede.";
	private static final String TASK_UMA_46_ERR_CREAZIONE_RICHIESTA = "Il campo data decesso risulta valorizzato. Il sistema non permette l'inserimento di una richiesta di carburante.";
	
	private static final Logger logger = LoggerFactory.getLogger(RichiestaCarburanteValidator.class);
	
	@Autowired
	private UmaAnagraficaClient anagraficaClient;
	@Autowired
	private UmaDotazioneTecnicaClient dotazioneTecnicaClient;
	@Autowired
	private RicercaRichiestaCarburanteService ricercaRichiestaCarburanteService;
	@Autowired
	private RicercaDichiarazioneConsumiService ricercaDichiarazioneConsumiService;
	@Autowired
	private Clock clock;
	
	void validaPresentazioneRichiesta(String cuaa) {
		
		var now = clock.now();
		
		// controlli uma
		haAlmenoUnaMacchina.andThen(nonHaUnaRichiestaInCompilazione)
				.andThen(nonEsisteUnaDichiarazioneConsumiAutorizzata)
				.andThen(nonEsisteUnaDichiarazioneConsumiInCompilazione).accept(cuaa);
		
		// controlli fascicolo
		FascicoloAgsDto fascicolo = anagraficaClient.getFascicolo(cuaa);
		Optional<Long> idRettificata = ricercaRichiestaCarburanteService.getIdRettificata(cuaa,
				Long.valueOf(now.getYear()), now);
		
		haFascicoloValido.andThen(haIscrizioniSpeciali).accept(fascicolo);
		
		if (fascicolo.getDataMorteTitolare() != null && PersonaSelector.isPersonaFisica(cuaa)) {
			// l'erede non può presentare una richiesta di carburante, può solo rettificare.
			Assert.isTrue(idRettificata.isPresent(), TASK_UMA_46_ERR_CREAZIONE_RICHIESTA);
			
			// esistenza di almeno un erede
			Assert.isTrue(!CollectionUtils.isEmpty(anagraficaClient.getEredi(cuaa)), TASK_UMA_46_ERR_NO_EREDI);
		}
		else {
			// esistenza di almeno un Titolare/Rappresentante legale
			Assert.isTrue(!CollectionUtils.isEmpty(anagraficaClient.getTitolariRappresentantiLegali(cuaa)),
					UMA_01_01_BR1_ERR_MSG);
		}
	}
	
	public void validaProtocollazione(RichiestaCarburanteModel richiesta) {
		validaFascicolo.andThen(validaFabbisogno).accept(richiesta);
	}
	
	// Esiste un fascicolo AGS validato almeno una volta nell'anno in corso ed è nello stato VALIDO.
	private Consumer<FascicoloAgsDto> haFascicoloValido = fascicolo -> {
		//BR2 - Fascicolo non valido
		//		if (fascicolo == null || !StatoEnum.VALIDO.equals(fascicolo.getStato()) || fascicolo.getDataValidazione().getYear() != clock.now().getYear()) {
		//			logger.info("UMA_01_01_BR2 - CUAA: {}", fascicolo.getCuaa());
		//			throw new IllegalArgumentException(UMA_01_01_BR2_ERR_MSG);
		//		}
	};
	
	// Verifica che nel fascicolo aziendale sia selezionata una delle due seguenti voci: Iscritto alla sezione speciale agricola oppure Non iscritto alla sezione speciale agricola, art.2 ,c.1, D.M. n.454/2001.
	private Consumer<FascicoloAgsDto> haIscrizioniSpeciali = fascicolo -> {
		//BR3 - Info CIAA
		//		if (!(fascicolo.isIscrittoSezioneSpecialeAgricola() || fascicolo.isNonIscrittoSezioneSpecialeAgricola())) {
		//			throw new IllegalArgumentException(UMA_01_01_BR3_ERR_MSG);
		//		}
	};
	
	// Presenza di almeno una macchina nel fascicolo.
	private Consumer<String> haAlmenoUnaMacchina = cuaa -> {
		if (CollectionUtils.isEmpty(dotazioneTecnicaClient.getMacchine(cuaa, clock.now()))) {
			throw new IllegalArgumentException(UMA_01_01_BR4_ERR_MSG);
		}
	};
	
	// Esiste già in A4G una domanda UMA con lo stato in compilazione per quell'azienda nell'anno in corso.
	private Consumer<String> nonHaUnaRichiestaInCompilazione = cuaa -> {
		RichiestaCarburanteFilter filter = new RichiestaCarburantePagedFilter().setNumeroElementiPagina(1).setCuaa(cuaa)
				.setCampagna(Arrays.asList(Long.valueOf(clock.now().getYear())))
				.setStati(Set.of(StatoRichiestaCarburante.IN_COMPILAZIONE));
		
		RisultatiPaginati<RichiestaCarburanteDto> richieste;
		try {
			richieste = ricercaRichiestaCarburanteService.getRichiestePaged(filter);
		}
		catch (Exception e) {
			throw new IllegalArgumentException(
					"Errore nella ricerca delle richieste di carburante per il cuaa - " + filter.getCuaa());
		}
		
		if (richieste != null && !CollectionUtils.isEmpty(richieste.getRisultati())) {
			richieste.getRisultati().forEach(richiesta -> {
				if (StatoRichiestaCarburante.IN_COMPILAZIONE.equals(richiesta.getStato())) {
					logger.info("UMA_01_02_BR1 - CUAA: {}", richiesta.getCuaa());
					throw new IllegalArgumentException(UMA_01_02_BR1_ERR_MSG);
				}
			});
		}
	};
	
	// Esiste già in A4G una dichiarazione consumi con lo stato autorizzata per quell'azienda nell'anno di campagna.
	private Consumer<String> nonEsisteUnaDichiarazioneConsumiAutorizzata = cuaa -> {
		
		DichiarazioneConsumiFilter filter = new DichiarazioneConsumiPagedFilter().setNumeroElementiPagina(1)
				.setCampagna(Arrays.asList(Long.valueOf(clock.now().getYear()))).setCuaa(cuaa)
				.setStati(Set.of(StatoDichiarazioneConsumi.PROTOCOLLATA));
		
		RisultatiPaginati<DichiarazioneConsumiDto> dichiarazioni;
		try {
			dichiarazioni = ricercaDichiarazioneConsumiService.getDichiarazioniConsumiPaged(filter);
		}
		catch (Exception e) {
			throw new IllegalArgumentException(
					"Errore nella ricerca delle dichiarazioni consumi per il cuaa - " + filter.getCuaa());
		}
		
		if (dichiarazioni != null && !CollectionUtils.isEmpty(dichiarazioni.getRisultati())) {
			dichiarazioni.getRisultati().forEach(dichiarazione -> {
				if (StatoDichiarazioneConsumi.PROTOCOLLATA.equals(dichiarazione.getStato())) {
					throw new IllegalArgumentException(UMA_CONTROLLO_DICHIARAZIONE_CONSUMI);
				}
			});
		}
		
	};
	
	// Esiste una dichiarazione consumi in A4G con lo stato in compilazione per quell'azienda nell'anno di campagna precedente.
	private Consumer<String> nonEsisteUnaDichiarazioneConsumiInCompilazione = cuaa -> {
		var filterConsumi = new DichiarazioneConsumiPagedFilter().setNumeroElementiPagina(1)
				.setCampagna(Arrays.asList(Long.valueOf(clock.now().getYear()) - 1)).setCuaa(cuaa)
				.setStati(Set.of(StatoDichiarazioneConsumi.PROTOCOLLATA));
		
		RichiestaCarburanteFilter filterRichieste = new RichiestaCarburantePagedFilter().setNumeroElementiPagina(1)
				.setCuaa(cuaa).setCampagna(Arrays.asList(Long.valueOf(clock.now().getYear()) - 1))
				.setStati(Set.of(StatoRichiestaCarburante.AUTORIZZATA));
		
		RisultatiPaginati<RichiestaCarburanteDto> richieste;
		try {
			richieste = ricercaRichiestaCarburanteService.getRichiestePaged(filterRichieste);
		}
		catch (Exception e) {
			throw new IllegalArgumentException(
					"Errore nella ricerca delle richieste di carburante per il cuaa - " + filterRichieste.getCuaa());
		}
		
		RisultatiPaginati<DichiarazioneConsumiDto> dichiarazioni;
		try {
			dichiarazioni = ricercaDichiarazioneConsumiService.getDichiarazioniConsumiPaged(filterConsumi);
		}
		catch (Exception e) {
			throw new IllegalArgumentException(
					"Errore nella ricerca delle dichiarazioni consumi per il cuaa - " + filterConsumi.getCuaa());
		}
		
		if ((richieste != null && !CollectionUtils.isEmpty(richieste.getRisultati()))
				&& (dichiarazioni == null || CollectionUtils.isEmpty(dichiarazioni.getRisultati()))) {
			throw new IllegalArgumentException(DICHIARAZIONE_CONSUMI_ANNO_PRECEDENTE);
		}
		
		if (dichiarazioni != null && !CollectionUtils.isEmpty(dichiarazioni.getRisultati())) {
			dichiarazioni.getRisultati().forEach(dichiarazione -> {
				if (!StatoDichiarazioneConsumi.PROTOCOLLATA.equals(dichiarazione.getStato())) {
					throw new IllegalArgumentException(DICHIARAZIONE_CONSUMI_ANNO_PRECEDENTE);
				}
			});
		}
		
	};
	
	// Controllo effettuato solo in fase di protocollazione della richiesta
	private Consumer<RichiestaCarburanteModel> validaFascicolo = richiesta -> {
		FascicoloAgsDto fascicoloValido = anagraficaClient.getFascicolo(richiesta.getCuaa());
		if (fascicoloValido == null
				|| fascicoloValido.getDataValidazione().compareTo(richiesta.getDataPresentazione()) > 0) {
			logger.info("UMA_08_BR1 - CUAA: {}", richiesta.getCuaa());
			throw new IllegalArgumentException(UMA_08_BR1_ERR_MSG);
		}
		haFascicoloValido.accept(fascicoloValido);
	};
	
	private Consumer<RichiestaCarburanteModel> validaFabbisogno = richiesta -> {
		if (richiesta.getBenzina() == null && richiesta.getGasolio() == null && richiesta.getGasolioSerre() == null
				&& richiesta.getGasolioTerzi() == null) {
			throw new IllegalArgumentException(FABBISOGNO_NON_SALVATO);
		}
		if (ricercaRichiestaCarburanteService
				.getIdRettificata(richiesta.getCuaa(), richiesta.getCampagna(), richiesta.getDataPresentazione())
				.isPresent()) {
			Assert.isTrue(!StringUtils.isEmpty(richiesta.getNote()), NOTE_NON_PRESENTI);
		}
	};
}
