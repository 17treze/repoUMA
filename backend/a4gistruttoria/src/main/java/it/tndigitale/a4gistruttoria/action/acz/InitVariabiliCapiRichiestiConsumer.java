package it.tndigitale.a4gistruttoria.action.acz;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.lavorazione.MapVariabili;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.CodiceInterventoAgs;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapoModel;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@Component
public class InitVariabiliCapiRichiestiConsumer extends InitVariabiliCapiCardinalitaAbstractConsumer {
	
	private static final Logger logger = LoggerFactory.getLogger(InitVariabiliCapiRichiestiConsumer.class);

	protected void popolaVariabiliInputDaMappa(MapVariabili inputListaVariabiliCalcolo, Map<CodiceInterventoAgs, Long> mappaCardinalitaIntervento) {
		logger.debug("Inserimento variabili capi richiesti - Inizio");
		addVariabile(inputListaVariabiliCalcolo, mappaCardinalitaIntervento, CodiceInterventoAgs.LATTE,
				TipoVariabile.ACZCAPIRIC_310);
		addVariabile(inputListaVariabiliCalcolo, mappaCardinalitaIntervento, CodiceInterventoAgs.LATTE_BMONT,
				TipoVariabile.ACZCAPIRIC_311);
		addVariabile(inputListaVariabiliCalcolo, mappaCardinalitaIntervento, CodiceInterventoAgs.BOVINI_VAC,
				TipoVariabile.ACZCAPIRIC_313);
		addVariabile(inputListaVariabiliCalcolo, mappaCardinalitaIntervento, CodiceInterventoAgs.BOVINI_VAC_NO_ISCR,
				TipoVariabile.ACZCAPIRIC_322);
		addVariabile(inputListaVariabiliCalcolo, mappaCardinalitaIntervento, CodiceInterventoAgs.BOVINI_MAC,
				TipoVariabile.ACZCAPIRIC_315);
		addVariabile(inputListaVariabiliCalcolo, mappaCardinalitaIntervento, CodiceInterventoAgs.BOVINI_MAC_12M,
				TipoVariabile.ACZCAPIRIC_316);
		addVariabile(inputListaVariabiliCalcolo, mappaCardinalitaIntervento, CodiceInterventoAgs.BOVINI_MAC_ETIC,
				TipoVariabile.ACZCAPIRIC_318);
		addVariabile(inputListaVariabiliCalcolo, mappaCardinalitaIntervento, CodiceInterventoAgs.OVICAP_AGN,
				TipoVariabile.ACZCAPIRIC_320);
		addVariabile(inputListaVariabiliCalcolo, mappaCardinalitaIntervento, CodiceInterventoAgs.OVICAP_MAC,
				TipoVariabile.ACZCAPIRIC_321);
		
		inputListaVariabiliCalcolo.add(new VariabileCalcolo(TipoVariabile.ACZCAPIRICTOT, BigDecimal.valueOf(calcolaTotale(mappaCardinalitaIntervento))));
		
		logger.debug("Inserimento variabili capi richiesti - Fine");
	}
	
	protected Predicate<? super EsitoCalcoloCapoModel> predicatoFiltro() {
		return (esito -> {
			return Boolean.TRUE.equals(esito.getRichiesto());
//			List<A4gtDomandaIntegrativa> domandeIntegrative = esito.getA4gtDomandaIntegrativas();
//			return (!domandeIntegrative.isEmpty() && StatoDomandaIntegrativa.PRESENTATA.name().equals(domandeIntegrative.get(0).getStato()));
		});
	}

}
