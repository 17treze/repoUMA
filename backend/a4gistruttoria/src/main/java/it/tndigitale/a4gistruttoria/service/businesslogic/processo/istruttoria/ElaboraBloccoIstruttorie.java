package it.tndigitale.a4gistruttoria.service.businesslogic.processo.istruttoria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.service.businesslogic.BloccaIstruttoriaService;

@Component("BLOCCO_ISTRUTTORIE")
public class ElaboraBloccoIstruttorie extends ElaboraIstruttoria {

	@Autowired
	private BloccaIstruttoriaService service;

	@Override
	protected BloccaIstruttoriaService getElaborazioneIstruttoriaService() {
		return service;
	}
}
