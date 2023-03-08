package it.tndigitale.a4gistruttoria.service.businesslogic.processo.istruttoria;

import it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia.CalcoloAccoppiatoZootecniaService;
import it.tndigitale.a4gistruttoria.service.businesslogic.ElaborazioneIstruttoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("CALCOLO_ACCOPPIATO_ZOOTECNIA_ISTRUTTORIA")
public class ElaboraCalcoloAccoppiatoZootecnia extends ElaboraIstruttoria {

    @Autowired
    private CalcoloAccoppiatoZootecniaService service;

    @Override
    protected ElaborazioneIstruttoria getElaborazioneIstruttoriaService() {
        return service;
    }

}
