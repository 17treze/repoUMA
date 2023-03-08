package it.tndigitale.a4gistruttoria.service.businesslogic.processo.istruttoria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato.CalcoloIstruttoriaDisaccoppiatoService;

@Component("CALCOLO_DISACCOPPIATO_ISTRUTTORIA")
public class ElaboraCalcoloDisaccoppiato extends ElaboraIstruttoria {

	@Autowired
	private CalcoloIstruttoriaDisaccoppiatoService service;

	@Override
	protected CalcoloIstruttoriaDisaccoppiatoService getElaborazioneIstruttoriaService() {
		return service;
	}

}
