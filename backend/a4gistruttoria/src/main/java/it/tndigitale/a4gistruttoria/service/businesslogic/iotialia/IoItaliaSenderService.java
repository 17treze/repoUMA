package it.tndigitale.a4gistruttoria.service.businesslogic.iotialia;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4gistruttoria.component.IoItaliaInvioMessaggioFactory;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;

@Service
public class IoItaliaSenderService {
	
	private static Logger log = LoggerFactory.getLogger(IoItaliaSenderService.class);
	
	@Autowired
	private IoItaliaInvioMessaggioFactory ioItaliaInvioMessaggioFactory;
	
	
	public void recuperaFactoryEinviaMessaggio(IstruttoriaModel istruttoriaModel) {
		try {
			//IOIT-DU-01-xx - invio messaggio IO Italia
			IoItaliaMessaggioByStato ioItaliaInvioMessaggioByStato = 
					ioItaliaInvioMessaggioFactory
					.getIoItaliaInvioMessaggioByStato(
							IoItaliaMessaggioByStato.getNomeQualificatore(istruttoriaModel.getStato())
							);
			ioItaliaInvioMessaggioByStato.inviaMessaggio(istruttoriaModel.getId());
		} catch (NoSuchBeanDefinitionException e) {
			// DO NOTHING
			//l'invio del messaggio è previsto solo per questi stati: NON_AMMISSIBILE, PAGAMENTO_AUTORIZZATO, NON LIQUIDABILE e PAGAMENTO NON AUTORIZZATO
			//questa eccezione viene lanciata nel caso in cui non è implementato l'invio per quello stato
		} catch (Exception e) {
			// L'invio del messaggio non è bloccante ai fini del cambio di stato dell'istruttoria.
			log.debug("Errore nell'invio del messaggio ad IoItalia: ", e);
		}
	}

}
