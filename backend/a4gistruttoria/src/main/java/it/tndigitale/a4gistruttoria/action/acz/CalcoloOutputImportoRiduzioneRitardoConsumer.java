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
import it.tndigitale.a4gistruttoria.util.ConversioniCalcoli;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@Component
public class CalcoloOutputImportoRiduzioneRitardoConsumer extends VariabiliComplesseAbstractConsumer {

	private static final Logger logger = LoggerFactory.getLogger(CalcoloOutputImportoRiduzioneRitardoConsumer.class);

	@Override
	protected BiFunction<CalcoloAccoppiatoHandler, Integer, BigDecimal> salvaVariabiliPerIntervento(IstruttoriaModel istruttoria) {
		return (handler, intervento) -> {
			MapVariabili variabiliOutput = handler.getVariabiliOutput();
			MapVariabili variabiliInput = handler.getVariabiliInput();
			BigDecimal importoAccertato = getValueOrDefault(variabiliOutput, getTipoVariabilePerIntervento("ACZIMPACC", intervento));
			BigDecimal importoRiduzioneSanzione = getValueOrDefault(variabiliOutput, getTipoVariabilePerIntervento("ACZIMPRIDSANZ", intervento));
			BigDecimal percentualeRitardo = getValueOrDefault(variabiliInput, TipoVariabile.PERCRIT);
			BigDecimal importoRiduzioneRitardo = null;
			if (importoAccertato != null && importoRiduzioneSanzione != null) {
				if (variabiliInput.get(TipoVariabile.PERCRITISTR) != null && variabiliInput.get(TipoVariabile.PERCRITISTR).getValBoolean() != null
						&& variabiliInput.get(TipoVariabile.PERCRITISTR).getValBoolean()) {
					importoRiduzioneRitardo = BigDecimal.ZERO;
				} else {
					importoRiduzioneRitardo = importoAccertato.subtract(importoRiduzioneSanzione).multiply(percentualeRitardo);
				}
				variabiliOutput.add(
						new VariabileCalcolo(getTipoVariabilePerIntervento("ACZIMPRIDRIT", intervento),
						ConversioniCalcoli.getImporto(importoRiduzioneRitardo)));
				logger.debug("Per l'intervento {}: importoAccertato {} importoRiduzioneSanzione {} percentualeRitardo {} importoRiduzioneRitardo {}", 
						intervento, importoAccertato, importoRiduzioneSanzione, percentualeRitardo, importoRiduzioneRitardo);
			}
			return importoRiduzioneRitardo;
		};
	}

	@Override
	protected BiConsumer<CalcoloAccoppiatoHandler, BigDecimal> salvaTotale() {
		return (handler, totale) -> {
			handler.getVariabiliOutput().add(TipoVariabile.ACZIMPRIDRITTOT, new VariabileCalcolo(TipoVariabile.ACZIMPRIDRITTOT, totale));
			logger.debug("Totale importoRiduzioneRitardo {}", totale);
		};
	}

	@Override
	protected Collector<BigDecimal, ?, BigDecimal> totalizzatore() {
		return Collectors.reducing(BigDecimal.ZERO, BigDecimal::add);
	}
}
