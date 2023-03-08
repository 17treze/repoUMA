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
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@Component
public class CalcoloOutputImportiCalcolatiMisureConsumer implements BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel> {

	private static final Logger logger = LoggerFactory.getLogger(CalcoloOutputImportiCalcolatiMisureConsumer.class);

	@Override
	public void accept(CalcoloAccoppiatoHandler handler, IstruttoriaModel istruttoria) {
		DomandaUnicaModel domanda = istruttoria.getDomandaUnicaModel();
		logger.debug("accept: inizio per la domanda {}", domanda.getId());		
		MapVariabili variabiliOutput = handler.getVariabiliOutput();
		BigDecimal totalePremioAmmesso = BigDecimal.ZERO;
		
		BigDecimal premioMisuraAmmesso = null;
		premioMisuraAmmesso = calcolaDifferenzaLordoErogatoMisura(variabiliOutput, TipoVariabile.ACSIMPCALCLORDO_M8, TipoVariabile.ACSIMPERO_M8, TipoVariabile.ACSIMPCALC_M8);		
		if (premioMisuraAmmesso != null) {
			totalePremioAmmesso = totalePremioAmmesso.add(premioMisuraAmmesso);
		}

		premioMisuraAmmesso = calcolaDifferenzaLordoErogatoMisura(variabiliOutput, TipoVariabile.ACSIMPCALCLORDO_M9, TipoVariabile.ACSIMPERO_M9, TipoVariabile.ACSIMPCALC_M9);		
		if (premioMisuraAmmesso != null) {
			totalePremioAmmesso = totalePremioAmmesso.add(premioMisuraAmmesso);
		}
		
		premioMisuraAmmesso = calcolaDifferenzaLordoErogatoMisura(variabiliOutput, TipoVariabile.ACSIMPCALCLORDO_M10, TipoVariabile.ACSIMPERO_M10, TipoVariabile.ACSIMPCALC_M10);		
		if (premioMisuraAmmesso != null) {
			totalePremioAmmesso = totalePremioAmmesso.add(premioMisuraAmmesso);
		}

		premioMisuraAmmesso = calcolaDifferenzaLordoErogatoMisura(variabiliOutput, TipoVariabile.ACSIMPCALCLORDO_M11, TipoVariabile.ACSIMPERO_M11, TipoVariabile.ACSIMPCALC_M11);		
		if (premioMisuraAmmesso != null) {
			totalePremioAmmesso = totalePremioAmmesso.add(premioMisuraAmmesso);
		}

		premioMisuraAmmesso = calcolaDifferenzaLordoErogatoMisura(variabiliOutput, TipoVariabile.ACSIMPCALCLORDO_M14, TipoVariabile.ACSIMPERO_M14, TipoVariabile.ACSIMPCALC_M14);		
		if (premioMisuraAmmesso != null) {
			totalePremioAmmesso = totalePremioAmmesso.add(premioMisuraAmmesso);
		}

		premioMisuraAmmesso = calcolaDifferenzaLordoErogatoMisura(variabiliOutput, TipoVariabile.ACSIMPCALCLORDO_M15, TipoVariabile.ACSIMPERO_M15, TipoVariabile.ACSIMPCALC_M15);		
		if (premioMisuraAmmesso != null) {
			totalePremioAmmesso = totalePremioAmmesso.add(premioMisuraAmmesso);
		}

		premioMisuraAmmesso = calcolaDifferenzaLordoErogatoMisura(variabiliOutput, TipoVariabile.ACSIMPCALCLORDO_M16, TipoVariabile.ACSIMPERO_M16, TipoVariabile.ACSIMPCALC_M16);		
		if (premioMisuraAmmesso != null) {
			totalePremioAmmesso = totalePremioAmmesso.add(premioMisuraAmmesso);
		}

		premioMisuraAmmesso = calcolaDifferenzaLordoErogatoMisura(variabiliOutput, TipoVariabile.ACSIMPCALCLORDO_M17, TipoVariabile.ACSIMPERO_M17, TipoVariabile.ACSIMPCALC_M17);		
		if (premioMisuraAmmesso != null) {
			totalePremioAmmesso = totalePremioAmmesso.add(premioMisuraAmmesso);
		}

		variabiliOutput.add(new VariabileCalcolo(TipoVariabile.ACSIMPCALCTOT, totalePremioAmmesso));
		logger.debug("accept: fine per la domanda {} con totale {}", domanda.getId(), totalePremioAmmesso);		
	}
	
	protected BigDecimal calcolaDifferenzaLordoErogatoMisura(MapVariabili variabiliOutput, TipoVariabile tipoVImpLordo, 
			TipoVariabile tipoVImpEro,
			TipoVariabile tipoImportoCalcolato) {
		BigDecimal importoLordo = getValNumber(variabiliOutput, tipoVImpLordo);
		BigDecimal importoErogato = getValNumber(variabiliOutput, tipoVImpEro);

		BigDecimal premioCalcolato = null;
		if (importoLordo != null) {
			premioCalcolato = importoLordo;

			if (importoErogato != null) {
				premioCalcolato = importoLordo.subtract(importoErogato);
				premioCalcolato = premioCalcolato.max(BigDecimal.ZERO);
			}
		}
		if (premioCalcolato != null) {
			variabiliOutput.add(new VariabileCalcolo(tipoImportoCalcolato, premioCalcolato));
		}
		return premioCalcolato;
	}

	protected BigDecimal getValNumber(MapVariabili variabiliOutput, TipoVariabile tipoVariabile) {
		VariabileCalcolo vc = variabiliOutput.get(tipoVariabile);
		if (vc != null) return vc.getValNumber();
		return null;
	}
}
