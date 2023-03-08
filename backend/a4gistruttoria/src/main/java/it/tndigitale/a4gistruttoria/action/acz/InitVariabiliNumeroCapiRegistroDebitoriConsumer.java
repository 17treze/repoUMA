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
public class InitVariabiliNumeroCapiRegistroDebitoriConsumer extends VariabiliComplesseAbstractConsumer {
	
	private static final Logger logger = LoggerFactory.getLogger(InitVariabiliNumeroCapiRegistroDebitoriConsumer.class);

	@Override
	protected BiFunction<CalcoloAccoppiatoHandler, Integer, BigDecimal> salvaVariabiliPerIntervento(IstruttoriaModel istruttoria)
	{ 
		return (handler, intervento) -> {
			MapVariabili input = handler.getVariabiliInput();
			BigDecimal result = BigDecimal.ZERO;
			TipoVariabile percSanzioneDet = getTipoVariabilePerIntervento("PERCSANZDET", intervento);
			TipoVariabile percSanzione = getTipoVariabilePerIntervento("PERCSANZ", intervento);
			TipoVariabile numeroCapiNetti = getTipoVariabilePerIntervento("ACZCAPIRICNET", intervento);
			TipoVariabile numeroCapiAccertati = getTipoVariabilePerIntervento("ACZCAPIACC", intervento);
			TipoVariabile numeroCapiConSanzione = getTipoVariabilePerIntervento("ACZCAPISANZ", intervento);
			TipoVariabile numeroCapiDebitori = getTipoVariabilePerIntervento("ACZCAPIDEB", intervento);
			VariabileCalcolo vcPercSanzioneDet = input.get(percSanzioneDet);
			VariabileCalcolo vcPercSanzione = input.get(percSanzione);
			VariabileCalcolo vcNumCapiSanz = input.get(numeroCapiConSanzione);
			if (vcPercSanzioneDet != null && vcPercSanzioneDet.getValNumber().compareTo(BigDecimal.valueOf(0.5)) > 0 
					&& vcPercSanzione != null && vcPercSanzione.getValNumber().compareTo(BigDecimal.valueOf(1.0)) == 0
					&& vcNumCapiSanz != null && vcNumCapiSanz.getValNumber().compareTo(BigDecimal.valueOf(3.0)) > 0) {
				BigDecimal capiNetti = getValueOrDefault(input, numeroCapiNetti);
				BigDecimal capiAccertati = getValueOrDefault(input, numeroCapiAccertati);
				BigDecimal capiDebitori = capiNetti.subtract(capiAccertati);
				result = capiDebitori;
				input.add(numeroCapiDebitori, new VariabileCalcolo(numeroCapiDebitori, capiDebitori));
				logger.debug("Per l'intervento {}: capiNetti {} capiAccertati {} capiDebitori {}", 
						intervento, capiNetti, capiAccertati, capiDebitori);
			}
			return result; 
		};
	}

	@Override
	protected BiConsumer<CalcoloAccoppiatoHandler, BigDecimal> salvaTotale() {
		return (handler, totale) -> handler.getVariabiliInput().add(TipoVariabile.ACZCAPIDEBTOT, new VariabileCalcolo(TipoVariabile.ACZCAPIDEBTOT, totale));
	}

	@Override
	protected Collector<BigDecimal, ?, BigDecimal> totalizzatore() {
		return Collectors.reducing(BigDecimal.ZERO, BigDecimal::add);
	}
}
