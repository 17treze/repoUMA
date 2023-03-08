package it.tndigitale.a4g.fascicolo.mediator.business.service.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import it.tndigitale.a4g.fascicolo.mediator.business.service.client.AnagraficaPrivateClient;
import it.tndigitale.a4g.fascicolo.mediator.business.service.fascicolo.DotazioneTecnicaService;
import it.tndigitale.a4g.fascicolo.mediator.business.service.fascicolo.FascicoloValidazioneService;
import it.tndigitale.a4g.fascicolo.mediator.business.service.fascicolo.SincronizzazioneFascicoloService;
import it.tndigitale.a4g.fascicolo.mediator.business.service.fascicolo.ZootecniaService;
import it.tndigitale.a4g.fascicolo.mediator.dto.SchedaValidazioneFascicoloDto;
import it.tndigitale.a4g.fascicolo.mediator.dto.TipoDetenzioneEnum;
import it.tndigitale.a4g.framework.extension.DefaultNameExtension;

@Service
public class ValidazioneFascicoloListener {
	private static final Logger log = LoggerFactory.getLogger(ValidazioneFascicoloListener.class);
	@Autowired private FascicoloValidazioneService validazioneFascicoloService;
	@Autowired private SincronizzazioneFascicoloService sincronizzazioneFascicoloService;
	@Autowired private ZootecniaService zootecniaService;
	@Autowired private DotazioneTecnicaService dotazioneTecnicaService;
	@Autowired private AnagraficaPrivateClient anagraficaPrivateClient;
	
	@Async(DefaultNameExtension.DEFAULT_SECURITY_THREAD_ASYNC)
	@TransactionalEventListener
	public void handleEvent(StartValidazioneFascicoloEvent event) {
		SchedaValidazioneFascicoloDto eventData = event.getData();
		String cuaa = eventData.getCodiceFiscale();
		Integer idValidazione = eventData.getNextIdValidazione();
		Integer result = 0;

		if (eventData.getTipoDetenzione() == TipoDetenzioneEnum.DETENZIONE_IN_PROPRIO) {
			result = validazioneFascicoloService.startValidazioneFascicoloAutonomoAsincrona(event, cuaa, idValidazione);
		} else { 
//			MANDATO e' il tipo di detenzione di default
			try {
				anagraficaPrivateClient.notificaMailCaaSchedaValidazioneAccettataUsingPOST(cuaa);
			} catch (Exception e) {
				log.warn("Errore: {} - {}", e.getMessage(), e.getCause());
			}
			result = validazioneFascicoloService.startValidazioneFascicoloAsincrona(event, cuaa, idValidazione);
		}
		
		try {
			if (result == 0) {
//				invio evento fine validazione per avvio sincronizzazione AGS.
				sincronizzazioneFascicoloService.invioEventoFineValidazione(eventData);
				
				if (eventData.getTipoDetenzione() == TipoDetenzioneEnum.MANDATO) {
					// chiamata a zootecniaClient per sincronizzazione AGS
					zootecniaService.invioEventoFineValidazione(cuaa, idValidazione);

					// chiamata a dotazioneTecnicaClient per validazione
					dotazioneTecnicaService.invioEventoFineValidazione(cuaa, idValidazione);
					
//					invio mail al CAA detentore del mandato: se errore non deve far fallire la transazione
					anagraficaPrivateClient.notificaMailCaaRichiestaValidazioneAccettataUsingPOST(cuaa);
				}
			}
		} catch (Exception e) {
			log.warn("Errore: {} - {}", e.getMessage(), e.getCause());
		}
	}
}
