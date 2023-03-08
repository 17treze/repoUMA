package it.tndigitale.a4gistruttoria.action.acs;

import java.util.Optional;
import java.util.function.BiConsumer;

import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloAccoppiatoHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.component.acs.ControlloRegistroOlioDOP;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@Component
public class InitVariabileOlioDOPConsumer implements BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel> {

	private static final Logger logger = LoggerFactory.getLogger(InitVariabileOlioDOPConsumer.class);
	
	@Autowired
	private ControlloRegistroOlioDOP controllo;
	
	public static final TipoVariabile OLIOQUAL = TipoVariabile.OLIOQUAL;

	@Override
	public void accept(CalcoloAccoppiatoHandler handler, IstruttoriaModel istruttoria) {
		DomandaUnicaModel domanda = istruttoria.getDomandaUnicaModel();
		logger.debug("Verifico se la domanda {} e' in elenco olio qualita", domanda.getId());
		Optional<Boolean> result = isRegistroOlivoDOP(domanda.getCuaaIntestatario(), domanda.getCampagna().intValue());
		if (result.isPresent()) {
			handler.getVariabiliInput().add(new VariabileCalcolo(OLIOQUAL, result.get()));
		} else {
			handler.getVariabiliInput().add(new VariabileCalcolo(OLIOQUAL));
		}
	}

	protected Optional<Boolean> isRegistroOlivoDOP(String cuaa, Integer annoCampagna) {
		if (cuaa != null) {
			return Optional.of(controllo.checkRegistroOlioDOP(cuaa, annoCampagna));
		} else {
			return Optional.empty();
		}
	}
}
