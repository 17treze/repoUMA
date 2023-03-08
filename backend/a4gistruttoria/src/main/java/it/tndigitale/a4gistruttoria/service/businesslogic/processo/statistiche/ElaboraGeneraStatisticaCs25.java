package it.tndigitale.a4gistruttoria.service.businesslogic.processo.statistiche;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.service.businesslogic.processo.domanda.ElaboraDomanda;
import it.tndigitale.a4gistruttoria.service.businesslogic.statistica.GeneraStatisticaCs25;

@Component("STATISTICA_CS25")
public class ElaboraGeneraStatisticaCs25 extends ElaboraDomanda{

    @Autowired
    private GeneraStatisticaCs25 service;

    @Override
    protected GeneraStatisticaCs25 getElaborazioneDomandaService() {
        return service;
    }

}
