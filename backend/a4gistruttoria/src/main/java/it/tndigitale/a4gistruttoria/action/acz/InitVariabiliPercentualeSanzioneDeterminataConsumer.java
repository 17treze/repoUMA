package it.tndigitale.a4gistruttoria.action.acz;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
public class InitVariabiliPercentualeSanzioneDeterminataConsumer extends VariabiliComplesseAbstractConsumer {
	
	private static final Logger logger = LoggerFactory.getLogger(InitVariabiliPercentualeSanzioneDeterminataConsumer.class);

	@Override
	protected BiFunction<CalcoloAccoppiatoHandler, Integer, BigDecimal> salvaVariabiliPerIntervento(IstruttoriaModel istruttoria)
	{ 
		return (handler, intervento) -> {
			MapVariabili input = handler.getVariabiliInput();
			TipoVariabile tipoCapiRichiesti = getTipoVariabilePerIntervento("ACZCAPIRIC", intervento);
			TipoVariabile tipoCapiAmmessiConSanzione = getTipoVariabilePerIntervento("ACZCAPISANZ", intervento);
			TipoVariabile tipoCapiAccertati = getTipoVariabilePerIntervento("ACZCAPIACC", intervento);
			TipoVariabile tipoPercentualeSanzioneDeterminata = getTipoVariabilePerIntervento("PERCSANZDET", intervento);
			if (input.get(tipoCapiRichiesti) != null 
					&& input.get(tipoCapiRichiesti).getValNumber().compareTo(BigDecimal.ZERO) > 0) {
				BigDecimal percentualeSanzioneDeterminata = BigDecimal.ZERO;
				BigDecimal capiAmmessiConSanzione = input.get(tipoCapiAmmessiConSanzione) == null ? BigDecimal.ZERO : input.get(tipoCapiAmmessiConSanzione).getValNumber();
				BigDecimal capiAccertati = input.get(tipoCapiAccertati) == null ? BigDecimal.ZERO : input.get(tipoCapiAccertati).getValNumber();
				if (capiAccertati.compareTo(BigDecimal.ZERO) == 0) {
					capiAccertati = capiAmmessiConSanzione;
				}
				if (capiAccertati.compareTo(BigDecimal.ZERO) > 0 && capiAmmessiConSanzione.compareTo(BigDecimal.ZERO) > 0) {
					percentualeSanzioneDeterminata = capiAmmessiConSanzione.divide(capiAccertati, 4, RoundingMode.HALF_UP);
				}
				input.add(tipoPercentualeSanzioneDeterminata, new VariabileCalcolo(tipoPercentualeSanzioneDeterminata, percentualeSanzioneDeterminata));
				logger.debug("Per l'intervento {}: capiAmmessiConSanzione {} capiAccertati {} percentualeSanzioneDeterminata {}", 
						intervento, capiAmmessiConSanzione, capiAccertati, percentualeSanzioneDeterminata);
			}
			return BigDecimal.ZERO; // Nessun totale in questo caso
		};
	}

	@Override
	protected BiConsumer<CalcoloAccoppiatoHandler, BigDecimal> salvaTotale() {
		return (handler, totale) -> { }; // In questo caso, non c'è una variabile con il totale delle precedenti
	}

	@Override
	protected Collector<BigDecimal, ?, BigDecimal> totalizzatore() {
		return Collectors.reducing(BigDecimal.ZERO, BigDecimal::add);
	}
}
