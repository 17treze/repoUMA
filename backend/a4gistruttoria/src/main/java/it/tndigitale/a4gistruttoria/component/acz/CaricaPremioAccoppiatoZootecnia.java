package it.tndigitale.a4gistruttoria.component.acz;

import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.component.CaricaPremioSostegno;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@Component
public class CaricaPremioAccoppiatoZootecnia extends CaricaPremioSostegno {

	@Override
	protected Sostegno getSostegno() {
		return Sostegno.ZOOTECNIA;
	}

	@Override
	protected TipologiaPassoTransizione getPassoCalcolo() {
		return TipologiaPassoTransizione.CALCOLO_ACZ;
	}

	@Override
	protected TipoVariabile getVariabileCalcoloPremioSostegno() {
		return TipoVariabile.ACZIMPCALCLORDOTOT;
	}

}
