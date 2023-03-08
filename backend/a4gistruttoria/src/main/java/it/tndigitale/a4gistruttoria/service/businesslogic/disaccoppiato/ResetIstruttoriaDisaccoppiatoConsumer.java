package it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato;

import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.ResetIstruttoriaConsumer;

import org.springframework.stereotype.Component;

@Component
public class ResetIstruttoriaDisaccoppiatoConsumer extends ResetIstruttoriaConsumer {
	
	@Override
	public void accept(IstruttoriaModel istruttoria) {
		super.accept(istruttoria);	
	}

}
