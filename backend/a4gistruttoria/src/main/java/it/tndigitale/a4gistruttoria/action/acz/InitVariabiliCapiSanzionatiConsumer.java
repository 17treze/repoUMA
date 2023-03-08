package it.tndigitale.a4gistruttoria.action.acz;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import it.tndigitale.a4gistruttoria.repository.model.CodiceInterventoAgs;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapoModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.lavorazione.MapVariabili;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.A4gtDomandaIntegrativa;
import it.tndigitale.a4gistruttoria.util.StatoDomandaIntegrativa;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapo;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@Component
public class InitVariabiliCapiSanzionatiConsumer extends InitVariabiliCapiCardinalitaAbstractConsumer {
	
	private static final Logger logger = LoggerFactory.getLogger(InitVariabiliCapiSanzionatiConsumer.class);

	protected void popolaVariabiliInputDaMappa(MapVariabili inputListaVariabiliCalcolo, Map<CodiceInterventoAgs, Long> mappaCardinalitaIntervento) {
		logger.debug("Inserimento variabili capi sanzionati - Inizio");
		addVariabile(inputListaVariabiliCalcolo, mappaCardinalitaIntervento, CodiceInterventoAgs.LATTE,
				TipoVariabile.ACZCAPISANZ_310);
		addVariabile(inputListaVariabiliCalcolo, mappaCardinalitaIntervento, CodiceInterventoAgs.LATTE_BMONT,
				TipoVariabile.ACZCAPISANZ_311);
		addVariabile(inputListaVariabiliCalcolo, mappaCardinalitaIntervento, CodiceInterventoAgs.BOVINI_VAC,
				TipoVariabile.ACZCAPISANZ_313);
		addVariabile(inputListaVariabiliCalcolo, mappaCardinalitaIntervento, CodiceInterventoAgs.BOVINI_VAC_NO_ISCR,
				TipoVariabile.ACZCAPISANZ_322);
		addVariabile(inputListaVariabiliCalcolo, mappaCardinalitaIntervento, CodiceInterventoAgs.BOVINI_MAC,
				TipoVariabile.ACZCAPISANZ_315);
		addVariabile(inputListaVariabiliCalcolo, mappaCardinalitaIntervento, CodiceInterventoAgs.BOVINI_MAC_12M,
				TipoVariabile.ACZCAPISANZ_316);
		addVariabile(inputListaVariabiliCalcolo, mappaCardinalitaIntervento, CodiceInterventoAgs.BOVINI_MAC_ETIC,
				TipoVariabile.ACZCAPISANZ_318);
		addVariabile(inputListaVariabiliCalcolo, mappaCardinalitaIntervento, CodiceInterventoAgs.OVICAP_AGN,
				TipoVariabile.ACZCAPISANZ_320);
		addVariabile(inputListaVariabiliCalcolo, mappaCardinalitaIntervento, CodiceInterventoAgs.OVICAP_MAC,
				TipoVariabile.ACZCAPISANZ_321);
		inputListaVariabiliCalcolo.add(new VariabileCalcolo(TipoVariabile.ACZCAPISANZTOT, BigDecimal.valueOf(calcolaTotale(mappaCardinalitaIntervento))));
		logger.debug("Inserimento variabili capi sanzionati - Fine");
	}
	
	protected Predicate<? super EsitoCalcoloCapoModel> predicatoFiltro() {
		return (esito -> {
//			List<A4gtDomandaIntegrativa> domandeIntegrative = esito.getA4gtDomandaIntegrativas();
			return (//!domandeIntegrative.isEmpty()
					//&& StatoDomandaIntegrativa.PRESENTATA.name().equals(domandeIntegrative.get(0).getStato())
					//&& 
					Boolean.TRUE.equals(esito.getRichiesto()) &&
					(Boolean.TRUE.equals(esito.getControlloNonSuperato())
						|| EsitoCalcoloCapo.AMMISSIBILE_CON_SANZIONE.equals(esito.getEsito()))
					&& Boolean.FALSE.equals(esito.getDuplicato() != null && esito.getDuplicato())
					);
		});
	}

}
