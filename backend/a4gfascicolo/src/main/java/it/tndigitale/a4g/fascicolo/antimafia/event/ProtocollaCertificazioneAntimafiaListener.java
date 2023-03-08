package it.tndigitale.a4g.fascicolo.antimafia.event;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.fascicolo.antimafia.dto.ProtocollaCertificazioneAntimafiaDto;
import it.tndigitale.a4g.fascicolo.antimafia.service.AntimafiaService;
import it.tndigitale.a4g.fascicolo.antimafia.service.AntimafiaServiceImpl;
import it.tndigitale.a4g.framework.client.custom.DocumentDto;
import it.tndigitale.a4g.framework.client.custom.MetadatiDto;
import it.tndigitale.a4g.framework.client.custom.ProtocolloClient;
import it.tndigitale.a4g.framework.event.store.annotation.ReprocessEvent;
import it.tndigitale.a4g.framework.extension.DefaultNameExtension;

@Service
public class ProtocollaCertificazioneAntimafiaListener {
	private static final Logger logger = LoggerFactory.getLogger(AntimafiaServiceImpl.class);

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired AntimafiaService antimafiaService;

	@Autowired protected ProtocolloClient protocolloClient;
	
	@Async(DefaultNameExtension.DEFAULT_SECURITY_THREAD_ASYNC)
	@TransactionalEventListener
	@ReprocessEvent
	public void handleEvent(ProtocollaCertificazioneAntimafiaEvent event) throws Exception {
		logger.info("start");
		Thread.sleep(100); // TODO [temporaneo] ritardo perche' il publisher possa terminare correttamente la transazione
		try {
			ProtocollaCertificazioneAntimafiaDto protocollaCertificazioneAntimafiaDto = event.getData();
			DocumentDto document = new DocumentDto();
			
			MetadatiDto infoDto = objectMapper.readValue(objectMapper.writeValueAsString(protocollaCertificazioneAntimafiaDto.getJsonProtocollazione()), MetadatiDto.class);
			document.setMetadati(infoDto);
			document.setDocumentoPrincipale(protocollaCertificazioneAntimafiaDto.getDocumento());
			document.setAllegati(protocollaCertificazioneAntimafiaDto.getAllegati());
			logger.debug("Antimafia da protocollare:" + document);
			String numeroProtocollazione = protocolloClient.protocolla(document);
			logger.debug("Domanda Antimafia protocollata con id {}", numeroProtocollazione);
			
			antimafiaService.saveOrUpdateDichiarazioneAntimafia(protocollaCertificazioneAntimafiaDto.getIdDomandaAntimafia(), numeroProtocollazione, new Date());
			
		} catch (Throwable t) {
			logger.error("Listener", t);
			throw new RuntimeException(t);
		} finally {
			logger.info("end");
		}
	}
}
