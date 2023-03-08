package it.tndigitale.a4g.uma.business.service.consumi;

import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.uma.business.persistence.entity.DichiarazioneConsumiModel;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.StatoDichiarazioneConsumi;
import it.tndigitale.a4g.uma.business.persistence.entity.StatoRichiestaCarburante;
import it.tndigitale.a4g.uma.business.persistence.repository.RichiestaCarburanteDao;
import it.tndigitale.a4g.uma.business.service.consumi.calcoli.CarburanteHelper;
import it.tndigitale.a4g.uma.business.service.richiesta.RicercaRichiestaCarburanteService;
import it.tndigitale.a4g.uma.dto.consumi.CarburanteDto;
import it.tndigitale.a4g.uma.dto.consumi.CarburanteDtoBuilder;
import it.tndigitale.a4g.uma.dto.consumi.DichiarazioneConsumiDto;
import it.tndigitale.a4g.uma.dto.consumi.DichiarazioneConsumiFilter;
import it.tndigitale.a4g.uma.dto.consumi.DichiarazioneConsumiPagedFilter;

@Component
public class DichiarazioneConsumiValidator {

	private static final String BR2_UMA_03_01 = "Il sistema non ha trovato una Richiesta di carburante autorizzata!";
	private static final String BR3_UMA_03_01 = "La dichiarazione consumi è già stata protocollata!";
	private static final String BR4_UMA_03_01 = "Esiste già una dichiarazione consumi in fase di compilazione!";
	private static final String ACCISA_OBBLIGATORIA = "E' necessario selezionare una motivazione accisa";
	private static final String ACCISA_ERR = "E’ necessario cancellare il motivo Accisa!";
	@Autowired
	private RicercaDichiarazioneConsumiService ricercaDichiarazioneConsumiService;
	@Autowired
	private RicercaRichiestaCarburanteService ricercaRichiestaCarburanteService;
	@Autowired
	private RichiestaCarburanteDao richiestaCarburanteDao;
	@Autowired
	private Clock clock;
	@Autowired
	private CarburanteHelper carburanteHelper;
	@Autowired
	private ConsuntiviConsumiValidator consuntiviConsumiValidator;


	RichiestaCarburanteModel validaPresentazione(String cuaa) {
		Optional<RichiestaCarburanteModel> optRichiesta = ricercaRichiestaCarburanteService.getRichiestaAutorizzataPiuRecente(cuaa);
		if (optRichiesta.isPresent()) {
			richiestaAnnoCorrenteOrPrecedente
			.andThen(alreadyExistDichiarazioneConsumi)
			.accept(optRichiesta.get());
		} else {
			throw new NoSuchElementException(BR2_UMA_03_01);
		}			
		return optRichiesta.get();
	}

	public void validaProtocollazione(DichiarazioneConsumiModel dichiarazione) {
		RichiestaCarburanteModel richiesta = dichiarazione.getRichiestaCarburante();
		Optional<RichiestaCarburanteModel> altraRichiestaInCompilazioneOpt = richiestaCarburanteDao.findByCuaaAndCampagnaAndStato(richiesta.getCuaa(), richiesta.getCampagna(), StatoRichiestaCarburante.IN_COMPILAZIONE);

		// non ci deve essere una richiesta di carburante in compilazione "in sospeso"
		if (altraRichiestaInCompilazioneOpt.isPresent() && ricercaRichiestaCarburanteService.getIdRettificata(altraRichiestaInCompilazioneOpt.get().getCuaa(), altraRichiestaInCompilazioneOpt.get().getCampagna(), altraRichiestaInCompilazioneOpt.get().getDataPresentazione()).isPresent()) {
			throw new IllegalArgumentException("[ERR-1] - Esiste una rettifica in compilazione per questa azienda, è necessario eliminarla per poter procedere");
		}

		// Verifico che la richiesta legata alla dichiarazione non sia stata rettificata
		if (richiesta.getStato().equals(StatoRichiestaCarburante.RETTIFICATA)) {
			throw new IllegalArgumentException("[ERR-2] - La Richiesta Di Carburante è stata rettificata, è necessario eliminare la dichiarazione consumi");
		}
		
		// validazione consuntivi
		validaMotivazioneAccisa(dichiarazione);
		consuntiviConsumiValidator.validaConsuntivi(dichiarazione);
		consuntiviConsumiValidator.esisteConsumato(dichiarazione);
		consuntiviConsumiValidator.validaAllegati(dichiarazione);
	}

	public void validaMotivazioneAccisa(DichiarazioneConsumiModel dichiarazione) {
		// valido accisa e motivazione
		var accisa = carburanteHelper.calcolaAccisa(dichiarazione);

		// se accisa > 0
		if (new CarburanteDtoBuilder().add(accisa).isGreaterThen(new CarburanteDto())) {
			Assert.isTrue(dichiarazione.getMotivazioneAccisa() != null, ACCISA_OBBLIGATORIA);
		} else {
			Assert.isTrue(dichiarazione.getMotivazioneAccisa() == null, ACCISA_ERR);
		}
	}

	// Verifico che la richiesta sia dell'anno corrente o del precedente
	private Consumer<RichiestaCarburanteModel> richiestaAnnoCorrenteOrPrecedente = richiesta ->  {
		if ((clock.now().getYear() - richiesta.getCampagna()) > 1) {
			throw new NoSuchElementException(BR2_UMA_03_01);
		}
	};

	private Consumer<RichiestaCarburanteModel> alreadyExistDichiarazioneConsumi = richiesta ->  {
		// BR3 UMA-03-01
		DichiarazioneConsumiFilter filter = new DichiarazioneConsumiPagedFilter()
				.setCuaa(richiesta.getCuaa())
				.setCampagna(Arrays.asList(richiesta.getCampagna()));

		Set<StatoDichiarazioneConsumi> statiToFilter = new HashSet<>();
		statiToFilter.add(StatoDichiarazioneConsumi.PROTOCOLLATA);
		statiToFilter.add(StatoDichiarazioneConsumi.IN_COMPILAZIONE);
		filter.setStati(statiToFilter);

		RisultatiPaginati<DichiarazioneConsumiDto> dtos;
		try {
			dtos = ricercaDichiarazioneConsumiService.getDichiarazioniConsumiPaged(filter);
		} catch (Exception e) {
			throw new IllegalArgumentException("Errore nella ricerca delle dichiarazioni consumi per il cuaa - " + richiesta.getCuaa());
		}

		if (!CollectionUtils.isEmpty(dtos.getRisultati())) {
			dtos.getRisultati().forEach(dtoToCheck -> {
				if (dtoToCheck != null && StatoDichiarazioneConsumi.PROTOCOLLATA.equals(dtoToCheck.getStato())) {
					throw new IllegalArgumentException(BR3_UMA_03_01);
				}
				// BR4 UMA-03-01
				if (dtoToCheck != null && StatoDichiarazioneConsumi.IN_COMPILAZIONE.equals(dtoToCheck.getStato())) {
					throw new IllegalArgumentException(BR4_UMA_03_01);
				}
			});
		}
	};

}
