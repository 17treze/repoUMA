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
public class CalcoloOutputSuperficiAmmesseConsumer
		implements BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel> {

	private static final Logger logger = LoggerFactory.getLogger(CalcoloOutputSuperficiAmmesseConsumer.class);

	@Override
	public void accept(CalcoloAccoppiatoHandler handler, IstruttoriaModel istruttoria) {
		DomandaUnicaModel domanda = istruttoria.getDomandaUnicaModel();
		logger.debug("accept: inizio per la domanda {}", domanda.getId());		
		MapVariabili variabiliOutput = handler.getVariabiliOutput();
		BigDecimal totaleSupAmmessa = BigDecimal.ZERO;
		BigDecimal ammessa = null;
		
		ammessa = calcolaAmmessaMisura(variabiliOutput, TipoVariabile.ACSSUPRIC_M8,
				TipoVariabile.ACSSUPDET_M8, TipoVariabile.ACSSUPAMM_M8);
		if (ammessa != null) {
			totaleSupAmmessa = totaleSupAmmessa.add(ammessa);
		}
		ammessa = calcolaAmmessaMisura(variabiliOutput, TipoVariabile.ACSSUPRIC_M9,
				TipoVariabile.ACSSUPDET_M9, TipoVariabile.ACSSUPAMM_M9);
		if (ammessa != null) {
			totaleSupAmmessa = totaleSupAmmessa.add(ammessa);
		}
		ammessa = calcolaAmmessaMisura(variabiliOutput, TipoVariabile.ACSSUPRIC_M10,
				TipoVariabile.ACSSUPDET_M10, TipoVariabile.ACSSUPAMM_M10);
		if (ammessa != null) {
			totaleSupAmmessa = totaleSupAmmessa.add(ammessa);
		}
		ammessa = calcolaAmmessaMisura(variabiliOutput, TipoVariabile.ACSSUPRIC_M11,
				TipoVariabile.ACSSUPDET_M11, TipoVariabile.ACSSUPAMM_M11);
		if (ammessa != null) {
			totaleSupAmmessa = totaleSupAmmessa.add(ammessa);
		}
		ammessa = calcolaAmmessaMisura(variabiliOutput, TipoVariabile.ACSSUPRIC_M14,
				TipoVariabile.ACSSUPDET_M14, TipoVariabile.ACSSUPAMM_M14);
		if (ammessa != null) {
			totaleSupAmmessa = totaleSupAmmessa.add(ammessa);
		}
		ammessa = calcolaAmmessaMisura(variabiliOutput, TipoVariabile.ACSSUPRIC_M15,
				TipoVariabile.ACSSUPDET_M15, TipoVariabile.ACSSUPAMM_M15);
		if (ammessa != null) {
			totaleSupAmmessa = totaleSupAmmessa.add(ammessa);
		}
		ammessa = calcolaAmmessaMisura(variabiliOutput, TipoVariabile.ACSSUPRIC_M16,
				TipoVariabile.ACSSUPDET_M16, TipoVariabile.ACSSUPAMM_M16);
		if (ammessa != null) {
			totaleSupAmmessa = totaleSupAmmessa.add(ammessa);
		}
		ammessa = calcolaAmmessaMisura(variabiliOutput, TipoVariabile.ACSSUPRIC_M17,
				TipoVariabile.ACSSUPDET_M17, TipoVariabile.ACSSUPAMM_M17);
		if (ammessa != null) {
			totaleSupAmmessa = totaleSupAmmessa.add(ammessa);
		}
		
		variabiliOutput.add(new VariabileCalcolo(TipoVariabile.ACSSUPAMMTOT, totaleSupAmmessa));
		logger.debug("accept: fine per la domanda {} con totale {}", domanda.getId(), totaleSupAmmessa);		
	}
	
	protected BigDecimal calcolaAmmessaMisura(MapVariabili variabiliOutput, TipoVariabile tipoSuperficieRichiesta,
			TipoVariabile tipoSuperficieDeterminata, TipoVariabile tipoSuperficieAmmessa) {
		BigDecimal supRichiesta = getValNumber(variabiliOutput, tipoSuperficieRichiesta);
		if (supRichiesta == null) return null;
		BigDecimal supDeterminata = getValNumber(variabiliOutput, tipoSuperficieDeterminata);
		BigDecimal ammessa = calcolaMinore(supRichiesta, supDeterminata);
		if (ammessa != null) {
			variabiliOutput.add(new VariabileCalcolo(tipoSuperficieAmmessa, ammessa));
		}
		return ammessa;
	}
	
	protected BigDecimal getValNumber(MapVariabili variabiliOutput, TipoVariabile tipoVariabile) {
		VariabileCalcolo vc = variabiliOutput.get(tipoVariabile);
		if (vc != null) return vc.getValNumber();
		return null;
	}

	protected BigDecimal calcolaMinore(BigDecimal sup1, BigDecimal sup2) {
		if (sup1 == null) return sup2;
		if (sup2 == null) return sup1;
		return sup1.min(sup2);
	}
}
