package it.tndigitale.a4gistruttoria.service.businesslogic.processo.statistiche;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.service.businesslogic.processo.domanda.ElaboraDomanda;
import it.tndigitale.a4gistruttoria.service.businesslogic.statistica.GeneraStatisticaCs22;

@Component("STATISTICA_CS22")
public class ElaboraGeneraStatisticaCs22 extends ElaboraDomanda{

    @Autowired
    private GeneraStatisticaCs22 service;

    @Override
    protected GeneraStatisticaCs22 getElaborazioneDomandaService() {
        return service;
    }

}
