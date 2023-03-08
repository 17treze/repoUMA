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
public class InitVariabiliCapiAccertatiConsumer extends VariabiliComplesseAbstractConsumer {
	
	private static final Logger logger = LoggerFactory.getLogger(InitVariabiliCapiAccertatiConsumer.class);

	@Override
	protected BiFunction<CalcoloAccoppiatoHandler, Integer, BigDecimal> salvaVariabiliPerIntervento(IstruttoriaModel istruttoria)
	{ 
		return (handler, intervento) -> {
			MapVariabili input = handler.getVariabiliInput();
			BigDecimal result = BigDecimal.ZERO;
			TipoVariabile tipoCapiRichiestiNetti = getTipoVariabilePerIntervento("ACZCAPIRICNET", intervento);
			TipoVariabile tipoCapiSanzionati = getTipoVariabilePerIntervento("ACZCAPISANZ", intervento);
			TipoVariabile tipoCapiAccertati = getTipoVariabilePerIntervento("ACZCAPIACC", intervento);
			VariabileCalcolo vcCapiRichiestiNetti = input.get(tipoCapiRichiestiNetti);
			if (vcCapiRichiestiNetti != null) {
				BigDecimal capiRichiestiNetti = vcCapiRichiestiNetti.getValNumber();
				BigDecimal capiSanzionati = getValueOrDefault(input, tipoCapiSanzionati);
				BigDecimal capiAccertati = capiRichiestiNetti.subtract(capiSanzionati);
				input.add(tipoCapiAccertati, new VariabileCalcolo(tipoCapiAccertati, capiAccertati));
				logger.debug("Per l'intervento {}: capiRichiestiNetti {} capiSanzionati {} capiAccertati {}", 
						intervento, capiRichiestiNetti, capiSanzionati, capiAccertati);
			}
			return result; 
		};
	}

	@Override
	protected BiConsumer<CalcoloAccoppiatoHandler, BigDecimal> salvaTotale() {
		return (handler, totale) -> handler.getVariabiliInput().add(TipoVariabile.ACZCAPIACCTOT, new VariabileCalcolo(TipoVariabile.ACZCAPIACCTOT, totale));
	}

	@Override
	protected Collector<BigDecimal, ?, BigDecimal> totalizzatore() {
		return Collectors.reducing(BigDecimal.ZERO, BigDecimal::add);
	}
}
