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
public class InitVariabiliCapiRichiestiNettiConsumer extends VariabiliComplesseAbstractConsumer {

	private static final Logger logger = LoggerFactory.getLogger(InitVariabiliCapiRichiestiNettiConsumer.class);
	
	@Override
	protected BiFunction<CalcoloAccoppiatoHandler, Integer, BigDecimal> salvaVariabiliPerIntervento(IstruttoriaModel istruttoria) {
		return (handler, intervento) -> {
			MapVariabili input = handler.getVariabiliInput();
			BigDecimal result = BigDecimal.ZERO;
			TipoVariabile tipoCapiRichiesti = getTipoVariabilePerIntervento("ACZCAPIRIC", intervento);
			TipoVariabile tipoCapiDuplicati = getTipoVariabilePerIntervento("ACZCAPIDUP", intervento);
			TipoVariabile tipoCapiRichiestiNetti = getTipoVariabilePerIntervento("ACZCAPIRICNET", intervento);
			VariabileCalcolo vcCapiRichiesti = input.get(tipoCapiRichiesti);
			if (vcCapiRichiesti != null) {
				BigDecimal capiRichiesti = vcCapiRichiesti.getValNumber();
				BigDecimal capiDuplicati = getValueOrDefault(input, tipoCapiDuplicati);
				BigDecimal capiRichiestiNetti = capiRichiesti.subtract(capiDuplicati);
				result = capiRichiestiNetti;
				input.add(tipoCapiRichiestiNetti, new VariabileCalcolo(tipoCapiRichiestiNetti, capiRichiestiNetti));
				logger.debug("Per l'intervento {}: capiRichiesti {} capiDuplicati {} capiRichiestiNetti {}", 
						intervento, capiRichiesti, capiDuplicati, capiRichiestiNetti);
			}
			return result;
		};
	}

	@Override
	protected BiConsumer<CalcoloAccoppiatoHandler, BigDecimal> salvaTotale() {
		return (handler, totale) -> handler.getVariabiliInput().add(TipoVariabile.ACZCAPIRICNETTOT, new VariabileCalcolo(TipoVariabile.ACZCAPIRICNETTOT, totale));
	}

	@Override
	protected Collector<BigDecimal, ?, BigDecimal> totalizzatore() {
		return Collectors.reducing(BigDecimal.ZERO, BigDecimal::add);
	}
}
