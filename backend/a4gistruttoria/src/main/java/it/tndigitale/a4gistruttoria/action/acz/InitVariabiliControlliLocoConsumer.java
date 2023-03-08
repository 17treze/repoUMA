package it.tndigitale.a4gistruttoria.action.acz;

import java.util.function.BiConsumer;

import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloAccoppiatoHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.component.acz.ControlloDatiIstruttoriaControlliInLoco;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@Component
public class InitVariabiliControlliLocoConsumer implements BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel> {

	@Autowired
	private ControlloDatiIstruttoriaControlliInLoco controlliInLoco;

	private static final Logger logger = LoggerFactory.getLogger(InitVariabiliControlliLocoConsumer.class);
	
	public static final TipoVariabile ACZCONTROLLILOCO = TipoVariabile.ACZCONTROLLILOCO;

	@Override
	public void accept(CalcoloAccoppiatoHandler handler, IstruttoriaModel istruttoria) {
		logger.debug("Carico le informazioni dell'esito controlli in loco per l'istruttoria{}", istruttoria.getId());
		handler.getVariabiliInput().add(new VariabileCalcolo(ACZCONTROLLILOCO, controlliInLoco.checkControlliInLocoOf(istruttoria)));
	}

}
