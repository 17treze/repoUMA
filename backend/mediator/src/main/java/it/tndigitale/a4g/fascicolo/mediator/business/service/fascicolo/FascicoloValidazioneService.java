package it.tndigitale.a4g.fascicolo.mediator.business.service.fascicolo;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4g.fascicolo.anagrafica.client.model.FascicoloDto;
import it.tndigitale.a4g.fascicolo.anagrafica.client.model.FascicoloDto.StatoEnum;
import it.tndigitale.a4g.fascicolo.mediator.business.service.client.AnagraficaPrivateClient;
import it.tndigitale.a4g.fascicolo.mediator.business.service.client.DotazioneTecnicaPrivateClient;
import it.tndigitale.a4g.fascicolo.mediator.business.service.client.ZootecniaPrivateClient;
import it.tndigitale.a4g.fascicolo.mediator.business.service.event.StartValidazioneFascicoloEvent;
import it.tndigitale.a4g.fascicolo.mediator.dto.SchedaValidazioneFascicoloDto;
import it.tndigitale.a4g.fascicolo.mediator.dto.StatusMessagesEnum;
import it.tndigitale.a4g.fascicolo.mediator.dto.TipoDetenzioneEnum;
import it.tndigitale.a4g.framework.event.store.EventStoreService;
import it.tndigitale.a4g.framework.event.store.handler.EventBus;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.FascicoloNonValidabileException;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.StatoFascicoloEnum;


@Service
public class FascicoloValidazioneService {

	@Autowired private AnagraficaPrivateClient anagraficaPrivateClient;
	@Autowired private ZootecniaPrivateClient zootecniaPrivateClient;
	@Autowired private DotazioneTecnicaPrivateClient dotazioneTecnicaPrivateClient;
	@Autowired private EventStoreService eventStoreService;
	@Autowired private EventBus eventBus;
	@Autowired private ControlliCompletezzaFascicoloService controlliCompletezzaFascicoloService;
	
	/**
	 * metodo globale di storicizzazione del fascicolo. Questo metodo dovra' gestire la storicizzazione delle
	 * varie tabelle 
	 * @param cuaa
	 * @return -1 in caso di errore; 0 se e' andato a buon fine
	 * @throws IOException 
	 * @throws FascicoloNonValidabileException 
	 */
	@Transactional
	public Integer startValidazioneFascicoloAutonomoAsincrona(
			final StartValidazioneFascicoloEvent event,
			final String cuaa, final Integer idValidazione) {
		try {
	//		reperire per cuaa e id_validazione = 0
			FascicoloDto fascicoloDto = anagraficaPrivateClient.getByCuaaUsingGET1(cuaa, 0);
			if (fascicoloDto == null) {
				eventStoreService.triggerRetry(
						new FascicoloNonValidabileException(StatusMessagesEnum.FASCICOLO_LOCALE_NON_ESISTENTE.name()),
						event);
				return -1;
			}
			if (!fascicoloDto.getStato().equals(StatoEnum.IN_VALIDAZIONE)) {
				eventStoreService.triggerRetry(
						new FascicoloNonValidabileException(StatoFascicoloEnum.valueOf(fascicoloDto.getStato().name()) ),
						event);
				return -1;
			}
		
			anagraficaPrivateClient.startValidazioneFascicoloAutonomoAsincronaUsingPOST(cuaa, idValidazione, event.getUsername());
		} catch (Exception exception) {
			eventStoreService.triggerRetry(exception, event);
			return -1;
		}
		return 0;
	}

	
	/**
	 * metodo globale di storicizzazione del fascicolo. Questo metodo dovra' gestire la storicizzazione delle
	 * varie tabelle 
	 * @param cuaa
	 * @return -1 in caso di errore; 0 se e' andato a buon fine
	 * @throws IOException 
	 * @throws FascicoloNonValidabileException 
	 */
	@Transactional
	public Integer startValidazioneFascicoloAsincrona(
			final StartValidazioneFascicoloEvent event,
			final String cuaa, final Integer idValidazione) {
		try {
	//		reperire per cuaa e id_validazione = 0
			FascicoloDto fascicoloDto = anagraficaPrivateClient.getByCuaaUsingGET1(cuaa, 0);
			if (fascicoloDto == null) {
				eventStoreService.triggerRetry(
						new FascicoloNonValidabileException(StatusMessagesEnum.FASCICOLO_LOCALE_NON_ESISTENTE.name()),
						event);
				return -1;
			}
			if (!fascicoloDto.getStato().equals(StatoEnum.IN_VALIDAZIONE)) {
				eventStoreService.triggerRetry(
						new FascicoloNonValidabileException(StatoFascicoloEnum.valueOf(fascicoloDto.getStato().name()) ),
						event);
				return -1;
			}
			
			/*
			String urlValidazioneZootecnia = String.format("%s/%s/%d/validazione", urlZootecnia, cuaa, idValidazione); da aggiornare
			
			private boolean isADDAuthentication(String username) {
				return username.indexOf("@") > 0;
			}
			
			private Pair<String, String> buildUsernameHeader(Supplier<String> username) {
					String currentUser = username.get();
					if(currentUser == null || currentUser.trim().length() == 0) {
						return null;	
					}
					if (isADDAuthentication(currentUser)) {
						return Pair.of(HEADER_UPN, currentUser);
					} else {
						return Pair.of(HEADER_CF, currentUser);
					}
			}
			
			private HttpEntity<String> buildWith(Supplier<String> username) {
				HttpHeaders headers = new HttpHeaders();
				Pair<String, String> headerUsername = buildUsernameHeader(username);
				if(headerUsername == null){
					return new HttpEntity<>(headers);
				} else {
					headers.set(headerUsername.getFirst(), headerUsername.getSecond());
					return new HttpEntity<>(headers);	
				}
			}			
			
			private Flux<Void> getFluxValidazione(final String url) {
				return webClientBuilderTn.buildWith(utenteCorrente::utenza)
					.put()
					.uri(url)
					.retrieve()
					.bodyToFlux(Void.class);
			}
		
			Flux.merge(getFluxValidazione(urlValidazioneZootecnia))
				.doOnError(exception -> eventStoreService.triggerRetry(exception, event))
				.all(res -> true)
				.subscribe(res -> {
					try {
						validazioneFascicolo(fascicoloLive, cuaa, idValidazione, event.getUsername());
					} catch (FascicoloNonValidabileException exception) {
						eventStoreService.triggerRetry(exception, event);
					}
				});
			*/
			
			ResponseEntity<Void> startValidazioneFascicolo = zootecniaPrivateClient.startValidazioneFascicolo(cuaa, idValidazione);
			if (!startValidazioneFascicolo.getStatusCode().equals(HttpStatus.OK)) {
				eventStoreService.triggerRetry(
	    			new FascicoloNonValidabileException("Errore in validazione zootecnia"), event);
				return -1;
			}

			
		    startValidazioneFascicolo = dotazioneTecnicaPrivateClient.startValidazioneFascicolo(cuaa, idValidazione);
		    if (!startValidazioneFascicolo.getStatusCode().equals(HttpStatus.OK)) {
		        eventStoreService.triggerRetry(
		            new FascicoloNonValidabileException("Errore in validazione dotazione tecnica"), event);
		        return -1;
		    }
			
			anagraficaPrivateClient.startValidazioneFascicoloAsincronaUsingPOST(cuaa, idValidazione, event.getUsername());
		} catch (Exception exception) {
			eventStoreService.triggerRetry(exception, event);
			return -1;
		}
		return 0;
	}
	
	@Transactional
    public void validazioneDetenzioneAutonoma(final String cuaa, final byte[] schedaValidazioneBytes, final Integer nextIdValidazione)
    		 {
		var schedaFascicoloDto = new SchedaValidazioneFascicoloDto();
    	schedaFascicoloDto.setCodiceFiscale(cuaa);
    	schedaFascicoloDto.setTipoDetenzione(TipoDetenzioneEnum.DETENZIONE_IN_PROPRIO);
    	schedaFascicoloDto.setReport(new ByteArrayResource(schedaValidazioneBytes));
    	schedaFascicoloDto.setNextIdValidazione(nextIdValidazione);
		eventBus.publishEvent(new StartValidazioneFascicoloEvent(schedaFascicoloDto));
    }
	 
    @Transactional
    public void validazioneMandato(final String cuaa, final byte[] schedaValidazioneBytes, final Integer nextIdValidazione)
    {
		var schedaFascicoloDto = new SchedaValidazioneFascicoloDto();
    	schedaFascicoloDto.setCodiceFiscale(cuaa);
    	schedaFascicoloDto.setTipoDetenzione(TipoDetenzioneEnum.MANDATO);
    	schedaFascicoloDto.setReport(new ByteArrayResource(schedaValidazioneBytes));
    	schedaFascicoloDto.setNextIdValidazione(nextIdValidazione);
		eventBus.publishEvent(new StartValidazioneFascicoloEvent(schedaFascicoloDto));
    }
    
    @Transactional
    public void annullaIterValidazione (final String cuaa) {
    	anagraficaPrivateClient.annullaIterValidazione(cuaa);
    	controlliCompletezzaFascicoloService.rimozioneControlliCompletezza(cuaa);
    }
}