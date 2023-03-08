package it.tndigitale.a4gistruttoria.strategy;

import java.util.Optional;

import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.DomandaCollegata;
import it.tndigitale.a4gistruttoria.repository.model.A4gtDomandeCollegate;

@Component
public class ControlloDatiDomandaUnicaStrategy implements ControlloDomandeCollegateStrategy {

	@Override
	public A4gtDomandeCollegate controlloDomandeCollegate(Optional<A4gtDomandeCollegate> domandeCollegate, DomandaCollegata collegata) {
		// TODO Auto-generated method stub
		return null;
	}

}
