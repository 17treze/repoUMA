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
public class CalcoloOutputImportiAmmessiMisureConsumer implements BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel> {

	private static final Logger logger = LoggerFactory.getLogger(CalcoloOutputImportiAmmessiMisureConsumer.class);

	@Override
	public void accept(CalcoloAccoppiatoHandler handler, IstruttoriaModel istruttoria) {
		DomandaUnicaModel domanda = istruttoria.getDomandaUnicaModel();
		logger.debug("accept: inizio per la domanda {}", domanda.getId());
		MapVariabili variabiliOutput = handler.getVariabiliOutput();
		BigDecimal totalePremioAmmesso = BigDecimal.ZERO;

		BigDecimal premioMisuraAmmesso = null;
		premioMisuraAmmesso = calcolaPremioMisura(handler, TipoVariabile.ACSSUPAMM_M8, TipoVariabile.ACSVAL_M8, TipoVariabile.ACSIMPAMM_M8);
		if (premioMisuraAmmesso != null) {
			totalePremioAmmesso = totalePremioAmmesso.add(premioMisuraAmmesso);
		}

		premioMisuraAmmesso = calcolaPremioMisura(handler, TipoVariabile.ACSSUPAMM_M9, TipoVariabile.ACSVAL_M9, TipoVariabile.ACSIMPAMM_M9);
		if (premioMisuraAmmesso != null) {
			totalePremioAmmesso = totalePremioAmmesso.add(premioMisuraAmmesso);
		}

		premioMisuraAmmesso = calcolaPremioMisura(handler, TipoVariabile.ACSSUPAMM_M10, TipoVariabile.ACSVAL_M10, TipoVariabile.ACSIMPAMM_M10);
		if (premioMisuraAmmesso != null) {
			totalePremioAmmesso = totalePremioAmmesso.add(premioMisuraAmmesso);
		}

		premioMisuraAmmesso = calcolaPremioMisura(handler, TipoVariabile.ACSSUPAMM_M11, TipoVariabile.ACSVAL_M11, TipoVariabile.ACSIMPAMM_M11);
		if (premioMisuraAmmesso != null) {
			totalePremioAmmesso = totalePremioAmmesso.add(premioMisuraAmmesso);
		}

		premioMisuraAmmesso = calcolaPremioMisura(handler, TipoVariabile.ACSSUPAMM_M14, TipoVariabile.ACSVAL_M14, TipoVariabile.ACSIMPAMM_M14);
		if (premioMisuraAmmesso != null) {
			totalePremioAmmesso = totalePremioAmmesso.add(premioMisuraAmmesso);
		}

		premioMisuraAmmesso = calcolaPremioMisura(handler, TipoVariabile.ACSSUPAMM_M15, TipoVariabile.ACSVAL_M15, TipoVariabile.ACSIMPAMM_M15);
		if (premioMisuraAmmesso != null) {
			totalePremioAmmesso = totalePremioAmmesso.add(premioMisuraAmmesso);
		}

		premioMisuraAmmesso = calcolaPremioMisura(handler, TipoVariabile.ACSSUPAMM_M16, TipoVariabile.ACSVAL_M16, TipoVariabile.ACSIMPAMM_M16);
		if (premioMisuraAmmesso != null) {
			totalePremioAmmesso = totalePremioAmmesso.add(premioMisuraAmmesso);
		}

		premioMisuraAmmesso = calcolaPremioMisura(handler, TipoVariabile.ACSSUPAMM_M17, TipoVariabile.ACSVAL_M17, TipoVariabile.ACSIMPAMM_M17);
		if (premioMisuraAmmesso != null) {
			totalePremioAmmesso = totalePremioAmmesso.add(premioMisuraAmmesso);
		}

		variabiliOutput.add(new VariabileCalcolo(TipoVariabile.ACSIMPAMMTOT, totalePremioAmmesso));

		logger.debug("accept: fine per la domanda {} con totale {}", domanda.getId(), totalePremioAmmesso);
	}

	protected BigDecimal calcolaPremioMisura(CalcoloAccoppiatoHandler handler, TipoVariabile tipoSupAmmessa,
			TipoVariabile tipoValoreUnitario, TipoVariabile tipoPremioAmmesso) {
		MapVariabili variabiliOutput = handler.getVariabiliOutput();
		MapVariabili variabiliInput = handler.getVariabiliInput();
		BigDecimal superficie = getValNumber(variabiliOutput, tipoSupAmmessa);
		BigDecimal valoreUnitarioMisura = getValNumber(variabiliInput, tipoValoreUnitario);
		BigDecimal premioMisura = null;
		if (superficie != null && valoreUnitarioMisura != null) {
			premioMisura = superficie.multiply(valoreUnitarioMisura);
			variabiliOutput.add(new VariabileCalcolo(tipoPremioAmmesso, ConversioniCalcoli.getImporto(premioMisura)));
		}
		return premioMisura;
	}


	protected BigDecimal getValNumber(MapVariabili variabiliOutput, TipoVariabile tipoVariabile) {
		VariabileCalcolo vc = variabiliOutput.get(tipoVariabile);
		if (vc != null) return vc.getValNumber();
		return null;
	}

}
