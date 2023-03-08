package it.tndigitale.a4g.fascicolo.anagrafica.business.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import it.tndigitale.a4g.fascicolo.anagrafica.business.service.PersonaConCaricaService;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloService;
import it.tndigitale.a4g.framework.event.store.annotation.ReprocessEvent;
import it.tndigitale.a4g.framework.extension.DefaultNameExtension;

@Service
public class PersoneFisicheConCaricaListener {
	private static final Logger logger = LoggerFactory.getLogger(PersoneFisicheConCaricaListener.class);

	@Autowired PersonaConCaricaService personaConCaricaService;
	@Autowired FascicoloService fascicoloService;
	
	@Async(DefaultNameExtension.DEFAULT_SECURITY_THREAD_ASYNC)
	@TransactionalEventListener
	@ReprocessEvent
	public void handleEvent(PersoneFisicheConCaricaEvent event) throws PersoneFisicheConCaricaListenerException {
		logger.info("start");
		try {
			personaConCaricaService.persistPersoneFisicheConCarica(event.getData());
			fascicoloService.fascicoloAggiornaDataFontiEsterne(event.getData().getIdFascicolo());
		} catch (Exception ex) {
			throw new PersoneFisicheConCaricaListenerException(ex);
		} finally {
			logger.info("end");
		}
	}
}
