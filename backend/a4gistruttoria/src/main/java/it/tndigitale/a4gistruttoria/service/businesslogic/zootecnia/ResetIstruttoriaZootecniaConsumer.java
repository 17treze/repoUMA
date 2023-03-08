package it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.repository.model.AllevamentoImpegnatoModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.ResetIstruttoriaConsumer;
import it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia.calcolocapi.CalcoloCapiHandler;

@Component
public class ResetIstruttoriaZootecniaConsumer extends ResetIstruttoriaConsumer {

private static Logger log = LoggerFactory.getLogger(ResetIstruttoriaZootecniaConsumer.class);

	@Autowired
	private CalcoloCapiHandler calcoloCapiHandler;
	
	@Override
	public void accept(IstruttoriaModel istruttoria) {
		Set<AllevamentoImpegnatoModel> sostegniZootecnici = istruttoria.getDomandaUnicaModel().getAllevamentiImpegnati();
		//pulizia dei dati dei capi e domanda integrativa 
		log.debug("pulizia dei dati dei capi");
		sostegniZootecnici.forEach(calcoloCapiHandler.deleteElement());
		super.accept(istruttoria);	
	}
}
