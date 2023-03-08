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
public class CalcoloOutputImportoRichiestoNettoConsumer extends VariabiliComplesseAbstractConsumer {

	private static final Integer INTERVENTO_320 = 320;
	private static final Logger logger = LoggerFactory.getLogger(CalcoloOutputImportoRichiestoNettoConsumer.class);
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
			BigDecimal capiRichiestiNetti = BigDecimal.ZERO;
			if (intervento.intValue() != INTERVENTO_320.intValue()) {
				capiRichiestiNetti = getValueOrDefault(variabiliInput, getTipoVariabilePerIntervento("ACZCAPIRICNET", intervento));
			} else {
				capiRichiestiNetti = calcoloAgnellePremiabili(variabiliInput);
			}
			BigDecimal valoreUnitario = getValueOrDefault(variabiliInput, getTipoVariabilePerIntervento("ACZVAL", intervento));
			BigDecimal importoRichiestoNetto = null;
			if (capiRichiestiNetti != null && valoreUnitario != null) {
				importoRichiestoNetto = capiRichiestiNetti.multiply(valoreUnitario).multiply(BigDecimal.valueOf(coefficientiIntervento.get(intervento)));
				variabiliOutput.add(new VariabileCalcolo(getTipoVariabilePerIntervento("ACZIMPRICNET", intervento), ConversioniCalcoli.getImporto(importoRichiestoNetto)));
				logger.debug("Per l'intervento {}: capiRichiestiNetti {} valoreUnitario {} importoRichiestoNetto {}", 
						intervento, capiRichiestiNetti, valoreUnitario, importoRichiestoNetto);
			}
			return importoRichiestoNetto;
		};
	}

	@Override
	protected BiConsumer<CalcoloAccoppiatoHandler, BigDecimal> salvaTotale() {
		return (handler, totale) -> {
			handler.getVariabiliInput().add(TipoVariabile.ACZIMPRICNETTOT, new VariabileCalcolo(TipoVariabile.ACZIMPRICNETTOT, totale));
			logger.debug("Totale importoRichiestoNetto {}", totale);
		};
	}

	@Override
	protected Collector<BigDecimal, ?, BigDecimal> totalizzatore() {
		return Collectors.reducing(BigDecimal.ZERO, BigDecimal::add);
	}


	// Calcolo Agnelle premiabili - intervento 320
	private BigDecimal calcoloAgnellePremiabili(MapVariabili variabiliInput) {
		// lettura della variabile ACZCAPIRIC_320: A = numeroCapiRichiesti 320
		BigDecimal a = getValueOrDefault(variabiliInput, TipoVariabile.ACZCAPIRIC_320);

		// lettura delle variabile ACZOVIADULTI_320: B = oviniAdulti * 0.03
		BigDecimal b = getValueOrDefault(variabiliInput, TipoVariabile.ACZOVIADULTI_320);

		// min (A, B)
		return b.min(new BigDecimal(a.intValue()));
	}
}
