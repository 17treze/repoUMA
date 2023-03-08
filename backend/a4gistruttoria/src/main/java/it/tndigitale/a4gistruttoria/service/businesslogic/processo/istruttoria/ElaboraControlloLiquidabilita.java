package it.tndigitale.a4gistruttoria.service.businesslogic.processo.istruttoria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.service.businesslogic.liquidabilita.ControlloLiquidabilitaService;

@Component("CONTROLLO_LIQUIDABILITA_ISTRUTTORIA")
public class ElaboraControlloLiquidabilita extends ElaboraIstruttoria {

	@Autowired
	private ControlloLiquidabilitaService service;

	@Override
	protected ControlloLiquidabilitaService getElaborazioneIstruttoriaService() {
		return service;
	}

}
