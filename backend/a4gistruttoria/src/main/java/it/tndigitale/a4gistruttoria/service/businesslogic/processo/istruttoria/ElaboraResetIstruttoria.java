package it.tndigitale.a4gistruttoria.service.businesslogic.processo.istruttoria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.service.businesslogic.ElaborazioneIstruttoria;
import it.tndigitale.a4gistruttoria.strategy.ResetIstruttoriaStrategy;

@Component("RESET_ISTRUTTORIA")
public class ElaboraResetIstruttoria extends ElaboraIstruttoria {
	@Autowired
	ResetIstruttoriaStrategy service;
	
	@Override
	protected ElaborazioneIstruttoria getElaborazioneIstruttoriaService() {
		return service;
	}
}
