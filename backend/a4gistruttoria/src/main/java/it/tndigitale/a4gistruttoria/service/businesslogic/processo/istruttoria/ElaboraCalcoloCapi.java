package it.tndigitale.a4gistruttoria.service.businesslogic.processo.istruttoria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.service.businesslogic.ElaborazioneIstruttoria;
import it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia.calcolocapi.CalcoloCapiService;

@Component("CALCOLO_CAPI_ISTRUTTORIE")
public class ElaboraCalcoloCapi extends ElaboraIstruttoria {

    @Autowired
    private CalcoloCapiService service;
    
	@Override
	protected ElaborazioneIstruttoria getElaborazioneIstruttoriaService() {
		return service;
	}

}
