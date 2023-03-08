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
public class CalcoloOutputImportoAccertatoConsumer extends VariabiliComplesseAbstractConsumer {

	private static final Logger logger = LoggerFactory.getLogger(CalcoloOutputImportoAccertatoConsumer.class);

	@Override
	protected BiFunction<CalcoloAccoppiatoHandler, Integer, BigDecimal> salvaVariabiliPerIntervento(IstruttoriaModel istruttoria) {
		return (handler, intervento) -> {
			MapVariabili variabiliOutput = handler.getVariabiliOutput();
			BigDecimal importoRichiestoNetto = getValueOrDefault(variabiliOutput, getTipoVariabilePerIntervento("ACZIMPRICNET", intervento));
			BigDecimal importoRiduzione = getValueOrDefault(variabiliOutput, getTipoVariabilePerIntervento("ACZIMPRID", intervento));
			BigDecimal importoAccertato = null;
			if (importoRichiestoNetto != null && importoRiduzione != null) {
				importoAccertato = importoRichiestoNetto.subtract(importoRiduzione);
				variabiliOutput.add(new VariabileCalcolo(getTipoVariabilePerIntervento("ACZIMPACC", intervento), ConversioniCalcoli.getImporto(importoAccertato)));
				logger.debug("Per l'intervento {}: importoRichiestoNetto {} importoRiduzione {} importoAccertato {}", 
						intervento, importoRichiestoNetto, importoRiduzione, importoAccertato);
			}
			return importoAccertato;
		};
	}

	@Override
	protected BiConsumer<CalcoloAccoppiatoHandler, BigDecimal> salvaTotale() {
		return (handler, totale) -> {
			handler.getVariabiliInput().add(TipoVariabile.ACZIMPACCTOT, new VariabileCalcolo(TipoVariabile.ACZIMPACCTOT, totale));
			logger.debug("Totale importoAccertato {}", totale);
		};
	}

	@Override
	protected Collector<BigDecimal, ?, BigDecimal> totalizzatore() {
		return Collectors.reducing(BigDecimal.ZERO, BigDecimal::add);
	}
}
