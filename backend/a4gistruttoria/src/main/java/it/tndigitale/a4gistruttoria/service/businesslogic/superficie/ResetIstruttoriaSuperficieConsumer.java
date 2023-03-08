package it.tndigitale.a4gistruttoria.service.businesslogic.superficie;

import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.ResetIstruttoriaConsumer;

import org.springframework.stereotype.Component;

@Component
public class ResetIstruttoriaSuperficieConsumer extends ResetIstruttoriaConsumer {
	
	@Override
	public void accept(IstruttoriaModel istruttoria) {
		super.accept(istruttoria);	
	}

}
