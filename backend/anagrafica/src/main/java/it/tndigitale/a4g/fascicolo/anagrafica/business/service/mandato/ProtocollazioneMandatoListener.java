package it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import it.tndigitale.a4g.fascicolo.anagrafica.dto.ApriFascicoloDto;
import it.tndigitale.a4g.framework.client.custom.DocumentDto;
import it.tndigitale.a4g.framework.client.custom.ProtocolloClient;
import it.tndigitale.a4g.framework.event.store.annotation.ReprocessEvent;
import it.tndigitale.a4g.framework.extension.DefaultNameExtension;

@Component
class ProtocollazioneMandatoListener {
	private static final Logger logger = LoggerFactory.getLogger(ProtocollazioneMandatoListener.class);

	@Autowired
	private ProtocolloClient clientProtocollo;
	
	@Autowired
	private ProtocollazioneMandatoService protocollazioneMandatoService;

	@Async(DefaultNameExtension.DEFAULT_SECURITY_THREAD_ASYNC)
	@ReprocessEvent
	@TransactionalEventListener
	void protocollaMandato(StipulaNuovoMandatatoEvent event) {
		ApriFascicoloDto data = event.getData();
		logger.debug("Mi accingo a protocollare il nuovo mandato per il cuaa {} all'ufficio {}", data.getCodiceFiscale(), data.getIdentificativoSportello());

		DocumentDto docDto = protocollazioneMandatoService.getDocumentDto(data, 0);

		clientProtocollo.protocolla(docDto);
	}
}
