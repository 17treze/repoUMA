package it.tndigitale.a4gistruttoria.action.acz;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.lavorazione.MapVariabili;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloAccoppiatoHandler;
import it.tndigitale.a4gistruttoria.util.ConversioniCalcoli;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@Component
public class CalcoloOutputImportoRiduzioneConsumer extends VariabiliComplesseAbstractConsumer {

	private static final Logger logger = LoggerFactory.getLogger(CalcoloOutputImportoRiduzioneConsumer.class);
	private static Map<Integer, Double> coefficientiIntervento;
	static {
		coefficientiIntervento = new HashMap<>();
		coefficientiIntervento.put(310, Double.valueOf(1.0));
		coefficientiIntervento.put(310, Double.valueOf(1.0));
		coefficientiIntervento.put(311, Double.valueOf(1.0));
		coefficientiIntervento.put(313, Double.valueOf(1.0));
		coefficientiIntervento.put(322, Double.valueOf(1.0));
		coefficientiIntervento.put(315, Double.valueOf(1.0));
		coefficientiIntervento.put(316, Double.valueOf(1.0));
		coefficientiIntervento.put(318, Double.valueOf(1.0));
		coefficientiIntervento.put(320, Double.valueOf(0.75));
		coefficientiIntervento.put(321, Double.valueOf(0.75));
	}

	@Override
	protected BiFunction<CalcoloAccoppiatoHandler, Integer, BigDecimal> salvaVariabiliPerIntervento(IstruttoriaModel istruttoria) {
		return (handler, intervento) -> {
			MapVariabili variabiliOutput = handler.getVariabiliOutput();
			MapVariabili variabiliInput = handler.getVariabiliInput();
			BigDecimal capiSanzionati = getValueOrDefault(variabiliInput, getTipoVariabilePerIntervento("ACZCAPISANZ", intervento));
			BigDecimal valoreUnitario = getValueOrDefault(variabiliInput, getTipoVariabilePerIntervento("ACZVAL", intervento));
			BigDecimal importoRiduzione = null;
			if (capiSanzionati != null && valoreUnitario != null) {
				importoRiduzione = capiSanzionati.multiply(valoreUnitario).multiply(BigDecimal.valueOf(coefficientiIntervento.get(intervento)));
				variabiliOutput.add(new VariabileCalcolo(getTipoVariabilePerIntervento("ACZIMPRID", intervento), ConversioniCalcoli.getImporto(importoRiduzione)));
				logger.debug("Per l'intervento {}: capiSanzionati {} valoreUnitario {} importoRiduzione {}", 
						intervento, capiSanzionati, valoreUnitario, importoRiduzione);
			}
			return importoRiduzione;
		};
	}

	@Override
	protected BiConsumer<CalcoloAccoppiatoHandler, BigDecimal> salvaTotale() {
		return (handler, totale) -> {
			handler.getVariabiliOutput().add(TipoVariabile.ACZIMPRIDTOT, new VariabileCalcolo(TipoVariabile.ACZIMPRIDTOT, totale));
			logger.debug("Totale importoRiduzione {}", totale);
		};
	}

	@Override
	protected Collector<BigDecimal, ?, BigDecimal> totalizzatore() {
		return Collectors.reducing(BigDecimal.ZERO, BigDecimal::add);
	}
}
