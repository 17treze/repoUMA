package it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4gistruttoria.component.dis.CaricaPremioCalcolatoDisaccoppiato;
import it.tndigitale.a4gistruttoria.dto.lavorazione.MapVariabili;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@Service("PassoDisciplinaFinanziaria_DISACCOPPIATO")
public class PassoDisciplinaFinanziariaDisaccoppiato extends PassoDisciplinaFinanziaria {

	private static final Logger logger = LoggerFactory.getLogger(PassoDisciplinaFinanziariaDisaccoppiato.class);

	@Autowired
	private CaricaPremioCalcolatoDisaccoppiato calcolatorePremio;
	
	@Override
	protected MapVariabili applicaFranchigia(MapVariabili mapVariabiliInput, IstruttoriaModel istruttoria) {
		MapVariabili output = new MapVariabili();
		MapVariabili variabili = new MapVariabili();
		variabili.getVariabiliCalcolo().putAll(mapVariabiliInput.getVariabiliCalcolo());
		BigDecimal franchigia = calcolaFranchigiaSostegno.apply(variabili, TipoVariabile.DISIMPCALC);
		logger.debug("Franchigia residua: {}", franchigia);
		aggiungiVariabile(variabili, output, new VariabileCalcolo(TipoVariabile.DFFRPAGDIS, franchigia));
		
		// DFFRPAGDISBPS = min(DFFRPAGDIS;BPSIMPCALCFIN)
		aggiungiVariabile(variabili, output, new VariabileCalcolo(TipoVariabile.DFFRPAGDISBPS, variabili.min(TipoVariabile.DFFRPAGDIS, TipoVariabile.BPSIMPCALCFIN)));

		// (100-DFPERC)/100
		BigDecimal riduzioneDaFranchigia = variabili.subtract(BigDecimal.ONE, TipoVariabile.DFPERC);
		// DFIMPDFDISBPS = (BPSIMPCALCFIN-DFFRPAGDISBPS)*(100-DFPERC)
		aggiungiVariabile(variabili, output, new VariabileCalcolo(TipoVariabile.DFIMPDFDISBPS, variabili
				.multiply(variabili.subtract(TipoVariabile.BPSIMPCALCFIN, TipoVariabile.DFFRPAGDISBPS), riduzioneDaFranchigia)));

		// DFFRPAGDISGRE = min(DFFRPAGDIS-DFFRPAGDISBPS;GREIMPCALCFIN)
		aggiungiVariabile(variabili, output, new VariabileCalcolo(TipoVariabile.DFFRPAGDISGRE,
				variabili.min(TipoVariabile.GREIMPCALCFIN, variabili.subtract(TipoVariabile.DFFRPAGDIS, TipoVariabile.DFFRPAGDISBPS))));

		// DFIMPDFDISGRE = (GREIMPCALCFIN-DFFRPAGDISGRE)*(100-DFPERC)
		aggiungiVariabile(variabili, output, new VariabileCalcolo(TipoVariabile.DFIMPDFDISGRE, variabili
				.multiply(variabili.subtract(TipoVariabile.GREIMPCALCFIN, TipoVariabile.DFFRPAGDISGRE), riduzioneDaFranchigia)));

		// DFFRPAGDISGIO = min(DFFRPAGDIS-DFFRPAGDISBPS-DFFRPAGDISGRE;GIOIMPCALCFIN) => min (DFFRPAGDIS-(DFFRPAGDISBPS+DFFRPAGDISGRE);GIOIMPCALCFIN)
		aggiungiVariabile(variabili, output, new VariabileCalcolo(TipoVariabile.DFFRPAGDISGIO, variabili.min(TipoVariabile.GIOIMPCALCFIN,
				variabili.subtract(TipoVariabile.DFFRPAGDIS, variabili.add(TipoVariabile.DFFRPAGDISBPS, TipoVariabile.DFFRPAGDISGRE)))));

		// DFIMPDFDISGIO = (GIOIMPCALCFIN-DFFRPAGDISGIO)*(100-DFPERC)
		aggiungiVariabile(variabili, output, new VariabileCalcolo(TipoVariabile.DFIMPDFDISGIO, variabili
				.multiply(variabili.subtract(TipoVariabile.GIOIMPCALCFIN, TipoVariabile.DFFRPAGDISGIO), riduzioneDaFranchigia)));

		// DFIMPLIQDIS = DFFRPAGDISBPS+DFFRPAGDISGRE+DFFRPAGDISGIO+DFIMPDFDISBPS+DFIMPDFDISGRE+DFIMPDFDISGIO
		aggiungiVariabile(variabili, output, new VariabileCalcolo(TipoVariabile.DFIMPLIQDIS, variabili.add(TipoVariabile.DFFRPAGDISBPS, TipoVariabile.DFFRPAGDISGRE, TipoVariabile.DFFRPAGDISGIO, TipoVariabile.DFIMPDFDISBPS,
				TipoVariabile.DFIMPDFDISGRE, TipoVariabile.DFIMPDFDISGIO)));

		// DFIMPRIDDIS = DISIMPCALC-DFIMPLIQDIS
		aggiungiVariabile(variabili, output, new VariabileCalcolo(TipoVariabile.DFIMPRIDDIS, variabili.subtract(TipoVariabile.DISIMPCALC, TipoVariabile.DFIMPLIQDIS)));
		
		return output;
	}
	
		protected void aggiungiVariabile(MapVariabili variabili, MapVariabili output, VariabileCalcolo variabile) {
			output.add(variabile);
			variabili.add(variabile);
		}

	@Override
	public CaricaPremioCalcolatoDisaccoppiato getCalcolatore() {
		return calcolatorePremio;
	}

	@Override
	public TipoVariabile[] getVariabiliPremi() {
		return new TipoVariabile[]{
				TipoVariabile.BPSIMPCALCFIN,
				TipoVariabile.GIOIMPCALCFIN,
				TipoVariabile.GREIMPCALCFIN};
	}

	@Override
	public TipoVariabile getNomeVariabilePremioTotale() {
		return TipoVariabile.DISIMPCALC;
	}

}
