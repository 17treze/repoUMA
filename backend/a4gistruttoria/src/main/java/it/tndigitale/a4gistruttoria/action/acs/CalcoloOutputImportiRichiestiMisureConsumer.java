package it.tndigitale.a4gistruttoria.action.acs;

import java.math.BigDecimal;
import java.util.function.BiConsumer;

import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloAccoppiatoHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.lavorazione.MapVariabili;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.util.ConversioniCalcoli;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@Component
public class CalcoloOutputImportiRichiestiMisureConsumer implements BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel> {

	private static final Logger logger = LoggerFactory.getLogger(CalcoloOutputImportiRichiestiMisureConsumer.class);

	@Override
	public void accept(CalcoloAccoppiatoHandler handler, IstruttoriaModel istruttoria) {
		DomandaUnicaModel domanda = istruttoria.getDomandaUnicaModel();
		logger.debug("accept: inizio per la domanda {}", domanda.getId());
		MapVariabili variabiliOutput = handler.getVariabiliOutput();
		BigDecimal totaleRichiesto = BigDecimal.ZERO;

		BigDecimal premioRichiestoMisura = null;
		premioRichiestoMisura = calcolaPremioMisura(handler, TipoVariabile.ACSSUPRIC_M8, TipoVariabile.ACSVAL_M8, TipoVariabile.ACSIMPRIC_M8);
		if (premioRichiestoMisura != null) {
			totaleRichiesto = totaleRichiesto.add(premioRichiestoMisura);
		}

		premioRichiestoMisura = calcolaPremioMisura(handler, TipoVariabile.ACSSUPRIC_M9, TipoVariabile.ACSVAL_M9, TipoVariabile.ACSIMPRIC_M9);
		if (premioRichiestoMisura != null) {
			totaleRichiesto = totaleRichiesto.add(premioRichiestoMisura);
		}

		premioRichiestoMisura = calcolaPremioMisura(handler, TipoVariabile.ACSSUPRIC_M10, TipoVariabile.ACSVAL_M10, TipoVariabile.ACSIMPRIC_M10);
		if (premioRichiestoMisura != null) {
			totaleRichiesto = totaleRichiesto.add(premioRichiestoMisura);
		}

		premioRichiestoMisura = calcolaPremioMisura(handler, TipoVariabile.ACSSUPRIC_M11, TipoVariabile.ACSVAL_M11, TipoVariabile.ACSIMPRIC_M11);
		if (premioRichiestoMisura != null) {
			totaleRichiesto = totaleRichiesto.add(premioRichiestoMisura);
		}

		premioRichiestoMisura = calcolaPremioMisura(handler, TipoVariabile.ACSSUPRIC_M14, TipoVariabile.ACSVAL_M14, TipoVariabile.ACSIMPRIC_M14);
		if (premioRichiestoMisura != null) {
			totaleRichiesto = totaleRichiesto.add(premioRichiestoMisura);
		}

		premioRichiestoMisura = calcolaPremioMisura(handler, TipoVariabile.ACSSUPRIC_M15, TipoVariabile.ACSVAL_M15, TipoVariabile.ACSIMPRIC_M15);
		if (premioRichiestoMisura != null) {
			totaleRichiesto = totaleRichiesto.add(premioRichiestoMisura);
		}

		premioRichiestoMisura = calcolaPremioMisura(handler, TipoVariabile.ACSSUPRIC_M16, TipoVariabile.ACSVAL_M16, TipoVariabile.ACSIMPRIC_M16);
		if (premioRichiestoMisura != null) {
			totaleRichiesto = totaleRichiesto.add(premioRichiestoMisura);
		}

		premioRichiestoMisura = calcolaPremioMisura(handler, TipoVariabile.ACSSUPRIC_M17, TipoVariabile.ACSVAL_M17, TipoVariabile.ACSIMPRIC_M17);
		if (premioRichiestoMisura != null) {
			totaleRichiesto = totaleRichiesto.add(premioRichiestoMisura);
		}

		variabiliOutput.add(new VariabileCalcolo(TipoVariabile.ACSIMPRICTOT, totaleRichiesto));
		logger.debug("accept: fine per la domanda {} con totale {}", domanda.getId(), totaleRichiesto);
	}

	protected BigDecimal calcolaPremioMisura(CalcoloAccoppiatoHandler handler, TipoVariabile tipoSupAmmessa,
			TipoVariabile tipoValoreUnitario, TipoVariabile tipoPremioRichiesto) {
		MapVariabili variabiliOutput = handler.getVariabiliOutput();
		MapVariabili variabiliInput = handler.getVariabiliInput();
		BigDecimal superficie = getValNumber(variabiliOutput, tipoSupAmmessa);
		BigDecimal valoreUnitarioMisura = getValNumber(variabiliInput, tipoValoreUnitario);
		BigDecimal premioMisura = null;
		if (superficie != null && valoreUnitarioMisura != null) {
			premioMisura = superficie.multiply(valoreUnitarioMisura);
			variabiliOutput.add(new VariabileCalcolo(tipoPremioRichiesto, ConversioniCalcoli.getImporto(premioMisura)));
		}
		return premioMisura;
	}


	protected BigDecimal getValNumber(MapVariabili variabiliOutput, TipoVariabile tipoVariabile) {
		VariabileCalcolo vc = variabiliOutput.get(tipoVariabile);
		if (vc != null) return vc.getValNumber();
		return null;
	}
}
