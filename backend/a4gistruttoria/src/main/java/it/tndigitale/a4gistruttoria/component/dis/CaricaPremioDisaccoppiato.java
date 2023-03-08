package it.tndigitale.a4gistruttoria.component.dis;

import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.component.CaricaPremioSostegno;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@Component
public class CaricaPremioDisaccoppiato extends CaricaPremioSostegno {

	@Override
	protected Sostegno getSostegno() {
		return Sostegno.DISACCOPPIATO;
	}

	@Override
	protected TipologiaPassoTransizione getPassoCalcolo() {
		return TipologiaPassoTransizione.CONTROLLI_FINALI;
	}

	@Override
	protected TipoVariabile getVariabileCalcoloPremioSostegno() {
		return TipoVariabile.IMPCALCFINLORDO;
	}

}
