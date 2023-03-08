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
public class CalcoloOutputImportiCalcolatiLordiMisureConsumer implements BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel> {

	private static final Logger logger = LoggerFactory.getLogger(CalcoloOutputImportiCalcolatiLordiMisureConsumer.class);

	@Override
	public void accept(CalcoloAccoppiatoHandler handler, IstruttoriaModel istruttoria) {
		DomandaUnicaModel domanda = istruttoria.getDomandaUnicaModel();
		logger.debug("accept: inizio per la domanda {}", domanda.getId());		
		MapVariabili variabiliOutput = handler.getVariabiliOutput();
		BigDecimal totalePremioAmmesso = BigDecimal.ZERO;
		
		BigDecimal premioMisuraAmmesso = null;
		premioMisuraAmmesso = calcolaPremioMisura(variabiliOutput, TipoVariabile.ACSIMPAMM_M8, TipoVariabile.ACSIMPRIDRIT_M8, TipoVariabile.ACSIMPCALCLORDO_M8);		
		if (premioMisuraAmmesso != null) {
			totalePremioAmmesso = totalePremioAmmesso.add(premioMisuraAmmesso);
		}

		premioMisuraAmmesso = calcolaPremioMisura(variabiliOutput, TipoVariabile.ACSIMPAMM_M9, TipoVariabile.ACSIMPRIDRIT_M9, TipoVariabile.ACSIMPCALCLORDO_M9);		
		if (premioMisuraAmmesso != null) {
			totalePremioAmmesso = totalePremioAmmesso.add(premioMisuraAmmesso);
		}
		
		premioMisuraAmmesso = calcolaPremioMisura(variabiliOutput, TipoVariabile.ACSIMPAMM_M10, TipoVariabile.ACSIMPRIDRIT_M10, TipoVariabile.ACSIMPCALCLORDO_M10);		
		if (premioMisuraAmmesso != null) {
			totalePremioAmmesso = totalePremioAmmesso.add(premioMisuraAmmesso);
		}

		premioMisuraAmmesso = calcolaPremioMisura(variabiliOutput, TipoVariabile.ACSIMPAMM_M11, TipoVariabile.ACSIMPRIDRIT_M11, TipoVariabile.ACSIMPCALCLORDO_M11);		
		if (premioMisuraAmmesso != null) {
			totalePremioAmmesso = totalePremioAmmesso.add(premioMisuraAmmesso);
		}

		premioMisuraAmmesso = calcolaPremioMisura(variabiliOutput, TipoVariabile.ACSIMPAMM_M14, TipoVariabile.ACSIMPRIDRIT_M14, TipoVariabile.ACSIMPCALCLORDO_M14);		
		if (premioMisuraAmmesso != null) {
			totalePremioAmmesso = totalePremioAmmesso.add(premioMisuraAmmesso);
		}

		premioMisuraAmmesso = calcolaPremioMisura(variabiliOutput, TipoVariabile.ACSIMPAMM_M15, TipoVariabile.ACSIMPRIDRIT_M15, TipoVariabile.ACSIMPCALCLORDO_M15);		
		if (premioMisuraAmmesso != null) {
			totalePremioAmmesso = totalePremioAmmesso.add(premioMisuraAmmesso);
		}

		premioMisuraAmmesso = calcolaPremioMisura(variabiliOutput, TipoVariabile.ACSIMPAMM_M16, TipoVariabile.ACSIMPRIDRIT_M16, TipoVariabile.ACSIMPCALCLORDO_M16);		
		if (premioMisuraAmmesso != null) {
			totalePremioAmmesso = totalePremioAmmesso.add(premioMisuraAmmesso);
		}

		premioMisuraAmmesso = calcolaPremioMisura(variabiliOutput, TipoVariabile.ACSIMPAMM_M17, TipoVariabile.ACSIMPRIDRIT_M17, TipoVariabile.ACSIMPCALCLORDO_M17);		
		if (premioMisuraAmmesso != null) {
			totalePremioAmmesso = totalePremioAmmesso.add(premioMisuraAmmesso);
		}

		variabiliOutput.add(new VariabileCalcolo(TipoVariabile.ACSIMPCALCLORDOTOT, totalePremioAmmesso));
		logger.debug("accept: fine per la domanda {} con totale {}", domanda.getId(), totalePremioAmmesso);		
	}
	
	protected BigDecimal calcolaPremioMisura(MapVariabili variabiliOutput, TipoVariabile tipoPremioAmmesso, 
			TipoVariabile tipoImportoRiduzioneRitardo,
			TipoVariabile tipoImportoCalcolato) {
		BigDecimal importoAmmesso = getValNumber(variabiliOutput, tipoPremioAmmesso);
		BigDecimal importoRiduzione = getValNumber(variabiliOutput, tipoImportoRiduzioneRitardo);

		BigDecimal premioCalcolato = null;
		if (importoAmmesso != null) {
			premioCalcolato = importoAmmesso;

			if (importoRiduzione != null) {
				premioCalcolato = importoAmmesso.subtract(importoRiduzione);
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
