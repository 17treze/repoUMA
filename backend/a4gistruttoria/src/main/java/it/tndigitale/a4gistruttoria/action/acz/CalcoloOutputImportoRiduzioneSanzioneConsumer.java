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
public class CalcoloOutputImportoRiduzioneSanzioneConsumer extends VariabiliComplesseAbstractConsumer {

	private static final Logger logger = LoggerFactory.getLogger(CalcoloOutputImportoRiduzioneSanzioneConsumer.class);

	@Override
	protected BiFunction<CalcoloAccoppiatoHandler, Integer, BigDecimal> salvaVariabiliPerIntervento(IstruttoriaModel istruttoria) {
		return (handler, intervento) -> {
			var campagna = istruttoria.getDomandaUnicaModel().getCampagna();
			MapVariabili variabiliInput = handler.getVariabiliInput();
			MapVariabili variabiliOutput = handler.getVariabiliOutput();
			BigDecimal importoAccertato = getValueOrDefault(variabiliOutput, getTipoVariabilePerIntervento("ACZIMPACC", intervento));
			// recupero i capi ammessi con sazione al fine di verificare che i capi siano > 3 (2021 - IDU-AS-03-03 Calcolo della sanzione)
			VariabileCalcolo variabileCalcolo = variabiliInput.get(getTipoVariabilePerIntervento("ACZCAPISANZ", intervento));
			BigDecimal capiAmmessiConSanzione = variabileCalcolo == null ? BigDecimal.ZERO : variabileCalcolo.getValNumber();
			// recupero la percentuale determinata per applicare sanzioni aggiuntive nel caso in cui sia > 50%  dalla campagna 2021
			BigDecimal percentualeSanzioneDeterminata = getValueOrDefault(variabiliInput, getTipoVariabilePerIntervento("PERCSANZDET", intervento));
			BigDecimal percentualeSanzioneApplicata = getValueOrDefault(variabiliInput, getTipoVariabilePerIntervento("PERCSANZ", intervento));
			BigDecimal importoRiduzioneSanzione = null;
			if (importoAccertato != null && percentualeSanzioneApplicata != null) {
				BigDecimal sanzioneAggiuntiva = BigDecimal.ZERO;
				// 2021 - IDU-AS-03-03 Calcolo della sanzione
				if (campagna > 2020 && capiAmmessiConSanzione.compareTo(BigDecimal.valueOf(3)) > 0 && percentualeSanzioneDeterminata.compareTo(BigDecimal.valueOf(0.5)) > 0) {
					BigDecimal valoreUnitario = getValueOrDefault(variabiliInput, getTipoVariabilePerIntervento("ACZVAL", intervento));
					// imponi sanzione aggiuntiva: sottrarre dal premio capi_anomali * importo unitario
					sanzioneAggiuntiva = capiAmmessiConSanzione.multiply(valoreUnitario);
				}
				importoRiduzioneSanzione = importoAccertato.multiply(percentualeSanzioneApplicata).add(sanzioneAggiuntiva);
				variabiliOutput.add(new VariabileCalcolo(getTipoVariabilePerIntervento("ACZIMPRIDSANZ", intervento), ConversioniCalcoli.getImporto(importoRiduzioneSanzione)));
				logger.debug("Per l'intervento {}: importoAccertato {} percentualeSanzione {} importoRiduzioneSanzione {}", 
						intervento, importoAccertato, percentualeSanzioneApplicata, importoRiduzioneSanzione);
			}
			return importoRiduzioneSanzione;
		};
	}

	@Override
	protected BiConsumer<CalcoloAccoppiatoHandler, BigDecimal> salvaTotale() {
		return (handler, totale) -> {
			handler.getVariabiliOutput().add(TipoVariabile.ACZIMPRIDSANZTOT, new VariabileCalcolo(TipoVariabile.ACZIMPRIDSANZTOT, totale));
			logger.debug("Totale importoRiduzioneSanzione {}", totale);
		};
	}

	@Override
	protected Collector<BigDecimal, ?, BigDecimal> totalizzatore() {
		return Collectors.reducing(BigDecimal.ZERO, BigDecimal::add);
	}
}
