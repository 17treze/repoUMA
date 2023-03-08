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
public class CalcoloOutputImportiRiduzioniRitardoMisureConsumer implements BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel> {

	private static final Logger logger = LoggerFactory.getLogger(CalcoloOutputImportiRiduzioniRitardoMisureConsumer.class);

	@Override
	public void accept(CalcoloAccoppiatoHandler handler, IstruttoriaModel istruttoria) {

		DomandaUnicaModel domanda = istruttoria.getDomandaUnicaModel();
		logger.debug("accept: inizio per la domanda {}", domanda.getId());		
		MapVariabili variabiliOutput = handler.getVariabiliOutput();
		BigDecimal totaleRiduzioni = BigDecimal.ZERO;
		
		BigDecimal percentualeRiduzione;
		if (handler.getVariabiliInput().get(TipoVariabile.PERCRITISTR) != null && handler.getVariabiliInput().get(TipoVariabile.PERCRITISTR).getValBoolean() != null
				&& handler.getVariabiliInput().get(TipoVariabile.PERCRITISTR).getValBoolean()) {
			percentualeRiduzione = BigDecimal.ZERO;
		} else {
			percentualeRiduzione = getValNumber(handler.getVariabiliInput(), TipoVariabile.PERCRIT);
		}
		
		BigDecimal premioMisura = null;
		premioMisura = calcolaRiduzioneRitardoMisura(variabiliOutput, TipoVariabile.ACSIMPAMM_M8, TipoVariabile.ACSIMPRIDRIT_M8, percentualeRiduzione);		
		if (premioMisura != null) {
			totaleRiduzioni = totaleRiduzioni.add(premioMisura);
		}

		premioMisura = calcolaRiduzioneRitardoMisura(variabiliOutput,TipoVariabile.ACSIMPAMM_M9, TipoVariabile.ACSIMPRIDRIT_M9, percentualeRiduzione);		
		if (premioMisura != null) {
			totaleRiduzioni = totaleRiduzioni.add(premioMisura);
		}
		
		premioMisura = calcolaRiduzioneRitardoMisura(variabiliOutput, TipoVariabile.ACSIMPAMM_M10, TipoVariabile.ACSIMPRIDRIT_M10, percentualeRiduzione);		
		if (premioMisura != null) {
			totaleRiduzioni = totaleRiduzioni.add(premioMisura);
		}

		premioMisura = calcolaRiduzioneRitardoMisura(variabiliOutput, TipoVariabile.ACSIMPAMM_M11, TipoVariabile.ACSIMPRIDRIT_M11, percentualeRiduzione);		
		if (premioMisura != null) {
			totaleRiduzioni = totaleRiduzioni.add(premioMisura);
		}

		premioMisura = calcolaRiduzioneRitardoMisura(variabiliOutput, TipoVariabile.ACSIMPAMM_M14, TipoVariabile.ACSIMPRIDRIT_M14, percentualeRiduzione);		
		if (premioMisura != null) {
			totaleRiduzioni = totaleRiduzioni.add(premioMisura);
		}

		premioMisura = calcolaRiduzioneRitardoMisura(variabiliOutput, TipoVariabile.ACSIMPAMM_M15, TipoVariabile.ACSIMPRIDRIT_M15, percentualeRiduzione);		
		if (premioMisura != null) {
			totaleRiduzioni = totaleRiduzioni.add(premioMisura);
		}

		premioMisura = calcolaRiduzioneRitardoMisura(variabiliOutput, TipoVariabile.ACSIMPAMM_M16, TipoVariabile.ACSIMPRIDRIT_M16, percentualeRiduzione);		
		if (premioMisura != null) {
			totaleRiduzioni = totaleRiduzioni.add(premioMisura);
		}

		premioMisura = calcolaRiduzioneRitardoMisura(variabiliOutput, TipoVariabile.ACSIMPAMM_M17, TipoVariabile.ACSIMPRIDRIT_M17, percentualeRiduzione);		
		if (premioMisura != null) {
			totaleRiduzioni = totaleRiduzioni.add(premioMisura);
		}

		variabiliOutput.add(new VariabileCalcolo(TipoVariabile.ACSIMPRIDRITTOT, totaleRiduzioni));
		logger.debug("accept: fine per la domanda {} con totale {}", domanda.getId(), totaleRiduzioni);		
	}
	
	protected BigDecimal calcolaRiduzioneRitardoMisura(MapVariabili variabiliOutput, TipoVariabile tipoPremioAmmesso,
			TipoVariabile tipoRiduzioneRitardo, BigDecimal percentualeRiduzione) {
		BigDecimal premioAmmesso = getValNumber(variabiliOutput, tipoPremioAmmesso);
		BigDecimal riduzione = null;
		if (premioAmmesso != null && percentualeRiduzione != null) {
			riduzione = premioAmmesso.multiply(percentualeRiduzione);
		}
		if (riduzione != null) {
			variabiliOutput.add(new VariabileCalcolo(tipoRiduzioneRitardo, riduzione));
		}
		return riduzione;
	}
	
	protected BigDecimal getValNumber(MapVariabili variabiliOutput, TipoVariabile tipoVariabile) {
		VariabileCalcolo vc = variabiliOutput.get(tipoVariabile);
		if (vc != null) return vc.getValNumber();
		return null;
	}
}
