package it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo;

import it.tndigitale.a4g.framework.ext.validazione.fascicolo.StatoFascicoloEnum;

public class FascicoloNonInChiusuraException extends Exception {

    public FascicoloNonInChiusuraException(StatoFascicoloEnum stato) {
        super(stato.name());
    }
}
