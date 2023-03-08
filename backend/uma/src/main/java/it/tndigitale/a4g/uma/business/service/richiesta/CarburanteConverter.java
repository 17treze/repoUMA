package it.tndigitale.a4g.uma.business.service.richiesta;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.uma.business.persistence.entity.AmbitoLavorazione;
import it.tndigitale.a4g.uma.business.persistence.entity.FabbisognoModel;
import it.tndigitale.a4g.uma.business.persistence.entity.TipoCarburante;
import it.tndigitale.a4g.uma.business.service.consumi.calcoli.CarburanteDecimal;

/**
 * Si occupa di trasformare le dichiarazioni utente in litri di carburante applicando gli opportuni coefficienti.
 * La classe viene istanziata con l'anno di campagna al fine di recuperare i coefficienti validi per quell'anno.
 * I valori di default sono 0 per ogni tipo di carburante.
 * @author B.Conetta
 */
public class CarburanteConverter {

	private Long campagna;
	private CarburanteDecimal carburante;
	private Map<TipoCarburante, BigDecimal> mappa;

	public CarburanteConverter(Long campagna) {
		this.mappa = new EnumMap<>(TipoCarburante.class);
		this.carburante = new CarburanteDecimal();
		this.campagna = campagna;
	}

	public CarburanteDecimal calcola(List<FabbisognoModel> fabbisogni) {
		if (CollectionUtils.isEmpty(fabbisogni)) { return new CarburanteDecimal(); }

		fabbisogni.stream().collect(Collectors.partitioningBy((FabbisognoModel f) -> AmbitoLavorazione.SERRE.equals(f.getLavorazioneModel().getGruppoLavorazione().getAmbitoLavorazione())))
		.forEach((isSerre, fab) -> {
			with(fab);
			if (isSerre.booleanValue()) {
				addGasolioSerre();
			} else {
				addBenzina().addGasolio();
			}
		});
		return carburante;
	}

	private CarburanteConverter with(List<FabbisognoModel> fabbisogni) {
		this.mappa = convertiInLitri(fabbisogni);
		return this;
	}

	private CarburanteConverter addGasolioSerre() {
		carburante.setGasolioSerre(carburante.getGasolioSerre().add(mappa.getOrDefault(TipoCarburante.GASOLIO, BigDecimal.ZERO)));
		return this;
	}

	private CarburanteConverter addGasolio() {
		carburante.setGasolio(carburante.getGasolio().add(mappa.getOrDefault(TipoCarburante.GASOLIO, BigDecimal.ZERO)));
		return this;
	}
	private CarburanteConverter addBenzina() {
		carburante.setBenzina(carburante.getBenzina().add(mappa.getOrDefault(TipoCarburante.BENZINA, BigDecimal.ZERO)));
		return this;
	}

	private Map<TipoCarburante, BigDecimal> convertiInLitri(List<FabbisognoModel> fabbisogni) {
		return fabbisogni.stream().collect(Collectors.groupingBy(FabbisognoModel::getCarburante, 
				Collectors.mapping(fabbisognoToLitri, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));
	}

	private Function<FabbisognoModel, BigDecimal> fabbisognoToLitri = f -> {
		Optional<BigDecimal> coefficienteOpt = f.getLavorazioneModel().getCoefficiente(campagna);
		if (coefficienteOpt.isPresent()) {
			return f.getQuantita().multiply(coefficienteOpt.get());
		}
		return BigDecimal.ZERO;
	};
}
