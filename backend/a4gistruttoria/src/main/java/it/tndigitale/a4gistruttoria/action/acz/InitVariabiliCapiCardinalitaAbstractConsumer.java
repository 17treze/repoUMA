package it.tndigitale.a4gistruttoria.action.acz;

import it.tndigitale.a4gistruttoria.dto.lavorazione.MapVariabili;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.*;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloAccoppiatoHandler;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class InitVariabiliCapiCardinalitaAbstractConsumer implements BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel> {
	
	private static final Logger logger = LoggerFactory.getLogger(InitVariabiliCapiCardinalitaAbstractConsumer.class);

	@Override
	public void accept(CalcoloAccoppiatoHandler handler, IstruttoriaModel istruttoria) {
		Map<CodiceInterventoAgs, Long> mappaCardinalitaIntervento = calcolaCardinalitaRichiestiPerIntervento(istruttoria);
		popolaVariabiliInputDaMappa(handler.getVariabiliInput(), mappaCardinalitaIntervento);
	}
	
	protected Long calcolaTotale(Map<CodiceInterventoAgs, Long> mappaCardinalitaIntervento) {
		return mappaCardinalitaIntervento.values().stream().collect(Collectors.summingLong(Long::longValue));
	}
	
	protected abstract void popolaVariabiliInputDaMappa(MapVariabili inputListaVariabiliCalcolo,
			Map<CodiceInterventoAgs, Long> mappaCardinalitaIntervento);
	
	protected void addVariabile(MapVariabili inputListaVariabiliCalcolo,
								Map<CodiceInterventoAgs, Long> mappaCardinalitaIntervento,
								CodiceInterventoAgs intervento,
								TipoVariabile tipoVariabile) {
		Long count = mappaCardinalitaIntervento.get(intervento);
		if (isInterventoRichiesto(count)) {
			inputListaVariabiliCalcolo.add(new VariabileCalcolo(tipoVariabile, BigDecimal.valueOf(count)));
			logger.debug("Per l'intervento {}: capi {}", intervento, count);
		}
	}
	
	private boolean isInterventoRichiesto(Long count) {
		return (count != null && count > 0);
	}
	
	protected Map<CodiceInterventoAgs, Long> calcolaCardinalitaRichiestiPerIntervento(IstruttoriaModel istruttoria) {
		DomandaUnicaModel domanda = istruttoria.getDomandaUnicaModel();
		Set<AllevamentoImpegnatoModel> allevamentiDomandaRichiesti = domanda.getAllevamentiImpegnati();
		Predicate<? super EsitoCalcoloCapoModel> predicate = predicatoFiltro();
		Map<CodiceInterventoAgs, Long> mappaCardinalitaIntervento = new EnumMap<>(CodiceInterventoAgs.class);
		allevamentiDomandaRichiesti.forEach(allevamento -> {
			Set<EsitoCalcoloCapoModel> esitiAllevamento = allevamento.getEsitiCalcoloCapi();
			long totaleIntervento = esitiAllevamento.stream().filter(predicate::test).count();
			aggiornaMappa(mappaCardinalitaIntervento,
					allevamento.getIntervento().getIdentificativoIntervento(), totaleIntervento);
		});
		return mappaCardinalitaIntervento;
	}
	
	protected abstract Predicate<? super EsitoCalcoloCapoModel> predicatoFiltro();
	
	protected void aggiornaMappa(Map<CodiceInterventoAgs, Long> mappaCardinalitaIntervento,
								 CodiceInterventoAgs intervento,
								 long totaleIntervento) {
		if (totaleIntervento > 0) {
			Long totale = mappaCardinalitaIntervento.get(intervento);
			if (totale == null) totale = 0L;
			totale += totaleIntervento;
			mappaCardinalitaIntervento.put(intervento, totale);
		}
	}

}
