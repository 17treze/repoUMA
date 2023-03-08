package it.tndigitale.a4gistruttoria.action.acz;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Collector;

import it.tndigitale.a4gistruttoria.dto.lavorazione.MapVariabili;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloAccoppiatoHandler;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

public abstract class VariabiliComplesseAbstractConsumer implements BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel> {

	/**
	 * Per ciascun intervento applico il popolamento delle variabili relative, 
	 * applico un totalizzatore e applico il salvataggio del totale
	 */
	@Override
	public void accept(CalcoloAccoppiatoHandler handler, IstruttoriaModel istruttoria) {

		List<Integer> interventi = Arrays.asList(310, 311, 313, 322, 315, 316, 318, 320, 321);

		BiConsumer<CalcoloAccoppiatoHandler, BigDecimal> totale = salvaTotale();
		BiFunction<CalcoloAccoppiatoHandler, Integer, BigDecimal> variabili = salvaVariabiliPerIntervento(istruttoria);
		totale.accept(
				handler, 
				interventi.stream()
					.map(intervento -> variabili.apply(handler, intervento))
					.collect(totalizzatore()));
	}
	
	/**
	 * Ritorna il valore della variabile richiesta all'interno della mappa di variabili
	 * se questo esiste, altrimenti BigDecimal.ZERO
	 */
	protected BigDecimal getValueOrDefault(MapVariabili variabili, TipoVariabile tipoVariabile) {
		BigDecimal result = BigDecimal.ZERO;
		VariabileCalcolo vc = variabili.get(tipoVariabile);
		if (vc != null) {
			result = vc.getValNumber();
			if (result == null) result = BigDecimal.ZERO;
		}
		return result;
	}
	
	/**
	 * Ritorna il tipo variabile corrispondente al prefisso relativo all'intervento passato
	 */
	protected TipoVariabile getTipoVariabilePerIntervento(String prefissoVariabile, Integer intervento) {
		return TipoVariabile.valueOf(prefissoVariabile.concat("_").concat(intervento.toString()));
	}

	/**
	 * Funzione da implementare che salva le variabili per ciascun intervento
	 */
	protected abstract BiFunction<CalcoloAccoppiatoHandler, Integer, BigDecimal> salvaVariabiliPerIntervento(IstruttoriaModel istruttoria);
	
	/**
	 * Funzione da implementare che salva il totale delle variabili per intervento nella variabile relativa
	 */
	protected abstract BiConsumer<CalcoloAccoppiatoHandler, BigDecimal> salvaTotale();

	/**
	 * Funzione che effettua il totale delle variabili per intervento
	 */
	protected abstract Collector<BigDecimal, ?, BigDecimal> totalizzatore();
}
