package it.tndigitale.a4gistruttoria.component.acz;

import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.component.PassoCalcoloComponent;

@Component
public class PassoCalcoloACZComponent extends PassoCalcoloComponent {

	
	protected TipologiaPassoTransizione getPassoCalcolo() {
		return TipologiaPassoTransizione.CALCOLO_ACZ;
	}
}
