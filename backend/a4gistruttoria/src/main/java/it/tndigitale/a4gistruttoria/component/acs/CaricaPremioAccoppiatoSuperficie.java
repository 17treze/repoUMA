package it.tndigitale.a4gistruttoria.component.acs;

import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.component.CaricaPremioSostegno;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@Component
public class CaricaPremioAccoppiatoSuperficie extends CaricaPremioSostegno {

	@Override
	protected Sostegno getSostegno() {
		return Sostegno.SUPERFICIE;
	}

	@Override
	protected TipologiaPassoTransizione getPassoCalcolo() {
		return TipologiaPassoTransizione.CALCOLO_ACS;
	}

	@Override
	protected TipoVariabile getVariabileCalcoloPremioSostegno() {
		return TipoVariabile.ACSIMPCALCLORDOTOT;
	}
}
