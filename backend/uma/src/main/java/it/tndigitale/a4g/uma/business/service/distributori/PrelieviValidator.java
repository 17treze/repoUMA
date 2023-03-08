package it.tndigitale.a4g.uma.business.service.distributori;

import java.util.Optional;
import java.util.function.Consumer;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.uma.business.persistence.entity.DichiarazioneConsumiModel;
import it.tndigitale.a4g.uma.business.persistence.entity.PrelievoModel;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.StatoDichiarazioneConsumi;
import it.tndigitale.a4g.uma.business.persistence.entity.StatoRichiestaCarburante;
import it.tndigitale.a4g.uma.business.persistence.repository.DichiarazioneConsumiDao;
import it.tndigitale.a4g.uma.business.persistence.repository.RichiestaCarburanteDao;
import it.tndigitale.a4g.uma.business.service.consumi.calcoli.CarburanteHelper;
import it.tndigitale.a4g.uma.dto.consumi.CarburanteDto;
import it.tndigitale.a4g.uma.dto.consumi.CarburanteDtoBuilder;
import it.tndigitale.a4g.uma.dto.richiesta.PrelievoDto;

@Component
public class PrelieviValidator {

	private static final String BR3 = "Attenzione! E’ necessario digitare la quantità di carburante prelevata";
	private static final String BR4 = "La quantità di carburante prelevato non può superare la quantità di carburante prelevabile";
	private static final String ESISTE_DICHIARAZIONE_CONSUMI_AUTORIZZATA = "Esiste una dichiarazione consumi protocollata per la richiesta di carburante indicata per il prelievo";

	private CarburanteDto prelevabile;

	@Autowired
	private CarburanteHelper carburanteHelper;
	@Autowired
	private CarburanteDtoBuilder carburanteDtoBuilder;
	@Autowired
	private DichiarazioneConsumiDao dichiarazioneConsumiDao;
	@Autowired
	private RichiestaCarburanteDao richiestaCarburanteDao;

	@Autowired
	private Clock clock;

	void validaPrelievo(PrelievoDto prelievo, RichiestaCarburanteModel richiestaCarburante) {
		// Calcolo il carburante prelevabile
		this.prelevabile = carburanteHelper.calcolaPrelevabile(richiestaCarburante);

		dataPrelievoAnnoCampagna
		.andThen(carburanteMaggioreDiZero)
		.andThen(carburantePrelevatoMinoreCarburanteDisponibile)
		.accept(prelievo);
	}

	void validaModificaPrelievo(PrelievoDto prelievo, PrelievoModel prelievoOriginale, RichiestaCarburanteModel richiestaCarburante) {
		// Calcolo il carburante prelevabile a meno di quello attualmente in modifica
		this.prelevabile = carburanteDtoBuilder.newDto()
				.add(carburanteHelper.calcolaPrelevabile(richiestaCarburante))
				.add(new CarburanteDto()
						.setGasolio(prelievoOriginale.getGasolio() != null ? prelievoOriginale.getGasolio() : 0)
						.setBenzina(prelievoOriginale.getBenzina() != null ? prelievoOriginale.getBenzina() : 0)
						.setGasolioSerre(prelievoOriginale.getGasolioSerre() != null ? prelievoOriginale.getGasolioSerre() : 0))
				.build();

		carburanteMaggioreDiZero
		.andThen(carburantePrelevatoMinoreCarburanteDisponibile)
		.accept(prelievo);
	}


	// Verifico che la data indicata nel prelievo rientri nell'anno solare
	private Consumer<PrelievoDto> dataPrelievoAnnoCampagna = dto ->  {
		if(dto.getData().getYear() != clock.now().getYear()) {
			throw new IllegalArgumentException("La data indicata per il prelievo non rientra nell'anno di campagna attuale");
		}

	};

	// BR3 Almeno un campo quantità di carburante è obbligatorio e deve essere maggiore di 0
	private Consumer<PrelievoDto> carburanteMaggioreDiZero = dto ->  {
		if (dto.getCarburante() == null) {
			throw new IllegalArgumentException(BR3);
		}

		if (dto.getCarburante().getGasolio() <= 0 
				&& dto.getCarburante().getBenzina() <= 0 
				&& dto.getCarburante().getGasolioSerre() <= 0) {
			throw new IllegalArgumentException(BR3);
		}
	};

	// BR4 Le quantità di carburante prelevato non possono superare il quantitativo prelevabile (disponibile nella storia)
	private Consumer<PrelievoDto> carburantePrelevatoMinoreCarburanteDisponibile = dto ->  {

		CarburanteDto carburanteDaPrelevare = dto.getCarburante();
		if (carburanteDaPrelevare.getGasolio() > this.prelevabile.getGasolio() 
				|| carburanteDaPrelevare.getBenzina() > this.prelevabile.getBenzina() 
				|| carburanteDaPrelevare.getGasolioSerre() > this.prelevabile.getGasolioSerre()) {
			throw new IllegalArgumentException(BR4);
		}
	};

	// Verifico che lo stato della richiesta sia autorizzata
	void checkRichiestaCarburanteAutorizzata(RichiestaCarburanteModel richiestaCarburante) {
		if (!richiestaCarburante.getStato().equals(StatoRichiestaCarburante.AUTORIZZATA)) {
			throw new IllegalArgumentException("La richiesta di carburante indicata per il prelievo è nello stato ".concat(richiestaCarburante.getStato().name()));
		}
	}

	// Verifico che non esista una dichiarazione consumi protocollata
	void checkDichiarazioneConsumiProtocollata(Long idRichiesta) {
		RichiestaCarburanteModel richiesta = richiestaCarburanteDao.findById(idRichiesta).orElseThrow(() -> new EntityNotFoundException("Richiesta con id: ".concat(String.valueOf(idRichiesta)).concat("non trovata")));
		Optional<DichiarazioneConsumiModel> dichiarazioneConsumi = dichiarazioneConsumiDao.findByRichiestaCarburante_cuaaAndRichiestaCarburante_campagna(richiesta.getCuaa(), richiesta.getCampagna());

		if (dichiarazioneConsumi.isPresent() && StatoDichiarazioneConsumi.PROTOCOLLATA.equals(dichiarazioneConsumi.get().getStato())) {
			throw new IllegalArgumentException(ESISTE_DICHIARAZIONE_CONSUMI_AUTORIZZATA);
		}
	}
}
