package it.tndigitale.a4gutente.event;

import java.time.LocalDateTime;
import java.util.Optional;

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
import it.tndigitale.a4gutente.dto.DomandaRegistrazioneUtenteDto;
import it.tndigitale.a4gutente.repository.dao.IDomandaRegistrazioneUtenteDao;
import it.tndigitale.a4gutente.repository.model.DomandaRegistrazioneUtente;

/**
 * @author ite3279
 */
@Component
public class DomandaRegistrazioneUtenteListener{
	private static final Logger logger = LoggerFactory.getLogger(DomandaRegistrazioneUtenteListener.class);
	
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	protected ProtocolloClient protocolloClient;

	@Autowired
	private IDomandaRegistrazioneUtenteDao domandaUtenteRep;

	@Async(DefaultNameExtension.DEFAULT_SECURITY_THREAD_ASYNC)
	@TransactionalEventListener
	@ReprocessEvent
	public void handleEvent(DomandaRegistrazioneUtenteEvent event) throws Exception {
		logger.info("start");
		try {
			DomandaRegistrazioneUtenteDto domandaRegistrataDto = event.getData();
			DocumentDto document = new DocumentDto();
			
			MetadatiDto infoDto = objectMapper.readValue(objectMapper.writeValueAsString(domandaRegistrataDto.getJsonProtocollazione()), MetadatiDto.class);
			document.setMetadati(infoDto);
			document.setDocumentoPrincipale(domandaRegistrataDto.getDocumentoPrincipale());
			document.setAllegati(domandaRegistrataDto.getAllegati());
			logger.info("Domanda accesso da protocollare:" + document);
			String numeroProtocollazione = protocolloClient.protocolla(document);

			Optional<DomandaRegistrazioneUtente> domandaRegistrataOptional = domandaUtenteRep.findById(domandaRegistrataDto.getIdDomanda());
			if(domandaRegistrataOptional.isPresent()) {
				DomandaRegistrazioneUtente  domandaRegistrata = domandaRegistrataOptional.get();
				domandaRegistrata.setIdProtocollo(numeroProtocollazione);
				domandaRegistrata.setDtProtocollazione(LocalDateTime.now());
				domandaUtenteRep.save(domandaRegistrata);
			}
		}catch(Throwable t) {
			logger.error("Listener", t);
			throw new RuntimeException(t);
		} 
		finally {
			logger.info("end");
		}
	}
}
