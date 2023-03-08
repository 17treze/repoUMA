package it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno;

import it.tndigitale.a4gistruttoria.component.acs.CaricaPremioCalcolatoSuperficie;
import it.tndigitale.a4gistruttoria.dto.lavorazione.MapVariabili;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service("PassoDisciplinaFinanziaria_SUPERFICIE")
public class PassoDisciplinaFinanziariaSuperficie extends PassoDisciplinaFinanziaria {
	
	@Autowired
	private CaricaPremioCalcolatoSuperficie calcolatore;

	private static List<String> misure;
	static {
		misure = Arrays.asList("M8", "M9", "M10", "M11", "M14", "M15", "M16", "M17");
	}
	
	@Override
	protected MapVariabili applicaFranchigia(MapVariabili mapVariabiliInput, IstruttoriaModel istruttoria) {
		MapVariabili output = new MapVariabili();
		MapVariabili variabili = new MapVariabili();
		variabili.getVariabiliCalcolo().putAll(mapVariabiliInput.getVariabiliCalcolo());
		BigDecimal franchigia = calcolaFranchigiaSostegno.apply(variabili, TipoVariabile.ACSIMPCALC);
		aggiungiVariabile(variabili, output, new VariabileCalcolo(TipoVariabile.DFFRPAGACS, franchigia));

			
		// DFFRPAGACS_M8 = min(DFFRPAGACS;ACSIMPCALC-M8)
		aggiungiVariabile(variabili, output, 
				new VariabileCalcolo(TipoVariabile.DFFRPAGACS_M8, variabili.min(TipoVariabile.DFFRPAGACS, TipoVariabile.ACSIMPCALC_M8)));
		// DFFRPAGACS_M9 = min(DFFRPAGACS-DFFRPAGACS-M8;ACSIMPCALC-M9)
		aggiungiVariabile(variabili, output, 
				new VariabileCalcolo(TipoVariabile.DFFRPAGACS_M9, variabili
				.min(TipoVariabile.ACSIMPCALC_M9, variabili.subtract(TipoVariabile.DFFRPAGACS, TipoVariabile.DFFRPAGACS_M8))));
		// DFFRPAGACS_M9 = min(DFFRPAGACS-DFFRPAGACS_M8-DFFRPAGACS_M9;ACSIMPCALC_M10)
		aggiungiVariabile(variabili, output, 
				new VariabileCalcolo(TipoVariabile.DFFRPAGACS_M10, variabili
				.min(TipoVariabile.ACSIMPCALC_M10, variabili.subtract(TipoVariabile.DFFRPAGACS, TipoVariabile.DFFRPAGACS_M8, TipoVariabile.DFFRPAGACS_M9))));
		aggiungiVariabile(variabili, output, 
				new VariabileCalcolo(TipoVariabile.DFFRPAGACS_M11, variabili.min(TipoVariabile.ACSIMPCALC_M11,
						variabili.subtract(TipoVariabile.DFFRPAGACS, TipoVariabile.DFFRPAGACS_M8, TipoVariabile.DFFRPAGACS_M9, TipoVariabile.DFFRPAGACS_M10))));
		aggiungiVariabile(variabili, output,
				new VariabileCalcolo(TipoVariabile.DFFRPAGACS_M14, variabili.min(TipoVariabile.ACSIMPCALC_M14, variabili
						.subtract(TipoVariabile.DFFRPAGACS, TipoVariabile.DFFRPAGACS_M8, TipoVariabile.DFFRPAGACS_M9, TipoVariabile.DFFRPAGACS_M10, TipoVariabile.DFFRPAGACS_M11))));
		aggiungiVariabile(variabili, output, 
				new VariabileCalcolo(TipoVariabile.DFFRPAGACS_M15,
						variabili.min(TipoVariabile.ACSIMPCALC_M15, variabili.subtract(TipoVariabile.DFFRPAGACS,
								TipoVariabile.DFFRPAGACS_M8, TipoVariabile.DFFRPAGACS_M9, TipoVariabile.DFFRPAGACS_M10, TipoVariabile.DFFRPAGACS_M11, TipoVariabile.DFFRPAGACS_M14))));
		aggiungiVariabile(variabili, output, 
				new VariabileCalcolo(TipoVariabile.DFFRPAGACS_M16,
						variabili.min(TipoVariabile.ACSIMPCALC_M16,
								variabili.subtract(TipoVariabile.DFFRPAGACS, TipoVariabile.DFFRPAGACS_M8, TipoVariabile.DFFRPAGACS_M9,
										TipoVariabile.DFFRPAGACS_M10, TipoVariabile.DFFRPAGACS_M11, TipoVariabile.DFFRPAGACS_M14, TipoVariabile.DFFRPAGACS_M15))));
		aggiungiVariabile(variabili, output, 
				new VariabileCalcolo(TipoVariabile.DFFRPAGACS_M17,
						variabili.min(TipoVariabile.ACSIMPCALC_M17,
								variabili.subtract(TipoVariabile.DFFRPAGACS, TipoVariabile.DFFRPAGACS_M8, TipoVariabile.DFFRPAGACS_M9,
										TipoVariabile.DFFRPAGACS_M10, TipoVariabile.DFFRPAGACS_M11, TipoVariabile.DFFRPAGACS_M14, TipoVariabile.DFFRPAGACS_M15, TipoVariabile.DFFRPAGACS_M16))));

		/** (ACSIMPCALC_XX-DFFRPAGACS_XX)*(100-DFPERC) */
		for (String str : misure) {
			aggiungiVariabile(variabili, output, 
					new VariabileCalcolo(TipoVariabile.valueOf("DFIMPDFDISACS_".concat(str)),
							variabili.multiply(
									variabili.subtract(TipoVariabile.valueOf("ACSIMPCALC_".concat(str)), TipoVariabile.valueOf("DFFRPAGACS_".concat(str))),
									variabili.subtract(BigDecimal.valueOf(1), TipoVariabile.DFPERC))));
		}

		BigDecimal valSum = BigDecimal.ZERO;
		for (String str : misure) {
			valSum = valSum.add(variabili.get(TipoVariabile.valueOf("DFFRPAGACS_".concat(str))).getValNumber());
		}
		aggiungiVariabile(variabili, output, new VariabileCalcolo(TipoVariabile.DFFRPAGACS, valSum));		
		
		BigDecimal dffrpagacsSum = misure.stream().map(m -> variabili.get(TipoVariabile.valueOf("DFFRPAGACS_".concat(m))).getValNumber()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

		BigDecimal dfimpdfdisacsSum = misure.stream().map(m -> variabili.get(TipoVariabile.valueOf("DFIMPDFDISACS_".concat(m))).getValNumber()).reduce(BigDecimal.ZERO,
				(a, b) -> a.add(b));
		VariabileCalcolo var = new VariabileCalcolo(TipoVariabile.DFIMPLIQACS, dffrpagacsSum.add(dfimpdfdisacsSum));

		aggiungiVariabile(variabili, output, var);
		
		var = new VariabileCalcolo(TipoVariabile.DFIMPRIDACS,
				variabili.get(TipoVariabile.ACSIMPCALC).getValNumber().subtract(variabili.get(TipoVariabile.DFIMPLIQACS).getValNumber()));

		// handler.getPassoLavorazioneDto().getDatiOutput().getVariabiliCalcolo().add(var);
		aggiungiVariabile(variabili, output, var);
		
		return output;
	}
	
	protected void aggiungiVariabile(MapVariabili variabili, MapVariabili output, VariabileCalcolo variabile) {
		output.add(variabile);
		variabili.add(variabile);
	}
	
	@Override
	public CaricaPremioCalcolatoSuperficie getCalcolatore() {
		return calcolatore;
	}

	@Override
	public TipoVariabile[] getVariabiliPremi() {
		return new TipoVariabile[] {
				TipoVariabile.ACSIMPCALC_M8, TipoVariabile.ACSIMPCALC_M9, 
				TipoVariabile.ACSIMPCALC_M10, TipoVariabile.ACSIMPCALC_M11, TipoVariabile.ACSIMPCALC_M14,
				TipoVariabile.ACSIMPCALC_M15, TipoVariabile.ACSIMPCALC_M16, TipoVariabile.ACSIMPCALC_M17
		};
	}

	@Override
	public TipoVariabile getNomeVariabilePremioTotale() {
		return TipoVariabile.ACSIMPCALC;
	}

}
