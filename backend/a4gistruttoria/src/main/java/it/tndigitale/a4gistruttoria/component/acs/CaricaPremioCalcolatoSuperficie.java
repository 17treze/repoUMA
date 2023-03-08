package it.tndigitale.a4gistruttoria.component.acs;

import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.component.CaricaPremioCalcolatoSostegno;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;

@Component(CaricaPremioCalcolatoSuperficie.NOME_QUALIFICATORE)
public class CaricaPremioCalcolatoSuperficie extends CaricaPremioCalcolatoSostegno {

	public static final String NOME_QUALIFICATORE = PREFISSO_NOME_QUALIFICATORE + "SUPERFICIE";

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
		return TipoVariabile.ACSIMPCALCTOT;
	}
	

}
