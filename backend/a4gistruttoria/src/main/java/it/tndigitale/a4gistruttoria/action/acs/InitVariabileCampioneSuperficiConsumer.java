package it.tndigitale.a4gistruttoria.action.acs;

import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.dao.CampioneDao;
import it.tndigitale.a4gistruttoria.repository.model.CampioneModel;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloAccoppiatoHandler;
import it.tndigitale.a4gistruttoria.repository.model.AmbitoCampione;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Component
public class InitVariabileCampioneSuperficiConsumer implements BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel> {

	@Autowired
	private CampioneDao daoCampione;

	private static final Logger logger = LoggerFactory.getLogger(InitVariabileCampioneSuperficiConsumer.class);
	
	public static final TipoVariabile ISCAMP = TipoVariabile.ISCAMP;
	
	@Override
	public void accept(CalcoloAccoppiatoHandler handler, IstruttoriaModel istruttoria) {
		DomandaUnicaModel domanda = istruttoria.getDomandaUnicaModel();
		logger.debug("Carico le informazioni del campione per la domanda {}", domanda.getId());
		CampioneModel campione = daoCampione.findByCuaaAndAmbitoCampioneAndAnnoCampagna(domanda.getCuaaIntestatario(),
				AmbitoCampione.SUPERFICIE, domanda.getCampagna());
		handler.getVariabiliInput().add(new VariabileCalcolo(ISCAMP, campione != null));
	}
	
}
