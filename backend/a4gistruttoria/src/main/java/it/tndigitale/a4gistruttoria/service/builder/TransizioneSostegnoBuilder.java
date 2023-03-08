package it.tndigitale.a4gistruttoria.service.builder;

import it.tndigitale.a4gistruttoria.repository.model.A4gdStatoLavSostegno;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;

public class TransizioneSostegnoBuilder {

    public TransizioneIstruttoriaModel from(IstruttoriaModel istruttoria, A4gdStatoLavSostegno nuovoStato) {
        TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
        transizione.setIstruttoria(istruttoria);
        //transizione.setDataEsecuzione();
        return transizione;
    }

}
