package it.tndigitale.a4gistruttoria.service.businesslogic.processo.istruttoria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.service.businesslogic.superficie.CalcoloAccoppiatoSuperficieService;
import it.tndigitale.a4gistruttoria.service.businesslogic.ElaborazioneIstruttoria;

@Component("CALCOLO_ACCOPPIATO_SUPERFICIE_ISTRUTTORIA")
public class ElaboraCalcoloAccoppiatoSuperficie extends ElaboraIstruttoria {

    @Autowired
    private CalcoloAccoppiatoSuperficieService service;

    @Override
    protected ElaborazioneIstruttoria getElaborazioneIstruttoriaService() {
        return service;
    }

}
