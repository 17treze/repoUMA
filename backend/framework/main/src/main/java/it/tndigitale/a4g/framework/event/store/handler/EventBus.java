package it.tndigitale.a4g.framework.event.store.handler;

import it.tndigitale.a4g.framework.event.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class EventBus {
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public EventBus setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
        return this;
    }

    public void publishEvent(Event event){
        applicationEventPublisher.publishEvent(event);
    }
}
