package it.tndigitale.a4g.uma.business.service.protocollo;

import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import it.tndigitale.a4g.framework.client.custom.DocumentDto;
import it.tndigitale.a4g.framework.client.custom.ProtocolloClient;
import it.tndigitale.a4g.framework.event.store.annotation.ReprocessEvent;
import it.tndigitale.a4g.framework.extension.DefaultNameExtension;
import it.tndigitale.a4g.uma.GeneralFactory;

@Component
public class ProtocollaDocumentoUmaListener {
	
	private static final Logger logger = LoggerFactory.getLogger(ProtocollaDocumentoUmaListener.class);
	
	@Autowired
	private ProtocolloClient clientProtocollo;
	@Autowired
	private GeneralFactory protocollazioneFactory;
	
	@Async(DefaultNameExtension.DEFAULT_SECURITY_THREAD_ASYNC)
	@ReprocessEvent
	@TransactionalEventListener
	void handleEvent(ProtocollaDocumentoUmaEvent event) {
		try {
			var data = event.getData();
			ProtocollazioneStrategy protocollazioneStrategy = protocollazioneFactory
					.getProtocollazioneStrategy("PROTOCOLLA_" + data.getTipoDocumentoUma().name());
			DocumentDto docDto = protocollazioneStrategy.buildDocumentDto(data);
			logger.info("Avvio Protocollo per domanda {} . CUAA {} Richiedente {}", data.getTipoDocumentoUma(),
					data.getCuaa(), data.getNome() + " " + data.getCognome());
			String numeroProtocollo = "prot" + ThreadLocalRandom.current().nextInt(33, 77); // clientProtocollo.protocolla(docDto);
			protocollazioneStrategy.aggiornaDomanda(event.getData(), numeroProtocollo);
		}
		catch (Exception t) {
			throw new ProtocollazioneUmaException(t.getMessage());
		}
	}
	
}