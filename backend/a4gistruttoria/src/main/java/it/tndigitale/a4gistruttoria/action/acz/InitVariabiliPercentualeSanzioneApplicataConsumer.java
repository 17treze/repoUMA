package it.tndigitale.a4gistruttoria.action.acz;

import java.math.BigDecimal;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;
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
public class InitVariabiliPercentualeSanzioneApplicataConsumer extends VariabiliComplesseAbstractConsumer {

	private static final Logger logger = LoggerFactory.getLogger(InitVariabiliPercentualeSanzioneApplicataConsumer.class);

	@Override
	protected BiFunction<CalcoloAccoppiatoHandler, Integer, BigDecimal> salvaVariabiliPerIntervento(IstruttoriaModel istruttoria)
	{ 
		return (handler, intervento) -> {
			var campagna = istruttoria.getDomandaUnicaModel().getCampagna();
			MapVariabili input = handler.getVariabiliInput();
			TipoVariabile tipoCapiAmmessiConSanzione = getTipoVariabilePerIntervento("ACZCAPISANZ", intervento); 
			TipoVariabile tipoPercentualeSanzioneDeterminata = getTipoVariabilePerIntervento("PERCSANZDET", intervento);
			TipoVariabile tipoPercentualeSanzioneApplicata = getTipoVariabilePerIntervento("PERCSANZ", intervento);
			if (input.get(tipoPercentualeSanzioneDeterminata) != null) {
				BigDecimal capiAmmessiConSanzione = input.get(tipoCapiAmmessiConSanzione) == null ? BigDecimal.ZERO : input.get(tipoCapiAmmessiConSanzione).getValNumber();
				BigDecimal percentualeSanzioneDeterminata = input.get(tipoPercentualeSanzioneDeterminata).getValNumber();
				BigDecimal percentualeSanzioneApplicata = calcolaPercentualeSanzioneApplicata(campagna, capiAmmessiConSanzione, percentualeSanzioneDeterminata);
				input.add(tipoPercentualeSanzioneApplicata, new VariabileCalcolo(tipoPercentualeSanzioneApplicata, percentualeSanzioneApplicata));
				logger.debug("Per l'intervento {}: capiAmmessiConSanzione {} percentualeSanzioneDeterminata {} percentualeSanzioneApplicata {}", 
						intervento, capiAmmessiConSanzione, percentualeSanzioneDeterminata, percentualeSanzioneApplicata);
			}
			return BigDecimal.ZERO; // Nessun totale in questo caso
		};
	}

	// Calcolo sanzione per singolo intervento -> calcolo sanzione con differenziazione su anno prima e dopo 2020
	private BigDecimal calcolaPercentualeSanzioneApplicata(Integer campagna, BigDecimal capiAmmessiConSanzione, BigDecimal percentualeSanzioneDeterminata) {

		Predicate<BigDecimal> capiUguale0 = p -> capiAmmessiConSanzione.compareTo(BigDecimal.ZERO) == 0;
		Predicate<BigDecimal> capiMinoreUguale3 = p -> capiAmmessiConSanzione.compareTo(BigDecimal.valueOf(3)) <= 0;
		Predicate<BigDecimal> capiMaggiore3 = p -> capiAmmessiConSanzione.compareTo(BigDecimal.valueOf(3)) > 0;
		Predicate<BigDecimal> percentualeMinoreUguale10 = p -> percentualeSanzioneDeterminata.compareTo(BigDecimal.valueOf(0.10)) <= 0;
		Predicate<BigDecimal> percentualeCompresaTra10e20 = p -> percentualeSanzioneDeterminata.compareTo(BigDecimal.valueOf(0.10)) > 0 
				&& percentualeSanzioneDeterminata.compareTo(BigDecimal.valueOf(0.20)) < 0;
		Predicate<BigDecimal> percentualeMaggiore20 = p -> percentualeSanzioneDeterminata.compareTo(BigDecimal.valueOf(0.20)) >= 0;
		// Campagna > 2020
		Predicate<BigDecimal> percentualeMinoreUguale20 = p -> percentualeSanzioneDeterminata.compareTo(BigDecimal.valueOf(0.20)) <= 0;
		Predicate<BigDecimal> percentualeCompresaTra20e30 = p -> percentualeSanzioneDeterminata.compareTo(BigDecimal.valueOf(0.20)) > 0 
				&& percentualeSanzioneDeterminata.compareTo(BigDecimal.valueOf(0.30)) <= 0;
		Predicate<BigDecimal> percentualeCompresaTra30e50 = p -> percentualeSanzioneDeterminata.compareTo(BigDecimal.valueOf(0.30)) > 0 
				&& percentualeSanzioneDeterminata.compareTo(BigDecimal.valueOf(0.50)) <= 0;
		Predicate<BigDecimal> percentualeMaggioreDi50 = p -> percentualeSanzioneDeterminata.compareTo(BigDecimal.valueOf(0.50)) > 0;

		BigDecimal sanzione = BigDecimal.ZERO;
		if (campagna.intValue() <= 2020) {
			if (capiUguale0.test(BigDecimal.ZERO)) {
				sanzione = BigDecimal.ZERO;
			} else if (capiMinoreUguale3.test(BigDecimal.ZERO)) {
				sanzione = percentualeSanzioneDeterminata;
			} else if (capiMaggiore3.and(percentualeMinoreUguale10).test(BigDecimal.ZERO)) {
				sanzione = percentualeSanzioneDeterminata;
			} else if (capiMaggiore3.and(percentualeCompresaTra10e20).test(BigDecimal.ZERO)) {
				sanzione = percentualeSanzioneDeterminata.multiply(BigDecimal.valueOf(2));
			} else if (capiMaggiore3.and(percentualeMaggiore20).test(BigDecimal.ZERO)) {
				sanzione = BigDecimal.valueOf(1);
			}
		} else { // CAMPAGNA > 2020
			if (capiMinoreUguale3.test(BigDecimal.ZERO)) {
				sanzione = BigDecimal.ZERO;
			} else if (capiMaggiore3.and(percentualeMinoreUguale20).test(BigDecimal.ZERO)) {
				sanzione = percentualeSanzioneDeterminata;
			} else if (capiMaggiore3.and(percentualeCompresaTra20e30).test(BigDecimal.ZERO)) {
				sanzione = percentualeSanzioneDeterminata.multiply(BigDecimal.valueOf(2));
			} else if (capiMaggiore3.and(percentualeCompresaTra30e50).test(BigDecimal.ZERO)) {
				sanzione = BigDecimal.valueOf(1);
			} else if (capiMaggiore3.and(percentualeMaggioreDi50).test(BigDecimal.ZERO)) {
				sanzione = BigDecimal.valueOf(1); // + Sanzione Aggiuntiva - vedi CalcoloOutputImportoRiduzioneSanzioneConsumer
			}
		}
		sanzione = sanzione.min(BigDecimal.valueOf(1));
		return sanzione;
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
