package it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import it.tndigitale.a4g.fascicolo.anagrafica.dto.RichiestaRevocaImmediataDto;
import it.tndigitale.a4g.framework.client.custom.DocumentDto;
import it.tndigitale.a4g.framework.client.custom.ProtocolloClient;
import it.tndigitale.a4g.framework.event.store.annotation.ReprocessEvent;
import it.tndigitale.a4g.framework.extension.DefaultNameExtension;

@Component
class RevocaImmediataMandatoListener {
	private static final Logger logger = LoggerFactory.getLogger(RevocaImmediataMandatoListener.class);

	@Autowired
	private ProtocolloClient clientProtocollo;
	
	@Autowired
	private RevocaImmediataMandatoService protocollazioneRevocaImmediataMandatoService;
	
	@Async(DefaultNameExtension.DEFAULT_SECURITY_THREAD_ASYNC)
	@ReprocessEvent
	@TransactionalEventListener
	void gestioneRevocaImmediata(RevocaImmediataMandatoEvent event) {
		RichiestaRevocaImmediataDto data = event.getData();
		logger.debug(
				"Mi accingo a protocollare la richiesta di revoca immediata per il mandato da parte del titolare/RL con cuaa {} all'ufficio {}",
				data.getCodiceFiscale(), data.getIdentificativoSportello());
		DocumentDto docDto = protocollazioneRevocaImmediataMandatoService.getDocumentDto(data, 0);
		String numeroProtocollo = clientProtocollo.protocolla(docDto);
		protocollazioneRevocaImmediataMandatoService.updateRevocaImmediataConProtocollo(data, numeroProtocollo);
	}
}
