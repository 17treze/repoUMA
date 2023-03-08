package it.tndigitale.a4gistruttoria.component.dis;

import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.component.AImportoErogatoComponent;

@Component
public class CalcoloImportoErogatoDisaccoppiatoComponent extends AImportoErogatoComponent {

	@Autowired
	private PassoCalcoloDisaccoppiatoComponent passoComp;
	
	@Override
	protected Sostegno getSostegno() {
		return Sostegno.DISACCOPPIATO;
	}

	@Override
	protected PassoCalcoloDisaccoppiatoComponent getPassoCalcolo() {
		return passoComp;
	}
}
