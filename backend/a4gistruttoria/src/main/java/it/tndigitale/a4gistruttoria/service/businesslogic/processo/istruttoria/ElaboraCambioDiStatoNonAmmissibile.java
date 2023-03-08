package it.tndigitale.a4gistruttoria.service.businesslogic.processo.istruttoria;

import it.tndigitale.a4gistruttoria.service.businesslogic.ElaborazioneIstruttoria;
import it.tndigitale.a4gistruttoria.service.businesslogic.ElaborazioneIstruttoriaPerCambioStatoNonAmmissibileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("NON_AMMISSIBILITA")
public class ElaboraCambioDiStatoNonAmmissibile extends ElaboraIstruttoria {

    @Autowired
    private ElaborazioneIstruttoriaPerCambioStatoNonAmmissibileService service;

    @Override
    protected ElaborazioneIstruttoria getElaborazioneIstruttoriaService() {
        return service;
    }

}
