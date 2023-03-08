package it.tndigitale.a4g.fascicolo.mediator.business.service.fascicolo;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4g.fascicolo.mediator.business.service.client.AnagraficaPrivateClient;
import it.tndigitale.a4g.fascicolo.mediator.business.service.client.DotazioneTecnicaPrivateClient;
import it.tndigitale.a4g.fascicolo.mediator.business.service.event.DotazioneTecnicaEndValidazioneFascicoloEvent;
import it.tndigitale.a4g.framework.event.store.handler.EventBus;

@Service
public class DotazioneTecnicaService {
	@Autowired 
	private EventBus eventBus;
	@Autowired
	private DotazioneTecnicaPrivateClient dotazioneTecnicaPrivateClient;
	@Autowired
	private AnagraficaPrivateClient anagraficaPrivateClient;

	@Transactional
	public void invioEventoFineValidazione(final String cuaa, final Integer idValidazione){
		var event = new DotazioneTecnicaEndValidazioneFascicoloEvent(cuaa, idValidazione);
		eventBus.publishEvent(event);
	}
	
	public Long dichiaraMacchina(final String dettaglioMacchinaDto, final String cuaa, final File documento) {
		// Post macchina su dotazione tecnica
		Long idMacchinaSalvata = null;
		try {
			idMacchinaSalvata = dotazioneTecnicaPrivateClient.postMacchina(cuaa, dettaglioMacchinaDto, documento);
		} catch (Exception e) {
			String message;
			if (e.getLocalizedMessage().startsWith("409")) {
				message = "Attenzione! Il macchinario è già stato inserito nel fascicolo";
				throw new IllegalArgumentException(message);
			} else {
			throw new IllegalArgumentException(e.getMessage());
			}
		}
		// Aggiornamento fascicolo
		anagraficaPrivateClient.putFascicoloStatoControlliInAggiornamentoUsingPUT(cuaa);
		return idMacchinaSalvata;
	}
	
	public void cancellaMacchina(final String cuaa, final Long idMacchina) {
		// Delete macchina su dotazione tecnica
		dotazioneTecnicaPrivateClient.deleteMacchina(cuaa, idMacchina);
		// Aggiornamento fascicolo
		anagraficaPrivateClient.putFascicoloStatoControlliInAggiornamentoUsingPUT(cuaa);
	}
}
