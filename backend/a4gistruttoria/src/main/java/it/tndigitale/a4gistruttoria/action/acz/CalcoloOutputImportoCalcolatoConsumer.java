package it.tndigitale.a4gistruttoria.action.acz;

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
public class CalcoloOutputImportoCalcolatoConsumer implements BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel> {

	private static final Logger logger = LoggerFactory.getLogger(CalcoloOutputImportoCalcolatoConsumer.class);

	@Override
	public void accept(CalcoloAccoppiatoHandler handler, IstruttoriaModel istruttoria) {
		DomandaUnicaModel domanda = istruttoria.getDomandaUnicaModel();
		logger.debug("accept: inizio per la domanda {}", domanda.getId());		
		MapVariabili variabiliOutput = handler.getVariabiliOutput();
		BigDecimal totaleCalcolato = BigDecimal.ZERO;
		
		BigDecimal calcolatoMisura = null;
		calcolatoMisura = calcolaDifferenzaLordoErogatoMisura(variabiliOutput, TipoVariabile.ACZIMPCALCLORDO_310, TipoVariabile.ACZIMPERO_310, TipoVariabile.ACZIMPCALC_310);		
		if (calcolatoMisura != null) {
			totaleCalcolato = totaleCalcolato.add(calcolatoMisura);
		}

		calcolatoMisura = calcolaDifferenzaLordoErogatoMisura(variabiliOutput, TipoVariabile.ACZIMPCALCLORDO_311, TipoVariabile.ACZIMPERO_311, TipoVariabile.ACZIMPCALC_311);		
		if (calcolatoMisura != null) {
			totaleCalcolato = totaleCalcolato.add(calcolatoMisura);
		}
		
		calcolatoMisura = calcolaDifferenzaLordoErogatoMisura(variabiliOutput, TipoVariabile.ACZIMPCALCLORDO_313, TipoVariabile.ACZIMPERO_313, TipoVariabile.ACZIMPCALC_313);		
		if (calcolatoMisura != null) {
			totaleCalcolato = totaleCalcolato.add(calcolatoMisura);
		}

		calcolatoMisura = calcolaDifferenzaLordoErogatoMisura(variabiliOutput, TipoVariabile.ACZIMPCALCLORDO_322, TipoVariabile.ACZIMPERO_322, TipoVariabile.ACZIMPCALC_322);		
		if (calcolatoMisura != null) {
			totaleCalcolato = totaleCalcolato.add(calcolatoMisura);
		}

		calcolatoMisura = calcolaDifferenzaLordoErogatoMisura(variabiliOutput, TipoVariabile.ACZIMPCALCLORDO_315, TipoVariabile.ACZIMPERO_315, TipoVariabile.ACZIMPCALC_315);		
		if (calcolatoMisura != null) {
			totaleCalcolato = totaleCalcolato.add(calcolatoMisura);
		}

		calcolatoMisura = calcolaDifferenzaLordoErogatoMisura(variabiliOutput, TipoVariabile.ACZIMPCALCLORDO_316, TipoVariabile.ACZIMPERO_316, TipoVariabile.ACZIMPCALC_316);		
		if (calcolatoMisura != null) {
			totaleCalcolato = totaleCalcolato.add(calcolatoMisura);
		}

		calcolatoMisura = calcolaDifferenzaLordoErogatoMisura(variabiliOutput, TipoVariabile.ACZIMPCALCLORDO_318, TipoVariabile.ACZIMPERO_318, TipoVariabile.ACZIMPCALC_318);		
		if (calcolatoMisura != null) {
			totaleCalcolato = totaleCalcolato.add(calcolatoMisura);
		}

		calcolatoMisura = calcolaDifferenzaLordoErogatoMisura(variabiliOutput, TipoVariabile.ACZIMPCALCLORDO_320, TipoVariabile.ACZIMPERO_320, TipoVariabile.ACZIMPCALC_320);		
		if (calcolatoMisura != null) {
			totaleCalcolato = totaleCalcolato.add(calcolatoMisura);
		}

		calcolatoMisura = calcolaDifferenzaLordoErogatoMisura(variabiliOutput, TipoVariabile.ACZIMPCALCLORDO_321, TipoVariabile.ACZIMPERO_321, TipoVariabile.ACZIMPCALC_321);		
		if (calcolatoMisura != null) {
			totaleCalcolato = totaleCalcolato.add(calcolatoMisura);
		}

		variabiliOutput.add(new VariabileCalcolo(TipoVariabile.ACZIMPCALCTOT, totaleCalcolato));
		logger.debug("accept: fine per la domanda {} con totale {}", domanda.getId(), totaleCalcolato);		
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
