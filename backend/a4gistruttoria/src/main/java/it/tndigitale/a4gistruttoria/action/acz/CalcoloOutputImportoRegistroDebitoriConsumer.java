package it.tndigitale.a4gistruttoria.action.acz;

import java.math.BigDecimal;
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
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@Component
public class CalcoloOutputImportoRegistroDebitoriConsumer extends VariabiliComplesseAbstractConsumer {
	
	private static final Logger logger = LoggerFactory.getLogger(CalcoloOutputImportoRegistroDebitoriConsumer.class);

	@Override
	protected BiFunction<CalcoloAccoppiatoHandler, Integer, BigDecimal> salvaVariabiliPerIntervento(IstruttoriaModel istruttoria)
	{ 
		return (handler, intervento) -> {
			MapVariabili input = handler.getVariabiliInput();
			MapVariabili variabiliOutput = handler.getVariabiliOutput();
			BigDecimal result = BigDecimal.ZERO;
			TipoVariabile numeroCapiDebitori = getTipoVariabilePerIntervento("ACZCAPIDEB", intervento);
			TipoVariabile valoreUnitario = getTipoVariabilePerIntervento("ACZVAL", intervento);
			TipoVariabile importo = getTipoVariabilePerIntervento("ACZIMPDEB", intervento);
			VariabileCalcolo vcNumeroCapiDebitori = input.get(numeroCapiDebitori);
			VariabileCalcolo vcValoreUnitario = input.get(valoreUnitario);
			if (vcNumeroCapiDebitori != null && vcValoreUnitario != null ) {
				BigDecimal numeroCapiDebitoriVal = vcNumeroCapiDebitori.getValNumber();
				BigDecimal valoreUnitarioVal = vcValoreUnitario.getValNumber();
				BigDecimal importoVal = numeroCapiDebitoriVal.multiply(valoreUnitarioVal);
				result = importoVal;
				variabiliOutput.add(importo, new VariabileCalcolo(importo, importoVal));
				logger.debug("Per l'intervento {}: numeroCapiDebitori {} valoreUnitario {} importo {}", 
						intervento, numeroCapiDebitoriVal, valoreUnitarioVal, importoVal);
			}
			return result; 
		};
	}

	@Override
	protected BiConsumer<CalcoloAccoppiatoHandler, BigDecimal> salvaTotale() {
		return (handler, totale) -> handler.getVariabiliOutput().add(TipoVariabile.ACZIMPDEBCTOT, new VariabileCalcolo(TipoVariabile.ACZIMPDEBCTOT, totale));
	}

	@Override
	protected Collector<BigDecimal, ?, BigDecimal> totalizzatore() {
		return Collectors.reducing(BigDecimal.ZERO, BigDecimal::add);
	}
}
