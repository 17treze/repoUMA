package it.tndigitale.a4gutente.event;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.framework.client.custom.DocumentDto;
import it.tndigitale.a4g.framework.client.custom.MetadatiDto;
import it.tndigitale.a4g.framework.client.custom.ProtocolloClient;
import it.tndigitale.a4g.framework.event.store.annotation.ReprocessEvent;
import it.tndigitale.a4g.framework.extension.DefaultNameExtension;
import it.tndigitale.a4gutente.dto.InfoPrivacyDto;
import it.tndigitale.a4gutente.dto.Persona;
import it.tndigitale.a4gutente.service.PersonaService;

/**
 * @author ite3279
 */
@Component
public class InfoPrivacyListener{
	private static final Logger logger = LoggerFactory.getLogger(InfoPrivacyListener.class);
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	protected ProtocolloClient protocolloClient;
	
	@Autowired private PersonaService personaService;
	
	@Async(DefaultNameExtension.DEFAULT_SECURITY_THREAD_ASYNC)
	@TransactionalEventListener
	@ReprocessEvent
	public void handleEvent(InfoPrivacyEvent event) throws Exception {
		logger.info("start");
		try {
			InfoPrivacyDto infoPrivacyDto = event.getData();
			DocumentDto document = new DocumentDto();
			MetadatiDto infoDto = objectMapper.readValue(infoPrivacyDto.getInfoIn(), MetadatiDto.class);
			document.setMetadati(infoDto);
			document.setDocumentoPrincipale(infoPrivacyDto.getDocumento());
			document.setAllegati(infoPrivacyDto.getAllegati());
			logger.info("InfoPrivacy da protocollare:" + document);
			String numeroProtocollazione = protocolloClient.protocolla(document);
			logger.debug("Domanda di accesso al sistema protocollata con id {}", numeroProtocollazione);
			List<Persona> ricercaPersone = personaService.ricercaPersone(infoPrivacyDto.getRichiedenteCodiceFiscale());
			Persona persona = ricercaPersone.get(0);
			persona.setNrProtocolloPrivacyGenerale(numeroProtocollazione);
			personaService.inserisciAggiornaPersona(persona);
			logger.info("end");
		}catch(Throwable t) {
			logger.error("Listener", t);
			throw new RuntimeException(t);
		} 
		finally {
			logger.info("end");
		}
	}
}
