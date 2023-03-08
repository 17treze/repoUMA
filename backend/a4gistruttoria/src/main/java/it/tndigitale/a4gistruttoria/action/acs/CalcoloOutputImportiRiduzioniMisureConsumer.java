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
public class CalcoloOutputImportiRiduzioniMisureConsumer implements BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel> {

	private static final Logger logger = LoggerFactory.getLogger(CalcoloOutputImportiRiduzioniMisureConsumer.class);

	@Override
	public void accept(CalcoloAccoppiatoHandler handler, IstruttoriaModel istruttoria) {
		DomandaUnicaModel domanda = istruttoria.getDomandaUnicaModel();
		logger.debug("accept: inizio per la domanda {}", domanda.getId());		
		MapVariabili variabiliOutput = handler.getVariabiliOutput();
		BigDecimal totalePremio = BigDecimal.ZERO;
		
		BigDecimal premioMisura = null;
		premioMisura = calcolaRiduzioneMisura(variabiliOutput, TipoVariabile.ACSIMPRIC_M8, TipoVariabile.ACSIMPAMM_M8, TipoVariabile.ACSIMPRID_M8);		
		if (premioMisura != null) {
			totalePremio = totalePremio.add(premioMisura);
		}

		premioMisura = calcolaRiduzioneMisura(variabiliOutput, TipoVariabile.ACSIMPRIC_M9, TipoVariabile.ACSIMPAMM_M9, TipoVariabile.ACSIMPRID_M9);		
		if (premioMisura != null) {
			totalePremio = totalePremio.add(premioMisura);
		}
		
		premioMisura = calcolaRiduzioneMisura(variabiliOutput, TipoVariabile.ACSIMPRIC_M10, TipoVariabile.ACSIMPAMM_M10, TipoVariabile.ACSIMPRID_M10);		
		if (premioMisura != null) {
			totalePremio = totalePremio.add(premioMisura);
		}

		premioMisura = calcolaRiduzioneMisura(variabiliOutput, TipoVariabile.ACSIMPRIC_M11, TipoVariabile.ACSIMPAMM_M11, TipoVariabile.ACSIMPRID_M11);		
		if (premioMisura != null) {
			totalePremio = totalePremio.add(premioMisura);
		}

		premioMisura = calcolaRiduzioneMisura(variabiliOutput, TipoVariabile.ACSIMPRIC_M14, TipoVariabile.ACSIMPAMM_M14, TipoVariabile.ACSIMPRID_M14);		
		if (premioMisura != null) {
			totalePremio = totalePremio.add(premioMisura);
		}

		premioMisura = calcolaRiduzioneMisura(variabiliOutput, TipoVariabile.ACSIMPRIC_M15, TipoVariabile.ACSIMPAMM_M15, TipoVariabile.ACSIMPRID_M15);		
		if (premioMisura != null) {
			totalePremio = totalePremio.add(premioMisura);
		}

		premioMisura = calcolaRiduzioneMisura(variabiliOutput, TipoVariabile.ACSIMPRIC_M16, TipoVariabile.ACSIMPAMM_M16, TipoVariabile.ACSIMPRID_M16);		
		if (premioMisura != null) {
			totalePremio = totalePremio.add(premioMisura);
		}

		premioMisura = calcolaRiduzioneMisura(variabiliOutput, TipoVariabile.ACSIMPRIC_M17, TipoVariabile.ACSIMPAMM_M17, TipoVariabile.ACSIMPRID_M17);		
		if (premioMisura != null) {
			totalePremio = totalePremio.add(premioMisura);
		}

		variabiliOutput.add(new VariabileCalcolo(TipoVariabile.ACSIMPRIDTOT, totalePremio));
		logger.debug("accept: fine per la domanda {} con totale {}", domanda.getId(), totalePremio);		
	}
	
	protected BigDecimal calcolaRiduzioneMisura(MapVariabili variabiliOutput, TipoVariabile tipoPremioRichiesto,
			TipoVariabile tipoPremioAmmesso, TipoVariabile tipoRiduzione) {
		BigDecimal premioRichiesto = getValNumber(variabiliOutput, tipoPremioRichiesto);
		BigDecimal premioAmmesso = getValNumber(variabiliOutput, tipoPremioAmmesso);
		BigDecimal riduzione = null;
		if (premioRichiesto != null && premioAmmesso != null) {
			riduzione = premioRichiesto.subtract(premioAmmesso);
		}
		if (riduzione != null) {
			variabiliOutput.add(new VariabileCalcolo(tipoRiduzione, riduzione));
		}
		return riduzione;
	}

	
	protected BigDecimal getValNumber(MapVariabili variabiliOutput, TipoVariabile tipoVariabile) {
		VariabileCalcolo vc = variabiliOutput.get(tipoVariabile);
		if (vc != null) return vc.getValNumber();
		return null;
	}
}
