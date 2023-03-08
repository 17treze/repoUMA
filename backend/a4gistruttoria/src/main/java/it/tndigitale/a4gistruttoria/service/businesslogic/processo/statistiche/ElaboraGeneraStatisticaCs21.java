package it.tndigitale.a4gistruttoria.service.businesslogic.processo.statistiche;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.service.businesslogic.processo.domanda.ElaboraDomanda;
import it.tndigitale.a4gistruttoria.service.businesslogic.statistica.GeneraStatisticaCs21;

@Component("STATISTICA_CS21")
public class ElaboraGeneraStatisticaCs21 extends ElaboraDomanda{

    @Autowired
    private GeneraStatisticaCs21 service;

    @Override
    protected GeneraStatisticaCs21 getElaborazioneDomandaService() {
        return service;
    }

}
