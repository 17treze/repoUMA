package it.tndigitale.a4gistruttoria.service.businesslogic.processo.sincronizzazione;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.service.businesslogic.processo.domanda.ElaboraDomanda;
import it.tndigitale.a4gistruttoria.service.businesslogic.sincronizzazione.GeneraDatiSincronizzazioneSuperficiAccertate;

@Component("SINCRONIZZAZIONE_SUPERFICI_ACCERTATE")
public class ElaboraSincronizzazioneSuperficiAccertate extends ElaboraDomanda {

    @Autowired
    private GeneraDatiSincronizzazioneSuperficiAccertate service;

    @Override
    protected GeneraDatiSincronizzazioneSuperficiAccertate getElaborazioneDomandaService() {
        return service;
    }
}