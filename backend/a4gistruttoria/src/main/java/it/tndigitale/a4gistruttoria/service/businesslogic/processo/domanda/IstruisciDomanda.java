package it.tndigitale.a4gistruttoria.service.businesslogic.processo.domanda;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.service.businesslogic.domanda.IstruisciDomandaService;

@Component("AVVIO_ISTRUTTORIA")
public class IstruisciDomanda extends ElaboraDomanda {

    @Autowired
    private IstruisciDomandaService service;

    @Override
    protected IstruisciDomandaService getElaborazioneDomandaService() {
        return service;
    }
}
