package it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno;

import it.tndigitale.a4gistruttoria.component.acz.CaricaPremioCalcolatoZootecnia;
import it.tndigitale.a4gistruttoria.dto.lavorazione.MapVariabili;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service("PassoDisciplinaFinanziaria_ZOOTECNIA")
public class PassoDisciplinaFinanziariaZootecnia extends PassoDisciplinaFinanziaria {
	
	@Autowired
	private CaricaPremioCalcolatoZootecnia calcolatore;

	private static List<String> interventi;
	static {
		interventi = Arrays.asList("310", "311", "313", "315", "316", "318", "320", "321", "322");
	}
	
	@Override
	protected MapVariabili applicaFranchigia(MapVariabili mapVariabiliInput, IstruttoriaModel istruttoria) {
		MapVariabili output = new MapVariabili();
		MapVariabili variabili = new MapVariabili();
		variabili.getVariabiliCalcolo().putAll(mapVariabiliInput.getVariabiliCalcolo());
		BigDecimal franchigia = calcolaFranchigiaSostegno.apply(mapVariabiliInput, TipoVariabile.ACZIMPCALC);
		aggiungiVariabile(variabili, output, new VariabileCalcolo(TipoVariabile.DFFRPAGACZ, franchigia));

		// DFFRPAGACZ_M8 = min(DFFRPAGACZ;ACZIMPCALC-M8)
		aggiungiVariabile(variabili, output, 
				new VariabileCalcolo(TipoVariabile.DFFRPAGACZ_310, variabili.min(TipoVariabile.DFFRPAGACZ, TipoVariabile.ACZIMPCALC_310)));
		// DFFRPAGACZ_M9 = min(DFFRPAGACZ-DFFRPAGACZ-M8;ACZIMPCALC-M9)
		aggiungiVariabile(variabili, output, 
				new VariabileCalcolo(TipoVariabile.DFFRPAGACZ_311, variabili
				.min(TipoVariabile.ACZIMPCALC_311, variabili.subtract(TipoVariabile.DFFRPAGACZ, TipoVariabile.DFFRPAGACZ_310))));
		// DFFRPAGACZ-313=min(DFFRPAGACZ-DFFRPAGACZ-310-DFFRPAGACZ-311;ACZIMPCALC-313)
		aggiungiVariabile(variabili, output, 
				new VariabileCalcolo(TipoVariabile.DFFRPAGACZ_313, variabili
				.min(TipoVariabile.ACZIMPCALC_313, variabili.subtract(TipoVariabile.DFFRPAGACZ, TipoVariabile.DFFRPAGACZ_310, TipoVariabile.DFFRPAGACZ_311))));
		aggiungiVariabile(variabili, output,
				new VariabileCalcolo(TipoVariabile.DFFRPAGACZ_322, variabili.min(TipoVariabile.ACZIMPCALC_322,
						variabili.subtract(TipoVariabile.DFFRPAGACZ, TipoVariabile.DFFRPAGACZ_310, TipoVariabile.DFFRPAGACZ_311, TipoVariabile.DFFRPAGACZ_313))));
		aggiungiVariabile(variabili, output,
				new VariabileCalcolo(TipoVariabile.DFFRPAGACZ_315, variabili.min(TipoVariabile.ACZIMPCALC_315, variabili
						.subtract(TipoVariabile.DFFRPAGACZ, TipoVariabile.DFFRPAGACZ_310, TipoVariabile.DFFRPAGACZ_311, TipoVariabile.DFFRPAGACZ_313, TipoVariabile.DFFRPAGACZ_322))));
		aggiungiVariabile(variabili, output,
				new VariabileCalcolo(TipoVariabile.DFFRPAGACZ_316,
						variabili.min(TipoVariabile.ACZIMPCALC_316, variabili.subtract(TipoVariabile.DFFRPAGACZ,
								TipoVariabile.DFFRPAGACZ_310, TipoVariabile.DFFRPAGACZ_311, TipoVariabile.DFFRPAGACZ_313, TipoVariabile.DFFRPAGACZ_322, TipoVariabile.DFFRPAGACZ_315))));
		aggiungiVariabile(variabili, output,
				new VariabileCalcolo(TipoVariabile.DFFRPAGACZ_318,
						variabili.min(TipoVariabile.ACZIMPCALC_318,
								variabili.subtract(TipoVariabile.DFFRPAGACZ, TipoVariabile.DFFRPAGACZ_310, TipoVariabile.DFFRPAGACZ_311,
										TipoVariabile.DFFRPAGACZ_313, TipoVariabile.DFFRPAGACZ_322, TipoVariabile.DFFRPAGACZ_315, TipoVariabile.DFFRPAGACZ_316))));
		aggiungiVariabile(variabili, output,
				new VariabileCalcolo(TipoVariabile.DFFRPAGACZ_320,
						variabili.min(TipoVariabile.ACZIMPCALC_320,
								variabili.subtract(TipoVariabile.DFFRPAGACZ, TipoVariabile.DFFRPAGACZ_310, TipoVariabile.DFFRPAGACZ_311,
										TipoVariabile.DFFRPAGACZ_313, TipoVariabile.DFFRPAGACZ_322, TipoVariabile.DFFRPAGACZ_315, TipoVariabile.DFFRPAGACZ_316, TipoVariabile.DFFRPAGACZ_318))));
		aggiungiVariabile(variabili, output,
				new VariabileCalcolo(TipoVariabile.DFFRPAGACZ_321,
						variabili.min(TipoVariabile.ACZIMPCALC_321,
								variabili.subtract(TipoVariabile.DFFRPAGACZ, TipoVariabile.DFFRPAGACZ_310, TipoVariabile.DFFRPAGACZ_311,
										TipoVariabile.DFFRPAGACZ_313, TipoVariabile.DFFRPAGACZ_322, TipoVariabile.DFFRPAGACZ_315, TipoVariabile.DFFRPAGACZ_316, TipoVariabile.DFFRPAGACZ_318,
										TipoVariabile.DFFRPAGACZ_320))));

		/** (ACZIMPCALC_XX-DFFRPAGACZ_XX)*(100-DFPERC) */
		for (String str : interventi) {
			aggiungiVariabile(variabili, output,
					new VariabileCalcolo(TipoVariabile.valueOf("DFIMPDFDISACZ_".concat(str)),
							variabili.multiply(
									variabili.subtract(TipoVariabile.valueOf("ACZIMPCALC_".concat(str)), TipoVariabile.valueOf("DFFRPAGACZ_".concat(str))),
									variabili.subtract(BigDecimal.valueOf(1), TipoVariabile.DFPERC))));
		}
		
		
		BigDecimal valSum = BigDecimal.ZERO;
		for (String str : interventi) {
			valSum = valSum.add(variabili.get(TipoVariabile.valueOf("DFFRPAGACZ_".concat(str))).getValNumber());
		}
		aggiungiVariabile(variabili, output, new VariabileCalcolo(TipoVariabile.DFFRPAGACZ, valSum));
		
		BigDecimal dffrpagacsSum = interventi.stream().map(m -> variabili.get(TipoVariabile.valueOf("DFFRPAGACZ_".concat(m))).getValNumber()).reduce(BigDecimal.ZERO,
				(a, b) -> a.add(b));

		BigDecimal dfimpdfdisacsSum = interventi.stream().map(m -> variabili.get(TipoVariabile.valueOf("DFIMPDFDISACZ_".concat(m))).getValNumber()).reduce(BigDecimal.ZERO,
				(a, b) -> a.add(b));
		VariabileCalcolo var = new VariabileCalcolo(TipoVariabile.DFIMPLIQACZ, dffrpagacsSum.add(dfimpdfdisacsSum));

		// handler.getPassoLavorazioneDto().getDatiOutput().getVariabiliCalcolo().add(var);
		aggiungiVariabile(variabili, output, var);
		
		var = new VariabileCalcolo(TipoVariabile.DFIMPRIDACZ,
				variabili.get(TipoVariabile.ACZIMPCALC).getValNumber().subtract(variabili.get(TipoVariabile.DFIMPLIQACZ).getValNumber()));
		
		aggiungiVariabile(variabili, output, var);
		
		return output;
	}
	
	protected void aggiungiVariabile(MapVariabili variabili, MapVariabili output, VariabileCalcolo variabile) {
		output.add(variabile);
		variabili.add(variabile);
	}
	

	@Override
	public CaricaPremioCalcolatoZootecnia getCalcolatore() {
		return calcolatore;
	}

	@Override
	public TipoVariabile[] getVariabiliPremi() {
		return new TipoVariabile[] {
				TipoVariabile.ACZIMPCALC_310, TipoVariabile.ACZIMPCALC_311, 
				TipoVariabile.ACZIMPCALC_313, TipoVariabile.ACZIMPCALC_315,
				TipoVariabile.ACZIMPCALC_316,
				TipoVariabile.ACZIMPCALC_318, TipoVariabile.ACZIMPCALC_320, 
				TipoVariabile.ACZIMPCALC_321, TipoVariabile.ACZIMPCALC_322
		};
	}

	@Override
	public TipoVariabile getNomeVariabilePremioTotale() {
		return TipoVariabile.ACZIMPCALC;
	}

}
