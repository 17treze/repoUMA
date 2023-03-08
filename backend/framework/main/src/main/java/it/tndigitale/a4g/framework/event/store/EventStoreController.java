package it.tndigitale.a4g.framework.event.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/framework/events")
public class EventStoreController {
    private EventStoreService eventStoreService;

    @Autowired
    public EventStoreController setEventStoreService(EventStoreService eventStoreService) {
        this.eventStoreService = eventStoreService;
        return this;
    }

    @GetMapping(path = "/{idEvent}/reprocess")
    public void reprocessEvent(@PathVariable(name = "idEvent") Long idEvent){
        eventStoreService.reprocessEvent(idEvent);
    }
}
