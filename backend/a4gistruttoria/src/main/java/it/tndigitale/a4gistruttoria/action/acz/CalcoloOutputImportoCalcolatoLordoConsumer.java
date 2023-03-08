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
public class CalcoloOutputImportoCalcolatoLordoConsumer extends VariabiliComplesseAbstractConsumer {

	private static final Logger logger = LoggerFactory.getLogger(CalcoloOutputImportoCalcolatoLordoConsumer.class);

	@Override
	protected BiFunction<CalcoloAccoppiatoHandler, Integer, BigDecimal> salvaVariabiliPerIntervento(IstruttoriaModel istruttoria) {
		return (handler, intervento) -> {
			MapVariabili variabiliOutput = handler.getVariabiliOutput();
			BigDecimal importoAccertato = getValueOrDefault(variabiliOutput, getTipoVariabilePerIntervento("ACZIMPACC", intervento));
			BigDecimal importoRiduzioneRitardo = getValueOrDefault(variabiliOutput, getTipoVariabilePerIntervento("ACZIMPRIDRIT", intervento));
			BigDecimal importoRiduzioneSanzione = getValueOrDefault(variabiliOutput, getTipoVariabilePerIntervento("ACZIMPRIDSANZ", intervento));
			BigDecimal importoCalcolato = null;
			if (importoAccertato != null && importoRiduzioneRitardo != null && importoRiduzioneSanzione != null) {
				importoCalcolato = importoAccertato.subtract(importoRiduzioneRitardo).subtract(importoRiduzioneSanzione);
				variabiliOutput.add(new VariabileCalcolo(getTipoVariabilePerIntervento("ACZIMPCALCLORDO", intervento), ConversioniCalcoli.getImporto(importoCalcolato)));
				logger.debug("Per l'intervento {}: importoAccertato {} importoRiduzioneRitardo {} importoRiduzioneSanzione {} importoCalcolato {}", 
						intervento, importoAccertato, importoRiduzioneRitardo, importoRiduzioneSanzione, importoCalcolato);
			}
			return importoCalcolato;
		};
	}

	@Override
	protected BiConsumer<CalcoloAccoppiatoHandler, BigDecimal> salvaTotale() {
		return (handler, totale) -> {
			handler.getVariabiliOutput().add(TipoVariabile.ACZIMPCALCLORDOTOT, new VariabileCalcolo(TipoVariabile.ACZIMPCALCLORDOTOT, totale));
			logger.debug("Totale importoCalcolato {}", totale);
		};
	}

	@Override
	protected Collector<BigDecimal, ?, BigDecimal> totalizzatore() {
		return Collectors.reducing(BigDecimal.ZERO, BigDecimal::add);
	}
}
