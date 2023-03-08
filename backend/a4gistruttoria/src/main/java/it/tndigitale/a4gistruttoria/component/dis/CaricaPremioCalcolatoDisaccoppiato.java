package it.tndigitale.a4gistruttoria.component.dis;

import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.component.CaricaPremioCalcolatoSostegno;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;

@Component(CaricaPremioCalcolatoDisaccoppiato.NOME_QUALIFICATORE)
public class CaricaPremioCalcolatoDisaccoppiato extends CaricaPremioCalcolatoSostegno {

	public static final String NOME_QUALIFICATORE = PREFISSO_NOME_QUALIFICATORE + "DISACCOPPIATO";
	
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
		return TipoVariabile.IMPCALCFIN;
	}
	

}
