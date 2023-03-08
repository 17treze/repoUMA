package it.tndigitale.a4gistruttoria.service.businesslogic.processo.sincronizzazione;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.service.businesslogic.processo.domanda.ElaboraDomanda;
import it.tndigitale.a4gistruttoria.service.businesslogic.sincronizzazione.GeneraDatiSincronizzazionePagamenti;

@Component("SINCRONIZZAZIONE_PAGAMENTI")
public class ElaboraSincronizzazionePagamenti extends ElaboraDomanda {

    @Autowired
    private GeneraDatiSincronizzazionePagamenti service;

    @Override
    protected GeneraDatiSincronizzazionePagamenti getElaborazioneDomandaService() {
        return service;
    }
}
