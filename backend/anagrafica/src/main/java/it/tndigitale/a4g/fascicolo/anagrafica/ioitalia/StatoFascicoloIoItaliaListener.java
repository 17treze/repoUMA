package it.tndigitale.a4g.fascicolo.anagrafica.ioitalia;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import it.tndigitale.a4g.framework.event.store.annotation.ReprocessEvent;
import it.tndigitale.a4g.framework.extension.DefaultNameExtension;

@Service
public class StatoFascicoloIoItaliaListener {
	private static final Logger logger = LoggerFactory.getLogger(StatoFascicoloIoItaliaListener.class);

	@Autowired
	private IoItaliaSenderService ioItSenderService;

	@Async(DefaultNameExtension.DEFAULT_SECURITY_THREAD_ASYNC)
	@TransactionalEventListener
	@ReprocessEvent
	public void handleEvent(ModificaStatoFascicoloIoItaliaEvent event) {
		logger.info("StatoFascicoloIoItaliaListener event start");
		IoItaliaMessage data = event.getData();
		if (event.getNumberOfRetry() < data.getNumMaxTentativi()) {
			try {
				logger.info("StatoFascicoloIoItaliaListener sending message");
				ioItSenderService.inviaNotificaSetStato(data);
			}
			catch (Exception e) {
				logger.error("Errore durante l'invio della notifica IoItalia", e);
			}
		}
		logger.info("StatoFascicoloIoItaliaListener event end");
	}
}
