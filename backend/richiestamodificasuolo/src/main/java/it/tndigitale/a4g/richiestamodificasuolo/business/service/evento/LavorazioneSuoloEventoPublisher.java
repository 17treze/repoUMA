package it.tndigitale.a4g.richiestamodificasuolo.business.service.evento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class LavorazioneSuoloEventoPublisher {

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	
    public void notificaEvento(LavorazioneSuoloEvento evento){
        applicationEventPublisher.publishEvent(evento);
    }
}
