package it.tndigitale.a4gistruttoria.component.acz;

import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.component.AImportoErogatoComponent;

@Component
public class CalcoloImportoErogatoACZComponent extends AImportoErogatoComponent {

	@Autowired
	private PassoCalcoloACZComponent passoComp;
	
	@Override
	protected Sostegno getSostegno() {
		return Sostegno.ZOOTECNIA;
	}

	@Override
	protected PassoCalcoloACZComponent getPassoCalcolo() {
		return passoComp;
	}
}
