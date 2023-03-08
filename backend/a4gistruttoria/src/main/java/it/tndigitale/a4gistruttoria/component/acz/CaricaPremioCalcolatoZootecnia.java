package it.tndigitale.a4gistruttoria.component.acz;

import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.component.CaricaPremioCalcolatoSostegno;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;

@Component(CaricaPremioCalcolatoZootecnia.NOME_QUALIFICATORE)
public class CaricaPremioCalcolatoZootecnia extends CaricaPremioCalcolatoSostegno {

	public static final String NOME_QUALIFICATORE = PREFISSO_NOME_QUALIFICATORE + "ZOOTECNIA";

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
		return TipoVariabile.ACZIMPCALCTOT;
	}
	

}
