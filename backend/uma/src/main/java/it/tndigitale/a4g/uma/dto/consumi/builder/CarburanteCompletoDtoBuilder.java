package it.tndigitale.a4g.uma.dto.consumi.builder;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.framework.support.CustomCollectors;
import it.tndigitale.a4g.uma.business.persistence.entity.ConsuntivoConsumiModel;
import it.tndigitale.a4g.uma.business.persistence.entity.TipoConsuntivo;
import it.tndigitale.a4g.uma.dto.richiesta.CarburanteCompletoDto;

public class CarburanteCompletoDtoBuilder {

	private CarburanteCompletoDto carburanteCompletoDto;

	public CarburanteCompletoDtoBuilder() {
		carburanteCompletoDto = new CarburanteCompletoDto().setBenzina(0).setGasolio(0).setGasolioSerre(0).setGasolioTerzi(0);
	}

	public CarburanteCompletoDtoBuilder newDto() {
		carburanteCompletoDto = new CarburanteCompletoDto().setBenzina(0).setGasolio(0).setGasolioSerre(0).setGasolioTerzi(0);
		return this;
	}

	// i consuntivi sono unique by TipoConsuntivo and Tipologia di carburante per ogni dichiarazione consumi.
	public CarburanteCompletoDtoBuilder from(List<ConsuntivoConsumiModel> consuntiviModel, TipoConsuntivo tipoConsuntivo) {
		if (!CollectionUtils.isEmpty(consuntiviModel)) {

			consuntiviModel.stream()
			.filter(c -> tipoConsuntivo.equals(c.getTipoConsuntivo()))
			.collect(Collectors.groupingBy(ConsuntivoConsumiModel::getTipoCarburante, CustomCollectors.toSingleton()))
			.forEach((tipoCarburante, consuntivo) -> {
				var quantita = consuntivo.getQuantita().intValue();
				switch (tipoCarburante) {
				case BENZINA:
					carburanteCompletoDto.setBenzina(quantita);
					break;
				case GASOLIO:
					carburanteCompletoDto.setGasolio(quantita);
					break;
				case GASOLIO_SERRE:
					carburanteCompletoDto.setGasolioSerre(quantita);
					break;
				case GASOLIO_TERZI:
					carburanteCompletoDto.setGasolioTerzi(quantita);
					break;
				default:
					break;
				}
			});
		}
		return this;
	}
	
	public CarburanteCompletoDto build() {
		return carburanteCompletoDto;
	}
}
