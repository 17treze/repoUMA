package it.tndigitale.a4gistruttoria.action.acs;

import java.util.Optional;
import java.util.function.BiConsumer;

import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloAccoppiatoHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.component.acs.ControlloRegistroOlioNazionale;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@Component
public class InitVariabileOlioNazionaleConsumer implements BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel> {

	private static final Logger logger = LoggerFactory.getLogger(InitVariabileOlioNazionaleConsumer.class);
	
	@Autowired
	private ControlloRegistroOlioNazionale controllo;
	
	public static final TipoVariabile OLIONAZ = TipoVariabile.OLIONAZ;

	@Override
	public void accept(CalcoloAccoppiatoHandler handler, IstruttoriaModel istruttoria) {
		DomandaUnicaModel domanda = istruttoria.getDomandaUnicaModel();
		logger.debug("Verifico se la domanda {} e' in elenco olio nazionale", domanda.getId());
		Optional<Boolean> result = isRegistroNazionaleOlivo(domanda.getCuaaIntestatario(), Integer.parseInt(domanda.getCampagna().toString()));
		if (result.isPresent()) {
			handler.getVariabiliInput().add(new VariabileCalcolo(OLIONAZ, result.get()));
		} else {
			handler.getVariabiliInput().add(new VariabileCalcolo(OLIONAZ));
		}
	}

	protected Optional<Boolean> isRegistroNazionaleOlivo(String cuaa, Integer annoCampagna) {
		if (cuaa != null) {
			return Optional.of(controllo.checkRegistroOlioNazionale(cuaa, annoCampagna));
		} else {
			return Optional.empty();
		}
	}
	
}
