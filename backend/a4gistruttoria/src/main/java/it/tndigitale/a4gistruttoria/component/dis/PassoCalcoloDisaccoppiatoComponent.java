package it.tndigitale.a4gistruttoria.component.dis;

import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.component.PassoCalcoloComponent;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;

@Component
public class PassoCalcoloDisaccoppiatoComponent extends PassoCalcoloComponent {

	
	protected TipologiaPassoTransizione getPassoCalcolo() {
		return TipologiaPassoTransizione.CONTROLLI_FINALI;
	}
}
