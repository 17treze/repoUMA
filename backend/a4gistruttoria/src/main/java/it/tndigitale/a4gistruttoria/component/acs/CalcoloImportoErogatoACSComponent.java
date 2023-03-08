package it.tndigitale.a4gistruttoria.component.acs;

import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.component.AImportoErogatoComponent;

@Component
public class CalcoloImportoErogatoACSComponent extends AImportoErogatoComponent {

	@Autowired
	PassoCalcoloACSComponent passoComp;

	@Override
	protected Sostegno getSostegno() {
		return Sostegno.SUPERFICIE;
	}

	@Override
	protected PassoCalcoloACSComponent getPassoCalcolo() {
		return passoComp;
	}
}
