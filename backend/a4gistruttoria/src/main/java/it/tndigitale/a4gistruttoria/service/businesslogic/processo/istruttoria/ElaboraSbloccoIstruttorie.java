package it.tndigitale.a4gistruttoria.service.businesslogic.processo.istruttoria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.service.businesslogic.SbloccaIstruttoriaService;

@Component("SBLOCCO_ISTRUTTORIE")
public class ElaboraSbloccoIstruttorie extends ElaboraIstruttoria {

	@Autowired
	private SbloccaIstruttoriaService service;

	@Override
	protected SbloccaIstruttoriaService getElaborazioneIstruttoriaService() {
		return service;
	}

	@Override
	protected boolean isIstruttoriaLavorabile(Long idIstruttoria) {
		return !super.isIstruttoriaLavorabile(idIstruttoria);
	}
}
