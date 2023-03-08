package it.tndigitale.a4gistruttoria.service.businesslogic.processo.statistiche;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.service.businesslogic.processo.domanda.ElaboraDomanda;
import it.tndigitale.a4gistruttoria.service.businesslogic.statistica.ProcessoGeneraStatisticaCs27;

@Component("STATISTICA_CS27")
public class ElaboraGeneraStatisticaCs27 extends ElaboraDomanda{

    @Autowired
    private ProcessoGeneraStatisticaCs27 service;
    
	@Override
	protected ProcessoGeneraStatisticaCs27 getElaborazioneDomandaService() {
		return service;
	}

}
