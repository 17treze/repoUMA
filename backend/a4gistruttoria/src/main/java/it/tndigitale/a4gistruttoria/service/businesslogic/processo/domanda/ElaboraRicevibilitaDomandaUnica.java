package it.tndigitale.a4gistruttoria.service.businesslogic.processo.domanda;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.service.businesslogic.domanda.RiceviDomandaService;

@Component("RICEVIBILITA_AGS")
public class ElaboraRicevibilitaDomandaUnica extends ElaboraDomanda {
    private static final Logger logger = LoggerFactory.getLogger(ElaboraRicevibilitaDomandaUnica.class);

    @Autowired
    private RiceviDomandaService domandaService;

	@Override
	protected RiceviDomandaService getElaborazioneDomandaService() {
		return domandaService;
	}
}
