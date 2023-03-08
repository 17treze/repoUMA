package it.tndigitale.a4gistruttoria.action.acz;

import java.util.function.BiConsumer;

import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloAccoppiatoHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.component.acz.ControlloCampioneZootecnia;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@Component
public class InitVariabiliCampioneZootecniaConsumer implements BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel> {

	@Autowired
	private ControlloCampioneZootecnia controlloCampioneZootecnia;

	private static final Logger logger = LoggerFactory.getLogger(InitVariabiliCampioneZootecniaConsumer.class);
	
	public static final TipoVariabile AZCMPBOV = TipoVariabile.AZCMPBOV;
	public static final TipoVariabile AZCMPOVI = TipoVariabile.AZCMPOVI;
	
	@Override
	public void accept(CalcoloAccoppiatoHandler handler, IstruttoriaModel istruttoria ) {
		DomandaUnicaModel domanda = istruttoria.getDomandaUnicaModel();
		logger.debug("Carico le informazioni del campione per la domanda {}", domanda.getId());
		
		handler.getVariabiliInput().add(new VariabileCalcolo(AZCMPBOV, controlloCampioneZootecnia.checkCampioneBovini(domanda.getCampagna(), domanda.getCuaaIntestatario())));
		handler.getVariabiliInput().add(new VariabileCalcolo(AZCMPOVI, controlloCampioneZootecnia.checkCampioneOvicaprini(domanda.getCampagna(), domanda.getCuaaIntestatario())));
	}
	
}
